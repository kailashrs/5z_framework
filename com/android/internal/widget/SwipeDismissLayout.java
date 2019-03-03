package com.android.internal.widget;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ReceiverCallNotAllowedException;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import com.android.internal.R.styleable;

public class SwipeDismissLayout
  extends FrameLayout
{
  private static final float MAX_DIST_THRESHOLD = 0.33F;
  private static final float MIN_DIST_THRESHOLD = 0.1F;
  private static final String TAG = "SwipeDismissLayout";
  private int mActiveTouchId;
  private boolean mActivityTranslucencyConverted = false;
  private boolean mBlockGesture = false;
  private boolean mDiscardIntercept;
  private final DismissAnimator mDismissAnimator = new DismissAnimator();
  private boolean mDismissable = true;
  private boolean mDismissed;
  private OnDismissedListener mDismissedListener;
  private float mDownX;
  private float mDownY;
  private boolean mIsWindowNativelyTranslucent;
  private float mLastX;
  private int mMinFlingVelocity;
  private OnSwipeProgressChangedListener mProgressListener;
  private IntentFilter mScreenOffFilter = new IntentFilter("android.intent.action.SCREEN_OFF");
  private BroadcastReceiver mScreenOffReceiver;
  private int mSlop;
  private boolean mSwiping;
  private VelocityTracker mVelocityTracker;
  
  public SwipeDismissLayout(Context paramContext)
  {
    super(paramContext);
    init(paramContext);
  }
  
  public SwipeDismissLayout(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    init(paramContext);
  }
  
  public SwipeDismissLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    init(paramContext);
  }
  
  private void checkGesture(MotionEvent paramMotionEvent)
  {
    if (paramMotionEvent.getActionMasked() == 0) {
      mBlockGesture = mDismissAnimator.isAnimating();
    }
  }
  
  private void dismiss()
  {
    if (mDismissedListener != null) {
      mDismissedListener.onDismissed(this);
    }
  }
  
  private Activity findActivity()
  {
    for (Context localContext = getContext(); (localContext instanceof ContextWrapper); localContext = ((ContextWrapper)localContext).getBaseContext()) {
      if ((localContext instanceof Activity)) {
        return (Activity)localContext;
      }
    }
    return null;
  }
  
  private void init(Context paramContext)
  {
    ViewConfiguration localViewConfiguration = ViewConfiguration.get(paramContext);
    mSlop = localViewConfiguration.getScaledTouchSlop();
    mMinFlingVelocity = localViewConfiguration.getScaledMinimumFlingVelocity();
    paramContext = paramContext.getTheme().obtainStyledAttributes(R.styleable.Theme);
    mIsWindowNativelyTranslucent = paramContext.getBoolean(5, false);
    paramContext.recycle();
  }
  
  private float progressToAlpha(float paramFloat)
  {
    return 1.0F - paramFloat * paramFloat * paramFloat;
  }
  
  private void resetMembers()
  {
    if (mVelocityTracker != null) {
      mVelocityTracker.recycle();
    }
    mVelocityTracker = null;
    mDownX = 0.0F;
    mLastX = -2.14748365E9F;
    mDownY = 0.0F;
    mSwiping = false;
    mDismissed = false;
    mDiscardIntercept = false;
  }
  
  private void setProgress(float paramFloat)
  {
    if ((mProgressListener != null) && (paramFloat >= 0.0F)) {
      mProgressListener.onSwipeProgressChanged(this, progressToAlpha(paramFloat / getWidth()), paramFloat);
    }
  }
  
  private void updateDismiss(MotionEvent paramMotionEvent)
  {
    float f1 = paramMotionEvent.getRawX() - mDownX;
    mVelocityTracker.computeCurrentVelocity(1000);
    float f2 = mVelocityTracker.getXVelocity();
    if (mLastX == -2.14748365E9F) {
      f2 = f1 / (float)((paramMotionEvent.getEventTime() - paramMotionEvent.getDownTime()) / 1000L);
    }
    if ((!mDismissed) && (((f1 > getWidth() * Math.max(Math.min(-0.23000002F * f2 / mMinFlingVelocity + 0.33F, 0.33F), 0.1F)) && (paramMotionEvent.getRawX() >= mLastX)) || (f2 >= mMinFlingVelocity))) {
      mDismissed = true;
    }
    if ((mDismissed) && (mSwiping) && (f2 < -mMinFlingVelocity)) {
      mDismissed = false;
    }
  }
  
  private void updateSwiping(MotionEvent paramMotionEvent)
  {
    boolean bool1 = mSwiping;
    if (!mSwiping)
    {
      float f1 = paramMotionEvent.getRawX() - mDownX;
      float f2 = paramMotionEvent.getRawY() - mDownY;
      float f3 = mSlop * mSlop;
      boolean bool2 = false;
      if (f1 * f1 + f2 * f2 > f3)
      {
        boolean bool3 = bool2;
        if (f1 > mSlop * 2)
        {
          bool3 = bool2;
          if (Math.abs(f2) < Math.abs(f1)) {
            bool3 = true;
          }
        }
        mSwiping = bool3;
      }
      else
      {
        mSwiping = false;
      }
    }
    if ((mSwiping) && (!bool1) && (!mIsWindowNativelyTranslucent))
    {
      paramMotionEvent = findActivity();
      if (paramMotionEvent != null) {
        mActivityTranslucencyConverted = paramMotionEvent.convertToTranslucent(null, null);
      }
    }
  }
  
  protected boolean canScroll(View paramView, boolean paramBoolean, float paramFloat1, float paramFloat2, float paramFloat3)
  {
    boolean bool1 = paramView instanceof ViewGroup;
    boolean bool2 = true;
    if (bool1)
    {
      ViewGroup localViewGroup = (ViewGroup)paramView;
      int i = paramView.getScrollX();
      int j = paramView.getScrollY();
      for (int k = localViewGroup.getChildCount() - 1; k >= 0; k--)
      {
        View localView = localViewGroup.getChildAt(k);
        if ((paramFloat2 + i >= localView.getLeft()) && (paramFloat2 + i < localView.getRight()) && (paramFloat3 + j >= localView.getTop()) && (paramFloat3 + j < localView.getBottom()) && (canScroll(localView, true, paramFloat1, paramFloat2 + i - localView.getLeft(), paramFloat3 + j - localView.getTop()))) {
          return true;
        }
      }
    }
    if ((paramBoolean) && (paramView.canScrollHorizontally((int)-paramFloat1))) {
      paramBoolean = bool2;
    } else {
      paramBoolean = false;
    }
    return paramBoolean;
  }
  
  protected void cancel()
  {
    if (!mIsWindowNativelyTranslucent)
    {
      Activity localActivity = findActivity();
      if ((localActivity != null) && (mActivityTranslucencyConverted))
      {
        localActivity.convertFromTranslucent();
        mActivityTranslucencyConverted = false;
      }
    }
    if (mProgressListener != null) {
      mProgressListener.onSwipeCancelled(this);
    }
  }
  
  protected void onAttachedToWindow()
  {
    super.onAttachedToWindow();
    try
    {
      BroadcastReceiver local1 = new com/android/internal/widget/SwipeDismissLayout$1;
      local1.<init>(this);
      mScreenOffReceiver = local1;
      getContext().registerReceiver(mScreenOffReceiver, mScreenOffFilter);
    }
    catch (ReceiverCallNotAllowedException localReceiverCallNotAllowedException)
    {
      mScreenOffReceiver = null;
    }
  }
  
  protected void onDetachedFromWindow()
  {
    if (mScreenOffReceiver != null)
    {
      getContext().unregisterReceiver(mScreenOffReceiver);
      mScreenOffReceiver = null;
    }
    super.onDetachedFromWindow();
  }
  
  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent)
  {
    checkGesture(paramMotionEvent);
    boolean bool1 = mBlockGesture;
    boolean bool2 = true;
    if (bool1) {
      return true;
    }
    if (!mDismissable) {
      return super.onInterceptTouchEvent(paramMotionEvent);
    }
    paramMotionEvent.offsetLocation(paramMotionEvent.getRawX() - paramMotionEvent.getX(), 0.0F);
    int i;
    switch (paramMotionEvent.getActionMasked())
    {
    case 4: 
    default: 
      break;
    case 6: 
      i = paramMotionEvent.getActionIndex();
      if (paramMotionEvent.getPointerId(i) == mActiveTouchId)
      {
        if (i == 0) {
          i = 1;
        } else {
          i = 0;
        }
        mActiveTouchId = paramMotionEvent.getPointerId(i);
      }
      break;
    case 5: 
      mActiveTouchId = paramMotionEvent.getPointerId(paramMotionEvent.getActionIndex());
      break;
    case 2: 
      if ((mVelocityTracker != null) && (!mDiscardIntercept))
      {
        i = paramMotionEvent.findPointerIndex(mActiveTouchId);
        if (i == -1)
        {
          Log.e("SwipeDismissLayout", "Invalid pointer index: ignoring.");
          mDiscardIntercept = true;
        }
        else
        {
          float f1 = paramMotionEvent.getRawX() - mDownX;
          float f2 = paramMotionEvent.getX(i);
          float f3 = paramMotionEvent.getY(i);
          if ((f1 != 0.0F) && (canScroll(this, false, f1, f2, f3))) {
            mDiscardIntercept = true;
          } else {
            updateSwiping(paramMotionEvent);
          }
        }
      }
      break;
    case 1: 
    case 3: 
      resetMembers();
      break;
    case 0: 
      resetMembers();
      mDownX = paramMotionEvent.getRawX();
      mDownY = paramMotionEvent.getRawY();
      mActiveTouchId = paramMotionEvent.getPointerId(0);
      mVelocityTracker = VelocityTracker.obtain("int1");
      mVelocityTracker.addMovement(paramMotionEvent);
    }
    if ((mDiscardIntercept) || (!mSwiping)) {
      bool2 = false;
    }
    return bool2;
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    checkGesture(paramMotionEvent);
    if (mBlockGesture) {
      return true;
    }
    if ((mVelocityTracker != null) && (mDismissable))
    {
      paramMotionEvent.offsetLocation(paramMotionEvent.getRawX() - paramMotionEvent.getX(), 0.0F);
      switch (paramMotionEvent.getActionMasked())
      {
      default: 
        break;
      case 3: 
        cancel();
        resetMembers();
        break;
      case 2: 
        mVelocityTracker.addMovement(paramMotionEvent);
        mLastX = paramMotionEvent.getRawX();
        updateSwiping(paramMotionEvent);
        if (mSwiping) {
          setProgress(paramMotionEvent.getRawX() - mDownX);
        }
        break;
      case 1: 
        updateDismiss(paramMotionEvent);
        if (mDismissed) {
          mDismissAnimator.animateDismissal(paramMotionEvent.getRawX() - mDownX);
        } else if ((mSwiping) && (mLastX != -2.14748365E9F)) {
          mDismissAnimator.animateRecovery(paramMotionEvent.getRawX() - mDownX);
        }
        resetMembers();
      }
      return true;
    }
    return super.onTouchEvent(paramMotionEvent);
  }
  
  public void setDismissable(boolean paramBoolean)
  {
    if ((!paramBoolean) && (mDismissable))
    {
      cancel();
      resetMembers();
    }
    mDismissable = paramBoolean;
  }
  
  public void setOnDismissedListener(OnDismissedListener paramOnDismissedListener)
  {
    mDismissedListener = paramOnDismissedListener;
  }
  
  public void setOnSwipeProgressChangedListener(OnSwipeProgressChangedListener paramOnSwipeProgressChangedListener)
  {
    mProgressListener = paramOnSwipeProgressChangedListener;
  }
  
  private class DismissAnimator
    implements ValueAnimator.AnimatorUpdateListener, Animator.AnimatorListener
  {
    private final long DISMISS_DURATION = 250L;
    private final TimeInterpolator DISMISS_INTERPOLATOR = new DecelerateInterpolator(1.5F);
    private final ValueAnimator mDismissAnimator = new ValueAnimator();
    private boolean mDismissOnComplete = false;
    private boolean mWasCanceled = false;
    
    DismissAnimator()
    {
      mDismissAnimator.addUpdateListener(this);
      mDismissAnimator.addListener(this);
    }
    
    private void animate(float paramFloat1, float paramFloat2, long paramLong, TimeInterpolator paramTimeInterpolator, boolean paramBoolean)
    {
      mDismissAnimator.cancel();
      mDismissOnComplete = paramBoolean;
      mDismissAnimator.setFloatValues(new float[] { paramFloat1, paramFloat2 });
      mDismissAnimator.setDuration(paramLong);
      mDismissAnimator.setInterpolator(paramTimeInterpolator);
      mDismissAnimator.start();
    }
    
    void animateDismissal(float paramFloat)
    {
      animate(paramFloat / getWidth(), 1.0F, 250L, DISMISS_INTERPOLATOR, true);
    }
    
    void animateRecovery(float paramFloat)
    {
      animate(paramFloat / getWidth(), 0.0F, 250L, DISMISS_INTERPOLATOR, false);
    }
    
    boolean isAnimating()
    {
      return mDismissAnimator.isStarted();
    }
    
    public void onAnimationCancel(Animator paramAnimator)
    {
      mWasCanceled = true;
    }
    
    public void onAnimationEnd(Animator paramAnimator)
    {
      if (!mWasCanceled) {
        if (mDismissOnComplete) {
          SwipeDismissLayout.this.dismiss();
        } else {
          cancel();
        }
      }
    }
    
    public void onAnimationRepeat(Animator paramAnimator) {}
    
    public void onAnimationStart(Animator paramAnimator)
    {
      mWasCanceled = false;
    }
    
    public void onAnimationUpdate(ValueAnimator paramValueAnimator)
    {
      float f = ((Float)paramValueAnimator.getAnimatedValue()).floatValue();
      SwipeDismissLayout.this.setProgress(getWidth() * f);
    }
  }
  
  public static abstract interface OnDismissedListener
  {
    public abstract void onDismissed(SwipeDismissLayout paramSwipeDismissLayout);
  }
  
  public static abstract interface OnSwipeProgressChangedListener
  {
    public abstract void onSwipeCancelled(SwipeDismissLayout paramSwipeDismissLayout);
    
    public abstract void onSwipeProgressChanged(SwipeDismissLayout paramSwipeDismissLayout, float paramFloat1, float paramFloat2);
  }
}
