package android.hardware.hdmi;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

@SystemApi
public class HdmiDeviceInfo
  implements Parcelable
{
  public static final int ADDR_INTERNAL = 0;
  public static final Parcelable.Creator<HdmiDeviceInfo> CREATOR = new Parcelable.Creator()
  {
    public HdmiDeviceInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      int i = paramAnonymousParcel.readInt();
      int j = paramAnonymousParcel.readInt();
      int k = paramAnonymousParcel.readInt();
      if (i != 100)
      {
        switch (i)
        {
        default: 
          return null;
        case 2: 
          return new HdmiDeviceInfo(j, k);
        case 1: 
          i = paramAnonymousParcel.readInt();
          return new HdmiDeviceInfo(j, k, paramAnonymousParcel.readInt(), i);
        }
        i = paramAnonymousParcel.readInt();
        int m = paramAnonymousParcel.readInt();
        int n = paramAnonymousParcel.readInt();
        int i1 = paramAnonymousParcel.readInt();
        return new HdmiDeviceInfo(i, j, k, m, n, paramAnonymousParcel.readString(), i1);
      }
      return HdmiDeviceInfo.INACTIVE_DEVICE;
    }
    
    public HdmiDeviceInfo[] newArray(int paramAnonymousInt)
    {
      return new HdmiDeviceInfo[paramAnonymousInt];
    }
  };
  public static final int DEVICE_AUDIO_SYSTEM = 5;
  public static final int DEVICE_INACTIVE = -1;
  public static final int DEVICE_PLAYBACK = 4;
  public static final int DEVICE_PURE_CEC_SWITCH = 6;
  public static final int DEVICE_RECORDER = 1;
  public static final int DEVICE_RESERVED = 2;
  public static final int DEVICE_TUNER = 3;
  public static final int DEVICE_TV = 0;
  public static final int DEVICE_VIDEO_PROCESSOR = 7;
  private static final int HDMI_DEVICE_TYPE_CEC = 0;
  private static final int HDMI_DEVICE_TYPE_HARDWARE = 2;
  private static final int HDMI_DEVICE_TYPE_INACTIVE = 100;
  private static final int HDMI_DEVICE_TYPE_MHL = 1;
  public static final int ID_INVALID = 65535;
  private static final int ID_OFFSET_CEC = 0;
  private static final int ID_OFFSET_HARDWARE = 192;
  private static final int ID_OFFSET_MHL = 128;
  public static final HdmiDeviceInfo INACTIVE_DEVICE = new HdmiDeviceInfo();
  public static final int PATH_INTERNAL = 0;
  public static final int PATH_INVALID = 65535;
  public static final int PORT_INVALID = -1;
  private final int mAdopterId;
  private final int mDeviceId;
  private final int mDevicePowerStatus;
  private final int mDeviceType;
  private final String mDisplayName;
  private final int mHdmiDeviceType;
  private final int mId;
  private final int mLogicalAddress;
  private final int mPhysicalAddress;
  private final int mPortId;
  private final int mVendorId;
  
  public HdmiDeviceInfo()
  {
    mHdmiDeviceType = 100;
    mPhysicalAddress = 65535;
    mId = 65535;
    mLogicalAddress = -1;
    mDeviceType = -1;
    mPortId = -1;
    mDevicePowerStatus = -1;
    mDisplayName = "Inactive";
    mVendorId = 0;
    mDeviceId = -1;
    mAdopterId = -1;
  }
  
  public HdmiDeviceInfo(int paramInt1, int paramInt2)
  {
    mHdmiDeviceType = 2;
    mPhysicalAddress = paramInt1;
    mPortId = paramInt2;
    mId = idForHardware(paramInt2);
    mLogicalAddress = -1;
    mDeviceType = 2;
    mVendorId = 0;
    mDevicePowerStatus = -1;
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("HDMI");
    localStringBuilder.append(paramInt2);
    mDisplayName = localStringBuilder.toString();
    mDeviceId = -1;
    mAdopterId = -1;
  }
  
  public HdmiDeviceInfo(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    mHdmiDeviceType = 1;
    mPhysicalAddress = paramInt1;
    mPortId = paramInt2;
    mId = idForMhlDevice(paramInt2);
    mLogicalAddress = -1;
    mDeviceType = 2;
    mVendorId = 0;
    mDevicePowerStatus = -1;
    mDisplayName = "Mobile";
    mDeviceId = paramInt3;
    mAdopterId = paramInt4;
  }
  
  public HdmiDeviceInfo(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, String paramString)
  {
    this(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramString, -1);
  }
  
  public HdmiDeviceInfo(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, String paramString, int paramInt6)
  {
    mHdmiDeviceType = 0;
    mPhysicalAddress = paramInt2;
    mPortId = paramInt3;
    mId = idForCecDevice(paramInt1);
    mLogicalAddress = paramInt1;
    mDeviceType = paramInt4;
    mVendorId = paramInt5;
    mDevicePowerStatus = paramInt6;
    mDisplayName = paramString;
    mDeviceId = -1;
    mAdopterId = -1;
  }
  
  public static int idForCecDevice(int paramInt)
  {
    return 0 + paramInt;
  }
  
  public static int idForHardware(int paramInt)
  {
    return 192 + paramInt;
  }
  
  public static int idForMhlDevice(int paramInt)
  {
    return 128 + paramInt;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool1 = paramObject instanceof HdmiDeviceInfo;
    boolean bool2 = false;
    if (!bool1) {
      return false;
    }
    paramObject = (HdmiDeviceInfo)paramObject;
    bool1 = bool2;
    if (mHdmiDeviceType == mHdmiDeviceType)
    {
      bool1 = bool2;
      if (mPhysicalAddress == mPhysicalAddress)
      {
        bool1 = bool2;
        if (mPortId == mPortId)
        {
          bool1 = bool2;
          if (mLogicalAddress == mLogicalAddress)
          {
            bool1 = bool2;
            if (mDeviceType == mDeviceType)
            {
              bool1 = bool2;
              if (mVendorId == mVendorId)
              {
                bool1 = bool2;
                if (mDevicePowerStatus == mDevicePowerStatus)
                {
                  bool1 = bool2;
                  if (mDisplayName.equals(mDisplayName))
                  {
                    bool1 = bool2;
                    if (mDeviceId == mDeviceId)
                    {
                      bool1 = bool2;
                      if (mAdopterId == mAdopterId) {
                        bool1 = true;
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
    return bool1;
  }
  
  public int getAdopterId()
  {
    return mAdopterId;
  }
  
  public int getDeviceId()
  {
    return mDeviceId;
  }
  
  public int getDevicePowerStatus()
  {
    return mDevicePowerStatus;
  }
  
  public int getDeviceType()
  {
    return mDeviceType;
  }
  
  public String getDisplayName()
  {
    return mDisplayName;
  }
  
  public int getId()
  {
    return mId;
  }
  
  public int getLogicalAddress()
  {
    return mLogicalAddress;
  }
  
  public int getPhysicalAddress()
  {
    return mPhysicalAddress;
  }
  
  public int getPortId()
  {
    return mPortId;
  }
  
  public int getVendorId()
  {
    return mVendorId;
  }
  
  public boolean isCecDevice()
  {
    boolean bool;
    if (mHdmiDeviceType == 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isInactivated()
  {
    boolean bool;
    if (mHdmiDeviceType == 100) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isMhlDevice()
  {
    int i = mHdmiDeviceType;
    boolean bool = true;
    if (i != 1) {
      bool = false;
    }
    return bool;
  }
  
  public boolean isSourceType()
  {
    boolean bool1 = isCecDevice();
    boolean bool2 = false;
    if (bool1)
    {
      if ((mDeviceType != 4) && (mDeviceType != 1) && (mDeviceType != 3)) {
        break label43;
      }
      bool2 = true;
      label43:
      return bool2;
    }
    return isMhlDevice();
  }
  
  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    int i = mHdmiDeviceType;
    if (i != 100) {
      switch (i)
      {
      default: 
        return "";
      case 2: 
        localStringBuffer.append("Hardware: ");
        break;
      case 1: 
        localStringBuffer.append("MHL: ");
        localStringBuffer.append("device_id: ");
        localStringBuffer.append(String.format("0x%04X", new Object[] { Integer.valueOf(mDeviceId) }));
        localStringBuffer.append(" ");
        localStringBuffer.append("adopter_id: ");
        localStringBuffer.append(String.format("0x%04X", new Object[] { Integer.valueOf(mAdopterId) }));
        localStringBuffer.append(" ");
        break;
      case 0: 
        localStringBuffer.append("CEC: ");
        localStringBuffer.append("logical_address: ");
        localStringBuffer.append(String.format("0x%02X", new Object[] { Integer.valueOf(mLogicalAddress) }));
        localStringBuffer.append(" ");
        localStringBuffer.append("device_type: ");
        localStringBuffer.append(mDeviceType);
        localStringBuffer.append(" ");
        localStringBuffer.append("vendor_id: ");
        localStringBuffer.append(mVendorId);
        localStringBuffer.append(" ");
        localStringBuffer.append("display_name: ");
        localStringBuffer.append(mDisplayName);
        localStringBuffer.append(" ");
        localStringBuffer.append("power_status: ");
        localStringBuffer.append(mDevicePowerStatus);
        localStringBuffer.append(" ");
        break;
      }
    } else {
      localStringBuffer.append("Inactivated: ");
    }
    localStringBuffer.append("physical_address: ");
    localStringBuffer.append(String.format("0x%04X", new Object[] { Integer.valueOf(mPhysicalAddress) }));
    localStringBuffer.append(" ");
    localStringBuffer.append("port_id: ");
    localStringBuffer.append(mPortId);
    return localStringBuffer.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mHdmiDeviceType);
    paramParcel.writeInt(mPhysicalAddress);
    paramParcel.writeInt(mPortId);
    switch (mHdmiDeviceType)
    {
    default: 
      break;
    case 1: 
      paramParcel.writeInt(mDeviceId);
      paramParcel.writeInt(mAdopterId);
      break;
    case 0: 
      paramParcel.writeInt(mLogicalAddress);
      paramParcel.writeInt(mDeviceType);
      paramParcel.writeInt(mVendorId);
      paramParcel.writeInt(mDevicePowerStatus);
      paramParcel.writeString(mDisplayName);
    }
  }
}
