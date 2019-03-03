package android.net.lowpan;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.Objects;

public class LowpanChannelInfo
  implements Parcelable
{
  public static final Parcelable.Creator<LowpanChannelInfo> CREATOR = new Parcelable.Creator()
  {
    public LowpanChannelInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      LowpanChannelInfo localLowpanChannelInfo = new LowpanChannelInfo(null);
      LowpanChannelInfo.access$102(localLowpanChannelInfo, paramAnonymousParcel.readInt());
      LowpanChannelInfo.access$202(localLowpanChannelInfo, paramAnonymousParcel.readString());
      LowpanChannelInfo.access$302(localLowpanChannelInfo, paramAnonymousParcel.readFloat());
      LowpanChannelInfo.access$402(localLowpanChannelInfo, paramAnonymousParcel.readFloat());
      LowpanChannelInfo.access$502(localLowpanChannelInfo, paramAnonymousParcel.readInt());
      LowpanChannelInfo.access$602(localLowpanChannelInfo, paramAnonymousParcel.readBoolean());
      return localLowpanChannelInfo;
    }
    
    public LowpanChannelInfo[] newArray(int paramAnonymousInt)
    {
      return new LowpanChannelInfo[paramAnonymousInt];
    }
  };
  public static final float UNKNOWN_BANDWIDTH = 0.0F;
  public static final float UNKNOWN_FREQUENCY = 0.0F;
  public static final int UNKNOWN_POWER = Integer.MAX_VALUE;
  private int mIndex = 0;
  private boolean mIsMaskedByRegulatoryDomain = false;
  private int mMaxTransmitPower = Integer.MAX_VALUE;
  private String mName = null;
  private float mSpectrumBandwidth = 0.0F;
  private float mSpectrumCenterFrequency = 0.0F;
  
  private LowpanChannelInfo() {}
  
  private LowpanChannelInfo(int paramInt, String paramString, float paramFloat1, float paramFloat2)
  {
    mIndex = paramInt;
    mName = paramString;
    mSpectrumCenterFrequency = paramFloat1;
    mSpectrumBandwidth = paramFloat2;
  }
  
  public static LowpanChannelInfo getChannelInfoForIeee802154Page0(int paramInt)
  {
    LowpanChannelInfo localLowpanChannelInfo = new LowpanChannelInfo();
    if (paramInt < 0)
    {
      localLowpanChannelInfo = null;
    }
    else if (paramInt == 0)
    {
      mSpectrumCenterFrequency = 8.6830003E8F;
      mSpectrumBandwidth = 600000.0F;
    }
    else if (paramInt < 11)
    {
      mSpectrumCenterFrequency = (9.04E8F + 2000000.0F * paramInt);
      mSpectrumBandwidth = 0.0F;
    }
    else if (paramInt < 26)
    {
      mSpectrumCenterFrequency = (2.34999987E9F + 5000000.0F * paramInt);
      mSpectrumBandwidth = 2000000.0F;
    }
    else
    {
      localLowpanChannelInfo = null;
    }
    mName = Integer.toString(paramInt);
    return localLowpanChannelInfo;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool1 = paramObject instanceof LowpanChannelInfo;
    boolean bool2 = false;
    if (!bool1) {
      return false;
    }
    paramObject = (LowpanChannelInfo)paramObject;
    bool1 = bool2;
    if (Objects.equals(mName, mName))
    {
      bool1 = bool2;
      if (mIndex == mIndex)
      {
        bool1 = bool2;
        if (mIsMaskedByRegulatoryDomain == mIsMaskedByRegulatoryDomain)
        {
          bool1 = bool2;
          if (mSpectrumCenterFrequency == mSpectrumCenterFrequency)
          {
            bool1 = bool2;
            if (mSpectrumBandwidth == mSpectrumBandwidth)
            {
              bool1 = bool2;
              if (mMaxTransmitPower == mMaxTransmitPower) {
                bool1 = true;
              }
            }
          }
        }
      }
    }
    return bool1;
  }
  
  public int getIndex()
  {
    return mIndex;
  }
  
  public int getMaxTransmitPower()
  {
    return mMaxTransmitPower;
  }
  
  public String getName()
  {
    return mName;
  }
  
  public float getSpectrumBandwidth()
  {
    return mSpectrumBandwidth;
  }
  
  public float getSpectrumCenterFrequency()
  {
    return mSpectrumCenterFrequency;
  }
  
  public int hashCode()
  {
    return Objects.hash(new Object[] { mName, Integer.valueOf(mIndex), Boolean.valueOf(mIsMaskedByRegulatoryDomain), Float.valueOf(mSpectrumCenterFrequency), Float.valueOf(mSpectrumBandwidth), Integer.valueOf(mMaxTransmitPower) });
  }
  
  public boolean isMaskedByRegulatoryDomain()
  {
    return mIsMaskedByRegulatoryDomain;
  }
  
  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append("Channel ");
    localStringBuffer.append(mIndex);
    if ((mName != null) && (!mName.equals(Integer.toString(mIndex))))
    {
      localStringBuffer.append(" (");
      localStringBuffer.append(mName);
      localStringBuffer.append(")");
    }
    if (mSpectrumCenterFrequency > 0.0F) {
      if (mSpectrumCenterFrequency > 1.0E9F)
      {
        localStringBuffer.append(", SpectrumCenterFrequency: ");
        localStringBuffer.append(mSpectrumCenterFrequency / 1.0E9F);
        localStringBuffer.append("GHz");
      }
      else if (mSpectrumCenterFrequency > 1000000.0F)
      {
        localStringBuffer.append(", SpectrumCenterFrequency: ");
        localStringBuffer.append(mSpectrumCenterFrequency / 1000000.0F);
        localStringBuffer.append("MHz");
      }
      else
      {
        localStringBuffer.append(", SpectrumCenterFrequency: ");
        localStringBuffer.append(mSpectrumCenterFrequency / 1000.0F);
        localStringBuffer.append("kHz");
      }
    }
    if (mSpectrumBandwidth > 0.0F) {
      if (mSpectrumBandwidth > 1.0E9F)
      {
        localStringBuffer.append(", SpectrumBandwidth: ");
        localStringBuffer.append(mSpectrumBandwidth / 1.0E9F);
        localStringBuffer.append("GHz");
      }
      else if (mSpectrumBandwidth > 1000000.0F)
      {
        localStringBuffer.append(", SpectrumBandwidth: ");
        localStringBuffer.append(mSpectrumBandwidth / 1000000.0F);
        localStringBuffer.append("MHz");
      }
      else
      {
        localStringBuffer.append(", SpectrumBandwidth: ");
        localStringBuffer.append(mSpectrumBandwidth / 1000.0F);
        localStringBuffer.append("kHz");
      }
    }
    if (mMaxTransmitPower != Integer.MAX_VALUE)
    {
      localStringBuffer.append(", MaxTransmitPower: ");
      localStringBuffer.append(mMaxTransmitPower);
      localStringBuffer.append("dBm");
    }
    return localStringBuffer.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mIndex);
    paramParcel.writeString(mName);
    paramParcel.writeFloat(mSpectrumCenterFrequency);
    paramParcel.writeFloat(mSpectrumBandwidth);
    paramParcel.writeInt(mMaxTransmitPower);
    paramParcel.writeBoolean(mIsMaskedByRegulatoryDomain);
  }
}
