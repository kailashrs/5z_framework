package android.media;

public class AudioPortConfig
{
  static final int CHANNEL_MASK = 2;
  static final int FORMAT = 4;
  static final int GAIN = 8;
  static final int SAMPLE_RATE = 1;
  private final int mChannelMask;
  int mConfigMask;
  private final int mFormat;
  private final AudioGainConfig mGain;
  final AudioPort mPort;
  private final int mSamplingRate;
  
  AudioPortConfig(AudioPort paramAudioPort, int paramInt1, int paramInt2, int paramInt3, AudioGainConfig paramAudioGainConfig)
  {
    mPort = paramAudioPort;
    mSamplingRate = paramInt1;
    mChannelMask = paramInt2;
    mFormat = paramInt3;
    mGain = paramAudioGainConfig;
    mConfigMask = 0;
  }
  
  public int channelMask()
  {
    return mChannelMask;
  }
  
  public int format()
  {
    return mFormat;
  }
  
  public AudioGainConfig gain()
  {
    return mGain;
  }
  
  public AudioPort port()
  {
    return mPort;
  }
  
  public int samplingRate()
  {
    return mSamplingRate;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{mPort:");
    localStringBuilder.append(mPort);
    localStringBuilder.append(", mSamplingRate:");
    localStringBuilder.append(mSamplingRate);
    localStringBuilder.append(", mChannelMask: ");
    localStringBuilder.append(mChannelMask);
    localStringBuilder.append(", mFormat:");
    localStringBuilder.append(mFormat);
    localStringBuilder.append(", mGain:");
    localStringBuilder.append(mGain);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
}
