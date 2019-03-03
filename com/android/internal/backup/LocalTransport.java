package com.android.internal.backup;

import android.app.backup.BackupDataInput;
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
import android.util.ArrayMap;
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

public class LocalTransport
  extends BackupTransport
{
  private static final long CURRENT_SET_TOKEN = 1L;
  private static final boolean DEBUG = false;
  private static final long FULL_BACKUP_SIZE_QUOTA = 26214400L;
  private static final String FULL_DATA_DIR = "_full";
  private static final String INCREMENTAL_DIR = "_delta";
  private static final long KEY_VALUE_BACKUP_SIZE_QUOTA = 5242880L;
  static final long[] POSSIBLE_SETS = { 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L };
  private static final String TAG = "LocalTransport";
  private static final String TRANSPORT_DATA_MANAGEMENT_LABEL = "";
  private static final String TRANSPORT_DESTINATION_STRING = "Backing up to debug-only private cache";
  private static final String TRANSPORT_DIR_NAME = "com.android.internal.backup.LocalTransport";
  private Context mContext;
  private FileInputStream mCurFullRestoreStream;
  private File mCurrentSetDir = new File(mDataDir, Long.toString(1L));
  private File mCurrentSetFullDir = new File(mCurrentSetDir, "_full");
  private File mCurrentSetIncrementalDir = new File(mCurrentSetDir, "_delta");
  private File mDataDir = new File(Environment.getDownloadCacheDirectory(), "backup");
  private byte[] mFullBackupBuffer;
  private BufferedOutputStream mFullBackupOutputStream;
  private long mFullBackupSize;
  private byte[] mFullRestoreBuffer;
  private FileOutputStream mFullRestoreSocketStream;
  private String mFullTargetPackage;
  private final LocalTransportParameters mParameters;
  private int mRestorePackage = -1;
  private PackageInfo[] mRestorePackages = null;
  private File mRestoreSetDir;
  private File mRestoreSetFullDir;
  private File mRestoreSetIncrementalDir;
  private int mRestoreType;
  private ParcelFileDescriptor mSocket;
  private FileInputStream mSocketInputStream;
  
  public LocalTransport(Context paramContext, LocalTransportParameters paramLocalTransportParameters)
  {
    mContext = paramContext;
    mParameters = paramLocalTransportParameters;
    makeDataDirs();
  }
  
  private ArrayList<DecodedFilename> contentsByKey(File paramFile)
  {
    File[] arrayOfFile = paramFile.listFiles();
    if ((arrayOfFile != null) && (arrayOfFile.length != 0))
    {
      paramFile = new ArrayList();
      int i = arrayOfFile.length;
      for (int j = 0; j < i; j++) {
        paramFile.add(new DecodedFilename(arrayOfFile[j]));
      }
      Collections.sort(paramFile);
      return paramFile;
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
  
  private void makeDataDirs()
  {
    mCurrentSetDir.mkdirs();
    mCurrentSetFullDir.mkdir();
    mCurrentSetIncrementalDir.mkdir();
  }
  
  private ArrayList<KVOperation> parseBackupStream(ParcelFileDescriptor paramParcelFileDescriptor)
    throws IOException
  {
    ArrayList localArrayList = new ArrayList();
    BackupDataInput localBackupDataInput = new BackupDataInput(paramParcelFileDescriptor.getFileDescriptor());
    while (localBackupDataInput.readNextHeader())
    {
      String str = new String(Base64.encode(localBackupDataInput.getKey().getBytes()));
      int i = localBackupDataInput.getDataSize();
      if (i >= 0) {
        paramParcelFileDescriptor = new byte[i];
      } else {
        paramParcelFileDescriptor = null;
      }
      if (i >= 0) {
        localBackupDataInput.readEntityData(paramParcelFileDescriptor, 0, i);
      }
      localArrayList.add(new KVOperation(str, paramParcelFileDescriptor));
    }
    return localArrayList;
  }
  
  private int parseKeySizes(File paramFile, ArrayMap<String, Integer> paramArrayMap)
  {
    int i = 0;
    int j = 0;
    String[] arrayOfString = paramFile.list();
    if (arrayOfString != null)
    {
      int k = arrayOfString.length;
      for (int m = 0;; m++)
      {
        i = j;
        if (m >= k) {
          break;
        }
        String str = arrayOfString[m];
        i = (int)new File(paramFile, str).length();
        j += i;
        paramArrayMap.put(str, Integer.valueOf(i));
      }
    }
    return i;
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
    //   1: getfield 269	com/android/internal/backup/LocalTransport:mSocket	Landroid/os/ParcelFileDescriptor;
    //   4: ifnull +82 -> 86
    //   7: aload_0
    //   8: getfield 271	com/android/internal/backup/LocalTransport:mFullBackupOutputStream	Ljava/io/BufferedOutputStream;
    //   11: ifnull +17 -> 28
    //   14: aload_0
    //   15: getfield 271	com/android/internal/backup/LocalTransport:mFullBackupOutputStream	Ljava/io/BufferedOutputStream;
    //   18: invokevirtual 276	java/io/BufferedOutputStream:flush	()V
    //   21: aload_0
    //   22: getfield 271	com/android/internal/backup/LocalTransport:mFullBackupOutputStream	Ljava/io/BufferedOutputStream;
    //   25: invokevirtual 279	java/io/BufferedOutputStream:close	()V
    //   28: aload_0
    //   29: aconst_null
    //   30: putfield 281	com/android/internal/backup/LocalTransport:mSocketInputStream	Ljava/io/FileInputStream;
    //   33: aload_0
    //   34: aconst_null
    //   35: putfield 283	com/android/internal/backup/LocalTransport:mFullTargetPackage	Ljava/lang/String;
    //   38: aload_0
    //   39: getfield 269	com/android/internal/backup/LocalTransport:mSocket	Landroid/os/ParcelFileDescriptor;
    //   42: invokevirtual 284	android/os/ParcelFileDescriptor:close	()V
    //   45: aload_0
    //   46: aconst_null
    //   47: putfield 269	com/android/internal/backup/LocalTransport:mSocket	Landroid/os/ParcelFileDescriptor;
    //   50: aload_0
    //   51: aconst_null
    //   52: putfield 271	com/android/internal/backup/LocalTransport:mFullBackupOutputStream	Ljava/io/BufferedOutputStream;
    //   55: goto +31 -> 86
    //   58: astore_1
    //   59: aload_0
    //   60: aconst_null
    //   61: putfield 269	com/android/internal/backup/LocalTransport:mSocket	Landroid/os/ParcelFileDescriptor;
    //   64: aload_0
    //   65: aconst_null
    //   66: putfield 271	com/android/internal/backup/LocalTransport:mFullBackupOutputStream	Ljava/io/BufferedOutputStream;
    //   69: aload_1
    //   70: athrow
    //   71: astore_1
    //   72: aload_0
    //   73: aconst_null
    //   74: putfield 269	com/android/internal/backup/LocalTransport:mSocket	Landroid/os/ParcelFileDescriptor;
    //   77: aload_0
    //   78: aconst_null
    //   79: putfield 271	com/android/internal/backup/LocalTransport:mFullBackupOutputStream	Ljava/io/BufferedOutputStream;
    //   82: sipush 64536
    //   85: ireturn
    //   86: iconst_0
    //   87: ireturn
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	88	0	this	LocalTransport
    //   58	12	1	localObject	Object
    //   71	1	1	localIOException	IOException
    // Exception table:
    //   from	to	target	type
    //   7	28	58	finally
    //   28	45	58	finally
    //   7	28	71	java/io/IOException
    //   28	45	71	java/io/IOException
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
    File localFile = tarballFile(mFullTargetPackage);
    tearDownFullBackup();
    if (localFile.exists()) {
      localFile.delete();
    }
  }
  
  public int checkFullBackupSize(long paramLong)
  {
    int i = 0;
    if (paramLong <= 0L) {
      i = 64534;
    } else if (paramLong > 26214400L) {
      i = 64531;
    }
    return i;
  }
  
  public int clearBackupData(PackageInfo paramPackageInfo)
  {
    File localFile = new File(mCurrentSetIncrementalDir, packageName);
    File[] arrayOfFile = localFile.listFiles();
    int i;
    int j;
    if (arrayOfFile != null)
    {
      i = arrayOfFile.length;
      for (j = 0; j < i; j++) {
        arrayOfFile[j].delete();
      }
      localFile.delete();
    }
    localFile = new File(mCurrentSetFullDir, packageName);
    paramPackageInfo = localFile.listFiles();
    if (paramPackageInfo != null)
    {
      i = paramPackageInfo.length;
      for (j = 0; j < i; j++) {
        paramPackageInfo[j].delete();
      }
      localFile.delete();
    }
    return 0;
  }
  
  public Intent configurationIntent()
  {
    return null;
  }
  
  public String currentDestinationString()
  {
    return "Backing up to debug-only private cache";
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
    return tearDownFullBackup();
  }
  
  public void finishRestore()
  {
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
    arrayOfLong[k] = 1L;
    localObject = new RestoreSet[k + 1];
    for (m = j; m < localObject.length; m++) {
      localObject[m] = new RestoreSet("Local disk image", "flash", arrayOfLong[m]);
    }
    return localObject;
  }
  
  public long getBackupQuota(String paramString, boolean paramBoolean)
  {
    long l;
    if (paramBoolean) {
      l = 26214400L;
    } else {
      l = 5242880L;
    }
    return l;
  }
  
  public long getCurrentRestoreSet()
  {
    return 1L;
  }
  
  /* Error */
  public int getNextFullRestoreDataChunk(ParcelFileDescriptor paramParcelFileDescriptor)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 287	com/android/internal/backup/LocalTransport:mRestoreType	I
    //   4: iconst_2
    //   5: if_icmpne +186 -> 191
    //   8: aload_0
    //   9: getfield 254	com/android/internal/backup/LocalTransport:mCurFullRestoreStream	Ljava/io/FileInputStream;
    //   12: ifnonnull +110 -> 122
    //   15: aload_0
    //   16: getfield 129	com/android/internal/backup/LocalTransport:mRestorePackages	[Landroid/content/pm/PackageInfo;
    //   19: aload_0
    //   20: getfield 131	com/android/internal/backup/LocalTransport:mRestorePackage	I
    //   23: aaload
    //   24: getfield 313	android/content/pm/PackageInfo:packageName	Ljava/lang/String;
    //   27: astore_2
    //   28: new 102	java/io/File
    //   31: dup
    //   32: aload_0
    //   33: getfield 338	com/android/internal/backup/LocalTransport:mRestoreSetFullDir	Ljava/io/File;
    //   36: aload_2
    //   37: invokespecial 113	java/io/File:<init>	(Ljava/io/File;Ljava/lang/String;)V
    //   40: astore_3
    //   41: new 340	java/io/FileInputStream
    //   44: astore 4
    //   46: aload 4
    //   48: aload_3
    //   49: invokespecial 341	java/io/FileInputStream:<init>	(Ljava/io/File;)V
    //   52: aload_0
    //   53: aload 4
    //   55: putfield 254	com/android/internal/backup/LocalTransport:mCurFullRestoreStream	Ljava/io/FileInputStream;
    //   58: aload_0
    //   59: new 343	java/io/FileOutputStream
    //   62: dup
    //   63: aload_1
    //   64: invokevirtual 190	android/os/ParcelFileDescriptor:getFileDescriptor	()Ljava/io/FileDescriptor;
    //   67: invokespecial 344	java/io/FileOutputStream:<init>	(Ljava/io/FileDescriptor;)V
    //   70: putfield 262	com/android/internal/backup/LocalTransport:mFullRestoreSocketStream	Ljava/io/FileOutputStream;
    //   73: aload_0
    //   74: sipush 2048
    //   77: newarray byte
    //   79: putfield 264	com/android/internal/backup/LocalTransport:mFullRestoreBuffer	[B
    //   82: goto +40 -> 122
    //   85: astore_1
    //   86: new 346	java/lang/StringBuilder
    //   89: dup
    //   90: invokespecial 347	java/lang/StringBuilder:<init>	()V
    //   93: astore_1
    //   94: aload_1
    //   95: ldc_w 349
    //   98: invokevirtual 353	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   101: pop
    //   102: aload_1
    //   103: aload_2
    //   104: invokevirtual 353	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   107: pop
    //   108: ldc 35
    //   110: aload_1
    //   111: invokevirtual 355	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   114: invokestatic 361	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   117: pop
    //   118: sipush 64534
    //   121: ireturn
    //   122: aload_0
    //   123: getfield 254	com/android/internal/backup/LocalTransport:mCurFullRestoreStream	Ljava/io/FileInputStream;
    //   126: aload_0
    //   127: getfield 264	com/android/internal/backup/LocalTransport:mFullRestoreBuffer	[B
    //   130: invokevirtual 365	java/io/FileInputStream:read	([B)I
    //   133: istore 5
    //   135: iload 5
    //   137: ifge +9 -> 146
    //   140: iconst_m1
    //   141: istore 5
    //   143: goto +37 -> 180
    //   146: iload 5
    //   148: ifne +18 -> 166
    //   151: ldc 35
    //   153: ldc_w 367
    //   156: invokestatic 370	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   159: pop
    //   160: iconst_m1
    //   161: istore 5
    //   163: goto +17 -> 180
    //   166: aload_0
    //   167: getfield 262	com/android/internal/backup/LocalTransport:mFullRestoreSocketStream	Ljava/io/FileOutputStream;
    //   170: aload_0
    //   171: getfield 264	com/android/internal/backup/LocalTransport:mFullRestoreBuffer	[B
    //   174: iconst_0
    //   175: iload 5
    //   177: invokevirtual 374	java/io/FileOutputStream:write	([BII)V
    //   180: iload 5
    //   182: ireturn
    //   183: astore_1
    //   184: aload_1
    //   185: athrow
    //   186: astore_1
    //   187: sipush 64536
    //   190: ireturn
    //   191: new 291	java/lang/IllegalStateException
    //   194: dup
    //   195: ldc_w 376
    //   198: invokespecial 296	java/lang/IllegalStateException:<init>	(Ljava/lang/String;)V
    //   201: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	202	0	this	LocalTransport
    //   0	202	1	paramParcelFileDescriptor	ParcelFileDescriptor
    //   27	77	2	str	String
    //   40	9	3	localFile	File
    //   44	10	4	localFileInputStream	FileInputStream
    //   133	48	5	i	int
    // Exception table:
    //   from	to	target	type
    //   41	58	85	java/io/IOException
    //   122	135	183	finally
    //   151	160	183	finally
    //   166	180	183	finally
    //   122	135	186	java/io/IOException
    //   151	160	186	java/io/IOException
    //   166	180	186	java/io/IOException
  }
  
  LocalTransportParameters getParameters()
  {
    return mParameters;
  }
  
  public int getRestoreData(ParcelFileDescriptor paramParcelFileDescriptor)
  {
    if (mRestorePackages != null)
    {
      if (mRestorePackage >= 0) {
        if (mRestoreType == 1)
        {
          Object localObject1 = new File(mRestoreSetIncrementalDir, mRestorePackages[mRestorePackage].packageName);
          Object localObject3 = contentsByKey((File)localObject1);
          if (localObject3 == null)
          {
            paramParcelFileDescriptor = new StringBuilder();
            paramParcelFileDescriptor.append("No keys for package: ");
            paramParcelFileDescriptor.append(localObject1);
            Log.e("LocalTransport", paramParcelFileDescriptor.toString());
            return 64536;
          }
          localObject1 = new BackupDataOutput(paramParcelFileDescriptor.getFileDescriptor());
          try
          {
            localObject3 = ((ArrayList)localObject3).iterator();
            for (;;)
            {
              DecodedFilename localDecodedFilename;
              Object localObject4;
              if (((Iterator)localObject3).hasNext())
              {
                localDecodedFilename = (DecodedFilename)((Iterator)localObject3).next();
                localObject4 = file;
                paramParcelFileDescriptor = new java/io/FileInputStream;
                paramParcelFileDescriptor.<init>((File)localObject4);
              }
              try
              {
                int i = (int)((File)localObject4).length();
                localObject4 = new byte[i];
                paramParcelFileDescriptor.read((byte[])localObject4);
                ((BackupDataOutput)localObject1).writeEntityHeader(key, i);
                ((BackupDataOutput)localObject1).writeEntityData((byte[])localObject4, i);
                paramParcelFileDescriptor.close();
              }
              finally
              {
                paramParcelFileDescriptor.close();
              }
            }
            throw new IllegalStateException("getRestoreData(fd) for non-key/value dataset");
          }
          catch (IOException paramParcelFileDescriptor)
          {
            Log.e("LocalTransport", "Unable to read backup records", paramParcelFileDescriptor);
            return 64536;
          }
        }
      }
      throw new IllegalStateException("nextRestorePackage not called");
    }
    throw new IllegalStateException("startRestore not called");
  }
  
  public int getTransportFlags()
  {
    int i = super.getTransportFlags();
    int j = i;
    if (mParameters.isFakeEncryptionFlag()) {
      j = i | 0x80000000;
    }
    return j;
  }
  
  public int initializeDevice()
  {
    deleteContents(mCurrentSetDir);
    makeDataDirs();
    return 0;
  }
  
  public String name()
  {
    return new ComponentName(mContext, getClass()).flattenToShortString();
  }
  
  public RestoreDescription nextRestorePackage()
  {
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
        String str = mRestorePackages[mRestorePackage].packageName;
        String[] arrayOfString = new File(mRestoreSetIncrementalDir, str).list();
        j = i;
        if (arrayOfString != null)
        {
          j = i;
          if (arrayOfString.length > 0)
          {
            mRestoreType = 1;
            j = 1;
          }
        }
        i = j;
        if (j == 0)
        {
          i = j;
          if (new File(mRestoreSetFullDir, str).length() > 0L)
          {
            mRestoreType = 2;
            mCurFullRestoreStream = null;
            i = 1;
          }
        }
        if (i != 0) {
          return new RestoreDescription(str, mRestoreType);
        }
      }
      return RestoreDescription.NO_MORE_PACKAGES;
    }
    throw new IllegalStateException("startRestore not called");
  }
  
  public int performBackup(PackageInfo paramPackageInfo, ParcelFileDescriptor paramParcelFileDescriptor)
  {
    return performBackup(paramPackageInfo, paramParcelFileDescriptor, 0);
  }
  
  /* Error */
  public int performBackup(PackageInfo paramPackageInfo, ParcelFileDescriptor paramParcelFileDescriptor, int paramInt)
  {
    // Byte code:
    //   0: iload_3
    //   1: iconst_2
    //   2: iand
    //   3: ifeq +9 -> 12
    //   6: iconst_1
    //   7: istore 4
    //   9: goto +6 -> 15
    //   12: iconst_0
    //   13: istore 4
    //   15: iload_3
    //   16: iconst_4
    //   17: iand
    //   18: ifeq +8 -> 26
    //   21: iconst_1
    //   22: istore_3
    //   23: goto +5 -> 28
    //   26: iconst_0
    //   27: istore_3
    //   28: iload 4
    //   30: ifeq +45 -> 75
    //   33: new 346	java/lang/StringBuilder
    //   36: dup
    //   37: invokespecial 347	java/lang/StringBuilder:<init>	()V
    //   40: astore 5
    //   42: aload 5
    //   44: ldc_w 475
    //   47: invokevirtual 353	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   50: pop
    //   51: aload 5
    //   53: aload_1
    //   54: getfield 313	android/content/pm/PackageInfo:packageName	Ljava/lang/String;
    //   57: invokevirtual 353	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   60: pop
    //   61: ldc 35
    //   63: aload 5
    //   65: invokevirtual 355	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   68: invokestatic 478	android/util/Log:i	(Ljava/lang/String;Ljava/lang/String;)I
    //   71: pop
    //   72: goto +88 -> 160
    //   75: iload_3
    //   76: ifeq +45 -> 121
    //   79: new 346	java/lang/StringBuilder
    //   82: dup
    //   83: invokespecial 347	java/lang/StringBuilder:<init>	()V
    //   86: astore 5
    //   88: aload 5
    //   90: ldc_w 480
    //   93: invokevirtual 353	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   96: pop
    //   97: aload 5
    //   99: aload_1
    //   100: getfield 313	android/content/pm/PackageInfo:packageName	Ljava/lang/String;
    //   103: invokevirtual 353	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   106: pop
    //   107: ldc 35
    //   109: aload 5
    //   111: invokevirtual 355	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   114: invokestatic 478	android/util/Log:i	(Ljava/lang/String;Ljava/lang/String;)I
    //   117: pop
    //   118: goto +42 -> 160
    //   121: new 346	java/lang/StringBuilder
    //   124: dup
    //   125: invokespecial 347	java/lang/StringBuilder:<init>	()V
    //   128: astore 5
    //   130: aload 5
    //   132: ldc_w 482
    //   135: invokevirtual 353	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   138: pop
    //   139: aload 5
    //   141: aload_1
    //   142: getfield 313	android/content/pm/PackageInfo:packageName	Ljava/lang/String;
    //   145: invokevirtual 353	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   148: pop
    //   149: ldc 35
    //   151: aload 5
    //   153: invokevirtual 355	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   156: invokestatic 478	android/util/Log:i	(Ljava/lang/String;Ljava/lang/String;)I
    //   159: pop
    //   160: new 102	java/io/File
    //   163: dup
    //   164: aload_0
    //   165: getfield 125	com/android/internal/backup/LocalTransport:mCurrentSetIncrementalDir	Ljava/io/File;
    //   168: aload_1
    //   169: getfield 313	android/content/pm/PackageInfo:packageName	Ljava/lang/String;
    //   172: invokespecial 113	java/io/File:<init>	(Ljava/io/File;Ljava/lang/String;)V
    //   175: astore 6
    //   177: aload 6
    //   179: invokevirtual 175	java/io/File:mkdirs	()Z
    //   182: iconst_1
    //   183: ixor
    //   184: istore 7
    //   186: iload 4
    //   188: ifeq +53 -> 241
    //   191: aload_0
    //   192: getfield 135	com/android/internal/backup/LocalTransport:mParameters	Lcom/android/internal/backup/LocalTransportParameters;
    //   195: invokevirtual 485	com/android/internal/backup/LocalTransportParameters:isNonIncrementalOnly	()Z
    //   198: ifne +8 -> 206
    //   201: iload 7
    //   203: ifne +38 -> 241
    //   206: aload_0
    //   207: getfield 135	com/android/internal/backup/LocalTransport:mParameters	Lcom/android/internal/backup/LocalTransportParameters;
    //   210: invokevirtual 485	com/android/internal/backup/LocalTransportParameters:isNonIncrementalOnly	()Z
    //   213: ifeq +15 -> 228
    //   216: ldc 35
    //   218: ldc_w 487
    //   221: invokestatic 370	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   224: pop
    //   225: goto +12 -> 237
    //   228: ldc 35
    //   230: ldc_w 489
    //   233: invokestatic 370	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   236: pop
    //   237: sipush 64530
    //   240: ireturn
    //   241: iload_3
    //   242: ifeq +29 -> 271
    //   245: iload 7
    //   247: ifeq +24 -> 271
    //   250: ldc 35
    //   252: ldc_w 491
    //   255: invokestatic 370	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   258: pop
    //   259: aload_0
    //   260: aload_1
    //   261: invokevirtual 493	com/android/internal/backup/LocalTransport:clearBackupData	(Landroid/content/pm/PackageInfo;)I
    //   264: pop
    //   265: aload 6
    //   267: invokevirtual 175	java/io/File:mkdirs	()Z
    //   270: pop
    //   271: aload_0
    //   272: aload_2
    //   273: invokespecial 495	com/android/internal/backup/LocalTransport:parseBackupStream	(Landroid/os/ParcelFileDescriptor;)Ljava/util/ArrayList;
    //   276: astore 8
    //   278: new 246	android/util/ArrayMap
    //   281: dup
    //   282: invokespecial 496	android/util/ArrayMap:<init>	()V
    //   285: astore_2
    //   286: aload_0
    //   287: aload 6
    //   289: aload_2
    //   290: invokespecial 498	com/android/internal/backup/LocalTransport:parseKeySizes	(Ljava/io/File;Landroid/util/ArrayMap;)I
    //   293: istore 4
    //   295: aload 8
    //   297: invokevirtual 395	java/util/ArrayList:iterator	()Ljava/util/Iterator;
    //   300: astore 5
    //   302: aload 5
    //   304: invokeinterface 400 1 0
    //   309: ifeq +67 -> 376
    //   312: aload 5
    //   314: invokeinterface 404 1 0
    //   319: checkcast 9	com/android/internal/backup/LocalTransport$KVOperation
    //   322: astore 9
    //   324: aload_2
    //   325: aload 9
    //   327: getfield 499	com/android/internal/backup/LocalTransport$KVOperation:key	Ljava/lang/String;
    //   330: invokevirtual 503	android/util/ArrayMap:get	(Ljava/lang/Object;)Ljava/lang/Object;
    //   333: checkcast 240	java/lang/Integer
    //   336: astore_1
    //   337: iload 4
    //   339: istore_3
    //   340: aload_1
    //   341: ifnull +11 -> 352
    //   344: iload 4
    //   346: aload_1
    //   347: invokevirtual 506	java/lang/Integer:intValue	()I
    //   350: isub
    //   351: istore_3
    //   352: iload_3
    //   353: istore 4
    //   355: aload 9
    //   357: getfield 509	com/android/internal/backup/LocalTransport$KVOperation:value	[B
    //   360: ifnull +13 -> 373
    //   363: iload_3
    //   364: aload 9
    //   366: getfield 509	com/android/internal/backup/LocalTransport$KVOperation:value	[B
    //   369: arraylength
    //   370: iadd
    //   371: istore 4
    //   373: goto -71 -> 302
    //   376: iload 4
    //   378: i2l
    //   379: ldc2_w 29
    //   382: lcmp
    //   383: ifle +7 -> 390
    //   386: sipush 64531
    //   389: ireturn
    //   390: aload 8
    //   392: invokevirtual 395	java/util/ArrayList:iterator	()Ljava/util/Iterator;
    //   395: astore_1
    //   396: aload_1
    //   397: invokeinterface 400 1 0
    //   402: ifeq +177 -> 579
    //   405: aload_1
    //   406: invokeinterface 404 1 0
    //   411: checkcast 9	com/android/internal/backup/LocalTransport$KVOperation
    //   414: astore_2
    //   415: new 102	java/io/File
    //   418: dup
    //   419: aload 6
    //   421: aload_2
    //   422: getfield 499	com/android/internal/backup/LocalTransport$KVOperation:key	Ljava/lang/String;
    //   425: invokespecial 113	java/io/File:<init>	(Ljava/io/File;Ljava/lang/String;)V
    //   428: astore 5
    //   430: aload 5
    //   432: invokevirtual 172	java/io/File:delete	()Z
    //   435: pop
    //   436: aload_2
    //   437: getfield 509	com/android/internal/backup/LocalTransport$KVOperation:value	[B
    //   440: ifnull +136 -> 576
    //   443: new 343	java/io/FileOutputStream
    //   446: astore 8
    //   448: aload 8
    //   450: aload 5
    //   452: invokespecial 510	java/io/FileOutputStream:<init>	(Ljava/io/File;)V
    //   455: aload_2
    //   456: getfield 509	com/android/internal/backup/LocalTransport$KVOperation:value	[B
    //   459: astore 9
    //   461: aload_2
    //   462: getfield 509	com/android/internal/backup/LocalTransport$KVOperation:value	[B
    //   465: arraylength
    //   466: istore_3
    //   467: aload 8
    //   469: aload 9
    //   471: iconst_0
    //   472: iload_3
    //   473: invokevirtual 374	java/io/FileOutputStream:write	([BII)V
    //   476: aload 8
    //   478: invokevirtual 511	java/io/FileOutputStream:close	()V
    //   481: goto +95 -> 576
    //   484: astore_1
    //   485: aconst_null
    //   486: astore_2
    //   487: goto +17 -> 504
    //   490: astore_2
    //   491: goto +10 -> 501
    //   494: astore_1
    //   495: aconst_null
    //   496: astore_2
    //   497: goto +7 -> 504
    //   500: astore_2
    //   501: aload_2
    //   502: athrow
    //   503: astore_1
    //   504: aload_2
    //   505: ifnull +26 -> 531
    //   508: aload 8
    //   510: invokevirtual 511	java/io/FileOutputStream:close	()V
    //   513: goto +23 -> 536
    //   516: astore_1
    //   517: goto +22 -> 539
    //   520: astore 8
    //   522: aload_2
    //   523: aload 8
    //   525: invokevirtual 515	java/lang/Throwable:addSuppressed	(Ljava/lang/Throwable;)V
    //   528: goto +8 -> 536
    //   531: aload 8
    //   533: invokevirtual 511	java/io/FileOutputStream:close	()V
    //   536: aload_1
    //   537: athrow
    //   538: astore_1
    //   539: new 346	java/lang/StringBuilder
    //   542: dup
    //   543: invokespecial 347	java/lang/StringBuilder:<init>	()V
    //   546: astore_1
    //   547: aload_1
    //   548: ldc_w 517
    //   551: invokevirtual 353	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   554: pop
    //   555: aload_1
    //   556: aload 5
    //   558: invokevirtual 388	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   561: pop
    //   562: ldc 35
    //   564: aload_1
    //   565: invokevirtual 355	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   568: invokestatic 361	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   571: pop
    //   572: sipush 64536
    //   575: ireturn
    //   576: goto -180 -> 396
    //   579: iconst_0
    //   580: ireturn
    //   581: astore_1
    //   582: ldc 35
    //   584: ldc_w 519
    //   587: aload_1
    //   588: invokestatic 522	android/util/Log:v	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   591: pop
    //   592: sipush 64536
    //   595: ireturn
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	596	0	this	LocalTransport
    //   0	596	1	paramPackageInfo	PackageInfo
    //   0	596	2	paramParcelFileDescriptor	ParcelFileDescriptor
    //   0	596	3	paramInt	int
    //   7	370	4	i	int
    //   40	517	5	localObject1	Object
    //   175	245	6	localFile	File
    //   184	62	7	bool	boolean
    //   276	233	8	localObject2	Object
    //   520	12	8	localThrowable	Throwable
    //   322	148	9	localObject3	Object
    // Exception table:
    //   from	to	target	type
    //   467	476	484	finally
    //   467	476	490	java/lang/Throwable
    //   455	467	494	finally
    //   455	467	500	java/lang/Throwable
    //   501	503	503	finally
    //   476	481	516	java/io/IOException
    //   508	513	516	java/io/IOException
    //   522	528	516	java/io/IOException
    //   531	536	516	java/io/IOException
    //   536	538	516	java/io/IOException
    //   508	513	520	java/lang/Throwable
    //   443	455	538	java/io/IOException
    //   271	278	581	java/io/IOException
  }
  
  public int performFullBackup(PackageInfo paramPackageInfo, ParcelFileDescriptor paramParcelFileDescriptor)
  {
    if (mSocket != null)
    {
      Log.e("LocalTransport", "Attempt to initiate full backup while one is in progress");
      return 64536;
    }
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
      Log.e("LocalTransport", "Unable to process socket for full backup");
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
      Log.w("LocalTransport", "Attempted sendBackupData before performFullBackup");
      return 64536;
    }
    mFullBackupSize += paramInt;
    if (mFullBackupSize > 26214400L) {
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
    while (paramInt > 0) {
      try
      {
        int i = mSocketInputStream.read(mFullBackupBuffer, 0, paramInt);
        if (i < 0)
        {
          Log.w("LocalTransport", "Unexpected EOD; failing backup");
          return 64536;
        }
        mFullBackupOutputStream.write(mFullBackupBuffer, 0, i);
        paramInt -= i;
      }
      catch (IOException localIOException)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Error handling backup data for ");
        localStringBuilder.append(mFullTargetPackage);
        Log.e("LocalTransport", localStringBuilder.toString());
        return 64536;
      }
    }
    return 0;
  }
  
  public int startRestore(long paramLong, PackageInfo[] paramArrayOfPackageInfo)
  {
    mRestorePackages = paramArrayOfPackageInfo;
    mRestorePackage = -1;
    mRestoreSetDir = new File(mDataDir, Long.toString(paramLong));
    mRestoreSetIncrementalDir = new File(mRestoreSetDir, "_delta");
    mRestoreSetFullDir = new File(mRestoreSetDir, "_full");
    return 0;
  }
  
  public String transportDirName()
  {
    return "com.android.internal.backup.LocalTransport";
  }
  
  static class DecodedFilename
    implements Comparable<DecodedFilename>
  {
    public File file;
    public String key;
    
    public DecodedFilename(File paramFile)
    {
      file = paramFile;
      key = new String(Base64.decode(paramFile.getName()));
    }
    
    public int compareTo(DecodedFilename paramDecodedFilename)
    {
      return key.compareTo(key);
    }
  }
  
  private class KVOperation
  {
    final String key;
    final byte[] value;
    
    KVOperation(String paramString, byte[] paramArrayOfByte)
    {
      key = paramString;
      value = paramArrayOfByte;
    }
  }
}
