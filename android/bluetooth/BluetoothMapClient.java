package android.bluetooth;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public final class BluetoothMapClient
  implements BluetoothProfile
{
  public static final String ACTION_CONNECTION_STATE_CHANGED = "android.bluetooth.mapmce.profile.action.CONNECTION_STATE_CHANGED";
  public static final String ACTION_MESSAGE_DELIVERED_SUCCESSFULLY = "android.bluetooth.mapmce.profile.action.MESSAGE_DELIVERED_SUCCESSFULLY";
  public static final String ACTION_MESSAGE_RECEIVED = "android.bluetooth.mapmce.profile.action.MESSAGE_RECEIVED";
  public static final String ACTION_MESSAGE_SENT_SUCCESSFULLY = "android.bluetooth.mapmce.profile.action.MESSAGE_SENT_SUCCESSFULLY";
  private static final boolean DBG = Log.isLoggable("BluetoothMapClient", 3);
  public static final String EXTRA_MESSAGE_HANDLE = "android.bluetooth.mapmce.profile.extra.MESSAGE_HANDLE";
  public static final String EXTRA_SENDER_CONTACT_NAME = "android.bluetooth.mapmce.profile.extra.SENDER_CONTACT_NAME";
  public static final String EXTRA_SENDER_CONTACT_URI = "android.bluetooth.mapmce.profile.extra.SENDER_CONTACT_URI";
  public static final int RESULT_CANCELED = 2;
  public static final int RESULT_FAILURE = 0;
  public static final int RESULT_SUCCESS = 1;
  public static final int STATE_ERROR = -1;
  private static final String TAG = "BluetoothMapClient";
  private static final boolean VDBG = Log.isLoggable("BluetoothMapClient", 2);
  private BluetoothAdapter mAdapter;
  private final IBluetoothStateChangeCallback mBluetoothStateChangeCallback = new IBluetoothStateChangeCallback.Stub()
  {
    public void onBluetoothStateChange(boolean paramAnonymousBoolean)
    {
      if (BluetoothMapClient.DBG)
      {
        ??? = new StringBuilder();
        ((StringBuilder)???).append("onBluetoothStateChange: up=");
        ((StringBuilder)???).append(paramAnonymousBoolean);
        Log.d("BluetoothMapClient", ((StringBuilder)???).toString());
      }
      if (!paramAnonymousBoolean)
      {
        if (BluetoothMapClient.VDBG) {
          Log.d("BluetoothMapClient", "Unbinding service...");
        }
        try
        {
          synchronized (mConnection)
          {
            BluetoothMapClient.access$302(BluetoothMapClient.this, null);
            mContext.unbindService(mConnection);
          }
        }
        catch (Exception localException1)
        {
          Log.e("BluetoothMapClient", "", localException1);
        }
        throw localException1;
      }
      else
      {
        try
        {
          synchronized (mConnection)
          {
            if (mService == null)
            {
              if (BluetoothMapClient.VDBG) {
                Log.d("BluetoothMapClient", "Binding service...");
              }
              doBind();
            }
          }
          return;
        }
        catch (Exception localException2)
        {
          Log.e("BluetoothMapClient", "", localException2);
        }
      }
    }
  };
  private final ServiceConnection mConnection = new ServiceConnection()
  {
    public void onServiceConnected(ComponentName paramAnonymousComponentName, IBinder paramAnonymousIBinder)
    {
      if (BluetoothMapClient.DBG) {
        Log.d("BluetoothMapClient", "Proxy object connected");
      }
      BluetoothMapClient.access$302(BluetoothMapClient.this, IBluetoothMapClient.Stub.asInterface(paramAnonymousIBinder));
      if (mServiceListener != null) {
        mServiceListener.onServiceConnected(18, BluetoothMapClient.this);
      }
    }
    
    public void onServiceDisconnected(ComponentName paramAnonymousComponentName)
    {
      if (BluetoothMapClient.DBG) {
        Log.d("BluetoothMapClient", "Proxy object disconnected");
      }
      BluetoothMapClient.access$302(BluetoothMapClient.this, null);
      if (mServiceListener != null) {
        mServiceListener.onServiceDisconnected(18);
      }
    }
  };
  private final Context mContext;
  private volatile IBluetoothMapClient mService;
  private BluetoothProfile.ServiceListener mServiceListener;
  
  BluetoothMapClient(Context paramContext, BluetoothProfile.ServiceListener paramServiceListener)
  {
    if (DBG) {
      Log.d("BluetoothMapClient", "Create BluetoothMapClient proxy object");
    }
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
        Log.e("BluetoothMapClient", "", paramContext);
      }
    }
    doBind();
  }
  
  private boolean isEnabled()
  {
    BluetoothAdapter localBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    if ((localBluetoothAdapter != null) && (localBluetoothAdapter.getState() == 12)) {
      return true;
    }
    if (DBG) {
      Log.d("BluetoothMapClient", "Bluetooth is Not enabled");
    }
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
  
  public void close()
  {
    IBluetoothManager localIBluetoothManager = mAdapter.getBluetoothManager();
    if (localIBluetoothManager != null) {
      try
      {
        localIBluetoothManager.unregisterStateChangeCallback(mBluetoothStateChangeCallback);
      }
      catch (Exception localException1)
      {
        Log.e("BluetoothMapClient", "", localException1);
      }
    }
    synchronized (mConnection)
    {
      IBluetoothMapClient localIBluetoothMapClient = mService;
      if (localIBluetoothMapClient != null) {
        try
        {
          mService = null;
          mContext.unbindService(mConnection);
        }
        catch (Exception localException2)
        {
          Log.e("BluetoothMapClient", "", localException2);
        }
      }
      mServiceListener = null;
      return;
    }
  }
  
  public boolean connect(BluetoothDevice paramBluetoothDevice)
  {
    if (DBG)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("connect(");
      ((StringBuilder)localObject).append(paramBluetoothDevice);
      ((StringBuilder)localObject).append(")for MAPS MCE");
      Log.d("BluetoothMapClient", ((StringBuilder)localObject).toString());
    }
    Object localObject = mService;
    if (localObject != null)
    {
      try
      {
        boolean bool = ((IBluetoothMapClient)localObject).connect(paramBluetoothDevice);
        return bool;
      }
      catch (RemoteException paramBluetoothDevice)
      {
        Log.e("BluetoothMapClient", paramBluetoothDevice.toString());
      }
    }
    else
    {
      Log.w("BluetoothMapClient", "Proxy not attached to service");
      if (DBG) {
        Log.d("BluetoothMapClient", Log.getStackTraceString(new Throwable()));
      }
    }
    return false;
  }
  
  public boolean disconnect(BluetoothDevice paramBluetoothDevice)
  {
    if (DBG)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("disconnect(");
      ((StringBuilder)localObject).append(paramBluetoothDevice);
      ((StringBuilder)localObject).append(")");
      Log.d("BluetoothMapClient", ((StringBuilder)localObject).toString());
    }
    Object localObject = mService;
    if ((localObject != null) && (isEnabled()) && (isValidDevice(paramBluetoothDevice))) {
      try
      {
        boolean bool = ((IBluetoothMapClient)localObject).disconnect(paramBluetoothDevice);
        return bool;
      }
      catch (RemoteException paramBluetoothDevice)
      {
        Log.e("BluetoothMapClient", Log.getStackTraceString(new Throwable()));
      }
    }
    if (localObject == null) {
      Log.w("BluetoothMapClient", "Proxy not attached to service");
    }
    return false;
  }
  
  boolean doBind()
  {
    Intent localIntent = new Intent(IBluetoothMapClient.class.getName());
    Object localObject = localIntent.resolveSystemService(mContext.getPackageManager(), 0);
    localIntent.setComponent((ComponentName)localObject);
    if ((localObject != null) && (mContext.bindServiceAsUser(localIntent, mConnection, 0, mContext.getUser()))) {
      return true;
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Could not bind to Bluetooth MAP MCE Service with ");
    ((StringBuilder)localObject).append(localIntent);
    Log.e("BluetoothMapClient", ((StringBuilder)localObject).toString());
    return false;
  }
  
  protected void finalize()
    throws Throwable
  {
    try
    {
      close();
      return;
    }
    finally
    {
      super.finalize();
    }
  }
  
  public List<BluetoothDevice> getConnectedDevices()
  {
    if (DBG) {
      Log.d("BluetoothMapClient", "getConnectedDevices()");
    }
    Object localObject = mService;
    if ((localObject != null) && (isEnabled())) {
      try
      {
        localObject = ((IBluetoothMapClient)localObject).getConnectedDevices();
        return localObject;
      }
      catch (RemoteException localRemoteException)
      {
        Log.e("BluetoothMapClient", Log.getStackTraceString(new Throwable()));
        return new ArrayList();
      }
    }
    if (localRemoteException == null) {
      Log.w("BluetoothMapClient", "Proxy not attached to service");
    }
    return new ArrayList();
  }
  
  public int getConnectionState(BluetoothDevice paramBluetoothDevice)
  {
    if (DBG)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("getConnectionState(");
      ((StringBuilder)localObject).append(paramBluetoothDevice);
      ((StringBuilder)localObject).append(")");
      Log.d("BluetoothMapClient", ((StringBuilder)localObject).toString());
    }
    Object localObject = mService;
    if ((localObject != null) && (isEnabled()) && (isValidDevice(paramBluetoothDevice))) {
      try
      {
        int i = ((IBluetoothMapClient)localObject).getConnectionState(paramBluetoothDevice);
        return i;
      }
      catch (RemoteException paramBluetoothDevice)
      {
        Log.e("BluetoothMapClient", Log.getStackTraceString(new Throwable()));
        return 0;
      }
    }
    if (localObject == null) {
      Log.w("BluetoothMapClient", "Proxy not attached to service");
    }
    return 0;
  }
  
  public List<BluetoothDevice> getDevicesMatchingConnectionStates(int[] paramArrayOfInt)
  {
    if (DBG) {
      Log.d("BluetoothMapClient", "getDevicesMatchingStates()");
    }
    IBluetoothMapClient localIBluetoothMapClient = mService;
    if ((localIBluetoothMapClient != null) && (isEnabled())) {
      try
      {
        paramArrayOfInt = localIBluetoothMapClient.getDevicesMatchingConnectionStates(paramArrayOfInt);
        return paramArrayOfInt;
      }
      catch (RemoteException paramArrayOfInt)
      {
        Log.e("BluetoothMapClient", Log.getStackTraceString(new Throwable()));
        return new ArrayList();
      }
    }
    if (localIBluetoothMapClient == null) {
      Log.w("BluetoothMapClient", "Proxy not attached to service");
    }
    return new ArrayList();
  }
  
  public int getPriority(BluetoothDevice paramBluetoothDevice)
  {
    if (VDBG)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("getPriority(");
      ((StringBuilder)localObject).append(paramBluetoothDevice);
      ((StringBuilder)localObject).append(")");
      Log.d("BluetoothMapClient", ((StringBuilder)localObject).toString());
    }
    Object localObject = mService;
    if ((localObject != null) && (isEnabled()) && (isValidDevice(paramBluetoothDevice))) {
      try
      {
        int i = ((IBluetoothMapClient)localObject).getPriority(paramBluetoothDevice);
        return i;
      }
      catch (RemoteException paramBluetoothDevice)
      {
        Log.e("BluetoothMapClient", Log.getStackTraceString(new Throwable()));
        return 0;
      }
    }
    if (localObject == null) {
      Log.w("BluetoothMapClient", "Proxy not attached to service");
    }
    return 0;
  }
  
  public boolean getUnreadMessages(BluetoothDevice paramBluetoothDevice)
  {
    if (DBG)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("getUnreadMessages(");
      ((StringBuilder)localObject).append(paramBluetoothDevice);
      ((StringBuilder)localObject).append(")");
      Log.d("BluetoothMapClient", ((StringBuilder)localObject).toString());
    }
    Object localObject = mService;
    if ((localObject != null) && (isEnabled()) && (isValidDevice(paramBluetoothDevice))) {
      try
      {
        boolean bool = ((IBluetoothMapClient)localObject).getUnreadMessages(paramBluetoothDevice);
        return bool;
      }
      catch (RemoteException paramBluetoothDevice)
      {
        Log.e("BluetoothMapClient", Log.getStackTraceString(new Throwable()));
        return false;
      }
    }
    return false;
  }
  
  public boolean isConnected(BluetoothDevice paramBluetoothDevice)
  {
    if (VDBG)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("isConnected(");
      ((StringBuilder)localObject).append(paramBluetoothDevice);
      ((StringBuilder)localObject).append(")");
      Log.d("BluetoothMapClient", ((StringBuilder)localObject).toString());
    }
    Object localObject = mService;
    if (localObject != null)
    {
      try
      {
        boolean bool = ((IBluetoothMapClient)localObject).isConnected(paramBluetoothDevice);
        return bool;
      }
      catch (RemoteException paramBluetoothDevice)
      {
        Log.e("BluetoothMapClient", paramBluetoothDevice.toString());
      }
    }
    else
    {
      Log.w("BluetoothMapClient", "Proxy not attached to service");
      if (DBG) {
        Log.d("BluetoothMapClient", Log.getStackTraceString(new Throwable()));
      }
    }
    return false;
  }
  
  public boolean sendMessage(BluetoothDevice paramBluetoothDevice, Uri[] paramArrayOfUri, String paramString, PendingIntent paramPendingIntent1, PendingIntent paramPendingIntent2)
  {
    if (DBG)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("sendMessage(");
      ((StringBuilder)localObject).append(paramBluetoothDevice);
      ((StringBuilder)localObject).append(", ");
      ((StringBuilder)localObject).append(paramArrayOfUri);
      ((StringBuilder)localObject).append(", ");
      ((StringBuilder)localObject).append(paramString);
      Log.d("BluetoothMapClient", ((StringBuilder)localObject).toString());
    }
    Object localObject = mService;
    if ((localObject != null) && (isEnabled()) && (isValidDevice(paramBluetoothDevice))) {
      try
      {
        boolean bool = ((IBluetoothMapClient)localObject).sendMessage(paramBluetoothDevice, paramArrayOfUri, paramString, paramPendingIntent1, paramPendingIntent2);
        return bool;
      }
      catch (RemoteException paramBluetoothDevice)
      {
        Log.e("BluetoothMapClient", Log.getStackTraceString(new Throwable()));
        return false;
      }
    }
    return false;
  }
  
  public boolean setPriority(BluetoothDevice paramBluetoothDevice, int paramInt)
  {
    if (DBG)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("setPriority(");
      ((StringBuilder)localObject).append(paramBluetoothDevice);
      ((StringBuilder)localObject).append(", ");
      ((StringBuilder)localObject).append(paramInt);
      ((StringBuilder)localObject).append(")");
      Log.d("BluetoothMapClient", ((StringBuilder)localObject).toString());
    }
    Object localObject = mService;
    if ((localObject != null) && (isEnabled()) && (isValidDevice(paramBluetoothDevice)))
    {
      if ((paramInt != 0) && (paramInt != 100)) {
        return false;
      }
      try
      {
        boolean bool = ((IBluetoothMapClient)localObject).setPriority(paramBluetoothDevice, paramInt);
        return bool;
      }
      catch (RemoteException paramBluetoothDevice)
      {
        Log.e("BluetoothMapClient", Log.getStackTraceString(new Throwable()));
        return false;
      }
    }
    if (localObject == null) {
      Log.w("BluetoothMapClient", "Proxy not attached to service");
    }
    return false;
  }
}
