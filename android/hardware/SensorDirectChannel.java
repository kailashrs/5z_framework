package android.hardware;

import android.os.MemoryFile;
import dalvik.system.CloseGuard;
import java.io.FileDescriptor;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.nio.channels.Channel;
import java.util.concurrent.atomic.AtomicBoolean;

public final class SensorDirectChannel
  implements Channel
{
  public static final int RATE_FAST = 2;
  public static final int RATE_NORMAL = 1;
  public static final int RATE_STOP = 0;
  public static final int RATE_VERY_FAST = 3;
  public static final int TYPE_HARDWARE_BUFFER = 2;
  public static final int TYPE_MEMORY_FILE = 1;
  private final CloseGuard mCloseGuard = CloseGuard.get();
  private final AtomicBoolean mClosed = new AtomicBoolean();
  private final SensorManager mManager;
  private final int mNativeHandle;
  private final long mSize;
  private final int mType;
  
  SensorDirectChannel(SensorManager paramSensorManager, int paramInt1, int paramInt2, long paramLong)
  {
    mManager = paramSensorManager;
    mNativeHandle = paramInt1;
    mType = paramInt2;
    mSize = paramLong;
    mCloseGuard.open("SensorDirectChannel");
  }
  
  static long[] encodeData(MemoryFile paramMemoryFile)
  {
    int i;
    try
    {
      i = paramMemoryFile.getFileDescriptor().getInt$();
    }
    catch (IOException paramMemoryFile)
    {
      i = -1;
    }
    return new long[] { 1L, 0L, i };
  }
  
  public void close()
  {
    if (mClosed.compareAndSet(false, true))
    {
      mCloseGuard.close();
      mManager.destroyDirectChannel(this);
    }
  }
  
  public int configure(Sensor paramSensor, int paramInt)
  {
    return mManager.configureDirectChannelImpl(this, paramSensor, paramInt);
  }
  
  protected void finalize()
    throws Throwable
  {
    try
    {
      if (mCloseGuard != null) {
        mCloseGuard.warnIfOpen();
      }
      close();
      return;
    }
    finally
    {
      super.finalize();
    }
  }
  
  int getNativeHandle()
  {
    return mNativeHandle;
  }
  
  public boolean isOpen()
  {
    return mClosed.get() ^ true;
  }
  
  @Deprecated
  public boolean isValid()
  {
    return isOpen();
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface MemoryType {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface RateLevel {}
}
