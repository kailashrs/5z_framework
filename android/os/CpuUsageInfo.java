package android.os;

public final class CpuUsageInfo
  implements Parcelable
{
  public static final Parcelable.Creator<CpuUsageInfo> CREATOR = new Parcelable.Creator()
  {
    public CpuUsageInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new CpuUsageInfo(paramAnonymousParcel, null);
    }
    
    public CpuUsageInfo[] newArray(int paramAnonymousInt)
    {
      return new CpuUsageInfo[paramAnonymousInt];
    }
  };
  private long mActive;
  private long mTotal;
  
  public CpuUsageInfo(long paramLong1, long paramLong2)
  {
    mActive = paramLong1;
    mTotal = paramLong2;
  }
  
  private CpuUsageInfo(Parcel paramParcel)
  {
    readFromParcel(paramParcel);
  }
  
  private void readFromParcel(Parcel paramParcel)
  {
    mActive = paramParcel.readLong();
    mTotal = paramParcel.readLong();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public long getActive()
  {
    return mActive;
  }
  
  public long getTotal()
  {
    return mTotal;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeLong(mActive);
    paramParcel.writeLong(mTotal);
  }
}
