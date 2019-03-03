package com.android.ims.internal.uce.presence;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class PresSubscriptionState
  implements Parcelable
{
  public static final Parcelable.Creator<PresSubscriptionState> CREATOR = new Parcelable.Creator()
  {
    public PresSubscriptionState createFromParcel(Parcel paramAnonymousParcel)
    {
      return new PresSubscriptionState(paramAnonymousParcel, null);
    }
    
    public PresSubscriptionState[] newArray(int paramAnonymousInt)
    {
      return new PresSubscriptionState[paramAnonymousInt];
    }
  };
  public static final int UCE_PRES_SUBSCRIPTION_STATE_ACTIVE = 0;
  public static final int UCE_PRES_SUBSCRIPTION_STATE_PENDING = 1;
  public static final int UCE_PRES_SUBSCRIPTION_STATE_TERMINATED = 2;
  public static final int UCE_PRES_SUBSCRIPTION_STATE_UNKNOWN = 3;
  private int mPresSubscriptionState = 3;
  
  public PresSubscriptionState() {}
  
  private PresSubscriptionState(Parcel paramParcel)
  {
    readFromParcel(paramParcel);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public int getPresSubscriptionStateValue()
  {
    return mPresSubscriptionState;
  }
  
  public void readFromParcel(Parcel paramParcel)
  {
    mPresSubscriptionState = paramParcel.readInt();
  }
  
  public void setPresSubscriptionState(int paramInt)
  {
    mPresSubscriptionState = paramInt;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mPresSubscriptionState);
  }
}
