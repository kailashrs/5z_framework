package android.app.backup;

import android.annotation.SystemApi;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

@SystemApi
public class RestoreSession
{
  static final String TAG = "RestoreSession";
  IRestoreSession mBinder;
  final Context mContext;
  RestoreObserverWrapper mObserver = null;
  
  RestoreSession(Context paramContext, IRestoreSession paramIRestoreSession)
  {
    mContext = paramContext;
    mBinder = paramIRestoreSession;
  }
  
  /* Error */
  public void endRestoreSession()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 34	android/app/backup/RestoreSession:mBinder	Landroid/app/backup/IRestoreSession;
    //   4: invokeinterface 42 1 0
    //   9: aload_0
    //   10: aconst_null
    //   11: putfield 34	android/app/backup/RestoreSession:mBinder	Landroid/app/backup/IRestoreSession;
    //   14: goto +19 -> 33
    //   17: astore_1
    //   18: goto +16 -> 34
    //   21: astore_1
    //   22: ldc 17
    //   24: ldc 44
    //   26: invokestatic 50	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   29: pop
    //   30: goto -21 -> 9
    //   33: return
    //   34: aload_0
    //   35: aconst_null
    //   36: putfield 34	android/app/backup/RestoreSession:mBinder	Landroid/app/backup/IRestoreSession;
    //   39: aload_1
    //   40: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	41	0	this	RestoreSession
    //   17	1	1	localObject	Object
    //   21	19	1	localRemoteException	RemoteException
    // Exception table:
    //   from	to	target	type
    //   0	9	17	finally
    //   22	30	17	finally
    //   0	9	21	android/os/RemoteException
  }
  
  public int getAvailableRestoreSets(RestoreObserver paramRestoreObserver)
  {
    return getAvailableRestoreSets(paramRestoreObserver, null);
  }
  
  public int getAvailableRestoreSets(RestoreObserver paramRestoreObserver, BackupManagerMonitor paramBackupManagerMonitor)
  {
    int i = -1;
    RestoreObserverWrapper localRestoreObserverWrapper = new RestoreObserverWrapper(mContext, paramRestoreObserver);
    if (paramBackupManagerMonitor == null) {
      paramRestoreObserver = null;
    } else {
      paramRestoreObserver = new BackupManagerMonitorWrapper(paramBackupManagerMonitor);
    }
    try
    {
      int j = mBinder.getAvailableRestoreSets(localRestoreObserverWrapper, paramRestoreObserver);
      i = j;
    }
    catch (RemoteException paramRestoreObserver)
    {
      Log.d("RestoreSession", "Can't contact server to get available sets");
    }
    return i;
  }
  
  public int restoreAll(long paramLong, RestoreObserver paramRestoreObserver)
  {
    return restoreAll(paramLong, paramRestoreObserver, null);
  }
  
  public int restoreAll(long paramLong, RestoreObserver paramRestoreObserver, BackupManagerMonitor paramBackupManagerMonitor)
  {
    int i = -1;
    if (mObserver != null)
    {
      Log.d("RestoreSession", "restoreAll() called during active restore");
      return -1;
    }
    mObserver = new RestoreObserverWrapper(mContext, paramRestoreObserver);
    if (paramBackupManagerMonitor == null) {
      paramRestoreObserver = null;
    } else {
      paramRestoreObserver = new BackupManagerMonitorWrapper(paramBackupManagerMonitor);
    }
    int j;
    try
    {
      j = mBinder.restoreAll(paramLong, mObserver, paramRestoreObserver);
    }
    catch (RemoteException paramRestoreObserver)
    {
      Log.d("RestoreSession", "Can't contact server to restore");
      j = i;
    }
    return j;
  }
  
  public int restorePackage(String paramString, RestoreObserver paramRestoreObserver)
  {
    return restorePackage(paramString, paramRestoreObserver, null);
  }
  
  public int restorePackage(String paramString, RestoreObserver paramRestoreObserver, BackupManagerMonitor paramBackupManagerMonitor)
  {
    int i = -1;
    if (mObserver != null)
    {
      Log.d("RestoreSession", "restorePackage() called during active restore");
      return -1;
    }
    mObserver = new RestoreObserverWrapper(mContext, paramRestoreObserver);
    if (paramBackupManagerMonitor == null) {
      paramRestoreObserver = null;
    } else {
      paramRestoreObserver = new BackupManagerMonitorWrapper(paramBackupManagerMonitor);
    }
    try
    {
      int j = mBinder.restorePackage(paramString, mObserver, paramRestoreObserver);
      i = j;
    }
    catch (RemoteException paramString)
    {
      Log.d("RestoreSession", "Can't contact server to restore package");
    }
    return i;
  }
  
  public int restoreSome(long paramLong, RestoreObserver paramRestoreObserver, BackupManagerMonitor paramBackupManagerMonitor, String[] paramArrayOfString)
  {
    int i = -1;
    if (mObserver != null)
    {
      Log.d("RestoreSession", "restoreAll() called during active restore");
      return -1;
    }
    mObserver = new RestoreObserverWrapper(mContext, paramRestoreObserver);
    if (paramBackupManagerMonitor == null) {}
    for (paramRestoreObserver = null;; paramRestoreObserver = new BackupManagerMonitorWrapper(paramBackupManagerMonitor)) {
      break;
    }
    int j;
    try
    {
      j = mBinder.restoreSome(paramLong, mObserver, paramRestoreObserver, paramArrayOfString);
    }
    catch (RemoteException paramRestoreObserver)
    {
      Log.d("RestoreSession", "Can't contact server to restore packages");
      j = i;
    }
    return j;
  }
  
  public int restoreSome(long paramLong, RestoreObserver paramRestoreObserver, String[] paramArrayOfString)
  {
    return restoreSome(paramLong, paramRestoreObserver, null, paramArrayOfString);
  }
  
  private class BackupManagerMonitorWrapper
    extends IBackupManagerMonitor.Stub
  {
    final BackupManagerMonitor mMonitor;
    
    BackupManagerMonitorWrapper(BackupManagerMonitor paramBackupManagerMonitor)
    {
      mMonitor = paramBackupManagerMonitor;
    }
    
    public void onEvent(Bundle paramBundle)
      throws RemoteException
    {
      mMonitor.onEvent(paramBundle);
    }
  }
  
  private class RestoreObserverWrapper
    extends IRestoreObserver.Stub
  {
    static final int MSG_RESTORE_FINISHED = 3;
    static final int MSG_RESTORE_SETS_AVAILABLE = 4;
    static final int MSG_RESTORE_STARTING = 1;
    static final int MSG_UPDATE = 2;
    final RestoreObserver mAppObserver;
    final Handler mHandler;
    
    RestoreObserverWrapper(Context paramContext, RestoreObserver paramRestoreObserver)
    {
      mHandler = new Handler(paramContext.getMainLooper())
      {
        public void handleMessage(Message paramAnonymousMessage)
        {
          switch (what)
          {
          default: 
            break;
          case 4: 
            mAppObserver.restoreSetsAvailable((RestoreSet[])obj);
            break;
          case 3: 
            mAppObserver.restoreFinished(arg1);
            break;
          case 2: 
            mAppObserver.onUpdate(arg1, (String)obj);
            break;
          case 1: 
            mAppObserver.restoreStarting(arg1);
          }
        }
      };
      mAppObserver = paramRestoreObserver;
    }
    
    public void onUpdate(int paramInt, String paramString)
    {
      mHandler.sendMessage(mHandler.obtainMessage(2, paramInt, 0, paramString));
    }
    
    public void restoreFinished(int paramInt)
    {
      mHandler.sendMessage(mHandler.obtainMessage(3, paramInt, 0));
    }
    
    public void restoreSetsAvailable(RestoreSet[] paramArrayOfRestoreSet)
    {
      mHandler.sendMessage(mHandler.obtainMessage(4, paramArrayOfRestoreSet));
    }
    
    public void restoreStarting(int paramInt)
    {
      mHandler.sendMessage(mHandler.obtainMessage(1, paramInt, 0));
    }
  }
}
