package android.bluetooth;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public final class BluetoothAvrcpController
  implements BluetoothProfile
{
  public static final String ACTION_CONNECTION_STATE_CHANGED = "android.bluetooth.avrcp-controller.profile.action.CONNECTION_STATE_CHANGED";
  public static final String ACTION_PLAYER_SETTING = "android.bluetooth.avrcp-controller.profile.action.PLAYER_SETTING";
  private static final boolean DBG = false;
  public static final String EXTRA_PLAYER_SETTING = "android.bluetooth.avrcp-controller.profile.extra.PLAYER_SETTING";
  private static final String TAG = "BluetoothAvrcpController";
  private static final boolean VDBG = false;
  private BluetoothAdapter mAdapter;
  private final IBluetoothStateChangeCallback mBluetoothStateChangeCallback = new IBluetoothStateChangeCallback.Stub()
  {
    public void onBluetoothStateChange(boolean paramAnonymousBoolean)
    {
      if (!paramAnonymousBoolean)
      {
        try
        {
          synchronized (mConnection)
          {
            BluetoothAvrcpController.access$102(BluetoothAvrcpController.this, null);
            mContext.unbindService(mConnection);
          }
        }
        catch (Exception localException1)
        {
          Log.e("BluetoothAvrcpController", "", localException1);
        }
        throw localException1;
      }
      else
      {
        try
        {
          synchronized (mConnection)
          {
            if (mService == null) {
              doBind();
            }
          }
          return;
        }
        catch (Exception localException2)
        {
          Log.e("BluetoothAvrcpController", "", localException2);
        }
      }
    }
  };
  private final ServiceConnection mConnection = new ServiceConnection()
  {
    public void onServiceConnected(ComponentName paramAnonymousComponentName, IBinder paramAnonymousIBinder)
    {
      BluetoothAvrcpController.access$102(BluetoothAvrcpController.this, IBluetoothAvrcpController.Stub.asInterface(Binder.allowBlocking(paramAnonymousIBinder)));
      if (mServiceListener != null) {
        mServiceListener.onServiceConnected(12, BluetoothAvrcpController.this);
      }
    }
    
    public void onServiceDisconnected(ComponentName paramAnonymousComponentName)
    {
      BluetoothAvrcpController.access$102(BluetoothAvrcpController.this, null);
      if (mServiceListener != null) {
        mServiceListener.onServiceDisconnected(12);
      }
    }
  };
  private Context mContext;
  private volatile IBluetoothAvrcpController mService;
  private BluetoothProfile.ServiceListener mServiceListener;
  
  BluetoothAvrcpController(Context paramContext, BluetoothProfile.ServiceListener paramServiceListener)
  {
    mContext = paramContext;
    mServiceListener = paramServiceListener;
    mAdapter = BluetoothAdapter.getDefaultAdapter();
    paramContext = mAdapter.getBluetoothManager();
    if (paramContext != null) {
      try
      {
        paramContext.registerStateChangeCallback(mBluetoothStateChangeCallback);
      }
      catch (RemoteException paramContext)
      {
        Log.e("BluetoothAvrcpController", "", paramContext);
      }
    }
    doBind();
  }
  
  private boolean isEnabled()
  {
    boolean bool;
    if (mAdapter.getState() == 12) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private static boolean isValidDevice(BluetoothDevice paramBluetoothDevice)
  {
    boolean bool;
    if ((paramBluetoothDevice != null) && (BluetoothAdapter.checkBluetoothAddress(paramBluetoothDevice.getAddress()))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private static void log(String paramString)
  {
    Log.d("BluetoothAvrcpController", paramString);
  }
  
  void close()
  {
    mServiceListener = null;
    IBluetoothManager localIBluetoothManager = mAdapter.getBluetoothManager();
    if (localIBluetoothManager != null) {
      try
      {
        localIBluetoothManager.unregisterStateChangeCallback(mBluetoothStateChangeCallback);
      }
      catch (Exception localException1)
      {
        Log.e("BluetoothAvrcpController", "", localException1);
      }
    }
    synchronized (mConnection)
    {
      IBluetoothAvrcpController localIBluetoothAvrcpController = mService;
      if (localIBluetoothAvrcpController != null) {
        try
        {
          mService = null;
          mContext.unbindService(mConnection);
        }
        catch (Exception localException2)
        {
          Log.e("BluetoothAvrcpController", "", localException2);
        }
      }
      return;
    }
  }
  
  boolean doBind()
  {
    Intent localIntent = new Intent(IBluetoothAvrcpController.class.getName());
    Object localObject = localIntent.resolveSystemService(mContext.getPackageManager(), 0);
    localIntent.setComponent((ComponentName)localObject);
    if ((localObject != null) && (mContext.bindServiceAsUser(localIntent, mConnection, 0, mContext.getUser()))) {
      return true;
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Could not bind to Bluetooth AVRCP Controller Service with ");
    ((StringBuilder)localObject).append(localIntent);
    Log.e("BluetoothAvrcpController", ((StringBuilder)localObject).toString());
    return false;
  }
  
  public void finalize()
  {
    close();
  }
  
  public List<BluetoothDevice> getConnectedDevices()
  {
    Object localObject = mService;
    StringBuilder localStringBuilder;
    if ((localObject != null) && (isEnabled())) {
      try
      {
        localObject = ((IBluetoothAvrcpController)localObject).getConnectedDevices();
        return localObject;
      }
      catch (RemoteException localRemoteException)
      {
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("Stack:");
        localStringBuilder.append(Log.getStackTraceString(new Throwable()));
        Log.e("BluetoothAvrcpController", localStringBuilder.toString());
        return new ArrayList();
      }
    }
    if (localStringBuilder == null) {
      Log.w("BluetoothAvrcpController", "Proxy not attached to service");
    }
    return new ArrayList();
  }
  
  public int getConnectionState(BluetoothDevice paramBluetoothDevice)
  {
    IBluetoothAvrcpController localIBluetoothAvrcpController = mService;
    if ((localIBluetoothAvrcpController != null) && (isEnabled()) && (isValidDevice(paramBluetoothDevice))) {
      try
      {
        int i = localIBluetoothAvrcpController.getConnectionState(paramBluetoothDevice);
        return i;
      }
      catch (RemoteException paramBluetoothDevice)
      {
        paramBluetoothDevice = new StringBuilder();
        paramBluetoothDevice.append("Stack:");
        paramBluetoothDevice.append(Log.getStackTraceString(new Throwable()));
        Log.e("BluetoothAvrcpController", paramBluetoothDevice.toString());
        return 0;
      }
    }
    if (localIBluetoothAvrcpController == null) {
      Log.w("BluetoothAvrcpController", "Proxy not attached to service");
    }
    return 0;
  }
  
  public List<BluetoothDevice> getDevicesMatchingConnectionStates(int[] paramArrayOfInt)
  {
    IBluetoothAvrcpController localIBluetoothAvrcpController = mService;
    if ((localIBluetoothAvrcpController != null) && (isEnabled())) {
      try
      {
        paramArrayOfInt = localIBluetoothAvrcpController.getDevicesMatchingConnectionStates(paramArrayOfInt);
        return paramArrayOfInt;
      }
      catch (RemoteException paramArrayOfInt)
      {
        paramArrayOfInt = new StringBuilder();
        paramArrayOfInt.append("Stack:");
        paramArrayOfInt.append(Log.getStackTraceString(new Throwable()));
        Log.e("BluetoothAvrcpController", paramArrayOfInt.toString());
        return new ArrayList();
      }
    }
    if (localIBluetoothAvrcpController == null) {
      Log.w("BluetoothAvrcpController", "Proxy not attached to service");
    }
    return new ArrayList();
  }
  
  public BluetoothAvrcpPlayerSettings getPlayerSettings(BluetoothDevice paramBluetoothDevice)
  {
    Object localObject1 = null;
    IBluetoothAvrcpController localIBluetoothAvrcpController = mService;
    Object localObject2 = localObject1;
    if (localIBluetoothAvrcpController != null)
    {
      localObject2 = localObject1;
      if (isEnabled()) {
        try
        {
          localObject2 = localIBluetoothAvrcpController.getPlayerSettings(paramBluetoothDevice);
        }
        catch (RemoteException paramBluetoothDevice)
        {
          localObject2 = new StringBuilder();
          ((StringBuilder)localObject2).append("Error talking to BT service in getMetadata() ");
          ((StringBuilder)localObject2).append(paramBluetoothDevice);
          Log.e("BluetoothAvrcpController", ((StringBuilder)localObject2).toString());
          return null;
        }
      }
    }
    return localObject2;
  }
  
  public void sendGroupNavigationCmd(BluetoothDevice paramBluetoothDevice, int paramInt1, int paramInt2)
  {
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("sendGroupNavigationCmd dev = ");
    ((StringBuilder)localObject).append(paramBluetoothDevice);
    ((StringBuilder)localObject).append(" key ");
    ((StringBuilder)localObject).append(paramInt1);
    ((StringBuilder)localObject).append(" State = ");
    ((StringBuilder)localObject).append(paramInt2);
    Log.d("BluetoothAvrcpController", ((StringBuilder)localObject).toString());
    localObject = mService;
    if ((localObject != null) && (isEnabled())) {
      try
      {
        ((IBluetoothAvrcpController)localObject).sendGroupNavigationCmd(paramBluetoothDevice, paramInt1, paramInt2);
        return;
      }
      catch (RemoteException paramBluetoothDevice)
      {
        Log.e("BluetoothAvrcpController", "Error talking to BT service in sendGroupNavigationCmd()", paramBluetoothDevice);
        return;
      }
    }
    if (localObject == null) {
      Log.w("BluetoothAvrcpController", "Proxy not attached to service");
    }
  }
  
  public boolean setPlayerApplicationSetting(BluetoothAvrcpPlayerSettings paramBluetoothAvrcpPlayerSettings)
  {
    Object localObject = mService;
    if ((localObject != null) && (isEnabled())) {
      try
      {
        boolean bool = ((IBluetoothAvrcpController)localObject).setPlayerApplicationSetting(paramBluetoothAvrcpPlayerSettings);
        return bool;
      }
      catch (RemoteException paramBluetoothAvrcpPlayerSettings)
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("Error talking to BT service in setPlayerApplicationSetting() ");
        ((StringBuilder)localObject).append(paramBluetoothAvrcpPlayerSettings);
        Log.e("BluetoothAvrcpController", ((StringBuilder)localObject).toString());
        return false;
      }
    }
    if (localObject == null) {
      Log.w("BluetoothAvrcpController", "Proxy not attached to service");
    }
    return false;
  }
}
