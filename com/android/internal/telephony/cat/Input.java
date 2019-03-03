package com.android.internal.telephony.cat;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class Input
  implements Parcelable
{
  public static final Parcelable.Creator<Input> CREATOR = new Parcelable.Creator()
  {
    public Input createFromParcel(Parcel paramAnonymousParcel)
    {
      return new Input(paramAnonymousParcel, null);
    }
    
    public Input[] newArray(int paramAnonymousInt)
    {
      return new Input[paramAnonymousInt];
    }
  };
  public String defaultText;
  public boolean digitOnly;
  public Duration duration;
  public boolean echo;
  public boolean helpAvailable;
  public Bitmap icon;
  public boolean iconSelfExplanatory;
  public int maxLen;
  public int minLen;
  public boolean packed;
  public String text;
  public boolean ucs2;
  public boolean yesNo;
  
  Input()
  {
    text = "";
    defaultText = null;
    icon = null;
    minLen = 0;
    maxLen = 1;
    ucs2 = false;
    packed = false;
    digitOnly = false;
    echo = false;
    yesNo = false;
    helpAvailable = false;
    duration = null;
    iconSelfExplanatory = false;
  }
  
  private Input(Parcel paramParcel)
  {
    text = paramParcel.readString();
    defaultText = paramParcel.readString();
    icon = ((Bitmap)paramParcel.readParcelable(null));
    minLen = paramParcel.readInt();
    maxLen = paramParcel.readInt();
    int i = paramParcel.readInt();
    boolean bool1 = false;
    if (i == 1) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    ucs2 = bool2;
    if (paramParcel.readInt() == 1) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    packed = bool2;
    if (paramParcel.readInt() == 1) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    digitOnly = bool2;
    if (paramParcel.readInt() == 1) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    echo = bool2;
    if (paramParcel.readInt() == 1) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    yesNo = bool2;
    if (paramParcel.readInt() == 1) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    helpAvailable = bool2;
    duration = ((Duration)paramParcel.readParcelable(null));
    boolean bool2 = bool1;
    if (paramParcel.readInt() == 1) {
      bool2 = true;
    }
    iconSelfExplanatory = bool2;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  boolean setIcon(Bitmap paramBitmap)
  {
    return true;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(text);
    paramParcel.writeString(defaultText);
    paramParcel.writeParcelable(icon, 0);
    paramParcel.writeInt(minLen);
    paramParcel.writeInt(maxLen);
    paramParcel.writeInt(ucs2);
    paramParcel.writeInt(packed);
    paramParcel.writeInt(digitOnly);
    paramParcel.writeInt(echo);
    paramParcel.writeInt(yesNo);
    paramParcel.writeInt(helpAvailable);
    paramParcel.writeParcelable(duration, 0);
    paramParcel.writeInt(iconSelfExplanatory);
  }
}
