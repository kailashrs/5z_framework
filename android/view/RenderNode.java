package android.view;

import android.graphics.Matrix;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.AnimatedVectorDrawable.VectorDrawableAnimatorRT;
import dalvik.annotation.optimization.CriticalNative;
import dalvik.annotation.optimization.FastNative;
import libcore.util.NativeAllocationRegistry;

public class RenderNode
{
  final long mNativeRenderNode;
  private final View mOwningView;
  
  private RenderNode(long paramLong)
  {
    mNativeRenderNode = paramLong;
    NoImagePreloadHolder.sRegistry.registerNativeAllocation(this, mNativeRenderNode);
    mOwningView = null;
  }
  
  private RenderNode(String paramString, View paramView)
  {
    mNativeRenderNode = nCreate(paramString);
    NoImagePreloadHolder.sRegistry.registerNativeAllocation(this, mNativeRenderNode);
    mOwningView = paramView;
  }
  
  public static RenderNode adopt(long paramLong)
  {
    return new RenderNode(paramLong);
  }
  
  public static RenderNode create(String paramString, View paramView)
  {
    return new RenderNode(paramString, paramView);
  }
  
  private static native void nAddAnimator(long paramLong1, long paramLong2);
  
  private static native long nCreate(String paramString);
  
  private static native void nEndAllAnimators(long paramLong);
  
  @CriticalNative
  private static native float nGetAlpha(long paramLong);
  
  @CriticalNative
  private static native int nGetAmbientShadowColor(long paramLong);
  
  @CriticalNative
  private static native float nGetCameraDistance(long paramLong);
  
  @CriticalNative
  private static native boolean nGetClipToOutline(long paramLong);
  
  private static native int nGetDebugSize(long paramLong);
  
  @CriticalNative
  private static native float nGetElevation(long paramLong);
  
  @CriticalNative
  private static native void nGetInverseTransformMatrix(long paramLong1, long paramLong2);
  
  private static native long nGetNativeFinalizer();
  
  @CriticalNative
  private static native float nGetPivotX(long paramLong);
  
  @CriticalNative
  private static native float nGetPivotY(long paramLong);
  
  @CriticalNative
  private static native float nGetRotation(long paramLong);
  
  @CriticalNative
  private static native float nGetRotationX(long paramLong);
  
  @CriticalNative
  private static native float nGetRotationY(long paramLong);
  
  @CriticalNative
  private static native float nGetScaleX(long paramLong);
  
  @CriticalNative
  private static native float nGetScaleY(long paramLong);
  
  @CriticalNative
  private static native int nGetSpotShadowColor(long paramLong);
  
  @CriticalNative
  private static native void nGetTransformMatrix(long paramLong1, long paramLong2);
  
  @CriticalNative
  private static native float nGetTranslationX(long paramLong);
  
  @CriticalNative
  private static native float nGetTranslationY(long paramLong);
  
  @CriticalNative
  private static native float nGetTranslationZ(long paramLong);
  
  @CriticalNative
  private static native boolean nHasIdentityMatrix(long paramLong);
  
  @CriticalNative
  private static native boolean nHasOverlappingRendering(long paramLong);
  
  @CriticalNative
  private static native boolean nHasShadow(long paramLong);
  
  @CriticalNative
  private static native boolean nIsPivotExplicitlySet(long paramLong);
  
  @CriticalNative
  private static native boolean nIsValid(long paramLong);
  
  @CriticalNative
  private static native boolean nOffsetLeftAndRight(long paramLong, int paramInt);
  
  @CriticalNative
  private static native boolean nOffsetTopAndBottom(long paramLong, int paramInt);
  
  private static native void nOutput(long paramLong);
  
  private static native void nRequestPositionUpdates(long paramLong, SurfaceView paramSurfaceView);
  
  @CriticalNative
  private static native boolean nResetPivot(long paramLong);
  
  @CriticalNative
  private static native boolean nSetAlpha(long paramLong, float paramFloat);
  
  @CriticalNative
  private static native boolean nSetAmbientShadowColor(long paramLong, int paramInt);
  
  @CriticalNative
  private static native boolean nSetAnimationMatrix(long paramLong1, long paramLong2);
  
  @CriticalNative
  private static native boolean nSetBottom(long paramLong, int paramInt);
  
  @CriticalNative
  private static native boolean nSetCameraDistance(long paramLong, float paramFloat);
  
  @CriticalNative
  private static native boolean nSetClipBounds(long paramLong, int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  
  @CriticalNative
  private static native boolean nSetClipBoundsEmpty(long paramLong);
  
  @CriticalNative
  private static native boolean nSetClipToBounds(long paramLong, boolean paramBoolean);
  
  @CriticalNative
  private static native boolean nSetClipToOutline(long paramLong, boolean paramBoolean);
  
  @FastNative
  private static native void nSetDisplayList(long paramLong1, long paramLong2);
  
  @CriticalNative
  private static native boolean nSetElevation(long paramLong, float paramFloat);
  
  @CriticalNative
  private static native boolean nSetHasOverlappingRendering(long paramLong, boolean paramBoolean);
  
  @CriticalNative
  private static native boolean nSetLayerPaint(long paramLong1, long paramLong2);
  
  @CriticalNative
  private static native boolean nSetLayerType(long paramLong, int paramInt);
  
  @CriticalNative
  private static native boolean nSetLeft(long paramLong, int paramInt);
  
  @CriticalNative
  private static native boolean nSetLeftTopRightBottom(long paramLong, int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  
  @CriticalNative
  private static native boolean nSetOutlineConvexPath(long paramLong1, long paramLong2, float paramFloat);
  
  @CriticalNative
  private static native boolean nSetOutlineEmpty(long paramLong);
  
  @CriticalNative
  private static native boolean nSetOutlineNone(long paramLong);
  
  @CriticalNative
  private static native boolean nSetOutlineRoundRect(long paramLong, int paramInt1, int paramInt2, int paramInt3, int paramInt4, float paramFloat1, float paramFloat2);
  
  @CriticalNative
  private static native boolean nSetPivotX(long paramLong, float paramFloat);
  
  @CriticalNative
  private static native boolean nSetPivotY(long paramLong, float paramFloat);
  
  @CriticalNative
  private static native boolean nSetProjectBackwards(long paramLong, boolean paramBoolean);
  
  @CriticalNative
  private static native boolean nSetProjectionReceiver(long paramLong, boolean paramBoolean);
  
  @CriticalNative
  private static native boolean nSetRevealClip(long paramLong, boolean paramBoolean, float paramFloat1, float paramFloat2, float paramFloat3);
  
  @CriticalNative
  private static native boolean nSetRight(long paramLong, int paramInt);
  
  @CriticalNative
  private static native boolean nSetRotation(long paramLong, float paramFloat);
  
  @CriticalNative
  private static native boolean nSetRotationX(long paramLong, float paramFloat);
  
  @CriticalNative
  private static native boolean nSetRotationY(long paramLong, float paramFloat);
  
  @CriticalNative
  private static native boolean nSetScaleX(long paramLong, float paramFloat);
  
  @CriticalNative
  private static native boolean nSetScaleY(long paramLong, float paramFloat);
  
  @CriticalNative
  private static native boolean nSetSpotShadowColor(long paramLong, int paramInt);
  
  @CriticalNative
  private static native boolean nSetStaticMatrix(long paramLong1, long paramLong2);
  
  @CriticalNative
  private static native boolean nSetTop(long paramLong, int paramInt);
  
  @CriticalNative
  private static native boolean nSetTranslationX(long paramLong, float paramFloat);
  
  @CriticalNative
  private static native boolean nSetTranslationY(long paramLong, float paramFloat);
  
  @CriticalNative
  private static native boolean nSetTranslationZ(long paramLong, float paramFloat);
  
  public void addAnimator(RenderNodeAnimator paramRenderNodeAnimator)
  {
    if ((mOwningView != null) && (mOwningView.mAttachInfo != null))
    {
      nAddAnimator(mNativeRenderNode, paramRenderNodeAnimator.getNativeAnimator());
      mOwningView.mAttachInfo.mViewRootImpl.registerAnimatingRenderNode(this);
      return;
    }
    throw new IllegalStateException("Cannot start this animator on a detached view!");
  }
  
  public void destroy() {}
  
  public void discardDisplayList()
  {
    nSetDisplayList(mNativeRenderNode, 0L);
  }
  
  public void end(DisplayListCanvas paramDisplayListCanvas)
  {
    long l = paramDisplayListCanvas.finishRecording();
    nSetDisplayList(mNativeRenderNode, l);
    paramDisplayListCanvas.recycle();
  }
  
  public void endAllAnimators()
  {
    nEndAllAnimators(mNativeRenderNode);
  }
  
  public float getAlpha()
  {
    return nGetAlpha(mNativeRenderNode);
  }
  
  public int getAmbientShadowColor()
  {
    return nGetAmbientShadowColor(mNativeRenderNode);
  }
  
  public float getCameraDistance()
  {
    return nGetCameraDistance(mNativeRenderNode);
  }
  
  public boolean getClipToOutline()
  {
    return nGetClipToOutline(mNativeRenderNode);
  }
  
  public int getDebugSize()
  {
    return nGetDebugSize(mNativeRenderNode);
  }
  
  public float getElevation()
  {
    return nGetElevation(mNativeRenderNode);
  }
  
  public void getInverseMatrix(Matrix paramMatrix)
  {
    nGetInverseTransformMatrix(mNativeRenderNode, native_instance);
  }
  
  public void getMatrix(Matrix paramMatrix)
  {
    nGetTransformMatrix(mNativeRenderNode, native_instance);
  }
  
  long getNativeDisplayList()
  {
    if (isValid()) {
      return mNativeRenderNode;
    }
    throw new IllegalStateException("The display list is not valid.");
  }
  
  public float getPivotX()
  {
    return nGetPivotX(mNativeRenderNode);
  }
  
  public float getPivotY()
  {
    return nGetPivotY(mNativeRenderNode);
  }
  
  public float getRotation()
  {
    return nGetRotation(mNativeRenderNode);
  }
  
  public float getRotationX()
  {
    return nGetRotationX(mNativeRenderNode);
  }
  
  public float getRotationY()
  {
    return nGetRotationY(mNativeRenderNode);
  }
  
  public float getScaleX()
  {
    return nGetScaleX(mNativeRenderNode);
  }
  
  public float getScaleY()
  {
    return nGetScaleY(mNativeRenderNode);
  }
  
  public int getSpotShadowColor()
  {
    return nGetSpotShadowColor(mNativeRenderNode);
  }
  
  public float getTranslationX()
  {
    return nGetTranslationX(mNativeRenderNode);
  }
  
  public float getTranslationY()
  {
    return nGetTranslationY(mNativeRenderNode);
  }
  
  public float getTranslationZ()
  {
    return nGetTranslationZ(mNativeRenderNode);
  }
  
  public boolean hasIdentityMatrix()
  {
    return nHasIdentityMatrix(mNativeRenderNode);
  }
  
  public boolean hasOverlappingRendering()
  {
    return nHasOverlappingRendering(mNativeRenderNode);
  }
  
  public boolean hasShadow()
  {
    return nHasShadow(mNativeRenderNode);
  }
  
  public boolean isAttached()
  {
    boolean bool;
    if ((mOwningView != null) && (mOwningView.mAttachInfo != null)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isPivotExplicitlySet()
  {
    return nIsPivotExplicitlySet(mNativeRenderNode);
  }
  
  public boolean isValid()
  {
    return nIsValid(mNativeRenderNode);
  }
  
  public boolean offsetLeftAndRight(int paramInt)
  {
    return nOffsetLeftAndRight(mNativeRenderNode, paramInt);
  }
  
  public boolean offsetTopAndBottom(int paramInt)
  {
    return nOffsetTopAndBottom(mNativeRenderNode, paramInt);
  }
  
  public void output()
  {
    nOutput(mNativeRenderNode);
  }
  
  public void registerVectorDrawableAnimator(AnimatedVectorDrawable.VectorDrawableAnimatorRT paramVectorDrawableAnimatorRT)
  {
    if ((mOwningView != null) && (mOwningView.mAttachInfo != null))
    {
      mOwningView.mAttachInfo.mViewRootImpl.registerVectorDrawableAnimator(paramVectorDrawableAnimatorRT);
      return;
    }
    throw new IllegalStateException("Cannot start this animator on a detached view!");
  }
  
  public void requestPositionUpdates(SurfaceView paramSurfaceView)
  {
    nRequestPositionUpdates(mNativeRenderNode, paramSurfaceView);
  }
  
  public boolean resetPivot()
  {
    return nResetPivot(mNativeRenderNode);
  }
  
  public boolean setAlpha(float paramFloat)
  {
    return nSetAlpha(mNativeRenderNode, paramFloat);
  }
  
  public boolean setAmbientShadowColor(int paramInt)
  {
    return nSetAmbientShadowColor(mNativeRenderNode, paramInt);
  }
  
  public boolean setAnimationMatrix(Matrix paramMatrix)
  {
    long l1 = mNativeRenderNode;
    long l2;
    if (paramMatrix != null) {
      l2 = native_instance;
    } else {
      l2 = 0L;
    }
    return nSetAnimationMatrix(l1, l2);
  }
  
  public boolean setBottom(int paramInt)
  {
    return nSetBottom(mNativeRenderNode, paramInt);
  }
  
  public boolean setCameraDistance(float paramFloat)
  {
    return nSetCameraDistance(mNativeRenderNode, paramFloat);
  }
  
  public boolean setClipBounds(Rect paramRect)
  {
    if (paramRect == null) {
      return nSetClipBoundsEmpty(mNativeRenderNode);
    }
    return nSetClipBounds(mNativeRenderNode, left, top, right, bottom);
  }
  
  public boolean setClipToBounds(boolean paramBoolean)
  {
    return nSetClipToBounds(mNativeRenderNode, paramBoolean);
  }
  
  public boolean setClipToOutline(boolean paramBoolean)
  {
    return nSetClipToOutline(mNativeRenderNode, paramBoolean);
  }
  
  public boolean setElevation(float paramFloat)
  {
    return nSetElevation(mNativeRenderNode, paramFloat);
  }
  
  public boolean setHasOverlappingRendering(boolean paramBoolean)
  {
    return nSetHasOverlappingRendering(mNativeRenderNode, paramBoolean);
  }
  
  public boolean setLayerPaint(Paint paramPaint)
  {
    long l1 = mNativeRenderNode;
    long l2;
    if (paramPaint != null) {
      l2 = paramPaint.getNativeInstance();
    } else {
      l2 = 0L;
    }
    return nSetLayerPaint(l1, l2);
  }
  
  public boolean setLayerType(int paramInt)
  {
    return nSetLayerType(mNativeRenderNode, paramInt);
  }
  
  public boolean setLeft(int paramInt)
  {
    return nSetLeft(mNativeRenderNode, paramInt);
  }
  
  public boolean setLeftTopRightBottom(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    return nSetLeftTopRightBottom(mNativeRenderNode, paramInt1, paramInt2, paramInt3, paramInt4);
  }
  
  public boolean setOutline(Outline paramOutline)
  {
    if (paramOutline == null) {
      return nSetOutlineNone(mNativeRenderNode);
    }
    switch (mMode)
    {
    default: 
      throw new IllegalArgumentException("Unrecognized outline?");
    case 2: 
      return nSetOutlineConvexPath(mNativeRenderNode, mPath.mNativePath, mAlpha);
    case 1: 
      return nSetOutlineRoundRect(mNativeRenderNode, mRect.left, mRect.top, mRect.right, mRect.bottom, mRadius, mAlpha);
    }
    return nSetOutlineEmpty(mNativeRenderNode);
  }
  
  public boolean setPivotX(float paramFloat)
  {
    return nSetPivotX(mNativeRenderNode, paramFloat);
  }
  
  public boolean setPivotY(float paramFloat)
  {
    return nSetPivotY(mNativeRenderNode, paramFloat);
  }
  
  public boolean setProjectBackwards(boolean paramBoolean)
  {
    return nSetProjectBackwards(mNativeRenderNode, paramBoolean);
  }
  
  public boolean setProjectionReceiver(boolean paramBoolean)
  {
    return nSetProjectionReceiver(mNativeRenderNode, paramBoolean);
  }
  
  public boolean setRevealClip(boolean paramBoolean, float paramFloat1, float paramFloat2, float paramFloat3)
  {
    return nSetRevealClip(mNativeRenderNode, paramBoolean, paramFloat1, paramFloat2, paramFloat3);
  }
  
  public boolean setRight(int paramInt)
  {
    return nSetRight(mNativeRenderNode, paramInt);
  }
  
  public boolean setRotation(float paramFloat)
  {
    return nSetRotation(mNativeRenderNode, paramFloat);
  }
  
  public boolean setRotationX(float paramFloat)
  {
    return nSetRotationX(mNativeRenderNode, paramFloat);
  }
  
  public boolean setRotationY(float paramFloat)
  {
    return nSetRotationY(mNativeRenderNode, paramFloat);
  }
  
  public boolean setScaleX(float paramFloat)
  {
    return nSetScaleX(mNativeRenderNode, paramFloat);
  }
  
  public boolean setScaleY(float paramFloat)
  {
    return nSetScaleY(mNativeRenderNode, paramFloat);
  }
  
  public boolean setSpotShadowColor(int paramInt)
  {
    return nSetSpotShadowColor(mNativeRenderNode, paramInt);
  }
  
  public boolean setStaticMatrix(Matrix paramMatrix)
  {
    return nSetStaticMatrix(mNativeRenderNode, native_instance);
  }
  
  public boolean setTop(int paramInt)
  {
    return nSetTop(mNativeRenderNode, paramInt);
  }
  
  public boolean setTranslationX(float paramFloat)
  {
    return nSetTranslationX(mNativeRenderNode, paramFloat);
  }
  
  public boolean setTranslationY(float paramFloat)
  {
    return nSetTranslationY(mNativeRenderNode, paramFloat);
  }
  
  public boolean setTranslationZ(float paramFloat)
  {
    return nSetTranslationZ(mNativeRenderNode, paramFloat);
  }
  
  public DisplayListCanvas start(int paramInt1, int paramInt2)
  {
    return DisplayListCanvas.obtain(this, paramInt1, paramInt2);
  }
  
  private static class NoImagePreloadHolder
  {
    public static final NativeAllocationRegistry sRegistry = new NativeAllocationRegistry(RenderNode.class.getClassLoader(), RenderNode.access$000(), 1024L);
    
    private NoImagePreloadHolder() {}
  }
}
