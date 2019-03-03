package android.os;

public class BatteryProperty
  implements Parcelable
{
  public static final Parcelable.Creator<BatteryProperty> CREATOR = new Parcelable.Creator()
  {
    public BatteryProperty createFromParcel(Parcel paramAnonymousParcel)
    {
      return new BatteryProperty(paramAnonymousParcel, null);
    }
    
    public BatteryProperty[] newArray(int paramAnonymousInt)
    {
      return new BatteryProperty[paramAnonymousInt];
    }
  };
  private long mValueLong;
  
  public BatteryProperty()
  {
    mValueLong = Long.MIN_VALUE;
  }
  
  private BatteryProperty(Parcel paramParcel)
  {
    readFromParcel(paramParcel);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public long getLong()
  {
    return mValueLong;
  }
  
  public void readFromParcel(Parcel paramParcel)
  {
    mValueLong = paramParcel.readLong();
  }
  
  public void setLong(long paramLong)
  {
    mValueLong = paramLong;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeLong(mValueLong);
  }
}
