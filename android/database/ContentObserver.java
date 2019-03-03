package android.database;

import android.net.Uri;
import android.os.Handler;
import android.os.UserHandle;

public abstract class ContentObserver
{
  Handler mHandler;
  private final Object mLock = new Object();
  private Transport mTransport;
  
  public ContentObserver(Handler paramHandler)
  {
    mHandler = paramHandler;
  }
  
  private void dispatchChange(boolean paramBoolean, Uri paramUri, int paramInt)
  {
    if (mHandler == null) {
      onChange(paramBoolean, paramUri, paramInt);
    } else {
      mHandler.post(new NotificationRunnable(paramBoolean, paramUri, paramInt));
    }
  }
  
  public boolean deliverSelfNotifications()
  {
    return false;
  }
  
  @Deprecated
  public final void dispatchChange(boolean paramBoolean)
  {
    dispatchChange(paramBoolean, null);
  }
  
  public final void dispatchChange(boolean paramBoolean, Uri paramUri)
  {
    dispatchChange(paramBoolean, paramUri, UserHandle.getCallingUserId());
  }
  
  public IContentObserver getContentObserver()
  {
    synchronized (mLock)
    {
      if (mTransport == null)
      {
        localTransport = new android/database/ContentObserver$Transport;
        localTransport.<init>(this);
        mTransport = localTransport;
      }
      Transport localTransport = mTransport;
      return localTransport;
    }
  }
  
  public void onChange(boolean paramBoolean) {}
  
  public void onChange(boolean paramBoolean, Uri paramUri)
  {
    onChange(paramBoolean);
  }
  
  public void onChange(boolean paramBoolean, Uri paramUri, int paramInt)
  {
    onChange(paramBoolean, paramUri);
  }
  
  public IContentObserver releaseContentObserver()
  {
    synchronized (mLock)
    {
      Transport localTransport = mTransport;
      if (localTransport != null)
      {
        localTransport.releaseContentObserver();
        mTransport = null;
      }
      return localTransport;
    }
  }
  
  private final class NotificationRunnable
    implements Runnable
  {
    private final boolean mSelfChange;
    private final Uri mUri;
    private final int mUserId;
    
    public NotificationRunnable(boolean paramBoolean, Uri paramUri, int paramInt)
    {
      mSelfChange = paramBoolean;
      mUri = paramUri;
      mUserId = paramInt;
    }
    
    public void run()
    {
      onChange(mSelfChange, mUri, mUserId);
    }
  }
  
  private static final class Transport
    extends IContentObserver.Stub
  {
    private ContentObserver mContentObserver;
    
    public Transport(ContentObserver paramContentObserver)
    {
      mContentObserver = paramContentObserver;
    }
    
    public void onChange(boolean paramBoolean, Uri paramUri, int paramInt)
    {
      ContentObserver localContentObserver = mContentObserver;
      if (localContentObserver != null) {
        localContentObserver.dispatchChange(paramBoolean, paramUri, paramInt);
      }
    }
    
    public void releaseContentObserver()
    {
      mContentObserver = null;
    }
  }
}
