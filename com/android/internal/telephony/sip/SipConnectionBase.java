package com.android.internal.telephony.sip;

import android.os.SystemClock;
import android.telephony.PhoneNumberUtils;
import android.telephony.Rlog;
import com.android.internal.telephony.Call.State;
import com.android.internal.telephony.Connection;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.UUSInfo;

abstract class SipConnectionBase
  extends Connection
{
  private static final boolean DBG = true;
  private static final String LOG_TAG = "SipConnBase";
  private static final boolean VDBG = false;
  private long mConnectTime;
  private long mConnectTimeReal;
  private long mCreateTime;
  private long mDisconnectTime;
  private long mDuration = -1L;
  private long mHoldingStartTime;
  
  SipConnectionBase(String paramString)
  {
    super(3);
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("SipConnectionBase: ctor dialString=");
    localStringBuilder.append(SipPhone.hidePii(paramString));
    log(localStringBuilder.toString());
    mPostDialString = PhoneNumberUtils.extractPostDialPortion(paramString);
    mCreateTime = System.currentTimeMillis();
  }
  
  private void log(String paramString)
  {
    Rlog.d("SipConnBase", paramString);
  }
  
  public void cancelPostDial()
  {
    log("cancelPostDial: ignore");
  }
  
  public long getConnectTime()
  {
    return mConnectTime;
  }
  
  public long getConnectTimeReal()
  {
    return mConnectTimeReal;
  }
  
  public long getCreateTime()
  {
    return mCreateTime;
  }
  
  public long getDisconnectTime()
  {
    return mDisconnectTime;
  }
  
  public long getDurationMillis()
  {
    long l;
    if (mConnectTimeReal == 0L) {
      l = 0L;
    }
    for (;;)
    {
      break;
      if (mDuration < 0L) {
        l = SystemClock.elapsedRealtime() - mConnectTimeReal;
      } else {
        l = mDuration;
      }
    }
    return l;
  }
  
  public long getHoldDurationMillis()
  {
    long l;
    if (getState() != Call.State.HOLDING) {
      l = 0L;
    } else {
      l = SystemClock.elapsedRealtime() - mHoldingStartTime;
    }
    return l;
  }
  
  public long getHoldingStartTime()
  {
    return mHoldingStartTime;
  }
  
  public int getNumberPresentation()
  {
    return 1;
  }
  
  public Connection getOrigConnection()
  {
    return null;
  }
  
  protected abstract Phone getPhone();
  
  public int getPreciseDisconnectCause()
  {
    return 0;
  }
  
  public UUSInfo getUUSInfo()
  {
    return null;
  }
  
  public String getVendorDisconnectCause()
  {
    return null;
  }
  
  public boolean isMultiparty()
  {
    return false;
  }
  
  public void proceedAfterWaitChar()
  {
    log("proceedAfterWaitChar: ignore");
  }
  
  public void proceedAfterWildChar(String paramString)
  {
    log("proceedAfterWildChar: ignore");
  }
  
  void setDisconnectCause(int paramInt)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("setDisconnectCause: prev=");
    localStringBuilder.append(mCause);
    localStringBuilder.append(" new=");
    localStringBuilder.append(paramInt);
    log(localStringBuilder.toString());
    mCause = paramInt;
  }
  
  protected void setState(Call.State paramState)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("setState: state=");
    localStringBuilder.append(paramState);
    log(localStringBuilder.toString());
    switch (1.$SwitchMap$com$android$internal$telephony$Call$State[paramState.ordinal()])
    {
    default: 
      break;
    case 3: 
      mHoldingStartTime = SystemClock.elapsedRealtime();
      break;
    case 2: 
      mDuration = getDurationMillis();
      mDisconnectTime = System.currentTimeMillis();
      break;
    case 1: 
      if (mConnectTime == 0L)
      {
        mConnectTimeReal = SystemClock.elapsedRealtime();
        mConnectTime = System.currentTimeMillis();
      }
      break;
    }
  }
}
