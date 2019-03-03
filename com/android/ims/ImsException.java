package com.android.ims;

public class ImsException
  extends Exception
{
  private int mCode;
  
  public ImsException() {}
  
  public ImsException(String paramString, int paramInt)
  {
    super(localStringBuilder.toString());
    mCode = paramInt;
  }
  
  public ImsException(String paramString, Throwable paramThrowable, int paramInt)
  {
    super(paramString, paramThrowable);
    mCode = paramInt;
  }
  
  public int getCode()
  {
    return mCode;
  }
}
