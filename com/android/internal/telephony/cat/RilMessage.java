package com.android.internal.telephony.cat;

class RilMessage
{
  Object mData;
  int mId;
  ResultCode mResCode;
  
  RilMessage(int paramInt, String paramString)
  {
    mId = paramInt;
    mData = paramString;
  }
  
  RilMessage(RilMessage paramRilMessage)
  {
    mId = mId;
    mData = mData;
    mResCode = mResCode;
  }
}
