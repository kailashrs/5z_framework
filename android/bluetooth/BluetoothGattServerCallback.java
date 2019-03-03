package android.bluetooth;

public abstract class BluetoothGattServerCallback
{
  public BluetoothGattServerCallback() {}
  
  public void onCharacteristicReadRequest(BluetoothDevice paramBluetoothDevice, int paramInt1, int paramInt2, BluetoothGattCharacteristic paramBluetoothGattCharacteristic) {}
  
  public void onCharacteristicWriteRequest(BluetoothDevice paramBluetoothDevice, int paramInt1, BluetoothGattCharacteristic paramBluetoothGattCharacteristic, boolean paramBoolean1, boolean paramBoolean2, int paramInt2, byte[] paramArrayOfByte) {}
  
  public void onConnectionStateChange(BluetoothDevice paramBluetoothDevice, int paramInt1, int paramInt2) {}
  
  public void onConnectionUpdated(BluetoothDevice paramBluetoothDevice, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {}
  
  public void onDescriptorReadRequest(BluetoothDevice paramBluetoothDevice, int paramInt1, int paramInt2, BluetoothGattDescriptor paramBluetoothGattDescriptor) {}
  
  public void onDescriptorWriteRequest(BluetoothDevice paramBluetoothDevice, int paramInt1, BluetoothGattDescriptor paramBluetoothGattDescriptor, boolean paramBoolean1, boolean paramBoolean2, int paramInt2, byte[] paramArrayOfByte) {}
  
  public void onExecuteWrite(BluetoothDevice paramBluetoothDevice, int paramInt, boolean paramBoolean) {}
  
  public void onMtuChanged(BluetoothDevice paramBluetoothDevice, int paramInt) {}
  
  public void onNotificationSent(BluetoothDevice paramBluetoothDevice, int paramInt) {}
  
  public void onPhyRead(BluetoothDevice paramBluetoothDevice, int paramInt1, int paramInt2, int paramInt3) {}
  
  public void onPhyUpdate(BluetoothDevice paramBluetoothDevice, int paramInt1, int paramInt2, int paramInt3) {}
  
  public void onServiceAdded(int paramInt, BluetoothGattService paramBluetoothGattService) {}
}
