package android.app;

import android.annotation.SystemApi;
import android.content.ComponentName;
import android.os.Handler;
import android.os.RemoteException;
import android.service.vr.IPersistentVrStateCallbacks;
import android.service.vr.IPersistentVrStateCallbacks.Stub;
import android.service.vr.IVrManager;
import android.service.vr.IVrStateCallbacks;
import android.service.vr.IVrStateCallbacks.Stub;
import android.util.ArrayMap;
import java.util.Map;

@SystemApi
public class VrManager
{
  private Map<VrStateCallback, CallbackEntry> mCallbackMap = new ArrayMap();
  private final IVrManager mService;
  
  public VrManager(IVrManager paramIVrManager)
  {
    mService = paramIVrManager;
  }
  
  public boolean getPersistentVrModeEnabled()
  {
    try
    {
      boolean bool = mService.getPersistentVrModeEnabled();
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      localRemoteException.rethrowFromSystemServer();
    }
    return false;
  }
  
  public boolean getVrModeEnabled()
  {
    try
    {
      boolean bool = mService.getVrModeState();
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      localRemoteException.rethrowFromSystemServer();
    }
    return false;
  }
  
  public void registerVrStateCallback(VrStateCallback paramVrStateCallback, Handler paramHandler)
  {
    if ((paramVrStateCallback != null) && (!mCallbackMap.containsKey(paramVrStateCallback)))
    {
      paramHandler = new CallbackEntry(paramVrStateCallback, paramHandler);
      mCallbackMap.put(paramVrStateCallback, paramHandler);
      try
      {
        mService.registerListener(mStateCallback);
        mService.registerPersistentVrStateListener(mPersistentStateCallback);
      }
      catch (RemoteException paramHandler)
      {
        try
        {
          unregisterVrStateCallback(paramVrStateCallback);
        }
        catch (Exception paramVrStateCallback)
        {
          paramHandler.rethrowFromSystemServer();
        }
      }
      return;
    }
  }
  
  public void setAndBindVrCompositor(ComponentName paramComponentName)
  {
    try
    {
      IVrManager localIVrManager = mService;
      if (paramComponentName == null) {
        paramComponentName = null;
      } else {
        paramComponentName = paramComponentName.flattenToString();
      }
      localIVrManager.setAndBindCompositor(paramComponentName);
    }
    catch (RemoteException paramComponentName)
    {
      paramComponentName.rethrowFromSystemServer();
    }
  }
  
  public void setPersistentVrModeEnabled(boolean paramBoolean)
  {
    try
    {
      mService.setPersistentVrModeEnabled(paramBoolean);
    }
    catch (RemoteException localRemoteException)
    {
      localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void setStandbyEnabled(boolean paramBoolean)
  {
    try
    {
      mService.setStandbyEnabled(paramBoolean);
    }
    catch (RemoteException localRemoteException)
    {
      localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void setVr2dDisplayProperties(Vr2dDisplayProperties paramVr2dDisplayProperties)
  {
    try
    {
      mService.setVr2dDisplayProperties(paramVr2dDisplayProperties);
    }
    catch (RemoteException paramVr2dDisplayProperties)
    {
      paramVr2dDisplayProperties.rethrowFromSystemServer();
    }
  }
  
  public void setVrInputMethod(ComponentName paramComponentName)
  {
    try
    {
      mService.setVrInputMethod(paramComponentName);
    }
    catch (RemoteException paramComponentName)
    {
      paramComponentName.rethrowFromSystemServer();
    }
  }
  
  public void unregisterVrStateCallback(VrStateCallback paramVrStateCallback)
  {
    CallbackEntry localCallbackEntry = (CallbackEntry)mCallbackMap.remove(paramVrStateCallback);
    if (localCallbackEntry != null)
    {
      try
      {
        mService.unregisterListener(mStateCallback);
      }
      catch (RemoteException paramVrStateCallback) {}
      try
      {
        mService.unregisterPersistentVrStateListener(mPersistentStateCallback);
      }
      catch (RemoteException paramVrStateCallback) {}
    }
  }
  
  private static class CallbackEntry
  {
    final VrStateCallback mCallback;
    final Handler mHandler;
    final IPersistentVrStateCallbacks mPersistentStateCallback = new IPersistentVrStateCallbacks.Stub()
    {
      public void onPersistentVrStateChanged(boolean paramAnonymousBoolean)
      {
        mHandler.post(new _..Lambda.VrManager.CallbackEntry.2.KvHLIXm3_7igcOqTEl46YdjhHMk(this, paramAnonymousBoolean));
      }
    };
    final IVrStateCallbacks mStateCallback = new IVrStateCallbacks.Stub()
    {
      public void onVrStateChanged(boolean paramAnonymousBoolean)
      {
        mHandler.post(new _..Lambda.VrManager.CallbackEntry.1.rgUBVVG1QhelpvAp8W3UQHDHJdU(this, paramAnonymousBoolean));
      }
    };
    
    CallbackEntry(VrStateCallback paramVrStateCallback, Handler paramHandler)
    {
      mCallback = paramVrStateCallback;
      mHandler = paramHandler;
    }
  }
}
