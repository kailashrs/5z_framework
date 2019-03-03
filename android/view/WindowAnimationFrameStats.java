package android.view;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class WindowAnimationFrameStats
  extends FrameStats
  implements Parcelable
{
  public static final Parcelable.Creator<WindowAnimationFrameStats> CREATOR = new Parcelable.Creator()
  {
    public WindowAnimationFrameStats createFromParcel(Parcel paramAnonymousParcel)
    {
      return new WindowAnimationFrameStats(paramAnonymousParcel, null);
    }
    
    public WindowAnimationFrameStats[] newArray(int paramAnonymousInt)
    {
      return new WindowAnimationFrameStats[paramAnonymousInt];
    }
  };
  
  public WindowAnimationFrameStats() {}
  
  private WindowAnimationFrameStats(Parcel paramParcel)
  {
    mRefreshPeriodNano = paramParcel.readLong();
    mFramesPresentedTimeNano = paramParcel.createLongArray();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void init(long paramLong, long[] paramArrayOfLong)
  {
    mRefreshPeriodNano = paramLong;
    mFramesPresentedTimeNano = paramArrayOfLong;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder1 = new StringBuilder();
    localStringBuilder1.append("WindowAnimationFrameStats[");
    StringBuilder localStringBuilder2 = new StringBuilder();
    localStringBuilder2.append("frameCount:");
    localStringBuilder2.append(getFrameCount());
    localStringBuilder1.append(localStringBuilder2.toString());
    localStringBuilder2 = new StringBuilder();
    localStringBuilder2.append(", fromTimeNano:");
    localStringBuilder2.append(getStartTimeNano());
    localStringBuilder1.append(localStringBuilder2.toString());
    localStringBuilder2 = new StringBuilder();
    localStringBuilder2.append(", toTimeNano:");
    localStringBuilder2.append(getEndTimeNano());
    localStringBuilder1.append(localStringBuilder2.toString());
    localStringBuilder1.append(']');
    return localStringBuilder1.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeLong(mRefreshPeriodNano);
    paramParcel.writeLongArray(mFramesPresentedTimeNano);
  }
}
