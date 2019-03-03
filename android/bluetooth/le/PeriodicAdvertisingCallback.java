package android.bluetooth.le;

import android.bluetooth.BluetoothDevice;

public abstract class PeriodicAdvertisingCallback
{
  public static final int SYNC_NO_RESOURCES = 2;
  public static final int SYNC_NO_RESPONSE = 1;
  public static final int SYNC_SUCCESS = 0;
  
  public PeriodicAdvertisingCallback() {}
  
  public void onPeriodicAdvertisingReport(PeriodicAdvertisingReport paramPeriodicAdvertisingReport) {}
  
  public void onSyncEstablished(int paramInt1, BluetoothDevice paramBluetoothDevice, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {}
  
  public void onSyncLost(int paramInt) {}
}
