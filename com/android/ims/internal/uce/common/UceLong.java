package com.android.ims.internal.uce.common;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class UceLong
  implements Parcelable
{
  public static final Parcelable.Creator<UceLong> CREATOR = new Parcelable.Creator()
  {
    public UceLong createFromParcel(Parcel paramAnonymousParcel)
    {
      return new UceLong(paramAnonymousParcel, null);
    }
    
    public UceLong[] newArray(int paramAnonymousInt)
    {
      return new UceLong[paramAnonymousInt];
    }
  };
  private int mClientId = 1001;
  private long mUceLong;
  
  public UceLong() {}
  
  private UceLong(Parcel paramParcel)
  {
    readFromParcel(paramParcel);
  }
  
  public static UceLong getUceLongInstance()
  {
    return new UceLong();
  }
  
  private void writeToParcel(Parcel paramParcel)
  {
    paramParcel.writeLong(mUceLong);
    paramParcel.writeInt(mClientId);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public int getClientId()
  {
    return mClientId;
  }
  
  public long getUceLong()
  {
    return mUceLong;
  }
  
  public void readFromParcel(Parcel paramParcel)
  {
    mUceLong = paramParcel.readLong();
    mClientId = paramParcel.readInt();
  }
  
  public void setClientId(int paramInt)
  {
    mClientId = paramInt;
  }
  
  public void setUceLong(long paramLong)
  {
    mUceLong = paramLong;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    writeToParcel(paramParcel);
  }
}
