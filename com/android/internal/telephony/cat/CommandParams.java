package com.android.internal.telephony.cat;

import android.graphics.Bitmap;

class CommandParams
{
  CommandDetails mCmdDet;
  boolean mLoadIconFailed = false;
  
  CommandParams(CommandDetails paramCommandDetails)
  {
    mCmdDet = paramCommandDetails;
  }
  
  AppInterface.CommandType getCommandType()
  {
    return AppInterface.CommandType.fromInt(mCmdDet.typeOfCommand);
  }
  
  boolean setIcon(Bitmap paramBitmap)
  {
    return true;
  }
  
  public String toString()
  {
    return mCmdDet.toString();
  }
}
