package com.android.ims.internal.uce.presence;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class PresResInfo
  implements Parcelable
{
  public static final Parcelable.Creator<PresResInfo> CREATOR = new Parcelable.Creator()
  {
    public PresResInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new PresResInfo(paramAnonymousParcel, null);
    }
    
    public PresResInfo[] newArray(int paramAnonymousInt)
    {
      return new PresResInfo[paramAnonymousInt];
    }
  };
  private String mDisplayName = "";
  private PresResInstanceInfo mInstanceInfo;
  private String mResUri = "";
  
  public PresResInfo()
  {
    mInstanceInfo = new PresResInstanceInfo();
  }
  
  private PresResInfo(Parcel paramParcel)
  {
    readFromParcel(paramParcel);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public String getDisplayName()
  {
    return mDisplayName;
  }
  
  public PresResInstanceInfo getInstanceInfo()
  {
    return mInstanceInfo;
  }
  
  public String getResUri()
  {
    return mResUri;
  }
  
  public void readFromParcel(Parcel paramParcel)
  {
    mResUri = paramParcel.readString();
    mDisplayName = paramParcel.readString();
    mInstanceInfo = ((PresResInstanceInfo)paramParcel.readParcelable(PresResInstanceInfo.class.getClassLoader()));
  }
  
  public void setDisplayName(String paramString)
  {
    mDisplayName = paramString;
  }
  
  public void setInstanceInfo(PresResInstanceInfo paramPresResInstanceInfo)
  {
    mInstanceInfo = paramPresResInstanceInfo;
  }
  
  public void setResUri(String paramString)
  {
    mResUri = paramString;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(mResUri);
    paramParcel.writeString(mDisplayName);
    paramParcel.writeParcelable(mInstanceInfo, paramInt);
  }
}
