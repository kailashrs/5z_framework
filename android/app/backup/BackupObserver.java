package android.app.backup;

import android.annotation.SystemApi;

@SystemApi
public abstract class BackupObserver
{
  public BackupObserver() {}
  
  public void backupFinished(int paramInt) {}
  
  public void onResult(String paramString, int paramInt) {}
  
  public void onUpdate(String paramString, BackupProgress paramBackupProgress) {}
}
