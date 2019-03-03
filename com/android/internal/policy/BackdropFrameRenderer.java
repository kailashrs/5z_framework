package com.android.internal.policy;

import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.ConstantState;
import android.os.Looper;
import android.view.Choreographer;
import android.view.Choreographer.FrameCallback;
import android.view.DisplayListCanvas;
import android.view.RenderNode;
import android.view.ThreadedRenderer;
import android.view.ViewRootImpl;

public class BackdropFrameRenderer
  extends Thread
  implements Choreographer.FrameCallback
{
  private Drawable mCaptionBackgroundDrawable;
  private Choreographer mChoreographer;
  private DecorView mDecorView;
  private RenderNode mFrameAndBackdropNode;
  private boolean mFullscreen;
  private int mLastCaptionHeight;
  private int mLastContentHeight;
  private int mLastContentWidth;
  private int mLastXOffset;
  private int mLastYOffset;
  private ColorDrawable mNavigationBarColor;
  private final Rect mNewTargetRect = new Rect();
  private boolean mOldFullscreen;
  private final Rect mOldStableInsets = new Rect();
  private final Rect mOldSystemInsets = new Rect();
  private final Rect mOldTargetRect = new Rect();
  private ThreadedRenderer mRenderer;
  private boolean mReportNextDraw;
  private final int mResizeMode;
  private Drawable mResizingBackgroundDrawable;
  private final Rect mStableInsets = new Rect();
  private ColorDrawable mStatusBarColor;
  private RenderNode mSystemBarBackgroundNode;
  private final Rect mSystemInsets = new Rect();
  private final Rect mTargetRect = new Rect();
  private final Rect mTmpRect = new Rect();
  private Drawable mUserCaptionBackgroundDrawable;
  
  public BackdropFrameRenderer(DecorView paramDecorView, ThreadedRenderer paramThreadedRenderer, Rect paramRect1, Drawable paramDrawable1, Drawable paramDrawable2, Drawable paramDrawable3, int paramInt1, int paramInt2, boolean paramBoolean, Rect paramRect2, Rect paramRect3, int paramInt3)
  {
    setName("ResizeFrame");
    mRenderer = paramThreadedRenderer;
    onResourcesLoaded(paramDecorView, paramDrawable1, paramDrawable2, paramDrawable3, paramInt1, paramInt2);
    mFrameAndBackdropNode = RenderNode.create("FrameAndBackdropNode", null);
    mRenderer.addRenderNode(mFrameAndBackdropNode, true);
    mTargetRect.set(paramRect1);
    mFullscreen = paramBoolean;
    mOldFullscreen = paramBoolean;
    mSystemInsets.set(paramRect2);
    mStableInsets.set(paramRect3);
    mOldSystemInsets.set(paramRect2);
    mOldStableInsets.set(paramRect3);
    mResizeMode = paramInt3;
    start();
  }
  
  private void addSystemBarNodeIfNeeded()
  {
    if (mSystemBarBackgroundNode != null) {
      return;
    }
    mSystemBarBackgroundNode = RenderNode.create("SystemBarBackgroundNode", null);
    mRenderer.addRenderNode(mSystemBarBackgroundNode, false);
  }
  
  private void doFrameUncheckedLocked()
  {
    mNewTargetRect.set(mTargetRect);
    if ((!mNewTargetRect.equals(mOldTargetRect)) || (mOldFullscreen != mFullscreen) || (!mStableInsets.equals(mOldStableInsets)) || (!mSystemInsets.equals(mOldSystemInsets)) || (mReportNextDraw))
    {
      mOldFullscreen = mFullscreen;
      mOldTargetRect.set(mNewTargetRect);
      mOldSystemInsets.set(mSystemInsets);
      mOldStableInsets.set(mStableInsets);
      redrawLocked(mNewTargetRect, mFullscreen, mSystemInsets, mStableInsets);
    }
  }
  
  private void drawColorViews(int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean, Rect paramRect1, Rect paramRect2)
  {
    if (mSystemBarBackgroundNode == null) {
      return;
    }
    DisplayListCanvas localDisplayListCanvas = mSystemBarBackgroundNode.start(paramInt3, paramInt4);
    mSystemBarBackgroundNode.setLeftTopRightBottom(paramInt1, paramInt2, paramInt1 + paramInt3, paramInt2 + paramInt4);
    paramInt2 = DecorView.getColorViewTopInset(mStableInsets.top, mSystemInsets.top);
    if (mStatusBarColor != null)
    {
      mStatusBarColor.setBounds(0, 0, paramInt1 + paramInt3, paramInt2);
      mStatusBarColor.draw(localDisplayListCanvas);
    }
    if ((mNavigationBarColor != null) && (paramBoolean))
    {
      DecorView.getNavigationBarRect(paramInt3, paramInt4, paramRect2, paramRect1, mTmpRect);
      mNavigationBarColor.setBounds(mTmpRect);
      mNavigationBarColor.draw(localDisplayListCanvas);
    }
    mSystemBarBackgroundNode.end(localDisplayListCanvas);
    mRenderer.drawRenderNode(mSystemBarBackgroundNode);
  }
  
  private void pingRenderLocked(boolean paramBoolean)
  {
    if ((mChoreographer != null) && (!paramBoolean)) {
      mChoreographer.postFrameCallback(this);
    } else {
      doFrameUncheckedLocked();
    }
  }
  
  private void redrawLocked(Rect paramRect1, boolean paramBoolean, Rect paramRect2, Rect paramRect3)
  {
    int i = mDecorView.getCaptionHeight();
    if (i != 0) {
      mLastCaptionHeight = i;
    }
    if (((mLastCaptionHeight != 0) || (!mDecorView.isShowingCaption())) && (mLastContentWidth != 0) && (mLastContentHeight != 0))
    {
      int j = mLastXOffset + left;
      i = mLastYOffset + top;
      int k = paramRect1.width();
      int m = paramRect1.height();
      mFrameAndBackdropNode.setLeftTopRightBottom(j, i, j + k, i + m);
      DisplayListCanvas localDisplayListCanvas = mFrameAndBackdropNode.start(k, m);
      if (mUserCaptionBackgroundDrawable != null) {
        paramRect1 = mUserCaptionBackgroundDrawable;
      } else {
        paramRect1 = mCaptionBackgroundDrawable;
      }
      if (paramRect1 != null)
      {
        paramRect1.setBounds(0, 0, j + k, mLastCaptionHeight + i);
        paramRect1.draw(localDisplayListCanvas);
      }
      if (mResizingBackgroundDrawable != null)
      {
        mResizingBackgroundDrawable.setBounds(0, mLastCaptionHeight, j + k, i + m);
        mResizingBackgroundDrawable.draw(localDisplayListCanvas);
      }
      mFrameAndBackdropNode.end(localDisplayListCanvas);
      drawColorViews(j, i, k, m, paramBoolean, paramRect2, paramRect3);
      mRenderer.drawRenderNode(mFrameAndBackdropNode);
      reportDrawIfNeeded();
      return;
    }
  }
  
  private void reportDrawIfNeeded()
  {
    if (mReportNextDraw)
    {
      if (mDecorView.isAttachedToWindow()) {
        mDecorView.getViewRootImpl().reportDrawFinish();
      }
      mReportNextDraw = false;
    }
  }
  
  public void doFrame(long paramLong)
  {
    try
    {
      if (mRenderer == null)
      {
        reportDrawIfNeeded();
        Looper.myLooper().quit();
        return;
      }
      doFrameUncheckedLocked();
      return;
    }
    finally {}
  }
  
  public void onConfigurationChange()
  {
    try
    {
      if (mRenderer != null)
      {
        mOldTargetRect.set(0, 0, 0, 0);
        pingRenderLocked(false);
      }
      return;
    }
    finally {}
  }
  
  public boolean onContentDrawn(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    try
    {
      int i = mLastContentWidth;
      boolean bool1 = false;
      if (i == 0) {
        i = 1;
      } else {
        i = 0;
      }
      mLastContentWidth = paramInt3;
      mLastContentHeight = (paramInt4 - mLastCaptionHeight);
      mLastXOffset = paramInt1;
      mLastYOffset = paramInt2;
      mRenderer.setContentDrawBounds(mLastXOffset, mLastYOffset, mLastXOffset + mLastContentWidth, mLastYOffset + mLastCaptionHeight + mLastContentHeight);
      boolean bool2 = bool1;
      if (i != 0) {
        if (mLastCaptionHeight == 0)
        {
          bool2 = bool1;
          if (mDecorView.isShowingCaption()) {}
        }
        else
        {
          bool2 = true;
        }
      }
      return bool2;
    }
    finally {}
  }
  
  public void onRequestDraw(boolean paramBoolean)
  {
    try
    {
      mReportNextDraw = paramBoolean;
      mOldTargetRect.set(0, 0, 0, 0);
      pingRenderLocked(true);
      return;
    }
    finally {}
  }
  
  void onResourcesLoaded(DecorView paramDecorView, Drawable paramDrawable1, Drawable paramDrawable2, Drawable paramDrawable3, int paramInt1, int paramInt2)
  {
    mDecorView = paramDecorView;
    if ((paramDrawable1 != null) && (paramDrawable1.getConstantState() != null)) {
      paramDecorView = paramDrawable1.getConstantState().newDrawable();
    } else {
      paramDecorView = null;
    }
    mResizingBackgroundDrawable = paramDecorView;
    if ((paramDrawable2 != null) && (paramDrawable2.getConstantState() != null)) {
      paramDecorView = paramDrawable2.getConstantState().newDrawable();
    } else {
      paramDecorView = null;
    }
    mCaptionBackgroundDrawable = paramDecorView;
    if ((paramDrawable3 != null) && (paramDrawable3.getConstantState() != null)) {
      paramDecorView = paramDrawable3.getConstantState().newDrawable();
    } else {
      paramDecorView = null;
    }
    mUserCaptionBackgroundDrawable = paramDecorView;
    if (mCaptionBackgroundDrawable == null) {
      mCaptionBackgroundDrawable = mResizingBackgroundDrawable;
    }
    if (paramInt1 != 0)
    {
      mStatusBarColor = new ColorDrawable(paramInt1);
      addSystemBarNodeIfNeeded();
    }
    else
    {
      mStatusBarColor = null;
    }
    if (paramInt2 != 0)
    {
      mNavigationBarColor = new ColorDrawable(paramInt2);
      addSystemBarNodeIfNeeded();
    }
    else
    {
      mNavigationBarColor = null;
    }
  }
  
  public void releaseRenderer()
  {
    try
    {
      if (mRenderer != null)
      {
        mRenderer.setContentDrawBounds(0, 0, 0, 0);
        mRenderer.removeRenderNode(mFrameAndBackdropNode);
        if (mSystemBarBackgroundNode != null) {
          mRenderer.removeRenderNode(mSystemBarBackgroundNode);
        }
        mRenderer = null;
        pingRenderLocked(false);
      }
      return;
    }
    finally {}
  }
  
  /* Error */
  public void run()
  {
    // Byte code:
    //   0: invokestatic 282	android/os/Looper:prepare	()V
    //   3: aload_0
    //   4: monitorenter
    //   5: aload_0
    //   6: invokestatic 286	android/view/Choreographer:getInstance	()Landroid/view/Choreographer;
    //   9: putfield 174	com/android/internal/policy/BackdropFrameRenderer:mChoreographer	Landroid/view/Choreographer;
    //   12: aload_0
    //   13: monitorexit
    //   14: invokestatic 289	android/os/Looper:loop	()V
    //   17: aload_0
    //   18: invokevirtual 291	com/android/internal/policy/BackdropFrameRenderer:releaseRenderer	()V
    //   21: aload_0
    //   22: monitorenter
    //   23: aload_0
    //   24: aconst_null
    //   25: putfield 174	com/android/internal/policy/BackdropFrameRenderer:mChoreographer	Landroid/view/Choreographer;
    //   28: invokestatic 294	android/view/Choreographer:releaseInstance	()V
    //   31: aload_0
    //   32: monitorexit
    //   33: return
    //   34: astore_1
    //   35: aload_0
    //   36: monitorexit
    //   37: aload_1
    //   38: athrow
    //   39: astore_1
    //   40: aload_0
    //   41: monitorexit
    //   42: aload_1
    //   43: athrow
    //   44: astore_1
    //   45: aload_0
    //   46: invokevirtual 291	com/android/internal/policy/BackdropFrameRenderer:releaseRenderer	()V
    //   49: aload_1
    //   50: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	51	0	this	BackdropFrameRenderer
    //   34	4	1	localObject1	Object
    //   39	4	1	localObject2	Object
    //   44	6	1	localObject3	Object
    // Exception table:
    //   from	to	target	type
    //   23	33	34	finally
    //   35	37	34	finally
    //   5	14	39	finally
    //   40	42	39	finally
    //   0	5	44	finally
    //   14	17	44	finally
    //   42	44	44	finally
  }
  
  public void setTargetRect(Rect paramRect1, boolean paramBoolean, Rect paramRect2, Rect paramRect3)
  {
    try
    {
      mFullscreen = paramBoolean;
      mTargetRect.set(paramRect1);
      mSystemInsets.set(paramRect2);
      mStableInsets.set(paramRect3);
      pingRenderLocked(false);
      return;
    }
    finally {}
  }
  
  void setUserCaptionBackgroundDrawable(Drawable paramDrawable)
  {
    mUserCaptionBackgroundDrawable = paramDrawable;
  }
}
