package android.bluetooth;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class BluetoothHidDeviceAppSdpSettings
  implements Parcelable
{
  public static final Parcelable.Creator<BluetoothHidDeviceAppSdpSettings> CREATOR = new Parcelable.Creator()
  {
    public BluetoothHidDeviceAppSdpSettings createFromParcel(Parcel paramAnonymousParcel)
    {
      return new BluetoothHidDeviceAppSdpSettings(paramAnonymousParcel.readString(), paramAnonymousParcel.readString(), paramAnonymousParcel.readString(), paramAnonymousParcel.readByte(), paramAnonymousParcel.createByteArray());
    }
    
    public BluetoothHidDeviceAppSdpSettings[] newArray(int paramAnonymousInt)
    {
      return new BluetoothHidDeviceAppSdpSettings[paramAnonymousInt];
    }
  };
  private final String mDescription;
  private final byte[] mDescriptors;
  private final String mName;
  private final String mProvider;
  private final byte mSubclass;
  
  public BluetoothHidDeviceAppSdpSettings(String paramString1, String paramString2, String paramString3, byte paramByte, byte[] paramArrayOfByte)
  {
    mName = paramString1;
    mDescription = paramString2;
    mProvider = paramString3;
    mSubclass = ((byte)paramByte);
    mDescriptors = ((byte[])paramArrayOfByte.clone());
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public String getDescription()
  {
    return mDescription;
  }
  
  public byte[] getDescriptors()
  {
    return mDescriptors;
  }
  
  public String getName()
  {
    return mName;
  }
  
  public String getProvider()
  {
    return mProvider;
  }
  
  public byte getSubclass()
  {
    return mSubclass;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(mName);
    paramParcel.writeString(mDescription);
    paramParcel.writeString(mProvider);
    paramParcel.writeByte(mSubclass);
    paramParcel.writeByteArray(mDescriptors);
  }
}
