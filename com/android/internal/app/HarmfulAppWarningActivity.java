package com.android.internal.app;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentSender;
import android.content.IntentSender.SendIntentException;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class HarmfulAppWarningActivity
  extends AlertActivity
  implements DialogInterface.OnClickListener
{
  private static final String EXTRA_HARMFUL_APP_WARNING = "harmful_app_warning";
  private static final String TAG = HarmfulAppWarningActivity.class.getSimpleName();
  private String mHarmfulAppWarning;
  private String mPackageName;
  private IntentSender mTarget;
  
  public HarmfulAppWarningActivity() {}
  
  public static Intent createHarmfulAppWarningIntent(Context paramContext, String paramString, IntentSender paramIntentSender, CharSequence paramCharSequence)
  {
    Intent localIntent = new Intent();
    localIntent.setClass(paramContext, HarmfulAppWarningActivity.class);
    localIntent.putExtra("android.intent.extra.PACKAGE_NAME", paramString);
    localIntent.putExtra("android.intent.extra.INTENT", paramIntentSender);
    localIntent.putExtra("harmful_app_warning", paramCharSequence);
    return localIntent;
  }
  
  private View createView(ApplicationInfo paramApplicationInfo)
  {
    View localView = getLayoutInflater().inflate(17367181, null);
    ((TextView)localView.findViewById(16908744)).setText(paramApplicationInfo.loadSafeLabel(getPackageManager()));
    ((TextView)localView.findViewById(16908299)).setText(mHarmfulAppWarning);
    return localView;
  }
  
  public void onClick(DialogInterface paramDialogInterface, int paramInt)
  {
    switch (paramInt)
    {
    default: 
      break;
    case -1: 
      getPackageManager().deletePackage(mPackageName, null, 0);
      EventLogTags.writeHarmfulAppWarningUninstall(mPackageName);
      finish();
      break;
    case -2: 
      getPackageManager().setHarmfulAppWarning(mPackageName, null);
      paramDialogInterface = (IntentSender)getIntent().getParcelableExtra("android.intent.extra.INTENT");
      try
      {
        startIntentSenderForResult(paramDialogInterface, -1, null, 0, 0, 0);
      }
      catch (IntentSender.SendIntentException paramDialogInterface)
      {
        Log.e(TAG, "Error while starting intent sender", paramDialogInterface);
      }
      EventLogTags.writeHarmfulAppWarningLaunchAnyway(mPackageName);
      finish();
    }
  }
  
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    Intent localIntent = getIntent();
    mPackageName = localIntent.getStringExtra("android.intent.extra.PACKAGE_NAME");
    mTarget = ((IntentSender)localIntent.getParcelableExtra("android.intent.extra.INTENT"));
    mHarmfulAppWarning = localIntent.getStringExtra("harmful_app_warning");
    Object localObject;
    if ((mPackageName == null) || (mTarget == null) || (mHarmfulAppWarning == null))
    {
      localObject = TAG;
      paramBundle = new StringBuilder();
      paramBundle.append("Invalid intent: ");
      paramBundle.append(localIntent.toString());
      Log.wtf((String)localObject, paramBundle.toString());
      finish();
    }
    try
    {
      paramBundle = getPackageManager().getApplicationInfo(mPackageName, 0);
      localObject = mAlertParams;
      mTitle = getString(17040082);
      mView = createView(paramBundle);
      mPositiveButtonText = getString(17040083);
      mPositiveButtonListener = this;
      mNegativeButtonText = getString(17040081);
      mNegativeButtonListener = this;
      mAlert.installContent(mAlertParams);
      return;
    }
    catch (PackageManager.NameNotFoundException paramBundle)
    {
      Log.e(TAG, "Could not show warning because package does not exist ", paramBundle);
      finish();
    }
  }
}
