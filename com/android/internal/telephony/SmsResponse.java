package com.android.internal.telephony;

public class SmsResponse
{
  String mAckPdu;
  public int mErrorCode;
  int mMessageRef;
  
  public SmsResponse(int paramInt1, String paramString, int paramInt2)
  {
    mMessageRef = paramInt1;
    mAckPdu = paramString;
    mErrorCode = paramInt2;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{ mMessageRef = ");
    localStringBuilder.append(mMessageRef);
    localStringBuilder.append(", mErrorCode = ");
    localStringBuilder.append(mErrorCode);
    localStringBuilder.append(", mAckPdu = ");
    localStringBuilder.append(mAckPdu);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
}
