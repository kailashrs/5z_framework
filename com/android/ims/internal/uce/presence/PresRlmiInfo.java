package com.android.ims.internal.uce.presence;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class PresRlmiInfo
  implements Parcelable
{
  public static final Parcelable.Creator<PresRlmiInfo> CREATOR = new Parcelable.Creator()
  {
    public PresRlmiInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new PresRlmiInfo(paramAnonymousParcel, null);
    }
    
    public PresRlmiInfo[] newArray(int paramAnonymousInt)
    {
      return new PresRlmiInfo[paramAnonymousInt];
    }
  };
  private boolean mFullState;
  private String mListName = "";
  private PresSubscriptionState mPresSubscriptionState;
  private int mRequestId;
  private int mSubscriptionExpireTime;
  private String mSubscriptionTerminatedReason;
  private String mUri = "";
  private int mVersion;
  
  public PresRlmiInfo() {}
  
  private PresRlmiInfo(Parcel paramParcel)
  {
    readFromParcel(paramParcel);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public String getListName()
  {
    return mListName;
  }
  
  public PresSubscriptionState getPresSubscriptionState()
  {
    return mPresSubscriptionState;
  }
  
  public int getRequestId()
  {
    return mRequestId;
  }
  
  public int getSubscriptionExpireTime()
  {
    return mSubscriptionExpireTime;
  }
  
  public String getSubscriptionTerminatedReason()
  {
    return mSubscriptionTerminatedReason;
  }
  
  public String getUri()
  {
    return mUri;
  }
  
  public int getVersion()
  {
    return mVersion;
  }
  
  public boolean isFullState()
  {
    return mFullState;
  }
  
  public void readFromParcel(Parcel paramParcel)
  {
    mUri = paramParcel.readString();
    mVersion = paramParcel.readInt();
    boolean bool;
    if (paramParcel.readInt() == 0) {
      bool = false;
    } else {
      bool = true;
    }
    mFullState = bool;
    mListName = paramParcel.readString();
    mRequestId = paramParcel.readInt();
    mPresSubscriptionState = ((PresSubscriptionState)paramParcel.readParcelable(PresSubscriptionState.class.getClassLoader()));
    mSubscriptionExpireTime = paramParcel.readInt();
    mSubscriptionTerminatedReason = paramParcel.readString();
  }
  
  public void setFullState(boolean paramBoolean)
  {
    mFullState = paramBoolean;
  }
  
  public void setListName(String paramString)
  {
    mListName = paramString;
  }
  
  public void setPresSubscriptionState(PresSubscriptionState paramPresSubscriptionState)
  {
    mPresSubscriptionState = paramPresSubscriptionState;
  }
  
  public void setRequestId(int paramInt)
  {
    mRequestId = paramInt;
  }
  
  public void setSubscriptionExpireTime(int paramInt)
  {
    mSubscriptionExpireTime = paramInt;
  }
  
  public void setSubscriptionTerminatedReason(String paramString)
  {
    mSubscriptionTerminatedReason = paramString;
  }
  
  public void setUri(String paramString)
  {
    mUri = paramString;
  }
  
  public void setVersion(int paramInt)
  {
    mVersion = paramInt;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(mUri);
    paramParcel.writeInt(mVersion);
    paramParcel.writeInt(mFullState);
    paramParcel.writeString(mListName);
    paramParcel.writeInt(mRequestId);
    paramParcel.writeParcelable(mPresSubscriptionState, paramInt);
    paramParcel.writeInt(mSubscriptionExpireTime);
    paramParcel.writeString(mSubscriptionTerminatedReason);
  }
}
