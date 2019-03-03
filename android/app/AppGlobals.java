package android.app;

import android.content.pm.IPackageManager;

public class AppGlobals
{
  public AppGlobals() {}
  
  public static Application getInitialApplication()
  {
    return ActivityThread.currentApplication();
  }
  
  public static String getInitialPackage()
  {
    return ActivityThread.currentPackageName();
  }
  
  public static int getIntCoreSetting(String paramString, int paramInt)
  {
    ActivityThread localActivityThread = ActivityThread.currentActivityThread();
    if (localActivityThread != null) {
      return localActivityThread.getIntCoreSetting(paramString, paramInt);
    }
    return paramInt;
  }
  
  public static IPackageManager getPackageManager()
  {
    return ActivityThread.getPackageManager();
  }
}
