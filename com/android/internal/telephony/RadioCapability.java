package com.android.internal.telephony;

public class RadioCapability
{
  private static final int RADIO_CAPABILITY_VERSION = 1;
  public static final int RC_PHASE_APPLY = 2;
  public static final int RC_PHASE_CONFIGURED = 0;
  public static final int RC_PHASE_FINISH = 4;
  public static final int RC_PHASE_START = 1;
  public static final int RC_PHASE_UNSOL_RSP = 3;
  public static final int RC_STATUS_FAIL = 2;
  public static final int RC_STATUS_NONE = 0;
  public static final int RC_STATUS_SUCCESS = 1;
  private String mLogicalModemUuid;
  private int mPhase;
  private int mPhoneId;
  private int mRadioAccessFamily;
  private int mSession;
  private int mStatus;
  
  public RadioCapability(int paramInt1, int paramInt2, int paramInt3, int paramInt4, String paramString, int paramInt5)
  {
    mPhoneId = paramInt1;
    mSession = paramInt2;
    mPhase = paramInt3;
    mRadioAccessFamily = paramInt4;
    mLogicalModemUuid = paramString;
    mStatus = paramInt5;
  }
  
  public String getLogicalModemUuid()
  {
    return mLogicalModemUuid;
  }
  
  public int getPhase()
  {
    return mPhase;
  }
  
  public int getPhoneId()
  {
    return mPhoneId;
  }
  
  public int getRadioAccessFamily()
  {
    return mRadioAccessFamily;
  }
  
  public int getSession()
  {
    return mSession;
  }
  
  public int getStatus()
  {
    return mStatus;
  }
  
  public int getVersion()
  {
    return 1;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{mPhoneId = ");
    localStringBuilder.append(mPhoneId);
    localStringBuilder.append(" mVersion=");
    localStringBuilder.append(getVersion());
    localStringBuilder.append(" mSession=");
    localStringBuilder.append(getSession());
    localStringBuilder.append(" mPhase=");
    localStringBuilder.append(getPhase());
    localStringBuilder.append(" mRadioAccessFamily=");
    localStringBuilder.append(getRadioAccessFamily());
    localStringBuilder.append(" mLogicModemId=");
    localStringBuilder.append(getLogicalModemUuid());
    localStringBuilder.append(" mStatus=");
    localStringBuilder.append(getStatus());
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
}
