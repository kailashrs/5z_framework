package android.bluetooth;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class SdpSapsRecord
  implements Parcelable
{
  public static final Parcelable.Creator CREATOR = new Parcelable.Creator()
  {
    public SdpSapsRecord createFromParcel(Parcel paramAnonymousParcel)
    {
      return new SdpSapsRecord(paramAnonymousParcel);
    }
    
    public SdpRecord[] newArray(int paramAnonymousInt)
    {
      return new SdpRecord[paramAnonymousInt];
    }
  };
  private final int mProfileVersion;
  private final int mRfcommChannelNumber;
  private final String mServiceName;
  
  public SdpSapsRecord(int paramInt1, int paramInt2, String paramString)
  {
    mRfcommChannelNumber = paramInt1;
    mProfileVersion = paramInt2;
    mServiceName = paramString;
  }
  
  public SdpSapsRecord(Parcel paramParcel)
  {
    mRfcommChannelNumber = paramParcel.readInt();
    mProfileVersion = paramParcel.readInt();
    mServiceName = paramParcel.readString();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public int getProfileVersion()
  {
    return mProfileVersion;
  }
  
  public int getRfcommCannelNumber()
  {
    return mRfcommChannelNumber;
  }
  
  public String getServiceName()
  {
    return mServiceName;
  }
  
  public String toString()
  {
    Object localObject1 = "Bluetooth MAS SDP Record:\n";
    if (mRfcommChannelNumber != -1)
    {
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append("Bluetooth MAS SDP Record:\n");
      ((StringBuilder)localObject2).append("RFCOMM Chan Number: ");
      ((StringBuilder)localObject2).append(mRfcommChannelNumber);
      ((StringBuilder)localObject2).append("\n");
      localObject1 = ((StringBuilder)localObject2).toString();
    }
    Object localObject2 = localObject1;
    if (mServiceName != null)
    {
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append((String)localObject1);
      ((StringBuilder)localObject2).append("Service Name: ");
      ((StringBuilder)localObject2).append(mServiceName);
      ((StringBuilder)localObject2).append("\n");
      localObject2 = ((StringBuilder)localObject2).toString();
    }
    localObject1 = localObject2;
    if (mProfileVersion != -1)
    {
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append((String)localObject2);
      ((StringBuilder)localObject1).append("Profile version: ");
      ((StringBuilder)localObject1).append(mProfileVersion);
      ((StringBuilder)localObject1).append("\n");
      localObject1 = ((StringBuilder)localObject1).toString();
    }
    return localObject1;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mRfcommChannelNumber);
    paramParcel.writeInt(mProfileVersion);
    paramParcel.writeString(mServiceName);
  }
}
