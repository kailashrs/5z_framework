package com.android.internal.os;

import android.util.SparseArray;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.annotations.VisibleForTesting.Visibility;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

@VisibleForTesting(visibility=VisibleForTesting.Visibility.PACKAGE)
public class KernelSingleUidTimeReader
{
  @VisibleForTesting
  public static final int TOTAL_READ_ERROR_COUNT = 5;
  private final boolean DBG = false;
  private final String PROC_FILE_DIR = "/proc/uid/";
  private final String PROC_FILE_NAME = "/time_in_state";
  private final String TAG = KernelUidCpuFreqTimeReader.class.getName();
  @GuardedBy("this")
  private final int mCpuFreqsCount;
  @GuardedBy("this")
  private boolean mCpuFreqsCountVerified;
  @GuardedBy("this")
  private boolean mHasStaleData;
  private final Injector mInjector;
  @GuardedBy("this")
  private SparseArray<long[]> mLastUidCpuTimeMs = new SparseArray();
  @GuardedBy("this")
  private int mReadErrorCounter;
  @GuardedBy("this")
  private boolean mSingleUidCpuTimesAvailable = true;
  
  KernelSingleUidTimeReader(int paramInt)
  {
    this(paramInt, new Injector());
  }
  
  public KernelSingleUidTimeReader(int paramInt, Injector paramInjector)
  {
    mInjector = paramInjector;
    mCpuFreqsCount = paramInt;
    if (mCpuFreqsCount == 0) {
      mSingleUidCpuTimesAvailable = false;
    }
  }
  
  private long[] readCpuTimesFromByteBuffer(ByteBuffer paramByteBuffer)
  {
    long[] arrayOfLong = new long[mCpuFreqsCount];
    for (int i = 0; i < mCpuFreqsCount; i++) {
      arrayOfLong[i] = (paramByteBuffer.getLong() * 10L);
    }
    return arrayOfLong;
  }
  
  private void verifyCpuFreqsCount(int paramInt, String paramString)
  {
    paramInt /= 8;
    if (mCpuFreqsCount == paramInt)
    {
      mCpuFreqsCountVerified = true;
      return;
    }
    mSingleUidCpuTimesAvailable = false;
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Freq count didn't match,count from /proc/uid_time_in_state=");
    localStringBuilder.append(mCpuFreqsCount);
    localStringBuilder.append(", butcount from ");
    localStringBuilder.append(paramString);
    localStringBuilder.append("=");
    localStringBuilder.append(paramInt);
    throw new IllegalStateException(localStringBuilder.toString());
  }
  
  public long[] computeDelta(int paramInt, long[] paramArrayOfLong)
  {
    try
    {
      if (!mSingleUidCpuTimesAvailable) {
        return null;
      }
      long[] arrayOfLong = getDeltaLocked((long[])mLastUidCpuTimeMs.get(paramInt), paramArrayOfLong);
      if (arrayOfLong == null) {
        return null;
      }
      int i = 0;
      int k;
      for (int j = arrayOfLong.length - 1;; j--)
      {
        k = i;
        if (j < 0) {
          break;
        }
        if (arrayOfLong[j] > 0L)
        {
          k = 1;
          break;
        }
      }
      if (k != 0)
      {
        mLastUidCpuTimeMs.put(paramInt, paramArrayOfLong);
        return arrayOfLong;
      }
      return null;
    }
    finally {}
  }
  
  @GuardedBy("this")
  @VisibleForTesting(visibility=VisibleForTesting.Visibility.PACKAGE)
  public long[] getDeltaLocked(long[] paramArrayOfLong1, long[] paramArrayOfLong2)
  {
    for (int i = paramArrayOfLong2.length - 1; i >= 0; i--) {
      if (paramArrayOfLong2[i] < 0L) {
        return null;
      }
    }
    if (paramArrayOfLong1 == null) {
      return paramArrayOfLong2;
    }
    long[] arrayOfLong = new long[paramArrayOfLong2.length];
    for (i = paramArrayOfLong2.length - 1; i >= 0; i--)
    {
      paramArrayOfLong2[i] -= paramArrayOfLong1[i];
      if (arrayOfLong[i] < 0L) {
        return null;
      }
    }
    return arrayOfLong;
  }
  
  @VisibleForTesting
  public SparseArray<long[]> getLastUidCpuTimeMs()
  {
    return mLastUidCpuTimeMs;
  }
  
  public boolean hasStaleData()
  {
    try
    {
      boolean bool = mHasStaleData;
      return bool;
    }
    finally {}
  }
  
  public void markDataAsStale(boolean paramBoolean)
  {
    try
    {
      mHasStaleData = paramBoolean;
      return;
    }
    finally {}
  }
  
  /* Error */
  public long[] readDeltaMs(int paramInt)
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield 70	com/android/internal/os/KernelSingleUidTimeReader:mSingleUidCpuTimesAvailable	Z
    //   6: ifne +7 -> 13
    //   9: aload_0
    //   10: monitorexit
    //   11: aconst_null
    //   12: areturn
    //   13: new 90	java/lang/StringBuilder
    //   16: astore_2
    //   17: aload_2
    //   18: ldc 57
    //   20: invokespecial 144	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   23: aload_2
    //   24: iload_1
    //   25: invokevirtual 100	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   28: pop
    //   29: aload_2
    //   30: ldc 61
    //   32: invokevirtual 97	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   35: pop
    //   36: aload_2
    //   37: invokevirtual 109	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   40: astore_2
    //   41: aload_0
    //   42: getfield 72	com/android/internal/os/KernelSingleUidTimeReader:mInjector	Lcom/android/internal/os/KernelSingleUidTimeReader$Injector;
    //   45: aload_2
    //   46: invokevirtual 148	com/android/internal/os/KernelSingleUidTimeReader$Injector:readData	(Ljava/lang/String;)[B
    //   49: astore_3
    //   50: aload_0
    //   51: getfield 88	com/android/internal/os/KernelSingleUidTimeReader:mCpuFreqsCountVerified	Z
    //   54: ifne +10 -> 64
    //   57: aload_0
    //   58: aload_3
    //   59: arraylength
    //   60: aload_2
    //   61: invokespecial 150	com/android/internal/os/KernelSingleUidTimeReader:verifyCpuFreqsCount	(ILjava/lang/String;)V
    //   64: aload_3
    //   65: invokestatic 154	java/nio/ByteBuffer:wrap	([B)Ljava/nio/ByteBuffer;
    //   68: astore_2
    //   69: aload_2
    //   70: invokestatic 160	java/nio/ByteOrder:nativeOrder	()Ljava/nio/ByteOrder;
    //   73: invokevirtual 164	java/nio/ByteBuffer:order	(Ljava/nio/ByteOrder;)Ljava/nio/ByteBuffer;
    //   76: pop
    //   77: aload_0
    //   78: aload_2
    //   79: invokespecial 166	com/android/internal/os/KernelSingleUidTimeReader:readCpuTimesFromByteBuffer	(Ljava/nio/ByteBuffer;)[J
    //   82: astore_2
    //   83: aload_0
    //   84: iload_1
    //   85: aload_2
    //   86: invokevirtual 168	com/android/internal/os/KernelSingleUidTimeReader:computeDelta	(I[J)[J
    //   89: astore_2
    //   90: aload_0
    //   91: monitorexit
    //   92: aload_2
    //   93: areturn
    //   94: astore_2
    //   95: aload_0
    //   96: getfield 170	com/android/internal/os/KernelSingleUidTimeReader:mReadErrorCounter	I
    //   99: iconst_1
    //   100: iadd
    //   101: istore_1
    //   102: aload_0
    //   103: iload_1
    //   104: putfield 170	com/android/internal/os/KernelSingleUidTimeReader:mReadErrorCounter	I
    //   107: iload_1
    //   108: iconst_5
    //   109: if_icmplt +8 -> 117
    //   112: aload_0
    //   113: iconst_0
    //   114: putfield 70	com/android/internal/os/KernelSingleUidTimeReader:mSingleUidCpuTimesAvailable	Z
    //   117: aload_0
    //   118: monitorexit
    //   119: aconst_null
    //   120: areturn
    //   121: astore_2
    //   122: aload_0
    //   123: monitorexit
    //   124: aload_2
    //   125: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	126	0	this	KernelSingleUidTimeReader
    //   0	126	1	paramInt	int
    //   16	77	2	localObject1	Object
    //   94	1	2	localException	Exception
    //   121	4	2	localObject2	Object
    //   49	16	3	arrayOfByte	byte[]
    // Exception table:
    //   from	to	target	type
    //   41	64	94	java/lang/Exception
    //   64	83	94	java/lang/Exception
    //   2	11	121	finally
    //   13	41	121	finally
    //   41	64	121	finally
    //   64	83	121	finally
    //   83	92	121	finally
    //   95	107	121	finally
    //   112	117	121	finally
    //   117	119	121	finally
    //   122	124	121	finally
  }
  
  public void removeUid(int paramInt)
  {
    try
    {
      mLastUidCpuTimeMs.delete(paramInt);
      return;
    }
    finally {}
  }
  
  public void removeUidsInRange(int paramInt1, int paramInt2)
  {
    if (paramInt2 < paramInt1) {
      return;
    }
    try
    {
      mLastUidCpuTimeMs.put(paramInt1, null);
      mLastUidCpuTimeMs.put(paramInt2, null);
      paramInt1 = mLastUidCpuTimeMs.indexOfKey(paramInt1);
      paramInt2 = mLastUidCpuTimeMs.indexOfKey(paramInt2);
      mLastUidCpuTimeMs.removeAtRange(paramInt1, paramInt2 - paramInt1 + 1);
      return;
    }
    finally {}
  }
  
  public void setAllUidsCpuTimesMs(SparseArray<long[]> paramSparseArray)
  {
    try
    {
      mLastUidCpuTimeMs.clear();
      for (int i = paramSparseArray.size() - 1; i >= 0; i--)
      {
        long[] arrayOfLong = (long[])paramSparseArray.valueAt(i);
        if (arrayOfLong != null) {
          mLastUidCpuTimeMs.put(paramSparseArray.keyAt(i), (long[])arrayOfLong.clone());
        }
      }
      return;
    }
    finally {}
  }
  
  @VisibleForTesting
  public void setSingleUidCpuTimesAvailable(boolean paramBoolean)
  {
    mSingleUidCpuTimesAvailable = paramBoolean;
  }
  
  public boolean singleUidCpuTimesAvailable()
  {
    return mSingleUidCpuTimesAvailable;
  }
  
  @VisibleForTesting
  public static class Injector
  {
    public Injector() {}
    
    public byte[] readData(String paramString)
      throws IOException
    {
      return Files.readAllBytes(Paths.get(paramString, new String[0]));
    }
  }
}
