package com.android.internal.telephony.uicc.asn1;

public class InvalidAsn1DataException
  extends Exception
{
  private final int mTag;
  
  public InvalidAsn1DataException(int paramInt, String paramString)
  {
    super(paramString);
    mTag = paramInt;
  }
  
  public InvalidAsn1DataException(int paramInt, String paramString, Throwable paramThrowable)
  {
    super(paramString, paramThrowable);
    mTag = paramInt;
  }
  
  public String getMessage()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(super.getMessage());
    localStringBuilder.append(" (tag=");
    localStringBuilder.append(mTag);
    localStringBuilder.append(")");
    return localStringBuilder.toString();
  }
  
  public int getTag()
  {
    return mTag;
  }
}
