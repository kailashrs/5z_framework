package com.android.internal.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.util.IntProperty;
import android.view.View;
import android.view.View.OnLayoutChangeListener;
import android.view.animation.Interpolator;
import android.view.animation.PathInterpolator;

public class MessagingPropertyAnimator
  implements View.OnLayoutChangeListener
{
  private static final Interpolator ALPHA_IN = new PathInterpolator(0.4F, 0.0F, 1.0F, 1.0F);
  public static final Interpolator ALPHA_OUT = new PathInterpolator(0.0F, 0.0F, 0.8F, 1.0F);
  private static final long APPEAR_ANIMATION_LENGTH = 210L;
  private static final ViewClippingUtil.ClippingParameters CLIPPING_PARAMETERS = _..Lambda.MessagingPropertyAnimator.7coWc0tjIUC7grCXucNFbpYTxDI.INSTANCE;
  private static final int TAG_ALPHA_ANIMATOR = 16909431;
  private static final int TAG_FIRST_LAYOUT = 16909432;
  private static final int TAG_LAYOUT_TOP = 16909433;
  private static final int TAG_TOP = 16909435;
  private static final int TAG_TOP_ANIMATOR = 16909434;
  private static final IntProperty<View> TOP = new IntProperty("top")
  {
    public Integer get(View paramAnonymousView)
    {
      return Integer.valueOf(MessagingPropertyAnimator.getTop(paramAnonymousView));
    }
    
    public void setValue(View paramAnonymousView, int paramAnonymousInt)
    {
      MessagingPropertyAnimator.setTop(paramAnonymousView, paramAnonymousInt);
    }
  };
  
  public MessagingPropertyAnimator() {}
  
  public static void fadeIn(View paramView)
  {
    ObjectAnimator localObjectAnimator = (ObjectAnimator)paramView.getTag(16909431);
    if (localObjectAnimator != null) {
      localObjectAnimator.cancel();
    }
    if (paramView.getVisibility() == 4) {
      paramView.setVisibility(0);
    }
    localObjectAnimator = ObjectAnimator.ofFloat(paramView, View.ALPHA, new float[] { 0.0F, 1.0F });
    paramView.setAlpha(0.0F);
    localObjectAnimator.setInterpolator(ALPHA_IN);
    localObjectAnimator.setDuration(210L);
    localObjectAnimator.addListener(new AnimatorListenerAdapter()
    {
      public void onAnimationEnd(Animator paramAnonymousAnimator)
      {
        setTagInternal(16909431, null);
        MessagingPropertyAnimator.updateLayerType(MessagingPropertyAnimator.this, false);
      }
    });
    updateLayerType(paramView, true);
    paramView.setTagInternal(16909431, localObjectAnimator);
    localObjectAnimator.start();
  }
  
  public static void fadeOut(View paramView, final Runnable paramRunnable)
  {
    ObjectAnimator localObjectAnimator = (ObjectAnimator)paramView.getTag(16909431);
    if (localObjectAnimator != null) {
      localObjectAnimator.cancel();
    }
    if ((paramView.isShown()) && ((!MessagingLinearLayout.isGone(paramView)) || (isHidingAnimated(paramView))))
    {
      localObjectAnimator = ObjectAnimator.ofFloat(paramView, View.ALPHA, new float[] { paramView.getAlpha(), 0.0F });
      localObjectAnimator.setInterpolator(ALPHA_OUT);
      localObjectAnimator.setDuration(210L);
      localObjectAnimator.addListener(new AnimatorListenerAdapter()
      {
        public void onAnimationEnd(Animator paramAnonymousAnimator)
        {
          setTagInternal(16909431, null);
          MessagingPropertyAnimator.updateLayerType(MessagingPropertyAnimator.this, false);
          if (paramRunnable != null) {
            paramRunnable.run();
          }
        }
      });
      updateLayerType(paramView, true);
      paramView.setTagInternal(16909431, localObjectAnimator);
      localObjectAnimator.start();
      return;
    }
    paramView.setAlpha(0.0F);
    if (paramRunnable != null) {
      paramRunnable.run();
    }
  }
  
  public static int getLayoutTop(View paramView)
  {
    Integer localInteger = (Integer)paramView.getTag(16909433);
    if (localInteger == null) {
      return getTop(paramView);
    }
    return localInteger.intValue();
  }
  
  public static int getTop(View paramView)
  {
    Integer localInteger = (Integer)paramView.getTag(16909435);
    if (localInteger == null) {
      return paramView.getTop();
    }
    return localInteger.intValue();
  }
  
  public static boolean isAnimatingAlpha(View paramView)
  {
    boolean bool;
    if (paramView.getTag(16909431) != null) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static boolean isAnimatingTranslation(View paramView)
  {
    boolean bool;
    if (paramView.getTag(16909434) != null) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private static boolean isFirstLayout(View paramView)
  {
    paramView = (Boolean)paramView.getTag(16909432);
    if (paramView == null) {
      return true;
    }
    return paramView.booleanValue();
  }
  
  private static boolean isHidingAnimated(View paramView)
  {
    if ((paramView instanceof MessagingLinearLayout.MessagingChild)) {
      return ((MessagingLinearLayout.MessagingChild)paramView).isHidingAnimated();
    }
    return false;
  }
  
  public static void recycle(View paramView)
  {
    setFirstLayout(paramView, true);
  }
  
  public static void setClippingDeactivated(View paramView, boolean paramBoolean)
  {
    ViewClippingUtil.setClippingDeactivated(paramView, paramBoolean, CLIPPING_PARAMETERS);
  }
  
  private static void setFirstLayout(View paramView, boolean paramBoolean)
  {
    paramView.setTagInternal(16909432, Boolean.valueOf(paramBoolean));
  }
  
  private static void setLayoutTop(View paramView, int paramInt)
  {
    paramView.setTagInternal(16909433, Integer.valueOf(paramInt));
  }
  
  public static void setToLaidOutPosition(View paramView)
  {
    setTop(paramView, getLayoutTop(paramView));
  }
  
  private static void setTop(View paramView, int paramInt)
  {
    paramView.setTagInternal(16909435, Integer.valueOf(paramInt));
    updateTopAndBottom(paramView);
  }
  
  public static void startLocalTranslationFrom(View paramView, int paramInt, Interpolator paramInterpolator)
  {
    startTopAnimation(paramView, getTop(paramView) + paramInt, getLayoutTop(paramView), paramInterpolator);
  }
  
  public static void startLocalTranslationTo(View paramView, int paramInt, Interpolator paramInterpolator)
  {
    int i = getTop(paramView);
    startTopAnimation(paramView, i, i + paramInt, paramInterpolator);
  }
  
  private static void startTopAnimation(View paramView, int paramInt1, int paramInt2, Interpolator paramInterpolator)
  {
    ObjectAnimator localObjectAnimator = (ObjectAnimator)paramView.getTag(16909434);
    if (localObjectAnimator != null) {
      localObjectAnimator.cancel();
    }
    if ((paramView.isShown()) && (paramInt1 != paramInt2) && ((!MessagingLinearLayout.isGone(paramView)) || (isHidingAnimated(paramView))))
    {
      localObjectAnimator = ObjectAnimator.ofInt(paramView, TOP, new int[] { paramInt1, paramInt2 });
      setTop(paramView, paramInt1);
      localObjectAnimator.setInterpolator(paramInterpolator);
      localObjectAnimator.setDuration(210L);
      localObjectAnimator.addListener(new AnimatorListenerAdapter()
      {
        public boolean mCancelled;
        
        public void onAnimationCancel(Animator paramAnonymousAnimator)
        {
          mCancelled = true;
        }
        
        public void onAnimationEnd(Animator paramAnonymousAnimator)
        {
          setTagInternal(16909434, null);
          MessagingPropertyAnimator.setClippingDeactivated(MessagingPropertyAnimator.this, false);
        }
      });
      setClippingDeactivated(paramView, true);
      paramView.setTagInternal(16909434, localObjectAnimator);
      localObjectAnimator.start();
      return;
    }
    setTop(paramView, paramInt2);
  }
  
  private static void updateLayerType(View paramView, boolean paramBoolean)
  {
    if ((paramView.hasOverlappingRendering()) && (paramBoolean)) {
      paramView.setLayerType(2, null);
    } else if (paramView.getLayerType() == 2) {
      paramView.setLayerType(0, null);
    }
  }
  
  private static void updateTopAndBottom(View paramView)
  {
    int i = getTop(paramView);
    int j = paramView.getHeight();
    paramView.setTop(i);
    paramView.setBottom(j + i);
  }
  
  public void onLayoutChange(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8)
  {
    setLayoutTop(paramView, paramInt2);
    if (isFirstLayout(paramView))
    {
      setFirstLayout(paramView, false);
      setTop(paramView, paramInt2);
      return;
    }
    startTopAnimation(paramView, getTop(paramView), paramInt2, MessagingLayout.FAST_OUT_SLOW_IN);
  }
}
