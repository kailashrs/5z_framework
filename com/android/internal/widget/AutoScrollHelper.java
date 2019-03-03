package com.android.internal.widget;

import android.content.res.Resources;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.AbsListView;

public abstract class AutoScrollHelper
  implements View.OnTouchListener
{
  private static final int DEFAULT_ACTIVATION_DELAY = ;
  private static final int DEFAULT_EDGE_TYPE = 1;
  private static final float DEFAULT_MAXIMUM_EDGE = Float.MAX_VALUE;
  private static final int DEFAULT_MAXIMUM_VELOCITY_DIPS = 1575;
  private static final int DEFAULT_MINIMUM_VELOCITY_DIPS = 315;
  private static final int DEFAULT_RAMP_DOWN_DURATION = 500;
  private static final int DEFAULT_RAMP_UP_DURATION = 500;
  private static final float DEFAULT_RELATIVE_EDGE = 0.2F;
  private static final float DEFAULT_RELATIVE_VELOCITY = 1.0F;
  public static final int EDGE_TYPE_INSIDE = 0;
  public static final int EDGE_TYPE_INSIDE_EXTEND = 1;
  public static final int EDGE_TYPE_OUTSIDE = 2;
  private static final int HORIZONTAL = 0;
  public static final float NO_MAX = Float.MAX_VALUE;
  public static final float NO_MIN = 0.0F;
  public static final float RELATIVE_UNSPECIFIED = 0.0F;
  private static final int VERTICAL = 1;
  private int mActivationDelay;
  private boolean mAlreadyDelayed;
  private boolean mAnimating;
  private final Interpolator mEdgeInterpolator = new AccelerateInterpolator();
  private int mEdgeType;
  private boolean mEnabled;
  private boolean mExclusive;
  private float[] mMaximumEdges = { Float.MAX_VALUE, Float.MAX_VALUE };
  private float[] mMaximumVelocity = { Float.MAX_VALUE, Float.MAX_VALUE };
  private float[] mMinimumVelocity = { 0.0F, 0.0F };
  private boolean mNeedsCancel;
  private boolean mNeedsReset;
  private float[] mRelativeEdges = { 0.0F, 0.0F };
  private float[] mRelativeVelocity = { 0.0F, 0.0F };
  private Runnable mRunnable;
  private final ClampedScroller mScroller = new ClampedScroller();
  private final View mTarget;
  
  public AutoScrollHelper(View paramView)
  {
    mTarget = paramView;
    paramView = Resources.getSystem().getDisplayMetrics();
    int i = (int)(1575.0F * density + 0.5F);
    int j = (int)(315.0F * density + 0.5F);
    setMaximumVelocity(i, i);
    setMinimumVelocity(j, j);
    setEdgeType(1);
    setMaximumEdges(Float.MAX_VALUE, Float.MAX_VALUE);
    setRelativeEdges(0.2F, 0.2F);
    setRelativeVelocity(1.0F, 1.0F);
    setActivationDelay(DEFAULT_ACTIVATION_DELAY);
    setRampUpDuration(500);
    setRampDownDuration(500);
  }
  
  private void cancelTargetTouch()
  {
    long l = SystemClock.uptimeMillis();
    MotionEvent localMotionEvent = MotionEvent.obtain(l, l, 3, 0.0F, 0.0F, 0);
    mTarget.onTouchEvent(localMotionEvent);
    localMotionEvent.recycle();
  }
  
  private float computeTargetVelocity(int paramInt, float paramFloat1, float paramFloat2, float paramFloat3)
  {
    paramFloat1 = getEdgeValue(mRelativeEdges[paramInt], paramFloat2, mMaximumEdges[paramInt], paramFloat1);
    if (paramFloat1 == 0.0F) {
      return 0.0F;
    }
    float f1 = mRelativeVelocity[paramInt];
    paramFloat2 = mMinimumVelocity[paramInt];
    float f2 = mMaximumVelocity[paramInt];
    paramFloat3 = f1 * paramFloat3;
    if (paramFloat1 > 0.0F) {
      return constrain(paramFloat1 * paramFloat3, paramFloat2, f2);
    }
    return -constrain(-paramFloat1 * paramFloat3, paramFloat2, f2);
  }
  
  private static float constrain(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    if (paramFloat1 > paramFloat3) {
      return paramFloat3;
    }
    if (paramFloat1 < paramFloat2) {
      return paramFloat2;
    }
    return paramFloat1;
  }
  
  private static int constrain(int paramInt1, int paramInt2, int paramInt3)
  {
    if (paramInt1 > paramInt3) {
      return paramInt3;
    }
    if (paramInt1 < paramInt2) {
      return paramInt2;
    }
    return paramInt1;
  }
  
  private float constrainEdgeValue(float paramFloat1, float paramFloat2)
  {
    if (paramFloat2 == 0.0F) {
      return 0.0F;
    }
    switch (mEdgeType)
    {
    default: 
      break;
    case 2: 
      if (paramFloat1 < 0.0F) {
        return paramFloat1 / -paramFloat2;
      }
      break;
    case 0: 
    case 1: 
      if (paramFloat1 < paramFloat2)
      {
        if (paramFloat1 >= 0.0F) {
          return 1.0F - paramFloat1 / paramFloat2;
        }
        if ((mAnimating) && (mEdgeType == 1)) {
          return 1.0F;
        }
      }
      break;
    }
    return 0.0F;
  }
  
  private float getEdgeValue(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    paramFloat1 = constrain(paramFloat1 * paramFloat2, 0.0F, paramFloat3);
    paramFloat3 = constrainEdgeValue(paramFloat4, paramFloat1);
    paramFloat1 = constrainEdgeValue(paramFloat2 - paramFloat4, paramFloat1) - paramFloat3;
    if (paramFloat1 < 0.0F) {}
    for (paramFloat1 = -mEdgeInterpolator.getInterpolation(-paramFloat1);; paramFloat1 = mEdgeInterpolator.getInterpolation(paramFloat1))
    {
      break;
      if (paramFloat1 <= 0.0F) {
        break label79;
      }
    }
    return constrain(paramFloat1, -1.0F, 1.0F);
    label79:
    return 0.0F;
  }
  
  private void requestStop()
  {
    if (mNeedsReset) {
      mAnimating = false;
    } else {
      mScroller.requestStop();
    }
  }
  
  private boolean shouldAnimate()
  {
    ClampedScroller localClampedScroller = mScroller;
    int i = localClampedScroller.getVerticalDirection();
    int j = localClampedScroller.getHorizontalDirection();
    boolean bool;
    if (((i != 0) && (canTargetScrollVertically(i))) || ((j != 0) && (canTargetScrollHorizontally(j)))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private void startAnimating()
  {
    if (mRunnable == null) {
      mRunnable = new ScrollAnimationRunnable(null);
    }
    mAnimating = true;
    mNeedsReset = true;
    if ((!mAlreadyDelayed) && (mActivationDelay > 0)) {
      mTarget.postOnAnimationDelayed(mRunnable, mActivationDelay);
    } else {
      mRunnable.run();
    }
    mAlreadyDelayed = true;
  }
  
  public abstract boolean canTargetScrollHorizontally(int paramInt);
  
  public abstract boolean canTargetScrollVertically(int paramInt);
  
  public boolean isEnabled()
  {
    return mEnabled;
  }
  
  public boolean isExclusive()
  {
    return mExclusive;
  }
  
  public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
  {
    boolean bool1 = mEnabled;
    boolean bool2 = false;
    if (!bool1) {
      return false;
    }
    switch (paramMotionEvent.getActionMasked())
    {
    default: 
      break;
    case 1: 
    case 3: 
      requestStop();
      break;
    case 0: 
      mNeedsCancel = true;
      mAlreadyDelayed = false;
    case 2: 
      float f1 = computeTargetVelocity(0, paramMotionEvent.getX(), paramView.getWidth(), mTarget.getWidth());
      float f2 = computeTargetVelocity(1, paramMotionEvent.getY(), paramView.getHeight(), mTarget.getHeight());
      mScroller.setTargetVelocity(f1, f2);
      if ((!mAnimating) && (shouldAnimate())) {
        startAnimating();
      }
      break;
    }
    bool1 = bool2;
    if (mExclusive)
    {
      bool1 = bool2;
      if (mAnimating) {
        bool1 = true;
      }
    }
    return bool1;
  }
  
  public abstract void scrollTargetBy(int paramInt1, int paramInt2);
  
  public AutoScrollHelper setActivationDelay(int paramInt)
  {
    mActivationDelay = paramInt;
    return this;
  }
  
  public AutoScrollHelper setEdgeType(int paramInt)
  {
    mEdgeType = paramInt;
    return this;
  }
  
  public AutoScrollHelper setEnabled(boolean paramBoolean)
  {
    if ((mEnabled) && (!paramBoolean)) {
      requestStop();
    }
    mEnabled = paramBoolean;
    return this;
  }
  
  public AutoScrollHelper setExclusive(boolean paramBoolean)
  {
    mExclusive = paramBoolean;
    return this;
  }
  
  public AutoScrollHelper setMaximumEdges(float paramFloat1, float paramFloat2)
  {
    mMaximumEdges[0] = paramFloat1;
    mMaximumEdges[1] = paramFloat2;
    return this;
  }
  
  public AutoScrollHelper setMaximumVelocity(float paramFloat1, float paramFloat2)
  {
    mMaximumVelocity[0] = (paramFloat1 / 1000.0F);
    mMaximumVelocity[1] = (paramFloat2 / 1000.0F);
    return this;
  }
  
  public AutoScrollHelper setMinimumVelocity(float paramFloat1, float paramFloat2)
  {
    mMinimumVelocity[0] = (paramFloat1 / 1000.0F);
    mMinimumVelocity[1] = (paramFloat2 / 1000.0F);
    return this;
  }
  
  public AutoScrollHelper setRampDownDuration(int paramInt)
  {
    mScroller.setRampDownDuration(paramInt);
    return this;
  }
  
  public AutoScrollHelper setRampUpDuration(int paramInt)
  {
    mScroller.setRampUpDuration(paramInt);
    return this;
  }
  
  public AutoScrollHelper setRelativeEdges(float paramFloat1, float paramFloat2)
  {
    mRelativeEdges[0] = paramFloat1;
    mRelativeEdges[1] = paramFloat2;
    return this;
  }
  
  public AutoScrollHelper setRelativeVelocity(float paramFloat1, float paramFloat2)
  {
    mRelativeVelocity[0] = (paramFloat1 / 1000.0F);
    mRelativeVelocity[1] = (paramFloat2 / 1000.0F);
    return this;
  }
  
  public static class AbsListViewAutoScroller
    extends AutoScrollHelper
  {
    private final AbsListView mTarget;
    
    public AbsListViewAutoScroller(AbsListView paramAbsListView)
    {
      super();
      mTarget = paramAbsListView;
    }
    
    public boolean canTargetScrollHorizontally(int paramInt)
    {
      return false;
    }
    
    public boolean canTargetScrollVertically(int paramInt)
    {
      AbsListView localAbsListView = mTarget;
      int i = localAbsListView.getCount();
      if (i == 0) {
        return false;
      }
      int j = localAbsListView.getChildCount();
      int k = localAbsListView.getFirstVisiblePosition();
      if (paramInt > 0)
      {
        if (k + j >= i) {
          if (localAbsListView.getChildAt(j - 1).getBottom() <= localAbsListView.getHeight()) {
            return false;
          }
        }
      }
      else
      {
        if (paramInt >= 0) {
          break label88;
        }
        if ((k <= 0) && (localAbsListView.getChildAt(0).getTop() >= 0)) {
          return false;
        }
      }
      return true;
      label88:
      return false;
    }
    
    public void scrollTargetBy(int paramInt1, int paramInt2)
    {
      mTarget.scrollListBy(paramInt2);
    }
  }
  
  private static class ClampedScroller
  {
    private long mDeltaTime = 0L;
    private int mDeltaX = 0;
    private int mDeltaY = 0;
    private int mEffectiveRampDown;
    private int mRampDownDuration;
    private int mRampUpDuration;
    private long mStartTime = Long.MIN_VALUE;
    private long mStopTime = -1L;
    private float mStopValue;
    private float mTargetVelocityX;
    private float mTargetVelocityY;
    
    public ClampedScroller() {}
    
    private float getValueAt(long paramLong)
    {
      if (paramLong < mStartTime) {
        return 0.0F;
      }
      if ((mStopTime >= 0L) && (paramLong >= mStopTime))
      {
        long l = mStopTime;
        return 1.0F - mStopValue + mStopValue * AutoScrollHelper.constrain((float)(paramLong - l) / mEffectiveRampDown, 0.0F, 1.0F);
      }
      return 0.5F * AutoScrollHelper.constrain((float)(paramLong - mStartTime) / mRampUpDuration, 0.0F, 1.0F);
    }
    
    private float interpolateValue(float paramFloat)
    {
      return -4.0F * paramFloat * paramFloat + 4.0F * paramFloat;
    }
    
    public void computeScrollDelta()
    {
      if (mDeltaTime != 0L)
      {
        long l1 = AnimationUtils.currentAnimationTimeMillis();
        float f = interpolateValue(getValueAt(l1));
        long l2 = l1 - mDeltaTime;
        mDeltaTime = l1;
        mDeltaX = ((int)((float)l2 * f * mTargetVelocityX));
        mDeltaY = ((int)((float)l2 * f * mTargetVelocityY));
        return;
      }
      throw new RuntimeException("Cannot compute scroll delta before calling start()");
    }
    
    public int getDeltaX()
    {
      return mDeltaX;
    }
    
    public int getDeltaY()
    {
      return mDeltaY;
    }
    
    public int getHorizontalDirection()
    {
      return (int)(mTargetVelocityX / Math.abs(mTargetVelocityX));
    }
    
    public int getVerticalDirection()
    {
      return (int)(mTargetVelocityY / Math.abs(mTargetVelocityY));
    }
    
    public boolean isFinished()
    {
      boolean bool;
      if ((mStopTime > 0L) && (AnimationUtils.currentAnimationTimeMillis() > mStopTime + mEffectiveRampDown)) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public void requestStop()
    {
      long l = AnimationUtils.currentAnimationTimeMillis();
      mEffectiveRampDown = AutoScrollHelper.constrain((int)(l - mStartTime), 0, mRampDownDuration);
      mStopValue = getValueAt(l);
      mStopTime = l;
    }
    
    public void setRampDownDuration(int paramInt)
    {
      mRampDownDuration = paramInt;
    }
    
    public void setRampUpDuration(int paramInt)
    {
      mRampUpDuration = paramInt;
    }
    
    public void setTargetVelocity(float paramFloat1, float paramFloat2)
    {
      mTargetVelocityX = paramFloat1;
      mTargetVelocityY = paramFloat2;
    }
    
    public void start()
    {
      mStartTime = AnimationUtils.currentAnimationTimeMillis();
      mStopTime = -1L;
      mDeltaTime = mStartTime;
      mStopValue = 0.5F;
      mDeltaX = 0;
      mDeltaY = 0;
    }
  }
  
  private class ScrollAnimationRunnable
    implements Runnable
  {
    private ScrollAnimationRunnable() {}
    
    public void run()
    {
      if (!mAnimating) {
        return;
      }
      if (mNeedsReset)
      {
        AutoScrollHelper.access$202(AutoScrollHelper.this, false);
        mScroller.start();
      }
      AutoScrollHelper.ClampedScroller localClampedScroller = mScroller;
      if ((!localClampedScroller.isFinished()) && (AutoScrollHelper.this.shouldAnimate()))
      {
        if (mNeedsCancel)
        {
          AutoScrollHelper.access$502(AutoScrollHelper.this, false);
          AutoScrollHelper.this.cancelTargetTouch();
        }
        localClampedScroller.computeScrollDelta();
        int i = localClampedScroller.getDeltaX();
        int j = localClampedScroller.getDeltaY();
        scrollTargetBy(i, j);
        mTarget.postOnAnimation(this);
        return;
      }
      AutoScrollHelper.access$102(AutoScrollHelper.this, false);
    }
  }
}
