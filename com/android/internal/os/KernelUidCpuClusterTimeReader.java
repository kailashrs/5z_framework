package com.android.internal.os;

import android.util.Slog;
import android.util.SparseArray;
import com.android.internal.annotations.VisibleForTesting;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.function.Consumer;

public class KernelUidCpuClusterTimeReader
  extends KernelUidCpuTimeReaderBase<Callback>
{
  private static final String TAG = KernelUidCpuClusterTimeReader.class.getSimpleName();
  private double[] mCurTime;
  private long[] mCurTimeRounded;
  private long[] mDeltaTime;
  private SparseArray<double[]> mLastUidPolicyTimeMs = new SparseArray();
  private int mNumClusters = -1;
  private int mNumCores;
  private int[] mNumCoresOnCluster;
  private final KernelCpuProcReader mProcReader;
  
  public KernelUidCpuClusterTimeReader()
  {
    mProcReader = KernelCpuProcReader.getClusterTimeReaderInstance();
  }
  
  @VisibleForTesting
  public KernelUidCpuClusterTimeReader(KernelCpuProcReader paramKernelCpuProcReader)
  {
    mProcReader = paramKernelCpuProcReader;
  }
  
  private boolean readCoreInfo(IntBuffer paramIntBuffer, int paramInt)
  {
    Object localObject = new int[paramInt];
    int i = 0;
    for (int j = 0; j < paramInt; j++)
    {
      localObject[j] = paramIntBuffer.get();
      i += localObject[j];
    }
    if (i <= 0)
    {
      paramIntBuffer = TAG;
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Invalid # cores from cluster time proc file: ");
      ((StringBuilder)localObject).append(i);
      Slog.e(paramIntBuffer, ((StringBuilder)localObject).toString());
      return false;
    }
    mNumCores = i;
    mNumCoresOnCluster = ((int[])localObject);
    mCurTime = new double[paramInt];
    mDeltaTime = new long[paramInt];
    mCurTimeRounded = new long[paramInt];
    return true;
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
          paramConsumer.append("Cannot parse cluster time proc bytes to int: ");
          paramConsumer.append(((ByteBuffer)localObject1).remaining());
          Slog.wtf((String)localObject2, paramConsumer.toString());
          return;
        }
        localObject1 = ((ByteBuffer)localObject1).asIntBuffer();
        int i = ((IntBuffer)localObject1).get();
        if (i <= 0)
        {
          localObject1 = TAG;
          paramConsumer = new java/lang/StringBuilder;
          paramConsumer.<init>();
          paramConsumer.append("Cluster time format error: ");
          paramConsumer.append(i);
          Slog.wtf((String)localObject1, paramConsumer.toString());
          return;
        }
        if (mNumClusters == -1) {
          mNumClusters = i;
        }
        if (((IntBuffer)localObject1).remaining() < i)
        {
          paramConsumer = TAG;
          localObject2 = new java/lang/StringBuilder;
          ((StringBuilder)localObject2).<init>();
          ((StringBuilder)localObject2).append("Too few data left in the buffer: ");
          ((StringBuilder)localObject2).append(((IntBuffer)localObject1).remaining());
          Slog.wtf(paramConsumer, ((StringBuilder)localObject2).toString());
          return;
        }
        if (mNumCores <= 0)
        {
          if (readCoreInfo((IntBuffer)localObject1, i)) {}
        }
        else {
          ((IntBuffer)localObject1).position(((IntBuffer)localObject1).position() + i);
        }
        if (((IntBuffer)localObject1).remaining() % (mNumCores + 1) != 0)
        {
          paramConsumer = TAG;
          localObject2 = new java/lang/StringBuilder;
          ((StringBuilder)localObject2).<init>();
          ((StringBuilder)localObject2).append("Cluster time format error: ");
          ((StringBuilder)localObject2).append(((IntBuffer)localObject1).remaining());
          ((StringBuilder)localObject2).append(" / ");
          ((StringBuilder)localObject2).append(mNumCores + 1);
          Slog.wtf(paramConsumer, ((StringBuilder)localObject2).toString());
          return;
        }
        int j = ((IntBuffer)localObject1).remaining() / (mNumCores + 1);
        for (i = 0; i < j; i++) {
          paramConsumer.accept(localObject1);
        }
        return;
      }
      return;
    }
  }
  
  private boolean sumClusterTime(IntBuffer paramIntBuffer, double[] paramArrayOfDouble)
  {
    boolean bool = true;
    for (int i = 0; i < mNumClusters; i++)
    {
      paramArrayOfDouble[i] = 0.0D;
      for (int j = 1; j <= mNumCoresOnCluster[i]; j++)
      {
        int k = paramIntBuffer.get();
        if (k < 0)
        {
          String str = TAG;
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("Negative time from cluster time proc: ");
          localStringBuilder.append(k);
          Slog.e(str, localStringBuilder.toString());
          bool = false;
        }
        paramArrayOfDouble[i] += k * 10.0D / j;
      }
    }
    return bool;
  }
  
  public void readAbsolute(Callback paramCallback)
  {
    readImpl(new _..Lambda.KernelUidCpuClusterTimeReader.SvNbuRWT162Eb4ur1GVE0r4GiDo(this, paramCallback));
  }
  
  protected void readDeltaImpl(Callback paramCallback)
  {
    readImpl(new _..Lambda.KernelUidCpuClusterTimeReader.j4vHMa0qvl5KRBiWr_LkFJbasC8(this, paramCallback));
  }
  
  public void removeUid(int paramInt)
  {
    mLastUidPolicyTimeMs.delete(paramInt);
  }
  
  public void removeUidsInRange(int paramInt1, int paramInt2)
  {
    mLastUidPolicyTimeMs.put(paramInt1, null);
    mLastUidPolicyTimeMs.put(paramInt2, null);
    paramInt1 = mLastUidPolicyTimeMs.indexOfKey(paramInt1);
    paramInt2 = mLastUidPolicyTimeMs.indexOfKey(paramInt2);
    mLastUidPolicyTimeMs.removeAtRange(paramInt1, paramInt2 - paramInt1 + 1);
  }
  
  public static abstract interface Callback
    extends KernelUidCpuTimeReaderBase.Callback
  {
    public abstract void onUidCpuPolicyTime(int paramInt, long[] paramArrayOfLong);
  }
}
