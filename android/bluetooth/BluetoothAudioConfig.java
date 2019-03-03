package android.bluetooth;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class BluetoothAudioConfig
  implements Parcelable
{
  public static final Parcelable.Creator<BluetoothAudioConfig> CREATOR = new Parcelable.Creator()
  {
    public BluetoothAudioConfig createFromParcel(Parcel paramAnonymousParcel)
    {
      return new BluetoothAudioConfig(paramAnonymousParcel.readInt(), paramAnonymousParcel.readInt(), paramAnonymousParcel.readInt());
    }
    
    public BluetoothAudioConfig[] newArray(int paramAnonymousInt)
    {
      return new BluetoothAudioConfig[paramAnonymousInt];
    }
  };
  private final int mAudioFormat;
  private final int mChannelConfig;
  private final int mSampleRate;
  
  public BluetoothAudioConfig(int paramInt1, int paramInt2, int paramInt3)
  {
    mSampleRate = paramInt1;
    mChannelConfig = paramInt2;
    mAudioFormat = paramInt3;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool1 = paramObject instanceof BluetoothAudioConfig;
    boolean bool2 = false;
    if (bool1)
    {
      paramObject = (BluetoothAudioConfig)paramObject;
      bool1 = bool2;
      if (mSampleRate == mSampleRate)
      {
        bool1 = bool2;
        if (mChannelConfig == mChannelConfig)
        {
          bool1 = bool2;
          if (mAudioFormat == mAudioFormat) {
            bool1 = true;
          }
        }
      }
      return bool1;
    }
    return false;
  }
  
  public int getAudioFormat()
  {
    return mAudioFormat;
  }
  
  public int getChannelConfig()
  {
    return mChannelConfig;
  }
  
  public int getSampleRate()
  {
    return mSampleRate;
  }
  
  public int hashCode()
  {
    return mSampleRate | mChannelConfig << 24 | mAudioFormat << 28;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{mSampleRate:");
    localStringBuilder.append(mSampleRate);
    localStringBuilder.append(",mChannelConfig:");
    localStringBuilder.append(mChannelConfig);
    localStringBuilder.append(",mAudioFormat:");
    localStringBuilder.append(mAudioFormat);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mSampleRate);
    paramParcel.writeInt(mChannelConfig);
    paramParcel.writeInt(mAudioFormat);
  }
}
