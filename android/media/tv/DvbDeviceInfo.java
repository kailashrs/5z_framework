package android.media.tv;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;

public final class DvbDeviceInfo
  implements Parcelable
{
  public static final Parcelable.Creator<DvbDeviceInfo> CREATOR = new Parcelable.Creator()
  {
    public DvbDeviceInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      try
      {
        paramAnonymousParcel = new DvbDeviceInfo(paramAnonymousParcel, null);
        return paramAnonymousParcel;
      }
      catch (Exception paramAnonymousParcel)
      {
        Log.e("DvbDeviceInfo", "Exception creating DvbDeviceInfo from parcel", paramAnonymousParcel);
      }
      return null;
    }
    
    public DvbDeviceInfo[] newArray(int paramAnonymousInt)
    {
      return new DvbDeviceInfo[paramAnonymousInt];
    }
  };
  static final String TAG = "DvbDeviceInfo";
  private final int mAdapterId;
  private final int mDeviceId;
  
  public DvbDeviceInfo(int paramInt1, int paramInt2)
  {
    mAdapterId = paramInt1;
    mDeviceId = paramInt2;
  }
  
  private DvbDeviceInfo(Parcel paramParcel)
  {
    mAdapterId = paramParcel.readInt();
    mDeviceId = paramParcel.readInt();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public int getAdapterId()
  {
    return mAdapterId;
  }
  
  public int getDeviceId()
  {
    return mDeviceId;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mAdapterId);
    paramParcel.writeInt(mDeviceId);
  }
}
