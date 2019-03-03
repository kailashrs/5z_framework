package com.android.internal.os;

import android.util.Slog;
import android.util.SparseArray;
import com.android.internal.annotations.VisibleForTesting;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.function.Consumer;

public class KernelUidCpuActiveTimeReader
  extends KernelUidCpuTimeReaderBase<Callback>
{
  private static final String TAG = KernelUidCpuActiveTimeReader.class.getSimpleName();
  private int mCores;
  private SparseArray<Double> mLastUidCpuActiveTimeMs = new SparseArray();
  private final KernelCpuProcReader mProcReader;
  
  public KernelUidCpuActiveTimeReader()
  {
    mProcReader = KernelCpuProcReader.getActiveTimeReaderInstance();
  }
  
  @VisibleForTesting
  public KernelUidCpuActiveTimeReader(KernelCpuProcReader paramKernelCpuProcReader)
  {
    mProcReader = paramKernelCpuProcReader;
  }
  
  private void readImpl(Consumer<IntBuffer> paramConsumer)
  {
    synchronized (mProcReader)
    {
      Object localObject = mProcReader.readBytes();
      if ((localObject != null) && (((ByteBuffer)localObject).remaining() > 4))
      {
        if ((((ByteBuffer)localObject).remaining() & 0x3) != 0)
        {
          str = TAG;
          paramConsumer = new java/lang/StringBuilder;
          paramConsumer.<init>();
          paramConsumer.append("Cannot parse active time proc bytes to int: ");
          paramConsumer.append(((ByteBuffer)localObject).remaining());
          Slog.wtf(str, paramConsumer.toString());
          return;
        }
        localObject = ((ByteBuffer)localObject).asIntBuffer();
        int i = ((IntBuffer)localObject).get();
        if ((mCores != 0) && (i != mCores))
        {
          paramConsumer = TAG;
          localObject = new java/lang/StringBuilder;
          ((StringBuilder)localObject).<init>();
          ((StringBuilder)localObject).append("Cpu active time wrong # cores: ");
          ((StringBuilder)localObject).append(i);
          Slog.wtf(paramConsumer, ((StringBuilder)localObject).toString());
          return;
        }
        mCores = i;
        if ((i > 0) && (((IntBuffer)localObject).remaining() % (i + 1) == 0))
        {
          int j = ((IntBuffer)localObject).remaining() / (i + 1);
          for (i = 0; i < j; i++) {
            paramConsumer.accept(localObject);
          }
          return;
        }
        String str = TAG;
        paramConsumer = new java/lang/StringBuilder;
        paramConsumer.<init>();
        paramConsumer.append("Cpu active time format error: ");
        paramConsumer.append(((IntBuffer)localObject).remaining());
        paramConsumer.append(" / ");
        paramConsumer.append(i + 1);
        Slog.wtf(str, paramConsumer.toString());
        return;
      }
      return;
    }
  }
  
  private double sumActiveTime(IntBuffer paramIntBuffer)
  {
    double d = 0.0D;
    int i = 0;
    for (int j = 1; j <= mCores; j++)
    {
      int k = paramIntBuffer.get();
      if (k < 0)
      {
        String str = TAG;
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Negative time from active time proc: ");
        localStringBuilder.append(k);
        Slog.e(str, localStringBuilder.toString());
        i = 1;
      }
      else
      {
        d += k * 10.0D / j;
      }
    }
    if (i != 0) {
      d = -1.0D;
    }
    return d;
  }
  
  public void readAbsolute(Callback paramCallback)
  {
    readImpl(new _..Lambda.KernelUidCpuActiveTimeReader.uXm3GBhF7PBpo0hLrva14EQYjPA(this, paramCallback));
  }
  
  protected void readDeltaImpl(Callback paramCallback)
  {
    readImpl(new _..Lambda.KernelUidCpuActiveTimeReader.bd1LhtH6p3uJgMUQoWfE2Qs8bRc(this, paramCallback));
  }
  
  public void removeUid(int paramInt)
  {
    mLastUidCpuActiveTimeMs.delete(paramInt);
  }
  
  public void removeUidsInRange(int paramInt1, int paramInt2)
  {
    mLastUidCpuActiveTimeMs.put(paramInt1, null);
    mLastUidCpuActiveTimeMs.put(paramInt2, null);
    paramInt1 = mLastUidCpuActiveTimeMs.indexOfKey(paramInt1);
    paramInt2 = mLastUidCpuActiveTimeMs.indexOfKey(paramInt2);
    mLastUidCpuActiveTimeMs.removeAtRange(paramInt1, paramInt2 - paramInt1 + 1);
  }
  
  public static abstract interface Callback
    extends KernelUidCpuTimeReaderBase.Callback
  {
    public abstract void onUidCpuActiveTime(int paramInt, long paramLong);
  }
}
