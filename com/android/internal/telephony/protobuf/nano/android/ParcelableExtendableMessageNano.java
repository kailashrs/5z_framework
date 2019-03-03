package com.android.internal.telephony.protobuf.nano.android;

import android.os.Parcel;
import android.os.Parcelable;
import com.android.internal.telephony.protobuf.nano.ExtendableMessageNano;

public abstract class ParcelableExtendableMessageNano<M extends ExtendableMessageNano<M>>
  extends ExtendableMessageNano<M>
  implements Parcelable
{
  public ParcelableExtendableMessageNano() {}
  
  public int describeContents()
  {
    return 0;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    ParcelableMessageNanoCreator.writeToParcel(getClass(), this, paramParcel);
  }
}
