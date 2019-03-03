package android.app.servertransaction;

import android.app.ActivityThread.ActivityClientRecord;
import android.app.ClientTransactionHandler;
import android.os.IBinder;
import android.util.IntArray;
import com.android.internal.annotations.VisibleForTesting;
import java.util.List;

public class TransactionExecutor
{
  private static final boolean DEBUG_RESOLVER = false;
  private static final String TAG = "TransactionExecutor";
  private TransactionExecutorHelper mHelper = new TransactionExecutorHelper();
  private PendingTransactionActions mPendingActions = new PendingTransactionActions();
  private ClientTransactionHandler mTransactionHandler;
  
  public TransactionExecutor(ClientTransactionHandler paramClientTransactionHandler)
  {
    mTransactionHandler = paramClientTransactionHandler;
  }
  
  private void cycleToPath(ActivityThread.ActivityClientRecord paramActivityClientRecord, int paramInt, boolean paramBoolean)
  {
    int i = paramActivityClientRecord.getLifecycleState();
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Cycle from: ");
    localStringBuilder.append(i);
    localStringBuilder.append(" to: ");
    localStringBuilder.append(paramInt);
    localStringBuilder.append(" excludeLastState:");
    localStringBuilder.append(paramBoolean);
    log(localStringBuilder.toString());
    performLifecycleSequence(paramActivityClientRecord, mHelper.getLifecyclePath(i, paramInt, paramBoolean));
  }
  
  private void executeLifecycleState(ClientTransaction paramClientTransaction)
  {
    ActivityLifecycleItem localActivityLifecycleItem = paramClientTransaction.getLifecycleStateRequest();
    if (localActivityLifecycleItem == null) {
      return;
    }
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Resolving lifecycle state: ");
    ((StringBuilder)localObject).append(localActivityLifecycleItem);
    log(((StringBuilder)localObject).toString());
    localObject = paramClientTransaction.getActivityToken();
    paramClientTransaction = mTransactionHandler.getActivityClient((IBinder)localObject);
    if (paramClientTransaction == null) {
      return;
    }
    cycleToPath(paramClientTransaction, localActivityLifecycleItem.getTargetState(), true);
    localActivityLifecycleItem.execute(mTransactionHandler, (IBinder)localObject, mPendingActions);
    localActivityLifecycleItem.postExecute(mTransactionHandler, (IBinder)localObject, mPendingActions);
  }
  
  private static void log(String paramString) {}
  
  private void performLifecycleSequence(ActivityThread.ActivityClientRecord paramActivityClientRecord, IntArray paramIntArray)
  {
    int i = paramIntArray.size();
    for (int j = 0; j < i; j++)
    {
      int k = paramIntArray.get(j);
      Object localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Transitioning to state: ");
      ((StringBuilder)localObject).append(k);
      log(((StringBuilder)localObject).toString());
      switch (k)
      {
      default: 
        paramActivityClientRecord = new StringBuilder();
        paramActivityClientRecord.append("Unexpected lifecycle state: ");
        paramActivityClientRecord.append(k);
        throw new IllegalArgumentException(paramActivityClientRecord.toString());
      case 7: 
        mTransactionHandler.performRestartActivity(token, false);
        break;
      case 6: 
        localObject = mTransactionHandler;
        IBinder localIBinder = token;
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("performLifecycleSequence. cycling to:");
        localStringBuilder.append(paramIntArray.get(i - 1));
        ((ClientTransactionHandler)localObject).handleDestroyActivity(localIBinder, false, 0, false, localStringBuilder.toString());
        break;
      case 5: 
        mTransactionHandler.handleStopActivity(token, false, 0, mPendingActions, false, "LIFECYCLER_STOP_ACTIVITY");
        break;
      case 4: 
        mTransactionHandler.handlePauseActivity(token, false, false, 0, mPendingActions, "LIFECYCLER_PAUSE_ACTIVITY");
        break;
      case 3: 
        mTransactionHandler.handleResumeActivity(token, false, isForward, "LIFECYCLER_RESUME_ACTIVITY");
        break;
      case 2: 
        mTransactionHandler.handleStartActivity(paramActivityClientRecord, mPendingActions);
        break;
      case 1: 
        mTransactionHandler.handleLaunchActivity(paramActivityClientRecord, mPendingActions, null);
      }
    }
  }
  
  @VisibleForTesting
  public void cycleToPath(ActivityThread.ActivityClientRecord paramActivityClientRecord, int paramInt)
  {
    cycleToPath(paramActivityClientRecord, paramInt, false);
  }
  
  public void execute(ClientTransaction paramClientTransaction)
  {
    IBinder localIBinder = paramClientTransaction.getActivityToken();
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Start resolving transaction for client: ");
    localStringBuilder.append(mTransactionHandler);
    localStringBuilder.append(", token: ");
    localStringBuilder.append(localIBinder);
    log(localStringBuilder.toString());
    executeCallbacks(paramClientTransaction);
    executeLifecycleState(paramClientTransaction);
    mPendingActions.clear();
    log("End resolving transaction");
  }
  
  @VisibleForTesting
  public void executeCallbacks(ClientTransaction paramClientTransaction)
  {
    List localList = paramClientTransaction.getCallbacks();
    if (localList == null) {
      return;
    }
    log("Resolving callbacks");
    IBinder localIBinder = paramClientTransaction.getActivityToken();
    Object localObject1 = mTransactionHandler.getActivityClient(localIBinder);
    Object localObject2 = paramClientTransaction.getLifecycleStateRequest();
    int i;
    if (localObject2 != null) {
      i = ((ActivityLifecycleItem)localObject2).getTargetState();
    } else {
      i = -1;
    }
    int j = TransactionExecutorHelper.lastCallbackRequestingState(paramClientTransaction);
    int k = localList.size();
    paramClientTransaction = (ClientTransaction)localObject1;
    int m = 0;
    while (m < k)
    {
      localObject1 = (ClientTransactionItem)localList.get(m);
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append("Resolving callback: ");
      ((StringBuilder)localObject2).append(localObject1);
      log(((StringBuilder)localObject2).toString());
      int n = ((ClientTransactionItem)localObject1).getPostExecutionState();
      int i1 = mHelper.getClosestPreExecutionState(paramClientTransaction, ((ClientTransactionItem)localObject1).getPostExecutionState());
      if (i1 != -1) {
        cycleToPath(paramClientTransaction, i1);
      }
      ((ClientTransactionItem)localObject1).execute(mTransactionHandler, localIBinder, mPendingActions);
      ((ClientTransactionItem)localObject1).postExecute(mTransactionHandler, localIBinder, mPendingActions);
      localObject1 = paramClientTransaction;
      if (paramClientTransaction == null) {
        localObject1 = mTransactionHandler.getActivityClient(localIBinder);
      }
      if ((n != -1) && (localObject1 != null))
      {
        boolean bool;
        if ((m == j) && (i == n)) {
          bool = true;
        } else {
          bool = false;
        }
        cycleToPath((ActivityThread.ActivityClientRecord)localObject1, n, bool);
      }
      m++;
      paramClientTransaction = (ClientTransaction)localObject1;
    }
  }
}
