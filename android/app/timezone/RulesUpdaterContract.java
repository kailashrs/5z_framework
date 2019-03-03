package android.app.timezone;

import android.content.Context;
import android.content.Intent;
import android.os.UserHandle;

public final class RulesUpdaterContract
{
  public static final String ACTION_TRIGGER_RULES_UPDATE_CHECK = "com.android.intent.action.timezone.TRIGGER_RULES_UPDATE_CHECK";
  public static final String EXTRA_CHECK_TOKEN = "com.android.intent.extra.timezone.CHECK_TOKEN";
  public static final String TRIGGER_TIME_ZONE_RULES_CHECK_PERMISSION = "android.permission.TRIGGER_TIME_ZONE_RULES_CHECK";
  public static final String UPDATE_TIME_ZONE_RULES_PERMISSION = "android.permission.UPDATE_TIME_ZONE_RULES";
  
  public RulesUpdaterContract() {}
  
  public static Intent createUpdaterIntent(String paramString)
  {
    Intent localIntent = new Intent("com.android.intent.action.timezone.TRIGGER_RULES_UPDATE_CHECK");
    localIntent.setPackage(paramString);
    localIntent.setFlags(32);
    return localIntent;
  }
  
  public static void sendBroadcast(Context paramContext, String paramString, byte[] paramArrayOfByte)
  {
    paramString = createUpdaterIntent(paramString);
    paramString.putExtra("com.android.intent.extra.timezone.CHECK_TOKEN", paramArrayOfByte);
    paramContext.sendBroadcastAsUser(paramString, UserHandle.SYSTEM, "android.permission.UPDATE_TIME_ZONE_RULES");
  }
}
