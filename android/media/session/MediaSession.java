package android.media.session;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ParceledListSlice;
import android.content.res.Resources;
import android.media.AudioAttributes;
import android.media.MediaDescription;
import android.media.MediaMetadata;
import android.media.MediaMetadata.Builder;
import android.media.Rating;
import android.media.VolumeProvider;
import android.media.VolumeProvider.Callback;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.ViewConfiguration;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Objects;

public final class MediaSession
{
  public static final int FLAG_EXCLUSIVE_GLOBAL_PRIORITY = 65536;
  @Deprecated
  public static final int FLAG_HANDLES_MEDIA_BUTTONS = 1;
  @Deprecated
  public static final int FLAG_HANDLES_TRANSPORT_CONTROLS = 2;
  public static final int INVALID_PID = -1;
  public static final int INVALID_UID = -1;
  private static final String TAG = "MediaSession";
  private boolean mActive = false;
  private final ISession mBinder;
  private CallbackMessageHandler mCallback;
  private final CallbackStub mCbStub;
  private final MediaController mController;
  private final Object mLock = new Object();
  private final int mMaxBitmapSize;
  private PlaybackState mPlaybackState;
  private final Token mSessionToken;
  private VolumeProvider mVolumeProvider;
  
  public MediaSession(Context paramContext, String paramString)
  {
    this(paramContext, paramString, UserHandle.myUserId());
  }
  
  public MediaSession(Context paramContext, String paramString, int paramInt)
  {
    if (paramContext != null)
    {
      if (!TextUtils.isEmpty(paramString))
      {
        mMaxBitmapSize = paramContext.getResources().getDimensionPixelSize(17105079);
        mCbStub = new CallbackStub(this);
        MediaSessionManager localMediaSessionManager = (MediaSessionManager)paramContext.getSystemService("media_session");
        try
        {
          mBinder = localMediaSessionManager.createSession(mCbStub, paramString, paramInt);
          paramString = new android/media/session/MediaSession$Token;
          paramString.<init>(mBinder.getController());
          mSessionToken = paramString;
          paramString = new android/media/session/MediaController;
          paramString.<init>(paramContext, mSessionToken);
          mController = paramString;
          return;
        }
        catch (RemoteException paramContext)
        {
          throw new RuntimeException("Remote error creating session.", paramContext);
        }
      }
      throw new IllegalArgumentException("tag cannot be null or empty");
    }
    throw new IllegalArgumentException("context cannot be null.");
  }
  
  private void dispatchAdjustVolume(MediaSessionManager.RemoteUserInfo paramRemoteUserInfo, int paramInt)
  {
    postToCallback(paramRemoteUserInfo, 21, Integer.valueOf(paramInt), null);
  }
  
  private void dispatchCommand(MediaSessionManager.RemoteUserInfo paramRemoteUserInfo, String paramString, Bundle paramBundle, ResultReceiver paramResultReceiver)
  {
    postToCallback(paramRemoteUserInfo, 1, new Command(paramString, paramBundle, paramResultReceiver), null);
  }
  
  private void dispatchCustomAction(MediaSessionManager.RemoteUserInfo paramRemoteUserInfo, String paramString, Bundle paramBundle)
  {
    postToCallback(paramRemoteUserInfo, 20, paramString, paramBundle);
  }
  
  private void dispatchFastForward(MediaSessionManager.RemoteUserInfo paramRemoteUserInfo)
  {
    postToCallback(paramRemoteUserInfo, 16, null, null);
  }
  
  private void dispatchMediaButton(MediaSessionManager.RemoteUserInfo paramRemoteUserInfo, Intent paramIntent)
  {
    postToCallback(paramRemoteUserInfo, 2, paramIntent, null);
  }
  
  private void dispatchMediaButtonDelayed(MediaSessionManager.RemoteUserInfo paramRemoteUserInfo, Intent paramIntent, long paramLong)
  {
    postToCallbackDelayed(paramRemoteUserInfo, 23, paramIntent, null, paramLong);
  }
  
  private void dispatchNext(MediaSessionManager.RemoteUserInfo paramRemoteUserInfo)
  {
    postToCallback(paramRemoteUserInfo, 14, null, null);
  }
  
  private void dispatchPause(MediaSessionManager.RemoteUserInfo paramRemoteUserInfo)
  {
    postToCallback(paramRemoteUserInfo, 12, null, null);
  }
  
  private void dispatchPlay(MediaSessionManager.RemoteUserInfo paramRemoteUserInfo)
  {
    postToCallback(paramRemoteUserInfo, 7, null, null);
  }
  
  private void dispatchPlayFromMediaId(MediaSessionManager.RemoteUserInfo paramRemoteUserInfo, String paramString, Bundle paramBundle)
  {
    postToCallback(paramRemoteUserInfo, 8, paramString, paramBundle);
  }
  
  private void dispatchPlayFromSearch(MediaSessionManager.RemoteUserInfo paramRemoteUserInfo, String paramString, Bundle paramBundle)
  {
    postToCallback(paramRemoteUserInfo, 9, paramString, paramBundle);
  }
  
  private void dispatchPlayFromUri(MediaSessionManager.RemoteUserInfo paramRemoteUserInfo, Uri paramUri, Bundle paramBundle)
  {
    postToCallback(paramRemoteUserInfo, 10, paramUri, paramBundle);
  }
  
  private void dispatchPrepare(MediaSessionManager.RemoteUserInfo paramRemoteUserInfo)
  {
    postToCallback(paramRemoteUserInfo, 3, null, null);
  }
  
  private void dispatchPrepareFromMediaId(MediaSessionManager.RemoteUserInfo paramRemoteUserInfo, String paramString, Bundle paramBundle)
  {
    postToCallback(paramRemoteUserInfo, 4, paramString, paramBundle);
  }
  
  private void dispatchPrepareFromSearch(MediaSessionManager.RemoteUserInfo paramRemoteUserInfo, String paramString, Bundle paramBundle)
  {
    postToCallback(paramRemoteUserInfo, 5, paramString, paramBundle);
  }
  
  private void dispatchPrepareFromUri(MediaSessionManager.RemoteUserInfo paramRemoteUserInfo, Uri paramUri, Bundle paramBundle)
  {
    postToCallback(paramRemoteUserInfo, 6, paramUri, paramBundle);
  }
  
  private void dispatchPrevious(MediaSessionManager.RemoteUserInfo paramRemoteUserInfo)
  {
    postToCallback(paramRemoteUserInfo, 15, null, null);
  }
  
  private void dispatchRate(MediaSessionManager.RemoteUserInfo paramRemoteUserInfo, Rating paramRating)
  {
    postToCallback(paramRemoteUserInfo, 19, paramRating, null);
  }
  
  private void dispatchRewind(MediaSessionManager.RemoteUserInfo paramRemoteUserInfo)
  {
    postToCallback(paramRemoteUserInfo, 17, null, null);
  }
  
  private void dispatchSeekTo(MediaSessionManager.RemoteUserInfo paramRemoteUserInfo, long paramLong)
  {
    postToCallback(paramRemoteUserInfo, 18, Long.valueOf(paramLong), null);
  }
  
  private void dispatchSetVolumeTo(MediaSessionManager.RemoteUserInfo paramRemoteUserInfo, int paramInt)
  {
    postToCallback(paramRemoteUserInfo, 22, Integer.valueOf(paramInt), null);
  }
  
  private void dispatchSkipToItem(MediaSessionManager.RemoteUserInfo paramRemoteUserInfo, long paramLong)
  {
    postToCallback(paramRemoteUserInfo, 11, Long.valueOf(paramLong), null);
  }
  
  private void dispatchStop(MediaSessionManager.RemoteUserInfo paramRemoteUserInfo)
  {
    postToCallback(paramRemoteUserInfo, 13, null, null);
  }
  
  public static boolean isActiveState(int paramInt)
  {
    switch (paramInt)
    {
    case 7: 
    default: 
      return false;
    }
    return true;
  }
  
  private void postToCallback(MediaSessionManager.RemoteUserInfo paramRemoteUserInfo, int paramInt, Object paramObject, Bundle paramBundle)
  {
    postToCallbackDelayed(paramRemoteUserInfo, paramInt, paramObject, paramBundle, 0L);
  }
  
  private void postToCallbackDelayed(MediaSessionManager.RemoteUserInfo paramRemoteUserInfo, int paramInt, Object paramObject, Bundle paramBundle, long paramLong)
  {
    synchronized (mLock)
    {
      if (mCallback != null) {
        mCallback.post(paramRemoteUserInfo, paramInt, paramObject, paramBundle, paramLong);
      }
      return;
    }
  }
  
  public String getCallingPackage()
  {
    if ((mCallback != null) && (mCallback.mCurrentControllerInfo != null)) {
      return mCallback.mCurrentControllerInfo.getPackageName();
    }
    return null;
  }
  
  public MediaController getController()
  {
    return mController;
  }
  
  public final MediaSessionManager.RemoteUserInfo getCurrentControllerInfo()
  {
    if ((mCallback != null) && (mCallback.mCurrentControllerInfo != null)) {
      return mCallback.mCurrentControllerInfo;
    }
    throw new IllegalStateException("This should be called inside of MediaSession.Callback methods");
  }
  
  public Token getSessionToken()
  {
    return mSessionToken;
  }
  
  public boolean isActive()
  {
    return mActive;
  }
  
  public void notifyRemoteVolumeChanged(VolumeProvider paramVolumeProvider)
  {
    Object localObject = mLock;
    if (paramVolumeProvider != null) {
      try
      {
        if (paramVolumeProvider == mVolumeProvider)
        {
          try
          {
            mBinder.setCurrentVolume(paramVolumeProvider.getCurrentVolume());
          }
          catch (RemoteException paramVolumeProvider)
          {
            Log.e("MediaSession", "Error in notifyVolumeChanged", paramVolumeProvider);
          }
          return;
        }
      }
      finally
      {
        break label68;
      }
    }
    Log.w("MediaSession", "Received update from stale volume provider");
    return;
    label68:
    throw paramVolumeProvider;
  }
  
  public void release()
  {
    try
    {
      mBinder.destroy();
    }
    catch (RemoteException localRemoteException)
    {
      Log.wtf("MediaSession", "Error releasing session: ", localRemoteException);
    }
  }
  
  public void sendSessionEvent(String paramString, Bundle paramBundle)
  {
    if (!TextUtils.isEmpty(paramString))
    {
      try
      {
        mBinder.sendEvent(paramString, paramBundle);
      }
      catch (RemoteException paramString)
      {
        Log.wtf("MediaSession", "Error sending event", paramString);
      }
      return;
    }
    throw new IllegalArgumentException("event cannot be null or empty");
  }
  
  public void setActive(boolean paramBoolean)
  {
    if (mActive == paramBoolean) {
      return;
    }
    try
    {
      mBinder.setActive(paramBoolean);
      mActive = paramBoolean;
    }
    catch (RemoteException localRemoteException)
    {
      Log.wtf("MediaSession", "Failure in setActive.", localRemoteException);
    }
  }
  
  public void setCallback(Callback paramCallback)
  {
    setCallback(paramCallback, null);
  }
  
  public void setCallback(Callback paramCallback, Handler paramHandler)
  {
    synchronized (mLock)
    {
      if (mCallback != null)
      {
        Callback.access$102(mCallback.mCallback, null);
        mCallback.removeCallbacksAndMessages(null);
      }
      if (paramCallback == null)
      {
        mCallback = null;
        return;
      }
      Handler localHandler = paramHandler;
      if (paramHandler == null)
      {
        localHandler = new android/os/Handler;
        localHandler.<init>();
      }
      Callback.access$102(paramCallback, this);
      paramHandler = new android/media/session/MediaSession$CallbackMessageHandler;
      paramHandler.<init>(this, localHandler.getLooper(), paramCallback);
      mCallback = paramHandler;
      return;
    }
  }
  
  public void setExtras(Bundle paramBundle)
  {
    try
    {
      mBinder.setExtras(paramBundle);
    }
    catch (RemoteException paramBundle)
    {
      Log.wtf("Dead object in setExtras.", paramBundle);
    }
  }
  
  public void setFlags(int paramInt)
  {
    try
    {
      mBinder.setFlags(paramInt);
    }
    catch (RemoteException localRemoteException)
    {
      Log.wtf("MediaSession", "Failure in setFlags.", localRemoteException);
    }
  }
  
  public void setMediaButtonReceiver(PendingIntent paramPendingIntent)
  {
    try
    {
      mBinder.setMediaButtonReceiver(paramPendingIntent);
    }
    catch (RemoteException paramPendingIntent)
    {
      Log.wtf("MediaSession", "Failure in setMediaButtonReceiver.", paramPendingIntent);
    }
  }
  
  public void setMetadata(MediaMetadata paramMediaMetadata)
  {
    MediaMetadata localMediaMetadata = paramMediaMetadata;
    if (paramMediaMetadata != null) {
      localMediaMetadata = new MediaMetadata.Builder(paramMediaMetadata, mMaxBitmapSize).build();
    }
    try
    {
      mBinder.setMetadata(localMediaMetadata);
    }
    catch (RemoteException paramMediaMetadata)
    {
      Log.wtf("MediaSession", "Dead object in setPlaybackState.", paramMediaMetadata);
    }
  }
  
  public void setPlaybackState(PlaybackState paramPlaybackState)
  {
    mPlaybackState = paramPlaybackState;
    try
    {
      mBinder.setPlaybackState(paramPlaybackState);
    }
    catch (RemoteException paramPlaybackState)
    {
      Log.wtf("MediaSession", "Dead object in setPlaybackState.", paramPlaybackState);
    }
  }
  
  public void setPlaybackToLocal(AudioAttributes paramAudioAttributes)
  {
    if (paramAudioAttributes != null)
    {
      try
      {
        mBinder.setPlaybackToLocal(paramAudioAttributes);
      }
      catch (RemoteException paramAudioAttributes)
      {
        Log.wtf("MediaSession", "Failure in setPlaybackToLocal.", paramAudioAttributes);
      }
      return;
    }
    throw new IllegalArgumentException("Attributes cannot be null for local playback.");
  }
  
  public void setPlaybackToRemote(VolumeProvider paramVolumeProvider)
  {
    if (paramVolumeProvider != null) {
      synchronized (mLock)
      {
        mVolumeProvider = paramVolumeProvider;
        paramVolumeProvider.setCallback(new VolumeProvider.Callback()
        {
          public void onVolumeChanged(VolumeProvider paramAnonymousVolumeProvider)
          {
            notifyRemoteVolumeChanged(paramAnonymousVolumeProvider);
          }
        });
        try
        {
          mBinder.setPlaybackToRemote(paramVolumeProvider.getVolumeControl(), paramVolumeProvider.getMaxVolume());
          mBinder.setCurrentVolume(paramVolumeProvider.getCurrentVolume());
        }
        catch (RemoteException paramVolumeProvider)
        {
          Log.wtf("MediaSession", "Failure in setPlaybackToRemote.", paramVolumeProvider);
        }
        return;
      }
    }
    throw new IllegalArgumentException("volumeProvider may not be null!");
  }
  
  public void setQueue(List<QueueItem> paramList)
  {
    try
    {
      ISession localISession = mBinder;
      if (paramList == null) {
        paramList = null;
      } else {
        paramList = new ParceledListSlice(paramList);
      }
      localISession.setQueue(paramList);
    }
    catch (RemoteException paramList)
    {
      Log.wtf("Dead object in setQueue.", paramList);
    }
  }
  
  public void setQueueTitle(CharSequence paramCharSequence)
  {
    try
    {
      mBinder.setQueueTitle(paramCharSequence);
    }
    catch (RemoteException paramCharSequence)
    {
      Log.wtf("Dead object in setQueueTitle.", paramCharSequence);
    }
  }
  
  public void setRatingType(int paramInt)
  {
    try
    {
      mBinder.setRatingType(paramInt);
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("MediaSession", "Error in setRatingType.", localRemoteException);
    }
  }
  
  public void setSessionActivity(PendingIntent paramPendingIntent)
  {
    try
    {
      mBinder.setLaunchPendingIntent(paramPendingIntent);
    }
    catch (RemoteException paramPendingIntent)
    {
      Log.wtf("MediaSession", "Failure in setLaunchPendingIntent.", paramPendingIntent);
    }
  }
  
  public static abstract class Callback
  {
    private MediaSession.CallbackMessageHandler mHandler;
    private boolean mMediaPlayPauseKeyPending;
    private MediaSession mSession;
    
    public Callback() {}
    
    private void handleMediaPlayPauseKeySingleTapIfPending()
    {
      if (!mMediaPlayPauseKeyPending) {
        return;
      }
      int i = 0;
      mMediaPlayPauseKeyPending = false;
      mHandler.removeMessages(23);
      PlaybackState localPlaybackState = mSession.mPlaybackState;
      long l;
      if (localPlaybackState == null) {
        l = 0L;
      } else {
        l = localPlaybackState.getActions();
      }
      int j;
      if ((localPlaybackState != null) && (localPlaybackState.getState() == 3)) {
        j = 1;
      } else {
        j = 0;
      }
      int k;
      if ((0x204 & l) != 0L) {
        k = 1;
      } else {
        k = 0;
      }
      if ((0x202 & l) != 0L) {
        i = 1;
      }
      if ((j != 0) && (i != 0)) {
        onPause();
      } else if ((j == 0) && (k != 0)) {
        onPlay();
      }
    }
    
    public void onCommand(String paramString, Bundle paramBundle, ResultReceiver paramResultReceiver) {}
    
    public void onCustomAction(String paramString, Bundle paramBundle) {}
    
    public void onFastForward() {}
    
    public boolean onMediaButtonEvent(Intent paramIntent)
    {
      if ((mSession != null) && (mHandler != null) && ("android.intent.action.MEDIA_BUTTON".equals(paramIntent.getAction())))
      {
        KeyEvent localKeyEvent = (KeyEvent)paramIntent.getParcelableExtra("android.intent.extra.KEY_EVENT");
        if ((localKeyEvent != null) && (localKeyEvent.getAction() == 0))
        {
          PlaybackState localPlaybackState = mSession.mPlaybackState;
          long l;
          if (localPlaybackState == null) {
            l = 0L;
          } else {
            l = localPlaybackState.getActions();
          }
          int i = localKeyEvent.getKeyCode();
          if ((i != 79) && (i != 85))
          {
            handleMediaPlayPauseKeySingleTapIfPending();
            i = localKeyEvent.getKeyCode();
          }
          switch (i)
          {
          default: 
            switch (i)
            {
            default: 
              break;
            case 127: 
              if ((0x2 & l) != 0L)
              {
                onPause();
                return true;
              }
              break;
            case 126: 
              if ((0x4 & l) != 0L)
              {
                onPlay();
                return true;
              }
              break;
            }
            break;
          case 90: 
            if ((0x40 & l) != 0L)
            {
              onFastForward();
              return true;
            }
            break;
          case 89: 
            if ((0x8 & l) != 0L)
            {
              onRewind();
              return true;
            }
            break;
          case 88: 
            if ((0x10 & l) != 0L)
            {
              onSkipToPrevious();
              return true;
            }
            break;
          case 87: 
            if ((l & 0x20) != 0L)
            {
              onSkipToNext();
              return true;
            }
            break;
          case 86: 
            if ((1L & l) != 0L)
            {
              onStop();
              return true;
              if (localKeyEvent.getRepeatCount() > 0)
              {
                handleMediaPlayPauseKeySingleTapIfPending();
              }
              else if (mMediaPlayPauseKeyPending)
              {
                mHandler.removeMessages(23);
                mMediaPlayPauseKeyPending = false;
                if ((l & 0x20) != 0L) {
                  onSkipToNext();
                }
              }
              else
              {
                mMediaPlayPauseKeyPending = true;
                mSession.dispatchMediaButtonDelayed(mSession.getCurrentControllerInfo(), paramIntent, ViewConfiguration.getDoubleTapTimeout());
              }
              return true;
            }
            break;
          }
        }
      }
      return false;
    }
    
    public void onPause() {}
    
    public void onPlay() {}
    
    public void onPlayFromMediaId(String paramString, Bundle paramBundle) {}
    
    public void onPlayFromSearch(String paramString, Bundle paramBundle) {}
    
    public void onPlayFromUri(Uri paramUri, Bundle paramBundle) {}
    
    public void onPrepare() {}
    
    public void onPrepareFromMediaId(String paramString, Bundle paramBundle) {}
    
    public void onPrepareFromSearch(String paramString, Bundle paramBundle) {}
    
    public void onPrepareFromUri(Uri paramUri, Bundle paramBundle) {}
    
    public void onRewind() {}
    
    public void onSeekTo(long paramLong) {}
    
    public void onSetRating(Rating paramRating) {}
    
    public void onSkipToNext() {}
    
    public void onSkipToPrevious() {}
    
    public void onSkipToQueueItem(long paramLong) {}
    
    public void onStop() {}
  }
  
  private class CallbackMessageHandler
    extends Handler
  {
    private static final int MSG_ADJUST_VOLUME = 21;
    private static final int MSG_COMMAND = 1;
    private static final int MSG_CUSTOM_ACTION = 20;
    private static final int MSG_FAST_FORWARD = 16;
    private static final int MSG_MEDIA_BUTTON = 2;
    private static final int MSG_NEXT = 14;
    private static final int MSG_PAUSE = 12;
    private static final int MSG_PLAY = 7;
    private static final int MSG_PLAY_MEDIA_ID = 8;
    private static final int MSG_PLAY_PAUSE_KEY_DOUBLE_TAP_TIMEOUT = 23;
    private static final int MSG_PLAY_SEARCH = 9;
    private static final int MSG_PLAY_URI = 10;
    private static final int MSG_PREPARE = 3;
    private static final int MSG_PREPARE_MEDIA_ID = 4;
    private static final int MSG_PREPARE_SEARCH = 5;
    private static final int MSG_PREPARE_URI = 6;
    private static final int MSG_PREVIOUS = 15;
    private static final int MSG_RATE = 19;
    private static final int MSG_REWIND = 17;
    private static final int MSG_SEEK_TO = 18;
    private static final int MSG_SET_VOLUME = 22;
    private static final int MSG_SKIP_TO_ITEM = 11;
    private static final int MSG_STOP = 13;
    private MediaSession.Callback mCallback;
    private MediaSessionManager.RemoteUserInfo mCurrentControllerInfo;
    
    public CallbackMessageHandler(Looper paramLooper, MediaSession.Callback paramCallback)
    {
      super(null, true);
      mCallback = paramCallback;
      MediaSession.Callback.access$2802(mCallback, this);
    }
    
    public void handleMessage(Message arg1)
    {
      mCurrentControllerInfo = ((MediaSessionManager.RemoteUserInfo)obj).first);
      Object localObject1 = obj).second;
      VolumeProvider localVolumeProvider;
      switch (what)
      {
      default: 
        break;
      case 23: 
        mCallback.handleMediaPlayPauseKeySingleTapIfPending();
        break;
      case 22: 
        synchronized (mLock)
        {
          localVolumeProvider = mVolumeProvider;
          if (localVolumeProvider != null) {
            localVolumeProvider.onSetVolumeTo(((Integer)localObject1).intValue());
          }
        }
      case 21: 
        synchronized (mLock)
        {
          localVolumeProvider = mVolumeProvider;
          if (localVolumeProvider != null) {
            localVolumeProvider.onAdjustVolume(((Integer)localObject2).intValue());
          }
        }
      case 20: 
        mCallback.onCustomAction((String)localObject3, ???.getData());
        break;
      case 19: 
        mCallback.onSetRating((Rating)localObject3);
        break;
      case 18: 
        mCallback.onSeekTo(((Long)localObject3).longValue());
        break;
      case 17: 
        mCallback.onRewind();
        break;
      case 16: 
        mCallback.onFastForward();
        break;
      case 15: 
        mCallback.onSkipToPrevious();
        break;
      case 14: 
        mCallback.onSkipToNext();
        break;
      case 13: 
        mCallback.onStop();
        break;
      case 12: 
        mCallback.onPause();
        break;
      case 11: 
        mCallback.onSkipToQueueItem(((Long)localObject3).longValue());
        break;
      case 10: 
        mCallback.onPlayFromUri((Uri)localObject3, ???.getData());
        break;
      case 9: 
        mCallback.onPlayFromSearch((String)localObject3, ???.getData());
        break;
      case 8: 
        mCallback.onPlayFromMediaId((String)localObject3, ???.getData());
        break;
      case 7: 
        mCallback.onPlay();
        break;
      case 6: 
        mCallback.onPrepareFromUri((Uri)localObject3, ???.getData());
        break;
      case 5: 
        mCallback.onPrepareFromSearch((String)localObject3, ???.getData());
        break;
      case 4: 
        mCallback.onPrepareFromMediaId((String)localObject3, ???.getData());
        break;
      case 3: 
        mCallback.onPrepare();
        break;
      case 2: 
        mCallback.onMediaButtonEvent((Intent)localObject3);
        break;
      case 1: 
        ??? = (MediaSession.Command)localObject3;
        mCallback.onCommand(command, extras, stub);
      }
      mCurrentControllerInfo = null;
    }
    
    public void post(MediaSessionManager.RemoteUserInfo paramRemoteUserInfo, int paramInt, Object paramObject, Bundle paramBundle, long paramLong)
    {
      paramRemoteUserInfo = obtainMessage(paramInt, Pair.create(paramRemoteUserInfo, paramObject));
      paramRemoteUserInfo.setData(paramBundle);
      if (paramLong > 0L) {
        sendMessageDelayed(paramRemoteUserInfo, paramLong);
      } else {
        sendMessage(paramRemoteUserInfo);
      }
    }
  }
  
  public static class CallbackStub
    extends ISessionCallback.Stub
  {
    private WeakReference<MediaSession> mMediaSession;
    
    public CallbackStub(MediaSession paramMediaSession)
    {
      mMediaSession = new WeakReference(paramMediaSession);
    }
    
    private static MediaSessionManager.RemoteUserInfo createRemoteUserInfo(String paramString, int paramInt1, int paramInt2, ISessionControllerCallback paramISessionControllerCallback)
    {
      if (paramISessionControllerCallback != null) {
        paramISessionControllerCallback = paramISessionControllerCallback.asBinder();
      } else {
        paramISessionControllerCallback = null;
      }
      return new MediaSessionManager.RemoteUserInfo(paramString, paramInt1, paramInt2, paramISessionControllerCallback);
    }
    
    public void onAdjustVolume(String paramString, int paramInt1, int paramInt2, ISessionControllerCallback paramISessionControllerCallback, int paramInt3)
    {
      MediaSession localMediaSession = (MediaSession)mMediaSession.get();
      if (localMediaSession != null) {
        localMediaSession.dispatchAdjustVolume(createRemoteUserInfo(paramString, paramInt1, paramInt2, paramISessionControllerCallback), paramInt3);
      }
    }
    
    public void onCommand(String paramString1, int paramInt1, int paramInt2, ISessionControllerCallback paramISessionControllerCallback, String paramString2, Bundle paramBundle, ResultReceiver paramResultReceiver)
    {
      MediaSession localMediaSession = (MediaSession)mMediaSession.get();
      if (localMediaSession != null) {
        localMediaSession.dispatchCommand(createRemoteUserInfo(paramString1, paramInt1, paramInt2, paramISessionControllerCallback), paramString2, paramBundle, paramResultReceiver);
      }
    }
    
    public void onCustomAction(String paramString1, int paramInt1, int paramInt2, ISessionControllerCallback paramISessionControllerCallback, String paramString2, Bundle paramBundle)
    {
      MediaSession localMediaSession = (MediaSession)mMediaSession.get();
      if (localMediaSession != null) {
        localMediaSession.dispatchCustomAction(createRemoteUserInfo(paramString1, paramInt1, paramInt2, paramISessionControllerCallback), paramString2, paramBundle);
      }
    }
    
    public void onFastForward(String paramString, int paramInt1, int paramInt2, ISessionControllerCallback paramISessionControllerCallback)
    {
      MediaSession localMediaSession = (MediaSession)mMediaSession.get();
      if (localMediaSession != null) {
        localMediaSession.dispatchFastForward(createRemoteUserInfo(paramString, paramInt1, paramInt2, paramISessionControllerCallback));
      }
    }
    
    public void onMediaButton(String paramString, int paramInt1, int paramInt2, Intent paramIntent, int paramInt3, ResultReceiver paramResultReceiver)
    {
      MediaSession localMediaSession = (MediaSession)mMediaSession.get();
      if (localMediaSession != null) {}
      try
      {
        localMediaSession.dispatchMediaButton(createRemoteUserInfo(paramString, paramInt1, paramInt2, null), paramIntent);
      }
      finally
      {
        if (paramResultReceiver != null) {
          paramResultReceiver.send(paramInt3, null);
        }
      }
    }
    
    public void onMediaButtonFromController(String paramString, int paramInt1, int paramInt2, ISessionControllerCallback paramISessionControllerCallback, Intent paramIntent)
    {
      MediaSession localMediaSession = (MediaSession)mMediaSession.get();
      if (localMediaSession != null) {
        localMediaSession.dispatchMediaButton(createRemoteUserInfo(paramString, paramInt1, paramInt2, paramISessionControllerCallback), paramIntent);
      }
    }
    
    public void onNext(String paramString, int paramInt1, int paramInt2, ISessionControllerCallback paramISessionControllerCallback)
    {
      MediaSession localMediaSession = (MediaSession)mMediaSession.get();
      if (localMediaSession != null) {
        localMediaSession.dispatchNext(createRemoteUserInfo(paramString, paramInt1, paramInt2, paramISessionControllerCallback));
      }
    }
    
    public void onPause(String paramString, int paramInt1, int paramInt2, ISessionControllerCallback paramISessionControllerCallback)
    {
      MediaSession localMediaSession = (MediaSession)mMediaSession.get();
      if (localMediaSession != null) {
        localMediaSession.dispatchPause(createRemoteUserInfo(paramString, paramInt1, paramInt2, paramISessionControllerCallback));
      }
    }
    
    public void onPlay(String paramString, int paramInt1, int paramInt2, ISessionControllerCallback paramISessionControllerCallback)
    {
      MediaSession localMediaSession = (MediaSession)mMediaSession.get();
      if (localMediaSession != null) {
        localMediaSession.dispatchPlay(createRemoteUserInfo(paramString, paramInt1, paramInt2, paramISessionControllerCallback));
      }
    }
    
    public void onPlayFromMediaId(String paramString1, int paramInt1, int paramInt2, ISessionControllerCallback paramISessionControllerCallback, String paramString2, Bundle paramBundle)
    {
      MediaSession localMediaSession = (MediaSession)mMediaSession.get();
      if (localMediaSession != null) {
        localMediaSession.dispatchPlayFromMediaId(createRemoteUserInfo(paramString1, paramInt1, paramInt2, paramISessionControllerCallback), paramString2, paramBundle);
      }
    }
    
    public void onPlayFromSearch(String paramString1, int paramInt1, int paramInt2, ISessionControllerCallback paramISessionControllerCallback, String paramString2, Bundle paramBundle)
    {
      MediaSession localMediaSession = (MediaSession)mMediaSession.get();
      if (localMediaSession != null) {
        localMediaSession.dispatchPlayFromSearch(createRemoteUserInfo(paramString1, paramInt1, paramInt2, paramISessionControllerCallback), paramString2, paramBundle);
      }
    }
    
    public void onPlayFromUri(String paramString, int paramInt1, int paramInt2, ISessionControllerCallback paramISessionControllerCallback, Uri paramUri, Bundle paramBundle)
    {
      MediaSession localMediaSession = (MediaSession)mMediaSession.get();
      if (localMediaSession != null) {
        localMediaSession.dispatchPlayFromUri(createRemoteUserInfo(paramString, paramInt1, paramInt2, paramISessionControllerCallback), paramUri, paramBundle);
      }
    }
    
    public void onPrepare(String paramString, int paramInt1, int paramInt2, ISessionControllerCallback paramISessionControllerCallback)
    {
      MediaSession localMediaSession = (MediaSession)mMediaSession.get();
      if (localMediaSession != null) {
        localMediaSession.dispatchPrepare(createRemoteUserInfo(paramString, paramInt1, paramInt2, paramISessionControllerCallback));
      }
    }
    
    public void onPrepareFromMediaId(String paramString1, int paramInt1, int paramInt2, ISessionControllerCallback paramISessionControllerCallback, String paramString2, Bundle paramBundle)
    {
      MediaSession localMediaSession = (MediaSession)mMediaSession.get();
      if (localMediaSession != null) {
        localMediaSession.dispatchPrepareFromMediaId(createRemoteUserInfo(paramString1, paramInt1, paramInt2, paramISessionControllerCallback), paramString2, paramBundle);
      }
    }
    
    public void onPrepareFromSearch(String paramString1, int paramInt1, int paramInt2, ISessionControllerCallback paramISessionControllerCallback, String paramString2, Bundle paramBundle)
    {
      MediaSession localMediaSession = (MediaSession)mMediaSession.get();
      if (localMediaSession != null) {
        localMediaSession.dispatchPrepareFromSearch(createRemoteUserInfo(paramString1, paramInt1, paramInt2, paramISessionControllerCallback), paramString2, paramBundle);
      }
    }
    
    public void onPrepareFromUri(String paramString, int paramInt1, int paramInt2, ISessionControllerCallback paramISessionControllerCallback, Uri paramUri, Bundle paramBundle)
    {
      MediaSession localMediaSession = (MediaSession)mMediaSession.get();
      if (localMediaSession != null) {
        localMediaSession.dispatchPrepareFromUri(createRemoteUserInfo(paramString, paramInt1, paramInt2, paramISessionControllerCallback), paramUri, paramBundle);
      }
    }
    
    public void onPrevious(String paramString, int paramInt1, int paramInt2, ISessionControllerCallback paramISessionControllerCallback)
    {
      MediaSession localMediaSession = (MediaSession)mMediaSession.get();
      if (localMediaSession != null) {
        localMediaSession.dispatchPrevious(createRemoteUserInfo(paramString, paramInt1, paramInt2, paramISessionControllerCallback));
      }
    }
    
    public void onRate(String paramString, int paramInt1, int paramInt2, ISessionControllerCallback paramISessionControllerCallback, Rating paramRating)
    {
      MediaSession localMediaSession = (MediaSession)mMediaSession.get();
      if (localMediaSession != null) {
        localMediaSession.dispatchRate(createRemoteUserInfo(paramString, paramInt1, paramInt2, paramISessionControllerCallback), paramRating);
      }
    }
    
    public void onRewind(String paramString, int paramInt1, int paramInt2, ISessionControllerCallback paramISessionControllerCallback)
    {
      MediaSession localMediaSession = (MediaSession)mMediaSession.get();
      if (localMediaSession != null) {
        localMediaSession.dispatchRewind(createRemoteUserInfo(paramString, paramInt1, paramInt2, paramISessionControllerCallback));
      }
    }
    
    public void onSeekTo(String paramString, int paramInt1, int paramInt2, ISessionControllerCallback paramISessionControllerCallback, long paramLong)
    {
      MediaSession localMediaSession = (MediaSession)mMediaSession.get();
      if (localMediaSession != null) {
        localMediaSession.dispatchSeekTo(createRemoteUserInfo(paramString, paramInt1, paramInt2, paramISessionControllerCallback), paramLong);
      }
    }
    
    public void onSetVolumeTo(String paramString, int paramInt1, int paramInt2, ISessionControllerCallback paramISessionControllerCallback, int paramInt3)
    {
      MediaSession localMediaSession = (MediaSession)mMediaSession.get();
      if (localMediaSession != null) {
        localMediaSession.dispatchSetVolumeTo(createRemoteUserInfo(paramString, paramInt1, paramInt2, paramISessionControllerCallback), paramInt3);
      }
    }
    
    public void onSkipToTrack(String paramString, int paramInt1, int paramInt2, ISessionControllerCallback paramISessionControllerCallback, long paramLong)
    {
      MediaSession localMediaSession = (MediaSession)mMediaSession.get();
      if (localMediaSession != null) {
        localMediaSession.dispatchSkipToItem(createRemoteUserInfo(paramString, paramInt1, paramInt2, paramISessionControllerCallback), paramLong);
      }
    }
    
    public void onStop(String paramString, int paramInt1, int paramInt2, ISessionControllerCallback paramISessionControllerCallback)
    {
      MediaSession localMediaSession = (MediaSession)mMediaSession.get();
      if (localMediaSession != null) {
        localMediaSession.dispatchStop(createRemoteUserInfo(paramString, paramInt1, paramInt2, paramISessionControllerCallback));
      }
    }
  }
  
  private static final class Command
  {
    public final String command;
    public final Bundle extras;
    public final ResultReceiver stub;
    
    public Command(String paramString, Bundle paramBundle, ResultReceiver paramResultReceiver)
    {
      command = paramString;
      extras = paramBundle;
      stub = paramResultReceiver;
    }
  }
  
  public static final class QueueItem
    implements Parcelable
  {
    public static final Parcelable.Creator<QueueItem> CREATOR = new Parcelable.Creator()
    {
      public MediaSession.QueueItem createFromParcel(Parcel paramAnonymousParcel)
      {
        return new MediaSession.QueueItem(paramAnonymousParcel, null);
      }
      
      public MediaSession.QueueItem[] newArray(int paramAnonymousInt)
      {
        return new MediaSession.QueueItem[paramAnonymousInt];
      }
    };
    public static final int UNKNOWN_ID = -1;
    private final MediaDescription mDescription;
    private final long mId;
    
    public QueueItem(MediaDescription paramMediaDescription, long paramLong)
    {
      if (paramMediaDescription != null)
      {
        if (paramLong != -1L)
        {
          mDescription = paramMediaDescription;
          mId = paramLong;
          return;
        }
        throw new IllegalArgumentException("Id cannot be QueueItem.UNKNOWN_ID");
      }
      throw new IllegalArgumentException("Description cannot be null.");
    }
    
    private QueueItem(Parcel paramParcel)
    {
      mDescription = ((MediaDescription)MediaDescription.CREATOR.createFromParcel(paramParcel));
      mId = paramParcel.readLong();
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public boolean equals(Object paramObject)
    {
      if (paramObject == null) {
        return false;
      }
      if (!(paramObject instanceof QueueItem)) {
        return false;
      }
      paramObject = (QueueItem)paramObject;
      if (mId != mId) {
        return false;
      }
      return Objects.equals(mDescription, mDescription);
    }
    
    public MediaDescription getDescription()
    {
      return mDescription;
    }
    
    public long getQueueId()
    {
      return mId;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("MediaSession.QueueItem {Description=");
      localStringBuilder.append(mDescription);
      localStringBuilder.append(", Id=");
      localStringBuilder.append(mId);
      localStringBuilder.append(" }");
      return localStringBuilder.toString();
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      mDescription.writeToParcel(paramParcel, paramInt);
      paramParcel.writeLong(mId);
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface SessionFlags {}
  
  public static final class Token
    implements Parcelable
  {
    public static final Parcelable.Creator<Token> CREATOR = new Parcelable.Creator()
    {
      public MediaSession.Token createFromParcel(Parcel paramAnonymousParcel)
      {
        return new MediaSession.Token(ISessionController.Stub.asInterface(paramAnonymousParcel.readStrongBinder()));
      }
      
      public MediaSession.Token[] newArray(int paramAnonymousInt)
      {
        return new MediaSession.Token[paramAnonymousInt];
      }
    };
    private ISessionController mBinder;
    
    public Token(ISessionController paramISessionController)
    {
      mBinder = paramISessionController;
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public boolean equals(Object paramObject)
    {
      if (this == paramObject) {
        return true;
      }
      if (paramObject == null) {
        return false;
      }
      if (getClass() != paramObject.getClass()) {
        return false;
      }
      paramObject = (Token)paramObject;
      if (mBinder == null)
      {
        if (mBinder != null) {
          return false;
        }
      }
      else if (!mBinder.asBinder().equals(mBinder.asBinder())) {
        return false;
      }
      return true;
    }
    
    ISessionController getBinder()
    {
      return mBinder;
    }
    
    public int hashCode()
    {
      int i;
      if (mBinder == null) {
        i = 0;
      } else {
        i = mBinder.asBinder().hashCode();
      }
      return 31 * 1 + i;
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeStrongBinder(mBinder.asBinder());
    }
  }
}
