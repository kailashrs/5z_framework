package android.hardware.camera2.utils;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class LongParcelable
  implements Parcelable
{
  public static final Parcelable.Creator<LongParcelable> CREATOR = new Parcelable.Creator()
  {
    public LongParcelable createFromParcel(Parcel paramAnonymousParcel)
    {
      return new LongParcelable(paramAnonymousParcel, null);
    }
    
    public LongParcelable[] newArray(int paramAnonymousInt)
    {
      return new LongParcelable[paramAnonymousInt];
    }
  };
  private long number;
  
  public LongParcelable()
  {
    number = 0L;
  }
  
  public LongParcelable(long paramLong)
  {
    number = paramLong;
  }
  
  private LongParcelable(Parcel paramParcel)
  {
    readFromParcel(paramParcel);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public long getNumber()
  {
    return number;
  }
  
  public void readFromParcel(Parcel paramParcel)
  {
    number = paramParcel.readLong();
  }
  
  public void setNumber(long paramLong)
  {
    number = paramLong;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeLong(number);
  }
}
