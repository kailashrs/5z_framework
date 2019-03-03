package android.net.metrics;

import android.net.MacAddress;
import java.util.StringJoiner;

public class WakeupEvent
{
  public MacAddress dstHwAddr;
  public String dstIp;
  public int dstPort;
  public int ethertype;
  public String iface;
  public int ipNextHeader;
  public String srcIp;
  public int srcPort;
  public long timestampMs;
  public int uid;
  
  public WakeupEvent() {}
  
  public String toString()
  {
    StringJoiner localStringJoiner = new StringJoiner(", ", "WakeupEvent(", ")");
    localStringJoiner.add(String.format("%tT.%tL", new Object[] { Long.valueOf(timestampMs), Long.valueOf(timestampMs) }));
    localStringJoiner.add(iface);
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("uid: ");
    localStringBuilder.append(Integer.toString(uid));
    localStringJoiner.add(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append("eth=0x");
    localStringBuilder.append(Integer.toHexString(ethertype));
    localStringJoiner.add(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append("dstHw=");
    localStringBuilder.append(dstHwAddr);
    localStringJoiner.add(localStringBuilder.toString());
    if (ipNextHeader > 0)
    {
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("ipNxtHdr=");
      localStringBuilder.append(ipNextHeader);
      localStringJoiner.add(localStringBuilder.toString());
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("srcIp=");
      localStringBuilder.append(srcIp);
      localStringJoiner.add(localStringBuilder.toString());
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("dstIp=");
      localStringBuilder.append(dstIp);
      localStringJoiner.add(localStringBuilder.toString());
      if (srcPort > -1)
      {
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("srcPort=");
        localStringBuilder.append(srcPort);
        localStringJoiner.add(localStringBuilder.toString());
      }
      if (dstPort > -1)
      {
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("dstPort=");
        localStringBuilder.append(dstPort);
        localStringJoiner.add(localStringBuilder.toString());
      }
    }
    return localStringJoiner.toString();
  }
}
