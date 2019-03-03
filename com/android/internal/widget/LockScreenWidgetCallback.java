package com.android.internal.widget;

import android.view.View;

public abstract interface LockScreenWidgetCallback
{
  public abstract boolean isVisible(View paramView);
  
  public abstract void requestHide(View paramView);
  
  public abstract void requestShow(View paramView);
  
  public abstract void userActivity(View paramView);
}
