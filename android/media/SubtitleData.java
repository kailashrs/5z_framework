package android.media;

import android.os.Parcel;

public final class SubtitleData
{
  private static final String TAG = "SubtitleData";
  private byte[] mData;
  private long mDurationUs;
  private long mStartTimeUs;
  private int mTrackIndex;
  
  public SubtitleData(Parcel paramParcel)
  {
    if (parseParcel(paramParcel)) {
      return;
    }
    throw new IllegalArgumentException("parseParcel() fails");
  }
  
  private boolean parseParcel(Parcel paramParcel)
  {
    paramParcel.setDataPosition(0);
    if (paramParcel.dataAvail() == 0) {
      return false;
    }
    mTrackIndex = paramParcel.readInt();
    mStartTimeUs = paramParcel.readLong();
    mDurationUs = paramParcel.readLong();
    mData = new byte[paramParcel.readInt()];
    paramParcel.readByteArray(mData);
    return true;
  }
  
  public byte[] getData()
  {
    return mData;
  }
  
  public long getDurationUs()
  {
    return mDurationUs;
  }
  
  public long getStartTimeUs()
  {
    return mStartTimeUs;
  }
  
  public int getTrackIndex()
  {
    return mTrackIndex;
  }
}
