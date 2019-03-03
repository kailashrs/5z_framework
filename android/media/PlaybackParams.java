package android.media;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class PlaybackParams
  implements Parcelable
{
  public static final int AUDIO_FALLBACK_MODE_DEFAULT = 0;
  public static final int AUDIO_FALLBACK_MODE_FAIL = 2;
  public static final int AUDIO_FALLBACK_MODE_MUTE = 1;
  public static final int AUDIO_STRETCH_MODE_DEFAULT = 0;
  public static final int AUDIO_STRETCH_MODE_VOICE = 1;
  public static final Parcelable.Creator<PlaybackParams> CREATOR = new Parcelable.Creator()
  {
    public PlaybackParams createFromParcel(Parcel paramAnonymousParcel)
    {
      return new PlaybackParams(paramAnonymousParcel, null);
    }
    
    public PlaybackParams[] newArray(int paramAnonymousInt)
    {
      return new PlaybackParams[paramAnonymousInt];
    }
  };
  private static final int SET_AUDIO_FALLBACK_MODE = 4;
  private static final int SET_AUDIO_STRETCH_MODE = 8;
  private static final int SET_PITCH = 2;
  private static final int SET_SPEED = 1;
  private int mAudioFallbackMode = 0;
  private int mAudioStretchMode = 0;
  private float mPitch = 1.0F;
  private int mSet = 0;
  private float mSpeed = 1.0F;
  
  public PlaybackParams() {}
  
  private PlaybackParams(Parcel paramParcel)
  {
    mSet = paramParcel.readInt();
    mAudioFallbackMode = paramParcel.readInt();
    mAudioStretchMode = paramParcel.readInt();
    mPitch = paramParcel.readFloat();
    if (mPitch < 0.0F) {
      mPitch = 0.0F;
    }
    mSpeed = paramParcel.readFloat();
  }
  
  public PlaybackParams allowDefaults()
  {
    mSet |= 0xF;
    return this;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public int getAudioFallbackMode()
  {
    if ((mSet & 0x4) != 0) {
      return mAudioFallbackMode;
    }
    throw new IllegalStateException("audio fallback mode not set");
  }
  
  public int getAudioStretchMode()
  {
    if ((mSet & 0x8) != 0) {
      return mAudioStretchMode;
    }
    throw new IllegalStateException("audio stretch mode not set");
  }
  
  public float getPitch()
  {
    if ((mSet & 0x2) != 0) {
      return mPitch;
    }
    throw new IllegalStateException("pitch not set");
  }
  
  public float getSpeed()
  {
    if ((mSet & 0x1) != 0) {
      return mSpeed;
    }
    throw new IllegalStateException("speed not set");
  }
  
  public PlaybackParams setAudioFallbackMode(int paramInt)
  {
    mAudioFallbackMode = paramInt;
    mSet |= 0x4;
    return this;
  }
  
  public PlaybackParams setAudioStretchMode(int paramInt)
  {
    mAudioStretchMode = paramInt;
    mSet |= 0x8;
    return this;
  }
  
  public PlaybackParams setPitch(float paramFloat)
  {
    if (paramFloat >= 0.0F)
    {
      mPitch = paramFloat;
      mSet |= 0x2;
      return this;
    }
    throw new IllegalArgumentException("pitch must not be negative");
  }
  
  public PlaybackParams setSpeed(float paramFloat)
  {
    mSpeed = paramFloat;
    mSet |= 0x1;
    return this;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mSet);
    paramParcel.writeInt(mAudioFallbackMode);
    paramParcel.writeInt(mAudioStretchMode);
    paramParcel.writeFloat(mPitch);
    paramParcel.writeFloat(mSpeed);
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface AudioFallbackMode {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface AudioStretchMode {}
}
