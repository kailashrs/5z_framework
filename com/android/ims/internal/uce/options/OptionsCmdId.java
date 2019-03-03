package com.android.ims.internal.uce.options;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class OptionsCmdId
  implements Parcelable
{
  public static final Parcelable.Creator<OptionsCmdId> CREATOR = new Parcelable.Creator()
  {
    public OptionsCmdId createFromParcel(Parcel paramAnonymousParcel)
    {
      return new OptionsCmdId(paramAnonymousParcel, null);
    }
    
    public OptionsCmdId[] newArray(int paramAnonymousInt)
    {
      return new OptionsCmdId[paramAnonymousInt];
    }
  };
  public static final int UCE_OPTIONS_CMD_GETCONTACTCAP = 2;
  public static final int UCE_OPTIONS_CMD_GETCONTACTLISTCAP = 3;
  public static final int UCE_OPTIONS_CMD_GETMYCDINFO = 0;
  public static final int UCE_OPTIONS_CMD_GET_VERSION = 5;
  public static final int UCE_OPTIONS_CMD_RESPONSEINCOMINGOPTIONS = 4;
  public static final int UCE_OPTIONS_CMD_SETMYCDINFO = 1;
  public static final int UCE_OPTIONS_CMD_UNKNOWN = 6;
  private int mCmdId = 6;
  
  public OptionsCmdId() {}
  
  private OptionsCmdId(Parcel paramParcel)
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
