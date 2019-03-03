package android.hardware.usb;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.android.internal.util.Preconditions;

public class UsbDevice
  implements Parcelable
{
  public static final Parcelable.Creator<UsbDevice> CREATOR = new Parcelable.Creator()
  {
    public UsbDevice createFromParcel(Parcel paramAnonymousParcel)
    {
      String str1 = paramAnonymousParcel.readString();
      int i = paramAnonymousParcel.readInt();
      int j = paramAnonymousParcel.readInt();
      int k = paramAnonymousParcel.readInt();
      int m = paramAnonymousParcel.readInt();
      int n = paramAnonymousParcel.readInt();
      String str2 = paramAnonymousParcel.readString();
      String str3 = paramAnonymousParcel.readString();
      Object localObject = paramAnonymousParcel.readString();
      String str4 = paramAnonymousParcel.readString();
      paramAnonymousParcel = paramAnonymousParcel.readParcelableArray(UsbInterface.class.getClassLoader());
      localObject = new UsbDevice(str1, i, j, k, m, n, str2, str3, (String)localObject, str4);
      ((UsbDevice)localObject).setConfigurations(paramAnonymousParcel);
      return localObject;
    }
    
    public UsbDevice[] newArray(int paramAnonymousInt)
    {
      return new UsbDevice[paramAnonymousInt];
    }
  };
  private static final boolean DEBUG = false;
  private static final String TAG = "UsbDevice";
  private final int mClass;
  private Parcelable[] mConfigurations;
  private UsbInterface[] mInterfaces;
  private final String mManufacturerName;
  private final String mName;
  private final int mProductId;
  private final String mProductName;
  private final int mProtocol;
  private final String mSerialNumber;
  private final int mSubclass;
  private final int mVendorId;
  private final String mVersion;
  
  public UsbDevice(String paramString1, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, String paramString2, String paramString3, String paramString4, String paramString5)
  {
    mName = ((String)Preconditions.checkNotNull(paramString1));
    mVendorId = paramInt1;
    mProductId = paramInt2;
    mClass = paramInt3;
    mSubclass = paramInt4;
    mProtocol = paramInt5;
    mManufacturerName = paramString2;
    mProductName = paramString3;
    mVersion = ((String)Preconditions.checkStringNotEmpty(paramString4));
    mSerialNumber = paramString5;
  }
  
  public static int getDeviceId(String paramString)
  {
    return native_get_device_id(paramString);
  }
  
  public static String getDeviceName(int paramInt)
  {
    return native_get_device_name(paramInt);
  }
  
  private UsbInterface[] getInterfaceList()
  {
    if (mInterfaces == null)
    {
      int i = mConfigurations.length;
      int j = 0;
      for (int k = 0; k < i; k++) {
        j += ((UsbConfiguration)mConfigurations[k]).getInterfaceCount();
      }
      mInterfaces = new UsbInterface[j];
      j = 0;
      for (k = 0; k < i; k++)
      {
        UsbConfiguration localUsbConfiguration = (UsbConfiguration)mConfigurations[k];
        int m = localUsbConfiguration.getInterfaceCount();
        int n = 0;
        while (n < m)
        {
          mInterfaces[j] = localUsbConfiguration.getInterface(n);
          n++;
          j++;
        }
      }
    }
    return mInterfaces;
  }
  
  private static native int native_get_device_id(String paramString);
  
  private static native String native_get_device_name(int paramInt);
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    if ((paramObject instanceof UsbDevice)) {
      return mName.equals(mName);
    }
    if ((paramObject instanceof String)) {
      return ((String)paramObject).equals(mName);
    }
    return false;
  }
  
  public UsbConfiguration getConfiguration(int paramInt)
  {
    return (UsbConfiguration)mConfigurations[paramInt];
  }
  
  public int getConfigurationCount()
  {
    return mConfigurations.length;
  }
  
  public int getDeviceClass()
  {
    return mClass;
  }
  
  public int getDeviceId()
  {
    return getDeviceId(mName);
  }
  
  public String getDeviceName()
  {
    return mName;
  }
  
  public int getDeviceProtocol()
  {
    return mProtocol;
  }
  
  public int getDeviceSubclass()
  {
    return mSubclass;
  }
  
  public UsbInterface getInterface(int paramInt)
  {
    return getInterfaceList()[paramInt];
  }
  
  public int getInterfaceCount()
  {
    return getInterfaceList().length;
  }
  
  public String getManufacturerName()
  {
    return mManufacturerName;
  }
  
  public int getProductId()
  {
    return mProductId;
  }
  
  public String getProductName()
  {
    return mProductName;
  }
  
  public String getSerialNumber()
  {
    return mSerialNumber;
  }
  
  public int getVendorId()
  {
    return mVendorId;
  }
  
  public String getVersion()
  {
    return mVersion;
  }
  
  public int hashCode()
  {
    return mName.hashCode();
  }
  
  public void setConfigurations(Parcelable[] paramArrayOfParcelable)
  {
    mConfigurations = ((Parcelable[])Preconditions.checkArrayElementsNotNull(paramArrayOfParcelable, "configuration"));
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("UsbDevice[mName=");
    localStringBuilder.append(mName);
    localStringBuilder.append(",mVendorId=");
    localStringBuilder.append(mVendorId);
    localStringBuilder.append(",mProductId=");
    localStringBuilder.append(mProductId);
    localStringBuilder.append(",mClass=");
    localStringBuilder.append(mClass);
    localStringBuilder.append(",mSubclass=");
    localStringBuilder.append(mSubclass);
    localStringBuilder.append(",mProtocol=");
    localStringBuilder.append(mProtocol);
    localStringBuilder.append(",mManufacturerName=");
    localStringBuilder.append(mManufacturerName);
    localStringBuilder.append(",mProductName=");
    localStringBuilder.append(mProductName);
    localStringBuilder.append(",mVersion=");
    localStringBuilder.append(mVersion);
    localStringBuilder.append(",mSerialNumber=");
    localStringBuilder.append(mSerialNumber);
    localStringBuilder.append(",mConfigurations=[");
    localStringBuilder = new StringBuilder(localStringBuilder.toString());
    for (int i = 0; i < mConfigurations.length; i++)
    {
      localStringBuilder.append("\n");
      localStringBuilder.append(mConfigurations[i].toString());
    }
    localStringBuilder.append("]");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(mName);
    paramParcel.writeInt(mVendorId);
    paramParcel.writeInt(mProductId);
    paramParcel.writeInt(mClass);
    paramParcel.writeInt(mSubclass);
    paramParcel.writeInt(mProtocol);
    paramParcel.writeString(mManufacturerName);
    paramParcel.writeString(mProductName);
    paramParcel.writeString(mVersion);
    paramParcel.writeString(mSerialNumber);
    paramParcel.writeParcelableArray(mConfigurations, 0);
  }
}
