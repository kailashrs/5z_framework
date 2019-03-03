package android.app.backup;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

@SystemApi
public class BackupProgress
  implements Parcelable
{
  public static final Parcelable.Creator<BackupProgress> CREATOR = new Parcelable.Creator()
  {
    public BackupProgress createFromParcel(Parcel paramAnonymousParcel)
    {
      return new BackupProgress(paramAnonymousParcel, null);
    }
    
    public BackupProgress[] newArray(int paramAnonymousInt)
    {
      return new BackupProgress[paramAnonymousInt];
    }
  };
  public final long bytesExpected;
  public final long bytesTransferred;
  
  public BackupProgress(long paramLong1, long paramLong2)
  {
    bytesExpected = paramLong1;
    bytesTransferred = paramLong2;
  }
  
  private BackupProgress(Parcel paramParcel)
  {
    bytesExpected = paramParcel.readLong();
    bytesTransferred = paramParcel.readLong();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeLong(bytesExpected);
    paramParcel.writeLong(bytesTransferred);
  }
}
