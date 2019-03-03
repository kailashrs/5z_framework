package android.hardware.location;

import android.content.Context;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

public class ActivityRecognitionHardware
  extends IActivityRecognitionHardware.Stub
{
  private static final boolean DEBUG = Log.isLoggable("ActivityRecognitionHW", 3);
  private static final String ENFORCE_HW_PERMISSION_MESSAGE = "Permission 'android.permission.LOCATION_HARDWARE' not granted to access ActivityRecognitionHardware";
  private static final int EVENT_TYPE_COUNT = 3;
  private static final int EVENT_TYPE_DISABLED = 0;
  private static final int EVENT_TYPE_ENABLED = 1;
  private static final String HARDWARE_PERMISSION = "android.permission.LOCATION_HARDWARE";
  private static final int INVALID_ACTIVITY_TYPE = -1;
  private static final int NATIVE_SUCCESS_RESULT = 0;
  private static final String TAG = "ActivityRecognitionHW";
  private static ActivityRecognitionHardware sSingletonInstance;
  private static final Object sSingletonInstanceLock = new Object();
  private final Context mContext;
  private final SinkList mSinks = new SinkList(null);
  private final String[] mSupportedActivities;
  private final int mSupportedActivitiesCount;
  private final int[][] mSupportedActivitiesEnabledEvents;
  
  static
  {
    nativeClassInit();
  }
  
  private ActivityRecognitionHardware(Context paramContext)
  {
    nativeInitialize();
    mContext = paramContext;
    mSupportedActivities = fetchSupportedActivities();
    mSupportedActivitiesCount = mSupportedActivities.length;
    mSupportedActivitiesEnabledEvents = new int[mSupportedActivitiesCount][3];
  }
  
  private void checkPermissions()
  {
    mContext.enforceCallingPermission("android.permission.LOCATION_HARDWARE", "Permission 'android.permission.LOCATION_HARDWARE' not granted to access ActivityRecognitionHardware");
  }
  
  private String[] fetchSupportedActivities()
  {
    String[] arrayOfString = nativeGetSupportedActivities();
    if (arrayOfString != null) {
      return arrayOfString;
    }
    return new String[0];
  }
  
  private String getActivityName(int paramInt)
  {
    if ((paramInt >= 0) && (paramInt < mSupportedActivities.length)) {
      return mSupportedActivities[paramInt];
    }
    Log.e("ActivityRecognitionHW", String.format("Invalid ActivityType: %d, SupportedActivities: %d", new Object[] { Integer.valueOf(paramInt), Integer.valueOf(mSupportedActivities.length) }));
    return null;
  }
  
  private int getActivityType(String paramString)
  {
    if (TextUtils.isEmpty(paramString)) {
      return -1;
    }
    int i = mSupportedActivities.length;
    for (int j = 0; j < i; j++) {
      if (paramString.equals(mSupportedActivities[j])) {
        return j;
      }
    }
    return -1;
  }
  
  public static ActivityRecognitionHardware getInstance(Context paramContext)
  {
    synchronized (sSingletonInstanceLock)
    {
      if (sSingletonInstance == null)
      {
        ActivityRecognitionHardware localActivityRecognitionHardware = new android/hardware/location/ActivityRecognitionHardware;
        localActivityRecognitionHardware.<init>(paramContext);
        sSingletonInstance = localActivityRecognitionHardware;
      }
      paramContext = sSingletonInstance;
      return paramContext;
    }
  }
  
  public static boolean isSupported()
  {
    return nativeIsSupported();
  }
  
  private static native void nativeClassInit();
  
  private native int nativeDisableActivityEvent(int paramInt1, int paramInt2);
  
  private native int nativeEnableActivityEvent(int paramInt1, int paramInt2, long paramLong);
  
  private native int nativeFlush();
  
  private native String[] nativeGetSupportedActivities();
  
  private native void nativeInitialize();
  
  private static native boolean nativeIsSupported();
  
  private native void nativeRelease();
  
  private void onActivityChanged(Event[] paramArrayOfEvent)
  {
    if ((paramArrayOfEvent != null) && (paramArrayOfEvent.length != 0))
    {
      int i = paramArrayOfEvent.length;
      Object localObject = new ActivityRecognitionEvent[i];
      int j = 0;
      for (int k = 0; k < i; k++)
      {
        Event localEvent = paramArrayOfEvent[k];
        localObject[k] = new ActivityRecognitionEvent(getActivityName(activity), type, timestamp);
      }
      paramArrayOfEvent = new ActivityChangedEvent((ActivityRecognitionEvent[])localObject);
      i = mSinks.beginBroadcast();
      for (k = j; k < i; k++)
      {
        localObject = (IActivityRecognitionHardwareSink)mSinks.getBroadcastItem(k);
        try
        {
          ((IActivityRecognitionHardwareSink)localObject).onActivityChanged(paramArrayOfEvent);
        }
        catch (RemoteException localRemoteException)
        {
          Log.e("ActivityRecognitionHW", "Error delivering activity changed event.", localRemoteException);
        }
      }
      mSinks.finishBroadcast();
      return;
    }
    if (DEBUG) {
      Log.d("ActivityRecognitionHW", "No events to broadcast for onActivityChanged.");
    }
  }
  
  public boolean disableActivityEvent(String paramString, int paramInt)
  {
    checkPermissions();
    int i = getActivityType(paramString);
    if (i == -1) {
      return false;
    }
    if (nativeDisableActivityEvent(i, paramInt) == 0)
    {
      mSupportedActivitiesEnabledEvents[i][paramInt] = 0;
      return true;
    }
    return false;
  }
  
  public boolean enableActivityEvent(String paramString, int paramInt, long paramLong)
  {
    checkPermissions();
    int i = getActivityType(paramString);
    if (i == -1) {
      return false;
    }
    if (nativeEnableActivityEvent(i, paramInt, paramLong) == 0)
    {
      mSupportedActivitiesEnabledEvents[i][paramInt] = 1;
      return true;
    }
    return false;
  }
  
  public boolean flush()
  {
    checkPermissions();
    boolean bool;
    if (nativeFlush() == 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public String[] getSupportedActivities()
  {
    checkPermissions();
    return mSupportedActivities;
  }
  
  public boolean isActivitySupported(String paramString)
  {
    checkPermissions();
    boolean bool;
    if (getActivityType(paramString) != -1) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean registerSink(IActivityRecognitionHardwareSink paramIActivityRecognitionHardwareSink)
  {
    checkPermissions();
    return mSinks.register(paramIActivityRecognitionHardwareSink);
  }
  
  public boolean unregisterSink(IActivityRecognitionHardwareSink paramIActivityRecognitionHardwareSink)
  {
    checkPermissions();
    return mSinks.unregister(paramIActivityRecognitionHardwareSink);
  }
  
  private static class Event
  {
    public int activity;
    public long timestamp;
    public int type;
    
    private Event() {}
  }
  
  private class SinkList
    extends RemoteCallbackList<IActivityRecognitionHardwareSink>
  {
    private SinkList() {}
    
    private void disableActivityEventIfEnabled(int paramInt1, int paramInt2)
    {
      if (mSupportedActivitiesEnabledEvents[paramInt1][paramInt2] != 1) {
        return;
      }
      int i = ActivityRecognitionHardware.this.nativeDisableActivityEvent(paramInt1, paramInt2);
      mSupportedActivitiesEnabledEvents[paramInt1][paramInt2] = 0;
      Log.e("ActivityRecognitionHW", String.format("DisableActivityEvent: activityType=%d, eventType=%d, result=%d", new Object[] { Integer.valueOf(paramInt1), Integer.valueOf(paramInt2), Integer.valueOf(i) }));
    }
    
    public void onCallbackDied(IActivityRecognitionHardwareSink paramIActivityRecognitionHardwareSink)
    {
      int i = mSinks.getRegisteredCallbackCount();
      if (ActivityRecognitionHardware.DEBUG)
      {
        paramIActivityRecognitionHardwareSink = new StringBuilder();
        paramIActivityRecognitionHardwareSink.append("RegisteredCallbackCount: ");
        paramIActivityRecognitionHardwareSink.append(i);
        Log.d("ActivityRecognitionHW", paramIActivityRecognitionHardwareSink.toString());
      }
      if (i != 0) {
        return;
      }
      for (i = 0; i < mSupportedActivitiesCount; i++) {
        for (int j = 0; j < 3; j++) {
          disableActivityEventIfEnabled(i, j);
        }
      }
    }
  }
}
