package com.android.internal.telephony.cat;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.ArrayList;
import java.util.List;

public class Menu
  implements Parcelable
{
  public static final Parcelable.Creator<Menu> CREATOR = new Parcelable.Creator()
  {
    public Menu createFromParcel(Parcel paramAnonymousParcel)
    {
      return new Menu(paramAnonymousParcel, null);
    }
    
    public Menu[] newArray(int paramAnonymousInt)
    {
      return new Menu[paramAnonymousInt];
    }
  };
  public int defaultItem;
  public boolean helpAvailable;
  public List<Item> items;
  public boolean itemsIconSelfExplanatory;
  public PresentationType presentationType;
  public boolean softKeyPreferred;
  public String title;
  public List<TextAttribute> titleAttrs;
  public Bitmap titleIcon;
  public boolean titleIconSelfExplanatory;
  
  public Menu()
  {
    items = new ArrayList();
    title = null;
    titleAttrs = null;
    defaultItem = 0;
    softKeyPreferred = false;
    helpAvailable = false;
    titleIconSelfExplanatory = false;
    itemsIconSelfExplanatory = false;
    titleIcon = null;
    presentationType = PresentationType.NAVIGATION_OPTIONS;
  }
  
  private Menu(Parcel paramParcel)
  {
    title = paramParcel.readString();
    titleIcon = ((Bitmap)paramParcel.readParcelable(null));
    items = new ArrayList();
    int i = paramParcel.readInt();
    boolean bool1 = false;
    for (int j = 0; j < i; j++)
    {
      Item localItem = (Item)paramParcel.readParcelable(null);
      items.add(localItem);
    }
    defaultItem = paramParcel.readInt();
    if (paramParcel.readInt() == 1) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    softKeyPreferred = bool2;
    if (paramParcel.readInt() == 1) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    helpAvailable = bool2;
    if (paramParcel.readInt() == 1) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    titleIconSelfExplanatory = bool2;
    boolean bool2 = bool1;
    if (paramParcel.readInt() == 1) {
      bool2 = true;
    }
    itemsIconSelfExplanatory = bool2;
    presentationType = PresentationType.values()[paramParcel.readInt()];
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(title);
    paramParcel.writeParcelable(titleIcon, paramInt);
    int i = items.size();
    paramParcel.writeInt(i);
    for (int j = 0; j < i; j++) {
      paramParcel.writeParcelable((Parcelable)items.get(j), paramInt);
    }
    paramParcel.writeInt(defaultItem);
    paramParcel.writeInt(softKeyPreferred);
    paramParcel.writeInt(helpAvailable);
    paramParcel.writeInt(titleIconSelfExplanatory);
    paramParcel.writeInt(itemsIconSelfExplanatory);
    paramParcel.writeInt(presentationType.ordinal());
  }
}
