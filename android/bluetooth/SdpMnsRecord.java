package android.bluetooth;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class SdpMnsRecord
  implements Parcelable
{
  public static final Parcelable.Creator CREATOR = new Parcelable.Creator()
  {
    public SdpMnsRecord createFromParcel(Parcel paramAnonymousParcel)
    {
      return new SdpMnsRecord(paramAnonymousParcel);
    }
    
    public SdpMnsRecord[] newArray(int paramAnonymousInt)
    {
      return new SdpMnsRecord[paramAnonymousInt];
    }
  };
  private final int mL2capPsm;
  private final int mProfileVersion;
  private final int mRfcommChannelNumber;
  private final String mServiceName;
  private final int mSupportedFeatures;
  
  public SdpMnsRecord(int paramInt1, int paramInt2, int paramInt3, int paramInt4, String paramString)
  {
    mL2capPsm = paramInt1;
    mRfcommChannelNumber = paramInt2;
    mSupportedFeatures = paramInt4;
    mServiceName = paramString;
    mProfileVersion = paramInt3;
  }
  
  public SdpMnsRecord(Parcel paramParcel)
  {
    mRfcommChannelNumber = paramParcel.readInt();
    mL2capPsm = paramParcel.readInt();
    mServiceName = paramParcel.readString();
    mSupportedFeatures = paramParcel.readInt();
    mProfileVersion = paramParcel.readInt();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public int getL2capPsm()
  {
    return mL2capPsm;
  }
  
  public int getProfileVersion()
  {
    return mProfileVersion;
  }
  
  public int getRfcommChannelNumber()
  {
    return mRfcommChannelNumber;
  }
  
  public String getServiceName()
  {
    return mServiceName;
  }
  
  public int getSupportedFeatures()
  {
    return mSupportedFeatures;
  }
  
  public String toString()
  {
    Object localObject1 = "Bluetooth MNS SDP Record:\n";
    if (mRfcommChannelNumber != -1)
    {
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append("Bluetooth MNS SDP Record:\n");
      ((StringBuilder)localObject2).append("RFCOMM Chan Number: ");
      ((StringBuilder)localObject2).append(mRfcommChannelNumber);
      ((StringBuilder)localObject2).append("\n");
      localObject1 = ((StringBuilder)localObject2).toString();
    }
    Object localObject2 = localObject1;
    if (mL2capPsm != -1)
    {
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append((String)localObject1);
      ((StringBuilder)localObject2).append("L2CAP PSM: ");
      ((StringBuilder)localObject2).append(mL2capPsm);
      ((StringBuilder)localObject2).append("\n");
      localObject2 = ((StringBuilder)localObject2).toString();
    }
    localObject1 = localObject2;
    if (mServiceName != null)
    {
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append((String)localObject2);
      ((StringBuilder)localObject1).append("Service Name: ");
      ((StringBuilder)localObject1).append(mServiceName);
      ((StringBuilder)localObject1).append("\n");
      localObject1 = ((StringBuilder)localObject1).toString();
    }
    localObject2 = localObject1;
    if (mSupportedFeatures != -1)
    {
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append((String)localObject1);
      ((StringBuilder)localObject2).append("Supported features: ");
      ((StringBuilder)localObject2).append(mSupportedFeatures);
      ((StringBuilder)localObject2).append("\n");
      localObject2 = ((StringBuilder)localObject2).toString();
    }
    localObject1 = localObject2;
    if (mProfileVersion != -1)
    {
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append((String)localObject2);
      ((StringBuilder)localObject1).append("Profile_version: ");
      ((StringBuilder)localObject1).append(mProfileVersion);
      ((StringBuilder)localObject1).append("\n");
      localObject1 = ((StringBuilder)localObject1).toString();
    }
    return localObject1;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mRfcommChannelNumber);
    paramParcel.writeInt(mL2capPsm);
    paramParcel.writeString(mServiceName);
    paramParcel.writeInt(mSupportedFeatures);
    paramParcel.writeInt(mProfileVersion);
  }
}
