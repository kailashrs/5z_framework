package com.android.internal.widget;

public abstract interface LockScreenWidgetInterface
{
  public abstract boolean providesClock();
  
  public abstract void setCallback(LockScreenWidgetCallback paramLockScreenWidgetCallback);
}
