package android.content;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class SyncResult
  implements Parcelable
{
  public static final SyncResult ALREADY_IN_PROGRESS = new SyncResult(true);
  public static final Parcelable.Creator<SyncResult> CREATOR = new Parcelable.Creator()
  {
    public SyncResult createFromParcel(Parcel paramAnonymousParcel)
    {
      return new SyncResult(paramAnonymousParcel, null);
    }
    
    public SyncResult[] newArray(int paramAnonymousInt)
    {
      return new SyncResult[paramAnonymousInt];
    }
  };
  public boolean databaseError;
  public long delayUntil;
  public boolean fullSyncRequested;
  public boolean moreRecordsToGet;
  public boolean partialSyncUnavailable;
  public final SyncStats stats;
  public final boolean syncAlreadyInProgress;
  public boolean tooManyDeletions;
  public boolean tooManyRetries;
  
  public SyncResult()
  {
    this(false);
  }
  
  private SyncResult(Parcel paramParcel)
  {
    int i = paramParcel.readInt();
    boolean bool1 = false;
    if (i != 0) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    syncAlreadyInProgress = bool2;
    if (paramParcel.readInt() != 0) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    tooManyDeletions = bool2;
    if (paramParcel.readInt() != 0) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    tooManyRetries = bool2;
    if (paramParcel.readInt() != 0) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    databaseError = bool2;
    if (paramParcel.readInt() != 0) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    fullSyncRequested = bool2;
    if (paramParcel.readInt() != 0) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    partialSyncUnavailable = bool2;
    boolean bool2 = bool1;
    if (paramParcel.readInt() != 0) {
      bool2 = true;
    }
    moreRecordsToGet = bool2;
    delayUntil = paramParcel.readLong();
    stats = new SyncStats(paramParcel);
  }
  
  private SyncResult(boolean paramBoolean)
  {
    syncAlreadyInProgress = paramBoolean;
    tooManyDeletions = false;
    tooManyRetries = false;
    fullSyncRequested = false;
    partialSyncUnavailable = false;
    moreRecordsToGet = false;
    delayUntil = 0L;
    stats = new SyncStats();
  }
  
  public void clear()
  {
    if (!syncAlreadyInProgress)
    {
      tooManyDeletions = false;
      tooManyRetries = false;
      databaseError = false;
      fullSyncRequested = false;
      partialSyncUnavailable = false;
      moreRecordsToGet = false;
      delayUntil = 0L;
      stats.clear();
      return;
    }
    throw new UnsupportedOperationException("you are not allowed to clear the ALREADY_IN_PROGRESS SyncStats");
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean hasError()
  {
    boolean bool;
    if ((!hasSoftError()) && (!hasHardError())) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public boolean hasHardError()
  {
    boolean bool;
    if ((stats.numParseExceptions <= 0L) && (stats.numConflictDetectedExceptions <= 0L) && (stats.numAuthExceptions <= 0L) && (!tooManyDeletions) && (!tooManyRetries) && (!databaseError)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public boolean hasSoftError()
  {
    boolean bool;
    if ((!syncAlreadyInProgress) && (stats.numIoExceptions <= 0L)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public boolean madeSomeProgress()
  {
    boolean bool;
    if (((stats.numDeletes <= 0L) || (tooManyDeletions)) && (stats.numInserts <= 0L) && (stats.numUpdates <= 0L)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public String toDebugString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    if (fullSyncRequested) {
      localStringBuffer.append("f1");
    }
    if (partialSyncUnavailable) {
      localStringBuffer.append("r1");
    }
    if (hasHardError()) {
      localStringBuffer.append("X1");
    }
    if (stats.numParseExceptions > 0L)
    {
      localStringBuffer.append("e");
      localStringBuffer.append(stats.numParseExceptions);
    }
    if (stats.numConflictDetectedExceptions > 0L)
    {
      localStringBuffer.append("c");
      localStringBuffer.append(stats.numConflictDetectedExceptions);
    }
    if (stats.numAuthExceptions > 0L)
    {
      localStringBuffer.append("a");
      localStringBuffer.append(stats.numAuthExceptions);
    }
    if (tooManyDeletions) {
      localStringBuffer.append("D1");
    }
    if (tooManyRetries) {
      localStringBuffer.append("R1");
    }
    if (databaseError) {
      localStringBuffer.append("b1");
    }
    if (hasSoftError()) {
      localStringBuffer.append("x1");
    }
    if (syncAlreadyInProgress) {
      localStringBuffer.append("l1");
    }
    if (stats.numIoExceptions > 0L)
    {
      localStringBuffer.append("I");
      localStringBuffer.append(stats.numIoExceptions);
    }
    return localStringBuffer.toString();
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("SyncResult:");
    if (syncAlreadyInProgress)
    {
      localStringBuilder.append(" syncAlreadyInProgress: ");
      localStringBuilder.append(syncAlreadyInProgress);
    }
    if (tooManyDeletions)
    {
      localStringBuilder.append(" tooManyDeletions: ");
      localStringBuilder.append(tooManyDeletions);
    }
    if (tooManyRetries)
    {
      localStringBuilder.append(" tooManyRetries: ");
      localStringBuilder.append(tooManyRetries);
    }
    if (databaseError)
    {
      localStringBuilder.append(" databaseError: ");
      localStringBuilder.append(databaseError);
    }
    if (fullSyncRequested)
    {
      localStringBuilder.append(" fullSyncRequested: ");
      localStringBuilder.append(fullSyncRequested);
    }
    if (partialSyncUnavailable)
    {
      localStringBuilder.append(" partialSyncUnavailable: ");
      localStringBuilder.append(partialSyncUnavailable);
    }
    if (moreRecordsToGet)
    {
      localStringBuilder.append(" moreRecordsToGet: ");
      localStringBuilder.append(moreRecordsToGet);
    }
    if (delayUntil > 0L)
    {
      localStringBuilder.append(" delayUntil: ");
      localStringBuilder.append(delayUntil);
    }
    localStringBuilder.append(stats);
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(syncAlreadyInProgress);
    paramParcel.writeInt(tooManyDeletions);
    paramParcel.writeInt(tooManyRetries);
    paramParcel.writeInt(databaseError);
    paramParcel.writeInt(fullSyncRequested);
    paramParcel.writeInt(partialSyncUnavailable);
    paramParcel.writeInt(moreRecordsToGet);
    paramParcel.writeLong(delayUntil);
    stats.writeToParcel(paramParcel, paramInt);
  }
}
