package com.android.ims.internal.uce.presence;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.android.ims.internal.uce.common.StatusCode;

public class PresCmdStatus
  implements Parcelable
{
  public static final Parcelable.Creator<PresCmdStatus> CREATOR = new Parcelable.Creator()
  {
    public PresCmdStatus createFromParcel(Parcel paramAnonymousParcel)
    {
      return new PresCmdStatus(paramAnonymousParcel, null);
    }
    
    public PresCmdStatus[] newArray(int paramAnonymousInt)
    {
      return new PresCmdStatus[paramAnonymousInt];
    }
  };
  private PresCmdId mCmdId = new PresCmdId();
  private int mRequestId;
  private StatusCode mStatus = new StatusCode();
  private int mUserData;
  
  public PresCmdStatus()
  {
    mStatus = new StatusCode();
  }
  
  private PresCmdStatus(Parcel paramParcel)
  {
    readFromParcel(paramParcel);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public PresCmdId getCmdId()
  {
    return mCmdId;
  }
  
  public int getRequestId()
  {
    return mRequestId;
  }
  
  public StatusCode getStatus()
  {
    return mStatus;
  }
  
  public int getUserData()
  {
    return mUserData;
  }
  
  public void readFromParcel(Parcel paramParcel)
  {
    mUserData = paramParcel.readInt();
    mRequestId = paramParcel.readInt();
    mCmdId = ((PresCmdId)paramParcel.readParcelable(PresCmdId.class.getClassLoader()));
    mStatus = ((StatusCode)paramParcel.readParcelable(StatusCode.class.getClassLoader()));
  }
  
  public void setCmdId(PresCmdId paramPresCmdId)
  {
    mCmdId = paramPresCmdId;
  }
  
  public void setRequestId(int paramInt)
  {
    mRequestId = paramInt;
  }
  
  public void setStatus(StatusCode paramStatusCode)
  {
    mStatus = paramStatusCode;
  }
  
  public void setUserData(int paramInt)
  {
    mUserData = paramInt;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mUserData);
    paramParcel.writeInt(mRequestId);
    paramParcel.writeParcelable(mCmdId, paramInt);
    paramParcel.writeParcelable(mStatus, paramInt);
  }
}
