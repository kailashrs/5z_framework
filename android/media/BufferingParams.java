package android.media;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class BufferingParams
  implements Parcelable
{
  private static final int BUFFERING_NO_MARK = -1;
  public static final Parcelable.Creator<BufferingParams> CREATOR = new Parcelable.Creator()
  {
    public BufferingParams createFromParcel(Parcel paramAnonymousParcel)
    {
      return new BufferingParams(paramAnonymousParcel, null);
    }
    
    public BufferingParams[] newArray(int paramAnonymousInt)
    {
      return new BufferingParams[paramAnonymousInt];
    }
  };
  private int mInitialMarkMs = -1;
  private int mResumePlaybackMarkMs = -1;
  
  private BufferingParams() {}
  
  private BufferingParams(Parcel paramParcel)
  {
    mInitialMarkMs = paramParcel.readInt();
    mResumePlaybackMarkMs = paramParcel.readInt();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public int getInitialMarkMs()
  {
    return mInitialMarkMs;
  }
  
  public int getResumePlaybackMarkMs()
  {
    return mResumePlaybackMarkMs;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mInitialMarkMs);
    paramParcel.writeInt(mResumePlaybackMarkMs);
  }
  
  public static class Builder
  {
    private int mInitialMarkMs = -1;
    private int mResumePlaybackMarkMs = -1;
    
    public Builder() {}
    
    public Builder(BufferingParams paramBufferingParams)
    {
      mInitialMarkMs = mInitialMarkMs;
      mResumePlaybackMarkMs = mResumePlaybackMarkMs;
    }
    
    public BufferingParams build()
    {
      BufferingParams localBufferingParams = new BufferingParams(null);
      BufferingParams.access$002(localBufferingParams, mInitialMarkMs);
      BufferingParams.access$102(localBufferingParams, mResumePlaybackMarkMs);
      return localBufferingParams;
    }
    
    public Builder setInitialMarkMs(int paramInt)
    {
      mInitialMarkMs = paramInt;
      return this;
    }
    
    public Builder setResumePlaybackMarkMs(int paramInt)
    {
      mResumePlaybackMarkMs = paramInt;
      return this;
    }
  }
}
