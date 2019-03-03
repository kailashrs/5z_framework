package com.android.internal.content;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import java.util.Objects;

public class ReferrerIntent
  extends Intent
{
  public static final Parcelable.Creator<ReferrerIntent> CREATOR = new Parcelable.Creator()
  {
    public ReferrerIntent createFromParcel(Parcel paramAnonymousParcel)
    {
      return new ReferrerIntent(paramAnonymousParcel);
    }
    
    public ReferrerIntent[] newArray(int paramAnonymousInt)
    {
      return new ReferrerIntent[paramAnonymousInt];
    }
  };
  public final String mReferrer;
  
  public ReferrerIntent(Intent paramIntent, String paramString)
  {
    super(paramIntent);
    mReferrer = paramString;
  }
  
  ReferrerIntent(Parcel paramParcel)
  {
    readFromParcel(paramParcel);
    mReferrer = paramParcel.readString();
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool1 = false;
    if ((paramObject != null) && ((paramObject instanceof ReferrerIntent)))
    {
      paramObject = (ReferrerIntent)paramObject;
      boolean bool2 = bool1;
      if (filterEquals(paramObject))
      {
        bool2 = bool1;
        if (Objects.equals(mReferrer, mReferrer)) {
          bool2 = true;
        }
      }
      return bool2;
    }
    return false;
  }
  
  public int hashCode()
  {
    return 31 * (31 * 17 + filterHashCode()) + Objects.hashCode(mReferrer);
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    super.writeToParcel(paramParcel, paramInt);
    paramParcel.writeString(mReferrer);
  }
}
