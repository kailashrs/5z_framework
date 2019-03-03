package android.transition;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import java.util.Map;

public class ChangeScroll
  extends Transition
{
  private static final String[] PROPERTIES = { "android:changeScroll:x", "android:changeScroll:y" };
  private static final String PROPNAME_SCROLL_X = "android:changeScroll:x";
  private static final String PROPNAME_SCROLL_Y = "android:changeScroll:y";
  
  public ChangeScroll() {}
  
  public ChangeScroll(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  private void captureValues(TransitionValues paramTransitionValues)
  {
    values.put("android:changeScroll:x", Integer.valueOf(view.getScrollX()));
    values.put("android:changeScroll:y", Integer.valueOf(view.getScrollY()));
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
    if ((paramTransitionValues1 != null) && (paramTransitionValues2 != null))
    {
      View localView = view;
      int i = ((Integer)values.get("android:changeScroll:x")).intValue();
      int j = ((Integer)values.get("android:changeScroll:x")).intValue();
      int k = ((Integer)values.get("android:changeScroll:y")).intValue();
      int m = ((Integer)values.get("android:changeScroll:y")).intValue();
      paramViewGroup = null;
      paramTransitionValues1 = null;
      if (i != j)
      {
        localView.setScrollX(i);
        paramViewGroup = ObjectAnimator.ofInt(localView, "scrollX", new int[] { i, j });
      }
      if (k != m)
      {
        localView.setScrollY(k);
        paramTransitionValues1 = ObjectAnimator.ofInt(localView, "scrollY", new int[] { k, m });
      }
      return TransitionUtils.mergeAnimators(paramViewGroup, paramTransitionValues1);
    }
    return null;
  }
  
  public String[] getTransitionProperties()
  {
    return PROPERTIES;
  }
}
