package android.accounts;

public abstract interface AccountManagerCallback<V>
{
  public abstract void run(AccountManagerFuture<V> paramAccountManagerFuture);
}
