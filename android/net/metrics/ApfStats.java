package android.net.metrics;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class ApfStats
  implements Parcelable
{
  public static final Parcelable.Creator<ApfStats> CREATOR = new Parcelable.Creator()
  {
    public ApfStats createFromParcel(Parcel paramAnonymousParcel)
    {
      return new ApfStats(paramAnonymousParcel, null);
    }
    
    public ApfStats[] newArray(int paramAnonymousInt)
    {
      return new ApfStats[paramAnonymousInt];
    }
  };
  public int droppedRas;
  public long durationMs;
  public int matchingRas;
  public int maxProgramSize;
  public int parseErrors;
  public int programUpdates;
  public int programUpdatesAll;
  public int programUpdatesAllowingMulticast;
  public int receivedRas;
  public int zeroLifetimeRas;
  
  public ApfStats() {}
  
  private ApfStats(Parcel paramParcel)
  {
    durationMs = paramParcel.readLong();
    receivedRas = paramParcel.readInt();
    matchingRas = paramParcel.readInt();
    droppedRas = paramParcel.readInt();
    zeroLifetimeRas = paramParcel.readInt();
    parseErrors = paramParcel.readInt();
    programUpdates = paramParcel.readInt();
    programUpdatesAll = paramParcel.readInt();
    programUpdatesAllowingMulticast = paramParcel.readInt();
    maxProgramSize = paramParcel.readInt();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder("ApfStats(");
    localStringBuilder.append(String.format("%dms ", new Object[] { Long.valueOf(durationMs) }));
    localStringBuilder.append(String.format("%dB RA: {", new Object[] { Integer.valueOf(maxProgramSize) }));
    localStringBuilder.append(String.format("%d received, ", new Object[] { Integer.valueOf(receivedRas) }));
    localStringBuilder.append(String.format("%d matching, ", new Object[] { Integer.valueOf(matchingRas) }));
    localStringBuilder.append(String.format("%d dropped, ", new Object[] { Integer.valueOf(droppedRas) }));
    localStringBuilder.append(String.format("%d zero lifetime, ", new Object[] { Integer.valueOf(zeroLifetimeRas) }));
    localStringBuilder.append(String.format("%d parse errors}, ", new Object[] { Integer.valueOf(parseErrors) }));
    localStringBuilder.append(String.format("updates: {all: %d, RAs: %d, allow multicast: %d})", new Object[] { Integer.valueOf(programUpdatesAll), Integer.valueOf(programUpdates), Integer.valueOf(programUpdatesAllowingMulticast) }));
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeLong(durationMs);
    paramParcel.writeInt(receivedRas);
    paramParcel.writeInt(matchingRas);
    paramParcel.writeInt(droppedRas);
    paramParcel.writeInt(zeroLifetimeRas);
    paramParcel.writeInt(parseErrors);
    paramParcel.writeInt(programUpdates);
    paramParcel.writeInt(programUpdatesAll);
    paramParcel.writeInt(programUpdatesAllowingMulticast);
    paramParcel.writeInt(maxProgramSize);
  }
}
