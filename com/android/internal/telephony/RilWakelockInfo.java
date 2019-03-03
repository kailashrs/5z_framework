package com.android.internal.telephony;

import android.annotation.TargetApi;
import android.os.Build;
import android.telephony.Rlog;
import com.android.internal.annotations.VisibleForTesting;

@TargetApi(8)
public class RilWakelockInfo
{
  private final String LOG_TAG = RilWakelockInfo.class.getSimpleName();
  private int mConcurrentRequests;
  private long mLastAggregatedTime;
  private long mRequestTime;
  private long mResponseTime;
  private int mRilRequestSent;
  private int mTokenNumber;
  private long mWakelockTimeAttributedSoFar;
  
  RilWakelockInfo(int paramInt1, int paramInt2, int paramInt3, long paramLong)
  {
    paramInt3 = validateConcurrentRequests(paramInt3);
    mRilRequestSent = paramInt1;
    mTokenNumber = paramInt2;
    mConcurrentRequests = paramInt3;
    mRequestTime = paramLong;
    mWakelockTimeAttributedSoFar = 0L;
    mLastAggregatedTime = paramLong;
  }
  
  private int validateConcurrentRequests(int paramInt)
  {
    int i = paramInt;
    if (paramInt <= 0) {
      if (!Build.IS_DEBUGGABLE)
      {
        i = 1;
      }
      else
      {
        IllegalArgumentException localIllegalArgumentException = new IllegalArgumentException("concurrentRequests should always be greater than 0.");
        Rlog.e(LOG_TAG, localIllegalArgumentException.toString());
        throw localIllegalArgumentException;
      }
    }
    return i;
  }
  
  @VisibleForTesting
  public int getConcurrentRequests()
  {
    return mConcurrentRequests;
  }
  
  int getRilRequestSent()
  {
    return mRilRequestSent;
  }
  
  int getTokenNumber()
  {
    return mTokenNumber;
  }
  
  long getWakelockTimeAttributedToClient()
  {
    return mWakelockTimeAttributedSoFar;
  }
  
  void setResponseTime(long paramLong)
  {
    updateTime(paramLong);
    mResponseTime = paramLong;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("WakelockInfo{rilRequestSent=");
    localStringBuilder.append(mRilRequestSent);
    localStringBuilder.append(", tokenNumber=");
    localStringBuilder.append(mTokenNumber);
    localStringBuilder.append(", requestTime=");
    localStringBuilder.append(mRequestTime);
    localStringBuilder.append(", responseTime=");
    localStringBuilder.append(mResponseTime);
    localStringBuilder.append(", mWakelockTimeAttributed=");
    localStringBuilder.append(mWakelockTimeAttributedSoFar);
    localStringBuilder.append('}');
    return localStringBuilder.toString();
  }
  
  void updateConcurrentRequests(int paramInt, long paramLong)
  {
    paramInt = validateConcurrentRequests(paramInt);
    updateTime(paramLong);
    mConcurrentRequests = paramInt;
  }
  
  void updateTime(long paramLong)
  {
    try
    {
      mWakelockTimeAttributedSoFar += (paramLong - mLastAggregatedTime) / mConcurrentRequests;
      mLastAggregatedTime = paramLong;
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
}
