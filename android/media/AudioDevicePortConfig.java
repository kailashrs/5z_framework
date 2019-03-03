package android.media;

public class AudioDevicePortConfig
  extends AudioPortConfig
{
  AudioDevicePortConfig(AudioDevicePort paramAudioDevicePort, int paramInt1, int paramInt2, int paramInt3, AudioGainConfig paramAudioGainConfig)
  {
    super(paramAudioDevicePort, paramInt1, paramInt2, paramInt3, paramAudioGainConfig);
  }
  
  AudioDevicePortConfig(AudioDevicePortConfig paramAudioDevicePortConfig)
  {
    this(paramAudioDevicePortConfig.port(), paramAudioDevicePortConfig.samplingRate(), paramAudioDevicePortConfig.channelMask(), paramAudioDevicePortConfig.format(), paramAudioDevicePortConfig.gain());
  }
  
  public AudioDevicePort port()
  {
    return (AudioDevicePort)mPort;
  }
}
