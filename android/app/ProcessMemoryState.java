package android.app;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class ProcessMemoryState
  implements Parcelable
{
  public static final Parcelable.Creator<ProcessMemoryState> CREATOR = new Parcelable.Creator()
  {
    public ProcessMemoryState createFromParcel(Parcel paramAnonymousParcel)
    {
      return new ProcessMemoryState(paramAnonymousParcel, null);
    }
    
    public ProcessMemoryState[] newArray(int paramAnonymousInt)
    {
      return new ProcessMemoryState[paramAnonymousInt];
    }
  };
  public long cacheInBytes;
  public int oomScore;
  public long pgfault;
  public long pgmajfault;
  public String processName;
  public long rssInBytes;
  public long swapInBytes;
  public int uid;
  
  public ProcessMemoryState(int paramInt1, String paramString, int paramInt2, long paramLong1, long paramLong2, long paramLong3, long paramLong4, long paramLong5)
  {
    uid = paramInt1;
    processName = paramString;
    oomScore = paramInt2;
    pgfault = paramLong1;
    pgmajfault = paramLong2;
    rssInBytes = paramLong3;
    cacheInBytes = paramLong4;
    swapInBytes = paramLong5;
  }
  
  private ProcessMemoryState(Parcel paramParcel)
  {
    uid = paramParcel.readInt();
    processName = paramParcel.readString();
    oomScore = paramParcel.readInt();
    pgfault = paramParcel.readLong();
    pgmajfault = paramParcel.readLong();
    rssInBytes = paramParcel.readLong();
    cacheInBytes = paramParcel.readLong();
    swapInBytes = paramParcel.readLong();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(uid);
    paramParcel.writeString(processName);
    paramParcel.writeInt(oomScore);
    paramParcel.writeLong(pgfault);
    paramParcel.writeLong(pgmajfault);
    paramParcel.writeLong(rssInBytes);
    paramParcel.writeLong(cacheInBytes);
    paramParcel.writeLong(swapInBytes);
  }
}
