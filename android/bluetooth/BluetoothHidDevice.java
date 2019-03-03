package android.bluetooth;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

public final class BluetoothHidDevice
  implements BluetoothProfile
{
  public static final String ACTION_CONNECTION_STATE_CHANGED = "android.bluetooth.hiddevice.profile.action.CONNECTION_STATE_CHANGED";
  public static final byte ERROR_RSP_INVALID_PARAM = 4;
  public static final byte ERROR_RSP_INVALID_RPT_ID = 2;
  public static final byte ERROR_RSP_NOT_READY = 1;
  public static final byte ERROR_RSP_SUCCESS = 0;
  public static final byte ERROR_RSP_UNKNOWN = 14;
  public static final byte ERROR_RSP_UNSUPPORTED_REQ = 3;
  public static final byte PROTOCOL_BOOT_MODE = 0;
  public static final byte PROTOCOL_REPORT_MODE = 1;
  public static final byte REPORT_TYPE_FEATURE = 3;
  public static final byte REPORT_TYPE_INPUT = 1;
  public static final byte REPORT_TYPE_OUTPUT = 2;
  public static final byte SUBCLASS1_COMBO = -64;
  public static final byte SUBCLASS1_KEYBOARD = 64;
  public static final byte SUBCLASS1_MOUSE = -128;
  public static final byte SUBCLASS1_NONE = 0;
  public static final byte SUBCLASS2_CARD_READER = 6;
  public static final byte SUBCLASS2_DIGITIZER_TABLET = 5;
  public static final byte SUBCLASS2_GAMEPAD = 2;
  public static final byte SUBCLASS2_JOYSTICK = 1;
  public static final byte SUBCLASS2_REMOTE_CONTROL = 3;
  public static final byte SUBCLASS2_SENSING_DEVICE = 4;
  public static final byte SUBCLASS2_UNCATEGORIZED = 0;
  private static final String TAG = BluetoothHidDevice.class.getSimpleName();
  private BluetoothAdapter mAdapter;
  private final IBluetoothStateChangeCallback mBluetoothStateChangeCallback = new IBluetoothStateChangeCallback.Stub()
  {
    /* Error */
    public void onBluetoothStateChange(boolean paramAnonymousBoolean)
    {
      // Byte code:
      //   0: invokestatic 26	android/bluetooth/BluetoothHidDevice:access$000	()Ljava/lang/String;
      //   3: astore_2
      //   4: new 28	java/lang/StringBuilder
      //   7: dup
      //   8: invokespecial 29	java/lang/StringBuilder:<init>	()V
      //   11: astore_3
      //   12: aload_3
      //   13: ldc 31
      //   15: invokevirtual 35	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   18: pop
      //   19: aload_3
      //   20: iload_1
      //   21: invokevirtual 38	java/lang/StringBuilder:append	(Z)Ljava/lang/StringBuilder;
      //   24: pop
      //   25: aload_2
      //   26: aload_3
      //   27: invokevirtual 41	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   30: invokestatic 47	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
      //   33: pop
      //   34: aload_0
      //   35: getfield 12	android/bluetooth/BluetoothHidDevice$1:this$0	Landroid/bluetooth/BluetoothHidDevice;
      //   38: invokestatic 51	android/bluetooth/BluetoothHidDevice:access$100	(Landroid/bluetooth/BluetoothHidDevice;)Landroid/content/ServiceConnection;
      //   41: astore_3
      //   42: aload_3
      //   43: monitorenter
      //   44: iload_1
      //   45: ifeq +65 -> 110
      //   48: aload_0
      //   49: getfield 12	android/bluetooth/BluetoothHidDevice$1:this$0	Landroid/bluetooth/BluetoothHidDevice;
      //   52: invokestatic 55	android/bluetooth/BluetoothHidDevice:access$200	(Landroid/bluetooth/BluetoothHidDevice;)Landroid/bluetooth/IBluetoothHidDevice;
      //   55: ifnonnull +20 -> 75
      //   58: invokestatic 26	android/bluetooth/BluetoothHidDevice:access$000	()Ljava/lang/String;
      //   61: ldc 57
      //   63: invokestatic 47	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
      //   66: pop
      //   67: aload_0
      //   68: getfield 12	android/bluetooth/BluetoothHidDevice$1:this$0	Landroid/bluetooth/BluetoothHidDevice;
      //   71: invokevirtual 61	android/bluetooth/BluetoothHidDevice:doBind	()Z
      //   74: pop
      //   75: goto +51 -> 126
      //   78: astore_2
      //   79: goto +50 -> 129
      //   82: astore_2
      //   83: invokestatic 26	android/bluetooth/BluetoothHidDevice:access$000	()Ljava/lang/String;
      //   86: ldc 63
      //   88: aload_2
      //   89: invokestatic 67	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
      //   92: pop
      //   93: goto -18 -> 75
      //   96: astore_2
      //   97: invokestatic 26	android/bluetooth/BluetoothHidDevice:access$000	()Ljava/lang/String;
      //   100: ldc 63
      //   102: aload_2
      //   103: invokestatic 67	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
      //   106: pop
      //   107: goto -32 -> 75
      //   110: invokestatic 26	android/bluetooth/BluetoothHidDevice:access$000	()Ljava/lang/String;
      //   113: ldc 69
      //   115: invokestatic 47	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
      //   118: pop
      //   119: aload_0
      //   120: getfield 12	android/bluetooth/BluetoothHidDevice$1:this$0	Landroid/bluetooth/BluetoothHidDevice;
      //   123: invokevirtual 72	android/bluetooth/BluetoothHidDevice:doUnbind	()V
      //   126: aload_3
      //   127: monitorexit
      //   128: return
      //   129: aload_3
      //   130: monitorexit
      //   131: aload_2
      //   132: athrow
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	133	0	this	1
      //   0	133	1	paramAnonymousBoolean	boolean
      //   3	23	2	str	String
      //   78	1	2	localObject1	Object
      //   82	7	2	localSecurityException	SecurityException
      //   96	36	2	localIllegalStateException	IllegalStateException
      //   11	119	3	localObject2	Object
      // Exception table:
      //   from	to	target	type
      //   48	75	78	finally
      //   83	93	78	finally
      //   97	107	78	finally
      //   110	126	78	finally
      //   126	128	78	finally
      //   129	131	78	finally
      //   48	75	82	java/lang/SecurityException
      //   48	75	96	java/lang/IllegalStateException
    }
  };
  private final ServiceConnection mConnection = new ServiceConnection()
  {
    public void onServiceConnected(ComponentName paramAnonymousComponentName, IBinder paramAnonymousIBinder)
    {
      Log.d(BluetoothHidDevice.TAG, "onServiceConnected()");
      BluetoothHidDevice.access$202(BluetoothHidDevice.this, IBluetoothHidDevice.Stub.asInterface(paramAnonymousIBinder));
      if (mServiceListener != null) {
        mServiceListener.onServiceConnected(19, BluetoothHidDevice.this);
      }
    }
    
    public void onServiceDisconnected(ComponentName paramAnonymousComponentName)
    {
      Log.d(BluetoothHidDevice.TAG, "onServiceDisconnected()");
      BluetoothHidDevice.access$202(BluetoothHidDevice.this, null);
      if (mServiceListener != null) {
        mServiceListener.onServiceDisconnected(19);
      }
    }
  };
  private Context mContext;
  private volatile IBluetoothHidDevice mService;
  private BluetoothProfile.ServiceListener mServiceListener;
  
  BluetoothHidDevice(Context paramContext, BluetoothProfile.ServiceListener paramServiceListener)
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
        paramContext.printStackTrace();
      }
    }
    doBind();
  }
  
  void close()
  {
    IBluetoothManager localIBluetoothManager = mAdapter.getBluetoothManager();
    if (localIBluetoothManager != null) {
      try
      {
        localIBluetoothManager.unregisterStateChangeCallback(mBluetoothStateChangeCallback);
      }
      catch (RemoteException localRemoteException)
      {
        localRemoteException.printStackTrace();
      }
    }
    synchronized (mConnection)
    {
      doUnbind();
      mServiceListener = null;
      return;
    }
  }
  
  public boolean connect(BluetoothDevice paramBluetoothDevice)
  {
    boolean bool1 = false;
    boolean bool2 = false;
    IBluetoothHidDevice localIBluetoothHidDevice = mService;
    if (localIBluetoothHidDevice != null)
    {
      try
      {
        bool1 = localIBluetoothHidDevice.connect(paramBluetoothDevice);
        bool2 = bool1;
      }
      catch (RemoteException paramBluetoothDevice)
      {
        for (;;)
        {
          Log.e(TAG, paramBluetoothDevice.toString());
        }
      }
    }
    else
    {
      Log.w(TAG, "Proxy not attached to service");
      bool2 = bool1;
    }
    return bool2;
  }
  
  public boolean disconnect(BluetoothDevice paramBluetoothDevice)
  {
    boolean bool1 = false;
    boolean bool2 = false;
    IBluetoothHidDevice localIBluetoothHidDevice = mService;
    if (localIBluetoothHidDevice != null)
    {
      try
      {
        bool1 = localIBluetoothHidDevice.disconnect(paramBluetoothDevice);
        bool2 = bool1;
      }
      catch (RemoteException paramBluetoothDevice)
      {
        for (;;)
        {
          Log.e(TAG, paramBluetoothDevice.toString());
        }
      }
    }
    else
    {
      Log.w(TAG, "Proxy not attached to service");
      bool2 = bool1;
    }
    return bool2;
  }
  
  boolean doBind()
  {
    Intent localIntent = new Intent(IBluetoothHidDevice.class.getName());
    Object localObject = localIntent.resolveSystemService(mContext.getPackageManager(), 0);
    localIntent.setComponent((ComponentName)localObject);
    if ((localObject != null) && (mContext.bindServiceAsUser(localIntent, mConnection, 0, mContext.getUser())))
    {
      Log.d(TAG, "Bound to HID Device Service");
      return true;
    }
    localObject = TAG;
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Could not bind to Bluetooth HID Device Service with ");
    localStringBuilder.append(localIntent);
    Log.e((String)localObject, localStringBuilder.toString());
    return false;
  }
  
  void doUnbind()
  {
    if (mService != null)
    {
      mService = null;
      try
      {
        mContext.unbindService(mConnection);
      }
      catch (IllegalArgumentException localIllegalArgumentException)
      {
        Log.e(TAG, "Unable to unbind HidDevService", localIllegalArgumentException);
      }
    }
  }
  
  public List<BluetoothDevice> getConnectedDevices()
  {
    Object localObject = mService;
    if (localObject != null) {
      try
      {
        localObject = ((IBluetoothHidDevice)localObject).getConnectedDevices();
        return localObject;
      }
      catch (RemoteException localRemoteException)
      {
        Log.e(TAG, localRemoteException.toString());
      }
    } else {
      Log.w(TAG, "Proxy not attached to service");
    }
    return new ArrayList();
  }
  
  public int getConnectionState(BluetoothDevice paramBluetoothDevice)
  {
    IBluetoothHidDevice localIBluetoothHidDevice = mService;
    if (localIBluetoothHidDevice != null) {
      try
      {
        int i = localIBluetoothHidDevice.getConnectionState(paramBluetoothDevice);
        return i;
      }
      catch (RemoteException paramBluetoothDevice)
      {
        Log.e(TAG, paramBluetoothDevice.toString());
      }
    } else {
      Log.w(TAG, "Proxy not attached to service");
    }
    return 0;
  }
  
  public List<BluetoothDevice> getDevicesMatchingConnectionStates(int[] paramArrayOfInt)
  {
    IBluetoothHidDevice localIBluetoothHidDevice = mService;
    if (localIBluetoothHidDevice != null) {
      try
      {
        paramArrayOfInt = localIBluetoothHidDevice.getDevicesMatchingConnectionStates(paramArrayOfInt);
        return paramArrayOfInt;
      }
      catch (RemoteException paramArrayOfInt)
      {
        Log.e(TAG, paramArrayOfInt.toString());
      }
    } else {
      Log.w(TAG, "Proxy not attached to service");
    }
    return new ArrayList();
  }
  
  public String getUserAppName()
  {
    Object localObject = mService;
    if (localObject != null) {
      try
      {
        localObject = ((IBluetoothHidDevice)localObject).getUserAppName();
        return localObject;
      }
      catch (RemoteException localRemoteException)
      {
        Log.e(TAG, localRemoteException.toString());
      }
    } else {
      Log.w(TAG, "Proxy not attached to service");
    }
    return "";
  }
  
  public boolean registerApp(BluetoothHidDeviceAppSdpSettings paramBluetoothHidDeviceAppSdpSettings, BluetoothHidDeviceAppQosSettings paramBluetoothHidDeviceAppQosSettings1, BluetoothHidDeviceAppQosSettings paramBluetoothHidDeviceAppQosSettings2, Executor paramExecutor, Callback paramCallback)
  {
    boolean bool1 = false;
    boolean bool2 = false;
    if (paramBluetoothHidDeviceAppSdpSettings != null)
    {
      if (paramExecutor != null)
      {
        if (paramCallback != null)
        {
          IBluetoothHidDevice localIBluetoothHidDevice = mService;
          if (localIBluetoothHidDevice != null)
          {
            try
            {
              CallbackWrapper localCallbackWrapper = new android/bluetooth/BluetoothHidDevice$CallbackWrapper;
              localCallbackWrapper.<init>(paramExecutor, paramCallback);
              bool1 = localIBluetoothHidDevice.registerApp(paramBluetoothHidDeviceAppSdpSettings, paramBluetoothHidDeviceAppQosSettings1, paramBluetoothHidDeviceAppQosSettings2, localCallbackWrapper);
              bool2 = bool1;
            }
            catch (RemoteException paramBluetoothHidDeviceAppSdpSettings)
            {
              for (;;)
              {
                Log.e(TAG, paramBluetoothHidDeviceAppSdpSettings.toString());
              }
            }
          }
          else
          {
            Log.w(TAG, "Proxy not attached to service");
            bool2 = bool1;
          }
          return bool2;
        }
        throw new IllegalArgumentException("callback parameter cannot be null");
      }
      throw new IllegalArgumentException("executor parameter cannot be null");
    }
    throw new IllegalArgumentException("sdp parameter cannot be null");
  }
  
  public boolean replyReport(BluetoothDevice paramBluetoothDevice, byte paramByte1, byte paramByte2, byte[] paramArrayOfByte)
  {
    boolean bool1 = false;
    boolean bool2 = false;
    IBluetoothHidDevice localIBluetoothHidDevice = mService;
    if (localIBluetoothHidDevice != null)
    {
      try
      {
        bool1 = localIBluetoothHidDevice.replyReport(paramBluetoothDevice, paramByte1, paramByte2, paramArrayOfByte);
        bool2 = bool1;
      }
      catch (RemoteException paramBluetoothDevice)
      {
        for (;;)
        {
          Log.e(TAG, paramBluetoothDevice.toString());
        }
      }
    }
    else
    {
      Log.w(TAG, "Proxy not attached to service");
      bool2 = bool1;
    }
    return bool2;
  }
  
  public boolean reportError(BluetoothDevice paramBluetoothDevice, byte paramByte)
  {
    boolean bool1 = false;
    boolean bool2 = false;
    IBluetoothHidDevice localIBluetoothHidDevice = mService;
    if (localIBluetoothHidDevice != null)
    {
      try
      {
        bool1 = localIBluetoothHidDevice.reportError(paramBluetoothDevice, paramByte);
        bool2 = bool1;
      }
      catch (RemoteException paramBluetoothDevice)
      {
        for (;;)
        {
          Log.e(TAG, paramBluetoothDevice.toString());
        }
      }
    }
    else
    {
      Log.w(TAG, "Proxy not attached to service");
      bool2 = bool1;
    }
    return bool2;
  }
  
  public boolean sendReport(BluetoothDevice paramBluetoothDevice, int paramInt, byte[] paramArrayOfByte)
  {
    boolean bool1 = false;
    boolean bool2 = false;
    IBluetoothHidDevice localIBluetoothHidDevice = mService;
    if (localIBluetoothHidDevice != null)
    {
      try
      {
        bool1 = localIBluetoothHidDevice.sendReport(paramBluetoothDevice, paramInt, paramArrayOfByte);
        bool2 = bool1;
      }
      catch (RemoteException paramBluetoothDevice)
      {
        for (;;)
        {
          Log.e(TAG, paramBluetoothDevice.toString());
        }
      }
    }
    else
    {
      Log.w(TAG, "Proxy not attached to service");
      bool2 = bool1;
    }
    return bool2;
  }
  
  public boolean unregisterApp()
  {
    boolean bool1 = false;
    boolean bool2 = false;
    IBluetoothHidDevice localIBluetoothHidDevice = mService;
    if (localIBluetoothHidDevice != null)
    {
      try
      {
        bool1 = localIBluetoothHidDevice.unregisterApp();
        bool2 = bool1;
      }
      catch (RemoteException localRemoteException)
      {
        for (;;)
        {
          Log.e(TAG, localRemoteException.toString());
        }
      }
    }
    else
    {
      Log.w(TAG, "Proxy not attached to service");
      bool2 = bool1;
    }
    return bool2;
  }
  
  public static abstract class Callback
  {
    private static final String TAG = "BluetoothHidDevCallback";
    
    public Callback() {}
    
    public void onAppStatusChanged(BluetoothDevice paramBluetoothDevice, boolean paramBoolean)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("onAppStatusChanged: pluggedDevice=");
      localStringBuilder.append(paramBluetoothDevice);
      localStringBuilder.append(" registered=");
      localStringBuilder.append(paramBoolean);
      Log.d("BluetoothHidDevCallback", localStringBuilder.toString());
    }
    
    public void onConnectionStateChanged(BluetoothDevice paramBluetoothDevice, int paramInt)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("onConnectionStateChanged: device=");
      localStringBuilder.append(paramBluetoothDevice);
      localStringBuilder.append(" state=");
      localStringBuilder.append(paramInt);
      Log.d("BluetoothHidDevCallback", localStringBuilder.toString());
    }
    
    public void onGetReport(BluetoothDevice paramBluetoothDevice, byte paramByte1, byte paramByte2, int paramInt)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("onGetReport: device=");
      localStringBuilder.append(paramBluetoothDevice);
      localStringBuilder.append(" type=");
      localStringBuilder.append(paramByte1);
      localStringBuilder.append(" id=");
      localStringBuilder.append(paramByte2);
      localStringBuilder.append(" bufferSize=");
      localStringBuilder.append(paramInt);
      Log.d("BluetoothHidDevCallback", localStringBuilder.toString());
    }
    
    public void onInterruptData(BluetoothDevice paramBluetoothDevice, byte paramByte, byte[] paramArrayOfByte)
    {
      paramArrayOfByte = new StringBuilder();
      paramArrayOfByte.append("onInterruptData: device=");
      paramArrayOfByte.append(paramBluetoothDevice);
      paramArrayOfByte.append(" reportId=");
      paramArrayOfByte.append(paramByte);
      Log.d("BluetoothHidDevCallback", paramArrayOfByte.toString());
    }
    
    public void onSetProtocol(BluetoothDevice paramBluetoothDevice, byte paramByte)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("onSetProtocol: device=");
      localStringBuilder.append(paramBluetoothDevice);
      localStringBuilder.append(" protocol=");
      localStringBuilder.append(paramByte);
      Log.d("BluetoothHidDevCallback", localStringBuilder.toString());
    }
    
    public void onSetReport(BluetoothDevice paramBluetoothDevice, byte paramByte1, byte paramByte2, byte[] paramArrayOfByte)
    {
      paramArrayOfByte = new StringBuilder();
      paramArrayOfByte.append("onSetReport: device=");
      paramArrayOfByte.append(paramBluetoothDevice);
      paramArrayOfByte.append(" type=");
      paramArrayOfByte.append(paramByte1);
      paramArrayOfByte.append(" id=");
      paramArrayOfByte.append(paramByte2);
      Log.d("BluetoothHidDevCallback", paramArrayOfByte.toString());
    }
    
    public void onVirtualCableUnplug(BluetoothDevice paramBluetoothDevice)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("onVirtualCableUnplug: device=");
      localStringBuilder.append(paramBluetoothDevice);
      Log.d("BluetoothHidDevCallback", localStringBuilder.toString());
    }
  }
  
  private static class CallbackWrapper
    extends IBluetoothHidDeviceCallback.Stub
  {
    private final BluetoothHidDevice.Callback mCallback;
    private final Executor mExecutor;
    
    CallbackWrapper(Executor paramExecutor, BluetoothHidDevice.Callback paramCallback)
    {
      mExecutor = paramExecutor;
      mCallback = paramCallback;
    }
    
    public void onAppStatusChanged(BluetoothDevice paramBluetoothDevice, boolean paramBoolean)
    {
      clearCallingIdentity();
      mExecutor.execute(new _..Lambda.BluetoothHidDevice.CallbackWrapper.NFluHjT4zTfYBRXClu_2k6mPKFI(this, paramBluetoothDevice, paramBoolean));
    }
    
    public void onConnectionStateChanged(BluetoothDevice paramBluetoothDevice, int paramInt)
    {
      clearCallingIdentity();
      mExecutor.execute(new _..Lambda.BluetoothHidDevice.CallbackWrapper.qtStwQVkGfOs2iJIiePWqJJpi0w(this, paramBluetoothDevice, paramInt));
    }
    
    public void onGetReport(BluetoothDevice paramBluetoothDevice, byte paramByte1, byte paramByte2, int paramInt)
    {
      clearCallingIdentity();
      mExecutor.execute(new _..Lambda.BluetoothHidDevice.CallbackWrapper.Eyz_qG6mvTlh6a8Bp41ZoEJzQCQ(this, paramBluetoothDevice, paramByte1, paramByte2, paramInt));
    }
    
    public void onInterruptData(BluetoothDevice paramBluetoothDevice, byte paramByte, byte[] paramArrayOfByte)
    {
      clearCallingIdentity();
      mExecutor.execute(new _..Lambda.BluetoothHidDevice.CallbackWrapper.xW99_tc95OmGApoKnpQ9q1TXb9k(this, paramBluetoothDevice, paramByte, paramArrayOfByte));
    }
    
    public void onSetProtocol(BluetoothDevice paramBluetoothDevice, byte paramByte)
    {
      clearCallingIdentity();
      mExecutor.execute(new _..Lambda.BluetoothHidDevice.CallbackWrapper.ypkr5GGxsAkGSBiLjIRwg_PzqCM(this, paramBluetoothDevice, paramByte));
    }
    
    public void onSetReport(BluetoothDevice paramBluetoothDevice, byte paramByte1, byte paramByte2, byte[] paramArrayOfByte)
    {
      clearCallingIdentity();
      mExecutor.execute(new _..Lambda.BluetoothHidDevice.CallbackWrapper.3bTGVlfKj7Y0SZdifW_Ya2myDKs(this, paramBluetoothDevice, paramByte1, paramByte2, paramArrayOfByte));
    }
    
    public void onVirtualCableUnplug(BluetoothDevice paramBluetoothDevice)
    {
      clearCallingIdentity();
      mExecutor.execute(new _..Lambda.BluetoothHidDevice.CallbackWrapper.jiodzbAJAcleQCwlDcBjvDddELM(this, paramBluetoothDevice));
    }
  }
}
