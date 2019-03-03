package android.bluetooth;

import android.annotation.SystemApi;
import android.app.ActivityThread;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.PeriodicAdvertisingManager;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter.Builder;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.bluetooth.le.ScanSettings.Builder;
import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ParcelUuid;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.os.ServiceManager;
import android.os.SynchronousResultReceiver;
import android.os.SynchronousResultReceiver.Result;
import android.util.Log;
import android.util.Pair;
import android.util.SeempLog;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

public final class BluetoothAdapter
{
  public static final String ACTION_BLE_ACL_CONNECTED = "android.bluetooth.adapter.action.BLE_ACL_CONNECTED";
  public static final String ACTION_BLE_ACL_DISCONNECTED = "android.bluetooth.adapter.action.BLE_ACL_DISCONNECTED";
  @SystemApi
  public static final String ACTION_BLE_STATE_CHANGED = "android.bluetooth.adapter.action.BLE_STATE_CHANGED";
  public static final String ACTION_BLUETOOTH_ADDRESS_CHANGED = "android.bluetooth.adapter.action.BLUETOOTH_ADDRESS_CHANGED";
  public static final String ACTION_CONNECTION_STATE_CHANGED = "android.bluetooth.adapter.action.CONNECTION_STATE_CHANGED";
  public static final String ACTION_DISCOVERY_FINISHED = "android.bluetooth.adapter.action.DISCOVERY_FINISHED";
  public static final String ACTION_DISCOVERY_STARTED = "android.bluetooth.adapter.action.DISCOVERY_STARTED";
  public static final String ACTION_LOCAL_NAME_CHANGED = "android.bluetooth.adapter.action.LOCAL_NAME_CHANGED";
  @SystemApi
  public static final String ACTION_REQUEST_BLE_SCAN_ALWAYS_AVAILABLE = "android.bluetooth.adapter.action.REQUEST_BLE_SCAN_ALWAYS_AVAILABLE";
  public static final String ACTION_REQUEST_DISABLE = "android.bluetooth.adapter.action.REQUEST_DISABLE";
  public static final String ACTION_REQUEST_DISCOVERABLE = "android.bluetooth.adapter.action.REQUEST_DISCOVERABLE";
  public static final String ACTION_REQUEST_ENABLE = "android.bluetooth.adapter.action.REQUEST_ENABLE";
  public static final String ACTION_SCAN_MODE_CHANGED = "android.bluetooth.adapter.action.SCAN_MODE_CHANGED";
  public static final String ACTION_STATE_CHANGED = "android.bluetooth.adapter.action.STATE_CHANGED";
  private static final int ADDRESS_LENGTH = 17;
  public static final String BLUETOOTH_MANAGER_SERVICE = "bluetooth_manager";
  private static final boolean DBG = true;
  public static final String DEFAULT_MAC_ADDRESS = "02:00:00:00:00:00";
  public static final int ERROR = Integer.MIN_VALUE;
  public static final String EXTRA_BLUETOOTH_ADDRESS = "android.bluetooth.adapter.extra.BLUETOOTH_ADDRESS";
  public static final String EXTRA_CONNECTION_STATE = "android.bluetooth.adapter.extra.CONNECTION_STATE";
  public static final String EXTRA_DISCOVERABLE_DURATION = "android.bluetooth.adapter.extra.DISCOVERABLE_DURATION";
  public static final String EXTRA_LOCAL_NAME = "android.bluetooth.adapter.extra.LOCAL_NAME";
  public static final String EXTRA_PREVIOUS_CONNECTION_STATE = "android.bluetooth.adapter.extra.PREVIOUS_CONNECTION_STATE";
  public static final String EXTRA_PREVIOUS_SCAN_MODE = "android.bluetooth.adapter.extra.PREVIOUS_SCAN_MODE";
  public static final String EXTRA_PREVIOUS_STATE = "android.bluetooth.adapter.extra.PREVIOUS_STATE";
  public static final String EXTRA_SCAN_MODE = "android.bluetooth.adapter.extra.SCAN_MODE";
  public static final String EXTRA_STATE = "android.bluetooth.adapter.extra.STATE";
  public static final UUID LE_PSM_CHARACTERISTIC_UUID = UUID.fromString("2d410339-82b6-42aa-b34e-e2e01df8cc1a");
  public static final int SCAN_MODE_CONNECTABLE = 21;
  public static final int SCAN_MODE_CONNECTABLE_DISCOVERABLE = 23;
  public static final int SCAN_MODE_NONE = 20;
  public static final int SOCKET_CHANNEL_AUTO_STATIC_NO_SDP = -2;
  public static final int STATE_BLE_ON = 15;
  public static final int STATE_BLE_TURNING_OFF = 16;
  public static final int STATE_BLE_TURNING_ON = 14;
  public static final int STATE_CONNECTED = 2;
  public static final int STATE_CONNECTING = 1;
  public static final int STATE_DISCONNECTED = 0;
  public static final int STATE_DISCONNECTING = 3;
  public static final int STATE_OFF = 10;
  public static final int STATE_ON = 12;
  public static final int STATE_TURNING_OFF = 13;
  public static final int STATE_TURNING_ON = 11;
  private static final String TAG = "BluetoothAdapter";
  private static final boolean VDBG = false;
  private static BluetoothAdapter sAdapter;
  private static BluetoothLeAdvertiser sBluetoothLeAdvertiser;
  private static BluetoothLeScanner sBluetoothLeScanner;
  private static PeriodicAdvertisingManager sPeriodicAdvertisingManager;
  private final Map<LeScanCallback, ScanCallback> mLeScanClients;
  private final Object mLock;
  private final IBluetoothManagerCallback mManagerCallback;
  private final IBluetoothManager mManagerService;
  private final ArrayList<IBluetoothManagerCallback> mProxyServiceStateCallbacks;
  private IBluetooth mService;
  private final ReentrantReadWriteLock mServiceLock;
  private final IBinder mToken;
  
  /* Error */
  BluetoothAdapter(IBluetoothManager paramIBluetoothManager)
  {
    // Byte code:
    //   0: aload_0
    //   1: invokespecial 188	java/lang/Object:<init>	()V
    //   4: aload_0
    //   5: new 190	java/util/concurrent/locks/ReentrantReadWriteLock
    //   8: dup
    //   9: invokespecial 191	java/util/concurrent/locks/ReentrantReadWriteLock:<init>	()V
    //   12: putfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   15: aload_0
    //   16: new 4	java/lang/Object
    //   19: dup
    //   20: invokespecial 188	java/lang/Object:<init>	()V
    //   23: putfield 195	android/bluetooth/BluetoothAdapter:mLock	Ljava/lang/Object;
    //   26: aload_0
    //   27: new 6	android/bluetooth/BluetoothAdapter$1
    //   30: dup
    //   31: aload_0
    //   32: invokespecial 198	android/bluetooth/BluetoothAdapter$1:<init>	(Landroid/bluetooth/BluetoothAdapter;)V
    //   35: putfield 200	android/bluetooth/BluetoothAdapter:mManagerCallback	Landroid/bluetooth/IBluetoothManagerCallback;
    //   38: aload_0
    //   39: new 202	java/util/ArrayList
    //   42: dup
    //   43: invokespecial 203	java/util/ArrayList:<init>	()V
    //   46: putfield 205	android/bluetooth/BluetoothAdapter:mProxyServiceStateCallbacks	Ljava/util/ArrayList;
    //   49: aload_1
    //   50: ifnull +94 -> 144
    //   53: aload_0
    //   54: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   57: invokevirtual 209	java/util/concurrent/locks/ReentrantReadWriteLock:writeLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$WriteLock;
    //   60: invokevirtual 214	java/util/concurrent/locks/ReentrantReadWriteLock$WriteLock:lock	()V
    //   63: aload_0
    //   64: aload_1
    //   65: aload_0
    //   66: getfield 200	android/bluetooth/BluetoothAdapter:mManagerCallback	Landroid/bluetooth/IBluetoothManagerCallback;
    //   69: invokeinterface 220 2 0
    //   74: putfield 222	android/bluetooth/BluetoothAdapter:mService	Landroid/bluetooth/IBluetooth;
    //   77: goto +17 -> 94
    //   80: astore_1
    //   81: goto +51 -> 132
    //   84: astore_2
    //   85: ldc -114
    //   87: ldc -32
    //   89: aload_2
    //   90: invokestatic 230	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   93: pop
    //   94: aload_0
    //   95: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   98: invokevirtual 209	java/util/concurrent/locks/ReentrantReadWriteLock:writeLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$WriteLock;
    //   101: invokevirtual 233	java/util/concurrent/locks/ReentrantReadWriteLock$WriteLock:unlock	()V
    //   104: aload_0
    //   105: aload_1
    //   106: putfield 235	android/bluetooth/BluetoothAdapter:mManagerService	Landroid/bluetooth/IBluetoothManager;
    //   109: aload_0
    //   110: new 237	java/util/HashMap
    //   113: dup
    //   114: invokespecial 238	java/util/HashMap:<init>	()V
    //   117: putfield 240	android/bluetooth/BluetoothAdapter:mLeScanClients	Ljava/util/Map;
    //   120: aload_0
    //   121: new 242	android/os/Binder
    //   124: dup
    //   125: invokespecial 243	android/os/Binder:<init>	()V
    //   128: putfield 245	android/bluetooth/BluetoothAdapter:mToken	Landroid/os/IBinder;
    //   131: return
    //   132: aload_0
    //   133: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   136: invokevirtual 209	java/util/concurrent/locks/ReentrantReadWriteLock:writeLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$WriteLock;
    //   139: invokevirtual 233	java/util/concurrent/locks/ReentrantReadWriteLock$WriteLock:unlock	()V
    //   142: aload_1
    //   143: athrow
    //   144: new 247	java/lang/IllegalArgumentException
    //   147: dup
    //   148: ldc -7
    //   150: invokespecial 252	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
    //   153: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	154	0	this	BluetoothAdapter
    //   0	154	1	paramIBluetoothManager	IBluetoothManager
    //   84	6	2	localRemoteException	RemoteException
    // Exception table:
    //   from	to	target	type
    //   53	77	80	finally
    //   85	94	80	finally
    //   53	77	84	android/os/RemoteException
  }
  
  public static boolean checkBluetoothAddress(String paramString)
  {
    if ((paramString != null) && (paramString.length() == 17))
    {
      for (int i = 0; i < 17; i++)
      {
        int j = paramString.charAt(i);
        switch (i % 3)
        {
        default: 
          break;
        case 2: 
          if (j != 58) {
            return false;
          }
          break;
        case 0: 
        case 1: 
          if (((j < 48) || (j > 57)) && ((j < 65) || (j > 70))) {
            return false;
          }
          break;
        }
      }
      return true;
    }
    return false;
  }
  
  private BluetoothServerSocket createNewRfcommSocketAndRecord(String paramString, UUID paramUUID, boolean paramBoolean1, boolean paramBoolean2)
    throws IOException
  {
    paramUUID = new BluetoothServerSocket(1, paramBoolean1, paramBoolean2, new ParcelUuid(paramUUID));
    paramUUID.setServiceName(paramString);
    int i = mSocket.bindListen();
    if (i == 0) {
      return paramUUID;
    }
    paramString = new StringBuilder();
    paramString.append("Error: ");
    paramString.append(i);
    throw new IOException(paramString.toString());
  }
  
  public static BluetoothAdapter getDefaultAdapter()
  {
    try
    {
      if (sAdapter == null)
      {
        localObject1 = ServiceManager.getService("bluetooth_manager");
        if (localObject1 != null)
        {
          localObject1 = IBluetoothManager.Stub.asInterface((IBinder)localObject1);
          BluetoothAdapter localBluetoothAdapter = new android/bluetooth/BluetoothAdapter;
          localBluetoothAdapter.<init>((IBluetoothManager)localObject1);
          sAdapter = localBluetoothAdapter;
        }
        else
        {
          Log.e("BluetoothAdapter", "Bluetooth binder is null");
        }
      }
      Object localObject1 = sAdapter;
      return localObject1;
    }
    finally {}
  }
  
  public static BluetoothServerSocket listenUsingScoOn()
    throws IOException
  {
    BluetoothServerSocket localBluetoothServerSocket = new BluetoothServerSocket(2, false, false, -1);
    mSocket.bindListen();
    return localBluetoothServerSocket;
  }
  
  public static String nameForState(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("?!?!? (");
      localStringBuilder.append(paramInt);
      localStringBuilder.append(")");
      return localStringBuilder.toString();
    case 16: 
      return "BLE_TURNING_OFF";
    case 15: 
      return "BLE_ON";
    case 14: 
      return "BLE_TURNING_ON";
    case 13: 
      return "TURNING_OFF";
    case 12: 
      return "ON";
    case 11: 
      return "TURNING_ON";
    }
    return "OFF";
  }
  
  private Set<BluetoothDevice> toDeviceSet(BluetoothDevice[] paramArrayOfBluetoothDevice)
  {
    return Collections.unmodifiableSet(new HashSet(Arrays.asList(paramArrayOfBluetoothDevice)));
  }
  
  /* Error */
  public boolean cancelDiscovery()
  {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual 400	android/bluetooth/BluetoothAdapter:getState	()I
    //   4: bipush 12
    //   6: if_icmpeq +5 -> 11
    //   9: iconst_0
    //   10: ireturn
    //   11: aload_0
    //   12: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   15: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   18: invokevirtual 407	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:lock	()V
    //   21: aload_0
    //   22: getfield 222	android/bluetooth/BluetoothAdapter:mService	Landroid/bluetooth/IBluetooth;
    //   25: ifnull +39 -> 64
    //   28: aload_0
    //   29: getfield 222	android/bluetooth/BluetoothAdapter:mService	Landroid/bluetooth/IBluetooth;
    //   32: invokeinterface 411 1 0
    //   37: istore_1
    //   38: aload_0
    //   39: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   42: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   45: invokevirtual 412	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   48: iload_1
    //   49: ireturn
    //   50: astore_2
    //   51: goto +25 -> 76
    //   54: astore_2
    //   55: ldc -114
    //   57: ldc -32
    //   59: aload_2
    //   60: invokestatic 230	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   63: pop
    //   64: aload_0
    //   65: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   68: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   71: invokevirtual 412	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   74: iconst_0
    //   75: ireturn
    //   76: aload_0
    //   77: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   80: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   83: invokevirtual 412	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   86: aload_2
    //   87: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	88	0	this	BluetoothAdapter
    //   37	12	1	bool	boolean
    //   50	1	2	localObject	Object
    //   54	33	2	localRemoteException	RemoteException
    // Exception table:
    //   from	to	target	type
    //   11	38	50	finally
    //   55	64	50	finally
    //   11	38	54	android/os/RemoteException
  }
  
  public boolean changeApplicationBluetoothState(boolean paramBoolean, BluetoothStateChangeCallback paramBluetoothStateChangeCallback)
  {
    return false;
  }
  
  public void closeProfileProxy(int paramInt, BluetoothProfile paramBluetoothProfile)
  {
    if (paramBluetoothProfile == null) {
      return;
    }
    switch (paramInt)
    {
    case 6: 
    case 13: 
    case 14: 
    case 15: 
    case 20: 
    default: 
      break;
    case 23: 
      ((BluetoothBATransmitter)paramBluetoothProfile).close();
      break;
    case 22: 
      ((BluetoothDun)paramBluetoothProfile).close();
      break;
    case 21: 
      ((BluetoothHearingAid)paramBluetoothProfile).close();
      break;
    case 19: 
      ((BluetoothHidDevice)paramBluetoothProfile).close();
      break;
    case 18: 
      ((BluetoothMapClient)paramBluetoothProfile).close();
      break;
    case 17: 
      ((BluetoothPbapClient)paramBluetoothProfile).close();
      break;
    case 16: 
      ((BluetoothHeadsetClient)paramBluetoothProfile).close();
      break;
    case 12: 
      ((BluetoothAvrcpController)paramBluetoothProfile).close();
      break;
    case 11: 
      ((BluetoothA2dpSink)paramBluetoothProfile).close();
      break;
    case 10: 
      ((BluetoothSap)paramBluetoothProfile).close();
      break;
    case 9: 
      ((BluetoothMap)paramBluetoothProfile).close();
      break;
    case 8: 
      ((BluetoothGattServer)paramBluetoothProfile).close();
      break;
    case 7: 
      ((BluetoothGatt)paramBluetoothProfile).close();
      break;
    case 5: 
      ((BluetoothPan)paramBluetoothProfile).close();
      break;
    case 4: 
      ((BluetoothHidHost)paramBluetoothProfile).close();
      break;
    case 3: 
      ((BluetoothHealth)paramBluetoothProfile).close();
      break;
    case 2: 
      ((BluetoothA2dp)paramBluetoothProfile).close();
      break;
    case 1: 
      ((BluetoothHeadset)paramBluetoothProfile).close();
    }
  }
  
  public boolean disable()
  {
    SeempLog.record(57);
    try
    {
      boolean bool = mManagerService.disable(ActivityThread.currentPackageName(), true);
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("BluetoothAdapter", "", localRemoteException);
    }
    return false;
  }
  
  public boolean disable(boolean paramBoolean)
  {
    SeempLog.record(57);
    try
    {
      paramBoolean = mManagerService.disable(ActivityThread.currentPackageName(), paramBoolean);
      return paramBoolean;
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("BluetoothAdapter", "", localRemoteException);
    }
    return false;
  }
  
  @SystemApi
  public boolean disableBLE()
  {
    if (!isBleScanAlwaysAvailable()) {
      return false;
    }
    int i = getLeState();
    if ((i != 12) && (i != 15))
    {
      Log.d("BluetoothAdapter", "disableBLE(): Already disabled");
      return false;
    }
    String str = ActivityThread.currentPackageName();
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("disableBLE(): de-registering ");
    localStringBuilder.append(str);
    Log.d("BluetoothAdapter", localStringBuilder.toString());
    try
    {
      mManagerService.updateBleAppCount(mToken, false, str);
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("BluetoothAdapter", "", localRemoteException);
    }
    return true;
  }
  
  public boolean enable()
  {
    SeempLog.record(56);
    if (isEnabled())
    {
      Log.d("BluetoothAdapter", "enable(): BT already enabled!");
      return true;
    }
    try
    {
      boolean bool = mManagerService.enable(ActivityThread.currentPackageName());
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("BluetoothAdapter", "", localRemoteException);
    }
    return false;
  }
  
  @SystemApi
  public boolean enableBLE()
  {
    if (!isBleScanAlwaysAvailable()) {
      return false;
    }
    try
    {
      String str = ActivityThread.currentPackageName();
      mManagerService.updateBleAppCount(mToken, true, str);
      if (isLeEnabled())
      {
        Log.d("BluetoothAdapter", "enableBLE(): Bluetooth already enabled");
        return true;
      }
      Log.d("BluetoothAdapter", "enableBLE(): Calling enable");
      boolean bool = mManagerService.enable(str);
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("BluetoothAdapter", "", localRemoteException);
    }
    return false;
  }
  
  @SystemApi
  public boolean enableNoAutoConnect()
  {
    if (isEnabled())
    {
      Log.d("BluetoothAdapter", "enableNoAutoConnect(): BT already enabled!");
      return true;
    }
    try
    {
      boolean bool = mManagerService.enableNoAutoConnect(ActivityThread.currentPackageName());
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("BluetoothAdapter", "", localRemoteException);
    }
    return false;
  }
  
  /* Error */
  public boolean factoryReset()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   4: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   7: invokevirtual 407	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:lock	()V
    //   10: aload_0
    //   11: getfield 235	android/bluetooth/BluetoothAdapter:mManagerService	Landroid/bluetooth/IBluetoothManager;
    //   14: ifnull +48 -> 62
    //   17: ldc_w 531
    //   20: ldc_w 533
    //   23: invokestatic 539	android/os/SystemProperties:set	(Ljava/lang/String;Ljava/lang/String;)V
    //   26: aload_0
    //   27: getfield 235	android/bluetooth/BluetoothAdapter:mManagerService	Landroid/bluetooth/IBluetoothManager;
    //   30: invokeinterface 541 1 0
    //   35: istore_1
    //   36: aload_0
    //   37: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   40: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   43: invokevirtual 412	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   46: iload_1
    //   47: ireturn
    //   48: astore_2
    //   49: goto +25 -> 74
    //   52: astore_2
    //   53: ldc -114
    //   55: ldc -32
    //   57: aload_2
    //   58: invokestatic 230	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   61: pop
    //   62: aload_0
    //   63: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   66: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   69: invokevirtual 412	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   72: iconst_0
    //   73: ireturn
    //   74: aload_0
    //   75: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   78: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   81: invokevirtual 412	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   84: aload_2
    //   85: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	86	0	this	BluetoothAdapter
    //   35	12	1	bool	boolean
    //   48	1	2	localObject	Object
    //   52	33	2	localRemoteException	RemoteException
    // Exception table:
    //   from	to	target	type
    //   0	36	48	finally
    //   53	62	48	finally
    //   0	36	52	android/os/RemoteException
  }
  
  /* Error */
  protected void finalize()
    throws java.lang.Throwable
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 235	android/bluetooth/BluetoothAdapter:mManagerService	Landroid/bluetooth/IBluetoothManager;
    //   4: aload_0
    //   5: getfield 200	android/bluetooth/BluetoothAdapter:mManagerCallback	Landroid/bluetooth/IBluetoothManagerCallback;
    //   8: invokeinterface 548 2 0
    //   13: aload_0
    //   14: invokespecial 550	java/lang/Object:finalize	()V
    //   17: goto +20 -> 37
    //   20: astore_1
    //   21: goto +17 -> 38
    //   24: astore_1
    //   25: ldc -114
    //   27: ldc -32
    //   29: aload_1
    //   30: invokestatic 230	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   33: pop
    //   34: goto -21 -> 13
    //   37: return
    //   38: aload_0
    //   39: invokespecial 550	java/lang/Object:finalize	()V
    //   42: aload_1
    //   43: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	44	0	this	BluetoothAdapter
    //   20	1	1	localObject	Object
    //   24	19	1	localRemoteException	RemoteException
    // Exception table:
    //   from	to	target	type
    //   0	13	20	finally
    //   25	34	20	finally
    //   0	13	24	android/os/RemoteException
  }
  
  public String getAddress()
  {
    try
    {
      String str = mManagerService.getAddress();
      return str;
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("BluetoothAdapter", "", localRemoteException);
    }
    return null;
  }
  
  /* Error */
  public BluetoothClass getBluetoothClass()
  {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual 400	android/bluetooth/BluetoothAdapter:getState	()I
    //   4: bipush 12
    //   6: if_icmpeq +5 -> 11
    //   9: aconst_null
    //   10: areturn
    //   11: aload_0
    //   12: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   15: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   18: invokevirtual 407	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:lock	()V
    //   21: aload_0
    //   22: getfield 222	android/bluetooth/BluetoothAdapter:mService	Landroid/bluetooth/IBluetooth;
    //   25: ifnull +39 -> 64
    //   28: aload_0
    //   29: getfield 222	android/bluetooth/BluetoothAdapter:mService	Landroid/bluetooth/IBluetooth;
    //   32: invokeinterface 557 1 0
    //   37: astore_1
    //   38: aload_0
    //   39: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   42: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   45: invokevirtual 412	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   48: aload_1
    //   49: areturn
    //   50: astore_1
    //   51: goto +25 -> 76
    //   54: astore_1
    //   55: ldc -114
    //   57: ldc -32
    //   59: aload_1
    //   60: invokestatic 230	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   63: pop
    //   64: aload_0
    //   65: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   68: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   71: invokevirtual 412	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   74: aconst_null
    //   75: areturn
    //   76: aload_0
    //   77: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   80: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   83: invokevirtual 412	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   86: aload_1
    //   87: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	88	0	this	BluetoothAdapter
    //   37	12	1	localBluetoothClass	BluetoothClass
    //   50	1	1	localObject	Object
    //   54	33	1	localRemoteException	RemoteException
    // Exception table:
    //   from	to	target	type
    //   11	38	50	finally
    //   55	64	50	finally
    //   11	38	54	android/os/RemoteException
  }
  
  public BluetoothLeAdvertiser getBluetoothLeAdvertiser()
  {
    if (!getLeAccess()) {
      return null;
    }
    synchronized (mLock)
    {
      if (sBluetoothLeAdvertiser == null)
      {
        BluetoothLeAdvertiser localBluetoothLeAdvertiser = new android/bluetooth/le/BluetoothLeAdvertiser;
        localBluetoothLeAdvertiser.<init>(mManagerService);
        sBluetoothLeAdvertiser = localBluetoothLeAdvertiser;
      }
      return sBluetoothLeAdvertiser;
    }
  }
  
  public BluetoothLeScanner getBluetoothLeScanner()
  {
    if (!getLeAccess()) {
      return null;
    }
    synchronized (mLock)
    {
      if (sBluetoothLeScanner == null)
      {
        BluetoothLeScanner localBluetoothLeScanner = new android/bluetooth/le/BluetoothLeScanner;
        localBluetoothLeScanner.<init>(mManagerService);
        sBluetoothLeScanner = localBluetoothLeScanner;
      }
      return sBluetoothLeScanner;
    }
  }
  
  IBluetoothManager getBluetoothManager()
  {
    return mManagerService;
  }
  
  IBluetooth getBluetoothService(IBluetoothManagerCallback paramIBluetoothManagerCallback)
  {
    ArrayList localArrayList = mProxyServiceStateCallbacks;
    if (paramIBluetoothManagerCallback == null) {
      try
      {
        Log.w("BluetoothAdapter", "getBluetoothService() called with no BluetoothManagerCallback");
      }
      finally
      {
        break label54;
      }
    } else if (!mProxyServiceStateCallbacks.contains(paramIBluetoothManagerCallback)) {
      mProxyServiceStateCallbacks.add(paramIBluetoothManagerCallback);
    }
    return mService;
    label54:
    throw paramIBluetoothManagerCallback;
  }
  
  /* Error */
  public Set<BluetoothDevice> getBondedDevices()
  {
    // Byte code:
    //   0: bipush 61
    //   2: invokestatic 479	android/util/SeempLog:record	(I)I
    //   5: pop
    //   6: aload_0
    //   7: invokevirtual 400	android/bluetooth/BluetoothAdapter:getState	()I
    //   10: bipush 12
    //   12: if_icmpeq +12 -> 24
    //   15: aload_0
    //   16: iconst_0
    //   17: anewarray 587	android/bluetooth/BluetoothDevice
    //   20: invokespecial 589	android/bluetooth/BluetoothAdapter:toDeviceSet	([Landroid/bluetooth/BluetoothDevice;)Ljava/util/Set;
    //   23: areturn
    //   24: aload_0
    //   25: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   28: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   31: invokevirtual 407	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:lock	()V
    //   34: aload_0
    //   35: getfield 222	android/bluetooth/BluetoothAdapter:mService	Landroid/bluetooth/IBluetooth;
    //   38: ifnull +29 -> 67
    //   41: aload_0
    //   42: aload_0
    //   43: getfield 222	android/bluetooth/BluetoothAdapter:mService	Landroid/bluetooth/IBluetooth;
    //   46: invokeinterface 592 1 0
    //   51: invokespecial 589	android/bluetooth/BluetoothAdapter:toDeviceSet	([Landroid/bluetooth/BluetoothDevice;)Ljava/util/Set;
    //   54: astore_1
    //   55: aload_0
    //   56: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   59: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   62: invokevirtual 412	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   65: aload_1
    //   66: areturn
    //   67: aload_0
    //   68: iconst_0
    //   69: anewarray 587	android/bluetooth/BluetoothDevice
    //   72: invokespecial 589	android/bluetooth/BluetoothAdapter:toDeviceSet	([Landroid/bluetooth/BluetoothDevice;)Ljava/util/Set;
    //   75: astore_1
    //   76: aload_0
    //   77: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   80: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   83: invokevirtual 412	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   86: aload_1
    //   87: areturn
    //   88: astore_1
    //   89: goto +25 -> 114
    //   92: astore_1
    //   93: ldc -114
    //   95: ldc -32
    //   97: aload_1
    //   98: invokestatic 230	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   101: pop
    //   102: aload_0
    //   103: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   106: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   109: invokevirtual 412	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   112: aconst_null
    //   113: areturn
    //   114: aload_0
    //   115: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   118: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   121: invokevirtual 412	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   124: aload_1
    //   125: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	126	0	this	BluetoothAdapter
    //   54	33	1	localSet	Set
    //   88	1	1	localObject	Object
    //   92	33	1	localRemoteException	RemoteException
    // Exception table:
    //   from	to	target	type
    //   24	55	88	finally
    //   67	76	88	finally
    //   93	102	88	finally
    //   24	55	92	android/os/RemoteException
    //   67	76	92	android/os/RemoteException
  }
  
  /* Error */
  public int getConnectionState()
  {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual 400	android/bluetooth/BluetoothAdapter:getState	()I
    //   4: bipush 12
    //   6: if_icmpeq +5 -> 11
    //   9: iconst_0
    //   10: ireturn
    //   11: aload_0
    //   12: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   15: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   18: invokevirtual 407	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:lock	()V
    //   21: aload_0
    //   22: getfield 222	android/bluetooth/BluetoothAdapter:mService	Landroid/bluetooth/IBluetooth;
    //   25: ifnull +40 -> 65
    //   28: aload_0
    //   29: getfield 222	android/bluetooth/BluetoothAdapter:mService	Landroid/bluetooth/IBluetooth;
    //   32: invokeinterface 597 1 0
    //   37: istore_1
    //   38: aload_0
    //   39: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   42: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   45: invokevirtual 412	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   48: iload_1
    //   49: ireturn
    //   50: astore_2
    //   51: goto +26 -> 77
    //   54: astore_2
    //   55: ldc -114
    //   57: ldc_w 599
    //   60: aload_2
    //   61: invokestatic 230	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   64: pop
    //   65: aload_0
    //   66: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   69: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   72: invokevirtual 412	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   75: iconst_0
    //   76: ireturn
    //   77: aload_0
    //   78: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   81: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   84: invokevirtual 412	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   87: aload_2
    //   88: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	89	0	this	BluetoothAdapter
    //   37	12	1	i	int
    //   50	1	2	localObject	Object
    //   54	34	2	localRemoteException	RemoteException
    // Exception table:
    //   from	to	target	type
    //   11	38	50	finally
    //   55	65	50	finally
    //   11	38	54	android/os/RemoteException
  }
  
  @Deprecated
  public BluetoothActivityEnergyInfo getControllerActivityEnergyInfo(int paramInt)
  {
    Object localObject = new SynchronousResultReceiver();
    requestControllerActivityEnergyInfo((ResultReceiver)localObject);
    try
    {
      localObject = ((SynchronousResultReceiver)localObject).awaitResult(1000L);
      if (bundle != null)
      {
        localObject = (BluetoothActivityEnergyInfo)bundle.getParcelable("controller_activity");
        return localObject;
      }
    }
    catch (TimeoutException localTimeoutException)
    {
      Log.e("BluetoothAdapter", "getControllerActivityEnergyInfo timed out");
    }
    return null;
  }
  
  /* Error */
  public int getDiscoverableTimeout()
  {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual 400	android/bluetooth/BluetoothAdapter:getState	()I
    //   4: bipush 12
    //   6: if_icmpeq +5 -> 11
    //   9: iconst_m1
    //   10: ireturn
    //   11: aload_0
    //   12: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   15: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   18: invokevirtual 407	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:lock	()V
    //   21: aload_0
    //   22: getfield 222	android/bluetooth/BluetoothAdapter:mService	Landroid/bluetooth/IBluetooth;
    //   25: ifnull +39 -> 64
    //   28: aload_0
    //   29: getfield 222	android/bluetooth/BluetoothAdapter:mService	Landroid/bluetooth/IBluetooth;
    //   32: invokeinterface 638 1 0
    //   37: istore_1
    //   38: aload_0
    //   39: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   42: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   45: invokevirtual 412	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   48: iload_1
    //   49: ireturn
    //   50: astore_2
    //   51: goto +25 -> 76
    //   54: astore_2
    //   55: ldc -114
    //   57: ldc -32
    //   59: aload_2
    //   60: invokestatic 230	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   63: pop
    //   64: aload_0
    //   65: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   68: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   71: invokevirtual 412	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   74: iconst_m1
    //   75: ireturn
    //   76: aload_0
    //   77: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   80: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   83: invokevirtual 412	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   86: aload_2
    //   87: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	88	0	this	BluetoothAdapter
    //   37	12	1	i	int
    //   50	1	2	localObject	Object
    //   54	33	2	localRemoteException	RemoteException
    // Exception table:
    //   from	to	target	type
    //   11	38	50	finally
    //   55	64	50	finally
    //   11	38	54	android/os/RemoteException
  }
  
  /* Error */
  public long getDiscoveryEndMillis()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   4: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   7: invokevirtual 407	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:lock	()V
    //   10: aload_0
    //   11: getfield 222	android/bluetooth/BluetoothAdapter:mService	Landroid/bluetooth/IBluetooth;
    //   14: ifnull +39 -> 53
    //   17: aload_0
    //   18: getfield 222	android/bluetooth/BluetoothAdapter:mService	Landroid/bluetooth/IBluetooth;
    //   21: invokeinterface 642 1 0
    //   26: lstore_1
    //   27: aload_0
    //   28: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   31: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   34: invokevirtual 412	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   37: lload_1
    //   38: lreturn
    //   39: astore_3
    //   40: goto +27 -> 67
    //   43: astore_3
    //   44: ldc -114
    //   46: ldc -32
    //   48: aload_3
    //   49: invokestatic 230	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   52: pop
    //   53: aload_0
    //   54: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   57: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   60: invokevirtual 412	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   63: ldc2_w 643
    //   66: lreturn
    //   67: aload_0
    //   68: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   71: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   74: invokevirtual 412	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   77: aload_3
    //   78: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	79	0	this	BluetoothAdapter
    //   26	12	1	l	long
    //   39	1	3	localObject	Object
    //   43	35	3	localRemoteException	RemoteException
    // Exception table:
    //   from	to	target	type
    //   0	27	39	finally
    //   44	53	39	finally
    //   0	27	43	android/os/RemoteException
  }
  
  boolean getLeAccess()
  {
    if (getLeState() == 12) {
      return true;
    }
    return getLeState() == 15;
  }
  
  /* Error */
  public int getLeMaximumAdvertisingDataLength()
  {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual 561	android/bluetooth/BluetoothAdapter:getLeAccess	()Z
    //   4: ifne +5 -> 9
    //   7: iconst_0
    //   8: ireturn
    //   9: aload_0
    //   10: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   13: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   16: invokevirtual 407	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:lock	()V
    //   19: aload_0
    //   20: getfield 222	android/bluetooth/BluetoothAdapter:mService	Landroid/bluetooth/IBluetooth;
    //   23: ifnull +40 -> 63
    //   26: aload_0
    //   27: getfield 222	android/bluetooth/BluetoothAdapter:mService	Landroid/bluetooth/IBluetooth;
    //   30: invokeinterface 647 1 0
    //   35: istore_1
    //   36: aload_0
    //   37: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   40: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   43: invokevirtual 412	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   46: iload_1
    //   47: ireturn
    //   48: astore_2
    //   49: goto +26 -> 75
    //   52: astore_2
    //   53: ldc -114
    //   55: ldc_w 649
    //   58: aload_2
    //   59: invokestatic 230	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   62: pop
    //   63: aload_0
    //   64: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   67: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   70: invokevirtual 412	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   73: iconst_0
    //   74: ireturn
    //   75: aload_0
    //   76: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   79: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   82: invokevirtual 412	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   85: aload_2
    //   86: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	87	0	this	BluetoothAdapter
    //   35	12	1	i	int
    //   48	1	2	localObject	Object
    //   52	34	2	localRemoteException	RemoteException
    // Exception table:
    //   from	to	target	type
    //   9	36	48	finally
    //   53	63	48	finally
    //   9	36	52	android/os/RemoteException
  }
  
  /* Error */
  public int getLeState()
  {
    // Byte code:
    //   0: bipush 10
    //   2: istore_1
    //   3: aload_0
    //   4: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   7: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   10: invokevirtual 407	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:lock	()V
    //   13: iload_1
    //   14: istore_2
    //   15: aload_0
    //   16: getfield 222	android/bluetooth/BluetoothAdapter:mService	Landroid/bluetooth/IBluetooth;
    //   19: ifnull +32 -> 51
    //   22: aload_0
    //   23: getfield 222	android/bluetooth/BluetoothAdapter:mService	Landroid/bluetooth/IBluetooth;
    //   26: invokeinterface 650 1 0
    //   31: istore_2
    //   32: goto +19 -> 51
    //   35: astore_3
    //   36: goto +27 -> 63
    //   39: astore_3
    //   40: ldc -114
    //   42: ldc -32
    //   44: aload_3
    //   45: invokestatic 230	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   48: pop
    //   49: iload_1
    //   50: istore_2
    //   51: aload_0
    //   52: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   55: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   58: invokevirtual 412	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   61: iload_2
    //   62: ireturn
    //   63: aload_0
    //   64: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   67: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   70: invokevirtual 412	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   73: aload_3
    //   74: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	75	0	this	BluetoothAdapter
    //   2	48	1	i	int
    //   14	48	2	j	int
    //   35	1	3	localObject	Object
    //   39	35	3	localRemoteException	RemoteException
    // Exception table:
    //   from	to	target	type
    //   3	13	35	finally
    //   15	32	35	finally
    //   40	49	35	finally
    //   3	13	39	android/os/RemoteException
    //   15	32	39	android/os/RemoteException
  }
  
  /* Error */
  public int getMaxConnectedAudioDevices()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   4: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   7: invokevirtual 407	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:lock	()V
    //   10: aload_0
    //   11: getfield 222	android/bluetooth/BluetoothAdapter:mService	Landroid/bluetooth/IBluetooth;
    //   14: ifnull +40 -> 54
    //   17: aload_0
    //   18: getfield 222	android/bluetooth/BluetoothAdapter:mService	Landroid/bluetooth/IBluetooth;
    //   21: invokeinterface 653 1 0
    //   26: istore_1
    //   27: aload_0
    //   28: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   31: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   34: invokevirtual 412	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   37: iload_1
    //   38: ireturn
    //   39: astore_2
    //   40: goto +26 -> 66
    //   43: astore_2
    //   44: ldc -114
    //   46: ldc_w 655
    //   49: aload_2
    //   50: invokestatic 230	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   53: pop
    //   54: aload_0
    //   55: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   58: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   61: invokevirtual 412	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   64: iconst_1
    //   65: ireturn
    //   66: aload_0
    //   67: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   70: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   73: invokevirtual 412	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   76: aload_2
    //   77: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	78	0	this	BluetoothAdapter
    //   26	12	1	i	int
    //   39	1	2	localObject	Object
    //   43	34	2	localRemoteException	RemoteException
    // Exception table:
    //   from	to	target	type
    //   0	27	39	finally
    //   44	54	39	finally
    //   0	27	43	android/os/RemoteException
  }
  
  public String getName()
  {
    try
    {
      String str = mManagerService.getName();
      return str;
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("BluetoothAdapter", "", localRemoteException);
    }
    return null;
  }
  
  public PeriodicAdvertisingManager getPeriodicAdvertisingManager()
  {
    if (!getLeAccess()) {
      return null;
    }
    if (!isLePeriodicAdvertisingSupported()) {
      return null;
    }
    synchronized (mLock)
    {
      if (sPeriodicAdvertisingManager == null)
      {
        PeriodicAdvertisingManager localPeriodicAdvertisingManager = new android/bluetooth/le/PeriodicAdvertisingManager;
        localPeriodicAdvertisingManager.<init>(mManagerService);
        sPeriodicAdvertisingManager = localPeriodicAdvertisingManager;
      }
      return sPeriodicAdvertisingManager;
    }
  }
  
  /* Error */
  public int getProfileConnectionState(int paramInt)
  {
    // Byte code:
    //   0: bipush 64
    //   2: invokestatic 479	android/util/SeempLog:record	(I)I
    //   5: pop
    //   6: aload_0
    //   7: invokevirtual 400	android/bluetooth/BluetoothAdapter:getState	()I
    //   10: bipush 12
    //   12: if_icmpeq +5 -> 17
    //   15: iconst_0
    //   16: ireturn
    //   17: aload_0
    //   18: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   21: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   24: invokevirtual 407	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:lock	()V
    //   27: aload_0
    //   28: getfield 222	android/bluetooth/BluetoothAdapter:mService	Landroid/bluetooth/IBluetooth;
    //   31: ifnull +41 -> 72
    //   34: aload_0
    //   35: getfield 222	android/bluetooth/BluetoothAdapter:mService	Landroid/bluetooth/IBluetooth;
    //   38: iload_1
    //   39: invokeinterface 671 2 0
    //   44: istore_1
    //   45: aload_0
    //   46: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   49: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   52: invokevirtual 412	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   55: iload_1
    //   56: ireturn
    //   57: astore_2
    //   58: goto +26 -> 84
    //   61: astore_2
    //   62: ldc -114
    //   64: ldc_w 673
    //   67: aload_2
    //   68: invokestatic 230	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   71: pop
    //   72: aload_0
    //   73: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   76: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   79: invokevirtual 412	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   82: iconst_0
    //   83: ireturn
    //   84: aload_0
    //   85: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   88: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   91: invokevirtual 412	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   94: aload_2
    //   95: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	96	0	this	BluetoothAdapter
    //   0	96	1	paramInt	int
    //   57	1	2	localObject	Object
    //   61	34	2	localRemoteException	RemoteException
    // Exception table:
    //   from	to	target	type
    //   17	45	57	finally
    //   62	72	57	finally
    //   17	45	61	android/os/RemoteException
  }
  
  public boolean getProfileProxy(Context paramContext, BluetoothProfile.ServiceListener paramServiceListener, int paramInt)
  {
    if ((paramContext != null) && (paramServiceListener != null))
    {
      if (paramInt == 1)
      {
        new BluetoothHeadset(paramContext, paramServiceListener);
        return true;
      }
      if (paramInt == 2)
      {
        new BluetoothA2dp(paramContext, paramServiceListener);
        return true;
      }
      if (paramInt == 11)
      {
        new BluetoothA2dpSink(paramContext, paramServiceListener);
        return true;
      }
      if (paramInt == 12)
      {
        new BluetoothAvrcpController(paramContext, paramServiceListener);
        return true;
      }
      if (paramInt == 4)
      {
        new BluetoothHidHost(paramContext, paramServiceListener);
        return true;
      }
      if (paramInt == 5)
      {
        new BluetoothPan(paramContext, paramServiceListener);
        return true;
      }
      if (paramInt == 22)
      {
        new BluetoothDun(paramContext, paramServiceListener);
        return true;
      }
      if (paramInt == 3)
      {
        new BluetoothHealth(paramContext, paramServiceListener);
        return true;
      }
      if (paramInt == 9)
      {
        new BluetoothMap(paramContext, paramServiceListener);
        return true;
      }
      if (paramInt == 16)
      {
        new BluetoothHeadsetClient(paramContext, paramServiceListener);
        return true;
      }
      if (paramInt == 10)
      {
        new BluetoothSap(paramContext, paramServiceListener);
        return true;
      }
      if (paramInt == 17)
      {
        new BluetoothPbapClient(paramContext, paramServiceListener);
        return true;
      }
      if (paramInt == 18)
      {
        new BluetoothMapClient(paramContext, paramServiceListener);
        return true;
      }
      if (paramInt == 19)
      {
        new BluetoothHidDevice(paramContext, paramServiceListener);
        return true;
      }
      if (paramInt == 21)
      {
        new BluetoothHearingAid(paramContext, paramServiceListener);
        return true;
      }
      if (paramInt == 23)
      {
        new BluetoothBATransmitter(paramContext, paramServiceListener);
        return true;
      }
      return false;
    }
    return false;
  }
  
  public BluetoothDevice getRemoteDevice(String paramString)
  {
    SeempLog.record(62);
    return new BluetoothDevice(paramString);
  }
  
  public BluetoothDevice getRemoteDevice(byte[] paramArrayOfByte)
  {
    SeempLog.record(62);
    if ((paramArrayOfByte != null) && (paramArrayOfByte.length == 6)) {
      return new BluetoothDevice(String.format(Locale.US, "%02X:%02X:%02X:%02X:%02X:%02X", new Object[] { Byte.valueOf(paramArrayOfByte[0]), Byte.valueOf(paramArrayOfByte[1]), Byte.valueOf(paramArrayOfByte[2]), Byte.valueOf(paramArrayOfByte[3]), Byte.valueOf(paramArrayOfByte[4]), Byte.valueOf(paramArrayOfByte[5]) }));
    }
    throw new IllegalArgumentException("Bluetooth address must have 6 bytes");
  }
  
  /* Error */
  public int getScanMode()
  {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual 400	android/bluetooth/BluetoothAdapter:getState	()I
    //   4: bipush 12
    //   6: if_icmpeq +6 -> 12
    //   9: bipush 20
    //   11: ireturn
    //   12: aload_0
    //   13: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   16: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   19: invokevirtual 407	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:lock	()V
    //   22: aload_0
    //   23: getfield 222	android/bluetooth/BluetoothAdapter:mService	Landroid/bluetooth/IBluetooth;
    //   26: ifnull +39 -> 65
    //   29: aload_0
    //   30: getfield 222	android/bluetooth/BluetoothAdapter:mService	Landroid/bluetooth/IBluetooth;
    //   33: invokeinterface 720 1 0
    //   38: istore_1
    //   39: aload_0
    //   40: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   43: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   46: invokevirtual 412	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   49: iload_1
    //   50: ireturn
    //   51: astore_2
    //   52: goto +26 -> 78
    //   55: astore_2
    //   56: ldc -114
    //   58: ldc -32
    //   60: aload_2
    //   61: invokestatic 230	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   64: pop
    //   65: aload_0
    //   66: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   69: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   72: invokevirtual 412	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   75: bipush 20
    //   77: ireturn
    //   78: aload_0
    //   79: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   82: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   85: invokevirtual 412	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   88: aload_2
    //   89: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	90	0	this	BluetoothAdapter
    //   38	12	1	i	int
    //   51	1	2	localObject	Object
    //   55	34	2	localRemoteException	RemoteException
    // Exception table:
    //   from	to	target	type
    //   12	39	51	finally
    //   56	65	51	finally
    //   12	39	55	android/os/RemoteException
  }
  
  /* Error */
  public int getState()
  {
    // Byte code:
    //   0: bipush 63
    //   2: invokestatic 479	android/util/SeempLog:record	(I)I
    //   5: pop
    //   6: bipush 10
    //   8: istore_1
    //   9: aload_0
    //   10: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   13: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   16: invokevirtual 407	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:lock	()V
    //   19: iload_1
    //   20: istore_2
    //   21: aload_0
    //   22: getfield 222	android/bluetooth/BluetoothAdapter:mService	Landroid/bluetooth/IBluetooth;
    //   25: ifnull +32 -> 57
    //   28: aload_0
    //   29: getfield 222	android/bluetooth/BluetoothAdapter:mService	Landroid/bluetooth/IBluetooth;
    //   32: invokeinterface 650 1 0
    //   37: istore_2
    //   38: goto +19 -> 57
    //   41: astore_3
    //   42: goto +50 -> 92
    //   45: astore_3
    //   46: ldc -114
    //   48: ldc -32
    //   50: aload_3
    //   51: invokestatic 230	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   54: pop
    //   55: iload_1
    //   56: istore_2
    //   57: aload_0
    //   58: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   61: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   64: invokevirtual 412	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   67: iload_2
    //   68: bipush 15
    //   70: if_icmpeq +17 -> 87
    //   73: iload_2
    //   74: bipush 14
    //   76: if_icmpeq +11 -> 87
    //   79: iload_2
    //   80: istore_1
    //   81: iload_2
    //   82: bipush 16
    //   84: if_icmpne +6 -> 90
    //   87: bipush 10
    //   89: istore_1
    //   90: iload_1
    //   91: ireturn
    //   92: aload_0
    //   93: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   96: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   99: invokevirtual 412	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   102: aload_3
    //   103: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	104	0	this	BluetoothAdapter
    //   8	83	1	i	int
    //   20	65	2	j	int
    //   41	1	3	localObject	Object
    //   45	58	3	localRemoteException	RemoteException
    // Exception table:
    //   from	to	target	type
    //   9	19	41	finally
    //   21	38	41	finally
    //   46	55	41	finally
    //   9	19	45	android/os/RemoteException
    //   21	38	45	android/os/RemoteException
  }
  
  public List<Integer> getSupportedProfiles()
  {
    localArrayList = new ArrayList();
    try
    {
      synchronized (mManagerCallback)
      {
        if (mService != null)
        {
          long l = mService.getSupportedProfiles();
          for (int i = 0; i <= 23; i++) {
            if ((1 << i & l) != 0L) {
              localArrayList.add(Integer.valueOf(i));
            }
          }
        }
      }
      return localArrayList;
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("BluetoothAdapter", "getSupportedProfiles:", localRemoteException);
    }
  }
  
  /* Error */
  public ParcelUuid[] getUuids()
  {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual 400	android/bluetooth/BluetoothAdapter:getState	()I
    //   4: bipush 12
    //   6: if_icmpeq +5 -> 11
    //   9: aconst_null
    //   10: areturn
    //   11: aload_0
    //   12: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   15: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   18: invokevirtual 407	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:lock	()V
    //   21: aload_0
    //   22: getfield 222	android/bluetooth/BluetoothAdapter:mService	Landroid/bluetooth/IBluetooth;
    //   25: ifnull +39 -> 64
    //   28: aload_0
    //   29: getfield 222	android/bluetooth/BluetoothAdapter:mService	Landroid/bluetooth/IBluetooth;
    //   32: invokeinterface 736 1 0
    //   37: astore_1
    //   38: aload_0
    //   39: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   42: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   45: invokevirtual 412	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   48: aload_1
    //   49: areturn
    //   50: astore_1
    //   51: goto +25 -> 76
    //   54: astore_1
    //   55: ldc -114
    //   57: ldc -32
    //   59: aload_1
    //   60: invokestatic 230	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   63: pop
    //   64: aload_0
    //   65: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   68: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   71: invokevirtual 412	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   74: aconst_null
    //   75: areturn
    //   76: aload_0
    //   77: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   80: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   83: invokevirtual 412	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   86: aload_1
    //   87: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	88	0	this	BluetoothAdapter
    //   37	12	1	arrayOfParcelUuid	ParcelUuid[]
    //   50	1	1	localObject	Object
    //   54	33	1	localRemoteException	RemoteException
    // Exception table:
    //   from	to	target	type
    //   11	38	50	finally
    //   55	64	50	finally
    //   11	38	54	android/os/RemoteException
  }
  
  @SystemApi
  public boolean isBleScanAlwaysAvailable()
  {
    try
    {
      boolean bool = mManagerService.isBleScanAlwaysAvailable();
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("BluetoothAdapter", "remote expection when calling isBleScanAlwaysAvailable", localRemoteException);
    }
    return false;
  }
  
  /* Error */
  public boolean isDiscovering()
  {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual 400	android/bluetooth/BluetoothAdapter:getState	()I
    //   4: bipush 12
    //   6: if_icmpeq +5 -> 11
    //   9: iconst_0
    //   10: ireturn
    //   11: aload_0
    //   12: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   15: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   18: invokevirtual 407	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:lock	()V
    //   21: aload_0
    //   22: getfield 222	android/bluetooth/BluetoothAdapter:mService	Landroid/bluetooth/IBluetooth;
    //   25: ifnull +39 -> 64
    //   28: aload_0
    //   29: getfield 222	android/bluetooth/BluetoothAdapter:mService	Landroid/bluetooth/IBluetooth;
    //   32: invokeinterface 742 1 0
    //   37: istore_1
    //   38: aload_0
    //   39: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   42: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   45: invokevirtual 412	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   48: iload_1
    //   49: ireturn
    //   50: astore_2
    //   51: goto +25 -> 76
    //   54: astore_2
    //   55: ldc -114
    //   57: ldc -32
    //   59: aload_2
    //   60: invokestatic 230	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   63: pop
    //   64: aload_0
    //   65: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   68: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   71: invokevirtual 412	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   74: iconst_0
    //   75: ireturn
    //   76: aload_0
    //   77: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   80: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   83: invokevirtual 412	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   86: aload_2
    //   87: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	88	0	this	BluetoothAdapter
    //   37	12	1	bool	boolean
    //   50	1	2	localObject	Object
    //   54	33	2	localRemoteException	RemoteException
    // Exception table:
    //   from	to	target	type
    //   11	38	50	finally
    //   55	64	50	finally
    //   11	38	54	android/os/RemoteException
  }
  
  /* Error */
  public boolean isEnabled()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   4: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   7: invokevirtual 407	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:lock	()V
    //   10: aload_0
    //   11: getfield 222	android/bluetooth/BluetoothAdapter:mService	Landroid/bluetooth/IBluetooth;
    //   14: ifnull +39 -> 53
    //   17: aload_0
    //   18: getfield 222	android/bluetooth/BluetoothAdapter:mService	Landroid/bluetooth/IBluetooth;
    //   21: invokeinterface 743 1 0
    //   26: istore_1
    //   27: aload_0
    //   28: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   31: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   34: invokevirtual 412	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   37: iload_1
    //   38: ireturn
    //   39: astore_2
    //   40: goto +25 -> 65
    //   43: astore_2
    //   44: ldc -114
    //   46: ldc -32
    //   48: aload_2
    //   49: invokestatic 230	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   52: pop
    //   53: aload_0
    //   54: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   57: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   60: invokevirtual 412	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   63: iconst_0
    //   64: ireturn
    //   65: aload_0
    //   66: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   69: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   72: invokevirtual 412	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   75: aload_2
    //   76: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	77	0	this	BluetoothAdapter
    //   26	12	1	bool	boolean
    //   39	1	2	localObject	Object
    //   43	33	2	localRemoteException	RemoteException
    // Exception table:
    //   from	to	target	type
    //   0	27	39	finally
    //   44	53	39	finally
    //   0	27	43	android/os/RemoteException
  }
  
  public boolean isHardwareTrackingFiltersAvailable()
  {
    boolean bool1 = getLeAccess();
    boolean bool2 = false;
    if (!bool1) {
      return false;
    }
    try
    {
      IBluetoothGatt localIBluetoothGatt = mManagerService.getBluetoothGatt();
      if (localIBluetoothGatt == null) {
        return false;
      }
      int i = localIBluetoothGatt.numHwTrackFiltersAvailable();
      if (i != 0) {
        bool2 = true;
      }
      return bool2;
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("BluetoothAdapter", "", localRemoteException);
    }
    return false;
  }
  
  /* Error */
  public boolean isLe2MPhySupported()
  {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual 561	android/bluetooth/BluetoothAdapter:getLeAccess	()Z
    //   4: ifne +5 -> 9
    //   7: iconst_0
    //   8: ireturn
    //   9: aload_0
    //   10: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   13: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   16: invokevirtual 407	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:lock	()V
    //   19: aload_0
    //   20: getfield 222	android/bluetooth/BluetoothAdapter:mService	Landroid/bluetooth/IBluetooth;
    //   23: ifnull +40 -> 63
    //   26: aload_0
    //   27: getfield 222	android/bluetooth/BluetoothAdapter:mService	Landroid/bluetooth/IBluetooth;
    //   30: invokeinterface 756 1 0
    //   35: istore_1
    //   36: aload_0
    //   37: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   40: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   43: invokevirtual 412	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   46: iload_1
    //   47: ireturn
    //   48: astore_2
    //   49: goto +26 -> 75
    //   52: astore_2
    //   53: ldc -114
    //   55: ldc_w 758
    //   58: aload_2
    //   59: invokestatic 230	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   62: pop
    //   63: aload_0
    //   64: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   67: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   70: invokevirtual 412	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   73: iconst_0
    //   74: ireturn
    //   75: aload_0
    //   76: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   79: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   82: invokevirtual 412	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   85: aload_2
    //   86: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	87	0	this	BluetoothAdapter
    //   35	12	1	bool	boolean
    //   48	1	2	localObject	Object
    //   52	34	2	localRemoteException	RemoteException
    // Exception table:
    //   from	to	target	type
    //   9	36	48	finally
    //   53	63	48	finally
    //   9	36	52	android/os/RemoteException
  }
  
  /* Error */
  public boolean isLeCodedPhySupported()
  {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual 561	android/bluetooth/BluetoothAdapter:getLeAccess	()Z
    //   4: ifne +5 -> 9
    //   7: iconst_0
    //   8: ireturn
    //   9: aload_0
    //   10: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   13: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   16: invokevirtual 407	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:lock	()V
    //   19: aload_0
    //   20: getfield 222	android/bluetooth/BluetoothAdapter:mService	Landroid/bluetooth/IBluetooth;
    //   23: ifnull +40 -> 63
    //   26: aload_0
    //   27: getfield 222	android/bluetooth/BluetoothAdapter:mService	Landroid/bluetooth/IBluetooth;
    //   30: invokeinterface 761 1 0
    //   35: istore_1
    //   36: aload_0
    //   37: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   40: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   43: invokevirtual 412	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   46: iload_1
    //   47: ireturn
    //   48: astore_2
    //   49: goto +26 -> 75
    //   52: astore_2
    //   53: ldc -114
    //   55: ldc_w 763
    //   58: aload_2
    //   59: invokestatic 230	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   62: pop
    //   63: aload_0
    //   64: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   67: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   70: invokevirtual 412	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   73: iconst_0
    //   74: ireturn
    //   75: aload_0
    //   76: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   79: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   82: invokevirtual 412	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   85: aload_2
    //   86: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	87	0	this	BluetoothAdapter
    //   35	12	1	bool	boolean
    //   48	1	2	localObject	Object
    //   52	34	2	localRemoteException	RemoteException
    // Exception table:
    //   from	to	target	type
    //   9	36	48	finally
    //   53	63	48	finally
    //   9	36	52	android/os/RemoteException
  }
  
  @SystemApi
  public boolean isLeEnabled()
  {
    int i = getLeState();
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("isLeEnabled(): ");
    localStringBuilder.append(nameForState(i));
    Log.d("BluetoothAdapter", localStringBuilder.toString());
    boolean bool;
    if ((i != 12) && (i != 15)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  /* Error */
  public boolean isLeExtendedAdvertisingSupported()
  {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual 561	android/bluetooth/BluetoothAdapter:getLeAccess	()Z
    //   4: ifne +5 -> 9
    //   7: iconst_0
    //   8: ireturn
    //   9: aload_0
    //   10: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   13: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   16: invokevirtual 407	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:lock	()V
    //   19: aload_0
    //   20: getfield 222	android/bluetooth/BluetoothAdapter:mService	Landroid/bluetooth/IBluetooth;
    //   23: ifnull +40 -> 63
    //   26: aload_0
    //   27: getfield 222	android/bluetooth/BluetoothAdapter:mService	Landroid/bluetooth/IBluetooth;
    //   30: invokeinterface 770 1 0
    //   35: istore_1
    //   36: aload_0
    //   37: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   40: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   43: invokevirtual 412	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   46: iload_1
    //   47: ireturn
    //   48: astore_2
    //   49: goto +26 -> 75
    //   52: astore_2
    //   53: ldc -114
    //   55: ldc_w 772
    //   58: aload_2
    //   59: invokestatic 230	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   62: pop
    //   63: aload_0
    //   64: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   67: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   70: invokevirtual 412	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   73: iconst_0
    //   74: ireturn
    //   75: aload_0
    //   76: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   79: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   82: invokevirtual 412	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   85: aload_2
    //   86: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	87	0	this	BluetoothAdapter
    //   35	12	1	bool	boolean
    //   48	1	2	localObject	Object
    //   52	34	2	localRemoteException	RemoteException
    // Exception table:
    //   from	to	target	type
    //   9	36	48	finally
    //   53	63	48	finally
    //   9	36	52	android/os/RemoteException
  }
  
  /* Error */
  public boolean isLePeriodicAdvertisingSupported()
  {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual 561	android/bluetooth/BluetoothAdapter:getLeAccess	()Z
    //   4: ifne +5 -> 9
    //   7: iconst_0
    //   8: ireturn
    //   9: aload_0
    //   10: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   13: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   16: invokevirtual 407	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:lock	()V
    //   19: aload_0
    //   20: getfield 222	android/bluetooth/BluetoothAdapter:mService	Landroid/bluetooth/IBluetooth;
    //   23: ifnull +40 -> 63
    //   26: aload_0
    //   27: getfield 222	android/bluetooth/BluetoothAdapter:mService	Landroid/bluetooth/IBluetooth;
    //   30: invokeinterface 773 1 0
    //   35: istore_1
    //   36: aload_0
    //   37: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   40: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   43: invokevirtual 412	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   46: iload_1
    //   47: ireturn
    //   48: astore_2
    //   49: goto +26 -> 75
    //   52: astore_2
    //   53: ldc -114
    //   55: ldc_w 775
    //   58: aload_2
    //   59: invokestatic 230	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   62: pop
    //   63: aload_0
    //   64: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   67: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   70: invokevirtual 412	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   73: iconst_0
    //   74: ireturn
    //   75: aload_0
    //   76: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   79: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   82: invokevirtual 412	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   85: aload_2
    //   86: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	87	0	this	BluetoothAdapter
    //   35	12	1	bool	boolean
    //   48	1	2	localObject	Object
    //   52	34	2	localRemoteException	RemoteException
    // Exception table:
    //   from	to	target	type
    //   9	36	48	finally
    //   53	63	48	finally
    //   9	36	52	android/os/RemoteException
  }
  
  /* Error */
  public boolean isMultipleAdvertisementSupported()
  {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual 400	android/bluetooth/BluetoothAdapter:getState	()I
    //   4: bipush 12
    //   6: if_icmpeq +5 -> 11
    //   9: iconst_0
    //   10: ireturn
    //   11: aload_0
    //   12: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   15: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   18: invokevirtual 407	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:lock	()V
    //   21: aload_0
    //   22: getfield 222	android/bluetooth/BluetoothAdapter:mService	Landroid/bluetooth/IBluetooth;
    //   25: ifnull +40 -> 65
    //   28: aload_0
    //   29: getfield 222	android/bluetooth/BluetoothAdapter:mService	Landroid/bluetooth/IBluetooth;
    //   32: invokeinterface 779 1 0
    //   37: istore_1
    //   38: aload_0
    //   39: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   42: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   45: invokevirtual 412	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   48: iload_1
    //   49: ireturn
    //   50: astore_2
    //   51: goto +26 -> 77
    //   54: astore_2
    //   55: ldc -114
    //   57: ldc_w 781
    //   60: aload_2
    //   61: invokestatic 230	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   64: pop
    //   65: aload_0
    //   66: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   69: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   72: invokevirtual 412	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   75: iconst_0
    //   76: ireturn
    //   77: aload_0
    //   78: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   81: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   84: invokevirtual 412	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   87: aload_2
    //   88: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	89	0	this	BluetoothAdapter
    //   37	12	1	bool	boolean
    //   50	1	2	localObject	Object
    //   54	34	2	localRemoteException	RemoteException
    // Exception table:
    //   from	to	target	type
    //   11	38	50	finally
    //   55	65	50	finally
    //   11	38	54	android/os/RemoteException
  }
  
  /* Error */
  public boolean isOffloadedFilteringSupported()
  {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual 561	android/bluetooth/BluetoothAdapter:getLeAccess	()Z
    //   4: ifne +5 -> 9
    //   7: iconst_0
    //   8: ireturn
    //   9: aload_0
    //   10: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   13: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   16: invokevirtual 407	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:lock	()V
    //   19: aload_0
    //   20: getfield 222	android/bluetooth/BluetoothAdapter:mService	Landroid/bluetooth/IBluetooth;
    //   23: ifnull +40 -> 63
    //   26: aload_0
    //   27: getfield 222	android/bluetooth/BluetoothAdapter:mService	Landroid/bluetooth/IBluetooth;
    //   30: invokeinterface 784 1 0
    //   35: istore_1
    //   36: aload_0
    //   37: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   40: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   43: invokevirtual 412	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   46: iload_1
    //   47: ireturn
    //   48: astore_2
    //   49: goto +26 -> 75
    //   52: astore_2
    //   53: ldc -114
    //   55: ldc_w 786
    //   58: aload_2
    //   59: invokestatic 230	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   62: pop
    //   63: aload_0
    //   64: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   67: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   70: invokevirtual 412	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   73: iconst_0
    //   74: ireturn
    //   75: aload_0
    //   76: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   79: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   82: invokevirtual 412	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   85: aload_2
    //   86: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	87	0	this	BluetoothAdapter
    //   35	12	1	bool	boolean
    //   48	1	2	localObject	Object
    //   52	34	2	localRemoteException	RemoteException
    // Exception table:
    //   from	to	target	type
    //   9	36	48	finally
    //   53	63	48	finally
    //   9	36	52	android/os/RemoteException
  }
  
  /* Error */
  public boolean isOffloadedScanBatchingSupported()
  {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual 561	android/bluetooth/BluetoothAdapter:getLeAccess	()Z
    //   4: ifne +5 -> 9
    //   7: iconst_0
    //   8: ireturn
    //   9: aload_0
    //   10: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   13: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   16: invokevirtual 407	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:lock	()V
    //   19: aload_0
    //   20: getfield 222	android/bluetooth/BluetoothAdapter:mService	Landroid/bluetooth/IBluetooth;
    //   23: ifnull +40 -> 63
    //   26: aload_0
    //   27: getfield 222	android/bluetooth/BluetoothAdapter:mService	Landroid/bluetooth/IBluetooth;
    //   30: invokeinterface 789 1 0
    //   35: istore_1
    //   36: aload_0
    //   37: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   40: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   43: invokevirtual 412	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   46: iload_1
    //   47: ireturn
    //   48: astore_2
    //   49: goto +26 -> 75
    //   52: astore_2
    //   53: ldc -114
    //   55: ldc_w 791
    //   58: aload_2
    //   59: invokestatic 230	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   62: pop
    //   63: aload_0
    //   64: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   67: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   70: invokevirtual 412	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   73: iconst_0
    //   74: ireturn
    //   75: aload_0
    //   76: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   79: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   82: invokevirtual 412	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   85: aload_2
    //   86: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	87	0	this	BluetoothAdapter
    //   35	12	1	bool	boolean
    //   48	1	2	localObject	Object
    //   52	34	2	localRemoteException	RemoteException
    // Exception table:
    //   from	to	target	type
    //   9	36	48	finally
    //   53	63	48	finally
    //   9	36	52	android/os/RemoteException
  }
  
  public BluetoothServerSocket listenUsingEncryptedRfcommOn(int paramInt)
    throws IOException
  {
    Object localObject = new BluetoothServerSocket(1, false, true, paramInt);
    int i = mSocket.bindListen();
    if (paramInt == -2) {
      ((BluetoothServerSocket)localObject).setChannel(mSocket.getPort());
    }
    if (i >= 0) {
      return localObject;
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Error: ");
    ((StringBuilder)localObject).append(i);
    throw new IOException(((StringBuilder)localObject).toString());
  }
  
  public BluetoothServerSocket listenUsingEncryptedRfcommWithServiceRecord(String paramString, UUID paramUUID)
    throws IOException
  {
    return createNewRfcommSocketAndRecord(paramString, paramUUID, false, true);
  }
  
  public BluetoothServerSocket listenUsingInsecureL2capCoc(int paramInt)
    throws IOException
  {
    if (paramInt == 2)
    {
      BluetoothServerSocket localBluetoothServerSocket = new BluetoothServerSocket(4, false, false, -2, false, false);
      paramInt = mSocket.bindListen();
      if (paramInt == 0)
      {
        paramInt = mSocket.getPort();
        if (paramInt != 0)
        {
          localStringBuilder = new StringBuilder();
          localStringBuilder.append("listenUsingInsecureL2capOn: set assigned PSM to ");
          localStringBuilder.append(paramInt);
          Log.d("BluetoothAdapter", localStringBuilder.toString());
          localBluetoothServerSocket.setChannel(paramInt);
          return localBluetoothServerSocket;
        }
        throw new IOException("Error: Unable to assign PSM value");
      }
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("Error: ");
      localStringBuilder.append(paramInt);
      throw new IOException(localStringBuilder.toString());
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Unsupported transport: ");
    localStringBuilder.append(paramInt);
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  public BluetoothServerSocket listenUsingInsecureL2capOn(int paramInt)
    throws IOException
  {
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("listenUsingInsecureL2capOn: port=");
    ((StringBuilder)localObject).append(paramInt);
    Log.d("BluetoothAdapter", ((StringBuilder)localObject).toString());
    localObject = new BluetoothServerSocket(3, false, false, paramInt, false, false);
    int i = mSocket.bindListen();
    if (paramInt == -2)
    {
      paramInt = mSocket.getPort();
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("listenUsingInsecureL2capOn: set assigned channel to ");
      localStringBuilder.append(paramInt);
      Log.d("BluetoothAdapter", localStringBuilder.toString());
      ((BluetoothServerSocket)localObject).setChannel(paramInt);
    }
    if (i == 0) {
      return localObject;
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Error: ");
    ((StringBuilder)localObject).append(i);
    throw new IOException(((StringBuilder)localObject).toString());
  }
  
  public BluetoothServerSocket listenUsingInsecureRfcommOn(int paramInt)
    throws IOException
  {
    Object localObject = new BluetoothServerSocket(1, false, false, paramInt);
    int i = mSocket.bindListen();
    if (paramInt == -2) {
      ((BluetoothServerSocket)localObject).setChannel(mSocket.getPort());
    }
    if (i == 0) {
      return localObject;
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Error: ");
    ((StringBuilder)localObject).append(i);
    throw new IOException(((StringBuilder)localObject).toString());
  }
  
  public BluetoothServerSocket listenUsingInsecureRfcommWithServiceRecord(String paramString, UUID paramUUID)
    throws IOException
  {
    SeempLog.record(59);
    return createNewRfcommSocketAndRecord(paramString, paramUUID, false, false);
  }
  
  public BluetoothServerSocket listenUsingL2capCoc(int paramInt)
    throws IOException
  {
    if (paramInt == 2)
    {
      BluetoothServerSocket localBluetoothServerSocket = new BluetoothServerSocket(4, true, true, -2, false, false);
      paramInt = mSocket.bindListen();
      if (paramInt == 0)
      {
        paramInt = mSocket.getPort();
        if (paramInt != 0)
        {
          localStringBuilder = new StringBuilder();
          localStringBuilder.append("listenUsingL2capCoc: set assigned PSM to ");
          localStringBuilder.append(paramInt);
          Log.d("BluetoothAdapter", localStringBuilder.toString());
          localBluetoothServerSocket.setChannel(paramInt);
          return localBluetoothServerSocket;
        }
        throw new IOException("Error: Unable to assign PSM value");
      }
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("Error: ");
      localStringBuilder.append(paramInt);
      throw new IOException(localStringBuilder.toString());
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Unsupported transport: ");
    localStringBuilder.append(paramInt);
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  public BluetoothServerSocket listenUsingL2capOn(int paramInt)
    throws IOException
  {
    return listenUsingL2capOn(paramInt, false, false);
  }
  
  public BluetoothServerSocket listenUsingL2capOn(int paramInt, boolean paramBoolean1, boolean paramBoolean2)
    throws IOException
  {
    BluetoothServerSocket localBluetoothServerSocket = new BluetoothServerSocket(3, true, true, paramInt, paramBoolean1, paramBoolean2);
    int i = mSocket.bindListen();
    if (paramInt == -2)
    {
      paramInt = mSocket.getPort();
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("listenUsingL2capOn: set assigned channel to ");
      localStringBuilder.append(paramInt);
      Log.d("BluetoothAdapter", localStringBuilder.toString());
      localBluetoothServerSocket.setChannel(paramInt);
    }
    if (i == 0) {
      return localBluetoothServerSocket;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Error: ");
    localStringBuilder.append(i);
    throw new IOException(localStringBuilder.toString());
  }
  
  public BluetoothServerSocket listenUsingRfcommOn(int paramInt)
    throws IOException
  {
    return listenUsingRfcommOn(paramInt, false, false);
  }
  
  public BluetoothServerSocket listenUsingRfcommOn(int paramInt, boolean paramBoolean1, boolean paramBoolean2)
    throws IOException
  {
    Object localObject = new BluetoothServerSocket(1, true, true, paramInt, paramBoolean1, paramBoolean2);
    int i = mSocket.bindListen();
    if (paramInt == -2) {
      ((BluetoothServerSocket)localObject).setChannel(mSocket.getPort());
    }
    if (i == 0) {
      return localObject;
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Error: ");
    ((StringBuilder)localObject).append(i);
    throw new IOException(((StringBuilder)localObject).toString());
  }
  
  public BluetoothServerSocket listenUsingRfcommWithServiceRecord(String paramString, UUID paramUUID)
    throws IOException
  {
    return createNewRfcommSocketAndRecord(paramString, paramUUID, true, true);
  }
  
  public Pair<byte[], byte[]> readOutOfBandData()
  {
    return null;
  }
  
  void removeServiceStateCallback(IBluetoothManagerCallback paramIBluetoothManagerCallback)
  {
    synchronized (mProxyServiceStateCallbacks)
    {
      mProxyServiceStateCallbacks.remove(paramIBluetoothManagerCallback);
      return;
    }
  }
  
  /* Error */
  public void requestControllerActivityEnergyInfo(ResultReceiver paramResultReceiver)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   4: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   7: invokevirtual 407	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:lock	()V
    //   10: aload_1
    //   11: astore_2
    //   12: aload_0
    //   13: getfield 222	android/bluetooth/BluetoothAdapter:mService	Landroid/bluetooth/IBluetooth;
    //   16: ifnull +15 -> 31
    //   19: aload_0
    //   20: getfield 222	android/bluetooth/BluetoothAdapter:mService	Landroid/bluetooth/IBluetooth;
    //   23: aload_1
    //   24: invokeinterface 844 2 0
    //   29: aconst_null
    //   30: astore_2
    //   31: aload_0
    //   32: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   35: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   38: invokevirtual 412	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   41: aload_2
    //   42: ifnull +68 -> 110
    //   45: aload_2
    //   46: astore_1
    //   47: aload_1
    //   48: iconst_0
    //   49: aconst_null
    //   50: invokevirtual 850	android/os/ResultReceiver:send	(ILandroid/os/Bundle;)V
    //   53: goto +57 -> 110
    //   56: astore_2
    //   57: goto +54 -> 111
    //   60: astore_2
    //   61: new 310	java/lang/StringBuilder
    //   64: astore_3
    //   65: aload_3
    //   66: invokespecial 311	java/lang/StringBuilder:<init>	()V
    //   69: aload_3
    //   70: ldc_w 852
    //   73: invokevirtual 317	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   76: pop
    //   77: aload_3
    //   78: aload_2
    //   79: invokevirtual 855	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   82: pop
    //   83: ldc -114
    //   85: aload_3
    //   86: invokevirtual 324	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   89: invokestatic 349	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   92: pop
    //   93: aload_0
    //   94: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   97: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   100: invokevirtual 412	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   103: aload_1
    //   104: ifnull +6 -> 110
    //   107: goto -60 -> 47
    //   110: return
    //   111: aload_0
    //   112: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   115: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   118: invokevirtual 412	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   121: aload_1
    //   122: ifnull +9 -> 131
    //   125: aload_1
    //   126: iconst_0
    //   127: aconst_null
    //   128: invokevirtual 850	android/os/ResultReceiver:send	(ILandroid/os/Bundle;)V
    //   131: aload_2
    //   132: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	133	0	this	BluetoothAdapter
    //   0	133	1	paramResultReceiver	ResultReceiver
    //   11	35	2	localResultReceiver	ResultReceiver
    //   56	1	2	localObject	Object
    //   60	72	2	localRemoteException	RemoteException
    //   64	22	3	localStringBuilder	StringBuilder
    // Exception table:
    //   from	to	target	type
    //   0	10	56	finally
    //   12	29	56	finally
    //   61	93	56	finally
    //   0	10	60	android/os/RemoteException
    //   12	29	60	android/os/RemoteException
  }
  
  /* Error */
  public boolean setBluetoothClass(BluetoothClass paramBluetoothClass)
  {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual 400	android/bluetooth/BluetoothAdapter:getState	()I
    //   4: bipush 12
    //   6: if_icmpeq +5 -> 11
    //   9: iconst_0
    //   10: ireturn
    //   11: aload_0
    //   12: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   15: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   18: invokevirtual 407	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:lock	()V
    //   21: aload_0
    //   22: getfield 222	android/bluetooth/BluetoothAdapter:mService	Landroid/bluetooth/IBluetooth;
    //   25: ifnull +40 -> 65
    //   28: aload_0
    //   29: getfield 222	android/bluetooth/BluetoothAdapter:mService	Landroid/bluetooth/IBluetooth;
    //   32: aload_1
    //   33: invokeinterface 859 2 0
    //   38: istore_2
    //   39: aload_0
    //   40: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   43: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   46: invokevirtual 412	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   49: iload_2
    //   50: ireturn
    //   51: astore_1
    //   52: goto +25 -> 77
    //   55: astore_1
    //   56: ldc -114
    //   58: ldc -32
    //   60: aload_1
    //   61: invokestatic 230	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   64: pop
    //   65: aload_0
    //   66: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   69: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   72: invokevirtual 412	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   75: iconst_0
    //   76: ireturn
    //   77: aload_0
    //   78: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   81: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   84: invokevirtual 412	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   87: aload_1
    //   88: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	89	0	this	BluetoothAdapter
    //   0	89	1	paramBluetoothClass	BluetoothClass
    //   38	12	2	bool	boolean
    // Exception table:
    //   from	to	target	type
    //   11	39	51	finally
    //   56	65	51	finally
    //   11	39	55	android/os/RemoteException
  }
  
  /* Error */
  public void setDiscoverableTimeout(int paramInt)
  {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual 400	android/bluetooth/BluetoothAdapter:getState	()I
    //   4: bipush 12
    //   6: if_icmpeq +4 -> 10
    //   9: return
    //   10: aload_0
    //   11: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   14: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   17: invokevirtual 407	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:lock	()V
    //   20: aload_0
    //   21: getfield 222	android/bluetooth/BluetoothAdapter:mService	Landroid/bluetooth/IBluetooth;
    //   24: ifnull +31 -> 55
    //   27: aload_0
    //   28: getfield 222	android/bluetooth/BluetoothAdapter:mService	Landroid/bluetooth/IBluetooth;
    //   31: iload_1
    //   32: invokeinterface 863 2 0
    //   37: pop
    //   38: goto +17 -> 55
    //   41: astore_2
    //   42: goto +24 -> 66
    //   45: astore_2
    //   46: ldc -114
    //   48: ldc -32
    //   50: aload_2
    //   51: invokestatic 230	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   54: pop
    //   55: aload_0
    //   56: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   59: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   62: invokevirtual 412	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   65: return
    //   66: aload_0
    //   67: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   70: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   73: invokevirtual 412	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   76: aload_2
    //   77: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	78	0	this	BluetoothAdapter
    //   0	78	1	paramInt	int
    //   41	1	2	localObject	Object
    //   45	32	2	localRemoteException	RemoteException
    // Exception table:
    //   from	to	target	type
    //   10	38	41	finally
    //   46	55	41	finally
    //   10	38	45	android/os/RemoteException
  }
  
  /* Error */
  public boolean setName(String paramString)
  {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual 400	android/bluetooth/BluetoothAdapter:getState	()I
    //   4: bipush 12
    //   6: if_icmpeq +5 -> 11
    //   9: iconst_0
    //   10: ireturn
    //   11: aload_0
    //   12: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   15: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   18: invokevirtual 407	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:lock	()V
    //   21: aload_0
    //   22: getfield 222	android/bluetooth/BluetoothAdapter:mService	Landroid/bluetooth/IBluetooth;
    //   25: ifnull +80 -> 105
    //   28: new 310	java/lang/StringBuilder
    //   31: astore_2
    //   32: aload_2
    //   33: invokespecial 311	java/lang/StringBuilder:<init>	()V
    //   36: aload_2
    //   37: invokestatic 484	android/app/ActivityThread:currentPackageName	()Ljava/lang/String;
    //   40: invokevirtual 317	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   43: pop
    //   44: aload_2
    //   45: ldc_w 866
    //   48: invokevirtual 317	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   51: pop
    //   52: aload_2
    //   53: aload_1
    //   54: invokevirtual 317	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   57: pop
    //   58: ldc -114
    //   60: aload_2
    //   61: invokevirtual 324	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   64: invokestatic 500	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   67: pop
    //   68: aload_0
    //   69: getfield 222	android/bluetooth/BluetoothAdapter:mService	Landroid/bluetooth/IBluetooth;
    //   72: aload_1
    //   73: invokeinterface 868 2 0
    //   78: istore_3
    //   79: aload_0
    //   80: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   83: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   86: invokevirtual 412	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   89: iload_3
    //   90: ireturn
    //   91: astore_1
    //   92: goto +25 -> 117
    //   95: astore_1
    //   96: ldc -114
    //   98: ldc -32
    //   100: aload_1
    //   101: invokestatic 230	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   104: pop
    //   105: aload_0
    //   106: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   109: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   112: invokevirtual 412	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   115: iconst_0
    //   116: ireturn
    //   117: aload_0
    //   118: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   121: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   124: invokevirtual 412	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   127: aload_1
    //   128: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	129	0	this	BluetoothAdapter
    //   0	129	1	paramString	String
    //   31	30	2	localStringBuilder	StringBuilder
    //   78	12	3	bool	boolean
    // Exception table:
    //   from	to	target	type
    //   11	79	91	finally
    //   96	105	91	finally
    //   11	79	95	android/os/RemoteException
  }
  
  public boolean setScanMode(int paramInt)
  {
    if (getState() != 12) {
      return false;
    }
    return setScanMode(paramInt, getDiscoverableTimeout());
  }
  
  /* Error */
  public boolean setScanMode(int paramInt1, int paramInt2)
  {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual 400	android/bluetooth/BluetoothAdapter:getState	()I
    //   4: bipush 12
    //   6: if_icmpeq +5 -> 11
    //   9: iconst_0
    //   10: ireturn
    //   11: aload_0
    //   12: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   15: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   18: invokevirtual 407	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:lock	()V
    //   21: aload_0
    //   22: getfield 222	android/bluetooth/BluetoothAdapter:mService	Landroid/bluetooth/IBluetooth;
    //   25: ifnull +44 -> 69
    //   28: aload_0
    //   29: getfield 222	android/bluetooth/BluetoothAdapter:mService	Landroid/bluetooth/IBluetooth;
    //   32: iload_1
    //   33: iload_2
    //   34: invokeinterface 874 3 0
    //   39: istore_3
    //   40: aload_0
    //   41: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   44: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   47: invokevirtual 412	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   50: iload_3
    //   51: ireturn
    //   52: astore 4
    //   54: goto +27 -> 81
    //   57: astore 4
    //   59: ldc -114
    //   61: ldc -32
    //   63: aload 4
    //   65: invokestatic 230	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   68: pop
    //   69: aload_0
    //   70: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   73: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   76: invokevirtual 412	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   79: iconst_0
    //   80: ireturn
    //   81: aload_0
    //   82: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   85: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   88: invokevirtual 412	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   91: aload 4
    //   93: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	94	0	this	BluetoothAdapter
    //   0	94	1	paramInt1	int
    //   0	94	2	paramInt2	int
    //   39	12	3	bool	boolean
    //   52	1	4	localObject	Object
    //   57	35	4	localRemoteException	RemoteException
    // Exception table:
    //   from	to	target	type
    //   11	40	52	finally
    //   59	69	52	finally
    //   11	40	57	android/os/RemoteException
  }
  
  /* Error */
  public boolean startDiscovery()
  {
    // Byte code:
    //   0: bipush 58
    //   2: invokestatic 479	android/util/SeempLog:record	(I)I
    //   5: pop
    //   6: aload_0
    //   7: invokevirtual 400	android/bluetooth/BluetoothAdapter:getState	()I
    //   10: bipush 12
    //   12: if_icmpeq +5 -> 17
    //   15: iconst_0
    //   16: ireturn
    //   17: aload_0
    //   18: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   21: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   24: invokevirtual 407	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:lock	()V
    //   27: aload_0
    //   28: getfield 222	android/bluetooth/BluetoothAdapter:mService	Landroid/bluetooth/IBluetooth;
    //   31: ifnull +39 -> 70
    //   34: aload_0
    //   35: getfield 222	android/bluetooth/BluetoothAdapter:mService	Landroid/bluetooth/IBluetooth;
    //   38: invokeinterface 877 1 0
    //   43: istore_1
    //   44: aload_0
    //   45: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   48: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   51: invokevirtual 412	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   54: iload_1
    //   55: ireturn
    //   56: astore_2
    //   57: goto +25 -> 82
    //   60: astore_2
    //   61: ldc -114
    //   63: ldc -32
    //   65: aload_2
    //   66: invokestatic 230	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   69: pop
    //   70: aload_0
    //   71: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   74: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   77: invokevirtual 412	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   80: iconst_0
    //   81: ireturn
    //   82: aload_0
    //   83: getfield 193	android/bluetooth/BluetoothAdapter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   86: invokevirtual 404	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   89: invokevirtual 412	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   92: aload_2
    //   93: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	94	0	this	BluetoothAdapter
    //   43	12	1	bool	boolean
    //   56	1	2	localObject	Object
    //   60	33	2	localRemoteException	RemoteException
    // Exception table:
    //   from	to	target	type
    //   17	44	56	finally
    //   61	70	56	finally
    //   17	44	60	android/os/RemoteException
  }
  
  @Deprecated
  public boolean startLeScan(LeScanCallback paramLeScanCallback)
  {
    return startLeScan(null, paramLeScanCallback);
  }
  
  @Deprecated
  public boolean startLeScan(UUID[] paramArrayOfUUID, LeScanCallback paramLeScanCallback)
  {
    ??? = new StringBuilder();
    ((StringBuilder)???).append("startLeScan(): ");
    ((StringBuilder)???).append(Arrays.toString(paramArrayOfUUID));
    Log.d("BluetoothAdapter", ((StringBuilder)???).toString());
    if (paramLeScanCallback == null)
    {
      Log.e("BluetoothAdapter", "startLeScan: null callback");
      return false;
    }
    BluetoothLeScanner localBluetoothLeScanner = getBluetoothLeScanner();
    if (localBluetoothLeScanner == null)
    {
      Log.e("BluetoothAdapter", "startLeScan: cannot get BluetoothLeScanner");
      return false;
    }
    synchronized (mLeScanClients)
    {
      if (mLeScanClients.containsKey(paramLeScanCallback))
      {
        Log.e("BluetoothAdapter", "LE Scan has already started");
        return false;
      }
      try
      {
        Object localObject2 = mManagerService.getBluetoothGatt();
        if (localObject2 == null) {
          return false;
        }
        localObject2 = new android/bluetooth/BluetoothAdapter$2;
        ((2)localObject2).<init>(this, paramArrayOfUUID, paramLeScanCallback);
        Object localObject3 = new android/bluetooth/le/ScanSettings$Builder;
        ((ScanSettings.Builder)localObject3).<init>();
        localObject3 = ((ScanSettings.Builder)localObject3).setCallbackType(1).setScanMode(2).build();
        ArrayList localArrayList = new java/util/ArrayList;
        localArrayList.<init>();
        if ((paramArrayOfUUID != null) && (paramArrayOfUUID.length > 0))
        {
          ScanFilter.Builder localBuilder = new android/bluetooth/le/ScanFilter$Builder;
          localBuilder.<init>();
          ParcelUuid localParcelUuid = new android/os/ParcelUuid;
          localParcelUuid.<init>(paramArrayOfUUID[0]);
          localArrayList.add(localBuilder.setServiceUuid(localParcelUuid).build());
        }
        localBluetoothLeScanner.startScan(localArrayList, (ScanSettings)localObject3, (ScanCallback)localObject2);
        mLeScanClients.put(paramLeScanCallback, localObject2);
        return true;
      }
      catch (RemoteException paramArrayOfUUID)
      {
        Log.e("BluetoothAdapter", "", paramArrayOfUUID);
        return false;
      }
    }
  }
  
  @Deprecated
  public void stopLeScan(LeScanCallback paramLeScanCallback)
  {
    Log.d("BluetoothAdapter", "stopLeScan()");
    BluetoothLeScanner localBluetoothLeScanner = getBluetoothLeScanner();
    if (localBluetoothLeScanner == null) {
      return;
    }
    synchronized (mLeScanClients)
    {
      paramLeScanCallback = (ScanCallback)mLeScanClients.remove(paramLeScanCallback);
      if (paramLeScanCallback == null)
      {
        Log.d("BluetoothAdapter", "scan not started yet");
        return;
      }
      localBluetoothLeScanner.stopScan(paramLeScanCallback);
      return;
    }
  }
  
  /* Error */
  public void unregisterAdapter()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 235	android/bluetooth/BluetoothAdapter:mManagerService	Landroid/bluetooth/IBluetoothManager;
    //   4: ifnull +16 -> 20
    //   7: aload_0
    //   8: getfield 235	android/bluetooth/BluetoothAdapter:mManagerService	Landroid/bluetooth/IBluetoothManager;
    //   11: aload_0
    //   12: getfield 200	android/bluetooth/BluetoothAdapter:mManagerCallback	Landroid/bluetooth/IBluetoothManagerCallback;
    //   15: invokeinterface 548 2 0
    //   20: goto +20 -> 40
    //   23: astore_1
    //   24: goto +17 -> 41
    //   27: astore_1
    //   28: ldc -114
    //   30: ldc -32
    //   32: aload_1
    //   33: invokestatic 230	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   36: pop
    //   37: goto -17 -> 20
    //   40: return
    //   41: aload_1
    //   42: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	43	0	this	BluetoothAdapter
    //   23	1	1	localObject	Object
    //   27	15	1	localRemoteException	RemoteException
    // Exception table:
    //   from	to	target	type
    //   0	20	23	finally
    //   28	37	23	finally
    //   0	20	27	android/os/RemoteException
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface AdapterState {}
  
  public static abstract interface BluetoothStateChangeCallback
  {
    public abstract void onBluetoothStateChange(boolean paramBoolean);
  }
  
  public static abstract interface LeScanCallback
  {
    public abstract void onLeScan(BluetoothDevice paramBluetoothDevice, int paramInt, byte[] paramArrayOfByte);
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface ScanMode {}
  
  public class StateChangeCallbackWrapper
    extends IBluetoothStateChangeCallback.Stub
  {
    private BluetoothAdapter.BluetoothStateChangeCallback mCallback;
    
    StateChangeCallbackWrapper(BluetoothAdapter.BluetoothStateChangeCallback paramBluetoothStateChangeCallback)
    {
      mCallback = paramBluetoothStateChangeCallback;
    }
    
    public void onBluetoothStateChange(boolean paramBoolean)
    {
      mCallback.onBluetoothStateChange(paramBoolean);
    }
  }
}
