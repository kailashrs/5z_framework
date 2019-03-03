package com.android.internal.telephony.uicc;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.provider.Settings.Global;
import android.text.TextUtils;
import android.util.Log;
import java.util.concurrent.TimeUnit;

public class InstallCarrierAppTrampolineActivity
  extends Activity
{
  private static final String BUNDLE_KEY_PACKAGE_NAME = "package_name";
  private static final String CARRIER_NAME = "carrier_name";
  private static final int DOWNLOAD_RESULT = 2;
  private static final int INSTALL_CARRIER_APP_DIALOG_REQUEST = 1;
  private static final String LOG_TAG = "CarrierAppInstall";
  private String mPackageName;
  
  public InstallCarrierAppTrampolineActivity() {}
  
  private void finishNoAnimation()
  {
    finish();
    overridePendingTransition(0, 0);
  }
  
  public static Intent get(Context paramContext, String paramString)
  {
    paramContext = new Intent(paramContext, InstallCarrierAppTrampolineActivity.class);
    paramContext.putExtra("package_name", paramString);
    return paramContext;
  }
  
  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
    if (paramInt1 == 1)
    {
      if (paramInt2 == 2) {
        startActivity(InstallCarrierAppUtils.getPlayStoreIntent(mPackageName));
      }
      finishNoAnimation();
    }
  }
  
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    Intent localIntent = getIntent();
    if (localIntent != null) {
      mPackageName = localIntent.getStringExtra("package_name");
    }
    if (paramBundle == null)
    {
      long l = Settings.Global.getLong(getContentResolver(), "install_carrier_app_notification_sleep_millis", TimeUnit.HOURS.toMillis(24L));
      paramBundle = new StringBuilder();
      paramBundle.append("Sleeping carrier app install notification for : ");
      paramBundle.append(l);
      paramBundle.append(" millis");
      Log.d("CarrierAppInstall", paramBundle.toString());
      InstallCarrierAppUtils.showNotificationIfNotInstalledDelayed(this, mPackageName, l);
    }
    localIntent = new Intent();
    localIntent.setComponent(ComponentName.unflattenFromString(Resources.getSystem().getString(17039673)));
    paramBundle = InstallCarrierAppUtils.getAppNameFromPackageName(this, mPackageName);
    if (!TextUtils.isEmpty(paramBundle)) {
      localIntent.putExtra("carrier_name", paramBundle);
    }
    if (localIntent.resolveActivity(getPackageManager()) == null)
    {
      Log.d("CarrierAppInstall", "Could not resolve activity for installing the carrier app");
      finishNoAnimation();
    }
    else
    {
      startActivityForResult(localIntent, 1);
    }
  }
}
