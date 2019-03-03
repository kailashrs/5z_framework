package android.media;

import android.graphics.ImageFormat;
import android.graphics.PixelFormat;
import android.hardware.HardwareBuffer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Surface;
import dalvik.system.VMRuntime;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.NioUtils;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class ImageReader
  implements AutoCloseable
{
  private static final int ACQUIRE_MAX_IMAGES = 2;
  private static final int ACQUIRE_NO_BUFS = 1;
  private static final int ACQUIRE_SUCCESS = 0;
  private static final long BUFFER_USAGE_UNKNOWN = 0L;
  private List<Image> mAcquiredImages = new CopyOnWriteArrayList();
  private final Object mCloseLock = new Object();
  private int mEstimatedNativeAllocBytes;
  private final int mFormat;
  private final int mHeight;
  private boolean mIsReaderValid = false;
  private OnImageAvailableListener mListener;
  private ListenerHandler mListenerHandler;
  private final Object mListenerLock = new Object();
  private final int mMaxImages;
  private long mNativeContext;
  private final int mNumPlanes;
  private final Surface mSurface;
  private final int mWidth;
  
  static
  {
    System.loadLibrary("media_jni");
    nativeClassInit();
  }
  
  protected ImageReader(int paramInt1, int paramInt2, int paramInt3, int paramInt4, long paramLong)
  {
    mWidth = paramInt1;
    mHeight = paramInt2;
    mFormat = paramInt3;
    mMaxImages = paramInt4;
    if ((paramInt1 >= 1) && (paramInt2 >= 1))
    {
      if (mMaxImages >= 1)
      {
        if (paramInt3 != 17)
        {
          mNumPlanes = ImageUtils.getNumPlanesForFormat(mFormat);
          nativeInit(new WeakReference(this), paramInt1, paramInt2, paramInt3, paramInt4, paramLong);
          mSurface = nativeGetSurface();
          mIsReaderValid = true;
          mEstimatedNativeAllocBytes = ImageUtils.getEstimatedNativeAllocBytes(paramInt1, paramInt2, paramInt3, 1);
          VMRuntime.getRuntime().registerNativeAllocation(mEstimatedNativeAllocBytes);
          return;
        }
        throw new IllegalArgumentException("NV21 format is not supported");
      }
      throw new IllegalArgumentException("Maximum outstanding image count must be at least 1");
    }
    throw new IllegalArgumentException("The image dimensions must be positive");
  }
  
  private int acquireNextSurfaceImage(SurfaceImage paramSurfaceImage)
  {
    Object localObject = mCloseLock;
    int i = 1;
    try
    {
      if (mIsReaderValid) {
        i = nativeImageSetup(paramSurfaceImage);
      }
      switch (i)
      {
      default: 
        paramSurfaceImage = new java/lang/AssertionError;
        break;
      case 0: 
        mIsImageValid = true;
      case 1: 
      case 2: 
        if (i == 0) {
          mAcquiredImages.add(paramSurfaceImage);
        }
        return i;
      }
      StringBuilder localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append("Unknown nativeImageSetup return code ");
      localStringBuilder.append(i);
      paramSurfaceImage.<init>(localStringBuilder.toString());
      throw paramSurfaceImage;
    }
    finally {}
  }
  
  private static boolean isFormatUsageCombinationAllowed(int paramInt, long paramLong)
  {
    if ((!ImageFormat.isPublicFormat(paramInt)) && (!PixelFormat.isPublicFormat(paramInt))) {
      return false;
    }
    return paramLong != 0L;
  }
  
  private boolean isImageOwnedbyMe(Image paramImage)
  {
    boolean bool1 = paramImage instanceof SurfaceImage;
    boolean bool2 = false;
    if (!bool1) {
      return false;
    }
    if (((SurfaceImage)paramImage).getReader() == this) {
      bool2 = true;
    }
    return bool2;
  }
  
  private static native void nativeClassInit();
  
  private synchronized native void nativeClose();
  
  private synchronized native int nativeDetachImage(Image paramImage);
  
  private synchronized native void nativeDiscardFreeBuffers();
  
  private synchronized native Surface nativeGetSurface();
  
  private synchronized native int nativeImageSetup(Image paramImage);
  
  private synchronized native void nativeInit(Object paramObject, int paramInt1, int paramInt2, int paramInt3, int paramInt4, long paramLong);
  
  private synchronized native void nativeReleaseImage(Image paramImage);
  
  public static ImageReader newInstance(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    return new ImageReader(paramInt1, paramInt2, paramInt3, paramInt4, 0L);
  }
  
  public static ImageReader newInstance(int paramInt1, int paramInt2, int paramInt3, int paramInt4, long paramLong)
  {
    if (isFormatUsageCombinationAllowed(paramInt3, paramLong)) {
      return new ImageReader(paramInt1, paramInt2, paramInt3, paramInt4, paramLong);
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Format usage combination is not supported: format = ");
    localStringBuilder.append(paramInt3);
    localStringBuilder.append(", usage = ");
    localStringBuilder.append(paramLong);
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  private static void postEventFromNative(Object arg0)
  {
    Object localObject1 = (ImageReader)((WeakReference)???).get();
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
  
  private void releaseImage(Image paramImage)
  {
    if ((paramImage instanceof SurfaceImage))
    {
      SurfaceImage localSurfaceImage = (SurfaceImage)paramImage;
      if (!mIsImageValid) {
        return;
      }
      if ((localSurfaceImage.getReader() == this) && (mAcquiredImages.contains(paramImage)))
      {
        localSurfaceImage.clearSurfacePlanes();
        nativeReleaseImage(paramImage);
        mIsImageValid = false;
        mAcquiredImages.remove(paramImage);
        return;
      }
      throw new IllegalArgumentException("This image was not produced by this ImageReader");
    }
    throw new IllegalArgumentException("This image was not produced by an ImageReader");
  }
  
  /* Error */
  public Image acquireLatestImage()
  {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual 256	android/media/ImageReader:acquireNextImage	()Landroid/media/Image;
    //   4: astore_1
    //   5: aload_1
    //   6: astore_2
    //   7: aload_1
    //   8: ifnonnull +5 -> 13
    //   11: aconst_null
    //   12: areturn
    //   13: aload_0
    //   14: invokevirtual 259	android/media/ImageReader:acquireNextImageNoThrowISE	()Landroid/media/Image;
    //   17: astore_1
    //   18: aload_1
    //   19: ifnonnull +17 -> 36
    //   22: iconst_0
    //   23: ifeq +11 -> 34
    //   26: new 261	java/lang/NullPointerException
    //   29: dup
    //   30: invokespecial 262	java/lang/NullPointerException:<init>	()V
    //   33: athrow
    //   34: aload_2
    //   35: areturn
    //   36: aload_2
    //   37: invokevirtual 267	android/media/Image:close	()V
    //   40: aload_1
    //   41: astore_2
    //   42: goto -29 -> 13
    //   45: astore_1
    //   46: aload_2
    //   47: ifnull +7 -> 54
    //   50: aload_2
    //   51: invokevirtual 267	android/media/Image:close	()V
    //   54: aload_1
    //   55: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	56	0	this	ImageReader
    //   4	37	1	localImage1	Image
    //   45	10	1	localObject	Object
    //   6	45	2	localImage2	Image
    // Exception table:
    //   from	to	target	type
    //   13	18	45	finally
    //   36	40	45	finally
  }
  
  public Image acquireNextImage()
  {
    Object localObject = new SurfaceImage(mFormat);
    int i = acquireNextSurfaceImage((SurfaceImage)localObject);
    switch (i)
    {
    default: 
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Unknown nativeImageSetup return code ");
      ((StringBuilder)localObject).append(i);
      throw new AssertionError(((StringBuilder)localObject).toString());
    case 2: 
      throw new IllegalStateException(String.format("maxImages (%d) has already been acquired, call #close before acquiring more.", new Object[] { Integer.valueOf(mMaxImages) }));
    case 1: 
      return null;
    }
    return localObject;
  }
  
  public Image acquireNextImageNoThrowISE()
  {
    SurfaceImage localSurfaceImage = new SurfaceImage(mFormat);
    if (acquireNextSurfaceImage(localSurfaceImage) != 0) {
      localSurfaceImage = null;
    }
    return localSurfaceImage;
  }
  
  public void close()
  {
    setOnImageAvailableListener(null, null);
    if (mSurface != null) {
      mSurface.release();
    }
    synchronized (mCloseLock)
    {
      mIsReaderValid = false;
      Iterator localIterator = mAcquiredImages.iterator();
      while (localIterator.hasNext()) {
        ((Image)localIterator.next()).close();
      }
      mAcquiredImages.clear();
      nativeClose();
      if (mEstimatedNativeAllocBytes > 0)
      {
        VMRuntime.getRuntime().registerNativeFree(mEstimatedNativeAllocBytes);
        mEstimatedNativeAllocBytes = 0;
      }
      return;
    }
  }
  
  void detachImage(Image paramImage)
  {
    if (paramImage != null)
    {
      if (isImageOwnedbyMe(paramImage))
      {
        SurfaceImage localSurfaceImage = (SurfaceImage)paramImage;
        localSurfaceImage.throwISEIfImageIsInvalid();
        if (!localSurfaceImage.isAttachable())
        {
          nativeDetachImage(paramImage);
          localSurfaceImage.clearSurfacePlanes();
          SurfaceImage.access$102(localSurfaceImage, null);
          localSurfaceImage.setDetached(true);
          return;
        }
        throw new IllegalStateException("Image was already detached from this ImageReader");
      }
      throw new IllegalArgumentException("Trying to detach an image that is not owned by this ImageReader");
    }
    throw new IllegalArgumentException("input image must not be null");
  }
  
  public void discardFreeBuffers()
  {
    synchronized (mCloseLock)
    {
      nativeDiscardFreeBuffers();
      return;
    }
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
  
  public int getHeight()
  {
    return mHeight;
  }
  
  public int getImageFormat()
  {
    return mFormat;
  }
  
  public int getMaxImages()
  {
    return mMaxImages;
  }
  
  public Surface getSurface()
  {
    return mSurface;
  }
  
  public int getWidth()
  {
    return mWidth;
  }
  
  public void setOnImageAvailableListener(OnImageAvailableListener paramOnImageAvailableListener, Handler paramHandler)
  {
    Object localObject = mListenerLock;
    if ((paramOnImageAvailableListener == null) || (paramHandler != null)) {}
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
          ListenerHandler localListenerHandler = new android/media/ImageReader$ListenerHandler;
          localListenerHandler.<init>(this, paramHandler);
          mListenerHandler = localListenerHandler;
        }
        mListener = paramOnImageAvailableListener;
      }
      else
      {
        paramOnImageAvailableListener = new java/lang/IllegalArgumentException;
        paramOnImageAvailableListener.<init>("handler is null but the current thread is not a looper");
        throw paramOnImageAvailableListener;
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
    
    public void handleMessage(Message paramMessage)
    {
      synchronized (mListenerLock)
      {
        paramMessage = mListener;
        synchronized (mCloseLock)
        {
          boolean bool = mIsReaderValid;
          if ((paramMessage != null) && (bool)) {
            paramMessage.onImageAvailable(ImageReader.this);
          }
          return;
        }
      }
    }
  }
  
  public static abstract interface OnImageAvailableListener
  {
    public abstract void onImageAvailable(ImageReader paramImageReader);
  }
  
  private class SurfaceImage
    extends Image
  {
    private int mFormat = 0;
    private AtomicBoolean mIsDetached = new AtomicBoolean(false);
    private long mNativeBuffer;
    private SurfacePlane[] mPlanes;
    private int mScalingMode;
    private long mTimestamp;
    private int mTransform;
    
    public SurfaceImage(int paramInt)
    {
      mFormat = paramInt;
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
    
    private synchronized native int nativeGetFormat(int paramInt);
    
    private synchronized native HardwareBuffer nativeGetHardwareBuffer();
    
    private synchronized native int nativeGetHeight();
    
    private synchronized native int nativeGetWidth();
    
    private void setDetached(boolean paramBoolean)
    {
      throwISEIfImageIsInvalid();
      mIsDetached.getAndSet(paramBoolean);
    }
    
    public void close()
    {
      ImageReader.this.releaseImage(this);
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
      int i = getImageFormat();
      if (i != 34) {
        i = nativeGetFormat(i);
      }
      mFormat = i;
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
      int i = getFormat();
      if (i != 36) {
        switch (i)
        {
        default: 
          i = nativeGetHeight();
          break;
        }
      } else {
        i = ImageReader.this.getHeight();
      }
      return i;
    }
    
    long getNativeContext()
    {
      throwISEIfImageIsInvalid();
      return mNativeBuffer;
    }
    
    ImageReader getOwner()
    {
      throwISEIfImageIsInvalid();
      return ImageReader.this;
    }
    
    public Image.Plane[] getPlanes()
    {
      throwISEIfImageIsInvalid();
      if (mPlanes == null) {
        mPlanes = nativeCreatePlanes(mNumPlanes, mFormat);
      }
      return (Image.Plane[])mPlanes.clone();
    }
    
    public ImageReader getReader()
    {
      return ImageReader.this;
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
      int i = getFormat();
      if (i != 36) {
        switch (i)
        {
        default: 
          i = nativeGetWidth();
          break;
        }
      } else {
        i = ImageReader.this.getWidth();
      }
      return i;
    }
    
    boolean isAttachable()
    {
      throwISEIfImageIsInvalid();
      return mIsDetached.get();
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
        if (mFormat != 36) {
          return mPixelStride;
        }
        throw new UnsupportedOperationException("getPixelStride is not supported for RAW_PRIVATE plane");
      }
      
      public int getRowStride()
      {
        throwISEIfImageIsInvalid();
        if (mFormat != 36) {
          return mRowStride;
        }
        throw new UnsupportedOperationException("getRowStride is not supported for RAW_PRIVATE plane");
      }
    }
  }
}
