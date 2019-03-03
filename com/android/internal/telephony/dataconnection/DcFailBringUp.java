package com.android.internal.telephony.dataconnection;

import android.content.Intent;
import android.telephony.Rlog;

public class DcFailBringUp
{
  static final String ACTION_FAIL_BRINGUP = "action_fail_bringup";
  static final String COUNTER = "counter";
  private static final boolean DBG = true;
  static final int DEFAULT_COUNTER = 2;
  static final DcFailCause DEFAULT_FAIL_CAUSE = DcFailCause.ERROR_UNSPECIFIED;
  static final int DEFAULT_SUGGESTED_RETRY_TIME = -1;
  static final String FAIL_CAUSE = "fail_cause";
  static final String INTENT_BASE = DataConnection.class.getPackage().getName();
  private static final String LOG_TAG = "DcFailBringUp";
  static final String SUGGESTED_RETRY_TIME = "suggested_retry_time";
  int mCounter;
  DcFailCause mFailCause;
  int mSuggestedRetryTime;
  
  public DcFailBringUp() {}
  
  private static void log(String paramString)
  {
    Rlog.d("DcFailBringUp", paramString);
  }
  
  public void saveParameters(int paramInt1, int paramInt2, int paramInt3)
  {
    mCounter = paramInt1;
    mFailCause = DcFailCause.fromInt(paramInt2);
    mSuggestedRetryTime = paramInt3;
  }
  
  void saveParameters(Intent paramIntent, String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString);
    localStringBuilder.append(".saveParameters: action=");
    localStringBuilder.append(paramIntent.getAction());
    log(localStringBuilder.toString());
    mCounter = paramIntent.getIntExtra("counter", 2);
    mFailCause = DcFailCause.fromInt(paramIntent.getIntExtra("fail_cause", DEFAULT_FAIL_CAUSE.getErrorCode()));
    mSuggestedRetryTime = paramIntent.getIntExtra("suggested_retry_time", -1);
    paramIntent = new StringBuilder();
    paramIntent.append(paramString);
    paramIntent.append(".saveParameters: ");
    paramIntent.append(this);
    log(paramIntent.toString());
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{mCounter=");
    localStringBuilder.append(mCounter);
    localStringBuilder.append(" mFailCause=");
    localStringBuilder.append(mFailCause);
    localStringBuilder.append(" mSuggestedRetryTime=");
    localStringBuilder.append(mSuggestedRetryTime);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
}
