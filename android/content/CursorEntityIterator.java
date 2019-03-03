package android.content;

import android.database.Cursor;
import android.os.RemoteException;

public abstract class CursorEntityIterator
  implements EntityIterator
{
  private final Cursor mCursor;
  private boolean mIsClosed = false;
  
  public CursorEntityIterator(Cursor paramCursor)
  {
    mCursor = paramCursor;
    mCursor.moveToFirst();
  }
  
  public final void close()
  {
    if (!mIsClosed)
    {
      mIsClosed = true;
      mCursor.close();
      return;
    }
    throw new IllegalStateException("closing when already closed");
  }
  
  public abstract Entity getEntityAndIncrementCursor(Cursor paramCursor)
    throws RemoteException;
  
  public final boolean hasNext()
  {
    if (!mIsClosed) {
      return mCursor.isAfterLast() ^ true;
    }
    throw new IllegalStateException("calling hasNext() when the iterator is closed");
  }
  
  public Entity next()
  {
    if (!mIsClosed)
    {
      if (hasNext()) {
        try
        {
          Entity localEntity = getEntityAndIncrementCursor(mCursor);
          return localEntity;
        }
        catch (RemoteException localRemoteException)
        {
          throw new RuntimeException("caught a remote exception, this process will die soon", localRemoteException);
        }
      }
      throw new IllegalStateException("you may only call next() if hasNext() is true");
    }
    throw new IllegalStateException("calling next() when the iterator is closed");
  }
  
  public void remove()
  {
    throw new UnsupportedOperationException("remove not supported by EntityIterators");
  }
  
  public final void reset()
  {
    if (!mIsClosed)
    {
      mCursor.moveToFirst();
      return;
    }
    throw new IllegalStateException("calling reset() when the iterator is closed");
  }
}
