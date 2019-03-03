package android.hardware.usb;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.android.internal.util.Preconditions;

public class UsbConfiguration
  implements Parcelable
{
  private static final int ATTR_REMOTE_WAKEUP = 32;
  private static final int ATTR_SELF_POWERED = 64;
  public static final Parcelable.Creator<UsbConfiguration> CREATOR = new Parcelable.Creator()
  {
    public UsbConfiguration createFromParcel(Parcel paramAnonymousParcel)
    {
      int i = paramAnonymousParcel.readInt();
      Object localObject = paramAnonymousParcel.readString();
      int j = paramAnonymousParcel.readInt();
      int k = paramAnonymousParcel.readInt();
      paramAnonymousParcel = paramAnonymousParcel.readParcelableArray(UsbInterface.class.getClassLoader());
      localObject = new UsbConfiguration(i, (String)localObject, j, k);
      ((UsbConfiguration)localObject).setInterfaces(paramAnonymousParcel);
      return localObject;
    }
    
    public UsbConfiguration[] newArray(int paramAnonymousInt)
    {
      return new UsbConfiguration[paramAnonymousInt];
    }
  };
  private final int mAttributes;
  private final int mId;
  private Parcelable[] mInterfaces;
  private final int mMaxPower;
  private final String mName;
  
  public UsbConfiguration(int paramInt1, String paramString, int paramInt2, int paramInt3)
  {
    mId = paramInt1;
    mName = paramString;
    mAttributes = paramInt2;
    mMaxPower = paramInt3;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public int getAttributes()
  {
    return mAttributes;
  }
  
  public int getId()
  {
    return mId;
  }
  
  public UsbInterface getInterface(int paramInt)
  {
    return (UsbInterface)mInterfaces[paramInt];
  }
  
  public int getInterfaceCount()
  {
    return mInterfaces.length;
  }
  
  public int getMaxPower()
  {
    return mMaxPower * 2;
  }
  
  public String getName()
  {
    return mName;
  }
  
  public boolean isRemoteWakeup()
  {
    boolean bool;
    if ((mAttributes & 0x20) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isSelfPowered()
  {
    boolean bool;
    if ((mAttributes & 0x40) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void setInterfaces(Parcelable[] paramArrayOfParcelable)
  {
    mInterfaces = ((Parcelable[])Preconditions.checkArrayElementsNotNull(paramArrayOfParcelable, "interfaces"));
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("UsbConfiguration[mId=");
    localStringBuilder.append(mId);
    localStringBuilder.append(",mName=");
    localStringBuilder.append(mName);
    localStringBuilder.append(",mAttributes=");
    localStringBuilder.append(mAttributes);
    localStringBuilder.append(",mMaxPower=");
    localStringBuilder.append(mMaxPower);
    localStringBuilder.append(",mInterfaces=[");
    localStringBuilder = new StringBuilder(localStringBuilder.toString());
    for (int i = 0; i < mInterfaces.length; i++)
    {
      localStringBuilder.append("\n");
      localStringBuilder.append(mInterfaces[i].toString());
    }
    localStringBuilder.append("]");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mId);
    paramParcel.writeString(mName);
    paramParcel.writeInt(mAttributes);
    paramParcel.writeInt(mMaxPower);
    paramParcel.writeParcelableArray(mInterfaces, 0);
  }
}
