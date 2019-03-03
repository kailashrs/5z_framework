package com.android.internal.os;

import android.util.IntArray;
import android.util.Slog;
import android.util.SparseArray;
import com.android.internal.annotations.VisibleForTesting;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.function.Consumer;

public class KernelUidCpuFreqTimeReader
  extends KernelUidCpuTimeReaderBase<Callback>
{
  private static final String TAG = KernelUidCpuFreqTimeReader.class.getSimpleName();
  private static final int TOTAL_READ_ERROR_COUNT = 5;
  static final String UID_TIMES_PROC_FILE = "/proc/uid_time_in_state";
  private boolean mAllUidTimesAvailable = true;
  private long[] mCpuFreqs;
  private int mCpuFreqsCount;
  private long[] mCurTimes;
  private long[] mDeltaTimes;
  private SparseArray<long[]> mLastUidCpuFreqTimeMs = new SparseArray();
  private boolean mPerClusterTimesAvailable;
  private final KernelCpuProcReader mProcReader;
  private int mReadErrorCounter;
  
  public KernelUidCpuFreqTimeReader()
  {
    mProcReader = KernelCpuProcReader.getFreqTimeReaderInstance();
  }
  
  @VisibleForTesting
  public KernelUidCpuFreqTimeReader(KernelCpuProcReader paramKernelCpuProcReader)
  {
    mProcReader = paramKernelCpuProcReader;
  }
  
  private IntArray extractClusterInfoFromProcFileFreqs()
  {
    IntArray localIntArray = new IntArray();
    int i = 0;
    for (int j = 0; j < mCpuFreqsCount; j++)
    {
      int k = i + 1;
      if (j + 1 != mCpuFreqsCount)
      {
        i = k;
        if (mCpuFreqs[(j + 1)] > mCpuFreqs[j]) {}
      }
      else
      {
        localIntArray.add(k);
        i = 0;
      }
    }
    return localIntArray;
  }
  
  private boolean getFreqTimeForUid(IntBuffer paramIntBuffer, long[] paramArrayOfLong)
  {
    boolean bool = true;
    for (int i = 0; i < mCpuFreqsCount; i++)
    {
      paramArrayOfLong[i] = (paramIntBuffer.get() * 10L);
      if (paramArrayOfLong[i] < 0L)
      {
        String str = TAG;
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Negative time from freq time proc: ");
        localStringBuilder.append(paramArrayOfLong[i]);
        Slog.e(str, localStringBuilder.toString());
        bool = false;
      }
    }
    return bool;
  }
  
  private void readImpl(Consumer<IntBuffer> paramConsumer)
  {
    synchronized (mProcReader)
    {
      Object localObject1 = mProcReader.readBytes();
      if ((localObject1 != null) && (((ByteBuffer)localObject1).remaining() > 4))
      {
        Object localObject2;
        if ((((ByteBuffer)localObject1).remaining() & 0x3) != 0)
        {
          localObject2 = TAG;
          paramConsumer = new java/lang/StringBuilder;
          paramConsumer.<init>();
          paramConsumer.append("Cannot parse freq time proc bytes to int: ");
          paramConsumer.append(((ByteBuffer)localObject1).remaining());
          Slog.wtf((String)localObject2, paramConsumer.toString());
          return;
        }
        localObject1 = ((ByteBuffer)localObject1).asIntBuffer();
        int i = ((IntBuffer)localObject1).get();
        if (i != mCpuFreqsCount)
        {
          paramConsumer = TAG;
          localObject1 = new java/lang/StringBuilder;
          ((StringBuilder)localObject1).<init>();
          ((StringBuilder)localObject1).append("Cpu freqs expect ");
          ((StringBuilder)localObject1).append(mCpuFreqsCount);
          ((StringBuilder)localObject1).append(" , got ");
          ((StringBuilder)localObject1).append(i);
          Slog.wtf(paramConsumer, ((StringBuilder)localObject1).toString());
          return;
        }
        if (((IntBuffer)localObject1).remaining() % (i + 1) != 0)
        {
          paramConsumer = TAG;
          localObject2 = new java/lang/StringBuilder;
          ((StringBuilder)localObject2).<init>();
          ((StringBuilder)localObject2).append("Freq time format error: ");
          ((StringBuilder)localObject2).append(((IntBuffer)localObject1).remaining());
          ((StringBuilder)localObject2).append(" / ");
          ((StringBuilder)localObject2).append(i + 1);
          Slog.wtf(paramConsumer, ((StringBuilder)localObject2).toString());
          return;
        }
        int j = ((IntBuffer)localObject1).remaining() / (i + 1);
        for (i = 0; i < j; i++) {
          paramConsumer.accept(localObject1);
        }
        return;
      }
      return;
    }
  }
  
  public boolean allUidTimesAvailable()
  {
    return mAllUidTimesAvailable;
  }
  
  public SparseArray<long[]> getAllUidCpuFreqTimeMs()
  {
    return mLastUidCpuFreqTimeMs;
  }
  
  public boolean perClusterTimesAvailable()
  {
    return mPerClusterTimesAvailable;
  }
  
  public void readAbsolute(Callback paramCallback)
  {
    readImpl(new _..Lambda.KernelUidCpuFreqTimeReader.s7iJKg0yjXXtqM4hsU8GS_gavIY(this, paramCallback));
  }
  
  @VisibleForTesting
  public void readDeltaImpl(Callback paramCallback)
  {
    if (mCpuFreqs == null) {
      return;
    }
    readImpl(new _..Lambda.KernelUidCpuFreqTimeReader._LfRKir9FA4B4VL15YGHagRZaR8(this, paramCallback));
  }
  
  /* Error */
  public long[] readFreqs(PowerProfile paramPowerProfile)
  {
    // Byte code:
    //   0: aload_1
    //   1: invokestatic 211	com/android/internal/util/Preconditions:checkNotNull	(Ljava/lang/Object;)Ljava/lang/Object;
    //   4: pop
    //   5: aload_0
    //   6: getfield 71	com/android/internal/os/KernelUidCpuFreqTimeReader:mCpuFreqs	[J
    //   9: ifnull +8 -> 17
    //   12: aload_0
    //   13: getfield 71	com/android/internal/os/KernelUidCpuFreqTimeReader:mCpuFreqs	[J
    //   16: areturn
    //   17: aload_0
    //   18: getfield 51	com/android/internal/os/KernelUidCpuFreqTimeReader:mAllUidTimesAvailable	Z
    //   21: ifne +5 -> 26
    //   24: aconst_null
    //   25: areturn
    //   26: invokestatic 216	android/os/StrictMode:allowThreadDiskReadsMask	()I
    //   29: istore_2
    //   30: new 218	java/io/BufferedReader
    //   33: astore_3
    //   34: new 220	java/io/FileReader
    //   37: astore 4
    //   39: aload 4
    //   41: ldc 16
    //   43: invokespecial 223	java/io/FileReader:<init>	(Ljava/lang/String;)V
    //   46: aload_3
    //   47: aload 4
    //   49: invokespecial 226	java/io/BufferedReader:<init>	(Ljava/io/Reader;)V
    //   52: aload_0
    //   53: aload_3
    //   54: aload_1
    //   55: invokevirtual 229	com/android/internal/os/KernelUidCpuFreqTimeReader:readFreqs	(Ljava/io/BufferedReader;Lcom/android/internal/os/PowerProfile;)[J
    //   58: astore_1
    //   59: aload_3
    //   60: invokevirtual 232	java/io/BufferedReader:close	()V
    //   63: iload_2
    //   64: invokestatic 235	android/os/StrictMode:setThreadPolicyMask	(I)V
    //   67: aload_1
    //   68: areturn
    //   69: astore 4
    //   71: aconst_null
    //   72: astore_1
    //   73: goto +8 -> 81
    //   76: astore_1
    //   77: aload_1
    //   78: athrow
    //   79: astore 4
    //   81: aload_1
    //   82: ifnull +19 -> 101
    //   85: aload_3
    //   86: invokevirtual 232	java/io/BufferedReader:close	()V
    //   89: goto +16 -> 105
    //   92: astore_3
    //   93: aload_1
    //   94: aload_3
    //   95: invokevirtual 239	java/lang/Throwable:addSuppressed	(Ljava/lang/Throwable;)V
    //   98: goto +7 -> 105
    //   101: aload_3
    //   102: invokevirtual 232	java/io/BufferedReader:close	()V
    //   105: aload 4
    //   107: athrow
    //   108: astore_1
    //   109: goto +71 -> 180
    //   112: astore_3
    //   113: aload_0
    //   114: getfield 241	com/android/internal/os/KernelUidCpuFreqTimeReader:mReadErrorCounter	I
    //   117: iconst_1
    //   118: iadd
    //   119: istore 5
    //   121: aload_0
    //   122: iload 5
    //   124: putfield 241	com/android/internal/os/KernelUidCpuFreqTimeReader:mReadErrorCounter	I
    //   127: iload 5
    //   129: iconst_5
    //   130: if_icmplt +8 -> 138
    //   133: aload_0
    //   134: iconst_0
    //   135: putfield 51	com/android/internal/os/KernelUidCpuFreqTimeReader:mAllUidTimesAvailable	Z
    //   138: getstatic 40	com/android/internal/os/KernelUidCpuFreqTimeReader:TAG	Ljava/lang/String;
    //   141: astore 4
    //   143: new 87	java/lang/StringBuilder
    //   146: astore_1
    //   147: aload_1
    //   148: invokespecial 88	java/lang/StringBuilder:<init>	()V
    //   151: aload_1
    //   152: ldc -13
    //   154: invokevirtual 94	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   157: pop
    //   158: aload_1
    //   159: aload_3
    //   160: invokevirtual 246	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   163: pop
    //   164: aload 4
    //   166: aload_1
    //   167: invokevirtual 100	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   170: invokestatic 106	android/util/Slog:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   173: pop
    //   174: iload_2
    //   175: invokestatic 235	android/os/StrictMode:setThreadPolicyMask	(I)V
    //   178: aconst_null
    //   179: areturn
    //   180: iload_2
    //   181: invokestatic 235	android/os/StrictMode:setThreadPolicyMask	(I)V
    //   184: aload_1
    //   185: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	186	0	this	KernelUidCpuFreqTimeReader
    //   0	186	1	paramPowerProfile	PowerProfile
    //   29	152	2	i	int
    //   33	53	3	localBufferedReader	BufferedReader
    //   92	10	3	localThrowable	Throwable
    //   112	48	3	localIOException	IOException
    //   37	11	4	localFileReader	java.io.FileReader
    //   69	1	4	localObject1	Object
    //   79	27	4	localObject2	Object
    //   141	24	4	str	String
    //   119	12	5	j	int
    // Exception table:
    //   from	to	target	type
    //   52	59	69	finally
    //   52	59	76	java/lang/Throwable
    //   77	79	79	finally
    //   85	89	92	java/lang/Throwable
    //   30	52	108	finally
    //   59	63	108	finally
    //   85	89	108	finally
    //   93	98	108	finally
    //   101	105	108	finally
    //   105	108	108	finally
    //   113	127	108	finally
    //   133	138	108	finally
    //   138	174	108	finally
    //   30	52	112	java/io/IOException
    //   59	63	112	java/io/IOException
    //   85	89	112	java/io/IOException
    //   93	98	112	java/io/IOException
    //   101	105	112	java/io/IOException
    //   105	108	112	java/io/IOException
  }
  
  @VisibleForTesting
  public long[] readFreqs(BufferedReader paramBufferedReader, PowerProfile paramPowerProfile)
    throws IOException
  {
    paramBufferedReader = paramBufferedReader.readLine();
    if (paramBufferedReader == null) {
      return null;
    }
    paramBufferedReader = paramBufferedReader.split(" ");
    mCpuFreqsCount = (paramBufferedReader.length - 1);
    mCpuFreqs = new long[mCpuFreqsCount];
    mCurTimes = new long[mCpuFreqsCount];
    mDeltaTimes = new long[mCpuFreqsCount];
    for (int i = 0; i < mCpuFreqsCount; i++) {
      mCpuFreqs[i] = Long.parseLong(paramBufferedReader[(i + 1)], 10);
    }
    paramBufferedReader = extractClusterInfoFromProcFileFreqs();
    int j = paramPowerProfile.getNumCpuClusters();
    if (paramBufferedReader.size() == j)
    {
      mPerClusterTimesAvailable = true;
      for (i = 0; i < j; i++) {
        if (paramBufferedReader.get(i) != paramPowerProfile.getNumSpeedStepsInCpuCluster(i))
        {
          mPerClusterTimesAvailable = false;
          break;
        }
      }
    }
    mPerClusterTimesAvailable = false;
    paramPowerProfile = TAG;
    paramBufferedReader = new StringBuilder();
    paramBufferedReader.append("mPerClusterTimesAvailable=");
    paramBufferedReader.append(mPerClusterTimesAvailable);
    Slog.i(paramPowerProfile, paramBufferedReader.toString());
    return mCpuFreqs;
  }
  
  public void removeUid(int paramInt)
  {
    mLastUidCpuFreqTimeMs.delete(paramInt);
  }
  
  public void removeUidsInRange(int paramInt1, int paramInt2)
  {
    mLastUidCpuFreqTimeMs.put(paramInt1, null);
    mLastUidCpuFreqTimeMs.put(paramInt2, null);
    paramInt1 = mLastUidCpuFreqTimeMs.indexOfKey(paramInt1);
    paramInt2 = mLastUidCpuFreqTimeMs.indexOfKey(paramInt2);
    mLastUidCpuFreqTimeMs.removeAtRange(paramInt1, paramInt2 - paramInt1 + 1);
  }
  
  public static abstract interface Callback
    extends KernelUidCpuTimeReaderBase.Callback
  {
    public abstract void onUidCpuFreqTime(int paramInt, long[] paramArrayOfLong);
  }
}
