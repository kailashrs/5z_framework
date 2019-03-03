package android.bluetooth.le;

import android.bluetooth.IBluetoothGatt;
import android.bluetooth.IBluetoothManager;
import android.os.RemoteException;
import android.util.Log;

public final class AdvertisingSet
{
  private static final String TAG = "AdvertisingSet";
  private int mAdvertiserId;
  private final IBluetoothGatt mGatt;
  
  AdvertisingSet(int paramInt, IBluetoothManager paramIBluetoothManager)
  {
    mAdvertiserId = paramInt;
    try
    {
      mGatt = paramIBluetoothManager.getBluetoothGatt();
      return;
    }
    catch (RemoteException paramIBluetoothManager)
    {
      Log.e("AdvertisingSet", "Failed to get Bluetooth gatt - ", paramIBluetoothManager);
      throw new IllegalStateException("Failed to get Bluetooth");
    }
  }
  
  public void enableAdvertising(boolean paramBoolean, int paramInt1, int paramInt2)
  {
    try
    {
      mGatt.enableAdvertisingSet(mAdvertiserId, paramBoolean, paramInt1, paramInt2);
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("AdvertisingSet", "remote exception - ", localRemoteException);
    }
  }
  
  public int getAdvertiserId()
  {
    return mAdvertiserId;
  }
  
  public void getOwnAddress()
  {
    try
    {
      mGatt.getOwnAddress(mAdvertiserId);
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("AdvertisingSet", "remote exception - ", localRemoteException);
    }
  }
  
  void setAdvertiserId(int paramInt)
  {
    mAdvertiserId = paramInt;
  }
  
  public void setAdvertisingData(AdvertiseData paramAdvertiseData)
  {
    try
    {
      mGatt.setAdvertisingData(mAdvertiserId, paramAdvertiseData);
    }
    catch (RemoteException paramAdvertiseData)
    {
      Log.e("AdvertisingSet", "remote exception - ", paramAdvertiseData);
    }
  }
  
  public void setAdvertisingParameters(AdvertisingSetParameters paramAdvertisingSetParameters)
  {
    try
    {
      mGatt.setAdvertisingParameters(mAdvertiserId, paramAdvertisingSetParameters);
    }
    catch (RemoteException paramAdvertisingSetParameters)
    {
      Log.e("AdvertisingSet", "remote exception - ", paramAdvertisingSetParameters);
    }
  }
  
  public void setPeriodicAdvertisingData(AdvertiseData paramAdvertiseData)
  {
    try
    {
      mGatt.setPeriodicAdvertisingData(mAdvertiserId, paramAdvertiseData);
    }
    catch (RemoteException paramAdvertiseData)
    {
      Log.e("AdvertisingSet", "remote exception - ", paramAdvertiseData);
    }
  }
  
  public void setPeriodicAdvertisingEnabled(boolean paramBoolean)
  {
    try
    {
      mGatt.setPeriodicAdvertisingEnable(mAdvertiserId, paramBoolean);
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("AdvertisingSet", "remote exception - ", localRemoteException);
    }
  }
  
  public void setPeriodicAdvertisingParameters(PeriodicAdvertisingParameters paramPeriodicAdvertisingParameters)
  {
    try
    {
      mGatt.setPeriodicAdvertisingParameters(mAdvertiserId, paramPeriodicAdvertisingParameters);
    }
    catch (RemoteException paramPeriodicAdvertisingParameters)
    {
      Log.e("AdvertisingSet", "remote exception - ", paramPeriodicAdvertisingParameters);
    }
  }
  
  public void setScanResponseData(AdvertiseData paramAdvertiseData)
  {
    try
    {
      mGatt.setScanResponseData(mAdvertiserId, paramAdvertiseData);
    }
    catch (RemoteException paramAdvertiseData)
    {
      Log.e("AdvertisingSet", "remote exception - ", paramAdvertiseData);
    }
  }
}
