package com.android.internal.app;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.LocaleList;
import android.os.UserHandle;
import android.util.Slog;
import android.view.Window;

public class SuspendedAppActivity
  extends AlertActivity
  implements DialogInterface.OnClickListener
{
  public static final String EXTRA_DIALOG_MESSAGE = "SuspendedAppActivity.extra.DIALOG_MESSAGE";
  public static final String EXTRA_SUSPENDED_PACKAGE = "SuspendedAppActivity.extra.SUSPENDED_PACKAGE";
  public static final String EXTRA_SUSPENDING_PACKAGE = "SuspendedAppActivity.extra.SUSPENDING_PACKAGE";
  private static final String TAG = "SuspendedAppActivity";
  private Intent mMoreDetailsIntent;
  private PackageManager mPm;
  private int mUserId;
  
  public SuspendedAppActivity() {}
  
  public static Intent createSuspendedAppInterceptIntent(String paramString1, String paramString2, String paramString3, int paramInt)
  {
    return new Intent().setClassName("android", SuspendedAppActivity.class.getName()).putExtra("SuspendedAppActivity.extra.SUSPENDED_PACKAGE", paramString1).putExtra("SuspendedAppActivity.extra.DIALOG_MESSAGE", paramString3).putExtra("SuspendedAppActivity.extra.SUSPENDING_PACKAGE", paramString2).putExtra("android.intent.extra.USER_ID", paramInt).setFlags(276824064);
  }
  
  private CharSequence getAppLabel(String paramString)
  {
    try
    {
      CharSequence localCharSequence = mPm.getApplicationInfoAsUser(paramString, 0, mUserId).loadLabel(mPm);
      return localCharSequence;
    }
    catch (PackageManager.NameNotFoundException localNameNotFoundException)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Package ");
      localStringBuilder.append(paramString);
      localStringBuilder.append(" not found");
      Slog.e("SuspendedAppActivity", localStringBuilder.toString(), localNameNotFoundException);
    }
    return paramString;
  }
  
  private Intent getMoreDetailsActivity(String paramString1, String paramString2, int paramInt)
  {
    Intent localIntent = new Intent("android.intent.action.SHOW_SUSPENDED_APP_DETAILS").setPackage(paramString1);
    paramString1 = mPm.resolveActivityAsUser(localIntent, 0, paramInt);
    if ((paramString1 != null) && (activityInfo != null) && ("android.permission.SEND_SHOW_SUSPENDED_APP_DETAILS".equals(activityInfo.permission)))
    {
      localIntent.putExtra("android.intent.extra.PACKAGE_NAME", paramString2).setFlags(335544320);
      return localIntent;
    }
    return null;
  }
  
  public void onClick(DialogInterface paramDialogInterface, int paramInt)
  {
    if (paramInt == -3)
    {
      startActivityAsUser(mMoreDetailsIntent, UserHandle.of(mUserId));
      Slog.i("SuspendedAppActivity", "Started more details activity");
    }
    finish();
  }
  
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    mPm = getPackageManager();
    getWindow().setType(2008);
    Object localObject1 = getIntent();
    mUserId = ((Intent)localObject1).getIntExtra("android.intent.extra.USER_ID", -1);
    if (mUserId < 0)
    {
      paramBundle = new StringBuilder();
      paramBundle.append("Invalid user: ");
      paramBundle.append(mUserId);
      Slog.wtf("SuspendedAppActivity", paramBundle.toString());
      finish();
      return;
    }
    paramBundle = ((Intent)localObject1).getStringExtra("SuspendedAppActivity.extra.DIALOG_MESSAGE");
    String str = ((Intent)localObject1).getStringExtra("SuspendedAppActivity.extra.SUSPENDED_PACKAGE");
    localObject1 = ((Intent)localObject1).getStringExtra("SuspendedAppActivity.extra.SUSPENDING_PACKAGE");
    Object localObject2 = getAppLabel(str);
    if (paramBundle == null) {
      paramBundle = getString(17039497, new Object[] { localObject2, getAppLabel((String)localObject1) });
    } else {
      paramBundle = String.format(getResources().getConfiguration().getLocales().get(0), paramBundle, new Object[] { localObject2 });
    }
    localObject2 = mAlertParams;
    mTitle = getString(17039499);
    mMessage = paramBundle;
    mPositiveButtonText = getString(17039370);
    mMoreDetailsIntent = getMoreDetailsActivity((String)localObject1, str, mUserId);
    if (mMoreDetailsIntent != null) {
      mNeutralButtonText = getString(17039498);
    }
    mNeutralButtonListener = this;
    mPositiveButtonListener = this;
    setupAlert();
  }
}
