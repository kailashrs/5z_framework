package android.transition;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.Animator.AnimatorPauseListener;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;
import com.android.internal.R.styleable;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Map;

public abstract class Visibility
  extends Transition
{
  public static final int MODE_IN = 1;
  public static final int MODE_OUT = 2;
  private static final String PROPNAME_PARENT = "android:visibility:parent";
  private static final String PROPNAME_SCREEN_LOCATION = "android:visibility:screenLocation";
  static final String PROPNAME_VISIBILITY = "android:visibility:visibility";
  private static final String[] sTransitionProperties = { "android:visibility:visibility", "android:visibility:parent" };
  private int mMode = 3;
  private boolean mSuppressLayout = true;
  
  public Visibility() {}
  
  public Visibility(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    paramContext = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.VisibilityTransition);
    int i = paramContext.getInt(0, 0);
    paramContext.recycle();
    if (i != 0) {
      setMode(i);
    }
  }
  
  private void captureValues(TransitionValues paramTransitionValues)
  {
    int i = view.getVisibility();
    values.put("android:visibility:visibility", Integer.valueOf(i));
    values.put("android:visibility:parent", view.getParent());
    int[] arrayOfInt = new int[2];
    view.getLocationOnScreen(arrayOfInt);
    values.put("android:visibility:screenLocation", arrayOfInt);
  }
  
  private static VisibilityInfo getVisibilityChangeInfo(TransitionValues paramTransitionValues1, TransitionValues paramTransitionValues2)
  {
    VisibilityInfo localVisibilityInfo = new VisibilityInfo(null);
    visibilityChange = false;
    fadeIn = false;
    if ((paramTransitionValues1 != null) && (values.containsKey("android:visibility:visibility")))
    {
      startVisibility = ((Integer)values.get("android:visibility:visibility")).intValue();
      startParent = ((ViewGroup)values.get("android:visibility:parent"));
    }
    else
    {
      startVisibility = -1;
      startParent = null;
    }
    if ((paramTransitionValues2 != null) && (values.containsKey("android:visibility:visibility")))
    {
      endVisibility = ((Integer)values.get("android:visibility:visibility")).intValue();
      endParent = ((ViewGroup)values.get("android:visibility:parent"));
    }
    else
    {
      endVisibility = -1;
      endParent = null;
    }
    if ((paramTransitionValues1 != null) && (paramTransitionValues2 != null))
    {
      if ((startVisibility == endVisibility) && (startParent == endParent)) {
        return localVisibilityInfo;
      }
      if (startVisibility != endVisibility)
      {
        if (startVisibility == 0)
        {
          fadeIn = false;
          visibilityChange = true;
        }
        else if (endVisibility == 0)
        {
          fadeIn = true;
          visibilityChange = true;
        }
      }
      else if (startParent != endParent) {
        if (endParent == null)
        {
          fadeIn = false;
          visibilityChange = true;
        }
        else if (startParent == null)
        {
          fadeIn = true;
          visibilityChange = true;
        }
      }
    }
    else if ((paramTransitionValues1 == null) && (endVisibility == 0))
    {
      fadeIn = true;
      visibilityChange = true;
    }
    else if ((paramTransitionValues2 == null) && (startVisibility == 0))
    {
      fadeIn = false;
      visibilityChange = true;
    }
    return localVisibilityInfo;
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
    VisibilityInfo localVisibilityInfo = getVisibilityChangeInfo(paramTransitionValues1, paramTransitionValues2);
    if ((visibilityChange) && ((startParent != null) || (endParent != null)))
    {
      if (fadeIn) {
        return onAppear(paramViewGroup, paramTransitionValues1, startVisibility, paramTransitionValues2, endVisibility);
      }
      return onDisappear(paramViewGroup, paramTransitionValues1, startVisibility, paramTransitionValues2, endVisibility);
    }
    return null;
  }
  
  public int getMode()
  {
    return mMode;
  }
  
  public String[] getTransitionProperties()
  {
    return sTransitionProperties;
  }
  
  public boolean isTransitionRequired(TransitionValues paramTransitionValues1, TransitionValues paramTransitionValues2)
  {
    boolean bool1 = false;
    if ((paramTransitionValues1 == null) && (paramTransitionValues2 == null)) {
      return false;
    }
    if ((paramTransitionValues1 != null) && (paramTransitionValues2 != null) && (values.containsKey("android:visibility:visibility") != values.containsKey("android:visibility:visibility"))) {
      return false;
    }
    paramTransitionValues1 = getVisibilityChangeInfo(paramTransitionValues1, paramTransitionValues2);
    boolean bool2 = bool1;
    if (visibilityChange) {
      if (startVisibility != 0)
      {
        bool2 = bool1;
        if (endVisibility != 0) {}
      }
      else
      {
        bool2 = true;
      }
    }
    return bool2;
  }
  
  public boolean isVisible(TransitionValues paramTransitionValues)
  {
    boolean bool1 = false;
    if (paramTransitionValues == null) {
      return false;
    }
    int i = ((Integer)values.get("android:visibility:visibility")).intValue();
    paramTransitionValues = (View)values.get("android:visibility:parent");
    boolean bool2 = bool1;
    if (i == 0)
    {
      bool2 = bool1;
      if (paramTransitionValues != null) {
        bool2 = true;
      }
    }
    return bool2;
  }
  
  public Animator onAppear(ViewGroup paramViewGroup, TransitionValues paramTransitionValues1, int paramInt1, TransitionValues paramTransitionValues2, int paramInt2)
  {
    if (((mMode & 0x1) == 1) && (paramTransitionValues2 != null))
    {
      if (paramTransitionValues1 == null)
      {
        Object localObject = (View)view.getParent();
        TransitionValues localTransitionValues = getMatchedTransitionValues((View)localObject, false);
        localObject = getTransitionValues((View)localObject, false);
        if (getVisibilityChangeInfovisibilityChange) {
          return null;
        }
      }
      return onAppear(paramViewGroup, view, paramTransitionValues1, paramTransitionValues2);
    }
    return null;
  }
  
  public Animator onAppear(ViewGroup paramViewGroup, View paramView, TransitionValues paramTransitionValues1, TransitionValues paramTransitionValues2)
  {
    return null;
  }
  
  public Animator onDisappear(final ViewGroup paramViewGroup, TransitionValues paramTransitionValues1, int paramInt1, TransitionValues paramTransitionValues2, int paramInt2)
  {
    if ((mMode & 0x2) != 2) {
      return null;
    }
    Object localObject1;
    if (paramTransitionValues1 != null) {
      localObject1 = view;
    } else {
      localObject1 = null;
    }
    Object localObject2;
    if (paramTransitionValues2 != null) {
      localObject2 = view;
    } else {
      localObject2 = null;
    }
    Object localObject3 = null;
    Object localObject4 = null;
    Object localObject5;
    if ((localObject2 != null) && (((View)localObject2).getParent() != null))
    {
      if (paramInt2 == 4)
      {
        localObject5 = localObject2;
        localObject2 = localObject3;
      }
      else if (localObject1 == localObject2)
      {
        localObject5 = localObject2;
        localObject2 = localObject3;
      }
      else if (mCanRemoveViews)
      {
        localObject2 = localObject1;
        localObject5 = localObject4;
      }
      else
      {
        localObject2 = TransitionUtils.copyViewImage(paramViewGroup, (View)localObject1, (View)((View)localObject1).getParent());
        localObject5 = localObject4;
      }
    }
    else if (localObject2 != null)
    {
      localObject5 = localObject4;
    }
    else
    {
      localObject2 = localObject3;
      localObject5 = localObject4;
      if (localObject1 != null) {
        if (((View)localObject1).getParent() == null)
        {
          localObject2 = localObject1;
          localObject5 = localObject4;
        }
        else
        {
          localObject2 = localObject3;
          localObject5 = localObject4;
          if ((((View)localObject1).getParent() instanceof View))
          {
            View localView = (View)((View)localObject1).getParent();
            localObject5 = getTransitionValues(localView, true);
            localObject2 = getMatchedTransitionValues(localView, true);
            if (!getVisibilityChangeInfovisibilityChange)
            {
              localObject2 = TransitionUtils.copyViewImage(paramViewGroup, (View)localObject1, localView);
              localObject5 = localObject4;
            }
            else
            {
              localObject2 = localObject3;
              localObject5 = localObject4;
              if (localView.getParent() == null)
              {
                paramInt1 = localView.getId();
                localObject2 = localObject3;
                localObject5 = localObject4;
                if (paramInt1 != -1)
                {
                  localObject2 = localObject3;
                  localObject5 = localObject4;
                  if (paramViewGroup.findViewById(paramInt1) != null)
                  {
                    localObject2 = localObject3;
                    localObject5 = localObject4;
                    if (mCanRemoveViews)
                    {
                      localObject2 = localObject1;
                      localObject5 = localObject4;
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
    if (localObject2 != null)
    {
      localObject1 = (int[])values.get("android:visibility:screenLocation");
      paramInt1 = localObject1[0];
      paramInt2 = localObject1[1];
      localObject1 = new int[2];
      paramViewGroup.getLocationOnScreen((int[])localObject1);
      ((View)localObject2).offsetLeftAndRight(paramInt1 - localObject1[0] - ((View)localObject2).getLeft());
      ((View)localObject2).offsetTopAndBottom(paramInt2 - localObject1[1] - ((View)localObject2).getTop());
      paramViewGroup.getOverlay().add((View)localObject2);
      paramTransitionValues1 = onDisappear(paramViewGroup, (View)localObject2, paramTransitionValues1, paramTransitionValues2);
      if (paramTransitionValues1 == null) {
        paramViewGroup.getOverlay().remove((View)localObject2);
      } else {
        addListener(new TransitionListenerAdapter()
        {
          public void onTransitionEnd(Transition paramAnonymousTransition)
          {
            paramViewGroup.getOverlay().remove(val$finalOverlayView);
            paramAnonymousTransition.removeListener(this);
          }
        });
      }
      return paramTransitionValues1;
    }
    if (localObject5 != null)
    {
      paramInt1 = ((View)localObject5).getVisibility();
      ((View)localObject5).setTransitionVisibility(0);
      paramViewGroup = onDisappear(paramViewGroup, (View)localObject5, paramTransitionValues1, paramTransitionValues2);
      if (paramViewGroup != null)
      {
        paramTransitionValues1 = new DisappearListener((View)localObject5, paramInt2, mSuppressLayout);
        paramViewGroup.addListener(paramTransitionValues1);
        paramViewGroup.addPauseListener(paramTransitionValues1);
        addListener(paramTransitionValues1);
      }
      else
      {
        ((View)localObject5).setTransitionVisibility(paramInt1);
      }
      return paramViewGroup;
    }
    return null;
  }
  
  public Animator onDisappear(ViewGroup paramViewGroup, View paramView, TransitionValues paramTransitionValues1, TransitionValues paramTransitionValues2)
  {
    return null;
  }
  
  public void setMode(int paramInt)
  {
    if ((paramInt & 0xFFFFFFFC) == 0)
    {
      mMode = paramInt;
      return;
    }
    throw new IllegalArgumentException("Only MODE_IN and MODE_OUT flags are allowed");
  }
  
  public void setSuppressLayout(boolean paramBoolean)
  {
    mSuppressLayout = paramBoolean;
  }
  
  private static class DisappearListener
    extends TransitionListenerAdapter
    implements Animator.AnimatorListener, Animator.AnimatorPauseListener
  {
    boolean mCanceled = false;
    private final int mFinalVisibility;
    private boolean mLayoutSuppressed;
    private final ViewGroup mParent;
    private final boolean mSuppressLayout;
    private final View mView;
    
    public DisappearListener(View paramView, int paramInt, boolean paramBoolean)
    {
      mView = paramView;
      mFinalVisibility = paramInt;
      mParent = ((ViewGroup)paramView.getParent());
      mSuppressLayout = paramBoolean;
      suppressLayout(true);
    }
    
    private void hideViewWhenNotCanceled()
    {
      if (!mCanceled)
      {
        mView.setTransitionVisibility(mFinalVisibility);
        if (mParent != null) {
          mParent.invalidate();
        }
      }
      suppressLayout(false);
    }
    
    private void suppressLayout(boolean paramBoolean)
    {
      if ((mSuppressLayout) && (mLayoutSuppressed != paramBoolean) && (mParent != null))
      {
        mLayoutSuppressed = paramBoolean;
        mParent.suppressLayout(paramBoolean);
      }
    }
    
    public void onAnimationCancel(Animator paramAnimator)
    {
      mCanceled = true;
    }
    
    public void onAnimationEnd(Animator paramAnimator)
    {
      hideViewWhenNotCanceled();
    }
    
    public void onAnimationPause(Animator paramAnimator)
    {
      if (!mCanceled) {
        mView.setTransitionVisibility(mFinalVisibility);
      }
    }
    
    public void onAnimationRepeat(Animator paramAnimator) {}
    
    public void onAnimationResume(Animator paramAnimator)
    {
      if (!mCanceled) {
        mView.setTransitionVisibility(0);
      }
    }
    
    public void onAnimationStart(Animator paramAnimator) {}
    
    public void onTransitionEnd(Transition paramTransition)
    {
      hideViewWhenNotCanceled();
      paramTransition.removeListener(this);
    }
    
    public void onTransitionPause(Transition paramTransition)
    {
      suppressLayout(false);
    }
    
    public void onTransitionResume(Transition paramTransition)
    {
      suppressLayout(true);
    }
  }
  
  private static class VisibilityInfo
  {
    ViewGroup endParent;
    int endVisibility;
    boolean fadeIn;
    ViewGroup startParent;
    int startVisibility;
    boolean visibilityChange;
    
    private VisibilityInfo() {}
  }
  
  @Retention(RetentionPolicy.SOURCE)
  static @interface VisibilityMode {}
}
