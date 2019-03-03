package android.media;

public class AudioGainConfig
{
  private final int mChannelMask;
  AudioGain mGain;
  private final int mIndex;
  private final int mMode;
  private final int mRampDurationMs;
  private final int[] mValues;
  
  AudioGainConfig(int paramInt1, AudioGain paramAudioGain, int paramInt2, int paramInt3, int[] paramArrayOfInt, int paramInt4)
  {
    mIndex = paramInt1;
    mGain = paramAudioGain;
    mMode = paramInt2;
    mChannelMask = paramInt3;
    mValues = paramArrayOfInt;
    mRampDurationMs = paramInt4;
  }
  
  public int channelMask()
  {
    return mChannelMask;
  }
  
  int index()
  {
    return mIndex;
  }
  
  public int mode()
  {
    return mMode;
  }
  
  public int rampDurationMs()
  {
    return mRampDurationMs;
  }
  
  public int[] values()
  {
    return mValues;
  }
}
