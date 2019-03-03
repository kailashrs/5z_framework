package com.android.internal.backup;

import android.app.backup.BackupDataOutput;
import android.app.backup.BackupTransport;
import android.app.backup.RestoreDescription;
import android.app.backup.RestoreSet;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.os.StatFs;
import android.util.Log;
import com.android.org.bouncycastle.util.encoders.Base64;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import libcore.io.IoUtils;

public class AsusTransport
  extends BackupTransport
{
  private static final long CURRENT_SET_TOKEN = 5566L;
  private static final boolean DEBUG = true;
  private static long FULL_BACKUP_SIZE_QUOTA = 268435456L;
  private static final String FULL_DATA_DIR = "_full";
  private static final String INCREMENTAL_DIR = "_delta";
  static final long[] POSSIBLE_SETS = { 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L };
  private static final String TAG = "AsusTransport";
  private static final String TRANSPORT_DATA_MANAGEMENT_LABEL = "";
  private static final String TRANSPORT_DESTINATION_STRING = "Transfer data to new ASUS device";
  private static final String TRANSPORT_DIR_NAME = "com.android.internal.backup.AsusTransport";
  public static final String TRANSPORT_NAME = "android/com.android.internal.backup.AsusTransport";
  private Context mContext;
  private FileInputStream mCurFullRestoreStream;
  private File mCurrentSetDir = new File(mDataDir, Long.toString(5566L));
  private File mCurrentSetFullDir = new File(mCurrentSetDir, "_full");
  private File mCurrentSetIncrementalDir = new File(mCurrentSetDir, "_delta");
  private File mDataDir = new File("/cache/AsusBackup");
  private byte[] mFullBackupBuffer;
  private BufferedOutputStream mFullBackupOutputStream;
  private long mFullBackupSize;
  private byte[] mFullRestoreBuffer;
  private FileOutputStream mFullRestoreSocketStream;
  private String mFullTargetPackage;
  private int mRestorePackage = -1;
  private PackageInfo[] mRestorePackages = null;
  private File mRestoreSetDir;
  private File mRestoreSetFullDir;
  private File mRestoreSetIncrementalDir;
  private int mRestoreType;
  private ParcelFileDescriptor mSocket;
  private FileInputStream mSocketInputStream;
  
  public AsusTransport(Context paramContext)
  {
    mContext = paramContext;
    if (!mCurrentSetDir.exists()) {
      makeDir();
    }
  }
  
  private void chmodALLFile()
  {
    try
    {
      Object localObject = new java/lang/StringBuilder;
      ((StringBuilder)localObject).<init>();
      ((StringBuilder)localObject).append("chmod 777 -R ");
      ((StringBuilder)localObject).append(mDataDir.getCanonicalPath());
      localObject = ((StringBuilder)localObject).toString();
      Runtime localRuntime = Runtime.getRuntime();
      StringBuilder localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append("command = ");
      localStringBuilder.append((String)localObject);
      Log.i("AsusTransport", localStringBuilder.toString());
      localRuntime.exec((String)localObject);
    }
    catch (IOException localIOException)
    {
      localIOException.fillInStackTrace();
    }
  }
  
  private ArrayList<LocalTransport.DecodedFilename> contentsByKey(File paramFile)
  {
    paramFile = paramFile.listFiles();
    if ((paramFile != null) && (paramFile.length != 0))
    {
      ArrayList localArrayList = new ArrayList();
      int i = paramFile.length;
      for (int j = 0; j < i; j++) {
        localArrayList.add(new LocalTransport.DecodedFilename(paramFile[j]));
      }
      Collections.sort(localArrayList);
      return localArrayList;
    }
    return null;
  }
  
  private void deleteContents(File paramFile)
  {
    File[] arrayOfFile = paramFile.listFiles();
    if (arrayOfFile != null)
    {
      int i = arrayOfFile.length;
      for (int j = 0; j < i; j++)
      {
        paramFile = arrayOfFile[j];
        if (paramFile.isDirectory()) {
          deleteContents(paramFile);
        }
        paramFile.delete();
      }
    }
  }
  
  private void makeDir()
  {
    boolean bool1 = mCurrentSetDir.mkdirs();
    boolean bool2 = mCurrentSetFullDir.mkdir();
    boolean bool3 = mCurrentSetIncrementalDir.mkdir();
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("1:");
    localStringBuilder.append(bool1);
    localStringBuilder.append(" 2:");
    localStringBuilder.append(bool2);
    localStringBuilder.append(" 3:");
    localStringBuilder.append(bool3);
    Log.i("Steve", localStringBuilder.toString());
  }
  
  private void refreshAvailableStorageSize()
  {
    long l = new StatFs(Environment.getDownloadCacheDirectory().getPath()).getAvailableBytes();
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Available storage size in cache is : ");
    localStringBuilder.append(l);
    localStringBuilder.append(" bytes");
    Log.d("AsusTransport", localStringBuilder.toString());
    FULL_BACKUP_SIZE_QUOTA = l;
  }
  
  private void resetFullRestoreState()
  {
    IoUtils.closeQuietly(mCurFullRestoreStream);
    mCurFullRestoreStream = null;
    mFullRestoreSocketStream = null;
    mFullRestoreBuffer = null;
  }
  
  private File tarballFile(String paramString)
  {
    return new File(mCurrentSetFullDir, paramString);
  }
  
  /* Error */
  private int tearDownFullBackup()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 274	com/android/internal/backup/AsusTransport:mSocket	Landroid/os/ParcelFileDescriptor;
    //   4: ifnull +95 -> 99
    //   7: aload_0
    //   8: getfield 276	com/android/internal/backup/AsusTransport:mFullBackupOutputStream	Ljava/io/BufferedOutputStream;
    //   11: ifnull +17 -> 28
    //   14: aload_0
    //   15: getfield 276	com/android/internal/backup/AsusTransport:mFullBackupOutputStream	Ljava/io/BufferedOutputStream;
    //   18: invokevirtual 281	java/io/BufferedOutputStream:flush	()V
    //   21: aload_0
    //   22: getfield 276	com/android/internal/backup/AsusTransport:mFullBackupOutputStream	Ljava/io/BufferedOutputStream;
    //   25: invokevirtual 284	java/io/BufferedOutputStream:close	()V
    //   28: aload_0
    //   29: aconst_null
    //   30: putfield 286	com/android/internal/backup/AsusTransport:mSocketInputStream	Ljava/io/FileInputStream;
    //   33: aload_0
    //   34: aconst_null
    //   35: putfield 288	com/android/internal/backup/AsusTransport:mFullTargetPackage	Ljava/lang/String;
    //   38: aload_0
    //   39: getfield 274	com/android/internal/backup/AsusTransport:mSocket	Landroid/os/ParcelFileDescriptor;
    //   42: invokevirtual 291	android/os/ParcelFileDescriptor:close	()V
    //   45: aload_0
    //   46: aconst_null
    //   47: putfield 274	com/android/internal/backup/AsusTransport:mSocket	Landroid/os/ParcelFileDescriptor;
    //   50: aload_0
    //   51: aconst_null
    //   52: putfield 276	com/android/internal/backup/AsusTransport:mFullBackupOutputStream	Ljava/io/BufferedOutputStream;
    //   55: goto +44 -> 99
    //   58: astore_1
    //   59: goto +28 -> 87
    //   62: astore_1
    //   63: ldc 29
    //   65: ldc_w 293
    //   68: aload_1
    //   69: invokestatic 297	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   72: pop
    //   73: aload_0
    //   74: aconst_null
    //   75: putfield 274	com/android/internal/backup/AsusTransport:mSocket	Landroid/os/ParcelFileDescriptor;
    //   78: aload_0
    //   79: aconst_null
    //   80: putfield 276	com/android/internal/backup/AsusTransport:mFullBackupOutputStream	Ljava/io/BufferedOutputStream;
    //   83: sipush 64536
    //   86: ireturn
    //   87: aload_0
    //   88: aconst_null
    //   89: putfield 274	com/android/internal/backup/AsusTransport:mSocket	Landroid/os/ParcelFileDescriptor;
    //   92: aload_0
    //   93: aconst_null
    //   94: putfield 276	com/android/internal/backup/AsusTransport:mFullBackupOutputStream	Ljava/io/BufferedOutputStream;
    //   97: aload_1
    //   98: athrow
    //   99: iconst_0
    //   100: ireturn
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	101	0	this	AsusTransport
    //   58	1	1	localObject	Object
    //   62	36	1	localIOException	IOException
    // Exception table:
    //   from	to	target	type
    //   7	28	58	finally
    //   28	45	58	finally
    //   63	73	58	finally
    //   7	28	62	java/io/IOException
    //   28	45	62	java/io/IOException
  }
  
  public int abortFullRestore()
  {
    if (mRestoreType == 2)
    {
      resetFullRestoreState();
      mRestoreType = 0;
      return 0;
    }
    throw new IllegalStateException("abortFullRestore() but not currently restoring");
  }
  
  public void cancelFullBackup()
  {
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Canceling full backup of ");
    ((StringBuilder)localObject).append(mFullTargetPackage);
    Log.i("AsusTransport", ((StringBuilder)localObject).toString());
    localObject = tarballFile(mFullTargetPackage);
    tearDownFullBackup();
    if (((File)localObject).exists()) {
      ((File)localObject).delete();
    }
  }
  
  public int checkFullBackupSize(long paramLong)
  {
    refreshAvailableStorageSize();
    int i = 0;
    if (paramLong <= 0L) {
      i = 64534;
    } else if (paramLong > FULL_BACKUP_SIZE_QUOTA) {
      i = 64531;
    }
    if (i != 0)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Declining backup of size ");
      localStringBuilder.append(paramLong);
      Log.v("AsusTransport", localStringBuilder.toString());
    }
    return i;
  }
  
  public int clearBackupData(PackageInfo paramPackageInfo)
  {
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("clearBackupData() pkg=");
    ((StringBuilder)localObject).append(packageName);
    Log.v("AsusTransport", ((StringBuilder)localObject).toString());
    localObject = new File(mCurrentSetIncrementalDir, packageName);
    File[] arrayOfFile = ((File)localObject).listFiles();
    int i;
    int j;
    if (arrayOfFile != null)
    {
      i = arrayOfFile.length;
      for (j = 0; j < i; j++) {
        arrayOfFile[j].delete();
      }
      ((File)localObject).delete();
    }
    paramPackageInfo = new File(mCurrentSetFullDir, packageName);
    localObject = paramPackageInfo.listFiles();
    if (localObject != null)
    {
      i = localObject.length;
      for (j = 0; j < i; j++) {
        localObject[j].delete();
      }
      paramPackageInfo.delete();
    }
    return 0;
  }
  
  public Intent configurationIntent()
  {
    return null;
  }
  
  public String currentDestinationString()
  {
    return "Transfer data to new ASUS device";
  }
  
  public Intent dataManagementIntent()
  {
    return null;
  }
  
  public String dataManagementLabel()
  {
    return "";
  }
  
  public int finishBackup()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("finishBackup() of ");
    localStringBuilder.append(mFullTargetPackage);
    Log.v("AsusTransport", localStringBuilder.toString());
    chmodALLFile();
    return tearDownFullBackup();
  }
  
  public void finishRestore()
  {
    Log.v("AsusTransport", "finishRestore()");
    if (mRestoreType == 2) {
      resetFullRestoreState();
    }
    mRestoreType = 0;
  }
  
  public RestoreSet[] getAvailableRestoreSets()
  {
    long[] arrayOfLong = new long[POSSIBLE_SETS.length + 1];
    Object localObject = POSSIBLE_SETS;
    int i = localObject.length;
    int j = 0;
    int k = 0;
    int m = 0;
    while (m < i)
    {
      long l = localObject[m];
      int n = k;
      if (new File(mDataDir, Long.toString(l)).exists())
      {
        arrayOfLong[k] = l;
        n = k + 1;
      }
      m++;
      k = n;
    }
    arrayOfLong[k] = 5566L;
    localObject = new RestoreSet[k + 1];
    for (m = j; m < localObject.length; m++) {
      localObject[m] = new RestoreSet("Local disk image", "flash", arrayOfLong[m]);
    }
    return localObject;
  }
  
  public long getBackupQuota(String paramString, boolean paramBoolean)
  {
    refreshAvailableStorageSize();
    long l;
    if (paramBoolean) {
      l = FULL_BACKUP_SIZE_QUOTA;
    } else {
      l = Long.MAX_VALUE;
    }
    return l;
  }
  
  public long getCurrentRestoreSet()
  {
    return 5566L;
  }
  
  /* Error */
  public int getNextFullRestoreDataChunk(ParcelFileDescriptor paramParcelFileDescriptor)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 300	com/android/internal/backup/AsusTransport:mRestoreType	I
    //   4: iconst_2
    //   5: if_icmpne +250 -> 255
    //   8: aload_0
    //   9: getfield 258	com/android/internal/backup/AsusTransport:mCurFullRestoreStream	Ljava/io/FileInputStream;
    //   12: ifnonnull +141 -> 153
    //   15: aload_0
    //   16: getfield 125	com/android/internal/backup/AsusTransport:mRestorePackages	[Landroid/content/pm/PackageInfo;
    //   19: aload_0
    //   20: getfield 127	com/android/internal/backup/AsusTransport:mRestorePackage	I
    //   23: aaload
    //   24: getfield 332	android/content/pm/PackageInfo:packageName	Ljava/lang/String;
    //   27: astore_2
    //   28: new 141	java/lang/StringBuilder
    //   31: dup
    //   32: invokespecial 142	java/lang/StringBuilder:<init>	()V
    //   35: astore_3
    //   36: aload_3
    //   37: ldc_w 365
    //   40: invokevirtual 148	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   43: pop
    //   44: aload_3
    //   45: aload_2
    //   46: invokevirtual 148	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   49: pop
    //   50: ldc 29
    //   52: aload_3
    //   53: invokevirtual 154	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   56: invokestatic 168	android/util/Log:i	(Ljava/lang/String;Ljava/lang/String;)I
    //   59: pop
    //   60: new 101	java/io/File
    //   63: dup
    //   64: aload_0
    //   65: getfield 367	com/android/internal/backup/AsusTransport:mRestoreSetFullDir	Ljava/io/File;
    //   68: aload_2
    //   69: invokespecial 117	java/io/File:<init>	(Ljava/io/File;Ljava/lang/String;)V
    //   72: astore 4
    //   74: new 369	java/io/FileInputStream
    //   77: astore_3
    //   78: aload_3
    //   79: aload 4
    //   81: invokespecial 370	java/io/FileInputStream:<init>	(Ljava/io/File;)V
    //   84: aload_0
    //   85: aload_3
    //   86: putfield 258	com/android/internal/backup/AsusTransport:mCurFullRestoreStream	Ljava/io/FileInputStream;
    //   89: aload_0
    //   90: new 372	java/io/FileOutputStream
    //   93: dup
    //   94: aload_1
    //   95: invokevirtual 376	android/os/ParcelFileDescriptor:getFileDescriptor	()Ljava/io/FileDescriptor;
    //   98: invokespecial 379	java/io/FileOutputStream:<init>	(Ljava/io/FileDescriptor;)V
    //   101: putfield 266	com/android/internal/backup/AsusTransport:mFullRestoreSocketStream	Ljava/io/FileOutputStream;
    //   104: aload_0
    //   105: sipush 2048
    //   108: newarray byte
    //   110: putfield 268	com/android/internal/backup/AsusTransport:mFullRestoreBuffer	[B
    //   113: goto +40 -> 153
    //   116: astore_1
    //   117: new 141	java/lang/StringBuilder
    //   120: dup
    //   121: invokespecial 142	java/lang/StringBuilder:<init>	()V
    //   124: astore_1
    //   125: aload_1
    //   126: ldc_w 381
    //   129: invokevirtual 148	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   132: pop
    //   133: aload_1
    //   134: aload_2
    //   135: invokevirtual 148	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   138: pop
    //   139: ldc 29
    //   141: aload_1
    //   142: invokevirtual 154	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   145: invokestatic 384	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   148: pop
    //   149: sipush 64534
    //   152: ireturn
    //   153: aload_0
    //   154: getfield 258	com/android/internal/backup/AsusTransport:mCurFullRestoreStream	Ljava/io/FileInputStream;
    //   157: aload_0
    //   158: getfield 268	com/android/internal/backup/AsusTransport:mFullRestoreBuffer	[B
    //   161: invokevirtual 388	java/io/FileInputStream:read	([B)I
    //   164: istore 5
    //   166: iload 5
    //   168: ifge +9 -> 177
    //   171: iconst_m1
    //   172: istore 5
    //   174: goto +70 -> 244
    //   177: iload 5
    //   179: ifne +18 -> 197
    //   182: ldc 29
    //   184: ldc_w 390
    //   187: invokestatic 392	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   190: pop
    //   191: iconst_m1
    //   192: istore 5
    //   194: goto +50 -> 244
    //   197: new 141	java/lang/StringBuilder
    //   200: astore_1
    //   201: aload_1
    //   202: invokespecial 142	java/lang/StringBuilder:<init>	()V
    //   205: aload_1
    //   206: ldc_w 394
    //   209: invokevirtual 148	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   212: pop
    //   213: aload_1
    //   214: iload 5
    //   216: invokevirtual 397	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   219: pop
    //   220: ldc 29
    //   222: aload_1
    //   223: invokevirtual 154	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   226: invokestatic 168	android/util/Log:i	(Ljava/lang/String;Ljava/lang/String;)I
    //   229: pop
    //   230: aload_0
    //   231: getfield 266	com/android/internal/backup/AsusTransport:mFullRestoreSocketStream	Ljava/io/FileOutputStream;
    //   234: aload_0
    //   235: getfield 268	com/android/internal/backup/AsusTransport:mFullRestoreBuffer	[B
    //   238: iconst_0
    //   239: iload 5
    //   241: invokevirtual 401	java/io/FileOutputStream:write	([BII)V
    //   244: iload 5
    //   246: ireturn
    //   247: astore_1
    //   248: aload_1
    //   249: athrow
    //   250: astore_1
    //   251: sipush 64536
    //   254: ireturn
    //   255: new 304	java/lang/IllegalStateException
    //   258: dup
    //   259: ldc_w 403
    //   262: invokespecial 307	java/lang/IllegalStateException:<init>	(Ljava/lang/String;)V
    //   265: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	266	0	this	AsusTransport
    //   0	266	1	paramParcelFileDescriptor	ParcelFileDescriptor
    //   27	108	2	str	String
    //   35	51	3	localObject	Object
    //   72	8	4	localFile	File
    //   164	81	5	i	int
    // Exception table:
    //   from	to	target	type
    //   74	89	116	java/io/IOException
    //   153	166	247	finally
    //   182	191	247	finally
    //   197	244	247	finally
    //   153	166	250	java/io/IOException
    //   182	191	250	java/io/IOException
    //   197	244	250	java/io/IOException
  }
  
  public int getRestoreData(ParcelFileDescriptor paramParcelFileDescriptor)
  {
    if (mRestorePackages != null)
    {
      if (mRestorePackage >= 0) {
        if (mRestoreType == 1)
        {
          Object localObject1 = new File(mRestoreSetIncrementalDir, mRestorePackages[mRestorePackage].packageName);
          Object localObject2 = contentsByKey((File)localObject1);
          if (localObject2 == null)
          {
            paramParcelFileDescriptor = new StringBuilder();
            paramParcelFileDescriptor.append("No keys for package: ");
            paramParcelFileDescriptor.append(localObject1);
            Log.e("AsusTransport", paramParcelFileDescriptor.toString());
            return 64536;
          }
          localObject1 = new StringBuilder();
          ((StringBuilder)localObject1).append("  getRestoreData() found ");
          ((StringBuilder)localObject1).append(((ArrayList)localObject2).size());
          ((StringBuilder)localObject1).append(" key files");
          Log.v("AsusTransport", ((StringBuilder)localObject1).toString());
          paramParcelFileDescriptor = new BackupDataOutput(paramParcelFileDescriptor.getFileDescriptor());
          try
          {
            Iterator localIterator = ((ArrayList)localObject2).iterator();
            for (;;)
            {
              Object localObject3;
              if (localIterator.hasNext())
              {
                localObject1 = (LocalTransport.DecodedFilename)localIterator.next();
                localObject3 = file;
                localObject2 = new java/io/FileInputStream;
                ((FileInputStream)localObject2).<init>((File)localObject3);
              }
              try
              {
                int i = (int)((File)localObject3).length();
                byte[] arrayOfByte = new byte[i];
                ((FileInputStream)localObject2).read(arrayOfByte);
                localObject3 = new java/lang/StringBuilder;
                ((StringBuilder)localObject3).<init>();
                ((StringBuilder)localObject3).append("    ... key=");
                ((StringBuilder)localObject3).append(key);
                ((StringBuilder)localObject3).append(" size=");
                ((StringBuilder)localObject3).append(i);
                Log.v("AsusTransport", ((StringBuilder)localObject3).toString());
                paramParcelFileDescriptor.writeEntityHeader(key, i);
                paramParcelFileDescriptor.writeEntityData(arrayOfByte, i);
                ((FileInputStream)localObject2).close();
              }
              finally
              {
                ((FileInputStream)localObject2).close();
              }
            }
            throw new IllegalStateException("getRestoreData(fd) for non-key/value dataset");
          }
          catch (IOException paramParcelFileDescriptor)
          {
            Log.e("AsusTransport", "Unable to read backup records", paramParcelFileDescriptor);
            return 64536;
          }
        }
      }
      throw new IllegalStateException("nextRestorePackage not called");
    }
    throw new IllegalStateException("startRestore not called");
  }
  
  public int initializeDevice()
  {
    Log.v("AsusTransport", "wiping all data");
    deleteContents(mCurrentSetDir);
    makeDir();
    return 0;
  }
  
  public String name()
  {
    return new ComponentName(mContext, getClass()).flattenToShortString();
  }
  
  public RestoreDescription nextRestorePackage()
  {
    Object localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append("nextRestorePackage() : mRestorePackage=");
    ((StringBuilder)localObject1).append(mRestorePackage);
    ((StringBuilder)localObject1).append(" length=");
    ((StringBuilder)localObject1).append(mRestorePackages.length);
    Log.v("AsusTransport", ((StringBuilder)localObject1).toString());
    if (mRestorePackages != null)
    {
      int i = 0;
      for (;;)
      {
        int j = mRestorePackage + 1;
        mRestorePackage = j;
        if (j >= mRestorePackages.length) {
          break;
        }
        localObject1 = mRestorePackages[mRestorePackage].packageName;
        Object localObject2 = new File(mRestoreSetIncrementalDir, (String)localObject1).list();
        j = i;
        if (localObject2 != null)
        {
          j = i;
          if (localObject2.length > 0)
          {
            localObject2 = new StringBuilder();
            ((StringBuilder)localObject2).append("  nextRestorePackage(TYPE_KEY_VALUE) @ ");
            ((StringBuilder)localObject2).append(mRestorePackage);
            ((StringBuilder)localObject2).append(" = ");
            ((StringBuilder)localObject2).append((String)localObject1);
            Log.v("AsusTransport", ((StringBuilder)localObject2).toString());
            mRestoreType = 1;
            j = 1;
          }
        }
        i = j;
        if (j == 0)
        {
          i = j;
          if (new File(mRestoreSetFullDir, (String)localObject1).length() > 0L)
          {
            localObject2 = new StringBuilder();
            ((StringBuilder)localObject2).append("  nextRestorePackage(TYPE_FULL_STREAM) @ ");
            ((StringBuilder)localObject2).append(mRestorePackage);
            ((StringBuilder)localObject2).append(" = ");
            ((StringBuilder)localObject2).append((String)localObject1);
            Log.v("AsusTransport", ((StringBuilder)localObject2).toString());
            mRestoreType = 2;
            mCurFullRestoreStream = null;
            i = 1;
          }
        }
        if (i != 0) {
          return new RestoreDescription((String)localObject1, mRestoreType);
        }
        localObject2 = new StringBuilder();
        ((StringBuilder)localObject2).append("  ... package @ ");
        ((StringBuilder)localObject2).append(mRestorePackage);
        ((StringBuilder)localObject2).append(" = ");
        ((StringBuilder)localObject2).append((String)localObject1);
        ((StringBuilder)localObject2).append(" has no data; skipping");
        Log.v("AsusTransport", ((StringBuilder)localObject2).toString());
      }
      Log.v("AsusTransport", "  no more packages to restore");
      return RestoreDescription.NO_MORE_PACKAGES;
    }
    throw new IllegalStateException("startRestore not called");
  }
  
  /* Error */
  public int performBackup(PackageInfo paramPackageInfo, ParcelFileDescriptor paramParcelFileDescriptor)
  {
    // Byte code:
    //   0: aload_2
    //   1: invokevirtual 376	android/os/ParcelFileDescriptor:getFileDescriptor	()Ljava/io/FileDescriptor;
    //   4: invokestatic 527	android/system/Os:fstat	(Ljava/io/FileDescriptor;)Landroid/system/StructStat;
    //   7: astore_3
    //   8: new 141	java/lang/StringBuilder
    //   11: astore 4
    //   13: aload 4
    //   15: invokespecial 142	java/lang/StringBuilder:<init>	()V
    //   18: aload 4
    //   20: ldc_w 529
    //   23: invokevirtual 148	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   26: pop
    //   27: aload 4
    //   29: aload_1
    //   30: getfield 332	android/content/pm/PackageInfo:packageName	Ljava/lang/String;
    //   33: invokevirtual 148	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   36: pop
    //   37: aload 4
    //   39: ldc_w 449
    //   42: invokevirtual 148	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   45: pop
    //   46: aload 4
    //   48: aload_3
    //   49: getfield 534	android/system/StructStat:st_size	J
    //   52: invokevirtual 250	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
    //   55: pop
    //   56: ldc 29
    //   58: aload 4
    //   60: invokevirtual 154	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   63: invokestatic 323	android/util/Log:v	(Ljava/lang/String;Ljava/lang/String;)I
    //   66: pop
    //   67: goto +44 -> 111
    //   70: astore 4
    //   72: new 141	java/lang/StringBuilder
    //   75: dup
    //   76: invokespecial 142	java/lang/StringBuilder:<init>	()V
    //   79: astore 4
    //   81: aload 4
    //   83: ldc_w 536
    //   86: invokevirtual 148	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   89: pop
    //   90: aload 4
    //   92: aload_1
    //   93: getfield 332	android/content/pm/PackageInfo:packageName	Ljava/lang/String;
    //   96: invokevirtual 148	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   99: pop
    //   100: ldc 29
    //   102: aload 4
    //   104: invokevirtual 154	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   107: invokestatic 392	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   110: pop
    //   111: new 101	java/io/File
    //   114: dup
    //   115: aload_0
    //   116: getfield 121	com/android/internal/backup/AsusTransport:mCurrentSetIncrementalDir	Ljava/io/File;
    //   119: aload_1
    //   120: getfield 332	android/content/pm/PackageInfo:packageName	Ljava/lang/String;
    //   123: invokespecial 117	java/io/File:<init>	(Ljava/io/File;Ljava/lang/String;)V
    //   126: astore 5
    //   128: aload 5
    //   130: invokevirtual 214	java/io/File:mkdirs	()Z
    //   133: pop
    //   134: new 538	android/app/backup/BackupDataInput
    //   137: dup
    //   138: aload_2
    //   139: invokevirtual 376	android/os/ParcelFileDescriptor:getFileDescriptor	()Ljava/io/FileDescriptor;
    //   142: invokespecial 539	android/app/backup/BackupDataInput:<init>	(Ljava/io/FileDescriptor;)V
    //   145: astore_3
    //   146: sipush 512
    //   149: istore 6
    //   151: sipush 512
    //   154: newarray byte
    //   156: astore 4
    //   158: aload_3
    //   159: invokevirtual 542	android/app/backup/BackupDataInput:readNextHeader	()Z
    //   162: ifeq +365 -> 527
    //   165: aload_3
    //   166: invokevirtual 545	android/app/backup/BackupDataInput:getKey	()Ljava/lang/String;
    //   169: astore 7
    //   171: new 547	java/lang/String
    //   174: astore 8
    //   176: aload 8
    //   178: aload 7
    //   180: invokevirtual 551	java/lang/String:getBytes	()[B
    //   183: invokestatic 557	com/android/org/bouncycastle/util/encoders/Base64:encode	([B)[B
    //   186: invokespecial 560	java/lang/String:<init>	([B)V
    //   189: new 101	java/io/File
    //   192: astore 9
    //   194: aload 9
    //   196: aload 5
    //   198: aload 8
    //   200: invokespecial 117	java/io/File:<init>	(Ljava/io/File;Ljava/lang/String;)V
    //   203: aload_3
    //   204: invokevirtual 563	android/app/backup/BackupDataInput:getDataSize	()I
    //   207: istore 10
    //   209: new 141	java/lang/StringBuilder
    //   212: astore 11
    //   214: aload 11
    //   216: invokespecial 142	java/lang/StringBuilder:<init>	()V
    //   219: aload 11
    //   221: ldc_w 565
    //   224: invokevirtual 148	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   227: pop
    //   228: aload 11
    //   230: aload 7
    //   232: invokevirtual 148	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   235: pop
    //   236: aload 11
    //   238: ldc_w 449
    //   241: invokevirtual 148	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   244: pop
    //   245: aload 11
    //   247: iload 10
    //   249: invokevirtual 397	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   252: pop
    //   253: aload 11
    //   255: ldc_w 567
    //   258: invokevirtual 148	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   261: pop
    //   262: aload 11
    //   264: aload 8
    //   266: invokevirtual 148	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   269: pop
    //   270: ldc 29
    //   272: aload 11
    //   274: invokevirtual 154	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   277: invokestatic 323	android/util/Log:v	(Ljava/lang/String;Ljava/lang/String;)I
    //   280: pop
    //   281: iload 10
    //   283: iflt +235 -> 518
    //   286: aload 9
    //   288: invokevirtual 133	java/io/File:exists	()Z
    //   291: ifeq +9 -> 300
    //   294: aload 9
    //   296: invokevirtual 211	java/io/File:delete	()Z
    //   299: pop
    //   300: new 372	java/io/FileOutputStream
    //   303: astore 8
    //   305: aload 8
    //   307: aload 9
    //   309: invokespecial 568	java/io/FileOutputStream:<init>	(Ljava/io/File;)V
    //   312: iload 6
    //   314: istore 12
    //   316: iload 10
    //   318: iload 6
    //   320: if_icmple +13 -> 333
    //   323: iload 10
    //   325: istore 12
    //   327: iload 12
    //   329: newarray byte
    //   331: astore 4
    //   333: aload_3
    //   334: aload 4
    //   336: iconst_0
    //   337: iload 10
    //   339: invokevirtual 572	android/app/backup/BackupDataInput:readEntityData	([BII)I
    //   342: pop
    //   343: aload_2
    //   344: invokevirtual 376	android/os/ParcelFileDescriptor:getFileDescriptor	()Ljava/io/FileDescriptor;
    //   347: lconst_0
    //   348: getstatic 577	android/system/OsConstants:SEEK_CUR	I
    //   351: invokestatic 581	android/system/Os:lseek	(Ljava/io/FileDescriptor;JI)J
    //   354: lstore 13
    //   356: new 141	java/lang/StringBuilder
    //   359: astore 11
    //   361: aload 11
    //   363: invokespecial 142	java/lang/StringBuilder:<init>	()V
    //   366: aload 11
    //   368: ldc_w 583
    //   371: invokevirtual 148	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   374: pop
    //   375: aload 11
    //   377: lload 13
    //   379: invokevirtual 250	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
    //   382: pop
    //   383: ldc 29
    //   385: aload 11
    //   387: invokevirtual 154	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   390: invokestatic 323	android/util/Log:v	(Ljava/lang/String;Ljava/lang/String;)I
    //   393: pop
    //   394: goto +45 -> 439
    //   397: astore 11
    //   399: new 141	java/lang/StringBuilder
    //   402: astore 11
    //   404: aload 11
    //   406: invokespecial 142	java/lang/StringBuilder:<init>	()V
    //   409: aload 11
    //   411: ldc_w 536
    //   414: invokevirtual 148	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   417: pop
    //   418: aload 11
    //   420: aload_1
    //   421: getfield 332	android/content/pm/PackageInfo:packageName	Ljava/lang/String;
    //   424: invokevirtual 148	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   427: pop
    //   428: ldc 29
    //   430: aload 11
    //   432: invokevirtual 154	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   435: invokestatic 392	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   438: pop
    //   439: aload 8
    //   441: aload 4
    //   443: iconst_0
    //   444: iload 10
    //   446: invokevirtual 401	java/io/FileOutputStream:write	([BII)V
    //   449: aload 8
    //   451: invokevirtual 584	java/io/FileOutputStream:close	()V
    //   454: iload 12
    //   456: istore 6
    //   458: goto +66 -> 524
    //   461: astore_1
    //   462: goto +49 -> 511
    //   465: astore_1
    //   466: new 141	java/lang/StringBuilder
    //   469: astore_1
    //   470: aload_1
    //   471: invokespecial 142	java/lang/StringBuilder:<init>	()V
    //   474: aload_1
    //   475: ldc_w 586
    //   478: invokevirtual 148	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   481: pop
    //   482: aload_1
    //   483: aload 9
    //   485: invokevirtual 589	java/io/File:getAbsolutePath	()Ljava/lang/String;
    //   488: invokevirtual 148	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   491: pop
    //   492: ldc 29
    //   494: aload_1
    //   495: invokevirtual 154	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   498: invokestatic 384	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   501: pop
    //   502: aload 8
    //   504: invokevirtual 584	java/io/FileOutputStream:close	()V
    //   507: sipush 64536
    //   510: ireturn
    //   511: aload 8
    //   513: invokevirtual 584	java/io/FileOutputStream:close	()V
    //   516: aload_1
    //   517: athrow
    //   518: aload 9
    //   520: invokevirtual 211	java/io/File:delete	()Z
    //   523: pop
    //   524: goto -366 -> 158
    //   527: iconst_0
    //   528: ireturn
    //   529: astore_1
    //   530: ldc 29
    //   532: ldc_w 591
    //   535: aload_1
    //   536: invokestatic 593	android/util/Log:v	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   539: pop
    //   540: sipush 64536
    //   543: ireturn
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	544	0	this	AsusTransport
    //   0	544	1	paramPackageInfo	PackageInfo
    //   0	544	2	paramParcelFileDescriptor	ParcelFileDescriptor
    //   7	327	3	localObject1	Object
    //   11	48	4	localStringBuilder1	StringBuilder
    //   70	1	4	localErrnoException1	android.system.ErrnoException
    //   79	363	4	localObject2	Object
    //   126	71	5	localFile1	File
    //   149	308	6	i	int
    //   169	62	7	str	String
    //   174	338	8	localObject3	Object
    //   192	327	9	localFile2	File
    //   207	238	10	j	int
    //   212	174	11	localStringBuilder2	StringBuilder
    //   397	1	11	localErrnoException2	android.system.ErrnoException
    //   402	29	11	localStringBuilder3	StringBuilder
    //   314	141	12	k	int
    //   354	24	13	l	long
    // Exception table:
    //   from	to	target	type
    //   0	67	70	android/system/ErrnoException
    //   343	394	397	android/system/ErrnoException
    //   439	449	461	finally
    //   466	502	461	finally
    //   439	449	465	java/io/IOException
    //   151	158	529	java/io/IOException
    //   158	281	529	java/io/IOException
    //   286	300	529	java/io/IOException
    //   300	312	529	java/io/IOException
    //   327	333	529	java/io/IOException
    //   333	343	529	java/io/IOException
    //   343	394	529	java/io/IOException
    //   399	439	529	java/io/IOException
    //   449	454	529	java/io/IOException
    //   502	507	529	java/io/IOException
    //   511	518	529	java/io/IOException
    //   518	524	529	java/io/IOException
  }
  
  public int performFullBackup(PackageInfo paramPackageInfo, ParcelFileDescriptor paramParcelFileDescriptor)
  {
    refreshAvailableStorageSize();
    if (mSocket != null)
    {
      Log.e("AsusTransport", "Attempt to initiate full backup while one is in progress");
      return 64536;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("performFullBackup : ");
    localStringBuilder.append(paramPackageInfo);
    Log.i("AsusTransport", localStringBuilder.toString());
    try
    {
      mFullBackupSize = 0L;
      mSocket = ParcelFileDescriptor.dup(paramParcelFileDescriptor.getFileDescriptor());
      paramParcelFileDescriptor = new java/io/FileInputStream;
      paramParcelFileDescriptor.<init>(mSocket.getFileDescriptor());
      mSocketInputStream = paramParcelFileDescriptor;
      mFullTargetPackage = packageName;
      mFullBackupBuffer = new byte['က'];
      return 0;
    }
    catch (IOException paramPackageInfo)
    {
      Log.e("AsusTransport", "Unable to process socket for full backup");
    }
    return 64536;
  }
  
  public long requestBackupTime()
  {
    return 0L;
  }
  
  public long requestFullBackupTime()
  {
    return 0L;
  }
  
  public int sendBackupData(int paramInt)
  {
    if (mSocket == null)
    {
      Log.w("AsusTransport", "Attempted sendBackupData before performFullBackup");
      return 64536;
    }
    mFullBackupSize += paramInt;
    if (mFullBackupSize > FULL_BACKUP_SIZE_QUOTA) {
      return 64531;
    }
    if (paramInt > mFullBackupBuffer.length) {
      mFullBackupBuffer = new byte[paramInt];
    }
    if (mFullBackupOutputStream == null) {
      try
      {
        FileOutputStream localFileOutputStream = new FileOutputStream(tarballFile(mFullTargetPackage));
        mFullBackupOutputStream = new BufferedOutputStream(localFileOutputStream);
      }
      catch (FileNotFoundException localFileNotFoundException)
      {
        return 64536;
      }
    }
    int i = paramInt;
    while (i > 0) {
      try
      {
        int j = mSocketInputStream.read(mFullBackupBuffer, 0, i);
        if (j < 0)
        {
          Log.w("AsusTransport", "Unexpected EOD; failing backup");
          return 64536;
        }
        mFullBackupOutputStream.write(mFullBackupBuffer, 0, j);
        i -= j;
      }
      catch (IOException localIOException)
      {
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("Error handling backup data for ");
        localStringBuilder.append(mFullTargetPackage);
        Log.e("AsusTransport", localStringBuilder.toString());
        return 64536;
      }
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("   stored ");
    localStringBuilder.append(paramInt);
    localStringBuilder.append(" of data");
    Log.v("AsusTransport", localStringBuilder.toString());
    return 0;
  }
  
  public int setAsusFileDestination(String paramString)
  {
    Object localObject = new File(paramString);
    if ((!((File)localObject).exists()) && (!((File)localObject).mkdirs())) {
      return 0;
    }
    mDataDir = ((File)localObject);
    mCurrentSetDir = new File(mDataDir, Long.toString(5566L));
    mCurrentSetIncrementalDir = new File(mCurrentSetDir, "_delta");
    mCurrentSetFullDir = new File(mCurrentSetDir, "_full");
    makeDir();
    chmodALLFile();
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("change AsusTransport file destination to ");
    ((StringBuilder)localObject).append(paramString);
    Log.i("AsusTransport", ((StringBuilder)localObject).toString());
    return 1;
  }
  
  public int startRestore(long paramLong, PackageInfo[] paramArrayOfPackageInfo)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("start restore ");
    localStringBuilder.append(paramLong);
    localStringBuilder.append(" : ");
    localStringBuilder.append(paramArrayOfPackageInfo.length);
    localStringBuilder.append(" matching packages");
    Log.v("AsusTransport", localStringBuilder.toString());
    mRestorePackages = paramArrayOfPackageInfo;
    mRestorePackage = -1;
    mRestoreSetDir = new File(mDataDir, Long.toString(paramLong));
    mRestoreSetIncrementalDir = new File(mRestoreSetDir, "_delta");
    mRestoreSetFullDir = new File(mRestoreSetDir, "_full");
    return 0;
  }
  
  public String transportDirName()
  {
    return "com.android.internal.backup.AsusTransport";
  }
  
  static class DecodedFilename
    implements Comparable<LocalTransport.DecodedFilename>
  {
    public File file;
    public String key;
    
    public DecodedFilename(File paramFile)
    {
      file = paramFile;
      key = new String(Base64.decode(paramFile.getName()));
    }
    
    public int compareTo(LocalTransport.DecodedFilename paramDecodedFilename)
    {
      return key.compareTo(key);
    }
  }
}
