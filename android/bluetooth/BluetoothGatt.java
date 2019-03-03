package android.bluetooth;

import android.os.Handler;
import android.os.ParcelUuid;
import android.os.RemoteException;
import android.util.Log;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public final class BluetoothGatt
  implements BluetoothProfile
{
  static final int AUTHENTICATION_MITM = 2;
  static final int AUTHENTICATION_NONE = 0;
  static final int AUTHENTICATION_NO_MITM = 1;
  private static final int AUTH_RETRY_STATE_IDLE = 0;
  private static final int AUTH_RETRY_STATE_MITM = 2;
  private static final int AUTH_RETRY_STATE_NO_MITM = 1;
  public static final int CONNECTION_PRIORITY_BALANCED = 0;
  public static final int CONNECTION_PRIORITY_HIGH = 1;
  public static final int CONNECTION_PRIORITY_LOW_POWER = 2;
  private static final int CONN_STATE_CLOSED = 4;
  private static final int CONN_STATE_CONNECTED = 2;
  private static final int CONN_STATE_CONNECTING = 1;
  private static final int CONN_STATE_DISCONNECTING = 3;
  private static final int CONN_STATE_IDLE = 0;
  private static final boolean DBG = true;
  public static final int GATT_CONNECTION_CONGESTED = 143;
  public static final int GATT_FAILURE = 257;
  public static final int GATT_INSUFFICIENT_AUTHENTICATION = 5;
  public static final int GATT_INSUFFICIENT_ENCRYPTION = 15;
  public static final int GATT_INVALID_ATTRIBUTE_LENGTH = 13;
  public static final int GATT_INVALID_OFFSET = 7;
  public static final int GATT_READ_NOT_PERMITTED = 2;
  public static final int GATT_REQUEST_NOT_SUPPORTED = 6;
  public static final int GATT_SUCCESS = 0;
  public static final int GATT_WRITE_NOT_PERMITTED = 3;
  private static final String TAG = "BluetoothGatt";
  private static final boolean VDBG = false;
  private int mAuthRetryState;
  private boolean mAutoConnect;
  private final IBluetoothGattCallback mBluetoothGattCallback = new IBluetoothGattCallback.Stub()
  {
    public void onCharacteristicRead(final String paramAnonymousString, final int paramAnonymousInt1, int paramAnonymousInt2, final byte[] paramAnonymousArrayOfByte)
    {
      if (!paramAnonymousString.equals(mDevice.getAddress())) {
        return;
      }
      synchronized (mDeviceBusy)
      {
        BluetoothGatt.access$1102(BluetoothGatt.this, Boolean.valueOf(false));
        if ((paramAnonymousInt1 == 5) || (paramAnonymousInt1 == 15))
        {
          int i = mAuthRetryState;
          int j = 2;
          if (i != 2) {
            try
            {
              if (mAuthRetryState == 0) {
                j = 1;
              }
              mService.readCharacteristic(mClientIf, paramAnonymousString, paramAnonymousInt2, j);
              BluetoothGatt.access$1308(BluetoothGatt.this);
              return;
            }
            catch (RemoteException paramAnonymousString)
            {
              Log.e("BluetoothGatt", "", paramAnonymousString);
            }
          }
        }
        BluetoothGatt.access$1302(BluetoothGatt.this, 0);
        paramAnonymousString = getCharacteristicById(mDevice, paramAnonymousInt2);
        if (paramAnonymousString == null)
        {
          Log.w("BluetoothGatt", "onCharacteristicRead() failed to find characteristic!");
          return;
        }
        BluetoothGatt.this.runOrQueueCallback(new Runnable()
        {
          public void run()
          {
            BluetoothGattCallback localBluetoothGattCallback = mCallback;
            if (localBluetoothGattCallback != null)
            {
              if (paramAnonymousInt1 == 0) {
                paramAnonymousString.setValue(paramAnonymousArrayOfByte);
              }
              localBluetoothGattCallback.onCharacteristicRead(BluetoothGatt.this, paramAnonymousString, paramAnonymousInt1);
            }
          }
        });
        return;
      }
    }
    
    public void onCharacteristicWrite(String paramAnonymousString, final int paramAnonymousInt1, int paramAnonymousInt2)
    {
      if (!paramAnonymousString.equals(mDevice.getAddress())) {
        return;
      }
      synchronized (mDeviceBusy)
      {
        BluetoothGatt.access$1102(BluetoothGatt.this, Boolean.valueOf(false));
        ??? = getCharacteristicById(mDevice, paramAnonymousInt2);
        if (??? == null) {
          return;
        }
        if ((paramAnonymousInt1 == 5) || (paramAnonymousInt1 == 15))
        {
          int i = mAuthRetryState;
          int j = 2;
          if (i != 2) {
            try
            {
              if (mAuthRetryState == 0) {
                j = 1;
              }
              for (;;)
              {
                break;
              }
              mService.writeCharacteristic(mClientIf, paramAnonymousString, paramAnonymousInt2, ((BluetoothGattCharacteristic)???).getWriteType(), j, ((BluetoothGattCharacteristic)???).getValue());
              BluetoothGatt.access$1308(BluetoothGatt.this);
              return;
            }
            catch (RemoteException paramAnonymousString)
            {
              Log.e("BluetoothGatt", "", paramAnonymousString);
            }
          }
        }
        BluetoothGatt.access$1302(BluetoothGatt.this, 0);
        BluetoothGatt.this.runOrQueueCallback(new Runnable()
        {
          public void run()
          {
            BluetoothGattCallback localBluetoothGattCallback = mCallback;
            if (localBluetoothGattCallback != null) {
              localBluetoothGattCallback.onCharacteristicWrite(BluetoothGatt.this, val$characteristic, paramAnonymousInt1);
            }
          }
        });
        return;
      }
    }
    
    public void onClientConnectionState(final int paramAnonymousInt1, final int paramAnonymousInt2, boolean paramAnonymousBoolean, String arg4)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("onClientConnectionState() - status=");
      localStringBuilder.append(paramAnonymousInt1);
      localStringBuilder.append(" clientIf=");
      localStringBuilder.append(paramAnonymousInt2);
      localStringBuilder.append(" device=");
      localStringBuilder.append(???);
      Log.d("BluetoothGatt", localStringBuilder.toString());
      if (!???.equals(mDevice.getAddress())) {
        return;
      }
      if (paramAnonymousBoolean) {
        paramAnonymousInt2 = 2;
      } else {
        paramAnonymousInt2 = 0;
      }
      BluetoothGatt.this.runOrQueueCallback(new Runnable()
      {
        public void run()
        {
          BluetoothGattCallback localBluetoothGattCallback = mCallback;
          if (localBluetoothGattCallback != null) {
            localBluetoothGattCallback.onConnectionStateChange(BluetoothGatt.this, paramAnonymousInt1, paramAnonymousInt2);
          }
        }
      });
      ??? = mStateLock;
      if (paramAnonymousBoolean) {
        try
        {
          BluetoothGatt.access$402(BluetoothGatt.this, 2);
        }
        finally
        {
          break label194;
        }
      } else {
        BluetoothGatt.access$402(BluetoothGatt.this, 0);
      }
      synchronized (mDeviceBusy)
      {
        BluetoothGatt.access$1102(BluetoothGatt.this, Boolean.valueOf(false));
        return;
      }
    }
    
    public void onClientRegistered(int paramAnonymousInt1, int paramAnonymousInt2)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("onClientRegistered() - status=");
      localStringBuilder.append(paramAnonymousInt1);
      localStringBuilder.append(" clientIf=");
      localStringBuilder.append(paramAnonymousInt2);
      Log.d("BluetoothGatt", localStringBuilder.toString());
      BluetoothGatt.access$002(BluetoothGatt.this, paramAnonymousInt2);
      if (paramAnonymousInt1 != 0)
      {
        BluetoothGatt.this.runOrQueueCallback(new Runnable()
        {
          public void run()
          {
            BluetoothGattCallback localBluetoothGattCallback = mCallback;
            if (localBluetoothGattCallback != null) {
              localBluetoothGattCallback.onConnectionStateChange(BluetoothGatt.this, 257, 0);
            }
          }
        });
        synchronized (mStateLock)
        {
          BluetoothGatt.access$402(BluetoothGatt.this, 0);
          return;
        }
      }
      try
      {
        mService.clientConnect(mClientIf, mDevice.getAddress(), mAutoConnect ^ true, mTransport, mOpportunistic, mPhy);
      }
      catch (RemoteException localRemoteException)
      {
        Log.e("BluetoothGatt", "", localRemoteException);
      }
    }
    
    public void onConfigureMTU(String paramAnonymousString, final int paramAnonymousInt1, final int paramAnonymousInt2)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("onConfigureMTU() - Device=");
      localStringBuilder.append(paramAnonymousString);
      localStringBuilder.append(" mtu=");
      localStringBuilder.append(paramAnonymousInt1);
      localStringBuilder.append(" status=");
      localStringBuilder.append(paramAnonymousInt2);
      Log.d("BluetoothGatt", localStringBuilder.toString());
      if (!paramAnonymousString.equals(mDevice.getAddress())) {
        return;
      }
      BluetoothGatt.this.runOrQueueCallback(new Runnable()
      {
        public void run()
        {
          BluetoothGattCallback localBluetoothGattCallback = mCallback;
          if (localBluetoothGattCallback != null) {
            localBluetoothGattCallback.onMtuChanged(BluetoothGatt.this, paramAnonymousInt1, paramAnonymousInt2);
          }
        }
      });
    }
    
    public void onConnectionUpdated(String paramAnonymousString, final int paramAnonymousInt1, final int paramAnonymousInt2, final int paramAnonymousInt3, final int paramAnonymousInt4)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("onConnectionUpdated() - Device=");
      localStringBuilder.append(paramAnonymousString);
      localStringBuilder.append(" interval=");
      localStringBuilder.append(paramAnonymousInt1);
      localStringBuilder.append(" latency=");
      localStringBuilder.append(paramAnonymousInt2);
      localStringBuilder.append(" timeout=");
      localStringBuilder.append(paramAnonymousInt3);
      localStringBuilder.append(" status=");
      localStringBuilder.append(paramAnonymousInt4);
      Log.d("BluetoothGatt", localStringBuilder.toString());
      if (!paramAnonymousString.equals(mDevice.getAddress())) {
        return;
      }
      BluetoothGatt.this.runOrQueueCallback(new Runnable()
      {
        public void run()
        {
          BluetoothGattCallback localBluetoothGattCallback = mCallback;
          if (localBluetoothGattCallback != null) {
            localBluetoothGattCallback.onConnectionUpdated(BluetoothGatt.this, paramAnonymousInt1, paramAnonymousInt2, paramAnonymousInt3, paramAnonymousInt4);
          }
        }
      });
    }
    
    public void onDescriptorRead(String paramAnonymousString, final int paramAnonymousInt1, int paramAnonymousInt2, final byte[] paramAnonymousArrayOfByte)
    {
      if (!paramAnonymousString.equals(mDevice.getAddress())) {
        return;
      }
      synchronized (mDeviceBusy)
      {
        BluetoothGatt.access$1102(BluetoothGatt.this, Boolean.valueOf(false));
        ??? = getDescriptorById(mDevice, paramAnonymousInt2);
        if (??? == null) {
          return;
        }
        if ((paramAnonymousInt1 == 5) || (paramAnonymousInt1 == 15))
        {
          int i = mAuthRetryState;
          int j = 2;
          if (i != 2) {
            try
            {
              if (mAuthRetryState == 0) {
                j = 1;
              }
              mService.readDescriptor(mClientIf, paramAnonymousString, paramAnonymousInt2, j);
              BluetoothGatt.access$1308(BluetoothGatt.this);
              return;
            }
            catch (RemoteException paramAnonymousString)
            {
              Log.e("BluetoothGatt", "", paramAnonymousString);
            }
          }
        }
        BluetoothGatt.access$1302(BluetoothGatt.this, 0);
        BluetoothGatt.this.runOrQueueCallback(new Runnable()
        {
          public void run()
          {
            BluetoothGattCallback localBluetoothGattCallback = mCallback;
            if (localBluetoothGattCallback != null)
            {
              if (paramAnonymousInt1 == 0) {
                val$descriptor.setValue(paramAnonymousArrayOfByte);
              }
              localBluetoothGattCallback.onDescriptorRead(BluetoothGatt.this, val$descriptor, paramAnonymousInt1);
            }
          }
        });
        return;
      }
    }
    
    public void onDescriptorWrite(String paramAnonymousString, final int paramAnonymousInt1, int paramAnonymousInt2)
    {
      if (!paramAnonymousString.equals(mDevice.getAddress())) {
        return;
      }
      synchronized (mDeviceBusy)
      {
        BluetoothGatt.access$1102(BluetoothGatt.this, Boolean.valueOf(false));
        ??? = getDescriptorById(mDevice, paramAnonymousInt2);
        if (??? == null) {
          return;
        }
        if ((paramAnonymousInt1 == 5) || (paramAnonymousInt1 == 15))
        {
          int i = mAuthRetryState;
          int j = 2;
          if (i != 2) {
            try
            {
              if (mAuthRetryState == 0) {
                j = 1;
              }
              for (;;)
              {
                break;
              }
              mService.writeDescriptor(mClientIf, paramAnonymousString, paramAnonymousInt2, j, ((BluetoothGattDescriptor)???).getValue());
              BluetoothGatt.access$1308(BluetoothGatt.this);
              return;
            }
            catch (RemoteException paramAnonymousString)
            {
              Log.e("BluetoothGatt", "", paramAnonymousString);
            }
          }
        }
        BluetoothGatt.access$1302(BluetoothGatt.this, 0);
        BluetoothGatt.this.runOrQueueCallback(new Runnable()
        {
          public void run()
          {
            BluetoothGattCallback localBluetoothGattCallback = mCallback;
            if (localBluetoothGattCallback != null) {
              localBluetoothGattCallback.onDescriptorWrite(BluetoothGatt.this, val$descriptor, paramAnonymousInt1);
            }
          }
        });
        return;
      }
    }
    
    public void onExecuteWrite(String paramAnonymousString, final int paramAnonymousInt)
    {
      if (!paramAnonymousString.equals(mDevice.getAddress())) {
        return;
      }
      synchronized (mDeviceBusy)
      {
        BluetoothGatt.access$1102(BluetoothGatt.this, Boolean.valueOf(false));
        BluetoothGatt.this.runOrQueueCallback(new Runnable()
        {
          public void run()
          {
            BluetoothGattCallback localBluetoothGattCallback = mCallback;
            if (localBluetoothGattCallback != null) {
              localBluetoothGattCallback.onReliableWriteCompleted(BluetoothGatt.this, paramAnonymousInt);
            }
          }
        });
        return;
      }
    }
    
    public void onNotify(final String paramAnonymousString, int paramAnonymousInt, final byte[] paramAnonymousArrayOfByte)
    {
      if (!paramAnonymousString.equals(mDevice.getAddress())) {
        return;
      }
      paramAnonymousString = getCharacteristicById(mDevice, paramAnonymousInt);
      if (paramAnonymousString == null) {
        return;
      }
      BluetoothGatt.this.runOrQueueCallback(new Runnable()
      {
        public void run()
        {
          BluetoothGattCallback localBluetoothGattCallback = mCallback;
          if (localBluetoothGattCallback != null)
          {
            paramAnonymousString.setValue(paramAnonymousArrayOfByte);
            localBluetoothGattCallback.onCharacteristicChanged(BluetoothGatt.this, paramAnonymousString);
          }
        }
      });
    }
    
    public void onPhyRead(String paramAnonymousString, final int paramAnonymousInt1, final int paramAnonymousInt2, final int paramAnonymousInt3)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("onPhyRead() - status=");
      localStringBuilder.append(paramAnonymousInt3);
      localStringBuilder.append(" address=");
      localStringBuilder.append(paramAnonymousString);
      localStringBuilder.append(" txPhy=");
      localStringBuilder.append(paramAnonymousInt1);
      localStringBuilder.append(" rxPhy=");
      localStringBuilder.append(paramAnonymousInt2);
      Log.d("BluetoothGatt", localStringBuilder.toString());
      if (!paramAnonymousString.equals(mDevice.getAddress())) {
        return;
      }
      BluetoothGatt.this.runOrQueueCallback(new Runnable()
      {
        public void run()
        {
          BluetoothGattCallback localBluetoothGattCallback = mCallback;
          if (localBluetoothGattCallback != null) {
            localBluetoothGattCallback.onPhyRead(BluetoothGatt.this, paramAnonymousInt1, paramAnonymousInt2, paramAnonymousInt3);
          }
        }
      });
    }
    
    public void onPhyUpdate(String paramAnonymousString, final int paramAnonymousInt1, final int paramAnonymousInt2, final int paramAnonymousInt3)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("onPhyUpdate() - status=");
      localStringBuilder.append(paramAnonymousInt3);
      localStringBuilder.append(" address=");
      localStringBuilder.append(paramAnonymousString);
      localStringBuilder.append(" txPhy=");
      localStringBuilder.append(paramAnonymousInt1);
      localStringBuilder.append(" rxPhy=");
      localStringBuilder.append(paramAnonymousInt2);
      Log.d("BluetoothGatt", localStringBuilder.toString());
      if (!paramAnonymousString.equals(mDevice.getAddress())) {
        return;
      }
      BluetoothGatt.this.runOrQueueCallback(new Runnable()
      {
        public void run()
        {
          BluetoothGattCallback localBluetoothGattCallback = mCallback;
          if (localBluetoothGattCallback != null) {
            localBluetoothGattCallback.onPhyUpdate(BluetoothGatt.this, paramAnonymousInt1, paramAnonymousInt2, paramAnonymousInt3);
          }
        }
      });
    }
    
    public void onReadRemoteRssi(String paramAnonymousString, final int paramAnonymousInt1, final int paramAnonymousInt2)
    {
      if (!paramAnonymousString.equals(mDevice.getAddress())) {
        return;
      }
      BluetoothGatt.this.runOrQueueCallback(new Runnable()
      {
        public void run()
        {
          BluetoothGattCallback localBluetoothGattCallback = mCallback;
          if (localBluetoothGattCallback != null) {
            localBluetoothGattCallback.onReadRemoteRssi(BluetoothGatt.this, paramAnonymousInt1, paramAnonymousInt2);
          }
        }
      });
    }
    
    public void onSearchComplete(String paramAnonymousString, List<BluetoothGattService> paramAnonymousList, final int paramAnonymousInt)
    {
      Object localObject = new StringBuilder();
      ((StringBuilder)localObject).append("onSearchComplete() = Device=");
      ((StringBuilder)localObject).append(paramAnonymousString);
      ((StringBuilder)localObject).append(" Status=");
      ((StringBuilder)localObject).append(paramAnonymousInt);
      Log.d("BluetoothGatt", ((StringBuilder)localObject).toString());
      if (!paramAnonymousString.equals(mDevice.getAddress())) {
        return;
      }
      paramAnonymousString = paramAnonymousList.iterator();
      while (paramAnonymousString.hasNext()) {
        ((BluetoothGattService)paramAnonymousString.next()).setDevice(mDevice);
      }
      mServices.addAll(paramAnonymousList);
      paramAnonymousList = mServices.iterator();
      while (paramAnonymousList.hasNext())
      {
        paramAnonymousString = (BluetoothGattService)paramAnonymousList.next();
        localObject = new ArrayList(paramAnonymousString.getIncludedServices());
        paramAnonymousString.getIncludedServices().clear();
        localObject = ((ArrayList)localObject).iterator();
        while (((Iterator)localObject).hasNext())
        {
          BluetoothGattService localBluetoothGattService = (BluetoothGattService)((Iterator)localObject).next();
          localBluetoothGattService = getService(mDevice, localBluetoothGattService.getUuid(), localBluetoothGattService.getInstanceId());
          if (localBluetoothGattService != null) {
            paramAnonymousString.addIncludedService(localBluetoothGattService);
          } else {
            Log.e("BluetoothGatt", "Broken GATT database: can't find included service.");
          }
        }
      }
      BluetoothGatt.this.runOrQueueCallback(new Runnable()
      {
        public void run()
        {
          BluetoothGattCallback localBluetoothGattCallback = mCallback;
          if (localBluetoothGattCallback != null) {
            localBluetoothGattCallback.onServicesDiscovered(BluetoothGatt.this, paramAnonymousInt);
          }
        }
      });
    }
  };
  private volatile BluetoothGattCallback mCallback;
  private int mClientIf;
  private int mConnState;
  private BluetoothDevice mDevice;
  private Boolean mDeviceBusy = Boolean.valueOf(false);
  private Handler mHandler;
  private boolean mOpportunistic;
  private int mPhy;
  private IBluetoothGatt mService;
  private List<BluetoothGattService> mServices;
  private final Object mStateLock = new Object();
  private int mTransport;
  
  BluetoothGatt(IBluetoothGatt paramIBluetoothGatt, BluetoothDevice paramBluetoothDevice, int paramInt1, boolean paramBoolean, int paramInt2)
  {
    mService = paramIBluetoothGatt;
    mDevice = paramBluetoothDevice;
    mTransport = paramInt1;
    mPhy = paramInt2;
    mOpportunistic = paramBoolean;
    mServices = new ArrayList();
    mConnState = 0;
    mAuthRetryState = 0;
  }
  
  private boolean registerApp(BluetoothGattCallback paramBluetoothGattCallback, Handler paramHandler)
  {
    Log.d("BluetoothGatt", "registerApp()");
    if (mService == null) {
      return false;
    }
    mCallback = paramBluetoothGattCallback;
    mHandler = paramHandler;
    paramBluetoothGattCallback = UUID.randomUUID();
    paramHandler = new StringBuilder();
    paramHandler.append("registerApp() - UUID=");
    paramHandler.append(paramBluetoothGattCallback);
    Log.d("BluetoothGatt", paramHandler.toString());
    try
    {
      paramHandler = mService;
      ParcelUuid localParcelUuid = new android/os/ParcelUuid;
      localParcelUuid.<init>(paramBluetoothGattCallback);
      paramHandler.registerClient(localParcelUuid, mBluetoothGattCallback);
      return true;
    }
    catch (RemoteException paramBluetoothGattCallback)
    {
      Log.e("BluetoothGatt", "", paramBluetoothGattCallback);
    }
    return false;
  }
  
  private void runOrQueueCallback(Runnable paramRunnable)
  {
    if (mHandler == null) {
      try
      {
        paramRunnable.run();
      }
      catch (Exception paramRunnable)
      {
        for (;;)
        {
          Log.w("BluetoothGatt", "Unhandled exception in callback", paramRunnable);
        }
      }
    } else {
      mHandler.post(paramRunnable);
    }
  }
  
  private void unregisterApp()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("unregisterApp() - mClientIf=");
    localStringBuilder.append(mClientIf);
    Log.d("BluetoothGatt", localStringBuilder.toString());
    if ((mService != null) && (mClientIf != 0))
    {
      try
      {
        mCallback = null;
        mService.unregisterClient(mClientIf);
        mClientIf = 0;
      }
      catch (RemoteException localRemoteException)
      {
        Log.e("BluetoothGatt", "", localRemoteException);
      }
      return;
    }
  }
  
  public void abortReliableWrite()
  {
    if ((mService != null) && (mClientIf != 0))
    {
      try
      {
        mService.endReliableWrite(mClientIf, mDevice.getAddress(), false);
      }
      catch (RemoteException localRemoteException)
      {
        Log.e("BluetoothGatt", "", localRemoteException);
      }
      return;
    }
  }
  
  @Deprecated
  public void abortReliableWrite(BluetoothDevice paramBluetoothDevice)
  {
    abortReliableWrite();
  }
  
  public boolean beginReliableWrite()
  {
    if ((mService != null) && (mClientIf != 0)) {
      try
      {
        mService.beginReliableWrite(mClientIf, mDevice.getAddress());
        return true;
      }
      catch (RemoteException localRemoteException)
      {
        Log.e("BluetoothGatt", "", localRemoteException);
        return false;
      }
    }
    return false;
  }
  
  public void close()
  {
    Log.d("BluetoothGatt", "close()");
    unregisterApp();
    mConnState = 4;
    mAuthRetryState = 0;
  }
  
  public boolean connect()
  {
    try
    {
      mService.clientConnect(mClientIf, mDevice.getAddress(), false, mTransport, mOpportunistic, mPhy);
      return true;
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("BluetoothGatt", "", localRemoteException);
    }
    return false;
  }
  
  boolean connect(Boolean paramBoolean, BluetoothGattCallback arg2, Handler paramHandler)
  {
    ??? = new StringBuilder();
    ((StringBuilder)???).append("connect() - device: ");
    ((StringBuilder)???).append(mDevice.getAddress());
    ((StringBuilder)???).append(", auto: ");
    ((StringBuilder)???).append(paramBoolean);
    Log.d("BluetoothGatt", ((StringBuilder)???).toString());
    synchronized (mStateLock)
    {
      if (mConnState == 0)
      {
        mConnState = 1;
        mAutoConnect = paramBoolean.booleanValue();
        if (!registerApp(???, paramHandler)) {
          synchronized (mStateLock)
          {
            mConnState = 0;
            Log.e("BluetoothGatt", "Failed to register callback");
            return false;
          }
        }
        return true;
      }
      paramBoolean = new java/lang/IllegalStateException;
      paramBoolean.<init>("Not idle");
      throw paramBoolean;
    }
  }
  
  public void disconnect()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("cancelOpen() - device: ");
    localStringBuilder.append(mDevice.getAddress());
    Log.d("BluetoothGatt", localStringBuilder.toString());
    if ((mService != null) && (mClientIf != 0))
    {
      try
      {
        mService.clientDisconnect(mClientIf, mDevice.getAddress());
      }
      catch (RemoteException localRemoteException)
      {
        Log.e("BluetoothGatt", "", localRemoteException);
      }
      return;
    }
  }
  
  public boolean discoverServiceByUuid(UUID paramUUID)
  {
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("discoverServiceByUuid() - device: ");
    ((StringBuilder)localObject).append(mDevice.getAddress());
    Log.d("BluetoothGatt", ((StringBuilder)localObject).toString());
    if ((mService != null) && (mClientIf != 0))
    {
      mServices.clear();
      try
      {
        IBluetoothGatt localIBluetoothGatt = mService;
        int i = mClientIf;
        String str = mDevice.getAddress();
        localObject = new android/os/ParcelUuid;
        ((ParcelUuid)localObject).<init>(paramUUID);
        localIBluetoothGatt.discoverServiceByUuid(i, str, (ParcelUuid)localObject);
        return true;
      }
      catch (RemoteException paramUUID)
      {
        Log.e("BluetoothGatt", "", paramUUID);
        return false;
      }
    }
    return false;
  }
  
  public boolean discoverServices()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("discoverServices() - device: ");
    localStringBuilder.append(mDevice.getAddress());
    Log.d("BluetoothGatt", localStringBuilder.toString());
    if ((mService != null) && (mClientIf != 0))
    {
      mServices.clear();
      try
      {
        mService.discoverServices(mClientIf, mDevice.getAddress());
        return true;
      }
      catch (RemoteException localRemoteException)
      {
        Log.e("BluetoothGatt", "", localRemoteException);
        return false;
      }
    }
    return false;
  }
  
  public boolean executeReliableWrite()
  {
    if ((mService != null) && (mClientIf != 0)) {
      synchronized (mDeviceBusy)
      {
        if (mDeviceBusy.booleanValue()) {
          return false;
        }
        mDeviceBusy = Boolean.valueOf(true);
        try
        {
          mService.endReliableWrite(mClientIf, mDevice.getAddress(), true);
          return true;
        }
        catch (RemoteException localRemoteException)
        {
          Log.e("BluetoothGatt", "", localRemoteException);
          mDeviceBusy = Boolean.valueOf(false);
          return false;
        }
      }
    }
    return false;
  }
  
  BluetoothGattCharacteristic getCharacteristicById(BluetoothDevice paramBluetoothDevice, int paramInt)
  {
    Iterator localIterator = mServices.iterator();
    while (localIterator.hasNext())
    {
      paramBluetoothDevice = ((BluetoothGattService)localIterator.next()).getCharacteristics().iterator();
      while (paramBluetoothDevice.hasNext())
      {
        BluetoothGattCharacteristic localBluetoothGattCharacteristic = (BluetoothGattCharacteristic)paramBluetoothDevice.next();
        if (localBluetoothGattCharacteristic.getInstanceId() == paramInt) {
          return localBluetoothGattCharacteristic;
        }
      }
    }
    return null;
  }
  
  public List<BluetoothDevice> getConnectedDevices()
  {
    throw new UnsupportedOperationException("Use BluetoothManager#getConnectedDevices instead.");
  }
  
  public int getConnectionState(BluetoothDevice paramBluetoothDevice)
  {
    throw new UnsupportedOperationException("Use BluetoothManager#getConnectionState instead.");
  }
  
  BluetoothGattDescriptor getDescriptorById(BluetoothDevice paramBluetoothDevice, int paramInt)
  {
    paramBluetoothDevice = mServices.iterator();
    while (paramBluetoothDevice.hasNext())
    {
      Iterator localIterator1 = ((BluetoothGattService)paramBluetoothDevice.next()).getCharacteristics().iterator();
      while (localIterator1.hasNext())
      {
        Iterator localIterator2 = ((BluetoothGattCharacteristic)localIterator1.next()).getDescriptors().iterator();
        while (localIterator2.hasNext())
        {
          BluetoothGattDescriptor localBluetoothGattDescriptor = (BluetoothGattDescriptor)localIterator2.next();
          if (localBluetoothGattDescriptor.getInstanceId() == paramInt) {
            return localBluetoothGattDescriptor;
          }
        }
      }
    }
    return null;
  }
  
  public BluetoothDevice getDevice()
  {
    return mDevice;
  }
  
  public List<BluetoothDevice> getDevicesMatchingConnectionStates(int[] paramArrayOfInt)
  {
    throw new UnsupportedOperationException("Use BluetoothManager#getDevicesMatchingConnectionStates instead.");
  }
  
  BluetoothGattService getService(BluetoothDevice paramBluetoothDevice, UUID paramUUID, int paramInt)
  {
    Iterator localIterator = mServices.iterator();
    while (localIterator.hasNext())
    {
      BluetoothGattService localBluetoothGattService = (BluetoothGattService)localIterator.next();
      if ((localBluetoothGattService.getDevice().equals(paramBluetoothDevice)) && (localBluetoothGattService.getInstanceId() == paramInt) && (localBluetoothGattService.getUuid().equals(paramUUID))) {
        return localBluetoothGattService;
      }
    }
    return null;
  }
  
  public BluetoothGattService getService(UUID paramUUID)
  {
    Iterator localIterator = mServices.iterator();
    while (localIterator.hasNext())
    {
      BluetoothGattService localBluetoothGattService = (BluetoothGattService)localIterator.next();
      if ((localBluetoothGattService.getDevice().equals(mDevice)) && (localBluetoothGattService.getUuid().equals(paramUUID))) {
        return localBluetoothGattService;
      }
    }
    return null;
  }
  
  public List<BluetoothGattService> getServices()
  {
    ArrayList localArrayList = new ArrayList();
    Iterator localIterator = mServices.iterator();
    while (localIterator.hasNext())
    {
      BluetoothGattService localBluetoothGattService = (BluetoothGattService)localIterator.next();
      if (localBluetoothGattService.getDevice().equals(mDevice)) {
        localArrayList.add(localBluetoothGattService);
      }
    }
    return localArrayList;
  }
  
  public boolean readCharacteristic(BluetoothGattCharacteristic paramBluetoothGattCharacteristic)
  {
    if ((paramBluetoothGattCharacteristic.getProperties() & 0x2) == 0) {
      return false;
    }
    if ((mService != null) && (mClientIf != 0))
    {
      ??? = paramBluetoothGattCharacteristic.getService();
      if (??? == null) {
        return false;
      }
      BluetoothDevice localBluetoothDevice = ((BluetoothGattService)???).getDevice();
      if (localBluetoothDevice == null) {
        return false;
      }
      synchronized (mDeviceBusy)
      {
        if (mDeviceBusy.booleanValue()) {
          return false;
        }
        mDeviceBusy = Boolean.valueOf(true);
        try
        {
          mService.readCharacteristic(mClientIf, localBluetoothDevice.getAddress(), paramBluetoothGattCharacteristic.getInstanceId(), 0);
          return true;
        }
        catch (RemoteException paramBluetoothGattCharacteristic)
        {
          Log.e("BluetoothGatt", "", paramBluetoothGattCharacteristic);
          mDeviceBusy = Boolean.valueOf(false);
          return false;
        }
      }
    }
    return false;
  }
  
  public boolean readDescriptor(BluetoothGattDescriptor paramBluetoothGattDescriptor)
  {
    if ((mService != null) && (mClientIf != 0))
    {
      ??? = paramBluetoothGattDescriptor.getCharacteristic();
      if (??? == null) {
        return false;
      }
      ??? = ((BluetoothGattCharacteristic)???).getService();
      if (??? == null) {
        return false;
      }
      BluetoothDevice localBluetoothDevice = ((BluetoothGattService)???).getDevice();
      if (localBluetoothDevice == null) {
        return false;
      }
      synchronized (mDeviceBusy)
      {
        if (mDeviceBusy.booleanValue()) {
          return false;
        }
        mDeviceBusy = Boolean.valueOf(true);
        try
        {
          mService.readDescriptor(mClientIf, localBluetoothDevice.getAddress(), paramBluetoothGattDescriptor.getInstanceId(), 0);
          return true;
        }
        catch (RemoteException paramBluetoothGattDescriptor)
        {
          Log.e("BluetoothGatt", "", paramBluetoothGattDescriptor);
          mDeviceBusy = Boolean.valueOf(false);
          return false;
        }
      }
    }
    return false;
  }
  
  public void readPhy()
  {
    try
    {
      mService.clientReadPhy(mClientIf, mDevice.getAddress());
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("BluetoothGatt", "", localRemoteException);
    }
  }
  
  public boolean readRemoteRssi()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("readRssi() - device: ");
    localStringBuilder.append(mDevice.getAddress());
    Log.d("BluetoothGatt", localStringBuilder.toString());
    if ((mService != null) && (mClientIf != 0)) {
      try
      {
        mService.readRemoteRssi(mClientIf, mDevice.getAddress());
        return true;
      }
      catch (RemoteException localRemoteException)
      {
        Log.e("BluetoothGatt", "", localRemoteException);
        return false;
      }
    }
    return false;
  }
  
  public boolean readUsingCharacteristicUuid(UUID paramUUID, int paramInt1, int paramInt2)
  {
    if ((mService != null) && (mClientIf != 0)) {
      synchronized (mDeviceBusy)
      {
        if (mDeviceBusy.booleanValue()) {
          return false;
        }
        mDeviceBusy = Boolean.valueOf(true);
        try
        {
          IBluetoothGatt localIBluetoothGatt = mService;
          int i = mClientIf;
          String str = mDevice.getAddress();
          ??? = new android/os/ParcelUuid;
          ((ParcelUuid)???).<init>(paramUUID);
          localIBluetoothGatt.readUsingCharacteristicUuid(i, str, (ParcelUuid)???, paramInt1, paramInt2, 0);
          return true;
        }
        catch (RemoteException paramUUID)
        {
          Log.e("BluetoothGatt", "", paramUUID);
          mDeviceBusy = Boolean.valueOf(false);
          return false;
        }
      }
    }
    return false;
  }
  
  public boolean refresh()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("refresh() - device: ");
    localStringBuilder.append(mDevice.getAddress());
    Log.d("BluetoothGatt", localStringBuilder.toString());
    if ((mService != null) && (mClientIf != 0)) {
      try
      {
        mService.refreshDevice(mClientIf, mDevice.getAddress());
        return true;
      }
      catch (RemoteException localRemoteException)
      {
        Log.e("BluetoothGatt", "", localRemoteException);
        return false;
      }
    }
    return false;
  }
  
  public boolean requestConnectionPriority(int paramInt)
  {
    if ((paramInt >= 0) && (paramInt <= 2))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("requestConnectionPriority() - params: ");
      localStringBuilder.append(paramInt);
      Log.d("BluetoothGatt", localStringBuilder.toString());
      if ((mService != null) && (mClientIf != 0)) {
        try
        {
          mService.connectionParameterUpdate(mClientIf, mDevice.getAddress(), paramInt);
          return true;
        }
        catch (RemoteException localRemoteException)
        {
          Log.e("BluetoothGatt", "", localRemoteException);
          return false;
        }
      }
      return false;
    }
    throw new IllegalArgumentException("connectionPriority not within valid range");
  }
  
  public boolean requestLeConnectionUpdate(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("requestLeConnectionUpdate() - min=(");
    localStringBuilder.append(paramInt1);
    localStringBuilder.append(")");
    localStringBuilder.append(paramInt1 * 1.25D);
    localStringBuilder.append("msec, max=(");
    localStringBuilder.append(paramInt2);
    localStringBuilder.append(")");
    localStringBuilder.append(1.25D * paramInt2);
    localStringBuilder.append("msec, latency=");
    localStringBuilder.append(paramInt3);
    localStringBuilder.append(", timeout=");
    localStringBuilder.append(paramInt4);
    localStringBuilder.append("msec, min_ce=");
    localStringBuilder.append(paramInt5);
    localStringBuilder.append(", max_ce=");
    localStringBuilder.append(paramInt6);
    Log.d("BluetoothGatt", localStringBuilder.toString());
    if ((mService != null) && (mClientIf != 0)) {
      try
      {
        mService.leConnectionUpdate(mClientIf, mDevice.getAddress(), paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6);
        return true;
      }
      catch (RemoteException localRemoteException)
      {
        Log.e("BluetoothGatt", "", localRemoteException);
        return false;
      }
    }
    return false;
  }
  
  public boolean requestMtu(int paramInt)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("configureMTU() - device: ");
    localStringBuilder.append(mDevice.getAddress());
    localStringBuilder.append(" mtu: ");
    localStringBuilder.append(paramInt);
    Log.d("BluetoothGatt", localStringBuilder.toString());
    if ((mService != null) && (mClientIf != 0)) {
      try
      {
        mService.configureMTU(mClientIf, mDevice.getAddress(), paramInt);
        return true;
      }
      catch (RemoteException localRemoteException)
      {
        Log.e("BluetoothGatt", "", localRemoteException);
        return false;
      }
    }
    return false;
  }
  
  public boolean setCharacteristicNotification(BluetoothGattCharacteristic paramBluetoothGattCharacteristic, boolean paramBoolean)
  {
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("setCharacteristicNotification() - uuid: ");
    ((StringBuilder)localObject).append(paramBluetoothGattCharacteristic.getUuid());
    ((StringBuilder)localObject).append(" enable: ");
    ((StringBuilder)localObject).append(paramBoolean);
    Log.d("BluetoothGatt", ((StringBuilder)localObject).toString());
    if ((mService != null) && (mClientIf != 0))
    {
      localObject = paramBluetoothGattCharacteristic.getService();
      if (localObject == null) {
        return false;
      }
      localObject = ((BluetoothGattService)localObject).getDevice();
      if (localObject == null) {
        return false;
      }
      try
      {
        mService.registerForNotification(mClientIf, ((BluetoothDevice)localObject).getAddress(), paramBluetoothGattCharacteristic.getInstanceId(), paramBoolean);
        return true;
      }
      catch (RemoteException paramBluetoothGattCharacteristic)
      {
        Log.e("BluetoothGatt", "", paramBluetoothGattCharacteristic);
        return false;
      }
    }
    return false;
  }
  
  public void setPreferredPhy(int paramInt1, int paramInt2, int paramInt3)
  {
    try
    {
      mService.clientSetPreferredPhy(mClientIf, mDevice.getAddress(), paramInt1, paramInt2, paramInt3);
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("BluetoothGatt", "", localRemoteException);
    }
  }
  
  public boolean writeCharacteristic(BluetoothGattCharacteristic paramBluetoothGattCharacteristic)
  {
    if (((paramBluetoothGattCharacteristic.getProperties() & 0x8) == 0) && ((paramBluetoothGattCharacteristic.getProperties() & 0x4) == 0)) {
      return false;
    }
    if ((mService != null) && (mClientIf != 0) && (paramBluetoothGattCharacteristic.getValue() != null))
    {
      ??? = paramBluetoothGattCharacteristic.getService();
      if (??? == null) {
        return false;
      }
      BluetoothDevice localBluetoothDevice = ((BluetoothGattService)???).getDevice();
      if (localBluetoothDevice == null) {
        return false;
      }
      synchronized (mDeviceBusy)
      {
        if (mDeviceBusy.booleanValue()) {
          return false;
        }
        mDeviceBusy = Boolean.valueOf(true);
        try
        {
          mService.writeCharacteristic(mClientIf, localBluetoothDevice.getAddress(), paramBluetoothGattCharacteristic.getInstanceId(), paramBluetoothGattCharacteristic.getWriteType(), 0, paramBluetoothGattCharacteristic.getValue());
          return true;
        }
        catch (RemoteException paramBluetoothGattCharacteristic)
        {
          Log.e("BluetoothGatt", "", paramBluetoothGattCharacteristic);
          mDeviceBusy = Boolean.valueOf(false);
          return false;
        }
      }
    }
    return false;
  }
  
  public boolean writeDescriptor(BluetoothGattDescriptor paramBluetoothGattDescriptor)
  {
    if ((mService != null) && (mClientIf != 0) && (paramBluetoothGattDescriptor.getValue() != null))
    {
      ??? = paramBluetoothGattDescriptor.getCharacteristic();
      if (??? == null) {
        return false;
      }
      ??? = ((BluetoothGattCharacteristic)???).getService();
      if (??? == null) {
        return false;
      }
      BluetoothDevice localBluetoothDevice = ((BluetoothGattService)???).getDevice();
      if (localBluetoothDevice == null) {
        return false;
      }
      synchronized (mDeviceBusy)
      {
        if (mDeviceBusy.booleanValue()) {
          return false;
        }
        mDeviceBusy = Boolean.valueOf(true);
        try
        {
          mService.writeDescriptor(mClientIf, localBluetoothDevice.getAddress(), paramBluetoothGattDescriptor.getInstanceId(), 0, paramBluetoothGattDescriptor.getValue());
          return true;
        }
        catch (RemoteException paramBluetoothGattDescriptor)
        {
          Log.e("BluetoothGatt", "", paramBluetoothGattDescriptor);
          mDeviceBusy = Boolean.valueOf(false);
          return false;
        }
      }
    }
    return false;
  }
}
