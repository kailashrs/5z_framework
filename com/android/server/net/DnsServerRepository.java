package com.android.server.net;

import android.net.LinkProperties;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

class DnsServerRepository
{
  public static final int NUM_CURRENT_SERVERS = 3;
  public static final int NUM_SERVERS = 12;
  public static final String TAG = "DnsServerRepository";
  private ArrayList<DnsServerEntry> mAllServers = new ArrayList(12);
  private Set<InetAddress> mCurrentServers = new HashSet();
  private HashMap<InetAddress, DnsServerEntry> mIndex = new HashMap(12);
  
  public DnsServerRepository() {}
  
  private boolean updateCurrentServers()
  {
    try
    {
      long l = System.currentTimeMillis();
      boolean bool1 = false;
      for (int i = mAllServers.size() - 1; (i >= 0) && ((i >= 12) || (mAllServers.get(i)).expiry < l)); i--)
      {
        localObject1 = (DnsServerEntry)mAllServers.remove(i);
        mIndex.remove(address);
        bool1 |= mCurrentServers.remove(address);
      }
      Object localObject1 = mAllServers.iterator();
      while (((Iterator)localObject1).hasNext())
      {
        DnsServerEntry localDnsServerEntry = (DnsServerEntry)((Iterator)localObject1).next();
        if (mCurrentServers.size() >= 3) {
          break;
        }
        boolean bool2 = mCurrentServers.add(address);
        bool1 |= bool2;
      }
      return bool1;
    }
    finally {}
  }
  
  private boolean updateExistingEntry(InetAddress paramInetAddress, long paramLong)
  {
    try
    {
      paramInetAddress = (DnsServerEntry)mIndex.get(paramInetAddress);
      if (paramInetAddress != null)
      {
        expiry = paramLong;
        return true;
      }
      return false;
    }
    finally {}
  }
  
  public boolean addServers(long paramLong, String[] paramArrayOfString)
  {
    try
    {
      long l = System.currentTimeMillis();
      paramLong = 1000L * paramLong + l;
      int i = paramArrayOfString.length;
      for (int j = 0; j < i; j++)
      {
        Object localObject = paramArrayOfString[j];
        try
        {
          InetAddress localInetAddress = InetAddress.parseNumericAddress((String)localObject);
          if ((!updateExistingEntry(localInetAddress, paramLong)) && (paramLong > l))
          {
            localObject = new com/android/server/net/DnsServerEntry;
            ((DnsServerEntry)localObject).<init>(localInetAddress, paramLong);
            mAllServers.add(localObject);
            mIndex.put(localInetAddress, localObject);
          }
        }
        catch (IllegalArgumentException localIllegalArgumentException) {}
      }
      Collections.sort(mAllServers);
      boolean bool = updateCurrentServers();
      return bool;
    }
    finally {}
  }
  
  public void setDnsServersOn(LinkProperties paramLinkProperties)
  {
    try
    {
      paramLinkProperties.setDnsServers(mCurrentServers);
      return;
    }
    finally
    {
      paramLinkProperties = finally;
      throw paramLinkProperties;
    }
  }
}
