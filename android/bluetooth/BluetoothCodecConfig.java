package android.bluetooth;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.Objects;

public final class BluetoothCodecConfig
  implements Parcelable
{
  public static final int BITS_PER_SAMPLE_16 = 1;
  public static final int BITS_PER_SAMPLE_24 = 2;
  public static final int BITS_PER_SAMPLE_32 = 4;
  public static final int BITS_PER_SAMPLE_NONE = 0;
  public static final int CHANNEL_MODE_MONO = 1;
  public static final int CHANNEL_MODE_NONE = 0;
  public static final int CHANNEL_MODE_STEREO = 2;
  public static final int CODEC_PRIORITY_DEFAULT = 0;
  public static final int CODEC_PRIORITY_DISABLED = -1;
  public static final int CODEC_PRIORITY_HIGHEST = 1000000;
  public static final Parcelable.Creator<BluetoothCodecConfig> CREATOR = new Parcelable.Creator()
  {
    public BluetoothCodecConfig createFromParcel(Parcel paramAnonymousParcel)
    {
      return new BluetoothCodecConfig(paramAnonymousParcel.readInt(), paramAnonymousParcel.readInt(), paramAnonymousParcel.readInt(), paramAnonymousParcel.readInt(), paramAnonymousParcel.readInt(), paramAnonymousParcel.readLong(), paramAnonymousParcel.readLong(), paramAnonymousParcel.readLong(), paramAnonymousParcel.readLong());
    }
    
    public BluetoothCodecConfig[] newArray(int paramAnonymousInt)
    {
      return new BluetoothCodecConfig[paramAnonymousInt];
    }
  };
  public static final int SAMPLE_RATE_176400 = 16;
  public static final int SAMPLE_RATE_192000 = 32;
  public static final int SAMPLE_RATE_44100 = 1;
  public static final int SAMPLE_RATE_48000 = 2;
  public static final int SAMPLE_RATE_88200 = 4;
  public static final int SAMPLE_RATE_96000 = 8;
  public static final int SAMPLE_RATE_NONE = 0;
  public static final int SOURCE_CODEC_TYPE_AAC = 1;
  public static final int SOURCE_CODEC_TYPE_APTX = 2;
  public static final int SOURCE_CODEC_TYPE_APTX_ADAPTIVE = 4;
  public static final int SOURCE_CODEC_TYPE_APTX_HD = 3;
  public static final int SOURCE_CODEC_TYPE_INVALID = 1000000;
  public static final int SOURCE_CODEC_TYPE_LDAC = 5;
  public static final int SOURCE_CODEC_TYPE_MAX = 6;
  public static final int SOURCE_CODEC_TYPE_SBC = 0;
  private final int mBitsPerSample;
  private final int mChannelMode;
  private int mCodecPriority;
  private final long mCodecSpecific1;
  private final long mCodecSpecific2;
  private final long mCodecSpecific3;
  private final long mCodecSpecific4;
  private final int mCodecType;
  private final int mSampleRate;
  
  public BluetoothCodecConfig(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, long paramLong1, long paramLong2, long paramLong3, long paramLong4)
  {
    mCodecType = paramInt1;
    mCodecPriority = paramInt2;
    mSampleRate = paramInt3;
    mBitsPerSample = paramInt4;
    mChannelMode = paramInt5;
    mCodecSpecific1 = paramLong1;
    mCodecSpecific2 = paramLong2;
    mCodecSpecific3 = paramLong3;
    mCodecSpecific4 = paramLong4;
  }
  
  private static String appendCapabilityToString(String paramString1, String paramString2)
  {
    if (paramString1 == null) {
      return paramString2;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString1);
    localStringBuilder.append("|");
    localStringBuilder.append(paramString2);
    return localStringBuilder.toString();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool1 = paramObject instanceof BluetoothCodecConfig;
    boolean bool2 = false;
    if (bool1)
    {
      paramObject = (BluetoothCodecConfig)paramObject;
      bool1 = bool2;
      if (mCodecType == mCodecType)
      {
        bool1 = bool2;
        if (mCodecPriority == mCodecPriority)
        {
          bool1 = bool2;
          if (mSampleRate == mSampleRate)
          {
            bool1 = bool2;
            if (mBitsPerSample == mBitsPerSample)
            {
              bool1 = bool2;
              if (mChannelMode == mChannelMode)
              {
                bool1 = bool2;
                if (mCodecSpecific1 == mCodecSpecific1)
                {
                  bool1 = bool2;
                  if (mCodecSpecific2 == mCodecSpecific2)
                  {
                    bool1 = bool2;
                    if (mCodecSpecific3 == mCodecSpecific3)
                    {
                      bool1 = bool2;
                      if (mCodecSpecific4 == mCodecSpecific4) {
                        bool1 = true;
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }
      return bool1;
    }
    return false;
  }
  
  public int getBitsPerSample()
  {
    return mBitsPerSample;
  }
  
  public int getChannelMode()
  {
    return mChannelMode;
  }
  
  public String getCodecName()
  {
    int i = mCodecType;
    if (i != 1000000)
    {
      switch (i)
      {
      default: 
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("UNKNOWN CODEC(");
        localStringBuilder.append(mCodecType);
        localStringBuilder.append(")");
        return localStringBuilder.toString();
      case 5: 
        return "LDAC";
      case 4: 
        return "aptX Adaptive";
      case 3: 
        return "aptX HD";
      case 2: 
        return "aptX";
      case 1: 
        return "AAC";
      }
      return "SBC";
    }
    return "INVALID CODEC";
  }
  
  public int getCodecPriority()
  {
    return mCodecPriority;
  }
  
  public long getCodecSpecific1()
  {
    return mCodecSpecific1;
  }
  
  public long getCodecSpecific2()
  {
    return mCodecSpecific2;
  }
  
  public long getCodecSpecific3()
  {
    return mCodecSpecific3;
  }
  
  public long getCodecSpecific4()
  {
    return mCodecSpecific4;
  }
  
  public int getCodecType()
  {
    return mCodecType;
  }
  
  public int getSampleRate()
  {
    return mSampleRate;
  }
  
  public int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(mCodecType), Integer.valueOf(mCodecPriority), Integer.valueOf(mSampleRate), Integer.valueOf(mBitsPerSample), Integer.valueOf(mChannelMode), Long.valueOf(mCodecSpecific1), Long.valueOf(mCodecSpecific2), Long.valueOf(mCodecSpecific3), Long.valueOf(mCodecSpecific4) });
  }
  
  public boolean isMandatoryCodec()
  {
    boolean bool;
    if (mCodecType == 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isValid()
  {
    boolean bool;
    if ((mSampleRate != 0) && (mBitsPerSample != 0) && (mChannelMode != 0)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean sameAudioFeedingParameters(BluetoothCodecConfig paramBluetoothCodecConfig)
  {
    boolean bool;
    if ((paramBluetoothCodecConfig != null) && (mSampleRate == mSampleRate) && (mBitsPerSample == mBitsPerSample) && (mChannelMode == mChannelMode)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void setCodecPriority(int paramInt)
  {
    mCodecPriority = paramInt;
  }
  
  public String toString()
  {
    Object localObject1 = null;
    if (mSampleRate == 0) {
      localObject1 = appendCapabilityToString(null, "NONE");
    }
    Object localObject2 = localObject1;
    if ((mSampleRate & 0x1) != 0) {
      localObject2 = appendCapabilityToString((String)localObject1, "44100");
    }
    localObject1 = localObject2;
    if ((mSampleRate & 0x2) != 0) {
      localObject1 = appendCapabilityToString((String)localObject2, "48000");
    }
    Object localObject3 = localObject1;
    if ((mSampleRate & 0x4) != 0) {
      localObject3 = appendCapabilityToString((String)localObject1, "88200");
    }
    localObject2 = localObject3;
    if ((mSampleRate & 0x8) != 0) {
      localObject2 = appendCapabilityToString((String)localObject3, "96000");
    }
    localObject1 = localObject2;
    if ((mSampleRate & 0x10) != 0) {
      localObject1 = appendCapabilityToString((String)localObject2, "176400");
    }
    localObject3 = localObject1;
    if ((mSampleRate & 0x20) != 0) {
      localObject3 = appendCapabilityToString((String)localObject1, "192000");
    }
    localObject1 = null;
    if (mBitsPerSample == 0) {
      localObject1 = appendCapabilityToString(null, "NONE");
    }
    localObject2 = localObject1;
    if ((mBitsPerSample & 0x1) != 0) {
      localObject2 = appendCapabilityToString((String)localObject1, "16");
    }
    localObject1 = localObject2;
    if ((mBitsPerSample & 0x2) != 0) {
      localObject1 = appendCapabilityToString((String)localObject2, "24");
    }
    Object localObject4 = localObject1;
    if ((mBitsPerSample & 0x4) != 0) {
      localObject4 = appendCapabilityToString((String)localObject1, "32");
    }
    localObject2 = null;
    if (mChannelMode == 0) {
      localObject2 = appendCapabilityToString(null, "NONE");
    }
    localObject1 = localObject2;
    if ((mChannelMode & 0x1) != 0) {
      localObject1 = appendCapabilityToString((String)localObject2, "MONO");
    }
    localObject2 = localObject1;
    if ((mChannelMode & 0x2) != 0) {
      localObject2 = appendCapabilityToString((String)localObject1, "STEREO");
    }
    localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append("{codecName:");
    ((StringBuilder)localObject1).append(getCodecName());
    ((StringBuilder)localObject1).append(",mCodecType:");
    ((StringBuilder)localObject1).append(mCodecType);
    ((StringBuilder)localObject1).append(",mCodecPriority:");
    ((StringBuilder)localObject1).append(mCodecPriority);
    ((StringBuilder)localObject1).append(",mSampleRate:");
    ((StringBuilder)localObject1).append(String.format("0x%x", new Object[] { Integer.valueOf(mSampleRate) }));
    ((StringBuilder)localObject1).append("(");
    ((StringBuilder)localObject1).append((String)localObject3);
    ((StringBuilder)localObject1).append("),mBitsPerSample:");
    ((StringBuilder)localObject1).append(String.format("0x%x", new Object[] { Integer.valueOf(mBitsPerSample) }));
    ((StringBuilder)localObject1).append("(");
    ((StringBuilder)localObject1).append((String)localObject4);
    ((StringBuilder)localObject1).append("),mChannelMode:");
    ((StringBuilder)localObject1).append(String.format("0x%x", new Object[] { Integer.valueOf(mChannelMode) }));
    ((StringBuilder)localObject1).append("(");
    ((StringBuilder)localObject1).append((String)localObject2);
    ((StringBuilder)localObject1).append("),mCodecSpecific1:");
    ((StringBuilder)localObject1).append(mCodecSpecific1);
    ((StringBuilder)localObject1).append(",mCodecSpecific2:");
    ((StringBuilder)localObject1).append(mCodecSpecific2);
    ((StringBuilder)localObject1).append(",mCodecSpecific3:");
    ((StringBuilder)localObject1).append(mCodecSpecific3);
    ((StringBuilder)localObject1).append(",mCodecSpecific4:");
    ((StringBuilder)localObject1).append(mCodecSpecific4);
    ((StringBuilder)localObject1).append("}");
    return ((StringBuilder)localObject1).toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mCodecType);
    paramParcel.writeInt(mCodecPriority);
    paramParcel.writeInt(mSampleRate);
    paramParcel.writeInt(mBitsPerSample);
    paramParcel.writeInt(mChannelMode);
    paramParcel.writeLong(mCodecSpecific1);
    paramParcel.writeLong(mCodecSpecific2);
    paramParcel.writeLong(mCodecSpecific3);
    paramParcel.writeLong(mCodecSpecific4);
  }
}
