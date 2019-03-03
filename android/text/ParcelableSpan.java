package android.text;

import android.os.Parcel;
import android.os.Parcelable;

public abstract interface ParcelableSpan
  extends Parcelable
{
  public abstract int getSpanTypeId();
  
  public abstract int getSpanTypeIdInternal();
  
  public abstract void writeToParcelInternal(Parcel paramParcel, int paramInt);
}
