package android.os;

import android.util.Log;

public class UpdateLock
{
  private static final boolean DEBUG = false;
  public static final String NOW_IS_CONVENIENT = "nowisconvenient";
  private static final String TAG = "UpdateLock";
  public static final String TIMESTAMP = "timestamp";
  public static final String UPDATE_LOCK_CHANGED = "android.os.UpdateLock.UPDATE_LOCK_CHANGED";
  private static IUpdateLock sService;
  int mCount = 0;
  boolean mHeld = false;
  boolean mRefCounted = true;
  final String mTag;
  IBinder mToken;
  
  public UpdateLock(String paramString)
  {
    mTag = paramString;
    mToken = new Binder();
  }
  
  private void acquireLocked()
  {
    if (mRefCounted)
    {
      int i = mCount;
      mCount = (i + 1);
      if (i != 0) {}
    }
    else
    {
      if (sService != null) {
        try
        {
          sService.acquireUpdateLock(mToken, mTag);
        }
        catch (RemoteException localRemoteException)
        {
          Log.e("UpdateLock", "Unable to contact service to acquire");
        }
      }
      mHeld = true;
    }
  }
  
  private static void checkService()
  {
    if (sService == null) {
      sService = IUpdateLock.Stub.asInterface(ServiceManager.getService("updatelock"));
    }
  }
  
  private void releaseLocked()
  {
    if (mRefCounted)
    {
      int i = mCount - 1;
      mCount = i;
      if (i != 0) {}
    }
    else
    {
      if (sService != null) {
        try
        {
          sService.releaseUpdateLock(mToken);
        }
        catch (RemoteException localRemoteException)
        {
          Log.e("UpdateLock", "Unable to contact service to release");
        }
      }
      mHeld = false;
    }
    if (mCount >= 0) {
      return;
    }
    throw new RuntimeException("UpdateLock under-locked");
  }
  
  public void acquire()
  {
    
    synchronized (mToken)
    {
      acquireLocked();
      return;
    }
  }
  
  protected void finalize()
    throws Throwable
  {
    synchronized (mToken)
    {
      if (mHeld)
      {
        Log.wtf("UpdateLock", "UpdateLock finalized while still held");
        try
        {
          sService.releaseUpdateLock(mToken);
        }
        catch (RemoteException localRemoteException)
        {
          Log.e("UpdateLock", "Unable to contact service to release");
        }
      }
      return;
    }
  }
  
  public boolean isHeld()
  {
    synchronized (mToken)
    {
      boolean bool = mHeld;
      return bool;
    }
  }
  
  public void release()
  {
    
    synchronized (mToken)
    {
      releaseLocked();
      return;
    }
  }
  
  public void setReferenceCounted(boolean paramBoolean)
  {
    mRefCounted = paramBoolean;
  }
}
