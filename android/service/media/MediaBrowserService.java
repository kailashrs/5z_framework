package android.service.media;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ParceledListSlice;
import android.media.browse.MediaBrowser.MediaItem;
import android.media.browse.MediaBrowserUtils;
import android.media.session.MediaSession.Token;
import android.media.session.MediaSessionManager.RemoteUserInfo;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.IBinder.DeathRecipient;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.util.ArrayMap;
import android.util.Log;
import android.util.Pair;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public abstract class MediaBrowserService
  extends Service
{
  private static final boolean DBG = false;
  public static final String KEY_MEDIA_ITEM = "media_item";
  private static final int RESULT_ERROR = -1;
  private static final int RESULT_FLAG_ON_LOAD_ITEM_NOT_IMPLEMENTED = 2;
  private static final int RESULT_FLAG_OPTION_NOT_HANDLED = 1;
  private static final int RESULT_OK = 0;
  public static final String SERVICE_INTERFACE = "android.media.browse.MediaBrowserService";
  private static final String TAG = "MediaBrowserService";
  private ServiceBinder mBinder;
  private final ArrayMap<IBinder, ConnectionRecord> mConnections = new ArrayMap();
  private ConnectionRecord mCurConnection;
  private final Handler mHandler = new Handler();
  MediaSession.Token mSession;
  
  public MediaBrowserService() {}
  
  private void addSubscription(String paramString, ConnectionRecord paramConnectionRecord, IBinder paramIBinder, Bundle paramBundle)
  {
    Object localObject1 = (List)subscriptions.get(paramString);
    Object localObject2 = localObject1;
    if (localObject1 == null) {
      localObject2 = new ArrayList();
    }
    Iterator localIterator = ((List)localObject2).iterator();
    while (localIterator.hasNext())
    {
      localObject1 = (Pair)localIterator.next();
      if ((paramIBinder == first) && (MediaBrowserUtils.areSameOptions(paramBundle, (Bundle)second))) {
        return;
      }
    }
    ((List)localObject2).add(new Pair(paramIBinder, paramBundle));
    subscriptions.put(paramString, localObject2);
    performLoadChildren(paramString, paramConnectionRecord, paramBundle);
  }
  
  private List<MediaBrowser.MediaItem> applyOptions(List<MediaBrowser.MediaItem> paramList, Bundle paramBundle)
  {
    if (paramList == null) {
      return null;
    }
    int i = paramBundle.getInt("android.media.browse.extra.PAGE", -1);
    int j = paramBundle.getInt("android.media.browse.extra.PAGE_SIZE", -1);
    if ((i == -1) && (j == -1)) {
      return paramList;
    }
    int k = j * i;
    int m = k + j;
    if ((i >= 0) && (j >= 1) && (k < paramList.size()))
    {
      j = m;
      if (m > paramList.size()) {
        j = paramList.size();
      }
      return paramList.subList(k, j);
    }
    return Collections.EMPTY_LIST;
  }
  
  private boolean isValidPackage(String paramString, int paramInt)
  {
    if (paramString == null) {
      return false;
    }
    String[] arrayOfString = getPackageManager().getPackagesForUid(paramInt);
    int i = arrayOfString.length;
    for (paramInt = 0; paramInt < i; paramInt++) {
      if (arrayOfString[paramInt].equals(paramString)) {
        return true;
      }
    }
    return false;
  }
  
  private void notifyChildrenChangedInternal(final String paramString, final Bundle paramBundle)
  {
    if (paramString != null)
    {
      mHandler.post(new Runnable()
      {
        public void run()
        {
          Iterator localIterator1 = mConnections.keySet().iterator();
          while (localIterator1.hasNext())
          {
            Object localObject1 = (IBinder)localIterator1.next();
            localObject1 = (MediaBrowserService.ConnectionRecord)mConnections.get(localObject1);
            Object localObject2 = (List)subscriptions.get(paramString);
            if (localObject2 != null)
            {
              Iterator localIterator2 = ((List)localObject2).iterator();
              while (localIterator2.hasNext())
              {
                localObject2 = (Pair)localIterator2.next();
                if (MediaBrowserUtils.hasDuplicatedItems(paramBundle, (Bundle)second)) {
                  MediaBrowserService.this.performLoadChildren(paramString, (MediaBrowserService.ConnectionRecord)localObject1, (Bundle)second);
                }
              }
            }
          }
        }
      });
      return;
    }
    throw new IllegalArgumentException("parentId cannot be null in notifyChildrenChanged");
  }
  
  private void performLoadChildren(final String paramString, final ConnectionRecord paramConnectionRecord, final Bundle paramBundle)
  {
    Result local3 = new Result(paramString, paramConnectionRecord)
    {
      void onResultSent(List<MediaBrowser.MediaItem> paramAnonymousList, int paramAnonymousInt)
      {
        if (mConnections.get(paramConnectionRecordcallbacks.asBinder()) != paramConnectionRecord) {
          return;
        }
        if ((paramAnonymousInt & 0x1) != 0) {
          paramAnonymousList = MediaBrowserService.this.applyOptions(paramAnonymousList, paramBundle);
        }
        if (paramAnonymousList == null) {
          paramAnonymousList = null;
        } else {
          paramAnonymousList = new ParceledListSlice(paramAnonymousList);
        }
        try
        {
          paramConnectionRecordcallbacks.onLoadChildrenWithOptions(paramString, paramAnonymousList, paramBundle);
        }
        catch (RemoteException paramAnonymousList)
        {
          paramAnonymousList = new StringBuilder();
          paramAnonymousList.append("Calling onLoadChildren() failed for id=");
          paramAnonymousList.append(paramString);
          paramAnonymousList.append(" package=");
          paramAnonymousList.append(paramConnectionRecordpkg);
          Log.w("MediaBrowserService", paramAnonymousList.toString());
        }
      }
    };
    mCurConnection = paramConnectionRecord;
    if (paramBundle == null) {
      onLoadChildren(paramString, local3);
    } else {
      onLoadChildren(paramString, local3, paramBundle);
    }
    mCurConnection = null;
    if (local3.isDone()) {
      return;
    }
    paramBundle = new StringBuilder();
    paramBundle.append("onLoadChildren must call detach() or sendResult() before returning for package=");
    paramBundle.append(pkg);
    paramBundle.append(" id=");
    paramBundle.append(paramString);
    throw new IllegalStateException(paramBundle.toString());
  }
  
  private void performLoadItem(final String paramString, final ConnectionRecord paramConnectionRecord, final ResultReceiver paramResultReceiver)
  {
    paramResultReceiver = new Result(paramString, paramConnectionRecord)
    {
      void onResultSent(MediaBrowser.MediaItem paramAnonymousMediaItem, int paramAnonymousInt)
      {
        if (mConnections.get(paramConnectionRecordcallbacks.asBinder()) != paramConnectionRecord) {
          return;
        }
        if ((paramAnonymousInt & 0x2) != 0)
        {
          paramResultReceiver.send(-1, null);
          return;
        }
        Bundle localBundle = new Bundle();
        localBundle.putParcelable("media_item", paramAnonymousMediaItem);
        paramResultReceiver.send(0, localBundle);
      }
    };
    mCurConnection = paramConnectionRecord;
    onLoadItem(paramString, paramResultReceiver);
    mCurConnection = null;
    if (paramResultReceiver.isDone()) {
      return;
    }
    paramConnectionRecord = new StringBuilder();
    paramConnectionRecord.append("onLoadItem must call detach() or sendResult() before returning for id=");
    paramConnectionRecord.append(paramString);
    throw new IllegalStateException(paramConnectionRecord.toString());
  }
  
  private boolean removeSubscription(String paramString, ConnectionRecord paramConnectionRecord, IBinder paramIBinder)
  {
    if (paramIBinder == null)
    {
      if (subscriptions.remove(paramString) != null) {
        bool1 = true;
      } else {
        bool1 = false;
      }
      return bool1;
    }
    boolean bool2 = false;
    boolean bool1 = false;
    List localList = (List)subscriptions.get(paramString);
    if (localList != null)
    {
      Iterator localIterator = localList.iterator();
      while (localIterator.hasNext()) {
        if (paramIBinder == nextfirst)
        {
          bool1 = true;
          localIterator.remove();
        }
      }
      bool2 = bool1;
      if (localList.size() == 0)
      {
        subscriptions.remove(paramString);
        bool2 = bool1;
      }
    }
    return bool2;
  }
  
  public void dump(FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString) {}
  
  public final Bundle getBrowserRootHints()
  {
    if (mCurConnection != null)
    {
      Bundle localBundle;
      if (mCurConnection.rootHints == null) {
        localBundle = null;
      } else {
        localBundle = new Bundle(mCurConnection.rootHints);
      }
      return localBundle;
    }
    throw new IllegalStateException("This should be called inside of onGetRoot or onLoadChildren or onLoadItem methods");
  }
  
  public final MediaSessionManager.RemoteUserInfo getCurrentBrowserInfo()
  {
    if (mCurConnection != null) {
      return new MediaSessionManager.RemoteUserInfo(mCurConnection.pkg, mCurConnection.pid, mCurConnection.uid, mCurConnection.callbacks.asBinder());
    }
    throw new IllegalStateException("This should be called inside of onGetRoot or onLoadChildren or onLoadItem methods");
  }
  
  public MediaSession.Token getSessionToken()
  {
    return mSession;
  }
  
  public void notifyChildrenChanged(String paramString)
  {
    notifyChildrenChangedInternal(paramString, null);
  }
  
  public void notifyChildrenChanged(String paramString, Bundle paramBundle)
  {
    if (paramBundle != null)
    {
      notifyChildrenChangedInternal(paramString, paramBundle);
      return;
    }
    throw new IllegalArgumentException("options cannot be null in notifyChildrenChanged");
  }
  
  public IBinder onBind(Intent paramIntent)
  {
    if ("android.media.browse.MediaBrowserService".equals(paramIntent.getAction())) {
      return mBinder;
    }
    return null;
  }
  
  public void onCreate()
  {
    super.onCreate();
    mBinder = new ServiceBinder(null);
  }
  
  public abstract BrowserRoot onGetRoot(String paramString, int paramInt, Bundle paramBundle);
  
  public abstract void onLoadChildren(String paramString, Result<List<MediaBrowser.MediaItem>> paramResult);
  
  public void onLoadChildren(String paramString, Result<List<MediaBrowser.MediaItem>> paramResult, Bundle paramBundle)
  {
    paramResult.setFlags(1);
    onLoadChildren(paramString, paramResult);
  }
  
  public void onLoadItem(String paramString, Result<MediaBrowser.MediaItem> paramResult)
  {
    paramResult.setFlags(2);
    paramResult.sendResult(null);
  }
  
  public void setSessionToken(final MediaSession.Token paramToken)
  {
    if (paramToken != null)
    {
      if (mSession == null)
      {
        mSession = paramToken;
        mHandler.post(new Runnable()
        {
          public void run()
          {
            Iterator localIterator = mConnections.values().iterator();
            while (localIterator.hasNext())
            {
              MediaBrowserService.ConnectionRecord localConnectionRecord = (MediaBrowserService.ConnectionRecord)localIterator.next();
              try
              {
                callbacks.onConnect(root.getRootId(), paramToken, root.getExtras());
              }
              catch (RemoteException localRemoteException)
              {
                StringBuilder localStringBuilder = new StringBuilder();
                localStringBuilder.append("Connection for ");
                localStringBuilder.append(pkg);
                localStringBuilder.append(" is no longer valid.");
                Log.w("MediaBrowserService", localStringBuilder.toString());
                localIterator.remove();
              }
            }
          }
        });
        return;
      }
      throw new IllegalStateException("The session token has already been set.");
    }
    throw new IllegalArgumentException("Session token may not be null.");
  }
  
  public static final class BrowserRoot
  {
    public static final String EXTRA_OFFLINE = "android.service.media.extra.OFFLINE";
    public static final String EXTRA_RECENT = "android.service.media.extra.RECENT";
    public static final String EXTRA_SUGGESTED = "android.service.media.extra.SUGGESTED";
    private final Bundle mExtras;
    private final String mRootId;
    
    public BrowserRoot(String paramString, Bundle paramBundle)
    {
      if (paramString != null)
      {
        mRootId = paramString;
        mExtras = paramBundle;
        return;
      }
      throw new IllegalArgumentException("The root id in BrowserRoot cannot be null. Use null for BrowserRoot instead.");
    }
    
    public Bundle getExtras()
    {
      return mExtras;
    }
    
    public String getRootId()
    {
      return mRootId;
    }
  }
  
  private class ConnectionRecord
    implements IBinder.DeathRecipient
  {
    IMediaBrowserServiceCallbacks callbacks;
    int pid;
    String pkg;
    MediaBrowserService.BrowserRoot root;
    Bundle rootHints;
    HashMap<String, List<Pair<IBinder, Bundle>>> subscriptions = new HashMap();
    int uid;
    
    private ConnectionRecord() {}
    
    public void binderDied()
    {
      mHandler.post(new Runnable()
      {
        public void run()
        {
          mConnections.remove(callbacks.asBinder());
        }
      });
    }
  }
  
  public class Result<T>
  {
    private Object mDebug;
    private boolean mDetachCalled;
    private int mFlags;
    private boolean mSendResultCalled;
    
    Result(Object paramObject)
    {
      mDebug = paramObject;
    }
    
    public void detach()
    {
      if (!mDetachCalled)
      {
        if (!mSendResultCalled)
        {
          mDetachCalled = true;
          return;
        }
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("detach() called when sendResult() had already been called for: ");
        localStringBuilder.append(mDebug);
        throw new IllegalStateException(localStringBuilder.toString());
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("detach() called when detach() had already been called for: ");
      localStringBuilder.append(mDebug);
      throw new IllegalStateException(localStringBuilder.toString());
    }
    
    boolean isDone()
    {
      boolean bool;
      if ((!mDetachCalled) && (!mSendResultCalled)) {
        bool = false;
      } else {
        bool = true;
      }
      return bool;
    }
    
    void onResultSent(T paramT, int paramInt) {}
    
    public void sendResult(T paramT)
    {
      if (!mSendResultCalled)
      {
        mSendResultCalled = true;
        onResultSent(paramT, mFlags);
        return;
      }
      paramT = new StringBuilder();
      paramT.append("sendResult() called twice for: ");
      paramT.append(mDebug);
      throw new IllegalStateException(paramT.toString());
    }
    
    void setFlags(int paramInt)
    {
      mFlags = paramInt;
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  private static @interface ResultFlags {}
  
  private class ServiceBinder
    extends IMediaBrowserService.Stub
  {
    private ServiceBinder() {}
    
    public void addSubscription(final String paramString, final IBinder paramIBinder, final Bundle paramBundle, final IMediaBrowserServiceCallbacks paramIMediaBrowserServiceCallbacks)
    {
      mHandler.post(new Runnable()
      {
        public void run()
        {
          Object localObject = paramIMediaBrowserServiceCallbacks.asBinder();
          localObject = (MediaBrowserService.ConnectionRecord)mConnections.get(localObject);
          if (localObject == null)
          {
            localObject = new StringBuilder();
            ((StringBuilder)localObject).append("addSubscription for callback that isn't registered id=");
            ((StringBuilder)localObject).append(paramString);
            Log.w("MediaBrowserService", ((StringBuilder)localObject).toString());
            return;
          }
          MediaBrowserService.this.addSubscription(paramString, (MediaBrowserService.ConnectionRecord)localObject, paramIBinder, paramBundle);
        }
      });
    }
    
    public void addSubscriptionDeprecated(String paramString, IMediaBrowserServiceCallbacks paramIMediaBrowserServiceCallbacks) {}
    
    public void connect(final String paramString, final Bundle paramBundle, final IMediaBrowserServiceCallbacks paramIMediaBrowserServiceCallbacks)
    {
      final int i = Binder.getCallingPid();
      final int j = Binder.getCallingUid();
      if (MediaBrowserService.this.isValidPackage(paramString, j))
      {
        mHandler.post(new Runnable()
        {
          public void run()
          {
            Object localObject = paramIMediaBrowserServiceCallbacks.asBinder();
            mConnections.remove(localObject);
            MediaBrowserService.ConnectionRecord localConnectionRecord = new MediaBrowserService.ConnectionRecord(MediaBrowserService.this, null);
            pkg = paramString;
            pid = i;
            uid = j;
            rootHints = paramBundle;
            callbacks = paramIMediaBrowserServiceCallbacks;
            MediaBrowserService.access$402(MediaBrowserService.this, localConnectionRecord);
            root = onGetRoot(paramString, j, paramBundle);
            MediaBrowserService.access$402(MediaBrowserService.this, null);
            StringBuilder localStringBuilder1;
            if (root == null)
            {
              localObject = new StringBuilder();
              ((StringBuilder)localObject).append("No root for client ");
              ((StringBuilder)localObject).append(paramString);
              ((StringBuilder)localObject).append(" from service ");
              ((StringBuilder)localObject).append(getClass().getName());
              Log.i("MediaBrowserService", ((StringBuilder)localObject).toString());
              try
              {
                paramIMediaBrowserServiceCallbacks.onConnectFailed();
              }
              catch (RemoteException localRemoteException1)
              {
                localStringBuilder1 = new StringBuilder();
                localStringBuilder1.append("Calling onConnectFailed() failed. Ignoring. pkg=");
                localStringBuilder1.append(paramString);
                Log.w("MediaBrowserService", localStringBuilder1.toString());
              }
            }
            else
            {
              try
              {
                mConnections.put(localStringBuilder1, localConnectionRecord);
                localStringBuilder1.linkToDeath(localConnectionRecord, 0);
                if (mSession != null) {
                  paramIMediaBrowserServiceCallbacks.onConnect(root.getRootId(), mSession, root.getExtras());
                }
              }
              catch (RemoteException localRemoteException2)
              {
                StringBuilder localStringBuilder2 = new StringBuilder();
                localStringBuilder2.append("Calling onConnect() failed. Dropping client. pkg=");
                localStringBuilder2.append(paramString);
                Log.w("MediaBrowserService", localStringBuilder2.toString());
                mConnections.remove(localStringBuilder1);
              }
            }
          }
        });
        return;
      }
      paramBundle = new StringBuilder();
      paramBundle.append("Package/uid mismatch: uid=");
      paramBundle.append(j);
      paramBundle.append(" package=");
      paramBundle.append(paramString);
      throw new IllegalArgumentException(paramBundle.toString());
    }
    
    public void disconnect(final IMediaBrowserServiceCallbacks paramIMediaBrowserServiceCallbacks)
    {
      mHandler.post(new Runnable()
      {
        public void run()
        {
          Object localObject = paramIMediaBrowserServiceCallbacks.asBinder();
          localObject = (MediaBrowserService.ConnectionRecord)mConnections.remove(localObject);
          if (localObject != null) {
            callbacks.asBinder().unlinkToDeath((IBinder.DeathRecipient)localObject, 0);
          }
        }
      });
    }
    
    public void getMediaItem(final String paramString, final ResultReceiver paramResultReceiver, final IMediaBrowserServiceCallbacks paramIMediaBrowserServiceCallbacks)
    {
      mHandler.post(new Runnable()
      {
        public void run()
        {
          Object localObject = paramIMediaBrowserServiceCallbacks.asBinder();
          localObject = (MediaBrowserService.ConnectionRecord)mConnections.get(localObject);
          if (localObject == null)
          {
            localObject = new StringBuilder();
            ((StringBuilder)localObject).append("getMediaItem for callback that isn't registered id=");
            ((StringBuilder)localObject).append(paramString);
            Log.w("MediaBrowserService", ((StringBuilder)localObject).toString());
            return;
          }
          MediaBrowserService.this.performLoadItem(paramString, (MediaBrowserService.ConnectionRecord)localObject, paramResultReceiver);
        }
      });
    }
    
    public void removeSubscription(final String paramString, final IBinder paramIBinder, final IMediaBrowserServiceCallbacks paramIMediaBrowserServiceCallbacks)
    {
      mHandler.post(new Runnable()
      {
        public void run()
        {
          Object localObject = paramIMediaBrowserServiceCallbacks.asBinder();
          localObject = (MediaBrowserService.ConnectionRecord)mConnections.get(localObject);
          if (localObject == null)
          {
            localObject = new StringBuilder();
            ((StringBuilder)localObject).append("removeSubscription for callback that isn't registered id=");
            ((StringBuilder)localObject).append(paramString);
            Log.w("MediaBrowserService", ((StringBuilder)localObject).toString());
            return;
          }
          if (!MediaBrowserService.this.removeSubscription(paramString, (MediaBrowserService.ConnectionRecord)localObject, paramIBinder))
          {
            localObject = new StringBuilder();
            ((StringBuilder)localObject).append("removeSubscription called for ");
            ((StringBuilder)localObject).append(paramString);
            ((StringBuilder)localObject).append(" which is not subscribed");
            Log.w("MediaBrowserService", ((StringBuilder)localObject).toString());
          }
        }
      });
    }
    
    public void removeSubscriptionDeprecated(String paramString, IMediaBrowserServiceCallbacks paramIMediaBrowserServiceCallbacks) {}
  }
}
