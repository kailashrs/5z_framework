package android.media;

public class AudioPatch
{
  private final AudioHandle mHandle;
  private final AudioPortConfig[] mSinks;
  private final AudioPortConfig[] mSources;
  
  AudioPatch(AudioHandle paramAudioHandle, AudioPortConfig[] paramArrayOfAudioPortConfig1, AudioPortConfig[] paramArrayOfAudioPortConfig2)
  {
    mHandle = paramAudioHandle;
    mSources = paramArrayOfAudioPortConfig1;
    mSinks = paramArrayOfAudioPortConfig2;
  }
  
  public int id()
  {
    return mHandle.id();
  }
  
  public AudioPortConfig[] sinks()
  {
    return mSinks;
  }
  
  public AudioPortConfig[] sources()
  {
    return mSources;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("mHandle: ");
    localStringBuilder.append(mHandle.toString());
    localStringBuilder.append(" mSources: {");
    AudioPortConfig[] arrayOfAudioPortConfig = mSources;
    int i = arrayOfAudioPortConfig.length;
    int j = 0;
    for (int k = 0; k < i; k++)
    {
      localStringBuilder.append(arrayOfAudioPortConfig[k].toString());
      localStringBuilder.append(", ");
    }
    localStringBuilder.append("} mSinks: {");
    arrayOfAudioPortConfig = mSinks;
    i = arrayOfAudioPortConfig.length;
    for (k = j; k < i; k++)
    {
      localStringBuilder.append(arrayOfAudioPortConfig[k].toString());
      localStringBuilder.append(", ");
    }
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
}
