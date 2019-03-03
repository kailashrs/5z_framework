package android.bluetooth;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class SdpMasRecord
  implements Parcelable
{
  public static final Parcelable.Creator CREATOR = new Parcelable.Creator()
  {
    public SdpMasRecord createFromParcel(Parcel paramAnonymousParcel)
    {
      return new SdpMasRecord(paramAnonymousParcel);
    }
    
    public SdpRecord[] newArray(int paramAnonymousInt)
    {
      return new SdpRecord[paramAnonymousInt];
    }
  };
  private final int mL2capPsm;
  private final int mMasInstanceId;
  private final int mProfileVersion;
  private final int mRfcommChannelNumber;
  private final String mServiceName;
  private final int mSupportedFeatures;
  private final int mSupportedMessageTypes;
  
  public SdpMasRecord(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, String paramString)
  {
    mMasInstanceId = paramInt1;
    mL2capPsm = paramInt2;
    mRfcommChannelNumber = paramInt3;
    mProfileVersion = paramInt4;
    mSupportedFeatures = paramInt5;
    mSupportedMessageTypes = paramInt6;
    mServiceName = paramString;
  }
  
  public SdpMasRecord(Parcel paramParcel)
  {
    mMasInstanceId = paramParcel.readInt();
    mL2capPsm = paramParcel.readInt();
    mRfcommChannelNumber = paramParcel.readInt();
    mProfileVersion = paramParcel.readInt();
    mSupportedFeatures = paramParcel.readInt();
    mSupportedMessageTypes = paramParcel.readInt();
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
  
  public int getMasInstanceId()
  {
    return mMasInstanceId;
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
  
  public int getSupportedFeatures()
  {
    return mSupportedFeatures;
  }
  
  public int getSupportedMessageTypes()
  {
    return mSupportedMessageTypes;
  }
  
  public boolean msgSupported(int paramInt)
  {
    boolean bool;
    if ((mSupportedMessageTypes & paramInt) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public String toString()
  {
    Object localObject1 = "Bluetooth MAS SDP Record:\n";
    if (mMasInstanceId != -1)
    {
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append("Bluetooth MAS SDP Record:\n");
      ((StringBuilder)localObject2).append("Mas Instance Id: ");
      ((StringBuilder)localObject2).append(mMasInstanceId);
      ((StringBuilder)localObject2).append("\n");
      localObject1 = ((StringBuilder)localObject2).toString();
    }
    Object localObject2 = localObject1;
    if (mRfcommChannelNumber != -1)
    {
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append((String)localObject1);
      ((StringBuilder)localObject2).append("RFCOMM Chan Number: ");
      ((StringBuilder)localObject2).append(mRfcommChannelNumber);
      ((StringBuilder)localObject2).append("\n");
      localObject2 = ((StringBuilder)localObject2).toString();
    }
    localObject1 = localObject2;
    if (mL2capPsm != -1)
    {
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append((String)localObject2);
      ((StringBuilder)localObject1).append("L2CAP PSM: ");
      ((StringBuilder)localObject1).append(mL2capPsm);
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
    if (mProfileVersion != -1)
    {
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append((String)localObject2);
      ((StringBuilder)localObject1).append("Profile version: ");
      ((StringBuilder)localObject1).append(mProfileVersion);
      ((StringBuilder)localObject1).append("\n");
      localObject1 = ((StringBuilder)localObject1).toString();
    }
    localObject2 = localObject1;
    if (mSupportedMessageTypes != -1)
    {
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append((String)localObject1);
      ((StringBuilder)localObject2).append("Supported msg types: ");
      ((StringBuilder)localObject2).append(mSupportedMessageTypes);
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
    return localObject1;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mMasInstanceId);
    paramParcel.writeInt(mL2capPsm);
    paramParcel.writeInt(mRfcommChannelNumber);
    paramParcel.writeInt(mProfileVersion);
    paramParcel.writeInt(mSupportedFeatures);
    paramParcel.writeInt(mSupportedMessageTypes);
    paramParcel.writeString(mServiceName);
  }
  
  public static final class MessageType
  {
    public static final int EMAIL = 1;
    public static final int MMS = 8;
    public static final int SMS_CDMA = 4;
    public static final int SMS_GSM = 2;
    
    public MessageType() {}
  }
}
