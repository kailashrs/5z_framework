package com.android.server.net;

import java.net.InetAddress;

class DnsServerEntry
  implements Comparable<DnsServerEntry>
{
  public final InetAddress address;
  public long expiry;
  
  public DnsServerEntry(InetAddress paramInetAddress, long paramLong)
    throws IllegalArgumentException
  {
    address = paramInetAddress;
    expiry = paramLong;
  }
  
  public int compareTo(DnsServerEntry paramDnsServerEntry)
  {
    return Long.compare(expiry, expiry);
  }
}
