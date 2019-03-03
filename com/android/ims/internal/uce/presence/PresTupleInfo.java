package com.android.ims.internal.uce.presence;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class PresTupleInfo
  implements Parcelable
{
  public static final Parcelable.Creator<PresTupleInfo> CREATOR = new Parcelable.Creator()
  {
    public PresTupleInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new PresTupleInfo(paramAnonymousParcel, null);
    }
    
    public PresTupleInfo[] newArray(int paramAnonymousInt)
    {
      return new PresTupleInfo[paramAnonymousInt];
    }
  };
  private String mContactUri = "";
  private String mFeatureTag = "";
  private String mTimestamp = "";
  
  public PresTupleInfo() {}
  
  private PresTupleInfo(Parcel paramParcel)
  {
    readFromParcel(paramParcel);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public String getContactUri()
  {
    return mContactUri;
  }
  
  public String getFeatureTag()
  {
    return mFeatureTag;
  }
  
  public String getTimestamp()
  {
    return mTimestamp;
  }
  
  public void readFromParcel(Parcel paramParcel)
  {
    mFeatureTag = paramParcel.readString();
    mContactUri = paramParcel.readString();
    mTimestamp = paramParcel.readString();
  }
  
  public void setContactUri(String paramString)
  {
    mContactUri = paramString;
  }
  
  public void setFeatureTag(String paramString)
  {
    mFeatureTag = paramString;
  }
  
  public void setTimestamp(String paramString)
  {
    mTimestamp = paramString;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(mFeatureTag);
    paramParcel.writeString(mContactUri);
    paramParcel.writeString(mTimestamp);
  }
}
