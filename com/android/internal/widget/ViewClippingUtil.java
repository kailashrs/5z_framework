package com.android.internal.widget;

import android.util.ArraySet;
import android.view.View;
import android.view.ViewGroup;

public class ViewClippingUtil
{
  private static final int CLIP_CHILDREN_TAG = 16908859;
  private static final int CLIP_CLIPPING_SET = 16908858;
  private static final int CLIP_TO_PADDING = 16908861;
  
  public ViewClippingUtil() {}
  
  public static void setClippingDeactivated(View paramView, boolean paramBoolean, ClippingParameters paramClippingParameters)
  {
    if ((!paramBoolean) && (!paramClippingParameters.isClippingEnablingAllowed(paramView))) {
      return;
    }
    if (!(paramView.getParent() instanceof ViewGroup)) {
      return;
    }
    for (Object localObject1 = (ViewGroup)paramView.getParent();; localObject1 = (ViewGroup)localObject1)
    {
      if ((!paramBoolean) && (!paramClippingParameters.isClippingEnablingAllowed(paramView))) {
        return;
      }
      Object localObject2 = (ArraySet)((ViewGroup)localObject1).getTag(16908858);
      Object localObject3 = localObject2;
      if (localObject2 == null)
      {
        localObject3 = new ArraySet();
        ((ViewGroup)localObject1).setTagInternal(16908858, localObject3);
      }
      Object localObject4 = (Boolean)((ViewGroup)localObject1).getTag(16908859);
      localObject2 = localObject4;
      if (localObject4 == null)
      {
        localObject2 = Boolean.valueOf(((ViewGroup)localObject1).getClipChildren());
        ((ViewGroup)localObject1).setTagInternal(16908859, localObject2);
      }
      Boolean localBoolean = (Boolean)((ViewGroup)localObject1).getTag(16908861);
      localObject4 = localBoolean;
      if (localBoolean == null)
      {
        localObject4 = Boolean.valueOf(((ViewGroup)localObject1).getClipToPadding());
        ((ViewGroup)localObject1).setTagInternal(16908861, localObject4);
      }
      if (!paramBoolean)
      {
        ((ArraySet)localObject3).remove(paramView);
        if (((ArraySet)localObject3).isEmpty())
        {
          ((ViewGroup)localObject1).setClipChildren(((Boolean)localObject2).booleanValue());
          ((ViewGroup)localObject1).setClipToPadding(((Boolean)localObject4).booleanValue());
          ((ViewGroup)localObject1).setTagInternal(16908858, null);
          paramClippingParameters.onClippingStateChanged((View)localObject1, true);
        }
      }
      else
      {
        ((ArraySet)localObject3).add(paramView);
        ((ViewGroup)localObject1).setClipChildren(false);
        ((ViewGroup)localObject1).setClipToPadding(false);
        paramClippingParameters.onClippingStateChanged((View)localObject1, false);
      }
      if (paramClippingParameters.shouldFinish((View)localObject1)) {
        return;
      }
      localObject1 = ((ViewGroup)localObject1).getParent();
      if (!(localObject1 instanceof ViewGroup)) {
        break;
      }
    }
  }
  
  public static abstract interface ClippingParameters
  {
    public boolean isClippingEnablingAllowed(View paramView)
    {
      return MessagingPropertyAnimator.isAnimatingTranslation(paramView) ^ true;
    }
    
    public void onClippingStateChanged(View paramView, boolean paramBoolean) {}
    
    public abstract boolean shouldFinish(View paramView);
  }
}
