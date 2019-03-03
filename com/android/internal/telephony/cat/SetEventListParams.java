package com.android.internal.telephony.cat;

class SetEventListParams
  extends CommandParams
{
  int[] mEventInfo;
  
  SetEventListParams(CommandDetails paramCommandDetails, int[] paramArrayOfInt)
  {
    super(paramCommandDetails);
    mEventInfo = paramArrayOfInt;
  }
}
