package com.android.internal.util;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.UserHandle;
import android.provider.Settings.Global;

public class EmergencyAffordanceManager
{
  private static final String EMERGENCY_CALL_NUMBER_SETTING = "emergency_affordance_number";
  public static final boolean ENABLED = true;
  private static final String FORCE_EMERGENCY_AFFORDANCE_SETTING = "force_emergency_affordance";
  private final Context mContext;
  
  public EmergencyAffordanceManager(Context paramContext)
  {
    mContext = paramContext;
  }
  
  private boolean forceShowing()
  {
    ContentResolver localContentResolver = mContext.getContentResolver();
    boolean bool = false;
    if (Settings.Global.getInt(localContentResolver, "force_emergency_affordance", 0) != 0) {
      bool = true;
    }
    return bool;
  }
  
  private static Uri getPhoneUri(Context paramContext)
  {
    String str = paramContext.getResources().getString(17039705);
    Object localObject = str;
    if (Build.IS_DEBUGGABLE)
    {
      paramContext = Settings.Global.getString(paramContext.getContentResolver(), "emergency_affordance_number");
      localObject = str;
      if (paramContext != null) {
        localObject = paramContext;
      }
    }
    return Uri.fromParts("tel", (String)localObject, null);
  }
  
  private boolean isEmergencyAffordanceNeeded()
  {
    ContentResolver localContentResolver = mContext.getContentResolver();
    boolean bool = false;
    if (Settings.Global.getInt(localContentResolver, "emergency_affordance_needed", 0) != 0) {
      bool = true;
    }
    return bool;
  }
  
  private static void performEmergencyCall(Context paramContext)
  {
    Intent localIntent = new Intent("android.intent.action.CALL_EMERGENCY");
    localIntent.setData(getPhoneUri(paramContext));
    localIntent.setFlags(268435456);
    paramContext.startActivityAsUser(localIntent, UserHandle.CURRENT);
  }
  
  public boolean needsEmergencyAffordance()
  {
    if (forceShowing()) {
      return true;
    }
    return isEmergencyAffordanceNeeded();
  }
  
  public final void performEmergencyCall()
  {
    performEmergencyCall(mContext);
  }
}
