package android.os;

import android.util.ArrayMap;
import android.util.Slog;
import java.io.PrintWriter;
import java.util.function.Consumer;

public class RemoteCallbackList<E extends IInterface>
{
  private static final String TAG = "RemoteCallbackList";
  private Object[] mActiveBroadcast;
  private int mBroadcastCount = -1;
  ArrayMap<IBinder, RemoteCallbackList<E>.Callback> mCallbacks = new ArrayMap();
  private boolean mKilled = false;
  private StringBuilder mRecentCallers;
  
  public RemoteCallbackList() {}
  
  private void logExcessiveCallbacks()
  {
    long l = mCallbacks.size();
    if (l >= 3000L)
    {
      if ((l == 3000L) && (mRecentCallers == null)) {
        mRecentCallers = new StringBuilder();
      }
      if ((mRecentCallers != null) && (mRecentCallers.length() < 1000L))
      {
        mRecentCallers.append(Debug.getCallers(5));
        mRecentCallers.append('\n');
        if (mRecentCallers.length() >= 1000L)
        {
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("More than 3000 remote callbacks registered. Recent callers:\n");
          localStringBuilder.append(mRecentCallers.toString());
          Slog.wtf("RemoteCallbackList", localStringBuilder.toString());
          mRecentCallers = null;
        }
      }
    }
  }
  
  public int beginBroadcast()
  {
    synchronized (mCallbacks)
    {
      if (mBroadcastCount <= 0)
      {
        int i = mCallbacks.size();
        mBroadcastCount = i;
        int j = 0;
        if (i <= 0) {
          return 0;
        }
        Object[] arrayOfObject = mActiveBroadcast;
        if (arrayOfObject != null)
        {
          localObject1 = arrayOfObject;
          if (arrayOfObject.length >= i) {}
        }
        else
        {
          arrayOfObject = new Object[i];
          localObject1 = arrayOfObject;
          mActiveBroadcast = arrayOfObject;
        }
        while (j < i)
        {
          localObject1[j] = mCallbacks.valueAt(j);
          j++;
        }
        return i;
      }
      Object localObject1 = new java/lang/IllegalStateException;
      ((IllegalStateException)localObject1).<init>("beginBroadcast() called while already in a broadcast");
      throw ((Throwable)localObject1);
    }
  }
  
  public void broadcast(Consumer<E> paramConsumer)
  {
    int i = beginBroadcast();
    int j = 0;
    for (;;)
    {
      if (j < i) {}
      try
      {
        paramConsumer.accept(getBroadcastItem(j));
        j++;
      }
      finally
      {
        finishBroadcast();
      }
    }
  }
  
  public <C> void broadcastForEachCookie(Consumer<C> paramConsumer)
  {
    int i = beginBroadcast();
    int j = 0;
    for (;;)
    {
      if (j < i) {}
      try
      {
        paramConsumer.accept(getBroadcastCookie(j));
        j++;
      }
      finally
      {
        finishBroadcast();
      }
    }
  }
  
  public void dump(PrintWriter paramPrintWriter, String paramString)
  {
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("callbacks: ");
    paramPrintWriter.println(mCallbacks.size());
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("killed: ");
    paramPrintWriter.println(mKilled);
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("broadcasts count: ");
    paramPrintWriter.println(mBroadcastCount);
  }
  
  public void finishBroadcast()
  {
    synchronized (mCallbacks)
    {
      if (mBroadcastCount >= 0)
      {
        localObject1 = mActiveBroadcast;
        if (localObject1 != null)
        {
          int i = mBroadcastCount;
          for (int j = 0; j < i; j++) {
            localObject1[j] = null;
          }
        }
        mBroadcastCount = -1;
        return;
      }
      Object localObject1 = new java/lang/IllegalStateException;
      ((IllegalStateException)localObject1).<init>("finishBroadcast() called outside of a broadcast");
      throw ((Throwable)localObject1);
    }
  }
  
  public Object getBroadcastCookie(int paramInt)
  {
    return mActiveBroadcast[paramInt]).mCookie;
  }
  
  public E getBroadcastItem(int paramInt)
  {
    return mActiveBroadcast[paramInt]).mCallback;
  }
  
  public Object getRegisteredCallbackCookie(int paramInt)
  {
    synchronized (mCallbacks)
    {
      if (mKilled) {
        return null;
      }
      Object localObject1 = mCallbacks.valueAt(paramInt)).mCookie;
      return localObject1;
    }
  }
  
  public int getRegisteredCallbackCount()
  {
    synchronized (mCallbacks)
    {
      if (mKilled) {
        return 0;
      }
      int i = mCallbacks.size();
      return i;
    }
  }
  
  public E getRegisteredCallbackItem(int paramInt)
  {
    synchronized (mCallbacks)
    {
      if (mKilled) {
        return null;
      }
      IInterface localIInterface = mCallbacks.valueAt(paramInt)).mCallback;
      return localIInterface;
    }
  }
  
  public void kill()
  {
    synchronized (mCallbacks)
    {
      for (int i = mCallbacks.size() - 1; i >= 0; i--)
      {
        Callback localCallback = (Callback)mCallbacks.valueAt(i);
        mCallback.asBinder().unlinkToDeath(localCallback, 0);
      }
      mCallbacks.clear();
      mKilled = true;
      return;
    }
  }
  
  public void onCallbackDied(E paramE) {}
  
  public void onCallbackDied(E paramE, Object paramObject)
  {
    onCallbackDied(paramE);
  }
  
  public boolean register(E paramE)
  {
    return register(paramE, null);
  }
  
  public boolean register(E paramE, Object paramObject)
  {
    synchronized (mCallbacks)
    {
      if (mKilled) {
        return false;
      }
      logExcessiveCallbacks();
      IBinder localIBinder = paramE.asBinder();
      try
      {
        Callback localCallback = new android/os/RemoteCallbackList$Callback;
        localCallback.<init>(this, paramE, paramObject);
        localIBinder.linkToDeath(localCallback, 0);
        mCallbacks.put(localIBinder, localCallback);
        return true;
      }
      catch (RemoteException paramE)
      {
        return false;
      }
    }
  }
  
  public boolean unregister(E paramE)
  {
    synchronized (mCallbacks)
    {
      paramE = (Callback)mCallbacks.remove(paramE.asBinder());
      if (paramE != null)
      {
        mCallback.asBinder().unlinkToDeath(paramE, 0);
        return true;
      }
      return false;
    }
  }
  
  private final class Callback
    implements IBinder.DeathRecipient
  {
    final E mCallback;
    final Object mCookie;
    
    Callback(Object paramObject)
    {
      mCallback = paramObject;
      Object localObject;
      mCookie = localObject;
    }
    
    public void binderDied()
    {
      synchronized (mCallbacks)
      {
        mCallbacks.remove(mCallback.asBinder());
        onCallbackDied(mCallback, mCookie);
        return;
      }
    }
  }
}
