package android.app;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.FileUtils;
import android.os.Handler;
import android.os.Looper;
import android.system.ErrnoException;
import android.system.Os;
import android.system.StructStat;
import android.system.StructTimespec;
import android.util.Log;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.util.ExponentiallyBucketedHistogram;
import dalvik.system.BlockGuard;
import dalvik.system.BlockGuard.Policy;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.CountDownLatch;

final class SharedPreferencesImpl
  implements SharedPreferences
{
  private static final Object CONTENT = new Object();
  private static final boolean DEBUG = false;
  private static final long MAX_FSYNC_DURATION_MILLIS = 256L;
  private static final String TAG = "SharedPreferencesImpl";
  private final File mBackupFile;
  @GuardedBy("this")
  private long mCurrentMemoryStateGeneration;
  @GuardedBy("mWritingToDiskLock")
  private long mDiskStateGeneration;
  @GuardedBy("mLock")
  private int mDiskWritesInFlight = 0;
  private final File mFile;
  @GuardedBy("mLock")
  private final WeakHashMap<SharedPreferences.OnSharedPreferenceChangeListener, Object> mListeners = new WeakHashMap();
  @GuardedBy("mLock")
  private boolean mLoaded = false;
  private final Object mLock = new Object();
  @GuardedBy("mLock")
  private Map<String, Object> mMap;
  private final int mMode;
  private int mNumSync = 0;
  @GuardedBy("mLock")
  private long mStatSize;
  @GuardedBy("mLock")
  private StructTimespec mStatTimestamp;
  @GuardedBy("mWritingToDiskLock")
  private final ExponentiallyBucketedHistogram mSyncTimes = new ExponentiallyBucketedHistogram(16);
  @GuardedBy("mLock")
  private Throwable mThrowable;
  private final Object mWritingToDiskLock = new Object();
  
  SharedPreferencesImpl(File paramFile, int paramInt)
  {
    mFile = paramFile;
    mBackupFile = makeBackupFile(paramFile);
    mMode = paramInt;
    mLoaded = false;
    mMap = null;
    mThrowable = null;
    startLoadFromDisk();
  }
  
  @GuardedBy("mLock")
  private void awaitLoadedLocked()
  {
    if (!mLoaded) {
      BlockGuard.getThreadPolicy().onReadFromDisk();
    }
    while (!mLoaded) {
      try
      {
        mLock.wait();
      }
      catch (InterruptedException localInterruptedException) {}
    }
    if (mThrowable == null) {
      return;
    }
    throw new IllegalStateException(mThrowable);
  }
  
  private static FileOutputStream createFileOutputStream(File paramFile)
  {
    StringBuilder localStringBuilder = null;
    try
    {
      FileOutputStream localFileOutputStream = new java/io/FileOutputStream;
      localFileOutputStream.<init>(paramFile);
      paramFile = localFileOutputStream;
    }
    catch (FileNotFoundException localFileNotFoundException1)
    {
      Object localObject = paramFile.getParentFile();
      if (!((File)localObject).mkdir())
      {
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("Couldn't create directory for SharedPreferences file ");
        localStringBuilder.append(paramFile);
        Log.e("SharedPreferencesImpl", localStringBuilder.toString());
        return null;
      }
      FileUtils.setPermissions(((File)localObject).getPath(), 505, -1, -1);
      try
      {
        localObject = new java/io/FileOutputStream;
        ((FileOutputStream)localObject).<init>(paramFile);
        paramFile = (File)localObject;
      }
      catch (FileNotFoundException localFileNotFoundException2)
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("Couldn't create SharedPreferences file ");
        ((StringBuilder)localObject).append(paramFile);
        Log.e("SharedPreferencesImpl", ((StringBuilder)localObject).toString(), localFileNotFoundException2);
        paramFile = localStringBuilder;
      }
    }
    return paramFile;
  }
  
  private void enqueueDiskWrite(MemoryCommitResult arg1, final Runnable paramRunnable)
  {
    boolean bool1 = false;
    final boolean bool2;
    if (paramRunnable == null) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    paramRunnable = new Runnable()
    {
      public void run()
      {
        synchronized (mWritingToDiskLock)
        {
          SharedPreferencesImpl.this.writeToFile(paramMemoryCommitResult, bool2);
          synchronized (mLock)
          {
            SharedPreferencesImpl.access$310(SharedPreferencesImpl.this);
            if (paramRunnable != null) {
              paramRunnable.run();
            }
            return;
          }
        }
      }
    };
    if (bool2) {
      synchronized (mLock)
      {
        int i;
        if (mDiskWritesInFlight == 1) {
          i = 1;
        } else {
          i = 0;
        }
        if (i != 0)
        {
          paramRunnable.run();
          return;
        }
      }
    }
    if (!bool2) {
      bool1 = true;
    }
    QueuedWork.queue(paramRunnable, bool1);
  }
  
  private boolean hasFileChangedUnexpectedly()
  {
    synchronized (mLock)
    {
      if (mDiskWritesInFlight > 0) {
        return false;
      }
      boolean bool1 = true;
      try
      {
        BlockGuard.getThreadPolicy().onReadFromDisk();
        StructStat localStructStat = Os.stat(mFile.getPath());
        ??? = mLock;
        boolean bool2 = bool1;
        try
        {
          if (st_mtim.equals(mStatTimestamp)) {
            if (mStatSize != st_size) {
              bool2 = bool1;
            } else {
              bool2 = false;
            }
          }
          return bool2;
        }
        finally {}
        localObject3 = finally;
      }
      catch (ErrnoException localErrnoException)
      {
        return true;
      }
    }
  }
  
  /* Error */
  private void loadFromDisk()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 72	android/app/SharedPreferencesImpl:mLock	Ljava/lang/Object;
    //   4: astore_1
    //   5: aload_1
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 78	android/app/SharedPreferencesImpl:mLoaded	Z
    //   11: ifeq +6 -> 17
    //   14: aload_1
    //   15: monitorexit
    //   16: return
    //   17: aload_0
    //   18: getfield 100	android/app/SharedPreferencesImpl:mBackupFile	Ljava/io/File;
    //   21: invokevirtual 270	java/io/File:exists	()Z
    //   24: ifeq +23 -> 47
    //   27: aload_0
    //   28: getfield 94	android/app/SharedPreferencesImpl:mFile	Ljava/io/File;
    //   31: invokevirtual 273	java/io/File:delete	()Z
    //   34: pop
    //   35: aload_0
    //   36: getfield 100	android/app/SharedPreferencesImpl:mBackupFile	Ljava/io/File;
    //   39: aload_0
    //   40: getfield 94	android/app/SharedPreferencesImpl:mFile	Ljava/io/File;
    //   43: invokevirtual 277	java/io/File:renameTo	(Ljava/io/File;)Z
    //   46: pop
    //   47: aload_1
    //   48: monitorexit
    //   49: aload_0
    //   50: getfield 94	android/app/SharedPreferencesImpl:mFile	Ljava/io/File;
    //   53: invokevirtual 270	java/io/File:exists	()Z
    //   56: ifeq +56 -> 112
    //   59: aload_0
    //   60: getfield 94	android/app/SharedPreferencesImpl:mFile	Ljava/io/File;
    //   63: invokevirtual 280	java/io/File:canRead	()Z
    //   66: ifne +46 -> 112
    //   69: new 188	java/lang/StringBuilder
    //   72: dup
    //   73: invokespecial 189	java/lang/StringBuilder:<init>	()V
    //   76: astore_2
    //   77: aload_2
    //   78: ldc_w 282
    //   81: invokevirtual 195	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   84: pop
    //   85: aload_2
    //   86: aload_0
    //   87: getfield 94	android/app/SharedPreferencesImpl:mFile	Ljava/io/File;
    //   90: invokevirtual 198	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   93: pop
    //   94: aload_2
    //   95: ldc_w 284
    //   98: invokevirtual 195	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   101: pop
    //   102: ldc 33
    //   104: aload_2
    //   105: invokevirtual 202	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   108: invokestatic 287	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   111: pop
    //   112: aconst_null
    //   113: astore_3
    //   114: aconst_null
    //   115: astore 4
    //   117: aconst_null
    //   118: astore_2
    //   119: aconst_null
    //   120: astore 5
    //   122: aconst_null
    //   123: astore 6
    //   125: aconst_null
    //   126: astore 7
    //   128: aconst_null
    //   129: astore 8
    //   131: aconst_null
    //   132: astore 9
    //   134: aconst_null
    //   135: astore 10
    //   137: aload_3
    //   138: astore 11
    //   140: aload 4
    //   142: astore 12
    //   144: aload_0
    //   145: getfield 94	android/app/SharedPreferencesImpl:mFile	Ljava/io/File;
    //   148: invokevirtual 211	java/io/File:getPath	()Ljava/lang/String;
    //   151: invokestatic 245	android/system/Os:stat	(Ljava/lang/String;)Landroid/system/StructStat;
    //   154: astore_1
    //   155: aload_3
    //   156: astore 11
    //   158: aload_1
    //   159: astore 7
    //   161: aload 4
    //   163: astore 12
    //   165: aload_1
    //   166: astore 6
    //   168: aload_0
    //   169: getfield 94	android/app/SharedPreferencesImpl:mFile	Ljava/io/File;
    //   172: invokevirtual 280	java/io/File:canRead	()Z
    //   175: istore 13
    //   177: aload_1
    //   178: astore 6
    //   180: iload 13
    //   182: ifeq +239 -> 421
    //   185: aload 9
    //   187: astore_2
    //   188: aload 8
    //   190: astore 14
    //   192: new 289	java/io/BufferedInputStream
    //   195: astore 7
    //   197: aload 9
    //   199: astore_2
    //   200: aload 8
    //   202: astore 14
    //   204: new 291	java/io/FileInputStream
    //   207: astore 11
    //   209: aload 9
    //   211: astore_2
    //   212: aload 8
    //   214: astore 14
    //   216: aload 11
    //   218: aload_0
    //   219: getfield 94	android/app/SharedPreferencesImpl:mFile	Ljava/io/File;
    //   222: invokespecial 292	java/io/FileInputStream:<init>	(Ljava/io/File;)V
    //   225: aload 9
    //   227: astore_2
    //   228: aload 8
    //   230: astore 14
    //   232: aload 7
    //   234: aload 11
    //   236: sipush 16384
    //   239: invokespecial 295	java/io/BufferedInputStream:<init>	(Ljava/io/InputStream;I)V
    //   242: aload 7
    //   244: astore 9
    //   246: aload 9
    //   248: astore_2
    //   249: aload 9
    //   251: astore 14
    //   253: aload 9
    //   255: invokestatic 301	com/android/internal/util/XmlUtils:readMapXml	(Ljava/io/InputStream;)Ljava/util/HashMap;
    //   258: astore 7
    //   260: aload 7
    //   262: astore_2
    //   263: aload_2
    //   264: astore 11
    //   266: aload_1
    //   267: astore 7
    //   269: aload_2
    //   270: astore 12
    //   272: aload_1
    //   273: astore 6
    //   275: aload 9
    //   277: invokestatic 307	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   280: aload_1
    //   281: astore 6
    //   283: goto +138 -> 421
    //   286: astore 14
    //   288: goto +89 -> 377
    //   291: astore 11
    //   293: aload 14
    //   295: astore_2
    //   296: new 188	java/lang/StringBuilder
    //   299: astore 7
    //   301: aload 14
    //   303: astore_2
    //   304: aload 7
    //   306: invokespecial 189	java/lang/StringBuilder:<init>	()V
    //   309: aload 14
    //   311: astore_2
    //   312: aload 7
    //   314: ldc_w 309
    //   317: invokevirtual 195	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   320: pop
    //   321: aload 14
    //   323: astore_2
    //   324: aload 7
    //   326: aload_0
    //   327: getfield 94	android/app/SharedPreferencesImpl:mFile	Ljava/io/File;
    //   330: invokevirtual 312	java/io/File:getAbsolutePath	()Ljava/lang/String;
    //   333: invokevirtual 195	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   336: pop
    //   337: aload 14
    //   339: astore_2
    //   340: ldc 33
    //   342: aload 7
    //   344: invokevirtual 202	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   347: aload 11
    //   349: invokestatic 314	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   352: pop
    //   353: aload_3
    //   354: astore 11
    //   356: aload_1
    //   357: astore 7
    //   359: aload 4
    //   361: astore 12
    //   363: aload_1
    //   364: astore 6
    //   366: aload 14
    //   368: invokestatic 307	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   371: aload 5
    //   373: astore_2
    //   374: goto -94 -> 280
    //   377: aload_3
    //   378: astore 11
    //   380: aload_1
    //   381: astore 7
    //   383: aload 4
    //   385: astore 12
    //   387: aload_1
    //   388: astore 6
    //   390: aload_2
    //   391: invokestatic 307	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   394: aload_3
    //   395: astore 11
    //   397: aload_1
    //   398: astore 7
    //   400: aload 4
    //   402: astore 12
    //   404: aload_1
    //   405: astore 6
    //   407: aload 14
    //   409: athrow
    //   410: astore_1
    //   411: aload 11
    //   413: astore_2
    //   414: goto +14 -> 428
    //   417: astore_2
    //   418: aload 12
    //   420: astore_2
    //   421: aload 10
    //   423: astore_1
    //   424: aload 6
    //   426: astore 7
    //   428: aload_0
    //   429: getfield 72	android/app/SharedPreferencesImpl:mLock	Ljava/lang/Object;
    //   432: astore 11
    //   434: aload 11
    //   436: monitorenter
    //   437: aload_0
    //   438: iconst_1
    //   439: putfield 78	android/app/SharedPreferencesImpl:mLoaded	Z
    //   442: aload_0
    //   443: aload_1
    //   444: putfield 106	android/app/SharedPreferencesImpl:mThrowable	Ljava/lang/Throwable;
    //   447: aload_1
    //   448: ifnonnull +79 -> 527
    //   451: aload_2
    //   452: ifnull +37 -> 489
    //   455: aload_0
    //   456: aload_2
    //   457: putfield 104	android/app/SharedPreferencesImpl:mMap	Ljava/util/Map;
    //   460: aload_0
    //   461: aload 7
    //   463: getfield 250	android/system/StructStat:st_mtim	Landroid/system/StructTimespec;
    //   466: putfield 252	android/app/SharedPreferencesImpl:mStatTimestamp	Landroid/system/StructTimespec;
    //   469: aload_0
    //   470: aload 7
    //   472: getfield 263	android/system/StructStat:st_size	J
    //   475: putfield 260	android/app/SharedPreferencesImpl:mStatSize	J
    //   478: goto +49 -> 527
    //   481: astore_2
    //   482: goto +36 -> 518
    //   485: astore_2
    //   486: goto +19 -> 505
    //   489: new 316	java/util/HashMap
    //   492: astore_2
    //   493: aload_2
    //   494: invokespecial 317	java/util/HashMap:<init>	()V
    //   497: aload_0
    //   498: aload_2
    //   499: putfield 104	android/app/SharedPreferencesImpl:mMap	Ljava/util/Map;
    //   502: goto +25 -> 527
    //   505: aload_0
    //   506: aload_2
    //   507: putfield 106	android/app/SharedPreferencesImpl:mThrowable	Ljava/lang/Throwable;
    //   510: aload_0
    //   511: getfield 72	android/app/SharedPreferencesImpl:mLock	Ljava/lang/Object;
    //   514: astore_2
    //   515: goto +17 -> 532
    //   518: aload_0
    //   519: getfield 72	android/app/SharedPreferencesImpl:mLock	Ljava/lang/Object;
    //   522: invokevirtual 320	java/lang/Object:notifyAll	()V
    //   525: aload_2
    //   526: athrow
    //   527: aload_0
    //   528: getfield 72	android/app/SharedPreferencesImpl:mLock	Ljava/lang/Object;
    //   531: astore_2
    //   532: aload_2
    //   533: invokevirtual 320	java/lang/Object:notifyAll	()V
    //   536: aload 11
    //   538: monitorexit
    //   539: return
    //   540: astore_2
    //   541: aload 11
    //   543: monitorexit
    //   544: aload_2
    //   545: athrow
    //   546: astore_2
    //   547: aload_1
    //   548: monitorexit
    //   549: aload_2
    //   550: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	551	0	this	SharedPreferencesImpl
    //   4	401	1	localObject1	Object
    //   410	1	1	localThrowable1	Throwable
    //   423	125	1	localObject2	Object
    //   76	338	2	localObject3	Object
    //   417	1	2	localErrnoException	ErrnoException
    //   420	37	2	localObject4	Object
    //   481	1	2	localObject5	Object
    //   485	1	2	localThrowable2	Throwable
    //   492	41	2	localObject6	Object
    //   540	5	2	localObject7	Object
    //   546	4	2	localObject8	Object
    //   113	282	3	localObject9	Object
    //   115	286	4	localObject10	Object
    //   120	252	5	localObject11	Object
    //   123	302	6	localObject12	Object
    //   126	345	7	localObject13	Object
    //   129	100	8	localObject14	Object
    //   132	144	9	localObject15	Object
    //   135	287	10	localObject16	Object
    //   138	127	11	localObject17	Object
    //   291	57	11	localException	Exception
    //   354	188	11	localObject18	Object
    //   142	277	12	localObject19	Object
    //   175	6	13	bool	boolean
    //   190	62	14	localObject20	Object
    //   286	122	14	localAutoCloseable	AutoCloseable
    // Exception table:
    //   from	to	target	type
    //   192	197	286	finally
    //   204	209	286	finally
    //   216	225	286	finally
    //   232	242	286	finally
    //   253	260	286	finally
    //   296	301	286	finally
    //   304	309	286	finally
    //   312	321	286	finally
    //   324	337	286	finally
    //   340	353	286	finally
    //   192	197	291	java/lang/Exception
    //   204	209	291	java/lang/Exception
    //   216	225	291	java/lang/Exception
    //   232	242	291	java/lang/Exception
    //   253	260	291	java/lang/Exception
    //   144	155	410	java/lang/Throwable
    //   168	177	410	java/lang/Throwable
    //   275	280	410	java/lang/Throwable
    //   366	371	410	java/lang/Throwable
    //   390	394	410	java/lang/Throwable
    //   407	410	410	java/lang/Throwable
    //   144	155	417	android/system/ErrnoException
    //   168	177	417	android/system/ErrnoException
    //   275	280	417	android/system/ErrnoException
    //   366	371	417	android/system/ErrnoException
    //   390	394	417	android/system/ErrnoException
    //   407	410	417	android/system/ErrnoException
    //   455	478	481	finally
    //   489	502	481	finally
    //   505	510	481	finally
    //   455	478	485	java/lang/Throwable
    //   489	502	485	java/lang/Throwable
    //   437	447	540	finally
    //   510	515	540	finally
    //   518	527	540	finally
    //   527	532	540	finally
    //   532	536	540	finally
    //   536	539	540	finally
    //   541	544	540	finally
    //   7	16	546	finally
    //   17	47	546	finally
    //   47	49	546	finally
    //   547	549	546	finally
  }
  
  static File makeBackupFile(File paramFile)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramFile.getPath());
    localStringBuilder.append(".bak");
    return new File(localStringBuilder.toString());
  }
  
  private void startLoadFromDisk()
  {
    synchronized (mLock)
    {
      mLoaded = false;
      new Thread("SharedPreferencesImpl-load")
      {
        public void run()
        {
          SharedPreferencesImpl.this.loadFromDisk();
        }
      }.start();
      return;
    }
  }
  
  /* Error */
  @GuardedBy("mWritingToDiskLock")
  private void writeToFile(MemoryCommitResult paramMemoryCommitResult, boolean paramBoolean)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 94	android/app/SharedPreferencesImpl:mFile	Ljava/io/File;
    //   4: invokevirtual 270	java/io/File:exists	()Z
    //   7: ifeq +189 -> 196
    //   10: iconst_0
    //   11: istore_3
    //   12: iconst_0
    //   13: istore 4
    //   15: aload_0
    //   16: getfield 339	android/app/SharedPreferencesImpl:mDiskStateGeneration	J
    //   19: aload_1
    //   20: getfield 342	android/app/SharedPreferencesImpl$MemoryCommitResult:memoryStateGeneration	J
    //   23: lcmp
    //   24: ifge +57 -> 81
    //   27: iload_2
    //   28: ifeq +9 -> 37
    //   31: iconst_1
    //   32: istore 4
    //   34: goto +50 -> 84
    //   37: aload_0
    //   38: getfield 72	android/app/SharedPreferencesImpl:mLock	Ljava/lang/Object;
    //   41: astore 5
    //   43: aload 5
    //   45: monitorenter
    //   46: aload_0
    //   47: getfield 136	android/app/SharedPreferencesImpl:mCurrentMemoryStateGeneration	J
    //   50: lstore 6
    //   52: lload 6
    //   54: aload_1
    //   55: getfield 342	android/app/SharedPreferencesImpl$MemoryCommitResult:memoryStateGeneration	J
    //   58: lcmp
    //   59: ifne +6 -> 65
    //   62: iconst_1
    //   63: istore 4
    //   65: aload 5
    //   67: monitorexit
    //   68: goto +16 -> 84
    //   71: astore_1
    //   72: goto +4 -> 76
    //   75: astore_1
    //   76: aload 5
    //   78: monitorexit
    //   79: aload_1
    //   80: athrow
    //   81: iload_3
    //   82: istore 4
    //   84: iload 4
    //   86: ifne +10 -> 96
    //   89: aload_1
    //   90: iconst_0
    //   91: iconst_1
    //   92: invokevirtual 346	android/app/SharedPreferencesImpl$MemoryCommitResult:setDiskWriteResult	(ZZ)V
    //   95: return
    //   96: aload_0
    //   97: getfield 100	android/app/SharedPreferencesImpl:mBackupFile	Ljava/io/File;
    //   100: invokevirtual 270	java/io/File:exists	()Z
    //   103: ifne +82 -> 185
    //   106: aload_0
    //   107: getfield 94	android/app/SharedPreferencesImpl:mFile	Ljava/io/File;
    //   110: aload_0
    //   111: getfield 100	android/app/SharedPreferencesImpl:mBackupFile	Ljava/io/File;
    //   114: invokevirtual 277	java/io/File:renameTo	(Ljava/io/File;)Z
    //   117: ifne +79 -> 196
    //   120: new 188	java/lang/StringBuilder
    //   123: dup
    //   124: invokespecial 189	java/lang/StringBuilder:<init>	()V
    //   127: astore 5
    //   129: aload 5
    //   131: ldc_w 348
    //   134: invokevirtual 195	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   137: pop
    //   138: aload 5
    //   140: aload_0
    //   141: getfield 94	android/app/SharedPreferencesImpl:mFile	Ljava/io/File;
    //   144: invokevirtual 198	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   147: pop
    //   148: aload 5
    //   150: ldc_w 350
    //   153: invokevirtual 195	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   156: pop
    //   157: aload 5
    //   159: aload_0
    //   160: getfield 100	android/app/SharedPreferencesImpl:mBackupFile	Ljava/io/File;
    //   163: invokevirtual 198	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   166: pop
    //   167: ldc 33
    //   169: aload 5
    //   171: invokevirtual 202	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   174: invokestatic 208	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   177: pop
    //   178: aload_1
    //   179: iconst_0
    //   180: iconst_0
    //   181: invokevirtual 346	android/app/SharedPreferencesImpl$MemoryCommitResult:setDiskWriteResult	(ZZ)V
    //   184: return
    //   185: aload_0
    //   186: getfield 94	android/app/SharedPreferencesImpl:mFile	Ljava/io/File;
    //   189: invokevirtual 273	java/io/File:delete	()Z
    //   192: pop
    //   193: goto +3 -> 196
    //   196: aload_0
    //   197: getfield 94	android/app/SharedPreferencesImpl:mFile	Ljava/io/File;
    //   200: invokestatic 352	android/app/SharedPreferencesImpl:createFileOutputStream	(Ljava/io/File;)Ljava/io/FileOutputStream;
    //   203: astore 5
    //   205: aload 5
    //   207: ifnonnull +10 -> 217
    //   210: aload_1
    //   211: iconst_0
    //   212: iconst_0
    //   213: invokevirtual 346	android/app/SharedPreferencesImpl$MemoryCommitResult:setDiskWriteResult	(ZZ)V
    //   216: return
    //   217: aload_1
    //   218: getfield 355	android/app/SharedPreferencesImpl$MemoryCommitResult:mapToWriteToDisk	Ljava/util/Map;
    //   221: aload 5
    //   223: invokestatic 359	com/android/internal/util/XmlUtils:writeMapXml	(Ljava/util/Map;Ljava/io/OutputStream;)V
    //   226: invokestatic 365	java/lang/System:currentTimeMillis	()J
    //   229: lstore 8
    //   231: aload 5
    //   233: invokestatic 369	android/os/FileUtils:sync	(Ljava/io/FileOutputStream;)Z
    //   236: pop
    //   237: invokestatic 365	java/lang/System:currentTimeMillis	()J
    //   240: lstore 6
    //   242: aload 5
    //   244: invokevirtual 372	java/io/FileOutputStream:close	()V
    //   247: aload_0
    //   248: getfield 94	android/app/SharedPreferencesImpl:mFile	Ljava/io/File;
    //   251: invokevirtual 211	java/io/File:getPath	()Ljava/lang/String;
    //   254: aload_0
    //   255: getfield 102	android/app/SharedPreferencesImpl:mMode	I
    //   258: iconst_0
    //   259: invokestatic 378	android/app/ContextImpl:setFilePermissionsFromMode	(Ljava/lang/String;II)V
    //   262: aload_0
    //   263: getfield 94	android/app/SharedPreferencesImpl:mFile	Ljava/io/File;
    //   266: invokevirtual 211	java/io/File:getPath	()Ljava/lang/String;
    //   269: invokestatic 245	android/system/Os:stat	(Ljava/lang/String;)Landroid/system/StructStat;
    //   272: astore 10
    //   274: aload_0
    //   275: getfield 72	android/app/SharedPreferencesImpl:mLock	Ljava/lang/Object;
    //   278: astore 5
    //   280: aload 5
    //   282: monitorenter
    //   283: aload_0
    //   284: aload 10
    //   286: getfield 250	android/system/StructStat:st_mtim	Landroid/system/StructTimespec;
    //   289: putfield 252	android/app/SharedPreferencesImpl:mStatTimestamp	Landroid/system/StructTimespec;
    //   292: aload_0
    //   293: aload 10
    //   295: getfield 263	android/system/StructStat:st_size	J
    //   298: putfield 260	android/app/SharedPreferencesImpl:mStatSize	J
    //   301: aload 5
    //   303: monitorexit
    //   304: goto +13 -> 317
    //   307: astore 10
    //   309: aload 5
    //   311: monitorexit
    //   312: aload 10
    //   314: athrow
    //   315: astore 5
    //   317: aload_0
    //   318: getfield 100	android/app/SharedPreferencesImpl:mBackupFile	Ljava/io/File;
    //   321: invokevirtual 273	java/io/File:delete	()Z
    //   324: pop
    //   325: aload_0
    //   326: aload_1
    //   327: getfield 342	android/app/SharedPreferencesImpl$MemoryCommitResult:memoryStateGeneration	J
    //   330: putfield 339	android/app/SharedPreferencesImpl:mDiskStateGeneration	J
    //   333: aload_1
    //   334: iconst_1
    //   335: iconst_1
    //   336: invokevirtual 346	android/app/SharedPreferencesImpl$MemoryCommitResult:setDiskWriteResult	(ZZ)V
    //   339: lload 6
    //   341: lload 8
    //   343: lsub
    //   344: lstore 6
    //   346: aload_0
    //   347: getfield 90	android/app/SharedPreferencesImpl:mSyncTimes	Lcom/android/internal/util/ExponentiallyBucketedHistogram;
    //   350: lload 6
    //   352: l2i
    //   353: invokevirtual 381	com/android/internal/util/ExponentiallyBucketedHistogram:add	(I)V
    //   356: aload_0
    //   357: aload_0
    //   358: getfield 92	android/app/SharedPreferencesImpl:mNumSync	I
    //   361: iconst_1
    //   362: iadd
    //   363: putfield 92	android/app/SharedPreferencesImpl:mNumSync	I
    //   366: aload_0
    //   367: getfield 92	android/app/SharedPreferencesImpl:mNumSync	I
    //   370: sipush 1024
    //   373: irem
    //   374: ifeq +12 -> 386
    //   377: lload 6
    //   379: ldc2_w 28
    //   382: lcmp
    //   383: ifle +59 -> 442
    //   386: aload_0
    //   387: getfield 90	android/app/SharedPreferencesImpl:mSyncTimes	Lcom/android/internal/util/ExponentiallyBucketedHistogram;
    //   390: astore 5
    //   392: new 188	java/lang/StringBuilder
    //   395: astore 10
    //   397: aload 10
    //   399: invokespecial 189	java/lang/StringBuilder:<init>	()V
    //   402: aload 10
    //   404: ldc_w 383
    //   407: invokevirtual 195	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   410: pop
    //   411: aload 10
    //   413: aload_0
    //   414: getfield 94	android/app/SharedPreferencesImpl:mFile	Ljava/io/File;
    //   417: invokevirtual 198	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   420: pop
    //   421: aload 10
    //   423: ldc_w 385
    //   426: invokevirtual 195	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   429: pop
    //   430: aload 5
    //   432: ldc 33
    //   434: aload 10
    //   436: invokevirtual 202	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   439: invokevirtual 389	com/android/internal/util/ExponentiallyBucketedHistogram:log	(Ljava/lang/String;Ljava/lang/CharSequence;)V
    //   442: return
    //   443: astore 5
    //   445: ldc 33
    //   447: ldc_w 391
    //   450: aload 5
    //   452: invokestatic 314	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   455: pop
    //   456: goto +16 -> 472
    //   459: astore 5
    //   461: ldc 33
    //   463: ldc_w 391
    //   466: aload 5
    //   468: invokestatic 314	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   471: pop
    //   472: aload_0
    //   473: getfield 94	android/app/SharedPreferencesImpl:mFile	Ljava/io/File;
    //   476: invokevirtual 270	java/io/File:exists	()Z
    //   479: ifeq +52 -> 531
    //   482: aload_0
    //   483: getfield 94	android/app/SharedPreferencesImpl:mFile	Ljava/io/File;
    //   486: invokevirtual 273	java/io/File:delete	()Z
    //   489: ifne +42 -> 531
    //   492: new 188	java/lang/StringBuilder
    //   495: dup
    //   496: invokespecial 189	java/lang/StringBuilder:<init>	()V
    //   499: astore 5
    //   501: aload 5
    //   503: ldc_w 393
    //   506: invokevirtual 195	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   509: pop
    //   510: aload 5
    //   512: aload_0
    //   513: getfield 94	android/app/SharedPreferencesImpl:mFile	Ljava/io/File;
    //   516: invokevirtual 198	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   519: pop
    //   520: ldc 33
    //   522: aload 5
    //   524: invokevirtual 202	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   527: invokestatic 208	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   530: pop
    //   531: aload_1
    //   532: iconst_0
    //   533: iconst_0
    //   534: invokevirtual 346	android/app/SharedPreferencesImpl$MemoryCommitResult:setDiskWriteResult	(ZZ)V
    //   537: return
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	538	0	this	SharedPreferencesImpl
    //   0	538	1	paramMemoryCommitResult	MemoryCommitResult
    //   0	538	2	paramBoolean	boolean
    //   11	71	3	i	int
    //   13	72	4	j	int
    //   315	1	5	localErrnoException	ErrnoException
    //   390	41	5	localExponentiallyBucketedHistogram	ExponentiallyBucketedHistogram
    //   443	8	5	localIOException	java.io.IOException
    //   459	8	5	localXmlPullParserException	org.xmlpull.v1.XmlPullParserException
    //   499	24	5	localStringBuilder1	StringBuilder
    //   50	328	6	l1	long
    //   229	113	8	l2	long
    //   272	22	10	localStructStat	StructStat
    //   307	6	10	localObject2	Object
    //   395	40	10	localStringBuilder2	StringBuilder
    // Exception table:
    //   from	to	target	type
    //   52	62	71	finally
    //   65	68	71	finally
    //   76	79	71	finally
    //   46	52	75	finally
    //   283	304	307	finally
    //   309	312	307	finally
    //   262	283	315	android/system/ErrnoException
    //   312	315	315	android/system/ErrnoException
    //   196	205	443	java/io/IOException
    //   210	216	443	java/io/IOException
    //   217	262	443	java/io/IOException
    //   262	283	443	java/io/IOException
    //   312	315	443	java/io/IOException
    //   317	339	443	java/io/IOException
    //   346	377	443	java/io/IOException
    //   386	442	443	java/io/IOException
    //   196	205	459	org/xmlpull/v1/XmlPullParserException
    //   210	216	459	org/xmlpull/v1/XmlPullParserException
    //   217	262	459	org/xmlpull/v1/XmlPullParserException
    //   262	283	459	org/xmlpull/v1/XmlPullParserException
    //   312	315	459	org/xmlpull/v1/XmlPullParserException
    //   317	339	459	org/xmlpull/v1/XmlPullParserException
    //   346	377	459	org/xmlpull/v1/XmlPullParserException
    //   386	442	459	org/xmlpull/v1/XmlPullParserException
  }
  
  public boolean contains(String paramString)
  {
    synchronized (mLock)
    {
      awaitLoadedLocked();
      boolean bool = mMap.containsKey(paramString);
      return bool;
    }
  }
  
  public SharedPreferences.Editor edit()
  {
    synchronized (mLock)
    {
      awaitLoadedLocked();
      return new EditorImpl();
    }
  }
  
  public Map<String, ?> getAll()
  {
    synchronized (mLock)
    {
      awaitLoadedLocked();
      HashMap localHashMap = new java/util/HashMap;
      localHashMap.<init>(mMap);
      return localHashMap;
    }
  }
  
  public boolean getBoolean(String paramString, boolean paramBoolean)
  {
    synchronized (mLock)
    {
      awaitLoadedLocked();
      paramString = (Boolean)mMap.get(paramString);
      if (paramString != null) {
        paramBoolean = paramString.booleanValue();
      }
      return paramBoolean;
    }
  }
  
  public float getFloat(String paramString, float paramFloat)
  {
    synchronized (mLock)
    {
      awaitLoadedLocked();
      paramString = (Float)mMap.get(paramString);
      if (paramString != null) {
        paramFloat = paramString.floatValue();
      }
      return paramFloat;
    }
  }
  
  public int getInt(String paramString, int paramInt)
  {
    synchronized (mLock)
    {
      awaitLoadedLocked();
      paramString = (Integer)mMap.get(paramString);
      if (paramString != null) {
        paramInt = paramString.intValue();
      }
      return paramInt;
    }
  }
  
  public long getLong(String paramString, long paramLong)
  {
    synchronized (mLock)
    {
      awaitLoadedLocked();
      paramString = (Long)mMap.get(paramString);
      if (paramString != null) {
        paramLong = paramString.longValue();
      }
      return paramLong;
    }
  }
  
  public String getString(String paramString1, String paramString2)
  {
    synchronized (mLock)
    {
      awaitLoadedLocked();
      paramString1 = (String)mMap.get(paramString1);
      if (paramString1 == null) {
        paramString1 = paramString2;
      }
      return paramString1;
    }
  }
  
  public Set<String> getStringSet(String paramString, Set<String> paramSet)
  {
    synchronized (mLock)
    {
      awaitLoadedLocked();
      paramString = (Set)mMap.get(paramString);
      if (paramString != null) {
        paramSet = paramString;
      }
      return paramSet;
    }
  }
  
  public void registerOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener paramOnSharedPreferenceChangeListener)
  {
    synchronized (mLock)
    {
      mListeners.put(paramOnSharedPreferenceChangeListener, CONTENT);
      return;
    }
  }
  
  void startReloadIfChangedUnexpectedly()
  {
    synchronized (mLock)
    {
      if (!hasFileChangedUnexpectedly()) {
        return;
      }
      startLoadFromDisk();
      return;
    }
  }
  
  public void unregisterOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener paramOnSharedPreferenceChangeListener)
  {
    synchronized (mLock)
    {
      mListeners.remove(paramOnSharedPreferenceChangeListener);
      return;
    }
  }
  
  public final class EditorImpl
    implements SharedPreferences.Editor
  {
    @GuardedBy("mEditorLock")
    private boolean mClear = false;
    private final Object mEditorLock = new Object();
    @GuardedBy("mEditorLock")
    private final Map<String, Object> mModified = new HashMap();
    
    public EditorImpl() {}
    
    private SharedPreferencesImpl.MemoryCommitResult commitToMemory()
    {
      ArrayList localArrayList = null;
      HashSet localHashSet = null;
      synchronized (mLock)
      {
        if (mDiskWritesInFlight > 0)
        {
          localObject4 = SharedPreferencesImpl.this;
          localObject5 = new java/util/HashMap;
          ((HashMap)localObject5).<init>(mMap);
          SharedPreferencesImpl.access$402((SharedPreferencesImpl)localObject4, (Map)localObject5);
        }
        Object localObject5 = mMap;
        SharedPreferencesImpl.access$308(SharedPreferencesImpl.this);
        int i;
        if (mListeners.size() > 0) {
          i = 1;
        } else {
          i = 0;
        }
        if (i != 0)
        {
          localArrayList = new java/util/ArrayList;
          localArrayList.<init>();
          localHashSet = new java/util/HashSet;
          localHashSet.<init>(mListeners.keySet());
        }
        Object localObject4 = mEditorLock;
        int j = 0;
        int k = 0;
        try
        {
          if (mClear)
          {
            j = k;
            if (!((Map)localObject5).isEmpty())
            {
              j = 1;
              ((Map)localObject5).clear();
            }
            mClear = false;
          }
          Iterator localIterator = mModified.entrySet().iterator();
          while (localIterator.hasNext())
          {
            Object localObject6 = (Map.Entry)localIterator.next();
            String str = (String)((Map.Entry)localObject6).getKey();
            localObject6 = ((Map.Entry)localObject6).getValue();
            if ((localObject6 != this) && (localObject6 != null))
            {
              if (((Map)localObject5).containsKey(str))
              {
                Object localObject7 = ((Map)localObject5).get(str);
                if ((localObject7 != null) && (localObject7.equals(localObject6))) {
                  continue;
                }
              }
              ((Map)localObject5).put(str, localObject6);
            }
            else
            {
              if (!((Map)localObject5).containsKey(str)) {
                continue;
              }
              ((Map)localObject5).remove(str);
            }
            j = 1;
            if (i != 0) {
              localArrayList.add(str);
            }
          }
          mModified.clear();
          if (j != 0) {
            SharedPreferencesImpl.access$608(SharedPreferencesImpl.this);
          }
          long l = mCurrentMemoryStateGeneration;
          return new SharedPreferencesImpl.MemoryCommitResult(l, localArrayList, localHashSet, (Map)localObject5, null);
        }
        finally {}
      }
    }
    
    private void notifyListeners(SharedPreferencesImpl.MemoryCommitResult paramMemoryCommitResult)
    {
      if ((listeners != null) && (keysModified != null) && (keysModified.size() != 0))
      {
        if (Looper.myLooper() == Looper.getMainLooper()) {
          for (int i = keysModified.size() - 1; i >= 0; i--)
          {
            String str = (String)keysModified.get(i);
            Iterator localIterator = listeners.iterator();
            while (localIterator.hasNext())
            {
              SharedPreferences.OnSharedPreferenceChangeListener localOnSharedPreferenceChangeListener = (SharedPreferences.OnSharedPreferenceChangeListener)localIterator.next();
              if (localOnSharedPreferenceChangeListener != null) {
                localOnSharedPreferenceChangeListener.onSharedPreferenceChanged(SharedPreferencesImpl.this, str);
              }
            }
          }
        }
        ActivityThread.sMainThreadHandler.post(new _..Lambda.SharedPreferencesImpl.EditorImpl.3CAjkhzA131V3V_sLfP2uy0FWZ0(this, paramMemoryCommitResult));
        return;
      }
    }
    
    public void apply()
    {
      final long l = System.currentTimeMillis();
      final SharedPreferencesImpl.MemoryCommitResult localMemoryCommitResult = commitToMemory();
      Object localObject = new Runnable()
      {
        public void run()
        {
          try
          {
            localMemoryCommitResultwrittenToDiskLatch.await();
          }
          catch (InterruptedException localInterruptedException) {}
        }
      };
      QueuedWork.addFinisher((Runnable)localObject);
      localObject = new Runnable()
      {
        public void run()
        {
          val$awaitCommit.run();
          QueuedWork.removeFinisher(val$awaitCommit);
        }
      };
      SharedPreferencesImpl.this.enqueueDiskWrite(localMemoryCommitResult, (Runnable)localObject);
      notifyListeners(localMemoryCommitResult);
    }
    
    public SharedPreferences.Editor clear()
    {
      synchronized (mEditorLock)
      {
        mClear = true;
        return this;
      }
    }
    
    /* Error */
    public boolean commit()
    {
      // Byte code:
      //   0: aload_0
      //   1: invokespecial 217	android/app/SharedPreferencesImpl$EditorImpl:commitToMemory	()Landroid/app/SharedPreferencesImpl$MemoryCommitResult;
      //   4: astore_1
      //   5: aload_0
      //   6: getfield 28	android/app/SharedPreferencesImpl$EditorImpl:this$0	Landroid/app/SharedPreferencesImpl;
      //   9: aload_1
      //   10: aconst_null
      //   11: invokestatic 233	android/app/SharedPreferencesImpl:access$100	(Landroid/app/SharedPreferencesImpl;Landroid/app/SharedPreferencesImpl$MemoryCommitResult;Ljava/lang/Runnable;)V
      //   14: aload_1
      //   15: getfield 241	android/app/SharedPreferencesImpl$MemoryCommitResult:writtenToDiskLatch	Ljava/util/concurrent/CountDownLatch;
      //   18: invokevirtual 246	java/util/concurrent/CountDownLatch:await	()V
      //   21: aload_0
      //   22: aload_1
      //   23: invokespecial 165	android/app/SharedPreferencesImpl$EditorImpl:notifyListeners	(Landroid/app/SharedPreferencesImpl$MemoryCommitResult;)V
      //   26: aload_1
      //   27: getfield 249	android/app/SharedPreferencesImpl$MemoryCommitResult:writeToDiskResult	Z
      //   30: ireturn
      //   31: astore_1
      //   32: aload_1
      //   33: athrow
      //   34: astore_1
      //   35: iconst_0
      //   36: ireturn
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	37	0	this	EditorImpl
      //   4	23	1	localMemoryCommitResult	SharedPreferencesImpl.MemoryCommitResult
      //   31	2	1	localObject	Object
      //   34	1	1	localInterruptedException	InterruptedException
      // Exception table:
      //   from	to	target	type
      //   14	21	31	finally
      //   14	21	34	java/lang/InterruptedException
    }
    
    public SharedPreferences.Editor putBoolean(String paramString, boolean paramBoolean)
    {
      synchronized (mEditorLock)
      {
        mModified.put(paramString, Boolean.valueOf(paramBoolean));
        return this;
      }
    }
    
    public SharedPreferences.Editor putFloat(String paramString, float paramFloat)
    {
      synchronized (mEditorLock)
      {
        mModified.put(paramString, Float.valueOf(paramFloat));
        return this;
      }
    }
    
    public SharedPreferences.Editor putInt(String paramString, int paramInt)
    {
      synchronized (mEditorLock)
      {
        mModified.put(paramString, Integer.valueOf(paramInt));
        return this;
      }
    }
    
    public SharedPreferences.Editor putLong(String paramString, long paramLong)
    {
      synchronized (mEditorLock)
      {
        mModified.put(paramString, Long.valueOf(paramLong));
        return this;
      }
    }
    
    public SharedPreferences.Editor putString(String paramString1, String paramString2)
    {
      synchronized (mEditorLock)
      {
        mModified.put(paramString1, paramString2);
        return this;
      }
    }
    
    public SharedPreferences.Editor putStringSet(String paramString, Set<String> paramSet)
    {
      synchronized (mEditorLock)
      {
        Map localMap = mModified;
        if (paramSet == null) {
          paramSet = null;
        } else {
          paramSet = new HashSet(paramSet);
        }
        localMap.put(paramString, paramSet);
        return this;
      }
    }
    
    public SharedPreferences.Editor remove(String paramString)
    {
      synchronized (mEditorLock)
      {
        mModified.put(paramString, this);
        return this;
      }
    }
  }
  
  private static class MemoryCommitResult
  {
    final List<String> keysModified;
    final Set<SharedPreferences.OnSharedPreferenceChangeListener> listeners;
    final Map<String, Object> mapToWriteToDisk;
    final long memoryStateGeneration;
    boolean wasWritten = false;
    @GuardedBy("mWritingToDiskLock")
    volatile boolean writeToDiskResult = false;
    final CountDownLatch writtenToDiskLatch = new CountDownLatch(1);
    
    private MemoryCommitResult(long paramLong, List<String> paramList, Set<SharedPreferences.OnSharedPreferenceChangeListener> paramSet, Map<String, Object> paramMap)
    {
      memoryStateGeneration = paramLong;
      keysModified = paramList;
      listeners = paramSet;
      mapToWriteToDisk = paramMap;
    }
    
    void setDiskWriteResult(boolean paramBoolean1, boolean paramBoolean2)
    {
      wasWritten = paramBoolean1;
      writeToDiskResult = paramBoolean2;
      writtenToDiskLatch.countDown();
    }
  }
}
