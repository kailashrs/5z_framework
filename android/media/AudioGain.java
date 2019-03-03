package android.media;

public class AudioGain
{
  public static final int MODE_CHANNELS = 2;
  public static final int MODE_JOINT = 1;
  public static final int MODE_RAMP = 4;
  private final int mChannelMask;
  private final int mDefaultValue;
  private final int mIndex;
  private final int mMaxValue;
  private final int mMinValue;
  private final int mMode;
  private final int mRampDurationMaxMs;
  private final int mRampDurationMinMs;
  private final int mStepValue;
  
  AudioGain(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9)
  {
    mIndex = paramInt1;
    mMode = paramInt2;
    mChannelMask = paramInt3;
    mMinValue = paramInt4;
    mMaxValue = paramInt5;
    mDefaultValue = paramInt6;
    mStepValue = paramInt7;
    mRampDurationMinMs = paramInt8;
    mRampDurationMaxMs = paramInt9;
  }
  
  public AudioGainConfig buildConfig(int paramInt1, int paramInt2, int[] paramArrayOfInt, int paramInt3)
  {
    return new AudioGainConfig(mIndex, this, paramInt1, paramInt2, paramArrayOfInt, paramInt3);
  }
  
  public int channelMask()
  {
    return mChannelMask;
  }
  
  public int defaultValue()
  {
    return mDefaultValue;
  }
  
  public int maxValue()
  {
    return mMaxValue;
  }
  
  public int minValue()
  {
    return mMinValue;
  }
  
  public int mode()
  {
    return mMode;
  }
  
  public int rampDurationMaxMs()
  {
    return mRampDurationMaxMs;
  }
  
  public int rampDurationMinMs()
  {
    return mRampDurationMinMs;
  }
  
  public int stepValue()
  {
    return mStepValue;
  }
}
