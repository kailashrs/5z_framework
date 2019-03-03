package com.android.internal.telephony.uicc;

import android.telephony.Rlog;
import com.android.internal.telephony.CommandsInterface;

public final class CsimFileHandler
  extends IccFileHandler
  implements IccConstants
{
  static final String LOG_TAG = "CsimFH";
  
  public CsimFileHandler(UiccCardApplication paramUiccCardApplication, String paramString, CommandsInterface paramCommandsInterface)
  {
    super(paramUiccCardApplication, paramString, paramCommandsInterface);
  }
  
  protected String getEFPath(int paramInt)
  {
    String str;
    switch (paramInt)
    {
    default: 
      str = getCommonIccEFPath(paramInt);
      if (str == null) {
        return "3F007F105F3A";
      }
      break;
    case 28450: 
    case 28456: 
    case 28464: 
    case 28465: 
    case 28466: 
    case 28474: 
    case 28475: 
    case 28476: 
    case 28480: 
    case 28481: 
    case 28484: 
    case 28493: 
    case 28506: 
      return "3F007FFF";
    case 20256: 
    case 20257: 
      return "3F007F105F3C";
    }
    return str;
  }
  
  protected void logd(String paramString)
  {
    Rlog.d("CsimFH", paramString);
  }
  
  protected void loge(String paramString)
  {
    Rlog.e("CsimFH", paramString);
  }
}
