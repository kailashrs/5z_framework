package com.android.internal.telephony.uicc;

import android.telephony.Rlog;
import com.android.internal.telephony.CommandsInterface;

public final class IsimFileHandler
  extends IccFileHandler
  implements IccConstants
{
  static final String LOG_TAG = "IsimFH";
  
  public IsimFileHandler(UiccCardApplication paramUiccCardApplication, String paramString, CommandsInterface paramCommandsInterface)
  {
    super(paramUiccCardApplication, paramString, paramCommandsInterface);
  }
  
  protected String getEFPath(int paramInt)
  {
    if ((paramInt != 28423) && (paramInt != 28425)) {
      switch (paramInt)
      {
      default: 
        return getCommonIccEFPath(paramInt);
      }
    }
    return "3F007FFF";
  }
  
  protected void logd(String paramString)
  {
    Rlog.d("IsimFH", paramString);
  }
  
  protected void loge(String paramString)
  {
    Rlog.e("IsimFH", paramString);
  }
}
