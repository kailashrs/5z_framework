package android.view;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class WindowContentFrameStats
  extends FrameStats
  implements Parcelable
{
  public static final Parcelable.Creator<WindowContentFrameStats> CREATOR = new Parcelable.Creator()
  {
    public WindowContentFrameStats createFromParcel(Parcel paramAnonymousParcel)
    {
      return new WindowContentFrameStats(paramAnonymousParcel, null);
    }
    
    public WindowContentFrameStats[] newArray(int paramAnonymousInt)
    {
      return new WindowContentFrameStats[paramAnonymousInt];
    }
  };
  private long[] mFramesPostedTimeNano;
  private long[] mFramesReadyTimeNano;
  
  public WindowContentFrameStats() {}
  
  private WindowContentFrameStats(Parcel paramParcel)
  {
    mRefreshPeriodNano = paramParcel.readLong();
    mFramesPostedTimeNano = paramParcel.createLongArray();
    mFramesPresentedTimeNano = paramParcel.createLongArray();
    mFramesReadyTimeNano = paramParcel.createLongArray();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public long getFramePostedTimeNano(int paramInt)
  {
    if (mFramesPostedTimeNano != null) {
      return mFramesPostedTimeNano[paramInt];
    }
    throw new IndexOutOfBoundsException();
  }
  
  public long getFrameReadyTimeNano(int paramInt)
  {
    if (mFramesReadyTimeNano != null) {
      return mFramesReadyTimeNano[paramInt];
    }
    throw new IndexOutOfBoundsException();
  }
  
  public void init(long paramLong, long[] paramArrayOfLong1, long[] paramArrayOfLong2, long[] paramArrayOfLong3)
  {
    mRefreshPeriodNano = paramLong;
    mFramesPostedTimeNano = paramArrayOfLong1;
    mFramesPresentedTimeNano = paramArrayOfLong2;
    mFramesReadyTimeNano = paramArrayOfLong3;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder1 = new StringBuilder();
    localStringBuilder1.append("WindowContentFrameStats[");
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
    paramParcel.writeLongArray(mFramesPostedTimeNano);
    paramParcel.writeLongArray(mFramesPresentedTimeNano);
    paramParcel.writeLongArray(mFramesReadyTimeNano);
  }
}
