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

public final class BluetoothMap
  implements BluetoothProfile
{
  public static final String ACTION_CONNECTION_STATE_CHANGED = "android.bluetooth.map.profile.action.CONNECTION_STATE_CHANGED";
  private static final boolean DBG = true;
  public static final int RESULT_CANCELED = 2;
  public static final int RESULT_FAILURE = 0;
  public static final int RESULT_SUCCESS = 1;
  public static final int STATE_ERROR = -1;
  private static final String TAG = "BluetoothMap";
  private static final boolean VDBG = false;
  private BluetoothAdapter mAdapter;
  private final IBluetoothStateChangeCallback mBluetoothStateChangeCallback = new IBluetoothStateChangeCallback.Stub()
  {
    public void onBluetoothStateChange(boolean paramAnonymousBoolean)
    {
      ??? = new StringBuilder();
      ((StringBuilder)???).append("onBluetoothStateChange: up=");
      ((StringBuilder)???).append(paramAnonymousBoolean);
      Log.d("BluetoothMap", ((StringBuilder)???).toString());
      if (!paramAnonymousBoolean)
      {
        try
        {
          synchronized (mConnection)
          {
            BluetoothMap.access$102(BluetoothMap.this, null);
            mContext.unbindService(mConnection);
          }
        }
        catch (Exception localException1)
        {
          Log.e("BluetoothMap", "", localException1);
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
          Log.e("BluetoothMap", "", localException2);
        }
      }
    }
  };
  private final ServiceConnection mConnection = new ServiceConnection()
  {
    public void onServiceConnected(ComponentName paramAnonymousComponentName, IBinder paramAnonymousIBinder)
    {
      BluetoothMap.log("Proxy object connected");
      BluetoothMap.access$102(BluetoothMap.this, IBluetoothMap.Stub.asInterface(Binder.allowBlocking(paramAnonymousIBinder)));
      if (mServiceListener != null) {
        mServiceListener.onServiceConnected(9, BluetoothMap.this);
      }
    }
    
    public void onServiceDisconnected(ComponentName paramAnonymousComponentName)
    {
      BluetoothMap.log("Proxy object disconnected");
      BluetoothMap.access$102(BluetoothMap.this, null);
      if (mServiceListener != null) {
        mServiceListener.onServiceDisconnected(9);
      }
    }
  };
  private final Context mContext;
  private volatile IBluetoothMap mService;
  private BluetoothProfile.ServiceListener mServiceListener;
  
  BluetoothMap(Context paramContext, BluetoothProfile.ServiceListener paramServiceListener)
  {
    Log.d("BluetoothMap", "Create BluetoothMap proxy object");
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
        Log.e("BluetoothMap", "", paramContext);
      }
    }
    doBind();
  }
  
  public static boolean doesClassMatchSink(BluetoothClass paramBluetoothClass)
  {
    int i = paramBluetoothClass.getDeviceClass();
    return (i == 256) || (i == 260) || (i == 264) || (i == 268);
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
    Log.d("BluetoothMap", paramString);
  }
  
  /* Error */
  public void close()
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield 76	android/bluetooth/BluetoothMap:mAdapter	Landroid/bluetooth/BluetoothAdapter;
    //   6: invokevirtual 80	android/bluetooth/BluetoothAdapter:getBluetoothManager	()Landroid/bluetooth/IBluetoothManager;
    //   9: astore_1
    //   10: aload_1
    //   11: ifnull +26 -> 37
    //   14: aload_1
    //   15: aload_0
    //   16: getfield 53	android/bluetooth/BluetoothMap:mBluetoothStateChangeCallback	Landroid/bluetooth/IBluetoothStateChangeCallback;
    //   19: invokeinterface 146 2 0
    //   24: goto +13 -> 37
    //   27: astore_1
    //   28: ldc 28
    //   30: ldc 88
    //   32: aload_1
    //   33: invokestatic 92	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   36: pop
    //   37: aload_0
    //   38: getfield 56	android/bluetooth/BluetoothMap:mConnection	Landroid/content/ServiceConnection;
    //   41: astore_1
    //   42: aload_1
    //   43: monitorenter
    //   44: aload_0
    //   45: getfield 103	android/bluetooth/BluetoothMap:mService	Landroid/bluetooth/IBluetoothMap;
    //   48: astore_2
    //   49: aload_2
    //   50: ifnull +32 -> 82
    //   53: aload_0
    //   54: aconst_null
    //   55: putfield 103	android/bluetooth/BluetoothMap:mService	Landroid/bluetooth/IBluetoothMap;
    //   58: aload_0
    //   59: getfield 66	android/bluetooth/BluetoothMap:mContext	Landroid/content/Context;
    //   62: aload_0
    //   63: getfield 56	android/bluetooth/BluetoothMap:mConnection	Landroid/content/ServiceConnection;
    //   66: invokevirtual 152	android/content/Context:unbindService	(Landroid/content/ServiceConnection;)V
    //   69: goto +13 -> 82
    //   72: astore_2
    //   73: ldc 28
    //   75: ldc 88
    //   77: aload_2
    //   78: invokestatic 92	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   81: pop
    //   82: aload_1
    //   83: monitorexit
    //   84: aload_0
    //   85: aconst_null
    //   86: putfield 68	android/bluetooth/BluetoothMap:mServiceListener	Landroid/bluetooth/BluetoothProfile$ServiceListener;
    //   89: aload_0
    //   90: monitorexit
    //   91: return
    //   92: astore_2
    //   93: aload_1
    //   94: monitorexit
    //   95: aload_2
    //   96: athrow
    //   97: astore_1
    //   98: aload_0
    //   99: monitorexit
    //   100: aload_1
    //   101: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	102	0	this	BluetoothMap
    //   9	6	1	localIBluetoothManager	IBluetoothManager
    //   27	6	1	localException1	Exception
    //   97	4	1	localObject1	Object
    //   48	2	2	localIBluetoothMap	IBluetoothMap
    //   72	6	2	localException2	Exception
    //   92	4	2	localObject2	Object
    // Exception table:
    //   from	to	target	type
    //   14	24	27	java/lang/Exception
    //   53	69	72	java/lang/Exception
    //   44	49	92	finally
    //   53	69	92	finally
    //   73	82	92	finally
    //   82	84	92	finally
    //   93	95	92	finally
    //   2	10	97	finally
    //   14	24	97	finally
    //   28	37	97	finally
    //   37	44	97	finally
    //   84	89	97	finally
    //   95	97	97	finally
  }
  
  public boolean connect(BluetoothDevice paramBluetoothDevice)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("connect(");
    localStringBuilder.append(paramBluetoothDevice);
    localStringBuilder.append(")not supported for MAPS");
    log(localStringBuilder.toString());
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
        boolean bool = ((IBluetoothMap)localObject).disconnect(paramBluetoothDevice);
        return bool;
      }
      catch (RemoteException paramBluetoothDevice)
      {
        Log.e("BluetoothMap", Log.getStackTraceString(new Throwable()));
        return false;
      }
    }
    if (localObject == null) {
      Log.w("BluetoothMap", "Proxy not attached to service");
    }
    return false;
  }
  
  boolean doBind()
  {
    Intent localIntent = new Intent(IBluetoothMap.class.getName());
    Object localObject = localIntent.resolveSystemService(mContext.getPackageManager(), 0);
    localIntent.setComponent((ComponentName)localObject);
    if ((localObject != null) && (mContext.bindServiceAsUser(localIntent, mConnection, 0, mContext.getUser()))) {
      return true;
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Could not bind to Bluetooth MAP Service with ");
    ((StringBuilder)localObject).append(localIntent);
    Log.e("BluetoothMap", ((StringBuilder)localObject).toString());
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
  
  public BluetoothDevice getClient()
  {
    Object localObject = mService;
    if (localObject != null)
    {
      try
      {
        localObject = ((IBluetoothMap)localObject).getClient();
        return localObject;
      }
      catch (RemoteException localRemoteException)
      {
        Log.e("BluetoothMap", localRemoteException.toString());
      }
    }
    else
    {
      Log.w("BluetoothMap", "Proxy not attached to service");
      log(Log.getStackTraceString(new Throwable()));
    }
    return null;
  }
  
  public List<BluetoothDevice> getConnectedDevices()
  {
    log("getConnectedDevices()");
    Object localObject = mService;
    if ((localObject != null) && (isEnabled())) {
      try
      {
        localObject = ((IBluetoothMap)localObject).getConnectedDevices();
        return localObject;
      }
      catch (RemoteException localRemoteException)
      {
        Log.e("BluetoothMap", Log.getStackTraceString(new Throwable()));
        return new ArrayList();
      }
    }
    if (localRemoteException == null) {
      Log.w("BluetoothMap", "Proxy not attached to service");
    }
    return new ArrayList();
  }
  
  public int getConnectionState(BluetoothDevice paramBluetoothDevice)
  {
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("getConnectionState(");
    ((StringBuilder)localObject).append(paramBluetoothDevice);
    ((StringBuilder)localObject).append(")");
    log(((StringBuilder)localObject).toString());
    localObject = mService;
    if ((localObject != null) && (isEnabled()) && (isValidDevice(paramBluetoothDevice))) {
      try
      {
        int i = ((IBluetoothMap)localObject).getConnectionState(paramBluetoothDevice);
        return i;
      }
      catch (RemoteException paramBluetoothDevice)
      {
        Log.e("BluetoothMap", Log.getStackTraceString(new Throwable()));
        return 0;
      }
    }
    if (localObject == null) {
      Log.w("BluetoothMap", "Proxy not attached to service");
    }
    return 0;
  }
  
  public List<BluetoothDevice> getDevicesMatchingConnectionStates(int[] paramArrayOfInt)
  {
    log("getDevicesMatchingStates()");
    IBluetoothMap localIBluetoothMap = mService;
    if ((localIBluetoothMap != null) && (isEnabled())) {
      try
      {
        paramArrayOfInt = localIBluetoothMap.getDevicesMatchingConnectionStates(paramArrayOfInt);
        return paramArrayOfInt;
      }
      catch (RemoteException paramArrayOfInt)
      {
        Log.e("BluetoothMap", Log.getStackTraceString(new Throwable()));
        return new ArrayList();
      }
    }
    if (localIBluetoothMap == null) {
      Log.w("BluetoothMap", "Proxy not attached to service");
    }
    return new ArrayList();
  }
  
  public int getPriority(BluetoothDevice paramBluetoothDevice)
  {
    IBluetoothMap localIBluetoothMap = mService;
    if ((localIBluetoothMap != null) && (isEnabled()) && (isValidDevice(paramBluetoothDevice))) {
      try
      {
        int i = localIBluetoothMap.getPriority(paramBluetoothDevice);
        return i;
      }
      catch (RemoteException paramBluetoothDevice)
      {
        Log.e("BluetoothMap", Log.getStackTraceString(new Throwable()));
        return 0;
      }
    }
    if (localIBluetoothMap == null) {
      Log.w("BluetoothMap", "Proxy not attached to service");
    }
    return 0;
  }
  
  public int getState()
  {
    IBluetoothMap localIBluetoothMap = mService;
    if (localIBluetoothMap != null)
    {
      try
      {
        int i = localIBluetoothMap.getState();
        return i;
      }
      catch (RemoteException localRemoteException)
      {
        Log.e("BluetoothMap", localRemoteException.toString());
      }
    }
    else
    {
      Log.w("BluetoothMap", "Proxy not attached to service");
      log(Log.getStackTraceString(new Throwable()));
    }
    return -1;
  }
  
  public boolean isConnected(BluetoothDevice paramBluetoothDevice)
  {
    IBluetoothMap localIBluetoothMap = mService;
    if (localIBluetoothMap != null)
    {
      try
      {
        boolean bool = localIBluetoothMap.isConnected(paramBluetoothDevice);
        return bool;
      }
      catch (RemoteException paramBluetoothDevice)
      {
        Log.e("BluetoothMap", paramBluetoothDevice.toString());
      }
    }
    else
    {
      Log.w("BluetoothMap", "Proxy not attached to service");
      log(Log.getStackTraceString(new Throwable()));
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
        boolean bool = ((IBluetoothMap)localObject).setPriority(paramBluetoothDevice, paramInt);
        return bool;
      }
      catch (RemoteException paramBluetoothDevice)
      {
        Log.e("BluetoothMap", Log.getStackTraceString(new Throwable()));
        return false;
      }
    }
    if (localObject == null) {
      Log.w("BluetoothMap", "Proxy not attached to service");
    }
    return false;
  }
}
