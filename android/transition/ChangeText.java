package android.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import java.util.Map;

public class ChangeText
  extends Transition
{
  public static final int CHANGE_BEHAVIOR_IN = 2;
  public static final int CHANGE_BEHAVIOR_KEEP = 0;
  public static final int CHANGE_BEHAVIOR_OUT = 1;
  public static final int CHANGE_BEHAVIOR_OUT_IN = 3;
  private static final String LOG_TAG = "TextChange";
  private static final String PROPNAME_TEXT = "android:textchange:text";
  private static final String PROPNAME_TEXT_COLOR = "android:textchange:textColor";
  private static final String PROPNAME_TEXT_SELECTION_END = "android:textchange:textSelectionEnd";
  private static final String PROPNAME_TEXT_SELECTION_START = "android:textchange:textSelectionStart";
  private static final String[] sTransitionProperties = { "android:textchange:text", "android:textchange:textSelectionStart", "android:textchange:textSelectionEnd" };
  private int mChangeBehavior = 0;
  
  public ChangeText() {}
  
  private void captureValues(TransitionValues paramTransitionValues)
  {
    if ((view instanceof TextView))
    {
      TextView localTextView = (TextView)view;
      values.put("android:textchange:text", localTextView.getText());
      if ((localTextView instanceof EditText))
      {
        values.put("android:textchange:textSelectionStart", Integer.valueOf(localTextView.getSelectionStart()));
        values.put("android:textchange:textSelectionEnd", Integer.valueOf(localTextView.getSelectionEnd()));
      }
      if (mChangeBehavior > 0) {
        values.put("android:textchange:textColor", Integer.valueOf(localTextView.getCurrentTextColor()));
      }
    }
  }
  
  private void setSelection(EditText paramEditText, int paramInt1, int paramInt2)
  {
    if ((paramInt1 >= 0) && (paramInt2 >= 0)) {
      paramEditText.setSelection(paramInt1, paramInt2);
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
  
  public Animator createAnimator(ViewGroup paramViewGroup, TransitionValues paramTransitionValues1, final TransitionValues paramTransitionValues2)
  {
    if ((paramTransitionValues1 != null) && (paramTransitionValues2 != null) && ((view instanceof TextView)) && ((view instanceof TextView)))
    {
      final TextView localTextView = (TextView)view;
      paramViewGroup = values;
      paramTransitionValues1 = values;
      if (paramViewGroup.get("android:textchange:text") != null) {
        paramTransitionValues2 = (CharSequence)paramViewGroup.get("android:textchange:text");
      } else {
        paramTransitionValues2 = "";
      }
      Object localObject;
      if (paramTransitionValues1.get("android:textchange:text") != null) {
        localObject = (CharSequence)paramTransitionValues1.get("android:textchange:text");
      } else {
        localObject = "";
      }
      boolean bool = localTextView instanceof EditText;
      final int i = -1;
      final int j;
      final int k;
      final int m;
      final int n;
      final int i1;
      if (bool)
      {
        if (paramViewGroup.get("android:textchange:textSelectionStart") != null) {
          j = ((Integer)paramViewGroup.get("android:textchange:textSelectionStart")).intValue();
        } else {
          j = -1;
        }
        if (paramViewGroup.get("android:textchange:textSelectionEnd") != null) {
          k = ((Integer)paramViewGroup.get("android:textchange:textSelectionEnd")).intValue();
        } else {
          k = j;
        }
        if (paramTransitionValues1.get("android:textchange:textSelectionStart") != null) {
          i = ((Integer)paramTransitionValues1.get("android:textchange:textSelectionStart")).intValue();
        }
        if (paramTransitionValues1.get("android:textchange:textSelectionEnd") != null) {
          m = ((Integer)paramTransitionValues1.get("android:textchange:textSelectionEnd")).intValue();
        } else {
          m = i;
        }
        n = k;
        i1 = m;
        k = j;
        m = n;
        n = i;
        i = i1;
      }
      else
      {
        k = -1;
        i = k;
        n = -1;
        m = -1;
      }
      if (!paramTransitionValues2.equals(localObject))
      {
        if (mChangeBehavior != 2)
        {
          localTextView.setText(paramTransitionValues2);
          if ((localTextView instanceof EditText)) {
            setSelection((EditText)localTextView, k, m);
          }
        }
        if (mChangeBehavior == 0)
        {
          j = 0;
          paramViewGroup = ValueAnimator.ofFloat(new float[] { 0.0F, 1.0F });
          paramViewGroup.addListener(new AnimatorListenerAdapter()
          {
            public void onAnimationEnd(Animator paramAnonymousAnimator)
            {
              if (paramTransitionValues2.equals(localTextView.getText()))
              {
                localTextView.setText(val$endText);
                if ((localTextView instanceof EditText)) {
                  ChangeText.this.setSelection((EditText)localTextView, n, i);
                }
              }
            }
          });
        }
        else
        {
          i1 = ((Integer)paramViewGroup.get("android:textchange:textColor")).intValue();
          j = ((Integer)paramTransitionValues1.get("android:textchange:textColor")).intValue();
          if ((mChangeBehavior != 3) && (mChangeBehavior != 1))
          {
            paramViewGroup = null;
          }
          else
          {
            paramViewGroup = ValueAnimator.ofInt(new int[] { Color.alpha(i1), 0 });
            paramViewGroup.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
            {
              public void onAnimationUpdate(ValueAnimator paramAnonymousValueAnimator)
              {
                int i = ((Integer)paramAnonymousValueAnimator.getAnimatedValue()).intValue();
                localTextView.setTextColor(i << 24 | i1 & 0xFFFFFF);
              }
            });
            paramViewGroup.addListener(new AnimatorListenerAdapter()
            {
              public void onAnimationEnd(Animator paramAnonymousAnimator)
              {
                if (paramTransitionValues2.equals(localTextView.getText()))
                {
                  localTextView.setText(val$endText);
                  if ((localTextView instanceof EditText)) {
                    ChangeText.this.setSelection((EditText)localTextView, n, i);
                  }
                }
                localTextView.setTextColor(j);
              }
            });
          }
          if ((mChangeBehavior != 3) && (mChangeBehavior != 2))
          {
            paramTransitionValues1 = null;
          }
          else
          {
            i1 = j;
            paramTransitionValues1 = ValueAnimator.ofInt(new int[] { 0, Color.alpha(i1) });
            paramTransitionValues1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
            {
              public void onAnimationUpdate(ValueAnimator paramAnonymousValueAnimator)
              {
                int i = ((Integer)paramAnonymousValueAnimator.getAnimatedValue()).intValue();
                localTextView.setTextColor(i << 24 | i1 & 0xFFFFFF);
              }
            });
            paramTransitionValues1.addListener(new AnimatorListenerAdapter()
            {
              public void onAnimationCancel(Animator paramAnonymousAnimator)
              {
                localTextView.setTextColor(i1);
              }
            });
          }
          if ((paramViewGroup != null) && (paramTransitionValues1 != null))
          {
            AnimatorSet localAnimatorSet = new AnimatorSet();
            ((AnimatorSet)localAnimatorSet).playSequentially(new Animator[] { paramViewGroup, paramTransitionValues1 });
            paramViewGroup = localAnimatorSet;
          }
          for (;;)
          {
            break;
            if (paramViewGroup == null) {
              paramViewGroup = paramTransitionValues1;
            }
          }
        }
        addListener(new TransitionListenerAdapter()
        {
          int mPausedColor = 0;
          
          public void onTransitionEnd(Transition paramAnonymousTransition)
          {
            paramAnonymousTransition.removeListener(this);
          }
          
          public void onTransitionPause(Transition paramAnonymousTransition)
          {
            if (mChangeBehavior != 2)
            {
              localTextView.setText(val$endText);
              if ((localTextView instanceof EditText)) {
                ChangeText.this.setSelection((EditText)localTextView, n, i);
              }
            }
            if (mChangeBehavior > 0)
            {
              mPausedColor = localTextView.getCurrentTextColor();
              localTextView.setTextColor(j);
            }
          }
          
          public void onTransitionResume(Transition paramAnonymousTransition)
          {
            if (mChangeBehavior != 2)
            {
              localTextView.setText(paramTransitionValues2);
              if ((localTextView instanceof EditText)) {
                ChangeText.this.setSelection((EditText)localTextView, k, m);
              }
            }
            if (mChangeBehavior > 0) {
              localTextView.setTextColor(mPausedColor);
            }
          }
        });
        return paramViewGroup;
      }
      return null;
    }
    return null;
  }
  
  public int getChangeBehavior()
  {
    return mChangeBehavior;
  }
  
  public String[] getTransitionProperties()
  {
    return sTransitionProperties;
  }
  
  public ChangeText setChangeBehavior(int paramInt)
  {
    if ((paramInt >= 0) && (paramInt <= 3)) {
      mChangeBehavior = paramInt;
    }
    return this;
  }
}
