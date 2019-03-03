package android.media.session;

import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.media.MediaMetadata;
import android.media.Rating;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.ArrayMap;
import android.util.Log;
import android.view.KeyEvent;

public class MediaSessionLegacyHelper
{
  private static final boolean DEBUG = Log.isLoggable("MediaSessionHelper", 3);
  private static final String TAG = "MediaSessionHelper";
  private static MediaSessionLegacyHelper sInstance;
  private static final Object sLock = new Object();
  private Context mContext;
  private Handler mHandler = new Handler(Looper.getMainLooper());
  private MediaSessionManager mSessionManager;
  private ArrayMap<PendingIntent, SessionHolder> mSessions = new ArrayMap();
  
  private MediaSessionLegacyHelper(Context paramContext)
  {
    mContext = paramContext;
    mSessionManager = ((MediaSessionManager)paramContext.getSystemService("media_session"));
  }
  
  public static MediaSessionLegacyHelper getHelper(Context paramContext)
  {
    synchronized (sLock)
    {
      if (sInstance == null)
      {
        MediaSessionLegacyHelper localMediaSessionLegacyHelper = new android/media/session/MediaSessionLegacyHelper;
        localMediaSessionLegacyHelper.<init>(paramContext.getApplicationContext());
        sInstance = localMediaSessionLegacyHelper;
      }
      return sInstance;
    }
  }
  
  private SessionHolder getHolder(PendingIntent paramPendingIntent, boolean paramBoolean)
  {
    Object localObject1 = (SessionHolder)mSessions.get(paramPendingIntent);
    Object localObject2 = localObject1;
    if (localObject1 == null)
    {
      localObject2 = localObject1;
      if (paramBoolean)
      {
        localObject2 = mContext;
        localObject1 = new StringBuilder();
        ((StringBuilder)localObject1).append("MediaSessionHelper-");
        ((StringBuilder)localObject1).append(paramPendingIntent.getCreatorPackage());
        localObject2 = new MediaSession((Context)localObject2, ((StringBuilder)localObject1).toString());
        ((MediaSession)localObject2).setActive(true);
        localObject2 = new SessionHolder((MediaSession)localObject2, paramPendingIntent);
        mSessions.put(paramPendingIntent, localObject2);
      }
    }
    return localObject2;
  }
  
  public static Bundle getOldMetadata(MediaMetadata paramMediaMetadata, int paramInt1, int paramInt2)
  {
    int i;
    if ((paramInt1 != -1) && (paramInt2 != -1)) {
      i = 1;
    } else {
      i = 0;
    }
    Bundle localBundle = new Bundle();
    if (paramMediaMetadata.containsKey("android.media.metadata.ALBUM")) {
      localBundle.putString(String.valueOf(1), paramMediaMetadata.getString("android.media.metadata.ALBUM"));
    }
    if ((i != 0) && (paramMediaMetadata.containsKey("android.media.metadata.ART"))) {
      localBundle.putParcelable(String.valueOf(100), scaleBitmapIfTooBig(paramMediaMetadata.getBitmap("android.media.metadata.ART"), paramInt1, paramInt2));
    } else if ((i != 0) && (paramMediaMetadata.containsKey("android.media.metadata.ALBUM_ART"))) {
      localBundle.putParcelable(String.valueOf(100), scaleBitmapIfTooBig(paramMediaMetadata.getBitmap("android.media.metadata.ALBUM_ART"), paramInt1, paramInt2));
    }
    if (paramMediaMetadata.containsKey("android.media.metadata.ALBUM_ARTIST")) {
      localBundle.putString(String.valueOf(13), paramMediaMetadata.getString("android.media.metadata.ALBUM_ARTIST"));
    }
    if (paramMediaMetadata.containsKey("android.media.metadata.ARTIST")) {
      localBundle.putString(String.valueOf(2), paramMediaMetadata.getString("android.media.metadata.ARTIST"));
    }
    if (paramMediaMetadata.containsKey("android.media.metadata.AUTHOR")) {
      localBundle.putString(String.valueOf(3), paramMediaMetadata.getString("android.media.metadata.AUTHOR"));
    }
    if (paramMediaMetadata.containsKey("android.media.metadata.COMPILATION")) {
      localBundle.putString(String.valueOf(15), paramMediaMetadata.getString("android.media.metadata.COMPILATION"));
    }
    if (paramMediaMetadata.containsKey("android.media.metadata.COMPOSER")) {
      localBundle.putString(String.valueOf(4), paramMediaMetadata.getString("android.media.metadata.COMPOSER"));
    }
    if (paramMediaMetadata.containsKey("android.media.metadata.DATE")) {
      localBundle.putString(String.valueOf(5), paramMediaMetadata.getString("android.media.metadata.DATE"));
    }
    if (paramMediaMetadata.containsKey("android.media.metadata.DISC_NUMBER")) {
      localBundle.putLong(String.valueOf(14), paramMediaMetadata.getLong("android.media.metadata.DISC_NUMBER"));
    }
    if (paramMediaMetadata.containsKey("android.media.metadata.DURATION")) {
      localBundle.putLong(String.valueOf(9), paramMediaMetadata.getLong("android.media.metadata.DURATION"));
    }
    if (paramMediaMetadata.containsKey("android.media.metadata.GENRE")) {
      localBundle.putString(String.valueOf(6), paramMediaMetadata.getString("android.media.metadata.GENRE"));
    }
    if (paramMediaMetadata.containsKey("android.media.metadata.NUM_TRACKS")) {
      localBundle.putLong(String.valueOf(10), paramMediaMetadata.getLong("android.media.metadata.NUM_TRACKS"));
    }
    if (paramMediaMetadata.containsKey("android.media.metadata.RATING")) {
      localBundle.putParcelable(String.valueOf(101), paramMediaMetadata.getRating("android.media.metadata.RATING"));
    }
    if (paramMediaMetadata.containsKey("android.media.metadata.USER_RATING")) {
      localBundle.putParcelable(String.valueOf(268435457), paramMediaMetadata.getRating("android.media.metadata.USER_RATING"));
    }
    if (paramMediaMetadata.containsKey("android.media.metadata.TITLE")) {
      localBundle.putString(String.valueOf(7), paramMediaMetadata.getString("android.media.metadata.TITLE"));
    }
    if (paramMediaMetadata.containsKey("android.media.metadata.TRACK_NUMBER")) {
      localBundle.putLong(String.valueOf(0), paramMediaMetadata.getLong("android.media.metadata.TRACK_NUMBER"));
    }
    if (paramMediaMetadata.containsKey("android.media.metadata.WRITER")) {
      localBundle.putString(String.valueOf(11), paramMediaMetadata.getString("android.media.metadata.WRITER"));
    }
    if (paramMediaMetadata.containsKey("android.media.metadata.YEAR")) {
      localBundle.putLong(String.valueOf(8), paramMediaMetadata.getLong("android.media.metadata.YEAR"));
    }
    return localBundle;
  }
  
  private static Bitmap scaleBitmapIfTooBig(Bitmap paramBitmap, int paramInt1, int paramInt2)
  {
    Bitmap localBitmap = paramBitmap;
    Object localObject = localBitmap;
    if (localBitmap != null)
    {
      int i = paramBitmap.getWidth();
      int j = paramBitmap.getHeight();
      if (i <= paramInt1)
      {
        localObject = localBitmap;
        if (j <= paramInt2) {}
      }
      else
      {
        float f = Math.min(paramInt1 / i, paramInt2 / j);
        paramInt1 = Math.round(i * f);
        paramInt2 = Math.round(j * f);
        localObject = paramBitmap.getConfig();
        paramBitmap = (Bitmap)localObject;
        if (localObject == null) {
          paramBitmap = Bitmap.Config.ARGB_8888;
        }
        localObject = Bitmap.createBitmap(paramInt1, paramInt2, paramBitmap);
        paramBitmap = new Canvas((Bitmap)localObject);
        Paint localPaint = new Paint();
        localPaint.setAntiAlias(true);
        localPaint.setFilterBitmap(true);
        paramBitmap.drawBitmap(localBitmap, null, new RectF(0.0F, 0.0F, ((Bitmap)localObject).getWidth(), ((Bitmap)localObject).getHeight()), localPaint);
      }
    }
    return localObject;
  }
  
  private static void sendKeyEvent(PendingIntent paramPendingIntent, Context paramContext, Intent paramIntent)
  {
    try
    {
      paramPendingIntent.send(paramContext, 0, paramIntent);
      return;
    }
    catch (PendingIntent.CanceledException paramPendingIntent)
    {
      Log.e("MediaSessionHelper", "Error sending media key down event:", paramPendingIntent);
    }
  }
  
  public void addMediaButtonListener(PendingIntent paramPendingIntent, ComponentName paramComponentName, Context paramContext)
  {
    if (paramPendingIntent == null)
    {
      Log.w("MediaSessionHelper", "Pending intent was null, can't addMediaButtonListener.");
      return;
    }
    paramComponentName = getHolder(paramPendingIntent, true);
    if (paramComponentName == null) {
      return;
    }
    if ((mMediaButtonListener != null) && (DEBUG))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("addMediaButtonListener already added ");
      localStringBuilder.append(paramPendingIntent);
      Log.d("MediaSessionHelper", localStringBuilder.toString());
    }
    mMediaButtonListener = new MediaButtonListener(paramPendingIntent, paramContext);
    mFlags = (0x1 | mFlags);
    mSession.setFlags(mFlags);
    mSession.setMediaButtonReceiver(paramPendingIntent);
    paramComponentName.update();
    if (DEBUG)
    {
      paramComponentName = new StringBuilder();
      paramComponentName.append("addMediaButtonListener added ");
      paramComponentName.append(paramPendingIntent);
      Log.d("MediaSessionHelper", paramComponentName.toString());
    }
  }
  
  public void addRccListener(PendingIntent paramPendingIntent, MediaSession.Callback paramCallback)
  {
    if (paramPendingIntent == null)
    {
      Log.w("MediaSessionHelper", "Pending intent was null, can't add rcc listener.");
      return;
    }
    SessionHolder localSessionHolder = getHolder(paramPendingIntent, true);
    if (localSessionHolder == null) {
      return;
    }
    if ((mRccListener != null) && (mRccListener == paramCallback))
    {
      if (DEBUG) {
        Log.d("MediaSessionHelper", "addRccListener listener already added.");
      }
      return;
    }
    mRccListener = paramCallback;
    mFlags |= 0x2;
    mSession.setFlags(mFlags);
    localSessionHolder.update();
    if (DEBUG)
    {
      paramCallback = new StringBuilder();
      paramCallback.append("Added rcc listener for ");
      paramCallback.append(paramPendingIntent);
      paramCallback.append(".");
      Log.d("MediaSessionHelper", paramCallback.toString());
    }
  }
  
  public MediaSession getSession(PendingIntent paramPendingIntent)
  {
    paramPendingIntent = (SessionHolder)mSessions.get(paramPendingIntent);
    if (paramPendingIntent == null) {
      paramPendingIntent = null;
    } else {
      paramPendingIntent = mSession;
    }
    return paramPendingIntent;
  }
  
  public boolean isGlobalPriorityActive()
  {
    return mSessionManager.isGlobalPriorityActive();
  }
  
  public void removeMediaButtonListener(PendingIntent paramPendingIntent)
  {
    if (paramPendingIntent == null) {
      return;
    }
    Object localObject = getHolder(paramPendingIntent, false);
    if ((localObject != null) && (mMediaButtonListener != null))
    {
      mFlags &= 0xFFFFFFFE;
      mSession.setFlags(mFlags);
      mMediaButtonListener = null;
      ((SessionHolder)localObject).update();
      if (DEBUG)
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("removeMediaButtonListener removed ");
        ((StringBuilder)localObject).append(paramPendingIntent);
        Log.d("MediaSessionHelper", ((StringBuilder)localObject).toString());
      }
    }
  }
  
  public void removeRccListener(PendingIntent paramPendingIntent)
  {
    if (paramPendingIntent == null) {
      return;
    }
    Object localObject = getHolder(paramPendingIntent, false);
    if ((localObject != null) && (mRccListener != null))
    {
      mRccListener = null;
      mFlags &= 0xFFFFFFFD;
      mSession.setFlags(mFlags);
      ((SessionHolder)localObject).update();
      if (DEBUG)
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("Removed rcc listener for ");
        ((StringBuilder)localObject).append(paramPendingIntent);
        ((StringBuilder)localObject).append(".");
        Log.d("MediaSessionHelper", ((StringBuilder)localObject).toString());
      }
    }
  }
  
  public void sendAdjustVolumeBy(int paramInt1, int paramInt2, int paramInt3)
  {
    mSessionManager.dispatchAdjustVolume(paramInt1, paramInt2, paramInt3);
    if (DEBUG) {
      Log.d("MediaSessionHelper", "dispatched volume adjustment");
    }
  }
  
  public void sendMediaButtonEvent(KeyEvent paramKeyEvent, boolean paramBoolean)
  {
    if (paramKeyEvent == null)
    {
      Log.w("MediaSessionHelper", "Tried to send a null key event. Ignoring.");
      return;
    }
    mSessionManager.dispatchMediaKeyEvent(paramKeyEvent, paramBoolean);
    if (DEBUG)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("dispatched media key ");
      localStringBuilder.append(paramKeyEvent);
      Log.d("MediaSessionHelper", localStringBuilder.toString());
    }
  }
  
  public void sendVolumeKeyEvent(KeyEvent paramKeyEvent, int paramInt, boolean paramBoolean)
  {
    if (paramKeyEvent == null)
    {
      Log.w("MediaSessionHelper", "Tried to send a null key event. Ignoring.");
      return;
    }
    mSessionManager.dispatchVolumeKeyEvent(paramKeyEvent, paramInt, paramBoolean);
  }
  
  private static final class MediaButtonListener
    extends MediaSession.Callback
  {
    private final Context mContext;
    private final PendingIntent mPendingIntent;
    
    public MediaButtonListener(PendingIntent paramPendingIntent, Context paramContext)
    {
      mPendingIntent = paramPendingIntent;
      mContext = paramContext;
    }
    
    private void sendKeyEvent(int paramInt)
    {
      KeyEvent localKeyEvent = new KeyEvent(0, paramInt);
      Object localObject = new Intent("android.intent.action.MEDIA_BUTTON");
      ((Intent)localObject).addFlags(268435456);
      ((Intent)localObject).putExtra("android.intent.extra.KEY_EVENT", localKeyEvent);
      MediaSessionLegacyHelper.sendKeyEvent(mPendingIntent, mContext, (Intent)localObject);
      ((Intent)localObject).putExtra("android.intent.extra.KEY_EVENT", new KeyEvent(1, paramInt));
      MediaSessionLegacyHelper.sendKeyEvent(mPendingIntent, mContext, (Intent)localObject);
      if (MediaSessionLegacyHelper.DEBUG)
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("Sent ");
        ((StringBuilder)localObject).append(paramInt);
        ((StringBuilder)localObject).append(" to pending intent ");
        ((StringBuilder)localObject).append(mPendingIntent);
        Log.d("MediaSessionHelper", ((StringBuilder)localObject).toString());
      }
    }
    
    public void onFastForward()
    {
      sendKeyEvent(90);
    }
    
    public boolean onMediaButtonEvent(Intent paramIntent)
    {
      MediaSessionLegacyHelper.sendKeyEvent(mPendingIntent, mContext, paramIntent);
      return true;
    }
    
    public void onPause()
    {
      sendKeyEvent(127);
    }
    
    public void onPlay()
    {
      sendKeyEvent(126);
    }
    
    public void onRewind()
    {
      sendKeyEvent(89);
    }
    
    public void onSkipToNext()
    {
      sendKeyEvent(87);
    }
    
    public void onSkipToPrevious()
    {
      sendKeyEvent(88);
    }
    
    public void onStop()
    {
      sendKeyEvent(86);
    }
  }
  
  private class SessionHolder
  {
    public SessionCallback mCb;
    public int mFlags;
    public MediaSessionLegacyHelper.MediaButtonListener mMediaButtonListener;
    public final PendingIntent mPi;
    public MediaSession.Callback mRccListener;
    public final MediaSession mSession;
    
    public SessionHolder(MediaSession paramMediaSession, PendingIntent paramPendingIntent)
    {
      mSession = paramMediaSession;
      mPi = paramPendingIntent;
    }
    
    public void update()
    {
      if ((mMediaButtonListener == null) && (mRccListener == null))
      {
        mSession.setCallback(null);
        mSession.release();
        mCb = null;
        mSessions.remove(mPi);
      }
      else if (mCb == null)
      {
        mCb = new SessionCallback(null);
        Handler localHandler = new Handler(Looper.getMainLooper());
        mSession.setCallback(mCb, localHandler);
      }
    }
    
    private class SessionCallback
      extends MediaSession.Callback
    {
      private SessionCallback() {}
      
      public void onFastForward()
      {
        if (mMediaButtonListener != null) {
          mMediaButtonListener.onFastForward();
        }
      }
      
      public boolean onMediaButtonEvent(Intent paramIntent)
      {
        if (mMediaButtonListener != null) {
          mMediaButtonListener.onMediaButtonEvent(paramIntent);
        }
        return true;
      }
      
      public void onPause()
      {
        if (mMediaButtonListener != null) {
          mMediaButtonListener.onPause();
        }
      }
      
      public void onPlay()
      {
        if (mMediaButtonListener != null) {
          mMediaButtonListener.onPlay();
        }
      }
      
      public void onRewind()
      {
        if (mMediaButtonListener != null) {
          mMediaButtonListener.onRewind();
        }
      }
      
      public void onSeekTo(long paramLong)
      {
        if (mRccListener != null) {
          mRccListener.onSeekTo(paramLong);
        }
      }
      
      public void onSetRating(Rating paramRating)
      {
        if (mRccListener != null) {
          mRccListener.onSetRating(paramRating);
        }
      }
      
      public void onSkipToNext()
      {
        if (mMediaButtonListener != null) {
          mMediaButtonListener.onSkipToNext();
        }
      }
      
      public void onSkipToPrevious()
      {
        if (mMediaButtonListener != null) {
          mMediaButtonListener.onSkipToPrevious();
        }
      }
      
      public void onStop()
      {
        if (mMediaButtonListener != null) {
          mMediaButtonListener.onStop();
        }
      }
    }
  }
}
