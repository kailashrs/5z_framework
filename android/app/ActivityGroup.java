package android.app;

import android.content.Intent;
import android.os.Bundle;
import java.util.HashMap;

@Deprecated
public class ActivityGroup
  extends Activity
{
  static final String PARENT_NON_CONFIG_INSTANCE_KEY = "android:parent_non_config_instance";
  private static final String STATES_KEY = "android:states";
  protected LocalActivityManager mLocalActivityManager;
  
  public ActivityGroup()
  {
    this(true);
  }
  
  public ActivityGroup(boolean paramBoolean)
  {
    mLocalActivityManager = new LocalActivityManager(this, paramBoolean);
  }
  
  void dispatchActivityResult(String paramString1, int paramInt1, int paramInt2, Intent paramIntent, String paramString2)
  {
    if (paramString1 != null)
    {
      Activity localActivity = mLocalActivityManager.getActivity(paramString1);
      if (localActivity != null)
      {
        localActivity.onActivityResult(paramInt1, paramInt2, paramIntent);
        return;
      }
    }
    super.dispatchActivityResult(paramString1, paramInt1, paramInt2, paramIntent, paramString2);
  }
  
  public Activity getCurrentActivity()
  {
    return mLocalActivityManager.getCurrentActivity();
  }
  
  public final LocalActivityManager getLocalActivityManager()
  {
    return mLocalActivityManager;
  }
  
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (paramBundle != null) {
      paramBundle = paramBundle.getBundle("android:states");
    } else {
      paramBundle = null;
    }
    mLocalActivityManager.dispatchCreate(paramBundle);
  }
  
  protected void onDestroy()
  {
    super.onDestroy();
    mLocalActivityManager.dispatchDestroy(isFinishing());
  }
  
  protected void onPause()
  {
    super.onPause();
    mLocalActivityManager.dispatchPause(isFinishing());
  }
  
  protected void onResume()
  {
    super.onResume();
    mLocalActivityManager.dispatchResume();
  }
  
  public HashMap<String, Object> onRetainNonConfigurationChildInstances()
  {
    return mLocalActivityManager.dispatchRetainNonConfigurationInstance();
  }
  
  protected void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    Bundle localBundle = mLocalActivityManager.saveInstanceState();
    if (localBundle != null) {
      paramBundle.putBundle("android:states", localBundle);
    }
  }
  
  protected void onStop()
  {
    super.onStop();
    mLocalActivityManager.dispatchStop();
  }
}
