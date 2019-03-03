package android.media.tv;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import com.android.internal.util.Preconditions;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Objects;

public final class TvTrackInfo
  implements Parcelable
{
  public static final Parcelable.Creator<TvTrackInfo> CREATOR = new Parcelable.Creator()
  {
    public TvTrackInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new TvTrackInfo(paramAnonymousParcel, null);
    }
    
    public TvTrackInfo[] newArray(int paramAnonymousInt)
    {
      return new TvTrackInfo[paramAnonymousInt];
    }
  };
  public static final int TYPE_AUDIO = 0;
  public static final int TYPE_SUBTITLE = 2;
  public static final int TYPE_VIDEO = 1;
  private final int mAudioChannelCount;
  private final int mAudioSampleRate;
  private final CharSequence mDescription;
  private final Bundle mExtra;
  private final String mId;
  private final String mLanguage;
  private final int mType;
  private final byte mVideoActiveFormatDescription;
  private final float mVideoFrameRate;
  private final int mVideoHeight;
  private final float mVideoPixelAspectRatio;
  private final int mVideoWidth;
  
  private TvTrackInfo(int paramInt1, String paramString1, String paramString2, CharSequence paramCharSequence, int paramInt2, int paramInt3, int paramInt4, int paramInt5, float paramFloat1, float paramFloat2, byte paramByte, Bundle paramBundle)
  {
    mType = paramInt1;
    mId = paramString1;
    mLanguage = paramString2;
    mDescription = paramCharSequence;
    mAudioChannelCount = paramInt2;
    mAudioSampleRate = paramInt3;
    mVideoWidth = paramInt4;
    mVideoHeight = paramInt5;
    mVideoFrameRate = paramFloat1;
    mVideoPixelAspectRatio = paramFloat2;
    mVideoActiveFormatDescription = ((byte)paramByte);
    mExtra = paramBundle;
  }
  
  private TvTrackInfo(Parcel paramParcel)
  {
    mType = paramParcel.readInt();
    mId = paramParcel.readString();
    mLanguage = paramParcel.readString();
    mDescription = paramParcel.readString();
    mAudioChannelCount = paramParcel.readInt();
    mAudioSampleRate = paramParcel.readInt();
    mVideoWidth = paramParcel.readInt();
    mVideoHeight = paramParcel.readInt();
    mVideoFrameRate = paramParcel.readFloat();
    mVideoPixelAspectRatio = paramParcel.readFloat();
    mVideoActiveFormatDescription = paramParcel.readByte();
    mExtra = paramParcel.readBundle();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool = true;
    if (this == paramObject) {
      return true;
    }
    if (!(paramObject instanceof TvTrackInfo)) {
      return false;
    }
    paramObject = (TvTrackInfo)paramObject;
    if ((!TextUtils.equals(mId, mId)) || (mType != mType) || (!TextUtils.equals(mLanguage, mLanguage)) || (!TextUtils.equals(mDescription, mDescription)) || (!Objects.equals(mExtra, mExtra)) || (mType == 0 ? (mAudioChannelCount == mAudioChannelCount) && (mAudioSampleRate == mAudioSampleRate) : (mType != 1) || ((mVideoWidth != mVideoWidth) || (mVideoHeight != mVideoHeight) || (mVideoFrameRate != mVideoFrameRate) || (mVideoPixelAspectRatio != mVideoPixelAspectRatio)))) {
      bool = false;
    }
    return bool;
  }
  
  public final int getAudioChannelCount()
  {
    if (mType == 0) {
      return mAudioChannelCount;
    }
    throw new IllegalStateException("Not an audio track");
  }
  
  public final int getAudioSampleRate()
  {
    if (mType == 0) {
      return mAudioSampleRate;
    }
    throw new IllegalStateException("Not an audio track");
  }
  
  public final CharSequence getDescription()
  {
    return mDescription;
  }
  
  public final Bundle getExtra()
  {
    return mExtra;
  }
  
  public final String getId()
  {
    return mId;
  }
  
  public final String getLanguage()
  {
    return mLanguage;
  }
  
  public final int getType()
  {
    return mType;
  }
  
  public final byte getVideoActiveFormatDescription()
  {
    if (mType == 1) {
      return mVideoActiveFormatDescription;
    }
    throw new IllegalStateException("Not a video track");
  }
  
  public final float getVideoFrameRate()
  {
    if (mType == 1) {
      return mVideoFrameRate;
    }
    throw new IllegalStateException("Not a video track");
  }
  
  public final int getVideoHeight()
  {
    if (mType == 1) {
      return mVideoHeight;
    }
    throw new IllegalStateException("Not a video track");
  }
  
  public final float getVideoPixelAspectRatio()
  {
    if (mType == 1) {
      return mVideoPixelAspectRatio;
    }
    throw new IllegalStateException("Not a video track");
  }
  
  public final int getVideoWidth()
  {
    if (mType == 1) {
      return mVideoWidth;
    }
    throw new IllegalStateException("Not a video track");
  }
  
  public int hashCode()
  {
    return Objects.hashCode(mId);
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mType);
    paramParcel.writeString(mId);
    paramParcel.writeString(mLanguage);
    String str;
    if (mDescription != null) {
      str = mDescription.toString();
    } else {
      str = null;
    }
    paramParcel.writeString(str);
    paramParcel.writeInt(mAudioChannelCount);
    paramParcel.writeInt(mAudioSampleRate);
    paramParcel.writeInt(mVideoWidth);
    paramParcel.writeInt(mVideoHeight);
    paramParcel.writeFloat(mVideoFrameRate);
    paramParcel.writeFloat(mVideoPixelAspectRatio);
    paramParcel.writeByte(mVideoActiveFormatDescription);
    paramParcel.writeBundle(mExtra);
  }
  
  public static final class Builder
  {
    private int mAudioChannelCount;
    private int mAudioSampleRate;
    private CharSequence mDescription;
    private Bundle mExtra;
    private final String mId;
    private String mLanguage;
    private final int mType;
    private byte mVideoActiveFormatDescription;
    private float mVideoFrameRate;
    private int mVideoHeight;
    private float mVideoPixelAspectRatio = 1.0F;
    private int mVideoWidth;
    
    public Builder(int paramInt, String paramString)
    {
      if ((paramInt != 0) && (paramInt != 1) && (paramInt != 2))
      {
        paramString = new StringBuilder();
        paramString.append("Unknown type: ");
        paramString.append(paramInt);
        throw new IllegalArgumentException(paramString.toString());
      }
      Preconditions.checkNotNull(paramString);
      mType = paramInt;
      mId = paramString;
    }
    
    public TvTrackInfo build()
    {
      return new TvTrackInfo(mType, mId, mLanguage, mDescription, mAudioChannelCount, mAudioSampleRate, mVideoWidth, mVideoHeight, mVideoFrameRate, mVideoPixelAspectRatio, mVideoActiveFormatDescription, mExtra, null);
    }
    
    public final Builder setAudioChannelCount(int paramInt)
    {
      if (mType == 0)
      {
        mAudioChannelCount = paramInt;
        return this;
      }
      throw new IllegalStateException("Not an audio track");
    }
    
    public final Builder setAudioSampleRate(int paramInt)
    {
      if (mType == 0)
      {
        mAudioSampleRate = paramInt;
        return this;
      }
      throw new IllegalStateException("Not an audio track");
    }
    
    public final Builder setDescription(CharSequence paramCharSequence)
    {
      mDescription = paramCharSequence;
      return this;
    }
    
    public final Builder setExtra(Bundle paramBundle)
    {
      mExtra = new Bundle(paramBundle);
      return this;
    }
    
    public final Builder setLanguage(String paramString)
    {
      mLanguage = paramString;
      return this;
    }
    
    public final Builder setVideoActiveFormatDescription(byte paramByte)
    {
      if (mType == 1)
      {
        mVideoActiveFormatDescription = ((byte)paramByte);
        return this;
      }
      throw new IllegalStateException("Not a video track");
    }
    
    public final Builder setVideoFrameRate(float paramFloat)
    {
      if (mType == 1)
      {
        mVideoFrameRate = paramFloat;
        return this;
      }
      throw new IllegalStateException("Not a video track");
    }
    
    public final Builder setVideoHeight(int paramInt)
    {
      if (mType == 1)
      {
        mVideoHeight = paramInt;
        return this;
      }
      throw new IllegalStateException("Not a video track");
    }
    
    public final Builder setVideoPixelAspectRatio(float paramFloat)
    {
      if (mType == 1)
      {
        mVideoPixelAspectRatio = paramFloat;
        return this;
      }
      throw new IllegalStateException("Not a video track");
    }
    
    public final Builder setVideoWidth(int paramInt)
    {
      if (mType == 1)
      {
        mVideoWidth = paramInt;
        return this;
      }
      throw new IllegalStateException("Not a video track");
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface Type {}
}
