package com.android.server.net;

import android.net.INetdEventCallback.Stub;

public class BaseNetdEventCallback
  extends INetdEventCallback.Stub
{
  public BaseNetdEventCallback() {}
  
  public void onConnectEvent(String paramString, int paramInt1, long paramLong, int paramInt2) {}
  
  public void onDnsEvent(String paramString, String[] paramArrayOfString, int paramInt1, long paramLong, int paramInt2) {}
  
  public void onPrivateDnsValidationEvent(int paramInt, String paramString1, String paramString2, boolean paramBoolean) {}
}
