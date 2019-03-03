package com.android.ims.internal.uce.presence;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.android.ims.internal.uce.common.CapInfo;

public class PresCapInfo
  implements Parcelable
{
  public static final Parcelable.Creator<PresCapInfo> CREATOR = new Parcelable.Creator()
  {
    public PresCapInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new PresCapInfo(paramAnonymousParcel, null);
    }
    
    public PresCapInfo[] newArray(int paramAnonymousInt)
    {
      return new PresCapInfo[paramAnonymousInt];
    }
  };
  private CapInfo mCapInfo;
  private String mContactUri = "";
  
  public PresCapInfo()
  {
    mCapInfo = new CapInfo();
  }
  
  private PresCapInfo(Parcel paramParcel)
  {
    readFromParcel(paramParcel);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public CapInfo getCapInfo()
  {
    return mCapInfo;
  }
  
  public String getContactUri()
  {
    return mContactUri;
  }
  
  public void readFromParcel(Parcel paramParcel)
  {
    mContactUri = paramParcel.readString();
    mCapInfo = ((CapInfo)paramParcel.readParcelable(CapInfo.class.getClassLoader()));
  }
  
  public void setCapInfo(CapInfo paramCapInfo)
  {
    mCapInfo = paramCapInfo;
  }
  
  public void setContactUri(String paramString)
  {
    mContactUri = paramString;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(mContactUri);
    paramParcel.writeParcelable(mCapInfo, paramInt);
  }
}
