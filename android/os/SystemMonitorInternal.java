package android.os;

import android.content.Intent;

public abstract class SystemMonitorInternal
{
  public SystemMonitorInternal() {}
  
  public abstract String getFocusAppClassName();
  
  public abstract String getFocusAppPackageName();
  
  public abstract void updateApiListener(ApiCallerMessage paramApiCallerMessage);
  
  public abstract void updateFocusApp(AppFocusedMessage paramAppFocusedMessage);
  
  public static class ApiCallerMessage
  {
    private String mAppPackageName = null;
    private String mCallingApi = null;
    
    public ApiCallerMessage() {}
    
    public ApiCallerMessage(Bundle paramBundle)
    {
      String str = paramBundle.getString("appPackageName");
      if (str != null) {
        mAppPackageName = new String(str);
      }
      paramBundle = paramBundle.getString("callingApi");
      if (paramBundle != null) {
        mCallingApi = new String(paramBundle);
      }
    }
    
    public void setAppPackageName(String paramString)
    {
      if (paramString != null) {
        mAppPackageName = new String(paramString);
      }
    }
    
    public void setCallingApi(String paramString)
    {
      if (paramString != null) {
        mCallingApi = new String(paramString);
      }
    }
    
    public Bundle toBundle()
    {
      Bundle localBundle = new Bundle();
      localBundle.putString("appPackageName", mAppPackageName);
      localBundle.putString("callingApi", mCallingApi);
      return localBundle;
    }
  }
  
  public static class AppFocusedMessage
  {
    private String mAppName = null;
    private int mAppTaskId = 0;
    private String mDisplayedTime = null;
    private Intent mIntent = null;
    private String mLaunchedFromPackage = null;
    private String mReason = null;
    
    public AppFocusedMessage() {}
    
    public AppFocusedMessage(Bundle paramBundle)
    {
      Object localObject = paramBundle.getString("focusApp");
      if (localObject != null) {
        mAppName = new String((String)localObject);
      }
      mAppTaskId = paramBundle.getInt("focusAppTaskId");
      localObject = (Intent)paramBundle.getParcelable("launchIntent");
      if (localObject != null) {
        mIntent = new Intent((Intent)localObject);
      }
      localObject = paramBundle.getString("displayedTime");
      if (localObject != null) {
        mDisplayedTime = new String((String)localObject);
      }
      localObject = paramBundle.getString("reason");
      if (localObject != null) {
        mReason = new String((String)localObject);
      }
      paramBundle = paramBundle.getString("launchedFromPackage");
      if (paramBundle != null) {
        mLaunchedFromPackage = new String(paramBundle);
      }
    }
    
    public void setAppTaskId(int paramInt)
    {
      mAppTaskId = paramInt;
    }
    
    public void setDisplayedTime(String paramString)
    {
      if (paramString != null) {
        mDisplayedTime = new String(paramString);
      }
    }
    
    public void setFocusedApp(String paramString)
    {
      if (paramString != null) {
        mAppName = new String(paramString);
      }
    }
    
    public void setIntent(Intent paramIntent)
    {
      if (paramIntent != null) {
        mIntent = new Intent(paramIntent);
      }
    }
    
    public void setLaunchedFromPackage(String paramString)
    {
      if (paramString != null) {
        mLaunchedFromPackage = new String(paramString);
      }
    }
    
    public void setReason(String paramString)
    {
      if (paramString != null) {
        mReason = new String(paramString);
      }
    }
    
    public Bundle toBundle()
    {
      Bundle localBundle = new Bundle();
      localBundle.putString("focusApp", mAppName);
      localBundle.putInt("focusAppTaskId", mAppTaskId);
      localBundle.putParcelable("launchIntent", mIntent);
      localBundle.putString("displayedTime", mDisplayedTime);
      localBundle.putString("reason", mReason);
      localBundle.putString("launchedFromPackage", mLaunchedFromPackage);
      return localBundle;
    }
  }
}
