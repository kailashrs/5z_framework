package android.bluetooth;

import android.os.ParcelFileDescriptor;
import android.util.Log;

public abstract class BluetoothHealthCallback
{
  private static final String TAG = "BluetoothHealthCallback";
  
  public BluetoothHealthCallback() {}
  
  public void onHealthAppConfigurationStatusChange(BluetoothHealthAppConfiguration paramBluetoothHealthAppConfiguration, int paramInt)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("onHealthAppConfigurationStatusChange: ");
    localStringBuilder.append(paramBluetoothHealthAppConfiguration);
    localStringBuilder.append("Status: ");
    localStringBuilder.append(paramInt);
    Log.d("BluetoothHealthCallback", localStringBuilder.toString());
  }
  
  public void onHealthChannelStateChange(BluetoothHealthAppConfiguration paramBluetoothHealthAppConfiguration, BluetoothDevice paramBluetoothDevice, int paramInt1, int paramInt2, ParcelFileDescriptor paramParcelFileDescriptor, int paramInt3)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("onHealthChannelStateChange: ");
    localStringBuilder.append(paramBluetoothHealthAppConfiguration);
    localStringBuilder.append("Device: ");
    localStringBuilder.append(paramBluetoothDevice);
    localStringBuilder.append("prevState:");
    localStringBuilder.append(paramInt1);
    localStringBuilder.append("newState:");
    localStringBuilder.append(paramInt2);
    localStringBuilder.append("ParcelFd:");
    localStringBuilder.append(paramParcelFileDescriptor);
    localStringBuilder.append("ChannelId:");
    localStringBuilder.append(paramInt3);
    Log.d("BluetoothHealthCallback", localStringBuilder.toString());
  }
}
