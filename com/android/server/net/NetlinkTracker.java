package com.android.server.net;

import android.net.LinkAddress;
import android.net.LinkProperties;
import android.net.RouteInfo;
import java.util.Arrays;

public class NetlinkTracker
  extends BaseNetworkObserver
{
  private static final boolean DBG = false;
  private final String TAG;
  private final Callback mCallback;
  private DnsServerRepository mDnsServerRepository;
  private final String mInterfaceName;
  private final LinkProperties mLinkProperties;
  
  public NetlinkTracker(String paramString, Callback paramCallback)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("NetlinkTracker/");
    localStringBuilder.append(paramString);
    TAG = localStringBuilder.toString();
    mInterfaceName = paramString;
    mCallback = paramCallback;
    mLinkProperties = new LinkProperties();
    mLinkProperties.setInterfaceName(mInterfaceName);
    mDnsServerRepository = new DnsServerRepository();
  }
  
  private void maybeLog(String paramString, Object paramObject) {}
  
  private void maybeLog(String paramString1, String paramString2, LinkAddress paramLinkAddress) {}
  
  public void addressRemoved(String paramString, LinkAddress paramLinkAddress)
  {
    if (mInterfaceName.equals(paramString))
    {
      maybeLog("addressRemoved", paramString, paramLinkAddress);
      try
      {
        boolean bool = mLinkProperties.removeLinkAddress(paramLinkAddress);
        if (bool) {
          mCallback.update();
        }
      }
      finally {}
    }
  }
  
  public void addressUpdated(String paramString, LinkAddress paramLinkAddress)
  {
    if (mInterfaceName.equals(paramString))
    {
      maybeLog("addressUpdated", paramString, paramLinkAddress);
      try
      {
        boolean bool = mLinkProperties.addLinkAddress(paramLinkAddress);
        if (bool) {
          mCallback.update();
        }
      }
      finally {}
    }
  }
  
  public void clearLinkProperties()
  {
    try
    {
      DnsServerRepository localDnsServerRepository = new com/android/server/net/DnsServerRepository;
      localDnsServerRepository.<init>();
      mDnsServerRepository = localDnsServerRepository;
      mLinkProperties.clear();
      mLinkProperties.setInterfaceName(mInterfaceName);
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public LinkProperties getLinkProperties()
  {
    try
    {
      LinkProperties localLinkProperties = new LinkProperties(mLinkProperties);
      return localLinkProperties;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public void interfaceDnsServerInfo(String paramString, long paramLong, String[] paramArrayOfString)
  {
    if (mInterfaceName.equals(paramString))
    {
      maybeLog("interfaceDnsServerInfo", Arrays.toString(paramArrayOfString));
      if (mDnsServerRepository.addServers(paramLong, paramArrayOfString)) {
        try
        {
          mDnsServerRepository.setDnsServersOn(mLinkProperties);
          mCallback.update();
        }
        finally {}
      }
    }
  }
  
  public void interfaceRemoved(String paramString)
  {
    maybeLog("interfaceRemoved", paramString);
    if (mInterfaceName.equals(paramString))
    {
      clearLinkProperties();
      mCallback.update();
    }
  }
  
  public void routeRemoved(RouteInfo paramRouteInfo)
  {
    if (mInterfaceName.equals(paramRouteInfo.getInterface()))
    {
      maybeLog("routeRemoved", paramRouteInfo);
      try
      {
        boolean bool = mLinkProperties.removeRoute(paramRouteInfo);
        if (bool) {
          mCallback.update();
        }
      }
      finally {}
    }
  }
  
  public void routeUpdated(RouteInfo paramRouteInfo)
  {
    if (mInterfaceName.equals(paramRouteInfo.getInterface()))
    {
      maybeLog("routeUpdated", paramRouteInfo);
      try
      {
        boolean bool = mLinkProperties.addRoute(paramRouteInfo);
        if (bool) {
          mCallback.update();
        }
      }
      finally {}
    }
  }
  
  public static abstract interface Callback
  {
    public abstract void update();
  }
}
