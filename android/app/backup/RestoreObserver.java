package android.app.backup;

import android.annotation.SystemApi;

public abstract class RestoreObserver
{
  public RestoreObserver() {}
  
  public void onUpdate(int paramInt, String paramString) {}
  
  public void restoreFinished(int paramInt) {}
  
  @SystemApi
  public void restoreSetsAvailable(RestoreSet[] paramArrayOfRestoreSet) {}
  
  public void restoreStarting(int paramInt) {}
}
