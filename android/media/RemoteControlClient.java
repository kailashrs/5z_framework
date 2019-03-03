package android.media;

import android.app.PendingIntent;
import android.graphics.Bitmap;
import android.media.session.MediaSession;
import android.media.session.MediaSession.Callback;
import android.media.session.MediaSessionLegacyHelper;
import android.media.session.PlaybackState;
import android.media.session.PlaybackState.Builder;
import android.os.Bundle;
import android.os.Looper;
import android.os.SystemClock;

@Deprecated
public class RemoteControlClient
{
  private static final boolean DEBUG = false;
  public static final int DEFAULT_PLAYBACK_VOLUME = 15;
  public static final int DEFAULT_PLAYBACK_VOLUME_HANDLING = 1;
  public static final int FLAGS_KEY_MEDIA_NONE = 0;
  public static final int FLAG_INFORMATION_REQUEST_ALBUM_ART = 8;
  public static final int FLAG_INFORMATION_REQUEST_KEY_MEDIA = 2;
  public static final int FLAG_INFORMATION_REQUEST_METADATA = 1;
  public static final int FLAG_INFORMATION_REQUEST_PLAYSTATE = 4;
  public static final int FLAG_KEY_MEDIA_FAST_FORWARD = 64;
  public static final int FLAG_KEY_MEDIA_NEXT = 128;
  public static final int FLAG_KEY_MEDIA_PAUSE = 16;
  public static final int FLAG_KEY_MEDIA_PLAY = 4;
  public static final int FLAG_KEY_MEDIA_PLAY_PAUSE = 8;
  public static final int FLAG_KEY_MEDIA_POSITION_UPDATE = 256;
  public static final int FLAG_KEY_MEDIA_PREVIOUS = 1;
  public static final int FLAG_KEY_MEDIA_RATING = 512;
  public static final int FLAG_KEY_MEDIA_REWIND = 2;
  public static final int FLAG_KEY_MEDIA_STOP = 32;
  public static int MEDIA_POSITION_READABLE = 1;
  public static int MEDIA_POSITION_WRITABLE = 2;
  public static final int PLAYBACKINFO_INVALID_VALUE = Integer.MIN_VALUE;
  public static final int PLAYBACKINFO_PLAYBACK_TYPE = 1;
  public static final int PLAYBACKINFO_USES_STREAM = 5;
  public static final int PLAYBACKINFO_VOLUME = 2;
  public static final int PLAYBACKINFO_VOLUME_HANDLING = 4;
  public static final int PLAYBACKINFO_VOLUME_MAX = 3;
  public static final long PLAYBACK_POSITION_ALWAYS_UNKNOWN = -9216204211029966080L;
  public static final long PLAYBACK_POSITION_INVALID = -1L;
  public static final float PLAYBACK_SPEED_1X = 1.0F;
  public static final int PLAYBACK_TYPE_LOCAL = 0;
  private static final int PLAYBACK_TYPE_MAX = 1;
  private static final int PLAYBACK_TYPE_MIN = 0;
  public static final int PLAYBACK_TYPE_REMOTE = 1;
  public static final int PLAYBACK_VOLUME_FIXED = 0;
  public static final int PLAYBACK_VOLUME_VARIABLE = 1;
  public static final int PLAYSTATE_BUFFERING = 8;
  public static final int PLAYSTATE_ERROR = 9;
  public static final int PLAYSTATE_FAST_FORWARDING = 4;
  public static final int PLAYSTATE_NONE = 0;
  public static final int PLAYSTATE_PAUSED = 2;
  public static final int PLAYSTATE_PLAYING = 3;
  public static final int PLAYSTATE_REWINDING = 5;
  public static final int PLAYSTATE_SKIPPING_BACKWARDS = 7;
  public static final int PLAYSTATE_SKIPPING_FORWARDS = 6;
  public static final int PLAYSTATE_STOPPED = 1;
  private static final long POSITION_DRIFT_MAX_MS = 500L;
  private static final long POSITION_REFRESH_PERIOD_MIN_MS = 2000L;
  private static final long POSITION_REFRESH_PERIOD_PLAYING_MS = 15000L;
  public static final int RCSE_ID_UNREGISTERED = -1;
  private static final String TAG = "RemoteControlClient";
  private final Object mCacheLock = new Object();
  private int mCurrentClientGenId = -1;
  private MediaMetadata mMediaMetadata;
  private Bundle mMetadata = new Bundle();
  private OnMetadataUpdateListener mMetadataUpdateListener;
  private boolean mNeedsPositionSync = false;
  private Bitmap mOriginalArtwork;
  private long mPlaybackPositionMs = -1L;
  private float mPlaybackSpeed = 1.0F;
  private int mPlaybackState = 0;
  private long mPlaybackStateChangeTimeMs = 0L;
  private OnGetPlaybackPositionListener mPositionProvider;
  private OnPlaybackPositionUpdateListener mPositionUpdateListener;
  private final PendingIntent mRcMediaIntent;
  private MediaSession mSession;
  private PlaybackState mSessionPlaybackState = null;
  private int mTransportControlFlags = 0;
  private MediaSession.Callback mTransportListener = new MediaSession.Callback()
  {
    public void onSeekTo(long paramAnonymousLong)
    {
      RemoteControlClient.this.onSeekTo(mCurrentClientGenId, paramAnonymousLong);
    }
    
    public void onSetRating(Rating paramAnonymousRating)
    {
      if ((mTransportControlFlags & 0x200) != 0) {
        RemoteControlClient.this.onUpdateMetadata(mCurrentClientGenId, 268435457, paramAnonymousRating);
      }
    }
  };
  
  public RemoteControlClient(PendingIntent paramPendingIntent)
  {
    mRcMediaIntent = paramPendingIntent;
  }
  
  public RemoteControlClient(PendingIntent paramPendingIntent, Looper paramLooper)
  {
    mRcMediaIntent = paramPendingIntent;
  }
  
  private static long getCheckPeriodFromSpeed(float paramFloat)
  {
    if (Math.abs(paramFloat) <= 1.0F) {
      return 15000L;
    }
    return Math.max((15000.0F / Math.abs(paramFloat)), 2000L);
  }
  
  private void onSeekTo(int paramInt, long paramLong)
  {
    synchronized (mCacheLock)
    {
      if ((mCurrentClientGenId == paramInt) && (mPositionUpdateListener != null)) {
        mPositionUpdateListener.onPlaybackPositionUpdate(paramLong);
      }
      return;
    }
  }
  
  private void onUpdateMetadata(int paramInt1, int paramInt2, Object paramObject)
  {
    synchronized (mCacheLock)
    {
      if ((mCurrentClientGenId == paramInt1) && (mMetadataUpdateListener != null)) {
        mMetadataUpdateListener.onMetadataUpdate(paramInt2, paramObject);
      }
      return;
    }
  }
  
  static boolean playbackPositionShouldMove(int paramInt)
  {
    switch (paramInt)
    {
    case 3: 
    case 4: 
    case 5: 
    default: 
      return true;
    }
    return false;
  }
  
  private void setPlaybackStateInt(int paramInt, long paramLong, float paramFloat, boolean paramBoolean)
  {
    synchronized (mCacheLock)
    {
      if ((mPlaybackState != paramInt) || (mPlaybackPositionMs != paramLong) || (mPlaybackSpeed != paramFloat))
      {
        mPlaybackState = paramInt;
        long l = -1L;
        if (paramBoolean)
        {
          if (paramLong < 0L) {
            mPlaybackPositionMs = -1L;
          } else {
            mPlaybackPositionMs = paramLong;
          }
        }
        else {
          mPlaybackPositionMs = -9216204211029966080L;
        }
        mPlaybackSpeed = paramFloat;
        mPlaybackStateChangeTimeMs = SystemClock.elapsedRealtime();
        if (mSession != null)
        {
          paramInt = PlaybackState.getStateFromRccState(paramInt);
          if (paramBoolean) {
            paramLong = mPlaybackPositionMs;
          } else {
            paramLong = l;
          }
          PlaybackState.Builder localBuilder = new android/media/session/PlaybackState$Builder;
          localBuilder.<init>(mSessionPlaybackState);
          localBuilder.setState(paramInt, paramLong, paramFloat, SystemClock.elapsedRealtime());
          localBuilder.setErrorMessage(null);
          mSessionPlaybackState = localBuilder.build();
          mSession.setPlaybackState(mSessionPlaybackState);
        }
      }
      return;
    }
  }
  
  public MetadataEditor editMetadata(boolean paramBoolean)
  {
    MetadataEditor localMetadataEditor = new MetadataEditor(null);
    if (paramBoolean)
    {
      mEditorMetadata = new Bundle();
      mEditorArtwork = null;
      mMetadataChanged = true;
      mArtworkChanged = true;
      mEditableKeys = 0L;
    }
    else
    {
      mEditorMetadata = new Bundle(mMetadata);
      mEditorArtwork = mOriginalArtwork;
      mMetadataChanged = false;
      mArtworkChanged = false;
    }
    if ((!paramBoolean) && (mMediaMetadata != null)) {
      mMetadataBuilder = new MediaMetadata.Builder(mMediaMetadata);
    } else {
      mMetadataBuilder = new MediaMetadata.Builder();
    }
    return localMetadataEditor;
  }
  
  public MediaSession getMediaSession()
  {
    return mSession;
  }
  
  public PendingIntent getRcMediaIntent()
  {
    return mRcMediaIntent;
  }
  
  public void registerWithSession(MediaSessionLegacyHelper paramMediaSessionLegacyHelper)
  {
    paramMediaSessionLegacyHelper.addRccListener(mRcMediaIntent, mTransportListener);
    mSession = paramMediaSessionLegacyHelper.getSession(mRcMediaIntent);
    setTransportControlFlags(mTransportControlFlags);
  }
  
  public void setMetadataUpdateListener(OnMetadataUpdateListener paramOnMetadataUpdateListener)
  {
    synchronized (mCacheLock)
    {
      mMetadataUpdateListener = paramOnMetadataUpdateListener;
      return;
    }
  }
  
  public void setOnGetPlaybackPositionListener(OnGetPlaybackPositionListener paramOnGetPlaybackPositionListener)
  {
    synchronized (mCacheLock)
    {
      mPositionProvider = paramOnGetPlaybackPositionListener;
      return;
    }
  }
  
  public void setPlaybackPositionUpdateListener(OnPlaybackPositionUpdateListener paramOnPlaybackPositionUpdateListener)
  {
    synchronized (mCacheLock)
    {
      mPositionUpdateListener = paramOnPlaybackPositionUpdateListener;
      return;
    }
  }
  
  public void setPlaybackState(int paramInt)
  {
    setPlaybackStateInt(paramInt, -9216204211029966080L, 1.0F, false);
  }
  
  public void setPlaybackState(int paramInt, long paramLong, float paramFloat)
  {
    setPlaybackStateInt(paramInt, paramLong, paramFloat, true);
  }
  
  public void setTransportControlFlags(int paramInt)
  {
    synchronized (mCacheLock)
    {
      mTransportControlFlags = paramInt;
      if (mSession != null)
      {
        PlaybackState.Builder localBuilder = new android/media/session/PlaybackState$Builder;
        localBuilder.<init>(mSessionPlaybackState);
        localBuilder.setActions(PlaybackState.getActionsFromRccControlFlags(paramInt));
        mSessionPlaybackState = localBuilder.build();
        mSession.setPlaybackState(mSessionPlaybackState);
      }
      return;
    }
  }
  
  public void unregisterWithSession(MediaSessionLegacyHelper paramMediaSessionLegacyHelper)
  {
    paramMediaSessionLegacyHelper.removeRccListener(mRcMediaIntent);
    mSession = null;
  }
  
  @Deprecated
  public class MetadataEditor
    extends MediaMetadataEditor
  {
    public static final int BITMAP_KEY_ARTWORK = 100;
    public static final int METADATA_KEY_ARTWORK = 100;
    
    private MetadataEditor() {}
    
    /* Error */
    public void apply()
    {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: getfield 30	android/media/RemoteControlClient$MetadataEditor:mApplied	Z
      //   6: ifeq +14 -> 20
      //   9: ldc 32
      //   11: ldc 34
      //   13: invokestatic 40	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
      //   16: pop
      //   17: aload_0
      //   18: monitorexit
      //   19: return
      //   20: aload_0
      //   21: getfield 18	android/media/RemoteControlClient$MetadataEditor:this$0	Landroid/media/RemoteControlClient;
      //   24: invokestatic 44	android/media/RemoteControlClient:access$000	(Landroid/media/RemoteControlClient;)Ljava/lang/Object;
      //   27: astore_1
      //   28: aload_1
      //   29: monitorenter
      //   30: aload_0
      //   31: getfield 18	android/media/RemoteControlClient$MetadataEditor:this$0	Landroid/media/RemoteControlClient;
      //   34: astore_2
      //   35: new 46	android/os/Bundle
      //   38: astore_3
      //   39: aload_3
      //   40: aload_0
      //   41: getfield 50	android/media/RemoteControlClient$MetadataEditor:mEditorMetadata	Landroid/os/Bundle;
      //   44: invokespecial 53	android/os/Bundle:<init>	(Landroid/os/Bundle;)V
      //   47: aload_2
      //   48: aload_3
      //   49: invokestatic 57	android/media/RemoteControlClient:access$102	(Landroid/media/RemoteControlClient;Landroid/os/Bundle;)Landroid/os/Bundle;
      //   52: pop
      //   53: aload_0
      //   54: getfield 18	android/media/RemoteControlClient$MetadataEditor:this$0	Landroid/media/RemoteControlClient;
      //   57: invokestatic 61	android/media/RemoteControlClient:access$100	(Landroid/media/RemoteControlClient;)Landroid/os/Bundle;
      //   60: ldc 62
      //   62: invokestatic 68	java/lang/String:valueOf	(I)Ljava/lang/String;
      //   65: aload_0
      //   66: getfield 72	android/media/RemoteControlClient$MetadataEditor:mEditableKeys	J
      //   69: invokevirtual 76	android/os/Bundle:putLong	(Ljava/lang/String;J)V
      //   72: aload_0
      //   73: getfield 18	android/media/RemoteControlClient$MetadataEditor:this$0	Landroid/media/RemoteControlClient;
      //   76: invokestatic 80	android/media/RemoteControlClient:access$200	(Landroid/media/RemoteControlClient;)Landroid/graphics/Bitmap;
      //   79: ifnull +30 -> 109
      //   82: aload_0
      //   83: getfield 18	android/media/RemoteControlClient$MetadataEditor:this$0	Landroid/media/RemoteControlClient;
      //   86: invokestatic 80	android/media/RemoteControlClient:access$200	(Landroid/media/RemoteControlClient;)Landroid/graphics/Bitmap;
      //   89: aload_0
      //   90: getfield 84	android/media/RemoteControlClient$MetadataEditor:mEditorArtwork	Landroid/graphics/Bitmap;
      //   93: invokevirtual 90	java/lang/Object:equals	(Ljava/lang/Object;)Z
      //   96: ifne +13 -> 109
      //   99: aload_0
      //   100: getfield 18	android/media/RemoteControlClient$MetadataEditor:this$0	Landroid/media/RemoteControlClient;
      //   103: invokestatic 80	android/media/RemoteControlClient:access$200	(Landroid/media/RemoteControlClient;)Landroid/graphics/Bitmap;
      //   106: invokevirtual 95	android/graphics/Bitmap:recycle	()V
      //   109: aload_0
      //   110: getfield 18	android/media/RemoteControlClient$MetadataEditor:this$0	Landroid/media/RemoteControlClient;
      //   113: aload_0
      //   114: getfield 84	android/media/RemoteControlClient$MetadataEditor:mEditorArtwork	Landroid/graphics/Bitmap;
      //   117: invokestatic 99	android/media/RemoteControlClient:access$202	(Landroid/media/RemoteControlClient;Landroid/graphics/Bitmap;)Landroid/graphics/Bitmap;
      //   120: pop
      //   121: aload_0
      //   122: aconst_null
      //   123: putfield 84	android/media/RemoteControlClient$MetadataEditor:mEditorArtwork	Landroid/graphics/Bitmap;
      //   126: aload_0
      //   127: getfield 18	android/media/RemoteControlClient$MetadataEditor:this$0	Landroid/media/RemoteControlClient;
      //   130: invokestatic 103	android/media/RemoteControlClient:access$300	(Landroid/media/RemoteControlClient;)Landroid/media/session/MediaSession;
      //   133: ifnull +42 -> 175
      //   136: aload_0
      //   137: getfield 107	android/media/RemoteControlClient$MetadataEditor:mMetadataBuilder	Landroid/media/MediaMetadata$Builder;
      //   140: ifnull +35 -> 175
      //   143: aload_0
      //   144: getfield 18	android/media/RemoteControlClient$MetadataEditor:this$0	Landroid/media/RemoteControlClient;
      //   147: aload_0
      //   148: getfield 107	android/media/RemoteControlClient$MetadataEditor:mMetadataBuilder	Landroid/media/MediaMetadata$Builder;
      //   151: invokevirtual 113	android/media/MediaMetadata$Builder:build	()Landroid/media/MediaMetadata;
      //   154: invokestatic 117	android/media/RemoteControlClient:access$402	(Landroid/media/RemoteControlClient;Landroid/media/MediaMetadata;)Landroid/media/MediaMetadata;
      //   157: pop
      //   158: aload_0
      //   159: getfield 18	android/media/RemoteControlClient$MetadataEditor:this$0	Landroid/media/RemoteControlClient;
      //   162: invokestatic 103	android/media/RemoteControlClient:access$300	(Landroid/media/RemoteControlClient;)Landroid/media/session/MediaSession;
      //   165: aload_0
      //   166: getfield 18	android/media/RemoteControlClient$MetadataEditor:this$0	Landroid/media/RemoteControlClient;
      //   169: invokestatic 121	android/media/RemoteControlClient:access$400	(Landroid/media/RemoteControlClient;)Landroid/media/MediaMetadata;
      //   172: invokevirtual 127	android/media/session/MediaSession:setMetadata	(Landroid/media/MediaMetadata;)V
      //   175: aload_0
      //   176: iconst_1
      //   177: putfield 30	android/media/RemoteControlClient$MetadataEditor:mApplied	Z
      //   180: aload_1
      //   181: monitorexit
      //   182: aload_0
      //   183: monitorexit
      //   184: return
      //   185: astore_2
      //   186: aload_1
      //   187: monitorexit
      //   188: aload_2
      //   189: athrow
      //   190: astore_1
      //   191: aload_0
      //   192: monitorexit
      //   193: aload_1
      //   194: athrow
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	195	0	this	MetadataEditor
      //   190	4	1	localObject2	Object
      //   34	14	2	localRemoteControlClient	RemoteControlClient
      //   185	4	2	localObject3	Object
      //   38	11	3	localBundle	Bundle
      // Exception table:
      //   from	to	target	type
      //   30	109	185	finally
      //   109	175	185	finally
      //   175	182	185	finally
      //   186	188	185	finally
      //   2	17	190	finally
      //   20	30	190	finally
      //   188	190	190	finally
    }
    
    public void clear()
    {
      try
      {
        super.clear();
        return;
      }
      finally
      {
        localObject = finally;
        throw localObject;
      }
    }
    
    public Object clone()
      throws CloneNotSupportedException
    {
      throw new CloneNotSupportedException();
    }
    
    public MetadataEditor putBitmap(int paramInt, Bitmap paramBitmap)
      throws IllegalArgumentException
    {
      try
      {
        super.putBitmap(paramInt, paramBitmap);
        if (mMetadataBuilder != null)
        {
          String str = MediaMetadata.getKeyFromMetadataEditorKey(paramInt);
          if (str != null) {
            mMetadataBuilder.putBitmap(str, paramBitmap);
          }
        }
        return this;
      }
      finally {}
    }
    
    public MetadataEditor putLong(int paramInt, long paramLong)
      throws IllegalArgumentException
    {
      try
      {
        super.putLong(paramInt, paramLong);
        if (mMetadataBuilder != null)
        {
          String str = MediaMetadata.getKeyFromMetadataEditorKey(paramInt);
          if (str != null) {
            mMetadataBuilder.putLong(str, paramLong);
          }
        }
        return this;
      }
      finally {}
    }
    
    public MetadataEditor putObject(int paramInt, Object paramObject)
      throws IllegalArgumentException
    {
      try
      {
        super.putObject(paramInt, paramObject);
        if ((mMetadataBuilder != null) && ((paramInt == 268435457) || (paramInt == 101)))
        {
          String str = MediaMetadata.getKeyFromMetadataEditorKey(paramInt);
          if (str != null) {
            mMetadataBuilder.putRating(str, (Rating)paramObject);
          }
        }
        return this;
      }
      finally {}
    }
    
    public MetadataEditor putString(int paramInt, String paramString)
      throws IllegalArgumentException
    {
      try
      {
        super.putString(paramInt, paramString);
        if (mMetadataBuilder != null)
        {
          String str = MediaMetadata.getKeyFromMetadataEditorKey(paramInt);
          if (str != null) {
            mMetadataBuilder.putText(str, paramString);
          }
        }
        return this;
      }
      finally {}
    }
  }
  
  public static abstract interface OnGetPlaybackPositionListener
  {
    public abstract long onGetPlaybackPosition();
  }
  
  public static abstract interface OnMetadataUpdateListener
  {
    public abstract void onMetadataUpdate(int paramInt, Object paramObject);
  }
  
  public static abstract interface OnPlaybackPositionUpdateListener
  {
    public abstract void onPlaybackPositionUpdate(long paramLong);
  }
}
