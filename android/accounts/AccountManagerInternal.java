package android.accounts;

import android.os.RemoteCallback;

public abstract class AccountManagerInternal
{
  public AccountManagerInternal() {}
  
  public abstract void addOnAppPermissionChangeListener(OnAppPermissionChangeListener paramOnAppPermissionChangeListener);
  
  public abstract byte[] backupAccountAccessPermissions(int paramInt);
  
  public abstract boolean hasAccountAccess(Account paramAccount, int paramInt);
  
  public abstract void requestAccountAccess(Account paramAccount, String paramString, int paramInt, RemoteCallback paramRemoteCallback);
  
  public abstract void restoreAccountAccessPermissions(byte[] paramArrayOfByte, int paramInt);
  
  public static abstract interface OnAppPermissionChangeListener
  {
    public abstract void onAppPermissionChanged(Account paramAccount, int paramInt);
  }
}
