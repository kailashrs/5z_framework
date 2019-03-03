package com.android.internal.telephony.cat;

import android.graphics.Bitmap;

class LaunchBrowserParams
  extends CommandParams
{
  TextMessage mConfirmMsg;
  LaunchBrowserMode mMode;
  String mUrl;
  
  LaunchBrowserParams(CommandDetails paramCommandDetails, TextMessage paramTextMessage, String paramString, LaunchBrowserMode paramLaunchBrowserMode)
  {
    super(paramCommandDetails);
    mConfirmMsg = paramTextMessage;
    mMode = paramLaunchBrowserMode;
    mUrl = paramString;
  }
  
  boolean setIcon(Bitmap paramBitmap)
  {
    if ((paramBitmap != null) && (mConfirmMsg != null))
    {
      mConfirmMsg.icon = paramBitmap;
      return true;
    }
    return false;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("TextMessage=");
    localStringBuilder.append(mConfirmMsg);
    localStringBuilder.append(" ");
    localStringBuilder.append(super.toString());
    return localStringBuilder.toString();
  }
}
