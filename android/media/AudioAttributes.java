package android.media;

import android.annotation.SystemApi;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseIntArray;
import android.util.proto.ProtoOutputStream;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public final class AudioAttributes
  implements Parcelable
{
  private static final int ALL_PARCEL_FLAGS = 1;
  private static final int ATTR_PARCEL_IS_NULL_BUNDLE = -1977;
  private static final int ATTR_PARCEL_IS_VALID_BUNDLE = 1980;
  public static final int CONTENT_TYPE_MOVIE = 3;
  public static final int CONTENT_TYPE_MUSIC = 2;
  public static final int CONTENT_TYPE_SONIFICATION = 4;
  public static final int CONTENT_TYPE_SPEECH = 1;
  public static final int CONTENT_TYPE_UNKNOWN = 0;
  public static final Parcelable.Creator<AudioAttributes> CREATOR = new Parcelable.Creator()
  {
    public AudioAttributes createFromParcel(Parcel paramAnonymousParcel)
    {
      return new AudioAttributes(paramAnonymousParcel, null);
    }
    
    public AudioAttributes[] newArray(int paramAnonymousInt)
    {
      return new AudioAttributes[paramAnonymousInt];
    }
  };
  private static final int FLAG_ALL = 1023;
  private static final int FLAG_ALL_PUBLIC = 273;
  public static final int FLAG_AUDIBILITY_ENFORCED = 1;
  @SystemApi
  public static final int FLAG_BEACON = 8;
  @SystemApi
  public static final int FLAG_BYPASS_INTERRUPTION_POLICY = 64;
  @SystemApi
  public static final int FLAG_BYPASS_MUTE = 128;
  public static final int FLAG_DEEP_BUFFER = 512;
  public static final int FLAG_HW_AV_SYNC = 16;
  @SystemApi
  public static final int FLAG_HW_HOTWORD = 32;
  public static final int FLAG_LOW_LATENCY = 256;
  public static final int FLAG_SCO = 4;
  public static final int FLAG_SECURE = 2;
  public static final int FLATTEN_TAGS = 1;
  public static final int[] SDK_USAGES;
  public static final int SUPPRESSIBLE_ALARM = 4;
  public static final int SUPPRESSIBLE_CALL = 2;
  public static final int SUPPRESSIBLE_MEDIA = 5;
  public static final int SUPPRESSIBLE_NEVER = 3;
  public static final int SUPPRESSIBLE_NOTIFICATION = 1;
  public static final int SUPPRESSIBLE_SYSTEM = 6;
  public static final SparseIntArray SUPPRESSIBLE_USAGES = new SparseIntArray();
  private static final String TAG = "AudioAttributes";
  public static final int USAGE_ALARM = 4;
  public static final int USAGE_ASSISTANCE_ACCESSIBILITY = 11;
  public static final int USAGE_ASSISTANCE_NAVIGATION_GUIDANCE = 12;
  public static final int USAGE_ASSISTANCE_SONIFICATION = 13;
  public static final int USAGE_ASSISTANT = 16;
  public static final int USAGE_GAME = 14;
  public static final int USAGE_MEDIA = 1;
  public static final int USAGE_NOTIFICATION = 5;
  public static final int USAGE_NOTIFICATION_COMMUNICATION_DELAYED = 9;
  public static final int USAGE_NOTIFICATION_COMMUNICATION_INSTANT = 8;
  public static final int USAGE_NOTIFICATION_COMMUNICATION_REQUEST = 7;
  public static final int USAGE_NOTIFICATION_EVENT = 10;
  public static final int USAGE_NOTIFICATION_RINGTONE = 6;
  public static final int USAGE_UNKNOWN = 0;
  public static final int USAGE_URGENT = 9527;
  public static final int USAGE_VIRTUAL_SOURCE = 15;
  public static final int USAGE_VOICE_COMMUNICATION = 2;
  public static final int USAGE_VOICE_COMMUNICATION_SIGNALLING = 3;
  private Bundle mBundle;
  private int mContentType;
  private int mFlags;
  private String mFormattedTags;
  private int mSource;
  private HashSet<String> mTags;
  private int mUsage;
  
  static
  {
    SUPPRESSIBLE_USAGES.put(5, 1);
    SUPPRESSIBLE_USAGES.put(6, 2);
    SUPPRESSIBLE_USAGES.put(7, 2);
    SUPPRESSIBLE_USAGES.put(8, 1);
    SUPPRESSIBLE_USAGES.put(9, 1);
    SUPPRESSIBLE_USAGES.put(10, 1);
    SUPPRESSIBLE_USAGES.put(11, 3);
    SUPPRESSIBLE_USAGES.put(2, 3);
    SUPPRESSIBLE_USAGES.put(4, 4);
    SUPPRESSIBLE_USAGES.put(1, 5);
    SUPPRESSIBLE_USAGES.put(12, 5);
    SUPPRESSIBLE_USAGES.put(14, 5);
    SUPPRESSIBLE_USAGES.put(16, 5);
    SUPPRESSIBLE_USAGES.put(0, 5);
    SUPPRESSIBLE_USAGES.put(3, 6);
    SUPPRESSIBLE_USAGES.put(13, 6);
    SDK_USAGES = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 16 };
  }
  
  private AudioAttributes()
  {
    mUsage = 0;
    mContentType = 0;
    mSource = -1;
    mFlags = 0;
  }
  
  private AudioAttributes(Parcel paramParcel)
  {
    int i = 0;
    mUsage = 0;
    mContentType = 0;
    mSource = -1;
    mFlags = 0;
    mUsage = paramParcel.readInt();
    mContentType = paramParcel.readInt();
    mSource = paramParcel.readInt();
    mFlags = paramParcel.readInt();
    if ((paramParcel.readInt() & 0x1) == 1) {
      i = 1;
    }
    mTags = new HashSet();
    if (i != 0)
    {
      mFormattedTags = new String(paramParcel.readString());
      mTags.add(mFormattedTags);
    }
    else
    {
      String[] arrayOfString = paramParcel.readStringArray();
      for (i = arrayOfString.length - 1; i >= 0; i--) {
        mTags.add(arrayOfString[i]);
      }
      mFormattedTags = TextUtils.join(";", mTags);
    }
    i = paramParcel.readInt();
    if (i != 63559)
    {
      if (i != 1980) {
        Log.e("AudioAttributes", "Illegal value unmarshalling AudioAttributes, can't initialize bundle");
      } else {
        mBundle = new Bundle(paramParcel.readBundle());
      }
    }
    else {
      mBundle = null;
    }
  }
  
  public static int toLegacyStreamType(AudioAttributes paramAudioAttributes)
  {
    return toVolumeStreamType(false, paramAudioAttributes);
  }
  
  private static int toVolumeStreamType(boolean paramBoolean, AudioAttributes paramAudioAttributes)
  {
    int i = paramAudioAttributes.getFlags();
    int j = 1;
    if ((i & 0x1) == 1)
    {
      if (!paramBoolean) {
        j = 7;
      }
      return j;
    }
    int k = paramAudioAttributes.getFlags();
    i = 0;
    j = 0;
    if ((k & 0x4) == 4)
    {
      if (!paramBoolean) {
        j = 6;
      }
      return j;
    }
    switch (paramAudioAttributes.getUsage())
    {
    case 15: 
    default: 
      if (!paramBoolean) {
        return 3;
      }
      break;
    case 13: 
      return 1;
    case 11: 
      return 10;
    case 6: 
      return 2;
    case 5: 
    case 7: 
    case 8: 
    case 9: 
    case 10: 
      return 5;
    case 4: 
      return 4;
    case 3: 
      if (paramBoolean) {
        j = i;
      } else {
        j = 8;
      }
      return j;
    case 2: 
      return 0;
    case 1: 
    case 12: 
    case 14: 
    case 16: 
      return 3;
    case 0: 
      return 3;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Unknown usage value ");
    localStringBuilder.append(paramAudioAttributes.getUsage());
    localStringBuilder.append(" in audio attributes");
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  private static int usageForStreamType(int paramInt)
  {
    switch (paramInt)
    {
    case 9: 
    default: 
      return 0;
    case 10: 
      return 11;
    case 8: 
      return 3;
    case 6: 
      return 2;
    case 5: 
      return 5;
    case 4: 
      return 4;
    case 3: 
      return 1;
    case 2: 
      return 6;
    case 1: 
    case 7: 
      return 13;
    }
    return 2;
  }
  
  public static String usageToString(int paramInt)
  {
    switch (paramInt)
    {
    case 15: 
    default: 
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("unknown usage ");
      localStringBuilder.append(paramInt);
      return new String(localStringBuilder.toString());
    case 16: 
      return new String("USAGE_ASSISTANT");
    case 14: 
      return new String("USAGE_GAME");
    case 13: 
      return new String("USAGE_ASSISTANCE_SONIFICATION");
    case 12: 
      return new String("USAGE_ASSISTANCE_NAVIGATION_GUIDANCE");
    case 11: 
      return new String("USAGE_ASSISTANCE_ACCESSIBILITY");
    case 10: 
      return new String("USAGE_NOTIFICATION_EVENT");
    case 9: 
      return new String("USAGE_NOTIFICATION_COMMUNICATION_DELAYED");
    case 8: 
      return new String("USAGE_NOTIFICATION_COMMUNICATION_INSTANT");
    case 7: 
      return new String("USAGE_NOTIFICATION_COMMUNICATION_REQUEST");
    case 6: 
      return new String("USAGE_NOTIFICATION_RINGTONE");
    case 5: 
      return new String("USAGE_NOTIFICATION");
    case 4: 
      return new String("USAGE_ALARM");
    case 3: 
      return new String("USAGE_VOICE_COMMUNICATION_SIGNALLING");
    case 2: 
      return new String("USAGE_VOICE_COMMUNICATION");
    case 1: 
      return new String("USAGE_MEDIA");
    }
    return new String("USAGE_UNKNOWN");
  }
  
  public String contentTypeToString()
  {
    switch (mContentType)
    {
    default: 
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("unknown content type ");
      localStringBuilder.append(mContentType);
      return new String(localStringBuilder.toString());
    case 4: 
      return new String("CONTENT_TYPE_SONIFICATION");
    case 3: 
      return new String("CONTENT_TYPE_MOVIE");
    case 2: 
      return new String("CONTENT_TYPE_MUSIC");
    case 1: 
      return new String("CONTENT_TYPE_SPEECH");
    }
    return new String("CONTENT_TYPE_UNKNOWN");
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool = true;
    if (this == paramObject) {
      return true;
    }
    if ((paramObject != null) && (getClass() == paramObject.getClass()))
    {
      paramObject = (AudioAttributes)paramObject;
      if ((mContentType != mContentType) || (mFlags != mFlags) || (mSource != mSource) || (mUsage != mUsage) || (!mFormattedTags.equals(mFormattedTags))) {
        bool = false;
      }
      return bool;
    }
    return false;
  }
  
  @SystemApi
  public int getAllFlags()
  {
    return mFlags & 0x3FF;
  }
  
  @SystemApi
  public Bundle getBundle()
  {
    if (mBundle == null) {
      return mBundle;
    }
    return new Bundle(mBundle);
  }
  
  @SystemApi
  public int getCapturePreset()
  {
    return mSource;
  }
  
  public int getContentType()
  {
    return mContentType;
  }
  
  public int getFlags()
  {
    return mFlags & 0x111;
  }
  
  public Set<String> getTags()
  {
    return Collections.unmodifiableSet(mTags);
  }
  
  public int getUsage()
  {
    return mUsage;
  }
  
  public int getVolumeControlStream()
  {
    return toVolumeStreamType(true, this);
  }
  
  public int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(mContentType), Integer.valueOf(mFlags), Integer.valueOf(mSource), Integer.valueOf(mUsage), mFormattedTags, mBundle });
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("AudioAttributes: usage=");
    localStringBuilder.append(usageToString());
    localStringBuilder.append(" content=");
    localStringBuilder.append(contentTypeToString());
    localStringBuilder.append(" flags=0x");
    localStringBuilder.append(Integer.toHexString(mFlags).toUpperCase());
    localStringBuilder.append(" tags=");
    localStringBuilder.append(mFormattedTags);
    localStringBuilder.append(" bundle=");
    String str;
    if (mBundle == null) {
      str = "null";
    } else {
      str = mBundle.toString();
    }
    localStringBuilder.append(str);
    return new String(localStringBuilder.toString());
  }
  
  public String usageToString()
  {
    return usageToString(mUsage);
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mUsage);
    paramParcel.writeInt(mContentType);
    paramParcel.writeInt(mSource);
    paramParcel.writeInt(mFlags);
    paramParcel.writeInt(paramInt & 0x1);
    if ((paramInt & 0x1) == 0)
    {
      String[] arrayOfString = new String[mTags.size()];
      mTags.toArray(arrayOfString);
      paramParcel.writeStringArray(arrayOfString);
    }
    else if ((paramInt & 0x1) == 1)
    {
      paramParcel.writeString(mFormattedTags);
    }
    if (mBundle == null)
    {
      paramParcel.writeInt(63559);
    }
    else
    {
      paramParcel.writeInt(1980);
      paramParcel.writeBundle(mBundle);
    }
  }
  
  public void writeToProto(ProtoOutputStream paramProtoOutputStream, long paramLong)
  {
    paramLong = paramProtoOutputStream.start(paramLong);
    paramProtoOutputStream.write(1159641169921L, mUsage);
    paramProtoOutputStream.write(1159641169922L, mContentType);
    paramProtoOutputStream.write(1120986464259L, mFlags);
    String[] arrayOfString = mFormattedTags.split(";");
    int i = arrayOfString.length;
    for (int j = 0; j < i; j++)
    {
      String str = arrayOfString[j].trim();
      if (str != "") {
        paramProtoOutputStream.write(2237677961220L, str);
      }
    }
    paramProtoOutputStream.end(paramLong);
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface AttributeContentType {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface AttributeUsage {}
  
  public static class Builder
  {
    private Bundle mBundle;
    private int mContentType = 0;
    private int mFlags = 0;
    private int mSource = -1;
    private HashSet<String> mTags = new HashSet();
    private int mUsage = 0;
    
    public Builder() {}
    
    public Builder(AudioAttributes paramAudioAttributes)
    {
      mUsage = mUsage;
      mContentType = mContentType;
      mFlags = mFlags;
      mTags = ((HashSet)mTags.clone());
    }
    
    @SystemApi
    public Builder addBundle(Bundle paramBundle)
    {
      if (paramBundle != null)
      {
        if (mBundle == null) {
          mBundle = new Bundle(paramBundle);
        } else {
          mBundle.putAll(paramBundle);
        }
        return this;
      }
      throw new IllegalArgumentException("Illegal null bundle");
    }
    
    public Builder addTag(String paramString)
    {
      mTags.add(paramString);
      return this;
    }
    
    public AudioAttributes build()
    {
      AudioAttributes localAudioAttributes = new AudioAttributes(null);
      AudioAttributes.access$102(localAudioAttributes, mContentType);
      AudioAttributes.access$002(localAudioAttributes, mUsage);
      AudioAttributes.access$502(localAudioAttributes, mSource);
      AudioAttributes.access$202(localAudioAttributes, mFlags);
      AudioAttributes.access$302(localAudioAttributes, (HashSet)mTags.clone());
      AudioAttributes.access$602(localAudioAttributes, TextUtils.join(";", mTags));
      if (mBundle != null) {
        AudioAttributes.access$702(localAudioAttributes, new Bundle(mBundle));
      }
      return localAudioAttributes;
    }
    
    public Builder replaceFlags(int paramInt)
    {
      mFlags = (paramInt & 0x3FF);
      return this;
    }
    
    @SystemApi
    public Builder setCapturePreset(int paramInt)
    {
      switch (paramInt)
      {
      case 2: 
      case 3: 
      case 4: 
      case 8: 
      default: 
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Invalid capture preset ");
        localStringBuilder.append(paramInt);
        localStringBuilder.append(" for AudioAttributes");
        Log.e("AudioAttributes", localStringBuilder.toString());
        break;
      case 0: 
      case 1: 
      case 5: 
      case 6: 
      case 7: 
      case 9: 
        mSource = paramInt;
      }
      return this;
    }
    
    public Builder setContentType(int paramInt)
    {
      switch (paramInt)
      {
      default: 
        mUsage = 0;
        break;
      case 0: 
      case 1: 
      case 2: 
      case 3: 
      case 4: 
        mContentType = paramInt;
      }
      return this;
    }
    
    public Builder setFlags(int paramInt)
    {
      mFlags |= paramInt & 0x3FF;
      return this;
    }
    
    @SystemApi
    public Builder setInternalCapturePreset(int paramInt)
    {
      if ((paramInt != 1999) && (paramInt != 8) && (paramInt != 10) && (paramInt != 1998) && (paramInt != 3) && (paramInt != 2) && (paramInt != 4)) {
        setCapturePreset(paramInt);
      } else {
        mSource = paramInt;
      }
      return this;
    }
    
    public Builder setInternalLegacyStreamType(int paramInt)
    {
      switch (paramInt)
      {
      default: 
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Invalid stream type ");
        localStringBuilder.append(paramInt);
        localStringBuilder.append(" for AudioAttributes");
        Log.e("AudioAttributes", localStringBuilder.toString());
        break;
      case 10: 
        mContentType = 1;
        break;
      case 9: 
        mContentType = 4;
        break;
      case 8: 
        mContentType = 4;
        break;
      case 7: 
        mFlags = (0x1 | mFlags);
        break;
      case 6: 
        mContentType = 1;
        mFlags |= 0x4;
        break;
      case 5: 
        mContentType = 4;
        break;
      case 4: 
        mContentType = 4;
        break;
      case 3: 
        mContentType = 2;
        break;
      case 2: 
        mContentType = 4;
        break;
      case 1: 
        mContentType = 4;
        break;
      }
      mContentType = 1;
      mUsage = AudioAttributes.usageForStreamType(paramInt);
      return this;
    }
    
    public Builder setLegacyStreamType(int paramInt)
    {
      if (paramInt != 10) {
        return setInternalLegacyStreamType(paramInt);
      }
      throw new IllegalArgumentException("STREAM_ACCESSIBILITY is not a legacy stream type that was used for audio playback");
    }
    
    public Builder setUsage(int paramInt)
    {
      if (paramInt != 9527) {
        switch (paramInt)
        {
        default: 
          mUsage = 0;
          break;
        }
      } else {
        mUsage = paramInt;
      }
      return this;
    }
  }
}
