package android.net;

import android.os.Parcel;

public class WifiLinkQualityInfo
  extends LinkQualityInfo
{
  private String mBssid;
  private int mRssi = Integer.MAX_VALUE;
  private long mTxBad = Long.MAX_VALUE;
  private long mTxGood = Long.MAX_VALUE;
  private int mType = Integer.MAX_VALUE;
  
  public WifiLinkQualityInfo() {}
  
  public static WifiLinkQualityInfo createFromParcelBody(Parcel paramParcel)
  {
    WifiLinkQualityInfo localWifiLinkQualityInfo = new WifiLinkQualityInfo();
    localWifiLinkQualityInfo.initializeFromParcel(paramParcel);
    mType = paramParcel.readInt();
    mRssi = paramParcel.readInt();
    mTxGood = paramParcel.readLong();
    mTxBad = paramParcel.readLong();
    mBssid = paramParcel.readString();
    return localWifiLinkQualityInfo;
  }
  
  public String getBssid()
  {
    return mBssid;
  }
  
  public int getRssi()
  {
    return mRssi;
  }
  
  public long getTxBad()
  {
    return mTxBad;
  }
  
  public long getTxGood()
  {
    return mTxGood;
  }
  
  public int getType()
  {
    return mType;
  }
  
  public void setBssid(String paramString)
  {
    mBssid = paramString;
  }
  
  public void setRssi(int paramInt)
  {
    mRssi = paramInt;
  }
  
  public void setTxBad(long paramLong)
  {
    mTxBad = paramLong;
  }
  
  public void setTxGood(long paramLong)
  {
    mTxGood = paramLong;
  }
  
  public void setType(int paramInt)
  {
    mType = paramInt;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    super.writeToParcel(paramParcel, paramInt, 2);
    paramParcel.writeInt(mType);
    paramParcel.writeInt(mRssi);
    paramParcel.writeLong(mTxGood);
    paramParcel.writeLong(mTxBad);
    paramParcel.writeString(mBssid);
  }
}
