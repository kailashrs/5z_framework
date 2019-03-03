package android.media;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;
import java.util.Objects;

public final class AudioFormat
  implements Parcelable
{
  public static final int AUDIO_FORMAT_HAS_PROPERTY_CHANNEL_INDEX_MASK = 8;
  public static final int AUDIO_FORMAT_HAS_PROPERTY_CHANNEL_MASK = 4;
  public static final int AUDIO_FORMAT_HAS_PROPERTY_ENCODING = 1;
  public static final int AUDIO_FORMAT_HAS_PROPERTY_NONE = 0;
  public static final int AUDIO_FORMAT_HAS_PROPERTY_SAMPLE_RATE = 2;
  @Deprecated
  public static final int CHANNEL_CONFIGURATION_DEFAULT = 1;
  @Deprecated
  public static final int CHANNEL_CONFIGURATION_INVALID = 0;
  @Deprecated
  public static final int CHANNEL_CONFIGURATION_MONO = 2;
  @Deprecated
  public static final int CHANNEL_CONFIGURATION_STEREO = 3;
  public static final int CHANNEL_INVALID = 0;
  public static final int CHANNEL_IN_5POINT1 = 252;
  public static final int CHANNEL_IN_BACK = 32;
  public static final int CHANNEL_IN_BACK_PROCESSED = 512;
  public static final int CHANNEL_IN_DEFAULT = 1;
  public static final int CHANNEL_IN_FRONT = 16;
  public static final int CHANNEL_IN_FRONT_BACK = 48;
  public static final int CHANNEL_IN_FRONT_PROCESSED = 256;
  public static final int CHANNEL_IN_LEFT = 4;
  public static final int CHANNEL_IN_LEFT_PROCESSED = 64;
  public static final int CHANNEL_IN_MONO = 16;
  public static final int CHANNEL_IN_PRESSURE = 1024;
  public static final int CHANNEL_IN_RIGHT = 8;
  public static final int CHANNEL_IN_RIGHT_PROCESSED = 128;
  public static final int CHANNEL_IN_STEREO = 12;
  public static final int CHANNEL_IN_VOICE_DNLINK = 32768;
  public static final int CHANNEL_IN_VOICE_UPLINK = 16384;
  public static final int CHANNEL_IN_X_AXIS = 2048;
  public static final int CHANNEL_IN_Y_AXIS = 4096;
  public static final int CHANNEL_IN_Z_AXIS = 8192;
  public static final int CHANNEL_OUT_5POINT1 = 252;
  public static final int CHANNEL_OUT_5POINT1_SIDE = 6204;
  @Deprecated
  public static final int CHANNEL_OUT_7POINT1 = 1020;
  public static final int CHANNEL_OUT_7POINT1_SURROUND = 6396;
  public static final int CHANNEL_OUT_BACK_CENTER = 1024;
  public static final int CHANNEL_OUT_BACK_LEFT = 64;
  public static final int CHANNEL_OUT_BACK_RIGHT = 128;
  public static final int CHANNEL_OUT_DEFAULT = 1;
  public static final int CHANNEL_OUT_FRONT_CENTER = 16;
  public static final int CHANNEL_OUT_FRONT_LEFT = 4;
  public static final int CHANNEL_OUT_FRONT_LEFT_OF_CENTER = 256;
  public static final int CHANNEL_OUT_FRONT_RIGHT = 8;
  public static final int CHANNEL_OUT_FRONT_RIGHT_OF_CENTER = 512;
  public static final int CHANNEL_OUT_LOW_FREQUENCY = 32;
  public static final int CHANNEL_OUT_MONO = 4;
  public static final int CHANNEL_OUT_QUAD = 204;
  public static final int CHANNEL_OUT_QUAD_SIDE = 6156;
  public static final int CHANNEL_OUT_SIDE_LEFT = 2048;
  public static final int CHANNEL_OUT_SIDE_RIGHT = 4096;
  public static final int CHANNEL_OUT_STEREO = 12;
  public static final int CHANNEL_OUT_SURROUND = 1052;
  public static final int CHANNEL_OUT_TOP_BACK_CENTER = 262144;
  public static final int CHANNEL_OUT_TOP_BACK_LEFT = 131072;
  public static final int CHANNEL_OUT_TOP_BACK_RIGHT = 524288;
  public static final int CHANNEL_OUT_TOP_CENTER = 8192;
  public static final int CHANNEL_OUT_TOP_FRONT_CENTER = 32768;
  public static final int CHANNEL_OUT_TOP_FRONT_LEFT = 16384;
  public static final int CHANNEL_OUT_TOP_FRONT_RIGHT = 65536;
  public static final Parcelable.Creator<AudioFormat> CREATOR = new Parcelable.Creator()
  {
    public AudioFormat createFromParcel(Parcel paramAnonymousParcel)
    {
      return new AudioFormat(paramAnonymousParcel, null);
    }
    
    public AudioFormat[] newArray(int paramAnonymousInt)
    {
      return new AudioFormat[paramAnonymousInt];
    }
  };
  public static final int ENCODING_AAC_ELD = 15;
  public static final int ENCODING_AAC_HE_V1 = 11;
  public static final int ENCODING_AAC_HE_V2 = 12;
  public static final int ENCODING_AAC_LC = 10;
  public static final int ENCODING_AAC_XHE = 16;
  public static final int ENCODING_AC3 = 5;
  public static final int ENCODING_AC4 = 17;
  public static final int ENCODING_AMRNB = 100;
  public static final int ENCODING_AMRWB = 101;
  public static final int ENCODING_DEFAULT = 1;
  public static final int ENCODING_DOLBY_TRUEHD = 14;
  public static final int ENCODING_DTS = 7;
  public static final int ENCODING_DTS_HD = 8;
  public static final int ENCODING_EVRC = 102;
  public static final int ENCODING_EVRCB = 103;
  public static final int ENCODING_EVRCNW = 105;
  public static final int ENCODING_EVRCWB = 104;
  public static final int ENCODING_E_AC3 = 6;
  public static final int ENCODING_E_AC3_JOC = 18;
  public static final int ENCODING_IEC61937 = 13;
  public static final int ENCODING_INVALID = 0;
  public static final int ENCODING_MP3 = 9;
  public static final int ENCODING_PCM_16BIT = 2;
  public static final int ENCODING_PCM_8BIT = 3;
  public static final int ENCODING_PCM_FLOAT = 4;
  public static final int SAMPLE_RATE_HZ_MAX = 192000;
  public static final int SAMPLE_RATE_HZ_MIN = 4000;
  public static final int SAMPLE_RATE_UNSPECIFIED = 0;
  public static final int[] SURROUND_SOUND_ENCODING = { 5, 6, 7, 8, 10, 14, 18 };
  private int mChannelIndexMask;
  private int mChannelMask;
  private int mEncoding;
  private int mPropertySetMask;
  private int mSampleRate;
  
  public AudioFormat()
  {
    throw new UnsupportedOperationException("There is no valid usage of this constructor");
  }
  
  private AudioFormat(int paramInt) {}
  
  private AudioFormat(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    mEncoding = paramInt1;
    mSampleRate = paramInt2;
    mChannelMask = paramInt3;
    mChannelIndexMask = paramInt4;
    mPropertySetMask = 15;
  }
  
  private AudioFormat(Parcel paramParcel)
  {
    mPropertySetMask = paramParcel.readInt();
    mEncoding = paramParcel.readInt();
    mSampleRate = paramParcel.readInt();
    mChannelMask = paramParcel.readInt();
    mChannelIndexMask = paramParcel.readInt();
  }
  
  public static int channelCountFromInChannelMask(int paramInt)
  {
    return Integer.bitCount(paramInt);
  }
  
  public static int channelCountFromOutChannelMask(int paramInt)
  {
    return Integer.bitCount(paramInt);
  }
  
  public static int convertChannelOutMaskToNativeMask(int paramInt)
  {
    return paramInt >> 2;
  }
  
  public static int convertNativeChannelMaskToOutMask(int paramInt)
  {
    return paramInt << 2;
  }
  
  public static int[] filterPublicFormats(int[] paramArrayOfInt)
  {
    if (paramArrayOfInt == null) {
      return null;
    }
    paramArrayOfInt = Arrays.copyOf(paramArrayOfInt, paramArrayOfInt.length);
    int i = 0;
    int j = 0;
    while (j < paramArrayOfInt.length)
    {
      int k = i;
      if (isPublicEncoding(paramArrayOfInt[j]))
      {
        if (i != j) {
          paramArrayOfInt[i] = paramArrayOfInt[j];
        }
        k = i + 1;
      }
      j++;
      i = k;
    }
    return Arrays.copyOf(paramArrayOfInt, i);
  }
  
  public static int getBytesPerSample(int paramInt)
  {
    if (paramInt != 13) {
      switch (paramInt)
      {
      default: 
        switch (paramInt)
        {
        default: 
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("Bad audio format ");
          localStringBuilder.append(paramInt);
          throw new IllegalArgumentException(localStringBuilder.toString());
        case 102: 
        case 103: 
        case 104: 
        case 105: 
          return 23;
        case 101: 
          return 61;
        }
        return 32;
      case 4: 
        return 4;
      case 3: 
        return 1;
      }
    }
    return 2;
  }
  
  public static int inChannelMaskFromOutChannelMask(int paramInt)
    throws IllegalArgumentException
  {
    if (paramInt != 1)
    {
      switch (channelCountFromOutChannelMask(paramInt))
      {
      default: 
        throw new IllegalArgumentException("Unsupported channel configuration for input.");
      case 2: 
        return 12;
      }
      return 16;
    }
    throw new IllegalArgumentException("Illegal CHANNEL_OUT_DEFAULT channel mask for input.");
  }
  
  public static boolean isEncodingLinearFrames(int paramInt)
  {
    switch (paramInt)
    {
    case 14: 
    default: 
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Bad audio format ");
      localStringBuilder.append(paramInt);
      throw new IllegalArgumentException(localStringBuilder.toString());
    case 5: 
    case 6: 
    case 7: 
    case 8: 
    case 9: 
    case 10: 
    case 11: 
    case 12: 
    case 15: 
    case 16: 
    case 17: 
    case 18: 
      return false;
    }
    return true;
  }
  
  public static boolean isEncodingLinearPcm(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      switch (paramInt)
      {
      default: 
        switch (paramInt)
        {
        default: 
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("Bad audio format ");
          localStringBuilder.append(paramInt);
          throw new IllegalArgumentException(localStringBuilder.toString());
        }
        break;
      }
    case 5: 
    case 6: 
    case 7: 
    case 8: 
    case 9: 
    case 10: 
    case 11: 
    case 12: 
    case 13: 
      return false;
    }
    return true;
  }
  
  public static boolean isPublicEncoding(int paramInt)
  {
    switch (paramInt)
    {
    case 14: 
    default: 
      return false;
    }
    return true;
  }
  
  public static boolean isValidEncoding(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      switch (paramInt)
      {
      default: 
        switch (paramInt)
        {
        default: 
          return false;
        }
        break;
      }
      break;
    }
    return true;
  }
  
  public static String toDisplayName(int paramInt)
  {
    if (paramInt != 10)
    {
      if (paramInt != 14)
      {
        if (paramInt != 18)
        {
          switch (paramInt)
          {
          default: 
            return "Unknown surround sound format";
          case 8: 
            return "DTS HD";
          case 7: 
            return "DTS";
          case 6: 
            return "Dolby Digital Plus (E_AC3)";
          }
          return "Dolby Digital (AC3)";
        }
        return "Dolby Atmos";
      }
      return "Dolby TrueHD";
    }
    return "AAC";
  }
  
  public static String toLogFriendlyEncoding(int paramInt)
  {
    if (paramInt != 0)
    {
      switch (paramInt)
      {
      default: 
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("invalid encoding ");
        localStringBuilder.append(paramInt);
        return localStringBuilder.toString();
      case 17: 
        return "ENCODING_AC4";
      case 16: 
        return "ENCODING_AAC_XHE";
      case 15: 
        return "ENCODING_AAC_ELD";
      case 14: 
        return "ENCODING_DOLBY_TRUEHD";
      case 13: 
        return "ENCODING_IEC61937";
      case 12: 
        return "ENCODING_AAC_HE_V2";
      case 11: 
        return "ENCODING_AAC_HE_V1";
      case 10: 
        return "ENCODING_AAC_LC";
      case 9: 
        return "ENCODING_MP3";
      case 8: 
        return "ENCODING_DTS_HD";
      case 7: 
        return "ENCODING_DTS";
      case 6: 
        return "ENCODING_E_AC3";
      case 5: 
        return "ENCODING_AC3";
      case 4: 
        return "ENCODING_PCM_FLOAT";
      case 3: 
        return "ENCODING_PCM_8BIT";
      }
      return "ENCODING_PCM_16BIT";
    }
    return "ENCODING_INVALID";
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool1 = true;
    if (this == paramObject) {
      return true;
    }
    if ((paramObject != null) && (getClass() == paramObject.getClass()))
    {
      paramObject = (AudioFormat)paramObject;
      if (mPropertySetMask != mPropertySetMask) {
        return false;
      }
      if ((((mPropertySetMask & 0x1) == 0) || (mEncoding == mEncoding)) && (((mPropertySetMask & 0x2) == 0) || (mSampleRate == mSampleRate)) && (((mPropertySetMask & 0x4) == 0) || (mChannelMask == mChannelMask)))
      {
        bool2 = bool1;
        if ((mPropertySetMask & 0x8) == 0) {
          break label135;
        }
        if (mChannelIndexMask == mChannelIndexMask)
        {
          bool2 = bool1;
          break label135;
        }
      }
      boolean bool2 = false;
      label135:
      return bool2;
    }
    return false;
  }
  
  public int getChannelCount()
  {
    int i = Integer.bitCount(getChannelIndexMask());
    int j = channelCountFromOutChannelMask(getChannelMask());
    int k;
    if (j == 0)
    {
      k = i;
    }
    else
    {
      k = j;
      if (j != i)
      {
        k = j;
        if (i != 0) {
          k = 0;
        }
      }
    }
    return k;
  }
  
  public int getChannelIndexMask()
  {
    if ((mPropertySetMask & 0x8) == 0) {
      return 0;
    }
    return mChannelIndexMask;
  }
  
  public int getChannelMask()
  {
    if ((mPropertySetMask & 0x4) == 0) {
      return 0;
    }
    return mChannelMask;
  }
  
  public int getEncoding()
  {
    if ((mPropertySetMask & 0x1) == 0) {
      return 0;
    }
    return mEncoding;
  }
  
  public int getPropertySetMask()
  {
    return mPropertySetMask;
  }
  
  public int getSampleRate()
  {
    return mSampleRate;
  }
  
  public int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(mPropertySetMask), Integer.valueOf(mSampleRate), Integer.valueOf(mEncoding), Integer.valueOf(mChannelMask), Integer.valueOf(mChannelIndexMask) });
  }
  
  public String toLogFriendlyString()
  {
    return String.format("%dch %dHz %s", new Object[] { Integer.valueOf(getChannelCount()), Integer.valueOf(mSampleRate), toLogFriendlyEncoding(mEncoding) });
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("AudioFormat: props=");
    localStringBuilder.append(mPropertySetMask);
    localStringBuilder.append(" enc=");
    localStringBuilder.append(mEncoding);
    localStringBuilder.append(" chan=0x");
    localStringBuilder.append(Integer.toHexString(mChannelMask).toUpperCase());
    localStringBuilder.append(" chan_index=0x");
    localStringBuilder.append(Integer.toHexString(mChannelIndexMask).toUpperCase());
    localStringBuilder.append(" rate=");
    localStringBuilder.append(mSampleRate);
    return new String(localStringBuilder.toString());
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mPropertySetMask);
    paramParcel.writeInt(mEncoding);
    paramParcel.writeInt(mSampleRate);
    paramParcel.writeInt(mChannelMask);
    paramParcel.writeInt(mChannelIndexMask);
  }
  
  public static class Builder
  {
    private int mChannelIndexMask = 0;
    private int mChannelMask = 0;
    private int mEncoding = 0;
    private int mPropertySetMask = 0;
    private int mSampleRate = 0;
    
    public Builder() {}
    
    public Builder(AudioFormat paramAudioFormat)
    {
      mEncoding = mEncoding;
      mSampleRate = mSampleRate;
      mChannelMask = mChannelMask;
      mChannelIndexMask = mChannelIndexMask;
      mPropertySetMask = mPropertySetMask;
    }
    
    public AudioFormat build()
    {
      AudioFormat localAudioFormat = new AudioFormat(1980, null);
      AudioFormat.access$002(localAudioFormat, mEncoding);
      AudioFormat.access$102(localAudioFormat, mSampleRate);
      AudioFormat.access$202(localAudioFormat, mChannelMask);
      AudioFormat.access$302(localAudioFormat, mChannelIndexMask);
      AudioFormat.access$402(localAudioFormat, mPropertySetMask);
      return localAudioFormat;
    }
    
    public Builder setChannelIndexMask(int paramInt)
    {
      if (paramInt != 0)
      {
        if ((mChannelMask != 0) && (Integer.bitCount(paramInt) != Integer.bitCount(mChannelMask)))
        {
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("Mismatched channel count for index mask ");
          localStringBuilder.append(Integer.toHexString(paramInt).toUpperCase());
          throw new IllegalArgumentException(localStringBuilder.toString());
        }
        mChannelIndexMask = paramInt;
        mPropertySetMask |= 0x8;
        return this;
      }
      throw new IllegalArgumentException("Invalid zero channel index mask");
    }
    
    public Builder setChannelMask(int paramInt)
    {
      if (paramInt != 0)
      {
        if ((mChannelIndexMask != 0) && (Integer.bitCount(paramInt) != Integer.bitCount(mChannelIndexMask)))
        {
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("Mismatched channel count for mask ");
          localStringBuilder.append(Integer.toHexString(paramInt).toUpperCase());
          throw new IllegalArgumentException(localStringBuilder.toString());
        }
        mChannelMask = paramInt;
        mPropertySetMask |= 0x4;
        return this;
      }
      throw new IllegalArgumentException("Invalid zero channel mask");
    }
    
    public Builder setEncoding(int paramInt)
      throws IllegalArgumentException
    {
      switch (paramInt)
      {
      default: 
        switch (paramInt)
        {
        default: 
          switch (paramInt)
          {
          default: 
            StringBuilder localStringBuilder = new StringBuilder();
            localStringBuilder.append("Invalid encoding ");
            localStringBuilder.append(paramInt);
            throw new IllegalArgumentException(localStringBuilder.toString());
          }
          break;
        }
      case 2: 
      case 3: 
      case 4: 
      case 5: 
      case 6: 
      case 7: 
      case 8: 
      case 9: 
      case 10: 
      case 11: 
      case 12: 
      case 13: 
        mEncoding = paramInt;
        break;
      case 1: 
        mEncoding = 2;
      }
      mPropertySetMask |= 0x1;
      return this;
    }
    
    public Builder setSampleRate(int paramInt)
      throws IllegalArgumentException
    {
      if (((paramInt >= 4000) && (paramInt <= 192000)) || (paramInt == 0))
      {
        mSampleRate = paramInt;
        mPropertySetMask |= 0x2;
        return this;
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Invalid sample rate ");
      localStringBuilder.append(paramInt);
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface Encoding {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface SurroundSoundEncoding {}
}
