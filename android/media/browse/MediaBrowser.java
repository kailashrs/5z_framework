package android.media.browse;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ParceledListSlice;
import android.media.MediaDescription;
import android.media.session.MediaSession.Token;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.service.media.IMediaBrowserService;
import android.service.media.IMediaBrowserService.Stub;
import android.service.media.IMediaBrowserServiceCallbacks;
import android.service.media.IMediaBrowserServiceCallbacks.Stub;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

public final class MediaBrowser
{
  private static final int CONNECT_STATE_CONNECTED = 3;
  private static final int CONNECT_STATE_CONNECTING = 2;
  private static final int CONNECT_STATE_DISCONNECTED = 1;
  private static final int CONNECT_STATE_DISCONNECTING = 0;
  private static final int CONNECT_STATE_SUSPENDED = 4;
  private static final boolean DBG = false;
  public static final String EXTRA_PAGE = "android.media.browse.extra.PAGE";
  public static final String EXTRA_PAGE_SIZE = "android.media.browse.extra.PAGE_SIZE";
  private static final String TAG = "MediaBrowser";
  private final ConnectionCallback mCallback;
  private final Context mContext;
  private volatile Bundle mExtras;
  private final Handler mHandler = new Handler();
  private volatile MediaSession.Token mMediaSessionToken;
  private final Bundle mRootHints;
  private volatile String mRootId;
  private IMediaBrowserService mServiceBinder;
  private IMediaBrowserServiceCallbacks mServiceCallbacks;
  private final ComponentName mServiceComponent;
  private MediaServiceConnection mServiceConnection;
  private volatile int mState = 1;
  private final ArrayMap<String, Subscription> mSubscriptions = new ArrayMap();
  
  public MediaBrowser(Context paramContext, ComponentName paramComponentName, ConnectionCallback paramConnectionCallback, Bundle paramBundle)
  {
    if (paramContext != null)
    {
      if (paramComponentName != null)
      {
        if (paramConnectionCallback != null)
        {
          mContext = paramContext;
          mServiceComponent = paramComponentName;
          mCallback = paramConnectionCallback;
          if (paramBundle == null) {
            paramContext = null;
          } else {
            paramContext = new Bundle(paramBundle);
          }
          mRootHints = paramContext;
          return;
        }
        throw new IllegalArgumentException("connection callback must not be null");
      }
      throw new IllegalArgumentException("service component must not be null");
    }
    throw new IllegalArgumentException("context must not be null");
  }
  
  private void forceCloseConnection()
  {
    if (mServiceConnection != null) {
      try
      {
        mContext.unbindService(mServiceConnection);
      }
      catch (IllegalArgumentException localIllegalArgumentException) {}
    }
    mState = 1;
    mServiceConnection = null;
    mServiceBinder = null;
    mServiceCallbacks = null;
    mRootId = null;
    mMediaSessionToken = null;
  }
  
  private ServiceCallbacks getNewServiceCallbacks()
  {
    return new ServiceCallbacks(this);
  }
  
  private static String getStateLabel(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("UNKNOWN/");
      localStringBuilder.append(paramInt);
      return localStringBuilder.toString();
    case 4: 
      return "CONNECT_STATE_SUSPENDED";
    case 3: 
      return "CONNECT_STATE_CONNECTED";
    case 2: 
      return "CONNECT_STATE_CONNECTING";
    case 1: 
      return "CONNECT_STATE_DISCONNECTED";
    }
    return "CONNECT_STATE_DISCONNECTING";
  }
  
  private boolean isCurrent(IMediaBrowserServiceCallbacks paramIMediaBrowserServiceCallbacks, String paramString)
  {
    if ((mServiceCallbacks == paramIMediaBrowserServiceCallbacks) && (mState != 0) && (mState != 1)) {
      return true;
    }
    if ((mState != 0) && (mState != 1))
    {
      paramIMediaBrowserServiceCallbacks = new StringBuilder();
      paramIMediaBrowserServiceCallbacks.append(paramString);
      paramIMediaBrowserServiceCallbacks.append(" for ");
      paramIMediaBrowserServiceCallbacks.append(mServiceComponent);
      paramIMediaBrowserServiceCallbacks.append(" with mServiceConnection=");
      paramIMediaBrowserServiceCallbacks.append(mServiceCallbacks);
      paramIMediaBrowserServiceCallbacks.append(" this=");
      paramIMediaBrowserServiceCallbacks.append(this);
      Log.i("MediaBrowser", paramIMediaBrowserServiceCallbacks.toString());
    }
    return false;
  }
  
  private final void onConnectionFailed(final IMediaBrowserServiceCallbacks paramIMediaBrowserServiceCallbacks)
  {
    mHandler.post(new Runnable()
    {
      public void run()
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("onConnectFailed for ");
        localStringBuilder.append(mServiceComponent);
        Log.e("MediaBrowser", localStringBuilder.toString());
        if (!MediaBrowser.this.isCurrent(paramIMediaBrowserServiceCallbacks, "onConnectFailed")) {
          return;
        }
        if (mState != 2)
        {
          localStringBuilder = new StringBuilder();
          localStringBuilder.append("onConnect from service while mState=");
          localStringBuilder.append(MediaBrowser.getStateLabel(mState));
          localStringBuilder.append("... ignoring");
          Log.w("MediaBrowser", localStringBuilder.toString());
          return;
        }
        MediaBrowser.this.forceCloseConnection();
        mCallback.onConnectionFailed();
      }
    });
  }
  
  private final void onLoadChildren(final IMediaBrowserServiceCallbacks paramIMediaBrowserServiceCallbacks, final String paramString, final ParceledListSlice paramParceledListSlice, final Bundle paramBundle)
  {
    mHandler.post(new Runnable()
    {
      public void run()
      {
        if (!MediaBrowser.this.isCurrent(paramIMediaBrowserServiceCallbacks, "onLoadChildren")) {
          return;
        }
        Object localObject = (MediaBrowser.Subscription)mSubscriptions.get(paramString);
        if (localObject != null)
        {
          MediaBrowser.SubscriptionCallback localSubscriptionCallback = ((MediaBrowser.Subscription)localObject).getCallback(mContext, paramBundle);
          if (localSubscriptionCallback != null)
          {
            if (paramParceledListSlice == null) {
              localObject = null;
            } else {
              localObject = paramParceledListSlice.getList();
            }
            if (paramBundle == null)
            {
              if (localObject == null) {
                localSubscriptionCallback.onError(paramString);
              } else {
                localSubscriptionCallback.onChildrenLoaded(paramString, (List)localObject);
              }
            }
            else if (localObject == null) {
              localSubscriptionCallback.onError(paramString, paramBundle);
            } else {
              localSubscriptionCallback.onChildrenLoaded(paramString, (List)localObject, paramBundle);
            }
            return;
          }
        }
      }
    });
  }
  
  private final void onServiceConnected(final IMediaBrowserServiceCallbacks paramIMediaBrowserServiceCallbacks, final String paramString, final MediaSession.Token paramToken, final Bundle paramBundle)
  {
    mHandler.post(new Runnable()
    {
      public void run()
      {
        if (!MediaBrowser.this.isCurrent(paramIMediaBrowserServiceCallbacks, "onConnect")) {
          return;
        }
        Object localObject1;
        if (mState != 2)
        {
          localObject1 = new StringBuilder();
          ((StringBuilder)localObject1).append("onConnect from service while mState=");
          ((StringBuilder)localObject1).append(MediaBrowser.getStateLabel(mState));
          ((StringBuilder)localObject1).append("... ignoring");
          Log.w("MediaBrowser", ((StringBuilder)localObject1).toString());
          return;
        }
        MediaBrowser.access$1102(MediaBrowser.this, paramString);
        MediaBrowser.access$1202(MediaBrowser.this, paramToken);
        MediaBrowser.access$1302(MediaBrowser.this, paramBundle);
        MediaBrowser.access$002(MediaBrowser.this, 3);
        mCallback.onConnected();
        Iterator localIterator = mSubscriptions.entrySet().iterator();
        while (localIterator.hasNext())
        {
          Object localObject2 = (Map.Entry)localIterator.next();
          localObject1 = (String)((Map.Entry)localObject2).getKey();
          Object localObject3 = (MediaBrowser.Subscription)((Map.Entry)localObject2).getValue();
          localObject2 = ((MediaBrowser.Subscription)localObject3).getCallbacks();
          localObject3 = ((MediaBrowser.Subscription)localObject3).getOptionsList();
          for (int i = 0; i < ((List)localObject2).size(); i++) {
            try
            {
              mServiceBinder.addSubscription((String)localObject1, getmToken, (Bundle)((List)localObject3).get(i), mServiceCallbacks);
            }
            catch (RemoteException localRemoteException)
            {
              StringBuilder localStringBuilder = new StringBuilder();
              localStringBuilder.append("addSubscription failed with RemoteException parentId=");
              localStringBuilder.append((String)localObject1);
              Log.d("MediaBrowser", localStringBuilder.toString());
            }
          }
        }
      }
    });
  }
  
  private void subscribeInternal(String paramString, Bundle paramBundle, SubscriptionCallback paramSubscriptionCallback)
  {
    if (!TextUtils.isEmpty(paramString))
    {
      if (paramSubscriptionCallback != null)
      {
        Subscription localSubscription1 = (Subscription)mSubscriptions.get(paramString);
        Subscription localSubscription2 = localSubscription1;
        if (localSubscription1 == null)
        {
          localSubscription2 = new Subscription();
          mSubscriptions.put(paramString, localSubscription2);
        }
        localSubscription2.putCallback(mContext, paramBundle, paramSubscriptionCallback);
        if (isConnected())
        {
          if (paramBundle == null) {
            try
            {
              mServiceBinder.addSubscriptionDeprecated(paramString, mServiceCallbacks);
            }
            catch (RemoteException paramBundle)
            {
              break label118;
            }
          }
          mServiceBinder.addSubscription(paramString, mToken, paramBundle, mServiceCallbacks);
          break label150;
          label118:
          paramBundle = new StringBuilder();
          paramBundle.append("addSubscription failed with RemoteException parentId=");
          paramBundle.append(paramString);
          Log.d("MediaBrowser", paramBundle.toString());
        }
        label150:
        return;
      }
      throw new IllegalArgumentException("callback cannot be null");
    }
    throw new IllegalArgumentException("parentId cannot be empty.");
  }
  
  private void unsubscribeInternal(String paramString, SubscriptionCallback paramSubscriptionCallback)
  {
    if (!TextUtils.isEmpty(paramString))
    {
      Subscription localSubscription = (Subscription)mSubscriptions.get(paramString);
      if (localSubscription == null) {
        return;
      }
      if (paramSubscriptionCallback == null)
      {
        try
        {
          if (!isConnected()) {
            break label164;
          }
          mServiceBinder.removeSubscriptionDeprecated(paramString, mServiceCallbacks);
          mServiceBinder.removeSubscription(paramString, null, mServiceCallbacks);
        }
        catch (RemoteException localRemoteException)
        {
          break label167;
        }
      }
      else
      {
        localObject = localSubscription.getCallbacks();
        List localList = localSubscription.getOptionsList();
        for (int i = ((List)localObject).size() - 1; i >= 0; i--) {
          if (((List)localObject).get(i) == paramSubscriptionCallback)
          {
            if (isConnected()) {
              mServiceBinder.removeSubscription(paramString, mToken, mServiceCallbacks);
            }
            ((List)localObject).remove(i);
            localList.remove(i);
          }
        }
      }
      label164:
      break label203;
      label167:
      Object localObject = new StringBuilder();
      ((StringBuilder)localObject).append("removeSubscription failed with RemoteException parentId=");
      ((StringBuilder)localObject).append(paramString);
      Log.d("MediaBrowser", ((StringBuilder)localObject).toString());
      label203:
      if ((localSubscription.isEmpty()) || (paramSubscriptionCallback == null)) {
        mSubscriptions.remove(paramString);
      }
      return;
    }
    throw new IllegalArgumentException("parentId cannot be empty.");
  }
  
  public void connect()
  {
    if ((mState != 0) && (mState != 1))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("connect() called while neither disconnecting nor disconnected (state=");
      localStringBuilder.append(getStateLabel(mState));
      localStringBuilder.append(")");
      throw new IllegalStateException(localStringBuilder.toString());
    }
    mState = 2;
    mHandler.post(new Runnable()
    {
      public void run()
      {
        if (mState == 0) {
          return;
        }
        MediaBrowser.access$002(MediaBrowser.this, 2);
        if (mServiceBinder == null)
        {
          if (mServiceCallbacks == null)
          {
            Intent localIntent = new Intent("android.media.browse.MediaBrowserService");
            localIntent.setComponent(mServiceComponent);
            MediaBrowser.access$402(MediaBrowser.this, new MediaBrowser.MediaServiceConnection(MediaBrowser.this, null));
            int i = 0;
            try
            {
              boolean bool = mContext.bindService(localIntent, mServiceConnection, 1);
              i = bool;
            }
            catch (Exception localException)
            {
              localStringBuilder = new StringBuilder();
              localStringBuilder.append("Failed binding to service ");
              localStringBuilder.append(mServiceComponent);
              Log.e("MediaBrowser", localStringBuilder.toString());
            }
            if (i == 0)
            {
              MediaBrowser.this.forceCloseConnection();
              mCallback.onConnectionFailed();
            }
            return;
          }
          localStringBuilder = new StringBuilder();
          localStringBuilder.append("mServiceCallbacks should be null. Instead it is ");
          localStringBuilder.append(mServiceCallbacks);
          throw new RuntimeException(localStringBuilder.toString());
        }
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("mServiceBinder should be null. Instead it is ");
        localStringBuilder.append(mServiceBinder);
        throw new RuntimeException(localStringBuilder.toString());
      }
    });
  }
  
  public void disconnect()
  {
    mState = 0;
    mHandler.post(new Runnable()
    {
      public void run()
      {
        if (mServiceCallbacks != null) {
          try
          {
            mServiceBinder.disconnect(mServiceCallbacks);
          }
          catch (RemoteException localRemoteException)
          {
            StringBuilder localStringBuilder = new StringBuilder();
            localStringBuilder.append("RemoteException during connect for ");
            localStringBuilder.append(mServiceComponent);
            Log.w("MediaBrowser", localStringBuilder.toString());
          }
        }
        int i = mState;
        MediaBrowser.this.forceCloseConnection();
        if (i != 0) {
          MediaBrowser.access$002(MediaBrowser.this, i);
        }
      }
    });
  }
  
  void dump()
  {
    Log.d("MediaBrowser", "MediaBrowser...");
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("  mServiceComponent=");
    localStringBuilder.append(mServiceComponent);
    Log.d("MediaBrowser", localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append("  mCallback=");
    localStringBuilder.append(mCallback);
    Log.d("MediaBrowser", localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append("  mRootHints=");
    localStringBuilder.append(mRootHints);
    Log.d("MediaBrowser", localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append("  mState=");
    localStringBuilder.append(getStateLabel(mState));
    Log.d("MediaBrowser", localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append("  mServiceConnection=");
    localStringBuilder.append(mServiceConnection);
    Log.d("MediaBrowser", localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append("  mServiceBinder=");
    localStringBuilder.append(mServiceBinder);
    Log.d("MediaBrowser", localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append("  mServiceCallbacks=");
    localStringBuilder.append(mServiceCallbacks);
    Log.d("MediaBrowser", localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append("  mRootId=");
    localStringBuilder.append(mRootId);
    Log.d("MediaBrowser", localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append("  mMediaSessionToken=");
    localStringBuilder.append(mMediaSessionToken);
    Log.d("MediaBrowser", localStringBuilder.toString());
  }
  
  public Bundle getExtras()
  {
    if (isConnected()) {
      return mExtras;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("getExtras() called while not connected (state=");
    localStringBuilder.append(getStateLabel(mState));
    localStringBuilder.append(")");
    throw new IllegalStateException(localStringBuilder.toString());
  }
  
  public void getItem(final String paramString, final ItemCallback paramItemCallback)
  {
    if (!TextUtils.isEmpty(paramString))
    {
      if (paramItemCallback != null)
      {
        if (mState != 3)
        {
          Log.i("MediaBrowser", "Not connected, unable to retrieve the MediaItem.");
          mHandler.post(new Runnable()
          {
            public void run()
            {
              paramItemCallback.onError(paramString);
            }
          });
          return;
        }
        ResultReceiver local4 = new ResultReceiver(mHandler)
        {
          protected void onReceiveResult(int paramAnonymousInt, Bundle paramAnonymousBundle)
          {
            if (!isConnected()) {
              return;
            }
            if ((paramAnonymousInt == 0) && (paramAnonymousBundle != null) && (paramAnonymousBundle.containsKey("media_item")))
            {
              paramAnonymousBundle = paramAnonymousBundle.getParcelable("media_item");
              if ((paramAnonymousBundle != null) && (!(paramAnonymousBundle instanceof MediaBrowser.MediaItem)))
              {
                paramItemCallback.onError(paramString);
                return;
              }
              paramItemCallback.onItemLoaded((MediaBrowser.MediaItem)paramAnonymousBundle);
              return;
            }
            paramItemCallback.onError(paramString);
          }
        };
        try
        {
          mServiceBinder.getMediaItem(paramString, local4, mServiceCallbacks);
        }
        catch (RemoteException localRemoteException)
        {
          Log.i("MediaBrowser", "Remote error getting media item.");
          mHandler.post(new Runnable()
          {
            public void run()
            {
              paramItemCallback.onError(paramString);
            }
          });
        }
        return;
      }
      throw new IllegalArgumentException("cb cannot be null.");
    }
    throw new IllegalArgumentException("mediaId cannot be empty.");
  }
  
  public String getRoot()
  {
    if (isConnected()) {
      return mRootId;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("getRoot() called while not connected (state=");
    localStringBuilder.append(getStateLabel(mState));
    localStringBuilder.append(")");
    throw new IllegalStateException(localStringBuilder.toString());
  }
  
  public ComponentName getServiceComponent()
  {
    if (isConnected()) {
      return mServiceComponent;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("getServiceComponent() called while not connected (state=");
    localStringBuilder.append(mState);
    localStringBuilder.append(")");
    throw new IllegalStateException(localStringBuilder.toString());
  }
  
  public MediaSession.Token getSessionToken()
  {
    if (isConnected()) {
      return mMediaSessionToken;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("getSessionToken() called while not connected (state=");
    localStringBuilder.append(mState);
    localStringBuilder.append(")");
    throw new IllegalStateException(localStringBuilder.toString());
  }
  
  public boolean isConnected()
  {
    boolean bool;
    if (mState == 3) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void subscribe(String paramString, SubscriptionCallback paramSubscriptionCallback)
  {
    subscribeInternal(paramString, null, paramSubscriptionCallback);
  }
  
  public void subscribe(String paramString, Bundle paramBundle, SubscriptionCallback paramSubscriptionCallback)
  {
    if (paramBundle != null)
    {
      subscribeInternal(paramString, new Bundle(paramBundle), paramSubscriptionCallback);
      return;
    }
    throw new IllegalArgumentException("options cannot be null");
  }
  
  public void unsubscribe(String paramString)
  {
    unsubscribeInternal(paramString, null);
  }
  
  public void unsubscribe(String paramString, SubscriptionCallback paramSubscriptionCallback)
  {
    if (paramSubscriptionCallback != null)
    {
      unsubscribeInternal(paramString, paramSubscriptionCallback);
      return;
    }
    throw new IllegalArgumentException("callback cannot be null");
  }
  
  public static class ConnectionCallback
  {
    public ConnectionCallback() {}
    
    public void onConnected() {}
    
    public void onConnectionFailed() {}
    
    public void onConnectionSuspended() {}
  }
  
  public static abstract class ItemCallback
  {
    public ItemCallback() {}
    
    public void onError(String paramString) {}
    
    public void onItemLoaded(MediaBrowser.MediaItem paramMediaItem) {}
  }
  
  public static class MediaItem
    implements Parcelable
  {
    public static final Parcelable.Creator<MediaItem> CREATOR = new Parcelable.Creator()
    {
      public MediaBrowser.MediaItem createFromParcel(Parcel paramAnonymousParcel)
      {
        return new MediaBrowser.MediaItem(paramAnonymousParcel, null);
      }
      
      public MediaBrowser.MediaItem[] newArray(int paramAnonymousInt)
      {
        return new MediaBrowser.MediaItem[paramAnonymousInt];
      }
    };
    public static final int FLAG_BROWSABLE = 1;
    public static final int FLAG_PLAYABLE = 2;
    private final MediaDescription mDescription;
    private final int mFlags;
    
    public MediaItem(MediaDescription paramMediaDescription, int paramInt)
    {
      if (paramMediaDescription != null)
      {
        if (!TextUtils.isEmpty(paramMediaDescription.getMediaId()))
        {
          mFlags = paramInt;
          mDescription = paramMediaDescription;
          return;
        }
        throw new IllegalArgumentException("description must have a non-empty media id");
      }
      throw new IllegalArgumentException("description cannot be null");
    }
    
    private MediaItem(Parcel paramParcel)
    {
      mFlags = paramParcel.readInt();
      mDescription = ((MediaDescription)MediaDescription.CREATOR.createFromParcel(paramParcel));
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public MediaDescription getDescription()
    {
      return mDescription;
    }
    
    public int getFlags()
    {
      return mFlags;
    }
    
    public String getMediaId()
    {
      return mDescription.getMediaId();
    }
    
    public boolean isBrowsable()
    {
      int i = mFlags;
      boolean bool = true;
      if ((i & 0x1) == 0) {
        bool = false;
      }
      return bool;
    }
    
    public boolean isPlayable()
    {
      boolean bool;
      if ((mFlags & 0x2) != 0) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder("MediaItem{");
      localStringBuilder.append("mFlags=");
      localStringBuilder.append(mFlags);
      localStringBuilder.append(", mDescription=");
      localStringBuilder.append(mDescription);
      localStringBuilder.append('}');
      return localStringBuilder.toString();
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeInt(mFlags);
      mDescription.writeToParcel(paramParcel, paramInt);
    }
    
    @Retention(RetentionPolicy.SOURCE)
    public static @interface Flags {}
  }
  
  private class MediaServiceConnection
    implements ServiceConnection
  {
    private MediaServiceConnection() {}
    
    private boolean isCurrent(String paramString)
    {
      if ((mServiceConnection == this) && (mState != 0) && (mState != 1)) {
        return true;
      }
      if ((mState != 0) && (mState != 1))
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append(paramString);
        localStringBuilder.append(" for ");
        localStringBuilder.append(mServiceComponent);
        localStringBuilder.append(" with mServiceConnection=");
        localStringBuilder.append(mServiceConnection);
        localStringBuilder.append(" this=");
        localStringBuilder.append(this);
        Log.i("MediaBrowser", localStringBuilder.toString());
      }
      return false;
    }
    
    private void postOrRun(Runnable paramRunnable)
    {
      if (Thread.currentThread() == mHandler.getLooper().getThread()) {
        paramRunnable.run();
      } else {
        mHandler.post(paramRunnable);
      }
    }
    
    public void onServiceConnected(final ComponentName paramComponentName, final IBinder paramIBinder)
    {
      postOrRun(new Runnable()
      {
        public void run()
        {
          if (!MediaBrowser.MediaServiceConnection.this.isCurrent("onServiceConnected")) {
            return;
          }
          MediaBrowser.access$102(MediaBrowser.this, IMediaBrowserService.Stub.asInterface(paramIBinder));
          MediaBrowser.access$202(MediaBrowser.this, MediaBrowser.this.getNewServiceCallbacks());
          MediaBrowser.access$002(MediaBrowser.this, 2);
          try
          {
            mServiceBinder.connect(mContext.getPackageName(), mRootHints, mServiceCallbacks);
          }
          catch (RemoteException localRemoteException)
          {
            StringBuilder localStringBuilder = new StringBuilder();
            localStringBuilder.append("RemoteException during connect for ");
            localStringBuilder.append(mServiceComponent);
            Log.w("MediaBrowser", localStringBuilder.toString());
          }
        }
      });
    }
    
    public void onServiceDisconnected(final ComponentName paramComponentName)
    {
      postOrRun(new Runnable()
      {
        public void run()
        {
          if (!MediaBrowser.MediaServiceConnection.this.isCurrent("onServiceDisconnected")) {
            return;
          }
          MediaBrowser.access$102(MediaBrowser.this, null);
          MediaBrowser.access$202(MediaBrowser.this, null);
          MediaBrowser.access$002(MediaBrowser.this, 4);
          mCallback.onConnectionSuspended();
        }
      });
    }
  }
  
  private static class ServiceCallbacks
    extends IMediaBrowserServiceCallbacks.Stub
  {
    private WeakReference<MediaBrowser> mMediaBrowser;
    
    public ServiceCallbacks(MediaBrowser paramMediaBrowser)
    {
      mMediaBrowser = new WeakReference(paramMediaBrowser);
    }
    
    public void onConnect(String paramString, MediaSession.Token paramToken, Bundle paramBundle)
    {
      MediaBrowser localMediaBrowser = (MediaBrowser)mMediaBrowser.get();
      if (localMediaBrowser != null) {
        localMediaBrowser.onServiceConnected(this, paramString, paramToken, paramBundle);
      }
    }
    
    public void onConnectFailed()
    {
      MediaBrowser localMediaBrowser = (MediaBrowser)mMediaBrowser.get();
      if (localMediaBrowser != null) {
        localMediaBrowser.onConnectionFailed(this);
      }
    }
    
    public void onLoadChildren(String paramString, ParceledListSlice paramParceledListSlice)
    {
      onLoadChildrenWithOptions(paramString, paramParceledListSlice, null);
    }
    
    public void onLoadChildrenWithOptions(String paramString, ParceledListSlice paramParceledListSlice, Bundle paramBundle)
    {
      MediaBrowser localMediaBrowser = (MediaBrowser)mMediaBrowser.get();
      if (localMediaBrowser != null) {
        localMediaBrowser.onLoadChildren(this, paramString, paramParceledListSlice, paramBundle);
      }
    }
  }
  
  private static class Subscription
  {
    private final List<MediaBrowser.SubscriptionCallback> mCallbacks = new ArrayList();
    private final List<Bundle> mOptionsList = new ArrayList();
    
    public Subscription() {}
    
    public MediaBrowser.SubscriptionCallback getCallback(Context paramContext, Bundle paramBundle)
    {
      if (paramBundle != null) {
        paramBundle.setClassLoader(paramContext.getClassLoader());
      }
      for (int i = 0; i < mOptionsList.size(); i++) {
        if (MediaBrowserUtils.areSameOptions((Bundle)mOptionsList.get(i), paramBundle)) {
          return (MediaBrowser.SubscriptionCallback)mCallbacks.get(i);
        }
      }
      return null;
    }
    
    public List<MediaBrowser.SubscriptionCallback> getCallbacks()
    {
      return mCallbacks;
    }
    
    public List<Bundle> getOptionsList()
    {
      return mOptionsList;
    }
    
    public boolean isEmpty()
    {
      return mCallbacks.isEmpty();
    }
    
    public void putCallback(Context paramContext, Bundle paramBundle, MediaBrowser.SubscriptionCallback paramSubscriptionCallback)
    {
      if (paramBundle != null) {
        paramBundle.setClassLoader(paramContext.getClassLoader());
      }
      for (int i = 0; i < mOptionsList.size(); i++) {
        if (MediaBrowserUtils.areSameOptions((Bundle)mOptionsList.get(i), paramBundle))
        {
          mCallbacks.set(i, paramSubscriptionCallback);
          return;
        }
      }
      mCallbacks.add(paramSubscriptionCallback);
      mOptionsList.add(paramBundle);
    }
  }
  
  public static abstract class SubscriptionCallback
  {
    Binder mToken = new Binder();
    
    public SubscriptionCallback() {}
    
    public void onChildrenLoaded(String paramString, List<MediaBrowser.MediaItem> paramList) {}
    
    public void onChildrenLoaded(String paramString, List<MediaBrowser.MediaItem> paramList, Bundle paramBundle) {}
    
    public void onError(String paramString) {}
    
    public void onError(String paramString, Bundle paramBundle) {}
  }
}
