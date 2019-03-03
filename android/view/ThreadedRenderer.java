package android.view;

import android.app.ActivityManager;
import android.app.IActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.AnimatedVectorDrawable.VectorDrawableAnimatorRT;
import android.os.IBinder;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.os.Trace;
import android.util.Log;
import android.view.animation.AnimationUtils;
import com.android.internal.R.styleable;
import com.android.internal.util.VirtualRefBasePtr;
import java.io.File;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

public final class ThreadedRenderer
{
  private static final String CACHE_PATH_SHADERS = "com.android.opengl.shaders_cache";
  private static final String CACHE_PATH_SKIASHADERS = "com.android.skia.shaders_cache";
  public static final String DEBUG_DIRTY_REGIONS_PROPERTY = "debug.hwui.show_dirty_regions";
  public static final String DEBUG_FPS_DIVISOR = "debug.hwui.fps_divisor";
  public static final String DEBUG_OVERDRAW_PROPERTY = "debug.hwui.overdraw";
  public static final String DEBUG_SHOW_LAYERS_UPDATES_PROPERTY = "debug.hwui.show_layers_updates";
  public static final String DEBUG_SHOW_NON_RECTANGULAR_CLIP_PROPERTY = "debug.hwui.show_non_rect_clip";
  public static int EGL_CONTEXT_PRIORITY_HIGH_IMG = 12545;
  public static int EGL_CONTEXT_PRIORITY_LOW_IMG = 0;
  public static int EGL_CONTEXT_PRIORITY_MEDIUM_IMG = 12546;
  private static final int FLAG_DUMP_ALL = 1;
  private static final int FLAG_DUMP_FRAMESTATS = 1;
  private static final int FLAG_DUMP_RESET = 2;
  private static final String LOG_TAG = "ThreadedRenderer";
  public static final String OVERDRAW_PROPERTY_SHOW = "show";
  static final String PRINT_CONFIG_PROPERTY = "debug.hwui.print_config";
  static final String PROFILE_MAXFRAMES_PROPERTY = "debug.hwui.profile.maxframes";
  public static final String PROFILE_PROPERTY = "debug.hwui.profile";
  public static final String PROFILE_PROPERTY_VISUALIZE_BARS = "visual_bars";
  private static final int SYNC_CONTEXT_IS_STOPPED = 4;
  private static final int SYNC_FRAME_DROPPED = 8;
  private static final int SYNC_INVALIDATE_REQUIRED = 1;
  private static final int SYNC_LOST_SURFACE_REWARD_IF_FOUND = 2;
  private static final int SYNC_OK = 0;
  private static final String[] VISUALIZERS = { "visual_bars" };
  public static boolean sRendererDisabled;
  private static Boolean sSupportsOpenGL;
  public static boolean sSystemRendererDisabled;
  public static boolean sTrimForeground;
  private final int mAmbientShadowAlpha;
  private boolean mEnabled;
  private boolean mHasInsets;
  private int mHeight;
  private boolean mInitialized = false;
  private int mInsetLeft;
  private int mInsetTop;
  private boolean mIsOpaque = false;
  private final float mLightRadius;
  private final float mLightY;
  private final float mLightZ;
  private long mNativeProxy;
  private boolean mRequested = true;
  private RenderNode mRootNode;
  private boolean mRootNodeNeedsUpdate;
  private final int mSpotShadowAlpha;
  private int mSurfaceHeight;
  private int mSurfaceWidth;
  private int mWidth;
  
  static
  {
    EGL_CONTEXT_PRIORITY_LOW_IMG = 12547;
    isAvailable();
    sRendererDisabled = false;
    sSystemRendererDisabled = false;
    sTrimForeground = false;
  }
  
  ThreadedRenderer(Context paramContext, boolean paramBoolean, String paramString)
  {
    TypedArray localTypedArray = paramContext.obtainStyledAttributes(null, R.styleable.Lighting, 0, 0);
    mLightY = localTypedArray.getDimension(3, 0.0F);
    mLightZ = localTypedArray.getDimension(4, 0.0F);
    mLightRadius = localTypedArray.getDimension(2, 0.0F);
    mAmbientShadowAlpha = ((int)(localTypedArray.getFloat(0, 0.0F) * 255.0F + 0.5F));
    mSpotShadowAlpha = ((int)(255.0F * localTypedArray.getFloat(1, 0.0F) + 0.5F));
    localTypedArray.recycle();
    long l = nCreateRootRenderNode();
    mRootNode = RenderNode.adopt(l);
    mRootNode.setClipToBounds(false);
    mIsOpaque = (paramBoolean ^ true);
    mNativeProxy = nCreateProxy(paramBoolean, l);
    nSetName(mNativeProxy, paramString);
    ProcessInitializer.sInstance.init(paramContext, mNativeProxy);
    loadSystemProperties();
  }
  
  public static int copySurfaceInto(Surface paramSurface, Rect paramRect, Bitmap paramBitmap)
  {
    if (paramRect == null) {
      return nCopySurfaceInto(paramSurface, 0, 0, 0, 0, paramBitmap);
    }
    return nCopySurfaceInto(paramSurface, left, top, right, bottom, paramBitmap);
  }
  
  public static ThreadedRenderer create(Context paramContext, boolean paramBoolean, String paramString)
  {
    ThreadedRenderer localThreadedRenderer = null;
    if (isAvailable()) {
      localThreadedRenderer = new ThreadedRenderer(paramContext, paramBoolean, paramString);
    }
    return localThreadedRenderer;
  }
  
  public static Bitmap createHardwareBitmap(RenderNode paramRenderNode, int paramInt1, int paramInt2)
  {
    return nCreateHardwareBitmap(paramRenderNode.getNativeDisplayList(), paramInt1, paramInt2);
  }
  
  private static void destroyResources(View paramView)
  {
    paramView.destroyHardwareResources();
  }
  
  public static void disable(boolean paramBoolean)
  {
    sRendererDisabled = true;
    if (paramBoolean) {
      sSystemRendererDisabled = true;
    }
  }
  
  public static native void disableVsync();
  
  public static void enableForegroundTrimming()
  {
    sTrimForeground = true;
  }
  
  static void invokeFunctor(long paramLong, boolean paramBoolean)
  {
    nInvokeFunctor(paramLong, paramBoolean);
  }
  
  public static boolean isAvailable()
  {
    if (sSupportsOpenGL != null) {
      return sSupportsOpenGL.booleanValue();
    }
    boolean bool = false;
    if (SystemProperties.getInt("ro.kernel.qemu", 0) == 0)
    {
      sSupportsOpenGL = Boolean.valueOf(true);
      return true;
    }
    int i = SystemProperties.getInt("qemu.gles", -1);
    if (i == -1) {
      return false;
    }
    if (i > 0) {
      bool = true;
    }
    sSupportsOpenGL = Boolean.valueOf(bool);
    return sSupportsOpenGL.booleanValue();
  }
  
  private static native long nAddFrameMetricsObserver(long paramLong, FrameMetricsObserver paramFrameMetricsObserver);
  
  private static native void nAddRenderNode(long paramLong1, long paramLong2, boolean paramBoolean);
  
  private static native void nBuildLayer(long paramLong1, long paramLong2);
  
  private static native void nCancelLayerUpdate(long paramLong1, long paramLong2);
  
  private static native boolean nCopyLayerInto(long paramLong1, long paramLong2, Bitmap paramBitmap);
  
  private static native int nCopySurfaceInto(Surface paramSurface, int paramInt1, int paramInt2, int paramInt3, int paramInt4, Bitmap paramBitmap);
  
  private static native Bitmap nCreateHardwareBitmap(long paramLong, int paramInt1, int paramInt2);
  
  private static native long nCreateProxy(boolean paramBoolean, long paramLong);
  
  private static native long nCreateRootRenderNode();
  
  private static native long nCreateTextureLayer(long paramLong);
  
  private static native void nDeleteProxy(long paramLong);
  
  private static native void nDestroy(long paramLong1, long paramLong2);
  
  private static native void nDestroyHardwareResources(long paramLong);
  
  private static native void nDetachSurfaceTexture(long paramLong1, long paramLong2);
  
  private static native void nDrawRenderNode(long paramLong1, long paramLong2);
  
  private static native void nDumpProfileInfo(long paramLong, FileDescriptor paramFileDescriptor, int paramInt);
  
  private static native void nFence(long paramLong);
  
  private static native int nGetRenderThreadTid(long paramLong);
  
  private static native void nHackySetRTAnimationsEnabled(boolean paramBoolean);
  
  private static native void nInitialize(long paramLong, Surface paramSurface);
  
  private static native void nInvokeFunctor(long paramLong, boolean paramBoolean);
  
  private static native boolean nLoadSystemProperties(long paramLong);
  
  private static native void nNotifyFramePending(long paramLong);
  
  private static native void nOverrideProperty(String paramString1, String paramString2);
  
  private static native boolean nPauseSurface(long paramLong, Surface paramSurface);
  
  private static native void nPushLayerUpdate(long paramLong1, long paramLong2);
  
  private static native void nRegisterAnimatingRenderNode(long paramLong1, long paramLong2);
  
  private static native void nRegisterVectorDrawableAnimator(long paramLong1, long paramLong2);
  
  private static native void nRemoveFrameMetricsObserver(long paramLong1, long paramLong2);
  
  private static native void nRemoveRenderNode(long paramLong1, long paramLong2);
  
  private static native void nRotateProcessStatsBuffer();
  
  private static native void nSerializeDisplayListTree(long paramLong);
  
  private static native void nSetContentDrawBounds(long paramLong, int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  
  private static native void nSetContextPriority(int paramInt);
  
  private static native void nSetDebuggingEnabled(boolean paramBoolean);
  
  private static native void nSetFrameCallback(long paramLong, FrameDrawingCallback paramFrameDrawingCallback);
  
  private static native void nSetFrameCompleteCallback(long paramLong, FrameCompleteCallback paramFrameCompleteCallback);
  
  private static native void nSetHighContrastText(boolean paramBoolean);
  
  private static native void nSetIsolatedProcess(boolean paramBoolean);
  
  private static native void nSetLightCenter(long paramLong, float paramFloat1, float paramFloat2, float paramFloat3);
  
  private static native void nSetName(long paramLong, String paramString);
  
  private static native void nSetOpaque(long paramLong, boolean paramBoolean);
  
  private static native void nSetProcessStatsBuffer(int paramInt);
  
  private static native void nSetStopped(long paramLong, boolean paramBoolean);
  
  private static native void nSetWideGamut(long paramLong, boolean paramBoolean);
  
  private static native void nSetup(long paramLong, float paramFloat, int paramInt1, int paramInt2);
  
  private static native void nStopDrawing(long paramLong);
  
  private static native int nSyncAndDrawFrame(long paramLong, long[] paramArrayOfLong, int paramInt);
  
  private static native void nTrimMemory(int paramInt);
  
  private static native void nUpdateSurface(long paramLong, Surface paramSurface);
  
  public static void overrideProperty(String paramString1, String paramString2)
  {
    if ((paramString1 != null) && (paramString2 != null))
    {
      nOverrideProperty(paramString1, paramString2);
      return;
    }
    throw new IllegalArgumentException("name and value must be non-null");
  }
  
  public static void setContextPriority(int paramInt)
  {
    nSetContextPriority(paramInt);
  }
  
  public static void setDebuggingEnabled(boolean paramBoolean)
  {
    nSetDebuggingEnabled(paramBoolean);
  }
  
  public static void setFPSDivisor(int paramInt)
  {
    boolean bool = true;
    if (paramInt > 1) {
      bool = false;
    }
    nHackySetRTAnimationsEnabled(bool);
  }
  
  public static void setHighContrastText(boolean paramBoolean)
  {
    nSetHighContrastText(paramBoolean);
  }
  
  public static void setIsolatedProcess(boolean paramBoolean)
  {
    nSetIsolatedProcess(paramBoolean);
  }
  
  public static void setupDiskCache(File paramFile)
  {
    setupShadersDiskCache(new File(paramFile, "com.android.opengl.shaders_cache").getAbsolutePath(), new File(paramFile, "com.android.skia.shaders_cache").getAbsolutePath());
  }
  
  static native void setupShadersDiskCache(String paramString1, String paramString2);
  
  public static void trimMemory(int paramInt)
  {
    nTrimMemory(paramInt);
  }
  
  private void updateEnabledState(Surface paramSurface)
  {
    if ((paramSurface != null) && (paramSurface.isValid())) {
      setEnabled(mInitialized);
    } else {
      setEnabled(false);
    }
  }
  
  private void updateRootDisplayList(View paramView, DrawCallbacks paramDrawCallbacks)
  {
    Trace.traceBegin(8L, "Record View#draw()");
    updateViewTreeDisplayList(paramView);
    DisplayListCanvas localDisplayListCanvas;
    if ((mRootNodeNeedsUpdate) || (!mRootNode.isValid())) {
      localDisplayListCanvas = mRootNode.start(mSurfaceWidth, mSurfaceHeight);
    }
    try
    {
      int i = localDisplayListCanvas.save();
      localDisplayListCanvas.translate(mInsetLeft, mInsetTop);
      paramDrawCallbacks.onPreDraw(localDisplayListCanvas);
      localDisplayListCanvas.insertReorderBarrier();
      localDisplayListCanvas.drawRenderNode(paramView.updateDisplayListIfDirty());
      localDisplayListCanvas.insertInorderBarrier();
      paramDrawCallbacks.onPostDraw(localDisplayListCanvas);
      localDisplayListCanvas.restoreToCount(i);
      mRootNodeNeedsUpdate = false;
      mRootNode.end(localDisplayListCanvas);
      Trace.traceEnd(8L);
      return;
    }
    finally
    {
      mRootNode.end(localDisplayListCanvas);
    }
  }
  
  private void updateViewTreeDisplayList(View paramView)
  {
    mPrivateFlags |= 0x20;
    boolean bool;
    if ((mPrivateFlags & 0x80000000) == Integer.MIN_VALUE) {
      bool = true;
    } else {
      bool = false;
    }
    mRecreateDisplayList = bool;
    mPrivateFlags &= 0x7FFFFFFF;
    paramView.updateDisplayListIfDirty();
    mRecreateDisplayList = false;
  }
  
  void addFrameMetricsObserver(FrameMetricsObserver paramFrameMetricsObserver)
  {
    mNative = new VirtualRefBasePtr(nAddFrameMetricsObserver(mNativeProxy, paramFrameMetricsObserver));
  }
  
  public void addRenderNode(RenderNode paramRenderNode, boolean paramBoolean)
  {
    nAddRenderNode(mNativeProxy, mNativeRenderNode, paramBoolean);
  }
  
  void buildLayer(RenderNode paramRenderNode)
  {
    nBuildLayer(mNativeProxy, paramRenderNode.getNativeDisplayList());
  }
  
  boolean copyLayerInto(TextureLayer paramTextureLayer, Bitmap paramBitmap)
  {
    return nCopyLayerInto(mNativeProxy, paramTextureLayer.getDeferredLayerUpdater(), paramBitmap);
  }
  
  TextureLayer createTextureLayer()
  {
    return TextureLayer.adoptTextureLayer(this, nCreateTextureLayer(mNativeProxy));
  }
  
  void destroy()
  {
    mInitialized = false;
    updateEnabledState(null);
    nDestroy(mNativeProxy, mRootNode.mNativeRenderNode);
  }
  
  void destroyHardwareResources(View paramView)
  {
    destroyResources(paramView);
    nDestroyHardwareResources(mNativeProxy);
  }
  
  void detachSurfaceTexture(long paramLong)
  {
    nDetachSurfaceTexture(mNativeProxy, paramLong);
  }
  
  void draw(View paramView, View.AttachInfo paramAttachInfo, DrawCallbacks paramDrawCallbacks, FrameDrawingCallback paramFrameDrawingCallback)
  {
    mIgnoreDirtyState = true;
    Choreographer localChoreographer = mViewRootImpl.mChoreographer;
    mFrameInfo.markDrawStart();
    updateRootDisplayList(paramView, paramDrawCallbacks);
    mIgnoreDirtyState = false;
    if (mPendingAnimatingRenderNodes != null)
    {
      int i = mPendingAnimatingRenderNodes.size();
      for (j = 0; j < i; j++) {
        registerAnimatingRenderNode((RenderNode)mPendingAnimatingRenderNodes.get(j));
      }
      mPendingAnimatingRenderNodes.clear();
      mPendingAnimatingRenderNodes = null;
    }
    paramView = mFrameInfo.mFrameInfo;
    if (paramFrameDrawingCallback != null) {
      nSetFrameCallback(mNativeProxy, paramFrameDrawingCallback);
    }
    int j = nSyncAndDrawFrame(mNativeProxy, paramView, paramView.length);
    if ((j & 0x2) != 0)
    {
      setEnabled(false);
      mViewRootImpl.mSurface.release();
      mViewRootImpl.invalidate();
    }
    if ((j & 0x1) != 0) {
      mViewRootImpl.invalidate();
    }
  }
  
  public void drawRenderNode(RenderNode paramRenderNode)
  {
    nDrawRenderNode(mNativeProxy, mNativeRenderNode);
  }
  
  void dumpGfxInfo(PrintWriter paramPrintWriter, FileDescriptor paramFileDescriptor, String[] paramArrayOfString)
  {
    paramPrintWriter.flush();
    int i;
    if ((paramArrayOfString != null) && (paramArrayOfString.length != 0)) {
      i = 0;
    } else {
      i = 1;
    }
    int j = 0;
    for (int k = i; j < paramArrayOfString.length; k = i)
    {
      paramPrintWriter = paramArrayOfString[j];
      i = paramPrintWriter.hashCode();
      if (i != -252053678)
      {
        if (i != 1492)
        {
          if ((i == 108404047) && (paramPrintWriter.equals("reset")))
          {
            i = 1;
            break label128;
          }
        }
        else if (paramPrintWriter.equals("-a"))
        {
          i = 2;
          break label128;
        }
      }
      else if (paramPrintWriter.equals("framestats"))
      {
        i = 0;
        break label128;
      }
      i = -1;
      switch (i)
      {
      default: 
        i = k;
        break;
      case 2: 
        i = 1;
        break;
      case 1: 
        i = k | 0x2;
        break;
      case 0: 
        label128:
        i = k | 0x1;
      }
      j++;
    }
    nDumpProfileInfo(mNativeProxy, paramFileDescriptor, k);
  }
  
  void fence()
  {
    nFence(mNativeProxy);
  }
  
  protected void finalize()
    throws Throwable
  {
    try
    {
      nDeleteProxy(mNativeProxy);
      mNativeProxy = 0L;
      return;
    }
    finally
    {
      super.finalize();
    }
  }
  
  int getHeight()
  {
    return mHeight;
  }
  
  int getWidth()
  {
    return mWidth;
  }
  
  boolean initialize(Surface paramSurface)
    throws Surface.OutOfResourcesException
  {
    boolean bool = mInitialized;
    mInitialized = true;
    updateEnabledState(paramSurface);
    nInitialize(mNativeProxy, paramSurface);
    return bool ^ true;
  }
  
  boolean initializeIfNeeded(int paramInt1, int paramInt2, View.AttachInfo paramAttachInfo, Surface paramSurface, Rect paramRect)
    throws Surface.OutOfResourcesException
  {
    if ((isRequested()) && (!isEnabled()) && (initialize(paramSurface)))
    {
      setup(paramInt1, paramInt2, paramAttachInfo, paramRect);
      return true;
    }
    return false;
  }
  
  void invalidateRoot()
  {
    mRootNodeNeedsUpdate = true;
  }
  
  boolean isEnabled()
  {
    return mEnabled;
  }
  
  boolean isOpaque()
  {
    return mIsOpaque;
  }
  
  boolean isRequested()
  {
    return mRequested;
  }
  
  boolean loadSystemProperties()
  {
    boolean bool = nLoadSystemProperties(mNativeProxy);
    if (bool) {
      invalidateRoot();
    }
    return bool;
  }
  
  public void notifyFramePending()
  {
    nNotifyFramePending(mNativeProxy);
  }
  
  void onLayerDestroyed(TextureLayer paramTextureLayer)
  {
    nCancelLayerUpdate(mNativeProxy, paramTextureLayer.getDeferredLayerUpdater());
  }
  
  boolean pauseSurface(Surface paramSurface)
  {
    return nPauseSurface(mNativeProxy, paramSurface);
  }
  
  void pushLayerUpdate(TextureLayer paramTextureLayer)
  {
    nPushLayerUpdate(mNativeProxy, paramTextureLayer.getDeferredLayerUpdater());
  }
  
  void registerAnimatingRenderNode(RenderNode paramRenderNode)
  {
    nRegisterAnimatingRenderNode(mRootNode.mNativeRenderNode, mNativeRenderNode);
  }
  
  void registerVectorDrawableAnimator(AnimatedVectorDrawable.VectorDrawableAnimatorRT paramVectorDrawableAnimatorRT)
  {
    nRegisterVectorDrawableAnimator(mRootNode.mNativeRenderNode, paramVectorDrawableAnimatorRT.getAnimatorNativePtr());
  }
  
  void removeFrameMetricsObserver(FrameMetricsObserver paramFrameMetricsObserver)
  {
    nRemoveFrameMetricsObserver(mNativeProxy, mNative.get());
    mNative = null;
  }
  
  public void removeRenderNode(RenderNode paramRenderNode)
  {
    nRemoveRenderNode(mNativeProxy, mNativeRenderNode);
  }
  
  public void serializeDisplayListTree()
  {
    nSerializeDisplayListTree(mNativeProxy);
  }
  
  public void setContentDrawBounds(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    nSetContentDrawBounds(mNativeProxy, paramInt1, paramInt2, paramInt3, paramInt4);
  }
  
  void setEnabled(boolean paramBoolean)
  {
    mEnabled = paramBoolean;
  }
  
  void setFrameCompleteCallback(FrameCompleteCallback paramFrameCompleteCallback)
  {
    nSetFrameCompleteCallback(mNativeProxy, paramFrameCompleteCallback);
  }
  
  void setLightCenter(View.AttachInfo paramAttachInfo)
  {
    Point localPoint = mPoint;
    mDisplay.getRealSize(localPoint);
    float f1 = x / 2.0F;
    float f2 = mWindowLeft;
    float f3 = mLightY;
    float f4 = mWindowTop;
    nSetLightCenter(mNativeProxy, f1 - f2, f3 - f4, mLightZ);
  }
  
  void setOpaque(boolean paramBoolean)
  {
    if ((paramBoolean) && (!mHasInsets)) {
      paramBoolean = true;
    } else {
      paramBoolean = false;
    }
    mIsOpaque = paramBoolean;
    nSetOpaque(mNativeProxy, mIsOpaque);
  }
  
  void setRequested(boolean paramBoolean)
  {
    mRequested = paramBoolean;
  }
  
  void setStopped(boolean paramBoolean)
  {
    nSetStopped(mNativeProxy, paramBoolean);
  }
  
  void setWideGamut(boolean paramBoolean)
  {
    nSetWideGamut(mNativeProxy, paramBoolean);
  }
  
  void setup(int paramInt1, int paramInt2, View.AttachInfo paramAttachInfo, Rect paramRect)
  {
    mWidth = paramInt1;
    mHeight = paramInt2;
    if ((paramRect != null) && ((left != 0) || (right != 0) || (top != 0) || (bottom != 0)))
    {
      mHasInsets = true;
      mInsetLeft = left;
      mInsetTop = top;
      mSurfaceWidth = (mInsetLeft + paramInt1 + right);
      mSurfaceHeight = (mInsetTop + paramInt2 + bottom);
      setOpaque(false);
    }
    else
    {
      mHasInsets = false;
      mInsetLeft = 0;
      mInsetTop = 0;
      mSurfaceWidth = paramInt1;
      mSurfaceHeight = paramInt2;
    }
    mRootNode.setLeftTopRightBottom(-mInsetLeft, -mInsetTop, mSurfaceWidth, mSurfaceHeight);
    nSetup(mNativeProxy, mLightRadius, mAmbientShadowAlpha, mSpotShadowAlpha);
    setLightCenter(paramAttachInfo);
  }
  
  void stopDrawing()
  {
    nStopDrawing(mNativeProxy);
  }
  
  void updateSurface(Surface paramSurface)
    throws Surface.OutOfResourcesException
  {
    updateEnabledState(paramSurface);
    nUpdateSurface(mNativeProxy, paramSurface);
  }
  
  static abstract interface DrawCallbacks
  {
    public abstract void onPostDraw(DisplayListCanvas paramDisplayListCanvas);
    
    public abstract void onPreDraw(DisplayListCanvas paramDisplayListCanvas);
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface DumpFlags {}
  
  public static abstract interface FrameCompleteCallback
  {
    public abstract void onFrameComplete(long paramLong);
  }
  
  public static abstract interface FrameDrawingCallback
  {
    public abstract void onFrameDraw(long paramLong);
  }
  
  private static class ProcessInitializer
  {
    static ProcessInitializer sInstance = new ProcessInitializer();
    private Context mAppContext;
    private IGraphicsStatsCallback mGraphicsStatsCallback = new IGraphicsStatsCallback.Stub()
    {
      public void onRotateGraphicsStatsBuffer()
        throws RemoteException
      {
        ThreadedRenderer.ProcessInitializer.this.rotateBuffer();
      }
    };
    private IGraphicsStats mGraphicsStatsService;
    private boolean mInitialized = false;
    
    private ProcessInitializer() {}
    
    private void initGraphicsStats()
    {
      try
      {
        IBinder localIBinder = ServiceManager.getService("graphicsstats");
        if (localIBinder == null) {
          return;
        }
        mGraphicsStatsService = IGraphicsStats.Stub.asInterface(localIBinder);
        requestBuffer();
      }
      catch (Throwable localThrowable)
      {
        Log.w("ThreadedRenderer", "Could not acquire gfx stats buffer", localThrowable);
      }
    }
    
    private void initSched(long paramLong)
    {
      try
      {
        int i = ThreadedRenderer.nGetRenderThreadTid(paramLong);
        ActivityManager.getService().setRenderThread(i);
      }
      catch (Throwable localThrowable)
      {
        Log.w("ThreadedRenderer", "Failed to set scheduler for RenderThread", localThrowable);
      }
    }
    
    private void requestBuffer()
    {
      try
      {
        Object localObject = mAppContext.getApplicationInfo().packageName;
        localObject = mGraphicsStatsService.requestBufferForProcess((String)localObject, mGraphicsStatsCallback);
        ThreadedRenderer.nSetProcessStatsBuffer(((ParcelFileDescriptor)localObject).getFd());
        ((ParcelFileDescriptor)localObject).close();
      }
      catch (Throwable localThrowable)
      {
        Log.w("ThreadedRenderer", "Could not acquire gfx stats buffer", localThrowable);
      }
    }
    
    private void rotateBuffer()
    {
      ThreadedRenderer.access$1300();
      requestBuffer();
    }
    
    void init(Context paramContext, long paramLong)
    {
      try
      {
        boolean bool = mInitialized;
        if (bool) {
          return;
        }
        mInitialized = true;
        mAppContext = paramContext.getApplicationContext();
        initSched(paramLong);
        if (mAppContext != null) {
          initGraphicsStats();
        }
        return;
      }
      finally {}
    }
  }
  
  public static class SimpleRenderer
  {
    private final FrameInfo mFrameInfo = new FrameInfo();
    private final float mLightY;
    private final float mLightZ;
    private long mNativeProxy;
    private final RenderNode mRootNode;
    private Surface mSurface;
    
    public SimpleRenderer(Context paramContext, String paramString, Surface paramSurface)
    {
      TypedArray localTypedArray = paramContext.obtainStyledAttributes(null, R.styleable.Lighting, 0, 0);
      mLightY = localTypedArray.getDimension(3, 0.0F);
      mLightZ = localTypedArray.getDimension(4, 0.0F);
      float f = localTypedArray.getDimension(2, 0.0F);
      int i = (int)(localTypedArray.getFloat(0, 0.0F) * 255.0F + 0.5F);
      int j = (int)(255.0F * localTypedArray.getFloat(1, 0.0F) + 0.5F);
      localTypedArray.recycle();
      long l = ThreadedRenderer.access$000();
      mRootNode = RenderNode.adopt(l);
      mRootNode.setClipToBounds(false);
      mNativeProxy = ThreadedRenderer.nCreateProxy(true, l);
      ThreadedRenderer.nSetName(mNativeProxy, paramString);
      ThreadedRenderer.ProcessInitializer.sInstance.init(paramContext, mNativeProxy);
      ThreadedRenderer.nLoadSystemProperties(mNativeProxy);
      ThreadedRenderer.nSetup(mNativeProxy, f, i, j);
      mSurface = paramSurface;
      ThreadedRenderer.nUpdateSurface(mNativeProxy, paramSurface);
    }
    
    public void destroy()
    {
      mSurface = null;
      ThreadedRenderer.nDestroy(mNativeProxy, mRootNode.mNativeRenderNode);
    }
    
    public void draw(ThreadedRenderer.FrameDrawingCallback paramFrameDrawingCallback)
    {
      long l = AnimationUtils.currentAnimationTimeMillis() * 1000000L;
      mFrameInfo.setVsync(l, l);
      mFrameInfo.addFlags(4L);
      if (paramFrameDrawingCallback != null) {
        ThreadedRenderer.nSetFrameCallback(mNativeProxy, paramFrameDrawingCallback);
      }
      ThreadedRenderer.nSyncAndDrawFrame(mNativeProxy, mFrameInfo.mFrameInfo, mFrameInfo.mFrameInfo.length);
    }
    
    protected void finalize()
      throws Throwable
    {
      try
      {
        ThreadedRenderer.nDeleteProxy(mNativeProxy);
        mNativeProxy = 0L;
        return;
      }
      finally
      {
        super.finalize();
      }
    }
    
    public RenderNode getRootNode()
    {
      return mRootNode;
    }
    
    public void setLightCenter(Display paramDisplay, int paramInt1, int paramInt2)
    {
      Point localPoint = new Point();
      paramDisplay.getRealSize(localPoint);
      float f1 = x / 2.0F;
      float f2 = paramInt1;
      float f3 = mLightY;
      float f4 = paramInt2;
      ThreadedRenderer.nSetLightCenter(mNativeProxy, f1 - f2, f3 - f4, mLightZ);
    }
  }
}
