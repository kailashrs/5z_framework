package android.media;

public class AudioMixPortConfig
  extends AudioPortConfig
{
  AudioMixPortConfig(AudioMixPort paramAudioMixPort, int paramInt1, int paramInt2, int paramInt3, AudioGainConfig paramAudioGainConfig)
  {
    super(paramAudioMixPort, paramInt1, paramInt2, paramInt3, paramAudioGainConfig);
  }
  
  public AudioMixPort port()
  {
    return (AudioMixPort)mPort;
  }
}
