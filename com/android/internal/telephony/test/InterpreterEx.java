package com.android.internal.telephony.test;

class InterpreterEx
  extends Exception
{
  String mResult;
  
  public InterpreterEx(String paramString)
  {
    mResult = paramString;
  }
}
