package android.view;

import android.content.res.CompatibilityInfo.Translator;
import android.graphics.Canvas;
import android.graphics.GraphicBuffer;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;
import dalvik.system.CloseGuard;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class Surface
  implements Parcelable
{
  public static final Parcelable.Creator<Surface> CREATOR = new Parcelable.Creator()
  {
    public Surface createFromParcel(Parcel paramAnonymousParcel)
    {
      try
      {
        Surface localSurface = new android/view/Surface;
        localSurface.<init>();
        localSurface.readFromParcel(paramAnonymousParcel);
        return localSurface;
      }
      catch (Exception paramAnonymousParcel)
      {
        Log.e("Surface", "Exception creating surface from parcel", paramAnonymousParcel);
      }
      return null;
    }
    
    public Surface[] newArray(int paramAnonymousInt)
    {
      return new Surface[paramAnonymousInt];
    }
  };
  public static final int ROTATION_0 = 0;
  public static final int ROTATION_180 = 2;
  public static final int ROTATION_270 = 3;
  public static final int ROTATION_90 = 1;
  public static final int SCALING_MODE_FREEZE = 0;
  public static final int SCALING_MODE_NO_SCALE_CROP = 3;
  public static final int SCALING_MODE_SCALE_CROP = 2;
  public static final int SCALING_MODE_SCALE_TO_WINDOW = 1;
  private static final String TAG = "Surface";
  private final Canvas mCanvas = new CompatibleCanvas(null);
  private final CloseGuard mCloseGuard = CloseGuard.get();
  private Matrix mCompatibleMatrix;
  private int mGenerationId;
  private HwuiContext mHwuiContext;
  private boolean mIsAutoRefreshEnabled;
  private boolean mIsSharedBufferModeEnabled;
  private boolean mIsSingleBuffered;
  final Object mLock = new Object();
  private long mLockedObject;
  private String mName;
  long mNativeObject;
  
  public Surface() {}
  
  private Surface(long paramLong)
  {
    synchronized (mLock)
    {
      setNativeObjectLocked(paramLong);
      return;
    }
  }
  
  public Surface(SurfaceTexture paramSurfaceTexture)
  {
    if (paramSurfaceTexture != null)
    {
      mIsSingleBuffered = paramSurfaceTexture.isSingleBuffered();
      synchronized (mLock)
      {
        mName = paramSurfaceTexture.toString();
        setNativeObjectLocked(nativeCreateFromSurfaceTexture(paramSurfaceTexture));
        return;
      }
    }
    throw new IllegalArgumentException("surfaceTexture must not be null");
  }
  
  private void checkNotReleasedLocked()
  {
    if (mNativeObject != 0L) {
      return;
    }
    throw new IllegalStateException("Surface has already been released.");
  }
  
  private static native long nHwuiCreate(long paramLong1, long paramLong2, boolean paramBoolean);
  
  private static native void nHwuiDestroy(long paramLong);
  
  private static native void nHwuiDraw(long paramLong);
  
  private static native void nHwuiSetSurface(long paramLong1, long paramLong2);
  
  private static native void nativeAllocateBuffers(long paramLong);
  
  private static native int nativeAttachAndQueueBuffer(long paramLong, GraphicBuffer paramGraphicBuffer);
  
  private static native long nativeCreateFromSurfaceControl(long paramLong);
  
  private static native long nativeCreateFromSurfaceTexture(SurfaceTexture paramSurfaceTexture)
    throws Surface.OutOfResourcesException;
  
  private static native int nativeForceScopedDisconnect(long paramLong);
  
  private static native long nativeGetFromSurfaceControl(long paramLong);
  
  private static native int nativeGetHeight(long paramLong);
  
  private static native long nativeGetNextFrameNumber(long paramLong);
  
  private static native int nativeGetWidth(long paramLong);
  
  private static native boolean nativeIsConsumerRunningBehind(long paramLong);
  
  private static native boolean nativeIsValid(long paramLong);
  
  private static native long nativeLockCanvas(long paramLong, Canvas paramCanvas, Rect paramRect)
    throws Surface.OutOfResourcesException;
  
  private static native long nativeReadFromParcel(long paramLong, Parcel paramParcel);
  
  private static native void nativeRelease(long paramLong);
  
  private static native int nativeSetAutoRefreshEnabled(long paramLong, boolean paramBoolean);
  
  private static native int nativeSetScalingMode(long paramLong, int paramInt);
  
  private static native int nativeSetSharedBufferModeEnabled(long paramLong, boolean paramBoolean);
  
  private static native void nativeUnlockCanvasAndPost(long paramLong, Canvas paramCanvas);
  
  private static native void nativeWriteToParcel(long paramLong, Parcel paramParcel);
  
  public static String rotationToString(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return Integer.toString(paramInt);
    case 3: 
      return "ROTATION_270";
    case 2: 
      return "ROTATION_180";
    case 1: 
      return "ROTATION_90";
    }
    return "ROTATION_0";
  }
  
  private void setNativeObjectLocked(long paramLong)
  {
    if (mNativeObject != paramLong)
    {
      if ((mNativeObject == 0L) && (paramLong != 0L)) {
        mCloseGuard.open("release");
      } else if ((mNativeObject != 0L) && (paramLong == 0L)) {
        mCloseGuard.close();
      }
      mNativeObject = paramLong;
      mGenerationId += 1;
      if (mHwuiContext != null) {
        mHwuiContext.updateSurface();
      }
    }
  }
  
  private void unlockSwCanvasAndPost(Canvas paramCanvas)
  {
    if (paramCanvas == mCanvas)
    {
      if (mNativeObject != mLockedObject)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("WARNING: Surface's mNativeObject (0x");
        localStringBuilder.append(Long.toHexString(mNativeObject));
        localStringBuilder.append(") != mLockedObject (0x");
        localStringBuilder.append(Long.toHexString(mLockedObject));
        localStringBuilder.append(")");
        Log.w("Surface", localStringBuilder.toString());
      }
      if (mLockedObject != 0L) {
        try
        {
          nativeUnlockCanvasAndPost(mLockedObject, paramCanvas);
          return;
        }
        finally
        {
          nativeRelease(mLockedObject);
          mLockedObject = 0L;
        }
      }
      throw new IllegalStateException("Surface was not locked");
    }
    throw new IllegalArgumentException("canvas object must be the same instance that was previously returned by lockCanvas");
  }
  
  public void allocateBuffers()
  {
    synchronized (mLock)
    {
      checkNotReleasedLocked();
      nativeAllocateBuffers(mNativeObject);
      return;
    }
  }
  
  public void attachAndQueueBuffer(GraphicBuffer paramGraphicBuffer)
  {
    synchronized (mLock)
    {
      checkNotReleasedLocked();
      if (nativeAttachAndQueueBuffer(mNativeObject, paramGraphicBuffer) == 0) {
        return;
      }
      paramGraphicBuffer = new java/lang/RuntimeException;
      paramGraphicBuffer.<init>("Failed to attach and queue buffer to Surface (bad object?)");
      throw paramGraphicBuffer;
    }
  }
  
  public void copyFrom(SurfaceControl paramSurfaceControl)
  {
    if (paramSurfaceControl != null)
    {
      long l = mNativeObject;
      if (l != 0L)
      {
        l = nativeGetFromSurfaceControl(l);
        synchronized (mLock)
        {
          if (mNativeObject != 0L) {
            nativeRelease(mNativeObject);
          }
          setNativeObjectLocked(l);
          return;
        }
      }
      throw new NullPointerException("null SurfaceControl native object. Are you using a released SurfaceControl?");
    }
    throw new IllegalArgumentException("other must not be null");
  }
  
  public void createFrom(SurfaceControl paramSurfaceControl)
  {
    if (paramSurfaceControl != null)
    {
      long l = mNativeObject;
      if (l != 0L)
      {
        l = nativeCreateFromSurfaceControl(l);
        synchronized (mLock)
        {
          if (mNativeObject != 0L) {
            nativeRelease(mNativeObject);
          }
          setNativeObjectLocked(l);
          return;
        }
      }
      throw new NullPointerException("null SurfaceControl native object. Are you using a released SurfaceControl?");
    }
    throw new IllegalArgumentException("other must not be null");
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void destroy()
  {
    release();
  }
  
  protected void finalize()
    throws Throwable
  {
    try
    {
      if (mCloseGuard != null) {
        mCloseGuard.warnIfOpen();
      }
      release();
      return;
    }
    finally
    {
      super.finalize();
    }
  }
  
  void forceScopedDisconnect()
  {
    synchronized (mLock)
    {
      checkNotReleasedLocked();
      if (nativeForceScopedDisconnect(mNativeObject) == 0) {
        return;
      }
      RuntimeException localRuntimeException = new java/lang/RuntimeException;
      localRuntimeException.<init>("Failed to disconnect Surface instance (bad object?)");
      throw localRuntimeException;
    }
  }
  
  public int getGenerationId()
  {
    synchronized (mLock)
    {
      int i = mGenerationId;
      return i;
    }
  }
  
  public long getNextFrameNumber()
  {
    synchronized (mLock)
    {
      checkNotReleasedLocked();
      long l = nativeGetNextFrameNumber(mNativeObject);
      return l;
    }
  }
  
  public void hwuiDestroy()
  {
    if (mHwuiContext != null)
    {
      mHwuiContext.destroy();
      mHwuiContext = null;
    }
  }
  
  public boolean isAutoRefreshEnabled()
  {
    return mIsAutoRefreshEnabled;
  }
  
  public boolean isConsumerRunningBehind()
  {
    synchronized (mLock)
    {
      checkNotReleasedLocked();
      boolean bool = nativeIsConsumerRunningBehind(mNativeObject);
      return bool;
    }
  }
  
  public boolean isSharedBufferModeEnabled()
  {
    return mIsSharedBufferModeEnabled;
  }
  
  public boolean isSingleBuffered()
  {
    return mIsSingleBuffered;
  }
  
  public boolean isValid()
  {
    synchronized (mLock)
    {
      if (mNativeObject == 0L) {
        return false;
      }
      boolean bool = nativeIsValid(mNativeObject);
      return bool;
    }
  }
  
  public Canvas lockCanvas(Rect paramRect)
    throws Surface.OutOfResourcesException, IllegalArgumentException
  {
    synchronized (mLock)
    {
      checkNotReleasedLocked();
      if (mLockedObject == 0L)
      {
        mLockedObject = nativeLockCanvas(mNativeObject, mCanvas, paramRect);
        paramRect = mCanvas;
        return paramRect;
      }
      paramRect = new java/lang/IllegalArgumentException;
      paramRect.<init>("Surface was already locked");
      throw paramRect;
    }
  }
  
  public Canvas lockHardwareCanvas()
  {
    synchronized (mLock)
    {
      checkNotReleasedLocked();
      if (mHwuiContext == null)
      {
        localObject2 = new android/view/Surface$HwuiContext;
        ((HwuiContext)localObject2).<init>(this, false);
        mHwuiContext = ((HwuiContext)localObject2);
      }
      Object localObject2 = mHwuiContext.lockCanvas(nativeGetWidth(mNativeObject), nativeGetHeight(mNativeObject));
      return localObject2;
    }
  }
  
  public Canvas lockHardwareWideColorGamutCanvas()
  {
    synchronized (mLock)
    {
      checkNotReleasedLocked();
      if ((mHwuiContext != null) && (!mHwuiContext.isWideColorGamut()))
      {
        mHwuiContext.destroy();
        mHwuiContext = null;
      }
      if (mHwuiContext == null)
      {
        localObject2 = new android/view/Surface$HwuiContext;
        ((HwuiContext)localObject2).<init>(this, true);
        mHwuiContext = ((HwuiContext)localObject2);
      }
      Object localObject2 = mHwuiContext.lockCanvas(nativeGetWidth(mNativeObject), nativeGetHeight(mNativeObject));
      return localObject2;
    }
  }
  
  public void readFromParcel(Parcel paramParcel)
  {
    if (paramParcel != null) {
      synchronized (mLock)
      {
        mName = paramParcel.readString();
        boolean bool;
        if (paramParcel.readInt() != 0) {
          bool = true;
        } else {
          bool = false;
        }
        mIsSingleBuffered = bool;
        setNativeObjectLocked(nativeReadFromParcel(mNativeObject, paramParcel));
        return;
      }
    }
    throw new IllegalArgumentException("source must not be null");
  }
  
  public void release()
  {
    synchronized (mLock)
    {
      if (mNativeObject != 0L)
      {
        nativeRelease(mNativeObject);
        setNativeObjectLocked(0L);
      }
      if (mHwuiContext != null)
      {
        mHwuiContext.destroy();
        mHwuiContext = null;
      }
      return;
    }
  }
  
  public void setAutoRefreshEnabled(boolean paramBoolean)
  {
    if (mIsAutoRefreshEnabled != paramBoolean) {
      if (nativeSetAutoRefreshEnabled(mNativeObject, paramBoolean) == 0) {
        mIsAutoRefreshEnabled = paramBoolean;
      } else {
        throw new RuntimeException("Failed to set auto refresh on Surface (bad object?)");
      }
    }
  }
  
  void setCompatibilityTranslator(CompatibilityInfo.Translator paramTranslator)
  {
    if (paramTranslator != null)
    {
      float f = applicationScale;
      mCompatibleMatrix = new Matrix();
      mCompatibleMatrix.setScale(f, f);
    }
  }
  
  void setScalingMode(int paramInt)
  {
    synchronized (mLock)
    {
      checkNotReleasedLocked();
      if (nativeSetScalingMode(mNativeObject, paramInt) == 0) {
        return;
      }
      IllegalArgumentException localIllegalArgumentException = new java/lang/IllegalArgumentException;
      StringBuilder localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append("Invalid scaling mode: ");
      localStringBuilder.append(paramInt);
      localIllegalArgumentException.<init>(localStringBuilder.toString());
      throw localIllegalArgumentException;
    }
  }
  
  public void setSharedBufferModeEnabled(boolean paramBoolean)
  {
    if (mIsSharedBufferModeEnabled != paramBoolean) {
      if (nativeSetSharedBufferModeEnabled(mNativeObject, paramBoolean) == 0) {
        mIsSharedBufferModeEnabled = paramBoolean;
      } else {
        throw new RuntimeException("Failed to set shared buffer mode on Surface (bad object?)");
      }
    }
  }
  
  public String toString()
  {
    synchronized (mLock)
    {
      Object localObject2 = new java/lang/StringBuilder;
      ((StringBuilder)localObject2).<init>();
      ((StringBuilder)localObject2).append("Surface(name=");
      ((StringBuilder)localObject2).append(mName);
      ((StringBuilder)localObject2).append(")/@0x");
      ((StringBuilder)localObject2).append(Integer.toHexString(System.identityHashCode(this)));
      localObject2 = ((StringBuilder)localObject2).toString();
      return localObject2;
    }
  }
  
  @Deprecated
  public void transferFrom(Surface arg1)
  {
    if (??? != null)
    {
      if (??? != this) {
        synchronized (mLock)
        {
          long l = mNativeObject;
          ???.setNativeObjectLocked(0L);
          synchronized (mLock)
          {
            if (mNativeObject != 0L) {
              nativeRelease(mNativeObject);
            }
            setNativeObjectLocked(l);
          }
        }
      }
      return;
    }
    throw new IllegalArgumentException("other must not be null");
  }
  
  @Deprecated
  public void unlockCanvas(Canvas paramCanvas)
  {
    throw new UnsupportedOperationException();
  }
  
  public void unlockCanvasAndPost(Canvas paramCanvas)
  {
    synchronized (mLock)
    {
      checkNotReleasedLocked();
      if (mHwuiContext != null) {
        mHwuiContext.unlockAndPost(paramCanvas);
      } else {
        unlockSwCanvasAndPost(paramCanvas);
      }
      return;
    }
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    if (paramParcel != null) {
      synchronized (mLock)
      {
        paramParcel.writeString(mName);
        paramParcel.writeInt(mIsSingleBuffered);
        nativeWriteToParcel(mNativeObject, paramParcel);
        if ((paramInt & 0x1) != 0) {
          release();
        }
        return;
      }
    }
    throw new IllegalArgumentException("dest must not be null");
  }
  
  private final class CompatibleCanvas
    extends Canvas
  {
    private Matrix mOrigMatrix = null;
    
    private CompatibleCanvas() {}
    
    public void getMatrix(Matrix paramMatrix)
    {
      super.getMatrix(paramMatrix);
      if (mOrigMatrix == null) {
        mOrigMatrix = new Matrix();
      }
      mOrigMatrix.set(paramMatrix);
    }
    
    public void setMatrix(Matrix paramMatrix)
    {
      if ((mCompatibleMatrix != null) && (mOrigMatrix != null) && (!mOrigMatrix.equals(paramMatrix)))
      {
        Matrix localMatrix = new Matrix(mCompatibleMatrix);
        localMatrix.preConcat(paramMatrix);
        super.setMatrix(localMatrix);
      }
      else
      {
        super.setMatrix(paramMatrix);
      }
    }
  }
  
  private final class HwuiContext
  {
    private DisplayListCanvas mCanvas;
    private long mHwuiRenderer;
    private final boolean mIsWideColorGamut;
    private final RenderNode mRenderNode = RenderNode.create("HwuiCanvas", null);
    
    HwuiContext(boolean paramBoolean)
    {
      mRenderNode.setClipToBounds(false);
      mIsWideColorGamut = paramBoolean;
      mHwuiRenderer = Surface.nHwuiCreate(mRenderNode.mNativeRenderNode, mNativeObject, paramBoolean);
    }
    
    void destroy()
    {
      if (mHwuiRenderer != 0L)
      {
        Surface.nHwuiDestroy(mHwuiRenderer);
        mHwuiRenderer = 0L;
      }
    }
    
    boolean isWideColorGamut()
    {
      return mIsWideColorGamut;
    }
    
    Canvas lockCanvas(int paramInt1, int paramInt2)
    {
      if (mCanvas == null)
      {
        mCanvas = mRenderNode.start(paramInt1, paramInt2);
        return mCanvas;
      }
      throw new IllegalStateException("Surface was already locked!");
    }
    
    void unlockAndPost(Canvas paramCanvas)
    {
      if (paramCanvas == mCanvas)
      {
        mRenderNode.end(mCanvas);
        mCanvas = null;
        Surface.nHwuiDraw(mHwuiRenderer);
        return;
      }
      throw new IllegalArgumentException("canvas object must be the same instance that was previously returned by lockCanvas");
    }
    
    void updateSurface()
    {
      Surface.nHwuiSetSurface(mHwuiRenderer, mNativeObject);
    }
  }
  
  public static class OutOfResourcesException
    extends RuntimeException
  {
    public OutOfResourcesException() {}
    
    public OutOfResourcesException(String paramString)
    {
      super();
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface Rotation {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface ScalingMode {}
}
