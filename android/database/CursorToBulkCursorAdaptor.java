package android.database;

import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IBinder.DeathRecipient;
import android.os.Process;
import android.os.RemoteException;

public final class CursorToBulkCursorAdaptor
  extends BulkCursorNative
  implements IBinder.DeathRecipient
{
  private static final String TAG = "Cursor";
  private CrossProcessCursor mCursor;
  private CursorWindow mFilledWindow;
  private final Object mLock = new Object();
  private ContentObserverProxy mObserver;
  private final String mProviderName;
  
  public CursorToBulkCursorAdaptor(Cursor arg1, IContentObserver paramIContentObserver, String paramString)
  {
    if ((??? instanceof CrossProcessCursor)) {
      mCursor = ((CrossProcessCursor)???);
    } else {
      mCursor = new CrossProcessCursorWrapper(???);
    }
    mProviderName = paramString;
    synchronized (mLock)
    {
      createAndRegisterObserverProxyLocked(paramIContentObserver);
      return;
    }
  }
  
  private void closeFilledWindowLocked()
  {
    if (mFilledWindow != null)
    {
      mFilledWindow.close();
      mFilledWindow = null;
    }
  }
  
  private void createAndRegisterObserverProxyLocked(IContentObserver paramIContentObserver)
  {
    if (mObserver == null)
    {
      mObserver = new ContentObserverProxy(paramIContentObserver, this);
      mCursor.registerContentObserver(mObserver);
      return;
    }
    throw new IllegalStateException("an observer is already registered");
  }
  
  private void disposeLocked()
  {
    if (mCursor != null)
    {
      unregisterObserverProxyLocked();
      mCursor.close();
      mCursor = null;
    }
    closeFilledWindowLocked();
  }
  
  private void throwIfCursorIsClosed()
  {
    if (mCursor != null) {
      return;
    }
    throw new StaleDataException("Attempted to access a cursor after it has been closed.");
  }
  
  private void unregisterObserverProxyLocked()
  {
    if (mObserver != null)
    {
      mCursor.unregisterContentObserver(mObserver);
      mObserver.unlinkToDeath(this);
      mObserver = null;
    }
  }
  
  public void binderDied()
  {
    synchronized (mLock)
    {
      disposeLocked();
      return;
    }
  }
  
  public void close()
  {
    synchronized (mLock)
    {
      disposeLocked();
      return;
    }
  }
  
  public void deactivate()
  {
    synchronized (mLock)
    {
      if (mCursor != null)
      {
        unregisterObserverProxyLocked();
        mCursor.deactivate();
      }
      closeFilledWindowLocked();
      return;
    }
  }
  
  public BulkCursorDescriptor getBulkCursorDescriptor()
  {
    synchronized (mLock)
    {
      throwIfCursorIsClosed();
      BulkCursorDescriptor localBulkCursorDescriptor = new android/database/BulkCursorDescriptor;
      localBulkCursorDescriptor.<init>();
      cursor = this;
      columnNames = mCursor.getColumnNames();
      wantsAllOnMoveCalls = mCursor.getWantsAllOnMoveCalls();
      count = mCursor.getCount();
      window = mCursor.getWindow();
      if (window != null) {
        window.acquireReference();
      }
      return localBulkCursorDescriptor;
    }
  }
  
  public Bundle getExtras()
  {
    synchronized (mLock)
    {
      throwIfCursorIsClosed();
      Bundle localBundle = mCursor.getExtras();
      return localBundle;
    }
  }
  
  public CursorWindow getWindow(int paramInt)
  {
    synchronized (mLock)
    {
      throwIfCursorIsClosed();
      if (!mCursor.moveToPosition(paramInt))
      {
        closeFilledWindowLocked();
        return null;
      }
      Object localObject2 = mCursor.getWindow();
      if (localObject2 != null)
      {
        closeFilledWindowLocked();
      }
      else
      {
        CursorWindow localCursorWindow = mFilledWindow;
        if (localCursorWindow == null)
        {
          localObject2 = new android/database/CursorWindow;
          ((CursorWindow)localObject2).<init>(mProviderName);
          mFilledWindow = ((CursorWindow)localObject2);
          localObject2 = mFilledWindow;
        }
        else if (paramInt >= localCursorWindow.getStartPosition())
        {
          localObject2 = localCursorWindow;
          if (paramInt < localCursorWindow.getStartPosition() + localCursorWindow.getNumRows()) {}
        }
        else
        {
          localCursorWindow.clear();
          localObject2 = localCursorWindow;
        }
        mCursor.fillWindow(paramInt, (CursorWindow)localObject2);
      }
      if (localObject2 != null) {
        ((CursorWindow)localObject2).acquireReference();
      }
      return localObject2;
    }
  }
  
  public void onMove(int paramInt)
  {
    synchronized (mLock)
    {
      throwIfCursorIsClosed();
      mCursor.onMove(mCursor.getPosition(), paramInt);
      return;
    }
  }
  
  public int requery(IContentObserver paramIContentObserver)
  {
    synchronized (mLock)
    {
      throwIfCursorIsClosed();
      closeFilledWindowLocked();
      try
      {
        boolean bool = mCursor.requery();
        if (!bool) {
          return -1;
        }
        unregisterObserverProxyLocked();
        createAndRegisterObserverProxyLocked(paramIContentObserver);
        int i = mCursor.getCount();
        return i;
      }
      catch (IllegalStateException localIllegalStateException)
      {
        paramIContentObserver = new java/lang/IllegalStateException;
        StringBuilder localStringBuilder = new java/lang/StringBuilder;
        localStringBuilder.<init>();
        localStringBuilder.append(mProviderName);
        localStringBuilder.append(" Requery misuse db, mCursor isClosed:");
        localStringBuilder.append(mCursor.isClosed());
        paramIContentObserver.<init>(localStringBuilder.toString(), localIllegalStateException);
        throw paramIContentObserver;
      }
    }
  }
  
  public Bundle respond(Bundle paramBundle)
  {
    synchronized (mLock)
    {
      throwIfCursorIsClosed();
      paramBundle = mCursor.respond(paramBundle);
      return paramBundle;
    }
  }
  
  private static final class ContentObserverProxy
    extends ContentObserver
  {
    protected IContentObserver mRemote;
    
    public ContentObserverProxy(IContentObserver paramIContentObserver, IBinder.DeathRecipient paramDeathRecipient)
    {
      super();
      mRemote = paramIContentObserver;
      try
      {
        paramIContentObserver.asBinder().linkToDeath(paramDeathRecipient, 0);
      }
      catch (RemoteException paramIContentObserver) {}
    }
    
    public boolean deliverSelfNotifications()
    {
      return false;
    }
    
    public void onChange(boolean paramBoolean, Uri paramUri)
    {
      try
      {
        mRemote.onChange(paramBoolean, paramUri, Process.myUid());
      }
      catch (RemoteException paramUri) {}
    }
    
    public boolean unlinkToDeath(IBinder.DeathRecipient paramDeathRecipient)
    {
      return mRemote.asBinder().unlinkToDeath(paramDeathRecipient, 0);
    }
  }
}
