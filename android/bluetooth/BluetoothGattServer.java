package android.bluetooth;

import android.os.ParcelUuid;
import android.os.RemoteException;
import android.util.Log;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public final class BluetoothGattServer
  implements BluetoothProfile
{
  private static final int CALLBACK_REG_TIMEOUT = 10000;
  private static final boolean DBG = true;
  private static final String TAG = "BluetoothGattServer";
  private static final boolean VDBG = false;
  private BluetoothAdapter mAdapter;
  private final IBluetoothGattServerCallback mBluetoothGattServerCallback = new IBluetoothGattServerCallback.Stub()
  {
    public void onCharacteristicReadRequest(String paramAnonymousString, int paramAnonymousInt1, int paramAnonymousInt2, boolean paramAnonymousBoolean, int paramAnonymousInt3)
    {
      BluetoothDevice localBluetoothDevice = mAdapter.getRemoteDevice(paramAnonymousString);
      paramAnonymousString = getCharacteristicByHandle(paramAnonymousInt3);
      if (paramAnonymousString == null)
      {
        paramAnonymousString = new StringBuilder();
        paramAnonymousString.append("onCharacteristicReadRequest() no char for handle ");
        paramAnonymousString.append(paramAnonymousInt3);
        Log.w("BluetoothGattServer", paramAnonymousString.toString());
        return;
      }
      try
      {
        mCallback.onCharacteristicReadRequest(localBluetoothDevice, paramAnonymousInt1, paramAnonymousInt2, paramAnonymousString);
      }
      catch (Exception paramAnonymousString)
      {
        Log.w("BluetoothGattServer", "Unhandled exception in callback", paramAnonymousString);
      }
    }
    
    public void onCharacteristicWriteRequest(String paramAnonymousString, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3, boolean paramAnonymousBoolean1, boolean paramAnonymousBoolean2, int paramAnonymousInt4, byte[] paramAnonymousArrayOfByte)
    {
      paramAnonymousString = mAdapter.getRemoteDevice(paramAnonymousString);
      BluetoothGattCharacteristic localBluetoothGattCharacteristic = getCharacteristicByHandle(paramAnonymousInt4);
      if (localBluetoothGattCharacteristic == null)
      {
        paramAnonymousString = new StringBuilder();
        paramAnonymousString.append("onCharacteristicWriteRequest() no char for handle ");
        paramAnonymousString.append(paramAnonymousInt4);
        Log.w("BluetoothGattServer", paramAnonymousString.toString());
        return;
      }
      try
      {
        mCallback.onCharacteristicWriteRequest(paramAnonymousString, paramAnonymousInt1, localBluetoothGattCharacteristic, paramAnonymousBoolean1, paramAnonymousBoolean2, paramAnonymousInt2, paramAnonymousArrayOfByte);
      }
      catch (Exception paramAnonymousString)
      {
        Log.w("BluetoothGattServer", "Unhandled exception in callback", paramAnonymousString);
      }
    }
    
    public void onConnectionUpdated(String paramAnonymousString, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3, int paramAnonymousInt4)
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
      Log.d("BluetoothGattServer", localStringBuilder.toString());
      paramAnonymousString = mAdapter.getRemoteDevice(paramAnonymousString);
      if (paramAnonymousString == null) {
        return;
      }
      try
      {
        mCallback.onConnectionUpdated(paramAnonymousString, paramAnonymousInt1, paramAnonymousInt2, paramAnonymousInt3, paramAnonymousInt4);
      }
      catch (Exception localException)
      {
        paramAnonymousString = new StringBuilder();
        paramAnonymousString.append("Unhandled exception: ");
        paramAnonymousString.append(localException);
        Log.w("BluetoothGattServer", paramAnonymousString.toString());
      }
    }
    
    public void onDescriptorReadRequest(String paramAnonymousString, int paramAnonymousInt1, int paramAnonymousInt2, boolean paramAnonymousBoolean, int paramAnonymousInt3)
    {
      BluetoothDevice localBluetoothDevice = mAdapter.getRemoteDevice(paramAnonymousString);
      paramAnonymousString = getDescriptorByHandle(paramAnonymousInt3);
      if (paramAnonymousString == null)
      {
        paramAnonymousString = new StringBuilder();
        paramAnonymousString.append("onDescriptorReadRequest() no desc for handle ");
        paramAnonymousString.append(paramAnonymousInt3);
        Log.w("BluetoothGattServer", paramAnonymousString.toString());
        return;
      }
      try
      {
        mCallback.onDescriptorReadRequest(localBluetoothDevice, paramAnonymousInt1, paramAnonymousInt2, paramAnonymousString);
      }
      catch (Exception paramAnonymousString)
      {
        Log.w("BluetoothGattServer", "Unhandled exception in callback", paramAnonymousString);
      }
    }
    
    public void onDescriptorWriteRequest(String paramAnonymousString, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3, boolean paramAnonymousBoolean1, boolean paramAnonymousBoolean2, int paramAnonymousInt4, byte[] paramAnonymousArrayOfByte)
    {
      paramAnonymousString = mAdapter.getRemoteDevice(paramAnonymousString);
      BluetoothGattDescriptor localBluetoothGattDescriptor = getDescriptorByHandle(paramAnonymousInt4);
      if (localBluetoothGattDescriptor == null)
      {
        paramAnonymousString = new StringBuilder();
        paramAnonymousString.append("onDescriptorWriteRequest() no desc for handle ");
        paramAnonymousString.append(paramAnonymousInt4);
        Log.w("BluetoothGattServer", paramAnonymousString.toString());
        return;
      }
      try
      {
        mCallback.onDescriptorWriteRequest(paramAnonymousString, paramAnonymousInt1, localBluetoothGattDescriptor, paramAnonymousBoolean1, paramAnonymousBoolean2, paramAnonymousInt2, paramAnonymousArrayOfByte);
      }
      catch (Exception paramAnonymousString)
      {
        Log.w("BluetoothGattServer", "Unhandled exception in callback", paramAnonymousString);
      }
    }
    
    public void onExecuteWrite(String paramAnonymousString, int paramAnonymousInt, boolean paramAnonymousBoolean)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("onExecuteWrite() - device=");
      localStringBuilder.append(paramAnonymousString);
      localStringBuilder.append(", transId=");
      localStringBuilder.append(paramAnonymousInt);
      localStringBuilder.append("execWrite=");
      localStringBuilder.append(paramAnonymousBoolean);
      Log.d("BluetoothGattServer", localStringBuilder.toString());
      paramAnonymousString = mAdapter.getRemoteDevice(paramAnonymousString);
      if (paramAnonymousString == null) {
        return;
      }
      try
      {
        mCallback.onExecuteWrite(paramAnonymousString, paramAnonymousInt, paramAnonymousBoolean);
      }
      catch (Exception paramAnonymousString)
      {
        Log.w("BluetoothGattServer", "Unhandled exception in callback", paramAnonymousString);
      }
    }
    
    public void onMtuChanged(String paramAnonymousString, int paramAnonymousInt)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("onMtuChanged() - device=");
      localStringBuilder.append(paramAnonymousString);
      localStringBuilder.append(", mtu=");
      localStringBuilder.append(paramAnonymousInt);
      Log.d("BluetoothGattServer", localStringBuilder.toString());
      paramAnonymousString = mAdapter.getRemoteDevice(paramAnonymousString);
      if (paramAnonymousString == null) {
        return;
      }
      try
      {
        mCallback.onMtuChanged(paramAnonymousString, paramAnonymousInt);
      }
      catch (Exception paramAnonymousString)
      {
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("Unhandled exception: ");
        localStringBuilder.append(paramAnonymousString);
        Log.w("BluetoothGattServer", localStringBuilder.toString());
      }
    }
    
    public void onNotificationSent(String paramAnonymousString, int paramAnonymousInt)
    {
      paramAnonymousString = mAdapter.getRemoteDevice(paramAnonymousString);
      if (paramAnonymousString == null) {
        return;
      }
      try
      {
        mCallback.onNotificationSent(paramAnonymousString, paramAnonymousInt);
      }
      catch (Exception localException)
      {
        paramAnonymousString = new StringBuilder();
        paramAnonymousString.append("Unhandled exception: ");
        paramAnonymousString.append(localException);
        Log.w("BluetoothGattServer", paramAnonymousString.toString());
      }
    }
    
    public void onPhyRead(String paramAnonymousString, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("onPhyUpdate() - device=");
      localStringBuilder.append(paramAnonymousString);
      localStringBuilder.append(", txPHy=");
      localStringBuilder.append(paramAnonymousInt1);
      localStringBuilder.append(", rxPHy=");
      localStringBuilder.append(paramAnonymousInt2);
      Log.d("BluetoothGattServer", localStringBuilder.toString());
      paramAnonymousString = mAdapter.getRemoteDevice(paramAnonymousString);
      if (paramAnonymousString == null) {
        return;
      }
      try
      {
        mCallback.onPhyRead(paramAnonymousString, paramAnonymousInt1, paramAnonymousInt2, paramAnonymousInt3);
      }
      catch (Exception paramAnonymousString)
      {
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("Unhandled exception: ");
        localStringBuilder.append(paramAnonymousString);
        Log.w("BluetoothGattServer", localStringBuilder.toString());
      }
    }
    
    public void onPhyUpdate(String paramAnonymousString, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("onPhyUpdate() - device=");
      localStringBuilder.append(paramAnonymousString);
      localStringBuilder.append(", txPHy=");
      localStringBuilder.append(paramAnonymousInt1);
      localStringBuilder.append(", rxPHy=");
      localStringBuilder.append(paramAnonymousInt2);
      Log.d("BluetoothGattServer", localStringBuilder.toString());
      paramAnonymousString = mAdapter.getRemoteDevice(paramAnonymousString);
      if (paramAnonymousString == null) {
        return;
      }
      try
      {
        mCallback.onPhyUpdate(paramAnonymousString, paramAnonymousInt1, paramAnonymousInt2, paramAnonymousInt3);
      }
      catch (Exception localException)
      {
        paramAnonymousString = new StringBuilder();
        paramAnonymousString.append("Unhandled exception: ");
        paramAnonymousString.append(localException);
        Log.w("BluetoothGattServer", paramAnonymousString.toString());
      }
    }
    
    public void onServerConnectionState(int paramAnonymousInt1, int paramAnonymousInt2, boolean paramAnonymousBoolean, String paramAnonymousString)
    {
      Object localObject = new StringBuilder();
      ((StringBuilder)localObject).append("onServerConnectionState() - status=");
      ((StringBuilder)localObject).append(paramAnonymousInt1);
      ((StringBuilder)localObject).append(" serverIf=");
      ((StringBuilder)localObject).append(paramAnonymousInt2);
      ((StringBuilder)localObject).append(" device=");
      ((StringBuilder)localObject).append(paramAnonymousString);
      Log.d("BluetoothGattServer", ((StringBuilder)localObject).toString());
      try
      {
        localObject = mCallback;
        paramAnonymousString = mAdapter.getRemoteDevice(paramAnonymousString);
        if (paramAnonymousBoolean) {
          paramAnonymousInt2 = 2;
        } else {
          paramAnonymousInt2 = 0;
        }
        ((BluetoothGattServerCallback)localObject).onConnectionStateChange(paramAnonymousString, paramAnonymousInt1, paramAnonymousInt2);
      }
      catch (Exception paramAnonymousString)
      {
        Log.w("BluetoothGattServer", "Unhandled exception in callback", paramAnonymousString);
      }
    }
    
    public void onServerRegistered(int paramAnonymousInt1, int paramAnonymousInt2)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("onServerRegistered() - status=");
      localStringBuilder.append(paramAnonymousInt1);
      localStringBuilder.append(" serverIf=");
      localStringBuilder.append(paramAnonymousInt2);
      Log.d("BluetoothGattServer", localStringBuilder.toString());
      synchronized (mServerIfLock)
      {
        if (mCallback != null)
        {
          BluetoothGattServer.access$202(BluetoothGattServer.this, paramAnonymousInt2);
          mServerIfLock.notify();
        }
        else
        {
          Log.e("BluetoothGattServer", "onServerRegistered: mCallback is null");
        }
        return;
      }
    }
    
    public void onServiceAdded(int paramAnonymousInt, BluetoothGattService paramAnonymousBluetoothGattService)
    {
      Object localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append("onServiceAdded() - handle=");
      ((StringBuilder)localObject1).append(paramAnonymousBluetoothGattService.getInstanceId());
      ((StringBuilder)localObject1).append(" uuid=");
      ((StringBuilder)localObject1).append(paramAnonymousBluetoothGattService.getUuid());
      ((StringBuilder)localObject1).append(" status=");
      ((StringBuilder)localObject1).append(paramAnonymousInt);
      Log.d("BluetoothGattServer", ((StringBuilder)localObject1).toString());
      if (mPendingService == null) {
        return;
      }
      localObject1 = mPendingService;
      BluetoothGattServer.access$402(BluetoothGattServer.this, null);
      ((BluetoothGattService)localObject1).setInstanceId(paramAnonymousBluetoothGattService.getInstanceId());
      List localList = ((BluetoothGattService)localObject1).getCharacteristics();
      paramAnonymousBluetoothGattService = paramAnonymousBluetoothGattService.getCharacteristics();
      for (int i = 0; i < paramAnonymousBluetoothGattService.size(); i++)
      {
        Object localObject2 = (BluetoothGattCharacteristic)localList.get(i);
        Object localObject3 = (BluetoothGattCharacteristic)paramAnonymousBluetoothGattService.get(i);
        ((BluetoothGattCharacteristic)localObject2).setInstanceId(((BluetoothGattCharacteristic)localObject3).getInstanceId());
        localObject2 = ((BluetoothGattCharacteristic)localObject2).getDescriptors();
        localObject3 = ((BluetoothGattCharacteristic)localObject3).getDescriptors();
        for (int j = 0; j < ((List)localObject3).size(); j++) {
          ((BluetoothGattDescriptor)((List)localObject2).get(j)).setInstanceId(((BluetoothGattDescriptor)((List)localObject3).get(j)).getInstanceId());
        }
      }
      mServices.add(localObject1);
      try
      {
        mCallback.onServiceAdded(paramAnonymousInt, (BluetoothGattService)localObject1);
      }
      catch (Exception paramAnonymousBluetoothGattService)
      {
        Log.w("BluetoothGattServer", "Unhandled exception in callback", paramAnonymousBluetoothGattService);
      }
    }
  };
  private BluetoothGattServerCallback mCallback;
  private BluetoothGattService mPendingService;
  private int mServerIf;
  private Object mServerIfLock = new Object();
  private IBluetoothGatt mService;
  private List<BluetoothGattService> mServices;
  private int mTransport;
  
  BluetoothGattServer(IBluetoothGatt paramIBluetoothGatt, int paramInt)
  {
    mService = paramIBluetoothGatt;
    mAdapter = BluetoothAdapter.getDefaultAdapter();
    mCallback = null;
    mServerIf = 0;
    mTransport = paramInt;
    mServices = new ArrayList();
  }
  
  private void unregisterCallback()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("unregisterCallback() - mServerIf=");
    localStringBuilder.append(mServerIf);
    Log.d("BluetoothGattServer", localStringBuilder.toString());
    if ((mService != null) && (mServerIf != 0))
    {
      try
      {
        mCallback = null;
        mService.unregisterServer(mServerIf);
        mServerIf = 0;
      }
      catch (RemoteException localRemoteException)
      {
        Log.e("BluetoothGattServer", "", localRemoteException);
      }
      return;
    }
  }
  
  public boolean addService(BluetoothGattService paramBluetoothGattService)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("addService() - service: ");
    localStringBuilder.append(paramBluetoothGattService.getUuid());
    Log.d("BluetoothGattServer", localStringBuilder.toString());
    if ((mService != null) && (mServerIf != 0))
    {
      mPendingService = paramBluetoothGattService;
      try
      {
        mService.addService(mServerIf, paramBluetoothGattService);
        return true;
      }
      catch (RemoteException paramBluetoothGattService)
      {
        Log.e("BluetoothGattServer", "", paramBluetoothGattService);
        return false;
      }
    }
    return false;
  }
  
  public void cancelConnection(BluetoothDevice paramBluetoothDevice)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("cancelConnection() - device: ");
    localStringBuilder.append(paramBluetoothDevice.getAddress());
    Log.d("BluetoothGattServer", localStringBuilder.toString());
    if ((mService != null) && (mServerIf != 0))
    {
      try
      {
        mService.serverDisconnect(mServerIf, paramBluetoothDevice.getAddress());
      }
      catch (RemoteException paramBluetoothDevice)
      {
        Log.e("BluetoothGattServer", "", paramBluetoothDevice);
      }
      return;
    }
  }
  
  public void clearServices()
  {
    Log.d("BluetoothGattServer", "clearServices()");
    if ((mService != null) && (mServerIf != 0))
    {
      try
      {
        mService.clearServices(mServerIf);
        mServices.clear();
      }
      catch (RemoteException localRemoteException)
      {
        Log.e("BluetoothGattServer", "", localRemoteException);
      }
      return;
    }
  }
  
  public void close()
  {
    Log.d("BluetoothGattServer", "close()");
    unregisterCallback();
  }
  
  public boolean connect(BluetoothDevice paramBluetoothDevice, boolean paramBoolean)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("connect() - device: ");
    localStringBuilder.append(paramBluetoothDevice.getAddress());
    localStringBuilder.append(", auto: ");
    localStringBuilder.append(paramBoolean);
    Log.d("BluetoothGattServer", localStringBuilder.toString());
    if ((mService != null) && (mServerIf != 0)) {
      try
      {
        mService.serverConnect(mServerIf, paramBluetoothDevice.getAddress(), paramBoolean ^ true, mTransport);
        return true;
      }
      catch (RemoteException paramBluetoothDevice)
      {
        Log.e("BluetoothGattServer", "", paramBluetoothDevice);
        return false;
      }
    }
    return false;
  }
  
  BluetoothGattCharacteristic getCharacteristicByHandle(int paramInt)
  {
    Iterator localIterator1 = mServices.iterator();
    while (localIterator1.hasNext())
    {
      Iterator localIterator2 = ((BluetoothGattService)localIterator1.next()).getCharacteristics().iterator();
      while (localIterator2.hasNext())
      {
        BluetoothGattCharacteristic localBluetoothGattCharacteristic = (BluetoothGattCharacteristic)localIterator2.next();
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
  
  BluetoothGattDescriptor getDescriptorByHandle(int paramInt)
  {
    Iterator localIterator1 = mServices.iterator();
    while (localIterator1.hasNext())
    {
      Iterator localIterator2 = ((BluetoothGattService)localIterator1.next()).getCharacteristics().iterator();
      while (localIterator2.hasNext())
      {
        Iterator localIterator3 = ((BluetoothGattCharacteristic)localIterator2.next()).getDescriptors().iterator();
        while (localIterator3.hasNext())
        {
          BluetoothGattDescriptor localBluetoothGattDescriptor = (BluetoothGattDescriptor)localIterator3.next();
          if (localBluetoothGattDescriptor.getInstanceId() == paramInt) {
            return localBluetoothGattDescriptor;
          }
        }
      }
    }
    return null;
  }
  
  public List<BluetoothDevice> getDevicesMatchingConnectionStates(int[] paramArrayOfInt)
  {
    throw new UnsupportedOperationException("Use BluetoothManager#getDevicesMatchingConnectionStates instead.");
  }
  
  public BluetoothGattService getService(UUID paramUUID)
  {
    Iterator localIterator = mServices.iterator();
    while (localIterator.hasNext())
    {
      BluetoothGattService localBluetoothGattService = (BluetoothGattService)localIterator.next();
      if (localBluetoothGattService.getUuid().equals(paramUUID)) {
        return localBluetoothGattService;
      }
    }
    return null;
  }
  
  BluetoothGattService getService(UUID paramUUID, int paramInt1, int paramInt2)
  {
    Iterator localIterator = mServices.iterator();
    while (localIterator.hasNext())
    {
      BluetoothGattService localBluetoothGattService = (BluetoothGattService)localIterator.next();
      if ((localBluetoothGattService.getType() == paramInt2) && (localBluetoothGattService.getInstanceId() == paramInt1) && (localBluetoothGattService.getUuid().equals(paramUUID))) {
        return localBluetoothGattService;
      }
    }
    return null;
  }
  
  public List<BluetoothGattService> getServices()
  {
    return mServices;
  }
  
  public boolean notifyCharacteristicChanged(BluetoothDevice paramBluetoothDevice, BluetoothGattCharacteristic paramBluetoothGattCharacteristic, boolean paramBoolean)
  {
    if ((mService != null) && (mServerIf != 0))
    {
      if (paramBluetoothGattCharacteristic.getService() == null) {
        return false;
      }
      if (paramBluetoothGattCharacteristic.getValue() != null) {
        try
        {
          mService.sendNotification(mServerIf, paramBluetoothDevice.getAddress(), paramBluetoothGattCharacteristic.getInstanceId(), paramBoolean, paramBluetoothGattCharacteristic.getValue());
          return true;
        }
        catch (RemoteException paramBluetoothDevice)
        {
          Log.e("BluetoothGattServer", "", paramBluetoothDevice);
          return false;
        }
      }
      throw new IllegalArgumentException("Chracteristic value is empty. Use BluetoothGattCharacteristic#setvalue to update");
    }
    return false;
  }
  
  public void readPhy(BluetoothDevice paramBluetoothDevice)
  {
    try
    {
      mService.serverReadPhy(mServerIf, paramBluetoothDevice.getAddress());
    }
    catch (RemoteException paramBluetoothDevice)
    {
      Log.e("BluetoothGattServer", "", paramBluetoothDevice);
    }
  }
  
  boolean registerCallback(BluetoothGattServerCallback paramBluetoothGattServerCallback)
  {
    Log.d("BluetoothGattServer", "registerCallback()");
    if (mService == null)
    {
      Log.e("BluetoothGattServer", "GATT service not available");
      return false;
    }
    Object localObject1 = UUID.randomUUID();
    ??? = new StringBuilder();
    ((StringBuilder)???).append("registerCallback() - UUID=");
    ((StringBuilder)???).append(localObject1);
    Log.d("BluetoothGattServer", ((StringBuilder)???).toString());
    synchronized (mServerIfLock)
    {
      if (mCallback != null)
      {
        Log.e("BluetoothGattServer", "App can register callback only once");
        return false;
      }
      mCallback = paramBluetoothGattServerCallback;
      try
      {
        paramBluetoothGattServerCallback = mService;
        ParcelUuid localParcelUuid = new android/os/ParcelUuid;
        localParcelUuid.<init>((UUID)localObject1);
        paramBluetoothGattServerCallback.registerServer(localParcelUuid, mBluetoothGattServerCallback);
        try
        {
          mServerIfLock.wait(10000L);
        }
        catch (InterruptedException paramBluetoothGattServerCallback)
        {
          localObject1 = new java/lang/StringBuilder;
          ((StringBuilder)localObject1).<init>();
          ((StringBuilder)localObject1).append("");
          ((StringBuilder)localObject1).append(paramBluetoothGattServerCallback);
          Log.e("BluetoothGattServer", ((StringBuilder)localObject1).toString());
          mCallback = null;
        }
        if (mServerIf == 0)
        {
          mCallback = null;
          return false;
        }
        return true;
      }
      catch (RemoteException paramBluetoothGattServerCallback)
      {
        Log.e("BluetoothGattServer", "", paramBluetoothGattServerCallback);
        mCallback = null;
        return false;
      }
    }
  }
  
  public boolean removeService(BluetoothGattService paramBluetoothGattService)
  {
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("removeService() - service: ");
    ((StringBuilder)localObject).append(paramBluetoothGattService.getUuid());
    Log.d("BluetoothGattServer", ((StringBuilder)localObject).toString());
    if ((mService != null) && (mServerIf != 0))
    {
      localObject = getService(paramBluetoothGattService.getUuid(), paramBluetoothGattService.getInstanceId(), paramBluetoothGattService.getType());
      if (localObject == null) {
        return false;
      }
      try
      {
        mService.removeService(mServerIf, paramBluetoothGattService.getInstanceId());
        mServices.remove(localObject);
        return true;
      }
      catch (RemoteException paramBluetoothGattService)
      {
        Log.e("BluetoothGattServer", "", paramBluetoothGattService);
        return false;
      }
    }
    return false;
  }
  
  public boolean sendResponse(BluetoothDevice paramBluetoothDevice, int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte)
  {
    if ((mService != null) && (mServerIf != 0)) {
      try
      {
        mService.sendResponse(mServerIf, paramBluetoothDevice.getAddress(), paramInt1, paramInt2, paramInt3, paramArrayOfByte);
        return true;
      }
      catch (RemoteException paramBluetoothDevice)
      {
        Log.e("BluetoothGattServer", "", paramBluetoothDevice);
        return false;
      }
    }
    return false;
  }
  
  public void setPreferredPhy(BluetoothDevice paramBluetoothDevice, int paramInt1, int paramInt2, int paramInt3)
  {
    try
    {
      mService.serverSetPreferredPhy(mServerIf, paramBluetoothDevice.getAddress(), paramInt1, paramInt2, paramInt3);
    }
    catch (RemoteException paramBluetoothDevice)
    {
      Log.e("BluetoothGattServer", "", paramBluetoothDevice);
    }
  }
}
