package com.android.internal.telephony.cat;

import android.graphics.Bitmap;

class PlayToneParams
  extends CommandParams
{
  ToneSettings mSettings;
  TextMessage mTextMsg;
  
  PlayToneParams(CommandDetails paramCommandDetails, TextMessage paramTextMessage, Tone paramTone, Duration paramDuration, boolean paramBoolean)
  {
    super(paramCommandDetails);
    mTextMsg = paramTextMessage;
    mSettings = new ToneSettings(paramDuration, paramTone, paramBoolean);
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
