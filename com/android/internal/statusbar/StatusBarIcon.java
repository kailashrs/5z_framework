package com.android.internal.statusbar;

import android.graphics.drawable.Icon;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.UserHandle;
import android.text.TextUtils;

public class StatusBarIcon
  implements Parcelable
{
  public static final Parcelable.Creator<StatusBarIcon> CREATOR = new Parcelable.Creator()
  {
    public StatusBarIcon createFromParcel(Parcel paramAnonymousParcel)
    {
      return new StatusBarIcon(paramAnonymousParcel);
    }
    
    public StatusBarIcon[] newArray(int paramAnonymousInt)
    {
      return new StatusBarIcon[paramAnonymousInt];
    }
  };
  public CharSequence contentDescription;
  public Icon icon;
  public int iconLevel;
  public int number;
  public String pkg;
  public UserHandle user;
  public boolean visible = true;
  
  public StatusBarIcon(Parcel paramParcel)
  {
    readFromParcel(paramParcel);
  }
  
  public StatusBarIcon(UserHandle paramUserHandle, String paramString, Icon paramIcon, int paramInt1, int paramInt2, CharSequence paramCharSequence)
  {
    Icon localIcon = paramIcon;
    if (paramIcon.getType() == 2)
    {
      localIcon = paramIcon;
      if (TextUtils.isEmpty(paramIcon.getResPackage())) {
        localIcon = Icon.createWithResource(paramString, paramIcon.getResId());
      }
    }
    pkg = paramString;
    user = paramUserHandle;
    icon = localIcon;
    iconLevel = paramInt1;
    number = paramInt2;
    contentDescription = paramCharSequence;
  }
  
  public StatusBarIcon(String paramString, UserHandle paramUserHandle, int paramInt1, int paramInt2, int paramInt3, CharSequence paramCharSequence)
  {
    this(paramUserHandle, paramString, Icon.createWithResource(paramString, paramInt1), paramInt2, paramInt3, paramCharSequence);
  }
  
  public StatusBarIcon clone()
  {
    StatusBarIcon localStatusBarIcon = new StatusBarIcon(user, pkg, icon, iconLevel, number, contentDescription);
    visible = visible;
    return localStatusBarIcon;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void readFromParcel(Parcel paramParcel)
  {
    icon = ((Icon)paramParcel.readParcelable(null));
    pkg = paramParcel.readString();
    user = ((UserHandle)paramParcel.readParcelable(null));
    iconLevel = paramParcel.readInt();
    boolean bool;
    if (paramParcel.readInt() != 0) {
      bool = true;
    } else {
      bool = false;
    }
    visible = bool;
    number = paramParcel.readInt();
    contentDescription = paramParcel.readCharSequence();
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("StatusBarIcon(icon=");
    localStringBuilder.append(icon);
    Object localObject;
    if (iconLevel != 0)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append(" level=");
      ((StringBuilder)localObject).append(iconLevel);
      localObject = ((StringBuilder)localObject).toString();
    }
    else
    {
      localObject = "";
    }
    localStringBuilder.append((String)localObject);
    if (visible) {
      localObject = " visible";
    } else {
      localObject = "";
    }
    localStringBuilder.append((String)localObject);
    localStringBuilder.append(" user=");
    localStringBuilder.append(user.getIdentifier());
    if (number != 0)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append(" num=");
      ((StringBuilder)localObject).append(number);
      localObject = ((StringBuilder)localObject).toString();
    }
    else
    {
      localObject = "";
    }
    localStringBuilder.append((String)localObject);
    localStringBuilder.append(" )");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeParcelable(icon, 0);
    paramParcel.writeString(pkg);
    paramParcel.writeParcelable(user, 0);
    paramParcel.writeInt(iconLevel);
    paramParcel.writeInt(visible);
    paramParcel.writeInt(number);
    paramParcel.writeCharSequence(contentDescription);
  }
}
