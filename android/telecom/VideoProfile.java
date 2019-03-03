package android.telecom;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class VideoProfile
  implements Parcelable
{
  public static final Parcelable.Creator<VideoProfile> CREATOR = new Parcelable.Creator()
  {
    public VideoProfile createFromParcel(Parcel paramAnonymousParcel)
    {
      int i = paramAnonymousParcel.readInt();
      int j = paramAnonymousParcel.readInt();
      VideoProfile.class.getClassLoader();
      return new VideoProfile(i, j);
    }
    
    public VideoProfile[] newArray(int paramAnonymousInt)
    {
      return new VideoProfile[paramAnonymousInt];
    }
  };
  public static final int QUALITY_DEFAULT = 4;
  public static final int QUALITY_HIGH = 1;
  public static final int QUALITY_LOW = 3;
  public static final int QUALITY_MEDIUM = 2;
  public static final int QUALITY_UNKNOWN = 0;
  public static final int STATE_AUDIO_ONLY = 0;
  public static final int STATE_BIDIRECTIONAL = 3;
  public static final int STATE_PAUSED = 4;
  public static final int STATE_RX_ENABLED = 2;
  public static final int STATE_TX_ENABLED = 1;
  private final int mQuality;
  private final int mVideoState;
  
  public VideoProfile(int paramInt)
  {
    this(paramInt, 4);
  }
  
  public VideoProfile(int paramInt1, int paramInt2)
  {
    mVideoState = paramInt1;
    mQuality = paramInt2;
  }
  
  private static boolean hasState(int paramInt1, int paramInt2)
  {
    boolean bool;
    if ((paramInt1 & paramInt2) == paramInt2) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static boolean isAudioOnly(int paramInt)
  {
    boolean bool = true;
    if ((hasState(paramInt, 1)) || (hasState(paramInt, 2))) {
      bool = false;
    }
    return bool;
  }
  
  public static boolean isBidirectional(int paramInt)
  {
    return hasState(paramInt, 3);
  }
  
  public static boolean isPaused(int paramInt)
  {
    return hasState(paramInt, 4);
  }
  
  public static boolean isReceptionEnabled(int paramInt)
  {
    return hasState(paramInt, 2);
  }
  
  public static boolean isTransmissionEnabled(int paramInt)
  {
    return hasState(paramInt, 1);
  }
  
  public static boolean isVideo(int paramInt)
  {
    boolean bool1 = true;
    boolean bool2 = bool1;
    if (!hasState(paramInt, 1))
    {
      bool2 = bool1;
      if (!hasState(paramInt, 2)) {
        if (hasState(paramInt, 3)) {
          bool2 = bool1;
        } else {
          bool2 = false;
        }
      }
    }
    return bool2;
  }
  
  public static String videoStateToString(int paramInt)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Audio");
    if (paramInt == 0)
    {
      localStringBuilder.append(" Only");
    }
    else
    {
      if (isTransmissionEnabled(paramInt)) {
        localStringBuilder.append(" Tx");
      }
      if (isReceptionEnabled(paramInt)) {
        localStringBuilder.append(" Rx");
      }
      if (isPaused(paramInt)) {
        localStringBuilder.append(" Pause");
      }
    }
    return localStringBuilder.toString();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public int getQuality()
  {
    return mQuality;
  }
  
  public int getVideoState()
  {
    return mVideoState;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("[VideoProfile videoState = ");
    localStringBuilder.append(videoStateToString(mVideoState));
    localStringBuilder.append(" videoQuality = ");
    localStringBuilder.append(mQuality);
    localStringBuilder.append("]");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mVideoState);
    paramParcel.writeInt(mQuality);
  }
  
  public static final class CameraCapabilities
    implements Parcelable
  {
    public static final Parcelable.Creator<CameraCapabilities> CREATOR = new Parcelable.Creator()
    {
      public VideoProfile.CameraCapabilities createFromParcel(Parcel paramAnonymousParcel)
      {
        int i = paramAnonymousParcel.readInt();
        int j = paramAnonymousParcel.readInt();
        boolean bool;
        if (paramAnonymousParcel.readByte() != 0) {
          bool = true;
        } else {
          bool = false;
        }
        return new VideoProfile.CameraCapabilities(i, j, bool, paramAnonymousParcel.readFloat());
      }
      
      public VideoProfile.CameraCapabilities[] newArray(int paramAnonymousInt)
      {
        return new VideoProfile.CameraCapabilities[paramAnonymousInt];
      }
    };
    private final int mHeight;
    private final float mMaxZoom;
    private final int mWidth;
    private final boolean mZoomSupported;
    
    public CameraCapabilities(int paramInt1, int paramInt2)
    {
      this(paramInt1, paramInt2, false, 1.0F);
    }
    
    public CameraCapabilities(int paramInt1, int paramInt2, boolean paramBoolean, float paramFloat)
    {
      mWidth = paramInt1;
      mHeight = paramInt2;
      mZoomSupported = paramBoolean;
      mMaxZoom = paramFloat;
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public int getHeight()
    {
      return mHeight;
    }
    
    public float getMaxZoom()
    {
      return mMaxZoom;
    }
    
    public int getWidth()
    {
      return mWidth;
    }
    
    public boolean isZoomSupported()
    {
      return mZoomSupported;
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeInt(getWidth());
      paramParcel.writeInt(getHeight());
      paramParcel.writeByte((byte)isZoomSupported());
      paramParcel.writeFloat(getMaxZoom());
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface VideoQuality {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface VideoState {}
}
