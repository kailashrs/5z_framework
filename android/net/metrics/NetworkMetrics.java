package android.net.metrics;

import android.net.NetworkCapabilities;
import com.android.internal.util.BitUtils;
import com.android.internal.util.TokenBucket;
import java.util.StringJoiner;

public class NetworkMetrics
{
  private static final int CONNECT_LATENCY_MAXIMUM_RECORDS = 20000;
  private static final int INITIAL_DNS_BATCH_SIZE = 100;
  public final ConnectStats connectMetrics;
  public final DnsEvent dnsMetrics;
  public final int netId;
  public Summary pendingSummary;
  public final Summary summary;
  public final long transports;
  
  public NetworkMetrics(int paramInt, long paramLong, TokenBucket paramTokenBucket)
  {
    netId = paramInt;
    transports = paramLong;
    connectMetrics = new ConnectStats(paramInt, paramLong, paramTokenBucket, 20000);
    dnsMetrics = new DnsEvent(paramInt, paramLong, 100);
    summary = new Summary(paramInt, paramLong);
  }
  
  public void addConnectResult(int paramInt1, int paramInt2, String paramString)
  {
    if (pendingSummary == null) {
      pendingSummary = new Summary(netId, transports);
    }
    boolean bool = connectMetrics.addEvent(paramInt1, paramInt2, paramString);
    paramString = pendingSummary.connectErrorRate;
    double d;
    if (bool) {
      d = 0.0D;
    } else {
      d = 1.0D;
    }
    paramString.count(d);
    if (ConnectStats.isNonBlocking(paramInt1)) {
      pendingSummary.connectLatencies.count(paramInt2);
    }
  }
  
  public void addDnsResult(int paramInt1, int paramInt2, int paramInt3)
  {
    if (pendingSummary == null) {
      pendingSummary = new Summary(netId, transports);
    }
    boolean bool = dnsMetrics.addResult((byte)paramInt1, (byte)paramInt2, paramInt3);
    pendingSummary.dnsLatencies.count(paramInt3);
    Metrics localMetrics = pendingSummary.dnsErrorRate;
    double d;
    if (bool) {
      d = 0.0D;
    } else {
      d = 1.0D;
    }
    localMetrics.count(d);
  }
  
  public void addTcpStatsResult(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    if (pendingSummary == null) {
      pendingSummary = new Summary(netId, transports);
    }
    pendingSummary.tcpLossRate.count(paramInt2, paramInt1);
    pendingSummary.roundTripTimeUs.count(paramInt3);
    pendingSummary.sentAckTimeDiffenceMs.count(paramInt4);
  }
  
  public Summary getPendingStats()
  {
    Summary localSummary = pendingSummary;
    pendingSummary = null;
    if (localSummary != null) {
      summary.merge(localSummary);
    }
    return localSummary;
  }
  
  static class Metrics
  {
    public int count;
    public double max = Double.MIN_VALUE;
    public double sum;
    
    Metrics() {}
    
    double average()
    {
      double d1 = sum / count;
      double d2 = d1;
      if (Double.isNaN(d1)) {
        d2 = 0.0D;
      }
      return d2;
    }
    
    void count(double paramDouble)
    {
      count(paramDouble, 1);
    }
    
    void count(double paramDouble, int paramInt)
    {
      count += paramInt;
      sum += paramDouble;
      max = Math.max(max, paramDouble);
    }
    
    void merge(Metrics paramMetrics)
    {
      count += count;
      sum += sum;
      max = Math.max(max, max);
    }
  }
  
  public static class Summary
  {
    public final NetworkMetrics.Metrics connectErrorRate = new NetworkMetrics.Metrics();
    public final NetworkMetrics.Metrics connectLatencies = new NetworkMetrics.Metrics();
    public final NetworkMetrics.Metrics dnsErrorRate = new NetworkMetrics.Metrics();
    public final NetworkMetrics.Metrics dnsLatencies = new NetworkMetrics.Metrics();
    public final int netId;
    public final NetworkMetrics.Metrics roundTripTimeUs = new NetworkMetrics.Metrics();
    public final NetworkMetrics.Metrics sentAckTimeDiffenceMs = new NetworkMetrics.Metrics();
    public final NetworkMetrics.Metrics tcpLossRate = new NetworkMetrics.Metrics();
    public final long transports;
    
    public Summary(int paramInt, long paramLong)
    {
      netId = paramInt;
      transports = paramLong;
    }
    
    void merge(Summary paramSummary)
    {
      dnsLatencies.merge(dnsLatencies);
      dnsErrorRate.merge(dnsErrorRate);
      connectLatencies.merge(connectLatencies);
      connectErrorRate.merge(connectErrorRate);
      tcpLossRate.merge(tcpLossRate);
    }
    
    public String toString()
    {
      StringJoiner localStringJoiner = new StringJoiner(", ", "{", "}");
      Object localObject = new StringBuilder();
      ((StringBuilder)localObject).append("netId=");
      ((StringBuilder)localObject).append(netId);
      localStringJoiner.add(((StringBuilder)localObject).toString());
      localObject = BitUtils.unpackBits(transports);
      int i = localObject.length;
      for (int j = 0; j < i; j++) {
        localStringJoiner.add(NetworkCapabilities.transportNameOf(localObject[j]));
      }
      localStringJoiner.add(String.format("dns avg=%dms max=%dms err=%.1f%% tot=%d", new Object[] { Integer.valueOf((int)dnsLatencies.average()), Integer.valueOf((int)dnsLatencies.max), Double.valueOf(dnsErrorRate.average() * 100.0D), Integer.valueOf(dnsErrorRate.count) }));
      localStringJoiner.add(String.format("connect avg=%dms max=%dms err=%.1f%% tot=%d", new Object[] { Integer.valueOf((int)connectLatencies.average()), Integer.valueOf((int)connectLatencies.max), Double.valueOf(connectErrorRate.average() * 100.0D), Integer.valueOf(connectErrorRate.count) }));
      localStringJoiner.add(String.format("tcp avg_loss=%.1f%% total_sent=%d total_lost=%d", new Object[] { Double.valueOf(100.0D * tcpLossRate.average()), Integer.valueOf(tcpLossRate.count), Integer.valueOf((int)tcpLossRate.sum) }));
      localStringJoiner.add(String.format("tcp rtt=%dms", new Object[] { Integer.valueOf((int)(roundTripTimeUs.average() / 1000.0D)) }));
      localStringJoiner.add(String.format("tcp sent-ack_diff=%dms", new Object[] { Integer.valueOf((int)sentAckTimeDiffenceMs.average()) }));
      return localStringJoiner.toString();
    }
  }
}
