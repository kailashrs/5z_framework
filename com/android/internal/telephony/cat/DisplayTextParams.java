package com.android.internal.telephony.cat;

import android.graphics.Bitmap;

class DisplayTextParams
  extends CommandParams
{
  TextMessage mTextMsg;
  
  DisplayTextParams(CommandDetails paramCommandDetails, TextMessage paramTextMessage)
  {
    super(paramCommandDetails);
    mTextMsg = paramTextMessage;
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
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("TextMessage=");
    localStringBuilder.append(mTextMsg);
    localStringBuilder.append(" ");
    localStringBuilder.append(super.toString());
    return localStringBuilder.toString();
  }
}
