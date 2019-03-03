package com.android.internal.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.UserHandle;
import android.util.Log;

public class ScreenshotHelper
{
  private static final String SYSUI_PACKAGE = "com.android.systemui";
  private static final String SYSUI_SCREENSHOT_ERROR_RECEIVER = "com.android.systemui.screenshot.ScreenshotServiceErrorReceiver";
  private static final String SYSUI_SCREENSHOT_SERVICE = "com.android.systemui.screenshot.TakeScreenshotService";
  private static final String TAG = "ScreenshotHelper";
  private final int SCREENSHOT_TIMEOUT_MS = 10000;
  private final Context mContext;
  private ServiceConnection mScreenshotConnection = null;
  private final Object mScreenshotLock = new Object();
  
  public ScreenshotHelper(Context paramContext)
  {
    mContext = paramContext;
  }
  
  private void notifyScreenshotError()
  {
    ComponentName localComponentName = new ComponentName("com.android.systemui", "com.android.systemui.screenshot.ScreenshotServiceErrorReceiver");
    Intent localIntent = new Intent("android.intent.action.USER_PRESENT");
    localIntent.setComponent(localComponentName);
    localIntent.addFlags(335544320);
    mContext.sendBroadcastAsUser(localIntent, UserHandle.CURRENT);
  }
  
  public void takeScreenshot(int paramInt, boolean paramBoolean1, boolean paramBoolean2, Handler paramHandler)
  {
    Runnable local1;
    synchronized (mScreenshotLock)
    {
      if (mScreenshotConnection != null) {
        return;
      }
      Object localObject2 = new android/content/ComponentName;
      ((ComponentName)localObject2).<init>("com.android.systemui", "com.android.systemui.screenshot.TakeScreenshotService");
      Intent localIntent = new android/content/Intent;
      localIntent.<init>();
      local1 = new com/android/internal/util/ScreenshotHelper$1;
      local1.<init>(this);
      localIntent.setComponent((ComponentName)localObject2);
      localObject2 = new com/android/internal/util/ScreenshotHelper$2;
      ((2)localObject2).<init>(this, paramInt, paramHandler, local1, paramBoolean1, paramBoolean2);
      if (mContext.bindServiceAsUser(localIntent, (ServiceConnection)localObject2, 33554433, UserHandle.CURRENT)) {
        mScreenshotConnection = ((ServiceConnection)localObject2);
      }
    }
    throw paramHandler;
  }
}
