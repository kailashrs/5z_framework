package android.bluetooth;

import android.content.Context;
import android.os.RemoteException;
import android.util.Log;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class BluetoothManager
{
  private static final boolean DBG = true;
  private static final String TAG = "BluetoothManager";
  private static final boolean VDBG = true;
  private final BluetoothAdapter mAdapter;
  
  public BluetoothManager(Context paramContext)
  {
    if (paramContext.getApplicationContext() != null)
    {
      mAdapter = BluetoothAdapter.getDefaultAdapter();
      return;
    }
    throw new IllegalArgumentException("context not associated with any application (using a mock context?)");
  }
  
  public BluetoothAdapter getAdapter()
  {
    return mAdapter;
  }
  
  public List<BluetoothDevice> getConnectedDevices(int paramInt)
  {
    Log.d("BluetoothManager", "getConnectedDevices");
    if ((paramInt != 7) && (paramInt != 8))
    {
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append("Profile not supported: ");
      ((StringBuilder)localObject1).append(paramInt);
      throw new IllegalArgumentException(((StringBuilder)localObject1).toString());
    }
    Object localObject1 = new ArrayList();
    try
    {
      Object localObject2 = mAdapter.getBluetoothManager().getBluetoothGatt();
      if (localObject2 == null) {
        return localObject1;
      }
      localObject2 = ((IBluetoothGatt)localObject2).getDevicesMatchingConnectionStates(new int[] { 2 });
      localObject1 = localObject2;
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("BluetoothManager", "", localRemoteException);
    }
    return localObject1;
  }
  
  public int getConnectionState(BluetoothDevice paramBluetoothDevice, int paramInt)
  {
    Log.d("BluetoothManager", "getConnectionState()");
    Iterator localIterator = getConnectedDevices(paramInt).iterator();
    while (localIterator.hasNext()) {
      if (paramBluetoothDevice.equals((BluetoothDevice)localIterator.next())) {
        return 2;
      }
    }
    return 0;
  }
  
  public List<BluetoothDevice> getDevicesMatchingConnectionStates(int paramInt, int[] paramArrayOfInt)
  {
    Log.d("BluetoothManager", "getDevicesMatchingConnectionStates");
    if ((paramInt != 7) && (paramInt != 8))
    {
      paramArrayOfInt = new StringBuilder();
      paramArrayOfInt.append("Profile not supported: ");
      paramArrayOfInt.append(paramInt);
      throw new IllegalArgumentException(paramArrayOfInt.toString());
    }
    ArrayList localArrayList = new ArrayList();
    try
    {
      IBluetoothGatt localIBluetoothGatt = mAdapter.getBluetoothManager().getBluetoothGatt();
      if (localIBluetoothGatt == null) {
        return localArrayList;
      }
      paramArrayOfInt = localIBluetoothGatt.getDevicesMatchingConnectionStates(paramArrayOfInt);
    }
    catch (RemoteException paramArrayOfInt)
    {
      Log.e("BluetoothManager", "", paramArrayOfInt);
      paramArrayOfInt = localArrayList;
    }
    return paramArrayOfInt;
  }
  
  public BluetoothGattServer openGattServer(Context paramContext, BluetoothGattServerCallback paramBluetoothGattServerCallback)
  {
    return openGattServer(paramContext, paramBluetoothGattServerCallback, 0);
  }
  
  public BluetoothGattServer openGattServer(Context paramContext, BluetoothGattServerCallback paramBluetoothGattServerCallback, int paramInt)
  {
    if ((paramContext != null) && (paramBluetoothGattServerCallback != null))
    {
      paramContext = null;
      try
      {
        IBluetoothGatt localIBluetoothGatt = mAdapter.getBluetoothManager().getBluetoothGatt();
        if (localIBluetoothGatt == null)
        {
          Log.e("BluetoothManager", "Fail to get GATT Server connection");
          return null;
        }
        localObject = new android/bluetooth/BluetoothGattServer;
        ((BluetoothGattServer)localObject).<init>(localIBluetoothGatt, paramInt);
        boolean bool = Boolean.valueOf(((BluetoothGattServer)localObject).registerCallback(paramBluetoothGattServerCallback)).booleanValue();
        if (bool) {
          paramContext = (Context)localObject;
        }
        return paramContext;
      }
      catch (RemoteException paramContext)
      {
        Log.e("BluetoothManager", "", paramContext);
        return null;
      }
    }
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("null parameter: ");
    ((StringBuilder)localObject).append(paramContext);
    ((StringBuilder)localObject).append(" ");
    ((StringBuilder)localObject).append(paramBluetoothGattServerCallback);
    throw new IllegalArgumentException(((StringBuilder)localObject).toString());
  }
}
