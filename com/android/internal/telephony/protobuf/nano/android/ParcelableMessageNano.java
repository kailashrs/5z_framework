package com.android.internal.telephony.protobuf.nano.android;

import android.os.Parcel;
import android.os.Parcelable;
import com.android.internal.telephony.protobuf.nano.MessageNano;

public abstract class ParcelableMessageNano
  extends MessageNano
  implements Parcelable
{
  public ParcelableMessageNano() {}
  
  public int describeContents()
  {
    return 0;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    ParcelableMessageNanoCreator.writeToParcel(getClass(), this, paramParcel);
  }
}
