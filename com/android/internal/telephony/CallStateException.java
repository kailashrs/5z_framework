package com.android.internal.telephony;

public class CallStateException
  extends Exception
{
  public static final int ERROR_INVALID = -1;
  public static final int ERROR_OUT_OF_SERVICE = 1;
  public static final int ERROR_POWER_OFF = 2;
  private int mError = -1;
  
  public CallStateException() {}
  
  public CallStateException(int paramInt, String paramString)
  {
    super(paramString);
    mError = paramInt;
  }
  
  public CallStateException(String paramString)
  {
    super(paramString);
  }
  
  public int getError()
  {
    return mError;
  }
}
