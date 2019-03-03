package com.android.internal.telephony.uicc;

import android.os.Message;
import android.telephony.Rlog;
import com.android.internal.telephony.CommandsInterface;

public final class RuimFileHandler
  extends IccFileHandler
{
  static final String LOG_TAG = "RuimFH";
  
  public RuimFileHandler(UiccCardApplication paramUiccCardApplication, String paramString, CommandsInterface paramCommandsInterface)
  {
    super(paramUiccCardApplication, paramString, paramCommandsInterface);
  }
  
  protected String getEFPath(int paramInt)
  {
    if ((paramInt != 28450) && (paramInt != 28456) && (paramInt != 28474) && (paramInt != 28476) && (paramInt != 28481) && (paramInt != 28484) && (paramInt != 28493) && (paramInt != 28506))
    {
      switch (paramInt)
      {
      default: 
        switch (paramInt)
        {
        default: 
          return getCommonIccEFPath(paramInt);
        }
        break;
      }
      return "3F007F105F3C";
    }
    return "3F007F25";
  }
  
  public void loadEFImgTransparent(int paramInt1, int paramInt2, int paramInt3, int paramInt4, Message paramMessage)
  {
    paramMessage = obtainMessage(10, paramInt1, 0, paramMessage);
    mCi.iccIOForApp(192, paramInt1, getEFPath(20256), 0, 0, 10, null, null, mAid, paramMessage);
  }
  
  protected void logd(String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("[RuimFileHandler] ");
    localStringBuilder.append(paramString);
    Rlog.d("RuimFH", localStringBuilder.toString());
  }
  
  protected void loge(String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("[RuimFileHandler] ");
    localStringBuilder.append(paramString);
    Rlog.e("RuimFH", localStringBuilder.toString());
  }
}
