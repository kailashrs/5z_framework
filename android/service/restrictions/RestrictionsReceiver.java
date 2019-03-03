package android.service.restrictions;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PersistableBundle;

public abstract class RestrictionsReceiver
  extends BroadcastReceiver
{
  private static final String TAG = "RestrictionsReceiver";
  
  public RestrictionsReceiver() {}
  
  public void onReceive(Context paramContext, Intent paramIntent)
  {
    if ("android.content.action.REQUEST_PERMISSION".equals(paramIntent.getAction())) {
      onRequestPermission(paramContext, paramIntent.getStringExtra("android.content.extra.PACKAGE_NAME"), paramIntent.getStringExtra("android.content.extra.REQUEST_TYPE"), paramIntent.getStringExtra("android.content.extra.REQUEST_ID"), (PersistableBundle)paramIntent.getParcelableExtra("android.content.extra.REQUEST_BUNDLE"));
    }
  }
  
  public abstract void onRequestPermission(Context paramContext, String paramString1, String paramString2, String paramString3, PersistableBundle paramPersistableBundle);
}
