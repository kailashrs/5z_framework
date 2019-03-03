package android.webkit;

import android.content.Context;
import android.os.Handler;

@Deprecated
abstract class WebSyncManager
  implements Runnable
{
  protected static final String LOGTAG = "websync";
  protected WebViewDatabase mDataBase;
  protected Handler mHandler;
  
  protected WebSyncManager(Context paramContext, String paramString) {}
  
  protected Object clone()
    throws CloneNotSupportedException
  {
    throw new CloneNotSupportedException("doesn't implement Cloneable");
  }
  
  protected void onSyncInit() {}
  
  public void resetSync() {}
  
  public void run() {}
  
  public void startSync() {}
  
  public void stopSync() {}
  
  public void sync() {}
  
  abstract void syncFromRamToFlash();
}
