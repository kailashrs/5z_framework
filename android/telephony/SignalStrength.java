package android.telephony;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.PersistableBundle;
import android.util.Log;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class SignalStrength
  implements Parcelable
{
  public static final Parcelable.Creator<SignalStrength> CREATOR = new Parcelable.Creator()
  {
    public SignalStrength createFromParcel(Parcel paramAnonymousParcel)
    {
      return new SignalStrength(paramAnonymousParcel);
    }
    
    public SignalStrength[] newArray(int paramAnonymousInt)
    {
      return new SignalStrength[paramAnonymousInt];
    }
  };
  private static final boolean DBG = false;
  public static final int INVALID = Integer.MAX_VALUE;
  private static final String LOG_TAG = "SignalStrength";
  private static final int LTE_RSRP_THRESHOLDS_NUM = 8;
  private static final int MAX_LTE_RSRP = -44;
  private static final int MAX_WCDMA_RSCP = -24;
  private static final String MEASUMENT_TYPE_RSCP = "rscp";
  private static final int MIN_LTE_RSRP = -140;
  private static final int MIN_WCDMA_RSCP = -120;
  public static final int NUM_SIGNAL_STRENGTH_BINS = 6;
  public static final int SIGNAL_STRENGTH_GOOD = 3;
  public static final int SIGNAL_STRENGTH_GREAT = 4;
  public static final int SIGNAL_STRENGTH_MODERATE = 2;
  public static final String[] SIGNAL_STRENGTH_NAMES = { "none", "poor", "moderate", "good", "great", "perfect" };
  public static final int SIGNAL_STRENGTH_NONE_OR_UNKNOWN = 0;
  public static final int SIGNAL_STRENGTH_PERFECT = 5;
  public static final int SIGNAL_STRENGTH_POOR = 1;
  private static final int WCDMA_RSCP_THRESHOLDS_NUM = 8;
  private int[] LTE_DBM_THRESH = { -126, -119, -114, -109, -100, -44 };
  private int[] WCDMA_DBM_THRESH = { -114, -106, -101, -96, -89, -24 };
  private int mCdmaDbm;
  private int mCdmaEcio;
  private int mEvdoDbm;
  private int mEvdoEcio;
  private int mEvdoSnr;
  private int mGsmBitErrorRate;
  private int mGsmRssi;
  private int mGsmSignalStrength;
  private boolean mIsGsm;
  private int mLteCqi;
  private int mLteRsrp;
  private int mLteRsrpBoost;
  private int[] mLteRsrpThresholds = new int[8];
  private int mLteRsrq;
  private int mLteRssnr;
  private int mLteSignalStrength;
  private int mTdScdmaRscp;
  private boolean mUseOnlyRsrpForLteLevel;
  private String mWcdmaDefaultSignalMeasurement;
  private int mWcdmaRscp;
  private int mWcdmaRscpAsu;
  private int[] mWcdmaRscpThresholds = new int[8];
  private int mWcdmaSignalStrength;
  
  public SignalStrength()
  {
    this(true);
  }
  
  public SignalStrength(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9, int paramInt10, int paramInt11, int paramInt12, int paramInt13)
  {
    this(paramInt1, paramInt2, 0, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramInt8, paramInt9, paramInt10, paramInt11, paramInt12, paramInt13, 99, Integer.MAX_VALUE, 0, true, false, "");
  }
  
  public SignalStrength(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9, int paramInt10, int paramInt11, int paramInt12, int paramInt13, int paramInt14, int paramInt15)
  {
    this(paramInt1, paramInt2, 0, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramInt8, paramInt9, paramInt10, paramInt11, paramInt12, paramInt13, paramInt14, paramInt15, 0, true, false, "");
  }
  
  public SignalStrength(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9, int paramInt10, int paramInt11, int paramInt12, int paramInt13, int paramInt14, int paramInt15, int paramInt16)
  {
    this(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramInt8, paramInt9, paramInt10, paramInt11, paramInt12, paramInt13, paramInt14, paramInt15, paramInt16, 0, true, false, "");
  }
  
  public SignalStrength(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9, int paramInt10, int paramInt11, int paramInt12, int paramInt13, int paramInt14, int paramInt15, int paramInt16, int paramInt17, boolean paramBoolean1, boolean paramBoolean2, String paramString)
  {
    mGsmSignalStrength = paramInt1;
    mGsmBitErrorRate = paramInt2;
    mGsmRssi = paramInt3;
    mCdmaDbm = paramInt4;
    mCdmaEcio = paramInt5;
    mEvdoDbm = paramInt6;
    mEvdoEcio = paramInt7;
    mEvdoSnr = paramInt8;
    mLteSignalStrength = paramInt9;
    mLteRsrp = paramInt10;
    mLteRsrq = paramInt11;
    mLteRssnr = paramInt12;
    mLteCqi = paramInt13;
    mTdScdmaRscp = Integer.MAX_VALUE;
    mWcdmaSignalStrength = paramInt15;
    mWcdmaRscpAsu = paramInt16;
    mWcdmaRscp = (paramInt16 - 120);
    mLteRsrpBoost = paramInt17;
    mIsGsm = paramBoolean1;
    mUseOnlyRsrpForLteLevel = paramBoolean2;
    mWcdmaDefaultSignalMeasurement = paramString;
    setLteRsrpThresholds(getDefaultLteRsrpThresholds());
    setWcdmaRscpThresholds(getDefaultWcdmaRscpThresholds());
  }
  
  public SignalStrength(Parcel paramParcel)
  {
    mGsmSignalStrength = paramParcel.readInt();
    mGsmBitErrorRate = paramParcel.readInt();
    mGsmRssi = paramParcel.readInt();
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
    mTdScdmaRscp = paramParcel.readInt();
    mWcdmaSignalStrength = paramParcel.readInt();
    mWcdmaRscpAsu = paramParcel.readInt();
    mWcdmaRscp = paramParcel.readInt();
    mLteRsrpBoost = paramParcel.readInt();
    mIsGsm = paramParcel.readBoolean();
    mUseOnlyRsrpForLteLevel = paramParcel.readBoolean();
    mWcdmaDefaultSignalMeasurement = paramParcel.readString();
    paramParcel.readIntArray(mLteRsrpThresholds);
    paramParcel.readIntArray(mWcdmaRscpThresholds);
  }
  
  public SignalStrength(SignalStrength paramSignalStrength)
  {
    copyFrom(paramSignalStrength);
  }
  
  public SignalStrength(boolean paramBoolean)
  {
    mGsmSignalStrength = 99;
    mGsmBitErrorRate = -1;
    mGsmRssi = 0;
    mCdmaDbm = -1;
    mCdmaEcio = -1;
    mEvdoDbm = -1;
    mEvdoEcio = -1;
    mEvdoSnr = -1;
    mLteSignalStrength = 99;
    mLteRsrp = Integer.MAX_VALUE;
    mLteRsrq = Integer.MAX_VALUE;
    mLteRssnr = Integer.MAX_VALUE;
    mLteCqi = Integer.MAX_VALUE;
    mTdScdmaRscp = Integer.MAX_VALUE;
    mWcdmaSignalStrength = 99;
    mWcdmaRscp = Integer.MAX_VALUE;
    mWcdmaRscpAsu = 255;
    mLteRsrpBoost = 0;
    mIsGsm = paramBoolean;
    mUseOnlyRsrpForLteLevel = false;
    mWcdmaDefaultSignalMeasurement = "";
    setLteRsrpThresholds(getDefaultLteRsrpThresholds());
    setWcdmaRscpThresholds(getDefaultWcdmaRscpThresholds());
  }
  
  private int getAsusCdmaRelatedSignalStrength()
  {
    int i = getAsusCdmaLevel();
    int j = getAsusEvdoLevel();
    if (j == 0) {
      j = i;
    }
    for (;;)
    {
      break;
      if (i != 0) {
        if (i < j) {
          j = i;
        }
      }
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("getAsusCdmaRelatedSignalStrength=");
    localStringBuilder.append(j);
    log(localStringBuilder.toString());
    return j;
  }
  
  private int getAsusGsmRelatedSignalStrength()
  {
    int i = getAsusLteLevel();
    int j = i;
    if (i == 0)
    {
      i = getAsusTdScdmaLevel();
      j = i;
      if (i == 0)
      {
        i = getAsusWcdmaLevel();
        j = i;
        if (i == 0) {
          j = getAsusGsmLevel();
        }
      }
    }
    return j;
  }
  
  private int getCdmaRelatedSignalStrength()
  {
    int i = getCdmaLevel();
    int j = getEvdoLevel();
    if (j != 0)
    {
      while (i == 0) {
        i = j;
      }
      if (i >= j) {
        i = j;
      }
    }
    return i;
  }
  
  private int[] getDefaultLteRsrpThresholds()
  {
    return CarrierConfigManager.getDefaultConfig().getIntArray("lte_rsrp_thresholds_int_array");
  }
  
  private int[] getDefaultWcdmaRscpThresholds()
  {
    return CarrierConfigManager.getDefaultConfig().getIntArray("wcdma_rscp_thresholds_int_array");
  }
  
  private int getGsmRelatedSignalStrength()
  {
    int i = getLteLevel();
    int j = i;
    if (i == 0)
    {
      i = getTdScdmaLevel();
      j = i;
      if (i == 0)
      {
        i = getWcdmaLevel();
        j = i;
        if (i == 0) {
          j = getGsmLevel();
        }
      }
    }
    return j;
  }
  
  private static void log(String paramString)
  {
    Rlog.w("SignalStrength", paramString);
  }
  
  public static SignalStrength newFromBundle(Bundle paramBundle)
  {
    SignalStrength localSignalStrength = new SignalStrength();
    localSignalStrength.setFromNotifierBundle(paramBundle);
    return localSignalStrength;
  }
  
  private void setFromNotifierBundle(Bundle paramBundle)
  {
    mGsmSignalStrength = paramBundle.getInt("GsmSignalStrength");
    mGsmBitErrorRate = paramBundle.getInt("GsmBitErrorRate");
    mGsmRssi = paramBundle.getInt("GsmRssi");
    mCdmaDbm = paramBundle.getInt("CdmaDbm");
    mCdmaEcio = paramBundle.getInt("CdmaEcio");
    mEvdoDbm = paramBundle.getInt("EvdoDbm");
    mEvdoEcio = paramBundle.getInt("EvdoEcio");
    mEvdoSnr = paramBundle.getInt("EvdoSnr");
    mLteSignalStrength = paramBundle.getInt("LteSignalStrength");
    mLteRsrp = paramBundle.getInt("LteRsrp");
    mLteRsrq = paramBundle.getInt("LteRsrq");
    mLteRssnr = paramBundle.getInt("LteRssnr");
    mLteCqi = paramBundle.getInt("LteCqi");
    mLteRsrpBoost = paramBundle.getInt("LteRsrpBoost");
    mTdScdmaRscp = paramBundle.getInt("TdScdma");
    mWcdmaSignalStrength = paramBundle.getInt("WcdmaSignalStrength");
    mWcdmaRscpAsu = paramBundle.getInt("WcdmaRscpAsu");
    mWcdmaRscp = paramBundle.getInt("WcdmaRscp");
    mIsGsm = paramBundle.getBoolean("IsGsm");
    mUseOnlyRsrpForLteLevel = paramBundle.getBoolean("UseOnlyRsrpForLteLevel");
    mWcdmaDefaultSignalMeasurement = paramBundle.getString("WcdmaDefaultSignalMeasurement");
    ArrayList localArrayList = paramBundle.getIntegerArrayList("lteRsrpThresholds");
    int i = 0;
    for (int j = 0; j < localArrayList.size(); j++) {
      mLteRsrpThresholds[j] = ((Integer)localArrayList.get(j)).intValue();
    }
    paramBundle = paramBundle.getIntegerArrayList("wcdmaRscpThresholds");
    for (j = i; j < paramBundle.size(); j++) {
      mWcdmaRscpThresholds[j] = ((Integer)paramBundle.get(j)).intValue();
    }
  }
  
  protected void copyFrom(SignalStrength paramSignalStrength)
  {
    mGsmSignalStrength = mGsmSignalStrength;
    mGsmBitErrorRate = mGsmBitErrorRate;
    mGsmRssi = mGsmRssi;
    mCdmaDbm = mCdmaDbm;
    mCdmaEcio = mCdmaEcio;
    mEvdoDbm = mEvdoDbm;
    mEvdoEcio = mEvdoEcio;
    mEvdoSnr = mEvdoSnr;
    mLteSignalStrength = mLteSignalStrength;
    mLteRsrp = mLteRsrp;
    mLteRsrq = mLteRsrq;
    mLteRssnr = mLteRssnr;
    mLteCqi = mLteCqi;
    mTdScdmaRscp = mTdScdmaRscp;
    mWcdmaSignalStrength = mWcdmaSignalStrength;
    mWcdmaRscpAsu = mWcdmaRscpAsu;
    mWcdmaRscp = mWcdmaRscp;
    mLteRsrpBoost = mLteRsrpBoost;
    mIsGsm = mIsGsm;
    mUseOnlyRsrpForLteLevel = mUseOnlyRsrpForLteLevel;
    mWcdmaDefaultSignalMeasurement = mWcdmaDefaultSignalMeasurement;
    setLteRsrpThresholds(mLteRsrpThresholds);
    setWcdmaRscpThresholds(mWcdmaRscpThresholds);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool1 = false;
    try
    {
      SignalStrength localSignalStrength = (SignalStrength)paramObject;
      if (paramObject == null) {
        return false;
      }
      boolean bool2 = bool1;
      if (mGsmSignalStrength == mGsmSignalStrength)
      {
        bool2 = bool1;
        if (mGsmBitErrorRate == mGsmBitErrorRate)
        {
          bool2 = bool1;
          if (mGsmRssi == mGsmRssi)
          {
            bool2 = bool1;
            if (mCdmaDbm == mCdmaDbm)
            {
              bool2 = bool1;
              if (mCdmaEcio == mCdmaEcio)
              {
                bool2 = bool1;
                if (mEvdoDbm == mEvdoDbm)
                {
                  bool2 = bool1;
                  if (mEvdoEcio == mEvdoEcio)
                  {
                    bool2 = bool1;
                    if (mEvdoSnr == mEvdoSnr)
                    {
                      bool2 = bool1;
                      if (mLteSignalStrength == mLteSignalStrength)
                      {
                        bool2 = bool1;
                        if (mLteRsrp == mLteRsrp)
                        {
                          bool2 = bool1;
                          if (mLteRsrq == mLteRsrq)
                          {
                            bool2 = bool1;
                            if (mLteRssnr == mLteRssnr)
                            {
                              bool2 = bool1;
                              if (mLteCqi == mLteCqi)
                              {
                                bool2 = bool1;
                                if (mLteRsrpBoost == mLteRsrpBoost)
                                {
                                  bool2 = bool1;
                                  if (mTdScdmaRscp == mTdScdmaRscp)
                                  {
                                    bool2 = bool1;
                                    if (mWcdmaSignalStrength == mWcdmaSignalStrength)
                                    {
                                      bool2 = bool1;
                                      if (mWcdmaRscpAsu == mWcdmaRscpAsu)
                                      {
                                        bool2 = bool1;
                                        if (mWcdmaRscp == mWcdmaRscp)
                                        {
                                          bool2 = bool1;
                                          if (mIsGsm == mIsGsm)
                                          {
                                            bool2 = bool1;
                                            if (mUseOnlyRsrpForLteLevel == mUseOnlyRsrpForLteLevel)
                                            {
                                              bool2 = bool1;
                                              if (Objects.equals(mWcdmaDefaultSignalMeasurement, mWcdmaDefaultSignalMeasurement))
                                              {
                                                bool2 = bool1;
                                                if (Arrays.equals(mLteRsrpThresholds, mLteRsrpThresholds))
                                                {
                                                  bool2 = bool1;
                                                  if (Arrays.equals(mWcdmaRscpThresholds, mWcdmaRscpThresholds)) {
                                                    bool2 = true;
                                                  }
                                                }
                                              }
                                            }
                                          }
                                        }
                                      }
                                    }
                                  }
                                }
                              }
                            }
                          }
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }
      return bool2;
    }
    catch (ClassCastException paramObject) {}
    return false;
  }
  
  public void fillInNotifierBundle(Bundle paramBundle)
  {
    paramBundle.putInt("GsmSignalStrength", mGsmSignalStrength);
    paramBundle.putInt("GsmBitErrorRate", mGsmBitErrorRate);
    paramBundle.putInt("GsmRssi", mGsmRssi);
    paramBundle.putInt("CdmaDbm", mCdmaDbm);
    paramBundle.putInt("CdmaEcio", mCdmaEcio);
    paramBundle.putInt("EvdoDbm", mEvdoDbm);
    paramBundle.putInt("EvdoEcio", mEvdoEcio);
    paramBundle.putInt("EvdoSnr", mEvdoSnr);
    paramBundle.putInt("LteSignalStrength", mLteSignalStrength);
    paramBundle.putInt("LteRsrp", mLteRsrp);
    paramBundle.putInt("LteRsrq", mLteRsrq);
    paramBundle.putInt("LteRssnr", mLteRssnr);
    paramBundle.putInt("LteCqi", mLteCqi);
    paramBundle.putInt("LteRsrpBoost", mLteRsrpBoost);
    paramBundle.putInt("TdScdma", mTdScdmaRscp);
    paramBundle.putInt("WcdmaSignalStrength", mWcdmaSignalStrength);
    paramBundle.putInt("WcdmaRscpAsu", mWcdmaRscpAsu);
    paramBundle.putInt("WcdmaRscp", mWcdmaRscp);
    paramBundle.putBoolean("IsGsm", mIsGsm);
    paramBundle.putBoolean("UseOnlyRsrpForLteLevel", mUseOnlyRsrpForLteLevel);
    paramBundle.putString("WcdmaDefaultSignalMeasurement", mWcdmaDefaultSignalMeasurement);
    ArrayList localArrayList = new ArrayList();
    int[] arrayOfInt = mLteRsrpThresholds;
    int i = arrayOfInt.length;
    int j = 0;
    for (int k = 0; k < i; k++) {
      localArrayList.add(Integer.valueOf(arrayOfInt[k]));
    }
    paramBundle.putIntegerArrayList("lteRsrpThresholds", localArrayList);
    localArrayList = new ArrayList();
    arrayOfInt = mWcdmaRscpThresholds;
    i = arrayOfInt.length;
    for (k = j; k < i; k++) {
      localArrayList.add(Integer.valueOf(arrayOfInt[k]));
    }
    paramBundle.putIntegerArrayList("wcdmaRscpThresholds", localArrayList);
  }
  
  public void fixType()
  {
    boolean bool;
    if (getCdmaRelatedSignalStrength() == 0) {
      bool = true;
    } else {
      bool = false;
    }
    mIsGsm = bool;
  }
  
  public int getAsuLevel()
  {
    int i;
    if (mIsGsm)
    {
      if (mLteRsrp != Integer.MAX_VALUE) {
        i = getLteAsuLevel();
      } else if (mTdScdmaRscp != Integer.MAX_VALUE) {
        i = getTdScdmaAsuLevel();
      } else if (mWcdmaRscp != Integer.MAX_VALUE) {
        i = getWcdmaAsuLevel();
      } else {
        i = getGsmAsuLevel();
      }
    }
    else
    {
      int j = getCdmaAsuLevel();
      i = getEvdoAsuLevel();
      if (i == 0) {
        i = j;
      } else if ((j != 0) && (j < i)) {
        i = j;
      }
    }
    return i;
  }
  
  public int getAsusCdmaLevel()
  {
    int i = getCdmaDbm();
    getCdmaEcio();
    if (i > -2) {
      i = 0;
    }
    for (;;)
    {
      break;
      if (i >= -86) {
        i = 5;
      } else if (i >= -94) {
        i = 4;
      } else if (i >= -101) {
        i = 3;
      } else if (i >= -106) {
        i = 2;
      } else if (i >= -114) {
        i = 1;
      } else {
        i = 0;
      }
    }
    return i;
  }
  
  public int getAsusEvdoLevel()
  {
    int i = getEvdoDbm();
    getEvdoSnr();
    if (i > -2) {
      i = 0;
    }
    for (;;)
    {
      break;
      if (i >= -86) {
        i = 5;
      } else if (i >= -92) {
        i = 4;
      } else if (i >= -99) {
        i = 3;
      } else if (i >= -106) {
        i = 2;
      } else if (i >= -114) {
        i = 1;
      } else {
        i = 0;
      }
    }
    return i;
  }
  
  public int getAsusGsmLevel()
  {
    int i = getGsmSignalStrength();
    int j = getGsmRssi();
    int k;
    Object localObject;
    if (mCdmaEcio == 65376)
    {
      if ((j > -115) && (j < -2))
      {
        if (j >= -90) {
          k = 5;
        } else if (j >= -96) {
          k = 4;
        } else if (j >= -101) {
          k = 3;
        } else if (j >= -106) {
          k = 2;
        } else {
          k = 1;
        }
      }
      else {
        k = 0;
      }
    }
    else
    {
      localObject = Resources.getSystem().getIntArray(17235982);
      if (localObject.length != 6) {
        localObject = WCDMA_DBM_THRESH;
      }
      if ((j >= localObject[0]) && (j <= localObject[5]))
      {
        if (j >= localObject[4]) {
          k = 5;
        } else if (j >= localObject[3]) {
          k = 4;
        } else if (j >= localObject[2]) {
          k = 3;
        } else if (j >= localObject[1]) {
          k = 2;
        } else {
          k = 1;
        }
      }
      else {
        k = 0;
      }
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("######get ASUS GSM signal=");
    localStringBuilder.append(i);
    localStringBuilder.append(",rssi=");
    localStringBuilder.append(j);
    if (mCdmaEcio == 65376) {
      localObject = " 2G";
    } else {
      localObject = " 3G";
    }
    localStringBuilder.append((String)localObject);
    localStringBuilder.append(" IconLevel:");
    localStringBuilder.append(k);
    log(localStringBuilder.toString());
    return k;
  }
  
  public int getAsusLevel()
  {
    int i;
    if (mIsGsm) {
      i = getAsusGsmRelatedSignalStrength();
    } else {
      i = getAsusCdmaRelatedSignalStrength();
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("getAsusLevel=");
    localStringBuilder.append(i);
    log(localStringBuilder.toString());
    return i;
  }
  
  public int getAsusLteLevel()
  {
    int i = 0;
    Object localObject = Resources.getSystem().getIntArray(17235981);
    if (localObject.length != 6) {
      localObject = LTE_DBM_THRESH;
    }
    int j;
    if (mLteRsrp > localObject[5]) {
      j = -1;
    } else if (mLteRsrp >= localObject[4]) {
      j = 5;
    } else if (mLteRsrp >= localObject[3]) {
      j = 4;
    } else if (mLteRsrp >= localObject[2]) {
      j = 3;
    } else if (mLteRsrp >= localObject[1]) {
      j = 2;
    } else if (mLteRsrp >= localObject[0]) {
      j = 1;
    } else {
      j = 0;
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("######get ASUS LTE signal rsrp:");
    ((StringBuilder)localObject).append(mLteRsrp);
    ((StringBuilder)localObject).append(" IconLevel:");
    ((StringBuilder)localObject).append(j);
    log(((StringBuilder)localObject).toString());
    if (j != -1) {
      return j;
    }
    if (mLteSignalStrength > 63)
    {
      j = 0;
    }
    else if (mLteSignalStrength >= 12)
    {
      j = 4;
    }
    else if (mLteSignalStrength >= 8)
    {
      j = 3;
    }
    else if (mLteSignalStrength >= 5)
    {
      j = 2;
    }
    else
    {
      j = i;
      if (mLteSignalStrength >= 0) {
        j = 1;
      }
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("getAsusLTELevel - rssi:");
    ((StringBuilder)localObject).append(mLteSignalStrength);
    ((StringBuilder)localObject).append(" rssiIconLevel:");
    ((StringBuilder)localObject).append(j);
    log(((StringBuilder)localObject).toString());
    return j;
  }
  
  public int getAsusTdScdmaLevel()
  {
    int i = getTdScdmaDbm();
    int j;
    if ((i <= -24) && (i != Integer.MAX_VALUE))
    {
      if (i >= -91) {
        j = 5;
      } else if (i >= -97) {
        j = 4;
      } else if (i >= -102) {
        j = 3;
      } else if (i >= -107) {
        j = 2;
      } else if (i >= -114) {
        j = 1;
      } else {
        j = 0;
      }
    }
    else {
      j = 0;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("######get ASUS TD-SCDMA signal :");
    localStringBuilder.append(i);
    localStringBuilder.append(" IconLevel:");
    localStringBuilder.append(j);
    log(localStringBuilder.toString());
    return j;
  }
  
  public int getAsusWcdmaLevel()
  {
    int i = 0;
    int j = getWcdmaDbm();
    Object localObject = WCDMA_DBM_THRESH;
    localObject = Resources.getSystem().getIntArray(17235982);
    if (localObject.length != 6) {
      localObject = WCDMA_DBM_THRESH;
    }
    if (j > localObject[5]) {
      i = 0;
    } else if (j >= localObject[4]) {
      i = 5;
    } else if (j >= localObject[3]) {
      i = 4;
    } else if (j >= localObject[2]) {
      i = 3;
    } else if (j >= localObject[1]) {
      i = 2;
    } else if (j >= localObject[0]) {
      i = 1;
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("getAsusWcdmaLevel-dbm:");
    ((StringBuilder)localObject).append(j);
    ((StringBuilder)localObject).append(" level=");
    ((StringBuilder)localObject).append(i);
    log(((StringBuilder)localObject).toString());
    return i;
  }
  
  public int getCdmaAsuLevel()
  {
    int i = getCdmaDbm();
    int j = getCdmaEcio();
    int k = 99;
    if (i >= -75) {
      i = 16;
    }
    for (;;)
    {
      m = i;
      break label77;
      if (i >= -82)
      {
        i = 8;
      }
      else if (i >= -90)
      {
        i = 4;
      }
      else if (i >= -95)
      {
        i = 2;
      }
      else
      {
        if (i < -100) {
          break;
        }
        i = 1;
      }
    }
    int m = 99;
    label77:
    if (j >= -90) {
      i = 16;
    }
    for (;;)
    {
      break;
      if (j >= -100) {
        i = 8;
      } else if (j >= -115) {
        i = 4;
      } else if (j >= 65406) {
        i = 2;
      } else if (j >= 65386) {
        i = 1;
      } else {
        i = k;
      }
    }
    if (m < i) {
      i = m;
    }
    return i;
  }
  
  public int getCdmaDbm()
  {
    return mCdmaDbm;
  }
  
  public int getCdmaEcio()
  {
    return mCdmaEcio;
  }
  
  public int getCdmaLevel()
  {
    int i = getCdmaDbm();
    int j = getCdmaEcio();
    int k = 0;
    if (i >= -75) {
      i = 4;
    }
    for (;;)
    {
      m = i;
      break label62;
      if (i >= -85)
      {
        i = 3;
      }
      else if (i >= -95)
      {
        i = 2;
      }
      else
      {
        if (i < -100) {
          break;
        }
        i = 1;
      }
    }
    int m = 0;
    label62:
    if (j >= -90) {
      i = 4;
    }
    for (;;)
    {
      break;
      if (j >= -110) {
        i = 3;
      } else if (j >= 65406) {
        i = 2;
      } else if (j >= 65386) {
        i = 1;
      } else {
        i = k;
      }
    }
    if (m < i) {
      i = m;
    }
    return i;
  }
  
  public int getDbm()
  {
    if (isGsm())
    {
      i = getLteDbm();
      j = i;
      if (i == Integer.MAX_VALUE) {
        if (getTdScdmaDbm() == Integer.MAX_VALUE)
        {
          if (getWcdmaDbm() == Integer.MAX_VALUE) {
            j = getGsmDbm();
          } else {
            j = getWcdmaDbm();
          }
        }
        else {
          j = getTdScdmaDbm();
        }
      }
      return j;
    }
    int j = getCdmaDbm();
    int i = getEvdoDbm();
    if ((i == -120) || ((j == -120) || (j >= i))) {
      j = i;
    }
    return j;
  }
  
  public int getEvdoAsuLevel()
  {
    int i = getEvdoDbm();
    int j = getEvdoSnr();
    int k = 99;
    if (i >= -65) {
      i = 16;
    }
    for (;;)
    {
      m = i;
      break label77;
      if (i >= -75)
      {
        i = 8;
      }
      else if (i >= -85)
      {
        i = 4;
      }
      else if (i >= -95)
      {
        i = 2;
      }
      else
      {
        if (i < -105) {
          break;
        }
        i = 1;
      }
    }
    int m = 99;
    label77:
    if (j >= 7) {
      i = 16;
    }
    for (;;)
    {
      break;
      if (j >= 6) {
        i = 8;
      } else if (j >= 5) {
        i = 4;
      } else if (j >= 3) {
        i = 2;
      } else if (j >= 1) {
        i = 1;
      } else {
        i = k;
      }
    }
    if (m < i) {
      i = m;
    }
    return i;
  }
  
  public int getEvdoDbm()
  {
    return mEvdoDbm;
  }
  
  public int getEvdoEcio()
  {
    return mEvdoEcio;
  }
  
  public int getEvdoLevel()
  {
    int i = getEvdoDbm();
    int j = getEvdoSnr();
    int k = 0;
    if (i >= -65) {
      i = 4;
    }
    for (;;)
    {
      m = i;
      break label62;
      if (i >= -75)
      {
        i = 3;
      }
      else if (i >= -90)
      {
        i = 2;
      }
      else
      {
        if (i < -105) {
          break;
        }
        i = 1;
      }
    }
    int m = 0;
    label62:
    if (j >= 7) {
      i = 4;
    }
    for (;;)
    {
      break;
      if (j >= 5) {
        i = 3;
      } else if (j >= 3) {
        i = 2;
      } else if (j >= 1) {
        i = 1;
      } else {
        i = k;
      }
    }
    if (m < i) {
      i = m;
    }
    return i;
  }
  
  public int getEvdoSnr()
  {
    return mEvdoSnr;
  }
  
  public int getGsmAsuLevel()
  {
    return getGsmSignalStrength();
  }
  
  public int getGsmBitErrorRate()
  {
    return mGsmBitErrorRate;
  }
  
  public int getGsmDbm()
  {
    return mGsmRssi;
  }
  
  public int getGsmLevel()
  {
    int i = getGsmSignalStrength();
    int j = getGsmRssi();
    int k;
    if (mCdmaEcio == 65376) {
      if ((j > -110) && (j < 0))
      {
        if (j >= -89) {
          k = 4;
        } else if (j >= -97) {
          k = 3;
        } else if (j >= -103) {
          k = 2;
        } else {
          k = 1;
        }
      }
      else {
        k = 0;
      }
    }
    for (;;)
    {
      break;
      if ((j >= -113) && (j < 0))
      {
        if (j >= -91)
        {
          k = 4;
        }
        else if (j >= -98)
        {
          k = 3;
        }
        else if (j >= -106)
        {
          k = 2;
        }
        else
        {
          k = 1;
          break;
        }
      }
      else {
        k = 0;
      }
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("######get GSM signal=");
    localStringBuilder.append(i);
    localStringBuilder.append(",rssi=");
    localStringBuilder.append(j);
    String str;
    if (mCdmaEcio == 65376) {
      str = " 2G";
    } else {
      str = " 3G";
    }
    localStringBuilder.append(str);
    localStringBuilder.append(" IconLevel:");
    localStringBuilder.append(k);
    log(localStringBuilder.toString());
    return k;
  }
  
  public int getGsmRssi()
  {
    return mGsmRssi;
  }
  
  public int getGsmSignalStrength()
  {
    return mGsmSignalStrength;
  }
  
  public int getLevel()
  {
    int i;
    if (mIsGsm) {
      i = getGsmRelatedSignalStrength();
    } else {
      i = getCdmaRelatedSignalStrength();
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("getLevel=");
    localStringBuilder.append(i);
    log(localStringBuilder.toString());
    return i;
  }
  
  public int getLteAsuLevel()
  {
    int i = getLteDbm();
    if (i == Integer.MAX_VALUE) {
      i = 255;
    } else {
      i += 140;
    }
    return i;
  }
  
  public int getLteCqi()
  {
    return mLteCqi;
  }
  
  public int getLteDbm()
  {
    return mLteRsrp;
  }
  
  public int getLteLevel()
  {
    int i = 0;
    int j;
    if (mLteRsrp > -44) {
      j = -1;
    } else if (mLteRsrp >= -98) {
      j = 4;
    } else if (mLteRsrp >= -106) {
      j = 3;
    } else if (mLteRsrp >= -114) {
      j = 2;
    } else if (mLteRsrp >= -126) {
      j = 1;
    } else {
      j = 0;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("######get LTE signal rsrp:");
    localStringBuilder.append(mLteRsrp);
    localStringBuilder.append(" IconLevel:");
    localStringBuilder.append(j);
    log(localStringBuilder.toString());
    if (j != -1) {
      return j;
    }
    if (mLteSignalStrength > 63)
    {
      j = 0;
    }
    else if (mLteSignalStrength >= 12)
    {
      j = 4;
    }
    else if (mLteSignalStrength >= 8)
    {
      j = 3;
    }
    else if (mLteSignalStrength >= 5)
    {
      j = 2;
    }
    else
    {
      j = i;
      if (mLteSignalStrength >= 0) {
        j = 1;
      }
    }
    localStringBuilder = new StringBuilder();
    localStringBuilder.append("getLteLevel - rssi:");
    localStringBuilder.append(mLteSignalStrength);
    localStringBuilder.append(" rssiIconLevel:");
    localStringBuilder.append(j);
    log(localStringBuilder.toString());
    return j;
  }
  
  public int getLteRsrp()
  {
    return mLteRsrp;
  }
  
  public int getLteRsrpBoost()
  {
    return mLteRsrpBoost;
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
  
  public int getTdScdmaAsuLevel()
  {
    int i = getTdScdmaDbm();
    if (i == Integer.MAX_VALUE) {
      i = 255;
    } else {
      i += 120;
    }
    return i;
  }
  
  public int getTdScdmaDbm()
  {
    return mTdScdmaRscp;
  }
  
  public int getTdScdmaLevel()
  {
    int i = getTdScdmaDbm();
    int j;
    if ((i <= -25) && (i != Integer.MAX_VALUE))
    {
      if (i >= -91) {
        j = 4;
      } else if (i >= -98) {
        j = 3;
      } else if (i >= -105) {
        j = 2;
      } else if (i >= -112) {
        j = 1;
      } else {
        j = 0;
      }
    }
    else {
      j = 0;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("######get TD-SCDMA signal :");
    localStringBuilder.append(i);
    localStringBuilder.append(" IconLevel:");
    localStringBuilder.append(j);
    log(localStringBuilder.toString());
    return j;
  }
  
  public int getWcdmaAsuLevel()
  {
    int i = getWcdmaDbm();
    if (i == Integer.MAX_VALUE) {
      i = 255;
    } else {
      i += 120;
    }
    return i;
  }
  
  public int getWcdmaDbm()
  {
    return mWcdmaRscp;
  }
  
  public int getWcdmaLevel()
  {
    int i = 0;
    int j = 0;
    if (mWcdmaDefaultSignalMeasurement == null)
    {
      Log.wtf("SignalStrength", "getWcdmaLevel - WCDMA default signal measurement is invalid.");
      return 0;
    }
    Object localObject = mWcdmaDefaultSignalMeasurement;
    int k = -1;
    if ((((String)localObject).hashCode() == 3509870) && (((String)localObject).equals("rscp"))) {
      k = 0;
    }
    if (k != 0)
    {
      if ((mWcdmaSignalStrength >= 0) && (mWcdmaSignalStrength <= 31))
      {
        if (mWcdmaSignalStrength >= 18)
        {
          k = 4;
        }
        else if (mWcdmaSignalStrength >= 13)
        {
          k = 3;
        }
        else if (mWcdmaSignalStrength >= 8)
        {
          k = 2;
        }
        else
        {
          k = j;
          if (mWcdmaSignalStrength >= 3) {
            k = 1;
          }
        }
      }
      else
      {
        k = j;
        if (mWcdmaSignalStrength != 99)
        {
          localObject = new StringBuilder();
          ((StringBuilder)localObject).append("getWcdmaLevel - invalid WCDMA RSSI: mWcdmaSignalStrength=");
          ((StringBuilder)localObject).append(mWcdmaSignalStrength);
          Log.wtf("SignalStrength", ((StringBuilder)localObject).toString());
          k = j;
        }
      }
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("getWcdmaLevel=");
      ((StringBuilder)localObject).append(k);
      ((StringBuilder)localObject).append(" WcdmaSignalStrength=");
      ((StringBuilder)localObject).append(mWcdmaSignalStrength);
      log(((StringBuilder)localObject).toString());
    }
    else
    {
      if ((mWcdmaRscp >= -120) && (mWcdmaRscp <= -24))
      {
        if (mWcdmaRscp >= mWcdmaRscpThresholds[3])
        {
          k = 4;
        }
        else if (mWcdmaRscp >= mWcdmaRscpThresholds[2])
        {
          k = 3;
        }
        else if (mWcdmaRscp >= mWcdmaRscpThresholds[1])
        {
          k = 2;
        }
        else
        {
          k = i;
          if (mWcdmaRscp >= mWcdmaRscpThresholds[0]) {
            k = 1;
          }
        }
      }
      else
      {
        k = i;
        if (mWcdmaRscp != Integer.MAX_VALUE)
        {
          localObject = new StringBuilder();
          ((StringBuilder)localObject).append("getWcdmaLevel - invalid WCDMA RSCP: mWcdmaRscp=");
          ((StringBuilder)localObject).append(mWcdmaRscp);
          Log.wtf("SignalStrength", ((StringBuilder)localObject).toString());
          k = i;
        }
      }
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("getWcdmaLevel=");
      ((StringBuilder)localObject).append(k);
      ((StringBuilder)localObject).append(" WcdmaRscp=");
      ((StringBuilder)localObject).append(mWcdmaRscp);
      log(((StringBuilder)localObject).toString());
    }
    return k;
  }
  
  public int getWcdmaRscp()
  {
    return mWcdmaRscp;
  }
  
  public int hashCode()
  {
    return mGsmSignalStrength * 31 + mGsmBitErrorRate * 31 + mGsmRssi * 31 + mCdmaDbm * 31 + mCdmaEcio * 31 + mEvdoDbm * 31 + mEvdoEcio * 31 + mEvdoSnr * 31 + mLteSignalStrength * 31 + mLteRsrp * 31 + mLteRsrq * 31 + mLteRssnr * 31 + mLteCqi * 31 + mLteRsrpBoost * 31 + mTdScdmaRscp * 31 + mWcdmaSignalStrength * 31 + mWcdmaRscpAsu * 31 + mWcdmaRscp * 31 + mIsGsm + mUseOnlyRsrpForLteLevel + Objects.hashCode(mWcdmaDefaultSignalMeasurement) + Arrays.hashCode(mLteRsrpThresholds) + Arrays.hashCode(mWcdmaRscpThresholds);
  }
  
  public boolean isGsm()
  {
    return mIsGsm;
  }
  
  public void setGsm(boolean paramBoolean)
  {
    mIsGsm = paramBoolean;
  }
  
  public void setLteRsrpBoost(int paramInt)
  {
    mLteRsrpBoost = paramInt;
  }
  
  public void setLteRsrpThresholds(int[] paramArrayOfInt)
  {
    if ((paramArrayOfInt != null) && (paramArrayOfInt.length == 8))
    {
      System.arraycopy(paramArrayOfInt, 0, mLteRsrpThresholds, 0, 8);
      return;
    }
    Log.wtf("SignalStrength", "setLteRsrpThresholds - lteRsrpThresholds is invalid.");
  }
  
  public void setUseOnlyRsrpForLteLevel(boolean paramBoolean)
  {
    mUseOnlyRsrpForLteLevel = paramBoolean;
  }
  
  public void setWcdmaDefaultSignalMeasurement(String paramString)
  {
    mWcdmaDefaultSignalMeasurement = paramString;
  }
  
  public void setWcdmaRscpThresholds(int[] paramArrayOfInt)
  {
    if ((paramArrayOfInt != null) && (paramArrayOfInt.length == 8))
    {
      System.arraycopy(paramArrayOfInt, 0, mWcdmaRscpThresholds, 0, 8);
      return;
    }
    Log.wtf("SignalStrength", "setWcdmaRscpThresholds - wcdmaRscpThresholds is invalid.");
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("SignalStrength: ");
    localStringBuilder.append(mGsmSignalStrength);
    localStringBuilder.append(" ");
    localStringBuilder.append(mGsmBitErrorRate);
    localStringBuilder.append(" ");
    localStringBuilder.append(mGsmRssi);
    localStringBuilder.append(" ");
    localStringBuilder.append(mCdmaDbm);
    localStringBuilder.append(" ");
    localStringBuilder.append(mCdmaEcio);
    localStringBuilder.append(" ");
    localStringBuilder.append(mEvdoDbm);
    localStringBuilder.append(" ");
    localStringBuilder.append(mEvdoEcio);
    localStringBuilder.append(" ");
    localStringBuilder.append(mEvdoSnr);
    localStringBuilder.append(" ");
    localStringBuilder.append(mLteSignalStrength);
    localStringBuilder.append(" ");
    localStringBuilder.append(mLteRsrp);
    localStringBuilder.append(" ");
    localStringBuilder.append(mLteRsrq);
    localStringBuilder.append(" ");
    localStringBuilder.append(mLteRssnr);
    localStringBuilder.append(" ");
    localStringBuilder.append(mLteCqi);
    localStringBuilder.append(" ");
    localStringBuilder.append(mLteRsrpBoost);
    localStringBuilder.append(" ");
    localStringBuilder.append(mTdScdmaRscp);
    localStringBuilder.append(" ");
    localStringBuilder.append(mWcdmaSignalStrength);
    localStringBuilder.append(" ");
    localStringBuilder.append(mWcdmaRscpAsu);
    localStringBuilder.append(" ");
    localStringBuilder.append(mWcdmaRscp);
    localStringBuilder.append(" ");
    String str;
    if (mIsGsm) {
      str = "gsm|lte";
    } else {
      str = "cdma";
    }
    localStringBuilder.append(str);
    localStringBuilder.append(" ");
    if (mUseOnlyRsrpForLteLevel) {
      str = "use_only_rsrp_for_lte_level";
    } else {
      str = "use_rsrp_and_rssnr_for_lte_level";
    }
    localStringBuilder.append(str);
    localStringBuilder.append(" ");
    localStringBuilder.append(mWcdmaDefaultSignalMeasurement);
    localStringBuilder.append(" ");
    localStringBuilder.append(Arrays.toString(mLteRsrpThresholds));
    localStringBuilder.append(" ");
    localStringBuilder.append(Arrays.toString(mWcdmaRscpThresholds));
    return localStringBuilder.toString();
  }
  
  public boolean useOnlyRsrpForLteLevel()
  {
    return mUseOnlyRsrpForLteLevel;
  }
  
  public void validateInput()
  {
    int i = mGsmSignalStrength;
    int j = 99;
    if (i >= 0) {
      i = mGsmSignalStrength;
    } else {
      i = 99;
    }
    mGsmSignalStrength = i;
    if (mGsmRssi < 0) {
      i = mGsmRssi;
    } else {
      i = 0;
    }
    mGsmRssi = i;
    if (mWcdmaSignalStrength >= 0) {
      i = mWcdmaSignalStrength;
    } else {
      i = 99;
    }
    mWcdmaSignalStrength = i;
    i = j;
    if (mLteSignalStrength >= 0) {
      i = mLteSignalStrength;
    }
    mLteSignalStrength = i;
    i = mWcdmaRscpAsu;
    int k = -120;
    if ((i - 120 >= -120) && (mWcdmaRscpAsu - 120 <= -24)) {
      i = mWcdmaRscpAsu;
    } else {
      i = 255;
    }
    mWcdmaRscpAsu = i;
    i = mWcdmaRscp;
    j = Integer.MAX_VALUE;
    if ((i >= -120) && (mWcdmaRscp <= -24)) {
      i = mWcdmaRscp;
    } else {
      i = Integer.MAX_VALUE;
    }
    mWcdmaRscp = i;
    if (mCdmaDbm > 0) {
      i = -mCdmaDbm;
    } else {
      i = -120;
    }
    mCdmaDbm = i;
    i = mCdmaEcio;
    int m = 65376;
    if (i >= 0) {
      i = -mCdmaEcio;
    } else {
      i = 65376;
    }
    mCdmaEcio = i;
    i = k;
    if (mEvdoDbm > 0) {
      i = -mEvdoDbm;
    }
    mEvdoDbm = i;
    i = m;
    if (mEvdoEcio >= 0) {
      i = -mEvdoEcio;
    }
    mEvdoEcio = i;
    if ((mEvdoSnr >= 0) && (mEvdoSnr <= 8)) {
      i = mEvdoSnr;
    } else {
      i = -1;
    }
    mEvdoSnr = i;
    if ((-mLteRsrp >= 65396) && (-mLteRsrp <= -44)) {
      i = -mLteRsrp;
    } else {
      i = Integer.MAX_VALUE;
    }
    mLteRsrp = i;
    if ((mLteRsrq >= 3) && (mLteRsrq <= 20)) {
      i = -mLteRsrq;
    } else {
      i = Integer.MAX_VALUE;
    }
    mLteRsrq = i;
    if ((mLteRssnr >= 65336) && (mLteRssnr <= 300)) {
      i = mLteRssnr;
    } else {
      i = Integer.MAX_VALUE;
    }
    mLteRssnr = i;
    i = j;
    if (mTdScdmaRscp >= 0)
    {
      i = j;
      if (mTdScdmaRscp <= 96) {
        i = mTdScdmaRscp - 120;
      }
    }
    mTdScdmaRscp = i;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mGsmSignalStrength);
    paramParcel.writeInt(mGsmBitErrorRate);
    paramParcel.writeInt(mGsmRssi);
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
    paramParcel.writeInt(mTdScdmaRscp);
    paramParcel.writeInt(mWcdmaSignalStrength);
    paramParcel.writeInt(mWcdmaRscpAsu);
    paramParcel.writeInt(mWcdmaRscp);
    paramParcel.writeInt(mLteRsrpBoost);
    paramParcel.writeBoolean(mIsGsm);
    paramParcel.writeBoolean(mUseOnlyRsrpForLteLevel);
    paramParcel.writeString(mWcdmaDefaultSignalMeasurement);
    paramParcel.writeIntArray(mLteRsrpThresholds);
    paramParcel.writeIntArray(mWcdmaRscpThresholds);
  }
}
