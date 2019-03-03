package android.media;

import android.os.Parcel;

public final class TimedMetaData
{
  private static final String TAG = "TimedMetaData";
  private byte[] mMetaData;
  private long mTimestampUs;
  
  private TimedMetaData(Parcel paramParcel)
  {
    if (parseParcel(paramParcel)) {
      return;
    }
    throw new IllegalArgumentException("parseParcel() fails");
  }
  
  static TimedMetaData createTimedMetaDataFromParcel(Parcel paramParcel)
  {
    return new TimedMetaData(paramParcel);
  }
  
  private boolean parseParcel(Parcel paramParcel)
  {
    paramParcel.setDataPosition(0);
    if (paramParcel.dataAvail() == 0) {
      return false;
    }
    mTimestampUs = paramParcel.readLong();
    mMetaData = new byte[paramParcel.readInt()];
    paramParcel.readByteArray(mMetaData);
    return true;
  }
  
  public byte[] getMetaData()
  {
    return mMetaData;
  }
  
  public long getTimestamp()
  {
    return mTimestampUs;
  }
}
