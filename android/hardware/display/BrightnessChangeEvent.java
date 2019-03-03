package android.hardware.display;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

@SystemApi
public final class BrightnessChangeEvent
  implements Parcelable
{
  public static final Parcelable.Creator<BrightnessChangeEvent> CREATOR = new Parcelable.Creator()
  {
    public BrightnessChangeEvent createFromParcel(Parcel paramAnonymousParcel)
    {
      return new BrightnessChangeEvent(paramAnonymousParcel, null);
    }
    
    public BrightnessChangeEvent[] newArray(int paramAnonymousInt)
    {
      return new BrightnessChangeEvent[paramAnonymousInt];
    }
  };
  public final float batteryLevel;
  public final float brightness;
  public final int colorTemperature;
  public final boolean isDefaultBrightnessConfig;
  public final boolean isUserSetBrightness;
  public final float lastBrightness;
  public final long[] luxTimestamps;
  public final float[] luxValues;
  public final boolean nightMode;
  public final String packageName;
  public final float powerBrightnessFactor;
  public final long timeStamp;
  public final int userId;
  
  private BrightnessChangeEvent(float paramFloat1, long paramLong, String paramString, int paramInt1, float[] paramArrayOfFloat, long[] paramArrayOfLong, float paramFloat2, float paramFloat3, boolean paramBoolean1, int paramInt2, float paramFloat4, boolean paramBoolean2, boolean paramBoolean3)
  {
    brightness = paramFloat1;
    timeStamp = paramLong;
    packageName = paramString;
    userId = paramInt1;
    luxValues = paramArrayOfFloat;
    luxTimestamps = paramArrayOfLong;
    batteryLevel = paramFloat2;
    powerBrightnessFactor = paramFloat3;
    nightMode = paramBoolean1;
    colorTemperature = paramInt2;
    lastBrightness = paramFloat4;
    isDefaultBrightnessConfig = paramBoolean2;
    isUserSetBrightness = paramBoolean3;
  }
  
  public BrightnessChangeEvent(BrightnessChangeEvent paramBrightnessChangeEvent, boolean paramBoolean)
  {
    brightness = brightness;
    timeStamp = timeStamp;
    String str;
    if (paramBoolean) {
      str = null;
    } else {
      str = packageName;
    }
    packageName = str;
    userId = userId;
    luxValues = luxValues;
    luxTimestamps = luxTimestamps;
    batteryLevel = batteryLevel;
    powerBrightnessFactor = powerBrightnessFactor;
    nightMode = nightMode;
    colorTemperature = colorTemperature;
    lastBrightness = lastBrightness;
    isDefaultBrightnessConfig = isDefaultBrightnessConfig;
    isUserSetBrightness = isUserSetBrightness;
  }
  
  private BrightnessChangeEvent(Parcel paramParcel)
  {
    brightness = paramParcel.readFloat();
    timeStamp = paramParcel.readLong();
    packageName = paramParcel.readString();
    userId = paramParcel.readInt();
    luxValues = paramParcel.createFloatArray();
    luxTimestamps = paramParcel.createLongArray();
    batteryLevel = paramParcel.readFloat();
    powerBrightnessFactor = paramParcel.readFloat();
    nightMode = paramParcel.readBoolean();
    colorTemperature = paramParcel.readInt();
    lastBrightness = paramParcel.readFloat();
    isDefaultBrightnessConfig = paramParcel.readBoolean();
    isUserSetBrightness = paramParcel.readBoolean();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeFloat(brightness);
    paramParcel.writeLong(timeStamp);
    paramParcel.writeString(packageName);
    paramParcel.writeInt(userId);
    paramParcel.writeFloatArray(luxValues);
    paramParcel.writeLongArray(luxTimestamps);
    paramParcel.writeFloat(batteryLevel);
    paramParcel.writeFloat(powerBrightnessFactor);
    paramParcel.writeBoolean(nightMode);
    paramParcel.writeInt(colorTemperature);
    paramParcel.writeFloat(lastBrightness);
    paramParcel.writeBoolean(isDefaultBrightnessConfig);
    paramParcel.writeBoolean(isUserSetBrightness);
  }
  
  public static class Builder
  {
    private float mBatteryLevel;
    private float mBrightness;
    private int mColorTemperature;
    private boolean mIsDefaultBrightnessConfig;
    private boolean mIsUserSetBrightness;
    private float mLastBrightness;
    private long[] mLuxTimestamps;
    private float[] mLuxValues;
    private boolean mNightMode;
    private String mPackageName;
    private float mPowerBrightnessFactor;
    private long mTimeStamp;
    private int mUserId;
    
    public Builder() {}
    
    public BrightnessChangeEvent build()
    {
      return new BrightnessChangeEvent(mBrightness, mTimeStamp, mPackageName, mUserId, mLuxValues, mLuxTimestamps, mBatteryLevel, mPowerBrightnessFactor, mNightMode, mColorTemperature, mLastBrightness, mIsDefaultBrightnessConfig, mIsUserSetBrightness, null);
    }
    
    public Builder setBatteryLevel(float paramFloat)
    {
      mBatteryLevel = paramFloat;
      return this;
    }
    
    public Builder setBrightness(float paramFloat)
    {
      mBrightness = paramFloat;
      return this;
    }
    
    public Builder setColorTemperature(int paramInt)
    {
      mColorTemperature = paramInt;
      return this;
    }
    
    public Builder setIsDefaultBrightnessConfig(boolean paramBoolean)
    {
      mIsDefaultBrightnessConfig = paramBoolean;
      return this;
    }
    
    public Builder setLastBrightness(float paramFloat)
    {
      mLastBrightness = paramFloat;
      return this;
    }
    
    public Builder setLuxTimestamps(long[] paramArrayOfLong)
    {
      mLuxTimestamps = paramArrayOfLong;
      return this;
    }
    
    public Builder setLuxValues(float[] paramArrayOfFloat)
    {
      mLuxValues = paramArrayOfFloat;
      return this;
    }
    
    public Builder setNightMode(boolean paramBoolean)
    {
      mNightMode = paramBoolean;
      return this;
    }
    
    public Builder setPackageName(String paramString)
    {
      mPackageName = paramString;
      return this;
    }
    
    public Builder setPowerBrightnessFactor(float paramFloat)
    {
      mPowerBrightnessFactor = paramFloat;
      return this;
    }
    
    public Builder setTimeStamp(long paramLong)
    {
      mTimeStamp = paramLong;
      return this;
    }
    
    public Builder setUserBrightnessPoint(boolean paramBoolean)
    {
      mIsUserSetBrightness = paramBoolean;
      return this;
    }
    
    public Builder setUserId(int paramInt)
    {
      mUserId = paramInt;
      return this;
    }
  }
}
