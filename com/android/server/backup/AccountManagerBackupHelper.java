package com.android.server.backup;

import android.accounts.AccountManagerInternal;
import android.app.backup.BlobBackupHelper;
import android.util.Slog;
import com.android.server.LocalServices;

public class AccountManagerBackupHelper
  extends BlobBackupHelper
{
  private static final boolean DEBUG = false;
  private static final String KEY_ACCOUNT_ACCESS_GRANTS = "account_access_grants";
  private static final int STATE_VERSION = 1;
  private static final String TAG = "AccountsBackup";
  
  public AccountManagerBackupHelper()
  {
    super(1, new String[] { "account_access_grants" });
  }
  
  protected void applyRestoredPayload(String paramString, byte[] paramArrayOfByte)
  {
    AccountManagerInternal localAccountManagerInternal = (AccountManagerInternal)LocalServices.getService(AccountManagerInternal.class);
    int i = -1;
    try
    {
      if ((paramString.hashCode() == 1544100736) && (paramString.equals("account_access_grants"))) {
        i = 0;
      }
      if (i != 0)
      {
        paramArrayOfByte = new java/lang/StringBuilder;
        paramArrayOfByte.<init>();
        paramArrayOfByte.append("Unexpected restore key ");
        paramArrayOfByte.append(paramString);
        Slog.w("AccountsBackup", paramArrayOfByte.toString());
      }
      else
      {
        localAccountManagerInternal.restoreAccountAccessPermissions(paramArrayOfByte, 0);
      }
    }
    catch (Exception paramArrayOfByte)
    {
      paramArrayOfByte = new StringBuilder();
      paramArrayOfByte.append("Unable to restore key ");
      paramArrayOfByte.append(paramString);
      Slog.w("AccountsBackup", paramArrayOfByte.toString());
    }
  }
  
  protected byte[] getBackupPayload(String paramString)
  {
    Object localObject = (AccountManagerInternal)LocalServices.getService(AccountManagerInternal.class);
    int i = -1;
    try
    {
      if ((paramString.hashCode() == 1544100736) && (paramString.equals("account_access_grants"))) {
        i = 0;
      }
      if (i != 0)
      {
        localObject = new java/lang/StringBuilder;
        ((StringBuilder)localObject).<init>();
        ((StringBuilder)localObject).append("Unexpected backup key ");
        ((StringBuilder)localObject).append(paramString);
        Slog.w("AccountsBackup", ((StringBuilder)localObject).toString());
      }
      else
      {
        localObject = ((AccountManagerInternal)localObject).backupAccountAccessPermissions(0);
        return localObject;
      }
    }
    catch (Exception localException)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Unable to store payload ");
      localStringBuilder.append(paramString);
      Slog.e("AccountsBackup", localStringBuilder.toString());
    }
    return new byte[0];
  }
}
