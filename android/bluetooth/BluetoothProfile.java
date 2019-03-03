package android.bluetooth;

import android.annotation.SystemApi;
import java.util.List;

public abstract interface BluetoothProfile
{
  public static final int A2DP = 2;
  public static final int A2DP_SINK = 11;
  public static final int AVRCP = 13;
  public static final int AVRCP_CONTROLLER = 12;
  public static final int BA_TRANSMITTER = 23;
  public static final int DUN = 22;
  public static final String EXTRA_PREVIOUS_STATE = "android.bluetooth.profile.extra.PREVIOUS_STATE";
  public static final String EXTRA_STATE = "android.bluetooth.profile.extra.STATE";
  public static final int GATT = 7;
  public static final int GATT_SERVER = 8;
  public static final int HEADSET = 1;
  public static final int HEADSET_CLIENT = 16;
  public static final int HEALTH = 3;
  public static final int HEARING_AID = 21;
  public static final int HID_DEVICE = 19;
  public static final int HID_HOST = 4;
  public static final int MAP = 9;
  public static final int MAP_CLIENT = 18;
  public static final int MAX_PROFILE_ID = 23;
  public static final int OPP = 20;
  public static final int PAN = 5;
  public static final int PBAP = 6;
  public static final int PBAP_CLIENT = 17;
  public static final int PRIORITY_AUTO_CONNECT = 1000;
  @SystemApi
  public static final int PRIORITY_OFF = 0;
  @SystemApi
  public static final int PRIORITY_ON = 100;
  public static final int PRIORITY_UNDEFINED = -1;
  public static final int SAP = 10;
  public static final int STATE_CONNECTED = 2;
  public static final int STATE_CONNECTING = 1;
  public static final int STATE_DISCONNECTED = 0;
  public static final int STATE_DISCONNECTING = 3;
  
  public static String getConnectionStateName(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return "STATE_UNKNOWN";
    case 3: 
      return "STATE_DISCONNECTING";
    case 2: 
      return "STATE_CONNECTED";
    case 1: 
      return "STATE_CONNECTING";
    }
    return "STATE_DISCONNECTED";
  }
  
  public abstract List<BluetoothDevice> getConnectedDevices();
  
  public abstract int getConnectionState(BluetoothDevice paramBluetoothDevice);
  
  public abstract List<BluetoothDevice> getDevicesMatchingConnectionStates(int[] paramArrayOfInt);
  
  public static abstract interface ServiceListener
  {
    public abstract void onServiceConnected(int paramInt, BluetoothProfile paramBluetoothProfile);
    
    public abstract void onServiceDisconnected(int paramInt);
  }
}
