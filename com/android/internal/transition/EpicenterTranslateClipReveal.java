package com.android.internal.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.TypeEvaluator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.transition.TransitionValues;
import android.transition.Visibility;
import android.util.AttributeSet;
import android.util.Property;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import com.android.internal.R.styleable;
import java.util.Map;

public class EpicenterTranslateClipReveal
  extends Visibility
{
  private static final String PROPNAME_BOUNDS = "android:epicenterReveal:bounds";
  private static final String PROPNAME_CLIP = "android:epicenterReveal:clip";
  private static final String PROPNAME_TRANSLATE_X = "android:epicenterReveal:translateX";
  private static final String PROPNAME_TRANSLATE_Y = "android:epicenterReveal:translateY";
  private static final String PROPNAME_TRANSLATE_Z = "android:epicenterReveal:translateZ";
  private static final String PROPNAME_Z = "android:epicenterReveal:z";
  private final TimeInterpolator mInterpolatorX;
  private final TimeInterpolator mInterpolatorY;
  private final TimeInterpolator mInterpolatorZ;
  
  public EpicenterTranslateClipReveal()
  {
    mInterpolatorX = null;
    mInterpolatorY = null;
    mInterpolatorZ = null;
  }
  
  public EpicenterTranslateClipReveal(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    paramAttributeSet = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.EpicenterTranslateClipReveal, 0, 0);
    int i = paramAttributeSet.getResourceId(0, 0);
    if (i != 0) {
      mInterpolatorX = AnimationUtils.loadInterpolator(paramContext, i);
    } else {
      mInterpolatorX = TransitionConstants.LINEAR_OUT_SLOW_IN;
    }
    i = paramAttributeSet.getResourceId(1, 0);
    if (i != 0) {
      mInterpolatorY = AnimationUtils.loadInterpolator(paramContext, i);
    } else {
      mInterpolatorY = TransitionConstants.FAST_OUT_SLOW_IN;
    }
    i = paramAttributeSet.getResourceId(2, 0);
    if (i != 0) {
      mInterpolatorZ = AnimationUtils.loadInterpolator(paramContext, i);
    } else {
      mInterpolatorZ = TransitionConstants.FAST_OUT_SLOW_IN;
    }
    paramAttributeSet.recycle();
  }
  
  private void captureValues(TransitionValues paramTransitionValues)
  {
    View localView = view;
    if (localView.getVisibility() == 8) {
      return;
    }
    Rect localRect = new Rect(0, 0, localView.getWidth(), localView.getHeight());
    values.put("android:epicenterReveal:bounds", localRect);
    values.put("android:epicenterReveal:translateX", Float.valueOf(localView.getTranslationX()));
    values.put("android:epicenterReveal:translateY", Float.valueOf(localView.getTranslationY()));
    values.put("android:epicenterReveal:translateZ", Float.valueOf(localView.getTranslationZ()));
    values.put("android:epicenterReveal:z", Float.valueOf(localView.getZ()));
    localRect = localView.getClipBounds();
    values.put("android:epicenterReveal:clip", localRect);
  }
  
  private static Animator createRectAnimator(View paramView, State paramState1, State paramState2, float paramFloat1, State paramState3, State paramState4, float paramFloat2, TransitionValues paramTransitionValues, TimeInterpolator paramTimeInterpolator1, TimeInterpolator paramTimeInterpolator2, TimeInterpolator paramTimeInterpolator3)
  {
    StateEvaluator localStateEvaluator = new StateEvaluator(null);
    ObjectAnimator localObjectAnimator = ObjectAnimator.ofFloat(paramView, View.TRANSLATION_Z, new float[] { paramFloat1, paramFloat2 });
    if (paramTimeInterpolator3 != null) {
      localObjectAnimator.setInterpolator(paramTimeInterpolator3);
    }
    paramState1 = ObjectAnimator.ofObject(paramView, new StateProperty('x'), localStateEvaluator, new State[] { paramState1, paramState3 });
    if (paramTimeInterpolator1 != null) {
      paramState1.setInterpolator(paramTimeInterpolator1);
    }
    paramState2 = ObjectAnimator.ofObject(paramView, new StateProperty('y'), localStateEvaluator, new State[] { paramState2, paramState4 });
    if (paramTimeInterpolator2 != null) {
      paramState2.setInterpolator(paramTimeInterpolator2);
    }
    paramState3 = new AnimatorListenerAdapter()
    {
      public void onAnimationEnd(Animator paramAnonymousAnimator)
      {
        setClipBounds(val$terminalClip);
      }
    };
    paramView = new AnimatorSet();
    paramView.playTogether(new Animator[] { paramState1, paramState2, localObjectAnimator });
    paramView.addListener(paramState3);
    return paramView;
  }
  
  private Rect getBestRect(TransitionValues paramTransitionValues)
  {
    Rect localRect = (Rect)values.get("android:epicenterReveal:clip");
    if (localRect == null) {
      return (Rect)values.get("android:epicenterReveal:bounds");
    }
    return localRect;
  }
  
  private Rect getEpicenterOrCenter(Rect paramRect)
  {
    Rect localRect = getEpicenter();
    if (localRect != null) {
      return localRect;
    }
    int i = paramRect.centerX();
    int j = paramRect.centerY();
    return new Rect(i, j, i, j);
  }
  
  public void captureEndValues(TransitionValues paramTransitionValues)
  {
    super.captureEndValues(paramTransitionValues);
    captureValues(paramTransitionValues);
  }
  
  public void captureStartValues(TransitionValues paramTransitionValues)
  {
    super.captureStartValues(paramTransitionValues);
    captureValues(paramTransitionValues);
  }
  
  public Animator onAppear(ViewGroup paramViewGroup, View paramView, TransitionValues paramTransitionValues1, TransitionValues paramTransitionValues2)
  {
    if (paramTransitionValues2 == null) {
      return null;
    }
    paramTransitionValues1 = (Rect)values.get("android:epicenterReveal:bounds");
    paramViewGroup = getEpicenterOrCenter(paramTransitionValues1);
    float f1 = paramViewGroup.centerX() - paramTransitionValues1.centerX();
    float f2 = paramViewGroup.centerY() - paramTransitionValues1.centerY();
    float f3 = 0.0F - ((Float)values.get("android:epicenterReveal:z")).floatValue();
    paramView.setTranslationX(f1);
    paramView.setTranslationY(f2);
    paramView.setTranslationZ(f3);
    float f4 = ((Float)values.get("android:epicenterReveal:translateX")).floatValue();
    float f5 = ((Float)values.get("android:epicenterReveal:translateY")).floatValue();
    float f6 = ((Float)values.get("android:epicenterReveal:translateZ")).floatValue();
    paramTransitionValues1 = getBestRect(paramTransitionValues2);
    Rect localRect = getEpicenterOrCenter(paramTransitionValues1);
    paramView.setClipBounds(localRect);
    paramViewGroup = new State(left, right, f1);
    State localState = new State(left, right, f4);
    return createRectAnimator(paramView, paramViewGroup, new State(top, bottom, f2), f3, localState, new State(top, bottom, f5), f6, paramTransitionValues2, mInterpolatorX, mInterpolatorY, mInterpolatorZ);
  }
  
  public Animator onDisappear(ViewGroup paramViewGroup, View paramView, TransitionValues paramTransitionValues1, TransitionValues paramTransitionValues2)
  {
    if (paramTransitionValues1 == null) {
      return null;
    }
    Rect localRect = (Rect)values.get("android:epicenterReveal:bounds");
    paramViewGroup = getEpicenterOrCenter(localRect);
    float f1 = paramViewGroup.centerX() - localRect.centerX();
    float f2 = paramViewGroup.centerY() - localRect.centerY();
    float f3 = ((Float)values.get("android:epicenterReveal:z")).floatValue();
    float f4 = ((Float)values.get("android:epicenterReveal:translateX")).floatValue();
    float f5 = ((Float)values.get("android:epicenterReveal:translateY")).floatValue();
    float f6 = ((Float)values.get("android:epicenterReveal:translateZ")).floatValue();
    localRect = getBestRect(paramTransitionValues1);
    paramViewGroup = getEpicenterOrCenter(localRect);
    paramView.setClipBounds(localRect);
    paramTransitionValues1 = new State(left, right, f4);
    State localState = new State(left, right, f1);
    return createRectAnimator(paramView, paramTransitionValues1, new State(top, bottom, f5), f6, localState, new State(top, bottom, f2), 0.0F - f3, paramTransitionValues2, mInterpolatorX, mInterpolatorY, mInterpolatorZ);
  }
  
  private static class State
  {
    int lower;
    float trans;
    int upper;
    
    public State() {}
    
    public State(int paramInt1, int paramInt2, float paramFloat)
    {
      lower = paramInt1;
      upper = paramInt2;
      trans = paramFloat;
    }
  }
  
  private static class StateEvaluator
    implements TypeEvaluator<EpicenterTranslateClipReveal.State>
  {
    private final EpicenterTranslateClipReveal.State mTemp = new EpicenterTranslateClipReveal.State();
    
    private StateEvaluator() {}
    
    public EpicenterTranslateClipReveal.State evaluate(float paramFloat, EpicenterTranslateClipReveal.State paramState1, EpicenterTranslateClipReveal.State paramState2)
    {
      mTemp.upper = (upper + (int)((upper - upper) * paramFloat));
      mTemp.lower = (lower + (int)((lower - lower) * paramFloat));
      mTemp.trans = (trans + (int)((trans - trans) * paramFloat));
      return mTemp;
    }
  }
  
  private static class StateProperty
    extends Property<View, EpicenterTranslateClipReveal.State>
  {
    public static final char TARGET_X = 'x';
    public static final char TARGET_Y = 'y';
    private final int mTargetDimension;
    private final Rect mTempRect = new Rect();
    private final EpicenterTranslateClipReveal.State mTempState = new EpicenterTranslateClipReveal.State();
    
    public StateProperty(char paramChar)
    {
      super(localStringBuilder.toString());
      mTargetDimension = paramChar;
    }
    
    public EpicenterTranslateClipReveal.State get(View paramView)
    {
      Rect localRect = mTempRect;
      if (!paramView.getClipBounds(localRect)) {
        localRect.setEmpty();
      }
      EpicenterTranslateClipReveal.State localState = mTempState;
      if (mTargetDimension == 120)
      {
        trans = paramView.getTranslationX();
        lower = (left + (int)trans);
        upper = (right + (int)trans);
      }
      else
      {
        trans = paramView.getTranslationY();
        lower = (top + (int)trans);
        upper = (bottom + (int)trans);
      }
      return localState;
    }
    
    public void set(View paramView, EpicenterTranslateClipReveal.State paramState)
    {
      Rect localRect = mTempRect;
      if (paramView.getClipBounds(localRect))
      {
        if (mTargetDimension == 120)
        {
          left = (lower - (int)trans);
          right = (upper - (int)trans);
        }
        else
        {
          top = (lower - (int)trans);
          bottom = (upper - (int)trans);
        }
        paramView.setClipBounds(localRect);
      }
      if (mTargetDimension == 120) {
        paramView.setTranslationX(trans);
      } else {
        paramView.setTranslationY(trans);
      }
    }
  }
}
