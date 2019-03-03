package com.android.server.net;

import android.net.INetworkManagementEventObserver.Stub;
import android.net.LinkAddress;
import android.net.RouteInfo;

public class BaseNetworkObserver
  extends INetworkManagementEventObserver.Stub
{
  public BaseNetworkObserver() {}
  
  public void addressRemoved(String paramString, LinkAddress paramLinkAddress) {}
  
  public void addressUpdated(String paramString, LinkAddress paramLinkAddress) {}
  
  public void interfaceAdded(String paramString) {}
  
  public void interfaceClassDataActivityChanged(String paramString, boolean paramBoolean, long paramLong) {}
  
  public void interfaceDnsServerInfo(String paramString, long paramLong, String[] paramArrayOfString) {}
  
  public void interfaceLinkStateChanged(String paramString, boolean paramBoolean) {}
  
  public void interfaceRemoved(String paramString) {}
  
  public void interfaceStatusChanged(String paramString, boolean paramBoolean) {}
  
  public void limitReached(String paramString1, String paramString2) {}
  
  public void routeRemoved(RouteInfo paramRouteInfo) {}
  
  public void routeUpdated(RouteInfo paramRouteInfo) {}
}
