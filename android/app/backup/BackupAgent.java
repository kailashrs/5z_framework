package android.app.backup;

import android.app.IBackupAgent.Stub;
import android.app.QueuedWork;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.ApplicationInfo;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.ParcelFileDescriptor;
import android.os.Process;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import android.system.StructStat;
import android.util.ArraySet;
import android.util.Log;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import org.xmlpull.v1.XmlPullParserException;

public abstract class BackupAgent
  extends ContextWrapper
{
  private static final boolean DEBUG = false;
  public static final int FLAG_CLIENT_SIDE_ENCRYPTION_ENABLED = 1;
  public static final int FLAG_DEVICE_TO_DEVICE_TRANSFER = 2;
  public static final int FLAG_FAKE_CLIENT_SIDE_ENCRYPTION_ENABLED = Integer.MIN_VALUE;
  private static final String TAG = "BackupAgent";
  public static final int TYPE_DIRECTORY = 2;
  public static final int TYPE_EOF = 0;
  public static final int TYPE_FILE = 1;
  public static final int TYPE_SYMLINK = 3;
  private final IBinder mBinder = new BackupServiceBinder(null).asBinder();
  Handler mHandler = null;
  
  public BackupAgent()
  {
    super(null);
  }
  
  private void applyXmlFiltersAndDoFullBackupForDomain(String paramString1, String paramString2, Map<String, Set<FullBackup.BackupScheme.PathWithRequiredFlags>> paramMap, ArraySet<FullBackup.BackupScheme.PathWithRequiredFlags> paramArraySet, ArraySet<String> paramArraySet1, FullBackupDataOutput paramFullBackupDataOutput)
    throws IOException
  {
    if ((paramMap != null) && (paramMap.size() != 0))
    {
      if (paramMap.get(paramString2) != null)
      {
        Iterator localIterator = ((Set)paramMap.get(paramString2)).iterator();
        while (localIterator.hasNext())
        {
          paramMap = (FullBackup.BackupScheme.PathWithRequiredFlags)localIterator.next();
          if (areIncludeRequiredTransportFlagsSatisfied(paramMap.getRequiredFlags(), paramFullBackupDataOutput.getTransportFlags())) {
            fullBackupFileTree(paramString1, paramString2, paramMap.getPath(), paramArraySet, paramArraySet1, paramFullBackupDataOutput);
          }
        }
      }
    }
    else {
      fullBackupFileTree(paramString1, paramString2, FullBackup.getBackupScheme(this).tokenToDirectoryPath(paramString2), paramArraySet, paramArraySet1, paramFullBackupDataOutput);
    }
  }
  
  private boolean areIncludeRequiredTransportFlagsSatisfied(int paramInt1, int paramInt2)
  {
    boolean bool;
    if ((paramInt2 & paramInt1) == paramInt1) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private boolean isFileEligibleForRestore(File paramFile)
    throws IOException
  {
    Object localObject1 = FullBackup.getBackupScheme(this);
    if (!((FullBackup.BackupScheme)localObject1).isFullBackupContentEnabled())
    {
      if (Log.isLoggable("BackupXmlParserLogging", 2))
      {
        localObject2 = new StringBuilder();
        ((StringBuilder)localObject2).append("onRestoreFile \"");
        ((StringBuilder)localObject2).append(paramFile.getCanonicalPath());
        ((StringBuilder)localObject2).append("\" : fullBackupContent not enabled for ");
        ((StringBuilder)localObject2).append(getPackageName());
        Log.v("BackupXmlParserLogging", ((StringBuilder)localObject2).toString());
      }
      return false;
    }
    Object localObject2 = paramFile.getCanonicalPath();
    try
    {
      localObject3 = ((FullBackup.BackupScheme)localObject1).maybeParseAndGetCanonicalIncludePaths();
      localObject1 = ((FullBackup.BackupScheme)localObject1).maybeParseAndGetCanonicalExcludePaths();
      if ((localObject1 != null) && (isFileSpecifiedInPathList(paramFile, (Collection)localObject1)))
      {
        if (Log.isLoggable("BackupXmlParserLogging", 2))
        {
          paramFile = new StringBuilder();
          paramFile.append("onRestoreFile: \"");
          paramFile.append((String)localObject2);
          paramFile.append("\": listed in excludes; skipping.");
          Log.v("BackupXmlParserLogging", paramFile.toString());
        }
        return false;
      }
      if ((localObject3 != null) && (!((Map)localObject3).isEmpty()))
      {
        boolean bool1 = false;
        localObject3 = ((Map)localObject3).values().iterator();
        boolean bool2;
        for (;;)
        {
          bool2 = bool1;
          if (!((Iterator)localObject3).hasNext()) {
            break;
          }
          bool1 |= isFileSpecifiedInPathList(paramFile, (Set)((Iterator)localObject3).next());
          if (bool1)
          {
            bool2 = bool1;
            break;
          }
        }
        if (!bool2)
        {
          if (Log.isLoggable("BackupXmlParserLogging", 2))
          {
            paramFile = new StringBuilder();
            paramFile.append("onRestoreFile: Trying to restore \"");
            paramFile.append((String)localObject2);
            paramFile.append("\" but it isn't specified in the included files; skipping.");
            Log.v("BackupXmlParserLogging", paramFile.toString());
          }
          return false;
        }
      }
      return true;
    }
    catch (XmlPullParserException paramFile)
    {
      Object localObject3;
      if (Log.isLoggable("BackupXmlParserLogging", 2))
      {
        localObject3 = new StringBuilder();
        ((StringBuilder)localObject3).append("onRestoreFile \"");
        ((StringBuilder)localObject3).append((String)localObject2);
        ((StringBuilder)localObject3).append("\" : Exception trying to parse fullBackupContent xml file! Aborting onRestoreFile.");
        Log.v("BackupXmlParserLogging", ((StringBuilder)localObject3).toString(), paramFile);
      }
    }
    return false;
  }
  
  private boolean isFileSpecifiedInPathList(File paramFile, Collection<FullBackup.BackupScheme.PathWithRequiredFlags> paramCollection)
    throws IOException
  {
    Iterator localIterator = paramCollection.iterator();
    while (localIterator.hasNext())
    {
      paramCollection = ((FullBackup.BackupScheme.PathWithRequiredFlags)localIterator.next()).getPath();
      File localFile = new File(paramCollection);
      if (localFile.isDirectory())
      {
        if (paramFile.isDirectory()) {
          return paramFile.equals(localFile);
        }
        return paramFile.getCanonicalPath().startsWith(paramCollection);
      }
      if (paramFile.equals(localFile)) {
        return true;
      }
    }
    return false;
  }
  
  private boolean manifestExcludesContainFilePath(ArraySet<FullBackup.BackupScheme.PathWithRequiredFlags> paramArraySet, String paramString)
  {
    Iterator localIterator = paramArraySet.iterator();
    while (localIterator.hasNext())
    {
      paramArraySet = ((FullBackup.BackupScheme.PathWithRequiredFlags)localIterator.next()).getPath();
      if ((paramArraySet != null) && (paramArraySet.equals(paramString))) {
        return true;
      }
    }
    return false;
  }
  
  private void waitForSharedPrefs()
  {
    Handler localHandler = getHandler();
    SharedPrefsSynchronizer localSharedPrefsSynchronizer = new SharedPrefsSynchronizer();
    localHandler.postAtFrontOfQueue(localSharedPrefsSynchronizer);
    try
    {
      mLatch.await();
    }
    catch (InterruptedException localInterruptedException) {}
  }
  
  public void attach(Context paramContext)
  {
    attachBaseContext(paramContext);
  }
  
  /* Error */
  public final void fullBackupFile(File paramFile, FullBackupDataOutput paramFullBackupDataOutput)
  {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual 261	android/app/backup/BackupAgent:getApplicationInfo	()Landroid/content/pm/ApplicationInfo;
    //   4: astore_3
    //   5: aload_0
    //   6: invokevirtual 265	android/app/backup/BackupAgent:createCredentialProtectedStorageContext	()Landroid/content/Context;
    //   9: astore 4
    //   11: aload 4
    //   13: invokevirtual 271	android/content/Context:getDataDir	()Ljava/io/File;
    //   16: invokevirtual 157	java/io/File:getCanonicalPath	()Ljava/lang/String;
    //   19: astore 5
    //   21: aload 4
    //   23: invokevirtual 274	android/content/Context:getFilesDir	()Ljava/io/File;
    //   26: invokevirtual 157	java/io/File:getCanonicalPath	()Ljava/lang/String;
    //   29: astore 6
    //   31: aload 4
    //   33: invokevirtual 277	android/content/Context:getNoBackupFilesDir	()Ljava/io/File;
    //   36: invokevirtual 157	java/io/File:getCanonicalPath	()Ljava/lang/String;
    //   39: astore 7
    //   41: aload 4
    //   43: ldc_w 279
    //   46: invokevirtual 283	android/content/Context:getDatabasePath	(Ljava/lang/String;)Ljava/io/File;
    //   49: invokevirtual 286	java/io/File:getParentFile	()Ljava/io/File;
    //   52: invokevirtual 157	java/io/File:getCanonicalPath	()Ljava/lang/String;
    //   55: astore 8
    //   57: aload 4
    //   59: ldc_w 279
    //   62: invokevirtual 289	android/content/Context:getSharedPreferencesPath	(Ljava/lang/String;)Ljava/io/File;
    //   65: invokevirtual 286	java/io/File:getParentFile	()Ljava/io/File;
    //   68: invokevirtual 157	java/io/File:getCanonicalPath	()Ljava/lang/String;
    //   71: astore 9
    //   73: aload 4
    //   75: invokevirtual 292	android/content/Context:getCacheDir	()Ljava/io/File;
    //   78: invokevirtual 157	java/io/File:getCanonicalPath	()Ljava/lang/String;
    //   81: astore 10
    //   83: aload 4
    //   85: invokevirtual 295	android/content/Context:getCodeCacheDir	()Ljava/io/File;
    //   88: invokevirtual 157	java/io/File:getCanonicalPath	()Ljava/lang/String;
    //   91: astore 11
    //   93: aload_0
    //   94: invokevirtual 298	android/app/backup/BackupAgent:createDeviceProtectedStorageContext	()Landroid/content/Context;
    //   97: astore 4
    //   99: aload 4
    //   101: invokevirtual 271	android/content/Context:getDataDir	()Ljava/io/File;
    //   104: invokevirtual 157	java/io/File:getCanonicalPath	()Ljava/lang/String;
    //   107: astore 12
    //   109: aload 4
    //   111: invokevirtual 274	android/content/Context:getFilesDir	()Ljava/io/File;
    //   114: invokevirtual 157	java/io/File:getCanonicalPath	()Ljava/lang/String;
    //   117: astore 13
    //   119: aload 4
    //   121: invokevirtual 277	android/content/Context:getNoBackupFilesDir	()Ljava/io/File;
    //   124: invokevirtual 157	java/io/File:getCanonicalPath	()Ljava/lang/String;
    //   127: astore 14
    //   129: aload 4
    //   131: ldc_w 279
    //   134: invokevirtual 283	android/content/Context:getDatabasePath	(Ljava/lang/String;)Ljava/io/File;
    //   137: invokevirtual 286	java/io/File:getParentFile	()Ljava/io/File;
    //   140: invokevirtual 157	java/io/File:getCanonicalPath	()Ljava/lang/String;
    //   143: astore 15
    //   145: aload 4
    //   147: ldc_w 279
    //   150: invokevirtual 289	android/content/Context:getSharedPreferencesPath	(Ljava/lang/String;)Ljava/io/File;
    //   153: invokevirtual 286	java/io/File:getParentFile	()Ljava/io/File;
    //   156: invokevirtual 157	java/io/File:getCanonicalPath	()Ljava/lang/String;
    //   159: astore 16
    //   161: aload 4
    //   163: invokevirtual 292	android/content/Context:getCacheDir	()Ljava/io/File;
    //   166: invokevirtual 157	java/io/File:getCanonicalPath	()Ljava/lang/String;
    //   169: astore 17
    //   171: aload 4
    //   173: invokevirtual 295	android/content/Context:getCodeCacheDir	()Ljava/io/File;
    //   176: invokevirtual 157	java/io/File:getCanonicalPath	()Ljava/lang/String;
    //   179: astore 18
    //   181: aload_3
    //   182: getfield 303	android/content/pm/ApplicationInfo:nativeLibraryDir	Ljava/lang/String;
    //   185: ifnonnull +9 -> 194
    //   188: aconst_null
    //   189: astore 4
    //   191: goto +24 -> 215
    //   194: new 154	java/io/File
    //   197: astore 4
    //   199: aload 4
    //   201: aload_3
    //   202: getfield 303	android/content/pm/ApplicationInfo:nativeLibraryDir	Ljava/lang/String;
    //   205: invokespecial 207	java/io/File:<init>	(Ljava/lang/String;)V
    //   208: aload 4
    //   210: invokevirtual 157	java/io/File:getCanonicalPath	()Ljava/lang/String;
    //   213: astore 4
    //   215: invokestatic 308	android/os/Process:myUid	()I
    //   218: istore 19
    //   220: iload 19
    //   222: sipush 1000
    //   225: if_icmpeq +25 -> 250
    //   228: aload_0
    //   229: aconst_null
    //   230: invokevirtual 311	android/app/backup/BackupAgent:getExternalFilesDir	(Ljava/lang/String;)Ljava/io/File;
    //   233: astore_3
    //   234: aload_3
    //   235: ifnull +15 -> 250
    //   238: aload_3
    //   239: invokevirtual 157	java/io/File:getCanonicalPath	()Ljava/lang/String;
    //   242: astore_3
    //   243: goto +9 -> 252
    //   246: astore_1
    //   247: goto +349 -> 596
    //   250: aconst_null
    //   251: astore_3
    //   252: aload_1
    //   253: invokevirtual 157	java/io/File:getCanonicalPath	()Ljava/lang/String;
    //   256: astore 20
    //   258: aload 20
    //   260: aload 10
    //   262: invokevirtual 220	java/lang/String:startsWith	(Ljava/lang/String;)Z
    //   265: ifne +312 -> 577
    //   268: aload 20
    //   270: aload 11
    //   272: invokevirtual 220	java/lang/String:startsWith	(Ljava/lang/String;)Z
    //   275: ifne +302 -> 577
    //   278: aload 20
    //   280: aload 7
    //   282: invokevirtual 220	java/lang/String:startsWith	(Ljava/lang/String;)Z
    //   285: ifne +292 -> 577
    //   288: aload 20
    //   290: aload 17
    //   292: invokevirtual 220	java/lang/String:startsWith	(Ljava/lang/String;)Z
    //   295: ifne +282 -> 577
    //   298: aload 20
    //   300: aload 18
    //   302: invokevirtual 220	java/lang/String:startsWith	(Ljava/lang/String;)Z
    //   305: ifne +269 -> 574
    //   308: aload 20
    //   310: aload 14
    //   312: invokevirtual 220	java/lang/String:startsWith	(Ljava/lang/String;)Z
    //   315: ifne +259 -> 574
    //   318: aload 20
    //   320: aload 4
    //   322: invokevirtual 220	java/lang/String:startsWith	(Ljava/lang/String;)Z
    //   325: ifeq +6 -> 331
    //   328: goto +249 -> 577
    //   331: aload 20
    //   333: aload 8
    //   335: invokevirtual 220	java/lang/String:startsWith	(Ljava/lang/String;)Z
    //   338: ifeq +13 -> 351
    //   341: ldc_w 313
    //   344: astore_3
    //   345: aload 8
    //   347: astore_1
    //   348: goto +169 -> 517
    //   351: aload 20
    //   353: aload 9
    //   355: invokevirtual 220	java/lang/String:startsWith	(Ljava/lang/String;)Z
    //   358: ifeq +13 -> 371
    //   361: ldc_w 315
    //   364: astore_3
    //   365: aload 9
    //   367: astore_1
    //   368: goto -20 -> 348
    //   371: aload 20
    //   373: aload 6
    //   375: invokevirtual 220	java/lang/String:startsWith	(Ljava/lang/String;)Z
    //   378: ifeq +13 -> 391
    //   381: ldc_w 317
    //   384: astore_3
    //   385: aload 6
    //   387: astore_1
    //   388: goto -40 -> 348
    //   391: aload 20
    //   393: aload 5
    //   395: invokevirtual 220	java/lang/String:startsWith	(Ljava/lang/String;)Z
    //   398: ifeq +13 -> 411
    //   401: ldc_w 319
    //   404: astore_3
    //   405: aload 5
    //   407: astore_1
    //   408: goto -60 -> 348
    //   411: aload 20
    //   413: aload 15
    //   415: invokevirtual 220	java/lang/String:startsWith	(Ljava/lang/String;)Z
    //   418: ifeq +13 -> 431
    //   421: ldc_w 321
    //   424: astore_3
    //   425: aload 15
    //   427: astore_1
    //   428: goto -80 -> 348
    //   431: aload 20
    //   433: aload 16
    //   435: invokevirtual 220	java/lang/String:startsWith	(Ljava/lang/String;)Z
    //   438: ifeq +13 -> 451
    //   441: ldc_w 323
    //   444: astore_3
    //   445: aload 16
    //   447: astore_1
    //   448: goto -100 -> 348
    //   451: aload 20
    //   453: aload 13
    //   455: invokevirtual 220	java/lang/String:startsWith	(Ljava/lang/String;)Z
    //   458: ifeq +13 -> 471
    //   461: ldc_w 325
    //   464: astore_3
    //   465: aload 13
    //   467: astore_1
    //   468: goto -120 -> 348
    //   471: aload 20
    //   473: aload 12
    //   475: invokevirtual 220	java/lang/String:startsWith	(Ljava/lang/String;)Z
    //   478: ifeq +13 -> 491
    //   481: ldc_w 327
    //   484: astore_3
    //   485: aload 12
    //   487: astore_1
    //   488: goto -140 -> 348
    //   491: aload_3
    //   492: ifnull +40 -> 532
    //   495: aload 20
    //   497: aload_3
    //   498: invokevirtual 220	java/lang/String:startsWith	(Ljava/lang/String;)Z
    //   501: ifeq +31 -> 532
    //   504: ldc_w 329
    //   507: astore 4
    //   509: aload_3
    //   510: astore_1
    //   511: aload 4
    //   513: astore_3
    //   514: goto -166 -> 348
    //   517: aload_0
    //   518: invokevirtual 162	android/app/backup/BackupAgent:getPackageName	()Ljava/lang/String;
    //   521: aload_3
    //   522: aconst_null
    //   523: aload_1
    //   524: aload 20
    //   526: aload_2
    //   527: invokestatic 333	android/app/backup/FullBackup:backupToTar	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Landroid/app/backup/FullBackupDataOutput;)I
    //   530: pop
    //   531: return
    //   532: new 144	java/lang/StringBuilder
    //   535: dup
    //   536: invokespecial 146	java/lang/StringBuilder:<init>	()V
    //   539: astore_1
    //   540: aload_1
    //   541: ldc_w 335
    //   544: invokevirtual 152	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   547: pop
    //   548: aload_1
    //   549: aload 20
    //   551: invokevirtual 152	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   554: pop
    //   555: aload_1
    //   556: ldc_w 337
    //   559: invokevirtual 152	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   562: pop
    //   563: ldc 29
    //   565: aload_1
    //   566: invokevirtual 165	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   569: invokestatic 340	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   572: pop
    //   573: return
    //   574: goto +3 -> 577
    //   577: ldc 29
    //   579: ldc_w 342
    //   582: invokestatic 340	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   585: pop
    //   586: return
    //   587: astore_1
    //   588: goto +8 -> 596
    //   591: astore_1
    //   592: goto +4 -> 596
    //   595: astore_1
    //   596: ldc 29
    //   598: ldc_w 344
    //   601: invokestatic 340	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   604: pop
    //   605: return
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	606	0	this	BackupAgent
    //   0	606	1	paramFile	File
    //   0	606	2	paramFullBackupDataOutput	FullBackupDataOutput
    //   4	518	3	localObject1	Object
    //   9	503	4	localObject2	Object
    //   19	387	5	str1	String
    //   29	357	6	str2	String
    //   39	242	7	str3	String
    //   55	291	8	str4	String
    //   71	295	9	str5	String
    //   81	180	10	str6	String
    //   91	180	11	str7	String
    //   107	379	12	str8	String
    //   117	349	13	str9	String
    //   127	184	14	str10	String
    //   143	283	15	str11	String
    //   159	287	16	str12	String
    //   169	122	17	str13	String
    //   179	122	18	str14	String
    //   218	8	19	i	int
    //   256	294	20	str15	String
    // Exception table:
    //   from	to	target	type
    //   228	234	246	java/io/IOException
    //   238	243	246	java/io/IOException
    //   252	258	587	java/io/IOException
    //   171	188	591	java/io/IOException
    //   194	215	591	java/io/IOException
    //   215	220	591	java/io/IOException
    //   5	171	595	java/io/IOException
  }
  
  protected final void fullBackupFileTree(String paramString1, String paramString2, String paramString3, ArraySet<FullBackup.BackupScheme.PathWithRequiredFlags> paramArraySet, ArraySet<String> paramArraySet1, FullBackupDataOutput paramFullBackupDataOutput)
  {
    String str = FullBackup.getBackupScheme(this).tokenToDirectoryPath(paramString2);
    if (str == null) {
      return;
    }
    paramString3 = new File(paramString3);
    if (paramString3.exists())
    {
      LinkedList localLinkedList = new LinkedList();
      localLinkedList.add(paramString3);
      while (localLinkedList.size() > 0)
      {
        File localFile = (File)localLinkedList.remove(0);
        try
        {
          localObject = Os.lstat(localFile.getPath());
          if ((OsConstants.S_ISREG(st_mode)) || (OsConstants.S_ISDIR(st_mode)))
          {
            paramString3 = localFile.getCanonicalPath();
            if (paramArraySet != null)
            {
              try
              {
                if (!manifestExcludesContainFilePath(paramArraySet, paramString3)) {}
              }
              catch (ErrnoException paramString3)
              {
                break label222;
              }
              catch (IOException paramString3)
              {
                break label288;
              }
            }
            else if ((paramArraySet1 == null) || (!paramArraySet1.contains(paramString3)))
            {
              if (OsConstants.S_ISDIR(st_mode))
              {
                localObject = localFile.listFiles();
                if (localObject != null)
                {
                  int i = localObject.length;
                  for (int j = 0; j < i; j++) {
                    localLinkedList.add(0, localObject[j]);
                  }
                }
              }
              FullBackup.backupToTar(paramString1, paramString2, null, str, paramString3, paramFullBackupDataOutput);
            }
          }
        }
        catch (ErrnoException paramString3)
        {
          Object localObject;
          if (Log.isLoggable("BackupXmlParserLogging", 2))
          {
            localObject = new StringBuilder();
            ((StringBuilder)localObject).append("Error scanning file ");
            ((StringBuilder)localObject).append(localFile);
            ((StringBuilder)localObject).append(" : ");
            ((StringBuilder)localObject).append(paramString3);
            Log.v("BackupXmlParserLogging", ((StringBuilder)localObject).toString());
          }
        }
        catch (IOException paramString3)
        {
          label222:
          label288:
          if (Log.isLoggable("BackupXmlParserLogging", 2))
          {
            paramString3 = new StringBuilder();
            paramString3.append("Error canonicalizing path of ");
            paramString3.append(localFile);
            Log.v("BackupXmlParserLogging", paramString3.toString());
          }
        }
      }
    }
  }
  
  Handler getHandler()
  {
    if (mHandler == null) {
      mHandler = new Handler(Looper.getMainLooper());
    }
    return mHandler;
  }
  
  public abstract void onBackup(ParcelFileDescriptor paramParcelFileDescriptor1, BackupDataOutput paramBackupDataOutput, ParcelFileDescriptor paramParcelFileDescriptor2)
    throws IOException;
  
  public final IBinder onBind()
  {
    return mBinder;
  }
  
  public void onCreate() {}
  
  public void onDestroy() {}
  
  public void onFullBackup(FullBackupDataOutput paramFullBackupDataOutput)
    throws IOException
  {
    Object localObject1 = FullBackup.getBackupScheme(this);
    if (!((FullBackup.BackupScheme)localObject1).isFullBackupContentEnabled()) {
      return;
    }
    try
    {
      Map localMap = ((FullBackup.BackupScheme)localObject1).maybeParseAndGetCanonicalIncludePaths();
      ArraySet localArraySet1 = ((FullBackup.BackupScheme)localObject1).maybeParseAndGetCanonicalExcludePaths();
      String str1 = getPackageName();
      localObject1 = getApplicationInfo();
      Object localObject2 = createCredentialProtectedStorageContext();
      String str2 = ((Context)localObject2).getDataDir().getCanonicalPath();
      String str3 = ((Context)localObject2).getFilesDir().getCanonicalPath();
      String str4 = ((Context)localObject2).getNoBackupFilesDir().getCanonicalPath();
      String str5 = ((Context)localObject2).getDatabasePath("foo").getParentFile().getCanonicalPath();
      String str6 = ((Context)localObject2).getSharedPreferencesPath("foo").getParentFile().getCanonicalPath();
      String str7 = ((Context)localObject2).getCacheDir().getCanonicalPath();
      String str8 = ((Context)localObject2).getCodeCacheDir().getCanonicalPath();
      Object localObject3 = createDeviceProtectedStorageContext();
      String str9 = ((Context)localObject3).getDataDir().getCanonicalPath();
      String str10 = ((Context)localObject3).getFilesDir().getCanonicalPath();
      String str11 = ((Context)localObject3).getNoBackupFilesDir().getCanonicalPath();
      String str12 = ((Context)localObject3).getDatabasePath("foo").getParentFile().getCanonicalPath();
      localObject2 = ((Context)localObject3).getSharedPreferencesPath("foo").getParentFile().getCanonicalPath();
      String str13 = ((Context)localObject3).getCacheDir().getCanonicalPath();
      localObject3 = ((Context)localObject3).getCodeCacheDir().getCanonicalPath();
      if (nativeLibraryDir != null) {
        localObject1 = new File(nativeLibraryDir).getCanonicalPath();
      } else {
        localObject1 = null;
      }
      ArraySet localArraySet2 = new ArraySet();
      localArraySet2.add(str3);
      localArraySet2.add(str4);
      localArraySet2.add(str5);
      localArraySet2.add(str6);
      localArraySet2.add(str7);
      localArraySet2.add(str8);
      localArraySet2.add(str10);
      localArraySet2.add(str11);
      localArraySet2.add(str12);
      localArraySet2.add(localObject2);
      localArraySet2.add(str13);
      localArraySet2.add(localObject3);
      if (localObject1 != null) {
        localArraySet2.add(localObject1);
      }
      applyXmlFiltersAndDoFullBackupForDomain(str1, "r", localMap, localArraySet1, localArraySet2, paramFullBackupDataOutput);
      localArraySet2.add(str2);
      applyXmlFiltersAndDoFullBackupForDomain(str1, "d_r", localMap, localArraySet1, localArraySet2, paramFullBackupDataOutput);
      localArraySet2.add(str9);
      localArraySet2.remove(str3);
      applyXmlFiltersAndDoFullBackupForDomain(str1, "f", localMap, localArraySet1, localArraySet2, paramFullBackupDataOutput);
      localArraySet2.add(str3);
      localArraySet2.remove(str10);
      applyXmlFiltersAndDoFullBackupForDomain(str1, "d_f", localMap, localArraySet1, localArraySet2, paramFullBackupDataOutput);
      localArraySet2.add(str10);
      localArraySet2.remove(str5);
      applyXmlFiltersAndDoFullBackupForDomain(str1, "db", localMap, localArraySet1, localArraySet2, paramFullBackupDataOutput);
      localArraySet2.add(str5);
      localArraySet2.remove(str12);
      applyXmlFiltersAndDoFullBackupForDomain(str1, "d_db", localMap, localArraySet1, localArraySet2, paramFullBackupDataOutput);
      localArraySet2.add(str12);
      localArraySet2.remove(str6);
      applyXmlFiltersAndDoFullBackupForDomain(str1, "sp", localMap, localArraySet1, localArraySet2, paramFullBackupDataOutput);
      localArraySet2.add(str6);
      localArraySet2.remove(localObject2);
      applyXmlFiltersAndDoFullBackupForDomain(str1, "d_sp", localMap, localArraySet1, localArraySet2, paramFullBackupDataOutput);
      localArraySet2.add(localObject2);
      if ((Process.myUid() != 1000) && (getExternalFilesDir(null) != null)) {
        applyXmlFiltersAndDoFullBackupForDomain(str1, "ef", localMap, localArraySet1, localArraySet2, paramFullBackupDataOutput);
      }
      return;
    }
    catch (IOException|XmlPullParserException paramFullBackupDataOutput)
    {
      if (Log.isLoggable("BackupXmlParserLogging", 2)) {
        Log.v("BackupXmlParserLogging", "Exception trying to parse fullBackupContent xml file! Aborting full backup.", paramFullBackupDataOutput);
      }
    }
  }
  
  public void onQuotaExceeded(long paramLong1, long paramLong2) {}
  
  public abstract void onRestore(BackupDataInput paramBackupDataInput, int paramInt, ParcelFileDescriptor paramParcelFileDescriptor)
    throws IOException;
  
  public void onRestore(BackupDataInput paramBackupDataInput, long paramLong, ParcelFileDescriptor paramParcelFileDescriptor)
    throws IOException
  {
    onRestore(paramBackupDataInput, (int)paramLong, paramParcelFileDescriptor);
  }
  
  protected void onRestoreFile(ParcelFileDescriptor paramParcelFileDescriptor, long paramLong1, int paramInt, String paramString1, String paramString2, long paramLong2, long paramLong3)
    throws IOException
  {
    String str = FullBackup.getBackupScheme(this).tokenToDirectoryPath(paramString1);
    if (paramString1.equals("ef")) {
      paramLong2 = -1L;
    }
    if (str != null)
    {
      paramString2 = new File(str, paramString2);
      paramString1 = paramString2.getCanonicalPath();
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(str);
      localStringBuilder.append(File.separatorChar);
      if (paramString1.startsWith(localStringBuilder.toString()))
      {
        onRestoreFile(paramParcelFileDescriptor, paramLong1, paramString2, paramInt, paramLong2, paramLong3);
        return;
      }
    }
    FullBackup.restoreFile(paramParcelFileDescriptor, paramLong1, paramInt, paramLong2, paramLong3, null);
  }
  
  public void onRestoreFile(ParcelFileDescriptor paramParcelFileDescriptor, long paramLong1, File paramFile, int paramInt, long paramLong2, long paramLong3)
    throws IOException
  {
    if (!isFileEligibleForRestore(paramFile)) {
      paramFile = null;
    }
    FullBackup.restoreFile(paramParcelFileDescriptor, paramLong1, paramInt, paramLong2, paramLong3, paramFile);
  }
  
  public void onRestoreFinished() {}
  
  private class BackupServiceBinder
    extends IBackupAgent.Stub
  {
    private static final String TAG = "BackupServiceBinder";
    
    private BackupServiceBinder() {}
    
    /* Error */
    public void doBackup(ParcelFileDescriptor paramParcelFileDescriptor1, ParcelFileDescriptor paramParcelFileDescriptor2, ParcelFileDescriptor paramParcelFileDescriptor3, long paramLong, int paramInt1, IBackupManager paramIBackupManager, int paramInt2)
      throws android.os.RemoteException
    {
      // Byte code:
      //   0: invokestatic 37	android/os/Binder:clearCallingIdentity	()J
      //   3: lstore 9
      //   5: new 39	android/app/backup/BackupDataOutput
      //   8: dup
      //   9: aload_2
      //   10: invokevirtual 45	android/os/ParcelFileDescriptor:getFileDescriptor	()Ljava/io/FileDescriptor;
      //   13: lload 4
      //   15: iload 8
      //   17: invokespecial 48	android/app/backup/BackupDataOutput:<init>	(Ljava/io/FileDescriptor;JI)V
      //   20: astore 11
      //   22: aload_0
      //   23: getfield 16	android/app/backup/BackupAgent$BackupServiceBinder:this$0	Landroid/app/backup/BackupAgent;
      //   26: astore 12
      //   28: aload 12
      //   30: aload_1
      //   31: aload 11
      //   33: aload_3
      //   34: invokevirtual 52	android/app/backup/BackupAgent:onBackup	(Landroid/os/ParcelFileDescriptor;Landroid/app/backup/BackupDataOutput;Landroid/os/ParcelFileDescriptor;)V
      //   37: aload_0
      //   38: getfield 16	android/app/backup/BackupAgent$BackupServiceBinder:this$0	Landroid/app/backup/BackupAgent;
      //   41: invokestatic 55	android/app/backup/BackupAgent:access$100	(Landroid/app/backup/BackupAgent;)V
      //   44: lload 9
      //   46: invokestatic 59	android/os/Binder:restoreCallingIdentity	(J)V
      //   49: aload 7
      //   51: iload 6
      //   53: lconst_0
      //   54: invokeinterface 65 4 0
      //   59: goto +5 -> 64
      //   62: astore 7
      //   64: invokestatic 69	android/os/Binder:getCallingPid	()I
      //   67: invokestatic 74	android/os/Process:myPid	()I
      //   70: if_icmpeq +15 -> 85
      //   73: aload_1
      //   74: invokestatic 80	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
      //   77: aload_2
      //   78: invokestatic 80	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
      //   81: aload_3
      //   82: invokestatic 80	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
      //   85: return
      //   86: astore 11
      //   88: goto +15 -> 103
      //   91: astore 11
      //   93: goto +15 -> 108
      //   96: astore 11
      //   98: goto +70 -> 168
      //   101: astore 11
      //   103: goto +135 -> 238
      //   106: astore 11
      //   108: new 82	java/lang/StringBuilder
      //   111: astore 12
      //   113: aload 12
      //   115: invokespecial 83	java/lang/StringBuilder:<init>	()V
      //   118: aload 12
      //   120: ldc 85
      //   122: invokevirtual 89	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   125: pop
      //   126: aload 12
      //   128: aload_0
      //   129: getfield 16	android/app/backup/BackupAgent$BackupServiceBinder:this$0	Landroid/app/backup/BackupAgent;
      //   132: invokevirtual 95	java/lang/Object:getClass	()Ljava/lang/Class;
      //   135: invokevirtual 101	java/lang/Class:getName	()Ljava/lang/String;
      //   138: invokevirtual 89	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   141: pop
      //   142: aload 12
      //   144: ldc 103
      //   146: invokevirtual 89	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   149: pop
      //   150: ldc 10
      //   152: aload 12
      //   154: invokevirtual 106	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   157: aload 11
      //   159: invokestatic 112	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
      //   162: pop
      //   163: aload 11
      //   165: athrow
      //   166: astore 11
      //   168: new 82	java/lang/StringBuilder
      //   171: astore 12
      //   173: aload 12
      //   175: invokespecial 83	java/lang/StringBuilder:<init>	()V
      //   178: aload 12
      //   180: ldc 85
      //   182: invokevirtual 89	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   185: pop
      //   186: aload 12
      //   188: aload_0
      //   189: getfield 16	android/app/backup/BackupAgent$BackupServiceBinder:this$0	Landroid/app/backup/BackupAgent;
      //   192: invokevirtual 95	java/lang/Object:getClass	()Ljava/lang/Class;
      //   195: invokevirtual 101	java/lang/Class:getName	()Ljava/lang/String;
      //   198: invokevirtual 89	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   201: pop
      //   202: aload 12
      //   204: ldc 103
      //   206: invokevirtual 89	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   209: pop
      //   210: ldc 10
      //   212: aload 12
      //   214: invokevirtual 106	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   217: aload 11
      //   219: invokestatic 112	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
      //   222: pop
      //   223: new 31	java/lang/RuntimeException
      //   226: astore 12
      //   228: aload 12
      //   230: aload 11
      //   232: invokespecial 115	java/lang/RuntimeException:<init>	(Ljava/lang/Throwable;)V
      //   235: aload 12
      //   237: athrow
      //   238: aload_0
      //   239: getfield 16	android/app/backup/BackupAgent$BackupServiceBinder:this$0	Landroid/app/backup/BackupAgent;
      //   242: invokestatic 55	android/app/backup/BackupAgent:access$100	(Landroid/app/backup/BackupAgent;)V
      //   245: lload 9
      //   247: invokestatic 59	android/os/Binder:restoreCallingIdentity	(J)V
      //   250: aload 7
      //   252: iload 6
      //   254: lconst_0
      //   255: invokeinterface 65 4 0
      //   260: goto +5 -> 265
      //   263: astore 7
      //   265: invokestatic 69	android/os/Binder:getCallingPid	()I
      //   268: invokestatic 74	android/os/Process:myPid	()I
      //   271: if_icmpeq +15 -> 286
      //   274: aload_1
      //   275: invokestatic 80	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
      //   278: aload_2
      //   279: invokestatic 80	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
      //   282: aload_3
      //   283: invokestatic 80	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
      //   286: aload 11
      //   288: athrow
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	289	0	this	BackupServiceBinder
      //   0	289	1	paramParcelFileDescriptor1	ParcelFileDescriptor
      //   0	289	2	paramParcelFileDescriptor2	ParcelFileDescriptor
      //   0	289	3	paramParcelFileDescriptor3	ParcelFileDescriptor
      //   0	289	4	paramLong	long
      //   0	289	6	paramInt1	int
      //   0	289	7	paramIBackupManager	IBackupManager
      //   0	289	8	paramInt2	int
      //   3	243	9	l	long
      //   20	12	11	localBackupDataOutput	BackupDataOutput
      //   86	1	11	localObject1	Object
      //   91	1	11	localRuntimeException1	RuntimeException
      //   96	1	11	localIOException1	IOException
      //   101	1	11	localObject2	Object
      //   106	58	11	localRuntimeException2	RuntimeException
      //   166	121	11	localIOException2	IOException
      //   26	210	12	localObject3	Object
      // Exception table:
      //   from	to	target	type
      //   49	59	62	android/os/RemoteException
      //   28	37	86	finally
      //   108	166	86	finally
      //   168	238	86	finally
      //   28	37	91	java/lang/RuntimeException
      //   28	37	96	java/io/IOException
      //   22	28	101	finally
      //   22	28	106	java/lang/RuntimeException
      //   22	28	166	java/io/IOException
      //   250	260	263	android/os/RemoteException
    }
    
    /* Error */
    public void doFullBackup(ParcelFileDescriptor paramParcelFileDescriptor, long paramLong, int paramInt1, IBackupManager paramIBackupManager, int paramInt2)
    {
      // Byte code:
      //   0: invokestatic 37	android/os/Binder:clearCallingIdentity	()J
      //   3: lstore 7
      //   5: aload_0
      //   6: getfield 16	android/app/backup/BackupAgent$BackupServiceBinder:this$0	Landroid/app/backup/BackupAgent;
      //   9: invokestatic 55	android/app/backup/BackupAgent:access$100	(Landroid/app/backup/BackupAgent;)V
      //   12: aload_0
      //   13: getfield 16	android/app/backup/BackupAgent$BackupServiceBinder:this$0	Landroid/app/backup/BackupAgent;
      //   16: astore 9
      //   18: new 120	android/app/backup/FullBackupDataOutput
      //   21: astore 10
      //   23: aload 10
      //   25: aload_1
      //   26: lload_2
      //   27: iload 6
      //   29: invokespecial 123	android/app/backup/FullBackupDataOutput:<init>	(Landroid/os/ParcelFileDescriptor;JI)V
      //   32: aload 9
      //   34: aload 10
      //   36: invokevirtual 127	android/app/backup/BackupAgent:onFullBackup	(Landroid/app/backup/FullBackupDataOutput;)V
      //   39: aload_0
      //   40: getfield 16	android/app/backup/BackupAgent$BackupServiceBinder:this$0	Landroid/app/backup/BackupAgent;
      //   43: invokestatic 55	android/app/backup/BackupAgent:access$100	(Landroid/app/backup/BackupAgent;)V
      //   46: new 129	java/io/FileOutputStream
      //   49: astore 9
      //   51: aload 9
      //   53: aload_1
      //   54: invokevirtual 45	android/os/ParcelFileDescriptor:getFileDescriptor	()Ljava/io/FileDescriptor;
      //   57: invokespecial 132	java/io/FileOutputStream:<init>	(Ljava/io/FileDescriptor;)V
      //   60: aload 9
      //   62: iconst_4
      //   63: newarray byte
      //   65: invokevirtual 136	java/io/FileOutputStream:write	([B)V
      //   68: goto +13 -> 81
      //   71: astore 9
      //   73: ldc 10
      //   75: ldc -118
      //   77: invokestatic 142	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
      //   80: pop
      //   81: lload 7
      //   83: invokestatic 59	android/os/Binder:restoreCallingIdentity	(J)V
      //   86: aload 5
      //   88: iload 4
      //   90: lconst_0
      //   91: invokeinterface 65 4 0
      //   96: goto +5 -> 101
      //   99: astore 5
      //   101: invokestatic 69	android/os/Binder:getCallingPid	()I
      //   104: invokestatic 74	android/os/Process:myPid	()I
      //   107: if_icmpeq +7 -> 114
      //   110: aload_1
      //   111: invokestatic 80	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
      //   114: return
      //   115: astore 9
      //   117: goto +135 -> 252
      //   120: astore 9
      //   122: new 82	java/lang/StringBuilder
      //   125: astore 10
      //   127: aload 10
      //   129: invokespecial 83	java/lang/StringBuilder:<init>	()V
      //   132: aload 10
      //   134: ldc -112
      //   136: invokevirtual 89	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   139: pop
      //   140: aload 10
      //   142: aload_0
      //   143: getfield 16	android/app/backup/BackupAgent$BackupServiceBinder:this$0	Landroid/app/backup/BackupAgent;
      //   146: invokevirtual 95	java/lang/Object:getClass	()Ljava/lang/Class;
      //   149: invokevirtual 101	java/lang/Class:getName	()Ljava/lang/String;
      //   152: invokevirtual 89	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   155: pop
      //   156: aload 10
      //   158: ldc 103
      //   160: invokevirtual 89	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   163: pop
      //   164: ldc 10
      //   166: aload 10
      //   168: invokevirtual 106	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   171: aload 9
      //   173: invokestatic 112	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
      //   176: pop
      //   177: aload 9
      //   179: athrow
      //   180: astore 9
      //   182: new 82	java/lang/StringBuilder
      //   185: astore 10
      //   187: aload 10
      //   189: invokespecial 83	java/lang/StringBuilder:<init>	()V
      //   192: aload 10
      //   194: ldc -112
      //   196: invokevirtual 89	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   199: pop
      //   200: aload 10
      //   202: aload_0
      //   203: getfield 16	android/app/backup/BackupAgent$BackupServiceBinder:this$0	Landroid/app/backup/BackupAgent;
      //   206: invokevirtual 95	java/lang/Object:getClass	()Ljava/lang/Class;
      //   209: invokevirtual 101	java/lang/Class:getName	()Ljava/lang/String;
      //   212: invokevirtual 89	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   215: pop
      //   216: aload 10
      //   218: ldc 103
      //   220: invokevirtual 89	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   223: pop
      //   224: ldc 10
      //   226: aload 10
      //   228: invokevirtual 106	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   231: aload 9
      //   233: invokestatic 112	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
      //   236: pop
      //   237: new 31	java/lang/RuntimeException
      //   240: astore 10
      //   242: aload 10
      //   244: aload 9
      //   246: invokespecial 115	java/lang/RuntimeException:<init>	(Ljava/lang/Throwable;)V
      //   249: aload 10
      //   251: athrow
      //   252: aload_0
      //   253: getfield 16	android/app/backup/BackupAgent$BackupServiceBinder:this$0	Landroid/app/backup/BackupAgent;
      //   256: invokestatic 55	android/app/backup/BackupAgent:access$100	(Landroid/app/backup/BackupAgent;)V
      //   259: new 129	java/io/FileOutputStream
      //   262: astore 10
      //   264: aload 10
      //   266: aload_1
      //   267: invokevirtual 45	android/os/ParcelFileDescriptor:getFileDescriptor	()Ljava/io/FileDescriptor;
      //   270: invokespecial 132	java/io/FileOutputStream:<init>	(Ljava/io/FileDescriptor;)V
      //   273: aload 10
      //   275: iconst_4
      //   276: newarray byte
      //   278: invokevirtual 136	java/io/FileOutputStream:write	([B)V
      //   281: goto +13 -> 294
      //   284: astore 10
      //   286: ldc 10
      //   288: ldc -118
      //   290: invokestatic 142	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
      //   293: pop
      //   294: lload 7
      //   296: invokestatic 59	android/os/Binder:restoreCallingIdentity	(J)V
      //   299: aload 5
      //   301: iload 4
      //   303: lconst_0
      //   304: invokeinterface 65 4 0
      //   309: goto +5 -> 314
      //   312: astore 5
      //   314: invokestatic 69	android/os/Binder:getCallingPid	()I
      //   317: invokestatic 74	android/os/Process:myPid	()I
      //   320: if_icmpeq +7 -> 327
      //   323: aload_1
      //   324: invokestatic 80	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
      //   327: aload 9
      //   329: athrow
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	330	0	this	BackupServiceBinder
      //   0	330	1	paramParcelFileDescriptor	ParcelFileDescriptor
      //   0	330	2	paramLong	long
      //   0	330	4	paramInt1	int
      //   0	330	5	paramIBackupManager	IBackupManager
      //   0	330	6	paramInt2	int
      //   3	292	7	l	long
      //   16	45	9	localObject1	Object
      //   71	1	9	localIOException1	IOException
      //   115	1	9	localObject2	Object
      //   120	58	9	localRuntimeException	RuntimeException
      //   180	148	9	localIOException2	IOException
      //   21	253	10	localObject3	Object
      //   284	1	10	localIOException3	IOException
      // Exception table:
      //   from	to	target	type
      //   46	68	71	java/io/IOException
      //   86	96	99	android/os/RemoteException
      //   12	39	115	finally
      //   122	180	115	finally
      //   182	252	115	finally
      //   12	39	120	java/lang/RuntimeException
      //   12	39	180	java/io/IOException
      //   259	281	284	java/io/IOException
      //   299	309	312	android/os/RemoteException
    }
    
    /* Error */
    public void doMeasureFullBackup(long paramLong, int paramInt1, IBackupManager paramIBackupManager, int paramInt2)
    {
      // Byte code:
      //   0: invokestatic 37	android/os/Binder:clearCallingIdentity	()J
      //   3: lstore 6
      //   5: new 120	android/app/backup/FullBackupDataOutput
      //   8: dup
      //   9: lload_1
      //   10: iload 5
      //   12: invokespecial 149	android/app/backup/FullBackupDataOutput:<init>	(JI)V
      //   15: astore 8
      //   17: aload_0
      //   18: getfield 16	android/app/backup/BackupAgent$BackupServiceBinder:this$0	Landroid/app/backup/BackupAgent;
      //   21: invokestatic 55	android/app/backup/BackupAgent:access$100	(Landroid/app/backup/BackupAgent;)V
      //   24: aload_0
      //   25: getfield 16	android/app/backup/BackupAgent$BackupServiceBinder:this$0	Landroid/app/backup/BackupAgent;
      //   28: aload 8
      //   30: invokevirtual 127	android/app/backup/BackupAgent:onFullBackup	(Landroid/app/backup/FullBackupDataOutput;)V
      //   33: lload 6
      //   35: invokestatic 59	android/os/Binder:restoreCallingIdentity	(J)V
      //   38: aload 4
      //   40: iload_3
      //   41: aload 8
      //   43: invokevirtual 152	android/app/backup/FullBackupDataOutput:getSize	()J
      //   46: invokeinterface 65 4 0
      //   51: goto +5 -> 56
      //   54: astore 4
      //   56: return
      //   57: astore 9
      //   59: goto +135 -> 194
      //   62: astore 9
      //   64: new 82	java/lang/StringBuilder
      //   67: astore 10
      //   69: aload 10
      //   71: invokespecial 83	java/lang/StringBuilder:<init>	()V
      //   74: aload 10
      //   76: ldc -102
      //   78: invokevirtual 89	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   81: pop
      //   82: aload 10
      //   84: aload_0
      //   85: getfield 16	android/app/backup/BackupAgent$BackupServiceBinder:this$0	Landroid/app/backup/BackupAgent;
      //   88: invokevirtual 95	java/lang/Object:getClass	()Ljava/lang/Class;
      //   91: invokevirtual 101	java/lang/Class:getName	()Ljava/lang/String;
      //   94: invokevirtual 89	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   97: pop
      //   98: aload 10
      //   100: ldc 103
      //   102: invokevirtual 89	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   105: pop
      //   106: ldc 10
      //   108: aload 10
      //   110: invokevirtual 106	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   113: aload 9
      //   115: invokestatic 112	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
      //   118: pop
      //   119: aload 9
      //   121: athrow
      //   122: astore 9
      //   124: new 82	java/lang/StringBuilder
      //   127: astore 10
      //   129: aload 10
      //   131: invokespecial 83	java/lang/StringBuilder:<init>	()V
      //   134: aload 10
      //   136: ldc -102
      //   138: invokevirtual 89	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   141: pop
      //   142: aload 10
      //   144: aload_0
      //   145: getfield 16	android/app/backup/BackupAgent$BackupServiceBinder:this$0	Landroid/app/backup/BackupAgent;
      //   148: invokevirtual 95	java/lang/Object:getClass	()Ljava/lang/Class;
      //   151: invokevirtual 101	java/lang/Class:getName	()Ljava/lang/String;
      //   154: invokevirtual 89	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   157: pop
      //   158: aload 10
      //   160: ldc 103
      //   162: invokevirtual 89	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   165: pop
      //   166: ldc 10
      //   168: aload 10
      //   170: invokevirtual 106	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   173: aload 9
      //   175: invokestatic 112	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
      //   178: pop
      //   179: new 31	java/lang/RuntimeException
      //   182: astore 10
      //   184: aload 10
      //   186: aload 9
      //   188: invokespecial 115	java/lang/RuntimeException:<init>	(Ljava/lang/Throwable;)V
      //   191: aload 10
      //   193: athrow
      //   194: lload 6
      //   196: invokestatic 59	android/os/Binder:restoreCallingIdentity	(J)V
      //   199: aload 4
      //   201: iload_3
      //   202: aload 8
      //   204: invokevirtual 152	android/app/backup/FullBackupDataOutput:getSize	()J
      //   207: invokeinterface 65 4 0
      //   212: goto +5 -> 217
      //   215: astore 4
      //   217: aload 9
      //   219: athrow
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	220	0	this	BackupServiceBinder
      //   0	220	1	paramLong	long
      //   0	220	3	paramInt1	int
      //   0	220	4	paramIBackupManager	IBackupManager
      //   0	220	5	paramInt2	int
      //   3	192	6	l	long
      //   15	188	8	localFullBackupDataOutput	FullBackupDataOutput
      //   57	1	9	localObject1	Object
      //   62	58	9	localRuntimeException	RuntimeException
      //   122	96	9	localIOException	IOException
      //   67	125	10	localObject2	Object
      // Exception table:
      //   from	to	target	type
      //   38	51	54	android/os/RemoteException
      //   24	33	57	finally
      //   64	122	57	finally
      //   124	194	57	finally
      //   24	33	62	java/lang/RuntimeException
      //   24	33	122	java/io/IOException
      //   199	212	215	android/os/RemoteException
    }
    
    /* Error */
    public void doQuotaExceeded(long paramLong1, long paramLong2)
    {
      // Byte code:
      //   0: invokestatic 37	android/os/Binder:clearCallingIdentity	()J
      //   3: lstore 5
      //   5: aload_0
      //   6: getfield 16	android/app/backup/BackupAgent$BackupServiceBinder:this$0	Landroid/app/backup/BackupAgent;
      //   9: lload_1
      //   10: lload_3
      //   11: invokevirtual 161	android/app/backup/BackupAgent:onQuotaExceeded	(JJ)V
      //   14: aload_0
      //   15: getfield 16	android/app/backup/BackupAgent$BackupServiceBinder:this$0	Landroid/app/backup/BackupAgent;
      //   18: invokestatic 55	android/app/backup/BackupAgent:access$100	(Landroid/app/backup/BackupAgent;)V
      //   21: lload 5
      //   23: invokestatic 59	android/os/Binder:restoreCallingIdentity	(J)V
      //   26: return
      //   27: astore 7
      //   29: goto +63 -> 92
      //   32: astore 7
      //   34: new 82	java/lang/StringBuilder
      //   37: astore 8
      //   39: aload 8
      //   41: invokespecial 83	java/lang/StringBuilder:<init>	()V
      //   44: aload 8
      //   46: ldc -93
      //   48: invokevirtual 89	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   51: pop
      //   52: aload 8
      //   54: aload_0
      //   55: getfield 16	android/app/backup/BackupAgent$BackupServiceBinder:this$0	Landroid/app/backup/BackupAgent;
      //   58: invokevirtual 95	java/lang/Object:getClass	()Ljava/lang/Class;
      //   61: invokevirtual 101	java/lang/Class:getName	()Ljava/lang/String;
      //   64: invokevirtual 89	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   67: pop
      //   68: aload 8
      //   70: ldc 103
      //   72: invokevirtual 89	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   75: pop
      //   76: ldc 10
      //   78: aload 8
      //   80: invokevirtual 106	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   83: aload 7
      //   85: invokestatic 112	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
      //   88: pop
      //   89: aload 7
      //   91: athrow
      //   92: aload_0
      //   93: getfield 16	android/app/backup/BackupAgent$BackupServiceBinder:this$0	Landroid/app/backup/BackupAgent;
      //   96: invokestatic 55	android/app/backup/BackupAgent:access$100	(Landroid/app/backup/BackupAgent;)V
      //   99: lload 5
      //   101: invokestatic 59	android/os/Binder:restoreCallingIdentity	(J)V
      //   104: aload 7
      //   106: athrow
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	107	0	this	BackupServiceBinder
      //   0	107	1	paramLong1	long
      //   0	107	3	paramLong2	long
      //   3	97	5	l	long
      //   27	1	7	localObject	Object
      //   32	73	7	localException	Exception
      //   37	42	8	localStringBuilder	StringBuilder
      // Exception table:
      //   from	to	target	type
      //   5	14	27	finally
      //   34	92	27	finally
      //   5	14	32	java/lang/Exception
    }
    
    /* Error */
    public void doRestore(ParcelFileDescriptor paramParcelFileDescriptor1, long paramLong, ParcelFileDescriptor paramParcelFileDescriptor2, int paramInt, IBackupManager paramIBackupManager)
      throws android.os.RemoteException
    {
      // Byte code:
      //   0: invokestatic 37	android/os/Binder:clearCallingIdentity	()J
      //   3: lstore 7
      //   5: aload_0
      //   6: getfield 16	android/app/backup/BackupAgent$BackupServiceBinder:this$0	Landroid/app/backup/BackupAgent;
      //   9: invokestatic 55	android/app/backup/BackupAgent:access$100	(Landroid/app/backup/BackupAgent;)V
      //   12: new 167	android/app/backup/BackupDataInput
      //   15: dup
      //   16: aload_1
      //   17: invokevirtual 45	android/os/ParcelFileDescriptor:getFileDescriptor	()Ljava/io/FileDescriptor;
      //   20: invokespecial 168	android/app/backup/BackupDataInput:<init>	(Ljava/io/FileDescriptor;)V
      //   23: astore 9
      //   25: aload_0
      //   26: getfield 16	android/app/backup/BackupAgent$BackupServiceBinder:this$0	Landroid/app/backup/BackupAgent;
      //   29: aload 9
      //   31: lload_2
      //   32: aload 4
      //   34: invokevirtual 172	android/app/backup/BackupAgent:onRestore	(Landroid/app/backup/BackupDataInput;JLandroid/os/ParcelFileDescriptor;)V
      //   37: aload_0
      //   38: getfield 16	android/app/backup/BackupAgent$BackupServiceBinder:this$0	Landroid/app/backup/BackupAgent;
      //   41: invokevirtual 175	android/app/backup/BackupAgent:reloadSharedPreferences	()V
      //   44: lload 7
      //   46: invokestatic 59	android/os/Binder:restoreCallingIdentity	(J)V
      //   49: aload 6
      //   51: iload 5
      //   53: lconst_0
      //   54: invokeinterface 65 4 0
      //   59: goto +5 -> 64
      //   62: astore 6
      //   64: invokestatic 69	android/os/Binder:getCallingPid	()I
      //   67: invokestatic 74	android/os/Process:myPid	()I
      //   70: if_icmpeq +12 -> 82
      //   73: aload_1
      //   74: invokestatic 80	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
      //   77: aload 4
      //   79: invokestatic 80	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
      //   82: return
      //   83: astore 9
      //   85: goto +135 -> 220
      //   88: astore 9
      //   90: new 82	java/lang/StringBuilder
      //   93: astore 10
      //   95: aload 10
      //   97: invokespecial 83	java/lang/StringBuilder:<init>	()V
      //   100: aload 10
      //   102: ldc -79
      //   104: invokevirtual 89	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   107: pop
      //   108: aload 10
      //   110: aload_0
      //   111: getfield 16	android/app/backup/BackupAgent$BackupServiceBinder:this$0	Landroid/app/backup/BackupAgent;
      //   114: invokevirtual 95	java/lang/Object:getClass	()Ljava/lang/Class;
      //   117: invokevirtual 101	java/lang/Class:getName	()Ljava/lang/String;
      //   120: invokevirtual 89	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   123: pop
      //   124: aload 10
      //   126: ldc 103
      //   128: invokevirtual 89	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   131: pop
      //   132: ldc 10
      //   134: aload 10
      //   136: invokevirtual 106	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   139: aload 9
      //   141: invokestatic 112	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
      //   144: pop
      //   145: aload 9
      //   147: athrow
      //   148: astore 9
      //   150: new 82	java/lang/StringBuilder
      //   153: astore 10
      //   155: aload 10
      //   157: invokespecial 83	java/lang/StringBuilder:<init>	()V
      //   160: aload 10
      //   162: ldc -79
      //   164: invokevirtual 89	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   167: pop
      //   168: aload 10
      //   170: aload_0
      //   171: getfield 16	android/app/backup/BackupAgent$BackupServiceBinder:this$0	Landroid/app/backup/BackupAgent;
      //   174: invokevirtual 95	java/lang/Object:getClass	()Ljava/lang/Class;
      //   177: invokevirtual 101	java/lang/Class:getName	()Ljava/lang/String;
      //   180: invokevirtual 89	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   183: pop
      //   184: aload 10
      //   186: ldc 103
      //   188: invokevirtual 89	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   191: pop
      //   192: ldc 10
      //   194: aload 10
      //   196: invokevirtual 106	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   199: aload 9
      //   201: invokestatic 112	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
      //   204: pop
      //   205: new 31	java/lang/RuntimeException
      //   208: astore 10
      //   210: aload 10
      //   212: aload 9
      //   214: invokespecial 115	java/lang/RuntimeException:<init>	(Ljava/lang/Throwable;)V
      //   217: aload 10
      //   219: athrow
      //   220: aload_0
      //   221: getfield 16	android/app/backup/BackupAgent$BackupServiceBinder:this$0	Landroid/app/backup/BackupAgent;
      //   224: invokevirtual 175	android/app/backup/BackupAgent:reloadSharedPreferences	()V
      //   227: lload 7
      //   229: invokestatic 59	android/os/Binder:restoreCallingIdentity	(J)V
      //   232: aload 6
      //   234: iload 5
      //   236: lconst_0
      //   237: invokeinterface 65 4 0
      //   242: goto +5 -> 247
      //   245: astore 6
      //   247: invokestatic 69	android/os/Binder:getCallingPid	()I
      //   250: invokestatic 74	android/os/Process:myPid	()I
      //   253: if_icmpeq +12 -> 265
      //   256: aload_1
      //   257: invokestatic 80	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
      //   260: aload 4
      //   262: invokestatic 80	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
      //   265: aload 9
      //   267: athrow
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	268	0	this	BackupServiceBinder
      //   0	268	1	paramParcelFileDescriptor1	ParcelFileDescriptor
      //   0	268	2	paramLong	long
      //   0	268	4	paramParcelFileDescriptor2	ParcelFileDescriptor
      //   0	268	5	paramInt	int
      //   0	268	6	paramIBackupManager	IBackupManager
      //   3	225	7	l	long
      //   23	7	9	localBackupDataInput	BackupDataInput
      //   83	1	9	localObject1	Object
      //   88	58	9	localRuntimeException	RuntimeException
      //   148	118	9	localIOException	IOException
      //   93	125	10	localObject2	Object
      // Exception table:
      //   from	to	target	type
      //   49	59	62	android/os/RemoteException
      //   25	37	83	finally
      //   90	148	83	finally
      //   150	220	83	finally
      //   25	37	88	java/lang/RuntimeException
      //   25	37	148	java/io/IOException
      //   232	242	245	android/os/RemoteException
    }
    
    /* Error */
    public void doRestoreFile(ParcelFileDescriptor paramParcelFileDescriptor, long paramLong1, int paramInt1, String paramString1, String paramString2, long paramLong2, long paramLong3, int paramInt2, IBackupManager paramIBackupManager)
      throws android.os.RemoteException
    {
      // Byte code:
      //   0: invokestatic 37	android/os/Binder:clearCallingIdentity	()J
      //   3: lstore 13
      //   5: aload_0
      //   6: getfield 16	android/app/backup/BackupAgent$BackupServiceBinder:this$0	Landroid/app/backup/BackupAgent;
      //   9: aload_1
      //   10: lload_2
      //   11: iload 4
      //   13: aload 5
      //   15: aload 6
      //   17: lload 7
      //   19: lload 9
      //   21: invokevirtual 183	android/app/backup/BackupAgent:onRestoreFile	(Landroid/os/ParcelFileDescriptor;JILjava/lang/String;Ljava/lang/String;JJ)V
      //   24: aload_0
      //   25: getfield 16	android/app/backup/BackupAgent$BackupServiceBinder:this$0	Landroid/app/backup/BackupAgent;
      //   28: invokestatic 55	android/app/backup/BackupAgent:access$100	(Landroid/app/backup/BackupAgent;)V
      //   31: aload_0
      //   32: getfield 16	android/app/backup/BackupAgent$BackupServiceBinder:this$0	Landroid/app/backup/BackupAgent;
      //   35: invokevirtual 175	android/app/backup/BackupAgent:reloadSharedPreferences	()V
      //   38: lload 13
      //   40: invokestatic 59	android/os/Binder:restoreCallingIdentity	(J)V
      //   43: aload 12
      //   45: iload 11
      //   47: lconst_0
      //   48: invokeinterface 65 4 0
      //   53: goto +5 -> 58
      //   56: astore 5
      //   58: invokestatic 69	android/os/Binder:getCallingPid	()I
      //   61: invokestatic 74	android/os/Process:myPid	()I
      //   64: if_icmpeq +7 -> 71
      //   67: aload_1
      //   68: invokestatic 80	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
      //   71: return
      //   72: astore 5
      //   74: goto +75 -> 149
      //   77: astore 5
      //   79: new 82	java/lang/StringBuilder
      //   82: astore 6
      //   84: aload 6
      //   86: invokespecial 83	java/lang/StringBuilder:<init>	()V
      //   89: aload 6
      //   91: ldc -71
      //   93: invokevirtual 89	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   96: pop
      //   97: aload 6
      //   99: aload_0
      //   100: getfield 16	android/app/backup/BackupAgent$BackupServiceBinder:this$0	Landroid/app/backup/BackupAgent;
      //   103: invokevirtual 95	java/lang/Object:getClass	()Ljava/lang/Class;
      //   106: invokevirtual 101	java/lang/Class:getName	()Ljava/lang/String;
      //   109: invokevirtual 89	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   112: pop
      //   113: aload 6
      //   115: ldc 103
      //   117: invokevirtual 89	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   120: pop
      //   121: ldc 10
      //   123: aload 6
      //   125: invokevirtual 106	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   128: aload 5
      //   130: invokestatic 112	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
      //   133: pop
      //   134: new 31	java/lang/RuntimeException
      //   137: astore 6
      //   139: aload 6
      //   141: aload 5
      //   143: invokespecial 115	java/lang/RuntimeException:<init>	(Ljava/lang/Throwable;)V
      //   146: aload 6
      //   148: athrow
      //   149: aload_0
      //   150: getfield 16	android/app/backup/BackupAgent$BackupServiceBinder:this$0	Landroid/app/backup/BackupAgent;
      //   153: invokestatic 55	android/app/backup/BackupAgent:access$100	(Landroid/app/backup/BackupAgent;)V
      //   156: aload_0
      //   157: getfield 16	android/app/backup/BackupAgent$BackupServiceBinder:this$0	Landroid/app/backup/BackupAgent;
      //   160: invokevirtual 175	android/app/backup/BackupAgent:reloadSharedPreferences	()V
      //   163: lload 13
      //   165: invokestatic 59	android/os/Binder:restoreCallingIdentity	(J)V
      //   168: aload 12
      //   170: iload 11
      //   172: lconst_0
      //   173: invokeinterface 65 4 0
      //   178: goto +5 -> 183
      //   181: astore 6
      //   183: invokestatic 69	android/os/Binder:getCallingPid	()I
      //   186: invokestatic 74	android/os/Process:myPid	()I
      //   189: if_icmpeq +7 -> 196
      //   192: aload_1
      //   193: invokestatic 80	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
      //   196: aload 5
      //   198: athrow
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	199	0	this	BackupServiceBinder
      //   0	199	1	paramParcelFileDescriptor	ParcelFileDescriptor
      //   0	199	2	paramLong1	long
      //   0	199	4	paramInt1	int
      //   0	199	5	paramString1	String
      //   0	199	6	paramString2	String
      //   0	199	7	paramLong2	long
      //   0	199	9	paramLong3	long
      //   0	199	11	paramInt2	int
      //   0	199	12	paramIBackupManager	IBackupManager
      //   3	161	13	l	long
      // Exception table:
      //   from	to	target	type
      //   43	53	56	android/os/RemoteException
      //   5	24	72	finally
      //   79	149	72	finally
      //   5	24	77	java/io/IOException
      //   168	178	181	android/os/RemoteException
    }
    
    /* Error */
    public void doRestoreFinished(int paramInt, IBackupManager paramIBackupManager)
    {
      // Byte code:
      //   0: invokestatic 37	android/os/Binder:clearCallingIdentity	()J
      //   3: lstore_3
      //   4: aload_0
      //   5: getfield 16	android/app/backup/BackupAgent$BackupServiceBinder:this$0	Landroid/app/backup/BackupAgent;
      //   8: invokevirtual 190	android/app/backup/BackupAgent:onRestoreFinished	()V
      //   11: aload_0
      //   12: getfield 16	android/app/backup/BackupAgent$BackupServiceBinder:this$0	Landroid/app/backup/BackupAgent;
      //   15: invokestatic 55	android/app/backup/BackupAgent:access$100	(Landroid/app/backup/BackupAgent;)V
      //   18: lload_3
      //   19: invokestatic 59	android/os/Binder:restoreCallingIdentity	(J)V
      //   22: aload_2
      //   23: iload_1
      //   24: lconst_0
      //   25: invokeinterface 65 4 0
      //   30: goto +4 -> 34
      //   33: astore_2
      //   34: return
      //   35: astore 5
      //   37: goto +63 -> 100
      //   40: astore 6
      //   42: new 82	java/lang/StringBuilder
      //   45: astore 5
      //   47: aload 5
      //   49: invokespecial 83	java/lang/StringBuilder:<init>	()V
      //   52: aload 5
      //   54: ldc -64
      //   56: invokevirtual 89	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   59: pop
      //   60: aload 5
      //   62: aload_0
      //   63: getfield 16	android/app/backup/BackupAgent$BackupServiceBinder:this$0	Landroid/app/backup/BackupAgent;
      //   66: invokevirtual 95	java/lang/Object:getClass	()Ljava/lang/Class;
      //   69: invokevirtual 101	java/lang/Class:getName	()Ljava/lang/String;
      //   72: invokevirtual 89	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   75: pop
      //   76: aload 5
      //   78: ldc 103
      //   80: invokevirtual 89	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   83: pop
      //   84: ldc 10
      //   86: aload 5
      //   88: invokevirtual 106	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   91: aload 6
      //   93: invokestatic 112	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
      //   96: pop
      //   97: aload 6
      //   99: athrow
      //   100: aload_0
      //   101: getfield 16	android/app/backup/BackupAgent$BackupServiceBinder:this$0	Landroid/app/backup/BackupAgent;
      //   104: invokestatic 55	android/app/backup/BackupAgent:access$100	(Landroid/app/backup/BackupAgent;)V
      //   107: lload_3
      //   108: invokestatic 59	android/os/Binder:restoreCallingIdentity	(J)V
      //   111: aload_2
      //   112: iload_1
      //   113: lconst_0
      //   114: invokeinterface 65 4 0
      //   119: goto +4 -> 123
      //   122: astore_2
      //   123: aload 5
      //   125: athrow
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	126	0	this	BackupServiceBinder
      //   0	126	1	paramInt	int
      //   0	126	2	paramIBackupManager	IBackupManager
      //   3	105	3	l	long
      //   35	1	5	localObject	Object
      //   45	79	5	localStringBuilder	StringBuilder
      //   40	58	6	localException	Exception
      // Exception table:
      //   from	to	target	type
      //   22	30	33	android/os/RemoteException
      //   4	11	35	finally
      //   42	100	35	finally
      //   4	11	40	java/lang/Exception
      //   111	119	122	android/os/RemoteException
    }
    
    public void fail(String paramString)
    {
      getHandler().post(new BackupAgent.FailRunnable(paramString));
    }
  }
  
  static class FailRunnable
    implements Runnable
  {
    private String mMessage;
    
    FailRunnable(String paramString)
    {
      mMessage = paramString;
    }
    
    public void run()
    {
      throw new IllegalStateException(mMessage);
    }
  }
  
  class SharedPrefsSynchronizer
    implements Runnable
  {
    public final CountDownLatch mLatch = new CountDownLatch(1);
    
    SharedPrefsSynchronizer() {}
    
    public void run()
    {
      QueuedWork.waitToFinish();
      mLatch.countDown();
    }
  }
}
