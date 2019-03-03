package com.android.internal.telephony;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class CallInfo
  implements Parcelable
{
  public static final Parcelable.Creator<CallInfo> CREATOR = new Parcelable.Creator()
  {
    public CallInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new CallInfo(paramAnonymousParcel.readString());
    }
    
    public CallInfo[] newArray(int paramAnonymousInt)
    {
      return new CallInfo[paramAnonymousInt];
    }
  };
  private String handle;
  
  public CallInfo(String paramString)
  {
    handle = paramString;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public String getHandle()
  {
    return handle;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(handle);
  }
}
