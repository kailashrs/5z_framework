package android.app.backup;

import android.annotation.SystemApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.util.Log;
import android.util.Pair;

public class BackupManager
{
  @SystemApi
  public static final int ERROR_AGENT_FAILURE = -1003;
  @SystemApi
  public static final int ERROR_BACKUP_CANCELLED = -2003;
  @SystemApi
  public static final int ERROR_BACKUP_NOT_ALLOWED = -2001;
  @SystemApi
  public static final int ERROR_PACKAGE_NOT_FOUND = -2002;
  @SystemApi
  public static final int ERROR_TRANSPORT_ABORTED = -1000;
  @SystemApi
  public static final int ERROR_TRANSPORT_INVALID = -2;
  @SystemApi
  public static final int ERROR_TRANSPORT_PACKAGE_REJECTED = -1002;
  @SystemApi
  public static final int ERROR_TRANSPORT_QUOTA_EXCEEDED = -1005;
  @SystemApi
  public static final int ERROR_TRANSPORT_UNAVAILABLE = -1;
  public static final String EXTRA_BACKUP_SERVICES_AVAILABLE = "backup_services_available";
  @SystemApi
  public static final int FLAG_NON_INCREMENTAL_BACKUP = 1;
  @SystemApi
  public static final String PACKAGE_MANAGER_SENTINEL = "@pm@";
  @SystemApi
  public static final int SUCCESS = 0;
  private static final String TAG = "BackupManager";
  private static IBackupManager sService;
  private Context mContext;
  
  public BackupManager(Context paramContext)
  {
    mContext = paramContext;
  }
  
  private static void checkServiceBinder()
  {
    if (sService == null) {
      sService = IBackupManager.Stub.asInterface(ServiceManager.getService("backup"));
    }
  }
  
  public static void dataChanged(String paramString)
  {
    
    if (sService != null) {
      try
      {
        sService.dataChanged(paramString);
      }
      catch (RemoteException paramString)
      {
        Log.e("BackupManager", "dataChanged(pkg) couldn't connect");
      }
    }
  }
  
  @SystemApi
  public void backupNow()
  {
    
    if (sService != null) {
      try
      {
        sService.backupNow();
      }
      catch (RemoteException localRemoteException)
      {
        Log.e("BackupManager", "backupNow() couldn't connect");
      }
    }
  }
  
  @SystemApi
  public RestoreSession beginRestoreSession()
  {
    Object localObject1 = null;
    Object localObject2 = null;
    checkServiceBinder();
    Object localObject3 = localObject1;
    Object localObject4;
    if (sService != null) {
      try
      {
        IRestoreSession localIRestoreSession = sService.beginRestoreSession(null, null);
        localObject3 = localObject2;
        if (localIRestoreSession != null)
        {
          localObject3 = new android/app/backup/RestoreSession;
          ((RestoreSession)localObject3).<init>(mContext, localIRestoreSession);
        }
      }
      catch (RemoteException localRemoteException)
      {
        Log.e("BackupManager", "beginRestoreSession() couldn't connect");
        localObject4 = localObject1;
      }
    }
    return localObject4;
  }
  
  @SystemApi
  public void cancelBackups()
  {
    
    if (sService != null) {
      try
      {
        sService.cancelBackups();
      }
      catch (RemoteException localRemoteException)
      {
        Log.e("BackupManager", "cancelBackups() couldn't connect.");
      }
    }
  }
  
  public void dataChanged()
  {
    
    if (sService != null) {
      try
      {
        sService.dataChanged(mContext.getPackageName());
      }
      catch (RemoteException localRemoteException)
      {
        Log.d("BackupManager", "dataChanged() couldn't connect");
      }
    }
  }
  
  @SystemApi
  public long getAvailableRestoreToken(String paramString)
  {
    
    if (sService != null) {
      try
      {
        long l = sService.getAvailableRestoreToken(paramString);
        return l;
      }
      catch (RemoteException paramString)
      {
        Log.e("BackupManager", "getAvailableRestoreToken() couldn't connect");
      }
    }
    return 0L;
  }
  
  @SystemApi
  public Intent getConfigurationIntent(String paramString)
  {
    if (sService != null) {
      try
      {
        paramString = sService.getConfigurationIntent(paramString);
        return paramString;
      }
      catch (RemoteException paramString)
      {
        Log.e("BackupManager", "getConfigurationIntent() couldn't connect");
      }
    }
    return null;
  }
  
  @SystemApi
  public String getCurrentTransport()
  {
    
    if (sService != null) {
      try
      {
        String str = sService.getCurrentTransport();
        return str;
      }
      catch (RemoteException localRemoteException)
      {
        Log.e("BackupManager", "getCurrentTransport() couldn't connect");
      }
    }
    return null;
  }
  
  @SystemApi
  public Intent getDataManagementIntent(String paramString)
  {
    if (sService != null) {
      try
      {
        paramString = sService.getDataManagementIntent(paramString);
        return paramString;
      }
      catch (RemoteException paramString)
      {
        Log.e("BackupManager", "getDataManagementIntent() couldn't connect");
      }
    }
    return null;
  }
  
  @SystemApi
  public String getDataManagementLabel(String paramString)
  {
    if (sService != null) {
      try
      {
        paramString = sService.getDataManagementLabel(paramString);
        return paramString;
      }
      catch (RemoteException paramString)
      {
        Log.e("BackupManager", "getDataManagementLabel() couldn't connect");
      }
    }
    return null;
  }
  
  @SystemApi
  public String getDestinationString(String paramString)
  {
    if (sService != null) {
      try
      {
        paramString = sService.getDestinationString(paramString);
        return paramString;
      }
      catch (RemoteException paramString)
      {
        Log.e("BackupManager", "getDestinationString() couldn't connect");
      }
    }
    return null;
  }
  
  @SystemApi
  public boolean isAppEligibleForBackup(String paramString)
  {
    
    if (sService != null) {
      try
      {
        boolean bool = sService.isAppEligibleForBackup(paramString);
        return bool;
      }
      catch (RemoteException paramString)
      {
        Log.e("BackupManager", "isAppEligibleForBackup(pkg) couldn't connect");
      }
    }
    return false;
  }
  
  @SystemApi
  public boolean isBackupEnabled()
  {
    
    if (sService != null) {
      try
      {
        boolean bool = sService.isBackupEnabled();
        return bool;
      }
      catch (RemoteException localRemoteException)
      {
        Log.e("BackupManager", "isBackupEnabled() couldn't connect");
      }
    }
    return false;
  }
  
  @SystemApi
  public boolean isBackupServiceActive(UserHandle paramUserHandle)
  {
    mContext.enforceCallingOrSelfPermission("android.permission.BACKUP", "isBackupServiceActive");
    checkServiceBinder();
    if (sService != null) {
      try
      {
        boolean bool = sService.isBackupServiceActive(paramUserHandle.getIdentifier());
        return bool;
      }
      catch (RemoteException paramUserHandle)
      {
        Log.e("BackupManager", "isBackupEnabled() couldn't connect");
      }
    }
    return false;
  }
  
  @SystemApi
  public String[] listAllTransports()
  {
    
    if (sService != null) {
      try
      {
        String[] arrayOfString = sService.listAllTransports();
        return arrayOfString;
      }
      catch (RemoteException localRemoteException)
      {
        Log.e("BackupManager", "listAllTransports() couldn't connect");
      }
    }
    return null;
  }
  
  @SystemApi
  public int requestBackup(String[] paramArrayOfString, BackupObserver paramBackupObserver)
  {
    return requestBackup(paramArrayOfString, paramBackupObserver, null, 0);
  }
  
  @SystemApi
  public int requestBackup(String[] paramArrayOfString, BackupObserver paramBackupObserver, BackupManagerMonitor paramBackupManagerMonitor, int paramInt)
  {
    
    if (sService != null)
    {
      Object localObject = null;
      if (paramBackupObserver == null) {
        paramBackupObserver = null;
      }
      try
      {
        paramBackupObserver = new BackupObserverWrapper(mContext, paramBackupObserver);
        if (paramBackupManagerMonitor == null) {
          paramBackupManagerMonitor = localObject;
        } else {
          paramBackupManagerMonitor = new BackupManagerMonitorWrapper(paramBackupManagerMonitor);
        }
        paramInt = sService.requestBackup(paramArrayOfString, paramBackupObserver, paramBackupManagerMonitor, paramInt);
        return paramInt;
      }
      catch (RemoteException paramArrayOfString)
      {
        Log.e("BackupManager", "requestBackup() couldn't connect");
      }
    }
    return -1;
  }
  
  @Deprecated
  public int requestRestore(RestoreObserver paramRestoreObserver)
  {
    return requestRestore(paramRestoreObserver, null);
  }
  
  @SystemApi
  @Deprecated
  public int requestRestore(RestoreObserver paramRestoreObserver, BackupManagerMonitor paramBackupManagerMonitor)
  {
    Log.w("BackupManager", "requestRestore(): Since Android P app can no longer request restoring of its backup.");
    return -1;
  }
  
  @SystemApi
  @Deprecated
  public String selectBackupTransport(String paramString)
  {
    
    if (sService != null) {
      try
      {
        paramString = sService.selectBackupTransport(paramString);
        return paramString;
      }
      catch (RemoteException paramString)
      {
        Log.e("BackupManager", "selectBackupTransport() couldn't connect");
      }
    }
    return null;
  }
  
  @SystemApi
  public void selectBackupTransport(ComponentName paramComponentName, SelectBackupTransportCallback paramSelectBackupTransportCallback)
  {
    
    if (sService != null)
    {
      if (paramSelectBackupTransportCallback == null) {
        paramSelectBackupTransportCallback = null;
      }
      try
      {
        paramSelectBackupTransportCallback = new SelectTransportListenerWrapper(mContext, paramSelectBackupTransportCallback);
        sService.selectBackupTransportAsync(paramComponentName, paramSelectBackupTransportCallback);
      }
      catch (RemoteException paramComponentName)
      {
        Log.e("BackupManager", "selectBackupTransportAsync() couldn't connect");
      }
    }
  }
  
  @SystemApi
  public void setAutoRestore(boolean paramBoolean)
  {
    
    if (sService != null) {
      try
      {
        sService.setAutoRestore(paramBoolean);
      }
      catch (RemoteException localRemoteException)
      {
        Log.e("BackupManager", "setAutoRestore() couldn't connect");
      }
    }
  }
  
  @SystemApi
  public void setBackupEnabled(boolean paramBoolean)
  {
    
    if (sService != null) {
      try
      {
        sService.setBackupEnabled(paramBoolean);
      }
      catch (RemoteException localRemoteException)
      {
        Log.e("BackupManager", "setBackupEnabled() couldn't connect");
      }
    }
  }
  
  @SystemApi
  public void updateTransportAttributes(ComponentName paramComponentName, String paramString1, Intent paramIntent1, String paramString2, Intent paramIntent2, String paramString3)
  {
    
    if (sService != null) {
      try
      {
        sService.updateTransportAttributes(paramComponentName, paramString1, paramIntent1, paramString2, paramIntent2, paramString3);
      }
      catch (RemoteException paramComponentName)
      {
        Log.e("BackupManager", "describeTransport() couldn't connect");
      }
    }
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
  
  private class BackupObserverWrapper
    extends IBackupObserver.Stub
  {
    static final int MSG_FINISHED = 3;
    static final int MSG_RESULT = 2;
    static final int MSG_UPDATE = 1;
    final Handler mHandler;
    final BackupObserver mObserver;
    
    BackupObserverWrapper(Context paramContext, BackupObserver paramBackupObserver)
    {
      mHandler = new Handler(paramContext.getMainLooper())
      {
        public void handleMessage(Message paramAnonymousMessage)
        {
          switch (what)
          {
          default: 
            StringBuilder localStringBuilder = new StringBuilder();
            localStringBuilder.append("Unknown message: ");
            localStringBuilder.append(paramAnonymousMessage);
            Log.w("BackupManager", localStringBuilder.toString());
            break;
          case 3: 
            mObserver.backupFinished(arg1);
            break;
          case 2: 
            mObserver.onResult((String)obj, arg1);
            break;
          case 1: 
            paramAnonymousMessage = (Pair)obj;
            mObserver.onUpdate((String)first, (BackupProgress)second);
          }
        }
      };
      mObserver = paramBackupObserver;
    }
    
    public void backupFinished(int paramInt)
    {
      mHandler.sendMessage(mHandler.obtainMessage(3, paramInt, 0));
    }
    
    public void onResult(String paramString, int paramInt)
    {
      mHandler.sendMessage(mHandler.obtainMessage(2, paramInt, 0, paramString));
    }
    
    public void onUpdate(String paramString, BackupProgress paramBackupProgress)
    {
      mHandler.sendMessage(mHandler.obtainMessage(1, Pair.create(paramString, paramBackupProgress)));
    }
  }
  
  private class SelectTransportListenerWrapper
    extends ISelectBackupTransportCallback.Stub
  {
    private final Handler mHandler;
    private final SelectBackupTransportCallback mListener;
    
    SelectTransportListenerWrapper(Context paramContext, SelectBackupTransportCallback paramSelectBackupTransportCallback)
    {
      mHandler = new Handler(paramContext.getMainLooper());
      mListener = paramSelectBackupTransportCallback;
    }
    
    public void onFailure(final int paramInt)
    {
      mHandler.post(new Runnable()
      {
        public void run()
        {
          mListener.onFailure(paramInt);
        }
      });
    }
    
    public void onSuccess(final String paramString)
    {
      mHandler.post(new Runnable()
      {
        public void run()
        {
          mListener.onSuccess(paramString);
        }
      });
    }
  }
}
