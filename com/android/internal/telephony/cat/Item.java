package com.android.internal.telephony.cat;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class Item
  implements Parcelable
{
  public static final Parcelable.Creator<Item> CREATOR = new Parcelable.Creator()
  {
    public Item createFromParcel(Parcel paramAnonymousParcel)
    {
      return new Item(paramAnonymousParcel);
    }
    
    public Item[] newArray(int paramAnonymousInt)
    {
      return new Item[paramAnonymousInt];
    }
  };
  public Bitmap icon;
  public int id;
  public String text;
  
  public Item(int paramInt, String paramString)
  {
    this(paramInt, paramString, null);
  }
  
  public Item(int paramInt, String paramString, Bitmap paramBitmap)
  {
    id = paramInt;
    text = paramString;
    icon = paramBitmap;
  }
  
  public Item(Parcel paramParcel)
  {
    id = paramParcel.readInt();
    text = paramParcel.readString();
    icon = ((Bitmap)paramParcel.readParcelable(null));
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public String toString()
  {
    return text;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(id);
    paramParcel.writeString(text);
    paramParcel.writeParcelable(icon, paramInt);
  }
}
