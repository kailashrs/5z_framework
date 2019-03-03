package android.bluetooth;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public final class BluetoothDun
  implements BluetoothProfile
{
  public static final String ACTION_CONNECTION_STATE_CHANGED = "codeaurora.bluetooth.dun.profile.action.CONNECTION_STATE_CHANGED";
  private static final boolean DBG = false;
  private static final String TAG = "BluetoothDun";
  private static final boolean VDBG = false;
  private BluetoothAdapter mAdapter;
  private ServiceConnection mConnection = new ServiceConnection()
  {
    public void onServiceConnected(ComponentName paramAnonymousComponentName, IBinder paramAnonymousIBinder)
    {
      BluetoothDun.access$002(BluetoothDun.this, IBluetoothDun.Stub.asInterface(paramAnonymousIBinder));
      if (mServiceListener != null) {
        mServiceListener.onServiceConnected(22, BluetoothDun.this);
      }
    }
    
    public void onServiceDisconnected(ComponentName paramAnonymousComponentName)
    {
      BluetoothDun.access$002(BluetoothDun.this, null);
      if (mServiceListener != null) {
        mServiceListener.onServiceDisconnected(22);
      }
    }
  };
  private Context mContext;
  private IBluetoothDun mDunService;
  private BluetoothProfile.ServiceListener mServiceListener;
  private IBluetoothStateChangeCallback mStateChangeCallback = new IBluetoothStateChangeCallback.Stub()
  {
    public void onBluetoothStateChange(boolean paramAnonymousBoolean)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("onBluetoothStateChange on: ");
      localStringBuilder.append(paramAnonymousBoolean);
      Log.d("BluetoothDun", localStringBuilder.toString());
      if (paramAnonymousBoolean) {
        try
        {
          if (mDunService == null)
          {
            Log.d("BluetoothDun", "onBluetoothStateChange call bindService");
            doBind();
          }
        }
        catch (SecurityException localSecurityException)
        {
          Log.e("BluetoothDun", "onBluetoothStateChange: could not bind to DUN service: ", localSecurityException);
        }
        catch (IllegalStateException localIllegalStateException)
        {
          Log.e("BluetoothDun", "onBluetoothStateChange: could not bind to DUN service: ", localIllegalStateException);
        }
      }
      synchronized (mConnection)
      {
        IBluetoothDun localIBluetoothDun = mDunService;
        if (localIBluetoothDun != null) {
          try
          {
            BluetoothDun.access$002(BluetoothDun.this, null);
            mContext.unbindService(mConnection);
          }
          catch (Exception localException)
          {
            Log.e("BluetoothDun", "", localException);
          }
        }
        return;
      }
    }
  };
  
  BluetoothDun(Context paramContext, BluetoothProfile.ServiceListener paramServiceListener)
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
      Log.w("BluetoothDun", "Unable to register BluetoothStateChangeCallback", paramContext);
    }
    Log.d("BluetoothDun", "BluetoothDun() call bindService");
    doBind();
  }
  
  private boolean isEnabled()
  {
    return mAdapter.getState() == 12;
  }
  
  private boolean isValidDevice(BluetoothDevice paramBluetoothDevice)
  {
    if (paramBluetoothDevice == null) {
      return false;
    }
    return BluetoothAdapter.checkBluetoothAddress(paramBluetoothDevice.getAddress());
  }
  
  private static void log(String paramString)
  {
    Log.d("BluetoothDun", paramString);
  }
  
  void close()
  {
    mServiceListener = null;
    IBluetoothManager localIBluetoothManager = mAdapter.getBluetoothManager();
    if (localIBluetoothManager != null) {
      try
      {
        localIBluetoothManager.unregisterStateChangeCallback(mStateChangeCallback);
      }
      catch (RemoteException localRemoteException)
      {
        Log.w("BluetoothDun", "Unable to unregister BluetoothStateChangeCallback", localRemoteException);
      }
    }
    synchronized (mConnection)
    {
      IBluetoothDun localIBluetoothDun = mDunService;
      if (localIBluetoothDun != null) {
        try
        {
          mDunService = null;
          mContext.unbindService(mConnection);
        }
        catch (Exception localException)
        {
          Log.e("BluetoothDun", "", localException);
        }
      }
      return;
    }
  }
  
  public boolean disconnect(BluetoothDevice paramBluetoothDevice)
  {
    if ((mDunService != null) && (isEnabled()) && (isValidDevice(paramBluetoothDevice))) {
      try
      {
        boolean bool = mDunService.disconnect(paramBluetoothDevice);
        return bool;
      }
      catch (RemoteException paramBluetoothDevice)
      {
        paramBluetoothDevice = new StringBuilder();
        paramBluetoothDevice.append("Stack:");
        paramBluetoothDevice.append(Log.getStackTraceString(new Throwable()));
        Log.e("BluetoothDun", paramBluetoothDevice.toString());
        return false;
      }
    }
    if (mDunService == null) {
      Log.w("BluetoothDun", "Proxy not attached to service");
    }
    return false;
  }
  
  boolean doBind()
  {
    Intent localIntent = new Intent(IBluetoothDun.class.getName());
    Object localObject = localIntent.resolveSystemService(mContext.getPackageManager(), 0);
    localIntent.setComponent((ComponentName)localObject);
    if ((localObject != null) && (mContext.bindServiceAsUser(localIntent, mConnection, 0, Process.myUserHandle()))) {
      return true;
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Could not bind to Bluetooth Dun Service with ");
    ((StringBuilder)localObject).append(localIntent);
    Log.e("BluetoothDun", ((StringBuilder)localObject).toString());
    return false;
  }
  
  protected void finalize()
  {
    close();
  }
  
  public List<BluetoothDevice> getConnectedDevices()
  {
    if ((mDunService != null) && (isEnabled())) {
      try
      {
        List localList = mDunService.getConnectedDevices();
        return localList;
      }
      catch (RemoteException localRemoteException)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Stack:");
        localStringBuilder.append(Log.getStackTraceString(new Throwable()));
        Log.e("BluetoothDun", localStringBuilder.toString());
        return new ArrayList();
      }
    }
    if (mDunService == null) {
      Log.w("BluetoothDun", "Proxy not attached to service");
    }
    return new ArrayList();
  }
  
  public int getConnectionState(BluetoothDevice paramBluetoothDevice)
  {
    if ((mDunService != null) && (isEnabled()) && (isValidDevice(paramBluetoothDevice))) {
      try
      {
        int i = mDunService.getConnectionState(paramBluetoothDevice);
        return i;
      }
      catch (RemoteException paramBluetoothDevice)
      {
        paramBluetoothDevice = new StringBuilder();
        paramBluetoothDevice.append("Stack:");
        paramBluetoothDevice.append(Log.getStackTraceString(new Throwable()));
        Log.e("BluetoothDun", paramBluetoothDevice.toString());
        return 0;
      }
    }
    if (mDunService == null) {
      Log.w("BluetoothDun", "Proxy not attached to service");
    }
    return 0;
  }
  
  public List<BluetoothDevice> getDevicesMatchingConnectionStates(int[] paramArrayOfInt)
  {
    if ((mDunService != null) && (isEnabled())) {
      try
      {
        paramArrayOfInt = mDunService.getDevicesMatchingConnectionStates(paramArrayOfInt);
        return paramArrayOfInt;
      }
      catch (RemoteException paramArrayOfInt)
      {
        paramArrayOfInt = new StringBuilder();
        paramArrayOfInt.append("Stack:");
        paramArrayOfInt.append(Log.getStackTraceString(new Throwable()));
        Log.e("BluetoothDun", paramArrayOfInt.toString());
        return new ArrayList();
      }
    }
    if (mDunService == null) {
      Log.w("BluetoothDun", "Proxy not attached to service");
    }
    return new ArrayList();
  }
}
