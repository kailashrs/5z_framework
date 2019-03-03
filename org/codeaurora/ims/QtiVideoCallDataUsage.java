package org.codeaurora.ims;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class QtiVideoCallDataUsage
  implements Parcelable
{
  public static final Parcelable.Creator<QtiVideoCallDataUsage> CREATOR = new Parcelable.Creator()
  {
    public QtiVideoCallDataUsage createFromParcel(Parcel paramAnonymousParcel)
    {
      return new QtiVideoCallDataUsage(paramAnonymousParcel);
    }
    
    public QtiVideoCallDataUsage[] newArray(int paramAnonymousInt)
    {
      return new QtiVideoCallDataUsage[paramAnonymousInt];
    }
  };
  public static final int DATA_USAGE_INVALID_VALUE = -1;
  public static final int DATA_USAGE_LTE = 0;
  public static final int DATA_USAGE_WLAN = 1;
  private static final String[] TEXT = { "LteDataUsage = ", " WlanDataUsage = " };
  private long[] mDataUsage;
  
  public QtiVideoCallDataUsage(Parcel paramParcel)
  {
    readFromParcel(paramParcel);
  }
  
  public QtiVideoCallDataUsage(long[] paramArrayOfLong)
  {
    if ((paramArrayOfLong != null) && (paramArrayOfLong.length != 0))
    {
      mDataUsage = paramArrayOfLong;
      return;
    }
    throw new RuntimeException();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public long getLteDataUsage()
  {
    long l;
    if (mDataUsage.length > 0) {
      l = mDataUsage[0];
    } else {
      l = -1L;
    }
    return l;
  }
  
  public long getWlanDataUsage()
  {
    long l;
    if (mDataUsage.length > 1) {
      l = mDataUsage[1];
    } else {
      l = -1L;
    }
    return l;
  }
  
  public void readFromParcel(Parcel paramParcel)
  {
    mDataUsage = paramParcel.createLongArray();
  }
  
  public String toString()
  {
    if (mDataUsage != null)
    {
      String str = "";
      for (int i = 0; i < mDataUsage.length; i++)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append(str);
        localStringBuilder.append(TEXT[i]);
        localStringBuilder.append(mDataUsage[i]);
        str = localStringBuilder.toString();
      }
      return str;
    }
    return null;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeLongArray(mDataUsage);
  }
}
