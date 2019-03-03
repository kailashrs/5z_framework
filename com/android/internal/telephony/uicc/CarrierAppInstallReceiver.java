package com.android.internal.telephony.uicc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class CarrierAppInstallReceiver
  extends BroadcastReceiver
{
  private static final String LOG_TAG = "CarrierAppInstall";
  
  public CarrierAppInstallReceiver() {}
  
  public void onReceive(Context paramContext, Intent paramIntent)
  {
    if ("android.intent.action.PACKAGE_ADDED".equals(paramIntent.getAction()))
    {
      Log.d("CarrierAppInstall", "Received package install intent");
      paramIntent = paramIntent.getData().getSchemeSpecificPart();
      if (TextUtils.isEmpty(paramIntent))
      {
        Log.w("CarrierAppInstall", "Package is empty, ignoring");
        return;
      }
      InstallCarrierAppUtils.hideNotification(paramContext, paramIntent);
      if (!InstallCarrierAppUtils.isPackageInstallNotificationActive(paramContext)) {
        InstallCarrierAppUtils.unregisterPackageInstallReceiver(paramContext);
      }
    }
  }
}
