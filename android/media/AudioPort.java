package android.media;

public class AudioPort
{
  public static final int ROLE_NONE = 0;
  public static final int ROLE_SINK = 2;
  public static final int ROLE_SOURCE = 1;
  private static final String TAG = "AudioPort";
  public static final int TYPE_DEVICE = 1;
  public static final int TYPE_NONE = 0;
  public static final int TYPE_SESSION = 3;
  public static final int TYPE_SUBMIX = 2;
  private AudioPortConfig mActiveConfig;
  private final int[] mChannelIndexMasks;
  private final int[] mChannelMasks;
  private final int[] mFormats;
  private final AudioGain[] mGains;
  AudioHandle mHandle;
  private final String mName;
  protected final int mRole;
  private final int[] mSamplingRates;
  
  AudioPort(AudioHandle paramAudioHandle, int paramInt, String paramString, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int[] paramArrayOfInt3, int[] paramArrayOfInt4, AudioGain[] paramArrayOfAudioGain)
  {
    mHandle = paramAudioHandle;
    mRole = paramInt;
    mName = paramString;
    mSamplingRates = paramArrayOfInt1;
    mChannelMasks = paramArrayOfInt2;
    mChannelIndexMasks = paramArrayOfInt3;
    mFormats = paramArrayOfInt4;
    mGains = paramArrayOfAudioGain;
  }
  
  public AudioPortConfig activeConfig()
  {
    return mActiveConfig;
  }
  
  public AudioPortConfig buildConfig(int paramInt1, int paramInt2, int paramInt3, AudioGainConfig paramAudioGainConfig)
  {
    return new AudioPortConfig(this, paramInt1, paramInt2, paramInt3, paramAudioGainConfig);
  }
  
  public int[] channelIndexMasks()
  {
    return mChannelIndexMasks;
  }
  
  public int[] channelMasks()
  {
    return mChannelMasks;
  }
  
  public boolean equals(Object paramObject)
  {
    if ((paramObject != null) && ((paramObject instanceof AudioPort)))
    {
      paramObject = (AudioPort)paramObject;
      return mHandle.equals(paramObject.handle());
    }
    return false;
  }
  
  public int[] formats()
  {
    return mFormats;
  }
  
  AudioGain gain(int paramInt)
  {
    if ((paramInt >= 0) && (paramInt < mGains.length)) {
      return mGains[paramInt];
    }
    return null;
  }
  
  public AudioGain[] gains()
  {
    return mGains;
  }
  
  AudioHandle handle()
  {
    return mHandle;
  }
  
  public int hashCode()
  {
    return mHandle.hashCode();
  }
  
  public int id()
  {
    return mHandle.id();
  }
  
  public String name()
  {
    return mName;
  }
  
  public int role()
  {
    return mRole;
  }
  
  public int[] samplingRates()
  {
    return mSamplingRates;
  }
  
  public String toString()
  {
    String str = Integer.toString(mRole);
    switch (mRole)
    {
    default: 
      break;
    case 2: 
      str = "SINK";
      break;
    case 1: 
      str = "SOURCE";
      break;
    case 0: 
      str = "NONE";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{mHandle: ");
    localStringBuilder.append(mHandle);
    localStringBuilder.append(", mRole: ");
    localStringBuilder.append(str);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
}
