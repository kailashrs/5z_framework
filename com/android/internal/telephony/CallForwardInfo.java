package com.android.internal.telephony;

import android.telecom.Log;

public class CallForwardInfo
{
  private static final String TAG = "CallForwardInfo";
  public String number;
  public int reason;
  public int serviceClass;
  public int status;
  public int timeSeconds;
  public int toa;
  
  public CallForwardInfo() {}
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("[CallForwardInfo: status=");
    String str;
    if (status == 0) {
      str = " not active ";
    } else {
      str = " active ";
    }
    localStringBuilder.append(str);
    localStringBuilder.append(", reason= ");
    localStringBuilder.append(reason);
    localStringBuilder.append(", serviceClass= ");
    localStringBuilder.append(serviceClass);
    localStringBuilder.append(", timeSec= ");
    localStringBuilder.append(timeSeconds);
    localStringBuilder.append(" seconds, number=");
    localStringBuilder.append(Log.pii(number));
    localStringBuilder.append("]");
    return localStringBuilder.toString();
  }
}
