package com.android.internal.telephony;

import android.telephony.Rlog;

public class TelephonyCapabilities
{
  private static final String LOG_TAG = "TelephonyCapabilities";
  
  private TelephonyCapabilities() {}
  
  public static boolean canDistinguishDialingAndConnected(int paramInt)
  {
    boolean bool = true;
    if (paramInt != 1) {
      bool = false;
    }
    return bool;
  }
  
  public static int getDeviceIdLabel(Phone paramPhone)
  {
    if (paramPhone.getPhoneType() == 1) {
      return 17040129;
    }
    if (paramPhone.getPhoneType() == 2) {
      return 17040425;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("getDeviceIdLabel: no known label for phone ");
    localStringBuilder.append(paramPhone.getPhoneName());
    Rlog.w("TelephonyCapabilities", localStringBuilder.toString());
    return 0;
  }
  
  public static boolean supportsAdn(int paramInt)
  {
    boolean bool = true;
    if (paramInt != 1) {
      bool = false;
    }
    return bool;
  }
  
  public static boolean supportsAnswerAndHold(Phone paramPhone)
  {
    int i = paramPhone.getPhoneType();
    boolean bool1 = true;
    boolean bool2 = bool1;
    if (i != 1) {
      if (paramPhone.getPhoneType() == 3) {
        bool2 = bool1;
      } else {
        bool2 = false;
      }
    }
    return bool2;
  }
  
  public static boolean supportsConferenceCallManagement(Phone paramPhone)
  {
    int i = paramPhone.getPhoneType();
    boolean bool1 = true;
    boolean bool2 = bool1;
    if (i != 1) {
      if (paramPhone.getPhoneType() == 3) {
        bool2 = bool1;
      } else {
        bool2 = false;
      }
    }
    return bool2;
  }
  
  public static boolean supportsEcm(Phone paramPhone)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("supportsEcm: Phone type = ");
    localStringBuilder.append(paramPhone.getPhoneType());
    localStringBuilder.append(" Ims Phone = ");
    localStringBuilder.append(paramPhone.getImsPhone());
    Rlog.d("TelephonyCapabilities", localStringBuilder.toString());
    boolean bool;
    if ((paramPhone.getPhoneType() != 2) && (paramPhone.getImsPhone() == null)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public static boolean supportsHoldAndUnhold(Phone paramPhone)
  {
    int i = paramPhone.getPhoneType();
    boolean bool1 = true;
    boolean bool2 = bool1;
    if (i != 1)
    {
      bool2 = bool1;
      if (paramPhone.getPhoneType() != 3) {
        if (paramPhone.getPhoneType() == 5) {
          bool2 = bool1;
        } else {
          bool2 = false;
        }
      }
    }
    return bool2;
  }
  
  public static boolean supportsNetworkSelection(Phone paramPhone)
  {
    int i = paramPhone.getPhoneType();
    boolean bool = true;
    if (i != 1) {
      bool = false;
    }
    return bool;
  }
  
  public static boolean supportsOtasp(Phone paramPhone)
  {
    boolean bool;
    if (paramPhone.getPhoneType() == 2) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static boolean supportsVoiceMessageCount(Phone paramPhone)
  {
    boolean bool;
    if (paramPhone.getVoiceMessageCount() != -1) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
}
