package com.android.ims.internal.uce.presence;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class PresCmdId
  implements Parcelable
{
  public static final Parcelable.Creator<PresCmdId> CREATOR = new Parcelable.Creator()
  {
    public PresCmdId createFromParcel(Parcel paramAnonymousParcel)
    {
      return new PresCmdId(paramAnonymousParcel, null);
    }
    
    public PresCmdId[] newArray(int paramAnonymousInt)
    {
      return new PresCmdId[paramAnonymousInt];
    }
  };
  public static final int UCE_PRES_CMD_GETCONTACTCAP = 2;
  public static final int UCE_PRES_CMD_GETCONTACTLISTCAP = 3;
  public static final int UCE_PRES_CMD_GET_VERSION = 0;
  public static final int UCE_PRES_CMD_PUBLISHMYCAP = 1;
  public static final int UCE_PRES_CMD_REENABLE_SERVICE = 5;
  public static final int UCE_PRES_CMD_SETNEWFEATURETAG = 4;
  public static final int UCE_PRES_CMD_UNKNOWN = 6;
  private int mCmdId = 6;
  
  public PresCmdId() {}
  
  private PresCmdId(Parcel paramParcel)
  {
    readFromParcel(paramParcel);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public int getCmdId()
  {
    return mCmdId;
  }
  
  public void readFromParcel(Parcel paramParcel)
  {
    mCmdId = paramParcel.readInt();
  }
  
  public void setCmdId(int paramInt)
  {
    mCmdId = paramInt;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mCmdId);
  }
}
