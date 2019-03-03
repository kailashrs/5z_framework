package com.android.internal.telephony.imsphone;

import android.content.Context;
import android.telephony.Rlog;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneNotifier;

public class ImsPhoneFactory
{
  public ImsPhoneFactory() {}
  
  public static ImsPhone makePhone(Context paramContext, PhoneNotifier paramPhoneNotifier, Phone paramPhone)
  {
    try
    {
      paramContext = new ImsPhone(paramContext, paramPhoneNotifier, paramPhone);
      return paramContext;
    }
    catch (Exception paramContext)
    {
      Rlog.e("VoltePhoneFactory", "makePhone", paramContext);
    }
    return null;
  }
}
