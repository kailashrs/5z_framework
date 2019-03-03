package com.android.ims.internal.uce.options;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class OptionsSipResponse
  implements Parcelable
{
  public static final Parcelable.Creator<OptionsSipResponse> CREATOR = new Parcelable.Creator()
  {
    public OptionsSipResponse createFromParcel(Parcel paramAnonymousParcel)
    {
      return new OptionsSipResponse(paramAnonymousParcel, null);
    }
    
    public OptionsSipResponse[] newArray(int paramAnonymousInt)
    {
      return new OptionsSipResponse[paramAnonymousInt];
    }
  };
  private OptionsCmdId mCmdId;
  private String mReasonPhrase = "";
  private int mRequestId = 0;
  private int mRetryAfter = 0;
  private int mSipResponseCode = 0;
  
  public OptionsSipResponse()
  {
    mCmdId = new OptionsCmdId();
  }
  
  private OptionsSipResponse(Parcel paramParcel)
  {
    readFromParcel(paramParcel);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public OptionsCmdId getCmdId()
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
    mCmdId = ((OptionsCmdId)paramParcel.readParcelable(OptionsCmdId.class.getClassLoader()));
    mRetryAfter = paramParcel.readInt();
  }
  
  public void setCmdId(OptionsCmdId paramOptionsCmdId)
  {
    mCmdId = paramOptionsCmdId;
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
