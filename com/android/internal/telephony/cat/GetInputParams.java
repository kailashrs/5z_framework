package com.android.internal.telephony.cat;

import android.graphics.Bitmap;

class GetInputParams
  extends CommandParams
{
  Input mInput = null;
  
  GetInputParams(CommandDetails paramCommandDetails, Input paramInput)
  {
    super(paramCommandDetails);
    mInput = paramInput;
  }
  
  boolean setIcon(Bitmap paramBitmap)
  {
    if ((paramBitmap != null) && (mInput != null)) {
      mInput.icon = paramBitmap;
    }
    return true;
  }
}
