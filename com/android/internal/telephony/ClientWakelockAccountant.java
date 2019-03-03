package com.android.internal.telephony;

import android.telephony.ClientRequestStats;
import android.telephony.Rlog;
import com.android.internal.annotations.VisibleForTesting;
import java.util.ArrayList;
import java.util.Iterator;

public class ClientWakelockAccountant
{
  public static final String LOG_TAG = "ClientWakelockAccountant: ";
  @VisibleForTesting
  public ArrayList<RilWakelockInfo> mPendingRilWakelocks = new ArrayList();
  @VisibleForTesting
  public ClientRequestStats mRequestStats = new ClientRequestStats();
  
  @VisibleForTesting
  public ClientWakelockAccountant(String paramString)
  {
    mRequestStats.setCallingPackage(paramString);
  }
  
  private void completeRequest(RilWakelockInfo paramRilWakelockInfo, long paramLong)
  {
    paramRilWakelockInfo.setResponseTime(paramLong);
    synchronized (mRequestStats)
    {
      mRequestStats.addCompletedWakelockTime(paramRilWakelockInfo.getWakelockTimeAttributedToClient());
      mRequestStats.incrementCompletedRequestsCount();
      mRequestStats.updateRequestHistograms(paramRilWakelockInfo.getRilRequestSent(), (int)paramRilWakelockInfo.getWakelockTimeAttributedToClient());
      return;
    }
  }
  
  private RilWakelockInfo removePendingWakelock(int paramInt1, int paramInt2)
  {
    Object localObject1 = null;
    synchronized (mPendingRilWakelocks)
    {
      Iterator localIterator = mPendingRilWakelocks.iterator();
      Object localObject3;
      while (localIterator.hasNext())
      {
        RilWakelockInfo localRilWakelockInfo = (RilWakelockInfo)localIterator.next();
        localObject3 = localObject1;
        if (localRilWakelockInfo.getTokenNumber() == paramInt2)
        {
          localObject3 = localObject1;
          if (localRilWakelockInfo.getRilRequestSent() == paramInt1) {
            localObject3 = localRilWakelockInfo;
          }
        }
        localObject1 = localObject3;
      }
      if (localObject1 != null) {
        mPendingRilWakelocks.remove(localObject1);
      }
      if (localObject1 == null)
      {
        localObject3 = new StringBuilder();
        ((StringBuilder)localObject3).append("Looking for Request<");
        ((StringBuilder)localObject3).append(paramInt1);
        ((StringBuilder)localObject3).append(",");
        ((StringBuilder)localObject3).append(paramInt2);
        ((StringBuilder)localObject3).append("> in ");
        ((StringBuilder)localObject3).append(mPendingRilWakelocks);
        Rlog.w("ClientWakelockAccountant: ", ((StringBuilder)localObject3).toString());
      }
      return localObject1;
    }
  }
  
  @VisibleForTesting
  public void changeConcurrentRequests(int paramInt, long paramLong)
  {
    synchronized (mPendingRilWakelocks)
    {
      Iterator localIterator = mPendingRilWakelocks.iterator();
      while (localIterator.hasNext()) {
        ((RilWakelockInfo)localIterator.next()).updateConcurrentRequests(paramInt, paramLong);
      }
      return;
    }
  }
  
  @VisibleForTesting
  public int getPendingRequestCount()
  {
    return mPendingRilWakelocks.size();
  }
  
  @VisibleForTesting
  public void startAttributingWakelock(int paramInt1, int paramInt2, int paramInt3, long paramLong)
  {
    RilWakelockInfo localRilWakelockInfo = new RilWakelockInfo(paramInt1, paramInt2, paramInt3, paramLong);
    synchronized (mPendingRilWakelocks)
    {
      mPendingRilWakelocks.add(localRilWakelockInfo);
      return;
    }
  }
  
  @VisibleForTesting
  public void stopAllPendingRequests(long paramLong)
  {
    synchronized (mPendingRilWakelocks)
    {
      Iterator localIterator = mPendingRilWakelocks.iterator();
      while (localIterator.hasNext()) {
        completeRequest((RilWakelockInfo)localIterator.next(), paramLong);
      }
      mPendingRilWakelocks.clear();
      return;
    }
  }
  
  @VisibleForTesting
  public void stopAttributingWakelock(int paramInt1, int paramInt2, long paramLong)
  {
    RilWakelockInfo localRilWakelockInfo = removePendingWakelock(paramInt1, paramInt2);
    if (localRilWakelockInfo != null) {
      completeRequest(localRilWakelockInfo, paramLong);
    }
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("ClientWakelockAccountant{mRequestStats=");
    localStringBuilder.append(mRequestStats);
    localStringBuilder.append(", mPendingRilWakelocks=");
    localStringBuilder.append(mPendingRilWakelocks);
    localStringBuilder.append('}');
    return localStringBuilder.toString();
  }
  
  /* Error */
  @VisibleForTesting
  public long updatePendingRequestWakelockTime(long paramLong)
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: lconst_0
    //   3: lstore_3
    //   4: aload_0
    //   5: getfield 29	com/android/internal/telephony/ClientWakelockAccountant:mPendingRilWakelocks	Ljava/util/ArrayList;
    //   8: astore 5
    //   10: aload 5
    //   12: monitorenter
    //   13: aload_0
    //   14: getfield 29	com/android/internal/telephony/ClientWakelockAccountant:mPendingRilWakelocks	Ljava/util/ArrayList;
    //   17: invokevirtual 66	java/util/ArrayList:iterator	()Ljava/util/Iterator;
    //   20: astore 6
    //   22: aload 6
    //   24: invokeinterface 72 1 0
    //   29: ifeq +32 -> 61
    //   32: aload 6
    //   34: invokeinterface 76 1 0
    //   39: checkcast 38	com/android/internal/telephony/RilWakelockInfo
    //   42: astore 7
    //   44: aload 7
    //   46: lload_1
    //   47: invokevirtual 150	com/android/internal/telephony/RilWakelockInfo:updateTime	(J)V
    //   50: lload_3
    //   51: aload 7
    //   53: invokevirtual 46	com/android/internal/telephony/RilWakelockInfo:getWakelockTimeAttributedToClient	()J
    //   56: ladd
    //   57: lstore_3
    //   58: goto -36 -> 22
    //   61: aload 5
    //   63: monitorexit
    //   64: aload_0
    //   65: getfield 24	com/android/internal/telephony/ClientWakelockAccountant:mRequestStats	Landroid/telephony/ClientRequestStats;
    //   68: astore 7
    //   70: aload 7
    //   72: monitorenter
    //   73: aload_0
    //   74: getfield 24	com/android/internal/telephony/ClientWakelockAccountant:mRequestStats	Landroid/telephony/ClientRequestStats;
    //   77: aload_0
    //   78: invokevirtual 152	com/android/internal/telephony/ClientWakelockAccountant:getPendingRequestCount	()I
    //   81: i2l
    //   82: invokevirtual 155	android/telephony/ClientRequestStats:setPendingRequestsCount	(J)V
    //   85: aload_0
    //   86: getfield 24	com/android/internal/telephony/ClientWakelockAccountant:mRequestStats	Landroid/telephony/ClientRequestStats;
    //   89: lload_3
    //   90: invokevirtual 158	android/telephony/ClientRequestStats:setPendingRequestsWakelockTime	(J)V
    //   93: aload 7
    //   95: monitorexit
    //   96: aload_0
    //   97: monitorexit
    //   98: lload_3
    //   99: lreturn
    //   100: astore 5
    //   102: aload 7
    //   104: monitorexit
    //   105: aload 5
    //   107: athrow
    //   108: astore 7
    //   110: aload 5
    //   112: monitorexit
    //   113: aload 7
    //   115: athrow
    //   116: astore 5
    //   118: aload_0
    //   119: monitorexit
    //   120: aload 5
    //   122: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	123	0	this	ClientWakelockAccountant
    //   0	123	1	paramLong	long
    //   3	96	3	l	long
    //   100	11	5	localObject1	Object
    //   116	5	5	localObject2	Object
    //   20	13	6	localIterator	Iterator
    //   108	6	7	localObject4	Object
    // Exception table:
    //   from	to	target	type
    //   73	96	100	finally
    //   102	105	100	finally
    //   13	22	108	finally
    //   22	58	108	finally
    //   61	64	108	finally
    //   110	113	108	finally
    //   4	13	116	finally
    //   64	73	116	finally
    //   105	108	116	finally
    //   113	116	116	finally
  }
}
