package android.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.RectEvaluator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import java.util.Map;

public class Crossfade
  extends Transition
{
  public static final int FADE_BEHAVIOR_CROSSFADE = 0;
  public static final int FADE_BEHAVIOR_OUT_IN = 2;
  public static final int FADE_BEHAVIOR_REVEAL = 1;
  private static final String LOG_TAG = "Crossfade";
  private static final String PROPNAME_BITMAP = "android:crossfade:bitmap";
  private static final String PROPNAME_BOUNDS = "android:crossfade:bounds";
  private static final String PROPNAME_DRAWABLE = "android:crossfade:drawable";
  public static final int RESIZE_BEHAVIOR_NONE = 0;
  public static final int RESIZE_BEHAVIOR_SCALE = 1;
  private static RectEvaluator sRectEvaluator = new RectEvaluator();
  private int mFadeBehavior = 1;
  private int mResizeBehavior = 1;
  
  public Crossfade() {}
  
  private void captureValues(TransitionValues paramTransitionValues)
  {
    View localView = view;
    Rect localRect = new Rect(0, 0, localView.getWidth(), localView.getHeight());
    if (mFadeBehavior != 1) {
      localRect.offset(localView.getLeft(), localView.getTop());
    }
    values.put("android:crossfade:bounds", localRect);
    Object localObject = Bitmap.createBitmap(localView.getWidth(), localView.getHeight(), Bitmap.Config.ARGB_8888);
    if ((localView instanceof TextureView)) {
      localObject = ((TextureView)localView).getBitmap();
    } else {
      localView.draw(new Canvas((Bitmap)localObject));
    }
    values.put("android:crossfade:bitmap", localObject);
    localObject = new BitmapDrawable((Bitmap)localObject);
    ((BitmapDrawable)localObject).setBounds(localRect);
    values.put("android:crossfade:drawable", localObject);
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
    if ((paramTransitionValues1 != null) && (paramTransitionValues2 != null))
    {
      final boolean bool;
      if (mFadeBehavior != 1) {
        bool = true;
      } else {
        bool = false;
      }
      Object localObject1 = view;
      Object localObject2 = values;
      paramTransitionValues2 = values;
      Rect localRect1 = (Rect)((Map)localObject2).get("android:crossfade:bounds");
      Rect localRect2 = (Rect)paramTransitionValues2.get("android:crossfade:bounds");
      paramTransitionValues1 = (Bitmap)((Map)localObject2).get("android:crossfade:bitmap");
      paramViewGroup = (Bitmap)paramTransitionValues2.get("android:crossfade:bitmap");
      localObject2 = (BitmapDrawable)((Map)localObject2).get("android:crossfade:drawable");
      paramTransitionValues2 = (BitmapDrawable)paramTransitionValues2.get("android:crossfade:drawable");
      if ((localObject2 != null) && (paramTransitionValues2 != null) && (!paramTransitionValues1.sameAs(paramViewGroup)))
      {
        if (bool) {
          paramViewGroup = ((ViewGroup)((View)localObject1).getParent()).getOverlay();
        } else {
          paramViewGroup = ((View)localObject1).getOverlay();
        }
        if (mFadeBehavior == 1) {
          paramViewGroup.add(paramTransitionValues2);
        }
        paramViewGroup.add((Drawable)localObject2);
        if (mFadeBehavior == 2) {
          paramTransitionValues1 = ObjectAnimator.ofInt(localObject2, "alpha", new int[] { 255, 0, 0 });
        } else {
          paramTransitionValues1 = ObjectAnimator.ofInt(localObject2, "alpha", new int[] { 0 });
        }
        paramTransitionValues1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
          public void onAnimationUpdate(ValueAnimator paramAnonymousValueAnimator)
          {
            val$view.invalidate(val$startDrawable.getBounds());
          }
        });
        int i = mFadeBehavior;
        paramViewGroup = null;
        if (i == 2) {
          paramViewGroup = ObjectAnimator.ofFloat(localObject1, View.ALPHA, new float[] { 0.0F, 0.0F, 1.0F });
        } else if (mFadeBehavior == 0) {
          paramViewGroup = ObjectAnimator.ofFloat(localObject1, View.ALPHA, new float[] { 0.0F, 1.0F });
        }
        paramTransitionValues1.addListener(new AnimatorListenerAdapter()
        {
          public void onAnimationEnd(Animator paramAnonymousAnimator)
          {
            if (bool) {
              paramAnonymousAnimator = ((ViewGroup)val$view.getParent()).getOverlay();
            } else {
              paramAnonymousAnimator = val$view.getOverlay();
            }
            paramAnonymousAnimator.remove(val$startDrawable);
            if (mFadeBehavior == 1) {
              paramAnonymousAnimator.remove(paramTransitionValues2);
            }
          }
        });
        localObject1 = new AnimatorSet();
        ((AnimatorSet)localObject1).playTogether(new Animator[] { paramTransitionValues1 });
        if (paramViewGroup != null) {
          ((AnimatorSet)localObject1).playTogether(new Animator[] { paramViewGroup });
        }
        if ((mResizeBehavior == 1) && (!localRect1.equals(localRect2)))
        {
          ((AnimatorSet)localObject1).playTogether(new Animator[] { ObjectAnimator.ofObject(localObject2, "bounds", sRectEvaluator, new Object[] { localRect1, localRect2 }) });
          if (mResizeBehavior == 1) {
            ((AnimatorSet)localObject1).playTogether(new Animator[] { ObjectAnimator.ofObject(paramTransitionValues2, "bounds", sRectEvaluator, new Object[] { localRect1, localRect2 }) });
          } else {}
        }
        return localObject1;
      }
      return null;
    }
    return null;
  }
  
  public int getFadeBehavior()
  {
    return mFadeBehavior;
  }
  
  public int getResizeBehavior()
  {
    return mResizeBehavior;
  }
  
  public Crossfade setFadeBehavior(int paramInt)
  {
    if ((paramInt >= 0) && (paramInt <= 2)) {
      mFadeBehavior = paramInt;
    }
    return this;
  }
  
  public Crossfade setResizeBehavior(int paramInt)
  {
    if ((paramInt >= 0) && (paramInt <= 1)) {
      mResizeBehavior = paramInt;
    }
    return this;
  }
}
