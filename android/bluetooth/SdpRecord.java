package android.bluetooth;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.Arrays;

public class SdpRecord
  implements Parcelable
{
  public static final Parcelable.Creator CREATOR = new Parcelable.Creator()
  {
    public SdpRecord createFromParcel(Parcel paramAnonymousParcel)
    {
      return new SdpRecord(paramAnonymousParcel);
    }
    
    public SdpRecord[] newArray(int paramAnonymousInt)
    {
      return new SdpRecord[paramAnonymousInt];
    }
  };
  private final byte[] mRawData;
  private final int mRawSize;
  
  public SdpRecord(int paramInt, byte[] paramArrayOfByte)
  {
    mRawData = paramArrayOfByte;
    mRawSize = paramInt;
  }
  
  public SdpRecord(Parcel paramParcel)
  {
    mRawSize = paramParcel.readInt();
    mRawData = new byte[mRawSize];
    paramParcel.readByteArray(mRawData);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public byte[] getRawData()
  {
    return mRawData;
  }
  
  public int getRawSize()
  {
    return mRawSize;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("BluetoothSdpRecord [rawData=");
    localStringBuilder.append(Arrays.toString(mRawData));
    localStringBuilder.append(", rawSize=");
    localStringBuilder.append(mRawSize);
    localStringBuilder.append("]");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mRawSize);
    paramParcel.writeByteArray(mRawData);
  }
}
