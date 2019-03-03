package com.android.ims.internal.uce.presence;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.Arrays;

public class PresResInstanceInfo
  implements Parcelable
{
  public static final Parcelable.Creator<PresResInstanceInfo> CREATOR = new Parcelable.Creator()
  {
    public PresResInstanceInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new PresResInstanceInfo(paramAnonymousParcel, null);
    }
    
    public PresResInstanceInfo[] newArray(int paramAnonymousInt)
    {
      return new PresResInstanceInfo[paramAnonymousInt];
    }
  };
  public static final int UCE_PRES_RES_INSTANCE_STATE_ACTIVE = 0;
  public static final int UCE_PRES_RES_INSTANCE_STATE_PENDING = 1;
  public static final int UCE_PRES_RES_INSTANCE_STATE_TERMINATED = 2;
  public static final int UCE_PRES_RES_INSTANCE_STATE_UNKNOWN = 3;
  public static final int UCE_PRES_RES_INSTANCE_UNKNOWN = 4;
  private String mId = "";
  private String mPresentityUri = "";
  private String mReason = "";
  private int mResInstanceState;
  private PresTupleInfo[] mTupleInfoArray;
  
  public PresResInstanceInfo() {}
  
  private PresResInstanceInfo(Parcel paramParcel)
  {
    readFromParcel(paramParcel);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public String getPresentityUri()
  {
    return mPresentityUri;
  }
  
  public String getReason()
  {
    return mReason;
  }
  
  public String getResId()
  {
    return mId;
  }
  
  public int getResInstanceState()
  {
    return mResInstanceState;
  }
  
  public PresTupleInfo[] getTupleInfo()
  {
    return mTupleInfoArray;
  }
  
  public void readFromParcel(Parcel paramParcel)
  {
    mId = paramParcel.readString();
    mReason = paramParcel.readString();
    mResInstanceState = paramParcel.readInt();
    mPresentityUri = paramParcel.readString();
    paramParcel = paramParcel.readParcelableArray(PresTupleInfo.class.getClassLoader());
    mTupleInfoArray = new PresTupleInfo[0];
    if (paramParcel != null) {
      mTupleInfoArray = ((PresTupleInfo[])Arrays.copyOf(paramParcel, paramParcel.length, [Lcom.android.ims.internal.uce.presence.PresTupleInfo.class));
    }
  }
  
  public void setPresentityUri(String paramString)
  {
    mPresentityUri = paramString;
  }
  
  public void setReason(String paramString)
  {
    mReason = paramString;
  }
  
  public void setResId(String paramString)
  {
    mId = paramString;
  }
  
  public void setResInstanceState(int paramInt)
  {
    mResInstanceState = paramInt;
  }
  
  public void setTupleInfo(PresTupleInfo[] paramArrayOfPresTupleInfo)
  {
    mTupleInfoArray = new PresTupleInfo[paramArrayOfPresTupleInfo.length];
    mTupleInfoArray = paramArrayOfPresTupleInfo;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(mId);
    paramParcel.writeString(mReason);
    paramParcel.writeInt(mResInstanceState);
    paramParcel.writeString(mPresentityUri);
    paramParcel.writeParcelableArray(mTupleInfoArray, paramInt);
  }
}
