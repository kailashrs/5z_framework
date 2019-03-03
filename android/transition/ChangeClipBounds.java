package android.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.RectEvaluator;
import android.animation.TypeEvaluator;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import java.util.Map;

public class ChangeClipBounds
  extends Transition
{
  private static final String PROPNAME_BOUNDS = "android:clipBounds:bounds";
  private static final String PROPNAME_CLIP = "android:clipBounds:clip";
  private static final String TAG = "ChangeTransform";
  private static final String[] sTransitionProperties = { "android:clipBounds:clip" };
  
  public ChangeClipBounds() {}
  
  public ChangeClipBounds(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  private void captureValues(TransitionValues paramTransitionValues)
  {
    View localView = view;
    if (localView.getVisibility() == 8) {
      return;
    }
    Rect localRect = localView.getClipBounds();
    values.put("android:clipBounds:clip", localRect);
    if (localRect == null)
    {
      localRect = new Rect(0, 0, localView.getWidth(), localView.getHeight());
      values.put("android:clipBounds:bounds", localRect);
    }
  }
  
  public void captureEndValues(TransitionValues paramTransitionValues)
  {
    captureValues(paramTransitionValues);
  }
  
  public void captureStartValues(TransitionValues paramTransitionValues)
  {
    captureValues(paramTransitionValues);
  }
  
  public Animator createAnimator(ViewGroup paramViewGroup, TransitionValues paramTransitionValues1, TransitionValues paramTransitionValues2)
  {
    if ((paramTransitionValues1 != null) && (paramTransitionValues2 != null) && (values.containsKey("android:clipBounds:clip")) && (values.containsKey("android:clipBounds:clip")))
    {
      Rect localRect = (Rect)values.get("android:clipBounds:clip");
      Object localObject = (Rect)values.get("android:clipBounds:clip");
      int i;
      if (localObject == null) {
        i = 1;
      } else {
        i = 0;
      }
      if ((localRect == null) && (localObject == null)) {
        return null;
      }
      if (localRect == null)
      {
        paramViewGroup = (Rect)values.get("android:clipBounds:bounds");
        paramTransitionValues1 = (TransitionValues)localObject;
      }
      else
      {
        paramViewGroup = localRect;
        paramTransitionValues1 = (TransitionValues)localObject;
        if (localObject == null)
        {
          paramTransitionValues1 = (Rect)values.get("android:clipBounds:bounds");
          paramViewGroup = localRect;
        }
      }
      if (paramViewGroup.equals(paramTransitionValues1)) {
        return null;
      }
      view.setClipBounds(paramViewGroup);
      localObject = new RectEvaluator(new Rect());
      paramViewGroup = ObjectAnimator.ofObject(view, "clipBounds", (TypeEvaluator)localObject, new Object[] { paramViewGroup, paramTransitionValues1 });
      if (i != 0) {
        paramViewGroup.addListener(new AnimatorListenerAdapter()
        {
          public void onAnimationEnd(Animator paramAnonymousAnimator)
          {
            val$endView.setClipBounds(null);
          }
        });
      }
      return paramViewGroup;
    }
    return null;
  }
  
  public String[] getTransitionProperties()
  {
    return sTransitionProperties;
  }
}
