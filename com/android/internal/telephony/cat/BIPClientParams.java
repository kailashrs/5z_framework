package com.android.internal.telephony.cat;

import android.graphics.Bitmap;

class BIPClientParams
  extends CommandParams
{
  boolean mHasAlphaId;
  TextMessage mTextMsg;
  
  BIPClientParams(CommandDetails paramCommandDetails, TextMessage paramTextMessage, boolean paramBoolean)
  {
    super(paramCommandDetails);
    mTextMsg = paramTextMessage;
    mHasAlphaId = paramBoolean;
  }
  
  boolean setIcon(Bitmap paramBitmap)
  {
    if ((paramBitmap != null) && (mTextMsg != null))
    {
      mTextMsg.icon = paramBitmap;
      return true;
    }
    return false;
  }
}
