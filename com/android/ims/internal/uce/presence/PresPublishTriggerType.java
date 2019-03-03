package com.android.ims.internal.uce.presence;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class PresPublishTriggerType
  implements Parcelable
{
  public static final Parcelable.Creator<PresPublishTriggerType> CREATOR = new Parcelable.Creator()
  {
    public PresPublishTriggerType createFromParcel(Parcel paramAnonymousParcel)
    {
      return new PresPublishTriggerType(paramAnonymousParcel, null);
    }
    
    public PresPublishTriggerType[] newArray(int paramAnonymousInt)
    {
      return new PresPublishTriggerType[paramAnonymousInt];
    }
  };
  public static final int UCE_PRES_PUBLISH_TRIGGER_ETAG_EXPIRED = 0;
  public static final int UCE_PRES_PUBLISH_TRIGGER_MOVE_TO_2G = 6;
  public static final int UCE_PRES_PUBLISH_TRIGGER_MOVE_TO_3G = 5;
  public static final int UCE_PRES_PUBLISH_TRIGGER_MOVE_TO_EHRPD = 3;
  public static final int UCE_PRES_PUBLISH_TRIGGER_MOVE_TO_HSPAPLUS = 4;
  public static final int UCE_PRES_PUBLISH_TRIGGER_MOVE_TO_IWLAN = 8;
  public static final int UCE_PRES_PUBLISH_TRIGGER_MOVE_TO_LTE_VOPS_DISABLED = 1;
  public static final int UCE_PRES_PUBLISH_TRIGGER_MOVE_TO_LTE_VOPS_ENABLED = 2;
  public static final int UCE_PRES_PUBLISH_TRIGGER_MOVE_TO_WLAN = 7;
  public static final int UCE_PRES_PUBLISH_TRIGGER_UNKNOWN = 9;
  private int mPublishTriggerType = 9;
  
  public PresPublishTriggerType() {}
  
  private PresPublishTriggerType(Parcel paramParcel)
  {
    readFromParcel(paramParcel);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public int getPublishTrigeerType()
  {
    return mPublishTriggerType;
  }
  
  public void readFromParcel(Parcel paramParcel)
  {
    mPublishTriggerType = paramParcel.readInt();
  }
  
  public void setPublishTrigeerType(int paramInt)
  {
    mPublishTriggerType = paramInt;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mPublishTriggerType);
  }
}
