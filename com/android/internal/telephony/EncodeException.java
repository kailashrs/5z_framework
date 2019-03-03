package com.android.internal.telephony;

public class EncodeException
  extends Exception
{
  public EncodeException() {}
  
  public EncodeException(char paramChar)
  {
    super(localStringBuilder.toString());
  }
  
  public EncodeException(String paramString)
  {
    super(paramString);
  }
}
