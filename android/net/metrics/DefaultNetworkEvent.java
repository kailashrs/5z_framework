package android.net.metrics;

import android.net.NetworkCapabilities;
import com.android.internal.util.BitUtils;
import java.util.StringJoiner;

public class DefaultNetworkEvent
{
  public final long creationTimeMs;
  public long durationMs;
  public int finalScore;
  public int initialScore;
  public boolean ipv4;
  public boolean ipv6;
  public int netId = 0;
  public int previousTransports;
  public int transports;
  public long validatedMs;
  
  public DefaultNetworkEvent(long paramLong)
  {
    creationTimeMs = paramLong;
  }
  
  private String ipSupport()
  {
    if ((ipv4) && (ipv6)) {
      return "IPv4v6";
    }
    if (ipv6) {
      return "IPv6";
    }
    if (ipv4) {
      return "IPv4";
    }
    return "NONE";
  }
  
  public String toString()
  {
    StringJoiner localStringJoiner = new StringJoiner(", ", "DefaultNetworkEvent(", ")");
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("netId=");
    ((StringBuilder)localObject).append(netId);
    localStringJoiner.add(((StringBuilder)localObject).toString());
    localObject = BitUtils.unpackBits(transports);
    int i = localObject.length;
    for (int j = 0; j < i; j++) {
      localStringJoiner.add(NetworkCapabilities.transportNameOf(localObject[j]));
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("ip=");
    ((StringBuilder)localObject).append(ipSupport());
    localStringJoiner.add(((StringBuilder)localObject).toString());
    if (initialScore > 0)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("initial_score=");
      ((StringBuilder)localObject).append(initialScore);
      localStringJoiner.add(((StringBuilder)localObject).toString());
    }
    if (finalScore > 0)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("final_score=");
      ((StringBuilder)localObject).append(finalScore);
      localStringJoiner.add(((StringBuilder)localObject).toString());
    }
    localStringJoiner.add(String.format("duration=%.0fs", new Object[] { Double.valueOf(durationMs / 1000.0D) }));
    localStringJoiner.add(String.format("validation=%04.1f%%", new Object[] { Double.valueOf(validatedMs * 100.0D / durationMs) }));
    return localStringJoiner.toString();
  }
  
  public void updateDuration(long paramLong)
  {
    durationMs = (paramLong - creationTimeMs);
  }
}
