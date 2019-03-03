package android.net.wifi.p2p;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WifiP2pDevice
  implements Parcelable
{
  public static final int AVAILABLE = 3;
  public static final int CONNECTED = 0;
  public static final Parcelable.Creator<WifiP2pDevice> CREATOR = new Parcelable.Creator()
  {
    public WifiP2pDevice createFromParcel(Parcel paramAnonymousParcel)
    {
      WifiP2pDevice localWifiP2pDevice = new WifiP2pDevice();
      deviceName = paramAnonymousParcel.readString();
      deviceAddress = paramAnonymousParcel.readString();
      primaryDeviceType = paramAnonymousParcel.readString();
      secondaryDeviceType = paramAnonymousParcel.readString();
      wpsConfigMethodsSupported = paramAnonymousParcel.readInt();
      deviceCapability = paramAnonymousParcel.readInt();
      groupCapability = paramAnonymousParcel.readInt();
      status = paramAnonymousParcel.readInt();
      if (paramAnonymousParcel.readInt() == 1) {
        wfdInfo = ((WifiP2pWfdInfo)WifiP2pWfdInfo.CREATOR.createFromParcel(paramAnonymousParcel));
      }
      return localWifiP2pDevice;
    }
    
    public WifiP2pDevice[] newArray(int paramAnonymousInt)
    {
      return new WifiP2pDevice[paramAnonymousInt];
    }
  };
  private static final int DEVICE_CAPAB_CLIENT_DISCOVERABILITY = 2;
  private static final int DEVICE_CAPAB_CONCURRENT_OPER = 4;
  private static final int DEVICE_CAPAB_DEVICE_LIMIT = 16;
  private static final int DEVICE_CAPAB_INFRA_MANAGED = 8;
  private static final int DEVICE_CAPAB_INVITATION_PROCEDURE = 32;
  private static final int DEVICE_CAPAB_SERVICE_DISCOVERY = 1;
  public static final int FAILED = 2;
  private static final int GROUP_CAPAB_CROSS_CONN = 16;
  private static final int GROUP_CAPAB_GROUP_FORMATION = 64;
  private static final int GROUP_CAPAB_GROUP_LIMIT = 4;
  private static final int GROUP_CAPAB_GROUP_OWNER = 1;
  private static final int GROUP_CAPAB_INTRA_BSS_DIST = 8;
  private static final int GROUP_CAPAB_PERSISTENT_GROUP = 2;
  private static final int GROUP_CAPAB_PERSISTENT_RECONN = 32;
  public static final int INVITED = 1;
  private static final String TAG = "WifiP2pDevice";
  public static final int UNAVAILABLE = 4;
  private static final int WPS_CONFIG_DISPLAY = 8;
  private static final int WPS_CONFIG_KEYPAD = 256;
  private static final int WPS_CONFIG_PUSHBUTTON = 128;
  private static final Pattern detailedDevicePattern = Pattern.compile("((?:[0-9a-f]{2}:){5}[0-9a-f]{2}) (\\d+ )?p2p_dev_addr=((?:[0-9a-f]{2}:){5}[0-9a-f]{2}) pri_dev_type=(\\d+-[0-9a-fA-F]+-\\d+) name='(.*)' config_methods=(0x[0-9a-fA-F]+) dev_capab=(0x[0-9a-fA-F]+) group_capab=(0x[0-9a-fA-F]+)( wfd_dev_info=0x([0-9a-fA-F]{12}))?( wfd_r2_dev_info=0x([0-9a-fA-F]{4}))?");
  private static final Pattern threeTokenPattern;
  private static final Pattern twoTokenPattern = Pattern.compile("(p2p_dev_addr=)?((?:[0-9a-f]{2}:){5}[0-9a-f]{2})");
  public String deviceAddress = "";
  public int deviceCapability;
  public String deviceName = "";
  public int groupCapability;
  public String primaryDeviceType;
  public String secondaryDeviceType;
  public int status = 4;
  public WifiP2pWfdInfo wfdInfo;
  public int wpsConfigMethodsSupported;
  
  static
  {
    threeTokenPattern = Pattern.compile("(?:[0-9a-f]{2}:){5}[0-9a-f]{2} p2p_dev_addr=((?:[0-9a-f]{2}:){5}[0-9a-f]{2})");
  }
  
  public WifiP2pDevice() {}
  
  public WifiP2pDevice(WifiP2pDevice paramWifiP2pDevice)
  {
    if (paramWifiP2pDevice != null)
    {
      deviceName = deviceName;
      deviceAddress = deviceAddress;
      primaryDeviceType = primaryDeviceType;
      secondaryDeviceType = secondaryDeviceType;
      wpsConfigMethodsSupported = wpsConfigMethodsSupported;
      deviceCapability = deviceCapability;
      groupCapability = groupCapability;
      status = status;
      wfdInfo = new WifiP2pWfdInfo(wfdInfo);
    }
  }
  
  public WifiP2pDevice(String paramString)
    throws IllegalArgumentException
  {
    String[] arrayOfString = paramString.split("[ \n]");
    if (arrayOfString.length >= 1)
    {
      switch (arrayOfString.length)
      {
      default: 
        Matcher localMatcher = detailedDevicePattern.matcher(paramString);
        if (!localMatcher.find()) {
          break label337;
        }
        deviceAddress = localMatcher.group(3);
        primaryDeviceType = localMatcher.group(4);
        deviceName = localMatcher.group(5);
        wpsConfigMethodsSupported = parseHex(localMatcher.group(6));
        deviceCapability = parseHex(localMatcher.group(7));
        groupCapability = parseHex(localMatcher.group(8));
        if (localMatcher.group(9) != null)
        {
          paramString = localMatcher.group(10);
          wfdInfo = new WifiP2pWfdInfo(parseHex(paramString.substring(0, 4)), parseHex(paramString.substring(4, 8)), parseHex(paramString.substring(8, 12)));
          if (localMatcher.group(11) != null)
          {
            paramString = localMatcher.group(12);
            wfdInfo.setWfdR2Device(parseHex(paramString.substring(0, 4)));
          }
        }
        break;
      case 3: 
        paramString = threeTokenPattern.matcher(paramString);
        if (paramString.find())
        {
          deviceAddress = paramString.group(1);
          return;
        }
        throw new IllegalArgumentException("Malformed supplicant event");
      case 2: 
        paramString = twoTokenPattern.matcher(paramString);
        if (paramString.find())
        {
          deviceAddress = paramString.group(2);
          return;
        }
        throw new IllegalArgumentException("Malformed supplicant event");
      case 1: 
        deviceAddress = paramString;
        return;
      }
      if (arrayOfString[0].startsWith("P2P-DEVICE-FOUND")) {
        status = 3;
      }
      return;
      label337:
      throw new IllegalArgumentException("Malformed supplicant event");
    }
    throw new IllegalArgumentException("Malformed supplicant event");
  }
  
  private int parseHex(String paramString)
  {
    int i = 0;
    String str;
    if (!paramString.startsWith("0x"))
    {
      str = paramString;
      if (!paramString.startsWith("0X")) {}
    }
    else
    {
      str = paramString.substring(2);
    }
    try
    {
      int j = Integer.parseInt(str, 16);
      i = j;
    }
    catch (NumberFormatException paramString)
    {
      paramString = new StringBuilder();
      paramString.append("Failed to parse hex string ");
      paramString.append(str);
      Log.e("WifiP2pDevice", paramString.toString());
    }
    return i;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool = true;
    if (this == paramObject) {
      return true;
    }
    if (!(paramObject instanceof WifiP2pDevice)) {
      return false;
    }
    paramObject = (WifiP2pDevice)paramObject;
    if ((paramObject != null) && (deviceAddress != null)) {
      return deviceAddress.equals(deviceAddress);
    }
    if (deviceAddress != null) {
      bool = false;
    }
    return bool;
  }
  
  public boolean isDeviceLimit()
  {
    boolean bool;
    if ((deviceCapability & 0x10) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isGroupLimit()
  {
    boolean bool;
    if ((groupCapability & 0x4) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isGroupOwner()
  {
    int i = groupCapability;
    boolean bool = true;
    if ((i & 0x1) == 0) {
      bool = false;
    }
    return bool;
  }
  
  public boolean isInvitationCapable()
  {
    boolean bool;
    if ((deviceCapability & 0x20) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isServiceDiscoveryCapable()
  {
    int i = deviceCapability;
    boolean bool = true;
    if ((i & 0x1) == 0) {
      bool = false;
    }
    return bool;
  }
  
  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append("Device: ");
    localStringBuffer.append(deviceName);
    localStringBuffer.append("\n deviceAddress: ");
    localStringBuffer.append(deviceAddress);
    localStringBuffer.append("\n primary type: ");
    localStringBuffer.append(primaryDeviceType);
    localStringBuffer.append("\n secondary type: ");
    localStringBuffer.append(secondaryDeviceType);
    localStringBuffer.append("\n wps: ");
    localStringBuffer.append(wpsConfigMethodsSupported);
    localStringBuffer.append("\n grpcapab: ");
    localStringBuffer.append(groupCapability);
    localStringBuffer.append("\n devcapab: ");
    localStringBuffer.append(deviceCapability);
    localStringBuffer.append("\n status: ");
    localStringBuffer.append(status);
    localStringBuffer.append("\n wfdInfo: ");
    localStringBuffer.append(wfdInfo);
    return localStringBuffer.toString();
  }
  
  public void update(WifiP2pDevice paramWifiP2pDevice)
  {
    updateSupplicantDetails(paramWifiP2pDevice);
    status = status;
  }
  
  public void updateSupplicantDetails(WifiP2pDevice paramWifiP2pDevice)
  {
    if (paramWifiP2pDevice != null)
    {
      if (deviceAddress != null)
      {
        if (deviceAddress.equals(deviceAddress))
        {
          deviceName = deviceName;
          primaryDeviceType = primaryDeviceType;
          secondaryDeviceType = secondaryDeviceType;
          wpsConfigMethodsSupported = wpsConfigMethodsSupported;
          deviceCapability = deviceCapability;
          groupCapability = groupCapability;
          wfdInfo = wfdInfo;
          return;
        }
        throw new IllegalArgumentException("deviceAddress does not match");
      }
      throw new IllegalArgumentException("deviceAddress is null");
    }
    throw new IllegalArgumentException("device is null");
  }
  
  public boolean wpsDisplaySupported()
  {
    boolean bool;
    if ((wpsConfigMethodsSupported & 0x8) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean wpsKeypadSupported()
  {
    boolean bool;
    if ((wpsConfigMethodsSupported & 0x100) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean wpsPbcSupported()
  {
    boolean bool;
    if ((wpsConfigMethodsSupported & 0x80) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(deviceName);
    paramParcel.writeString(deviceAddress);
    paramParcel.writeString(primaryDeviceType);
    paramParcel.writeString(secondaryDeviceType);
    paramParcel.writeInt(wpsConfigMethodsSupported);
    paramParcel.writeInt(deviceCapability);
    paramParcel.writeInt(groupCapability);
    paramParcel.writeInt(status);
    if (wfdInfo != null)
    {
      paramParcel.writeInt(1);
      wfdInfo.writeToParcel(paramParcel, paramInt);
    }
    else
    {
      paramParcel.writeInt(0);
    }
  }
}
