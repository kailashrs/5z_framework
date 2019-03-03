package android.telephony;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.Arrays;

@SystemApi
public final class TelephonyHistogram
  implements Parcelable
{
  private static final int ABSENT = 0;
  public static final Parcelable.Creator<TelephonyHistogram> CREATOR = new Parcelable.Creator()
  {
    public TelephonyHistogram createFromParcel(Parcel paramAnonymousParcel)
    {
      return new TelephonyHistogram(paramAnonymousParcel);
    }
    
    public TelephonyHistogram[] newArray(int paramAnonymousInt)
    {
      return new TelephonyHistogram[paramAnonymousInt];
    }
  };
  private static final int PRESENT = 1;
  private static final int RANGE_CALCULATION_COUNT = 10;
  public static final int TELEPHONY_CATEGORY_RIL = 1;
  private int mAverageTimeMs;
  private final int mBucketCount;
  private final int[] mBucketCounters;
  private final int[] mBucketEndPoints;
  private final int mCategory;
  private final int mId;
  private int[] mInitialTimings;
  private int mMaxTimeMs;
  private int mMinTimeMs;
  private int mSampleCount;
  
  public TelephonyHistogram(int paramInt1, int paramInt2, int paramInt3)
  {
    if (paramInt3 > 1)
    {
      mCategory = paramInt1;
      mId = paramInt2;
      mMinTimeMs = Integer.MAX_VALUE;
      mMaxTimeMs = 0;
      mAverageTimeMs = 0;
      mSampleCount = 0;
      mInitialTimings = new int[10];
      mBucketCount = paramInt3;
      mBucketEndPoints = new int[paramInt3 - 1];
      mBucketCounters = new int[paramInt3];
      return;
    }
    throw new IllegalArgumentException("Invalid number of buckets");
  }
  
  public TelephonyHistogram(Parcel paramParcel)
  {
    mCategory = paramParcel.readInt();
    mId = paramParcel.readInt();
    mMinTimeMs = paramParcel.readInt();
    mMaxTimeMs = paramParcel.readInt();
    mAverageTimeMs = paramParcel.readInt();
    mSampleCount = paramParcel.readInt();
    if (paramParcel.readInt() == 1)
    {
      mInitialTimings = new int[10];
      paramParcel.readIntArray(mInitialTimings);
    }
    mBucketCount = paramParcel.readInt();
    mBucketEndPoints = new int[mBucketCount - 1];
    paramParcel.readIntArray(mBucketEndPoints);
    mBucketCounters = new int[mBucketCount];
    paramParcel.readIntArray(mBucketCounters);
  }
  
  public TelephonyHistogram(TelephonyHistogram paramTelephonyHistogram)
  {
    mCategory = paramTelephonyHistogram.getCategory();
    mId = paramTelephonyHistogram.getId();
    mMinTimeMs = paramTelephonyHistogram.getMinTime();
    mMaxTimeMs = paramTelephonyHistogram.getMaxTime();
    mAverageTimeMs = paramTelephonyHistogram.getAverageTime();
    mSampleCount = paramTelephonyHistogram.getSampleCount();
    mInitialTimings = paramTelephonyHistogram.getInitialTimings();
    mBucketCount = paramTelephonyHistogram.getBucketCount();
    mBucketEndPoints = paramTelephonyHistogram.getBucketEndPoints();
    mBucketCounters = paramTelephonyHistogram.getBucketCounters();
  }
  
  private void addToBucketCounter(int[] paramArrayOfInt1, int[] paramArrayOfInt2, int paramInt)
  {
    for (int i = 0; i < paramArrayOfInt1.length; i++) {
      if (paramInt <= paramArrayOfInt1[i])
      {
        paramArrayOfInt2[i] += 1;
        return;
      }
    }
    paramArrayOfInt2[i] += 1;
  }
  
  private void calculateBucketEndPoints(int[] paramArrayOfInt)
  {
    for (int i = 1; i < mBucketCount; i++) {
      paramArrayOfInt[(i - 1)] = (mMinTimeMs + (mMaxTimeMs - mMinTimeMs) * i / mBucketCount);
    }
  }
  
  private int[] getDeepCopyOfArray(int[] paramArrayOfInt)
  {
    int[] arrayOfInt = new int[paramArrayOfInt.length];
    System.arraycopy(paramArrayOfInt, 0, arrayOfInt, 0, paramArrayOfInt.length);
    return arrayOfInt;
  }
  
  private int[] getInitialTimings()
  {
    return mInitialTimings;
  }
  
  public void addTimeTaken(int paramInt)
  {
    int i = mSampleCount;
    int j = 0;
    if ((i != 0) && (mSampleCount != Integer.MAX_VALUE))
    {
      if (paramInt < mMinTimeMs) {
        mMinTimeMs = paramInt;
      }
      if (paramInt > mMaxTimeMs) {
        mMaxTimeMs = paramInt;
      }
      long l1 = mAverageTimeMs;
      long l2 = mSampleCount;
      long l3 = paramInt;
      i = mSampleCount + 1;
      mSampleCount = i;
      mAverageTimeMs = ((int)((l1 * l2 + l3) / i));
      if (mSampleCount < 10)
      {
        mInitialTimings[(mSampleCount - 1)] = paramInt;
      }
      else if (mSampleCount == 10)
      {
        mInitialTimings[(mSampleCount - 1)] = paramInt;
        calculateBucketEndPoints(mBucketEndPoints);
        for (paramInt = j; paramInt < 10; paramInt++) {
          addToBucketCounter(mBucketEndPoints, mBucketCounters, mInitialTimings[paramInt]);
        }
        mInitialTimings = null;
      }
      else
      {
        addToBucketCounter(mBucketEndPoints, mBucketCounters, paramInt);
      }
    }
    else
    {
      if (mSampleCount == 0)
      {
        mMinTimeMs = paramInt;
        mMaxTimeMs = paramInt;
        mAverageTimeMs = paramInt;
      }
      else
      {
        mInitialTimings = new int[10];
      }
      mSampleCount = 1;
      Arrays.fill(mInitialTimings, 0);
      mInitialTimings[0] = paramInt;
      Arrays.fill(mBucketEndPoints, 0);
      Arrays.fill(mBucketCounters, 0);
    }
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public int getAverageTime()
  {
    return mAverageTimeMs;
  }
  
  public int getBucketCount()
  {
    return mBucketCount;
  }
  
  public int[] getBucketCounters()
  {
    if ((mSampleCount > 1) && (mSampleCount < 10))
    {
      int[] arrayOfInt1 = new int[mBucketCount - 1];
      int[] arrayOfInt2 = new int[mBucketCount];
      calculateBucketEndPoints(arrayOfInt1);
      for (int i = 0; i < mSampleCount; i++) {
        addToBucketCounter(arrayOfInt1, arrayOfInt2, mInitialTimings[i]);
      }
      return arrayOfInt2;
    }
    return getDeepCopyOfArray(mBucketCounters);
  }
  
  public int[] getBucketEndPoints()
  {
    if ((mSampleCount > 1) && (mSampleCount < 10))
    {
      int[] arrayOfInt = new int[mBucketCount - 1];
      calculateBucketEndPoints(arrayOfInt);
      return arrayOfInt;
    }
    return getDeepCopyOfArray(mBucketEndPoints);
  }
  
  public int getCategory()
  {
    return mCategory;
  }
  
  public int getId()
  {
    return mId;
  }
  
  public int getMaxTime()
  {
    return mMaxTimeMs;
  }
  
  public int getMinTime()
  {
    return mMinTimeMs;
  }
  
  public int getSampleCount()
  {
    return mSampleCount;
  }
  
  public String toString()
  {
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append(" Histogram id = ");
    ((StringBuilder)localObject).append(mId);
    ((StringBuilder)localObject).append(" Time(ms): min = ");
    ((StringBuilder)localObject).append(mMinTimeMs);
    ((StringBuilder)localObject).append(" max = ");
    ((StringBuilder)localObject).append(mMaxTimeMs);
    ((StringBuilder)localObject).append(" avg = ");
    ((StringBuilder)localObject).append(mAverageTimeMs);
    ((StringBuilder)localObject).append(" Count = ");
    ((StringBuilder)localObject).append(mSampleCount);
    String str = ((StringBuilder)localObject).toString();
    if (mSampleCount < 10) {
      return str;
    }
    localObject = new StringBuffer(" Interval Endpoints:");
    int i = 0;
    for (int j = 0; j < mBucketEndPoints.length; j++)
    {
      localStringBuilder = new StringBuilder();
      localStringBuilder.append(" ");
      localStringBuilder.append(mBucketEndPoints[j]);
      ((StringBuffer)localObject).append(localStringBuilder.toString());
    }
    ((StringBuffer)localObject).append(" Interval counters:");
    for (j = i; j < mBucketCounters.length; j++)
    {
      localStringBuilder = new StringBuilder();
      localStringBuilder.append(" ");
      localStringBuilder.append(mBucketCounters[j]);
      ((StringBuffer)localObject).append(localStringBuilder.toString());
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(str);
    localStringBuilder.append(localObject);
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mCategory);
    paramParcel.writeInt(mId);
    paramParcel.writeInt(mMinTimeMs);
    paramParcel.writeInt(mMaxTimeMs);
    paramParcel.writeInt(mAverageTimeMs);
    paramParcel.writeInt(mSampleCount);
    if (mInitialTimings == null)
    {
      paramParcel.writeInt(0);
    }
    else
    {
      paramParcel.writeInt(1);
      paramParcel.writeIntArray(mInitialTimings);
    }
    paramParcel.writeInt(mBucketCount);
    paramParcel.writeIntArray(mBucketEndPoints);
    paramParcel.writeIntArray(mBucketCounters);
  }
}
