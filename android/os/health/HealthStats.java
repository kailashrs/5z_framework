package android.os.health;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.ArrayMap;
import java.util.Arrays;
import java.util.Map;

public class HealthStats
{
  private String mDataType;
  private int[] mMeasurementKeys;
  private long[] mMeasurementValues;
  private int[] mMeasurementsKeys;
  private ArrayMap<String, Long>[] mMeasurementsValues;
  private int[] mStatsKeys;
  private ArrayMap<String, HealthStats>[] mStatsValues;
  private int[] mTimerCounts;
  private int[] mTimerKeys;
  private long[] mTimerTimes;
  private int[] mTimersKeys;
  private ArrayMap<String, TimerStat>[] mTimersValues;
  
  private HealthStats()
  {
    throw new RuntimeException("unsupported");
  }
  
  public HealthStats(Parcel paramParcel)
  {
    mDataType = paramParcel.readString();
    int i = paramParcel.readInt();
    mTimerKeys = new int[i];
    mTimerCounts = new int[i];
    mTimerTimes = new long[i];
    int j = 0;
    for (int k = 0; k < i; k++)
    {
      mTimerKeys[k] = paramParcel.readInt();
      mTimerCounts[k] = paramParcel.readInt();
      mTimerTimes[k] = paramParcel.readLong();
    }
    i = paramParcel.readInt();
    mMeasurementKeys = new int[i];
    mMeasurementValues = new long[i];
    for (k = 0; k < i; k++)
    {
      mMeasurementKeys[k] = paramParcel.readInt();
      mMeasurementValues[k] = paramParcel.readLong();
    }
    i = paramParcel.readInt();
    mStatsKeys = new int[i];
    mStatsValues = new ArrayMap[i];
    for (k = 0; k < i; k++)
    {
      mStatsKeys[k] = paramParcel.readInt();
      mStatsValues[k] = createHealthStatsMap(paramParcel);
    }
    i = paramParcel.readInt();
    mTimersKeys = new int[i];
    mTimersValues = new ArrayMap[i];
    for (k = 0; k < i; k++)
    {
      mTimersKeys[k] = paramParcel.readInt();
      mTimersValues[k] = createParcelableMap(paramParcel, TimerStat.CREATOR);
    }
    i = paramParcel.readInt();
    mMeasurementsKeys = new int[i];
    mMeasurementsValues = new ArrayMap[i];
    for (k = j; k < i; k++)
    {
      mMeasurementsKeys[k] = paramParcel.readInt();
      mMeasurementsValues[k] = createLongsMap(paramParcel);
    }
  }
  
  private static ArrayMap<String, HealthStats> createHealthStatsMap(Parcel paramParcel)
  {
    int i = paramParcel.readInt();
    ArrayMap localArrayMap = new ArrayMap(i);
    for (int j = 0; j < i; j++) {
      localArrayMap.put(paramParcel.readString(), new HealthStats(paramParcel));
    }
    return localArrayMap;
  }
  
  private static ArrayMap<String, Long> createLongsMap(Parcel paramParcel)
  {
    int i = paramParcel.readInt();
    ArrayMap localArrayMap = new ArrayMap(i);
    for (int j = 0; j < i; j++) {
      localArrayMap.put(paramParcel.readString(), Long.valueOf(paramParcel.readLong()));
    }
    return localArrayMap;
  }
  
  private static <T extends Parcelable> ArrayMap<String, T> createParcelableMap(Parcel paramParcel, Parcelable.Creator<T> paramCreator)
  {
    int i = paramParcel.readInt();
    ArrayMap localArrayMap = new ArrayMap(i);
    for (int j = 0; j < i; j++) {
      localArrayMap.put(paramParcel.readString(), (Parcelable)paramCreator.createFromParcel(paramParcel));
    }
    return localArrayMap;
  }
  
  private static int getIndex(int[] paramArrayOfInt, int paramInt)
  {
    return Arrays.binarySearch(paramArrayOfInt, paramInt);
  }
  
  public String getDataType()
  {
    return mDataType;
  }
  
  public long getMeasurement(int paramInt)
  {
    int i = getIndex(mMeasurementKeys, paramInt);
    if (i >= 0) {
      return mMeasurementValues[i];
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Bad measurement key dataType=");
    localStringBuilder.append(mDataType);
    localStringBuilder.append(" key=");
    localStringBuilder.append(paramInt);
    throw new IndexOutOfBoundsException(localStringBuilder.toString());
  }
  
  public int getMeasurementKeyAt(int paramInt)
  {
    return mMeasurementKeys[paramInt];
  }
  
  public int getMeasurementKeyCount()
  {
    return mMeasurementKeys.length;
  }
  
  public Map<String, Long> getMeasurements(int paramInt)
  {
    int i = getIndex(mMeasurementsKeys, paramInt);
    if (i >= 0) {
      return mMeasurementsValues[i];
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Bad measurements key dataType=");
    localStringBuilder.append(mDataType);
    localStringBuilder.append(" key=");
    localStringBuilder.append(paramInt);
    throw new IndexOutOfBoundsException(localStringBuilder.toString());
  }
  
  public int getMeasurementsKeyAt(int paramInt)
  {
    return mMeasurementsKeys[paramInt];
  }
  
  public int getMeasurementsKeyCount()
  {
    return mMeasurementsKeys.length;
  }
  
  public Map<String, HealthStats> getStats(int paramInt)
  {
    int i = getIndex(mStatsKeys, paramInt);
    if (i >= 0) {
      return mStatsValues[i];
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Bad stats key dataType=");
    localStringBuilder.append(mDataType);
    localStringBuilder.append(" key=");
    localStringBuilder.append(paramInt);
    throw new IndexOutOfBoundsException(localStringBuilder.toString());
  }
  
  public int getStatsKeyAt(int paramInt)
  {
    return mStatsKeys[paramInt];
  }
  
  public int getStatsKeyCount()
  {
    return mStatsKeys.length;
  }
  
  public TimerStat getTimer(int paramInt)
  {
    int i = getIndex(mTimerKeys, paramInt);
    if (i >= 0) {
      return new TimerStat(mTimerCounts[i], mTimerTimes[i]);
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Bad timer key dataType=");
    localStringBuilder.append(mDataType);
    localStringBuilder.append(" key=");
    localStringBuilder.append(paramInt);
    throw new IndexOutOfBoundsException(localStringBuilder.toString());
  }
  
  public int getTimerCount(int paramInt)
  {
    int i = getIndex(mTimerKeys, paramInt);
    if (i >= 0) {
      return mTimerCounts[i];
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Bad timer key dataType=");
    localStringBuilder.append(mDataType);
    localStringBuilder.append(" key=");
    localStringBuilder.append(paramInt);
    throw new IndexOutOfBoundsException(localStringBuilder.toString());
  }
  
  public int getTimerKeyAt(int paramInt)
  {
    return mTimerKeys[paramInt];
  }
  
  public int getTimerKeyCount()
  {
    return mTimerKeys.length;
  }
  
  public long getTimerTime(int paramInt)
  {
    int i = getIndex(mTimerKeys, paramInt);
    if (i >= 0) {
      return mTimerTimes[i];
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Bad timer key dataType=");
    localStringBuilder.append(mDataType);
    localStringBuilder.append(" key=");
    localStringBuilder.append(paramInt);
    throw new IndexOutOfBoundsException(localStringBuilder.toString());
  }
  
  public Map<String, TimerStat> getTimers(int paramInt)
  {
    int i = getIndex(mTimersKeys, paramInt);
    if (i >= 0) {
      return mTimersValues[i];
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Bad timers key dataType=");
    localStringBuilder.append(mDataType);
    localStringBuilder.append(" key=");
    localStringBuilder.append(paramInt);
    throw new IndexOutOfBoundsException(localStringBuilder.toString());
  }
  
  public int getTimersKeyAt(int paramInt)
  {
    return mTimersKeys[paramInt];
  }
  
  public int getTimersKeyCount()
  {
    return mTimersKeys.length;
  }
  
  public boolean hasMeasurement(int paramInt)
  {
    boolean bool;
    if (getIndex(mMeasurementKeys, paramInt) >= 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean hasMeasurements(int paramInt)
  {
    boolean bool;
    if (getIndex(mMeasurementsKeys, paramInt) >= 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean hasStats(int paramInt)
  {
    boolean bool;
    if (getIndex(mStatsKeys, paramInt) >= 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean hasTimer(int paramInt)
  {
    boolean bool;
    if (getIndex(mTimerKeys, paramInt) >= 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean hasTimers(int paramInt)
  {
    boolean bool;
    if (getIndex(mTimersKeys, paramInt) >= 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
}
