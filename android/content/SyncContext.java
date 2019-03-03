package android.content;

import android.os.IBinder;
import android.os.RemoteException;
import android.os.SystemClock;

public class SyncContext
{
  private static final long HEARTBEAT_SEND_INTERVAL_IN_MS = 1000L;
  private long mLastHeartbeatSendTime;
  private ISyncContext mSyncContext;
  
  public SyncContext(ISyncContext paramISyncContext)
  {
    mSyncContext = paramISyncContext;
    mLastHeartbeatSendTime = 0L;
  }
  
  private void updateHeartbeat()
  {
    long l = SystemClock.elapsedRealtime();
    if (l < mLastHeartbeatSendTime + 1000L) {
      return;
    }
    try
    {
      mLastHeartbeatSendTime = l;
      if (mSyncContext != null) {
        mSyncContext.sendHeartbeat();
      }
    }
    catch (RemoteException localRemoteException) {}
  }
  
  public IBinder getSyncContextBinder()
  {
    IBinder localIBinder;
    if (mSyncContext == null) {
      localIBinder = null;
    } else {
      localIBinder = mSyncContext.asBinder();
    }
    return localIBinder;
  }
  
  public void onFinished(SyncResult paramSyncResult)
  {
    try
    {
      if (mSyncContext != null) {
        mSyncContext.onFinished(paramSyncResult);
      }
    }
    catch (RemoteException paramSyncResult) {}
  }
  
  public void setStatusText(String paramString)
  {
    updateHeartbeat();
  }
}
