package android.transition;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.Map;

public class Recolor
  extends Transition
{
  private static final String PROPNAME_BACKGROUND = "android:recolor:background";
  private static final String PROPNAME_TEXT_COLOR = "android:recolor:textColor";
  
  public Recolor() {}
  
  public Recolor(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  private void captureValues(TransitionValues paramTransitionValues)
  {
    values.put("android:recolor:background", view.getBackground());
    if ((view instanceof TextView)) {
      values.put("android:recolor:textColor", Integer.valueOf(((TextView)view).getCurrentTextColor()));
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
    if ((paramTransitionValues1 != null) && (paramTransitionValues2 != null))
    {
      paramViewGroup = view;
      Object localObject = (Drawable)values.get("android:recolor:background");
      Drawable localDrawable = (Drawable)values.get("android:recolor:background");
      if (((localObject instanceof ColorDrawable)) && ((localDrawable instanceof ColorDrawable)))
      {
        ColorDrawable localColorDrawable = (ColorDrawable)localObject;
        localObject = (ColorDrawable)localDrawable;
        if (localColorDrawable.getColor() != ((ColorDrawable)localObject).getColor())
        {
          ((ColorDrawable)localObject).setColor(localColorDrawable.getColor());
          return ObjectAnimator.ofArgb(localDrawable, "color", new int[] { localColorDrawable.getColor(), ((ColorDrawable)localObject).getColor() });
        }
      }
      if ((paramViewGroup instanceof TextView))
      {
        paramViewGroup = (TextView)paramViewGroup;
        int i = ((Integer)values.get("android:recolor:textColor")).intValue();
        int j = ((Integer)values.get("android:recolor:textColor")).intValue();
        if (i != j)
        {
          paramViewGroup.setTextColor(j);
          return ObjectAnimator.ofArgb(paramViewGroup, "textColor", new int[] { i, j });
        }
      }
      return null;
    }
    return null;
  }
}
