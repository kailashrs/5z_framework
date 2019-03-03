package android.app.backup;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.ParcelFileDescriptor;
import android.os.Process;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.system.ErrnoException;
import android.system.Os;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class FullBackup
{
  public static final String APK_TREE_TOKEN = "a";
  public static final String APPS_PREFIX = "apps/";
  public static final String CACHE_TREE_TOKEN = "c";
  public static final String CONF_TOKEN_INTENT_EXTRA = "conftoken";
  public static final String DATABASE_TREE_TOKEN = "db";
  public static final String DEVICE_CACHE_TREE_TOKEN = "d_c";
  public static final String DEVICE_DATABASE_TREE_TOKEN = "d_db";
  public static final String DEVICE_FILES_TREE_TOKEN = "d_f";
  public static final String DEVICE_NO_BACKUP_TREE_TOKEN = "d_nb";
  public static final String DEVICE_ROOT_TREE_TOKEN = "d_r";
  public static final String DEVICE_SHAREDPREFS_TREE_TOKEN = "d_sp";
  public static final String FILES_TREE_TOKEN = "f";
  public static final String FLAG_REQUIRED_CLIENT_SIDE_ENCRYPTION = "clientSideEncryption";
  public static final String FLAG_REQUIRED_DEVICE_TO_DEVICE_TRANSFER = "deviceToDeviceTransfer";
  public static final String FLAG_REQUIRED_FAKE_CLIENT_SIDE_ENCRYPTION = "fakeClientSideEncryption";
  public static final String FULL_BACKUP_INTENT_ACTION = "fullback";
  public static final String FULL_RESTORE_INTENT_ACTION = "fullrest";
  public static final String KEY_VALUE_DATA_TOKEN = "k";
  public static final String MANAGED_EXTERNAL_TREE_TOKEN = "ef";
  public static final String NO_BACKUP_TREE_TOKEN = "nb";
  public static final String OBB_TREE_TOKEN = "obb";
  public static final String ROOT_TREE_TOKEN = "r";
  public static final String SHAREDPREFS_TREE_TOKEN = "sp";
  public static final String SHARED_PREFIX = "shared/";
  public static final String SHARED_STORAGE_TOKEN = "shared";
  static final String TAG = "FullBackup";
  static final String TAG_XML_PARSER = "BackupXmlParserLogging";
  private static final Map<String, BackupScheme> kPackageBackupSchemeMap = new ArrayMap();
  
  public FullBackup() {}
  
  public static native int backupToTar(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, FullBackupDataOutput paramFullBackupDataOutput);
  
  static BackupScheme getBackupScheme(Context paramContext)
  {
    try
    {
      BackupScheme localBackupScheme1 = (BackupScheme)kPackageBackupSchemeMap.get(paramContext.getPackageName());
      BackupScheme localBackupScheme2 = localBackupScheme1;
      if (localBackupScheme1 == null)
      {
        localBackupScheme2 = new android/app/backup/FullBackup$BackupScheme;
        localBackupScheme2.<init>(paramContext);
        kPackageBackupSchemeMap.put(paramContext.getPackageName(), localBackupScheme2);
      }
      return localBackupScheme2;
    }
    finally {}
  }
  
  public static BackupScheme getBackupSchemeForTest(Context paramContext)
  {
    paramContext = new BackupScheme(paramContext);
    mExcludes = new ArraySet();
    mIncludes = new ArrayMap();
    return paramContext;
  }
  
  public static void restoreFile(ParcelFileDescriptor paramParcelFileDescriptor, long paramLong1, int paramInt, long paramLong2, long paramLong3, File paramFile)
    throws IOException
  {
    if (paramInt == 2)
    {
      if (paramFile != null) {
        paramFile.mkdirs();
      }
    }
    else
    {
      Object localObject1 = null;
      Object localObject2 = null;
      if (paramFile != null) {
        try
        {
          localObject2 = paramFile.getParentFile();
          if (!((File)localObject2).exists()) {
            ((File)localObject2).mkdirs();
          }
          localObject2 = new java/io/FileOutputStream;
          ((FileOutputStream)localObject2).<init>(paramFile);
        }
        catch (IOException localIOException1)
        {
          localObject2 = new StringBuilder();
          ((StringBuilder)localObject2).append("Unable to create/open file ");
          ((StringBuilder)localObject2).append(paramFile.getPath());
          Log.e("FullBackup", ((StringBuilder)localObject2).toString(), localIOException1);
          localObject2 = localObject1;
        }
      }
      byte[] arrayOfByte = new byte[32768];
      paramParcelFileDescriptor = new FileInputStream(paramParcelFileDescriptor.getFileDescriptor());
      long l = paramLong1;
      while (l > 0L)
      {
        if (l > arrayOfByte.length) {
          paramInt = arrayOfByte.length;
        } else {
          paramInt = (int)l;
        }
        paramInt = paramParcelFileDescriptor.read(arrayOfByte, 0, paramInt);
        if (paramInt <= 0)
        {
          paramParcelFileDescriptor = new StringBuilder();
          paramParcelFileDescriptor.append("Incomplete read: expected ");
          paramParcelFileDescriptor.append(l);
          paramParcelFileDescriptor.append(" but got ");
          paramParcelFileDescriptor.append(paramLong1 - l);
          Log.w("FullBackup", paramParcelFileDescriptor.toString());
          break;
        }
        localObject1 = localObject2;
        if (localObject2 != null) {
          try
          {
            ((FileOutputStream)localObject2).write(arrayOfByte, 0, paramInt);
            localObject1 = localObject2;
          }
          catch (IOException localIOException2)
          {
            localObject1 = new StringBuilder();
            ((StringBuilder)localObject1).append("Unable to write to file ");
            ((StringBuilder)localObject1).append(paramFile.getPath());
            Log.e("FullBackup", ((StringBuilder)localObject1).toString(), localIOException2);
            ((FileOutputStream)localObject2).close();
            paramFile.delete();
            localObject1 = null;
          }
        }
        l -= paramInt;
        localObject2 = localObject1;
      }
      if (localObject2 != null) {
        ((FileOutputStream)localObject2).close();
      }
    }
    if ((paramLong2 >= 0L) && (paramFile != null))
    {
      try
      {
        Os.chmod(paramFile.getPath(), (int)(paramLong2 & 0x1C0));
      }
      catch (ErrnoException paramParcelFileDescriptor)
      {
        paramParcelFileDescriptor.rethrowAsIOException();
      }
      paramFile.setLastModified(paramLong3);
    }
  }
  
  @VisibleForTesting
  public static class BackupScheme
  {
    private static final String TAG_EXCLUDE = "exclude";
    private static final String TAG_INCLUDE = "include";
    private final File CACHE_DIR;
    private final File DATABASE_DIR;
    private final File DEVICE_CACHE_DIR;
    private final File DEVICE_DATABASE_DIR;
    private final File DEVICE_FILES_DIR;
    private final File DEVICE_NOBACKUP_DIR;
    private final File DEVICE_ROOT_DIR;
    private final File DEVICE_SHAREDPREF_DIR;
    private final File EXTERNAL_DIR;
    private final File FILES_DIR;
    private final File NOBACKUP_DIR;
    private final File ROOT_DIR;
    private final File SHAREDPREF_DIR;
    ArraySet<PathWithRequiredFlags> mExcludes;
    final int mFullBackupContent;
    Map<String, Set<PathWithRequiredFlags>> mIncludes;
    final PackageManager mPackageManager;
    final String mPackageName;
    final StorageManager mStorageManager;
    private StorageVolume[] mVolumes = null;
    
    BackupScheme(Context paramContext)
    {
      mFullBackupContent = getApplicationInfofullBackupContent;
      mStorageManager = ((StorageManager)paramContext.getSystemService("storage"));
      mPackageManager = paramContext.getPackageManager();
      mPackageName = paramContext.getPackageName();
      Context localContext = paramContext.createCredentialProtectedStorageContext();
      FILES_DIR = localContext.getFilesDir();
      DATABASE_DIR = localContext.getDatabasePath("foo").getParentFile();
      ROOT_DIR = localContext.getDataDir();
      SHAREDPREF_DIR = localContext.getSharedPreferencesPath("foo").getParentFile();
      CACHE_DIR = localContext.getCacheDir();
      NOBACKUP_DIR = localContext.getNoBackupFilesDir();
      localContext = paramContext.createDeviceProtectedStorageContext();
      DEVICE_FILES_DIR = localContext.getFilesDir();
      DEVICE_DATABASE_DIR = localContext.getDatabasePath("foo").getParentFile();
      DEVICE_ROOT_DIR = localContext.getDataDir();
      DEVICE_SHAREDPREF_DIR = localContext.getSharedPreferencesPath("foo").getParentFile();
      DEVICE_CACHE_DIR = localContext.getCacheDir();
      DEVICE_NOBACKUP_DIR = localContext.getNoBackupFilesDir();
      if (Process.myUid() != 1000) {
        EXTERNAL_DIR = paramContext.getExternalFilesDir(null);
      } else {
        EXTERNAL_DIR = null;
      }
    }
    
    private File extractCanonicalFile(File paramFile, String paramString)
    {
      String str = paramString;
      if (paramString == null) {
        str = "";
      }
      if (str.contains(".."))
      {
        if (Log.isLoggable("BackupXmlParserLogging", 2))
        {
          paramString = new StringBuilder();
          paramString.append("...resolved \"");
          paramString.append(paramFile.getPath());
          paramString.append(" ");
          paramString.append(str);
          paramString.append("\", but the \"..\" path is not permitted; skipping.");
          Log.v("BackupXmlParserLogging", paramString.toString());
        }
        return null;
      }
      if (str.contains("//"))
      {
        if (Log.isLoggable("BackupXmlParserLogging", 2))
        {
          paramString = new StringBuilder();
          paramString.append("...resolved \"");
          paramString.append(paramFile.getPath());
          paramString.append(" ");
          paramString.append(str);
          paramString.append("\", which contains the invalid \"//\" sequence; skipping.");
          Log.v("BackupXmlParserLogging", paramString.toString());
        }
        return null;
      }
      return new File(paramFile, str);
    }
    
    private File getDirectoryForCriteriaDomain(String paramString)
    {
      if (TextUtils.isEmpty(paramString)) {
        return null;
      }
      if ("file".equals(paramString)) {
        return FILES_DIR;
      }
      if ("database".equals(paramString)) {
        return DATABASE_DIR;
      }
      if ("root".equals(paramString)) {
        return ROOT_DIR;
      }
      if ("sharedpref".equals(paramString)) {
        return SHAREDPREF_DIR;
      }
      if ("device_file".equals(paramString)) {
        return DEVICE_FILES_DIR;
      }
      if ("device_database".equals(paramString)) {
        return DEVICE_DATABASE_DIR;
      }
      if ("device_root".equals(paramString)) {
        return DEVICE_ROOT_DIR;
      }
      if ("device_sharedpref".equals(paramString)) {
        return DEVICE_SHAREDPREF_DIR;
      }
      if ("external".equals(paramString)) {
        return EXTERNAL_DIR;
      }
      return null;
    }
    
    private int getRequiredFlagsFromString(String paramString)
    {
      if ((paramString != null) && (paramString.length() != 0))
      {
        paramString = paramString.split("\\|");
        int i = paramString.length;
        int j = 0;
        int k = 0;
        while (k < i)
        {
          String str = paramString[k];
          int m = -1;
          int n = str.hashCode();
          if (n != 482744282)
          {
            if (n != 1499007205)
            {
              if ((n == 1935925810) && (str.equals("deviceToDeviceTransfer"))) {
                m = 1;
              }
            }
            else if (str.equals("clientSideEncryption")) {
              m = 0;
            }
          }
          else if (str.equals("fakeClientSideEncryption")) {
            m = 2;
          }
          switch (m)
          {
          default: 
            m = j;
            break;
          case 2: 
            m = j | 0x80000000;
            break;
          case 1: 
            m = j | 0x2;
            break;
          case 0: 
            m = j | 0x1;
            break;
          }
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("Unrecognized requiredFlag provided, value: \"");
          localStringBuilder.append(str);
          localStringBuilder.append("\"");
          Log.w("FullBackup", localStringBuilder.toString());
          k++;
          j = m;
        }
        return j;
      }
      return 0;
    }
    
    private String getTokenForXmlDomain(String paramString)
    {
      if ("root".equals(paramString)) {
        return "r";
      }
      if ("file".equals(paramString)) {
        return "f";
      }
      if ("database".equals(paramString)) {
        return "db";
      }
      if ("sharedpref".equals(paramString)) {
        return "sp";
      }
      if ("device_root".equals(paramString)) {
        return "d_r";
      }
      if ("device_file".equals(paramString)) {
        return "d_f";
      }
      if ("device_database".equals(paramString)) {
        return "d_db";
      }
      if ("device_sharedpref".equals(paramString)) {
        return "d_sp";
      }
      if ("external".equals(paramString)) {
        return "ef";
      }
      return null;
    }
    
    private StorageVolume[] getVolumeList()
    {
      if (mStorageManager != null)
      {
        if (mVolumes == null) {
          mVolumes = mStorageManager.getVolumeList();
        }
      }
      else {
        Log.e("FullBackup", "Unable to access Storage Manager");
      }
      return mVolumes;
    }
    
    /* Error */
    private void maybeParseBackupSchemeLocked()
      throws IOException, XmlPullParserException
    {
      // Byte code:
      //   0: aload_0
      //   1: new 308	android/util/ArrayMap
      //   4: dup
      //   5: invokespecial 309	android/util/ArrayMap:<init>	()V
      //   8: putfield 311	android/app/backup/FullBackup$BackupScheme:mIncludes	Ljava/util/Map;
      //   11: aload_0
      //   12: new 313	android/util/ArraySet
      //   15: dup
      //   16: invokespecial 314	android/util/ArraySet:<init>	()V
      //   19: putfield 316	android/app/backup/FullBackup$BackupScheme:mExcludes	Landroid/util/ArraySet;
      //   22: aload_0
      //   23: getfield 67	android/app/backup/FullBackup$BackupScheme:mFullBackupContent	I
      //   26: ifne +24 -> 50
      //   29: ldc -83
      //   31: iconst_2
      //   32: invokestatic 179	android/util/Log:isLoggable	(Ljava/lang/String;I)Z
      //   35: ifeq +83 -> 118
      //   38: ldc -83
      //   40: ldc_w 318
      //   43: invokestatic 202	android/util/Log:v	(Ljava/lang/String;Ljava/lang/String;)I
      //   46: pop
      //   47: goto +71 -> 118
      //   50: ldc -83
      //   52: iconst_2
      //   53: invokestatic 179	android/util/Log:isLoggable	(Ljava/lang/String;I)Z
      //   56: ifeq +12 -> 68
      //   59: ldc -83
      //   61: ldc_w 320
      //   64: invokestatic 202	android/util/Log:v	(Ljava/lang/String;Ljava/lang/String;)I
      //   67: pop
      //   68: aconst_null
      //   69: astore_1
      //   70: aconst_null
      //   71: astore_2
      //   72: aload_0
      //   73: getfield 83	android/app/backup/FullBackup$BackupScheme:mPackageManager	Landroid/content/pm/PackageManager;
      //   76: aload_0
      //   77: getfield 89	android/app/backup/FullBackup$BackupScheme:mPackageName	Ljava/lang/String;
      //   80: invokevirtual 326	android/content/pm/PackageManager:getResourcesForApplication	(Ljava/lang/String;)Landroid/content/res/Resources;
      //   83: aload_0
      //   84: getfield 67	android/app/backup/FullBackup$BackupScheme:mFullBackupContent	I
      //   87: invokevirtual 332	android/content/res/Resources:getXml	(I)Landroid/content/res/XmlResourceParser;
      //   90: astore_3
      //   91: aload_3
      //   92: astore_2
      //   93: aload_3
      //   94: astore_1
      //   95: aload_0
      //   96: aload_3
      //   97: aload_0
      //   98: getfield 316	android/app/backup/FullBackup$BackupScheme:mExcludes	Landroid/util/ArraySet;
      //   101: aload_0
      //   102: getfield 311	android/app/backup/FullBackup$BackupScheme:mIncludes	Ljava/util/Map;
      //   105: invokevirtual 336	android/app/backup/FullBackup$BackupScheme:parseBackupSchemeFromXmlLocked	(Lorg/xmlpull/v1/XmlPullParser;Ljava/util/Set;Ljava/util/Map;)V
      //   108: aload_3
      //   109: ifnull +9 -> 118
      //   112: aload_3
      //   113: invokeinterface 341 1 0
      //   118: return
      //   119: astore_1
      //   120: goto +23 -> 143
      //   123: astore 4
      //   125: aload_1
      //   126: astore_2
      //   127: new 302	java/io/IOException
      //   130: astore_3
      //   131: aload_1
      //   132: astore_2
      //   133: aload_3
      //   134: aload 4
      //   136: invokespecial 344	java/io/IOException:<init>	(Ljava/lang/Throwable;)V
      //   139: aload_1
      //   140: astore_2
      //   141: aload_3
      //   142: athrow
      //   143: aload_2
      //   144: ifnull +9 -> 153
      //   147: aload_2
      //   148: invokeinterface 341 1 0
      //   153: aload_1
      //   154: athrow
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	155	0	this	BackupScheme
      //   69	26	1	localObject1	Object
      //   119	35	1	localObject2	Object
      //   71	77	2	localObject3	Object
      //   90	52	3	localObject4	Object
      //   123	12	4	localNameNotFoundException	android.content.pm.PackageManager.NameNotFoundException
      // Exception table:
      //   from	to	target	type
      //   72	91	119	finally
      //   95	108	119	finally
      //   127	131	119	finally
      //   133	139	119	finally
      //   141	143	119	finally
      //   72	91	123	android/content/pm/PackageManager$NameNotFoundException
      //   95	108	123	android/content/pm/PackageManager$NameNotFoundException
    }
    
    private Set<PathWithRequiredFlags> parseCurrentTagForDomain(XmlPullParser paramXmlPullParser, Set<PathWithRequiredFlags> paramSet, Map<String, Set<PathWithRequiredFlags>> paramMap, String paramString)
      throws XmlPullParserException
    {
      if ("include".equals(paramXmlPullParser.getName()))
      {
        paramString = getTokenForXmlDomain(paramString);
        paramSet = (Set)paramMap.get(paramString);
        paramXmlPullParser = paramSet;
        if (paramSet == null)
        {
          paramXmlPullParser = new ArraySet();
          paramMap.put(paramString, paramXmlPullParser);
        }
        return paramXmlPullParser;
      }
      if ("exclude".equals(paramXmlPullParser.getName())) {
        return paramSet;
      }
      if (Log.isLoggable("BackupXmlParserLogging", 2))
      {
        paramSet = new StringBuilder();
        paramSet.append("Invalid tag found in xml \"");
        paramSet.append(paramXmlPullParser.getName());
        paramSet.append("\"; aborting operation.");
        Log.v("BackupXmlParserLogging", paramSet.toString());
      }
      paramSet = new StringBuilder();
      paramSet.append("Unrecognised tag in backup criteria xml (");
      paramSet.append(paramXmlPullParser.getName());
      paramSet.append(")");
      throw new XmlPullParserException(paramSet.toString());
    }
    
    private String sharedDomainToPath(String paramString)
      throws IOException
    {
      paramString = paramString.substring("shared/".length());
      StorageVolume[] arrayOfStorageVolume = getVolumeList();
      int i = Integer.parseInt(paramString);
      if (i < mVolumes.length) {
        return arrayOfStorageVolume[i].getPathFile().getCanonicalPath();
      }
      return null;
    }
    
    private void validateInnerTagContents(XmlPullParser paramXmlPullParser)
      throws XmlPullParserException
    {
      if (paramXmlPullParser == null) {
        return;
      }
      Object localObject = paramXmlPullParser.getName();
      int i = -1;
      int j = ((String)localObject).hashCode();
      if (j != -1321148966)
      {
        if ((j == 1942574248) && (((String)localObject).equals("include"))) {
          i = 0;
        }
      }
      else if (((String)localObject).equals("exclude")) {
        i = 1;
      }
      switch (i)
      {
      default: 
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("A valid tag is one of \"<include/>\" or \"<exclude/>. You provided \"");
        ((StringBuilder)localObject).append(paramXmlPullParser.getName());
        ((StringBuilder)localObject).append("\"");
        throw new XmlPullParserException(((StringBuilder)localObject).toString());
      case 1: 
        if (paramXmlPullParser.getAttributeCount() > 2) {
          throw new XmlPullParserException("At most 2 tag attributes allowed for \"exclude\" tag (\"domain\" & \"path\".");
        }
        break;
      case 0: 
        if (paramXmlPullParser.getAttributeCount() > 3) {
          break label170;
        }
      }
      return;
      label170:
      throw new XmlPullParserException("At most 3 tag attributes allowed for \"include\" tag (\"domain\" & \"path\" & optional \"requiredFlags\").");
    }
    
    boolean isFullBackupContentEnabled()
    {
      if (mFullBackupContent < 0)
      {
        if (Log.isLoggable("BackupXmlParserLogging", 2)) {
          Log.v("BackupXmlParserLogging", "android:fullBackupContent - \"false\"");
        }
        return false;
      }
      return true;
    }
    
    public ArraySet<PathWithRequiredFlags> maybeParseAndGetCanonicalExcludePaths()
      throws IOException, XmlPullParserException
    {
      try
      {
        if (mExcludes == null) {
          maybeParseBackupSchemeLocked();
        }
        ArraySet localArraySet = mExcludes;
        return localArraySet;
      }
      finally {}
    }
    
    public Map<String, Set<PathWithRequiredFlags>> maybeParseAndGetCanonicalIncludePaths()
      throws IOException, XmlPullParserException
    {
      try
      {
        if (mIncludes == null) {
          maybeParseBackupSchemeLocked();
        }
        Map localMap = mIncludes;
        return localMap;
      }
      finally {}
    }
    
    @VisibleForTesting
    public void parseBackupSchemeFromXmlLocked(XmlPullParser paramXmlPullParser, Set<PathWithRequiredFlags> paramSet, Map<String, Set<PathWithRequiredFlags>> paramMap)
      throws IOException, XmlPullParserException
    {
      for (int i = paramXmlPullParser.getEventType(); i != 2; i = paramXmlPullParser.next()) {}
      if ("full-backup-content".equals(paramXmlPullParser.getName()))
      {
        if (Log.isLoggable("BackupXmlParserLogging", 2))
        {
          Log.v("BackupXmlParserLogging", "\n");
          Log.v("BackupXmlParserLogging", "====================================================");
          Log.v("BackupXmlParserLogging", "Found valid fullBackupContent; parsing xml resource.");
          Log.v("BackupXmlParserLogging", "====================================================");
          Log.v("BackupXmlParserLogging", "");
        }
        Object localObject1;
        Object localObject2;
        for (;;)
        {
          i = paramXmlPullParser.next();
          if (i == 1) {
            break;
          }
          if (i == 2)
          {
            validateInnerTagContents(paramXmlPullParser);
            localObject1 = paramXmlPullParser.getAttributeValue(null, "domain");
            localObject2 = getDirectoryForCriteriaDomain((String)localObject1);
            if (localObject2 == null)
            {
              if (Log.isLoggable("BackupXmlParserLogging", 2))
              {
                localObject2 = new StringBuilder();
                ((StringBuilder)localObject2).append("...parsing \"");
                ((StringBuilder)localObject2).append(paramXmlPullParser.getName());
                ((StringBuilder)localObject2).append("\": domain=\"");
                ((StringBuilder)localObject2).append((String)localObject1);
                ((StringBuilder)localObject2).append("\" invalid; skipping");
                Log.v("BackupXmlParserLogging", ((StringBuilder)localObject2).toString());
              }
            }
            else
            {
              File localFile = extractCanonicalFile((File)localObject2, paramXmlPullParser.getAttributeValue(null, "path"));
              if (localFile != null)
              {
                i = 0;
                if ("include".equals(paramXmlPullParser.getName())) {
                  i = getRequiredFlagsFromString(paramXmlPullParser.getAttributeValue(null, "requireFlags"));
                }
                localObject2 = parseCurrentTagForDomain(paramXmlPullParser, paramSet, paramMap, (String)localObject1);
                ((Set)localObject2).add(new PathWithRequiredFlags(localFile.getCanonicalPath(), i));
                Object localObject3;
                if (Log.isLoggable("BackupXmlParserLogging", 2))
                {
                  localObject3 = new StringBuilder();
                  ((StringBuilder)localObject3).append("...parsed ");
                  ((StringBuilder)localObject3).append(localFile.getCanonicalPath());
                  ((StringBuilder)localObject3).append(" for domain \"");
                  ((StringBuilder)localObject3).append((String)localObject1);
                  ((StringBuilder)localObject3).append("\", requiredFlags + \"");
                  ((StringBuilder)localObject3).append(i);
                  ((StringBuilder)localObject3).append("\"");
                  Log.v("BackupXmlParserLogging", ((StringBuilder)localObject3).toString());
                }
                if (("database".equals(localObject1)) && (!localFile.isDirectory()))
                {
                  localObject3 = new StringBuilder();
                  ((StringBuilder)localObject3).append(localFile.getCanonicalPath());
                  ((StringBuilder)localObject3).append("-journal");
                  localObject3 = ((StringBuilder)localObject3).toString();
                  ((Set)localObject2).add(new PathWithRequiredFlags((String)localObject3, i));
                  StringBuilder localStringBuilder;
                  if (Log.isLoggable("BackupXmlParserLogging", 2))
                  {
                    localStringBuilder = new StringBuilder();
                    localStringBuilder.append("...automatically generated ");
                    localStringBuilder.append((String)localObject3);
                    localStringBuilder.append(". Ignore if nonexistent.");
                    Log.v("BackupXmlParserLogging", localStringBuilder.toString());
                  }
                  localObject3 = new StringBuilder();
                  ((StringBuilder)localObject3).append(localFile.getCanonicalPath());
                  ((StringBuilder)localObject3).append("-wal");
                  localObject3 = ((StringBuilder)localObject3).toString();
                  ((Set)localObject2).add(new PathWithRequiredFlags((String)localObject3, i));
                  if (Log.isLoggable("BackupXmlParserLogging", 2))
                  {
                    localStringBuilder = new StringBuilder();
                    localStringBuilder.append("...automatically generated ");
                    localStringBuilder.append((String)localObject3);
                    localStringBuilder.append(". Ignore if nonexistent.");
                    Log.v("BackupXmlParserLogging", localStringBuilder.toString());
                  }
                }
                if (("sharedpref".equals(localObject1)) && (!localFile.isDirectory()) && (!localFile.getCanonicalPath().endsWith(".xml")))
                {
                  localObject1 = new StringBuilder();
                  ((StringBuilder)localObject1).append(localFile.getCanonicalPath());
                  ((StringBuilder)localObject1).append(".xml");
                  localObject1 = ((StringBuilder)localObject1).toString();
                  ((Set)localObject2).add(new PathWithRequiredFlags((String)localObject1, i));
                  if (Log.isLoggable("BackupXmlParserLogging", 2))
                  {
                    localObject2 = new StringBuilder();
                    ((StringBuilder)localObject2).append("...automatically generated ");
                    ((StringBuilder)localObject2).append((String)localObject1);
                    ((StringBuilder)localObject2).append(". Ignore if nonexistent.");
                    Log.v("BackupXmlParserLogging", ((StringBuilder)localObject2).toString());
                  }
                }
              }
            }
          }
        }
        if (Log.isLoggable("BackupXmlParserLogging", 2))
        {
          Log.v("BackupXmlParserLogging", "\n");
          Log.v("BackupXmlParserLogging", "Xml resource parsing complete.");
          Log.v("BackupXmlParserLogging", "Final tally.");
          Log.v("BackupXmlParserLogging", "Includes:");
          if (paramMap.isEmpty())
          {
            Log.v("BackupXmlParserLogging", "  ...nothing specified (This means the entirety of app data minus excludes)");
          }
          else
          {
            paramXmlPullParser = paramMap.entrySet().iterator();
            while (paramXmlPullParser.hasNext())
            {
              localObject1 = (Map.Entry)paramXmlPullParser.next();
              paramMap = new StringBuilder();
              paramMap.append("  domain=");
              paramMap.append((String)((Map.Entry)localObject1).getKey());
              Log.v("BackupXmlParserLogging", paramMap.toString());
              localObject1 = ((Set)((Map.Entry)localObject1).getValue()).iterator();
              while (((Iterator)localObject1).hasNext())
              {
                localObject2 = (PathWithRequiredFlags)((Iterator)localObject1).next();
                paramMap = new StringBuilder();
                paramMap.append(" path: ");
                paramMap.append(((PathWithRequiredFlags)localObject2).getPath());
                paramMap.append(" requiredFlags: ");
                paramMap.append(((PathWithRequiredFlags)localObject2).getRequiredFlags());
                Log.v("BackupXmlParserLogging", paramMap.toString());
              }
            }
          }
          Log.v("BackupXmlParserLogging", "Excludes:");
          if (paramSet.isEmpty())
          {
            Log.v("BackupXmlParserLogging", "  ...nothing to exclude.");
          }
          else
          {
            paramSet = paramSet.iterator();
            while (paramSet.hasNext())
            {
              paramXmlPullParser = (PathWithRequiredFlags)paramSet.next();
              paramMap = new StringBuilder();
              paramMap.append(" path: ");
              paramMap.append(paramXmlPullParser.getPath());
              paramMap.append(" requiredFlags: ");
              paramMap.append(paramXmlPullParser.getRequiredFlags());
              Log.v("BackupXmlParserLogging", paramMap.toString());
            }
          }
          Log.v("BackupXmlParserLogging", "  ");
          Log.v("BackupXmlParserLogging", "====================================================");
          Log.v("BackupXmlParserLogging", "\n");
        }
        return;
      }
      paramSet = new StringBuilder();
      paramSet.append("Xml file didn't start with correct tag (<full-backup-content>). Found \"");
      paramSet.append(paramXmlPullParser.getName());
      paramSet.append("\"");
      throw new XmlPullParserException(paramSet.toString());
    }
    
    String tokenToDirectoryPath(String paramString)
    {
      try
      {
        if (paramString.equals("f")) {
          return FILES_DIR.getCanonicalPath();
        }
        if (paramString.equals("db")) {
          return DATABASE_DIR.getCanonicalPath();
        }
        if (paramString.equals("r")) {
          return ROOT_DIR.getCanonicalPath();
        }
        if (paramString.equals("sp")) {
          return SHAREDPREF_DIR.getCanonicalPath();
        }
        if (paramString.equals("c")) {
          return CACHE_DIR.getCanonicalPath();
        }
        if (paramString.equals("nb")) {
          return NOBACKUP_DIR.getCanonicalPath();
        }
        if (paramString.equals("d_f")) {
          return DEVICE_FILES_DIR.getCanonicalPath();
        }
        if (paramString.equals("d_db")) {
          return DEVICE_DATABASE_DIR.getCanonicalPath();
        }
        if (paramString.equals("d_r")) {
          return DEVICE_ROOT_DIR.getCanonicalPath();
        }
        if (paramString.equals("d_sp")) {
          return DEVICE_SHAREDPREF_DIR.getCanonicalPath();
        }
        if (paramString.equals("d_c")) {
          return DEVICE_CACHE_DIR.getCanonicalPath();
        }
        if (paramString.equals("d_nb")) {
          return DEVICE_NOBACKUP_DIR.getCanonicalPath();
        }
        if (paramString.equals("ef"))
        {
          if (EXTERNAL_DIR != null) {
            return EXTERNAL_DIR.getCanonicalPath();
          }
          return null;
        }
        if (paramString.startsWith("shared/")) {
          return sharedDomainToPath(paramString);
        }
        StringBuilder localStringBuilder1 = new java/lang/StringBuilder;
        localStringBuilder1.<init>();
        localStringBuilder1.append("Unrecognized domain ");
        localStringBuilder1.append(paramString);
        Log.i("FullBackup", localStringBuilder1.toString());
        return null;
      }
      catch (Exception localException)
      {
        StringBuilder localStringBuilder2 = new StringBuilder();
        localStringBuilder2.append("Error reading directory for domain: ");
        localStringBuilder2.append(paramString);
        Log.i("FullBackup", localStringBuilder2.toString());
      }
      return null;
    }
    
    public static class PathWithRequiredFlags
    {
      private final String mPath;
      private final int mRequiredFlags;
      
      public PathWithRequiredFlags(String paramString, int paramInt)
      {
        mPath = paramString;
        mRequiredFlags = paramInt;
      }
      
      public String getPath()
      {
        return mPath;
      }
      
      public int getRequiredFlags()
      {
        return mRequiredFlags;
      }
    }
  }
}
