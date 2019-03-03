package android.bluetooth;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.Arrays;

public class SdpOppOpsRecord
  implements Parcelable
{
  public static final Parcelable.Creator CREATOR = new Parcelable.Creator()
  {
    public SdpOppOpsRecord createFromParcel(Parcel paramAnonymousParcel)
    {
      return new SdpOppOpsRecord(paramAnonymousParcel);
    }
    
    public SdpOppOpsRecord[] newArray(int paramAnonymousInt)
    {
      return new SdpOppOpsRecord[paramAnonymousInt];
    }
  };
  private final byte[] mFormatsList;
  private final int mL2capPsm;
  private final int mProfileVersion;
  private final int mRfcommChannel;
  private final String mServiceName;
  
  public SdpOppOpsRecord(Parcel paramParcel)
  {
    mRfcommChannel = paramParcel.readInt();
    mL2capPsm = paramParcel.readInt();
    mProfileVersion = paramParcel.readInt();
    mServiceName = paramParcel.readString();
    int i = paramParcel.readInt();
    if (i > 0)
    {
      byte[] arrayOfByte = new byte[i];
      paramParcel.readByteArray(arrayOfByte);
      mFormatsList = arrayOfByte;
    }
    else
    {
      mFormatsList = null;
    }
  }
  
  public SdpOppOpsRecord(String paramString, int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte)
  {
    mServiceName = paramString;
    mRfcommChannel = paramInt1;
    mL2capPsm = paramInt2;
    mProfileVersion = paramInt3;
    mFormatsList = paramArrayOfByte;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public byte[] getFormatsList()
  {
    return mFormatsList;
  }
  
  public int getL2capPsm()
  {
    return mL2capPsm;
  }
  
  public int getProfileVersion()
  {
    return mProfileVersion;
  }
  
  public int getRfcommChannel()
  {
    return mRfcommChannel;
  }
  
  public String getServiceName()
  {
    return mServiceName;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder("Bluetooth OPP Server SDP Record:\n");
    localStringBuilder.append("  RFCOMM Chan Number: ");
    localStringBuilder.append(mRfcommChannel);
    localStringBuilder.append("\n  L2CAP PSM: ");
    localStringBuilder.append(mL2capPsm);
    localStringBuilder.append("\n  Profile version: ");
    localStringBuilder.append(mProfileVersion);
    localStringBuilder.append("\n  Service Name: ");
    localStringBuilder.append(mServiceName);
    localStringBuilder.append("\n  Formats List: ");
    localStringBuilder.append(Arrays.toString(mFormatsList));
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mRfcommChannel);
    paramParcel.writeInt(mL2capPsm);
    paramParcel.writeInt(mProfileVersion);
    paramParcel.writeString(mServiceName);
    if ((mFormatsList != null) && (mFormatsList.length > 0))
    {
      paramParcel.writeInt(mFormatsList.length);
      paramParcel.writeByteArray(mFormatsList);
    }
    else
    {
      paramParcel.writeInt(0);
    }
  }
}
