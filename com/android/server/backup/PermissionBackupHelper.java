package com.android.server.backup;

import android.app.AppGlobals;
import android.app.backup.BlobBackupHelper;
import android.content.pm.IPackageManager;
import android.util.Slog;

public class PermissionBackupHelper
  extends BlobBackupHelper
{
  private static final boolean DEBUG = false;
  private static final String KEY_PERMISSIONS = "permissions";
  private static final int STATE_VERSION = 1;
  private static final String TAG = "PermissionBackup";
  
  public PermissionBackupHelper()
  {
    super(1, new String[] { "permissions" });
  }
  
  protected void applyRestoredPayload(String paramString, byte[] paramArrayOfByte)
  {
    IPackageManager localIPackageManager = AppGlobals.getPackageManager();
    int i = -1;
    try
    {
      if ((paramString.hashCode() == 1133704324) && (paramString.equals("permissions"))) {
        i = 0;
      }
      if (i != 0)
      {
        paramArrayOfByte = new java/lang/StringBuilder;
        paramArrayOfByte.<init>();
        paramArrayOfByte.append("Unexpected restore key ");
        paramArrayOfByte.append(paramString);
        Slog.w("PermissionBackup", paramArrayOfByte.toString());
      }
      else
      {
        localIPackageManager.restorePermissionGrants(paramArrayOfByte, 0);
      }
    }
    catch (Exception paramArrayOfByte)
    {
      paramArrayOfByte = new StringBuilder();
      paramArrayOfByte.append("Unable to restore key ");
      paramArrayOfByte.append(paramString);
      Slog.w("PermissionBackup", paramArrayOfByte.toString());
    }
  }
  
  protected byte[] getBackupPayload(String paramString)
  {
    Object localObject = AppGlobals.getPackageManager();
    int i = -1;
    try
    {
      if ((paramString.hashCode() == 1133704324) && (paramString.equals("permissions"))) {
        i = 0;
      }
      if (i != 0)
      {
        localObject = new java/lang/StringBuilder;
        ((StringBuilder)localObject).<init>();
        ((StringBuilder)localObject).append("Unexpected backup key ");
        ((StringBuilder)localObject).append(paramString);
        Slog.w("PermissionBackup", ((StringBuilder)localObject).toString());
      }
      else
      {
        localObject = ((IPackageManager)localObject).getPermissionGrantBackup(0);
        return localObject;
      }
    }
    catch (Exception localException)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Unable to store payload ");
      localStringBuilder.append(paramString);
      Slog.e("PermissionBackup", localStringBuilder.toString());
    }
    return null;
  }
}
