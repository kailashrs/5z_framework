package android.transition;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.ViewGroup;
import java.util.Map;

public class Rotate
  extends Transition
{
  private static final String PROPNAME_ROTATION = "android:rotate:rotation";
  
  public Rotate() {}
  
  public void captureEndValues(TransitionValues paramTransitionValues)
  {
    values.put("android:rotate:rotation", Float.valueOf(view.getRotation()));
  }
  
  public void captureStartValues(TransitionValues paramTransitionValues)
  {
    values.put("android:rotate:rotation", Float.valueOf(view.getRotation()));
  }
  
  public Animator createAnimator(ViewGroup paramViewGroup, TransitionValues paramTransitionValues1, TransitionValues paramTransitionValues2)
  {
    if ((paramTransitionValues1 != null) && (paramTransitionValues2 != null))
    {
      paramViewGroup = view;
      float f1 = ((Float)values.get("android:rotate:rotation")).floatValue();
      float f2 = ((Float)values.get("android:rotate:rotation")).floatValue();
      if (f1 != f2)
      {
        paramViewGroup.setRotation(f1);
        return ObjectAnimator.ofFloat(paramViewGroup, View.ROTATION, new float[] { f1, f2 });
      }
      return null;
    }
    return null;
  }
}
