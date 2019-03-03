package com.android.internal.telephony.cat;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class ToneSettings
  implements Parcelable
{
  public static final Parcelable.Creator<ToneSettings> CREATOR = new Parcelable.Creator()
  {
    public ToneSettings createFromParcel(Parcel paramAnonymousParcel)
    {
      return new ToneSettings(paramAnonymousParcel, null);
    }
    
    public ToneSettings[] newArray(int paramAnonymousInt)
    {
      return new ToneSettings[paramAnonymousInt];
    }
  };
  public Duration duration;
  public Tone tone;
  public boolean vibrate;
  
  private ToneSettings(Parcel paramParcel)
  {
    duration = ((Duration)paramParcel.readParcelable(null));
    tone = ((Tone)paramParcel.readParcelable(null));
    int i = paramParcel.readInt();
    boolean bool = true;
    if (i != 1) {
      bool = false;
    }
    vibrate = bool;
  }
  
  public ToneSettings(Duration paramDuration, Tone paramTone, boolean paramBoolean)
  {
    duration = paramDuration;
    tone = paramTone;
    vibrate = paramBoolean;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeParcelable(duration, 0);
    paramParcel.writeParcelable(tone, 0);
    paramParcel.writeInt(vibrate);
  }
}
