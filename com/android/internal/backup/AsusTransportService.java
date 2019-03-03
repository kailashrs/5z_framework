package com.android.internal.backup;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AsusTransportService
  extends Service
{
  private static AsusTransport sTransport = null;
  
  public AsusTransportService() {}
  
  public IBinder onBind(Intent paramIntent)
  {
    return sTransport.getBinder();
  }
  
  public void onCreate()
  {
    if (sTransport == null) {
      sTransport = new AsusTransport(this);
    }
  }
}
