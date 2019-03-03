package android.database;

import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

public final class BulkCursorToCursorAdaptor
  extends AbstractWindowedCursor
{
  private static final String TAG = "BulkCursor";
  private IBulkCursor mBulkCursor;
  private String[] mColumns;
  private int mCount;
  private AbstractCursor.SelfContentObserver mObserverBridge = new AbstractCursor.SelfContentObserver(this);
  private boolean mWantsAllOnMoveCalls;
  
  public BulkCursorToCursorAdaptor() {}
  
  private void throwIfCursorIsClosed()
  {
    if (mBulkCursor != null) {
      return;
    }
    throw new StaleDataException("Attempted to access a cursor after it has been closed.");
  }
  
  /* Error */
  public void close()
  {
    // Byte code:
    //   0: aload_0
    //   1: invokespecial 45	android/database/AbstractWindowedCursor:close	()V
    //   4: aload_0
    //   5: getfield 33	android/database/BulkCursorToCursorAdaptor:mBulkCursor	Landroid/database/IBulkCursor;
    //   8: ifnull +43 -> 51
    //   11: aload_0
    //   12: getfield 33	android/database/BulkCursorToCursorAdaptor:mBulkCursor	Landroid/database/IBulkCursor;
    //   15: invokeinterface 48 1 0
    //   20: aload_0
    //   21: aconst_null
    //   22: putfield 33	android/database/BulkCursorToCursorAdaptor:mBulkCursor	Landroid/database/IBulkCursor;
    //   25: goto +26 -> 51
    //   28: astore_1
    //   29: goto +15 -> 44
    //   32: astore_1
    //   33: ldc 8
    //   35: ldc 50
    //   37: invokestatic 56	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   40: pop
    //   41: goto -21 -> 20
    //   44: aload_0
    //   45: aconst_null
    //   46: putfield 33	android/database/BulkCursorToCursorAdaptor:mBulkCursor	Landroid/database/IBulkCursor;
    //   49: aload_1
    //   50: athrow
    //   51: return
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	52	0	this	BulkCursorToCursorAdaptor
    //   28	1	1	localObject	Object
    //   32	18	1	localRemoteException	RemoteException
    // Exception table:
    //   from	to	target	type
    //   11	20	28	finally
    //   33	41	28	finally
    //   11	20	32	android/os/RemoteException
  }
  
  public void deactivate()
  {
    super.deactivate();
    if (mBulkCursor != null) {
      try
      {
        mBulkCursor.deactivate();
      }
      catch (RemoteException localRemoteException)
      {
        Log.w("BulkCursor", "Remote process exception when deactivating");
      }
    }
  }
  
  public String[] getColumnNames()
  {
    throwIfCursorIsClosed();
    return mColumns;
  }
  
  public int getCount()
  {
    throwIfCursorIsClosed();
    return mCount;
  }
  
  public Bundle getExtras()
  {
    throwIfCursorIsClosed();
    try
    {
      Bundle localBundle = mBulkCursor.getExtras();
      return localBundle;
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeException(localRemoteException);
    }
  }
  
  public IContentObserver getObserver()
  {
    return mObserverBridge.getContentObserver();
  }
  
  public void initialize(BulkCursorDescriptor paramBulkCursorDescriptor)
  {
    mBulkCursor = cursor;
    mColumns = columnNames;
    mWantsAllOnMoveCalls = wantsAllOnMoveCalls;
    mCount = count;
    if (window != null) {
      setWindow(window);
    }
  }
  
  public boolean onMove(int paramInt1, int paramInt2)
  {
    throwIfCursorIsClosed();
    try
    {
      if ((mWindow != null) && (paramInt2 >= mWindow.getStartPosition()) && (paramInt2 < mWindow.getStartPosition() + mWindow.getNumRows()))
      {
        if (mWantsAllOnMoveCalls) {
          mBulkCursor.onMove(paramInt2);
        }
      }
      else {
        setWindow(mBulkCursor.getWindow(paramInt2));
      }
      return mWindow != null;
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("BulkCursor", "Unable to get window because the remote process is dead");
    }
    return false;
  }
  
  public boolean requery()
  {
    throwIfCursorIsClosed();
    try
    {
      mCount = mBulkCursor.requery(getObserver());
      if (mCount != -1)
      {
        mPos = -1;
        closeWindow();
        super.requery();
        return true;
      }
      deactivate();
      return false;
    }
    catch (Exception localException)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Unable to requery because the remote process exception ");
      localStringBuilder.append(localException.getMessage());
      Log.e("BulkCursor", localStringBuilder.toString());
      deactivate();
    }
    return false;
  }
  
  public Bundle respond(Bundle paramBundle)
  {
    throwIfCursorIsClosed();
    try
    {
      paramBundle = mBulkCursor.respond(paramBundle);
      return paramBundle;
    }
    catch (RemoteException paramBundle)
    {
      Log.w("BulkCursor", "respond() threw RemoteException, returning an empty bundle.", paramBundle);
    }
    return Bundle.EMPTY;
  }
}
