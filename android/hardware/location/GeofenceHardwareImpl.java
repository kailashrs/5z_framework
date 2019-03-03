package android.hardware.location;

import android.content.Context;
import android.location.IFusedGeofenceHardware;
import android.location.IGpsGeofenceHardware;
import android.location.Location;
import android.os.Handler;
import android.os.IBinder;
import android.os.IBinder.DeathRecipient;
import android.os.IInterface;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.RemoteException;
import android.util.Log;
import android.util.SparseArray;
import java.util.ArrayList;
import java.util.Iterator;

public final class GeofenceHardwareImpl
{
  private static final int ADD_GEOFENCE_CALLBACK = 2;
  private static final int CALLBACK_ADD = 2;
  private static final int CALLBACK_REMOVE = 3;
  private static final int CAPABILITY_GNSS = 1;
  private static final boolean DEBUG = Log.isLoggable("GeofenceHardwareImpl", 3);
  private static final int FIRST_VERSION_WITH_CAPABILITIES = 2;
  private static final int GEOFENCE_CALLBACK_BINDER_DIED = 6;
  private static final int GEOFENCE_STATUS = 1;
  private static final int GEOFENCE_TRANSITION_CALLBACK = 1;
  private static final int LOCATION_HAS_ACCURACY = 16;
  private static final int LOCATION_HAS_ALTITUDE = 2;
  private static final int LOCATION_HAS_BEARING = 8;
  private static final int LOCATION_HAS_LAT_LONG = 1;
  private static final int LOCATION_HAS_SPEED = 4;
  private static final int LOCATION_INVALID = 0;
  private static final int MONITOR_CALLBACK_BINDER_DIED = 4;
  private static final int PAUSE_GEOFENCE_CALLBACK = 4;
  private static final int REAPER_GEOFENCE_ADDED = 1;
  private static final int REAPER_MONITOR_CALLBACK_ADDED = 2;
  private static final int REAPER_REMOVED = 3;
  private static final int REMOVE_GEOFENCE_CALLBACK = 3;
  private static final int RESOLUTION_LEVEL_COARSE = 2;
  private static final int RESOLUTION_LEVEL_FINE = 3;
  private static final int RESOLUTION_LEVEL_NONE = 1;
  private static final int RESUME_GEOFENCE_CALLBACK = 5;
  private static final String TAG = "GeofenceHardwareImpl";
  private static GeofenceHardwareImpl sInstance;
  private final ArrayList<IGeofenceHardwareMonitorCallback>[] mCallbacks = new ArrayList[2];
  private Handler mCallbacksHandler = new Handler()
  {
    public void handleMessage(Message paramAnonymousMessage)
    {
      Object localObject1;
      Object localObject2;
      int i;
      switch (what)
      {
      default: 
        break;
      case 4: 
        localObject1 = (IGeofenceHardwareMonitorCallback)obj;
        if (GeofenceHardwareImpl.DEBUG)
        {
          localObject2 = new StringBuilder();
          ((StringBuilder)localObject2).append("Monitor callback reaped:");
          ((StringBuilder)localObject2).append(localObject1);
          Log.d("GeofenceHardwareImpl", ((StringBuilder)localObject2).toString());
        }
        paramAnonymousMessage = mCallbacks[arg1];
        if ((paramAnonymousMessage != null) && (paramAnonymousMessage.contains(localObject1))) {
          paramAnonymousMessage.remove(localObject1);
        }
        break;
      case 3: 
        i = arg1;
        localObject1 = (IGeofenceHardwareMonitorCallback)obj;
        paramAnonymousMessage = mCallbacks[i];
        if (paramAnonymousMessage != null) {
          paramAnonymousMessage.remove(localObject1);
        }
        break;
      case 2: 
        i = arg1;
        localObject2 = (IGeofenceHardwareMonitorCallback)obj;
        localObject1 = mCallbacks[i];
        paramAnonymousMessage = (Message)localObject1;
        if (localObject1 == null)
        {
          paramAnonymousMessage = new ArrayList();
          mCallbacks[i] = paramAnonymousMessage;
        }
        if (!paramAnonymousMessage.contains(localObject2)) {
          paramAnonymousMessage.add(localObject2);
        }
        break;
      case 1: 
        paramAnonymousMessage = (GeofenceHardwareMonitorEvent)obj;
        localObject2 = mCallbacks[paramAnonymousMessage.getMonitoringType()];
        if (localObject2 != null)
        {
          if (GeofenceHardwareImpl.DEBUG)
          {
            localObject1 = new StringBuilder();
            ((StringBuilder)localObject1).append("MonitoringSystemChangeCallback: ");
            ((StringBuilder)localObject1).append(paramAnonymousMessage);
            Log.d("GeofenceHardwareImpl", ((StringBuilder)localObject1).toString());
          }
          localObject1 = ((ArrayList)localObject2).iterator();
          while (((Iterator)localObject1).hasNext())
          {
            localObject2 = (IGeofenceHardwareMonitorCallback)((Iterator)localObject1).next();
            try
            {
              ((IGeofenceHardwareMonitorCallback)localObject2).onMonitoringSystemChange(paramAnonymousMessage);
            }
            catch (RemoteException localRemoteException)
            {
              Log.d("GeofenceHardwareImpl", "Error reporting onMonitoringSystemChange.", localRemoteException);
            }
          }
        }
        GeofenceHardwareImpl.this.releaseWakeLock();
      }
    }
  };
  private int mCapabilities;
  private final Context mContext;
  private IFusedGeofenceHardware mFusedService;
  private Handler mGeofenceHandler = new Handler()
  {
    public void handleMessage(Message arg1)
    {
      int i = what;
      int j = 0;
      Object localObject4;
      int k;
      switch (i)
      {
      default: 
        break;
      case 6: 
        IGeofenceHardwareCallback localIGeofenceHardwareCallback = (IGeofenceHardwareCallback)obj;
        if (GeofenceHardwareImpl.DEBUG)
        {
          localObject4 = new StringBuilder();
          ((StringBuilder)localObject4).append("Geofence callback reaped:");
          ((StringBuilder)localObject4).append(localIGeofenceHardwareCallback);
          Log.d("GeofenceHardwareImpl", ((StringBuilder)localObject4).toString());
        }
        i = arg1;
        synchronized (mGeofences)
        {
          while (j < mGeofences.size())
          {
            if (((IGeofenceHardwareCallback)mGeofences.valueAt(j)).equals(localIGeofenceHardwareCallback))
            {
              k = mGeofences.keyAt(j);
              removeGeofence(mGeofences.keyAt(j), i);
              mGeofences.remove(k);
            }
            j++;
          }
        }
      case 5: 
        j = arg1;
        synchronized (mGeofences)
        {
          localObject4 = (IGeofenceHardwareCallback)mGeofences.get(j);
          if (localObject4 != null) {
            try
            {
              ((IGeofenceHardwareCallback)localObject4).onGeofenceResume(j, arg2);
            }
            catch (RemoteException ???) {}
          }
          GeofenceHardwareImpl.this.releaseWakeLock();
        }
      case 4: 
        j = arg1;
        synchronized (mGeofences)
        {
          localObject4 = (IGeofenceHardwareCallback)mGeofences.get(j);
          if (localObject4 != null) {
            try
            {
              ((IGeofenceHardwareCallback)localObject4).onGeofencePause(j, arg2);
            }
            catch (RemoteException ???) {}
          }
          GeofenceHardwareImpl.this.releaseWakeLock();
        }
      case 3: 
        j = arg1;
        synchronized (mGeofences)
        {
          localObject4 = (IGeofenceHardwareCallback)mGeofences.get(j);
          if (localObject4 != null)
          {
            try
            {
              ((IGeofenceHardwareCallback)localObject4).onGeofenceRemove(j, arg2);
            }
            catch (RemoteException ???) {}
            ??? = ((IGeofenceHardwareCallback)localObject4).asBinder();
            k = 0;
            synchronized (mGeofences)
            {
              mGeofences.remove(j);
              for (j = 0;; j++)
              {
                i = k;
                if (j >= mGeofences.size()) {
                  break;
                }
                if (((IGeofenceHardwareCallback)mGeofences.valueAt(j)).asBinder() == ???)
                {
                  i = 1;
                  break;
                }
              }
              if (i == 0)
              {
                ??? = mReapers.iterator();
                if (((Iterator)???).hasNext())
                {
                  localObject4 = (GeofenceHardwareImpl.Reaper)((Iterator)???).next();
                  if ((GeofenceHardwareImpl.Reaper.access$300((GeofenceHardwareImpl.Reaper)localObject4) != null) && (GeofenceHardwareImpl.Reaper.access$300((GeofenceHardwareImpl.Reaper)localObject4).asBinder() == ???))
                  {
                    ((Iterator)???).remove();
                    GeofenceHardwareImpl.Reaper.access$400((GeofenceHardwareImpl.Reaper)localObject4);
                    if (GeofenceHardwareImpl.DEBUG) {
                      Log.d("GeofenceHardwareImpl", String.format("Removed reaper %s because binder %s is no longer needed.", new Object[] { localObject4, ??? }));
                    }
                  }
                }
              }
            }
          }
          GeofenceHardwareImpl.this.releaseWakeLock();
        }
      case 2: 
        j = arg1;
        synchronized (mGeofences)
        {
          localObject4 = (IGeofenceHardwareCallback)mGeofences.get(j);
          if (localObject4 != null) {
            try
            {
              ((IGeofenceHardwareCallback)localObject4).onGeofenceAdd(j, arg2);
            }
            catch (RemoteException ???)
            {
              ??? = new StringBuilder();
              ((StringBuilder)???).append("Remote Exception:");
              ((StringBuilder)???).append(???);
              Log.i("GeofenceHardwareImpl", ((StringBuilder)???).toString());
            }
          }
          GeofenceHardwareImpl.this.releaseWakeLock();
        }
      case 1: 
        localObject4 = (GeofenceHardwareImpl.GeofenceTransition)obj;
        synchronized (mGeofences)
        {
          ??? = (IGeofenceHardwareCallback)mGeofences.get(GeofenceHardwareImpl.GeofenceTransition.access$600((GeofenceHardwareImpl.GeofenceTransition)localObject4));
          if (GeofenceHardwareImpl.DEBUG)
          {
            StringBuilder localStringBuilder = new java/lang/StringBuilder;
            localStringBuilder.<init>();
            localStringBuilder.append("GeofenceTransistionCallback: GPS : GeofenceId: ");
            localStringBuilder.append(GeofenceHardwareImpl.GeofenceTransition.access$600((GeofenceHardwareImpl.GeofenceTransition)localObject4));
            localStringBuilder.append(" Transition: ");
            localStringBuilder.append(GeofenceHardwareImpl.GeofenceTransition.access$700((GeofenceHardwareImpl.GeofenceTransition)localObject4));
            localStringBuilder.append(" Location: ");
            localStringBuilder.append(GeofenceHardwareImpl.GeofenceTransition.access$800((GeofenceHardwareImpl.GeofenceTransition)localObject4));
            localStringBuilder.append(":");
            localStringBuilder.append(mGeofences);
            Log.d("GeofenceHardwareImpl", localStringBuilder.toString());
          }
          if (??? != null) {
            try
            {
              ((IGeofenceHardwareCallback)???).onGeofenceTransition(GeofenceHardwareImpl.GeofenceTransition.access$600((GeofenceHardwareImpl.GeofenceTransition)localObject4), GeofenceHardwareImpl.GeofenceTransition.access$700((GeofenceHardwareImpl.GeofenceTransition)localObject4), GeofenceHardwareImpl.GeofenceTransition.access$800((GeofenceHardwareImpl.GeofenceTransition)localObject4), GeofenceHardwareImpl.GeofenceTransition.access$900((GeofenceHardwareImpl.GeofenceTransition)localObject4), GeofenceHardwareImpl.GeofenceTransition.access$1000((GeofenceHardwareImpl.GeofenceTransition)localObject4));
            }
            catch (RemoteException ???) {}
          }
          GeofenceHardwareImpl.this.releaseWakeLock();
        }
      }
    }
  };
  private final SparseArray<IGeofenceHardwareCallback> mGeofences = new SparseArray();
  private IGpsGeofenceHardware mGpsService;
  private Handler mReaperHandler = new Handler()
  {
    public void handleMessage(Message paramAnonymousMessage)
    {
      Object localObject;
      int i;
      switch (what)
      {
      default: 
        break;
      case 3: 
        paramAnonymousMessage = (GeofenceHardwareImpl.Reaper)obj;
        mReapers.remove(paramAnonymousMessage);
        break;
      case 2: 
        localObject = (IGeofenceHardwareMonitorCallback)obj;
        i = arg1;
        paramAnonymousMessage = new GeofenceHardwareImpl.Reaper(GeofenceHardwareImpl.this, (IGeofenceHardwareMonitorCallback)localObject, i);
        if (!mReapers.contains(paramAnonymousMessage))
        {
          mReapers.add(paramAnonymousMessage);
          localObject = ((IGeofenceHardwareMonitorCallback)localObject).asBinder();
          try
          {
            ((IBinder)localObject).linkToDeath(paramAnonymousMessage, 0);
          }
          catch (RemoteException paramAnonymousMessage) {}
        }
        break;
      case 1: 
        localObject = (IGeofenceHardwareCallback)obj;
        i = arg1;
        paramAnonymousMessage = new GeofenceHardwareImpl.Reaper(GeofenceHardwareImpl.this, (IGeofenceHardwareCallback)localObject, i);
        if (!mReapers.contains(paramAnonymousMessage))
        {
          mReapers.add(paramAnonymousMessage);
          localObject = ((IGeofenceHardwareCallback)localObject).asBinder();
          try
          {
            ((IBinder)localObject).linkToDeath(paramAnonymousMessage, 0);
          }
          catch (RemoteException paramAnonymousMessage) {}
        }
        break;
      }
    }
  };
  private final ArrayList<Reaper> mReapers = new ArrayList();
  private int[] mSupportedMonitorTypes = new int[2];
  private int mVersion = 1;
  private PowerManager.WakeLock mWakeLock;
  
  private GeofenceHardwareImpl(Context paramContext)
  {
    mContext = paramContext;
    setMonitorAvailability(0, 2);
    setMonitorAvailability(1, 2);
  }
  
  private void acquireWakeLock()
  {
    if (mWakeLock == null) {
      mWakeLock = ((PowerManager)mContext.getSystemService("power")).newWakeLock(1, "GeofenceHardwareImpl");
    }
    mWakeLock.acquire();
  }
  
  public static GeofenceHardwareImpl getInstance(Context paramContext)
  {
    try
    {
      if (sInstance == null)
      {
        GeofenceHardwareImpl localGeofenceHardwareImpl = new android/hardware/location/GeofenceHardwareImpl;
        localGeofenceHardwareImpl.<init>(paramContext);
        sInstance = localGeofenceHardwareImpl;
      }
      paramContext = sInstance;
      return paramContext;
    }
    finally {}
  }
  
  private void releaseWakeLock()
  {
    if (mWakeLock.isHeld()) {
      mWakeLock.release();
    }
  }
  
  private void reportGeofenceOperationStatus(int paramInt1, int paramInt2, int paramInt3)
  {
    acquireWakeLock();
    Message localMessage = mGeofenceHandler.obtainMessage(paramInt1);
    arg1 = paramInt2;
    arg2 = paramInt3;
    localMessage.sendToTarget();
  }
  
  private void setMonitorAvailability(int paramInt1, int paramInt2)
  {
    synchronized (mSupportedMonitorTypes)
    {
      mSupportedMonitorTypes[paramInt1] = paramInt2;
      return;
    }
  }
  
  private void updateFusedHardwareAvailability()
  {
    int i;
    try
    {
      if ((mVersion >= 2) && ((mCapabilities & 0x1) == 0)) {
        i = 0;
      } else {
        i = 1;
      }
      if (mFusedService != null)
      {
        boolean bool = mFusedService.isSupported();
        if ((bool) && (i != 0))
        {
          i = 1;
          break label59;
        }
      }
      i = 0;
    }
    catch (RemoteException localRemoteException)
    {
      label59:
      Log.e("GeofenceHardwareImpl", "RemoteException calling LocationManagerService");
      i = 0;
    }
    if (i != 0) {
      setMonitorAvailability(1, 0);
    }
  }
  
  private void updateGpsHardwareAvailability()
  {
    boolean bool;
    try
    {
      bool = mGpsService.isHardwareGeofenceSupported();
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("GeofenceHardwareImpl", "Remote Exception calling LocationManagerService");
      bool = false;
    }
    if (bool) {
      setMonitorAvailability(0, 0);
    }
  }
  
  public boolean addCircularFence(int paramInt, GeofenceHardwareRequestParcelable paramGeofenceHardwareRequestParcelable, IGeofenceHardwareCallback arg3)
  {
    int i = paramGeofenceHardwareRequestParcelable.getId();
    boolean bool1 = DEBUG;
    boolean bool2 = false;
    if (bool1) {
      Log.d("GeofenceHardwareImpl", String.format("addCircularFence: monitoringType=%d, %s", new Object[] { Integer.valueOf(paramInt), paramGeofenceHardwareRequestParcelable }));
    }
    synchronized (mGeofences)
    {
      mGeofences.put(i, ???);
      switch (paramInt)
      {
      default: 
        break;
      case 1: 
        if (mFusedService == null) {
          return false;
        }
        try
        {
          mFusedService.addGeofences(new GeofenceHardwareRequestParcelable[] { paramGeofenceHardwareRequestParcelable });
          bool2 = true;
        }
        catch (RemoteException paramGeofenceHardwareRequestParcelable)
        {
          Log.e("GeofenceHardwareImpl", "AddGeofence: RemoteException calling LocationManagerService");
          bool2 = false;
        }
      case 0: 
        if (mGpsService == null) {
          return false;
        }
        try
        {
          bool2 = mGpsService.addCircularHardwareGeofence(paramGeofenceHardwareRequestParcelable.getId(), paramGeofenceHardwareRequestParcelable.getLatitude(), paramGeofenceHardwareRequestParcelable.getLongitude(), paramGeofenceHardwareRequestParcelable.getRadius(), paramGeofenceHardwareRequestParcelable.getLastTransition(), paramGeofenceHardwareRequestParcelable.getMonitorTransitions(), paramGeofenceHardwareRequestParcelable.getNotificationResponsiveness(), paramGeofenceHardwareRequestParcelable.getUnknownTimer());
        }
        catch (RemoteException paramGeofenceHardwareRequestParcelable)
        {
          Log.e("GeofenceHardwareImpl", "AddGeofence: Remote Exception calling LocationManagerService");
          bool2 = false;
        }
      }
      if (bool2)
      {
        paramGeofenceHardwareRequestParcelable = mReaperHandler.obtainMessage(1, ???);
        arg1 = paramInt;
        mReaperHandler.sendMessage(paramGeofenceHardwareRequestParcelable);
      }
      synchronized (mGeofences)
      {
        mGeofences.remove(i);
        if (DEBUG)
        {
          paramGeofenceHardwareRequestParcelable = new StringBuilder();
          paramGeofenceHardwareRequestParcelable.append("addCircularFence: Result is: ");
          paramGeofenceHardwareRequestParcelable.append(bool2);
          Log.d("GeofenceHardwareImpl", paramGeofenceHardwareRequestParcelable.toString());
        }
        return bool2;
      }
    }
  }
  
  int getAllowedResolutionLevel(int paramInt1, int paramInt2)
  {
    if (mContext.checkPermission("android.permission.ACCESS_FINE_LOCATION", paramInt1, paramInt2) == 0) {
      return 3;
    }
    if (mContext.checkPermission("android.permission.ACCESS_COARSE_LOCATION", paramInt1, paramInt2) == 0) {
      return 2;
    }
    return 1;
  }
  
  public int getCapabilitiesForMonitoringType(int paramInt)
  {
    if (mSupportedMonitorTypes[paramInt] == 0) {
      switch (paramInt)
      {
      default: 
        break;
      case 1: 
        if (mVersion >= 2) {
          return mCapabilities;
        }
        return 1;
      case 0: 
        return 1;
      }
    }
    return 0;
  }
  
  int getMonitoringResolutionLevel(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return 1;
    case 1: 
      return 3;
    }
    return 3;
  }
  
  public int[] getMonitoringTypes()
  {
    synchronized (mSupportedMonitorTypes)
    {
      int i;
      if (mSupportedMonitorTypes[0] != 2) {
        i = 1;
      } else {
        i = 0;
      }
      int j;
      if (mSupportedMonitorTypes[1] != 2) {
        j = 1;
      } else {
        j = 0;
      }
      if (i != 0)
      {
        if (j != 0) {
          return new int[] { 0, 1 };
        }
        return new int[] { 0 };
      }
      if (j != 0) {
        return new int[] { 1 };
      }
      return new int[0];
    }
  }
  
  public int getStatusOfMonitoringType(int paramInt)
  {
    synchronized (mSupportedMonitorTypes)
    {
      if ((paramInt < mSupportedMonitorTypes.length) && (paramInt >= 0))
      {
        paramInt = mSupportedMonitorTypes[paramInt];
        return paramInt;
      }
      IllegalArgumentException localIllegalArgumentException = new java/lang/IllegalArgumentException;
      localIllegalArgumentException.<init>("Unknown monitoring type");
      throw localIllegalArgumentException;
    }
  }
  
  public void onCapabilities(int paramInt)
  {
    mCapabilities = paramInt;
    updateFusedHardwareAvailability();
  }
  
  public boolean pauseGeofence(int paramInt1, int paramInt2)
  {
    if (DEBUG)
    {
      ??? = new StringBuilder();
      ((StringBuilder)???).append("Pause Geofence: GeofenceId: ");
      ((StringBuilder)???).append(paramInt1);
      Log.d("GeofenceHardwareImpl", ((StringBuilder)???).toString());
    }
    StringBuilder localStringBuilder1;
    synchronized (mGeofences)
    {
      if (mGeofences.get(paramInt1) != null)
      {
        boolean bool = false;
        switch (paramInt2)
        {
        default: 
          break;
        case 1: 
          if (mFusedService == null) {
            return false;
          }
          try
          {
            mFusedService.pauseMonitoringGeofence(paramInt1);
            bool = true;
          }
          catch (RemoteException localRemoteException1)
          {
            Log.e("GeofenceHardwareImpl", "PauseGeofence: RemoteException calling LocationManagerService");
            bool = false;
          }
        case 0: 
          if (mGpsService == null) {
            return false;
          }
          try
          {
            bool = mGpsService.pauseHardwareGeofence(paramInt1);
          }
          catch (RemoteException localRemoteException2)
          {
            Log.e("GeofenceHardwareImpl", "PauseGeofence: Remote Exception calling LocationManagerService");
            bool = false;
          }
        }
        if (DEBUG)
        {
          localStringBuilder1 = new StringBuilder();
          localStringBuilder1.append("pauseGeofence: Result is: ");
          localStringBuilder1.append(bool);
          Log.d("GeofenceHardwareImpl", localStringBuilder1.toString());
        }
        return bool;
      }
      IllegalArgumentException localIllegalArgumentException = new java/lang/IllegalArgumentException;
      StringBuilder localStringBuilder2 = new java/lang/StringBuilder;
      localStringBuilder2.<init>();
      localStringBuilder2.append("Geofence ");
      localStringBuilder2.append(paramInt1);
      localStringBuilder2.append(" not registered.");
      localIllegalArgumentException.<init>(localStringBuilder2.toString());
      throw localIllegalArgumentException;
    }
  }
  
  public boolean registerForMonitorStateChangeCallback(int paramInt, IGeofenceHardwareMonitorCallback paramIGeofenceHardwareMonitorCallback)
  {
    Message localMessage = mReaperHandler.obtainMessage(2, paramIGeofenceHardwareMonitorCallback);
    arg1 = paramInt;
    mReaperHandler.sendMessage(localMessage);
    paramIGeofenceHardwareMonitorCallback = mCallbacksHandler.obtainMessage(2, paramIGeofenceHardwareMonitorCallback);
    arg1 = paramInt;
    mCallbacksHandler.sendMessage(paramIGeofenceHardwareMonitorCallback);
    return true;
  }
  
  public boolean removeGeofence(int paramInt1, int paramInt2)
  {
    if (DEBUG)
    {
      ??? = new StringBuilder();
      ((StringBuilder)???).append("Remove Geofence: GeofenceId: ");
      ((StringBuilder)???).append(paramInt1);
      Log.d("GeofenceHardwareImpl", ((StringBuilder)???).toString());
    }
    StringBuilder localStringBuilder1;
    synchronized (mGeofences)
    {
      if (mGeofences.get(paramInt1) != null)
      {
        boolean bool;
        switch (paramInt2)
        {
        default: 
          bool = false;
          break;
        case 1: 
          if (mFusedService == null) {
            return false;
          }
          try
          {
            mFusedService.removeGeofences(new int[] { paramInt1 });
            bool = true;
          }
          catch (RemoteException localRemoteException1)
          {
            Log.e("GeofenceHardwareImpl", "RemoveGeofence: RemoteException calling LocationManagerService");
            bool = false;
          }
        case 0: 
          if (mGpsService == null) {
            return false;
          }
          try
          {
            bool = mGpsService.removeHardwareGeofence(paramInt1);
          }
          catch (RemoteException localRemoteException2)
          {
            Log.e("GeofenceHardwareImpl", "RemoveGeofence: Remote Exception calling LocationManagerService");
            bool = false;
          }
        }
        if (DEBUG)
        {
          localStringBuilder1 = new StringBuilder();
          localStringBuilder1.append("removeGeofence: Result is: ");
          localStringBuilder1.append(bool);
          Log.d("GeofenceHardwareImpl", localStringBuilder1.toString());
        }
        return bool;
      }
      IllegalArgumentException localIllegalArgumentException = new java/lang/IllegalArgumentException;
      StringBuilder localStringBuilder2 = new java/lang/StringBuilder;
      localStringBuilder2.<init>();
      localStringBuilder2.append("Geofence ");
      localStringBuilder2.append(paramInt1);
      localStringBuilder2.append(" not registered.");
      localIllegalArgumentException.<init>(localStringBuilder2.toString());
      throw localIllegalArgumentException;
    }
  }
  
  public void reportGeofenceAddStatus(int paramInt1, int paramInt2)
  {
    if (DEBUG)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("AddCallback| id:");
      localStringBuilder.append(paramInt1);
      localStringBuilder.append(", status:");
      localStringBuilder.append(paramInt2);
      Log.d("GeofenceHardwareImpl", localStringBuilder.toString());
    }
    reportGeofenceOperationStatus(2, paramInt1, paramInt2);
  }
  
  public void reportGeofenceMonitorStatus(int paramInt1, int paramInt2, Location paramLocation, int paramInt3)
  {
    setMonitorAvailability(paramInt1, paramInt2);
    acquireWakeLock();
    paramLocation = new GeofenceHardwareMonitorEvent(paramInt1, paramInt2, paramInt3, paramLocation);
    mCallbacksHandler.obtainMessage(1, paramLocation).sendToTarget();
  }
  
  public void reportGeofencePauseStatus(int paramInt1, int paramInt2)
  {
    if (DEBUG)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("PauseCallbac| id:");
      localStringBuilder.append(paramInt1);
      localStringBuilder.append(", status");
      localStringBuilder.append(paramInt2);
      Log.d("GeofenceHardwareImpl", localStringBuilder.toString());
    }
    reportGeofenceOperationStatus(4, paramInt1, paramInt2);
  }
  
  public void reportGeofenceRemoveStatus(int paramInt1, int paramInt2)
  {
    if (DEBUG)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("RemoveCallback| id:");
      localStringBuilder.append(paramInt1);
      localStringBuilder.append(", status:");
      localStringBuilder.append(paramInt2);
      Log.d("GeofenceHardwareImpl", localStringBuilder.toString());
    }
    reportGeofenceOperationStatus(3, paramInt1, paramInt2);
  }
  
  public void reportGeofenceResumeStatus(int paramInt1, int paramInt2)
  {
    if (DEBUG)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("ResumeCallback| id:");
      localStringBuilder.append(paramInt1);
      localStringBuilder.append(", status:");
      localStringBuilder.append(paramInt2);
      Log.d("GeofenceHardwareImpl", localStringBuilder.toString());
    }
    reportGeofenceOperationStatus(5, paramInt1, paramInt2);
  }
  
  public void reportGeofenceTransition(int paramInt1, Location paramLocation, int paramInt2, long paramLong, int paramInt3, int paramInt4)
  {
    if (paramLocation == null)
    {
      Log.e("GeofenceHardwareImpl", String.format("Invalid Geofence Transition: location=null", new Object[0]));
      return;
    }
    if (DEBUG)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("GeofenceTransition| ");
      localStringBuilder.append(paramLocation);
      localStringBuilder.append(", transition:");
      localStringBuilder.append(paramInt2);
      localStringBuilder.append(", transitionTimestamp:");
      localStringBuilder.append(paramLong);
      localStringBuilder.append(", monitoringType:");
      localStringBuilder.append(paramInt3);
      localStringBuilder.append(", sourcesUsed:");
      localStringBuilder.append(paramInt4);
      Log.d("GeofenceHardwareImpl", localStringBuilder.toString());
    }
    paramLocation = new GeofenceTransition(paramInt1, paramInt2, paramLong, paramLocation, paramInt3, paramInt4);
    acquireWakeLock();
    mGeofenceHandler.obtainMessage(1, paramLocation).sendToTarget();
  }
  
  public boolean resumeGeofence(int paramInt1, int paramInt2, int paramInt3)
  {
    if (DEBUG)
    {
      ??? = new StringBuilder();
      ((StringBuilder)???).append("Resume Geofence: GeofenceId: ");
      ((StringBuilder)???).append(paramInt1);
      Log.d("GeofenceHardwareImpl", ((StringBuilder)???).toString());
    }
    StringBuilder localStringBuilder1;
    synchronized (mGeofences)
    {
      if (mGeofences.get(paramInt1) != null)
      {
        boolean bool = false;
        switch (paramInt2)
        {
        default: 
          break;
        case 1: 
          if (mFusedService == null) {
            return false;
          }
          try
          {
            mFusedService.resumeMonitoringGeofence(paramInt1, paramInt3);
            bool = true;
          }
          catch (RemoteException localRemoteException1)
          {
            Log.e("GeofenceHardwareImpl", "ResumeGeofence: RemoteException calling LocationManagerService");
            bool = false;
          }
        case 0: 
          if (mGpsService == null) {
            return false;
          }
          try
          {
            bool = mGpsService.resumeHardwareGeofence(paramInt1, paramInt3);
          }
          catch (RemoteException localRemoteException2)
          {
            Log.e("GeofenceHardwareImpl", "ResumeGeofence: Remote Exception calling LocationManagerService");
            bool = false;
          }
        }
        if (DEBUG)
        {
          localStringBuilder1 = new StringBuilder();
          localStringBuilder1.append("resumeGeofence: Result is: ");
          localStringBuilder1.append(bool);
          Log.d("GeofenceHardwareImpl", localStringBuilder1.toString());
        }
        return bool;
      }
      IllegalArgumentException localIllegalArgumentException = new java/lang/IllegalArgumentException;
      StringBuilder localStringBuilder2 = new java/lang/StringBuilder;
      localStringBuilder2.<init>();
      localStringBuilder2.append("Geofence ");
      localStringBuilder2.append(paramInt1);
      localStringBuilder2.append(" not registered.");
      localIllegalArgumentException.<init>(localStringBuilder2.toString());
      throw localIllegalArgumentException;
    }
  }
  
  public void setFusedGeofenceHardware(IFusedGeofenceHardware paramIFusedGeofenceHardware)
  {
    if (mFusedService == null)
    {
      mFusedService = paramIFusedGeofenceHardware;
      updateFusedHardwareAvailability();
    }
    else if (paramIFusedGeofenceHardware == null)
    {
      mFusedService = null;
      Log.w("GeofenceHardwareImpl", "Fused Geofence Hardware service seems to have crashed");
    }
    else
    {
      Log.e("GeofenceHardwareImpl", "Error: FusedService being set again");
    }
  }
  
  public void setGpsHardwareGeofence(IGpsGeofenceHardware paramIGpsGeofenceHardware)
  {
    if (mGpsService == null)
    {
      mGpsService = paramIGpsGeofenceHardware;
      updateGpsHardwareAvailability();
    }
    else if (paramIGpsGeofenceHardware == null)
    {
      mGpsService = null;
      Log.w("GeofenceHardwareImpl", "GPS Geofence Hardware service seems to have crashed");
    }
    else
    {
      Log.e("GeofenceHardwareImpl", "Error: GpsService being set again.");
    }
  }
  
  public void setVersion(int paramInt)
  {
    mVersion = paramInt;
    updateFusedHardwareAvailability();
  }
  
  public boolean unregisterForMonitorStateChangeCallback(int paramInt, IGeofenceHardwareMonitorCallback paramIGeofenceHardwareMonitorCallback)
  {
    paramIGeofenceHardwareMonitorCallback = mCallbacksHandler.obtainMessage(3, paramIGeofenceHardwareMonitorCallback);
    arg1 = paramInt;
    mCallbacksHandler.sendMessage(paramIGeofenceHardwareMonitorCallback);
    return true;
  }
  
  private class GeofenceTransition
  {
    private int mGeofenceId;
    private Location mLocation;
    private int mMonitoringType;
    private int mSourcesUsed;
    private long mTimestamp;
    private int mTransition;
    
    GeofenceTransition(int paramInt1, int paramInt2, long paramLong, Location paramLocation, int paramInt3, int paramInt4)
    {
      mGeofenceId = paramInt1;
      mTransition = paramInt2;
      mTimestamp = paramLong;
      mLocation = paramLocation;
      mMonitoringType = paramInt3;
      mSourcesUsed = paramInt4;
    }
  }
  
  class Reaper
    implements IBinder.DeathRecipient
  {
    private IGeofenceHardwareCallback mCallback;
    private IGeofenceHardwareMonitorCallback mMonitorCallback;
    private int mMonitoringType;
    
    Reaper(IGeofenceHardwareCallback paramIGeofenceHardwareCallback, int paramInt)
    {
      mCallback = paramIGeofenceHardwareCallback;
      mMonitoringType = paramInt;
    }
    
    Reaper(IGeofenceHardwareMonitorCallback paramIGeofenceHardwareMonitorCallback, int paramInt)
    {
      mMonitorCallback = paramIGeofenceHardwareMonitorCallback;
      mMonitoringType = paramInt;
    }
    
    private boolean binderEquals(IInterface paramIInterface1, IInterface paramIInterface2)
    {
      boolean bool1 = true;
      boolean bool2 = false;
      if (paramIInterface1 == null)
      {
        if (paramIInterface2 != null) {
          bool1 = false;
        }
        return bool1;
      }
      if (paramIInterface2 == null) {}
      while (paramIInterface1.asBinder() != paramIInterface2.asBinder())
      {
        bool1 = bool2;
        break;
      }
      bool1 = true;
      return bool1;
    }
    
    private boolean callbackEquals(IGeofenceHardwareCallback paramIGeofenceHardwareCallback)
    {
      boolean bool;
      if ((mCallback != null) && (mCallback.asBinder() == paramIGeofenceHardwareCallback.asBinder())) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    private boolean unlinkToDeath()
    {
      if (mMonitorCallback != null) {
        return mMonitorCallback.asBinder().unlinkToDeath(this, 0);
      }
      if (mCallback != null) {
        return mCallback.asBinder().unlinkToDeath(this, 0);
      }
      return true;
    }
    
    public void binderDied()
    {
      if (mCallback != null)
      {
        localMessage = mGeofenceHandler.obtainMessage(6, mCallback);
        arg1 = mMonitoringType;
        mGeofenceHandler.sendMessage(localMessage);
      }
      else if (mMonitorCallback != null)
      {
        localMessage = mCallbacksHandler.obtainMessage(4, mMonitorCallback);
        arg1 = mMonitoringType;
        mCallbacksHandler.sendMessage(localMessage);
      }
      Message localMessage = mReaperHandler.obtainMessage(3, this);
      mReaperHandler.sendMessage(localMessage);
    }
    
    public boolean equals(Object paramObject)
    {
      boolean bool = false;
      if (paramObject == null) {
        return false;
      }
      if (paramObject == this) {
        return true;
      }
      paramObject = (Reaper)paramObject;
      if ((binderEquals(mCallback, mCallback)) && (binderEquals(mMonitorCallback, mMonitorCallback)) && (mMonitoringType == mMonitoringType)) {
        bool = true;
      }
      return bool;
    }
    
    public int hashCode()
    {
      IGeofenceHardwareCallback localIGeofenceHardwareCallback = mCallback;
      int i = 0;
      int j;
      if (localIGeofenceHardwareCallback != null) {
        j = mCallback.asBinder().hashCode();
      } else {
        j = 0;
      }
      if (mMonitorCallback != null) {
        i = mMonitorCallback.asBinder().hashCode();
      }
      return 31 * (31 * (31 * 17 + j) + i) + mMonitoringType;
    }
  }
}
