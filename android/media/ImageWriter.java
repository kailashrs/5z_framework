package android.media;

import android.graphics.ImageFormat;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.hardware.HardwareBuffer;
import android.hardware.camera2.utils.SurfaceUtils;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Size;
import android.view.Surface;
import dalvik.system.VMRuntime;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.NioUtils;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ImageWriter
  implements AutoCloseable
{
  private List<Image> mDequeuedImages = new CopyOnWriteArrayList();
  private int mEstimatedNativeAllocBytes;
  private OnImageReleasedListener mListener;
  private ListenerHandler mListenerHandler;
  private final Object mListenerLock = new Object();
  private final int mMaxImages;
  private long mNativeContext;
  private int mWriterFormat;
  
  static
  {
    System.loadLibrary("media_jni");
    nativeClassInit();
  }
  
  protected ImageWriter(Surface paramSurface, int paramInt1, int paramInt2)
  {
    if ((paramSurface != null) && (paramInt1 >= 1))
    {
      mMaxImages = paramInt1;
      int i = paramInt2;
      if (paramInt2 == 0) {
        i = SurfaceUtils.getSurfaceFormat(paramSurface);
      }
      mNativeContext = nativeInit(new WeakReference(this), paramSurface, paramInt1, i);
      paramSurface = SurfaceUtils.getSurfaceSize(paramSurface);
      mEstimatedNativeAllocBytes = ImageUtils.getEstimatedNativeAllocBytes(paramSurface.getWidth(), paramSurface.getHeight(), i, 1);
      VMRuntime.getRuntime().registerNativeAllocation(mEstimatedNativeAllocBytes);
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Illegal input argument: surface ");
    localStringBuilder.append(paramSurface);
    localStringBuilder.append(", maxImages: ");
    localStringBuilder.append(paramInt1);
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  private void abortImage(Image paramImage)
  {
    if (paramImage != null)
    {
      if (mDequeuedImages.contains(paramImage))
      {
        WriterSurfaceImage localWriterSurfaceImage = (WriterSurfaceImage)paramImage;
        if (!mIsImageValid) {
          return;
        }
        cancelImage(mNativeContext, paramImage);
        mDequeuedImages.remove(paramImage);
        localWriterSurfaceImage.clearSurfacePlanes();
        mIsImageValid = false;
        return;
      }
      throw new IllegalStateException("It is illegal to abort some image that is not dequeued yet");
    }
    throw new IllegalArgumentException("image shouldn't be null");
  }
  
  private void attachAndQueueInputImage(Image paramImage)
  {
    if (paramImage != null)
    {
      if (!isImageOwnedByMe(paramImage))
      {
        if (paramImage.isAttachable())
        {
          Rect localRect = paramImage.getCropRect();
          nativeAttachAndQueueImage(mNativeContext, paramImage.getNativeContext(), paramImage.getFormat(), paramImage.getTimestamp(), left, top, right, bottom, paramImage.getTransform(), paramImage.getScalingMode());
          return;
        }
        throw new IllegalStateException("Image was not detached from last owner, or image  is not detachable");
      }
      throw new IllegalArgumentException("Can not attach an image that is owned ImageWriter already");
    }
    throw new IllegalArgumentException("image shouldn't be null");
  }
  
  private synchronized native void cancelImage(long paramLong, Image paramImage);
  
  private boolean isImageOwnedByMe(Image paramImage)
  {
    if (!(paramImage instanceof WriterSurfaceImage)) {
      return false;
    }
    return ((WriterSurfaceImage)paramImage).getOwner() == this;
  }
  
  private synchronized native int nativeAttachAndQueueImage(long paramLong1, long paramLong2, int paramInt1, long paramLong3, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7);
  
  private static native void nativeClassInit();
  
  private synchronized native void nativeClose(long paramLong);
  
  private synchronized native void nativeDequeueInputImage(long paramLong, Image paramImage);
  
  private synchronized native long nativeInit(Object paramObject, Surface paramSurface, int paramInt1, int paramInt2);
  
  private synchronized native void nativeQueueInputImage(long paramLong1, Image paramImage, long paramLong2, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6);
  
  public static ImageWriter newInstance(Surface paramSurface, int paramInt)
  {
    return new ImageWriter(paramSurface, paramInt, 0);
  }
  
  public static ImageWriter newInstance(Surface paramSurface, int paramInt1, int paramInt2)
  {
    if ((!ImageFormat.isPublicFormat(paramInt2)) && (!PixelFormat.isPublicFormat(paramInt2)))
    {
      paramSurface = new StringBuilder();
      paramSurface.append("Invalid format is specified: ");
      paramSurface.append(paramInt2);
      throw new IllegalArgumentException(paramSurface.toString());
    }
    return new ImageWriter(paramSurface, paramInt1, paramInt2);
  }
  
  private static void postEventFromNative(Object arg0)
  {
    Object localObject1 = (ImageWriter)((WeakReference)???).get();
    if (localObject1 == null) {
      return;
    }
    synchronized (mListenerLock)
    {
      localObject1 = mListenerHandler;
      if (localObject1 != null) {
        ((Handler)localObject1).sendEmptyMessage(0);
      }
      return;
    }
  }
  
  public void close()
  {
    setOnImageReleasedListener(null, null);
    Iterator localIterator = mDequeuedImages.iterator();
    while (localIterator.hasNext()) {
      ((Image)localIterator.next()).close();
    }
    mDequeuedImages.clear();
    nativeClose(mNativeContext);
    mNativeContext = 0L;
    if (mEstimatedNativeAllocBytes > 0)
    {
      VMRuntime.getRuntime().registerNativeFree(mEstimatedNativeAllocBytes);
      mEstimatedNativeAllocBytes = 0;
    }
  }
  
  public Image dequeueInputImage()
  {
    if (mDequeuedImages.size() < mMaxImages)
    {
      localObject = new WriterSurfaceImage(this);
      nativeDequeueInputImage(mNativeContext, (Image)localObject);
      mDequeuedImages.add(localObject);
      mIsImageValid = true;
      return localObject;
    }
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Already dequeued max number of Images ");
    ((StringBuilder)localObject).append(mMaxImages);
    throw new IllegalStateException(((StringBuilder)localObject).toString());
  }
  
  protected void finalize()
    throws Throwable
  {
    try
    {
      close();
      return;
    }
    finally
    {
      super.finalize();
    }
  }
  
  public int getFormat()
  {
    return mWriterFormat;
  }
  
  public int getMaxImages()
  {
    return mMaxImages;
  }
  
  public void queueInputImage(Image paramImage)
  {
    if (paramImage != null)
    {
      boolean bool = isImageOwnedByMe(paramImage);
      if ((bool) && (!mIsImageValid)) {
        throw new IllegalStateException("Image from ImageWriter is invalid");
      }
      if (!bool)
      {
        if ((paramImage.getOwner() instanceof ImageReader))
        {
          ((ImageReader)paramImage.getOwner()).detachImage(paramImage);
          attachAndQueueInputImage(paramImage);
          paramImage.close();
          return;
        }
        throw new IllegalArgumentException("Only images from ImageReader can be queued to ImageWriter, other image source is not supported yet!");
      }
      Rect localRect = paramImage.getCropRect();
      nativeQueueInputImage(mNativeContext, paramImage, paramImage.getTimestamp(), left, top, right, bottom, paramImage.getTransform(), paramImage.getScalingMode());
      if (bool)
      {
        mDequeuedImages.remove(paramImage);
        paramImage = (WriterSurfaceImage)paramImage;
        paramImage.clearSurfacePlanes();
        mIsImageValid = false;
      }
      return;
    }
    throw new IllegalArgumentException("image shouldn't be null");
  }
  
  public void setOnImageReleasedListener(OnImageReleasedListener paramOnImageReleasedListener, Handler paramHandler)
  {
    Object localObject = mListenerLock;
    if ((paramOnImageReleasedListener == null) || (paramHandler != null)) {}
    try
    {
      paramHandler = paramHandler.getLooper();
      break label27;
      paramHandler = Looper.myLooper();
      label27:
      if (paramHandler != null)
      {
        if ((mListenerHandler == null) || (mListenerHandler.getLooper() != paramHandler))
        {
          ListenerHandler localListenerHandler = new android/media/ImageWriter$ListenerHandler;
          localListenerHandler.<init>(this, paramHandler);
          mListenerHandler = localListenerHandler;
        }
        mListener = paramOnImageReleasedListener;
      }
      else
      {
        paramOnImageReleasedListener = new java/lang/IllegalArgumentException;
        paramOnImageReleasedListener.<init>("handler is null but the current thread is not a looper");
        throw paramOnImageReleasedListener;
        mListener = null;
        mListenerHandler = null;
      }
      return;
    }
    finally {}
  }
  
  private final class ListenerHandler
    extends Handler
  {
    public ListenerHandler(Looper paramLooper)
    {
      super(null, true);
    }
    
    public void handleMessage(Message arg1)
    {
      synchronized (mListenerLock)
      {
        ImageWriter.OnImageReleasedListener localOnImageReleasedListener = mListener;
        if (localOnImageReleasedListener != null) {
          localOnImageReleasedListener.onImageReleased(ImageWriter.this);
        }
        return;
      }
    }
  }
  
  public static abstract interface OnImageReleasedListener
  {
    public abstract void onImageReleased(ImageWriter paramImageWriter);
  }
  
  private static class WriterSurfaceImage
    extends Image
  {
    private final long DEFAULT_TIMESTAMP = Long.MIN_VALUE;
    private int mFormat = -1;
    private int mHeight = -1;
    private long mNativeBuffer;
    private int mNativeFenceFd = -1;
    private ImageWriter mOwner;
    private SurfacePlane[] mPlanes;
    private int mScalingMode = 0;
    private long mTimestamp = Long.MIN_VALUE;
    private int mTransform = 0;
    private int mWidth = -1;
    
    public WriterSurfaceImage(ImageWriter paramImageWriter)
    {
      mOwner = paramImageWriter;
    }
    
    private void clearSurfacePlanes()
    {
      if ((mIsImageValid) && (mPlanes != null)) {
        for (int i = 0; i < mPlanes.length; i++) {
          if (mPlanes[i] != null)
          {
            mPlanes[i].clearBuffer();
            mPlanes[i] = null;
          }
        }
      }
    }
    
    private synchronized native SurfacePlane[] nativeCreatePlanes(int paramInt1, int paramInt2);
    
    private synchronized native int nativeGetFormat();
    
    private synchronized native HardwareBuffer nativeGetHardwareBuffer();
    
    private synchronized native int nativeGetHeight();
    
    private synchronized native int nativeGetWidth();
    
    public void close()
    {
      if (mIsImageValid) {
        getOwner().abortImage(this);
      }
    }
    
    protected final void finalize()
      throws Throwable
    {
      try
      {
        close();
        return;
      }
      finally
      {
        super.finalize();
      }
    }
    
    public int getFormat()
    {
      throwISEIfImageIsInvalid();
      if (mFormat == -1) {
        mFormat = nativeGetFormat();
      }
      return mFormat;
    }
    
    public HardwareBuffer getHardwareBuffer()
    {
      throwISEIfImageIsInvalid();
      return nativeGetHardwareBuffer();
    }
    
    public int getHeight()
    {
      throwISEIfImageIsInvalid();
      if (mHeight == -1) {
        mHeight = nativeGetHeight();
      }
      return mHeight;
    }
    
    long getNativeContext()
    {
      throwISEIfImageIsInvalid();
      return mNativeBuffer;
    }
    
    ImageWriter getOwner()
    {
      throwISEIfImageIsInvalid();
      return mOwner;
    }
    
    public Image.Plane[] getPlanes()
    {
      throwISEIfImageIsInvalid();
      if (mPlanes == null) {
        mPlanes = nativeCreatePlanes(ImageUtils.getNumPlanesForFormat(getFormat()), getOwner().getFormat());
      }
      return (Image.Plane[])mPlanes.clone();
    }
    
    public int getScalingMode()
    {
      throwISEIfImageIsInvalid();
      return mScalingMode;
    }
    
    public long getTimestamp()
    {
      throwISEIfImageIsInvalid();
      return mTimestamp;
    }
    
    public int getTransform()
    {
      throwISEIfImageIsInvalid();
      return mTransform;
    }
    
    public int getWidth()
    {
      throwISEIfImageIsInvalid();
      if (mWidth == -1) {
        mWidth = nativeGetWidth();
      }
      return mWidth;
    }
    
    boolean isAttachable()
    {
      throwISEIfImageIsInvalid();
      return false;
    }
    
    public void setTimestamp(long paramLong)
    {
      throwISEIfImageIsInvalid();
      mTimestamp = paramLong;
    }
    
    private class SurfacePlane
      extends Image.Plane
    {
      private ByteBuffer mBuffer;
      private final int mPixelStride;
      private final int mRowStride;
      
      private SurfacePlane(int paramInt1, int paramInt2, ByteBuffer paramByteBuffer)
      {
        mRowStride = paramInt1;
        mPixelStride = paramInt2;
        mBuffer = paramByteBuffer;
        mBuffer.order(ByteOrder.nativeOrder());
      }
      
      private void clearBuffer()
      {
        if (mBuffer == null) {
          return;
        }
        if (mBuffer.isDirect()) {
          NioUtils.freeDirectBuffer(mBuffer);
        }
        mBuffer = null;
      }
      
      public ByteBuffer getBuffer()
      {
        throwISEIfImageIsInvalid();
        return mBuffer;
      }
      
      public int getPixelStride()
      {
        throwISEIfImageIsInvalid();
        return mPixelStride;
      }
      
      public int getRowStride()
      {
        throwISEIfImageIsInvalid();
        return mRowStride;
      }
    }
  }
}
