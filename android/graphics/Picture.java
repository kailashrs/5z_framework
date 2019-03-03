package android.graphics;

import java.io.InputStream;
import java.io.OutputStream;

public class Picture
{
  private static final int WORKING_STREAM_STORAGE = 16384;
  private long mNativePicture;
  private PictureCanvas mRecordingCanvas;
  private boolean mRequiresHwAcceleration;
  
  public Picture()
  {
    this(nativeConstructor(0L));
  }
  
  private Picture(long paramLong)
  {
    if (paramLong != 0L)
    {
      mNativePicture = paramLong;
      return;
    }
    throw new RuntimeException();
  }
  
  public Picture(Picture paramPicture)
  {
    this(nativeConstructor(l));
  }
  
  @Deprecated
  public static Picture createFromStream(InputStream paramInputStream)
  {
    return new Picture(nativeCreateFromStream(paramInputStream, new byte['䀀']));
  }
  
  private static native long nativeBeginRecording(long paramLong, int paramInt1, int paramInt2);
  
  private static native long nativeConstructor(long paramLong);
  
  private static native long nativeCreateFromStream(InputStream paramInputStream, byte[] paramArrayOfByte);
  
  private static native void nativeDestructor(long paramLong);
  
  private static native void nativeDraw(long paramLong1, long paramLong2);
  
  private static native void nativeEndRecording(long paramLong);
  
  private static native int nativeGetHeight(long paramLong);
  
  private static native int nativeGetWidth(long paramLong);
  
  private static native boolean nativeWriteToStream(long paramLong, OutputStream paramOutputStream, byte[] paramArrayOfByte);
  
  public Canvas beginRecording(int paramInt1, int paramInt2)
  {
    if (mRecordingCanvas == null)
    {
      mRecordingCanvas = new PictureCanvas(this, nativeBeginRecording(mNativePicture, paramInt1, paramInt2));
      mRequiresHwAcceleration = false;
      return mRecordingCanvas;
    }
    throw new IllegalStateException("Picture already recording, must call #endRecording()");
  }
  
  public void draw(Canvas paramCanvas)
  {
    if (mRecordingCanvas != null) {
      endRecording();
    }
    if ((mRequiresHwAcceleration) && (!paramCanvas.isHardwareAccelerated())) {
      paramCanvas.onHwBitmapInSwMode();
    }
    nativeDraw(paramCanvas.getNativeCanvasWrapper(), mNativePicture);
  }
  
  public void endRecording()
  {
    if (mRecordingCanvas != null)
    {
      mRequiresHwAcceleration = mRecordingCanvas.mHoldsHwBitmap;
      mRecordingCanvas = null;
      nativeEndRecording(mNativePicture);
    }
  }
  
  protected void finalize()
    throws Throwable
  {
    try
    {
      nativeDestructor(mNativePicture);
      mNativePicture = 0L;
      return;
    }
    finally
    {
      super.finalize();
    }
  }
  
  public int getHeight()
  {
    return nativeGetHeight(mNativePicture);
  }
  
  public int getWidth()
  {
    return nativeGetWidth(mNativePicture);
  }
  
  public boolean requiresHardwareAcceleration()
  {
    return mRequiresHwAcceleration;
  }
  
  @Deprecated
  public void writeToStream(OutputStream paramOutputStream)
  {
    if (paramOutputStream != null)
    {
      if (nativeWriteToStream(mNativePicture, paramOutputStream, new byte['䀀'])) {
        return;
      }
      throw new RuntimeException();
    }
    throw new NullPointerException();
  }
  
  private static class PictureCanvas
    extends Canvas
  {
    boolean mHoldsHwBitmap;
    private final Picture mPicture;
    
    public PictureCanvas(Picture paramPicture, long paramLong)
    {
      super();
      mPicture = paramPicture;
      mDensity = 0;
    }
    
    public void drawPicture(Picture paramPicture)
    {
      if (mPicture != paramPicture)
      {
        super.drawPicture(paramPicture);
        return;
      }
      throw new RuntimeException("Cannot draw a picture into its recording canvas");
    }
    
    protected void onHwBitmapInSwMode()
    {
      mHoldsHwBitmap = true;
    }
    
    public void setBitmap(Bitmap paramBitmap)
    {
      throw new RuntimeException("Cannot call setBitmap on a picture canvas");
    }
  }
}
