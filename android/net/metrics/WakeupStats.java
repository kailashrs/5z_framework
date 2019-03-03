package android.net.metrics;

import android.net.MacAddress;
import android.os.SystemClock;
import android.util.SparseIntArray;
import java.util.StringJoiner;

public class WakeupStats
{
  private static final int NO_UID = -1;
  public long applicationWakeups = 0L;
  public final long creationTimeMs = SystemClock.elapsedRealtime();
  public long durationSec = 0L;
  public final SparseIntArray ethertypes = new SparseIntArray();
  public final String iface;
  public final SparseIntArray ipNextHeaders = new SparseIntArray();
  public long l2BroadcastCount = 0L;
  public long l2MulticastCount = 0L;
  public long l2UnicastCount = 0L;
  public long noUidWakeups = 0L;
  public long nonApplicationWakeups = 0L;
  public long rootWakeups = 0L;
  public long systemWakeups = 0L;
  public long totalWakeups = 0L;
  
  public WakeupStats(String paramString)
  {
    iface = paramString;
  }
  
  private static void increment(SparseIntArray paramSparseIntArray, int paramInt)
  {
    paramSparseIntArray.put(paramInt, paramSparseIntArray.get(paramInt, 0) + 1);
  }
  
  public void countEvent(WakeupEvent paramWakeupEvent)
  {
    totalWakeups += 1L;
    int i = uid;
    if (i != 1000) {
      switch (i)
      {
      default: 
        if (uid >= 10000) {
          applicationWakeups += 1L;
        } else {
          nonApplicationWakeups += 1L;
        }
        break;
      case 0: 
        rootWakeups += 1L;
        break;
      case -1: 
        noUidWakeups += 1L;
        break;
      }
    } else {
      systemWakeups += 1L;
    }
    switch (dstHwAddr.getAddressType())
    {
    default: 
      break;
    case 3: 
      l2BroadcastCount += 1L;
      break;
    case 2: 
      l2MulticastCount += 1L;
      break;
    case 1: 
      l2UnicastCount += 1L;
    }
    increment(ethertypes, ethertype);
    if (ipNextHeader >= 0) {
      increment(ipNextHeaders, ipNextHeader);
    }
  }
  
  public String toString()
  {
    updateDuration();
    StringJoiner localStringJoiner = new StringJoiner(", ", "WakeupStats(", ")");
    localStringJoiner.add(iface);
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("");
    localStringBuilder.append(durationSec);
    localStringBuilder.append("s");
    localStringJoiner.add(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append("total: ");
    localStringBuilder.append(totalWakeups);
    localStringJoiner.add(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append("root: ");
    localStringBuilder.append(rootWakeups);
    localStringJoiner.add(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append("system: ");
    localStringBuilder.append(systemWakeups);
    localStringJoiner.add(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append("apps: ");
    localStringBuilder.append(applicationWakeups);
    localStringJoiner.add(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append("non-apps: ");
    localStringBuilder.append(nonApplicationWakeups);
    localStringJoiner.add(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append("no uid: ");
    localStringBuilder.append(noUidWakeups);
    localStringJoiner.add(localStringBuilder.toString());
    localStringJoiner.add(String.format("l2 unicast/multicast/broadcast: %d/%d/%d", new Object[] { Long.valueOf(l2UnicastCount), Long.valueOf(l2MulticastCount), Long.valueOf(l2BroadcastCount) }));
    for (int i = 0; i < ethertypes.size(); i++) {
      localStringJoiner.add(String.format("ethertype 0x%x: %d", new Object[] { Integer.valueOf(ethertypes.keyAt(i)), Integer.valueOf(ethertypes.valueAt(i)) }));
    }
    for (i = 0; i < ipNextHeaders.size(); i++) {
      localStringJoiner.add(String.format("ipNxtHdr %d: %d", new Object[] { Integer.valueOf(ipNextHeaders.keyAt(i)), Integer.valueOf(ipNextHeaders.valueAt(i)) }));
    }
    return localStringJoiner.toString();
  }
  
  public void updateDuration()
  {
    durationSec = ((SystemClock.elapsedRealtime() - creationTimeMs) / 1000L);
  }
}
