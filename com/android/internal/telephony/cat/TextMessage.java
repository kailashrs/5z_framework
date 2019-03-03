package com.android.internal.telephony.cat;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class TextMessage
  implements Parcelable
{
  public static final Parcelable.Creator<TextMessage> CREATOR = new Parcelable.Creator()
  {
    public TextMessage createFromParcel(Parcel paramAnonymousParcel)
    {
      return new TextMessage(paramAnonymousParcel, null);
    }
    
    public TextMessage[] newArray(int paramAnonymousInt)
    {
      return new TextMessage[paramAnonymousInt];
    }
  };
  public Duration duration;
  public Bitmap icon = null;
  public boolean iconSelfExplanatory;
  public boolean isHighPriority;
  public boolean responseNeeded;
  public String text = null;
  public String title = "";
  public boolean userClear;
  
  TextMessage()
  {
    iconSelfExplanatory = false;
    isHighPriority = false;
    responseNeeded = true;
    userClear = false;
    duration = null;
  }
  
  private TextMessage(Parcel paramParcel)
  {
    boolean bool1 = false;
    iconSelfExplanatory = false;
    isHighPriority = false;
    responseNeeded = true;
    userClear = false;
    duration = null;
    title = paramParcel.readString();
    text = paramParcel.readString();
    icon = ((Bitmap)paramParcel.readParcelable(null));
    if (paramParcel.readInt() == 1) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    iconSelfExplanatory = bool2;
    if (paramParcel.readInt() == 1) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    isHighPriority = bool2;
    if (paramParcel.readInt() == 1) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    responseNeeded = bool2;
    boolean bool2 = bool1;
    if (paramParcel.readInt() == 1) {
      bool2 = true;
    }
    userClear = bool2;
    duration = ((Duration)paramParcel.readParcelable(null));
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("title=");
    localStringBuilder.append(title);
    localStringBuilder.append(" text=");
    localStringBuilder.append(text);
    localStringBuilder.append(" icon=");
    localStringBuilder.append(icon);
    localStringBuilder.append(" iconSelfExplanatory=");
    localStringBuilder.append(iconSelfExplanatory);
    localStringBuilder.append(" isHighPriority=");
    localStringBuilder.append(isHighPriority);
    localStringBuilder.append(" responseNeeded=");
    localStringBuilder.append(responseNeeded);
    localStringBuilder.append(" userClear=");
    localStringBuilder.append(userClear);
    localStringBuilder.append(" duration=");
    localStringBuilder.append(duration);
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(title);
    paramParcel.writeString(text);
    paramParcel.writeParcelable(icon, 0);
    paramParcel.writeInt(iconSelfExplanatory);
    paramParcel.writeInt(isHighPriority);
    paramParcel.writeInt(responseNeeded);
    paramParcel.writeInt(userClear);
    paramParcel.writeParcelable(duration, 0);
  }
}
