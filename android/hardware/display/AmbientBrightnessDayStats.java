package android.hardware.display;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.android.internal.util.Preconditions;
import java.time.LocalDate;
import java.util.Arrays;

@SystemApi
public final class AmbientBrightnessDayStats
  implements Parcelable
{
  public static final Parcelable.Creator<AmbientBrightnessDayStats> CREATOR = new Parcelable.Creator()
  {
    public AmbientBrightnessDayStats createFromParcel(Parcel paramAnonymousParcel)
    {
      return new AmbientBrightnessDayStats(paramAnonymousParcel, null);
    }
    
    public AmbientBrightnessDayStats[] newArray(int paramAnonymousInt)
    {
      return new AmbientBrightnessDayStats[paramAnonymousInt];
    }
  };
  private final float[] mBucketBoundaries;
  private final LocalDate mLocalDate;
  private final float[] mStats;
  
  private AmbientBrightnessDayStats(Parcel paramParcel)
  {
    mLocalDate = LocalDate.parse(paramParcel.readString());
    mBucketBoundaries = paramParcel.createFloatArray();
    mStats = paramParcel.createFloatArray();
  }
  
  public AmbientBrightnessDayStats(LocalDate paramLocalDate, float[] paramArrayOfFloat)
  {
    this(paramLocalDate, paramArrayOfFloat, null);
  }
  
  public AmbientBrightnessDayStats(LocalDate paramLocalDate, float[] paramArrayOfFloat1, float[] paramArrayOfFloat2)
  {
    Preconditions.checkNotNull(paramLocalDate);
    Preconditions.checkNotNull(paramArrayOfFloat1);
    Preconditions.checkArrayElementsInRange(paramArrayOfFloat1, 0.0F, Float.MAX_VALUE, "bucketBoundaries");
    if (paramArrayOfFloat1.length >= 1)
    {
      checkSorted(paramArrayOfFloat1);
      if (paramArrayOfFloat2 == null)
      {
        paramArrayOfFloat2 = new float[paramArrayOfFloat1.length];
      }
      else
      {
        Preconditions.checkArrayElementsInRange(paramArrayOfFloat2, 0.0F, Float.MAX_VALUE, "stats");
        if (paramArrayOfFloat1.length != paramArrayOfFloat2.length) {
          break label79;
        }
      }
      mLocalDate = paramLocalDate;
      mBucketBoundaries = paramArrayOfFloat1;
      mStats = paramArrayOfFloat2;
      return;
      label79:
      throw new IllegalArgumentException("Bucket boundaries and stats must be of same size.");
    }
    throw new IllegalArgumentException("Bucket boundaries must contain at least 1 value");
  }
  
  private static void checkSorted(float[] paramArrayOfFloat)
  {
    if (paramArrayOfFloat.length <= 1) {
      return;
    }
    float f = paramArrayOfFloat[0];
    for (int i = 1; i < paramArrayOfFloat.length; i++)
    {
      boolean bool;
      if (f < paramArrayOfFloat[i]) {
        bool = true;
      } else {
        bool = false;
      }
      Preconditions.checkState(bool);
      f = paramArrayOfFloat[i];
    }
  }
  
  private int getBucketIndex(float paramFloat)
  {
    if (paramFloat < mBucketBoundaries[0]) {
      return -1;
    }
    int i = 0;
    int j = mBucketBoundaries.length - 1;
    while (i < j)
    {
      int k = (i + j) / 2;
      if ((mBucketBoundaries[k] <= paramFloat) && (paramFloat < mBucketBoundaries[(k + 1)])) {
        return k;
      }
      int m;
      if (mBucketBoundaries[k] < paramFloat)
      {
        m = k + 1;
      }
      else
      {
        m = i;
        if (mBucketBoundaries[k] > paramFloat)
        {
          j = k - 1;
          m = i;
        }
      }
      i = m;
    }
    return i;
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
    if (paramObject == null) {
      return false;
    }
    if (getClass() != paramObject.getClass()) {
      return false;
    }
    paramObject = (AmbientBrightnessDayStats)paramObject;
    if ((!mLocalDate.equals(mLocalDate)) || (!Arrays.equals(mBucketBoundaries, mBucketBoundaries)) || (!Arrays.equals(mStats, mStats))) {
      bool = false;
    }
    return bool;
  }
  
  public float[] getBucketBoundaries()
  {
    return mBucketBoundaries;
  }
  
  public LocalDate getLocalDate()
  {
    return mLocalDate;
  }
  
  public float[] getStats()
  {
    return mStats;
  }
  
  public int hashCode()
  {
    return ((1 * 31 + mLocalDate.hashCode()) * 31 + Arrays.hashCode(mBucketBoundaries)) * 31 + Arrays.hashCode(mStats);
  }
  
  public void log(float paramFloat1, float paramFloat2)
  {
    int i = getBucketIndex(paramFloat1);
    if (i >= 0)
    {
      float[] arrayOfFloat = mStats;
      arrayOfFloat[i] += paramFloat2;
    }
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder1 = new StringBuilder();
    StringBuilder localStringBuilder2 = new StringBuilder();
    for (int i = 0; i < mBucketBoundaries.length; i++)
    {
      if (i != 0)
      {
        localStringBuilder1.append(", ");
        localStringBuilder2.append(", ");
      }
      localStringBuilder1.append(mBucketBoundaries[i]);
      localStringBuilder2.append(mStats[i]);
    }
    StringBuilder localStringBuilder3 = new StringBuilder();
    localStringBuilder3.append(mLocalDate);
    localStringBuilder3.append(" ");
    localStringBuilder3.append("{");
    localStringBuilder3.append(localStringBuilder1);
    localStringBuilder3.append("} ");
    localStringBuilder3.append("{");
    localStringBuilder3.append(localStringBuilder2);
    localStringBuilder3.append("}");
    return localStringBuilder3.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(mLocalDate.toString());
    paramParcel.writeFloatArray(mBucketBoundaries);
    paramParcel.writeFloatArray(mStats);
  }
}
