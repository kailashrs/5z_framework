package android.net;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class UidRange
  implements Parcelable
{
  public static final Parcelable.Creator<UidRange> CREATOR = new Parcelable.Creator()
  {
    public UidRange createFromParcel(Parcel paramAnonymousParcel)
    {
      return new UidRange(paramAnonymousParcel.readInt(), paramAnonymousParcel.readInt());
    }
    
    public UidRange[] newArray(int paramAnonymousInt)
    {
      return new UidRange[paramAnonymousInt];
    }
  };
  public final int start;
  public final int stop;
  
  public UidRange(int paramInt1, int paramInt2)
  {
    if (paramInt1 >= 0)
    {
      if (paramInt2 >= 0)
      {
        if (paramInt1 <= paramInt2)
        {
          start = paramInt1;
          stop = paramInt2;
          return;
        }
        throw new IllegalArgumentException("Invalid UID range.");
      }
      throw new IllegalArgumentException("Invalid stop UID.");
    }
    throw new IllegalArgumentException("Invalid start UID.");
  }
  
  public static UidRange createForUser(int paramInt)
  {
    return new UidRange(paramInt * 100000, (paramInt + 1) * 100000 - 1);
  }
  
  public boolean contains(int paramInt)
  {
    boolean bool;
    if ((start <= paramInt) && (paramInt <= stop)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean containsRange(UidRange paramUidRange)
  {
    boolean bool;
    if ((start <= start) && (stop <= stop)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public int count()
  {
    return 1 + stop - start;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool = true;
    if (this == paramObject) {
      return true;
    }
    if ((paramObject instanceof UidRange))
    {
      paramObject = (UidRange)paramObject;
      if ((start != start) || (stop != stop)) {
        bool = false;
      }
      return bool;
    }
    return false;
  }
  
  public int getStartUser()
  {
    return start / 100000;
  }
  
  public int hashCode()
  {
    return 31 * (31 * 17 + start) + stop;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(start);
    localStringBuilder.append("-");
    localStringBuilder.append(stop);
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(start);
    paramParcel.writeInt(stop);
  }
}
