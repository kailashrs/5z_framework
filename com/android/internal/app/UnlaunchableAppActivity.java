package com.android.internal.app;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.IntentSender;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.UserHandle;
import android.os.UserManager;
import android.util.Log;

public class UnlaunchableAppActivity
  extends Activity
  implements DialogInterface.OnDismissListener, DialogInterface.OnClickListener
{
  private static final String EXTRA_UNLAUNCHABLE_REASON = "unlaunchable_reason";
  private static final String TAG = "UnlaunchableAppActivity";
  private static final int UNLAUNCHABLE_REASON_QUIET_MODE = 1;
  private int mReason;
  private IntentSender mTarget;
  private int mUserId;
  
  public UnlaunchableAppActivity() {}
  
  private static final Intent createBaseIntent()
  {
    Intent localIntent = new Intent();
    localIntent.setComponent(new ComponentName("android", UnlaunchableAppActivity.class.getName()));
    localIntent.setFlags(276824064);
    return localIntent;
  }
  
  public static Intent createInQuietModeDialogIntent(int paramInt)
  {
    Intent localIntent = createBaseIntent();
    localIntent.putExtra("unlaunchable_reason", 1);
    localIntent.putExtra("android.intent.extra.user_handle", paramInt);
    return localIntent;
  }
  
  public static Intent createInQuietModeDialogIntent(int paramInt, IntentSender paramIntentSender)
  {
    Intent localIntent = createInQuietModeDialogIntent(paramInt);
    localIntent.putExtra("android.intent.extra.INTENT", paramIntentSender);
    return localIntent;
  }
  
  public void onClick(DialogInterface paramDialogInterface, int paramInt)
  {
    if ((mReason == 1) && (paramInt == -1)) {
      UserManager.get(this).requestQuietModeEnabled(false, UserHandle.of(mUserId), mTarget);
    }
  }
  
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    requestWindowFeature(1);
    paramBundle = getIntent();
    mReason = paramBundle.getIntExtra("unlaunchable_reason", -1);
    mUserId = paramBundle.getIntExtra("android.intent.extra.user_handle", 55536);
    mTarget = ((IntentSender)paramBundle.getParcelableExtra("android.intent.extra.INTENT"));
    if (mUserId == 55536)
    {
      paramBundle = new StringBuilder();
      paramBundle.append("Invalid user id: ");
      paramBundle.append(mUserId);
      paramBundle.append(". Stopping.");
      Log.wtf("UnlaunchableAppActivity", paramBundle.toString());
      finish();
      return;
    }
    if (mReason == 1)
    {
      paramBundle = getResources().getString(17041303);
      String str = getResources().getString(17041302);
      paramBundle = new AlertDialog.Builder(this).setTitle(paramBundle).setMessage(str).setOnDismissListener(this);
      if (mReason == 1) {
        paramBundle.setPositiveButton(17041304, this).setNegativeButton(17039360, null);
      } else {
        paramBundle.setPositiveButton(17039370, null);
      }
      paramBundle.show();
      return;
    }
    paramBundle = new StringBuilder();
    paramBundle.append("Invalid unlaunchable type: ");
    paramBundle.append(mReason);
    Log.wtf("UnlaunchableAppActivity", paramBundle.toString());
    finish();
  }
  
  public void onDismiss(DialogInterface paramDialogInterface)
  {
    finish();
  }
}
