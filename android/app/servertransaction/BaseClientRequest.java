package android.app.servertransaction;

import android.app.ClientTransactionHandler;
import android.os.IBinder;

public abstract interface BaseClientRequest
  extends ObjectPoolItem
{
  public abstract void execute(ClientTransactionHandler paramClientTransactionHandler, IBinder paramIBinder, PendingTransactionActions paramPendingTransactionActions);
  
  public void postExecute(ClientTransactionHandler paramClientTransactionHandler, IBinder paramIBinder, PendingTransactionActions paramPendingTransactionActions) {}
  
  public void preExecute(ClientTransactionHandler paramClientTransactionHandler, IBinder paramIBinder) {}
}
