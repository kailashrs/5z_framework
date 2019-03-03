package android.media;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.Objects;

@SystemApi
public final class AudioFocusInfo
  implements Parcelable
{
  public static final Parcelable.Creator<AudioFocusInfo> CREATOR = new Parcelable.Creator()
  {
    public AudioFocusInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      AudioFocusInfo localAudioFocusInfo = new AudioFocusInfo((AudioAttributes)AudioAttributes.CREATOR.createFromParcel(paramAnonymousParcel), paramAnonymousParcel.readInt(), paramAnonymousParcel.readString(), paramAnonymousParcel.readString(), paramAnonymousParcel.readInt(), paramAnonymousParcel.readInt(), paramAnonymousParcel.readInt(), paramAnonymousParcel.readInt());
      localAudioFocusInfo.setGen(paramAnonymousParcel.readLong());
      return localAudioFocusInfo;
    }
    
    public AudioFocusInfo[] newArray(int paramAnonymousInt)
    {
      return new AudioFocusInfo[paramAnonymousInt];
    }
  };
  private final AudioAttributes mAttributes;
  private final String mClientId;
  private final int mClientUid;
  private int mFlags;
  private int mGainRequest;
  private long mGenCount = -1L;
  private int mLossReceived;
  private final String mPackageName;
  private final int mSdkTarget;
  
  public AudioFocusInfo(AudioAttributes paramAudioAttributes, int paramInt1, String paramString1, String paramString2, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
  {
    if (paramAudioAttributes == null) {
      paramAudioAttributes = new AudioAttributes.Builder().build();
    }
    mAttributes = paramAudioAttributes;
    mClientUid = paramInt1;
    if (paramString1 == null) {
      paramAudioAttributes = "";
    } else {
      paramAudioAttributes = paramString1;
    }
    mClientId = paramAudioAttributes;
    if (paramString2 == null) {
      paramAudioAttributes = "";
    } else {
      paramAudioAttributes = paramString2;
    }
    mPackageName = paramAudioAttributes;
    mGainRequest = paramInt2;
    mLossReceived = paramInt3;
    mFlags = paramInt4;
    mSdkTarget = paramInt5;
  }
  
  public void clearLossReceived()
  {
    mLossReceived = 0;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    if (this == paramObject) {
      return true;
    }
    if (paramObject == null) {
      return false;
    }
    if (getClass() != paramObject.getClass()) {
      return false;
    }
    paramObject = (AudioFocusInfo)paramObject;
    if (!mAttributes.equals(mAttributes)) {
      return false;
    }
    if (mClientUid != mClientUid) {
      return false;
    }
    if (!mClientId.equals(mClientId)) {
      return false;
    }
    if (!mPackageName.equals(mPackageName)) {
      return false;
    }
    if (mGainRequest != mGainRequest) {
      return false;
    }
    if (mLossReceived != mLossReceived) {
      return false;
    }
    if (mFlags != mFlags) {
      return false;
    }
    return mSdkTarget == mSdkTarget;
  }
  
  @SystemApi
  public AudioAttributes getAttributes()
  {
    return mAttributes;
  }
  
  @SystemApi
  public String getClientId()
  {
    return mClientId;
  }
  
  @SystemApi
  public int getClientUid()
  {
    return mClientUid;
  }
  
  @SystemApi
  public int getFlags()
  {
    return mFlags;
  }
  
  @SystemApi
  public int getGainRequest()
  {
    return mGainRequest;
  }
  
  public long getGen()
  {
    return mGenCount;
  }
  
  @SystemApi
  public int getLossReceived()
  {
    return mLossReceived;
  }
  
  @SystemApi
  public String getPackageName()
  {
    return mPackageName;
  }
  
  public int getSdkTarget()
  {
    return mSdkTarget;
  }
  
  public int hashCode()
  {
    return Objects.hash(new Object[] { mAttributes, Integer.valueOf(mClientUid), mClientId, mPackageName, Integer.valueOf(mGainRequest), Integer.valueOf(mFlags) });
  }
  
  public void setGen(long paramLong)
  {
    mGenCount = paramLong;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    mAttributes.writeToParcel(paramParcel, paramInt);
    paramParcel.writeInt(mClientUid);
    paramParcel.writeString(mClientId);
    paramParcel.writeString(mPackageName);
    paramParcel.writeInt(mGainRequest);
    paramParcel.writeInt(mLossReceived);
    paramParcel.writeInt(mFlags);
    paramParcel.writeInt(mSdkTarget);
    paramParcel.writeLong(mGenCount);
  }
}
