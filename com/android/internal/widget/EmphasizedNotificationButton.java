package com.android.internal.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableWrapper;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.util.AttributeSet;
import android.view.RemotableViewMethod;
import android.widget.Button;
import android.widget.RemoteViews.RemoteView;

@RemoteViews.RemoteView
public class EmphasizedNotificationButton
  extends Button
{
  private final RippleDrawable mRipple = (RippleDrawable)((DrawableWrapper)getBackground().mutate()).getDrawable();
  private final int mStrokeColor = getContext().getColor(17170763);
  private final int mStrokeWidth = getResources().getDimensionPixelSize(17105158);
  
  public EmphasizedNotificationButton(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public EmphasizedNotificationButton(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public EmphasizedNotificationButton(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public EmphasizedNotificationButton(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
    mRipple.mutate();
  }
  
  @RemotableViewMethod
  public void setButtonBackground(ColorStateList paramColorStateList)
  {
    ((GradientDrawable)mRipple.getDrawable(0)).setColor(paramColorStateList);
    invalidate();
  }
  
  @RemotableViewMethod
  public void setHasStroke(boolean paramBoolean)
  {
    Object localObject = mRipple;
    int i = 0;
    localObject = (GradientDrawable)((RippleDrawable)localObject).getDrawable(0);
    if (paramBoolean) {
      i = mStrokeWidth;
    }
    ((GradientDrawable)localObject).setStroke(i, mStrokeColor);
    invalidate();
  }
  
  @RemotableViewMethod
  public void setRippleColor(ColorStateList paramColorStateList)
  {
    mRipple.setColor(paramColorStateList);
    invalidate();
  }
}
