package com.android.internal.telephony.uicc.asn1;

public class TagNotFoundException
  extends Exception
{
  private final int mTag;
  
  public TagNotFoundException(int paramInt)
  {
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
