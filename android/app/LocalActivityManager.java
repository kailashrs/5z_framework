package android.app;

import android.app.servertransaction.PendingTransactionActions;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Window;
import com.android.internal.content.ReferrerIntent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@Deprecated
public class LocalActivityManager
{
  static final int CREATED = 2;
  static final int DESTROYED = 5;
  static final int INITIALIZING = 1;
  static final int RESTORED = 0;
  static final int RESUMED = 4;
  static final int STARTED = 3;
  private static final String TAG = "LocalActivityManager";
  private static final boolean localLOGV = false;
  private final Map<String, LocalActivityRecord> mActivities = new HashMap();
  private final ArrayList<LocalActivityRecord> mActivityArray = new ArrayList();
  private final ActivityThread mActivityThread = ActivityThread.currentActivityThread();
  private int mCurState = 1;
  private boolean mFinishing;
  private final Activity mParent;
  private LocalActivityRecord mResumed;
  private boolean mSingleMode;
  
  public LocalActivityManager(Activity paramActivity, boolean paramBoolean)
  {
    mParent = paramActivity;
    mSingleMode = paramBoolean;
  }
  
  private void moveToState(LocalActivityRecord paramLocalActivityRecord, int paramInt)
  {
    if ((curState != 0) && (curState != 5))
    {
      int i = curState;
      Object localObject1 = null;
      if (i == 1)
      {
        Object localObject2 = mParent.getLastNonConfigurationChildInstances();
        Object localObject3 = null;
        if (localObject2 != null) {
          localObject3 = ((HashMap)localObject2).get(id);
        }
        localObject2 = null;
        if (localObject3 != null)
        {
          localObject2 = new Activity.NonConfigurationInstances();
          activity = localObject3;
        }
        if (activityInfo == null) {
          activityInfo = mActivityThread.resolveActivityInfo(intent);
        }
        activity = mActivityThread.startActivityNow(mParent, id, intent, activityInfo, paramLocalActivityRecord, instanceState, (Activity.NonConfigurationInstances)localObject2);
        if (activity == null) {
          return;
        }
        window = activity.getWindow();
        instanceState = null;
        localObject2 = mActivityThread.getActivityClient(paramLocalActivityRecord);
        if (!activity.mFinished)
        {
          localObject3 = new PendingTransactionActions();
          ((PendingTransactionActions)localObject3).setOldState(state);
          ((PendingTransactionActions)localObject3).setRestoreInstanceState(true);
          ((PendingTransactionActions)localObject3).setCallOnPostCreate(true);
        }
        else
        {
          localObject3 = localObject1;
        }
        mActivityThread.handleStartActivity((ActivityThread.ActivityClientRecord)localObject2, (PendingTransactionActions)localObject3);
        curState = 3;
        if (paramInt == 4)
        {
          mActivityThread.performResumeActivity(paramLocalActivityRecord, true, "moveToState-INITIALIZING");
          curState = 4;
        }
        return;
      }
      switch (curState)
      {
      default: 
        return;
      case 4: 
        if (paramInt == 3)
        {
          performPause(paramLocalActivityRecord, mFinishing);
          curState = 3;
        }
        if (paramInt == 2)
        {
          performPause(paramLocalActivityRecord, mFinishing);
          mActivityThread.performStopActivity(paramLocalActivityRecord, false, "moveToState-RESUMED");
          curState = 2;
        }
        return;
      case 3: 
        if (paramInt == 4)
        {
          mActivityThread.performResumeActivity(paramLocalActivityRecord, true, "moveToState-STARTED");
          instanceState = null;
          curState = 4;
        }
        if (paramInt == 2)
        {
          mActivityThread.performStopActivity(paramLocalActivityRecord, false, "moveToState-STARTED");
          curState = 2;
        }
        return;
      }
      if (paramInt == 3)
      {
        mActivityThread.performRestartActivity(paramLocalActivityRecord, true);
        curState = 3;
      }
      if (paramInt == 4)
      {
        mActivityThread.performRestartActivity(paramLocalActivityRecord, true);
        mActivityThread.performResumeActivity(paramLocalActivityRecord, true, "moveToState-CREATED");
        curState = 4;
      }
      return;
    }
  }
  
  private Window performDestroy(LocalActivityRecord paramLocalActivityRecord, boolean paramBoolean)
  {
    Window localWindow = window;
    if ((curState == 4) && (!paramBoolean)) {
      performPause(paramLocalActivityRecord, paramBoolean);
    }
    mActivityThread.performDestroyActivity(paramLocalActivityRecord, paramBoolean, 0, false, "LocalActivityManager::performDestroy");
    activity = null;
    window = null;
    if (paramBoolean) {
      instanceState = null;
    }
    curState = 5;
    return localWindow;
  }
  
  private void performPause(LocalActivityRecord paramLocalActivityRecord, boolean paramBoolean)
  {
    int i;
    if (instanceState == null) {
      i = 1;
    } else {
      i = 0;
    }
    Bundle localBundle = mActivityThread.performPauseActivity(paramLocalActivityRecord, paramBoolean, "performPause", null);
    if (i != 0) {
      instanceState = localBundle;
    }
  }
  
  public Window destroyActivity(String paramString, boolean paramBoolean)
  {
    LocalActivityRecord localLocalActivityRecord = (LocalActivityRecord)mActivities.get(paramString);
    Object localObject = null;
    if (localLocalActivityRecord != null)
    {
      Window localWindow = performDestroy(localLocalActivityRecord, paramBoolean);
      localObject = localWindow;
      if (paramBoolean)
      {
        mActivities.remove(paramString);
        mActivityArray.remove(localLocalActivityRecord);
        localObject = localWindow;
      }
    }
    return localObject;
  }
  
  public void dispatchCreate(Bundle paramBundle)
  {
    if (paramBundle != null)
    {
      Iterator localIterator = paramBundle.keySet().iterator();
      while (localIterator.hasNext())
      {
        String str = (String)localIterator.next();
        try
        {
          Bundle localBundle = paramBundle.getBundle(str);
          LocalActivityRecord localLocalActivityRecord = (LocalActivityRecord)mActivities.get(str);
          if (localLocalActivityRecord != null)
          {
            instanceState = localBundle;
          }
          else
          {
            localLocalActivityRecord = new android/app/LocalActivityManager$LocalActivityRecord;
            localLocalActivityRecord.<init>(str, null);
            instanceState = localBundle;
            mActivities.put(str, localLocalActivityRecord);
            mActivityArray.add(localLocalActivityRecord);
          }
        }
        catch (Exception localException)
        {
          Log.e("LocalActivityManager", "Exception thrown when restoring LocalActivityManager state", localException);
        }
      }
    }
    mCurState = 2;
  }
  
  public void dispatchDestroy(boolean paramBoolean)
  {
    int i = mActivityArray.size();
    for (int j = 0; j < i; j++)
    {
      LocalActivityRecord localLocalActivityRecord = (LocalActivityRecord)mActivityArray.get(j);
      mActivityThread.performDestroyActivity(localLocalActivityRecord, paramBoolean, 0, false, "LocalActivityManager::dispatchDestroy");
    }
    mActivities.clear();
    mActivityArray.clear();
  }
  
  public void dispatchPause(boolean paramBoolean)
  {
    if (paramBoolean) {
      mFinishing = true;
    }
    mCurState = 3;
    if (mSingleMode)
    {
      if (mResumed != null) {
        moveToState(mResumed, 3);
      }
    }
    else
    {
      int i = mActivityArray.size();
      for (int j = 0; j < i; j++)
      {
        LocalActivityRecord localLocalActivityRecord = (LocalActivityRecord)mActivityArray.get(j);
        if (curState == 4) {
          moveToState(localLocalActivityRecord, 3);
        }
      }
    }
  }
  
  public void dispatchResume()
  {
    mCurState = 4;
    if (mSingleMode)
    {
      if (mResumed != null) {
        moveToState(mResumed, 4);
      }
    }
    else
    {
      int i = mActivityArray.size();
      for (int j = 0; j < i; j++) {
        moveToState((LocalActivityRecord)mActivityArray.get(j), 4);
      }
    }
  }
  
  public HashMap<String, Object> dispatchRetainNonConfigurationInstance()
  {
    Object localObject1 = null;
    int i = mActivityArray.size();
    int j = 0;
    while (j < i)
    {
      LocalActivityRecord localLocalActivityRecord = (LocalActivityRecord)mActivityArray.get(j);
      Object localObject2 = localObject1;
      if (localLocalActivityRecord != null)
      {
        localObject2 = localObject1;
        if (activity != null)
        {
          Object localObject3 = activity.onRetainNonConfigurationInstance();
          localObject2 = localObject1;
          if (localObject3 != null)
          {
            localObject2 = localObject1;
            if (localObject1 == null) {
              localObject2 = new HashMap();
            }
            ((HashMap)localObject2).put(id, localObject3);
          }
        }
      }
      j++;
      localObject1 = localObject2;
    }
    return localObject1;
  }
  
  public void dispatchStop()
  {
    mCurState = 2;
    int i = mActivityArray.size();
    for (int j = 0; j < i; j++) {
      moveToState((LocalActivityRecord)mActivityArray.get(j), 2);
    }
  }
  
  public Activity getActivity(String paramString)
  {
    paramString = (LocalActivityRecord)mActivities.get(paramString);
    if (paramString != null) {
      paramString = activity;
    } else {
      paramString = null;
    }
    return paramString;
  }
  
  public Activity getCurrentActivity()
  {
    Activity localActivity;
    if (mResumed != null) {
      localActivity = mResumed.activity;
    } else {
      localActivity = null;
    }
    return localActivity;
  }
  
  public String getCurrentId()
  {
    String str;
    if (mResumed != null) {
      str = mResumed.id;
    } else {
      str = null;
    }
    return str;
  }
  
  public void removeAllActivities()
  {
    dispatchDestroy(true);
  }
  
  public Bundle saveInstanceState()
  {
    Object localObject1 = null;
    int i = mActivityArray.size();
    int j = 0;
    while (j < i)
    {
      LocalActivityRecord localLocalActivityRecord = (LocalActivityRecord)mActivityArray.get(j);
      Object localObject2 = localObject1;
      if (localObject1 == null) {
        localObject2 = new Bundle();
      }
      if (((instanceState != null) || (curState == 4)) && (activity != null))
      {
        localObject1 = new Bundle();
        activity.performSaveInstanceState((Bundle)localObject1);
        instanceState = ((Bundle)localObject1);
      }
      if (instanceState != null) {
        ((Bundle)localObject2).putBundle(id, instanceState);
      }
      j++;
      localObject1 = localObject2;
    }
    return localObject1;
  }
  
  public Window startActivity(String paramString, Intent paramIntent)
  {
    if (mCurState != 1)
    {
      int i = 0;
      int j = 0;
      Object localObject1 = null;
      Object localObject2 = (LocalActivityRecord)mActivities.get(paramString);
      Object localObject3;
      int k;
      Object localObject4;
      if (localObject2 == null)
      {
        localObject3 = new LocalActivityRecord(paramString, paramIntent);
        k = 1;
        localObject4 = localObject1;
      }
      else
      {
        k = i;
        localObject4 = localObject1;
        localObject3 = localObject2;
        if (intent != null)
        {
          boolean bool = intent.filterEquals(paramIntent);
          k = i;
          j = bool;
          localObject4 = localObject1;
          localObject3 = localObject2;
          if (bool)
          {
            localObject4 = activityInfo;
            localObject3 = localObject2;
            j = bool;
            k = i;
          }
        }
      }
      localObject2 = localObject4;
      if (localObject4 == null) {
        localObject2 = mActivityThread.resolveActivityInfo(paramIntent);
      }
      if (mSingleMode)
      {
        localObject4 = mResumed;
        if ((localObject4 != null) && (localObject4 != localObject3) && (mCurState == 4)) {
          moveToState((LocalActivityRecord)localObject4, 3);
        }
      }
      if (k != 0)
      {
        mActivities.put(paramString, localObject3);
        mActivityArray.add(localObject3);
      }
      else if (activityInfo != null)
      {
        if ((localObject2 == activityInfo) || ((name.equals(activityInfo.name)) && (packageName.equals(activityInfo.packageName))))
        {
          if ((launchMode != 0) || ((paramIntent.getFlags() & 0x20000000) != 0)) {
            break label355;
          }
          if ((j != 0) && ((paramIntent.getFlags() & 0x4000000) == 0))
          {
            intent = paramIntent;
            moveToState((LocalActivityRecord)localObject3, mCurState);
            if (mSingleMode) {
              mResumed = ((LocalActivityRecord)localObject3);
            }
            return window;
          }
        }
        performDestroy((LocalActivityRecord)localObject3, true);
        break label430;
        label355:
        paramString = new ArrayList(1);
        paramString.add(new ReferrerIntent(paramIntent, mParent.getPackageName()));
        mActivityThread.performNewIntents((IBinder)localObject3, paramString, false);
        intent = paramIntent;
        moveToState((LocalActivityRecord)localObject3, mCurState);
        if (mSingleMode) {
          mResumed = ((LocalActivityRecord)localObject3);
        }
        return window;
      }
      label430:
      intent = paramIntent;
      curState = 1;
      activityInfo = ((ActivityInfo)localObject2);
      moveToState((LocalActivityRecord)localObject3, mCurState);
      if (mSingleMode) {
        mResumed = ((LocalActivityRecord)localObject3);
      }
      return window;
    }
    throw new IllegalStateException("Activities can't be added until the containing group has been created.");
  }
  
  private static class LocalActivityRecord
    extends Binder
  {
    Activity activity;
    ActivityInfo activityInfo;
    int curState = 0;
    final String id;
    Bundle instanceState;
    Intent intent;
    Window window;
    
    LocalActivityRecord(String paramString, Intent paramIntent)
    {
      id = paramString;
      intent = paramIntent;
    }
  }
}
