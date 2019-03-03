package android.telephony.ims;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

@SystemApi
public final class ImsStreamMediaProfile
  implements Parcelable
{
  public static final int AUDIO_QUALITY_AMR = 1;
  public static final int AUDIO_QUALITY_AMR_WB = 2;
  public static final int AUDIO_QUALITY_EVRC = 4;
  public static final int AUDIO_QUALITY_EVRC_B = 5;
  public static final int AUDIO_QUALITY_EVRC_NW = 7;
  public static final int AUDIO_QUALITY_EVRC_WB = 6;
  public static final int AUDIO_QUALITY_EVS_FB = 20;
  public static final int AUDIO_QUALITY_EVS_NB = 17;
  public static final int AUDIO_QUALITY_EVS_SWB = 19;
  public static final int AUDIO_QUALITY_EVS_WB = 18;
  public static final int AUDIO_QUALITY_G711A = 13;
  public static final int AUDIO_QUALITY_G711AB = 15;
  public static final int AUDIO_QUALITY_G711U = 11;
  public static final int AUDIO_QUALITY_G722 = 14;
  public static final int AUDIO_QUALITY_G723 = 12;
  public static final int AUDIO_QUALITY_G729 = 16;
  public static final int AUDIO_QUALITY_GSM_EFR = 8;
  public static final int AUDIO_QUALITY_GSM_FR = 9;
  public static final int AUDIO_QUALITY_GSM_HR = 10;
  public static final int AUDIO_QUALITY_NONE = 0;
  public static final int AUDIO_QUALITY_QCELP13K = 3;
  public static final Parcelable.Creator<ImsStreamMediaProfile> CREATOR = new Parcelable.Creator()
  {
    public ImsStreamMediaProfile createFromParcel(Parcel paramAnonymousParcel)
    {
      return new ImsStreamMediaProfile(paramAnonymousParcel);
    }
    
    public ImsStreamMediaProfile[] newArray(int paramAnonymousInt)
    {
      return new ImsStreamMediaProfile[paramAnonymousInt];
    }
  };
  public static final int DIRECTION_INACTIVE = 0;
  public static final int DIRECTION_INVALID = -1;
  public static final int DIRECTION_RECEIVE = 1;
  public static final int DIRECTION_SEND = 2;
  public static final int DIRECTION_SEND_RECEIVE = 3;
  public static final int RTT_MODE_DISABLED = 0;
  public static final int RTT_MODE_FULL = 1;
  private static final String TAG = "ImsStreamMediaProfile";
  public static final int VIDEO_QUALITY_NONE = 0;
  public static final int VIDEO_QUALITY_QCIF = 1;
  public static final int VIDEO_QUALITY_QVGA_LANDSCAPE = 2;
  public static final int VIDEO_QUALITY_QVGA_PORTRAIT = 4;
  public static final int VIDEO_QUALITY_VGA_LANDSCAPE = 8;
  public static final int VIDEO_QUALITY_VGA_PORTRAIT = 16;
  public int mAudioDirection;
  public int mAudioQuality;
  public int mRttMode;
  public int mVideoDirection;
  public int mVideoQuality;
  
  public ImsStreamMediaProfile()
  {
    mAudioQuality = 0;
    mAudioDirection = 3;
    mVideoQuality = 0;
    mVideoDirection = -1;
    mRttMode = 0;
  }
  
  public ImsStreamMediaProfile(int paramInt)
  {
    mRttMode = paramInt;
  }
  
  public ImsStreamMediaProfile(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    mAudioQuality = paramInt1;
    mAudioDirection = paramInt2;
    mVideoQuality = paramInt3;
    mVideoDirection = paramInt4;
  }
  
  public ImsStreamMediaProfile(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
  {
    mAudioQuality = paramInt1;
    mAudioDirection = paramInt2;
    mVideoQuality = paramInt3;
    mVideoDirection = paramInt4;
    mRttMode = paramInt5;
  }
  
  public ImsStreamMediaProfile(Parcel paramParcel)
  {
    readFromParcel(paramParcel);
  }
  
  private void readFromParcel(Parcel paramParcel)
  {
    mAudioQuality = paramParcel.readInt();
    mAudioDirection = paramParcel.readInt();
    mVideoQuality = paramParcel.readInt();
    mVideoDirection = paramParcel.readInt();
    mRttMode = paramParcel.readInt();
  }
  
  public void copyFrom(ImsStreamMediaProfile paramImsStreamMediaProfile)
  {
    mAudioQuality = mAudioQuality;
    mAudioDirection = mAudioDirection;
    mVideoQuality = mVideoQuality;
    mVideoDirection = mVideoDirection;
    mRttMode = mRttMode;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public int getAudioDirection()
  {
    return mAudioDirection;
  }
  
  public int getAudioQuality()
  {
    return mAudioQuality;
  }
  
  public int getRttMode()
  {
    return mRttMode;
  }
  
  public int getVideoDirection()
  {
    return mVideoDirection;
  }
  
  public int getVideoQuality()
  {
    return mVideoQuality;
  }
  
  public boolean isRttCall()
  {
    int i = mRttMode;
    boolean bool = true;
    if (i != 1) {
      bool = false;
    }
    return bool;
  }
  
  public void setRttMode(int paramInt)
  {
    mRttMode = paramInt;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{ audioQuality=");
    localStringBuilder.append(mAudioQuality);
    localStringBuilder.append(", audioDirection=");
    localStringBuilder.append(mAudioDirection);
    localStringBuilder.append(", videoQuality=");
    localStringBuilder.append(mVideoQuality);
    localStringBuilder.append(", videoDirection=");
    localStringBuilder.append(mVideoDirection);
    localStringBuilder.append(", rttMode=");
    localStringBuilder.append(mRttMode);
    localStringBuilder.append(" }");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mAudioQuality);
    paramParcel.writeInt(mAudioDirection);
    paramParcel.writeInt(mVideoQuality);
    paramParcel.writeInt(mVideoDirection);
    paramParcel.writeInt(mRttMode);
  }
}
