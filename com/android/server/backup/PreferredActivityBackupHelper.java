package com.android.server.backup;

import android.app.AppGlobals;
import android.app.backup.BlobBackupHelper;
import android.content.pm.IPackageManager;
import android.util.Slog;

public class PreferredActivityBackupHelper
  extends BlobBackupHelper
{
  private static final boolean DEBUG = false;
  private static final String KEY_DEFAULT_APPS = "default-apps";
  private static final String KEY_INTENT_VERIFICATION = "intent-verification";
  private static final String KEY_PREFERRED = "preferred-activity";
  private static final int STATE_VERSION = 3;
  private static final String TAG = "PreferredBackup";
  
  public PreferredActivityBackupHelper()
  {
    super(3, new String[] { "preferred-activity", "default-apps", "intent-verification" });
  }
  
  protected void applyRestoredPayload(String paramString, byte[] paramArrayOfByte)
  {
    IPackageManager localIPackageManager = AppGlobals.getPackageManager();
    int i = -1;
    try
    {
      int j = paramString.hashCode();
      if (j != -696985986)
      {
        if (j != -429170260)
        {
          if ((j == 1336142555) && (paramString.equals("preferred-activity"))) {
            i = 0;
          }
        }
        else if (paramString.equals("intent-verification")) {
          i = 2;
        }
      }
      else if (paramString.equals("default-apps")) {
        i = 1;
      }
      switch (i)
      {
      default: 
        break;
      case 2: 
        localIPackageManager.restoreIntentFilterVerification(paramArrayOfByte, 0);
        break;
      case 1: 
        localIPackageManager.restoreDefaultApps(paramArrayOfByte, 0);
        break;
      case 0: 
        localIPackageManager.restorePreferredActivities(paramArrayOfByte, 0);
        break;
      }
      paramArrayOfByte = new java/lang/StringBuilder;
      paramArrayOfByte.<init>();
      paramArrayOfByte.append("Unexpected restore key ");
      paramArrayOfByte.append(paramString);
      Slog.w("PreferredBackup", paramArrayOfByte.toString());
    }
    catch (Exception paramArrayOfByte)
    {
      paramArrayOfByte = new StringBuilder();
      paramArrayOfByte.append("Unable to restore key ");
      paramArrayOfByte.append(paramString);
      Slog.w("PreferredBackup", paramArrayOfByte.toString());
    }
  }
  
  protected byte[] getBackupPayload(String paramString)
  {
    Object localObject = AppGlobals.getPackageManager();
    int i = -1;
    try
    {
      int j = paramString.hashCode();
      if (j != -696985986)
      {
        if (j != -429170260)
        {
          if ((j == 1336142555) && (paramString.equals("preferred-activity"))) {
            i = 0;
          }
        }
        else if (paramString.equals("intent-verification")) {
          i = 2;
        }
      }
      else if (paramString.equals("default-apps")) {
        i = 1;
      }
      switch (i)
      {
      default: 
        break;
      case 2: 
        return ((IPackageManager)localObject).getIntentFilterVerificationBackup(0);
      case 1: 
        return ((IPackageManager)localObject).getDefaultAppsBackup(0);
      case 0: 
        return ((IPackageManager)localObject).getPreferredActivityBackup(0);
      }
      localObject = new java/lang/StringBuilder;
      ((StringBuilder)localObject).<init>();
      ((StringBuilder)localObject).append("Unexpected backup key ");
      ((StringBuilder)localObject).append(paramString);
      Slog.w("PreferredBackup", ((StringBuilder)localObject).toString());
    }
    catch (Exception localException)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Unable to store payload ");
      localStringBuilder.append(paramString);
      Slog.e("PreferredBackup", localStringBuilder.toString());
    }
    return null;
  }
}
