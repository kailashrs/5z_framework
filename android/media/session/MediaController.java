package android.media.session;

import android.app.PendingIntent;
import android.content.Context;
import android.content.pm.ParceledListSlice;
import android.media.AudioAttributes;
import android.media.MediaMetadata;
import android.media.Rating;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public final class MediaController
{
  private static final int MSG_DESTROYED = 8;
  private static final int MSG_EVENT = 1;
  private static final int MSG_UPDATE_EXTRAS = 7;
  private static final int MSG_UPDATE_METADATA = 3;
  private static final int MSG_UPDATE_PLAYBACK_STATE = 2;
  private static final int MSG_UPDATE_QUEUE = 5;
  private static final int MSG_UPDATE_QUEUE_TITLE = 6;
  private static final int MSG_UPDATE_VOLUME = 4;
  private static final String TAG = "MediaController";
  private final ArrayList<MessageHandler> mCallbacks = new ArrayList();
  private boolean mCbRegistered = false;
  private final CallbackStub mCbStub = new CallbackStub(this);
  private final Context mContext;
  private final Object mLock = new Object();
  private String mPackageName;
  private final ISessionController mSessionBinder;
  private String mTag;
  private final MediaSession.Token mToken;
  private final TransportControls mTransportControls;
  
  public MediaController(Context paramContext, ISessionController paramISessionController)
  {
    if (paramISessionController != null)
    {
      if (paramContext != null)
      {
        mSessionBinder = paramISessionController;
        mTransportControls = new TransportControls(null);
        mToken = new MediaSession.Token(paramISessionController);
        mContext = paramContext;
        return;
      }
      throw new IllegalArgumentException("Context cannot be null");
    }
    throw new IllegalArgumentException("Session token cannot be null");
  }
  
  public MediaController(Context paramContext, MediaSession.Token paramToken)
  {
    this(paramContext, paramToken.getBinder());
  }
  
  private void addCallbackLocked(Callback paramCallback, Handler paramHandler)
  {
    if (getHandlerForCallbackLocked(paramCallback) != null)
    {
      Log.w("MediaController", "Callback is already added, ignoring");
      return;
    }
    paramCallback = new MessageHandler(paramHandler.getLooper(), paramCallback);
    mCallbacks.add(paramCallback);
    MessageHandler.access$102(paramCallback, true);
    if (!mCbRegistered) {
      try
      {
        mSessionBinder.registerCallbackListener(mContext.getPackageName(), mCbStub);
        mCbRegistered = true;
      }
      catch (RemoteException paramCallback)
      {
        Log.e("MediaController", "Dead object in registerCallback", paramCallback);
      }
    }
  }
  
  private boolean dispatchMediaButtonEventInternal(boolean paramBoolean, KeyEvent paramKeyEvent)
  {
    if (paramKeyEvent != null)
    {
      if (!KeyEvent.isMediaKey(paramKeyEvent.getKeyCode())) {
        return false;
      }
      try
      {
        paramBoolean = mSessionBinder.sendMediaButton(mContext.getPackageName(), mCbStub, paramBoolean, paramKeyEvent);
        return paramBoolean;
      }
      catch (RemoteException paramKeyEvent)
      {
        return false;
      }
    }
    throw new IllegalArgumentException("KeyEvent may not be null");
  }
  
  private MessageHandler getHandlerForCallbackLocked(Callback paramCallback)
  {
    if (paramCallback != null)
    {
      for (int i = mCallbacks.size() - 1; i >= 0; i--)
      {
        MessageHandler localMessageHandler = (MessageHandler)mCallbacks.get(i);
        if (paramCallback == mCallback) {
          return localMessageHandler;
        }
      }
      return null;
    }
    throw new IllegalArgumentException("Callback cannot be null");
  }
  
  private final void postMessage(int paramInt, Object paramObject, Bundle paramBundle)
  {
    synchronized (mLock)
    {
      for (int i = mCallbacks.size() - 1; i >= 0; i--) {
        ((MessageHandler)mCallbacks.get(i)).post(paramInt, paramObject, paramBundle);
      }
      return;
    }
  }
  
  private boolean removeCallbackLocked(Callback paramCallback)
  {
    boolean bool = false;
    for (int i = mCallbacks.size() - 1; i >= 0; i--)
    {
      MessageHandler localMessageHandler = (MessageHandler)mCallbacks.get(i);
      if (paramCallback == mCallback)
      {
        mCallbacks.remove(i);
        bool = true;
        MessageHandler.access$102(localMessageHandler, false);
      }
    }
    if ((mCbRegistered) && (mCallbacks.size() == 0))
    {
      try
      {
        mSessionBinder.unregisterCallbackListener(mCbStub);
      }
      catch (RemoteException paramCallback)
      {
        Log.e("MediaController", "Dead object in removeCallbackLocked");
      }
      mCbRegistered = false;
    }
    return bool;
  }
  
  public void adjustVolume(int paramInt1, int paramInt2)
  {
    try
    {
      mSessionBinder.adjustVolume(mContext.getPackageName(), mCbStub, false, paramInt1, paramInt2);
    }
    catch (RemoteException localRemoteException)
    {
      Log.wtf("MediaController", "Error calling adjustVolumeBy.", localRemoteException);
    }
  }
  
  public boolean controlsSameSession(MediaController paramMediaController)
  {
    boolean bool = false;
    if (paramMediaController == null) {
      return false;
    }
    if (mSessionBinder.asBinder() == paramMediaController.getSessionBinder().asBinder()) {
      bool = true;
    }
    return bool;
  }
  
  public boolean dispatchMediaButtonEvent(KeyEvent paramKeyEvent)
  {
    return dispatchMediaButtonEventInternal(false, paramKeyEvent);
  }
  
  public boolean dispatchMediaButtonEventAsSystemService(KeyEvent paramKeyEvent)
  {
    return dispatchMediaButtonEventInternal(true, paramKeyEvent);
  }
  
  public void dispatchVolumeButtonEventAsSystemService(KeyEvent paramKeyEvent)
  {
    switch (paramKeyEvent.getAction())
    {
    default: 
      break;
    case 0: 
      int i = 0;
      int j = paramKeyEvent.getKeyCode();
      if (j != 164) {
        switch (j)
        {
        default: 
          break;
        case 25: 
          i = -1;
          break;
        case 24: 
          i = 1;
          break;
        }
      } else {
        i = 101;
      }
      try
      {
        mSessionBinder.adjustVolume(mContext.getPackageName(), mCbStub, true, i, 1);
      }
      catch (RemoteException paramKeyEvent)
      {
        Log.wtf("MediaController", "Error calling adjustVolumeBy", paramKeyEvent);
      }
    }
    try
    {
      mSessionBinder.adjustVolume(mContext.getPackageName(), mCbStub, true, 0, 4116);
    }
    catch (RemoteException paramKeyEvent)
    {
      Log.wtf("MediaController", "Error calling adjustVolumeBy", paramKeyEvent);
    }
  }
  
  public Bundle getExtras()
  {
    try
    {
      Bundle localBundle = mSessionBinder.getExtras();
      return localBundle;
    }
    catch (RemoteException localRemoteException)
    {
      Log.wtf("MediaController", "Error calling getExtras", localRemoteException);
    }
    return null;
  }
  
  public long getFlags()
  {
    try
    {
      long l = mSessionBinder.getFlags();
      return l;
    }
    catch (RemoteException localRemoteException)
    {
      Log.wtf("MediaController", "Error calling getFlags.", localRemoteException);
    }
    return 0L;
  }
  
  public MediaMetadata getMetadata()
  {
    try
    {
      MediaMetadata localMediaMetadata = mSessionBinder.getMetadata();
      return localMediaMetadata;
    }
    catch (RemoteException localRemoteException)
    {
      Log.wtf("MediaController", "Error calling getMetadata.", localRemoteException);
    }
    return null;
  }
  
  public String getPackageName()
  {
    if (mPackageName == null) {
      try
      {
        mPackageName = mSessionBinder.getPackageName();
      }
      catch (RemoteException localRemoteException)
      {
        Log.d("MediaController", "Dead object in getPackageName.", localRemoteException);
      }
    }
    return mPackageName;
  }
  
  public PlaybackInfo getPlaybackInfo()
  {
    try
    {
      Object localObject = mSessionBinder.getVolumeAttributes();
      localObject = new PlaybackInfo(volumeType, audioAttrs, controlType, maxVolume, currentVolume);
      return localObject;
    }
    catch (RemoteException localRemoteException)
    {
      Log.wtf("MediaController", "Error calling getAudioInfo.", localRemoteException);
    }
    return null;
  }
  
  public PlaybackState getPlaybackState()
  {
    try
    {
      PlaybackState localPlaybackState = mSessionBinder.getPlaybackState();
      return localPlaybackState;
    }
    catch (RemoteException localRemoteException)
    {
      Log.wtf("MediaController", "Error calling getPlaybackState.", localRemoteException);
    }
    return null;
  }
  
  public List<MediaSession.QueueItem> getQueue()
  {
    try
    {
      Object localObject = mSessionBinder.getQueue();
      if (localObject != null)
      {
        localObject = ((ParceledListSlice)localObject).getList();
        return localObject;
      }
    }
    catch (RemoteException localRemoteException)
    {
      Log.wtf("MediaController", "Error calling getQueue.", localRemoteException);
    }
    return null;
  }
  
  public CharSequence getQueueTitle()
  {
    try
    {
      CharSequence localCharSequence = mSessionBinder.getQueueTitle();
      return localCharSequence;
    }
    catch (RemoteException localRemoteException)
    {
      Log.wtf("MediaController", "Error calling getQueueTitle", localRemoteException);
    }
    return null;
  }
  
  public int getRatingType()
  {
    try
    {
      int i = mSessionBinder.getRatingType();
      return i;
    }
    catch (RemoteException localRemoteException)
    {
      Log.wtf("MediaController", "Error calling getRatingType.", localRemoteException);
    }
    return 0;
  }
  
  public PendingIntent getSessionActivity()
  {
    try
    {
      PendingIntent localPendingIntent = mSessionBinder.getLaunchPendingIntent();
      return localPendingIntent;
    }
    catch (RemoteException localRemoteException)
    {
      Log.wtf("MediaController", "Error calling getPendingIntent.", localRemoteException);
    }
    return null;
  }
  
  ISessionController getSessionBinder()
  {
    return mSessionBinder;
  }
  
  public MediaSession.Token getSessionToken()
  {
    return mToken;
  }
  
  public String getTag()
  {
    if (mTag == null) {
      try
      {
        mTag = mSessionBinder.getTag();
      }
      catch (RemoteException localRemoteException)
      {
        Log.d("MediaController", "Dead object in getTag.", localRemoteException);
      }
    }
    return mTag;
  }
  
  public TransportControls getTransportControls()
  {
    return mTransportControls;
  }
  
  public void registerCallback(Callback paramCallback)
  {
    registerCallback(paramCallback, null);
  }
  
  public void registerCallback(Callback paramCallback, Handler paramHandler)
  {
    if (paramCallback != null)
    {
      if (paramHandler == null) {
        paramHandler = new Handler();
      }
      synchronized (mLock)
      {
        addCallbackLocked(paramCallback, paramHandler);
        return;
      }
    }
    throw new IllegalArgumentException("callback must not be null");
  }
  
  public void sendCommand(String paramString, Bundle paramBundle, ResultReceiver paramResultReceiver)
  {
    if (!TextUtils.isEmpty(paramString))
    {
      try
      {
        mSessionBinder.sendCommand(mContext.getPackageName(), mCbStub, paramString, paramBundle, paramResultReceiver);
      }
      catch (RemoteException paramString)
      {
        Log.d("MediaController", "Dead object in sendCommand.", paramString);
      }
      return;
    }
    throw new IllegalArgumentException("command cannot be null or empty");
  }
  
  public void setVolumeTo(int paramInt1, int paramInt2)
  {
    try
    {
      mSessionBinder.setVolumeTo(mContext.getPackageName(), mCbStub, paramInt1, paramInt2);
    }
    catch (RemoteException localRemoteException)
    {
      Log.wtf("MediaController", "Error calling setVolumeTo.", localRemoteException);
    }
  }
  
  public void unregisterCallback(Callback paramCallback)
  {
    if (paramCallback != null) {
      synchronized (mLock)
      {
        removeCallbackLocked(paramCallback);
        return;
      }
    }
    throw new IllegalArgumentException("callback must not be null");
  }
  
  public static abstract class Callback
  {
    public Callback() {}
    
    public void onAudioInfoChanged(MediaController.PlaybackInfo paramPlaybackInfo) {}
    
    public void onExtrasChanged(Bundle paramBundle) {}
    
    public void onMetadataChanged(MediaMetadata paramMediaMetadata) {}
    
    public void onPlaybackStateChanged(PlaybackState paramPlaybackState) {}
    
    public void onQueueChanged(List<MediaSession.QueueItem> paramList) {}
    
    public void onQueueTitleChanged(CharSequence paramCharSequence) {}
    
    public void onSessionDestroyed() {}
    
    public void onSessionEvent(String paramString, Bundle paramBundle) {}
  }
  
  private static final class CallbackStub
    extends ISessionControllerCallback.Stub
  {
    private final WeakReference<MediaController> mController;
    
    public CallbackStub(MediaController paramMediaController)
    {
      mController = new WeakReference(paramMediaController);
    }
    
    public void onEvent(String paramString, Bundle paramBundle)
    {
      MediaController localMediaController = (MediaController)mController.get();
      if (localMediaController != null) {
        localMediaController.postMessage(1, paramString, paramBundle);
      }
    }
    
    public void onExtrasChanged(Bundle paramBundle)
    {
      MediaController localMediaController = (MediaController)mController.get();
      if (localMediaController != null) {
        localMediaController.postMessage(7, paramBundle, null);
      }
    }
    
    public void onMetadataChanged(MediaMetadata paramMediaMetadata)
    {
      MediaController localMediaController = (MediaController)mController.get();
      if (localMediaController != null) {
        localMediaController.postMessage(3, paramMediaMetadata, null);
      }
    }
    
    public void onPlaybackStateChanged(PlaybackState paramPlaybackState)
    {
      MediaController localMediaController = (MediaController)mController.get();
      if (localMediaController != null) {
        localMediaController.postMessage(2, paramPlaybackState, null);
      }
    }
    
    public void onQueueChanged(ParceledListSlice paramParceledListSlice)
    {
      if (paramParceledListSlice == null) {
        paramParceledListSlice = null;
      } else {
        paramParceledListSlice = paramParceledListSlice.getList();
      }
      MediaController localMediaController = (MediaController)mController.get();
      if (localMediaController != null) {
        localMediaController.postMessage(5, paramParceledListSlice, null);
      }
    }
    
    public void onQueueTitleChanged(CharSequence paramCharSequence)
    {
      MediaController localMediaController = (MediaController)mController.get();
      if (localMediaController != null) {
        localMediaController.postMessage(6, paramCharSequence, null);
      }
    }
    
    public void onSessionDestroyed()
    {
      MediaController localMediaController = (MediaController)mController.get();
      if (localMediaController != null) {
        localMediaController.postMessage(8, null, null);
      }
    }
    
    public void onVolumeInfoChanged(ParcelableVolumeInfo paramParcelableVolumeInfo)
    {
      MediaController localMediaController = (MediaController)mController.get();
      if (localMediaController != null) {
        localMediaController.postMessage(4, new MediaController.PlaybackInfo(volumeType, audioAttrs, controlType, maxVolume, currentVolume), null);
      }
    }
  }
  
  private static final class MessageHandler
    extends Handler
  {
    private final MediaController.Callback mCallback;
    private boolean mRegistered = false;
    
    public MessageHandler(Looper paramLooper, MediaController.Callback paramCallback)
    {
      super(null, true);
      mCallback = paramCallback;
    }
    
    public void handleMessage(Message paramMessage)
    {
      if (!mRegistered) {
        return;
      }
      switch (what)
      {
      default: 
        break;
      case 8: 
        mCallback.onSessionDestroyed();
        break;
      case 7: 
        mCallback.onExtrasChanged((Bundle)obj);
        break;
      case 6: 
        mCallback.onQueueTitleChanged((CharSequence)obj);
        break;
      case 5: 
        mCallback.onQueueChanged((List)obj);
        break;
      case 4: 
        mCallback.onAudioInfoChanged((MediaController.PlaybackInfo)obj);
        break;
      case 3: 
        mCallback.onMetadataChanged((MediaMetadata)obj);
        break;
      case 2: 
        mCallback.onPlaybackStateChanged((PlaybackState)obj);
        break;
      case 1: 
        mCallback.onSessionEvent((String)obj, paramMessage.getData());
      }
    }
    
    public void post(int paramInt, Object paramObject, Bundle paramBundle)
    {
      paramObject = obtainMessage(paramInt, paramObject);
      paramObject.setData(paramBundle);
      paramObject.sendToTarget();
    }
  }
  
  public static final class PlaybackInfo
  {
    public static final int PLAYBACK_TYPE_LOCAL = 1;
    public static final int PLAYBACK_TYPE_REMOTE = 2;
    private final AudioAttributes mAudioAttrs;
    private final int mCurrentVolume;
    private final int mMaxVolume;
    private final int mVolumeControl;
    private final int mVolumeType;
    
    public PlaybackInfo(int paramInt1, AudioAttributes paramAudioAttributes, int paramInt2, int paramInt3, int paramInt4)
    {
      mVolumeType = paramInt1;
      mAudioAttrs = paramAudioAttributes;
      mVolumeControl = paramInt2;
      mMaxVolume = paramInt3;
      mCurrentVolume = paramInt4;
    }
    
    public AudioAttributes getAudioAttributes()
    {
      return mAudioAttrs;
    }
    
    public int getCurrentVolume()
    {
      return mCurrentVolume;
    }
    
    public int getMaxVolume()
    {
      return mMaxVolume;
    }
    
    public int getPlaybackType()
    {
      return mVolumeType;
    }
    
    public int getVolumeControl()
    {
      return mVolumeControl;
    }
  }
  
  public final class TransportControls
  {
    private static final String TAG = "TransportController";
    
    private TransportControls() {}
    
    public void fastForward()
    {
      try
      {
        mSessionBinder.fastForward(mContext.getPackageName(), mCbStub);
      }
      catch (RemoteException localRemoteException)
      {
        Log.wtf("TransportController", "Error calling fastForward.", localRemoteException);
      }
    }
    
    public void pause()
    {
      try
      {
        mSessionBinder.pause(mContext.getPackageName(), mCbStub);
      }
      catch (RemoteException localRemoteException)
      {
        Log.wtf("TransportController", "Error calling pause.", localRemoteException);
      }
    }
    
    public void play()
    {
      try
      {
        mSessionBinder.play(mContext.getPackageName(), mCbStub);
      }
      catch (RemoteException localRemoteException)
      {
        Log.wtf("TransportController", "Error calling play.", localRemoteException);
      }
    }
    
    public void playFromMediaId(String paramString, Bundle paramBundle)
    {
      if (!TextUtils.isEmpty(paramString))
      {
        try
        {
          mSessionBinder.playFromMediaId(mContext.getPackageName(), mCbStub, paramString, paramBundle);
        }
        catch (RemoteException localRemoteException)
        {
          paramBundle = new StringBuilder();
          paramBundle.append("Error calling play(");
          paramBundle.append(paramString);
          paramBundle.append(").");
          Log.wtf("TransportController", paramBundle.toString(), localRemoteException);
        }
        return;
      }
      throw new IllegalArgumentException("You must specify a non-empty String for playFromMediaId.");
    }
    
    public void playFromSearch(String paramString, Bundle paramBundle)
    {
      String str = paramString;
      if (paramString == null) {
        str = "";
      }
      try
      {
        mSessionBinder.playFromSearch(mContext.getPackageName(), mCbStub, str, paramBundle);
      }
      catch (RemoteException paramString)
      {
        paramBundle = new StringBuilder();
        paramBundle.append("Error calling play(");
        paramBundle.append(str);
        paramBundle.append(").");
        Log.wtf("TransportController", paramBundle.toString(), paramString);
      }
    }
    
    public void playFromUri(Uri paramUri, Bundle paramBundle)
    {
      if ((paramUri != null) && (!Uri.EMPTY.equals(paramUri)))
      {
        try
        {
          mSessionBinder.playFromUri(mContext.getPackageName(), mCbStub, paramUri, paramBundle);
        }
        catch (RemoteException paramBundle)
        {
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("Error calling play(");
          localStringBuilder.append(paramUri);
          localStringBuilder.append(").");
          Log.wtf("TransportController", localStringBuilder.toString(), paramBundle);
        }
        return;
      }
      throw new IllegalArgumentException("You must specify a non-empty Uri for playFromUri.");
    }
    
    public void prepare()
    {
      try
      {
        mSessionBinder.prepare(mContext.getPackageName(), mCbStub);
      }
      catch (RemoteException localRemoteException)
      {
        Log.wtf("TransportController", "Error calling prepare.", localRemoteException);
      }
    }
    
    public void prepareFromMediaId(String paramString, Bundle paramBundle)
    {
      if (!TextUtils.isEmpty(paramString))
      {
        try
        {
          mSessionBinder.prepareFromMediaId(mContext.getPackageName(), mCbStub, paramString, paramBundle);
        }
        catch (RemoteException localRemoteException)
        {
          paramBundle = new StringBuilder();
          paramBundle.append("Error calling prepare(");
          paramBundle.append(paramString);
          paramBundle.append(").");
          Log.wtf("TransportController", paramBundle.toString(), localRemoteException);
        }
        return;
      }
      throw new IllegalArgumentException("You must specify a non-empty String for prepareFromMediaId.");
    }
    
    public void prepareFromSearch(String paramString, Bundle paramBundle)
    {
      String str = paramString;
      if (paramString == null) {
        str = "";
      }
      try
      {
        mSessionBinder.prepareFromSearch(mContext.getPackageName(), mCbStub, str, paramBundle);
      }
      catch (RemoteException paramString)
      {
        paramBundle = new StringBuilder();
        paramBundle.append("Error calling prepare(");
        paramBundle.append(str);
        paramBundle.append(").");
        Log.wtf("TransportController", paramBundle.toString(), paramString);
      }
    }
    
    public void prepareFromUri(Uri paramUri, Bundle paramBundle)
    {
      if ((paramUri != null) && (!Uri.EMPTY.equals(paramUri)))
      {
        try
        {
          mSessionBinder.prepareFromUri(mContext.getPackageName(), mCbStub, paramUri, paramBundle);
        }
        catch (RemoteException paramBundle)
        {
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("Error calling prepare(");
          localStringBuilder.append(paramUri);
          localStringBuilder.append(").");
          Log.wtf("TransportController", localStringBuilder.toString(), paramBundle);
        }
        return;
      }
      throw new IllegalArgumentException("You must specify a non-empty Uri for prepareFromUri.");
    }
    
    public void rewind()
    {
      try
      {
        mSessionBinder.rewind(mContext.getPackageName(), mCbStub);
      }
      catch (RemoteException localRemoteException)
      {
        Log.wtf("TransportController", "Error calling rewind.", localRemoteException);
      }
    }
    
    public void seekTo(long paramLong)
    {
      try
      {
        mSessionBinder.seekTo(mContext.getPackageName(), mCbStub, paramLong);
      }
      catch (RemoteException localRemoteException)
      {
        Log.wtf("TransportController", "Error calling seekTo.", localRemoteException);
      }
    }
    
    public void sendCustomAction(PlaybackState.CustomAction paramCustomAction, Bundle paramBundle)
    {
      if (paramCustomAction != null)
      {
        sendCustomAction(paramCustomAction.getAction(), paramBundle);
        return;
      }
      throw new IllegalArgumentException("CustomAction cannot be null.");
    }
    
    public void sendCustomAction(String paramString, Bundle paramBundle)
    {
      if (!TextUtils.isEmpty(paramString))
      {
        try
        {
          mSessionBinder.sendCustomAction(mContext.getPackageName(), mCbStub, paramString, paramBundle);
        }
        catch (RemoteException paramString)
        {
          Log.d("TransportController", "Dead object in sendCustomAction.", paramString);
        }
        return;
      }
      throw new IllegalArgumentException("CustomAction cannot be null.");
    }
    
    public void setRating(Rating paramRating)
    {
      try
      {
        mSessionBinder.rate(mContext.getPackageName(), mCbStub, paramRating);
      }
      catch (RemoteException paramRating)
      {
        Log.wtf("TransportController", "Error calling rate.", paramRating);
      }
    }
    
    public void skipToNext()
    {
      try
      {
        mSessionBinder.next(mContext.getPackageName(), mCbStub);
      }
      catch (RemoteException localRemoteException)
      {
        Log.wtf("TransportController", "Error calling next.", localRemoteException);
      }
    }
    
    public void skipToPrevious()
    {
      try
      {
        mSessionBinder.previous(mContext.getPackageName(), mCbStub);
      }
      catch (RemoteException localRemoteException)
      {
        Log.wtf("TransportController", "Error calling previous.", localRemoteException);
      }
    }
    
    public void skipToQueueItem(long paramLong)
    {
      try
      {
        mSessionBinder.skipToQueueItem(mContext.getPackageName(), mCbStub, paramLong);
      }
      catch (RemoteException localRemoteException)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Error calling skipToItem(");
        localStringBuilder.append(paramLong);
        localStringBuilder.append(").");
        Log.wtf("TransportController", localStringBuilder.toString(), localRemoteException);
      }
    }
    
    public void stop()
    {
      try
      {
        mSessionBinder.stop(mContext.getPackageName(), mCbStub);
      }
      catch (RemoteException localRemoteException)
      {
        Log.wtf("TransportController", "Error calling stop.", localRemoteException);
      }
    }
  }
}
