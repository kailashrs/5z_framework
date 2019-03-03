package com.android.internal.telephony;

import android.os.SystemClock;
import android.telephony.ClientRequestStats;
import com.android.internal.annotations.VisibleForTesting;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ClientWakelockTracker
{
  public static final String LOG_TAG = "ClientWakelockTracker";
  @VisibleForTesting
  public ArrayList<ClientWakelockAccountant> mActiveClients = new ArrayList();
  @VisibleForTesting
  public HashMap<String, ClientWakelockAccountant> mClients = new HashMap();
  
  public ClientWakelockTracker() {}
  
  private ClientWakelockAccountant getClientWakelockAccountant(String paramString)
  {
    synchronized (mClients)
    {
      if (mClients.containsKey(paramString))
      {
        paramString = (ClientWakelockAccountant)mClients.get(paramString);
      }
      else
      {
        ClientWakelockAccountant localClientWakelockAccountant = new com/android/internal/telephony/ClientWakelockAccountant;
        localClientWakelockAccountant.<init>(paramString);
        mClients.put(paramString, localClientWakelockAccountant);
        paramString = localClientWakelockAccountant;
      }
      return paramString;
    }
  }
  
  private void updateConcurrentRequests(int paramInt, long paramLong)
  {
    if (paramInt != 0) {
      synchronized (mActiveClients)
      {
        Iterator localIterator = mActiveClients.iterator();
        while (localIterator.hasNext()) {
          ((ClientWakelockAccountant)localIterator.next()).changeConcurrentRequests(paramInt, paramLong);
        }
      }
    }
  }
  
  void dumpClientRequestTracker(PrintWriter paramPrintWriter)
  {
    paramPrintWriter.println("-------mClients---------------");
    synchronized (mClients)
    {
      Iterator localIterator = mClients.keySet().iterator();
      while (localIterator.hasNext())
      {
        String str = (String)localIterator.next();
        StringBuilder localStringBuilder = new java/lang/StringBuilder;
        localStringBuilder.<init>();
        localStringBuilder.append("Client : ");
        localStringBuilder.append(str);
        paramPrintWriter.println(localStringBuilder.toString());
        paramPrintWriter.println(((ClientWakelockAccountant)mClients.get(str)).toString());
      }
      return;
    }
  }
  
  List<ClientRequestStats> getClientRequestStats()
  {
    long l = SystemClock.uptimeMillis();
    synchronized (mClients)
    {
      ArrayList localArrayList = new java/util/ArrayList;
      localArrayList.<init>(mClients.size());
      Iterator localIterator = mClients.keySet().iterator();
      while (localIterator.hasNext())
      {
        Object localObject2 = (String)localIterator.next();
        ClientWakelockAccountant localClientWakelockAccountant = (ClientWakelockAccountant)mClients.get(localObject2);
        localClientWakelockAccountant.updatePendingRequestWakelockTime(l);
        localObject2 = new android/telephony/ClientRequestStats;
        ((ClientRequestStats)localObject2).<init>(mRequestStats);
        localArrayList.add(localObject2);
      }
      return localArrayList;
    }
  }
  
  public boolean isClientActive(String arg1)
  {
    ClientWakelockAccountant localClientWakelockAccountant = getClientWakelockAccountant(???);
    synchronized (mActiveClients)
    {
      return mActiveClients.contains(localClientWakelockAccountant);
    }
  }
  
  @VisibleForTesting
  public void startTracking(String arg1, int paramInt1, int paramInt2, int paramInt3)
  {
    ClientWakelockAccountant localClientWakelockAccountant = getClientWakelockAccountant(???);
    long l = SystemClock.uptimeMillis();
    localClientWakelockAccountant.startAttributingWakelock(paramInt1, paramInt2, paramInt3, l);
    updateConcurrentRequests(paramInt3, l);
    synchronized (mActiveClients)
    {
      if (!mActiveClients.contains(localClientWakelockAccountant)) {
        mActiveClients.add(localClientWakelockAccountant);
      }
      return;
    }
  }
  
  @VisibleForTesting
  public void stopTracking(String arg1, int paramInt1, int paramInt2, int paramInt3)
  {
    ClientWakelockAccountant localClientWakelockAccountant = getClientWakelockAccountant(???);
    long l = SystemClock.uptimeMillis();
    localClientWakelockAccountant.stopAttributingWakelock(paramInt1, paramInt2, l);
    if (localClientWakelockAccountant.getPendingRequestCount() == 0) {
      synchronized (mActiveClients)
      {
        mActiveClients.remove(localClientWakelockAccountant);
      }
    }
    updateConcurrentRequests(paramInt3, l);
  }
  
  @VisibleForTesting
  public void stopTrackingAll()
  {
    long l = SystemClock.uptimeMillis();
    synchronized (mActiveClients)
    {
      Iterator localIterator = mActiveClients.iterator();
      while (localIterator.hasNext()) {
        ((ClientWakelockAccountant)localIterator.next()).stopAllPendingRequests(l);
      }
      mActiveClients.clear();
      return;
    }
  }
}
