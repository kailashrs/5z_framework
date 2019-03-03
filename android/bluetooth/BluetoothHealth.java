package android.bluetooth;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public final class BluetoothHealth
  implements BluetoothProfile
{
  public static final int APP_CONFIG_REGISTRATION_FAILURE = 1;
  public static final int APP_CONFIG_REGISTRATION_SUCCESS = 0;
  public static final int APP_CONFIG_UNREGISTRATION_FAILURE = 3;
  public static final int APP_CONFIG_UNREGISTRATION_SUCCESS = 2;
  public static final int CHANNEL_TYPE_ANY = 12;
  public static final int CHANNEL_TYPE_RELIABLE = 10;
  public static final int CHANNEL_TYPE_STREAMING = 11;
  private static final boolean DBG = true;
  public static final int HEALTH_OPERATION_ERROR = 6001;
  public static final int HEALTH_OPERATION_GENERIC_FAILURE = 6003;
  public static final int HEALTH_OPERATION_INVALID_ARGS = 6002;
  public static final int HEALTH_OPERATION_NOT_ALLOWED = 6005;
  public static final int HEALTH_OPERATION_NOT_FOUND = 6004;
  public static final int HEALTH_OPERATION_SUCCESS = 6000;
  public static final int SINK_ROLE = 2;
  public static final int SOURCE_ROLE = 1;
  public static final int STATE_CHANNEL_CONNECTED = 2;
  public static final int STATE_CHANNEL_CONNECTING = 1;
  public static final int STATE_CHANNEL_DISCONNECTED = 0;
  public static final int STATE_CHANNEL_DISCONNECTING = 3;
  private static final String TAG = "BluetoothHealth";
  private static final boolean VDBG = false;
  BluetoothAdapter mAdapter;
  private final IBluetoothStateChangeCallback mBluetoothStateChangeCallback = new IBluetoothStateChangeCallback.Stub()
  {
    public void onBluetoothStateChange(boolean paramAnonymousBoolean)
    {
      ??? = new StringBuilder();
      ((StringBuilder)???).append("onBluetoothStateChange: up=");
      ((StringBuilder)???).append(paramAnonymousBoolean);
      Log.d("BluetoothHealth", ((StringBuilder)???).toString());
      if (!paramAnonymousBoolean)
      {
        try
        {
          synchronized (mConnection)
          {
            BluetoothHealth.access$102(BluetoothHealth.this, null);
            mContext.unbindService(mConnection);
          }
        }
        catch (Exception localException1)
        {
          Log.e("BluetoothHealth", "", localException1);
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
          Log.e("BluetoothHealth", "", localException2);
        }
      }
    }
  };
  private final ServiceConnection mConnection = new ServiceConnection()
  {
    public void onServiceConnected(ComponentName paramAnonymousComponentName, IBinder paramAnonymousIBinder)
    {
      Log.d("BluetoothHealth", "Proxy object connected");
      BluetoothHealth.access$102(BluetoothHealth.this, IBluetoothHealth.Stub.asInterface(Binder.allowBlocking(paramAnonymousIBinder)));
      if (mServiceListener != null) {
        mServiceListener.onServiceConnected(3, BluetoothHealth.this);
      }
    }
    
    public void onServiceDisconnected(ComponentName paramAnonymousComponentName)
    {
      Log.d("BluetoothHealth", "Proxy object disconnected");
      BluetoothHealth.access$102(BluetoothHealth.this, null);
      if (mServiceListener != null) {
        mServiceListener.onServiceDisconnected(3);
      }
    }
  };
  private Context mContext;
  private volatile IBluetoothHealth mService;
  private BluetoothProfile.ServiceListener mServiceListener;
  
  BluetoothHealth(Context paramContext, BluetoothProfile.ServiceListener paramServiceListener)
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
        Log.e("BluetoothHealth", "", paramContext);
      }
    }
    doBind();
  }
  
  private boolean checkAppParam(String paramString, int paramInt1, int paramInt2, BluetoothHealthCallback paramBluetoothHealthCallback)
  {
    if ((paramString != null) && ((paramInt1 == 1) || (paramInt1 == 2)) && ((paramInt2 == 10) || (paramInt2 == 11) || (paramInt2 == 12)) && (paramBluetoothHealthCallback != null)) {
      return (paramInt1 != 1) || (paramInt2 != 12);
    }
    return false;
  }
  
  private boolean isEnabled()
  {
    BluetoothAdapter localBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    if ((localBluetoothAdapter != null) && (localBluetoothAdapter.getState() == 12)) {
      return true;
    }
    log("Bluetooth is Not enabled");
    return false;
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
    Log.d("BluetoothHealth", paramString);
  }
  
  void close()
  {
    IBluetoothManager localIBluetoothManager = mAdapter.getBluetoothManager();
    if (localIBluetoothManager != null) {
      try
      {
        localIBluetoothManager.unregisterStateChangeCallback(mBluetoothStateChangeCallback);
      }
      catch (Exception localException1)
      {
        Log.e("BluetoothHealth", "", localException1);
      }
    }
    synchronized (mConnection)
    {
      IBluetoothHealth localIBluetoothHealth = mService;
      if (localIBluetoothHealth != null) {
        try
        {
          mService = null;
          mContext.unbindService(mConnection);
        }
        catch (Exception localException2)
        {
          Log.e("BluetoothHealth", "", localException2);
        }
      }
      mServiceListener = null;
      return;
    }
  }
  
  public boolean connectChannelToSink(BluetoothDevice paramBluetoothDevice, BluetoothHealthAppConfiguration paramBluetoothHealthAppConfiguration, int paramInt)
  {
    IBluetoothHealth localIBluetoothHealth = mService;
    if ((localIBluetoothHealth != null) && (isEnabled()) && (isValidDevice(paramBluetoothDevice)) && (paramBluetoothHealthAppConfiguration != null))
    {
      try
      {
        boolean bool = localIBluetoothHealth.connectChannelToSink(paramBluetoothDevice, paramBluetoothHealthAppConfiguration, paramInt);
        return bool;
      }
      catch (RemoteException paramBluetoothDevice)
      {
        Log.e("BluetoothHealth", paramBluetoothDevice.toString());
      }
    }
    else
    {
      Log.w("BluetoothHealth", "Proxy not attached to service");
      Log.d("BluetoothHealth", Log.getStackTraceString(new Throwable()));
    }
    return false;
  }
  
  public boolean connectChannelToSource(BluetoothDevice paramBluetoothDevice, BluetoothHealthAppConfiguration paramBluetoothHealthAppConfiguration)
  {
    IBluetoothHealth localIBluetoothHealth = mService;
    if ((localIBluetoothHealth != null) && (isEnabled()) && (isValidDevice(paramBluetoothDevice)) && (paramBluetoothHealthAppConfiguration != null))
    {
      try
      {
        boolean bool = localIBluetoothHealth.connectChannelToSource(paramBluetoothDevice, paramBluetoothHealthAppConfiguration);
        return bool;
      }
      catch (RemoteException paramBluetoothDevice)
      {
        Log.e("BluetoothHealth", paramBluetoothDevice.toString());
      }
    }
    else
    {
      Log.w("BluetoothHealth", "Proxy not attached to service");
      Log.d("BluetoothHealth", Log.getStackTraceString(new Throwable()));
    }
    return false;
  }
  
  public boolean disconnectChannel(BluetoothDevice paramBluetoothDevice, BluetoothHealthAppConfiguration paramBluetoothHealthAppConfiguration, int paramInt)
  {
    IBluetoothHealth localIBluetoothHealth = mService;
    if ((localIBluetoothHealth != null) && (isEnabled()) && (isValidDevice(paramBluetoothDevice)) && (paramBluetoothHealthAppConfiguration != null))
    {
      try
      {
        boolean bool = localIBluetoothHealth.disconnectChannel(paramBluetoothDevice, paramBluetoothHealthAppConfiguration, paramInt);
        return bool;
      }
      catch (RemoteException paramBluetoothDevice)
      {
        Log.e("BluetoothHealth", paramBluetoothDevice.toString());
      }
    }
    else
    {
      Log.w("BluetoothHealth", "Proxy not attached to service");
      Log.d("BluetoothHealth", Log.getStackTraceString(new Throwable()));
    }
    return false;
  }
  
  boolean doBind()
  {
    Intent localIntent = new Intent(IBluetoothHealth.class.getName());
    Object localObject = localIntent.resolveSystemService(mContext.getPackageManager(), 0);
    localIntent.setComponent((ComponentName)localObject);
    if ((localObject != null) && (mContext.bindServiceAsUser(localIntent, mConnection, 0, mContext.getUser()))) {
      return true;
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Could not bind to Bluetooth Health Service with ");
    ((StringBuilder)localObject).append(localIntent);
    Log.e("BluetoothHealth", ((StringBuilder)localObject).toString());
    return false;
  }
  
  public List<BluetoothDevice> getConnectedDevices()
  {
    Object localObject = mService;
    StringBuilder localStringBuilder;
    if ((localObject != null) && (isEnabled())) {
      try
      {
        localObject = ((IBluetoothHealth)localObject).getConnectedHealthDevices();
        return localObject;
      }
      catch (RemoteException localRemoteException)
      {
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("Stack:");
        localStringBuilder.append(Log.getStackTraceString(new Throwable()));
        Log.e("BluetoothHealth", localStringBuilder.toString());
        return new ArrayList();
      }
    }
    if (localStringBuilder == null) {
      Log.w("BluetoothHealth", "Proxy not attached to service");
    }
    return new ArrayList();
  }
  
  public int getConnectionState(BluetoothDevice paramBluetoothDevice)
  {
    IBluetoothHealth localIBluetoothHealth = mService;
    if ((localIBluetoothHealth != null) && (isEnabled()) && (isValidDevice(paramBluetoothDevice)))
    {
      try
      {
        int i = localIBluetoothHealth.getHealthDeviceConnectionState(paramBluetoothDevice);
        return i;
      }
      catch (RemoteException paramBluetoothDevice)
      {
        Log.e("BluetoothHealth", paramBluetoothDevice.toString());
      }
    }
    else
    {
      Log.w("BluetoothHealth", "Proxy not attached to service");
      Log.d("BluetoothHealth", Log.getStackTraceString(new Throwable()));
    }
    return 0;
  }
  
  public List<BluetoothDevice> getDevicesMatchingConnectionStates(int[] paramArrayOfInt)
  {
    IBluetoothHealth localIBluetoothHealth = mService;
    if ((localIBluetoothHealth != null) && (isEnabled())) {
      try
      {
        paramArrayOfInt = localIBluetoothHealth.getHealthDevicesMatchingConnectionStates(paramArrayOfInt);
        return paramArrayOfInt;
      }
      catch (RemoteException paramArrayOfInt)
      {
        paramArrayOfInt = new StringBuilder();
        paramArrayOfInt.append("Stack:");
        paramArrayOfInt.append(Log.getStackTraceString(new Throwable()));
        Log.e("BluetoothHealth", paramArrayOfInt.toString());
        return new ArrayList();
      }
    }
    if (localIBluetoothHealth == null) {
      Log.w("BluetoothHealth", "Proxy not attached to service");
    }
    return new ArrayList();
  }
  
  public ParcelFileDescriptor getMainChannelFd(BluetoothDevice paramBluetoothDevice, BluetoothHealthAppConfiguration paramBluetoothHealthAppConfiguration)
  {
    IBluetoothHealth localIBluetoothHealth = mService;
    if ((localIBluetoothHealth != null) && (isEnabled()) && (isValidDevice(paramBluetoothDevice)) && (paramBluetoothHealthAppConfiguration != null))
    {
      try
      {
        paramBluetoothDevice = localIBluetoothHealth.getMainChannelFd(paramBluetoothDevice, paramBluetoothHealthAppConfiguration);
        return paramBluetoothDevice;
      }
      catch (RemoteException paramBluetoothDevice)
      {
        Log.e("BluetoothHealth", paramBluetoothDevice.toString());
      }
    }
    else
    {
      Log.w("BluetoothHealth", "Proxy not attached to service");
      Log.d("BluetoothHealth", Log.getStackTraceString(new Throwable()));
    }
    return null;
  }
  
  public boolean registerAppConfiguration(String paramString, int paramInt1, int paramInt2, int paramInt3, BluetoothHealthCallback paramBluetoothHealthCallback)
  {
    boolean bool1 = false;
    boolean bool2 = false;
    if ((isEnabled()) && (checkAppParam(paramString, paramInt2, paramInt3, paramBluetoothHealthCallback)))
    {
      paramBluetoothHealthCallback = new BluetoothHealthCallbackWrapper(paramBluetoothHealthCallback);
      paramString = new BluetoothHealthAppConfiguration(paramString, paramInt1, paramInt2, paramInt3);
      IBluetoothHealth localIBluetoothHealth = mService;
      if (localIBluetoothHealth != null)
      {
        try
        {
          bool1 = localIBluetoothHealth.registerAppConfiguration(paramString, paramBluetoothHealthCallback);
          bool2 = bool1;
        }
        catch (RemoteException paramString)
        {
          for (;;)
          {
            Log.e("BluetoothHealth", paramString.toString());
          }
        }
      }
      else
      {
        Log.w("BluetoothHealth", "Proxy not attached to service");
        Log.d("BluetoothHealth", Log.getStackTraceString(new Throwable()));
        bool2 = bool1;
      }
      return bool2;
    }
    return false;
  }
  
  public boolean registerSinkAppConfiguration(String paramString, int paramInt, BluetoothHealthCallback paramBluetoothHealthCallback)
  {
    if ((isEnabled()) && (paramString != null)) {
      return registerAppConfiguration(paramString, paramInt, 2, 12, paramBluetoothHealthCallback);
    }
    return false;
  }
  
  public boolean unregisterAppConfiguration(BluetoothHealthAppConfiguration paramBluetoothHealthAppConfiguration)
  {
    boolean bool1 = false;
    boolean bool2 = false;
    IBluetoothHealth localIBluetoothHealth = mService;
    if ((localIBluetoothHealth != null) && (isEnabled()) && (paramBluetoothHealthAppConfiguration != null))
    {
      try
      {
        bool1 = localIBluetoothHealth.unregisterAppConfiguration(paramBluetoothHealthAppConfiguration);
        bool2 = bool1;
      }
      catch (RemoteException paramBluetoothHealthAppConfiguration)
      {
        for (;;)
        {
          Log.e("BluetoothHealth", paramBluetoothHealthAppConfiguration.toString());
        }
      }
    }
    else
    {
      Log.w("BluetoothHealth", "Proxy not attached to service");
      Log.d("BluetoothHealth", Log.getStackTraceString(new Throwable()));
      bool2 = bool1;
    }
    return bool2;
  }
  
  private static class BluetoothHealthCallbackWrapper
    extends IBluetoothHealthCallback.Stub
  {
    private BluetoothHealthCallback mCallback;
    
    public BluetoothHealthCallbackWrapper(BluetoothHealthCallback paramBluetoothHealthCallback)
    {
      mCallback = paramBluetoothHealthCallback;
    }
    
    public void onHealthAppConfigurationStatusChange(BluetoothHealthAppConfiguration paramBluetoothHealthAppConfiguration, int paramInt)
    {
      mCallback.onHealthAppConfigurationStatusChange(paramBluetoothHealthAppConfiguration, paramInt);
    }
    
    public void onHealthChannelStateChange(BluetoothHealthAppConfiguration paramBluetoothHealthAppConfiguration, BluetoothDevice paramBluetoothDevice, int paramInt1, int paramInt2, ParcelFileDescriptor paramParcelFileDescriptor, int paramInt3)
    {
      mCallback.onHealthChannelStateChange(paramBluetoothHealthAppConfiguration, paramBluetoothDevice, paramInt1, paramInt2, paramParcelFileDescriptor, paramInt3);
    }
  }
}
