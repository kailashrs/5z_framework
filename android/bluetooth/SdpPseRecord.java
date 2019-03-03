package android.bluetooth;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class SdpPseRecord
  implements Parcelable
{
  public static final Parcelable.Creator CREATOR = new Parcelable.Creator()
  {
    public SdpPseRecord createFromParcel(Parcel paramAnonymousParcel)
    {
      return new SdpPseRecord(paramAnonymousParcel);
    }
    
    public SdpPseRecord[] newArray(int paramAnonymousInt)
    {
      return new SdpPseRecord[paramAnonymousInt];
    }
  };
  private final int mL2capPsm;
  private final int mProfileVersion;
  private final int mRfcommChannelNumber;
  private final String mServiceName;
  private final int mSupportedFeatures;
  private final int mSupportedRepositories;
  
  public SdpPseRecord(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, String paramString)
  {
    mL2capPsm = paramInt1;
    mRfcommChannelNumber = paramInt2;
    mProfileVersion = paramInt3;
    mSupportedFeatures = paramInt4;
    mSupportedRepositories = paramInt5;
    mServiceName = paramString;
  }
  
  public SdpPseRecord(Parcel paramParcel)
  {
    mRfcommChannelNumber = paramParcel.readInt();
    mL2capPsm = paramParcel.readInt();
    mProfileVersion = paramParcel.readInt();
    mSupportedFeatures = paramParcel.readInt();
    mSupportedRepositories = paramParcel.readInt();
    mServiceName = paramParcel.readString();
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
  
  public int getSupportedRepositories()
  {
    return mSupportedRepositories;
  }
  
  public String toString()
  {
    Object localObject1 = "Bluetooth MNS SDP Record:\n";
    if (mRfcommChannelNumber != -1)
    {
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append("Bluetooth MNS SDP Record:\n");
      ((StringBuilder)localObject1).append("RFCOMM Chan Number: ");
      ((StringBuilder)localObject1).append(mRfcommChannelNumber);
      ((StringBuilder)localObject1).append("\n");
      localObject1 = ((StringBuilder)localObject1).toString();
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
    if (mProfileVersion != -1)
    {
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append((String)localObject2);
      ((StringBuilder)localObject1).append("profile version: ");
      ((StringBuilder)localObject1).append(mProfileVersion);
      ((StringBuilder)localObject1).append("\n");
      localObject1 = ((StringBuilder)localObject1).toString();
    }
    localObject2 = localObject1;
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
    if (mSupportedFeatures != -1)
    {
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append((String)localObject2);
      ((StringBuilder)localObject1).append("Supported features: ");
      ((StringBuilder)localObject1).append(mSupportedFeatures);
      ((StringBuilder)localObject1).append("\n");
      localObject1 = ((StringBuilder)localObject1).toString();
    }
    localObject2 = localObject1;
    if (mSupportedRepositories != -1)
    {
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append((String)localObject1);
      ((StringBuilder)localObject2).append("Supported repositories: ");
      ((StringBuilder)localObject2).append(mSupportedRepositories);
      ((StringBuilder)localObject2).append("\n");
      localObject2 = ((StringBuilder)localObject2).toString();
    }
    return localObject2;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mRfcommChannelNumber);
    paramParcel.writeInt(mL2capPsm);
    paramParcel.writeInt(mProfileVersion);
    paramParcel.writeInt(mSupportedFeatures);
    paramParcel.writeInt(mSupportedRepositories);
    paramParcel.writeString(mServiceName);
  }
}
