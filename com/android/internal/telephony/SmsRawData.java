package com.android.internal.telephony;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class SmsRawData
  implements Parcelable
{
  public static final Parcelable.Creator<SmsRawData> CREATOR = new Parcelable.Creator()
  {
    public SmsRawData createFromParcel(Parcel paramAnonymousParcel)
    {
      byte[] arrayOfByte = new byte[paramAnonymousParcel.readInt()];
      paramAnonymousParcel.readByteArray(arrayOfByte);
      return new SmsRawData(arrayOfByte);
    }
    
    public SmsRawData[] newArray(int paramAnonymousInt)
    {
      return new SmsRawData[paramAnonymousInt];
    }
  };
  byte[] data;
  
  public SmsRawData(byte[] paramArrayOfByte)
  {
    data = paramArrayOfByte;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public byte[] getBytes()
  {
    return data;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(data.length);
    paramParcel.writeByteArray(data);
  }
}
