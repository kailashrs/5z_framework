package android.location;

import android.content.Context;
import android.os.Handler;
import android.os.RemoteException;
import android.util.Log;
import com.android.internal.util.Preconditions;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

abstract class LocalListenerHelper<TListener>
{
  private final Context mContext;
  private final HashMap<TListener, Handler> mListeners = new HashMap();
  private final String mTag;
  
  protected LocalListenerHelper(Context paramContext, String paramString)
  {
    Preconditions.checkNotNull(paramString);
    mContext = paramContext;
    mTag = paramString;
  }
  
  private void executeOperation(ListenerOperation<TListener> paramListenerOperation, TListener paramTListener)
  {
    try
    {
      paramListenerOperation.execute(paramTListener);
    }
    catch (RemoteException paramListenerOperation)
    {
      Log.e(mTag, "Error in monitored listener.", paramListenerOperation);
    }
  }
  
  public boolean add(TListener paramTListener, Handler paramHandler)
  {
    Preconditions.checkNotNull(paramTListener);
    synchronized (mListeners)
    {
      boolean bool = mListeners.isEmpty();
      if (bool) {
        try
        {
          bool = registerWithServer();
          if (!bool)
          {
            Log.e(mTag, "Unable to register listener transport.");
            return false;
          }
        }
        catch (RemoteException paramTListener)
        {
          Log.e(mTag, "Error handling first listener.", paramTListener);
          return false;
        }
      }
      if (mListeners.containsKey(paramTListener)) {
        return true;
      }
      mListeners.put(paramTListener, paramHandler);
      return true;
    }
  }
  
  protected void foreach(final ListenerOperation<TListener> paramListenerOperation)
  {
    synchronized (mListeners)
    {
      Object localObject2 = new java/util/ArrayList;
      ((ArrayList)localObject2).<init>(mListeners.entrySet());
      ??? = ((Collection)localObject2).iterator();
      while (((Iterator)???).hasNext())
      {
        localObject2 = (Map.Entry)((Iterator)???).next();
        if (((Map.Entry)localObject2).getValue() == null) {
          executeOperation(paramListenerOperation, ((Map.Entry)localObject2).getKey());
        } else {
          ((Handler)((Map.Entry)localObject2).getValue()).post(new Runnable()
          {
            public void run()
            {
              LocalListenerHelper.this.executeOperation(paramListenerOperation, val$listener.getKey());
            }
          });
        }
      }
      return;
    }
  }
  
  protected Context getContext()
  {
    return mContext;
  }
  
  protected abstract boolean registerWithServer()
    throws RemoteException;
  
  public void remove(TListener paramTListener)
  {
    Preconditions.checkNotNull(paramTListener);
    synchronized (mListeners)
    {
      boolean bool = mListeners.containsKey(paramTListener);
      mListeners.remove(paramTListener);
      if (bool)
      {
        bool = mListeners.isEmpty();
        if (bool)
        {
          i = 1;
          break label55;
        }
      }
      int i = 0;
      label55:
      if (i != 0) {
        try
        {
          unregisterFromServer();
        }
        catch (RemoteException paramTListener)
        {
          Log.v(mTag, "Error handling last listener removal", paramTListener);
        }
      }
      return;
    }
  }
  
  protected abstract void unregisterFromServer()
    throws RemoteException;
  
  protected static abstract interface ListenerOperation<TListener>
  {
    public abstract void execute(TListener paramTListener)
      throws RemoteException;
  }
}
