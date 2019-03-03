package android.app.backup;

import android.annotation.SystemApi;

@SystemApi
public abstract class SelectBackupTransportCallback
{
  public SelectBackupTransportCallback() {}
  
  public void onFailure(int paramInt) {}
  
  public void onSuccess(String paramString) {}
}
