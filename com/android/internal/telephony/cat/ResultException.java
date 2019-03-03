package com.android.internal.telephony.cat;

public class ResultException
  extends CatException
{
  private int mAdditionalInfo;
  private String mExplanation;
  private ResultCode mResult;
  
  public ResultException(ResultCode paramResultCode)
  {
    switch (1.$SwitchMap$com$android$internal$telephony$cat$ResultCode[paramResultCode.ordinal()])
    {
    default: 
      mResult = paramResultCode;
      mAdditionalInfo = -1;
      mExplanation = "";
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("For result code, ");
    localStringBuilder.append(paramResultCode);
    localStringBuilder.append(", additional information must be given!");
    throw new AssertionError(localStringBuilder.toString());
  }
  
  public ResultException(ResultCode paramResultCode, int paramInt)
  {
    this(paramResultCode);
    if (paramInt >= 0)
    {
      mAdditionalInfo = paramInt;
      return;
    }
    throw new AssertionError("Additional info must be greater than zero!");
  }
  
  public ResultException(ResultCode paramResultCode, int paramInt, String paramString)
  {
    this(paramResultCode, paramInt);
    mExplanation = paramString;
  }
  
  public ResultException(ResultCode paramResultCode, String paramString)
  {
    this(paramResultCode);
    mExplanation = paramString;
  }
  
  public int additionalInfo()
  {
    return mAdditionalInfo;
  }
  
  public String explanation()
  {
    return mExplanation;
  }
  
  public boolean hasAdditionalInfo()
  {
    boolean bool;
    if (mAdditionalInfo >= 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public ResultCode result()
  {
    return mResult;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("result=");
    localStringBuilder.append(mResult);
    localStringBuilder.append(" additionalInfo=");
    localStringBuilder.append(mAdditionalInfo);
    localStringBuilder.append(" explantion=");
    localStringBuilder.append(mExplanation);
    return localStringBuilder.toString();
  }
}
