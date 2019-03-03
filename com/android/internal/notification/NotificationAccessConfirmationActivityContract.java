package com.android.internal.notification;

import android.content.ComponentName;
import android.content.Intent;

public final class NotificationAccessConfirmationActivityContract
{
  private static final ComponentName COMPONENT_NAME = new ComponentName("com.android.settings", "com.android.settings.notification.NotificationAccessConfirmationActivity");
  public static final String EXTRA_COMPONENT_NAME = "component_name";
  public static final String EXTRA_PACKAGE_TITLE = "package_title";
  public static final String EXTRA_USER_ID = "user_id";
  
  public NotificationAccessConfirmationActivityContract() {}
  
  public static Intent launcherIntent(int paramInt, ComponentName paramComponentName, String paramString)
  {
    return new Intent().setComponent(COMPONENT_NAME).putExtra("user_id", paramInt).putExtra("component_name", paramComponentName).putExtra("package_title", paramString);
  }
}
