package com.android.internal.app;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.util.Log;

public class ActivityTrigger
{
  public static final int ANIMATION_SCALE = 3;
  private static final int FLAG_HARDWARE_ACCELERATED = 512;
  private static final int FLAG_OVERRIDE_RESOLUTION = 1;
  public static final int NETWORK_OPTS = 2;
  public static final int START_PROCESS = 1;
  private static final String TAG = "ActivityTrigger";
  
  public ActivityTrigger() {}
  
  private native void native_at_deinit();
  
  private native float native_at_miscActivity(int paramInt1, String paramString, int paramInt2, int paramInt3);
  
  private native void native_at_pauseActivity(String paramString);
  
  private native void native_at_resumeActivity(String paramString);
  
  private native int native_at_startActivity(String paramString, int paramInt);
  
  private native void native_at_stopActivity(String paramString);
  
  public float activityMiscTrigger(int paramInt1, String paramString, int paramInt2, int paramInt3)
  {
    return native_at_miscActivity(paramInt1, paramString, paramInt2, paramInt3);
  }
  
  public void activityPauseTrigger(Intent paramIntent, ActivityInfo paramActivityInfo, ApplicationInfo paramApplicationInfo)
  {
    ComponentName localComponentName = paramIntent.getComponent();
    paramActivityInfo = null;
    Log.d("ActivityTrigger", "ActivityTrigger activityPauseTrigger ");
    paramIntent = paramActivityInfo;
    if (localComponentName != null)
    {
      paramIntent = paramActivityInfo;
      if (paramApplicationInfo != null)
      {
        paramIntent = new StringBuilder();
        paramIntent.append(localComponentName.flattenToString());
        paramIntent.append("/");
        paramIntent.append(versionCode);
        paramIntent = paramIntent.toString();
      }
    }
    native_at_pauseActivity(paramIntent);
  }
  
  public void activityResumeTrigger(Intent paramIntent, ActivityInfo paramActivityInfo, ApplicationInfo paramApplicationInfo, boolean paramBoolean)
  {
    paramActivityInfo = paramIntent.getComponent();
    paramIntent = null;
    if (paramActivityInfo != null)
    {
      paramIntent = new StringBuilder();
      paramIntent.append(paramActivityInfo.flattenToString());
      paramIntent.append("/");
      paramIntent.append(versionCode);
      paramIntent = paramIntent.toString();
    }
    native_at_resumeActivity(paramIntent);
    if (paramBoolean)
    {
      paramActivityInfo = new StringBuilder();
      paramActivityInfo.append("activityResumeTrigger: The activity in ");
      paramActivityInfo.append(paramApplicationInfo);
      paramActivityInfo.append(" is now in focus and seems to be in full-screen mode");
      Log.d("ActivityTrigger", paramActivityInfo.toString());
      if (paramApplicationInfo.isAppWhiteListed())
      {
        paramActivityInfo = new StringBuilder();
        paramActivityInfo.append("activityResumeTrigger: whiteListed ");
        paramActivityInfo.append(paramIntent);
        paramActivityInfo.append(" appInfo.flags - ");
        paramActivityInfo.append(Integer.toHexString(flags));
        Log.d("ActivityTrigger", paramActivityInfo.toString());
        paramApplicationInfo.setAppOverrideDensity();
      }
      else
      {
        paramApplicationInfo.setOverrideDensity(0);
        paramActivityInfo = new StringBuilder();
        paramActivityInfo.append("activityResumeTrigger: not whiteListed");
        paramActivityInfo.append(paramIntent);
        Log.e("ActivityTrigger", paramActivityInfo.toString());
      }
    }
    else
    {
      paramIntent = new StringBuilder();
      paramIntent.append("activityResumeTrigger: Activity is not Triggerred in full screen ");
      paramIntent.append(paramApplicationInfo);
      Log.d("ActivityTrigger", paramIntent.toString());
      paramApplicationInfo.setOverrideDensity(0);
    }
  }
  
  public void activityStartTrigger(Intent paramIntent, ActivityInfo paramActivityInfo, ApplicationInfo paramApplicationInfo, boolean paramBoolean)
  {
    ComponentName localComponentName = paramIntent.getComponent();
    paramIntent = null;
    if (localComponentName != null)
    {
      paramIntent = new StringBuilder();
      paramIntent.append(localComponentName.flattenToString());
      paramIntent.append("/");
      paramIntent.append(versionCode);
      paramIntent = paramIntent.toString();
    }
    int i = native_at_startActivity(paramIntent, 0);
    if ((i & 0x200) != 0) {
      flags |= 0x200;
    }
    if (paramBoolean)
    {
      paramActivityInfo = new StringBuilder();
      paramActivityInfo.append("activityStartTrigger: Activity is Triggerred in full screen ");
      paramActivityInfo.append(paramApplicationInfo);
      Log.d("ActivityTrigger", paramActivityInfo.toString());
      if ((i & 0x1) != 0)
      {
        paramActivityInfo = new StringBuilder();
        paramActivityInfo.append("activityStartTrigger: whiteListed ");
        paramActivityInfo.append(paramIntent);
        paramActivityInfo.append(" appInfo.flags - ");
        paramActivityInfo.append(Integer.toHexString(flags));
        Log.e("ActivityTrigger", paramActivityInfo.toString());
        paramApplicationInfo.setAppOverrideDensity();
        paramApplicationInfo.setAppWhiteListed(1);
      }
      else
      {
        paramApplicationInfo.setOverrideDensity(0);
        paramApplicationInfo.setAppWhiteListed(0);
        paramActivityInfo = new StringBuilder();
        paramActivityInfo.append("activityStartTrigger: not whiteListed");
        paramActivityInfo.append(paramIntent);
        Log.e("ActivityTrigger", paramActivityInfo.toString());
      }
    }
    else
    {
      paramIntent = new StringBuilder();
      paramIntent.append("activityStartTrigger: Activity is not Triggerred in full screen ");
      paramIntent.append(paramApplicationInfo);
      Log.d("ActivityTrigger", paramIntent.toString());
      paramApplicationInfo.setOverrideDensity(0);
    }
  }
  
  public void activityStopTrigger(Intent paramIntent, ActivityInfo paramActivityInfo, ApplicationInfo paramApplicationInfo)
  {
    ComponentName localComponentName = paramIntent.getComponent();
    paramActivityInfo = null;
    Log.d("ActivityTrigger", "ActivityTrigger activityStopTrigger ");
    paramIntent = paramActivityInfo;
    if (localComponentName != null)
    {
      paramIntent = paramActivityInfo;
      if (paramApplicationInfo != null)
      {
        paramIntent = new StringBuilder();
        paramIntent.append(localComponentName.flattenToString());
        paramIntent.append("/");
        paramIntent.append(versionCode);
        paramIntent = paramIntent.toString();
      }
    }
    native_at_stopActivity(paramIntent);
  }
  
  protected void finalize()
  {
    native_at_deinit();
  }
}
