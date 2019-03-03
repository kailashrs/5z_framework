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

public final class BluetoothPan
  implements BluetoothProfile
{
  public static final String ACTION_CONNECTION_STATE_CHANGED = "android.bluetooth.pan.profile.action.CONNECTION_STATE_CHANGED";
  private static final boolean DBG = true;
  public static final String EXTRA_LOCAL_ROLE = "android.bluetooth.pan.extra.LOCAL_ROLE";
  public static final int LOCAL_NAP_ROLE = 1;
  public static final int LOCAL_PANU_ROLE = 2;
  public static final int PAN_CONNECT_FAILED_ALREADY_CONNECTED = 1001;
  public static final int PAN_CONNECT_FAILED_ATTEMPT_FAILED = 1002;
  public static final int PAN_DISCONNECT_FAILED_NOT_CONNECTED = 1000;
  public static final int PAN_OPERATION_GENERIC_FAILURE = 1003;
  public static final int PAN_OPERATION_SUCCESS = 1004;
  public static final int PAN_ROLE_NONE = 0;
  public static final int REMOTE_NAP_ROLE = 1;
  public static final int REMOTE_PANU_ROLE = 2;
  private static final String TAG = "BluetoothPan";
  private static final boolean VDBG = false;
  private BluetoothAdapter mAdapter;
  private final ServiceConnection mConnection = new ServiceConnection()
  {
    public void onServiceConnected(ComponentName paramAnonymousComponentName, IBinder paramAnonymousIBinder)
    {
      Log.d("BluetoothPan", "BluetoothPAN Proxy object connected");
      BluetoothPan.access$002(BluetoothPan.this, IBluetoothPan.Stub.asInterface(Binder.allowBlocking(paramAnonymousIBinder)));
      if (mServiceListener != null) {
        mServiceListener.onServiceConnected(5, BluetoothPan.this);
      }
    }
    
    public void onServiceDisconnected(ComponentName paramAnonymousComponentName)
    {
      Log.d("BluetoothPan", "BluetoothPAN Proxy object disconnected");
      BluetoothPan.access$002(BluetoothPan.this, null);
      if (mServiceListener != null) {
        mServiceListener.onServiceDisconnected(5);
      }
    }
  };
  private Context mContext;
  private volatile IBluetoothPan mPanService;
  private BluetoothProfile.ServiceListener mServiceListener;
  private final IBluetoothStateChangeCallback mStateChangeCallback = new IBluetoothStateChangeCallback.Stub()
  {
    public void onBluetoothStateChange(boolean paramAnonymousBoolean)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("onBluetoothStateChange on: ");
      localStringBuilder.append(paramAnonymousBoolean);
      Log.d("BluetoothPan", localStringBuilder.toString());
      if (paramAnonymousBoolean) {
        try
        {
          if (mPanService == null) {
            doBind();
          }
        }
        catch (SecurityException localSecurityException)
        {
          Log.e("BluetoothPan", "onBluetoothStateChange: could not bind to PAN service: ", localSecurityException);
        }
        catch (IllegalStateException localIllegalStateException)
        {
          Log.e("BluetoothPan", "onBluetoothStateChange: could not bind to PAN service: ", localIllegalStateException);
        }
      } else {
        try
        {
          synchronized (mConnection)
          {
            BluetoothPan.access$002(BluetoothPan.this, null);
            mContext.unbindService(mConnection);
          }
          return;
        }
        catch (Exception localException)
        {
          Log.e("BluetoothPan", "", localException);
        }
      }
    }
  };
  
  BluetoothPan(Context paramContext, BluetoothProfile.ServiceListener paramServiceListener)
  {
    mContext = paramContext;
    mServiceListener = paramServiceListener;
    mAdapter = BluetoothAdapter.getDefaultAdapter();
    try
    {
      mAdapter.getBluetoothManager().registerStateChangeCallback(mStateChangeCallback);
    }
    catch (RemoteException paramContext)
    {
      Log.w("BluetoothPan", "Unable to register BluetoothStateChangeCallback", paramContext);
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
    Log.d("BluetoothPan", paramString);
  }
  
  void close()
  {
    IBluetoothManager localIBluetoothManager = mAdapter.getBluetoothManager();
    if (localIBluetoothManager != null) {
      try
      {
        localIBluetoothManager.unregisterStateChangeCallback(mStateChangeCallback);
      }
      catch (RemoteException localRemoteException)
      {
        Log.w("BluetoothPan", "Unable to unregister BluetoothStateChangeCallback", localRemoteException);
      }
    }
    synchronized (mConnection)
    {
      IBluetoothPan localIBluetoothPan = mPanService;
      if (localIBluetoothPan != null) {
        try
        {
          mPanService = null;
          mContext.unbindService(mConnection);
        }
        catch (Exception localException)
        {
          Log.e("BluetoothPan", "", localException);
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
    localObject = mPanService;
    if ((localObject != null) && (isEnabled()) && (isValidDevice(paramBluetoothDevice))) {
      try
      {
        boolean bool = ((IBluetoothPan)localObject).connect(paramBluetoothDevice);
        return bool;
      }
      catch (RemoteException paramBluetoothDevice)
      {
        paramBluetoothDevice = new StringBuilder();
        paramBluetoothDevice.append("Stack:");
        paramBluetoothDevice.append(Log.getStackTraceString(new Throwable()));
        Log.e("BluetoothPan", paramBluetoothDevice.toString());
        return false;
      }
    }
    if (localObject == null) {
      Log.w("BluetoothPan", "Proxy not attached to service");
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
    localObject = mPanService;
    if ((localObject != null) && (isEnabled()) && (isValidDevice(paramBluetoothDevice))) {
      try
      {
        boolean bool = ((IBluetoothPan)localObject).disconnect(paramBluetoothDevice);
        return bool;
      }
      catch (RemoteException paramBluetoothDevice)
      {
        paramBluetoothDevice = new StringBuilder();
        paramBluetoothDevice.append("Stack:");
        paramBluetoothDevice.append(Log.getStackTraceString(new Throwable()));
        Log.e("BluetoothPan", paramBluetoothDevice.toString());
        return false;
      }
    }
    if (localObject == null) {
      Log.w("BluetoothPan", "Proxy not attached to service");
    }
    return false;
  }
  
  boolean doBind()
  {
    Intent localIntent = new Intent(IBluetoothPan.class.getName());
    Object localObject = localIntent.resolveSystemService(mContext.getPackageManager(), 0);
    localIntent.setComponent((ComponentName)localObject);
    if ((localObject != null) && (mContext.bindServiceAsUser(localIntent, mConnection, 0, mContext.getUser()))) {
      return true;
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Could not bind to Bluetooth Pan Service with ");
    ((StringBuilder)localObject).append(localIntent);
    Log.e("BluetoothPan", ((StringBuilder)localObject).toString());
    return false;
  }
  
  protected void finalize()
  {
    close();
  }
  
  public List<BluetoothDevice> getConnectedDevices()
  {
    Object localObject = mPanService;
    StringBuilder localStringBuilder;
    if ((localObject != null) && (isEnabled())) {
      try
      {
        localObject = ((IBluetoothPan)localObject).getConnectedDevices();
        return localObject;
      }
      catch (RemoteException localRemoteException)
      {
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("Stack:");
        localStringBuilder.append(Log.getStackTraceString(new Throwable()));
        Log.e("BluetoothPan", localStringBuilder.toString());
        return new ArrayList();
      }
    }
    if (localStringBuilder == null) {
      Log.w("BluetoothPan", "Proxy not attached to service");
    }
    return new ArrayList();
  }
  
  public int getConnectionState(BluetoothDevice paramBluetoothDevice)
  {
    IBluetoothPan localIBluetoothPan = mPanService;
    if ((localIBluetoothPan != null) && (isEnabled()) && (isValidDevice(paramBluetoothDevice))) {
      try
      {
        int i = localIBluetoothPan.getConnectionState(paramBluetoothDevice);
        return i;
      }
      catch (RemoteException paramBluetoothDevice)
      {
        paramBluetoothDevice = new StringBuilder();
        paramBluetoothDevice.append("Stack:");
        paramBluetoothDevice.append(Log.getStackTraceString(new Throwable()));
        Log.e("BluetoothPan", paramBluetoothDevice.toString());
        return 0;
      }
    }
    if (localIBluetoothPan == null) {
      Log.w("BluetoothPan", "Proxy not attached to service");
    }
    return 0;
  }
  
  public List<BluetoothDevice> getDevicesMatchingConnectionStates(int[] paramArrayOfInt)
  {
    IBluetoothPan localIBluetoothPan = mPanService;
    if ((localIBluetoothPan != null) && (isEnabled())) {
      try
      {
        paramArrayOfInt = localIBluetoothPan.getDevicesMatchingConnectionStates(paramArrayOfInt);
        return paramArrayOfInt;
      }
      catch (RemoteException paramArrayOfInt)
      {
        paramArrayOfInt = new StringBuilder();
        paramArrayOfInt.append("Stack:");
        paramArrayOfInt.append(Log.getStackTraceString(new Throwable()));
        Log.e("BluetoothPan", paramArrayOfInt.toString());
        return new ArrayList();
      }
    }
    if (localIBluetoothPan == null) {
      Log.w("BluetoothPan", "Proxy not attached to service");
    }
    return new ArrayList();
  }
  
  public boolean isTetheringOn()
  {
    IBluetoothPan localIBluetoothPan = mPanService;
    if ((localIBluetoothPan != null) && (isEnabled())) {
      try
      {
        boolean bool = localIBluetoothPan.isTetheringOn();
        return bool;
      }
      catch (RemoteException localRemoteException)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Stack:");
        localStringBuilder.append(Log.getStackTraceString(new Throwable()));
        Log.e("BluetoothPan", localStringBuilder.toString());
      }
    }
    return false;
  }
  
  public void setBluetoothTethering(boolean paramBoolean)
  {
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("setBluetoothTethering(");
    ((StringBuilder)localObject).append(paramBoolean);
    ((StringBuilder)localObject).append(")");
    log(((StringBuilder)localObject).toString());
    localObject = mPanService;
    if ((localObject != null) && (isEnabled())) {
      try
      {
        ((IBluetoothPan)localObject).setBluetoothTethering(paramBoolean);
      }
      catch (RemoteException localRemoteException)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Stack:");
        localStringBuilder.append(Log.getStackTraceString(new Throwable()));
        Log.e("BluetoothPan", localStringBuilder.toString());
      }
    }
  }
}
