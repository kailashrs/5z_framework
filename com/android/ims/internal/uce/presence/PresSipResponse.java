package com.android.ims.internal.uce.presence;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class PresSipResponse
  implements Parcelable
{
  public static final Parcelable.Creator<PresSipResponse> CREATOR = new Parcelable.Creator()
  {
    public PresSipResponse createFromParcel(Parcel paramAnonymousParcel)
    {
      return new PresSipResponse(paramAnonymousParcel, null);
    }
    
    public PresSipResponse[] newArray(int paramAnonymousInt)
    {
      return new PresSipResponse[paramAnonymousInt];
    }
  };
  private PresCmdId mCmdId = new PresCmdId();
  private String mReasonPhrase = "";
  private int mRequestId = 0;
  private int mRetryAfter = 0;
  private int mSipResponseCode = 0;
  
  public PresSipResponse() {}
  
  private PresSipResponse(Parcel paramParcel)
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
  
  public String getReasonPhrase()
  {
    return mReasonPhrase;
  }
  
  public int getRequestId()
  {
    return mRequestId;
  }
  
  public int getRetryAfter()
  {
    return mRetryAfter;
  }
  
  public int getSipResponseCode()
  {
    return mSipResponseCode;
  }
  
  public void readFromParcel(Parcel paramParcel)
  {
    mRequestId = paramParcel.readInt();
    mSipResponseCode = paramParcel.readInt();
    mReasonPhrase = paramParcel.readString();
    mCmdId = ((PresCmdId)paramParcel.readParcelable(PresCmdId.class.getClassLoader()));
    mRetryAfter = paramParcel.readInt();
  }
  
  public void setCmdId(PresCmdId paramPresCmdId)
  {
    mCmdId = paramPresCmdId;
  }
  
  public void setReasonPhrase(String paramString)
  {
    mReasonPhrase = paramString;
  }
  
  public void setRequestId(int paramInt)
  {
    mRequestId = paramInt;
  }
  
  public void setRetryAfter(int paramInt)
  {
    mRetryAfter = paramInt;
  }
  
  public void setSipResponseCode(int paramInt)
  {
    mSipResponseCode = paramInt;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mRequestId);
    paramParcel.writeInt(mSipResponseCode);
    paramParcel.writeString(mReasonPhrase);
    paramParcel.writeParcelable(mCmdId, paramInt);
    paramParcel.writeInt(mRetryAfter);
  }
}
