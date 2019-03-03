package android.hardware.camera2.utils;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class SubmitInfo
  implements Parcelable
{
  public static final Parcelable.Creator<SubmitInfo> CREATOR = new Parcelable.Creator()
  {
    public SubmitInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new SubmitInfo(paramAnonymousParcel, null);
    }
    
    public SubmitInfo[] newArray(int paramAnonymousInt)
    {
      return new SubmitInfo[paramAnonymousInt];
    }
  };
  private long mLastFrameNumber;
  private int mRequestId;
  
  public SubmitInfo()
  {
    mRequestId = -1;
    mLastFrameNumber = -1L;
  }
  
  public SubmitInfo(int paramInt, long paramLong)
  {
    mRequestId = paramInt;
    mLastFrameNumber = paramLong;
  }
  
  private SubmitInfo(Parcel paramParcel)
  {
    readFromParcel(paramParcel);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public long getLastFrameNumber()
  {
    return mLastFrameNumber;
  }
  
  public int getRequestId()
  {
    return mRequestId;
  }
  
  public void readFromParcel(Parcel paramParcel)
  {
    mRequestId = paramParcel.readInt();
    mLastFrameNumber = paramParcel.readLong();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mRequestId);
    paramParcel.writeLong(mLastFrameNumber);
  }
}
