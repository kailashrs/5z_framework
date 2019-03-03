package com.android.ims.internal.uce.presence;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class PresServiceInfo
  implements Parcelable
{
  public static final Parcelable.Creator<PresServiceInfo> CREATOR = new Parcelable.Creator()
  {
    public PresServiceInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new PresServiceInfo(paramAnonymousParcel, null);
    }
    
    public PresServiceInfo[] newArray(int paramAnonymousInt)
    {
      return new PresServiceInfo[paramAnonymousInt];
    }
  };
  public static final int UCE_PRES_MEDIA_CAP_FULL_AUDIO_AND_VIDEO = 2;
  public static final int UCE_PRES_MEDIA_CAP_FULL_AUDIO_ONLY = 1;
  public static final int UCE_PRES_MEDIA_CAP_NONE = 0;
  public static final int UCE_PRES_MEDIA_CAP_UNKNOWN = 3;
  private int mMediaCap = 0;
  private String mServiceDesc = "";
  private String mServiceID = "";
  private String mServiceVer = "";
  
  public PresServiceInfo() {}
  
  private PresServiceInfo(Parcel paramParcel)
  {
    readFromParcel(paramParcel);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public int getMediaType()
  {
    return mMediaCap;
  }
  
  public String getServiceDesc()
  {
    return mServiceDesc;
  }
  
  public String getServiceId()
  {
    return mServiceID;
  }
  
  public String getServiceVer()
  {
    return mServiceVer;
  }
  
  public void readFromParcel(Parcel paramParcel)
  {
    mServiceID = paramParcel.readString();
    mServiceDesc = paramParcel.readString();
    mServiceVer = paramParcel.readString();
    mMediaCap = paramParcel.readInt();
  }
  
  public void setMediaType(int paramInt)
  {
    mMediaCap = paramInt;
  }
  
  public void setServiceDesc(String paramString)
  {
    mServiceDesc = paramString;
  }
  
  public void setServiceId(String paramString)
  {
    mServiceID = paramString;
  }
  
  public void setServiceVer(String paramString)
  {
    mServiceVer = paramString;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(mServiceID);
    paramParcel.writeString(mServiceDesc);
    paramParcel.writeString(mServiceVer);
    paramParcel.writeInt(mMediaCap);
  }
}
