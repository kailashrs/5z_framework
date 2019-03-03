package com.android.internal.telephony;

import android.os.AsyncResult;
import android.os.Message;
import android.os.SystemClock;
import android.os.WorkSource;
import android.os.WorkSource.WorkChain;
import android.telephony.Rlog;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class RILRequest
{
  static final String LOG_TAG = "RilRequest";
  private static final int MAX_POOL_SIZE = 4;
  static AtomicInteger sNextSerial;
  private static RILRequest sPool = null;
  private static int sPoolSize = 0;
  private static Object sPoolSync;
  static Random sRandom = new Random();
  String mClientId;
  RILRequest mNext;
  int mRequest;
  Message mResult;
  int mSerial;
  long mStartTimeMs;
  int mWakeLockType;
  WorkSource mWorkSource;
  
  static
  {
    sNextSerial = new AtomicInteger(0);
    sPoolSync = new Object();
  }
  
  private RILRequest() {}
  
  private static RILRequest obtain(int paramInt, Message paramMessage)
  {
    RILRequest localRILRequest = null;
    synchronized (sPoolSync)
    {
      if (sPool != null)
      {
        localRILRequest = sPool;
        sPool = mNext;
        mNext = null;
        sPoolSize -= 1;
      }
      ??? = localRILRequest;
      if (localRILRequest == null) {
        ??? = new RILRequest();
      }
      mSerial = sNextSerial.getAndIncrement();
      mRequest = paramInt;
      mResult = paramMessage;
      mWakeLockType = -1;
      mWorkSource = null;
      mStartTimeMs = SystemClock.elapsedRealtime();
      if ((paramMessage != null) && (paramMessage.getTarget() == null)) {
        throw new NullPointerException("Message target must not be null");
      }
      return ???;
    }
  }
  
  public static RILRequest obtain(int paramInt, Message paramMessage, WorkSource paramWorkSource)
  {
    paramMessage = obtain(paramInt, paramMessage);
    if (paramWorkSource != null)
    {
      mWorkSource = paramWorkSource;
      mClientId = paramMessage.getWorkSourceClientId();
    }
    else
    {
      paramWorkSource = new StringBuilder();
      paramWorkSource.append("null workSource ");
      paramWorkSource.append(paramInt);
      Rlog.e("RilRequest", paramWorkSource.toString());
    }
    return paramMessage;
  }
  
  static void resetSerial()
  {
    sNextSerial.set(sRandom.nextInt());
  }
  
  public int getRequest()
  {
    return mRequest;
  }
  
  public Message getResult()
  {
    return mResult;
  }
  
  public int getSerial()
  {
    return mSerial;
  }
  
  public String getWorkSourceClientId()
  {
    if ((mWorkSource != null) && (!mWorkSource.isEmpty()))
    {
      if (mWorkSource.size() > 0)
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append(mWorkSource.get(0));
        ((StringBuilder)localObject).append(":");
        ((StringBuilder)localObject).append(mWorkSource.getName(0));
        return ((StringBuilder)localObject).toString();
      }
      Object localObject = mWorkSource.getWorkChains();
      if ((localObject != null) && (!((ArrayList)localObject).isEmpty()))
      {
        WorkSource.WorkChain localWorkChain = (WorkSource.WorkChain)((ArrayList)localObject).get(0);
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append(localWorkChain.getAttributionUid());
        ((StringBuilder)localObject).append(":");
        ((StringBuilder)localObject).append(localWorkChain.getTags()[0]);
        return ((StringBuilder)localObject).toString();
      }
      return null;
    }
    return null;
  }
  
  void onError(int paramInt, Object paramObject)
  {
    CommandException localCommandException = CommandException.fromRilErrno(paramInt);
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(serialString());
    localStringBuilder.append("< ");
    localStringBuilder.append(RIL.requestToString(mRequest));
    localStringBuilder.append(" error: ");
    localStringBuilder.append(localCommandException);
    localStringBuilder.append(" ret=");
    localStringBuilder.append(RIL.retToString(mRequest, paramObject));
    Rlog.d("RilRequest", localStringBuilder.toString());
    if (mResult != null)
    {
      AsyncResult.forMessage(mResult, paramObject, localCommandException);
      mResult.sendToTarget();
    }
  }
  
  void release()
  {
    synchronized (sPoolSync)
    {
      if ((sPoolSize < 4) && (mNext == null))
      {
        mNext = sPool;
        sPool = this;
        sPoolSize += 1;
        mResult = null;
        if ((mWakeLockType != -1) && (mWakeLockType == 0))
        {
          StringBuilder localStringBuilder = new java/lang/StringBuilder;
          localStringBuilder.<init>();
          localStringBuilder.append("RILRequest releasing with held wake lock: ");
          localStringBuilder.append(serialString());
          Rlog.e("RilRequest", localStringBuilder.toString());
        }
      }
      return;
    }
  }
  
  String serialString()
  {
    StringBuilder localStringBuilder = new StringBuilder(8);
    String str = Long.toString((mSerial + 2147483648L) % 10000L);
    localStringBuilder.append('[');
    int i = 0;
    int j = str.length();
    while (i < 4 - j)
    {
      localStringBuilder.append('0');
      i++;
    }
    localStringBuilder.append(str);
    localStringBuilder.append(']');
    return localStringBuilder.toString();
  }
}
