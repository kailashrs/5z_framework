package android.content;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class SyncStats
  implements Parcelable
{
  public static final Parcelable.Creator<SyncStats> CREATOR = new Parcelable.Creator()
  {
    public SyncStats createFromParcel(Parcel paramAnonymousParcel)
    {
      return new SyncStats(paramAnonymousParcel);
    }
    
    public SyncStats[] newArray(int paramAnonymousInt)
    {
      return new SyncStats[paramAnonymousInt];
    }
  };
  public long numAuthExceptions;
  public long numConflictDetectedExceptions;
  public long numDeletes;
  public long numEntries;
  public long numInserts;
  public long numIoExceptions;
  public long numParseExceptions;
  public long numSkippedEntries;
  public long numUpdates;
  
  public SyncStats()
  {
    numAuthExceptions = 0L;
    numIoExceptions = 0L;
    numParseExceptions = 0L;
    numConflictDetectedExceptions = 0L;
    numInserts = 0L;
    numUpdates = 0L;
    numDeletes = 0L;
    numEntries = 0L;
    numSkippedEntries = 0L;
  }
  
  public SyncStats(Parcel paramParcel)
  {
    numAuthExceptions = paramParcel.readLong();
    numIoExceptions = paramParcel.readLong();
    numParseExceptions = paramParcel.readLong();
    numConflictDetectedExceptions = paramParcel.readLong();
    numInserts = paramParcel.readLong();
    numUpdates = paramParcel.readLong();
    numDeletes = paramParcel.readLong();
    numEntries = paramParcel.readLong();
    numSkippedEntries = paramParcel.readLong();
  }
  
  public void clear()
  {
    numAuthExceptions = 0L;
    numIoExceptions = 0L;
    numParseExceptions = 0L;
    numConflictDetectedExceptions = 0L;
    numInserts = 0L;
    numUpdates = 0L;
    numDeletes = 0L;
    numEntries = 0L;
    numSkippedEntries = 0L;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(" stats [");
    if (numAuthExceptions > 0L)
    {
      localStringBuilder.append(" numAuthExceptions: ");
      localStringBuilder.append(numAuthExceptions);
    }
    if (numIoExceptions > 0L)
    {
      localStringBuilder.append(" numIoExceptions: ");
      localStringBuilder.append(numIoExceptions);
    }
    if (numParseExceptions > 0L)
    {
      localStringBuilder.append(" numParseExceptions: ");
      localStringBuilder.append(numParseExceptions);
    }
    if (numConflictDetectedExceptions > 0L)
    {
      localStringBuilder.append(" numConflictDetectedExceptions: ");
      localStringBuilder.append(numConflictDetectedExceptions);
    }
    if (numInserts > 0L)
    {
      localStringBuilder.append(" numInserts: ");
      localStringBuilder.append(numInserts);
    }
    if (numUpdates > 0L)
    {
      localStringBuilder.append(" numUpdates: ");
      localStringBuilder.append(numUpdates);
    }
    if (numDeletes > 0L)
    {
      localStringBuilder.append(" numDeletes: ");
      localStringBuilder.append(numDeletes);
    }
    if (numEntries > 0L)
    {
      localStringBuilder.append(" numEntries: ");
      localStringBuilder.append(numEntries);
    }
    if (numSkippedEntries > 0L)
    {
      localStringBuilder.append(" numSkippedEntries: ");
      localStringBuilder.append(numSkippedEntries);
    }
    localStringBuilder.append("]");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeLong(numAuthExceptions);
    paramParcel.writeLong(numIoExceptions);
    paramParcel.writeLong(numParseExceptions);
    paramParcel.writeLong(numConflictDetectedExceptions);
    paramParcel.writeLong(numInserts);
    paramParcel.writeLong(numUpdates);
    paramParcel.writeLong(numDeletes);
    paramParcel.writeLong(numEntries);
    paramParcel.writeLong(numSkippedEntries);
  }
}
