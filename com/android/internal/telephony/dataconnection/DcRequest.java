package com.android.internal.telephony.dataconnection;

import android.content.Context;
import android.content.res.Resources;
import android.net.NetworkCapabilities;
import android.net.NetworkConfig;
import android.net.NetworkRequest;
import android.telephony.Rlog;
import java.util.HashMap;

public class DcRequest
  implements Comparable<DcRequest>
{
  private static final String LOG_TAG = "DcRequest";
  private static final HashMap<Integer, Integer> sApnPriorityMap = new HashMap();
  public final int apnId;
  public final NetworkRequest networkRequest;
  public final int priority;
  
  public DcRequest(NetworkRequest paramNetworkRequest, Context paramContext)
  {
    initApnPriorities(paramContext);
    networkRequest = paramNetworkRequest;
    apnId = apnIdForNetworkRequest(networkRequest);
    priority = priorityForApnId(apnId);
  }
  
  private int apnIdForNetworkRequest(NetworkRequest paramNetworkRequest)
  {
    Object localObject = networkCapabilities;
    if ((((NetworkCapabilities)localObject).getTransportTypes().length > 0) && (!((NetworkCapabilities)localObject).hasTransport(0))) {
      return -1;
    }
    int i = -1;
    int j = 0;
    int k = 0;
    if (((NetworkCapabilities)localObject).hasCapability(12))
    {
      j = k;
      if (-1 != -1) {
        j = 1;
      }
      i = 0;
    }
    int m = i;
    k = j;
    if (((NetworkCapabilities)localObject).hasCapability(0))
    {
      if (i != -1) {
        j = 1;
      }
      m = 1;
      k = j;
    }
    i = m;
    j = k;
    if (((NetworkCapabilities)localObject).hasCapability(1))
    {
      j = k;
      if (m != -1) {
        j = 1;
      }
      i = 2;
    }
    m = i;
    k = j;
    if (((NetworkCapabilities)localObject).hasCapability(2))
    {
      if (i != -1) {
        j = 1;
      }
      m = 3;
      k = j;
    }
    i = m;
    j = k;
    if (((NetworkCapabilities)localObject).hasCapability(3))
    {
      j = k;
      if (m != -1) {
        j = 1;
      }
      i = 6;
    }
    m = i;
    k = j;
    if (((NetworkCapabilities)localObject).hasCapability(4))
    {
      if (i != -1) {
        j = 1;
      }
      m = 5;
      k = j;
    }
    i = m;
    j = k;
    if (((NetworkCapabilities)localObject).hasCapability(5))
    {
      j = k;
      if (m != -1) {
        j = 1;
      }
      i = 7;
    }
    int n = i;
    k = j;
    if (((NetworkCapabilities)localObject).hasCapability(7))
    {
      if (i != -1) {
        j = 1;
      }
      n = 8;
      k = j;
    }
    m = n;
    j = k;
    if (((NetworkCapabilities)localObject).hasCapability(8))
    {
      j = k;
      if (n != -1) {
        j = 1;
      }
      m = -1;
      loge("RCS APN type not yet supported");
    }
    i = m;
    k = j;
    if (((NetworkCapabilities)localObject).hasCapability(9))
    {
      if (m != -1) {
        j = 1;
      }
      i = -1;
      loge("XCAP APN type not yet supported");
      k = j;
    }
    m = i;
    j = k;
    if (((NetworkCapabilities)localObject).hasCapability(10))
    {
      if (i != -1) {
        k = 1;
      }
      m = 9;
      j = k;
    }
    if (j != 0) {
      loge("Multiple apn types specified in request - result is unspecified!");
    }
    if (m == -1)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Unsupported NetworkRequest in Telephony: nr=");
      ((StringBuilder)localObject).append(paramNetworkRequest);
      loge(((StringBuilder)localObject).toString());
    }
    return m;
  }
  
  private void initApnPriorities(Context paramContext)
  {
    synchronized (sApnPriorityMap)
    {
      if (sApnPriorityMap.isEmpty()) {
        for (String str : paramContext.getResources().getStringArray(17236068))
        {
          NetworkConfig localNetworkConfig = new android/net/NetworkConfig;
          localNetworkConfig.<init>(str);
          int k = ApnContext.apnIdForType(type);
          sApnPriorityMap.put(Integer.valueOf(k), Integer.valueOf(priority));
        }
      }
      return;
    }
  }
  
  private void loge(String paramString)
  {
    Rlog.e("DcRequest", paramString);
  }
  
  private int priorityForApnId(int paramInt)
  {
    Integer localInteger = (Integer)sApnPriorityMap.get(Integer.valueOf(paramInt));
    if (localInteger != null) {
      paramInt = localInteger.intValue();
    } else {
      paramInt = 0;
    }
    return paramInt;
  }
  
  public int compareTo(DcRequest paramDcRequest)
  {
    return priority - priority;
  }
  
  public boolean equals(Object paramObject)
  {
    if ((paramObject instanceof DcRequest)) {
      return networkRequest.equals(networkRequest);
    }
    return false;
  }
  
  public int hashCode()
  {
    return networkRequest.hashCode();
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(networkRequest.toString());
    localStringBuilder.append(", priority=");
    localStringBuilder.append(priority);
    localStringBuilder.append(", apnId=");
    localStringBuilder.append(apnId);
    return localStringBuilder.toString();
  }
}
