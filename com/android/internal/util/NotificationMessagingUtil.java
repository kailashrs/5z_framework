package com.android.internal.util;

import android.app.Notification;
import android.app.Notification.MessagingStyle;
import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings.Secure;
import android.service.notification.StatusBarNotification;
import android.util.ArrayMap;
import java.util.Objects;

public class NotificationMessagingUtil
{
  private static final String DEFAULT_SMS_APP_SETTING = "sms_default_application";
  private final Context mContext;
  private ArrayMap<Integer, String> mDefaultSmsApp = new ArrayMap();
  private final ContentObserver mSmsContentObserver = new ContentObserver(new Handler(Looper.getMainLooper()))
  {
    public void onChange(boolean paramAnonymousBoolean, Uri paramAnonymousUri, int paramAnonymousInt)
    {
      if (Settings.Secure.getUriFor("sms_default_application").equals(paramAnonymousUri)) {
        NotificationMessagingUtil.this.cacheDefaultSmsApp(paramAnonymousInt);
      }
    }
  };
  
  public NotificationMessagingUtil(Context paramContext)
  {
    mContext = paramContext;
    mContext.getContentResolver().registerContentObserver(Settings.Secure.getUriFor("sms_default_application"), false, mSmsContentObserver);
  }
  
  private void cacheDefaultSmsApp(int paramInt)
  {
    mDefaultSmsApp.put(Integer.valueOf(paramInt), Settings.Secure.getStringForUser(mContext.getContentResolver(), "sms_default_application", paramInt));
  }
  
  private boolean hasMessagingStyle(StatusBarNotification paramStatusBarNotification)
  {
    return Notification.MessagingStyle.class.equals(paramStatusBarNotification.getNotification().getNotificationStyle());
  }
  
  private boolean isCategoryMessage(StatusBarNotification paramStatusBarNotification)
  {
    return "msg".equals(getNotificationcategory);
  }
  
  private boolean isDefaultMessagingApp(StatusBarNotification paramStatusBarNotification)
  {
    int i = paramStatusBarNotification.getUserId();
    if ((i != 55536) && (i != -1))
    {
      if (mDefaultSmsApp.get(Integer.valueOf(i)) == null) {
        cacheDefaultSmsApp(i);
      }
      return Objects.equals(mDefaultSmsApp.get(Integer.valueOf(i)), paramStatusBarNotification.getPackageName());
    }
    return false;
  }
  
  public boolean isImportantMessaging(StatusBarNotification paramStatusBarNotification, int paramInt)
  {
    boolean bool = false;
    if (paramInt < 2) {
      return false;
    }
    if ((!hasMessagingStyle(paramStatusBarNotification)) && ((!isCategoryMessage(paramStatusBarNotification)) || (!isDefaultMessagingApp(paramStatusBarNotification)))) {
      return bool;
    }
    bool = true;
    return bool;
  }
  
  public boolean isMessaging(StatusBarNotification paramStatusBarNotification)
  {
    boolean bool;
    if ((!hasMessagingStyle(paramStatusBarNotification)) && (!isDefaultMessagingApp(paramStatusBarNotification)) && (!isCategoryMessage(paramStatusBarNotification))) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
}
