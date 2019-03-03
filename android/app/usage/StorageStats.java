package android.app.usage;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class StorageStats
  implements Parcelable
{
  public static final Parcelable.Creator<StorageStats> CREATOR = new Parcelable.Creator()
  {
    public StorageStats createFromParcel(Parcel paramAnonymousParcel)
    {
      return new StorageStats(paramAnonymousParcel);
    }
    
    public StorageStats[] newArray(int paramAnonymousInt)
    {
      return new StorageStats[paramAnonymousInt];
    }
  };
  public long cacheBytes;
  public long codeBytes;
  public long dataBytes;
  
  public StorageStats() {}
  
  public StorageStats(Parcel paramParcel)
  {
    codeBytes = paramParcel.readLong();
    dataBytes = paramParcel.readLong();
    cacheBytes = paramParcel.readLong();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public long getAppBytes()
  {
    return codeBytes;
  }
  
  public long getCacheBytes()
  {
    return cacheBytes;
  }
  
  @Deprecated
  public long getCodeBytes()
  {
    return getAppBytes();
  }
  
  public long getDataBytes()
  {
    return dataBytes;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeLong(codeBytes);
    paramParcel.writeLong(dataBytes);
    paramParcel.writeLong(cacheBytes);
  }
}
