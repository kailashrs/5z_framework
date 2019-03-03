package com.android.internal.view;

import android.view.InputQueue.Callback;
import android.view.SurfaceHolder.Callback2;

public abstract interface RootViewSurfaceTaker
{
  public abstract void onRootViewScrollYChanged(int paramInt);
  
  public abstract void setSurfaceFormat(int paramInt);
  
  public abstract void setSurfaceKeepScreenOn(boolean paramBoolean);
  
  public abstract void setSurfaceType(int paramInt);
  
  public abstract InputQueue.Callback willYouTakeTheInputQueue();
  
  public abstract SurfaceHolder.Callback2 willYouTakeTheSurface();
}
