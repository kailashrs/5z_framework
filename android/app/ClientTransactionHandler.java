package android.app;

import android.app.servertransaction.ClientTransaction;
import android.app.servertransaction.PendingTransactionActions;
import android.app.servertransaction.TransactionExecutor;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.res.CompatibilityInfo;
import android.content.res.Configuration;
import android.os.IBinder;
import android.util.MergedConfiguration;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.content.ReferrerIntent;
import java.util.List;

public abstract class ClientTransactionHandler
{
  public ClientTransactionHandler() {}
  
  @VisibleForTesting
  public void executeTransaction(ClientTransaction paramClientTransaction)
  {
    paramClientTransaction.preExecute(this);
    getTransactionExecutor().execute(paramClientTransaction);
    paramClientTransaction.recycle();
  }
  
  public abstract ActivityThread.ActivityClientRecord getActivityClient(IBinder paramIBinder);
  
  public abstract LoadedApk getPackageInfoNoCheck(ApplicationInfo paramApplicationInfo, CompatibilityInfo paramCompatibilityInfo);
  
  abstract TransactionExecutor getTransactionExecutor();
  
  public abstract void handleActivityConfigurationChanged(IBinder paramIBinder, Configuration paramConfiguration, int paramInt);
  
  public abstract void handleConfigurationChanged(Configuration paramConfiguration);
  
  public abstract void handleDestroyActivity(IBinder paramIBinder, boolean paramBoolean1, int paramInt, boolean paramBoolean2, String paramString);
  
  public abstract Activity handleLaunchActivity(ActivityThread.ActivityClientRecord paramActivityClientRecord, PendingTransactionActions paramPendingTransactionActions, Intent paramIntent);
  
  public abstract void handleMultiWindowModeChanged(IBinder paramIBinder, boolean paramBoolean, Configuration paramConfiguration);
  
  public abstract void handleNewIntent(IBinder paramIBinder, List<ReferrerIntent> paramList, boolean paramBoolean);
  
  public abstract void handlePauseActivity(IBinder paramIBinder, boolean paramBoolean1, boolean paramBoolean2, int paramInt, PendingTransactionActions paramPendingTransactionActions, String paramString);
  
  public abstract void handlePictureInPictureModeChanged(IBinder paramIBinder, boolean paramBoolean, Configuration paramConfiguration);
  
  public abstract void handleRelaunchActivity(ActivityThread.ActivityClientRecord paramActivityClientRecord, PendingTransactionActions paramPendingTransactionActions);
  
  public abstract void handleResumeActivity(IBinder paramIBinder, boolean paramBoolean1, boolean paramBoolean2, String paramString);
  
  public abstract void handleSendResult(IBinder paramIBinder, List<ResultInfo> paramList, String paramString);
  
  public abstract void handleStartActivity(ActivityThread.ActivityClientRecord paramActivityClientRecord, PendingTransactionActions paramPendingTransactionActions);
  
  public abstract void handleStopActivity(IBinder paramIBinder, boolean paramBoolean1, int paramInt, PendingTransactionActions paramPendingTransactionActions, boolean paramBoolean2, String paramString);
  
  public abstract void handleWindowVisibility(IBinder paramIBinder, boolean paramBoolean);
  
  public abstract void performRestartActivity(IBinder paramIBinder, boolean paramBoolean);
  
  public abstract ActivityThread.ActivityClientRecord prepareRelaunchActivity(IBinder paramIBinder, List<ResultInfo> paramList, List<ReferrerIntent> paramList1, int paramInt, MergedConfiguration paramMergedConfiguration, boolean paramBoolean);
  
  public abstract void reportRelaunch(IBinder paramIBinder, PendingTransactionActions paramPendingTransactionActions);
  
  public abstract void reportStop(PendingTransactionActions paramPendingTransactionActions);
  
  void scheduleTransaction(ClientTransaction paramClientTransaction)
  {
    paramClientTransaction.preExecute(this);
    sendMessage(159, paramClientTransaction);
  }
  
  abstract void sendMessage(int paramInt, Object paramObject);
  
  public abstract void updatePendingConfiguration(Configuration paramConfiguration);
  
  public abstract void updateProcessState(int paramInt, boolean paramBoolean);
}
