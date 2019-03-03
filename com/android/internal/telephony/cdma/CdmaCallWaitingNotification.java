package com.android.internal.telephony.cdma;

import android.telephony.Rlog;

public class CdmaCallWaitingNotification
{
  static final String LOG_TAG = "CdmaCallWaitingNotification";
  public int alertPitch = 0;
  public int isPresent = 0;
  public String name = null;
  public int namePresentation = 0;
  public String number = null;
  public int numberPlan = 0;
  public int numberPresentation = 0;
  public int numberType = 0;
  public int signal = 0;
  public int signalType = 0;
  
  public CdmaCallWaitingNotification() {}
  
  public static int presentationFromCLIP(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Unexpected presentation ");
      localStringBuilder.append(paramInt);
      Rlog.d("CdmaCallWaitingNotification", localStringBuilder.toString());
      return 3;
    case 2: 
      return 3;
    case 1: 
      return 2;
    }
    return 1;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(super.toString());
    localStringBuilder.append("Call Waiting Notification   number: ");
    localStringBuilder.append(number);
    localStringBuilder.append(" numberPresentation: ");
    localStringBuilder.append(numberPresentation);
    localStringBuilder.append(" name: ");
    localStringBuilder.append(name);
    localStringBuilder.append(" namePresentation: ");
    localStringBuilder.append(namePresentation);
    localStringBuilder.append(" numberType: ");
    localStringBuilder.append(numberType);
    localStringBuilder.append(" numberPlan: ");
    localStringBuilder.append(numberPlan);
    localStringBuilder.append(" isPresent: ");
    localStringBuilder.append(isPresent);
    localStringBuilder.append(" signalType: ");
    localStringBuilder.append(signalType);
    localStringBuilder.append(" alertPitch: ");
    localStringBuilder.append(alertPitch);
    localStringBuilder.append(" signal: ");
    localStringBuilder.append(signal);
    return localStringBuilder.toString();
  }
}
