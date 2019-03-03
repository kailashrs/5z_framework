package android.telephony;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class ServiceState
  implements Parcelable
{
  public static final Parcelable.Creator<ServiceState> CREATOR = new Parcelable.Creator()
  {
    public ServiceState createFromParcel(Parcel paramAnonymousParcel)
    {
      return new ServiceState(paramAnonymousParcel);
    }
    
    public ServiceState[] newArray(int paramAnonymousInt)
    {
      return new ServiceState[paramAnonymousInt];
    }
  };
  static final boolean DBG = false;
  public static final int DUPLEX_MODE_FDD = 1;
  public static final int DUPLEX_MODE_TDD = 2;
  public static final int DUPLEX_MODE_UNKNOWN = 0;
  private static final int NEXT_RIL_RADIO_TECHNOLOGY = 20;
  public static final int RIL_RADIO_CDMA_TECHNOLOGY_BITMASK = 6392;
  public static final int RIL_RADIO_TECHNOLOGY_1xRTT = 6;
  public static final int RIL_RADIO_TECHNOLOGY_EDGE = 2;
  public static final int RIL_RADIO_TECHNOLOGY_EHRPD = 13;
  public static final int RIL_RADIO_TECHNOLOGY_EVDO_0 = 7;
  public static final int RIL_RADIO_TECHNOLOGY_EVDO_A = 8;
  public static final int RIL_RADIO_TECHNOLOGY_EVDO_B = 12;
  public static final int RIL_RADIO_TECHNOLOGY_GPRS = 1;
  public static final int RIL_RADIO_TECHNOLOGY_GSM = 16;
  public static final int RIL_RADIO_TECHNOLOGY_HSDPA = 9;
  public static final int RIL_RADIO_TECHNOLOGY_HSPA = 11;
  public static final int RIL_RADIO_TECHNOLOGY_HSPAP = 15;
  public static final int RIL_RADIO_TECHNOLOGY_HSUPA = 10;
  public static final int RIL_RADIO_TECHNOLOGY_IS95A = 4;
  public static final int RIL_RADIO_TECHNOLOGY_IS95B = 5;
  public static final int RIL_RADIO_TECHNOLOGY_IWLAN = 18;
  public static final int RIL_RADIO_TECHNOLOGY_LTE = 14;
  public static final int RIL_RADIO_TECHNOLOGY_LTE_CA = 19;
  public static final int RIL_RADIO_TECHNOLOGY_TD_SCDMA = 17;
  public static final int RIL_RADIO_TECHNOLOGY_UMTS = 3;
  public static final int RIL_RADIO_TECHNOLOGY_UNKNOWN = 0;
  public static final int ROAMING_TYPE_DOMESTIC = 2;
  public static final int ROAMING_TYPE_INTERNATIONAL = 3;
  public static final int ROAMING_TYPE_NOT_ROAMING = 0;
  public static final int ROAMING_TYPE_UNKNOWN = 1;
  public static final int STATE_EMERGENCY_ONLY = 2;
  public static final int STATE_IN_SERVICE = 0;
  public static final int STATE_OUT_OF_SERVICE = 1;
  public static final int STATE_POWER_OFF = 3;
  public static final int UNKNOWN_ID = -1;
  static final boolean VDBG = false;
  String LOG_TAG = "PHONE";
  private int mCdmaDefaultRoamingIndicator;
  private int mCdmaEriIconIndex;
  private int mCdmaEriIconMode;
  private int mCdmaRoamingIndicator;
  private int[] mCellBandwidths;
  private int mChannelNumber;
  private boolean mCssIndicator;
  private String mDataOperatorAlphaLong;
  private String mDataOperatorAlphaShort;
  private String mDataOperatorNumeric;
  private int mDataRegState;
  private int mDataRoamingType;
  private boolean mIsDataRoamingFromRegistration;
  private boolean mIsEmergencyOnly;
  private boolean mIsManualNetworkSelection;
  private boolean mIsUsingCarrierAggregation;
  private int mLteEarfcnRsrpBoost;
  private int mNetworkId;
  private List<NetworkRegistrationState> mNetworkRegistrationStates;
  private int mRilDataRadioTechnology;
  private int mRilVoiceRadioTechnology;
  private int mSystemId;
  private String mVoiceOperatorAlphaLong;
  private String mVoiceOperatorAlphaShort;
  private String mVoiceOperatorNumeric;
  private int mVoiceRegState;
  private int mVoiceRoamingType;
  
  public ServiceState()
  {
    mVoiceRegState = 1;
    mDataRegState = 1;
    mCellBandwidths = new int[0];
    mLteEarfcnRsrpBoost = 0;
    mNetworkRegistrationStates = new ArrayList();
  }
  
  public ServiceState(Parcel paramParcel)
  {
    boolean bool1 = true;
    mVoiceRegState = 1;
    mDataRegState = 1;
    mCellBandwidths = new int[0];
    mLteEarfcnRsrpBoost = 0;
    mNetworkRegistrationStates = new ArrayList();
    mVoiceRegState = paramParcel.readInt();
    mDataRegState = paramParcel.readInt();
    mVoiceRoamingType = paramParcel.readInt();
    mDataRoamingType = paramParcel.readInt();
    mVoiceOperatorAlphaLong = paramParcel.readString();
    mVoiceOperatorAlphaShort = paramParcel.readString();
    mVoiceOperatorNumeric = paramParcel.readString();
    mDataOperatorAlphaLong = paramParcel.readString();
    mDataOperatorAlphaShort = paramParcel.readString();
    mDataOperatorNumeric = paramParcel.readString();
    boolean bool2;
    if (paramParcel.readInt() != 0) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    mIsManualNetworkSelection = bool2;
    mRilVoiceRadioTechnology = paramParcel.readInt();
    mRilDataRadioTechnology = paramParcel.readInt();
    if (paramParcel.readInt() != 0) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    mCssIndicator = bool2;
    mNetworkId = paramParcel.readInt();
    mSystemId = paramParcel.readInt();
    mCdmaRoamingIndicator = paramParcel.readInt();
    mCdmaDefaultRoamingIndicator = paramParcel.readInt();
    mCdmaEriIconIndex = paramParcel.readInt();
    mCdmaEriIconMode = paramParcel.readInt();
    if (paramParcel.readInt() != 0) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    mIsEmergencyOnly = bool2;
    if (paramParcel.readInt() != 0) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    mIsDataRoamingFromRegistration = bool2;
    if (paramParcel.readInt() != 0) {
      bool2 = bool1;
    } else {
      bool2 = false;
    }
    mIsUsingCarrierAggregation = bool2;
    mLteEarfcnRsrpBoost = paramParcel.readInt();
    mNetworkRegistrationStates = new ArrayList();
    paramParcel.readList(mNetworkRegistrationStates, NetworkRegistrationState.class.getClassLoader());
    mChannelNumber = paramParcel.readInt();
    mCellBandwidths = paramParcel.createIntArray();
  }
  
  public ServiceState(ServiceState paramServiceState)
  {
    mVoiceRegState = 1;
    mDataRegState = 1;
    mCellBandwidths = new int[0];
    mLteEarfcnRsrpBoost = 0;
    mNetworkRegistrationStates = new ArrayList();
    copyFrom(paramServiceState);
  }
  
  public static boolean bearerBitmapHasCdma(int paramInt)
  {
    boolean bool;
    if ((0x18F8 & paramInt) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static boolean bitmaskHasTech(int paramInt1, int paramInt2)
  {
    boolean bool = true;
    if (paramInt1 == 0) {
      return true;
    }
    if (paramInt2 >= 1)
    {
      if ((1 << paramInt2 - 1 & paramInt1) == 0) {
        bool = false;
      }
      return bool;
    }
    return false;
  }
  
  public static int convertBearerBitmaskToNetworkTypeBitmask(int paramInt)
  {
    int i = 0;
    if (paramInt == 0) {
      return 0;
    }
    int k;
    for (int j = 0; i < 20; j = k)
    {
      k = j;
      if (bitmaskHasTech(paramInt, i)) {
        k = j | getBitmaskForTech(rilRadioTechnologyToNetworkType(i));
      }
      i++;
    }
    return j;
  }
  
  public static int convertNetworkTypeBitmaskToBearerBitmask(int paramInt)
  {
    int i = 0;
    if (paramInt == 0) {
      return 0;
    }
    int k;
    for (int j = 0; i < 20; j = k)
    {
      k = j;
      if (bitmaskHasTech(paramInt, rilRadioTechnologyToNetworkType(i))) {
        k = j | getBitmaskForTech(i);
      }
      i++;
    }
    return j;
  }
  
  private static boolean equalsHandlesNulls(Object paramObject1, Object paramObject2)
  {
    boolean bool;
    if (paramObject1 == null)
    {
      if (paramObject2 == null) {
        bool = true;
      } else {
        bool = false;
      }
    }
    else {
      bool = paramObject1.equals(paramObject2);
    }
    return bool;
  }
  
  public static int getBitmaskForTech(int paramInt)
  {
    if (paramInt >= 1) {
      return 1 << paramInt - 1;
    }
    return 0;
  }
  
  public static int getBitmaskFromString(String paramString)
  {
    paramString = paramString.split("\\|");
    int i = paramString.length;
    int j = 0;
    int k = 0;
    while (k < i)
    {
      Object localObject = paramString[k];
      try
      {
        int m = Integer.parseInt(localObject.trim());
        if (m == 0) {
          return 0;
        }
        j |= getBitmaskForTech(m);
        k++;
      }
      catch (NumberFormatException paramString)
      {
        return 0;
      }
    }
    return j;
  }
  
  public static final String getRoamingLogString(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return "UNKNOWN";
    case 3: 
      return "International Roaming";
    case 2: 
      return "Domestic Roaming";
    case 1: 
      return "roaming";
    }
    return "home";
  }
  
  public static boolean isCdma(int paramInt)
  {
    boolean bool;
    if ((paramInt != 4) && (paramInt != 5) && (paramInt != 6) && (paramInt != 7) && (paramInt != 8) && (paramInt != 12) && (paramInt != 13)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public static boolean isGsm(int paramInt)
  {
    boolean bool1 = true;
    boolean bool2 = bool1;
    if (paramInt != 1)
    {
      bool2 = bool1;
      if (paramInt != 2)
      {
        bool2 = bool1;
        if (paramInt != 3)
        {
          bool2 = bool1;
          if (paramInt != 9)
          {
            bool2 = bool1;
            if (paramInt != 10)
            {
              bool2 = bool1;
              if (paramInt != 11)
              {
                bool2 = bool1;
                if (paramInt != 14)
                {
                  bool2 = bool1;
                  if (paramInt != 15)
                  {
                    bool2 = bool1;
                    if (paramInt != 16)
                    {
                      bool2 = bool1;
                      if (paramInt != 17)
                      {
                        bool2 = bool1;
                        if (paramInt != 18) {
                          if (paramInt == 19) {
                            bool2 = bool1;
                          } else {
                            bool2 = false;
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
  
  public static boolean isLte(int paramInt)
  {
    boolean bool;
    if ((paramInt != 14) && (paramInt != 19)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public static ServiceState mergeServiceStates(ServiceState paramServiceState1, ServiceState paramServiceState2)
  {
    if (mVoiceRegState != 0) {
      return paramServiceState1;
    }
    paramServiceState1 = new ServiceState(paramServiceState1);
    mVoiceRegState = mVoiceRegState;
    mIsEmergencyOnly = false;
    return paramServiceState1;
  }
  
  public static int networkTypeToRilRadioTechnology(int paramInt)
  {
    switch (paramInt)
    {
    case 11: 
    default: 
      return 0;
    case 19: 
      return 19;
    case 18: 
      return 18;
    case 17: 
      return 17;
    case 16: 
      return 16;
    case 15: 
      return 15;
    case 14: 
      return 13;
    case 13: 
      return 14;
    case 12: 
      return 12;
    case 10: 
      return 11;
    case 9: 
      return 10;
    case 8: 
      return 9;
    case 7: 
      return 6;
    case 6: 
      return 8;
    case 5: 
      return 7;
    case 4: 
      return 4;
    case 3: 
      return 3;
    case 2: 
      return 2;
    }
    return 1;
  }
  
  public static ServiceState newFromBundle(Bundle paramBundle)
  {
    ServiceState localServiceState = new ServiceState();
    localServiceState.setFromNotifierBundle(paramBundle);
    return localServiceState;
  }
  
  public static int rilRadioTechnologyToAccessNetworkType(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return 0;
    case 18: 
      return 5;
    case 14: 
    case 19: 
      return 3;
    case 4: 
    case 5: 
    case 6: 
    case 7: 
    case 8: 
    case 12: 
    case 13: 
      return 4;
    case 3: 
    case 9: 
    case 10: 
    case 11: 
    case 15: 
    case 17: 
      return 2;
    }
    return 1;
  }
  
  public static int rilRadioTechnologyToNetworkType(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return 0;
    case 19: 
      return 19;
    case 18: 
      return 18;
    case 17: 
      return 17;
    case 16: 
      return 16;
    case 15: 
      return 15;
    case 14: 
      return 13;
    case 13: 
      return 14;
    case 12: 
      return 12;
    case 11: 
      return 10;
    case 10: 
      return 9;
    case 9: 
      return 8;
    case 8: 
      return 6;
    case 7: 
      return 5;
    case 6: 
      return 7;
    case 4: 
    case 5: 
      return 4;
    case 3: 
      return 3;
    case 2: 
      return 2;
    }
    return 1;
  }
  
  public static String rilRadioTechnologyToString(int paramInt)
  {
    String str;
    switch (paramInt)
    {
    default: 
      str = "Unexpected";
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Unexpected radioTechnology=");
      localStringBuilder.append(paramInt);
      Rlog.w("PHONE", localStringBuilder.toString());
      break;
    case 19: 
      str = "LTE_CA";
      break;
    case 18: 
      str = "IWLAN";
      break;
    case 17: 
      str = "TD-SCDMA";
      break;
    case 16: 
      str = "GSM";
      break;
    case 15: 
      str = "HSPAP";
      break;
    case 14: 
      str = "LTE";
      break;
    case 13: 
      str = "eHRPD";
      break;
    case 12: 
      str = "EvDo-rev.B";
      break;
    case 11: 
      str = "HSPA";
      break;
    case 10: 
      str = "HSUPA";
      break;
    case 9: 
      str = "HSDPA";
      break;
    case 8: 
      str = "EvDo-rev.A";
      break;
    case 7: 
      str = "EvDo-rev.0";
      break;
    case 6: 
      str = "1xRTT";
      break;
    case 5: 
      str = "CDMA-IS95B";
      break;
    case 4: 
      str = "CDMA-IS95A";
      break;
    case 3: 
      str = "UMTS";
      break;
    case 2: 
      str = "EDGE";
      break;
    case 1: 
      str = "GPRS";
      break;
    case 0: 
      str = "Unknown";
    }
    return str;
  }
  
  public static String rilServiceStateToString(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return "UNKNOWN";
    case 3: 
      return "POWER_OFF";
    case 2: 
      return "EMERGENCY_ONLY";
    case 1: 
      return "OUT_OF_SERVICE";
    }
    return "IN_SERVICE";
  }
  
  private void setFromNotifierBundle(Bundle paramBundle)
  {
    mVoiceRegState = paramBundle.getInt("voiceRegState");
    mDataRegState = paramBundle.getInt("dataRegState");
    mVoiceRoamingType = paramBundle.getInt("voiceRoamingType");
    mDataRoamingType = paramBundle.getInt("dataRoamingType");
    mVoiceOperatorAlphaLong = paramBundle.getString("operator-alpha-long");
    mVoiceOperatorAlphaShort = paramBundle.getString("operator-alpha-short");
    mVoiceOperatorNumeric = paramBundle.getString("operator-numeric");
    mDataOperatorAlphaLong = paramBundle.getString("data-operator-alpha-long");
    mDataOperatorAlphaShort = paramBundle.getString("data-operator-alpha-short");
    mDataOperatorNumeric = paramBundle.getString("data-operator-numeric");
    mIsManualNetworkSelection = paramBundle.getBoolean("manual");
    mRilVoiceRadioTechnology = paramBundle.getInt("radioTechnology");
    mRilDataRadioTechnology = paramBundle.getInt("dataRadioTechnology");
    mCssIndicator = paramBundle.getBoolean("cssIndicator");
    mNetworkId = paramBundle.getInt("networkId");
    mSystemId = paramBundle.getInt("systemId");
    mCdmaRoamingIndicator = paramBundle.getInt("cdmaRoamingIndicator");
    mCdmaDefaultRoamingIndicator = paramBundle.getInt("cdmaDefaultRoamingIndicator");
    mIsEmergencyOnly = paramBundle.getBoolean("emergencyOnly");
    mIsDataRoamingFromRegistration = paramBundle.getBoolean("isDataRoamingFromRegistration");
    mIsUsingCarrierAggregation = paramBundle.getBoolean("isUsingCarrierAggregation");
    mLteEarfcnRsrpBoost = paramBundle.getInt("LteEarfcnRsrpBoost");
    mChannelNumber = paramBundle.getInt("ChannelNumber");
    mCellBandwidths = paramBundle.getIntArray("CellBandwidths");
  }
  
  private void setNullState(int paramInt)
  {
    mVoiceRegState = paramInt;
    mDataRegState = paramInt;
    mVoiceRoamingType = 0;
    mDataRoamingType = 0;
    mChannelNumber = -1;
    mCellBandwidths = new int[0];
    mVoiceOperatorAlphaLong = null;
    mVoiceOperatorAlphaShort = null;
    mVoiceOperatorNumeric = null;
    mDataOperatorAlphaLong = null;
    mDataOperatorAlphaShort = null;
    mDataOperatorNumeric = null;
    mIsManualNetworkSelection = false;
    mRilVoiceRadioTechnology = 0;
    mRilDataRadioTechnology = 0;
    mCssIndicator = false;
    mNetworkId = -1;
    mSystemId = -1;
    mCdmaRoamingIndicator = -1;
    mCdmaDefaultRoamingIndicator = -1;
    mCdmaEriIconIndex = -1;
    mCdmaEriIconMode = -1;
    mIsEmergencyOnly = false;
    mIsDataRoamingFromRegistration = false;
    mIsUsingCarrierAggregation = false;
    mLteEarfcnRsrpBoost = 0;
    mNetworkRegistrationStates = new ArrayList();
  }
  
  public void addNetworkRegistrationState(NetworkRegistrationState paramNetworkRegistrationState)
  {
    if (paramNetworkRegistrationState == null) {
      return;
    }
    List localList = mNetworkRegistrationStates;
    int i = 0;
    try
    {
      while (i < mNetworkRegistrationStates.size())
      {
        NetworkRegistrationState localNetworkRegistrationState = (NetworkRegistrationState)mNetworkRegistrationStates.get(i);
        if ((localNetworkRegistrationState.getTransportType() == paramNetworkRegistrationState.getTransportType()) && (localNetworkRegistrationState.getDomain() == paramNetworkRegistrationState.getDomain()))
        {
          mNetworkRegistrationStates.remove(i);
          break;
        }
        i++;
      }
      mNetworkRegistrationStates.add(paramNetworkRegistrationState);
      return;
    }
    finally {}
  }
  
  protected void copyFrom(ServiceState paramServiceState)
  {
    mVoiceRegState = mVoiceRegState;
    mDataRegState = mDataRegState;
    mVoiceRoamingType = mVoiceRoamingType;
    mDataRoamingType = mDataRoamingType;
    mVoiceOperatorAlphaLong = mVoiceOperatorAlphaLong;
    mVoiceOperatorAlphaShort = mVoiceOperatorAlphaShort;
    mVoiceOperatorNumeric = mVoiceOperatorNumeric;
    mDataOperatorAlphaLong = mDataOperatorAlphaLong;
    mDataOperatorAlphaShort = mDataOperatorAlphaShort;
    mDataOperatorNumeric = mDataOperatorNumeric;
    mIsManualNetworkSelection = mIsManualNetworkSelection;
    mRilVoiceRadioTechnology = mRilVoiceRadioTechnology;
    mRilDataRadioTechnology = mRilDataRadioTechnology;
    mCssIndicator = mCssIndicator;
    mNetworkId = mNetworkId;
    mSystemId = mSystemId;
    mCdmaRoamingIndicator = mCdmaRoamingIndicator;
    mCdmaDefaultRoamingIndicator = mCdmaDefaultRoamingIndicator;
    mCdmaEriIconIndex = mCdmaEriIconIndex;
    mCdmaEriIconMode = mCdmaEriIconMode;
    mIsEmergencyOnly = mIsEmergencyOnly;
    mIsDataRoamingFromRegistration = mIsDataRoamingFromRegistration;
    mIsUsingCarrierAggregation = mIsUsingCarrierAggregation;
    mChannelNumber = mChannelNumber;
    mCellBandwidths = Arrays.copyOf(mCellBandwidths, mCellBandwidths.length);
    mLteEarfcnRsrpBoost = mLteEarfcnRsrpBoost;
    mNetworkRegistrationStates = new ArrayList(mNetworkRegistrationStates);
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
      ServiceState localServiceState = (ServiceState)paramObject;
      if (paramObject == null) {
        return false;
      }
      boolean bool2 = bool1;
      if (mVoiceRegState == mVoiceRegState)
      {
        bool2 = bool1;
        if (mDataRegState == mDataRegState)
        {
          bool2 = bool1;
          if (mIsManualNetworkSelection == mIsManualNetworkSelection)
          {
            bool2 = bool1;
            if (mVoiceRoamingType == mVoiceRoamingType)
            {
              bool2 = bool1;
              if (mDataRoamingType == mDataRoamingType)
              {
                bool2 = bool1;
                if (mChannelNumber == mChannelNumber)
                {
                  bool2 = bool1;
                  if (Arrays.equals(mCellBandwidths, mCellBandwidths))
                  {
                    bool2 = bool1;
                    if (equalsHandlesNulls(mVoiceOperatorAlphaLong, mVoiceOperatorAlphaLong))
                    {
                      bool2 = bool1;
                      if (equalsHandlesNulls(mVoiceOperatorAlphaShort, mVoiceOperatorAlphaShort))
                      {
                        bool2 = bool1;
                        if (equalsHandlesNulls(mVoiceOperatorNumeric, mVoiceOperatorNumeric))
                        {
                          bool2 = bool1;
                          if (equalsHandlesNulls(mDataOperatorAlphaLong, mDataOperatorAlphaLong))
                          {
                            bool2 = bool1;
                            if (equalsHandlesNulls(mDataOperatorAlphaShort, mDataOperatorAlphaShort))
                            {
                              bool2 = bool1;
                              if (equalsHandlesNulls(mDataOperatorNumeric, mDataOperatorNumeric))
                              {
                                bool2 = bool1;
                                if (equalsHandlesNulls(Integer.valueOf(mRilVoiceRadioTechnology), Integer.valueOf(mRilVoiceRadioTechnology)))
                                {
                                  bool2 = bool1;
                                  if (equalsHandlesNulls(Integer.valueOf(mRilDataRadioTechnology), Integer.valueOf(mRilDataRadioTechnology)))
                                  {
                                    bool2 = bool1;
                                    if (equalsHandlesNulls(Boolean.valueOf(mCssIndicator), Boolean.valueOf(mCssIndicator)))
                                    {
                                      bool2 = bool1;
                                      if (equalsHandlesNulls(Integer.valueOf(mNetworkId), Integer.valueOf(mNetworkId)))
                                      {
                                        bool2 = bool1;
                                        if (equalsHandlesNulls(Integer.valueOf(mSystemId), Integer.valueOf(mSystemId)))
                                        {
                                          bool2 = bool1;
                                          if (equalsHandlesNulls(Integer.valueOf(mCdmaRoamingIndicator), Integer.valueOf(mCdmaRoamingIndicator)))
                                          {
                                            bool2 = bool1;
                                            if (equalsHandlesNulls(Integer.valueOf(mCdmaDefaultRoamingIndicator), Integer.valueOf(mCdmaDefaultRoamingIndicator)))
                                            {
                                              bool2 = bool1;
                                              if (mIsEmergencyOnly == mIsEmergencyOnly)
                                              {
                                                bool2 = bool1;
                                                if (mIsDataRoamingFromRegistration == mIsDataRoamingFromRegistration)
                                                {
                                                  bool2 = bool1;
                                                  if (mIsUsingCarrierAggregation == mIsUsingCarrierAggregation)
                                                  {
                                                    bool2 = bool1;
                                                    if (mNetworkRegistrationStates.containsAll(mNetworkRegistrationStates)) {
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
      }
      return bool2;
    }
    catch (ClassCastException paramObject) {}
    return false;
  }
  
  public void fillInNotifierBundle(Bundle paramBundle)
  {
    paramBundle.putInt("voiceRegState", mVoiceRegState);
    paramBundle.putInt("dataRegState", mDataRegState);
    paramBundle.putInt("voiceRoamingType", mVoiceRoamingType);
    paramBundle.putInt("dataRoamingType", mDataRoamingType);
    paramBundle.putString("operator-alpha-long", mVoiceOperatorAlphaLong);
    paramBundle.putString("operator-alpha-short", mVoiceOperatorAlphaShort);
    paramBundle.putString("operator-numeric", mVoiceOperatorNumeric);
    paramBundle.putString("data-operator-alpha-long", mDataOperatorAlphaLong);
    paramBundle.putString("data-operator-alpha-short", mDataOperatorAlphaShort);
    paramBundle.putString("data-operator-numeric", mDataOperatorNumeric);
    paramBundle.putBoolean("manual", mIsManualNetworkSelection);
    paramBundle.putInt("radioTechnology", mRilVoiceRadioTechnology);
    paramBundle.putInt("dataRadioTechnology", mRilDataRadioTechnology);
    paramBundle.putBoolean("cssIndicator", mCssIndicator);
    paramBundle.putInt("networkId", mNetworkId);
    paramBundle.putInt("systemId", mSystemId);
    paramBundle.putInt("cdmaRoamingIndicator", mCdmaRoamingIndicator);
    paramBundle.putInt("cdmaDefaultRoamingIndicator", mCdmaDefaultRoamingIndicator);
    paramBundle.putBoolean("emergencyOnly", mIsEmergencyOnly);
    paramBundle.putBoolean("isDataRoamingFromRegistration", mIsDataRoamingFromRegistration);
    paramBundle.putBoolean("isUsingCarrierAggregation", mIsUsingCarrierAggregation);
    paramBundle.putInt("LteEarfcnRsrpBoost", mLteEarfcnRsrpBoost);
    paramBundle.putInt("ChannelNumber", mChannelNumber);
    paramBundle.putIntArray("CellBandwidths", mCellBandwidths);
  }
  
  public int getCdmaDefaultRoamingIndicator()
  {
    return mCdmaDefaultRoamingIndicator;
  }
  
  public int getCdmaEriIconIndex()
  {
    return mCdmaEriIconIndex;
  }
  
  public int getCdmaEriIconMode()
  {
    return mCdmaEriIconMode;
  }
  
  public int getCdmaNetworkId()
  {
    return mNetworkId;
  }
  
  public int getCdmaRoamingIndicator()
  {
    return mCdmaRoamingIndicator;
  }
  
  public int getCdmaSystemId()
  {
    return mSystemId;
  }
  
  public int[] getCellBandwidths()
  {
    int[] arrayOfInt;
    if (mCellBandwidths == null) {
      arrayOfInt = new int[0];
    } else {
      arrayOfInt = mCellBandwidths;
    }
    return arrayOfInt;
  }
  
  public int getChannelNumber()
  {
    return mChannelNumber;
  }
  
  public int getCssIndicator()
  {
    return mCssIndicator;
  }
  
  public int getDataNetworkType()
  {
    return rilRadioTechnologyToNetworkType(mRilDataRadioTechnology);
  }
  
  public String getDataOperatorAlphaLong()
  {
    return mDataOperatorAlphaLong;
  }
  
  public String getDataOperatorAlphaShort()
  {
    return mDataOperatorAlphaShort;
  }
  
  public String getDataOperatorNumeric()
  {
    return mDataOperatorNumeric;
  }
  
  public int getDataRegState()
  {
    return mDataRegState;
  }
  
  public boolean getDataRoaming()
  {
    boolean bool;
    if (mDataRoamingType != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean getDataRoamingFromRegistration()
  {
    return mIsDataRoamingFromRegistration;
  }
  
  public int getDataRoamingType()
  {
    return mDataRoamingType;
  }
  
  public int getDuplexMode()
  {
    if (!isLte(mRilDataRadioTechnology)) {
      return 0;
    }
    return AccessNetworkUtils.getDuplexModeForEutranBand(AccessNetworkUtils.getOperatingBandForEarfcn(mChannelNumber));
  }
  
  public boolean getIsManualSelection()
  {
    return mIsManualNetworkSelection;
  }
  
  public int getLteEarfcnRsrpBoost()
  {
    return mLteEarfcnRsrpBoost;
  }
  
  public NetworkRegistrationState getNetworkRegistrationStates(int paramInt1, int paramInt2)
  {
    synchronized (mNetworkRegistrationStates)
    {
      Iterator localIterator = mNetworkRegistrationStates.iterator();
      while (localIterator.hasNext())
      {
        NetworkRegistrationState localNetworkRegistrationState = (NetworkRegistrationState)localIterator.next();
        if ((localNetworkRegistrationState.getTransportType() == paramInt1) && (localNetworkRegistrationState.getDomain() == paramInt2)) {
          return localNetworkRegistrationState;
        }
      }
      return null;
    }
  }
  
  public List<NetworkRegistrationState> getNetworkRegistrationStates()
  {
    synchronized (mNetworkRegistrationStates)
    {
      ArrayList localArrayList = new java/util/ArrayList;
      localArrayList.<init>(mNetworkRegistrationStates);
      return localArrayList;
    }
  }
  
  public List<NetworkRegistrationState> getNetworkRegistrationStates(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    synchronized (mNetworkRegistrationStates)
    {
      Iterator localIterator = mNetworkRegistrationStates.iterator();
      while (localIterator.hasNext())
      {
        NetworkRegistrationState localNetworkRegistrationState = (NetworkRegistrationState)localIterator.next();
        if (localNetworkRegistrationState.getTransportType() == paramInt) {
          localArrayList.add(localNetworkRegistrationState);
        }
      }
      return localArrayList;
    }
  }
  
  public String getOperatorAlpha()
  {
    if (TextUtils.isEmpty(mVoiceOperatorAlphaLong)) {
      return mVoiceOperatorAlphaShort;
    }
    return mVoiceOperatorAlphaLong;
  }
  
  public String getOperatorAlphaLong()
  {
    return mVoiceOperatorAlphaLong;
  }
  
  public String getOperatorAlphaShort()
  {
    return mVoiceOperatorAlphaShort;
  }
  
  public String getOperatorNumeric()
  {
    return mVoiceOperatorNumeric;
  }
  
  public int getRadioTechnology()
  {
    Rlog.e(LOG_TAG, "ServiceState.getRadioTechnology() DEPRECATED will be removed *******");
    return getRilDataRadioTechnology();
  }
  
  public int getRilDataRadioTechnology()
  {
    return mRilDataRadioTechnology;
  }
  
  public int getRilVoiceRadioTechnology()
  {
    return mRilVoiceRadioTechnology;
  }
  
  public boolean getRoaming()
  {
    boolean bool;
    if ((!getVoiceRoaming()) && (!getDataRoaming())) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public int getState()
  {
    return getVoiceRegState();
  }
  
  public int getVoiceNetworkType()
  {
    return rilRadioTechnologyToNetworkType(mRilVoiceRadioTechnology);
  }
  
  public String getVoiceOperatorAlphaLong()
  {
    return mVoiceOperatorAlphaLong;
  }
  
  public String getVoiceOperatorAlphaShort()
  {
    return mVoiceOperatorAlphaShort;
  }
  
  public String getVoiceOperatorNumeric()
  {
    return mVoiceOperatorNumeric;
  }
  
  public int getVoiceRegState()
  {
    return mVoiceRegState;
  }
  
  public boolean getVoiceRoaming()
  {
    boolean bool;
    if (mVoiceRoamingType != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public int getVoiceRoamingType()
  {
    return mVoiceRoamingType;
  }
  
  public int hashCode()
  {
    int i = mVoiceRegState;
    int j = mDataRegState;
    int k = mVoiceRoamingType;
    int m = mDataRoamingType;
    int n = mChannelNumber;
    int i1 = Arrays.hashCode(mCellBandwidths);
    int i2 = mIsManualNetworkSelection;
    String str = mVoiceOperatorAlphaLong;
    int i3 = 0;
    int i4;
    if (str == null) {
      i4 = 0;
    } else {
      i4 = mVoiceOperatorAlphaLong.hashCode();
    }
    int i5;
    if (mVoiceOperatorAlphaShort == null) {
      i5 = 0;
    } else {
      i5 = mVoiceOperatorAlphaShort.hashCode();
    }
    int i6;
    if (mVoiceOperatorNumeric == null) {
      i6 = 0;
    } else {
      i6 = mVoiceOperatorNumeric.hashCode();
    }
    int i7;
    if (mDataOperatorAlphaLong == null) {
      i7 = 0;
    } else {
      i7 = mDataOperatorAlphaLong.hashCode();
    }
    int i8;
    if (mDataOperatorAlphaShort == null) {
      i8 = 0;
    } else {
      i8 = mDataOperatorAlphaShort.hashCode();
    }
    if (mDataOperatorNumeric != null) {
      i3 = mDataOperatorNumeric.hashCode();
    }
    return i * 31 + j * 37 + k + m + n + i1 + i2 + i4 + i5 + i6 + i7 + i8 + i3 + mCdmaRoamingIndicator + mCdmaDefaultRoamingIndicator + mIsEmergencyOnly + mIsDataRoamingFromRegistration;
  }
  
  public boolean isEmergencyOnly()
  {
    return mIsEmergencyOnly;
  }
  
  public boolean isUsingCarrierAggregation()
  {
    return mIsUsingCarrierAggregation;
  }
  
  public void setCdmaDefaultRoamingIndicator(int paramInt)
  {
    mCdmaDefaultRoamingIndicator = paramInt;
  }
  
  public void setCdmaEriIconIndex(int paramInt)
  {
    mCdmaEriIconIndex = paramInt;
  }
  
  public void setCdmaEriIconMode(int paramInt)
  {
    mCdmaEriIconMode = paramInt;
  }
  
  public void setCdmaRoamingIndicator(int paramInt)
  {
    mCdmaRoamingIndicator = paramInt;
  }
  
  public void setCdmaSystemAndNetworkId(int paramInt1, int paramInt2)
  {
    mSystemId = paramInt1;
    mNetworkId = paramInt2;
  }
  
  public void setCellBandwidths(int[] paramArrayOfInt)
  {
    mCellBandwidths = paramArrayOfInt;
  }
  
  public void setChannelNumber(int paramInt)
  {
    mChannelNumber = paramInt;
  }
  
  public void setCssIndicator(int paramInt)
  {
    boolean bool;
    if (paramInt != 0) {
      bool = true;
    } else {
      bool = false;
    }
    mCssIndicator = bool;
  }
  
  public void setDataOperatorAlphaLong(String paramString)
  {
    mDataOperatorAlphaLong = paramString;
  }
  
  public void setDataOperatorName(String paramString1, String paramString2, String paramString3)
  {
    mDataOperatorAlphaLong = paramString1;
    mDataOperatorAlphaShort = paramString2;
    mDataOperatorNumeric = paramString3;
  }
  
  public void setDataRegState(int paramInt)
  {
    mDataRegState = paramInt;
  }
  
  public void setDataRoaming(boolean paramBoolean)
  {
    mDataRoamingType = paramBoolean;
  }
  
  public void setDataRoamingFromRegistration(boolean paramBoolean)
  {
    mIsDataRoamingFromRegistration = paramBoolean;
  }
  
  public void setDataRoamingType(int paramInt)
  {
    mDataRoamingType = paramInt;
  }
  
  public void setEmergencyOnly(boolean paramBoolean)
  {
    mIsEmergencyOnly = paramBoolean;
  }
  
  public void setIsManualSelection(boolean paramBoolean)
  {
    mIsManualNetworkSelection = paramBoolean;
  }
  
  public void setIsUsingCarrierAggregation(boolean paramBoolean)
  {
    mIsUsingCarrierAggregation = paramBoolean;
  }
  
  public void setLteEarfcnRsrpBoost(int paramInt)
  {
    mLteEarfcnRsrpBoost = paramInt;
  }
  
  public void setOperatorAlphaLong(String paramString)
  {
    mVoiceOperatorAlphaLong = paramString;
    mDataOperatorAlphaLong = paramString;
  }
  
  public void setOperatorName(String paramString1, String paramString2, String paramString3)
  {
    mVoiceOperatorAlphaLong = paramString1;
    mVoiceOperatorAlphaShort = paramString2;
    mVoiceOperatorNumeric = paramString3;
    mDataOperatorAlphaLong = paramString1;
    mDataOperatorAlphaShort = paramString2;
    mDataOperatorNumeric = paramString3;
  }
  
  public void setPhoneId(int paramInt)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(LOG_TAG);
    localStringBuilder.append("-");
    localStringBuilder.append(paramInt);
    LOG_TAG = localStringBuilder.toString();
  }
  
  public void setRilDataRadioTechnology(int paramInt)
  {
    if (paramInt == 19)
    {
      paramInt = 14;
      mIsUsingCarrierAggregation = true;
    }
    else
    {
      mIsUsingCarrierAggregation = false;
    }
    mRilDataRadioTechnology = paramInt;
  }
  
  public void setRilVoiceRadioTechnology(int paramInt)
  {
    int i = paramInt;
    if (paramInt == 19) {
      i = 14;
    }
    mRilVoiceRadioTechnology = i;
  }
  
  public void setRoaming(boolean paramBoolean)
  {
    mVoiceRoamingType = paramBoolean;
    mDataRoamingType = mVoiceRoamingType;
  }
  
  public void setState(int paramInt)
  {
    setVoiceRegState(paramInt);
  }
  
  public void setStateOff()
  {
    setNullState(3);
  }
  
  public void setStateOutOfService()
  {
    setNullState(1);
  }
  
  public void setVoiceOperatorAlphaLong(String paramString)
  {
    mVoiceOperatorAlphaLong = paramString;
  }
  
  public void setVoiceOperatorName(String paramString1, String paramString2, String paramString3)
  {
    mVoiceOperatorAlphaLong = paramString1;
    mVoiceOperatorAlphaShort = paramString2;
    mVoiceOperatorNumeric = paramString3;
  }
  
  public void setVoiceRegState(int paramInt)
  {
    mVoiceRegState = paramInt;
  }
  
  public void setVoiceRoaming(boolean paramBoolean)
  {
    mVoiceRoamingType = paramBoolean;
  }
  
  public void setVoiceRoamingType(int paramInt)
  {
    mVoiceRoamingType = paramInt;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{mVoiceRegState=");
    localStringBuilder.append(mVoiceRegState);
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("(");
    ((StringBuilder)localObject).append(rilServiceStateToString(mVoiceRegState));
    ((StringBuilder)localObject).append(")");
    localStringBuilder.append(((StringBuilder)localObject).toString());
    localStringBuilder.append(", mDataRegState=");
    localStringBuilder.append(mDataRegState);
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("(");
    ((StringBuilder)localObject).append(rilServiceStateToString(mDataRegState));
    ((StringBuilder)localObject).append(")");
    localStringBuilder.append(((StringBuilder)localObject).toString());
    localStringBuilder.append(", mChannelNumber=");
    localStringBuilder.append(mChannelNumber);
    localStringBuilder.append(", duplexMode()=");
    localStringBuilder.append(getDuplexMode());
    localStringBuilder.append(", mCellBandwidths=");
    localStringBuilder.append(Arrays.toString(mCellBandwidths));
    localStringBuilder.append(", mVoiceRoamingType=");
    localStringBuilder.append(getRoamingLogString(mVoiceRoamingType));
    localStringBuilder.append(", mDataRoamingType=");
    localStringBuilder.append(getRoamingLogString(mDataRoamingType));
    localStringBuilder.append(", mVoiceOperatorAlphaLong=");
    localStringBuilder.append(mVoiceOperatorAlphaLong);
    localStringBuilder.append(", mVoiceOperatorAlphaShort=");
    localStringBuilder.append(mVoiceOperatorAlphaShort);
    localStringBuilder.append(", mDataOperatorAlphaLong=");
    localStringBuilder.append(mDataOperatorAlphaLong);
    localStringBuilder.append(", mDataOperatorAlphaShort=");
    localStringBuilder.append(mDataOperatorAlphaShort);
    localStringBuilder.append(", isManualNetworkSelection=");
    localStringBuilder.append(mIsManualNetworkSelection);
    if (mIsManualNetworkSelection) {
      localObject = "(manual)";
    } else {
      localObject = "(automatic)";
    }
    localStringBuilder.append((String)localObject);
    localStringBuilder.append(", mRilVoiceRadioTechnology=");
    localStringBuilder.append(mRilVoiceRadioTechnology);
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("(");
    ((StringBuilder)localObject).append(rilRadioTechnologyToString(mRilVoiceRadioTechnology));
    ((StringBuilder)localObject).append(")");
    localStringBuilder.append(((StringBuilder)localObject).toString());
    localStringBuilder.append(", mRilDataRadioTechnology=");
    localStringBuilder.append(mRilDataRadioTechnology);
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("(");
    ((StringBuilder)localObject).append(rilRadioTechnologyToString(mRilDataRadioTechnology));
    ((StringBuilder)localObject).append(")");
    localStringBuilder.append(((StringBuilder)localObject).toString());
    localStringBuilder.append(", mCssIndicator=");
    if (mCssIndicator) {
      localObject = "supported";
    } else {
      localObject = "unsupported";
    }
    localStringBuilder.append((String)localObject);
    localStringBuilder.append(", mNetworkId=");
    localStringBuilder.append(mNetworkId);
    localStringBuilder.append(", mSystemId=");
    localStringBuilder.append(mSystemId);
    localStringBuilder.append(", mCdmaRoamingIndicator=");
    localStringBuilder.append(mCdmaRoamingIndicator);
    localStringBuilder.append(", mCdmaDefaultRoamingIndicator=");
    localStringBuilder.append(mCdmaDefaultRoamingIndicator);
    localStringBuilder.append(", mIsEmergencyOnly=");
    localStringBuilder.append(mIsEmergencyOnly);
    localStringBuilder.append(", mIsDataRoamingFromRegistration=");
    localStringBuilder.append(mIsDataRoamingFromRegistration);
    localStringBuilder.append(", mIsUsingCarrierAggregation=");
    localStringBuilder.append(mIsUsingCarrierAggregation);
    localStringBuilder.append(", mLteEarfcnRsrpBoost=");
    localStringBuilder.append(mLteEarfcnRsrpBoost);
    localStringBuilder.append(", mNetworkRegistrationStates=");
    localStringBuilder.append(mNetworkRegistrationStates);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mVoiceRegState);
    paramParcel.writeInt(mDataRegState);
    paramParcel.writeInt(mVoiceRoamingType);
    paramParcel.writeInt(mDataRoamingType);
    paramParcel.writeString(mVoiceOperatorAlphaLong);
    paramParcel.writeString(mVoiceOperatorAlphaShort);
    paramParcel.writeString(mVoiceOperatorNumeric);
    paramParcel.writeString(mDataOperatorAlphaLong);
    paramParcel.writeString(mDataOperatorAlphaShort);
    paramParcel.writeString(mDataOperatorNumeric);
    paramParcel.writeInt(mIsManualNetworkSelection);
    paramParcel.writeInt(mRilVoiceRadioTechnology);
    paramParcel.writeInt(mRilDataRadioTechnology);
    paramParcel.writeInt(mCssIndicator);
    paramParcel.writeInt(mNetworkId);
    paramParcel.writeInt(mSystemId);
    paramParcel.writeInt(mCdmaRoamingIndicator);
    paramParcel.writeInt(mCdmaDefaultRoamingIndicator);
    paramParcel.writeInt(mCdmaEriIconIndex);
    paramParcel.writeInt(mCdmaEriIconMode);
    paramParcel.writeInt(mIsEmergencyOnly);
    paramParcel.writeInt(mIsDataRoamingFromRegistration);
    paramParcel.writeInt(mIsUsingCarrierAggregation);
    paramParcel.writeInt(mLteEarfcnRsrpBoost);
    paramParcel.writeList(mNetworkRegistrationStates);
    paramParcel.writeInt(mChannelNumber);
    paramParcel.writeIntArray(mCellBandwidths);
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface DuplexMode {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface RilRadioTechnology {}
}
