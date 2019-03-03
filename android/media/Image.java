package android.media;

import android.graphics.Rect;
import android.hardware.HardwareBuffer;
import java.nio.ByteBuffer;

public abstract class Image
  implements AutoCloseable
{
  private Rect mCropRect;
  protected boolean mIsImageValid = false;
  
  protected Image() {}
  
  public abstract void close();
  
  public Rect getCropRect()
  {
    throwISEIfImageIsInvalid();
    if (mCropRect == null) {
      return new Rect(0, 0, getWidth(), getHeight());
    }
    return new Rect(mCropRect);
  }
  
  public abstract int getFormat();
  
  public HardwareBuffer getHardwareBuffer()
  {
    throwISEIfImageIsInvalid();
    return null;
  }
  
  public abstract int getHeight();
  
  long getNativeContext()
  {
    throwISEIfImageIsInvalid();
    return 0L;
  }
  
  Object getOwner()
  {
    throwISEIfImageIsInvalid();
    return null;
  }
  
  public abstract Plane[] getPlanes();
  
  public abstract int getScalingMode();
  
  public abstract long getTimestamp();
  
  public abstract int getTransform();
  
  public abstract int getWidth();
  
  boolean isAttachable()
  {
    throwISEIfImageIsInvalid();
    return false;
  }
  
  public void setCropRect(Rect paramRect)
  {
    throwISEIfImageIsInvalid();
    Rect localRect = paramRect;
    if (paramRect != null)
    {
      paramRect = new Rect(paramRect);
      localRect = paramRect;
      if (!paramRect.intersect(0, 0, getWidth(), getHeight()))
      {
        paramRect.setEmpty();
        localRect = paramRect;
      }
    }
    mCropRect = localRect;
  }
  
  public void setTimestamp(long paramLong)
  {
    throwISEIfImageIsInvalid();
  }
  
  protected void throwISEIfImageIsInvalid()
  {
    if (mIsImageValid) {
      return;
    }
    throw new IllegalStateException("Image is already closed");
  }
  
  public static abstract class Plane
  {
    protected Plane() {}
    
    public abstract ByteBuffer getBuffer();
    
    public abstract int getPixelStride();
    
    public abstract int getRowStride();
  }
}
