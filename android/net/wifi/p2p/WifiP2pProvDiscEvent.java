package android.net.wifi.p2p;

public class WifiP2pProvDiscEvent
{
  public static final int ENTER_PIN = 3;
  public static final int PBC_REQ = 1;
  public static final int PBC_RSP = 2;
  public static final int SHOW_PIN = 4;
  private static final String TAG = "WifiP2pProvDiscEvent";
  public WifiP2pDevice device;
  public int event;
  public String pin;
  
  public WifiP2pProvDiscEvent()
  {
    device = new WifiP2pDevice();
  }
  
  public WifiP2pProvDiscEvent(String paramString)
    throws IllegalArgumentException
  {
    Object localObject = paramString.split(" ");
    if (localObject.length >= 2)
    {
      if (localObject[0].endsWith("PBC-REQ"))
      {
        event = 1;
      }
      else if (localObject[0].endsWith("PBC-RESP"))
      {
        event = 2;
      }
      else if (localObject[0].endsWith("ENTER-PIN"))
      {
        event = 3;
      }
      else
      {
        if (!localObject[0].endsWith("SHOW-PIN")) {
          break label127;
        }
        event = 4;
      }
      device = new WifiP2pDevice();
      device.deviceAddress = localObject[1];
      if (event == 4) {
        pin = localObject[2];
      }
      return;
      label127:
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Malformed event ");
      ((StringBuilder)localObject).append(paramString);
      throw new IllegalArgumentException(((StringBuilder)localObject).toString());
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Malformed event ");
    ((StringBuilder)localObject).append(paramString);
    throw new IllegalArgumentException(((StringBuilder)localObject).toString());
  }
  
  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append(device);
    localStringBuffer.append("\n event: ");
    localStringBuffer.append(event);
    localStringBuffer.append("\n pin: ");
    localStringBuffer.append(pin);
    return localStringBuffer.toString();
  }
}
