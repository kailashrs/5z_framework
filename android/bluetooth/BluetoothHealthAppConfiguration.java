package android.bluetooth;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class BluetoothHealthAppConfiguration
  implements Parcelable
{
  public static final Parcelable.Creator<BluetoothHealthAppConfiguration> CREATOR = new Parcelable.Creator()
  {
    public BluetoothHealthAppConfiguration createFromParcel(Parcel paramAnonymousParcel)
    {
      return new BluetoothHealthAppConfiguration(paramAnonymousParcel.readString(), paramAnonymousParcel.readInt(), paramAnonymousParcel.readInt(), paramAnonymousParcel.readInt());
    }
    
    public BluetoothHealthAppConfiguration[] newArray(int paramAnonymousInt)
    {
      return new BluetoothHealthAppConfiguration[paramAnonymousInt];
    }
  };
  private final int mChannelType;
  private final int mDataType;
  private final String mName;
  private final int mRole;
  
  BluetoothHealthAppConfiguration(String paramString, int paramInt)
  {
    mName = paramString;
    mDataType = paramInt;
    mRole = 2;
    mChannelType = 12;
  }
  
  BluetoothHealthAppConfiguration(String paramString, int paramInt1, int paramInt2, int paramInt3)
  {
    mName = paramString;
    mDataType = paramInt1;
    mRole = paramInt2;
    mChannelType = paramInt3;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool1 = paramObject instanceof BluetoothHealthAppConfiguration;
    boolean bool2 = false;
    if (bool1)
    {
      paramObject = (BluetoothHealthAppConfiguration)paramObject;
      if (mName == null) {
        return false;
      }
      bool1 = bool2;
      if (mName.equals(paramObject.getName()))
      {
        bool1 = bool2;
        if (mDataType == paramObject.getDataType())
        {
          bool1 = bool2;
          if (mRole == paramObject.getRole())
          {
            bool1 = bool2;
            if (mChannelType == paramObject.getChannelType()) {
              bool1 = true;
            }
          }
        }
      }
      return bool1;
    }
    return false;
  }
  
  public int getChannelType()
  {
    return mChannelType;
  }
  
  public int getDataType()
  {
    return mDataType;
  }
  
  public String getName()
  {
    return mName;
  }
  
  public int getRole()
  {
    return mRole;
  }
  
  public int hashCode()
  {
    int i;
    if (mName != null) {
      i = mName.hashCode();
    } else {
      i = 0;
    }
    return 31 * (31 * (31 * (31 * 17 + i) + mDataType) + mRole) + mChannelType;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("BluetoothHealthAppConfiguration [mName = ");
    localStringBuilder.append(mName);
    localStringBuilder.append(",mDataType = ");
    localStringBuilder.append(mDataType);
    localStringBuilder.append(", mRole = ");
    localStringBuilder.append(mRole);
    localStringBuilder.append(",mChannelType = ");
    localStringBuilder.append(mChannelType);
    localStringBuilder.append("]");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(mName);
    paramParcel.writeInt(mDataType);
    paramParcel.writeInt(mRole);
    paramParcel.writeInt(mChannelType);
  }
}
