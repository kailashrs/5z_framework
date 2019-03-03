package com.android.internal.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.IPowerManager;
import android.os.IPowerManager.Stub;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Slog;

public class ShutdownActivity
  extends Activity
{
  private static final String TAG = "ShutdownActivity";
  private boolean mConfirm;
  private boolean mReboot;
  private boolean mUserRequested;
  
  public ShutdownActivity() {}
  
  protected void onCreate(final Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    paramBundle = getIntent();
    mReboot = "android.intent.action.REBOOT".equals(paramBundle.getAction());
    mConfirm = paramBundle.getBooleanExtra("android.intent.extra.KEY_CONFIRM", false);
    mUserRequested = paramBundle.getBooleanExtra("android.intent.extra.USER_REQUESTED_SHUTDOWN", false);
    if (mUserRequested) {
      paramBundle = "userrequested";
    } else {
      paramBundle = paramBundle.getStringExtra("android.intent.extra.REASON");
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("onCreate(): confirm=");
    localStringBuilder.append(mConfirm);
    Slog.i("ShutdownActivity", localStringBuilder.toString());
    paramBundle = new Thread("ShutdownActivity")
    {
      public void run()
      {
        IPowerManager localIPowerManager = IPowerManager.Stub.asInterface(ServiceManager.getService("power"));
        try
        {
          if (mReboot) {
            localIPowerManager.reboot(mConfirm, null, false);
          } else {
            localIPowerManager.shutdown(mConfirm, paramBundle, false);
          }
        }
        catch (RemoteException localRemoteException) {}
      }
    };
    paramBundle.start();
    finish();
    try
    {
      paramBundle.join();
    }
    catch (InterruptedException paramBundle) {}
  }
}
