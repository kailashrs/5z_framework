package com.android.internal.telephony.sip;

import android.content.Context;
import android.net.sip.SipProfile.Builder;
import android.telephony.Rlog;
import com.android.internal.telephony.PhoneNotifier;
import java.text.ParseException;

public class SipPhoneFactory
{
  public SipPhoneFactory() {}
  
  public static SipPhone makePhone(String paramString, Context paramContext, PhoneNotifier paramPhoneNotifier)
  {
    try
    {
      SipProfile.Builder localBuilder = new android/net/sip/SipProfile$Builder;
      localBuilder.<init>(paramString);
      paramString = new SipPhone(paramContext, paramPhoneNotifier, localBuilder.build());
      return paramString;
    }
    catch (ParseException paramString)
    {
      Rlog.w("SipPhoneFactory", "makePhone", paramString);
    }
    return null;
  }
}
