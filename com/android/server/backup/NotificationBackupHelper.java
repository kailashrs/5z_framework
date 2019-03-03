package com.android.server.backup;

import android.app.INotificationManager;
import android.app.INotificationManager.Stub;
import android.app.backup.BlobBackupHelper;
import android.content.Context;
import android.os.ServiceManager;
import android.util.Log;
import android.util.Slog;

public class NotificationBackupHelper
  extends BlobBackupHelper
{
  static final int BLOB_VERSION = 1;
  static final boolean DEBUG = Log.isLoggable("NotifBackupHelper", 3);
  static final String KEY_NOTIFICATIONS = "notifications";
  static final String TAG = "NotifBackupHelper";
  
  public NotificationBackupHelper(Context paramContext)
  {
    super(1, new String[] { "notifications" });
  }
  
  protected void applyRestoredPayload(String paramString, byte[] paramArrayOfByte)
  {
    if (DEBUG)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Got restore of ");
      localStringBuilder.append(paramString);
      Slog.v("NotifBackupHelper", localStringBuilder.toString());
    }
    if ("notifications".equals(paramString)) {
      try
      {
        INotificationManager.Stub.asInterface(ServiceManager.getService("notification")).applyRestore(paramArrayOfByte, 0);
      }
      catch (Exception paramString)
      {
        Slog.e("NotifBackupHelper", "Couldn't communicate with notification manager");
      }
    }
  }
  
  protected byte[] getBackupPayload(String paramString)
  {
    byte[] arrayOfByte = null;
    if ("notifications".equals(paramString)) {
      try
      {
        arrayOfByte = INotificationManager.Stub.asInterface(ServiceManager.getService("notification")).getBackupPayload(0);
      }
      catch (Exception paramString)
      {
        Slog.e("NotifBackupHelper", "Couldn't communicate with notification manager");
        arrayOfByte = null;
      }
    }
    return arrayOfByte;
  }
}
