package android.os.health;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class HealthStatsParceler
  implements Parcelable
{
  public static final Parcelable.Creator<HealthStatsParceler> CREATOR = new Parcelable.Creator()
  {
    public HealthStatsParceler createFromParcel(Parcel paramAnonymousParcel)
    {
      return new HealthStatsParceler(paramAnonymousParcel);
    }
    
    public HealthStatsParceler[] newArray(int paramAnonymousInt)
    {
      return new HealthStatsParceler[paramAnonymousInt];
    }
  };
  private HealthStats mHealthStats;
  private HealthStatsWriter mWriter;
  
  public HealthStatsParceler(Parcel paramParcel)
  {
    mHealthStats = new HealthStats(paramParcel);
  }
  
  public HealthStatsParceler(HealthStatsWriter paramHealthStatsWriter)
  {
    mWriter = paramHealthStatsWriter;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public HealthStats getHealthStats()
  {
    if (mWriter != null)
    {
      Parcel localParcel = Parcel.obtain();
      mWriter.flattenToParcel(localParcel);
      localParcel.setDataPosition(0);
      mHealthStats = new HealthStats(localParcel);
      localParcel.recycle();
    }
    return mHealthStats;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    if (mWriter != null)
    {
      mWriter.flattenToParcel(paramParcel);
      return;
    }
    throw new RuntimeException("Can not re-parcel HealthStatsParceler that was constructed from a Parcel");
  }
}
