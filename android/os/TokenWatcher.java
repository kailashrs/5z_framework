package android.os;

import android.util.Log;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.WeakHashMap;

public abstract class TokenWatcher
{
  private volatile boolean mAcquired = false;
  private Handler mHandler;
  private int mNotificationQueue = -1;
  private Runnable mNotificationTask = new Runnable()
  {
    public void run()
    {
      synchronized (mTokens)
      {
        int i = mNotificationQueue;
        TokenWatcher.access$102(TokenWatcher.this, -1);
        if (i == 1) {
          acquired();
        } else if (i == 0) {
          released();
        }
        return;
      }
    }
  };
  private String mTag;
  private WeakHashMap<IBinder, Death> mTokens = new WeakHashMap();
  
  public TokenWatcher(Handler paramHandler, String paramString)
  {
    mHandler = paramHandler;
    if (paramString != null) {
      paramHandler = paramString;
    } else {
      paramHandler = "TokenWatcher";
    }
    mTag = paramHandler;
  }
  
  private ArrayList<String> dumpInternal()
  {
    ArrayList localArrayList = new ArrayList();
    synchronized (mTokens)
    {
      Object localObject2 = mTokens.keySet();
      StringBuilder localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append("Token count: ");
      localStringBuilder.append(mTokens.size());
      localArrayList.add(localStringBuilder.toString());
      int i = 0;
      Iterator localIterator = ((Set)localObject2).iterator();
      while (localIterator.hasNext())
      {
        localObject2 = (IBinder)localIterator.next();
        localStringBuilder = new java/lang/StringBuilder;
        localStringBuilder.<init>();
        localStringBuilder.append("[");
        localStringBuilder.append(i);
        localStringBuilder.append("] ");
        localStringBuilder.append(mTokens.get(localObject2)).tag);
        localStringBuilder.append(" - ");
        localStringBuilder.append(localObject2);
        localArrayList.add(localStringBuilder.toString());
        i++;
      }
      return localArrayList;
    }
  }
  
  private void sendNotificationLocked(boolean paramBoolean)
  {
    if (mNotificationQueue == -1)
    {
      mNotificationQueue = paramBoolean;
      mHandler.post(mNotificationTask);
    }
    else if (mNotificationQueue != paramBoolean)
    {
      mNotificationQueue = -1;
      mHandler.removeCallbacks(mNotificationTask);
    }
  }
  
  public void acquire(IBinder paramIBinder, String paramString)
  {
    synchronized (mTokens)
    {
      if (mTokens.containsKey(paramIBinder)) {
        return;
      }
      int i = mTokens.size();
      Death localDeath = new android/os/TokenWatcher$Death;
      localDeath.<init>(this, paramIBinder, paramString);
      try
      {
        paramIBinder.linkToDeath(localDeath, 0);
        mTokens.put(paramIBinder, localDeath);
        if ((i == 0) && (!mAcquired))
        {
          sendNotificationLocked(true);
          mAcquired = true;
        }
        return;
      }
      catch (RemoteException paramIBinder) {}
    }
  }
  
  public abstract void acquired();
  
  public void cleanup(IBinder paramIBinder, boolean paramBoolean)
  {
    synchronized (mTokens)
    {
      paramIBinder = (Death)mTokens.remove(paramIBinder);
      if ((paramBoolean) && (paramIBinder != null))
      {
        token.unlinkToDeath(paramIBinder, 0);
        token = null;
      }
      if ((mTokens.size() == 0) && (mAcquired))
      {
        sendNotificationLocked(false);
        mAcquired = false;
      }
      return;
    }
  }
  
  public void dump()
  {
    Iterator localIterator = dumpInternal().iterator();
    while (localIterator.hasNext())
    {
      String str = (String)localIterator.next();
      Log.i(mTag, str);
    }
  }
  
  public void dump(PrintWriter paramPrintWriter)
  {
    Iterator localIterator = dumpInternal().iterator();
    while (localIterator.hasNext()) {
      paramPrintWriter.println((String)localIterator.next());
    }
  }
  
  public boolean isAcquired()
  {
    synchronized (mTokens)
    {
      boolean bool = mAcquired;
      return bool;
    }
  }
  
  public void release(IBinder paramIBinder)
  {
    cleanup(paramIBinder, true);
  }
  
  public abstract void released();
  
  private class Death
    implements IBinder.DeathRecipient
  {
    String tag;
    IBinder token;
    
    Death(IBinder paramIBinder, String paramString)
    {
      token = paramIBinder;
      tag = paramString;
    }
    
    public void binderDied()
    {
      cleanup(token, false);
    }
    
    protected void finalize()
      throws Throwable
    {
      try
      {
        if (token != null)
        {
          String str = mTag;
          StringBuilder localStringBuilder = new java/lang/StringBuilder;
          localStringBuilder.<init>();
          localStringBuilder.append("cleaning up leaked reference: ");
          localStringBuilder.append(tag);
          Log.w(str, localStringBuilder.toString());
          release(token);
        }
        return;
      }
      finally
      {
        super.finalize();
      }
    }
  }
}
