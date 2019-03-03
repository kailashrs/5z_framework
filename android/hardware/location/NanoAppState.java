package android.hardware.location;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

@SystemApi
public final class NanoAppState
  implements Parcelable
{
  public static final Parcelable.Creator<NanoAppState> CREATOR = new Parcelable.Creator()
  {
    public NanoAppState createFromParcel(Parcel paramAnonymousParcel)
    {
      return new NanoAppState(paramAnonymousParcel, null);
    }
    
    public NanoAppState[] newArray(int paramAnonymousInt)
    {
      return new NanoAppState[paramAnonymousInt];
    }
  };
  private boolean mIsEnabled;
  private long mNanoAppId;
  private int mNanoAppVersion;
  
  public NanoAppState(long paramLong, int paramInt, boolean paramBoolean)
  {
    mNanoAppId = paramLong;
    mNanoAppVersion = paramInt;
    mIsEnabled = paramBoolean;
  }
  
  private NanoAppState(Parcel paramParcel)
  {
    mNanoAppId = paramParcel.readLong();
    mNanoAppVersion = paramParcel.readInt();
    int i = paramParcel.readInt();
    boolean bool = true;
    if (i != 1) {
      bool = false;
    }
    mIsEnabled = bool;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public long getNanoAppId()
  {
    return mNanoAppId;
  }
  
  public long getNanoAppVersion()
  {
    return mNanoAppVersion;
  }
  
  public boolean isEnabled()
  {
    return mIsEnabled;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeLong(mNanoAppId);
    paramParcel.writeInt(mNanoAppVersion);
    paramParcel.writeInt(mIsEnabled);
  }
}
