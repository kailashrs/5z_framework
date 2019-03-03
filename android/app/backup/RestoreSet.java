package android.app.backup;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

@SystemApi
public class RestoreSet
  implements Parcelable
{
  public static final Parcelable.Creator<RestoreSet> CREATOR = new Parcelable.Creator()
  {
    public RestoreSet createFromParcel(Parcel paramAnonymousParcel)
    {
      return new RestoreSet(paramAnonymousParcel, null);
    }
    
    public RestoreSet[] newArray(int paramAnonymousInt)
    {
      return new RestoreSet[paramAnonymousInt];
    }
  };
  public String device;
  public String name;
  public long token;
  
  public RestoreSet() {}
  
  private RestoreSet(Parcel paramParcel)
  {
    name = paramParcel.readString();
    device = paramParcel.readString();
    token = paramParcel.readLong();
  }
  
  public RestoreSet(String paramString1, String paramString2, long paramLong)
  {
    name = paramString1;
    device = paramString2;
    token = paramLong;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(name);
    paramParcel.writeString(device);
    paramParcel.writeLong(token);
  }
}
