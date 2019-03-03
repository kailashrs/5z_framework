package com.android.ims.internal.uce.common;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class StatusCode
  implements Parcelable
{
  public static final Parcelable.Creator<StatusCode> CREATOR = new Parcelable.Creator()
  {
    public StatusCode createFromParcel(Parcel paramAnonymousParcel)
    {
      return new StatusCode(paramAnonymousParcel, null);
    }
    
    public StatusCode[] newArray(int paramAnonymousInt)
    {
      return new StatusCode[paramAnonymousInt];
    }
  };
  public static final int UCE_FAILURE = 1;
  public static final int UCE_FETCH_ERROR = 6;
  public static final int UCE_INSUFFICIENT_MEMORY = 8;
  public static final int UCE_INVALID_FEATURE_TAG = 15;
  public static final int UCE_INVALID_LISTENER_HANDLE = 4;
  public static final int UCE_INVALID_PARAM = 5;
  public static final int UCE_INVALID_SERVICE_HANDLE = 3;
  public static final int UCE_LOST_NET = 9;
  public static final int UCE_NOT_FOUND = 11;
  public static final int UCE_NOT_SUPPORTED = 10;
  public static final int UCE_NO_CHANGE_IN_CAP = 13;
  public static final int UCE_REQUEST_TIMEOUT = 7;
  public static final int UCE_SERVICE_AVAILABLE = 16;
  public static final int UCE_SERVICE_UNAVAILABLE = 12;
  public static final int UCE_SERVICE_UNKNOWN = 14;
  public static final int UCE_SUCCESS = 0;
  public static final int UCE_SUCCESS_ASYC_UPDATE = 2;
  private int mStatusCode = 0;
  
  public StatusCode() {}
  
  private StatusCode(Parcel paramParcel)
  {
    readFromParcel(paramParcel);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public int getStatusCode()
  {
    return mStatusCode;
  }
  
  public void readFromParcel(Parcel paramParcel)
  {
    mStatusCode = paramParcel.readInt();
  }
  
  public void setStatusCode(int paramInt)
  {
    mStatusCode = paramInt;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mStatusCode);
  }
}
