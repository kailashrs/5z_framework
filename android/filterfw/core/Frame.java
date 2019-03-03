package android.filterfw.core;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import java.nio.ByteBuffer;

public abstract class Frame
{
  public static final int NO_BINDING = 0;
  public static final long TIMESTAMP_NOT_SET = -2L;
  public static final long TIMESTAMP_UNKNOWN = -1L;
  private long mBindingId = 0L;
  private int mBindingType = 0;
  private FrameFormat mFormat;
  private FrameManager mFrameManager;
  private boolean mReadOnly = false;
  private int mRefCount = 1;
  private boolean mReusable = false;
  private long mTimestamp = -2L;
  
  Frame(FrameFormat paramFrameFormat, FrameManager paramFrameManager)
  {
    mFormat = paramFrameFormat.mutableCopy();
    mFrameManager = paramFrameManager;
  }
  
  Frame(FrameFormat paramFrameFormat, FrameManager paramFrameManager, int paramInt, long paramLong)
  {
    mFormat = paramFrameFormat.mutableCopy();
    mFrameManager = paramFrameManager;
    mBindingType = paramInt;
    mBindingId = paramLong;
  }
  
  protected static Bitmap convertBitmapToRGBA(Bitmap paramBitmap)
  {
    if (paramBitmap.getConfig() == Bitmap.Config.ARGB_8888) {
      return paramBitmap;
    }
    paramBitmap = paramBitmap.copy(Bitmap.Config.ARGB_8888, false);
    if (paramBitmap != null)
    {
      if (paramBitmap.getRowBytes() == paramBitmap.getWidth() * 4) {
        return paramBitmap;
      }
      throw new RuntimeException("Unsupported row byte count in bitmap!");
    }
    throw new RuntimeException("Error converting bitmap to RGBA!");
  }
  
  protected void assertFrameMutable()
  {
    if (!isReadOnly()) {
      return;
    }
    throw new RuntimeException("Attempting to modify read-only frame!");
  }
  
  final int decRefCount()
  {
    mRefCount -= 1;
    return mRefCount;
  }
  
  public long getBindingId()
  {
    return mBindingId;
  }
  
  public int getBindingType()
  {
    return mBindingType;
  }
  
  public abstract Bitmap getBitmap();
  
  public int getCapacity()
  {
    return getFormat().getSize();
  }
  
  public abstract ByteBuffer getData();
  
  public abstract float[] getFloats();
  
  public FrameFormat getFormat()
  {
    return mFormat;
  }
  
  public FrameManager getFrameManager()
  {
    return mFrameManager;
  }
  
  public abstract int[] getInts();
  
  public abstract Object getObjectValue();
  
  public int getRefCount()
  {
    return mRefCount;
  }
  
  public long getTimestamp()
  {
    return mTimestamp;
  }
  
  protected abstract boolean hasNativeAllocation();
  
  final int incRefCount()
  {
    mRefCount += 1;
    return mRefCount;
  }
  
  public boolean isReadOnly()
  {
    return mReadOnly;
  }
  
  final boolean isReusable()
  {
    return mReusable;
  }
  
  final void markReadOnly()
  {
    mReadOnly = true;
  }
  
  protected void onFrameFetch() {}
  
  protected void onFrameStore() {}
  
  public Frame release()
  {
    if (mFrameManager != null) {
      return mFrameManager.releaseFrame(this);
    }
    return this;
  }
  
  protected abstract void releaseNativeAllocation();
  
  protected boolean requestResize(int[] paramArrayOfInt)
  {
    return false;
  }
  
  protected void reset(FrameFormat paramFrameFormat)
  {
    mFormat = paramFrameFormat.mutableCopy();
    mReadOnly = false;
    mRefCount = 1;
  }
  
  public Frame retain()
  {
    if (mFrameManager != null) {
      return mFrameManager.retainFrame(this);
    }
    return this;
  }
  
  public abstract void setBitmap(Bitmap paramBitmap);
  
  public void setData(ByteBuffer paramByteBuffer)
  {
    setData(paramByteBuffer, 0, paramByteBuffer.limit());
  }
  
  public abstract void setData(ByteBuffer paramByteBuffer, int paramInt1, int paramInt2);
  
  public void setData(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    setData(ByteBuffer.wrap(paramArrayOfByte, paramInt1, paramInt2));
  }
  
  public void setDataFromFrame(Frame paramFrame)
  {
    setData(paramFrame.getData());
  }
  
  public abstract void setFloats(float[] paramArrayOfFloat);
  
  protected void setFormat(FrameFormat paramFrameFormat)
  {
    mFormat = paramFrameFormat.mutableCopy();
  }
  
  protected void setGenericObjectValue(Object paramObject)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Cannot set object value of unsupported type: ");
    localStringBuilder.append(paramObject.getClass());
    throw new RuntimeException(localStringBuilder.toString());
  }
  
  public abstract void setInts(int[] paramArrayOfInt);
  
  public void setObjectValue(Object paramObject)
  {
    assertFrameMutable();
    if ((paramObject instanceof int[])) {
      setInts((int[])paramObject);
    } else if ((paramObject instanceof float[])) {
      setFloats((float[])paramObject);
    } else if ((paramObject instanceof ByteBuffer)) {
      setData((ByteBuffer)paramObject);
    } else if ((paramObject instanceof Bitmap)) {
      setBitmap((Bitmap)paramObject);
    } else {
      setGenericObjectValue(paramObject);
    }
  }
  
  protected void setReusable(boolean paramBoolean)
  {
    mReusable = paramBoolean;
  }
  
  public void setTimestamp(long paramLong)
  {
    mTimestamp = paramLong;
  }
}
