package com.android.internal.telephony.uicc;

import android.telephony.Rlog;
import com.android.internal.telephony.CommandsInterface;

public final class UsimFileHandler
  extends IccFileHandler
  implements IccConstants
{
  static final String LOG_TAG = "UsimFH";
  
  public UsimFileHandler(UiccCardApplication paramUiccCardApplication, String paramString, CommandsInterface paramCommandsInterface)
  {
    super(paramUiccCardApplication, paramString, paramCommandsInterface);
  }
  
  protected String getEFPath(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      switch (paramInt)
      {
      default: 
        switch (paramInt)
        {
        default: 
          switch (paramInt)
          {
          default: 
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
                    return "3F007F105F3A";
                  }
                  return str;
                case 20272: 
                  return "3F007F105F3A";
                }
                break;
              }
              break;
            }
            break;
          }
          break;
        }
        break;
      }
      break;
    }
    return "3F007FFF";
  }
  
  protected void logd(String paramString)
  {
    Rlog.d("UsimFH", paramString);
  }
  
  protected void loge(String paramString)
  {
    Rlog.e("UsimFH", paramString);
  }
}
