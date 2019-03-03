package android.hardware.usb;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class UsbEndpoint
  implements Parcelable
{
  public static final Parcelable.Creator<UsbEndpoint> CREATOR = new Parcelable.Creator()
  {
    public UsbEndpoint createFromParcel(Parcel paramAnonymousParcel)
    {
      return new UsbEndpoint(paramAnonymousParcel.readInt(), paramAnonymousParcel.readInt(), paramAnonymousParcel.readInt(), paramAnonymousParcel.readInt());
    }
    
    public UsbEndpoint[] newArray(int paramAnonymousInt)
    {
      return new UsbEndpoint[paramAnonymousInt];
    }
  };
  private final int mAddress;
  private final int mAttributes;
  private final int mInterval;
  private final int mMaxPacketSize;
  
  public UsbEndpoint(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    mAddress = paramInt1;
    mAttributes = paramInt2;
    mMaxPacketSize = paramInt3;
    mInterval = paramInt4;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public int getAddress()
  {
    return mAddress;
  }
  
  public int getAttributes()
  {
    return mAttributes;
  }
  
  public int getDirection()
  {
    return mAddress & 0x80;
  }
  
  public int getEndpointNumber()
  {
    return mAddress & 0xF;
  }
  
  public int getInterval()
  {
    return mInterval;
  }
  
  public int getMaxPacketSize()
  {
    return mMaxPacketSize;
  }
  
  public int getType()
  {
    return mAttributes & 0x3;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("UsbEndpoint[mAddress=");
    localStringBuilder.append(mAddress);
    localStringBuilder.append(",mAttributes=");
    localStringBuilder.append(mAttributes);
    localStringBuilder.append(",mMaxPacketSize=");
    localStringBuilder.append(mMaxPacketSize);
    localStringBuilder.append(",mInterval=");
    localStringBuilder.append(mInterval);
    localStringBuilder.append("]");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mAddress);
    paramParcel.writeInt(mAttributes);
    paramParcel.writeInt(mMaxPacketSize);
    paramParcel.writeInt(mInterval);
  }
}
