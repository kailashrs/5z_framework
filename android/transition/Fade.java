package android.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import com.android.internal.R.styleable;
import java.util.Map;

public class Fade
  extends Visibility
{
  private static boolean DBG = false;
  public static final int IN = 1;
  private static final String LOG_TAG = "Fade";
  public static final int OUT = 2;
  static final String PROPNAME_TRANSITION_ALPHA = "android:fade:transitionAlpha";
  
  public Fade() {}
  
  public Fade(int paramInt)
  {
    setMode(paramInt);
  }
  
  public Fade(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    paramContext = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.Fade);
    setMode(paramContext.getInt(0, getMode()));
    paramContext.recycle();
  }
  
  private Animator createAnimation(final View paramView, float paramFloat1, float paramFloat2)
  {
    if (paramFloat1 == paramFloat2) {
      return null;
    }
    paramView.setTransitionAlpha(paramFloat1);
    ObjectAnimator localObjectAnimator = ObjectAnimator.ofFloat(paramView, "transitionAlpha", new float[] { paramFloat2 });
    if (DBG)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Created animator ");
      localStringBuilder.append(localObjectAnimator);
      Log.d("Fade", localStringBuilder.toString());
    }
    localObjectAnimator.addListener(new FadeAnimatorListener(paramView));
    addListener(new TransitionListenerAdapter()
    {
      public void onTransitionEnd(Transition paramAnonymousTransition)
      {
        paramView.setTransitionAlpha(1.0F);
        paramAnonymousTransition.removeListener(this);
      }
    });
    return localObjectAnimator;
  }
  
  private static float getStartAlpha(TransitionValues paramTransitionValues, float paramFloat)
  {
    float f = paramFloat;
    paramFloat = f;
    if (paramTransitionValues != null)
    {
      paramTransitionValues = (Float)values.get("android:fade:transitionAlpha");
      paramFloat = f;
      if (paramTransitionValues != null) {
        paramFloat = paramTransitionValues.floatValue();
      }
    }
    return paramFloat;
  }
  
  public void captureStartValues(TransitionValues paramTransitionValues)
  {
    super.captureStartValues(paramTransitionValues);
    values.put("android:fade:transitionAlpha", Float.valueOf(view.getTransitionAlpha()));
  }
  
  public Animator onAppear(ViewGroup paramViewGroup, View paramView, TransitionValues paramTransitionValues1, TransitionValues paramTransitionValues2)
  {
    if (DBG)
    {
      if (paramTransitionValues1 != null) {
        paramViewGroup = view;
      } else {
        paramViewGroup = null;
      }
      paramTransitionValues2 = new StringBuilder();
      paramTransitionValues2.append("Fade.onAppear: startView, startVis, endView, endVis = ");
      paramTransitionValues2.append(paramViewGroup);
      paramTransitionValues2.append(", ");
      paramTransitionValues2.append(paramView);
      Log.d("Fade", paramTransitionValues2.toString());
    }
    float f1 = getStartAlpha(paramTransitionValues1, 0.0F);
    float f2 = f1;
    if (f1 == 1.0F) {
      f2 = 0.0F;
    }
    return createAnimation(paramView, f2, 1.0F);
  }
  
  public Animator onDisappear(ViewGroup paramViewGroup, View paramView, TransitionValues paramTransitionValues1, TransitionValues paramTransitionValues2)
  {
    return createAnimation(paramView, getStartAlpha(paramTransitionValues1, 1.0F), 0.0F);
  }
  
  private static class FadeAnimatorListener
    extends AnimatorListenerAdapter
  {
    private boolean mLayerTypeChanged = false;
    private final View mView;
    
    public FadeAnimatorListener(View paramView)
    {
      mView = paramView;
    }
    
    public void onAnimationEnd(Animator paramAnimator)
    {
      mView.setTransitionAlpha(1.0F);
      if (mLayerTypeChanged) {
        mView.setLayerType(0, null);
      }
    }
    
    public void onAnimationStart(Animator paramAnimator)
    {
      if ((mView.hasOverlappingRendering()) && (mView.getLayerType() == 0))
      {
        mLayerTypeChanged = true;
        mView.setLayerType(2, null);
      }
    }
  }
}
