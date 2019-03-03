package android.media;

public abstract class AudioDeviceCallback
{
  public AudioDeviceCallback() {}
  
  public void onAudioDevicesAdded(AudioDeviceInfo[] paramArrayOfAudioDeviceInfo) {}
  
  public void onAudioDevicesRemoved(AudioDeviceInfo[] paramArrayOfAudioDeviceInfo) {}
}
