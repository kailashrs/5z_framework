package com.android.internal.widget;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.AudioAttributes;
import android.media.AudioAttributes.Builder;
import android.os.Vibrator;
import android.provider.Settings.System;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import com.android.internal.R.styleable;

public class SlidingTab
  extends ViewGroup
{
  private static final int ANIM_DURATION = 250;
  private static final int ANIM_TARGET_TIME = 500;
  private static final boolean DBG = false;
  private static final int HORIZONTAL = 0;
  private static final String LOG_TAG = "SlidingTab";
  private static final float THRESHOLD = 0.6666667F;
  private static final int TRACKING_MARGIN = 50;
  private static final int VERTICAL = 1;
  private static final long VIBRATE_LONG = 40L;
  private static final long VIBRATE_SHORT = 30L;
  private static final AudioAttributes VIBRATION_ATTRIBUTES = new AudioAttributes.Builder().setContentType(4).setUsage(13).build();
  private boolean mAnimating;
  private final Animation.AnimationListener mAnimationDoneListener = new Animation.AnimationListener()
  {
    public void onAnimationEnd(Animation paramAnonymousAnimation)
    {
      SlidingTab.this.onAnimationDone();
    }
    
    public void onAnimationRepeat(Animation paramAnonymousAnimation) {}
    
    public void onAnimationStart(Animation paramAnonymousAnimation) {}
  };
  private Slider mCurrentSlider;
  private final float mDensity;
  private int mGrabbedState = 0;
  private boolean mHoldLeftOnTransition = true;
  private boolean mHoldRightOnTransition = true;
  private final Slider mLeftSlider;
  private OnTriggerListener mOnTriggerListener;
  private final int mOrientation;
  private Slider mOtherSlider;
  private final Slider mRightSlider;
  private float mThreshold;
  private final Rect mTmpRect = new Rect();
  private boolean mTracking;
  private boolean mTriggered = false;
  private Vibrator mVibrator;
  
  public SlidingTab(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public SlidingTab(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    paramContext = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.SlidingTab);
    mOrientation = paramContext.getInt(0, 0);
    paramContext.recycle();
    mDensity = getResourcesgetDisplayMetricsdensity;
    mLeftSlider = new Slider(this, 17303118, 17303101, 17303132);
    mRightSlider = new Slider(this, 17303127, 17303110, 17303132);
  }
  
  private void cancelGrab()
  {
    mTracking = false;
    mTriggered = false;
    mOtherSlider.show(true);
    mCurrentSlider.reset(false);
    mCurrentSlider.hideTarget();
    mCurrentSlider = null;
    mOtherSlider = null;
    setGrabbedState(0);
  }
  
  private void dispatchTriggerEvent(int paramInt)
  {
    vibrate(40L);
    if (mOnTriggerListener != null) {
      mOnTriggerListener.onTrigger(this, paramInt);
    }
  }
  
  private boolean isHorizontal()
  {
    boolean bool;
    if (mOrientation == 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private void log(String paramString)
  {
    Log.d("SlidingTab", paramString);
  }
  
  private void moveHandle(float paramFloat1, float paramFloat2)
  {
    ImageView localImageView = mCurrentSlider.tab;
    TextView localTextView = mCurrentSlider.text;
    int i;
    if (isHorizontal())
    {
      i = (int)paramFloat1 - localImageView.getLeft() - localImageView.getWidth() / 2;
      localImageView.offsetLeftAndRight(i);
      localTextView.offsetLeftAndRight(i);
    }
    else
    {
      i = (int)paramFloat2 - localImageView.getTop() - localImageView.getHeight() / 2;
      localImageView.offsetTopAndBottom(i);
      localTextView.offsetTopAndBottom(i);
    }
    invalidate();
  }
  
  private void onAnimationDone()
  {
    resetView();
    mAnimating = false;
  }
  
  private void resetView()
  {
    mLeftSlider.reset(false);
    mRightSlider.reset(false);
  }
  
  private void setGrabbedState(int paramInt)
  {
    if (paramInt != mGrabbedState)
    {
      mGrabbedState = paramInt;
      if (mOnTriggerListener != null) {
        mOnTriggerListener.onGrabbedStateChange(this, mGrabbedState);
      }
    }
  }
  
  private void vibrate(long paramLong)
  {
    try
    {
      ContentResolver localContentResolver = mContext.getContentResolver();
      int i = 1;
      if (Settings.System.getIntForUser(localContentResolver, "haptic_feedback_enabled", 1, -2) == 0) {
        i = 0;
      }
      if (i != 0)
      {
        if (mVibrator == null) {
          mVibrator = ((Vibrator)getContext().getSystemService("vibrator"));
        }
        mVibrator.vibrate(paramLong, VIBRATION_ATTRIBUTES);
      }
      return;
    }
    finally {}
  }
  
  private boolean withinView(float paramFloat1, float paramFloat2, View paramView)
  {
    boolean bool;
    if (((isHorizontal()) && (paramFloat2 > -50.0F) && (paramFloat2 < paramView.getHeight() + 50)) || ((!isHorizontal()) && (paramFloat1 > -50.0F) && (paramFloat1 < 50 + paramView.getWidth()))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent)
  {
    int i = paramMotionEvent.getAction();
    float f1 = paramMotionEvent.getX();
    float f2 = paramMotionEvent.getY();
    if (mAnimating) {
      return false;
    }
    mLeftSlider.tab.getHitRect(mTmpRect);
    boolean bool1 = mTmpRect.contains((int)f1, (int)f2);
    mRightSlider.tab.getHitRect(mTmpRect);
    boolean bool2 = mTmpRect.contains((int)f1, (int)f2);
    if ((!mTracking) && (!bool1) && (!bool2)) {
      return false;
    }
    if (i == 0)
    {
      mTracking = true;
      mTriggered = false;
      vibrate(30L);
      f2 = 0.3333333F;
      if (bool1)
      {
        mCurrentSlider = mLeftSlider;
        mOtherSlider = mRightSlider;
        if (isHorizontal()) {
          f2 = 0.6666667F;
        }
        mThreshold = f2;
        setGrabbedState(1);
      }
      else
      {
        mCurrentSlider = mRightSlider;
        mOtherSlider = mLeftSlider;
        if (!isHorizontal()) {
          f2 = 0.6666667F;
        }
        mThreshold = f2;
        setGrabbedState(2);
      }
      mCurrentSlider.setState(1);
      mCurrentSlider.showTarget();
      mOtherSlider.hide();
    }
    return true;
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    if (!paramBoolean) {
      return;
    }
    Slider localSlider = mLeftSlider;
    if (isHorizontal()) {}
    for (int i = 0;; i = 3) {
      break;
    }
    localSlider.layout(paramInt1, paramInt2, paramInt3, paramInt4, i);
    localSlider = mRightSlider;
    if (isHorizontal()) {}
    for (i = 1;; i = 2) {
      break;
    }
    localSlider.layout(paramInt1, paramInt2, paramInt3, paramInt4, i);
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    View.MeasureSpec.getMode(paramInt1);
    int i = View.MeasureSpec.getSize(paramInt1);
    View.MeasureSpec.getMode(paramInt2);
    int j = View.MeasureSpec.getSize(paramInt2);
    mLeftSlider.measure(paramInt1, paramInt2);
    mRightSlider.measure(paramInt1, paramInt2);
    int k = mLeftSlider.getTabWidth();
    paramInt2 = mRightSlider.getTabWidth();
    paramInt1 = mLeftSlider.getTabHeight();
    int m = mRightSlider.getTabHeight();
    if (isHorizontal())
    {
      paramInt2 = Math.max(i, k + paramInt2);
      paramInt1 = Math.max(paramInt1, m);
    }
    else
    {
      paramInt2 = Math.max(k, m);
      paramInt1 = Math.max(j, paramInt1 + m);
    }
    setMeasuredDimension(paramInt2, paramInt1);
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    boolean bool1 = mTracking;
    boolean bool2 = false;
    if (bool1)
    {
      int i = paramMotionEvent.getAction();
      float f1 = paramMotionEvent.getX();
      float f2 = paramMotionEvent.getY();
      switch (i)
      {
      default: 
        break;
      case 2: 
        if (withinView(f1, f2, this))
        {
          moveHandle(f1, f2);
          if (isHorizontal()) {
            f2 = f1;
          }
          f1 = mThreshold;
          if (isHorizontal()) {
            i = getWidth();
          } else {
            i = getHeight();
          }
          f1 *= i;
          if (isHorizontal())
          {
            if (mCurrentSlider == mLeftSlider) {
              if (f2 <= f1) {}
            }
            for (;;)
            {
              i = 1;
              break;
              do
              {
                i = 0;
                break;
              } while (f2 >= f1);
            }
          }
          else
          {
            if (mCurrentSlider == mLeftSlider) {
              if (f2 >= f1) {}
            }
            for (;;)
            {
              i = 1;
              break;
              do
              {
                i = 0;
                break;
              } while (f2 <= f1);
            }
          }
          if ((mTriggered) || (i == 0)) {
            break label332;
          }
          mTriggered = true;
          mTracking = false;
          Slider localSlider = mCurrentSlider;
          int j = 2;
          localSlider.setState(2);
          if (mCurrentSlider == mLeftSlider) {
            i = 1;
          } else {
            i = 0;
          }
          if (i != 0) {
            j = 1;
          }
          dispatchTriggerEvent(j);
          if (i != 0) {
            bool1 = mHoldLeftOnTransition;
          } else {
            bool1 = mHoldRightOnTransition;
          }
          startAnimating(bool1);
          setGrabbedState(0);
        }
        break;
      }
      cancelGrab();
    }
    label332:
    if ((!mTracking) && (!super.onTouchEvent(paramMotionEvent))) {
      bool1 = bool2;
    } else {
      bool1 = true;
    }
    return bool1;
  }
  
  protected void onVisibilityChanged(View paramView, int paramInt)
  {
    super.onVisibilityChanged(paramView, paramInt);
    if ((paramView == this) && (paramInt != 0) && (mGrabbedState != 0)) {
      cancelGrab();
    }
  }
  
  public void reset(boolean paramBoolean)
  {
    mLeftSlider.reset(paramBoolean);
    mRightSlider.reset(paramBoolean);
    if (!paramBoolean) {
      mAnimating = false;
    }
  }
  
  public void setHoldAfterTrigger(boolean paramBoolean1, boolean paramBoolean2)
  {
    mHoldLeftOnTransition = paramBoolean1;
    mHoldRightOnTransition = paramBoolean2;
  }
  
  public void setLeftHintText(int paramInt)
  {
    if (isHorizontal()) {
      mLeftSlider.setHintText(paramInt);
    }
  }
  
  public void setLeftTabResources(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    mLeftSlider.setIcon(paramInt1);
    mLeftSlider.setTarget(paramInt2);
    mLeftSlider.setBarBackgroundResource(paramInt3);
    mLeftSlider.setTabBackgroundResource(paramInt4);
    mLeftSlider.updateDrawableStates();
  }
  
  public void setOnTriggerListener(OnTriggerListener paramOnTriggerListener)
  {
    mOnTriggerListener = paramOnTriggerListener;
  }
  
  public void setRightHintText(int paramInt)
  {
    if (isHorizontal()) {
      mRightSlider.setHintText(paramInt);
    }
  }
  
  public void setRightTabResources(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    mRightSlider.setIcon(paramInt1);
    mRightSlider.setTarget(paramInt2);
    mRightSlider.setBarBackgroundResource(paramInt3);
    mRightSlider.setTabBackgroundResource(paramInt4);
    mRightSlider.updateDrawableStates();
  }
  
  public void setVisibility(int paramInt)
  {
    if ((paramInt != getVisibility()) && (paramInt == 4)) {
      reset(false);
    }
    super.setVisibility(paramInt);
  }
  
  void startAnimating(final boolean paramBoolean)
  {
    mAnimating = true;
    Slider localSlider = mCurrentSlider;
    Object localObject = mOtherSlider;
    boolean bool = isHorizontal();
    final int i = 0;
    final int j = 0;
    int k;
    int m;
    int n;
    if (bool)
    {
      k = tab.getRight();
      i = tab.getWidth();
      m = tab.getLeft();
      n = getWidth();
      if (!paramBoolean) {
        j = i;
      }
      if (localSlider == mRightSlider) {
        j = -(k + n - j);
      } else {
        j = n - m + n - j;
      }
      i = 0;
    }
    else
    {
      int i1 = tab.getTop();
      n = tab.getBottom();
      j = tab.getHeight();
      k = getHeight();
      if (paramBoolean) {
        j = i;
      }
      m = 0;
      if (localSlider == mRightSlider) {
        j = i1 + k - j;
      } else {
        j = -(k - n + k - j);
      }
      i = j;
      j = m;
    }
    localObject = new TranslateAnimation(0.0F, j, 0.0F, i);
    ((Animation)localObject).setDuration(250L);
    ((Animation)localObject).setInterpolator(new LinearInterpolator());
    ((Animation)localObject).setFillAfter(true);
    TranslateAnimation localTranslateAnimation = new TranslateAnimation(0.0F, j, 0.0F, i);
    localTranslateAnimation.setDuration(250L);
    localTranslateAnimation.setInterpolator(new LinearInterpolator());
    localTranslateAnimation.setFillAfter(true);
    ((Animation)localObject).setAnimationListener(new Animation.AnimationListener()
    {
      public void onAnimationEnd(Animation paramAnonymousAnimation)
      {
        if (paramBoolean)
        {
          paramAnonymousAnimation = new TranslateAnimation(j, j, i, i);
          paramAnonymousAnimation.setDuration(1000L);
          SlidingTab.access$202(SlidingTab.this, false);
        }
        else
        {
          paramAnonymousAnimation = new AlphaAnimation(0.5F, 1.0F);
          paramAnonymousAnimation.setDuration(250L);
          SlidingTab.this.resetView();
        }
        paramAnonymousAnimation.setAnimationListener(mAnimationDoneListener);
        mLeftSlider.startAnimation(paramAnonymousAnimation, paramAnonymousAnimation);
        mRightSlider.startAnimation(paramAnonymousAnimation, paramAnonymousAnimation);
      }
      
      public void onAnimationRepeat(Animation paramAnonymousAnimation) {}
      
      public void onAnimationStart(Animation paramAnonymousAnimation) {}
    });
    localSlider.hideTarget();
    localSlider.startAnimation((Animation)localObject, localTranslateAnimation);
  }
  
  public static abstract interface OnTriggerListener
  {
    public static final int LEFT_HANDLE = 1;
    public static final int NO_HANDLE = 0;
    public static final int RIGHT_HANDLE = 2;
    
    public abstract void onGrabbedStateChange(View paramView, int paramInt);
    
    public abstract void onTrigger(View paramView, int paramInt);
  }
  
  private static class Slider
  {
    public static final int ALIGN_BOTTOM = 3;
    public static final int ALIGN_LEFT = 0;
    public static final int ALIGN_RIGHT = 1;
    public static final int ALIGN_TOP = 2;
    public static final int ALIGN_UNKNOWN = 4;
    private static final int STATE_ACTIVE = 2;
    private static final int STATE_NORMAL = 0;
    private static final int STATE_PRESSED = 1;
    private int alignment = 4;
    private int alignment_value;
    private int currentState = 0;
    private final ImageView tab;
    private final ImageView target;
    private final TextView text;
    
    Slider(ViewGroup paramViewGroup, int paramInt1, int paramInt2, int paramInt3)
    {
      tab = new ImageView(paramViewGroup.getContext());
      tab.setBackgroundResource(paramInt1);
      tab.setScaleType(ImageView.ScaleType.CENTER);
      tab.setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
      text = new TextView(paramViewGroup.getContext());
      text.setLayoutParams(new ViewGroup.LayoutParams(-2, -1));
      text.setBackgroundResource(paramInt2);
      text.setTextAppearance(paramViewGroup.getContext(), 16974988);
      target = new ImageView(paramViewGroup.getContext());
      target.setImageResource(paramInt3);
      target.setScaleType(ImageView.ScaleType.CENTER);
      target.setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
      target.setVisibility(4);
      paramViewGroup.addView(target);
      paramViewGroup.addView(tab);
      paramViewGroup.addView(text);
    }
    
    public int getTabHeight()
    {
      return tab.getMeasuredHeight();
    }
    
    public int getTabWidth()
    {
      return tab.getMeasuredWidth();
    }
    
    void hide()
    {
      int i = alignment;
      int j = 0;
      int k;
      if ((i != 0) && (alignment != 1)) {
        k = 0;
      } else {
        k = 1;
      }
      if (k != 0)
      {
        if (alignment == 0) {
          i = alignment_value - tab.getRight();
        } else {
          i = alignment_value - tab.getLeft();
        }
      }
      else {
        i = 0;
      }
      if (k != 0) {
        k = j;
      } else if (alignment == 2) {
        k = alignment_value - tab.getBottom();
      } else {
        k = alignment_value - tab.getTop();
      }
      TranslateAnimation localTranslateAnimation = new TranslateAnimation(0.0F, i, 0.0F, k);
      localTranslateAnimation.setDuration(250L);
      localTranslateAnimation.setFillAfter(true);
      tab.startAnimation(localTranslateAnimation);
      text.startAnimation(localTranslateAnimation);
      target.setVisibility(4);
    }
    
    public void hideTarget()
    {
      target.clearAnimation();
      target.setVisibility(4);
    }
    
    void layout(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
    {
      alignment = paramInt5;
      Drawable localDrawable = tab.getBackground();
      int i = localDrawable.getIntrinsicWidth();
      int j = localDrawable.getIntrinsicHeight();
      localDrawable = target.getDrawable();
      int k = localDrawable.getIntrinsicWidth();
      int m = localDrawable.getIntrinsicHeight();
      int n = paramInt3 - paramInt1;
      int i1 = paramInt4 - paramInt2;
      int i2 = (int)(n * 0.6666667F) - k + i / 2;
      int i3 = (int)(n * 0.3333333F) - i / 2;
      int i4 = (n - i) / 2;
      int i5 = i4 + i;
      if ((paramInt5 != 0) && (paramInt5 != 1))
      {
        paramInt1 = (n - k) / 2;
        k = (n + k) / 2;
        paramInt3 = (int)(i1 * 0.6666667F) + j / 2 - m;
        i3 = (int)(i1 * 0.3333333F) - j / 2;
        if (paramInt5 == 2)
        {
          tab.layout(i4, 0, i5, j);
          text.layout(i4, 0 - i1, i5, 0);
          target.layout(paramInt1, paramInt3, k, paramInt3 + m);
          alignment_value = paramInt2;
        }
      }
      for (;;)
      {
        break;
        tab.layout(i4, i1 - j, i5, i1);
        text.layout(i4, i1, i5, i1 + i1);
        target.layout(paramInt1, i3, k, i3 + m);
        alignment_value = paramInt4;
        continue;
        paramInt2 = i;
        paramInt4 = n;
        n = i2;
        i2 = (i1 - m) / 2;
        m = i2 + m;
        i = (i1 - j) / 2;
        i1 = (i1 + j) / 2;
        if (paramInt5 == 0)
        {
          tab.layout(0, i, paramInt2, i1);
          text.layout(0 - paramInt4, i, 0, i1);
          text.setGravity(5);
          target.layout(n, i2, n + k, m);
          alignment_value = paramInt1;
        }
        else
        {
          tab.layout(paramInt4 - paramInt2, i, paramInt4, i1);
          text.layout(paramInt4, i, paramInt4 + paramInt4, i1);
          target.layout(i3, i2, i3 + k, m);
          text.setGravity(48);
          alignment_value = paramInt3;
        }
      }
    }
    
    public void measure(int paramInt1, int paramInt2)
    {
      paramInt1 = View.MeasureSpec.getSize(paramInt1);
      paramInt2 = View.MeasureSpec.getSize(paramInt2);
      tab.measure(View.MeasureSpec.makeSafeMeasureSpec(paramInt1, 0), View.MeasureSpec.makeSafeMeasureSpec(paramInt2, 0));
      text.measure(View.MeasureSpec.makeSafeMeasureSpec(paramInt1, 0), View.MeasureSpec.makeSafeMeasureSpec(paramInt2, 0));
    }
    
    void reset(boolean paramBoolean)
    {
      setState(0);
      text.setVisibility(0);
      text.setTextAppearance(text.getContext(), 16974988);
      tab.setVisibility(0);
      target.setVisibility(4);
      int i = alignment;
      int j = 1;
      int k = j;
      if (i != 0) {
        if (alignment == 1) {
          k = j;
        } else {
          k = 0;
        }
      }
      if (k != 0)
      {
        if (alignment == 0) {
          j = alignment_value - tab.getLeft();
        } else {
          j = alignment_value - tab.getRight();
        }
      }
      else {
        j = 0;
      }
      if (k != 0) {
        i = 0;
      } else if (alignment == 2) {
        i = alignment_value - tab.getTop();
      } else {
        i = alignment_value - tab.getBottom();
      }
      if (paramBoolean)
      {
        TranslateAnimation localTranslateAnimation = new TranslateAnimation(0.0F, j, 0.0F, i);
        localTranslateAnimation.setDuration(250L);
        localTranslateAnimation.setFillAfter(false);
        text.startAnimation(localTranslateAnimation);
        tab.startAnimation(localTranslateAnimation);
      }
      else
      {
        if (k != 0)
        {
          text.offsetLeftAndRight(j);
          tab.offsetLeftAndRight(j);
        }
        else
        {
          text.offsetTopAndBottom(i);
          tab.offsetTopAndBottom(i);
        }
        text.clearAnimation();
        tab.clearAnimation();
        target.clearAnimation();
      }
    }
    
    void setBarBackgroundResource(int paramInt)
    {
      text.setBackgroundResource(paramInt);
    }
    
    void setHintText(int paramInt)
    {
      text.setText(paramInt);
    }
    
    void setIcon(int paramInt)
    {
      tab.setImageResource(paramInt);
    }
    
    void setState(int paramInt)
    {
      Object localObject = text;
      boolean bool;
      if (paramInt == 1) {
        bool = true;
      } else {
        bool = false;
      }
      ((TextView)localObject).setPressed(bool);
      localObject = tab;
      if (paramInt == 1) {
        bool = true;
      } else {
        bool = false;
      }
      ((ImageView)localObject).setPressed(bool);
      if (paramInt == 2)
      {
        localObject = new int[1];
        localObject[0] = 16842914;
        if (text.getBackground().isStateful()) {
          text.getBackground().setState((int[])localObject);
        }
        if (tab.getBackground().isStateful()) {
          tab.getBackground().setState((int[])localObject);
        }
        text.setTextAppearance(text.getContext(), 16974987);
      }
      else
      {
        text.setTextAppearance(text.getContext(), 16974988);
      }
      currentState = paramInt;
    }
    
    void setTabBackgroundResource(int paramInt)
    {
      tab.setBackgroundResource(paramInt);
    }
    
    void setTarget(int paramInt)
    {
      target.setImageResource(paramInt);
    }
    
    void show(boolean paramBoolean)
    {
      Object localObject = text;
      int i = 0;
      ((TextView)localObject).setVisibility(0);
      tab.setVisibility(0);
      if (paramBoolean)
      {
        int j = alignment;
        int k = 1;
        int m = k;
        if (j != 0) {
          if (alignment == 1) {
            m = k;
          } else {
            m = 0;
          }
        }
        if (m != 0)
        {
          if (alignment == 0) {
            k = tab.getWidth();
          } else {
            k = -tab.getWidth();
          }
        }
        else {
          k = 0;
        }
        if (m != 0) {
          m = i;
        } else if (alignment == 2) {
          m = tab.getHeight();
        } else {
          m = -tab.getHeight();
        }
        localObject = new TranslateAnimation(-k, 0.0F, -m, 0.0F);
        ((Animation)localObject).setDuration(250L);
        tab.startAnimation((Animation)localObject);
        text.startAnimation((Animation)localObject);
      }
    }
    
    void showTarget()
    {
      AlphaAnimation localAlphaAnimation = new AlphaAnimation(0.0F, 1.0F);
      localAlphaAnimation.setDuration(500L);
      target.startAnimation(localAlphaAnimation);
      target.setVisibility(0);
    }
    
    public void startAnimation(Animation paramAnimation1, Animation paramAnimation2)
    {
      tab.startAnimation(paramAnimation1);
      text.startAnimation(paramAnimation2);
    }
    
    public void updateDrawableStates()
    {
      setState(currentState);
    }
  }
}
