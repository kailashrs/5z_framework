package android.net;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class LinkQualityInfo
  implements Parcelable
{
  public static final Parcelable.Creator<LinkQualityInfo> CREATOR = new Parcelable.Creator()
  {
    public LinkQualityInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      int i = paramAnonymousParcel.readInt();
      if (i == 1)
      {
        LinkQualityInfo localLinkQualityInfo = new LinkQualityInfo();
        localLinkQualityInfo.initializeFromParcel(paramAnonymousParcel);
        return localLinkQualityInfo;
      }
      if (i == 2) {
        return WifiLinkQualityInfo.createFromParcelBody(paramAnonymousParcel);
      }
      if (i == 3) {
        return MobileLinkQualityInfo.createFromParcelBody(paramAnonymousParcel);
      }
      return null;
    }
    
    public LinkQualityInfo[] newArray(int paramAnonymousInt)
    {
      return new LinkQualityInfo[paramAnonymousInt];
    }
  };
  public static final int NORMALIZED_MAX_SIGNAL_STRENGTH = 99;
  public static final int NORMALIZED_MIN_SIGNAL_STRENGTH = 0;
  public static final int NORMALIZED_SIGNAL_STRENGTH_RANGE = 100;
  protected static final int OBJECT_TYPE_LINK_QUALITY_INFO = 1;
  protected static final int OBJECT_TYPE_MOBILE_LINK_QUALITY_INFO = 3;
  protected static final int OBJECT_TYPE_WIFI_LINK_QUALITY_INFO = 2;
  public static final int UNKNOWN_INT = Integer.MAX_VALUE;
  public static final long UNKNOWN_LONG = Long.MAX_VALUE;
  private int mDataSampleDuration = Integer.MAX_VALUE;
  private long mLastDataSampleTime = Long.MAX_VALUE;
  private int mNetworkType = -1;
  private int mNormalizedSignalStrength = Integer.MAX_VALUE;
  private long mPacketCount = Long.MAX_VALUE;
  private long mPacketErrorCount = Long.MAX_VALUE;
  private int mTheoreticalLatency = Integer.MAX_VALUE;
  private int mTheoreticalRxBandwidth = Integer.MAX_VALUE;
  private int mTheoreticalTxBandwidth = Integer.MAX_VALUE;
  
  public LinkQualityInfo() {}
  
  public int describeContents()
  {
    return 0;
  }
  
  public int getDataSampleDuration()
  {
    return mDataSampleDuration;
  }
  
  public long getLastDataSampleTime()
  {
    return mLastDataSampleTime;
  }
  
  public int getNetworkType()
  {
    return mNetworkType;
  }
  
  public int getNormalizedSignalStrength()
  {
    return mNormalizedSignalStrength;
  }
  
  public long getPacketCount()
  {
    return mPacketCount;
  }
  
  public long getPacketErrorCount()
  {
    return mPacketErrorCount;
  }
  
  public int getTheoreticalLatency()
  {
    return mTheoreticalLatency;
  }
  
  public int getTheoreticalRxBandwidth()
  {
    return mTheoreticalRxBandwidth;
  }
  
  public int getTheoreticalTxBandwidth()
  {
    return mTheoreticalTxBandwidth;
  }
  
  protected void initializeFromParcel(Parcel paramParcel)
  {
    mNetworkType = paramParcel.readInt();
    mNormalizedSignalStrength = paramParcel.readInt();
    mPacketCount = paramParcel.readLong();
    mPacketErrorCount = paramParcel.readLong();
    mTheoreticalTxBandwidth = paramParcel.readInt();
    mTheoreticalRxBandwidth = paramParcel.readInt();
    mTheoreticalLatency = paramParcel.readInt();
    mLastDataSampleTime = paramParcel.readLong();
    mDataSampleDuration = paramParcel.readInt();
  }
  
  public void setDataSampleDuration(int paramInt)
  {
    mDataSampleDuration = paramInt;
  }
  
  public void setLastDataSampleTime(long paramLong)
  {
    mLastDataSampleTime = paramLong;
  }
  
  public void setNetworkType(int paramInt)
  {
    mNetworkType = paramInt;
  }
  
  public void setNormalizedSignalStrength(int paramInt)
  {
    mNormalizedSignalStrength = paramInt;
  }
  
  public void setPacketCount(long paramLong)
  {
    mPacketCount = paramLong;
  }
  
  public void setPacketErrorCount(long paramLong)
  {
    mPacketErrorCount = paramLong;
  }
  
  public void setTheoreticalLatency(int paramInt)
  {
    mTheoreticalLatency = paramInt;
  }
  
  public void setTheoreticalRxBandwidth(int paramInt)
  {
    mTheoreticalRxBandwidth = paramInt;
  }
  
  public void setTheoreticalTxBandwidth(int paramInt)
  {
    mTheoreticalTxBandwidth = paramInt;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    writeToParcel(paramParcel, paramInt, 1);
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt1, int paramInt2)
  {
    paramParcel.writeInt(paramInt2);
    paramParcel.writeInt(mNetworkType);
    paramParcel.writeInt(mNormalizedSignalStrength);
    paramParcel.writeLong(mPacketCount);
    paramParcel.writeLong(mPacketErrorCount);
    paramParcel.writeInt(mTheoreticalTxBandwidth);
    paramParcel.writeInt(mTheoreticalRxBandwidth);
    paramParcel.writeInt(mTheoreticalLatency);
    paramParcel.writeLong(mLastDataSampleTime);
    paramParcel.writeInt(mDataSampleDuration);
  }
}
