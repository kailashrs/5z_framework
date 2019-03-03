package android.media;

public class AudioMixPort
  extends AudioPort
{
  private final int mIoHandle;
  
  AudioMixPort(AudioHandle paramAudioHandle, int paramInt1, int paramInt2, String paramString, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int[] paramArrayOfInt3, int[] paramArrayOfInt4, AudioGain[] paramArrayOfAudioGain)
  {
    super(paramAudioHandle, paramInt2, paramString, paramArrayOfInt1, paramArrayOfInt2, paramArrayOfInt3, paramArrayOfInt4, paramArrayOfAudioGain);
    mIoHandle = paramInt1;
  }
  
  public AudioMixPortConfig buildConfig(int paramInt1, int paramInt2, int paramInt3, AudioGainConfig paramAudioGainConfig)
  {
    return new AudioMixPortConfig(this, paramInt1, paramInt2, paramInt3, paramAudioGainConfig);
  }
  
  public boolean equals(Object paramObject)
  {
    if ((paramObject != null) && ((paramObject instanceof AudioMixPort)))
    {
      AudioMixPort localAudioMixPort = (AudioMixPort)paramObject;
      if (mIoHandle != localAudioMixPort.ioHandle()) {
        return false;
      }
      return super.equals(paramObject);
    }
    return false;
  }
  
  public int ioHandle()
  {
    return mIoHandle;
  }
}
