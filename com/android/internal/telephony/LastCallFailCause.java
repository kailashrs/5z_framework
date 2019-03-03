package com.android.internal.telephony;

public class LastCallFailCause
{
  public int causeCode;
  public String vendorCause;
  
  public LastCallFailCause() {}
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(super.toString());
    localStringBuilder.append(" causeCode: ");
    localStringBuilder.append(causeCode);
    localStringBuilder.append(" vendorCause: ");
    localStringBuilder.append(vendorCause);
    return localStringBuilder.toString();
  }
}
