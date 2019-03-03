package android.graphics;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class GraphicBuffer
  implements Parcelable
{
  public static final Parcelable.Creator<GraphicBuffer> CREATOR = new Parcelable.Creator()
  {
    public GraphicBuffer createFromParcel(Parcel paramAnonymousParcel)
    {
      int i = paramAnonymousParcel.readInt();
      int j = paramAnonymousParcel.readInt();
      int k = paramAnonymousParcel.readInt();
      int m = paramAnonymousParcel.readInt();
      long l = GraphicBuffer.nReadGraphicBufferFromParcel(paramAnonymousParcel);
      if (l != 0L) {
        return new GraphicBuffer(i, j, k, m, l, null);
      }
      return null;
    }
    
    public GraphicBuffer[] newArray(int paramAnonymousInt)
    {
      return new GraphicBuffer[paramAnonymousInt];
    }
  };
  public static final int USAGE_HW_2D = 1024;
  public static final int USAGE_HW_COMPOSER = 2048;
  public static final int USAGE_HW_MASK = 466688;
  public static final int USAGE_HW_RENDER = 512;
  public static final int USAGE_HW_TEXTURE = 256;
  public static final int USAGE_HW_VIDEO_ENCODER = 65536;
  public static final int USAGE_PROTECTED = 16384;
  public static final int USAGE_SOFTWARE_MASK = 255;
  public static final int USAGE_SW_READ_MASK = 15;
  public static final int USAGE_SW_READ_NEVER = 0;
  public static final int USAGE_SW_READ_OFTEN = 3;
  public static final int USAGE_SW_READ_RARELY = 2;
  public static final int USAGE_SW_WRITE_MASK = 240;
  public static final int USAGE_SW_WRITE_NEVER = 0;
  public static final int USAGE_SW_WRITE_OFTEN = 48;
  public static final int USAGE_SW_WRITE_RARELY = 32;
  private Canvas mCanvas;
  private boolean mDestroyed;
  private final int mFormat;
  private final int mHeight;
  private final long mNativeObject;
  private int mSaveCount;
  private final int mUsage;
  private final int mWidth;
  
  private GraphicBuffer(int paramInt1, int paramInt2, int paramInt3, int paramInt4, long paramLong)
  {
    mWidth = paramInt1;
    mHeight = paramInt2;
    mFormat = paramInt3;
    mUsage = paramInt4;
    mNativeObject = paramLong;
  }
  
  public static GraphicBuffer create(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    long l = nCreateGraphicBuffer(paramInt1, paramInt2, paramInt3, paramInt4);
    if (l != 0L) {
      return new GraphicBuffer(paramInt1, paramInt2, paramInt3, paramInt4, l);
    }
    return null;
  }
  
  public static GraphicBuffer createFromExisting(int paramInt1, int paramInt2, int paramInt3, int paramInt4, long paramLong)
  {
    paramLong = nWrapGraphicBuffer(paramLong);
    if (paramLong != 0L) {
      return new GraphicBuffer(paramInt1, paramInt2, paramInt3, paramInt4, paramLong);
    }
    return null;
  }
  
  private static native long nCreateGraphicBuffer(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  
  private static native void nDestroyGraphicBuffer(long paramLong);
  
  private static native boolean nLockCanvas(long paramLong, Canvas paramCanvas, Rect paramRect);
  
  private static native long nReadGraphicBufferFromParcel(Parcel paramParcel);
  
  private static native boolean nUnlockCanvasAndPost(long paramLong, Canvas paramCanvas);
  
  private static native long nWrapGraphicBuffer(long paramLong);
  
  private static native void nWriteGraphicBufferToParcel(long paramLong, Parcel paramParcel);
  
  public int describeContents()
  {
    return 0;
  }
  
  public void destroy()
  {
    if (!mDestroyed)
    {
      mDestroyed = true;
      nDestroyGraphicBuffer(mNativeObject);
    }
  }
  
  protected void finalize()
    throws Throwable
  {
    try
    {
      if (!mDestroyed) {
        nDestroyGraphicBuffer(mNativeObject);
      }
      return;
    }
    finally
    {
      super.finalize();
    }
  }
  
  public int getFormat()
  {
    return mFormat;
  }
  
  public int getHeight()
  {
    return mHeight;
  }
  
  public int getUsage()
  {
    return mUsage;
  }
  
  public int getWidth()
  {
    return mWidth;
  }
  
  public boolean isDestroyed()
  {
    return mDestroyed;
  }
  
  public Canvas lockCanvas()
  {
    return lockCanvas(null);
  }
  
  public Canvas lockCanvas(Rect paramRect)
  {
    if (mDestroyed) {
      return null;
    }
    if (mCanvas == null) {
      mCanvas = new Canvas();
    }
    if (nLockCanvas(mNativeObject, mCanvas, paramRect))
    {
      mSaveCount = mCanvas.save();
      return mCanvas;
    }
    return null;
  }
  
  public void unlockCanvasAndPost(Canvas paramCanvas)
  {
    if ((!mDestroyed) && (mCanvas != null) && (paramCanvas == mCanvas))
    {
      paramCanvas.restoreToCount(mSaveCount);
      mSaveCount = 0;
      nUnlockCanvasAndPost(mNativeObject, mCanvas);
    }
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    if (!mDestroyed)
    {
      paramParcel.writeInt(mWidth);
      paramParcel.writeInt(mHeight);
      paramParcel.writeInt(mFormat);
      paramParcel.writeInt(mUsage);
      nWriteGraphicBufferToParcel(mNativeObject, paramParcel);
      return;
    }
    throw new IllegalStateException("This GraphicBuffer has been destroyed and cannot be written to a parcel.");
  }
}
