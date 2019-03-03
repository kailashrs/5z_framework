package com.android.internal.telephony.uicc.euicc.apdu;

public class ApduException
  extends Exception
{
  private final int mApduStatus;
  
  public ApduException(int paramInt)
  {
    mApduStatus = paramInt;
  }
  
  public ApduException(String paramString)
  {
    super(paramString);
    mApduStatus = 0;
  }
  
  public int getApduStatus()
  {
    return mApduStatus;
  }
  
  public String getMessage()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(super.getMessage());
    localStringBuilder.append(" (apduStatus=");
    localStringBuilder.append(getStatusHex());
    localStringBuilder.append(")");
    return localStringBuilder.toString();
  }
  
  public String getStatusHex()
  {
    return Integer.toHexString(mApduStatus);
  }
}
