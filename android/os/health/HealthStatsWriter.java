package android.os.health;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.ArrayMap;

public class HealthStatsWriter
{
  private final HealthKeys.Constants mConstants;
  private final boolean[] mMeasurementFields;
  private final long[] mMeasurementValues;
  private final ArrayMap<String, Long>[] mMeasurementsValues;
  private final ArrayMap<String, HealthStatsWriter>[] mStatsValues;
  private final int[] mTimerCounts;
  private final boolean[] mTimerFields;
  private final long[] mTimerTimes;
  private final ArrayMap<String, TimerStat>[] mTimersValues;
  
  public HealthStatsWriter(HealthKeys.Constants paramConstants)
  {
    mConstants = paramConstants;
    int i = paramConstants.getSize(0);
    mTimerFields = new boolean[i];
    mTimerCounts = new int[i];
    mTimerTimes = new long[i];
    i = paramConstants.getSize(1);
    mMeasurementFields = new boolean[i];
    mMeasurementValues = new long[i];
    mStatsValues = new ArrayMap[paramConstants.getSize(2)];
    mTimersValues = new ArrayMap[paramConstants.getSize(3)];
    mMeasurementsValues = new ArrayMap[paramConstants.getSize(4)];
  }
  
  private static int countBooleanArray(boolean[] paramArrayOfBoolean)
  {
    int i = 0;
    int j = paramArrayOfBoolean.length;
    int k = 0;
    while (k < j)
    {
      int m = i;
      if (paramArrayOfBoolean[k] != 0) {
        m = i + 1;
      }
      k++;
      i = m;
    }
    return i;
  }
  
  private static <T> int countObjectArray(T[] paramArrayOfT)
  {
    int i = 0;
    int j = paramArrayOfT.length;
    int k = 0;
    while (k < j)
    {
      int m = i;
      if (paramArrayOfT[k] != null) {
        m = i + 1;
      }
      k++;
      i = m;
    }
    return i;
  }
  
  private static void writeHealthStatsWriterMap(Parcel paramParcel, ArrayMap<String, HealthStatsWriter> paramArrayMap)
  {
    int i = paramArrayMap.size();
    paramParcel.writeInt(i);
    for (int j = 0; j < i; j++)
    {
      paramParcel.writeString((String)paramArrayMap.keyAt(j));
      ((HealthStatsWriter)paramArrayMap.valueAt(j)).flattenToParcel(paramParcel);
    }
  }
  
  private static void writeLongsMap(Parcel paramParcel, ArrayMap<String, Long> paramArrayMap)
  {
    int i = paramArrayMap.size();
    paramParcel.writeInt(i);
    for (int j = 0; j < i; j++)
    {
      paramParcel.writeString((String)paramArrayMap.keyAt(j));
      paramParcel.writeLong(((Long)paramArrayMap.valueAt(j)).longValue());
    }
  }
  
  private static <T extends Parcelable> void writeParcelableMap(Parcel paramParcel, ArrayMap<String, T> paramArrayMap)
  {
    int i = paramArrayMap.size();
    paramParcel.writeInt(i);
    for (int j = 0; j < i; j++)
    {
      paramParcel.writeString((String)paramArrayMap.keyAt(j));
      ((Parcelable)paramArrayMap.valueAt(j)).writeToParcel(paramParcel, 0);
    }
  }
  
  public void addMeasurement(int paramInt, long paramLong)
  {
    paramInt = mConstants.getIndex(1, paramInt);
    mMeasurementFields[paramInt] = true;
    mMeasurementValues[paramInt] = paramLong;
  }
  
  public void addMeasurements(int paramInt, String paramString, long paramLong)
  {
    paramInt = mConstants.getIndex(4, paramInt);
    Object localObject1 = mMeasurementsValues[paramInt];
    Object localObject2 = localObject1;
    if (localObject1 == null)
    {
      localObject1 = mMeasurementsValues;
      localObject2 = new ArrayMap(1);
      localObject1[paramInt] = localObject2;
    }
    ((ArrayMap)localObject2).put(paramString, Long.valueOf(paramLong));
  }
  
  public void addStats(int paramInt, String paramString, HealthStatsWriter paramHealthStatsWriter)
  {
    paramInt = mConstants.getIndex(2, paramInt);
    Object localObject1 = mStatsValues[paramInt];
    Object localObject2 = localObject1;
    if (localObject1 == null)
    {
      localObject1 = mStatsValues;
      localObject2 = new ArrayMap(1);
      localObject1[paramInt] = localObject2;
    }
    ((ArrayMap)localObject2).put(paramString, paramHealthStatsWriter);
  }
  
  public void addTimer(int paramInt1, int paramInt2, long paramLong)
  {
    paramInt1 = mConstants.getIndex(0, paramInt1);
    mTimerFields[paramInt1] = true;
    mTimerCounts[paramInt1] = paramInt2;
    mTimerTimes[paramInt1] = paramLong;
  }
  
  public void addTimers(int paramInt, String paramString, TimerStat paramTimerStat)
  {
    paramInt = mConstants.getIndex(3, paramInt);
    Object localObject1 = mTimersValues[paramInt];
    Object localObject2 = localObject1;
    if (localObject1 == null)
    {
      localObject1 = mTimersValues;
      localObject2 = new ArrayMap(1);
      localObject1[paramInt] = localObject2;
    }
    ((ArrayMap)localObject2).put(paramString, paramTimerStat);
  }
  
  public void flattenToParcel(Parcel paramParcel)
  {
    paramParcel.writeString(mConstants.getDataType());
    paramParcel.writeInt(countBooleanArray(mTimerFields));
    Object localObject = mConstants;
    int i = 0;
    localObject = ((HealthKeys.Constants)localObject).getKeys(0);
    for (int j = 0; j < localObject.length; j++) {
      if (mTimerFields[j] != 0)
      {
        paramParcel.writeInt(localObject[j]);
        paramParcel.writeInt(mTimerCounts[j]);
        paramParcel.writeLong(mTimerTimes[j]);
      }
    }
    paramParcel.writeInt(countBooleanArray(mMeasurementFields));
    localObject = mConstants.getKeys(1);
    for (j = 0; j < localObject.length; j++) {
      if (mMeasurementFields[j] != 0)
      {
        paramParcel.writeInt(localObject[j]);
        paramParcel.writeLong(mMeasurementValues[j]);
      }
    }
    paramParcel.writeInt(countObjectArray(mStatsValues));
    localObject = mConstants.getKeys(2);
    for (j = 0; j < localObject.length; j++) {
      if (mStatsValues[j] != null)
      {
        paramParcel.writeInt(localObject[j]);
        writeHealthStatsWriterMap(paramParcel, mStatsValues[j]);
      }
    }
    paramParcel.writeInt(countObjectArray(mTimersValues));
    localObject = mConstants.getKeys(3);
    for (j = 0; j < localObject.length; j++) {
      if (mTimersValues[j] != null)
      {
        paramParcel.writeInt(localObject[j]);
        writeParcelableMap(paramParcel, mTimersValues[j]);
      }
    }
    paramParcel.writeInt(countObjectArray(mMeasurementsValues));
    localObject = mConstants.getKeys(4);
    for (j = i; j < localObject.length; j++) {
      if (mMeasurementsValues[j] != null)
      {
        paramParcel.writeInt(localObject[j]);
        writeLongsMap(paramParcel, mMeasurementsValues[j]);
      }
    }
  }
}
