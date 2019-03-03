package com.android.internal.telephony.cat;

import android.graphics.Bitmap;

class CallSetupParams
  extends CommandParams
{
  TextMessage mCallMsg;
  TextMessage mConfirmMsg;
  
  CallSetupParams(CommandDetails paramCommandDetails, TextMessage paramTextMessage1, TextMessage paramTextMessage2)
  {
    super(paramCommandDetails);
    mConfirmMsg = paramTextMessage1;
    mCallMsg = paramTextMessage2;
  }
  
  boolean setIcon(Bitmap paramBitmap)
  {
    if (paramBitmap == null) {
      return false;
    }
    if ((mConfirmMsg != null) && (mConfirmMsg.icon == null))
    {
      mConfirmMsg.icon = paramBitmap;
      return true;
    }
    if ((mCallMsg != null) && (mCallMsg.icon == null))
    {
      mCallMsg.icon = paramBitmap;
      return true;
    }
    return false;
  }
}
