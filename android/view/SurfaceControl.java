package android.view;

import android.graphics.Bitmap;
import android.graphics.GraphicBuffer;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Region;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.Process;
import android.os.UserHandle;
import android.util.ArrayMap;
import android.util.Log;
import android.util.proto.ProtoOutputStream;
import com.android.internal.annotations.GuardedBy;
import dalvik.system.CloseGuard;
import java.io.Closeable;
import libcore.util.NativeAllocationRegistry;

public class SurfaceControl
  implements Parcelable
{
  public static final int BUILT_IN_DISPLAY_ID_EXT_MAX = 7;
  public static final int BUILT_IN_DISPLAY_ID_EXT_MIN = 5;
  public static final int BUILT_IN_DISPLAY_ID_HDMI = 1;
  public static final int BUILT_IN_DISPLAY_ID_MAIN = 0;
  public static final Parcelable.Creator<SurfaceControl> CREATOR = new Parcelable.Creator()
  {
    public SurfaceControl createFromParcel(Parcel paramAnonymousParcel)
    {
      return new SurfaceControl(paramAnonymousParcel, null);
    }
    
    public SurfaceControl[] newArray(int paramAnonymousInt)
    {
      return new SurfaceControl[paramAnonymousInt];
    }
  };
  public static final int CURSOR_WINDOW = 8192;
  public static final int FX_SURFACE_DIM = 131072;
  public static final int FX_SURFACE_MASK = 983040;
  public static final int FX_SURFACE_NORMAL = 0;
  public static final int HIDDEN = 4;
  public static final int NON_PREMULTIPLIED = 256;
  public static final int OPAQUE = 1024;
  public static final int POWER_MODE_DOZE = 1;
  public static final int POWER_MODE_DOZE_SUSPEND = 3;
  public static final int POWER_MODE_NORMAL = 2;
  public static final int POWER_MODE_OFF = 0;
  public static final int POWER_MODE_ON_SUSPEND = 4;
  public static final int PROTECTED_APP = 2048;
  public static final int SECURE = 128;
  private static final int SURFACE_HIDDEN = 1;
  private static final int SURFACE_OPAQUE = 2;
  private static final String TAG = "SurfaceControl";
  public static final int WINDOW_TYPE_DONT_SCREENSHOT = 441731;
  static Transaction sGlobalTransaction;
  static long sTransactionNestCount = 0L;
  private final CloseGuard mCloseGuard = CloseGuard.get();
  @GuardedBy("mSizeLock")
  private int mHeight;
  private final String mName;
  long mNativeObject;
  private final Object mSizeLock = new Object();
  @GuardedBy("mSizeLock")
  private int mWidth;
  
  private SurfaceControl(Parcel paramParcel)
  {
    mName = paramParcel.readString();
    mWidth = paramParcel.readInt();
    mHeight = paramParcel.readInt();
    mNativeObject = nativeReadFromParcel(paramParcel);
    if (mNativeObject != 0L)
    {
      mCloseGuard.open("release");
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Couldn't read SurfaceControl from parcel=");
    localStringBuilder.append(paramParcel);
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  public SurfaceControl(SurfaceControl paramSurfaceControl)
  {
    mName = mName;
    mWidth = mWidth;
    mHeight = mHeight;
    mNativeObject = mNativeObject;
    mCloseGuard.close();
    mNativeObject = 0L;
    mCloseGuard.open("release");
  }
  
  private SurfaceControl(SurfaceSession paramSurfaceSession, String paramString, int paramInt1, int paramInt2, int paramInt3, int paramInt4, SurfaceControl paramSurfaceControl, int paramInt5, int paramInt6)
    throws Surface.OutOfResourcesException, IllegalArgumentException
  {
    if (paramSurfaceSession != null)
    {
      if (paramString != null)
      {
        if ((paramInt4 & 0x4) == 0)
        {
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("Surfaces should always be created with the HIDDEN flag set to ensure that they are not made visible prematurely before all of the surface's properties have been configured.  Set the other properties and make the surface visible within a transaction.  New surface name: ");
          localStringBuilder.append(paramString);
          Log.w("SurfaceControl", localStringBuilder.toString(), new Throwable());
        }
        mName = paramString;
        mWidth = paramInt1;
        mHeight = paramInt2;
        long l;
        if (paramSurfaceControl != null) {
          l = mNativeObject;
        } else {
          l = 0L;
        }
        mNativeObject = nativeCreate(paramSurfaceSession, paramString, paramInt1, paramInt2, paramInt3, paramInt4, l, paramInt5, paramInt6);
        if (mNativeObject != 0L)
        {
          mCloseGuard.open("release");
          return;
        }
        throw new Surface.OutOfResourcesException("Couldn't allocate SurfaceControl native object");
      }
      throw new IllegalArgumentException("name must not be null");
    }
    throw new IllegalArgumentException("session must not be null");
  }
  
  public static GraphicBuffer captureLayers(IBinder paramIBinder, Rect paramRect, float paramFloat)
  {
    return nativeCaptureLayers(paramIBinder, paramRect, paramFloat);
  }
  
  private void checkNotReleased()
  {
    if (mNativeObject != 0L) {
      return;
    }
    throw new NullPointerException("mNativeObject is null. Have you called release() already?");
  }
  
  public static boolean clearAnimationFrameStats()
  {
    return nativeClearAnimationFrameStats();
  }
  
  public static void closeTransaction()
  {
    closeTransaction(false);
  }
  
  private static void closeTransaction(boolean paramBoolean)
  {
    try
    {
      if (sTransactionNestCount == 0L)
      {
        Log.e("SurfaceControl", "Call to SurfaceControl.closeTransaction without matching openTransaction");
      }
      else
      {
        long l = sTransactionNestCount - 1L;
        sTransactionNestCount = l;
        if (l > 0L) {
          return;
        }
      }
      sGlobalTransaction.apply(paramBoolean);
      return;
    }
    finally {}
  }
  
  public static void closeTransactionSync()
  {
    closeTransaction(true);
  }
  
  public static IBinder createDisplay(String paramString, boolean paramBoolean)
  {
    if (paramString != null) {
      return nativeCreateDisplay(paramString, paramBoolean);
    }
    throw new IllegalArgumentException("name must not be null");
  }
  
  public static void destroyDisplay(IBinder paramIBinder)
  {
    if (paramIBinder != null)
    {
      nativeDestroyDisplay(paramIBinder);
      return;
    }
    throw new IllegalArgumentException("displayToken must not be null");
  }
  
  public static int getActiveColorMode(IBinder paramIBinder)
  {
    if (paramIBinder != null) {
      return nativeGetActiveColorMode(paramIBinder);
    }
    throw new IllegalArgumentException("displayToken must not be null");
  }
  
  public static int getActiveConfig(IBinder paramIBinder)
  {
    if (paramIBinder != null) {
      return nativeGetActiveConfig(paramIBinder);
    }
    throw new IllegalArgumentException("displayToken must not be null");
  }
  
  public static boolean getAnimationFrameStats(WindowAnimationFrameStats paramWindowAnimationFrameStats)
  {
    return nativeGetAnimationFrameStats(paramWindowAnimationFrameStats);
  }
  
  public static IBinder getBuiltInDisplay(int paramInt)
  {
    return nativeGetBuiltInDisplay(paramInt);
  }
  
  public static int[] getDisplayColorModes(IBinder paramIBinder)
  {
    if (paramIBinder != null) {
      return nativeGetDisplayColorModes(paramIBinder);
    }
    throw new IllegalArgumentException("displayToken must not be null");
  }
  
  public static PhysicalDisplayInfo[] getDisplayConfigs(IBinder paramIBinder)
  {
    if (paramIBinder != null) {
      return nativeGetDisplayConfigs(paramIBinder);
    }
    throw new IllegalArgumentException("displayToken must not be null");
  }
  
  public static Display.HdrCapabilities getHdrCapabilities(IBinder paramIBinder)
  {
    if (paramIBinder != null) {
      return nativeGetHdrCapabilities(paramIBinder);
    }
    throw new IllegalArgumentException("displayToken must not be null");
  }
  
  @Deprecated
  public static void mergeToGlobalTransaction(Transaction paramTransaction)
  {
    try
    {
      sGlobalTransaction.merge(paramTransaction);
      return;
    }
    finally {}
  }
  
  private static native void nativeApplyTransaction(long paramLong, boolean paramBoolean);
  
  private static native GraphicBuffer nativeCaptureLayers(IBinder paramIBinder, Rect paramRect, float paramFloat);
  
  private static native boolean nativeClearAnimationFrameStats();
  
  private static native boolean nativeClearContentFrameStats(long paramLong);
  
  private static native long nativeCreate(SurfaceSession paramSurfaceSession, String paramString, int paramInt1, int paramInt2, int paramInt3, int paramInt4, long paramLong, int paramInt5, int paramInt6)
    throws Surface.OutOfResourcesException;
  
  private static native IBinder nativeCreateDisplay(String paramString, boolean paramBoolean);
  
  private static native long nativeCreateTransaction();
  
  private static native void nativeDeferTransactionUntil(long paramLong1, long paramLong2, IBinder paramIBinder, long paramLong3);
  
  private static native void nativeDeferTransactionUntilSurface(long paramLong1, long paramLong2, long paramLong3, long paramLong4);
  
  private static native void nativeDestroy(long paramLong);
  
  private static native void nativeDestroy(long paramLong1, long paramLong2);
  
  private static native void nativeDestroyDisplay(IBinder paramIBinder);
  
  private static native void nativeDisconnect(long paramLong);
  
  private static native int nativeGetActiveColorMode(IBinder paramIBinder);
  
  private static native int nativeGetActiveConfig(IBinder paramIBinder);
  
  private static native boolean nativeGetAnimationFrameStats(WindowAnimationFrameStats paramWindowAnimationFrameStats);
  
  private static native IBinder nativeGetBuiltInDisplay(int paramInt);
  
  private static native boolean nativeGetContentFrameStats(long paramLong, WindowContentFrameStats paramWindowContentFrameStats);
  
  private static native int[] nativeGetDisplayColorModes(IBinder paramIBinder);
  
  private static native PhysicalDisplayInfo[] nativeGetDisplayConfigs(IBinder paramIBinder);
  
  private static native IBinder nativeGetHandle(long paramLong);
  
  private static native Display.HdrCapabilities nativeGetHdrCapabilities(IBinder paramIBinder);
  
  private static native long nativeGetNativeTransactionFinalizer();
  
  private static native boolean nativeGetTransformToDisplayInverse(long paramLong);
  
  private static native void nativeMergeTransaction(long paramLong1, long paramLong2);
  
  private static native long nativeReadFromParcel(Parcel paramParcel);
  
  private static native void nativeRelease(long paramLong);
  
  private static native void nativeReparent(long paramLong1, long paramLong2, IBinder paramIBinder);
  
  private static native void nativeReparentChildren(long paramLong1, long paramLong2, IBinder paramIBinder);
  
  private static native Bitmap nativeScreenshot(IBinder paramIBinder, Rect paramRect, int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean1, boolean paramBoolean2, int paramInt5);
  
  private static native void nativeScreenshot(IBinder paramIBinder, Surface paramSurface, Rect paramRect, int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean1, boolean paramBoolean2);
  
  private static native GraphicBuffer nativeScreenshotToBuffer(IBinder paramIBinder, Rect paramRect, int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean1, boolean paramBoolean2, int paramInt5);
  
  private static native boolean nativeSetActiveColorMode(IBinder paramIBinder, int paramInt);
  
  private static native boolean nativeSetActiveConfig(IBinder paramIBinder, int paramInt);
  
  private static native void nativeSetAlpha(long paramLong1, long paramLong2, float paramFloat);
  
  private static native void nativeSetAnimationTransaction(long paramLong);
  
  private static native void nativeSetColor(long paramLong1, long paramLong2, float[] paramArrayOfFloat);
  
  private static native void nativeSetDisplayLayerStack(long paramLong, IBinder paramIBinder, int paramInt);
  
  private static native void nativeSetDisplayPowerMode(IBinder paramIBinder, int paramInt);
  
  private static native void nativeSetDisplayProjection(long paramLong, IBinder paramIBinder, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9);
  
  private static native void nativeSetDisplaySize(long paramLong, IBinder paramIBinder, int paramInt1, int paramInt2);
  
  private static native void nativeSetDisplaySurface(long paramLong1, IBinder paramIBinder, long paramLong2);
  
  private static native void nativeSetEarlyWakeup(long paramLong);
  
  private static native void nativeSetFinalCrop(long paramLong1, long paramLong2, int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  
  private static native void nativeSetFlags(long paramLong1, long paramLong2, int paramInt1, int paramInt2);
  
  private static native void nativeSetGeometryAppliesWithResize(long paramLong1, long paramLong2);
  
  private static native void nativeSetLayer(long paramLong1, long paramLong2, int paramInt);
  
  private static native void nativeSetLayerStack(long paramLong1, long paramLong2, int paramInt);
  
  private static native void nativeSetMatrix(long paramLong1, long paramLong2, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4);
  
  private static native void nativeSetOverrideScalingMode(long paramLong1, long paramLong2, int paramInt);
  
  private static native void nativeSetPosition(long paramLong1, long paramLong2, float paramFloat1, float paramFloat2);
  
  private static native void nativeSetRelativeLayer(long paramLong1, long paramLong2, IBinder paramIBinder, int paramInt);
  
  private static native void nativeSetSize(long paramLong1, long paramLong2, int paramInt1, int paramInt2);
  
  private static native void nativeSetTransparentRegionHint(long paramLong1, long paramLong2, Region paramRegion);
  
  private static native void nativeSetWindowCrop(long paramLong1, long paramLong2, int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  
  private static native void nativeSeverChildren(long paramLong1, long paramLong2);
  
  private static native void nativeWriteToParcel(long paramLong, Parcel paramParcel);
  
  /* Error */
  public static void openTransaction()
  {
    // Byte code:
    //   0: ldc 2
    //   2: monitorenter
    //   3: getstatic 358	android/view/SurfaceControl:sGlobalTransaction	Landroid/view/SurfaceControl$Transaction;
    //   6: ifnonnull +15 -> 21
    //   9: new 16	android/view/SurfaceControl$Transaction
    //   12: astore_0
    //   13: aload_0
    //   14: invokespecial 439	android/view/SurfaceControl$Transaction:<init>	()V
    //   17: aload_0
    //   18: putstatic 358	android/view/SurfaceControl:sGlobalTransaction	Landroid/view/SurfaceControl$Transaction;
    //   21: ldc 2
    //   23: monitorenter
    //   24: getstatic 79	android/view/SurfaceControl:sTransactionNestCount	J
    //   27: lconst_1
    //   28: ladd
    //   29: putstatic 79	android/view/SurfaceControl:sTransactionNestCount	J
    //   32: ldc 2
    //   34: monitorexit
    //   35: ldc 2
    //   37: monitorexit
    //   38: return
    //   39: astore_0
    //   40: ldc 2
    //   42: monitorexit
    //   43: aload_0
    //   44: athrow
    //   45: astore_0
    //   46: ldc 2
    //   48: monitorexit
    //   49: aload_0
    //   50: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   12	6	0	localTransaction	Transaction
    //   39	5	0	localObject1	Object
    //   45	5	0	localObject2	Object
    // Exception table:
    //   from	to	target	type
    //   24	35	39	finally
    //   40	43	39	finally
    //   3	21	45	finally
    //   21	24	45	finally
    //   35	38	45	finally
    //   43	45	45	finally
    //   46	49	45	finally
  }
  
  private static void rotateCropForSF(Rect paramRect, int paramInt)
  {
    if ((paramInt == 1) || (paramInt == 3))
    {
      paramInt = top;
      top = left;
      left = paramInt;
      paramInt = right;
      right = bottom;
      bottom = paramInt;
    }
  }
  
  public static Bitmap screenshot(Rect paramRect, int paramInt1, int paramInt2, int paramInt3)
  {
    IBinder localIBinder = getBuiltInDisplay(0);
    int i = 3;
    int j;
    if (paramInt3 != 1)
    {
      j = paramInt3;
      if (paramInt3 != 3) {}
    }
    else
    {
      if (paramInt3 == 1) {
        paramInt3 = i;
      } else {
        paramInt3 = 1;
      }
      j = paramInt3;
    }
    rotateCropForSF(paramRect, j);
    return nativeScreenshot(localIBinder, paramRect, paramInt1, paramInt2, 0, 0, true, false, j);
  }
  
  public static Bitmap screenshot(Rect paramRect, int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean, int paramInt5)
  {
    return nativeScreenshot(getBuiltInDisplay(0), paramRect, paramInt1, paramInt2, paramInt3, paramInt4, false, paramBoolean, paramInt5);
  }
  
  public static void screenshot(IBinder paramIBinder, Surface paramSurface)
  {
    screenshot(paramIBinder, paramSurface, new Rect(), 0, 0, 0, 0, true, false);
  }
  
  public static void screenshot(IBinder paramIBinder, Surface paramSurface, int paramInt1, int paramInt2)
  {
    screenshot(paramIBinder, paramSurface, new Rect(), paramInt1, paramInt2, 0, 0, true, false);
  }
  
  public static void screenshot(IBinder paramIBinder, Surface paramSurface, int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean)
  {
    screenshot(paramIBinder, paramSurface, new Rect(), paramInt1, paramInt2, paramInt3, paramInt4, false, paramBoolean);
  }
  
  private static void screenshot(IBinder paramIBinder, Surface paramSurface, Rect paramRect, int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean1, boolean paramBoolean2)
  {
    if (paramIBinder != null)
    {
      if (paramSurface != null)
      {
        nativeScreenshot(paramIBinder, paramSurface, paramRect, paramInt1, paramInt2, paramInt3, paramInt4, paramBoolean1, paramBoolean2);
        return;
      }
      throw new IllegalArgumentException("consumer must not be null");
    }
    throw new IllegalArgumentException("displayToken must not be null");
  }
  
  public static GraphicBuffer screenshotToBuffer(Rect paramRect, int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean, int paramInt5)
  {
    return nativeScreenshotToBuffer(getBuiltInDisplay(0), paramRect, paramInt1, paramInt2, paramInt3, paramInt4, false, paramBoolean, paramInt5);
  }
  
  public static boolean setActiveColorMode(IBinder paramIBinder, int paramInt)
  {
    if (paramIBinder != null) {
      return nativeSetActiveColorMode(paramIBinder, paramInt);
    }
    throw new IllegalArgumentException("displayToken must not be null");
  }
  
  public static boolean setActiveConfig(IBinder paramIBinder, int paramInt)
  {
    if (paramIBinder != null) {
      return nativeSetActiveConfig(paramIBinder, paramInt);
    }
    throw new IllegalArgumentException("displayToken must not be null");
  }
  
  public static void setAnimationTransaction()
  {
    try
    {
      sGlobalTransaction.setAnimationTransaction();
      return;
    }
    finally {}
  }
  
  public static void setDisplayLayerStack(IBinder paramIBinder, int paramInt)
  {
    try
    {
      sGlobalTransaction.setDisplayLayerStack(paramIBinder, paramInt);
      return;
    }
    finally {}
  }
  
  public static void setDisplayPowerMode(IBinder paramIBinder, int paramInt)
  {
    if (paramIBinder != null)
    {
      nativeSetDisplayPowerMode(paramIBinder, paramInt);
      return;
    }
    throw new IllegalArgumentException("displayToken must not be null");
  }
  
  public static void setDisplayProjection(IBinder paramIBinder, int paramInt, Rect paramRect1, Rect paramRect2)
  {
    try
    {
      sGlobalTransaction.setDisplayProjection(paramIBinder, paramInt, paramRect1, paramRect2);
      return;
    }
    finally {}
  }
  
  public static void setDisplaySize(IBinder paramIBinder, int paramInt1, int paramInt2)
  {
    try
    {
      sGlobalTransaction.setDisplaySize(paramIBinder, paramInt1, paramInt2);
      return;
    }
    finally {}
  }
  
  public static void setDisplaySurface(IBinder paramIBinder, Surface paramSurface)
  {
    try
    {
      sGlobalTransaction.setDisplaySurface(paramIBinder, paramSurface);
      return;
    }
    finally {}
  }
  
  public boolean clearContentFrameStats()
  {
    checkNotReleased();
    return nativeClearContentFrameStats(mNativeObject);
  }
  
  public void deferTransactionUntil(IBinder paramIBinder, long paramLong)
  {
    try
    {
      sGlobalTransaction.deferTransactionUntil(this, paramIBinder, paramLong);
      return;
    }
    finally {}
  }
  
  public void deferTransactionUntil(Surface paramSurface, long paramLong)
  {
    try
    {
      sGlobalTransaction.deferTransactionUntilSurface(this, paramSurface, paramLong);
      return;
    }
    finally {}
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void destroy()
  {
    if (mNativeObject != 0L)
    {
      nativeDestroy(mNativeObject);
      mNativeObject = 0L;
    }
    mCloseGuard.close();
  }
  
  public void detachChildren()
  {
    try
    {
      sGlobalTransaction.detachChildren(this);
      return;
    }
    finally {}
  }
  
  public void disconnect()
  {
    if (mNativeObject != 0L) {
      nativeDisconnect(mNativeObject);
    }
  }
  
  protected void finalize()
    throws Throwable
  {
    try
    {
      if (mCloseGuard != null) {
        mCloseGuard.warnIfOpen();
      }
      if (mNativeObject != 0L) {
        nativeRelease(mNativeObject);
      }
      return;
    }
    finally
    {
      super.finalize();
    }
  }
  
  public boolean getContentFrameStats(WindowContentFrameStats paramWindowContentFrameStats)
  {
    checkNotReleased();
    return nativeGetContentFrameStats(mNativeObject, paramWindowContentFrameStats);
  }
  
  public IBinder getHandle()
  {
    return nativeGetHandle(mNativeObject);
  }
  
  public int getHeight()
  {
    synchronized (mSizeLock)
    {
      int i = mHeight;
      return i;
    }
  }
  
  public int getWidth()
  {
    synchronized (mSizeLock)
    {
      int i = mWidth;
      return i;
    }
  }
  
  public void hide()
  {
    checkNotReleased();
    try
    {
      sGlobalTransaction.hide(this);
      return;
    }
    finally {}
  }
  
  public void release()
  {
    if (mNativeObject != 0L)
    {
      nativeRelease(mNativeObject);
      mNativeObject = 0L;
    }
    mCloseGuard.close();
  }
  
  public void reparent(IBinder paramIBinder)
  {
    try
    {
      sGlobalTransaction.reparent(this, paramIBinder);
      return;
    }
    finally {}
  }
  
  public void reparentChildren(IBinder paramIBinder)
  {
    try
    {
      sGlobalTransaction.reparentChildren(this, paramIBinder);
      return;
    }
    finally {}
  }
  
  public void setAlpha(float paramFloat)
  {
    checkNotReleased();
    try
    {
      sGlobalTransaction.setAlpha(this, paramFloat);
      return;
    }
    finally {}
  }
  
  public void setColor(float[] paramArrayOfFloat)
  {
    checkNotReleased();
    try
    {
      sGlobalTransaction.setColor(this, paramArrayOfFloat);
      return;
    }
    finally {}
  }
  
  public void setFinalCrop(Rect paramRect)
  {
    checkNotReleased();
    try
    {
      sGlobalTransaction.setFinalCrop(this, paramRect);
      return;
    }
    finally {}
  }
  
  public void setGeometryAppliesWithResize()
  {
    checkNotReleased();
    try
    {
      sGlobalTransaction.setGeometryAppliesWithResize(this);
      return;
    }
    finally {}
  }
  
  public void setLayer(int paramInt)
  {
    checkNotReleased();
    try
    {
      sGlobalTransaction.setLayer(this, paramInt);
      return;
    }
    finally {}
  }
  
  public void setLayerStack(int paramInt)
  {
    checkNotReleased();
    try
    {
      sGlobalTransaction.setLayerStack(this, paramInt);
      return;
    }
    finally {}
  }
  
  public void setMatrix(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    checkNotReleased();
    try
    {
      sGlobalTransaction.setMatrix(this, paramFloat1, paramFloat2, paramFloat3, paramFloat4);
      return;
    }
    finally {}
  }
  
  public void setMatrix(Matrix paramMatrix, float[] paramArrayOfFloat)
  {
    checkNotReleased();
    paramMatrix.getValues(paramArrayOfFloat);
    try
    {
      sGlobalTransaction.setMatrix(this, paramArrayOfFloat[0], paramArrayOfFloat[3], paramArrayOfFloat[1], paramArrayOfFloat[4]);
      sGlobalTransaction.setPosition(this, paramArrayOfFloat[2], paramArrayOfFloat[5]);
      return;
    }
    finally {}
  }
  
  public void setOpaque(boolean paramBoolean)
  {
    checkNotReleased();
    try
    {
      sGlobalTransaction.setOpaque(this, paramBoolean);
      return;
    }
    finally {}
  }
  
  public void setOverrideScalingMode(int paramInt)
  {
    checkNotReleased();
    try
    {
      sGlobalTransaction.setOverrideScalingMode(this, paramInt);
      return;
    }
    finally {}
  }
  
  public void setPosition(float paramFloat1, float paramFloat2)
  {
    checkNotReleased();
    try
    {
      sGlobalTransaction.setPosition(this, paramFloat1, paramFloat2);
      return;
    }
    finally {}
  }
  
  public void setRelativeLayer(SurfaceControl paramSurfaceControl, int paramInt)
  {
    checkNotReleased();
    try
    {
      sGlobalTransaction.setRelativeLayer(this, paramSurfaceControl, paramInt);
      return;
    }
    finally {}
  }
  
  public void setSecure(boolean paramBoolean)
  {
    checkNotReleased();
    try
    {
      sGlobalTransaction.setSecure(this, paramBoolean);
      return;
    }
    finally {}
  }
  
  public void setSize(int paramInt1, int paramInt2)
  {
    checkNotReleased();
    try
    {
      sGlobalTransaction.setSize(this, paramInt1, paramInt2);
      return;
    }
    finally {}
  }
  
  public void setTransparentRegionHint(Region paramRegion)
  {
    checkNotReleased();
    try
    {
      sGlobalTransaction.setTransparentRegionHint(this, paramRegion);
      return;
    }
    finally {}
  }
  
  public void setWindowCrop(Rect paramRect)
  {
    checkNotReleased();
    try
    {
      sGlobalTransaction.setWindowCrop(this, paramRect);
      return;
    }
    finally {}
  }
  
  public void show()
  {
    checkNotReleased();
    try
    {
      sGlobalTransaction.show(this);
      return;
    }
    finally {}
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Surface(name=");
    localStringBuilder.append(mName);
    localStringBuilder.append(")/@0x");
    localStringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(mName);
    paramParcel.writeInt(mWidth);
    paramParcel.writeInt(mHeight);
    nativeWriteToParcel(mNativeObject, paramParcel);
  }
  
  public void writeToProto(ProtoOutputStream paramProtoOutputStream, long paramLong)
  {
    paramLong = paramProtoOutputStream.start(paramLong);
    paramProtoOutputStream.write(1120986464257L, System.identityHashCode(this));
    paramProtoOutputStream.write(1138166333442L, mName);
    paramProtoOutputStream.end(paramLong);
  }
  
  public static class Builder
  {
    private int mFlags = 4;
    private int mFormat = -1;
    private int mHeight;
    private String mName;
    private int mOwnerUid = -1;
    private SurfaceControl mParent;
    private SurfaceSession mSession;
    private int mWidth;
    private int mWindowType = -1;
    
    public Builder(SurfaceSession paramSurfaceSession)
    {
      mSession = paramSurfaceSession;
    }
    
    public SurfaceControl build()
    {
      if ((mWidth > 0) && (mHeight > 0)) {
        return new SurfaceControl(mSession, mName, mWidth, mHeight, mFormat, mFlags, mParent, mWindowType, mOwnerUid, null);
      }
      throw new IllegalArgumentException("width and height must be set");
    }
    
    public Builder setColorLayer(boolean paramBoolean)
    {
      if (paramBoolean) {
        mFlags |= 0x20000;
      } else {
        mFlags &= 0xFFFDFFFF;
      }
      return this;
    }
    
    public Builder setFlags(int paramInt)
    {
      mFlags = paramInt;
      return this;
    }
    
    public Builder setFormat(int paramInt)
    {
      mFormat = paramInt;
      return this;
    }
    
    public Builder setMetadata(int paramInt1, int paramInt2)
    {
      if (UserHandle.getAppId(Process.myUid()) == 1000)
      {
        mWindowType = paramInt1;
        mOwnerUid = paramInt2;
        return this;
      }
      throw new UnsupportedOperationException("It only makes sense to set Surface metadata from the WindowManager");
    }
    
    public Builder setName(String paramString)
    {
      mName = paramString;
      return this;
    }
    
    public Builder setOpaque(boolean paramBoolean)
    {
      if (paramBoolean) {
        mFlags |= 0x400;
      } else {
        mFlags &= 0xFBFF;
      }
      return this;
    }
    
    public Builder setParent(SurfaceControl paramSurfaceControl)
    {
      mParent = paramSurfaceControl;
      return this;
    }
    
    public Builder setProtected(boolean paramBoolean)
    {
      if (paramBoolean) {
        mFlags |= 0x800;
      } else {
        mFlags &= 0xF7FF;
      }
      return this;
    }
    
    public Builder setSecure(boolean paramBoolean)
    {
      if (paramBoolean) {
        mFlags |= 0x80;
      } else {
        mFlags &= 0xFF7F;
      }
      return this;
    }
    
    public Builder setSize(int paramInt1, int paramInt2)
    {
      if ((paramInt1 > 0) && (paramInt2 > 0))
      {
        mWidth = paramInt1;
        mHeight = paramInt2;
        return this;
      }
      throw new IllegalArgumentException("width and height must be positive");
    }
  }
  
  public static final class PhysicalDisplayInfo
  {
    public long appVsyncOffsetNanos;
    public float density;
    public int height;
    public long presentationDeadlineNanos;
    public float refreshRate;
    public boolean secure;
    public int width;
    public float xDpi;
    public float yDpi;
    
    public PhysicalDisplayInfo() {}
    
    public PhysicalDisplayInfo(PhysicalDisplayInfo paramPhysicalDisplayInfo)
    {
      copyFrom(paramPhysicalDisplayInfo);
    }
    
    public void copyFrom(PhysicalDisplayInfo paramPhysicalDisplayInfo)
    {
      width = width;
      height = height;
      refreshRate = refreshRate;
      density = density;
      xDpi = xDpi;
      yDpi = yDpi;
      secure = secure;
      appVsyncOffsetNanos = appVsyncOffsetNanos;
      presentationDeadlineNanos = presentationDeadlineNanos;
    }
    
    public boolean equals(PhysicalDisplayInfo paramPhysicalDisplayInfo)
    {
      boolean bool;
      if ((paramPhysicalDisplayInfo != null) && (width == width) && (height == height) && (refreshRate == refreshRate) && (density == density) && (xDpi == xDpi) && (yDpi == yDpi) && (secure == secure) && (appVsyncOffsetNanos == appVsyncOffsetNanos) && (presentationDeadlineNanos == presentationDeadlineNanos)) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public boolean equals(Object paramObject)
    {
      boolean bool;
      if (((paramObject instanceof PhysicalDisplayInfo)) && (equals((PhysicalDisplayInfo)paramObject))) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public int hashCode()
    {
      return 0;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("PhysicalDisplayInfo{");
      localStringBuilder.append(width);
      localStringBuilder.append(" x ");
      localStringBuilder.append(height);
      localStringBuilder.append(", ");
      localStringBuilder.append(refreshRate);
      localStringBuilder.append(" fps, density ");
      localStringBuilder.append(density);
      localStringBuilder.append(", ");
      localStringBuilder.append(xDpi);
      localStringBuilder.append(" x ");
      localStringBuilder.append(yDpi);
      localStringBuilder.append(" dpi, secure ");
      localStringBuilder.append(secure);
      localStringBuilder.append(", appVsyncOffset ");
      localStringBuilder.append(appVsyncOffsetNanos);
      localStringBuilder.append(", bufferDeadline ");
      localStringBuilder.append(presentationDeadlineNanos);
      localStringBuilder.append("}");
      return localStringBuilder.toString();
    }
  }
  
  public static class Transaction
    implements Closeable
  {
    public static final NativeAllocationRegistry sRegistry = new NativeAllocationRegistry(Transaction.class.getClassLoader(), SurfaceControl.access$200(), 512L);
    Runnable mFreeNativeResources = sRegistry.registerNativeAllocation(this, mNativeObject);
    private long mNativeObject = SurfaceControl.access$300();
    private final ArrayMap<SurfaceControl, Point> mResizedSurfaces = new ArrayMap();
    
    public Transaction() {}
    
    private void applyResizedSurfaces()
    {
      int i = mResizedSurfaces.size() - 1;
      while (i >= 0)
      {
        Point localPoint = (Point)mResizedSurfaces.valueAt(i);
        SurfaceControl localSurfaceControl = (SurfaceControl)mResizedSurfaces.keyAt(i);
        synchronized (mSizeLock)
        {
          SurfaceControl.access$602(localSurfaceControl, x);
          SurfaceControl.access$702(localSurfaceControl, y);
          i--;
        }
      }
      mResizedSurfaces.clear();
    }
    
    public void apply()
    {
      apply(false);
    }
    
    public void apply(boolean paramBoolean)
    {
      applyResizedSurfaces();
      SurfaceControl.nativeApplyTransaction(mNativeObject, paramBoolean);
    }
    
    public void close()
    {
      mFreeNativeResources.run();
      mNativeObject = 0L;
    }
    
    public Transaction deferTransactionUntil(SurfaceControl paramSurfaceControl, IBinder paramIBinder, long paramLong)
    {
      if (paramLong < 0L) {
        return this;
      }
      paramSurfaceControl.checkNotReleased();
      SurfaceControl.nativeDeferTransactionUntil(mNativeObject, mNativeObject, paramIBinder, paramLong);
      return this;
    }
    
    public Transaction deferTransactionUntilSurface(SurfaceControl paramSurfaceControl, Surface paramSurface, long paramLong)
    {
      if (paramLong < 0L) {
        return this;
      }
      paramSurfaceControl.checkNotReleased();
      SurfaceControl.nativeDeferTransactionUntilSurface(mNativeObject, mNativeObject, mNativeObject, paramLong);
      return this;
    }
    
    public Transaction destroy(SurfaceControl paramSurfaceControl)
    {
      paramSurfaceControl.checkNotReleased();
      mCloseGuard.close();
      SurfaceControl.nativeDestroy(mNativeObject, mNativeObject);
      return this;
    }
    
    public Transaction detachChildren(SurfaceControl paramSurfaceControl)
    {
      paramSurfaceControl.checkNotReleased();
      SurfaceControl.nativeSeverChildren(mNativeObject, mNativeObject);
      return this;
    }
    
    public Transaction hide(SurfaceControl paramSurfaceControl)
    {
      paramSurfaceControl.checkNotReleased();
      SurfaceControl.nativeSetFlags(mNativeObject, mNativeObject, 1, 1);
      return this;
    }
    
    public Transaction merge(Transaction paramTransaction)
    {
      mResizedSurfaces.putAll(mResizedSurfaces);
      mResizedSurfaces.clear();
      SurfaceControl.nativeMergeTransaction(mNativeObject, mNativeObject);
      return this;
    }
    
    public Transaction reparent(SurfaceControl paramSurfaceControl, IBinder paramIBinder)
    {
      paramSurfaceControl.checkNotReleased();
      SurfaceControl.nativeReparent(mNativeObject, mNativeObject, paramIBinder);
      return this;
    }
    
    public Transaction reparentChildren(SurfaceControl paramSurfaceControl, IBinder paramIBinder)
    {
      paramSurfaceControl.checkNotReleased();
      SurfaceControl.nativeReparentChildren(mNativeObject, mNativeObject, paramIBinder);
      return this;
    }
    
    public Transaction setAlpha(SurfaceControl paramSurfaceControl, float paramFloat)
    {
      paramSurfaceControl.checkNotReleased();
      SurfaceControl.nativeSetAlpha(mNativeObject, mNativeObject, paramFloat);
      return this;
    }
    
    public Transaction setAnimationTransaction()
    {
      SurfaceControl.nativeSetAnimationTransaction(mNativeObject);
      return this;
    }
    
    public Transaction setColor(SurfaceControl paramSurfaceControl, float[] paramArrayOfFloat)
    {
      paramSurfaceControl.checkNotReleased();
      SurfaceControl.nativeSetColor(mNativeObject, mNativeObject, paramArrayOfFloat);
      return this;
    }
    
    public Transaction setDisplayLayerStack(IBinder paramIBinder, int paramInt)
    {
      if (paramIBinder != null)
      {
        SurfaceControl.nativeSetDisplayLayerStack(mNativeObject, paramIBinder, paramInt);
        return this;
      }
      throw new IllegalArgumentException("displayToken must not be null");
    }
    
    public Transaction setDisplayProjection(IBinder paramIBinder, int paramInt, Rect paramRect1, Rect paramRect2)
    {
      if (paramIBinder != null)
      {
        if (paramRect1 != null)
        {
          if (paramRect2 != null)
          {
            SurfaceControl.nativeSetDisplayProjection(mNativeObject, paramIBinder, paramInt, left, top, right, bottom, left, top, right, bottom);
            return this;
          }
          throw new IllegalArgumentException("displayRect must not be null");
        }
        throw new IllegalArgumentException("layerStackRect must not be null");
      }
      throw new IllegalArgumentException("displayToken must not be null");
    }
    
    public Transaction setDisplaySize(IBinder paramIBinder, int paramInt1, int paramInt2)
    {
      if (paramIBinder != null)
      {
        if ((paramInt1 > 0) && (paramInt2 > 0))
        {
          SurfaceControl.nativeSetDisplaySize(mNativeObject, paramIBinder, paramInt1, paramInt2);
          return this;
        }
        throw new IllegalArgumentException("width and height must be positive");
      }
      throw new IllegalArgumentException("displayToken must not be null");
    }
    
    public Transaction setDisplaySurface(IBinder paramIBinder, Surface paramSurface)
    {
      if (paramIBinder != null)
      {
        if (paramSurface != null) {
          synchronized (mLock)
          {
            SurfaceControl.nativeSetDisplaySurface(mNativeObject, paramIBinder, mNativeObject);
          }
        }
        SurfaceControl.nativeSetDisplaySurface(mNativeObject, paramIBinder, 0L);
        return this;
      }
      throw new IllegalArgumentException("displayToken must not be null");
    }
    
    public Transaction setEarlyWakeup()
    {
      SurfaceControl.nativeSetEarlyWakeup(mNativeObject);
      return this;
    }
    
    public Transaction setFinalCrop(SurfaceControl paramSurfaceControl, Rect paramRect)
    {
      paramSurfaceControl.checkNotReleased();
      if (paramRect != null) {
        SurfaceControl.nativeSetFinalCrop(mNativeObject, mNativeObject, left, top, right, bottom);
      } else {
        SurfaceControl.nativeSetFinalCrop(mNativeObject, mNativeObject, 0, 0, 0, 0);
      }
      return this;
    }
    
    public Transaction setGeometryAppliesWithResize(SurfaceControl paramSurfaceControl)
    {
      paramSurfaceControl.checkNotReleased();
      SurfaceControl.nativeSetGeometryAppliesWithResize(mNativeObject, mNativeObject);
      return this;
    }
    
    public Transaction setLayer(SurfaceControl paramSurfaceControl, int paramInt)
    {
      paramSurfaceControl.checkNotReleased();
      SurfaceControl.nativeSetLayer(mNativeObject, mNativeObject, paramInt);
      return this;
    }
    
    public Transaction setLayerStack(SurfaceControl paramSurfaceControl, int paramInt)
    {
      paramSurfaceControl.checkNotReleased();
      SurfaceControl.nativeSetLayerStack(mNativeObject, mNativeObject, paramInt);
      return this;
    }
    
    public Transaction setMatrix(SurfaceControl paramSurfaceControl, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
    {
      paramSurfaceControl.checkNotReleased();
      SurfaceControl.nativeSetMatrix(mNativeObject, mNativeObject, paramFloat1, paramFloat2, paramFloat3, paramFloat4);
      return this;
    }
    
    public Transaction setMatrix(SurfaceControl paramSurfaceControl, Matrix paramMatrix, float[] paramArrayOfFloat)
    {
      paramMatrix.getValues(paramArrayOfFloat);
      setMatrix(paramSurfaceControl, paramArrayOfFloat[0], paramArrayOfFloat[3], paramArrayOfFloat[1], paramArrayOfFloat[4]);
      setPosition(paramSurfaceControl, paramArrayOfFloat[2], paramArrayOfFloat[5]);
      return this;
    }
    
    public Transaction setOpaque(SurfaceControl paramSurfaceControl, boolean paramBoolean)
    {
      paramSurfaceControl.checkNotReleased();
      if (paramBoolean) {
        SurfaceControl.nativeSetFlags(mNativeObject, mNativeObject, 2, 2);
      } else {
        SurfaceControl.nativeSetFlags(mNativeObject, mNativeObject, 0, 2);
      }
      return this;
    }
    
    public Transaction setOverrideScalingMode(SurfaceControl paramSurfaceControl, int paramInt)
    {
      paramSurfaceControl.checkNotReleased();
      SurfaceControl.nativeSetOverrideScalingMode(mNativeObject, mNativeObject, paramInt);
      return this;
    }
    
    public Transaction setPosition(SurfaceControl paramSurfaceControl, float paramFloat1, float paramFloat2)
    {
      paramSurfaceControl.checkNotReleased();
      SurfaceControl.nativeSetPosition(mNativeObject, mNativeObject, paramFloat1, paramFloat2);
      return this;
    }
    
    public Transaction setRelativeLayer(SurfaceControl paramSurfaceControl1, SurfaceControl paramSurfaceControl2, int paramInt)
    {
      paramSurfaceControl1.checkNotReleased();
      SurfaceControl.nativeSetRelativeLayer(mNativeObject, mNativeObject, paramSurfaceControl2.getHandle(), paramInt);
      return this;
    }
    
    public Transaction setSecure(SurfaceControl paramSurfaceControl, boolean paramBoolean)
    {
      paramSurfaceControl.checkNotReleased();
      if (paramBoolean) {
        SurfaceControl.nativeSetFlags(mNativeObject, mNativeObject, 128, 128);
      } else {
        SurfaceControl.nativeSetFlags(mNativeObject, mNativeObject, 0, 128);
      }
      return this;
    }
    
    public Transaction setSize(SurfaceControl paramSurfaceControl, int paramInt1, int paramInt2)
    {
      paramSurfaceControl.checkNotReleased();
      mResizedSurfaces.put(paramSurfaceControl, new Point(paramInt1, paramInt2));
      SurfaceControl.nativeSetSize(mNativeObject, mNativeObject, paramInt1, paramInt2);
      return this;
    }
    
    public Transaction setTransparentRegionHint(SurfaceControl paramSurfaceControl, Region paramRegion)
    {
      paramSurfaceControl.checkNotReleased();
      SurfaceControl.nativeSetTransparentRegionHint(mNativeObject, mNativeObject, paramRegion);
      return this;
    }
    
    public Transaction setWindowCrop(SurfaceControl paramSurfaceControl, Rect paramRect)
    {
      paramSurfaceControl.checkNotReleased();
      if (paramRect != null) {
        SurfaceControl.nativeSetWindowCrop(mNativeObject, mNativeObject, left, top, right, bottom);
      } else {
        SurfaceControl.nativeSetWindowCrop(mNativeObject, mNativeObject, 0, 0, 0, 0);
      }
      return this;
    }
    
    public Transaction show(SurfaceControl paramSurfaceControl)
    {
      paramSurfaceControl.checkNotReleased();
      SurfaceControl.nativeSetFlags(mNativeObject, mNativeObject, 0, 1);
      return this;
    }
  }
}
