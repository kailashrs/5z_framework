package android.net;

import android.os.Parcel;

public class MobileLinkQualityInfo
  extends LinkQualityInfo
{
  private int mCdmaDbm = Integer.MAX_VALUE;
  private int mCdmaEcio = Integer.MAX_VALUE;
  private int mEvdoDbm = Integer.MAX_VALUE;
  private int mEvdoEcio = Integer.MAX_VALUE;
  private int mEvdoSnr = Integer.MAX_VALUE;
  private int mGsmErrorRate = Integer.MAX_VALUE;
  private int mLteCqi = Integer.MAX_VALUE;
  private int mLteRsrp = Integer.MAX_VALUE;
  private int mLteRsrq = Integer.MAX_VALUE;
  private int mLteRssnr = Integer.MAX_VALUE;
  private int mLteSignalStrength = Integer.MAX_VALUE;
  private int mMobileNetworkType = Integer.MAX_VALUE;
  private int mRssi = Integer.MAX_VALUE;
  
  public MobileLinkQualityInfo() {}
  
  public static MobileLinkQualityInfo createFromParcelBody(Parcel paramParcel)
  {
    MobileLinkQualityInfo localMobileLinkQualityInfo = new MobileLinkQualityInfo();
    localMobileLinkQualityInfo.initializeFromParcel(paramParcel);
    mMobileNetworkType = paramParcel.readInt();
    mRssi = paramParcel.readInt();
    mGsmErrorRate = paramParcel.readInt();
    mCdmaDbm = paramParcel.readInt();
    mCdmaEcio = paramParcel.readInt();
    mEvdoDbm = paramParcel.readInt();
    mEvdoEcio = paramParcel.readInt();
    mEvdoSnr = paramParcel.readInt();
    mLteSignalStrength = paramParcel.readInt();
    mLteRsrp = paramParcel.readInt();
    mLteRsrq = paramParcel.readInt();
    mLteRssnr = paramParcel.readInt();
    mLteCqi = paramParcel.readInt();
    return localMobileLinkQualityInfo;
  }
  
  public int getCdmaDbm()
  {
    return mCdmaDbm;
  }
  
  public int getCdmaEcio()
  {
    return mCdmaEcio;
  }
  
  public int getEvdoDbm()
  {
    return mEvdoDbm;
  }
  
  public int getEvdoEcio()
  {
    return mEvdoEcio;
  }
  
  public int getEvdoSnr()
  {
    return mEvdoSnr;
  }
  
  public int getGsmErrorRate()
  {
    return mGsmErrorRate;
  }
  
  public int getLteCqi()
  {
    return mLteCqi;
  }
  
  public int getLteRsrp()
  {
    return mLteRsrp;
  }
  
  public int getLteRsrq()
  {
    return mLteRsrq;
  }
  
  public int getLteRssnr()
  {
    return mLteRssnr;
  }
  
  public int getLteSignalStrength()
  {
    return mLteSignalStrength;
  }
  
  public int getMobileNetworkType()
  {
    return mMobileNetworkType;
  }
  
  public int getRssi()
  {
    return mRssi;
  }
  
  public void setCdmaDbm(int paramInt)
  {
    mCdmaDbm = paramInt;
  }
  
  public void setCdmaEcio(int paramInt)
  {
    mCdmaEcio = paramInt;
  }
  
  public void setEvdoDbm(int paramInt)
  {
    mEvdoDbm = paramInt;
  }
  
  public void setEvdoEcio(int paramInt)
  {
    mEvdoEcio = paramInt;
  }
  
  public void setEvdoSnr(int paramInt)
  {
    mEvdoSnr = paramInt;
  }
  
  public void setGsmErrorRate(int paramInt)
  {
    mGsmErrorRate = paramInt;
  }
  
  public void setLteCqi(int paramInt)
  {
    mLteCqi = paramInt;
  }
  
  public void setLteRsrp(int paramInt)
  {
    mLteRsrp = paramInt;
  }
  
  public void setLteRsrq(int paramInt)
  {
    mLteRsrq = paramInt;
  }
  
  public void setLteRssnr(int paramInt)
  {
    mLteRssnr = paramInt;
  }
  
  public void setLteSignalStrength(int paramInt)
  {
    mLteSignalStrength = paramInt;
  }
  
  public void setMobileNetworkType(int paramInt)
  {
    mMobileNetworkType = paramInt;
  }
  
  public void setRssi(int paramInt)
  {
    mRssi = paramInt;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    super.writeToParcel(paramParcel, paramInt, 3);
    paramParcel.writeInt(mMobileNetworkType);
    paramParcel.writeInt(mRssi);
    paramParcel.writeInt(mGsmErrorRate);
    paramParcel.writeInt(mCdmaDbm);
    paramParcel.writeInt(mCdmaEcio);
    paramParcel.writeInt(mEvdoDbm);
    paramParcel.writeInt(mEvdoEcio);
    paramParcel.writeInt(mEvdoSnr);
    paramParcel.writeInt(mLteSignalStrength);
    paramParcel.writeInt(mLteRsrp);
    paramParcel.writeInt(mLteRsrq);
    paramParcel.writeInt(mLteRssnr);
    paramParcel.writeInt(mLteCqi);
  }
}
