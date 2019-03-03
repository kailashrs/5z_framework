package com.android.internal.telephony.uicc;

import android.telephony.Rlog;
import com.android.internal.telephony.CommandsInterface;

public final class SIMFileHandler
  extends IccFileHandler
  implements IccConstants
{
  static final String LOG_TAG = "SIMFileHandler";
  
  public SIMFileHandler(UiccCardApplication paramUiccCardApplication, String paramString, CommandsInterface paramCommandsInterface)
  {
    super(paramUiccCardApplication, paramString, paramCommandsInterface);
  }
  
  protected String getEFPath(int paramInt)
  {
    if ((paramInt != 28433) && (paramInt != 28472)) {
      if (paramInt != 28476)
      {
        if ((paramInt == 28486) || (paramInt == 28589) || (paramInt == 28613) || (paramInt == 28621)) {}
      }
      else {
        switch (paramInt)
        {
        default: 
          switch (paramInt)
          {
          default: 
            switch (paramInt)
            {
            default: 
              String str = getCommonIccEFPath(paramInt);
              if (str == null) {
                Rlog.e("SIMFileHandler", "Error: EF Path being returned in null");
              }
              return str;
              return "3F007F10";
            }
            break;
          }
          break;
        }
      }
    }
    return "3F007F20";
  }
  
  protected void logd(String paramString)
  {
    Rlog.d("SIMFileHandler", paramString);
  }
  
  protected void loge(String paramString)
  {
    Rlog.e("SIMFileHandler", paramString);
  }
}
