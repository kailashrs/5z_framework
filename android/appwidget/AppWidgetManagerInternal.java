package android.appwidget;

import android.util.ArraySet;

public abstract class AppWidgetManagerInternal
{
  public AppWidgetManagerInternal() {}
  
  public abstract ArraySet<String> getHostedWidgetPackages(int paramInt);
}
