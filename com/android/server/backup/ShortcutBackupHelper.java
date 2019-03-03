package com.android.server.backup;

import android.app.backup.BlobBackupHelper;
import android.content.pm.IShortcutService;
import android.content.pm.IShortcutService.Stub;
import android.os.ServiceManager;
import android.util.Slog;

public class ShortcutBackupHelper
  extends BlobBackupHelper
{
  private static final int BLOB_VERSION = 1;
  private static final String KEY_USER_FILE = "shortcutuser.xml";
  private static final String TAG = "ShortcutBackupAgent";
  
  public ShortcutBackupHelper()
  {
    super(1, new String[] { "shortcutuser.xml" });
  }
  
  private IShortcutService getShortcutService()
  {
    return IShortcutService.Stub.asInterface(ServiceManager.getService("shortcut"));
  }
  
  protected void applyRestoredPayload(String paramString, byte[] paramArrayOfByte)
  {
    int i;
    if ((paramString.hashCode() == -792920646) && (paramString.equals("shortcutuser.xml"))) {
      i = 0;
    } else {
      i = -1;
    }
    if (i != 0)
    {
      paramArrayOfByte = new StringBuilder();
      paramArrayOfByte.append("Unknown key: ");
      paramArrayOfByte.append(paramString);
      Slog.w("ShortcutBackupAgent", paramArrayOfByte.toString());
    }
    else
    {
      try
      {
        getShortcutService().applyRestore(paramArrayOfByte, 0);
      }
      catch (Exception paramString)
      {
        Slog.wtf("ShortcutBackupAgent", "Restore failed", paramString);
      }
    }
  }
  
  protected byte[] getBackupPayload(String paramString)
  {
    int i;
    if ((paramString.hashCode() == -792920646) && (paramString.equals("shortcutuser.xml"))) {
      i = 0;
    } else {
      i = -1;
    }
    if (i != 0)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Unknown key: ");
      localStringBuilder.append(paramString);
      Slog.w("ShortcutBackupAgent", localStringBuilder.toString());
    }
    else
    {
      try
      {
        paramString = getShortcutService().getBackupPayload(0);
        return paramString;
      }
      catch (Exception paramString)
      {
        Slog.wtf("ShortcutBackupAgent", "Backup failed", paramString);
      }
    }
    return null;
  }
}
