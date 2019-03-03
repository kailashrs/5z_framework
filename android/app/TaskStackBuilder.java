package android.app;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.UserHandle;
import android.util.Log;
import java.util.ArrayList;

public class TaskStackBuilder
{
  private static final String TAG = "TaskStackBuilder";
  private final ArrayList<Intent> mIntents = new ArrayList();
  private final Context mSourceContext;
  
  private TaskStackBuilder(Context paramContext)
  {
    mSourceContext = paramContext;
  }
  
  public static TaskStackBuilder create(Context paramContext)
  {
    return new TaskStackBuilder(paramContext);
  }
  
  public TaskStackBuilder addNextIntent(Intent paramIntent)
  {
    mIntents.add(paramIntent);
    return this;
  }
  
  public TaskStackBuilder addNextIntentWithParentStack(Intent paramIntent)
  {
    ComponentName localComponentName1 = paramIntent.getComponent();
    ComponentName localComponentName2 = localComponentName1;
    if (localComponentName1 == null) {
      localComponentName2 = paramIntent.resolveActivity(mSourceContext.getPackageManager());
    }
    if (localComponentName2 != null) {
      addParentStack(localComponentName2);
    }
    addNextIntent(paramIntent);
    return this;
  }
  
  public TaskStackBuilder addParentStack(Activity paramActivity)
  {
    Intent localIntent = paramActivity.getParentActivityIntent();
    if (localIntent != null)
    {
      ComponentName localComponentName = localIntent.getComponent();
      paramActivity = localComponentName;
      if (localComponentName == null) {
        paramActivity = localIntent.resolveActivity(mSourceContext.getPackageManager());
      }
      addParentStack(paramActivity);
      addNextIntent(localIntent);
    }
    return this;
  }
  
  public TaskStackBuilder addParentStack(ComponentName paramComponentName)
  {
    int i = mIntents.size();
    PackageManager localPackageManager = mSourceContext.getPackageManager();
    try
    {
      paramComponentName = localPackageManager.getActivityInfo(paramComponentName, 0);
      String str = parentActivityName;
      while (str != null)
      {
        ComponentName localComponentName = new android/content/ComponentName;
        localComponentName.<init>(packageName, str);
        ActivityInfo localActivityInfo = localPackageManager.getActivityInfo(localComponentName, 0);
        str = parentActivityName;
        if ((str == null) && (i == 0))
        {
          paramComponentName = Intent.makeMainActivity(localComponentName);
        }
        else
        {
          paramComponentName = new android/content/Intent;
          paramComponentName.<init>();
          paramComponentName = paramComponentName.setComponent(localComponentName);
        }
        mIntents.add(i, paramComponentName);
        paramComponentName = localActivityInfo;
      }
      return this;
    }
    catch (PackageManager.NameNotFoundException paramComponentName)
    {
      Log.e("TaskStackBuilder", "Bad ComponentName while traversing activity parent metadata");
      throw new IllegalArgumentException(paramComponentName);
    }
  }
  
  public TaskStackBuilder addParentStack(Class<?> paramClass)
  {
    return addParentStack(new ComponentName(mSourceContext, paramClass));
  }
  
  public Intent editIntentAt(int paramInt)
  {
    return (Intent)mIntents.get(paramInt);
  }
  
  public int getIntentCount()
  {
    return mIntents.size();
  }
  
  public Intent[] getIntents()
  {
    Intent[] arrayOfIntent = new Intent[mIntents.size()];
    if (arrayOfIntent.length == 0) {
      return arrayOfIntent;
    }
    arrayOfIntent[0] = new Intent((Intent)mIntents.get(0)).addFlags(268484608);
    for (int i = 1; i < arrayOfIntent.length; i++) {
      arrayOfIntent[i] = new Intent((Intent)mIntents.get(i));
    }
    return arrayOfIntent;
  }
  
  public PendingIntent getPendingIntent(int paramInt1, int paramInt2)
  {
    return getPendingIntent(paramInt1, paramInt2, null);
  }
  
  public PendingIntent getPendingIntent(int paramInt1, int paramInt2, Bundle paramBundle)
  {
    if (!mIntents.isEmpty()) {
      return PendingIntent.getActivities(mSourceContext, paramInt1, getIntents(), paramInt2, paramBundle);
    }
    throw new IllegalStateException("No intents added to TaskStackBuilder; cannot getPendingIntent");
  }
  
  public PendingIntent getPendingIntent(int paramInt1, int paramInt2, Bundle paramBundle, UserHandle paramUserHandle)
  {
    if (!mIntents.isEmpty()) {
      return PendingIntent.getActivitiesAsUser(mSourceContext, paramInt1, getIntents(), paramInt2, paramBundle, paramUserHandle);
    }
    throw new IllegalStateException("No intents added to TaskStackBuilder; cannot getPendingIntent");
  }
  
  public int startActivities(Bundle paramBundle, UserHandle paramUserHandle)
  {
    if (!mIntents.isEmpty()) {
      return mSourceContext.startActivitiesAsUser(getIntents(), paramBundle, paramUserHandle);
    }
    throw new IllegalStateException("No intents added to TaskStackBuilder; cannot startActivities");
  }
  
  public void startActivities()
  {
    startActivities(null);
  }
  
  public void startActivities(Bundle paramBundle)
  {
    startActivities(paramBundle, mSourceContext.getUser());
  }
}
