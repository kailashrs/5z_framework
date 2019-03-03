package android.net.metrics;

import android.net.NetworkCapabilities;
import android.system.OsConstants;
import android.util.IntArray;
import android.util.SparseIntArray;
import com.android.internal.util.BitUtils;
import com.android.internal.util.TokenBucket;

public class ConnectStats
{
  private static final int EALREADY = OsConstants.EALREADY;
  private static final int EINPROGRESS = OsConstants.EINPROGRESS;
  public int connectBlockingCount = 0;
  public int connectCount = 0;
  public final SparseIntArray errnos = new SparseIntArray();
  public int eventCount = 0;
  public int ipv6ConnectCount = 0;
  public final IntArray latencies = new IntArray();
  public final TokenBucket mLatencyTb;
  public final int mMaxLatencyRecords;
  public final int netId;
  public final long transports;
  
  public ConnectStats(int paramInt1, long paramLong, TokenBucket paramTokenBucket, int paramInt2)
  {
    netId = paramInt1;
    transports = paramLong;
    mLatencyTb = paramTokenBucket;
    mMaxLatencyRecords = paramInt2;
  }
  
  private void countConnect(int paramInt, String paramString)
  {
    connectCount += 1;
    if (!isNonBlocking(paramInt)) {
      connectBlockingCount += 1;
    }
    if (isIPv6(paramString)) {
      ipv6ConnectCount += 1;
    }
  }
  
  private void countError(int paramInt)
  {
    int i = errnos.get(paramInt, 0);
    errnos.put(paramInt, i + 1);
  }
  
  private void countLatency(int paramInt1, int paramInt2)
  {
    if (isNonBlocking(paramInt1)) {
      return;
    }
    if (!mLatencyTb.get()) {
      return;
    }
    if (latencies.size() >= mMaxLatencyRecords) {
      return;
    }
    latencies.add(paramInt2);
  }
  
  private static boolean isIPv6(String paramString)
  {
    return paramString.contains(":");
  }
  
  static boolean isNonBlocking(int paramInt)
  {
    boolean bool;
    if ((paramInt != EINPROGRESS) && (paramInt != EALREADY)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  private static boolean isSuccess(int paramInt)
  {
    boolean bool;
    if ((paramInt != 0) && (!isNonBlocking(paramInt))) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  boolean addEvent(int paramInt1, int paramInt2, String paramString)
  {
    eventCount += 1;
    if (isSuccess(paramInt1))
    {
      countConnect(paramInt1, paramString);
      countLatency(paramInt1, paramInt2);
      return true;
    }
    countError(paramInt1);
    return false;
  }
  
  public String toString()
  {
    Object localObject = new StringBuilder("ConnectStats(");
    ((StringBuilder)localObject).append("netId=");
    ((StringBuilder)localObject).append(netId);
    StringBuilder localStringBuilder = ((StringBuilder)localObject).append(", ");
    localObject = BitUtils.unpackBits(transports);
    int i = localObject.length;
    for (int j = 0; j < i; j++)
    {
      localStringBuilder.append(NetworkCapabilities.transportNameOf(localObject[j]));
      localStringBuilder.append(", ");
    }
    localStringBuilder.append(String.format("%d events, ", new Object[] { Integer.valueOf(eventCount) }));
    localStringBuilder.append(String.format("%d success, ", new Object[] { Integer.valueOf(connectCount) }));
    localStringBuilder.append(String.format("%d blocking, ", new Object[] { Integer.valueOf(connectBlockingCount) }));
    localStringBuilder.append(String.format("%d IPv6 dst", new Object[] { Integer.valueOf(ipv6ConnectCount) }));
    for (j = 0; j < errnos.size(); j++) {
      localStringBuilder.append(String.format(", %s: %d", new Object[] { OsConstants.errnoName(errnos.keyAt(j)), Integer.valueOf(errnos.valueAt(j)) }));
    }
    localStringBuilder.append(")");
    return localStringBuilder.toString();
  }
}
