package android.media.audiopolicy;

import android.annotation.SystemApi;
import android.media.AudioDeviceInfo;
import android.media.AudioFormat;
import android.media.AudioFormat.Builder;
import android.media.AudioSystem;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Objects;

@SystemApi
public class AudioMix
{
  private static final int CALLBACK_FLAGS_ALL = 1;
  public static final int CALLBACK_FLAG_NOTIFY_ACTIVITY = 1;
  @SystemApi
  public static final int MIX_STATE_DISABLED = -1;
  @SystemApi
  public static final int MIX_STATE_IDLE = 0;
  @SystemApi
  public static final int MIX_STATE_MIXING = 1;
  public static final int MIX_TYPE_INVALID = -1;
  public static final int MIX_TYPE_PLAYERS = 0;
  public static final int MIX_TYPE_RECORDERS = 1;
  @SystemApi
  public static final int ROUTE_FLAG_LOOP_BACK = 2;
  @SystemApi
  public static final int ROUTE_FLAG_RENDER = 1;
  private static final int ROUTE_FLAG_SUPPORTED = 3;
  int mCallbackFlags;
  String mDeviceAddress;
  final int mDeviceSystemType;
  private AudioFormat mFormat;
  int mMixState = -1;
  private int mMixType = -1;
  private int mRouteFlags;
  private AudioMixingRule mRule;
  
  private AudioMix(AudioMixingRule paramAudioMixingRule, AudioFormat paramAudioFormat, int paramInt1, int paramInt2, int paramInt3, String paramString)
  {
    mRule = paramAudioMixingRule;
    mFormat = paramAudioFormat;
    mRouteFlags = paramInt1;
    mMixType = paramAudioMixingRule.getTargetMixType();
    mCallbackFlags = paramInt2;
    mDeviceSystemType = paramInt3;
    if (paramString == null) {
      paramAudioMixingRule = new String("");
    } else {
      paramAudioMixingRule = paramString;
    }
    mDeviceAddress = paramAudioMixingRule;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool = true;
    if (this == paramObject) {
      return true;
    }
    if ((paramObject != null) && (getClass() == paramObject.getClass()))
    {
      paramObject = (AudioMix)paramObject;
      if ((mRouteFlags != mRouteFlags) || (mRule != mRule) || (mMixType != mMixType) || (mFormat != mFormat)) {
        bool = false;
      }
      return bool;
    }
    return false;
  }
  
  AudioFormat getFormat()
  {
    return mFormat;
  }
  
  @SystemApi
  public int getMixState()
  {
    return mMixState;
  }
  
  public int getMixType()
  {
    return mMixType;
  }
  
  public String getRegistration()
  {
    return mDeviceAddress;
  }
  
  int getRouteFlags()
  {
    return mRouteFlags;
  }
  
  AudioMixingRule getRule()
  {
    return mRule;
  }
  
  public int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(mRouteFlags), mRule, Integer.valueOf(mMixType), mFormat });
  }
  
  public boolean isAffectingUsage(int paramInt)
  {
    return mRule.isAffectingUsage(paramInt);
  }
  
  void setRegistration(String paramString)
  {
    mDeviceAddress = paramString;
  }
  
  @SystemApi
  public static class Builder
  {
    private int mCallbackFlags = 0;
    private String mDeviceAddress = null;
    private int mDeviceSystemType = 0;
    private AudioFormat mFormat = null;
    private int mRouteFlags = 0;
    private AudioMixingRule mRule = null;
    
    Builder() {}
    
    @SystemApi
    public Builder(AudioMixingRule paramAudioMixingRule)
      throws IllegalArgumentException
    {
      if (paramAudioMixingRule != null)
      {
        mRule = paramAudioMixingRule;
        return;
      }
      throw new IllegalArgumentException("Illegal null AudioMixingRule argument");
    }
    
    @SystemApi
    public AudioMix build()
      throws IllegalArgumentException
    {
      if (mRule != null)
      {
        if (mRouteFlags == 0) {
          mRouteFlags = 2;
        }
        if (mRouteFlags != 3)
        {
          if (mFormat == null)
          {
            int i = AudioSystem.getPrimaryOutputSamplingRate();
            int j = i;
            if (i <= 0) {
              j = 44100;
            }
            mFormat = new AudioFormat.Builder().setSampleRate(j).build();
          }
          if ((mDeviceSystemType != 0) && (mDeviceSystemType != 32768) && (mDeviceSystemType != -2147483392))
          {
            if ((mRouteFlags & 0x1) != 0)
            {
              if (mRule.getTargetMixType() != 0) {
                throw new IllegalArgumentException("Unsupported device on non-playback mix");
              }
            }
            else {
              throw new IllegalArgumentException("Can't have audio device without flag ROUTE_FLAG_RENDER");
            }
          }
          else
          {
            if ((mRouteFlags & 0x1) == 1) {
              break label234;
            }
            if ((mRouteFlags & 0x3) == 2) {
              if (mRule.getTargetMixType() == 0) {
                mDeviceSystemType = 32768;
              } else if (mRule.getTargetMixType() == 1) {
                mDeviceSystemType = -2147483392;
              } else {
                throw new IllegalArgumentException("Unknown mixing rule type");
              }
            }
          }
          return new AudioMix(mRule, mFormat, mRouteFlags, mCallbackFlags, mDeviceSystemType, mDeviceAddress, null);
          label234:
          throw new IllegalArgumentException("Can't have flag ROUTE_FLAG_RENDER without an audio device");
        }
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Unsupported route behavior combination 0x");
        localStringBuilder.append(Integer.toHexString(mRouteFlags));
        throw new IllegalArgumentException(localStringBuilder.toString());
      }
      throw new IllegalArgumentException("Illegal null AudioMixingRule");
    }
    
    Builder setCallbackFlags(int paramInt)
      throws IllegalArgumentException
    {
      if ((paramInt != 0) && ((paramInt & 0x1) == 0))
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Illegal callback flags 0x");
        localStringBuilder.append(Integer.toHexString(paramInt).toUpperCase());
        throw new IllegalArgumentException(localStringBuilder.toString());
      }
      mCallbackFlags = paramInt;
      return this;
    }
    
    Builder setDevice(int paramInt, String paramString)
    {
      mDeviceSystemType = paramInt;
      mDeviceAddress = paramString;
      return this;
    }
    
    @SystemApi
    public Builder setDevice(AudioDeviceInfo paramAudioDeviceInfo)
      throws IllegalArgumentException
    {
      if (paramAudioDeviceInfo != null)
      {
        if (paramAudioDeviceInfo.isSink())
        {
          mDeviceSystemType = AudioDeviceInfo.convertDeviceTypeToInternalDevice(paramAudioDeviceInfo.getType());
          mDeviceAddress = paramAudioDeviceInfo.getAddress();
          return this;
        }
        throw new IllegalArgumentException("Unsupported device type on mix, not a sink");
      }
      throw new IllegalArgumentException("Illegal null AudioDeviceInfo argument");
    }
    
    @SystemApi
    public Builder setFormat(AudioFormat paramAudioFormat)
      throws IllegalArgumentException
    {
      if (paramAudioFormat != null)
      {
        mFormat = paramAudioFormat;
        return this;
      }
      throw new IllegalArgumentException("Illegal null AudioFormat argument");
    }
    
    Builder setMixingRule(AudioMixingRule paramAudioMixingRule)
      throws IllegalArgumentException
    {
      if (paramAudioMixingRule != null)
      {
        mRule = paramAudioMixingRule;
        return this;
      }
      throw new IllegalArgumentException("Illegal null AudioMixingRule argument");
    }
    
    @SystemApi
    public Builder setRouteFlags(int paramInt)
      throws IllegalArgumentException
    {
      if (paramInt != 0)
      {
        if ((paramInt & 0x3) != 0)
        {
          if ((paramInt & 0xFFFFFFFC) == 0)
          {
            mRouteFlags = paramInt;
            return this;
          }
          localStringBuilder = new StringBuilder();
          localStringBuilder.append("Unknown route flags 0x");
          localStringBuilder.append(Integer.toHexString(paramInt));
          localStringBuilder.append("when configuring an AudioMix");
          throw new IllegalArgumentException(localStringBuilder.toString());
        }
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Invalid route flags 0x");
        localStringBuilder.append(Integer.toHexString(paramInt));
        localStringBuilder.append("when configuring an AudioMix");
        throw new IllegalArgumentException(localStringBuilder.toString());
      }
      throw new IllegalArgumentException("Illegal empty route flags");
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface RouteFlags {}
}
