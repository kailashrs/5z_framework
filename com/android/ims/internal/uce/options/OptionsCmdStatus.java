package com.android.ims.internal.uce.options;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.android.ims.internal.uce.common.CapInfo;
import com.android.ims.internal.uce.common.StatusCode;

public class OptionsCmdStatus
  implements Parcelable
{
  public static final Parcelable.Creator<OptionsCmdStatus> CREATOR = new Parcelable.Creator()
  {
    public OptionsCmdStatus createFromParcel(Parcel paramAnonymousParcel)
    {
      return new OptionsCmdStatus(paramAnonymousParcel, null);
    }
    
    public OptionsCmdStatus[] newArray(int paramAnonymousInt)
    {
      return new OptionsCmdStatus[paramAnonymousInt];
    }
  };
  private CapInfo mCapInfo;
  private OptionsCmdId mCmdId;
  private StatusCode mStatus;
  private int mUserData;
  
  public OptionsCmdStatus()
  {
    mStatus = new StatusCode();
    mCapInfo = new CapInfo();
    mCmdId = new OptionsCmdId();
    mUserData = 0;
  }
  
  private OptionsCmdStatus(Parcel paramParcel)
  {
    readFromParcel(paramParcel);
  }
  
  public static OptionsCmdStatus getOptionsCmdStatusInstance()
  {
    return new OptionsCmdStatus();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public CapInfo getCapInfo()
  {
    return mCapInfo;
  }
  
  public OptionsCmdId getCmdId()
  {
    return mCmdId;
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
    mCmdId = ((OptionsCmdId)paramParcel.readParcelable(OptionsCmdId.class.getClassLoader()));
    mStatus = ((StatusCode)paramParcel.readParcelable(StatusCode.class.getClassLoader()));
    mCapInfo = ((CapInfo)paramParcel.readParcelable(CapInfo.class.getClassLoader()));
  }
  
  public void setCapInfo(CapInfo paramCapInfo)
  {
    mCapInfo = paramCapInfo;
  }
  
  public void setCmdId(OptionsCmdId paramOptionsCmdId)
  {
    mCmdId = paramOptionsCmdId;
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
    paramParcel.writeParcelable(mCmdId, paramInt);
    paramParcel.writeParcelable(mStatus, paramInt);
    paramParcel.writeParcelable(mCapInfo, paramInt);
  }
}
