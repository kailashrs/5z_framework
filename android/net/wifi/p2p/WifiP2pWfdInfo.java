package android.net.wifi.p2p;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.Locale;

public class WifiP2pWfdInfo
  implements Parcelable
{
  private static final int COUPLED_SINK_SUPPORT_AT_SINK = 8;
  private static final int COUPLED_SINK_SUPPORT_AT_SOURCE = 4;
  public static final Parcelable.Creator<WifiP2pWfdInfo> CREATOR = new Parcelable.Creator()
  {
    public WifiP2pWfdInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      WifiP2pWfdInfo localWifiP2pWfdInfo = new WifiP2pWfdInfo();
      localWifiP2pWfdInfo.readFromParcel(paramAnonymousParcel);
      return localWifiP2pWfdInfo;
    }
    
    public WifiP2pWfdInfo[] newArray(int paramAnonymousInt)
    {
      return new WifiP2pWfdInfo[paramAnonymousInt];
    }
  };
  private static final int DEVICE_TYPE = 3;
  public static final int PRIMARY_SINK = 1;
  public static final int SECONDARY_SINK = 2;
  private static final int SESSION_AVAILABLE = 48;
  private static final int SESSION_AVAILABLE_BIT1 = 16;
  private static final int SESSION_AVAILABLE_BIT2 = 32;
  public static final int SOURCE_OR_PRIMARY_SINK = 3;
  private static final String TAG = "WifiP2pWfdInfo";
  public static final int WFD_SOURCE = 0;
  private int mCtrlPort;
  private int mDeviceInfo;
  private int mMaxThroughput;
  private int mR2DeviceInfo;
  private boolean mWfdEnabled;
  
  public WifiP2pWfdInfo() {}
  
  public WifiP2pWfdInfo(int paramInt1, int paramInt2, int paramInt3)
  {
    mWfdEnabled = true;
    mDeviceInfo = paramInt1;
    mCtrlPort = paramInt2;
    mMaxThroughput = paramInt3;
    mR2DeviceInfo = -1;
  }
  
  public WifiP2pWfdInfo(WifiP2pWfdInfo paramWifiP2pWfdInfo)
  {
    if (paramWifiP2pWfdInfo != null)
    {
      mWfdEnabled = mWfdEnabled;
      mDeviceInfo = mDeviceInfo;
      mCtrlPort = mCtrlPort;
      mMaxThroughput = mMaxThroughput;
      mR2DeviceInfo = mR2DeviceInfo;
    }
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public int getControlPort()
  {
    return mCtrlPort;
  }
  
  public String getDeviceInfoHex()
  {
    return String.format(Locale.US, "%04x%04x%04x", new Object[] { Integer.valueOf(mDeviceInfo), Integer.valueOf(mCtrlPort), Integer.valueOf(mMaxThroughput) });
  }
  
  public int getDeviceType()
  {
    return mDeviceInfo & 0x3;
  }
  
  public int getMaxThroughput()
  {
    return mMaxThroughput;
  }
  
  public String getR2DeviceInfoHex()
  {
    return String.format(Locale.US, "%04x%04x", new Object[] { Integer.valueOf(2), Integer.valueOf(mR2DeviceInfo) });
  }
  
  public boolean isCoupledSinkSupportedAtSink()
  {
    boolean bool;
    if ((mDeviceInfo & 0x8) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isCoupledSinkSupportedAtSource()
  {
    boolean bool;
    if ((mDeviceInfo & 0x8) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isSessionAvailable()
  {
    boolean bool;
    if ((mDeviceInfo & 0x30) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isWfdEnabled()
  {
    return mWfdEnabled;
  }
  
  public boolean isWfdR2Supported()
  {
    boolean bool;
    if (mR2DeviceInfo < 0) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public void readFromParcel(Parcel paramParcel)
  {
    int i = paramParcel.readInt();
    boolean bool = true;
    if (i != 1) {
      bool = false;
    }
    mWfdEnabled = bool;
    mDeviceInfo = paramParcel.readInt();
    mCtrlPort = paramParcel.readInt();
    mMaxThroughput = paramParcel.readInt();
    mR2DeviceInfo = paramParcel.readInt();
  }
  
  public void setControlPort(int paramInt)
  {
    mCtrlPort = paramInt;
  }
  
  public void setCoupledSinkSupportAtSink(boolean paramBoolean)
  {
    if (paramBoolean) {
      mDeviceInfo |= 0x8;
    } else {
      mDeviceInfo &= 0xFFFFFFF7;
    }
  }
  
  public void setCoupledSinkSupportAtSource(boolean paramBoolean)
  {
    if (paramBoolean) {
      mDeviceInfo |= 0x8;
    } else {
      mDeviceInfo &= 0xFFFFFFF7;
    }
  }
  
  public boolean setDeviceType(int paramInt)
  {
    if ((paramInt >= 0) && (paramInt <= 3))
    {
      mDeviceInfo &= 0xFFFFFFFC;
      mDeviceInfo |= paramInt;
      return true;
    }
    return false;
  }
  
  public void setMaxThroughput(int paramInt)
  {
    mMaxThroughput = paramInt;
  }
  
  public void setSessionAvailable(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      mDeviceInfo |= 0x10;
      mDeviceInfo &= 0xFFFFFFDF;
    }
    else
    {
      mDeviceInfo &= 0xFFFFFFCF;
    }
  }
  
  public void setWfdEnabled(boolean paramBoolean)
  {
    mWfdEnabled = paramBoolean;
  }
  
  public void setWfdR2Device(int paramInt)
  {
    mR2DeviceInfo = paramInt;
  }
  
  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append("WFD enabled: ");
    localStringBuffer.append(mWfdEnabled);
    localStringBuffer.append("WFD DeviceInfo: ");
    localStringBuffer.append(mDeviceInfo);
    localStringBuffer.append("\n WFD CtrlPort: ");
    localStringBuffer.append(mCtrlPort);
    localStringBuffer.append("\n WFD MaxThroughput: ");
    localStringBuffer.append(mMaxThroughput);
    localStringBuffer.append("\n WFD R2 DeviceInfo: ");
    localStringBuffer.append(mR2DeviceInfo);
    return localStringBuffer.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mWfdEnabled);
    paramParcel.writeInt(mDeviceInfo);
    paramParcel.writeInt(mCtrlPort);
    paramParcel.writeInt(mMaxThroughput);
    paramParcel.writeInt(mR2DeviceInfo);
  }
}
