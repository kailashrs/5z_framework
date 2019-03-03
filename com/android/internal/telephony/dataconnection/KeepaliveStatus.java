package com.android.internal.telephony.dataconnection;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class KeepaliveStatus
  implements Parcelable
{
  public static final Parcelable.Creator<KeepaliveStatus> CREATOR = new Parcelable.Creator()
  {
    public KeepaliveStatus createFromParcel(Parcel paramAnonymousParcel)
    {
      return new KeepaliveStatus(paramAnonymousParcel, null);
    }
    
    public KeepaliveStatus[] newArray(int paramAnonymousInt)
    {
      return new KeepaliveStatus[paramAnonymousInt];
    }
  };
  public static final int ERROR_NONE = 0;
  public static final int ERROR_NO_RESOURCES = 2;
  public static final int ERROR_UNKNOWN = 3;
  public static final int ERROR_UNSUPPORTED = 1;
  public static final int INVALID_HANDLE = Integer.MAX_VALUE;
  private static final String LOG_TAG = "KeepaliveStatus";
  public static final int STATUS_ACTIVE = 0;
  public static final int STATUS_INACTIVE = 1;
  public static final int STATUS_PENDING = 2;
  public final int errorCode;
  public final int sessionHandle;
  public final int statusCode;
  
  public KeepaliveStatus(int paramInt)
  {
    sessionHandle = Integer.MAX_VALUE;
    statusCode = 1;
    errorCode = paramInt;
  }
  
  public KeepaliveStatus(int paramInt1, int paramInt2)
  {
    sessionHandle = paramInt1;
    statusCode = paramInt2;
    errorCode = 0;
  }
  
  private KeepaliveStatus(Parcel paramParcel)
  {
    errorCode = paramParcel.readInt();
    sessionHandle = paramParcel.readInt();
    statusCode = paramParcel.readInt();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public String toString()
  {
    return String.format("{errorCode=%d, sessionHandle=%d, statusCode=%d}", new Object[] { Integer.valueOf(errorCode), Integer.valueOf(sessionHandle), Integer.valueOf(statusCode) });
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(errorCode);
    paramParcel.writeInt(sessionHandle);
    paramParcel.writeInt(statusCode);
  }
}
