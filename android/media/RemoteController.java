package android.media;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.media.session.MediaController;
import android.media.session.MediaController.Callback;
import android.media.session.MediaController.TransportControls;
import android.media.session.MediaSession.Token;
import android.media.session.MediaSessionLegacyHelper;
import android.media.session.MediaSessionManager;
import android.media.session.MediaSessionManager.OnActiveSessionsChangedListener;
import android.media.session.PlaybackState;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.UserHandle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.KeyEvent;
import java.util.List;

@Deprecated
public final class RemoteController
{
  private static final boolean DEBUG = false;
  private static final int MAX_BITMAP_DIMENSION = 512;
  private static final int MSG_CLIENT_CHANGE = 0;
  private static final int MSG_NEW_MEDIA_METADATA = 2;
  private static final int MSG_NEW_PLAYBACK_STATE = 1;
  public static final int POSITION_SYNCHRONIZATION_CHECK = 1;
  public static final int POSITION_SYNCHRONIZATION_NONE = 0;
  private static final int SENDMSG_NOOP = 1;
  private static final int SENDMSG_QUEUE = 2;
  private static final int SENDMSG_REPLACE = 0;
  private static final String TAG = "RemoteController";
  private static final Object mInfoLock = new Object();
  private int mArtworkHeight = -1;
  private int mArtworkWidth = -1;
  private final Context mContext;
  private MediaController mCurrentSession;
  private boolean mEnabled = true;
  private final EventHandler mEventHandler;
  private boolean mIsRegistered = false;
  private PlaybackInfo mLastPlaybackInfo;
  private final int mMaxBitmapDimension;
  private MetadataEditor mMetadataEditor;
  private OnClientUpdateListener mOnClientUpdateListener;
  private MediaController.Callback mSessionCb = new MediaControllerCallback(null);
  private MediaSessionManager.OnActiveSessionsChangedListener mSessionListener;
  private MediaSessionManager mSessionManager;
  
  public RemoteController(Context paramContext, OnClientUpdateListener paramOnClientUpdateListener)
    throws IllegalArgumentException
  {
    this(paramContext, paramOnClientUpdateListener, null);
  }
  
  public RemoteController(Context paramContext, OnClientUpdateListener paramOnClientUpdateListener, Looper paramLooper)
    throws IllegalArgumentException
  {
    if (paramContext != null)
    {
      if (paramOnClientUpdateListener != null)
      {
        if (paramLooper != null)
        {
          mEventHandler = new EventHandler(this, paramLooper);
        }
        else
        {
          paramLooper = Looper.myLooper();
          if (paramLooper == null) {
            break label164;
          }
          mEventHandler = new EventHandler(this, paramLooper);
        }
        mOnClientUpdateListener = paramOnClientUpdateListener;
        mContext = paramContext;
        mSessionManager = ((MediaSessionManager)paramContext.getSystemService("media_session"));
        mSessionListener = new TopTransportSessionListener(null);
        if (ActivityManager.isLowRamDeviceStatic())
        {
          mMaxBitmapDimension = 512;
        }
        else
        {
          paramContext = paramContext.getResources().getDisplayMetrics();
          mMaxBitmapDimension = Math.max(widthPixels, heightPixels);
        }
        return;
        label164:
        throw new IllegalArgumentException("Calling thread not associated with a looper");
      }
      throw new IllegalArgumentException("Invalid null OnClientUpdateListener");
    }
    throw new IllegalArgumentException("Invalid null Context");
  }
  
  private void onClientChange(boolean paramBoolean)
  {
    synchronized (mInfoLock)
    {
      OnClientUpdateListener localOnClientUpdateListener = mOnClientUpdateListener;
      mMetadataEditor = null;
      if (localOnClientUpdateListener != null) {
        localOnClientUpdateListener.onClientChange(paramBoolean);
      }
      return;
    }
  }
  
  private void onNewMediaMetadata(MediaMetadata paramMediaMetadata)
  {
    if (paramMediaMetadata == null) {
      return;
    }
    synchronized (mInfoLock)
    {
      OnClientUpdateListener localOnClientUpdateListener = mOnClientUpdateListener;
      int i;
      if ((mCurrentSession != null) && (mCurrentSession.getRatingType() != 0)) {
        i = 1;
      } else {
        i = 0;
      }
      long l;
      if (i != 0) {
        l = 268435457L;
      } else {
        l = 0L;
      }
      Bundle localBundle = MediaSessionLegacyHelper.getOldMetadata(paramMediaMetadata, mArtworkWidth, mArtworkHeight);
      paramMediaMetadata = new android/media/RemoteController$MetadataEditor;
      paramMediaMetadata.<init>(this, localBundle, l);
      mMetadataEditor = paramMediaMetadata;
      paramMediaMetadata = mMetadataEditor;
      if (localOnClientUpdateListener != null) {
        localOnClientUpdateListener.onClientMetadataUpdate(paramMediaMetadata);
      }
      return;
    }
  }
  
  private void onNewPlaybackState(PlaybackState paramPlaybackState)
  {
    synchronized (mInfoLock)
    {
      OnClientUpdateListener localOnClientUpdateListener = mOnClientUpdateListener;
      if (localOnClientUpdateListener != null)
      {
        int i;
        if (paramPlaybackState == null) {
          i = 0;
        } else {
          i = PlaybackState.getRccStateFromState(paramPlaybackState.getState());
        }
        if ((paramPlaybackState != null) && (paramPlaybackState.getPosition() != -1L)) {
          localOnClientUpdateListener.onClientPlaybackStateUpdate(i, paramPlaybackState.getLastPositionUpdateTime(), paramPlaybackState.getPosition(), paramPlaybackState.getPlaybackSpeed());
        } else {
          localOnClientUpdateListener.onClientPlaybackStateUpdate(i);
        }
        if (paramPlaybackState != null) {
          localOnClientUpdateListener.onClientTransportControlUpdate(PlaybackState.getRccControlFlagsFromActions(paramPlaybackState.getActions()));
        }
      }
      return;
    }
  }
  
  private static void sendMsg(Handler paramHandler, int paramInt1, int paramInt2, int paramInt3, int paramInt4, Object paramObject, int paramInt5)
  {
    if (paramHandler == null)
    {
      paramHandler = new StringBuilder();
      paramHandler.append("null event handler, will not deliver message ");
      paramHandler.append(paramInt1);
      Log.e("RemoteController", paramHandler.toString());
      return;
    }
    if (paramInt2 == 0) {
      paramHandler.removeMessages(paramInt1);
    } else if ((paramInt2 == 1) && (paramHandler.hasMessages(paramInt1))) {
      return;
    }
    paramHandler.sendMessageDelayed(paramHandler.obtainMessage(paramInt1, paramInt3, paramInt4, paramObject), paramInt5);
  }
  
  private void updateController(MediaController paramMediaController)
  {
    Object localObject = mInfoLock;
    if (paramMediaController == null)
    {
      try
      {
        if (mCurrentSession == null) {
          break label164;
        }
        mCurrentSession.unregisterCallback(mSessionCb);
        mCurrentSession = null;
        sendMsg(mEventHandler, 0, 0, 0, 1, null, 0);
      }
      finally
      {
        break label167;
      }
    }
    else if ((mCurrentSession == null) || (!paramMediaController.getSessionToken().equals(mCurrentSession.getSessionToken())))
    {
      if (mCurrentSession != null) {
        mCurrentSession.unregisterCallback(mSessionCb);
      }
      sendMsg(mEventHandler, 0, 0, 0, 0, null, 0);
      mCurrentSession = paramMediaController;
      mCurrentSession.registerCallback(mSessionCb, mEventHandler);
      PlaybackState localPlaybackState = paramMediaController.getPlaybackState();
      sendMsg(mEventHandler, 1, 0, 0, 0, localPlaybackState, 0);
      paramMediaController = paramMediaController.getMetadata();
      sendMsg(mEventHandler, 2, 0, 0, 0, paramMediaController, 0);
    }
    label164:
    return;
    label167:
    throw paramMediaController;
  }
  
  public boolean clearArtworkConfiguration()
  {
    return setArtworkConfiguration(false, -1, -1);
  }
  
  public MetadataEditor editMetadata()
  {
    MetadataEditor localMetadataEditor = new MetadataEditor();
    mEditorMetadata = new Bundle();
    mEditorArtwork = null;
    mMetadataChanged = true;
    mArtworkChanged = true;
    mEditableKeys = 0L;
    return localMetadataEditor;
  }
  
  public long getEstimatedMediaPosition()
  {
    synchronized (mInfoLock)
    {
      if (mCurrentSession != null)
      {
        PlaybackState localPlaybackState = mCurrentSession.getPlaybackState();
        if (localPlaybackState != null)
        {
          long l = localPlaybackState.getPosition();
          return l;
        }
      }
      return -1L;
    }
  }
  
  OnClientUpdateListener getUpdateListener()
  {
    return mOnClientUpdateListener;
  }
  
  public boolean seekTo(long paramLong)
    throws IllegalArgumentException
  {
    if (!mEnabled)
    {
      Log.e("RemoteController", "Cannot use seekTo() from a disabled RemoteController");
      return false;
    }
    if (paramLong >= 0L) {
      synchronized (mInfoLock)
      {
        if (mCurrentSession != null) {
          mCurrentSession.getTransportControls().seekTo(paramLong);
        }
        return true;
      }
    }
    throw new IllegalArgumentException("illegal negative time value");
  }
  
  public boolean sendMediaKeyEvent(KeyEvent paramKeyEvent)
    throws IllegalArgumentException
  {
    if (KeyEvent.isMediaKey(paramKeyEvent.getKeyCode())) {
      synchronized (mInfoLock)
      {
        if (mCurrentSession != null)
        {
          boolean bool = mCurrentSession.dispatchMediaButtonEvent(paramKeyEvent);
          return bool;
        }
        return false;
      }
    }
    throw new IllegalArgumentException("not a media key event");
  }
  
  public boolean setArtworkConfiguration(int paramInt1, int paramInt2)
    throws IllegalArgumentException
  {
    return setArtworkConfiguration(true, paramInt1, paramInt2);
  }
  
  public boolean setArtworkConfiguration(boolean paramBoolean, int paramInt1, int paramInt2)
    throws IllegalArgumentException
  {
    Object localObject1 = mInfoLock;
    int i;
    if (paramBoolean) {
      if ((paramInt1 > 0) && (paramInt2 > 0)) {
        i = paramInt1;
      }
    }
    try
    {
      if (paramInt1 > mMaxBitmapDimension) {
        i = mMaxBitmapDimension;
      }
      paramInt1 = paramInt2;
      if (paramInt2 > mMaxBitmapDimension) {
        paramInt1 = mMaxBitmapDimension;
      }
      mArtworkWidth = i;
      mArtworkHeight = paramInt1;
      break label92;
      IllegalArgumentException localIllegalArgumentException = new java/lang/IllegalArgumentException;
      localIllegalArgumentException.<init>("Invalid dimensions");
      throw localIllegalArgumentException;
      mArtworkWidth = -1;
      mArtworkHeight = -1;
      label92:
      return true;
    }
    finally {}
  }
  
  public boolean setSynchronizationMode(int paramInt)
    throws IllegalArgumentException
  {
    if ((paramInt != 0) && (paramInt != 1))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Unknown synchronization mode ");
      localStringBuilder.append(paramInt);
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
    if (!mIsRegistered)
    {
      Log.e("RemoteController", "Cannot set synchronization mode on an unregistered RemoteController");
      return false;
    }
    return true;
  }
  
  void startListeningToSessions()
  {
    ComponentName localComponentName = new ComponentName(mContext, mOnClientUpdateListener.getClass());
    Handler localHandler = null;
    if (Looper.myLooper() == null) {
      localHandler = new Handler(Looper.getMainLooper());
    }
    mSessionManager.addOnActiveSessionsChangedListener(mSessionListener, localComponentName, UserHandle.myUserId(), localHandler);
    mSessionListener.onActiveSessionsChanged(mSessionManager.getActiveSessions(localComponentName));
  }
  
  void stopListeningToSessions()
  {
    mSessionManager.removeOnActiveSessionsChangedListener(mSessionListener);
  }
  
  private class EventHandler
    extends Handler
  {
    public EventHandler(RemoteController paramRemoteController, Looper paramLooper)
    {
      super();
    }
    
    public void handleMessage(Message paramMessage)
    {
      Object localObject;
      switch (what)
      {
      default: 
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("unknown event ");
        ((StringBuilder)localObject).append(what);
        Log.e("RemoteController", ((StringBuilder)localObject).toString());
        break;
      case 2: 
        RemoteController.this.onNewMediaMetadata((MediaMetadata)obj);
        break;
      case 1: 
        RemoteController.this.onNewPlaybackState((PlaybackState)obj);
        break;
      case 0: 
        localObject = RemoteController.this;
        int i = arg2;
        boolean bool = true;
        if (i != 1) {
          bool = false;
        }
        ((RemoteController)localObject).onClientChange(bool);
      }
    }
  }
  
  private class MediaControllerCallback
    extends MediaController.Callback
  {
    private MediaControllerCallback() {}
    
    public void onMetadataChanged(MediaMetadata paramMediaMetadata)
    {
      RemoteController.this.onNewMediaMetadata(paramMediaMetadata);
    }
    
    public void onPlaybackStateChanged(PlaybackState paramPlaybackState)
    {
      RemoteController.this.onNewPlaybackState(paramPlaybackState);
    }
  }
  
  public class MetadataEditor
    extends MediaMetadataEditor
  {
    protected MetadataEditor() {}
    
    protected MetadataEditor(Bundle paramBundle, long paramLong)
    {
      mEditorMetadata = paramBundle;
      mEditableKeys = paramLong;
      mEditorArtwork = ((Bitmap)paramBundle.getParcelable(String.valueOf(100)));
      if (mEditorArtwork != null) {
        cleanupBitmapFromBundle(100);
      }
      mMetadataChanged = true;
      mArtworkChanged = true;
      mApplied = false;
    }
    
    private void cleanupBitmapFromBundle(int paramInt)
    {
      if (METADATA_KEYS_TYPE.get(paramInt, -1) == 2) {
        mEditorMetadata.remove(String.valueOf(paramInt));
      }
    }
    
    /* Error */
    public void apply()
    {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: getfield 52	android/media/RemoteController$MetadataEditor:mMetadataChanged	Z
      //   6: istore_1
      //   7: iload_1
      //   8: ifne +6 -> 14
      //   11: aload_0
      //   12: monitorexit
      //   13: return
      //   14: invokestatic 77	android/media/RemoteController:access$200	()Ljava/lang/Object;
      //   17: astore_2
      //   18: aload_2
      //   19: monitorenter
      //   20: aload_0
      //   21: getfield 13	android/media/RemoteController$MetadataEditor:this$0	Landroid/media/RemoteController;
      //   24: invokestatic 81	android/media/RemoteController:access$300	(Landroid/media/RemoteController;)Landroid/media/session/MediaController;
      //   27: ifnull +47 -> 74
      //   30: aload_0
      //   31: getfield 22	android/media/RemoteController$MetadataEditor:mEditorMetadata	Landroid/os/Bundle;
      //   34: ldc 82
      //   36: invokestatic 32	java/lang/String:valueOf	(I)Ljava/lang/String;
      //   39: invokevirtual 86	android/os/Bundle:containsKey	(Ljava/lang/String;)Z
      //   42: ifeq +32 -> 74
      //   45: aload_0
      //   46: ldc 82
      //   48: aconst_null
      //   49: invokevirtual 90	android/media/RemoteController$MetadataEditor:getObject	(ILjava/lang/Object;)Ljava/lang/Object;
      //   52: checkcast 92	android/media/Rating
      //   55: astore_3
      //   56: aload_3
      //   57: ifnull +17 -> 74
      //   60: aload_0
      //   61: getfield 13	android/media/RemoteController$MetadataEditor:this$0	Landroid/media/RemoteController;
      //   64: invokestatic 81	android/media/RemoteController:access$300	(Landroid/media/RemoteController;)Landroid/media/session/MediaController;
      //   67: invokevirtual 98	android/media/session/MediaController:getTransportControls	()Landroid/media/session/MediaController$TransportControls;
      //   70: aload_3
      //   71: invokevirtual 104	android/media/session/MediaController$TransportControls:setRating	(Landroid/media/Rating;)V
      //   74: aload_2
      //   75: monitorexit
      //   76: aload_0
      //   77: iconst_0
      //   78: putfield 58	android/media/RemoteController$MetadataEditor:mApplied	Z
      //   81: aload_0
      //   82: monitorexit
      //   83: return
      //   84: astore_3
      //   85: aload_2
      //   86: monitorexit
      //   87: aload_3
      //   88: athrow
      //   89: astore_2
      //   90: aload_0
      //   91: monitorexit
      //   92: aload_2
      //   93: athrow
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	94	0	this	MetadataEditor
      //   6	2	1	bool	boolean
      //   89	4	2	localObject2	Object
      //   55	16	3	localRating	Rating
      //   84	4	3	localObject3	Object
      // Exception table:
      //   from	to	target	type
      //   20	56	84	finally
      //   60	74	84	finally
      //   74	76	84	finally
      //   85	87	84	finally
      //   2	7	89	finally
      //   14	20	89	finally
      //   76	81	89	finally
      //   87	89	89	finally
    }
  }
  
  public static abstract interface OnClientUpdateListener
  {
    public abstract void onClientChange(boolean paramBoolean);
    
    public abstract void onClientMetadataUpdate(RemoteController.MetadataEditor paramMetadataEditor);
    
    public abstract void onClientPlaybackStateUpdate(int paramInt);
    
    public abstract void onClientPlaybackStateUpdate(int paramInt, long paramLong1, long paramLong2, float paramFloat);
    
    public abstract void onClientTransportControlUpdate(int paramInt);
  }
  
  private static class PlaybackInfo
  {
    long mCurrentPosMs;
    float mSpeed;
    int mState;
    long mStateChangeTimeMs;
    
    PlaybackInfo(int paramInt, long paramLong1, long paramLong2, float paramFloat)
    {
      mState = paramInt;
      mStateChangeTimeMs = paramLong1;
      mCurrentPosMs = paramLong2;
      mSpeed = paramFloat;
    }
  }
  
  private class TopTransportSessionListener
    implements MediaSessionManager.OnActiveSessionsChangedListener
  {
    private TopTransportSessionListener() {}
    
    public void onActiveSessionsChanged(List<MediaController> paramList)
    {
      int i = paramList.size();
      for (int j = 0; j < i; j++)
      {
        MediaController localMediaController = (MediaController)paramList.get(j);
        if ((0x2 & localMediaController.getFlags()) != 0L)
        {
          RemoteController.this.updateController(localMediaController);
          return;
        }
      }
      RemoteController.this.updateController(null);
    }
  }
}
