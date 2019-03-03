package com.android.internal.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RemoteViews.RemoteView;

@RemoteViews.RemoteView
public class NotificationExpandButton
  extends ImageView
{
  public NotificationExpandButton(Context paramContext)
  {
    super(paramContext);
  }
  
  public NotificationExpandButton(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public NotificationExpandButton(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public NotificationExpandButton(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
  }
  
  private void extendRectToMinTouchSize(Rect paramRect)
  {
    int i = (int)(getResourcesgetDisplayMetricsdensity * 48.0F);
    left = (paramRect.centerX() - i / 2);
    right = (left + i);
    top = (paramRect.centerY() - i / 2);
    bottom = (top + i);
  }
  
  public void getBoundsOnScreen(Rect paramRect, boolean paramBoolean)
  {
    super.getBoundsOnScreen(paramRect, paramBoolean);
    extendRectToMinTouchSize(paramRect);
  }
  
  public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo paramAccessibilityNodeInfo)
  {
    super.onInitializeAccessibilityNodeInfo(paramAccessibilityNodeInfo);
    paramAccessibilityNodeInfo.setClassName(Button.class.getName());
  }
}
