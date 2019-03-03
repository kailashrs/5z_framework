package android.database.sqlite;

import android.database.CursorWindow;
import android.database.DatabaseUtils;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;

public final class SQLiteSession
{
  public static final int TRANSACTION_MODE_DEFERRED = 0;
  public static final int TRANSACTION_MODE_EXCLUSIVE = 2;
  public static final int TRANSACTION_MODE_IMMEDIATE = 1;
  private SQLiteConnection mConnection;
  private int mConnectionFlags;
  private final SQLiteConnectionPool mConnectionPool;
  private int mConnectionUseCount;
  private Transaction mTransactionPool;
  private Transaction mTransactionStack;
  
  public SQLiteSession(SQLiteConnectionPool paramSQLiteConnectionPool)
  {
    if (paramSQLiteConnectionPool != null)
    {
      mConnectionPool = paramSQLiteConnectionPool;
      return;
    }
    throw new IllegalArgumentException("connectionPool must not be null");
  }
  
  private void acquireConnection(String paramString, int paramInt, CancellationSignal paramCancellationSignal)
  {
    if (mConnection == null)
    {
      mConnection = mConnectionPool.acquireConnection(paramString, paramInt, paramCancellationSignal);
      mConnectionFlags = paramInt;
    }
    mConnectionUseCount += 1;
  }
  
  private void beginTransactionUnchecked(int paramInt1, SQLiteTransactionListener paramSQLiteTransactionListener, int paramInt2, CancellationSignal paramCancellationSignal)
  {
    if (paramCancellationSignal != null) {
      paramCancellationSignal.throwIfCanceled();
    }
    if (mTransactionStack == null) {
      acquireConnection(null, paramInt2, paramCancellationSignal);
    }
    try
    {
      if (mTransactionStack == null)
      {
        SQLiteConnection localSQLiteConnection;
        switch (paramInt1)
        {
        default: 
          localSQLiteConnection = mConnection;
          break;
        case 2: 
          mConnection.execute("BEGIN EXCLUSIVE;", null, paramCancellationSignal);
          break;
        case 1: 
          mConnection.execute("BEGIN IMMEDIATE;", null, paramCancellationSignal);
          break;
        }
        localSQLiteConnection.execute("BEGIN;", null, paramCancellationSignal);
      }
      if (paramSQLiteTransactionListener != null) {
        try
        {
          paramSQLiteTransactionListener.onBegin();
        }
        catch (RuntimeException paramSQLiteTransactionListener)
        {
          if (mTransactionStack == null) {
            mConnection.execute("ROLLBACK;", null, paramCancellationSignal);
          }
          throw paramSQLiteTransactionListener;
        }
      }
      paramSQLiteTransactionListener = obtainTransaction(paramInt1, paramSQLiteTransactionListener);
      mParent = mTransactionStack;
      mTransactionStack = paramSQLiteTransactionListener;
      return;
    }
    finally
    {
      if (mTransactionStack == null) {
        releaseConnection();
      }
    }
  }
  
  private void endTransactionUnchecked(CancellationSignal paramCancellationSignal, boolean paramBoolean)
  {
    if (paramCancellationSignal != null) {
      paramCancellationSignal.throwIfCanceled();
    }
    Transaction localTransaction = mTransactionStack;
    int i;
    if (((mMarkedSuccessful) || (paramBoolean)) && (!mChildFailed)) {
      i = 1;
    } else {
      i = 0;
    }
    Object localObject1 = null;
    SQLiteTransactionListener localSQLiteTransactionListener = mListener;
    int j = i;
    Object localObject2 = localObject1;
    Object localObject3;
    if (localSQLiteTransactionListener != null)
    {
      if (i != 0) {
        try
        {
          localSQLiteTransactionListener.onCommit();
        }
        catch (RuntimeException localRuntimeException)
        {
          break label100;
        }
      } else {
        localSQLiteTransactionListener.onRollback();
      }
      j = i;
      localObject3 = localObject1;
      break label103;
      label100:
      j = 0;
    }
    label103:
    mTransactionStack = mParent;
    recycleTransaction(localTransaction);
    if (mTransactionStack != null)
    {
      if (j == 0) {
        mTransactionStack.mChildFailed = true;
      }
    }
    else
    {
      if (j != 0) {
        try
        {
          mConnection.execute("COMMIT;", null, paramCancellationSignal);
        }
        finally
        {
          break label186;
        }
      } else {
        mConnection.execute("ROLLBACK;", null, paramCancellationSignal);
      }
      releaseConnection();
    }
    if (localObject3 == null) {
      return;
    }
    throw localObject3;
    label186:
    releaseConnection();
    throw paramCancellationSignal;
  }
  
  private boolean executeSpecial(String paramString, Object[] paramArrayOfObject, int paramInt, CancellationSignal paramCancellationSignal)
  {
    if (paramCancellationSignal != null) {
      paramCancellationSignal.throwIfCanceled();
    }
    switch (DatabaseUtils.getSqlStatementType(paramString))
    {
    default: 
      return false;
    case 6: 
      endTransaction(paramCancellationSignal);
      return true;
    case 5: 
      setTransactionSuccessful();
      endTransaction(paramCancellationSignal);
      return true;
    }
    beginTransaction(2, null, paramInt, paramCancellationSignal);
    return true;
  }
  
  private Transaction obtainTransaction(int paramInt, SQLiteTransactionListener paramSQLiteTransactionListener)
  {
    Transaction localTransaction = mTransactionPool;
    if (localTransaction != null)
    {
      mTransactionPool = mParent;
      mParent = null;
      mMarkedSuccessful = false;
      mChildFailed = false;
    }
    else
    {
      localTransaction = new Transaction(null);
    }
    mMode = paramInt;
    mListener = paramSQLiteTransactionListener;
    return localTransaction;
  }
  
  private void recycleTransaction(Transaction paramTransaction)
  {
    mParent = mTransactionPool;
    mListener = null;
    mTransactionPool = paramTransaction;
  }
  
  private void releaseConnection()
  {
    int i = mConnectionUseCount - 1;
    mConnectionUseCount = i;
    if (i == 0) {
      try
      {
        mConnectionPool.releaseConnection(mConnection);
        mConnection = null;
      }
      finally
      {
        mConnection = null;
      }
    }
  }
  
  private void throwIfNestedTransaction()
  {
    if (!hasNestedTransaction()) {
      return;
    }
    throw new IllegalStateException("Cannot perform this operation because a nested transaction is in progress.");
  }
  
  private void throwIfNoTransaction()
  {
    if (mTransactionStack != null) {
      return;
    }
    throw new IllegalStateException("Cannot perform this operation because there is no current transaction.");
  }
  
  private void throwIfTransactionMarkedSuccessful()
  {
    if ((mTransactionStack != null) && (mTransactionStack.mMarkedSuccessful)) {
      throw new IllegalStateException("Cannot perform this operation because the transaction has already been marked successful.  The only thing you can do now is call endTransaction().");
    }
  }
  
  private boolean yieldTransactionUnchecked(long paramLong, CancellationSignal paramCancellationSignal)
  {
    if (paramCancellationSignal != null) {
      paramCancellationSignal.throwIfCanceled();
    }
    if (!mConnectionPool.shouldYieldConnection(mConnection, mConnectionFlags)) {
      return false;
    }
    int i = mTransactionStack.mMode;
    SQLiteTransactionListener localSQLiteTransactionListener = mTransactionStack.mListener;
    int j = mConnectionFlags;
    endTransactionUnchecked(paramCancellationSignal, true);
    if (paramLong > 0L) {
      try
      {
        Thread.sleep(paramLong);
      }
      catch (InterruptedException localInterruptedException) {}
    }
    beginTransactionUnchecked(i, localSQLiteTransactionListener, j, paramCancellationSignal);
    return true;
  }
  
  public void beginTransaction(int paramInt1, SQLiteTransactionListener paramSQLiteTransactionListener, int paramInt2, CancellationSignal paramCancellationSignal)
  {
    throwIfTransactionMarkedSuccessful();
    beginTransactionUnchecked(paramInt1, paramSQLiteTransactionListener, paramInt2, paramCancellationSignal);
  }
  
  public void endTransaction(CancellationSignal paramCancellationSignal)
  {
    throwIfNoTransaction();
    endTransactionUnchecked(paramCancellationSignal, false);
  }
  
  public void execute(String paramString, Object[] paramArrayOfObject, int paramInt, CancellationSignal paramCancellationSignal)
  {
    if (paramString != null)
    {
      if (executeSpecial(paramString, paramArrayOfObject, paramInt, paramCancellationSignal)) {
        return;
      }
      acquireConnection(paramString, paramInt, paramCancellationSignal);
      try
      {
        mConnection.execute(paramString, paramArrayOfObject, paramCancellationSignal);
        return;
      }
      finally
      {
        releaseConnection();
      }
    }
    throw new IllegalArgumentException("sql must not be null.");
  }
  
  public ParcelFileDescriptor executeForBlobFileDescriptor(String paramString, Object[] paramArrayOfObject, int paramInt, CancellationSignal paramCancellationSignal)
  {
    if (paramString != null)
    {
      if (executeSpecial(paramString, paramArrayOfObject, paramInt, paramCancellationSignal)) {
        return null;
      }
      acquireConnection(paramString, paramInt, paramCancellationSignal);
      try
      {
        paramString = mConnection.executeForBlobFileDescriptor(paramString, paramArrayOfObject, paramCancellationSignal);
        return paramString;
      }
      finally
      {
        releaseConnection();
      }
    }
    throw new IllegalArgumentException("sql must not be null.");
  }
  
  public int executeForChangedRowCount(String paramString, Object[] paramArrayOfObject, int paramInt, CancellationSignal paramCancellationSignal)
  {
    if (paramString != null)
    {
      if (executeSpecial(paramString, paramArrayOfObject, paramInt, paramCancellationSignal)) {
        return 0;
      }
      acquireConnection(paramString, paramInt, paramCancellationSignal);
      try
      {
        paramInt = mConnection.executeForChangedRowCount(paramString, paramArrayOfObject, paramCancellationSignal);
        return paramInt;
      }
      finally
      {
        releaseConnection();
      }
    }
    throw new IllegalArgumentException("sql must not be null.");
  }
  
  public int executeForCursorWindow(String paramString, Object[] paramArrayOfObject, CursorWindow paramCursorWindow, int paramInt1, int paramInt2, boolean paramBoolean, int paramInt3, CancellationSignal paramCancellationSignal)
  {
    if (paramString != null)
    {
      if (paramCursorWindow != null)
      {
        if (executeSpecial(paramString, paramArrayOfObject, paramInt3, paramCancellationSignal))
        {
          paramCursorWindow.clear();
          return 0;
        }
        acquireConnection(paramString, paramInt3, paramCancellationSignal);
        try
        {
          paramInt1 = mConnection.executeForCursorWindow(paramString, paramArrayOfObject, paramCursorWindow, paramInt1, paramInt2, paramBoolean, paramCancellationSignal);
          return paramInt1;
        }
        finally
        {
          releaseConnection();
        }
      }
      throw new IllegalArgumentException("window must not be null.");
    }
    throw new IllegalArgumentException("sql must not be null.");
  }
  
  public long executeForLastInsertedRowId(String paramString, Object[] paramArrayOfObject, int paramInt, CancellationSignal paramCancellationSignal)
  {
    if (paramString != null)
    {
      if (executeSpecial(paramString, paramArrayOfObject, paramInt, paramCancellationSignal)) {
        return 0L;
      }
      acquireConnection(paramString, paramInt, paramCancellationSignal);
      try
      {
        long l = mConnection.executeForLastInsertedRowId(paramString, paramArrayOfObject, paramCancellationSignal);
        return l;
      }
      finally
      {
        releaseConnection();
      }
    }
    throw new IllegalArgumentException("sql must not be null.");
  }
  
  public long executeForLong(String paramString, Object[] paramArrayOfObject, int paramInt, CancellationSignal paramCancellationSignal)
  {
    if (paramString != null)
    {
      if (executeSpecial(paramString, paramArrayOfObject, paramInt, paramCancellationSignal)) {
        return 0L;
      }
      acquireConnection(paramString, paramInt, paramCancellationSignal);
      try
      {
        long l = mConnection.executeForLong(paramString, paramArrayOfObject, paramCancellationSignal);
        return l;
      }
      finally
      {
        releaseConnection();
      }
    }
    throw new IllegalArgumentException("sql must not be null.");
  }
  
  public String executeForString(String paramString, Object[] paramArrayOfObject, int paramInt, CancellationSignal paramCancellationSignal)
  {
    if (paramString != null)
    {
      if (executeSpecial(paramString, paramArrayOfObject, paramInt, paramCancellationSignal)) {
        return null;
      }
      acquireConnection(paramString, paramInt, paramCancellationSignal);
      try
      {
        paramString = mConnection.executeForString(paramString, paramArrayOfObject, paramCancellationSignal);
        return paramString;
      }
      finally
      {
        releaseConnection();
      }
    }
    throw new IllegalArgumentException("sql must not be null.");
  }
  
  public boolean hasConnection()
  {
    boolean bool;
    if (mConnection != null) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean hasNestedTransaction()
  {
    boolean bool;
    if ((mTransactionStack != null) && (mTransactionStack.mParent != null)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean hasTransaction()
  {
    boolean bool;
    if (mTransactionStack != null) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void prepare(String paramString, int paramInt, CancellationSignal paramCancellationSignal, SQLiteStatementInfo paramSQLiteStatementInfo)
  {
    if (paramString != null)
    {
      if (paramCancellationSignal != null) {
        paramCancellationSignal.throwIfCanceled();
      }
      acquireConnection(paramString, paramInt, paramCancellationSignal);
      try
      {
        mConnection.prepare(paramString, paramSQLiteStatementInfo);
        return;
      }
      finally
      {
        releaseConnection();
      }
    }
    throw new IllegalArgumentException("sql must not be null.");
  }
  
  public void setTransactionSuccessful()
  {
    throwIfNoTransaction();
    throwIfTransactionMarkedSuccessful();
    mTransactionStack.mMarkedSuccessful = true;
  }
  
  public boolean yieldTransaction(long paramLong, boolean paramBoolean, CancellationSignal paramCancellationSignal)
  {
    if (paramBoolean)
    {
      throwIfNoTransaction();
      throwIfTransactionMarkedSuccessful();
      throwIfNestedTransaction();
    }
    else
    {
      if ((mTransactionStack == null) || (mTransactionStack.mMarkedSuccessful) || (mTransactionStack.mParent != null)) {
        break label69;
      }
    }
    if (mTransactionStack.mChildFailed) {
      return false;
    }
    return yieldTransactionUnchecked(paramLong, paramCancellationSignal);
    label69:
    return false;
  }
  
  private static final class Transaction
  {
    public boolean mChildFailed;
    public SQLiteTransactionListener mListener;
    public boolean mMarkedSuccessful;
    public int mMode;
    public Transaction mParent;
    
    private Transaction() {}
  }
}
