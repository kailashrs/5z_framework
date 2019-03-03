package com.google.android.mms;

public class MmsException
  extends Exception
{
  private static final long serialVersionUID = -7323249827281485390L;
  
  public MmsException() {}
  
  public MmsException(String paramString)
  {
    super(paramString);
  }
  
  public MmsException(String paramString, Throwable paramThrowable)
  {
    super(paramString, paramThrowable);
  }
  
  public MmsException(Throwable paramThrowable)
  {
    super(paramThrowable);
  }
}
