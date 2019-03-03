package android.media;

public class AudioDevicePort
  extends AudioPort
{
  private final String mAddress;
  private final int mType;
  
  AudioDevicePort(AudioHandle paramAudioHandle, String paramString1, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int[] paramArrayOfInt3, int[] paramArrayOfInt4, AudioGain[] paramArrayOfAudioGain, int paramInt, String paramString2)
  {
    super(paramAudioHandle, i, paramString1, paramArrayOfInt1, paramArrayOfInt2, paramArrayOfInt3, paramArrayOfInt4, paramArrayOfAudioGain);
    mType = paramInt;
    mAddress = paramString2;
  }
  
  public String address()
  {
    return mAddress;
  }
  
  public AudioDevicePortConfig buildConfig(int paramInt1, int paramInt2, int paramInt3, AudioGainConfig paramAudioGainConfig)
  {
    return new AudioDevicePortConfig(this, paramInt1, paramInt2, paramInt3, paramAudioGainConfig);
  }
  
  public boolean equals(Object paramObject)
  {
    if ((paramObject != null) && ((paramObject instanceof AudioDevicePort)))
    {
      AudioDevicePort localAudioDevicePort = (AudioDevicePort)paramObject;
      if (mType != localAudioDevicePort.type()) {
        return false;
      }
      if ((mAddress == null) && (localAudioDevicePort.address() != null)) {
        return false;
      }
      if (!mAddress.equals(localAudioDevicePort.address())) {
        return false;
      }
      return super.equals(paramObject);
    }
    return false;
  }
  
  public String toString()
  {
    String str;
    if (mRole == 1) {
      str = AudioSystem.getInputDeviceName(mType);
    } else {
      str = AudioSystem.getOutputDeviceName(mType);
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{");
    localStringBuilder.append(super.toString());
    localStringBuilder.append(", mType: ");
    localStringBuilder.append(str);
    localStringBuilder.append(", mAddress: ");
    localStringBuilder.append(mAddress);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public int type()
  {
    return mType;
  }
}
