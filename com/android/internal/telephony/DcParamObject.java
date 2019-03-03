package com.android.internal.telephony;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class DcParamObject
  implements Parcelable
{
  public static final Parcelable.Creator<DcParamObject> CREATOR = new Parcelable.Creator()
  {
    public DcParamObject createFromParcel(Parcel paramAnonymousParcel)
    {
      return new DcParamObject(paramAnonymousParcel);
    }
    
    public DcParamObject[] newArray(int paramAnonymousInt)
    {
      return new DcParamObject[paramAnonymousInt];
    }
  };
  private int mSubId;
  
  public DcParamObject(int paramInt)
  {
    mSubId = paramInt;
  }
  
  public DcParamObject(Parcel paramParcel)
  {
    readFromParcel(paramParcel);
  }
  
  private void readFromParcel(Parcel paramParcel)
  {
    mSubId = paramParcel.readInt();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public int getSubId()
  {
    return mSubId;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mSubId);
  }
}
