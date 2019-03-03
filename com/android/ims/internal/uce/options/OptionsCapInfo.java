package com.android.ims.internal.uce.options;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.android.ims.internal.uce.common.CapInfo;

public class OptionsCapInfo
  implements Parcelable
{
  public static final Parcelable.Creator<OptionsCapInfo> CREATOR = new Parcelable.Creator()
  {
    public OptionsCapInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new OptionsCapInfo(paramAnonymousParcel, null);
    }
    
    public OptionsCapInfo[] newArray(int paramAnonymousInt)
    {
      return new OptionsCapInfo[paramAnonymousInt];
    }
  };
  private CapInfo mCapInfo;
  private String mSdp = "";
  
  public OptionsCapInfo()
  {
    mCapInfo = new CapInfo();
  }
  
  private OptionsCapInfo(Parcel paramParcel)
  {
    readFromParcel(paramParcel);
  }
  
  public static OptionsCapInfo getOptionsCapInfoInstance()
  {
    return new OptionsCapInfo();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public CapInfo getCapInfo()
  {
    return mCapInfo;
  }
  
  public String getSdp()
  {
    return mSdp;
  }
  
  public void readFromParcel(Parcel paramParcel)
  {
    mSdp = paramParcel.readString();
    mCapInfo = ((CapInfo)paramParcel.readParcelable(CapInfo.class.getClassLoader()));
  }
  
  public void setCapInfo(CapInfo paramCapInfo)
  {
    mCapInfo = paramCapInfo;
  }
  
  public void setSdp(String paramString)
  {
    mSdp = paramString;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(mSdp);
    paramParcel.writeParcelable(mCapInfo, paramInt);
  }
}
