package com.android.internal.location;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class ProviderProperties
  implements Parcelable
{
  public static final Parcelable.Creator<ProviderProperties> CREATOR = new Parcelable.Creator()
  {
    public ProviderProperties createFromParcel(Parcel paramAnonymousParcel)
    {
      boolean bool1;
      if (paramAnonymousParcel.readInt() == 1) {
        bool1 = true;
      } else {
        bool1 = false;
      }
      boolean bool2;
      if (paramAnonymousParcel.readInt() == 1) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      boolean bool3;
      if (paramAnonymousParcel.readInt() == 1) {
        bool3 = true;
      } else {
        bool3 = false;
      }
      boolean bool4;
      if (paramAnonymousParcel.readInt() == 1) {
        bool4 = true;
      } else {
        bool4 = false;
      }
      boolean bool5;
      if (paramAnonymousParcel.readInt() == 1) {
        bool5 = true;
      } else {
        bool5 = false;
      }
      boolean bool6;
      if (paramAnonymousParcel.readInt() == 1) {
        bool6 = true;
      } else {
        bool6 = false;
      }
      boolean bool7;
      if (paramAnonymousParcel.readInt() == 1) {
        bool7 = true;
      } else {
        bool7 = false;
      }
      return new ProviderProperties(bool1, bool2, bool3, bool4, bool5, bool6, bool7, paramAnonymousParcel.readInt(), paramAnonymousParcel.readInt());
    }
    
    public ProviderProperties[] newArray(int paramAnonymousInt)
    {
      return new ProviderProperties[paramAnonymousInt];
    }
  };
  public final int mAccuracy;
  public final boolean mHasMonetaryCost;
  public final int mPowerRequirement;
  public final boolean mRequiresCell;
  public final boolean mRequiresNetwork;
  public final boolean mRequiresSatellite;
  public final boolean mSupportsAltitude;
  public final boolean mSupportsBearing;
  public final boolean mSupportsSpeed;
  
  public ProviderProperties(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4, boolean paramBoolean5, boolean paramBoolean6, boolean paramBoolean7, int paramInt1, int paramInt2)
  {
    mRequiresNetwork = paramBoolean1;
    mRequiresSatellite = paramBoolean2;
    mRequiresCell = paramBoolean3;
    mHasMonetaryCost = paramBoolean4;
    mSupportsAltitude = paramBoolean5;
    mSupportsSpeed = paramBoolean6;
    mSupportsBearing = paramBoolean7;
    mPowerRequirement = paramInt1;
    mAccuracy = paramInt2;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mRequiresNetwork);
    paramParcel.writeInt(mRequiresSatellite);
    paramParcel.writeInt(mRequiresCell);
    paramParcel.writeInt(mHasMonetaryCost);
    paramParcel.writeInt(mSupportsAltitude);
    paramParcel.writeInt(mSupportsSpeed);
    paramParcel.writeInt(mSupportsBearing);
    paramParcel.writeInt(mPowerRequirement);
    paramParcel.writeInt(mAccuracy);
  }
}
