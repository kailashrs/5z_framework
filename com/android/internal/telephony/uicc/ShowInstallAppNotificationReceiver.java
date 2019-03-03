package com.android.internal.telephony.uicc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ShowInstallAppNotificationReceiver
  extends BroadcastReceiver
{
  private static final String EXTRA_PACKAGE_NAME = "package_name";
  
  public ShowInstallAppNotificationReceiver() {}
  
  public static Intent get(Context paramContext, String paramString)
  {
    paramContext = new Intent(paramContext, ShowInstallAppNotificationReceiver.class);
    paramContext.putExtra("package_name", paramString);
    return paramContext;
  }
  
  public void onReceive(Context paramContext, Intent paramIntent)
  {
    paramIntent = paramIntent.getStringExtra("package_name");
    if (!UiccProfile.isPackageInstalled(paramContext, paramIntent))
    {
      InstallCarrierAppUtils.showNotification(paramContext, paramIntent);
      InstallCarrierAppUtils.registerPackageInstallReceiver(paramContext);
    }
  }
}
