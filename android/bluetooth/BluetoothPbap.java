package android.bluetooth;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BluetoothPbap
  implements BluetoothProfile
{
  public static final String ACTION_CONNECTION_STATE_CHANGED = "android.bluetooth.pbap.profile.action.CONNECTION_STATE_CHANGED";
  private static final boolean DBG = false;
  public static final int RESULT_CANCELED = 2;
  public static final int RESULT_FAILURE = 0;
  public static final int RESULT_SUCCESS = 1;
  private static final String TAG = "BluetoothPbap";
  private BluetoothAdapter mAdapter;
  private final IBluetoothStateChangeCallback mBluetoothStateChangeCallback = new IBluetoothStateChangeCallback.Stub()
  {
    public void onBluetoothStateChange(boolean paramAnonymousBoolean)
    {
      ??? = new StringBuilder();
      ((StringBuilder)???).append("onBluetoothStateChange: up=");
      ((StringBuilder)???).append(paramAnonymousBoolean);
      BluetoothPbap.log(((StringBuilder)???).toString());
      if (!paramAnonymousBoolean)
      {
        BluetoothPbap.log("Unbinding service...");
        try
        {
          synchronized (mConnection)
          {
            BluetoothPbap.access$202(BluetoothPbap.this, null);
            mContext.unbindService(mConnection);
          }
        }
        catch (Exception localException1)
        {
          Log.e("BluetoothPbap", "", localException1);
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
              BluetoothPbap.log("Binding service...");
              doBind();
            }
          }
          return;
        }
        catch (Exception localException2)
        {
          Log.e("BluetoothPbap", "", localException2);
        }
      }
    }
  };
  private final ServiceConnection mConnection = new ServiceConnection()
  {
    public void onServiceConnected(ComponentName paramAnonymousComponentName, IBinder paramAnonymousIBinder)
    {
      BluetoothPbap.log("Proxy object connected");
      BluetoothPbap.access$202(BluetoothPbap.this, IBluetoothPbap.Stub.asInterface(paramAnonymousIBinder));
      if (mServiceListener != null) {
        mServiceListener.onServiceConnected(BluetoothPbap.this);
      }
    }
    
    public void onServiceDisconnected(ComponentName paramAnonymousComponentName)
    {
      BluetoothPbap.log("Proxy object disconnected");
      BluetoothPbap.access$202(BluetoothPbap.this, null);
      if (mServiceListener != null) {
        mServiceListener.onServiceDisconnected();
      }
    }
  };
  private final Context mContext;
  private volatile IBluetoothPbap mService;
  private ServiceListener mServiceListener;
  
  public BluetoothPbap(Context paramContext, ServiceListener paramServiceListener)
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
        Log.e("BluetoothPbap", "", paramContext);
      }
    }
    doBind();
  }
  
  private static void log(String paramString) {}
  
  /* Error */
  public void close()
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield 68	android/bluetooth/BluetoothPbap:mAdapter	Landroid/bluetooth/BluetoothAdapter;
    //   6: invokevirtual 72	android/bluetooth/BluetoothAdapter:getBluetoothManager	()Landroid/bluetooth/IBluetoothManager;
    //   9: astore_1
    //   10: aload_1
    //   11: ifnull +26 -> 37
    //   14: aload_1
    //   15: aload_0
    //   16: getfield 53	android/bluetooth/BluetoothPbap:mBluetoothStateChangeCallback	Landroid/bluetooth/IBluetoothStateChangeCallback;
    //   19: invokeinterface 114 2 0
    //   24: goto +13 -> 37
    //   27: astore_1
    //   28: ldc 29
    //   30: ldc 80
    //   32: aload_1
    //   33: invokestatic 86	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   36: pop
    //   37: aload_0
    //   38: getfield 56	android/bluetooth/BluetoothPbap:mConnection	Landroid/content/ServiceConnection;
    //   41: astore_1
    //   42: aload_1
    //   43: monitorenter
    //   44: aload_0
    //   45: getfield 102	android/bluetooth/BluetoothPbap:mService	Landroid/bluetooth/IBluetoothPbap;
    //   48: astore_2
    //   49: aload_2
    //   50: ifnull +32 -> 82
    //   53: aload_0
    //   54: aconst_null
    //   55: putfield 102	android/bluetooth/BluetoothPbap:mService	Landroid/bluetooth/IBluetoothPbap;
    //   58: aload_0
    //   59: getfield 58	android/bluetooth/BluetoothPbap:mContext	Landroid/content/Context;
    //   62: aload_0
    //   63: getfield 56	android/bluetooth/BluetoothPbap:mConnection	Landroid/content/ServiceConnection;
    //   66: invokevirtual 120	android/content/Context:unbindService	(Landroid/content/ServiceConnection;)V
    //   69: goto +13 -> 82
    //   72: astore_2
    //   73: ldc 29
    //   75: ldc 80
    //   77: aload_2
    //   78: invokestatic 86	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   81: pop
    //   82: aload_1
    //   83: monitorexit
    //   84: aload_0
    //   85: aconst_null
    //   86: putfield 60	android/bluetooth/BluetoothPbap:mServiceListener	Landroid/bluetooth/BluetoothPbap$ServiceListener;
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
    //   0	102	0	this	BluetoothPbap
    //   9	6	1	localIBluetoothManager	IBluetoothManager
    //   27	6	1	localException1	Exception
    //   97	4	1	localObject1	Object
    //   48	2	2	localIBluetoothPbap	IBluetoothPbap
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
  
  public boolean disconnect(BluetoothDevice paramBluetoothDevice)
  {
    log("disconnect()");
    IBluetoothPbap localIBluetoothPbap = mService;
    if (localIBluetoothPbap == null)
    {
      Log.w("BluetoothPbap", "Proxy not attached to service");
      return false;
    }
    try
    {
      localIBluetoothPbap.disconnect(paramBluetoothDevice);
      return true;
    }
    catch (RemoteException paramBluetoothDevice)
    {
      Log.e("BluetoothPbap", paramBluetoothDevice.toString());
    }
    return false;
  }
  
  boolean doBind()
  {
    Intent localIntent = new Intent(IBluetoothPbap.class.getName());
    Object localObject = localIntent.resolveSystemService(mContext.getPackageManager(), 0);
    localIntent.setComponent((ComponentName)localObject);
    if ((localObject != null) && (mContext.bindServiceAsUser(localIntent, mConnection, 0, mContext.getUser()))) {
      return true;
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Could not bind to Bluetooth Pbap Service with ");
    ((StringBuilder)localObject).append(localIntent);
    Log.e("BluetoothPbap", ((StringBuilder)localObject).toString());
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
    log("getConnectedDevices()");
    Object localObject = mService;
    if (localObject == null)
    {
      Log.w("BluetoothPbap", "Proxy not attached to service");
      return new ArrayList();
    }
    try
    {
      localObject = ((IBluetoothPbap)localObject).getConnectedDevices();
      return localObject;
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("BluetoothPbap", localRemoteException.toString());
    }
    return new ArrayList();
  }
  
  public int getConnectionState(BluetoothDevice paramBluetoothDevice)
  {
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("getConnectionState: device=");
    ((StringBuilder)localObject).append(paramBluetoothDevice);
    log(((StringBuilder)localObject).toString());
    localObject = mService;
    if (localObject == null)
    {
      Log.w("BluetoothPbap", "Proxy not attached to service");
      return 0;
    }
    try
    {
      int i = ((IBluetoothPbap)localObject).getConnectionState(paramBluetoothDevice);
      return i;
    }
    catch (RemoteException paramBluetoothDevice)
    {
      Log.e("BluetoothPbap", paramBluetoothDevice.toString());
    }
    return 0;
  }
  
  public List<BluetoothDevice> getDevicesMatchingConnectionStates(int[] paramArrayOfInt)
  {
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("getDevicesMatchingConnectionStates: states=");
    ((StringBuilder)localObject).append(Arrays.toString(paramArrayOfInt));
    log(((StringBuilder)localObject).toString());
    localObject = mService;
    if (localObject == null)
    {
      Log.w("BluetoothPbap", "Proxy not attached to service");
      return new ArrayList();
    }
    try
    {
      paramArrayOfInt = ((IBluetoothPbap)localObject).getDevicesMatchingConnectionStates(paramArrayOfInt);
      return paramArrayOfInt;
    }
    catch (RemoteException paramArrayOfInt)
    {
      Log.e("BluetoothPbap", paramArrayOfInt.toString());
    }
    return new ArrayList();
  }
  
  public boolean isConnected(BluetoothDevice paramBluetoothDevice)
  {
    boolean bool;
    if (getConnectionState(paramBluetoothDevice) == 2) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static abstract interface ServiceListener
  {
    public abstract void onServiceConnected(BluetoothPbap paramBluetoothPbap);
    
    public abstract void onServiceDisconnected();
  }
}
