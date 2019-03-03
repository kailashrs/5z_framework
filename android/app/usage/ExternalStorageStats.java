package android.app.usage;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class ExternalStorageStats
  implements Parcelable
{
  public static final Parcelable.Creator<ExternalStorageStats> CREATOR = new Parcelable.Creator()
  {
    public ExternalStorageStats createFromParcel(Parcel paramAnonymousParcel)
    {
      return new ExternalStorageStats(paramAnonymousParcel);
    }
    
    public ExternalStorageStats[] newArray(int paramAnonymousInt)
    {
      return new ExternalStorageStats[paramAnonymousInt];
    }
  };
  public long appBytes;
  public long audioBytes;
  public long imageBytes;
  public long obbBytes;
  public long totalBytes;
  public long videoBytes;
  
  public ExternalStorageStats() {}
  
  public ExternalStorageStats(Parcel paramParcel)
  {
    totalBytes = paramParcel.readLong();
    audioBytes = paramParcel.readLong();
    videoBytes = paramParcel.readLong();
    imageBytes = paramParcel.readLong();
    appBytes = paramParcel.readLong();
    obbBytes = paramParcel.readLong();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public long getAppBytes()
  {
    return appBytes;
  }
  
  public long getAudioBytes()
  {
    return audioBytes;
  }
  
  public long getImageBytes()
  {
    return imageBytes;
  }
  
  public long getObbBytes()
  {
    return obbBytes;
  }
  
  public long getTotalBytes()
  {
    return totalBytes;
  }
  
  public long getVideoBytes()
  {
    return videoBytes;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeLong(totalBytes);
    paramParcel.writeLong(audioBytes);
    paramParcel.writeLong(videoBytes);
    paramParcel.writeLong(imageBytes);
    paramParcel.writeLong(appBytes);
    paramParcel.writeLong(obbBytes);
  }
}
