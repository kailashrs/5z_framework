package com.android.internal.statusbar;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.ArrayDeque;

public class NotificationVisibility
  implements Parcelable
{
  public static final Parcelable.Creator<NotificationVisibility> CREATOR = new Parcelable.Creator()
  {
    public NotificationVisibility createFromParcel(Parcel paramAnonymousParcel)
    {
      return NotificationVisibility.obtain(paramAnonymousParcel);
    }
    
    public NotificationVisibility[] newArray(int paramAnonymousInt)
    {
      return new NotificationVisibility[paramAnonymousInt];
    }
  };
  private static final int MAX_POOL_SIZE = 25;
  private static final String TAG = "NoViz";
  private static int sNexrId;
  private static ArrayDeque<NotificationVisibility> sPool = new ArrayDeque(25);
  public int count;
  int id;
  public String key;
  public int rank;
  public boolean visible = true;
  
  static
  {
    sNexrId = 0;
  }
  
  private NotificationVisibility()
  {
    int i = sNexrId;
    sNexrId = i + 1;
    id = i;
  }
  
  private NotificationVisibility(String paramString, int paramInt1, int paramInt2, boolean paramBoolean)
  {
    this();
    key = paramString;
    rank = paramInt1;
    count = paramInt2;
  }
  
  private static NotificationVisibility obtain()
  {
    synchronized (sPool)
    {
      if (!sPool.isEmpty())
      {
        NotificationVisibility localNotificationVisibility = (NotificationVisibility)sPool.poll();
        return localNotificationVisibility;
      }
      return new NotificationVisibility();
    }
  }
  
  private static NotificationVisibility obtain(Parcel paramParcel)
  {
    NotificationVisibility localNotificationVisibility = obtain();
    localNotificationVisibility.readFromParcel(paramParcel);
    return localNotificationVisibility;
  }
  
  public static NotificationVisibility obtain(String paramString, int paramInt1, int paramInt2, boolean paramBoolean)
  {
    NotificationVisibility localNotificationVisibility = obtain();
    key = paramString;
    rank = paramInt1;
    count = paramInt2;
    visible = paramBoolean;
    return localNotificationVisibility;
  }
  
  private void readFromParcel(Parcel paramParcel)
  {
    key = paramParcel.readString();
    rank = paramParcel.readInt();
    count = paramParcel.readInt();
    boolean bool;
    if (paramParcel.readInt() != 0) {
      bool = true;
    } else {
      bool = false;
    }
    visible = bool;
  }
  
  public NotificationVisibility clone()
  {
    return obtain(key, rank, count, visible);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool1 = paramObject instanceof NotificationVisibility;
    boolean bool2 = false;
    if (bool1)
    {
      paramObject = (NotificationVisibility)paramObject;
      if (((key == null) && (key == null)) || (key.equals(key))) {
        bool2 = true;
      }
      return bool2;
    }
    return false;
  }
  
  public int hashCode()
  {
    int i;
    if (key == null) {
      i = 0;
    } else {
      i = key.hashCode();
    }
    return i;
  }
  
  public void recycle()
  {
    if (key == null) {
      return;
    }
    key = null;
    if (sPool.size() < 25) {
      synchronized (sPool)
      {
        sPool.offer(this);
      }
    }
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("NotificationVisibility(id=");
    localStringBuilder.append(id);
    localStringBuilder.append(" key=");
    localStringBuilder.append(key);
    localStringBuilder.append(" rank=");
    localStringBuilder.append(rank);
    localStringBuilder.append(" count=");
    localStringBuilder.append(count);
    String str;
    if (visible) {
      str = " visible";
    } else {
      str = "";
    }
    localStringBuilder.append(str);
    localStringBuilder.append(" )");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(key);
    paramParcel.writeInt(rank);
    paramParcel.writeInt(count);
    paramParcel.writeInt(visible);
  }
}
