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

public final class BluetoothHidHost
  implements BluetoothProfile
{
  public static final String ACTION_CONNECTION_STATE_CHANGED = "android.bluetooth.input.profile.action.CONNECTION_STATE_CHANGED";
  public static final String ACTION_HANDSHAKE = "android.bluetooth.input.profile.action.HANDSHAKE";
  public static final String ACTION_IDLE_TIME_CHANGED = "android.bluetooth.input.profile.action.IDLE_TIME_CHANGED";
  public static final String ACTION_PROTOCOL_MODE_CHANGED = "android.bluetooth.input.profile.action.PROTOCOL_MODE_CHANGED";
  public static final String ACTION_REPORT = "android.bluetooth.input.profile.action.REPORT";
  public static final String ACTION_VIRTUAL_UNPLUG_STATUS = "android.bluetooth.input.profile.action.VIRTUAL_UNPLUG_STATUS";
  private static final boolean DBG = true;
  public static final String EXTRA_IDLE_TIME = "android.bluetooth.BluetoothHidHost.extra.IDLE_TIME";
  public static final String EXTRA_PROTOCOL_MODE = "android.bluetooth.BluetoothHidHost.extra.PROTOCOL_MODE";
  public static final String EXTRA_REPORT = "android.bluetooth.BluetoothHidHost.extra.REPORT";
  public static final String EXTRA_REPORT_BUFFER_SIZE = "android.bluetooth.BluetoothHidHost.extra.REPORT_BUFFER_SIZE";
  public static final String EXTRA_REPORT_ID = "android.bluetooth.BluetoothHidHost.extra.REPORT_ID";
  public static final String EXTRA_REPORT_TYPE = "android.bluetooth.BluetoothHidHost.extra.REPORT_TYPE";
  public static final String EXTRA_STATUS = "android.bluetooth.BluetoothHidHost.extra.STATUS";
  public static final String EXTRA_VIRTUAL_UNPLUG_STATUS = "android.bluetooth.BluetoothHidHost.extra.VIRTUAL_UNPLUG_STATUS";
  public static final int INPUT_CONNECT_FAILED_ALREADY_CONNECTED = 5001;
  public static final int INPUT_CONNECT_FAILED_ATTEMPT_FAILED = 5002;
  public static final int INPUT_DISCONNECT_FAILED_NOT_CONNECTED = 5000;
  public static final int INPUT_OPERATION_GENERIC_FAILURE = 5003;
  public static final int INPUT_OPERATION_SUCCESS = 5004;
  public static final int PROTOCOL_BOOT_MODE = 1;
  public static final int PROTOCOL_REPORT_MODE = 0;
  public static final int PROTOCOL_UNSUPPORTED_MODE = 255;
  public static final byte REPORT_TYPE_FEATURE = 3;
  public static final byte REPORT_TYPE_INPUT = 1;
  public static final byte REPORT_TYPE_OUTPUT = 2;
  private static final String TAG = "BluetoothHidHost";
  private static final boolean VDBG = false;
  public static final int VIRTUAL_UNPLUG_STATUS_FAIL = 1;
  public static final int VIRTUAL_UNPLUG_STATUS_SUCCESS = 0;
  private BluetoothAdapter mAdapter;
  private final IBluetoothStateChangeCallback mBluetoothStateChangeCallback = new IBluetoothStateChangeCallback.Stub()
  {
    public void onBluetoothStateChange(boolean paramAnonymousBoolean)
    {
      ??? = new StringBuilder();
      ((StringBuilder)???).append("onBluetoothStateChange: up=");
      ((StringBuilder)???).append(paramAnonymousBoolean);
      Log.d("BluetoothHidHost", ((StringBuilder)???).toString());
      if (!paramAnonymousBoolean)
      {
        try
        {
          synchronized (mConnection)
          {
            BluetoothHidHost.access$102(BluetoothHidHost.this, null);
            mContext.unbindService(mConnection);
          }
        }
        catch (Exception localException1)
        {
          Log.e("BluetoothHidHost", "", localException1);
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
          Log.e("BluetoothHidHost", "", localException2);
        }
      }
    }
  };
  private final ServiceConnection mConnection = new ServiceConnection()
  {
    public void onServiceConnected(ComponentName paramAnonymousComponentName, IBinder paramAnonymousIBinder)
    {
      Log.d("BluetoothHidHost", "Proxy object connected");
      BluetoothHidHost.access$102(BluetoothHidHost.this, IBluetoothHidHost.Stub.asInterface(Binder.allowBlocking(paramAnonymousIBinder)));
      if (mServiceListener != null) {
        mServiceListener.onServiceConnected(4, BluetoothHidHost.this);
      }
    }
    
    public void onServiceDisconnected(ComponentName paramAnonymousComponentName)
    {
      Log.d("BluetoothHidHost", "Proxy object disconnected");
      BluetoothHidHost.access$102(BluetoothHidHost.this, null);
      if (mServiceListener != null) {
        mServiceListener.onServiceDisconnected(4);
      }
    }
  };
  private Context mContext;
  private volatile IBluetoothHidHost mService;
  private BluetoothProfile.ServiceListener mServiceListener;
  
  BluetoothHidHost(Context paramContext, BluetoothProfile.ServiceListener paramServiceListener)
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
        Log.e("BluetoothHidHost", "", paramContext);
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
    Log.d("BluetoothHidHost", paramString);
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
        Log.e("BluetoothHidHost", "", localException1);
      }
    }
    synchronized (mConnection)
    {
      IBluetoothHidHost localIBluetoothHidHost = mService;
      if (localIBluetoothHidHost != null) {
        try
        {
          mService = null;
          mContext.unbindService(mConnection);
        }
        catch (Exception localException2)
        {
          Log.e("BluetoothHidHost", "", localException2);
        }
      }
      mServiceListener = null;
      return;
    }
  }
  
  public boolean connect(BluetoothDevice paramBluetoothDevice)
  {
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("connect(");
    ((StringBuilder)localObject).append(paramBluetoothDevice);
    ((StringBuilder)localObject).append(")");
    log(((StringBuilder)localObject).toString());
    localObject = mService;
    if ((localObject != null) && (isEnabled()) && (isValidDevice(paramBluetoothDevice))) {
      try
      {
        boolean bool = ((IBluetoothHidHost)localObject).connect(paramBluetoothDevice);
        return bool;
      }
      catch (RemoteException paramBluetoothDevice)
      {
        paramBluetoothDevice = new StringBuilder();
        paramBluetoothDevice.append("Stack:");
        paramBluetoothDevice.append(Log.getStackTraceString(new Throwable()));
        Log.e("BluetoothHidHost", paramBluetoothDevice.toString());
        return false;
      }
    }
    if (localObject == null) {
      Log.w("BluetoothHidHost", "Proxy not attached to service");
    }
    return false;
  }
  
  public boolean disconnect(BluetoothDevice paramBluetoothDevice)
  {
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("disconnect(");
    ((StringBuilder)localObject).append(paramBluetoothDevice);
    ((StringBuilder)localObject).append(")");
    log(((StringBuilder)localObject).toString());
    localObject = mService;
    if ((localObject != null) && (isEnabled()) && (isValidDevice(paramBluetoothDevice))) {
      try
      {
        boolean bool = ((IBluetoothHidHost)localObject).disconnect(paramBluetoothDevice);
        return bool;
      }
      catch (RemoteException paramBluetoothDevice)
      {
        paramBluetoothDevice = new StringBuilder();
        paramBluetoothDevice.append("Stack:");
        paramBluetoothDevice.append(Log.getStackTraceString(new Throwable()));
        Log.e("BluetoothHidHost", paramBluetoothDevice.toString());
        return false;
      }
    }
    if (localObject == null) {
      Log.w("BluetoothHidHost", "Proxy not attached to service");
    }
    return false;
  }
  
  boolean doBind()
  {
    Intent localIntent = new Intent(IBluetoothHidHost.class.getName());
    Object localObject = localIntent.resolveSystemService(mContext.getPackageManager(), 0);
    localIntent.setComponent((ComponentName)localObject);
    if ((localObject != null) && (mContext.bindServiceAsUser(localIntent, mConnection, 0, mContext.getUser()))) {
      return true;
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Could not bind to Bluetooth HID Service with ");
    ((StringBuilder)localObject).append(localIntent);
    Log.e("BluetoothHidHost", ((StringBuilder)localObject).toString());
    return false;
  }
  
  public List<BluetoothDevice> getConnectedDevices()
  {
    Object localObject = mService;
    StringBuilder localStringBuilder;
    if ((localObject != null) && (isEnabled())) {
      try
      {
        localObject = ((IBluetoothHidHost)localObject).getConnectedDevices();
        return localObject;
      }
      catch (RemoteException localRemoteException)
      {
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("Stack:");
        localStringBuilder.append(Log.getStackTraceString(new Throwable()));
        Log.e("BluetoothHidHost", localStringBuilder.toString());
        return new ArrayList();
      }
    }
    if (localStringBuilder == null) {
      Log.w("BluetoothHidHost", "Proxy not attached to service");
    }
    return new ArrayList();
  }
  
  public int getConnectionState(BluetoothDevice paramBluetoothDevice)
  {
    IBluetoothHidHost localIBluetoothHidHost = mService;
    if ((localIBluetoothHidHost != null) && (isEnabled()) && (isValidDevice(paramBluetoothDevice))) {
      try
      {
        int i = localIBluetoothHidHost.getConnectionState(paramBluetoothDevice);
        return i;
      }
      catch (RemoteException paramBluetoothDevice)
      {
        paramBluetoothDevice = new StringBuilder();
        paramBluetoothDevice.append("Stack:");
        paramBluetoothDevice.append(Log.getStackTraceString(new Throwable()));
        Log.e("BluetoothHidHost", paramBluetoothDevice.toString());
        return 0;
      }
    }
    if (localIBluetoothHidHost == null) {
      Log.w("BluetoothHidHost", "Proxy not attached to service");
    }
    return 0;
  }
  
  public List<BluetoothDevice> getDevicesMatchingConnectionStates(int[] paramArrayOfInt)
  {
    IBluetoothHidHost localIBluetoothHidHost = mService;
    if ((localIBluetoothHidHost != null) && (isEnabled())) {
      try
      {
        paramArrayOfInt = localIBluetoothHidHost.getDevicesMatchingConnectionStates(paramArrayOfInt);
        return paramArrayOfInt;
      }
      catch (RemoteException paramArrayOfInt)
      {
        paramArrayOfInt = new StringBuilder();
        paramArrayOfInt.append("Stack:");
        paramArrayOfInt.append(Log.getStackTraceString(new Throwable()));
        Log.e("BluetoothHidHost", paramArrayOfInt.toString());
        return new ArrayList();
      }
    }
    if (localIBluetoothHidHost == null) {
      Log.w("BluetoothHidHost", "Proxy not attached to service");
    }
    return new ArrayList();
  }
  
  public boolean getIdleTime(BluetoothDevice paramBluetoothDevice)
  {
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("getIdletime(");
    ((StringBuilder)localObject).append(paramBluetoothDevice);
    ((StringBuilder)localObject).append(")");
    log(((StringBuilder)localObject).toString());
    localObject = mService;
    if ((localObject != null) && (isEnabled()) && (isValidDevice(paramBluetoothDevice))) {
      try
      {
        boolean bool = ((IBluetoothHidHost)localObject).getIdleTime(paramBluetoothDevice);
        return bool;
      }
      catch (RemoteException paramBluetoothDevice)
      {
        paramBluetoothDevice = new StringBuilder();
        paramBluetoothDevice.append("Stack:");
        paramBluetoothDevice.append(Log.getStackTraceString(new Throwable()));
        Log.e("BluetoothHidHost", paramBluetoothDevice.toString());
        return false;
      }
    }
    if (localObject == null) {
      Log.w("BluetoothHidHost", "Proxy not attached to service");
    }
    return false;
  }
  
  public int getPriority(BluetoothDevice paramBluetoothDevice)
  {
    IBluetoothHidHost localIBluetoothHidHost = mService;
    if ((localIBluetoothHidHost != null) && (isEnabled()) && (isValidDevice(paramBluetoothDevice))) {
      try
      {
        int i = localIBluetoothHidHost.getPriority(paramBluetoothDevice);
        return i;
      }
      catch (RemoteException paramBluetoothDevice)
      {
        paramBluetoothDevice = new StringBuilder();
        paramBluetoothDevice.append("Stack:");
        paramBluetoothDevice.append(Log.getStackTraceString(new Throwable()));
        Log.e("BluetoothHidHost", paramBluetoothDevice.toString());
        return 0;
      }
    }
    if (localIBluetoothHidHost == null) {
      Log.w("BluetoothHidHost", "Proxy not attached to service");
    }
    return 0;
  }
  
  public boolean getProtocolMode(BluetoothDevice paramBluetoothDevice)
  {
    IBluetoothHidHost localIBluetoothHidHost = mService;
    if ((localIBluetoothHidHost != null) && (isEnabled()) && (isValidDevice(paramBluetoothDevice))) {
      try
      {
        boolean bool = localIBluetoothHidHost.getProtocolMode(paramBluetoothDevice);
        return bool;
      }
      catch (RemoteException paramBluetoothDevice)
      {
        paramBluetoothDevice = new StringBuilder();
        paramBluetoothDevice.append("Stack:");
        paramBluetoothDevice.append(Log.getStackTraceString(new Throwable()));
        Log.e("BluetoothHidHost", paramBluetoothDevice.toString());
        return false;
      }
    }
    if (localIBluetoothHidHost == null) {
      Log.w("BluetoothHidHost", "Proxy not attached to service");
    }
    return false;
  }
  
  public boolean getReport(BluetoothDevice paramBluetoothDevice, byte paramByte1, byte paramByte2, int paramInt)
  {
    IBluetoothHidHost localIBluetoothHidHost = mService;
    if ((localIBluetoothHidHost != null) && (isEnabled()) && (isValidDevice(paramBluetoothDevice))) {
      try
      {
        boolean bool = localIBluetoothHidHost.getReport(paramBluetoothDevice, paramByte1, paramByte2, paramInt);
        return bool;
      }
      catch (RemoteException paramBluetoothDevice)
      {
        paramBluetoothDevice = new StringBuilder();
        paramBluetoothDevice.append("Stack:");
        paramBluetoothDevice.append(Log.getStackTraceString(new Throwable()));
        Log.e("BluetoothHidHost", paramBluetoothDevice.toString());
        return false;
      }
    }
    if (localIBluetoothHidHost == null) {
      Log.w("BluetoothHidHost", "Proxy not attached to service");
    }
    return false;
  }
  
  public boolean sendData(BluetoothDevice paramBluetoothDevice, String paramString)
  {
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("sendData(");
    ((StringBuilder)localObject).append(paramBluetoothDevice);
    ((StringBuilder)localObject).append("), report=");
    ((StringBuilder)localObject).append(paramString);
    log(((StringBuilder)localObject).toString());
    localObject = mService;
    if ((localObject != null) && (isEnabled()) && (isValidDevice(paramBluetoothDevice))) {
      try
      {
        boolean bool = ((IBluetoothHidHost)localObject).sendData(paramBluetoothDevice, paramString);
        return bool;
      }
      catch (RemoteException paramBluetoothDevice)
      {
        paramBluetoothDevice = new StringBuilder();
        paramBluetoothDevice.append("Stack:");
        paramBluetoothDevice.append(Log.getStackTraceString(new Throwable()));
        Log.e("BluetoothHidHost", paramBluetoothDevice.toString());
        return false;
      }
    }
    if (localObject == null) {
      Log.w("BluetoothHidHost", "Proxy not attached to service");
    }
    return false;
  }
  
  public boolean setIdleTime(BluetoothDevice paramBluetoothDevice, byte paramByte)
  {
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("setIdletime(");
    ((StringBuilder)localObject).append(paramBluetoothDevice);
    ((StringBuilder)localObject).append("), idleTime=");
    ((StringBuilder)localObject).append(paramByte);
    log(((StringBuilder)localObject).toString());
    localObject = mService;
    if ((localObject != null) && (isEnabled()) && (isValidDevice(paramBluetoothDevice))) {
      try
      {
        boolean bool = ((IBluetoothHidHost)localObject).setIdleTime(paramBluetoothDevice, paramByte);
        return bool;
      }
      catch (RemoteException paramBluetoothDevice)
      {
        paramBluetoothDevice = new StringBuilder();
        paramBluetoothDevice.append("Stack:");
        paramBluetoothDevice.append(Log.getStackTraceString(new Throwable()));
        Log.e("BluetoothHidHost", paramBluetoothDevice.toString());
        return false;
      }
    }
    if (localObject == null) {
      Log.w("BluetoothHidHost", "Proxy not attached to service");
    }
    return false;
  }
  
  public boolean setPriority(BluetoothDevice paramBluetoothDevice, int paramInt)
  {
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("setPriority(");
    ((StringBuilder)localObject).append(paramBluetoothDevice);
    ((StringBuilder)localObject).append(", ");
    ((StringBuilder)localObject).append(paramInt);
    ((StringBuilder)localObject).append(")");
    log(((StringBuilder)localObject).toString());
    localObject = mService;
    if ((localObject != null) && (isEnabled()) && (isValidDevice(paramBluetoothDevice)))
    {
      if ((paramInt != 0) && (paramInt != 100)) {
        return false;
      }
      try
      {
        boolean bool = ((IBluetoothHidHost)localObject).setPriority(paramBluetoothDevice, paramInt);
        return bool;
      }
      catch (RemoteException paramBluetoothDevice)
      {
        paramBluetoothDevice = new StringBuilder();
        paramBluetoothDevice.append("Stack:");
        paramBluetoothDevice.append(Log.getStackTraceString(new Throwable()));
        Log.e("BluetoothHidHost", paramBluetoothDevice.toString());
        return false;
      }
    }
    if (localObject == null) {
      Log.w("BluetoothHidHost", "Proxy not attached to service");
    }
    return false;
  }
  
  public boolean setProtocolMode(BluetoothDevice paramBluetoothDevice, int paramInt)
  {
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("setProtocolMode(");
    ((StringBuilder)localObject).append(paramBluetoothDevice);
    ((StringBuilder)localObject).append(")");
    log(((StringBuilder)localObject).toString());
    localObject = mService;
    if ((localObject != null) && (isEnabled()) && (isValidDevice(paramBluetoothDevice))) {
      try
      {
        boolean bool = ((IBluetoothHidHost)localObject).setProtocolMode(paramBluetoothDevice, paramInt);
        return bool;
      }
      catch (RemoteException paramBluetoothDevice)
      {
        paramBluetoothDevice = new StringBuilder();
        paramBluetoothDevice.append("Stack:");
        paramBluetoothDevice.append(Log.getStackTraceString(new Throwable()));
        Log.e("BluetoothHidHost", paramBluetoothDevice.toString());
        return false;
      }
    }
    if (localObject == null) {
      Log.w("BluetoothHidHost", "Proxy not attached to service");
    }
    return false;
  }
  
  public boolean setReport(BluetoothDevice paramBluetoothDevice, byte paramByte, String paramString)
  {
    IBluetoothHidHost localIBluetoothHidHost = mService;
    if ((localIBluetoothHidHost != null) && (isEnabled()) && (isValidDevice(paramBluetoothDevice))) {
      try
      {
        boolean bool = localIBluetoothHidHost.setReport(paramBluetoothDevice, paramByte, paramString);
        return bool;
      }
      catch (RemoteException paramBluetoothDevice)
      {
        paramBluetoothDevice = new StringBuilder();
        paramBluetoothDevice.append("Stack:");
        paramBluetoothDevice.append(Log.getStackTraceString(new Throwable()));
        Log.e("BluetoothHidHost", paramBluetoothDevice.toString());
        return false;
      }
    }
    if (localIBluetoothHidHost == null) {
      Log.w("BluetoothHidHost", "Proxy not attached to service");
    }
    return false;
  }
  
  public boolean virtualUnplug(BluetoothDevice paramBluetoothDevice)
  {
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("virtualUnplug(");
    ((StringBuilder)localObject).append(paramBluetoothDevice);
    ((StringBuilder)localObject).append(")");
    log(((StringBuilder)localObject).toString());
    localObject = mService;
    if ((localObject != null) && (isEnabled()) && (isValidDevice(paramBluetoothDevice))) {
      try
      {
        boolean bool = ((IBluetoothHidHost)localObject).virtualUnplug(paramBluetoothDevice);
        return bool;
      }
      catch (RemoteException paramBluetoothDevice)
      {
        paramBluetoothDevice = new StringBuilder();
        paramBluetoothDevice.append("Stack:");
        paramBluetoothDevice.append(Log.getStackTraceString(new Throwable()));
        Log.e("BluetoothHidHost", paramBluetoothDevice.toString());
        return false;
      }
    }
    if (localObject == null) {
      Log.w("BluetoothHidHost", "Proxy not attached to service");
    }
    return false;
  }
}
