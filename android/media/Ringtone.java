package android.media;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.net.Uri;
import android.os.Binder;
import android.os.RemoteException;
import android.util.Log;
import java.io.IOException;
import java.util.ArrayList;

public class Ringtone
{
  private static final boolean LOGD = true;
  private static final String[] MEDIA_COLUMNS = { "_id", "_data", "title" };
  private static final String MEDIA_SELECTION = "mime_type LIKE 'audio/%' OR mime_type IN ('application/ogg', 'application/x-flac')";
  private static final String TAG = "Ringtone";
  private static final String TAGD = "Ringtone-debug";
  private static final ArrayList<Ringtone> sActiveRingtones = new ArrayList();
  private final boolean mAllowRemote;
  private AudioAttributes mAudioAttributes = new AudioAttributes.Builder().setUsage(6).setContentType(4).build();
  private final AudioManager mAudioManager;
  private final MyOnCompletionListener mCompletionListener = new MyOnCompletionListener();
  private final Context mContext;
  private boolean mIsLooping = false;
  private MediaPlayer mLocalPlayer;
  private final Object mPlaybackSettingsLock = new Object();
  private final IRingtonePlayer mRemotePlayer;
  private final Binder mRemoteToken;
  private String mTitle;
  private Uri mUri;
  private float mVolume = 1.0F;
  
  public Ringtone(Context paramContext, boolean paramBoolean)
  {
    mContext = paramContext;
    mAudioManager = ((AudioManager)mContext.getSystemService("audio"));
    mAllowRemote = paramBoolean;
    Object localObject = null;
    if (paramBoolean) {
      paramContext = mAudioManager.getRingtonePlayer();
    } else {
      paramContext = null;
    }
    mRemotePlayer = paramContext;
    paramContext = localObject;
    if (paramBoolean) {
      paramContext = new Binder();
    }
    mRemoteToken = paramContext;
  }
  
  private void applyPlaybackProperties_sync()
  {
    if (mLocalPlayer != null)
    {
      mLocalPlayer.setVolume(mVolume);
      mLocalPlayer.setLooping(mIsLooping);
    }
    else if ((mAllowRemote) && (mRemotePlayer != null))
    {
      try
      {
        mRemotePlayer.setPlaybackProperties(mRemoteToken, mVolume, mIsLooping);
      }
      catch (RemoteException localRemoteException)
      {
        Log.w("Ringtone", "Problem setting playback properties: ", localRemoteException);
      }
    }
    else
    {
      Log.w("Ringtone", "Neither local nor remote player available when applying playback properties");
    }
  }
  
  private void destroyLocalPlayer()
  {
    if (mLocalPlayer != null)
    {
      mLocalPlayer.setOnCompletionListener(null);
      mLocalPlayer.reset();
      mLocalPlayer.release();
      mLocalPlayer = null;
      synchronized (sActiveRingtones)
      {
        sActiveRingtones.remove(this);
      }
    }
  }
  
  /* Error */
  public static String getTitle(Context paramContext, Uri paramUri, boolean paramBoolean1, boolean paramBoolean2)
  {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual 183	android/content/Context:getContentResolver	()Landroid/content/ContentResolver;
    //   4: astore 4
    //   6: aconst_null
    //   7: astore 5
    //   9: aconst_null
    //   10: astore 6
    //   12: aconst_null
    //   13: astore 7
    //   15: aconst_null
    //   16: astore 8
    //   18: aload_1
    //   19: ifnull +348 -> 367
    //   22: aload_1
    //   23: invokevirtual 189	android/net/Uri:getAuthority	()Ljava/lang/String;
    //   26: invokestatic 195	android/content/ContentProvider:getAuthorityWithoutUserId	(Ljava/lang/String;)Ljava/lang/String;
    //   29: astore 9
    //   31: ldc -59
    //   33: aload 9
    //   35: invokevirtual 200	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   38: ifeq +39 -> 77
    //   41: iload_2
    //   42: ifeq +322 -> 364
    //   45: aload_0
    //   46: ldc -55
    //   48: iconst_1
    //   49: anewarray 4	java/lang/Object
    //   52: dup
    //   53: iconst_0
    //   54: aload_0
    //   55: aload_0
    //   56: aload_1
    //   57: invokestatic 207	android/media/RingtoneManager:getDefaultType	(Landroid/net/Uri;)I
    //   60: invokestatic 211	android/media/RingtoneManager:getActualDefaultRingtoneUri	(Landroid/content/Context;I)Landroid/net/Uri;
    //   63: iconst_0
    //   64: iload_3
    //   65: invokestatic 213	android/media/Ringtone:getTitle	(Landroid/content/Context;Landroid/net/Uri;ZZ)Ljava/lang/String;
    //   68: aastore
    //   69: invokevirtual 217	android/content/Context:getString	(I[Ljava/lang/Object;)Ljava/lang/String;
    //   72: astore 7
    //   74: goto +290 -> 364
    //   77: aconst_null
    //   78: astore 10
    //   80: aconst_null
    //   81: astore 11
    //   83: aconst_null
    //   84: astore 12
    //   86: aconst_null
    //   87: astore 13
    //   89: aload 11
    //   91: astore 14
    //   93: aload 12
    //   95: astore 7
    //   97: ldc -37
    //   99: aload 9
    //   101: invokevirtual 200	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   104: ifeq +125 -> 229
    //   107: iload_3
    //   108: ifeq +10 -> 118
    //   111: aload 10
    //   113: astore 13
    //   115: goto +10 -> 125
    //   118: ldc 16
    //   120: astore 13
    //   122: goto -7 -> 115
    //   125: aload 11
    //   127: astore 14
    //   129: aload 12
    //   131: astore 7
    //   133: aload 4
    //   135: aload_1
    //   136: getstatic 60	android/media/Ringtone:MEDIA_COLUMNS	[Ljava/lang/String;
    //   139: aload 13
    //   141: aconst_null
    //   142: aconst_null
    //   143: invokevirtual 225	android/content/ContentResolver:query	(Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   146: astore 11
    //   148: aload 11
    //   150: astore 13
    //   152: aload 11
    //   154: ifnull +75 -> 229
    //   157: aload 11
    //   159: astore 13
    //   161: aload 11
    //   163: astore 14
    //   165: aload 11
    //   167: astore 7
    //   169: aload 11
    //   171: invokeinterface 231 1 0
    //   176: iconst_1
    //   177: if_icmpne +52 -> 229
    //   180: aload 11
    //   182: astore 14
    //   184: aload 11
    //   186: astore 7
    //   188: aload 11
    //   190: invokeinterface 235 1 0
    //   195: pop
    //   196: aload 11
    //   198: astore 14
    //   200: aload 11
    //   202: astore 7
    //   204: aload 11
    //   206: iconst_2
    //   207: invokeinterface 238 2 0
    //   212: astore 13
    //   214: aload 11
    //   216: ifnull +10 -> 226
    //   219: aload 11
    //   221: invokeinterface 241 1 0
    //   226: aload 13
    //   228: areturn
    //   229: aload 5
    //   231: astore 14
    //   233: aload 13
    //   235: ifnull +18 -> 253
    //   238: aload 13
    //   240: astore 7
    //   242: aload 8
    //   244: astore 14
    //   246: aload 7
    //   248: invokeinterface 241 1 0
    //   253: goto +96 -> 349
    //   256: astore_0
    //   257: goto +56 -> 313
    //   260: astore 14
    //   262: aconst_null
    //   263: astore 11
    //   265: iload_3
    //   266: ifeq +21 -> 287
    //   269: aload 7
    //   271: astore 14
    //   273: aload_0
    //   274: ldc 101
    //   276: invokevirtual 107	android/content/Context:getSystemService	(Ljava/lang/String;)Ljava/lang/Object;
    //   279: checkcast 109	android/media/AudioManager
    //   282: invokevirtual 117	android/media/AudioManager:getRingtonePlayer	()Landroid/media/IRingtonePlayer;
    //   285: astore 11
    //   287: aload 6
    //   289: astore 13
    //   291: aload 11
    //   293: ifnull +40 -> 333
    //   296: aload 7
    //   298: astore 14
    //   300: aload 11
    //   302: aload_1
    //   303: invokeinterface 244 2 0
    //   308: astore 13
    //   310: goto +23 -> 333
    //   313: aload 14
    //   315: ifnull +10 -> 325
    //   318: aload 14
    //   320: invokeinterface 241 1 0
    //   325: aload_0
    //   326: athrow
    //   327: astore 14
    //   329: aload 6
    //   331: astore 13
    //   333: aload 13
    //   335: astore 14
    //   337: aload 7
    //   339: ifnull -86 -> 253
    //   342: aload 13
    //   344: astore 14
    //   346: goto -100 -> 246
    //   349: aload 14
    //   351: astore 7
    //   353: aload 14
    //   355: ifnonnull +9 -> 364
    //   358: aload_1
    //   359: invokevirtual 247	android/net/Uri:getLastPathSegment	()Ljava/lang/String;
    //   362: astore 7
    //   364: goto +11 -> 375
    //   367: aload_0
    //   368: ldc -8
    //   370: invokevirtual 249	android/content/Context:getString	(I)Ljava/lang/String;
    //   373: astore 7
    //   375: aload 7
    //   377: astore_1
    //   378: aload 7
    //   380: ifnonnull +19 -> 399
    //   383: aload_0
    //   384: ldc -6
    //   386: invokevirtual 249	android/content/Context:getString	(I)Ljava/lang/String;
    //   389: astore_0
    //   390: aload_0
    //   391: astore_1
    //   392: aload_0
    //   393: ifnonnull +6 -> 399
    //   396: ldc -4
    //   398: astore_1
    //   399: aload_1
    //   400: areturn
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	401	0	paramContext	Context
    //   0	401	1	paramUri	Uri
    //   0	401	2	paramBoolean1	boolean
    //   0	401	3	paramBoolean2	boolean
    //   4	130	4	localContentResolver	android.content.ContentResolver
    //   7	223	5	localObject1	Object
    //   10	320	6	localObject2	Object
    //   13	366	7	localObject3	Object
    //   16	227	8	localObject4	Object
    //   29	71	9	str	String
    //   78	34	10	localObject5	Object
    //   81	220	11	localObject6	Object
    //   84	46	12	localObject7	Object
    //   87	256	13	localObject8	Object
    //   91	154	14	localObject9	Object
    //   260	1	14	localSecurityException	SecurityException
    //   271	48	14	localObject10	Object
    //   327	1	14	localRemoteException	RemoteException
    //   335	19	14	localObject11	Object
    // Exception table:
    //   from	to	target	type
    //   97	107	256	finally
    //   133	148	256	finally
    //   169	180	256	finally
    //   188	196	256	finally
    //   204	214	256	finally
    //   273	287	256	finally
    //   300	310	256	finally
    //   97	107	260	java/lang/SecurityException
    //   133	148	260	java/lang/SecurityException
    //   169	180	260	java/lang/SecurityException
    //   188	196	260	java/lang/SecurityException
    //   204	214	260	java/lang/SecurityException
    //   300	310	327	android/os/RemoteException
  }
  
  private boolean playFallbackRingtone()
  {
    if (mAudioManager.getStreamVolume(AudioAttributes.toLegacyStreamType(mAudioAttributes)) != 0)
    {
      int i = RingtoneManager.getDefaultType(mUri);
      Object localObject1;
      if ((i != -1) && (RingtoneManager.getActualDefaultRingtoneUri(mContext, i) == null))
      {
        localObject1 = new StringBuilder();
        ((StringBuilder)localObject1).append("not playing fallback for ");
        ((StringBuilder)localObject1).append(mUri);
        Log.w("Ringtone", ((StringBuilder)localObject1).toString());
      }
      else
      {
        try
        {
          localObject1 = mContext.getResources().openRawResourceFd(17825797);
          if (localObject1 != null)
          {
            ??? = new android/media/MediaPlayer;
            ((MediaPlayer)???).<init>();
            mLocalPlayer = ((MediaPlayer)???);
            if (((AssetFileDescriptor)localObject1).getDeclaredLength() < 0L) {
              mLocalPlayer.setDataSource(((AssetFileDescriptor)localObject1).getFileDescriptor());
            } else {
              mLocalPlayer.setDataSource(((AssetFileDescriptor)localObject1).getFileDescriptor(), ((AssetFileDescriptor)localObject1).getStartOffset(), ((AssetFileDescriptor)localObject1).getDeclaredLength());
            }
            mLocalPlayer.setAudioAttributes(mAudioAttributes);
            synchronized (mPlaybackSettingsLock)
            {
              applyPlaybackProperties_sync();
              mLocalPlayer.prepare();
              startLocalPlayer();
              Log.d("Ringtone-debug", "playFallbackRingtone+after startLocalPlayer");
              ((AssetFileDescriptor)localObject1).close();
              return true;
            }
          }
          Log.e("Ringtone", "Could not load fallback ringtone");
        }
        catch (Resources.NotFoundException localNotFoundException)
        {
          Log.e("Ringtone", "Fallback ringtone does not exist");
        }
        catch (IOException localIOException)
        {
          destroyLocalPlayer();
          Log.e("Ringtone", "Failed to open fallback ringtone");
        }
      }
    }
    return false;
  }
  
  private void startLocalPlayer()
  {
    if (mLocalPlayer == null) {
      return;
    }
    synchronized (sActiveRingtones)
    {
      sActiveRingtones.add(this);
      mLocalPlayer.setOnCompletionListener(mCompletionListener);
      mLocalPlayer.start();
      Log.d("Ringtone-debug", "startLocalPlayer");
      return;
    }
  }
  
  protected void finalize()
  {
    if (mLocalPlayer != null) {
      mLocalPlayer.release();
    }
  }
  
  public AudioAttributes getAudioAttributes()
  {
    return mAudioAttributes;
  }
  
  @Deprecated
  public int getStreamType()
  {
    return AudioAttributes.toLegacyStreamType(mAudioAttributes);
  }
  
  public String getTitle(Context paramContext)
  {
    if (mTitle != null) {
      return mTitle;
    }
    paramContext = getTitle(paramContext, mUri, true, mAllowRemote);
    mTitle = paramContext;
    return paramContext;
  }
  
  public Uri getUri()
  {
    return mUri;
  }
  
  public float getVolume()
  {
    synchronized (mPlaybackSettingsLock)
    {
      float f = mVolume;
      return f;
    }
  }
  
  public boolean isLooping()
  {
    synchronized (mPlaybackSettingsLock)
    {
      boolean bool = mIsLooping;
      return bool;
    }
  }
  
  public boolean isPlaying()
  {
    if (mLocalPlayer != null) {
      return mLocalPlayer.isPlaying();
    }
    if ((mAllowRemote) && (mRemotePlayer != null)) {
      try
      {
        boolean bool = mRemotePlayer.isPlaying(mRemoteToken);
        return bool;
      }
      catch (RemoteException localRemoteException)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Problem checking ringtone: ");
        localStringBuilder.append(localRemoteException);
        Log.w("Ringtone", localStringBuilder.toString());
        return false;
      }
    }
    Log.w("Ringtone", "Neither local nor remote playback available");
    return false;
  }
  
  public void play()
  {
    if (mLocalPlayer != null)
    {
      if (mAudioManager.getStreamVolume(AudioAttributes.toLegacyStreamType(mAudioAttributes)) != 0)
      {
        startLocalPlayer();
        Log.d("Ringtone-debug", "after startLocalPlayer");
      }
    }
    else
    {
      if ((mAllowRemote) && (mRemotePlayer != null) && (mUri != null))
      {
        Object localObject1 = mUri.getCanonicalUri();
        ??? = new StringBuilder();
        ((StringBuilder)???).append("canonicalUri=");
        ((StringBuilder)???).append(localObject1);
        Log.d("Ringtone-debug", ((StringBuilder)???).toString());
        synchronized (mPlaybackSettingsLock)
        {
          boolean bool = mIsLooping;
          float f = mVolume;
          try
          {
            mRemotePlayer.play(mRemoteToken, (Uri)localObject1, mAudioAttributes, f, bool);
            ??? = new java/lang/StringBuilder;
            ((StringBuilder)???).<init>();
            ((StringBuilder)???).append("play+canonicalUri=");
            ((StringBuilder)???).append(localObject1);
            Log.d("Ringtone-debug", ((StringBuilder)???).toString());
          }
          catch (RemoteException localRemoteException)
          {
            if (!playFallbackRingtone())
            {
              localObject1 = new StringBuilder();
              ((StringBuilder)localObject1).append("Problem playing ringtone: ");
              ((StringBuilder)localObject1).append(localRemoteException);
              Log.w("Ringtone", ((StringBuilder)localObject1).toString());
            }
          }
        }
      }
      if (!playFallbackRingtone()) {
        Log.w("Ringtone", "Neither local nor remote playback available");
      }
    }
  }
  
  public void setAudioAttributes(AudioAttributes paramAudioAttributes)
    throws IllegalArgumentException
  {
    if (paramAudioAttributes != null)
    {
      mAudioAttributes = paramAudioAttributes;
      setUri(mUri);
      return;
    }
    throw new IllegalArgumentException("Invalid null AudioAttributes for Ringtone");
  }
  
  public void setLooping(boolean paramBoolean)
  {
    synchronized (mPlaybackSettingsLock)
    {
      mIsLooping = paramBoolean;
      applyPlaybackProperties_sync();
      return;
    }
  }
  
  @Deprecated
  public void setStreamType(int paramInt)
  {
    PlayerBase.deprecateStreamTypeForPlayback(paramInt, "Ringtone", "setStreamType()");
    setAudioAttributes(new AudioAttributes.Builder().setInternalLegacyStreamType(paramInt).build());
  }
  
  void setTitle(String paramString)
  {
    mTitle = paramString;
  }
  
  public void setUri(Uri paramUri)
  {
    destroyLocalPlayer();
    mUri = paramUri;
    paramUri = new StringBuilder();
    paramUri.append("setUri+mUri=");
    paramUri.append(mUri);
    Log.d("Ringtone-debug", paramUri.toString());
    if (mUri == null) {
      return;
    }
    mLocalPlayer = new MediaPlayer();
    try
    {
      mLocalPlayer.setDataSource(mContext, mUri);
      mLocalPlayer.setAudioAttributes(mAudioAttributes);
      synchronized (mPlaybackSettingsLock)
      {
        applyPlaybackProperties_sync();
        mLocalPlayer.prepare();
      }
      return;
    }
    catch (SecurityException|IOException localSecurityException)
    {
      destroyLocalPlayer();
      if (!mAllowRemote)
      {
        paramUri = new StringBuilder();
        paramUri.append("Remote playback not allowed: ");
        paramUri.append(localSecurityException);
        Log.w("Ringtone", paramUri.toString());
      }
      if (mLocalPlayer != null) {
        Log.d("Ringtone", "Successfully created local player");
      } else {
        Log.d("Ringtone", "Problem opening; delegating to remote player");
      }
    }
  }
  
  public void setVolume(float paramFloat)
  {
    Object localObject1 = mPlaybackSettingsLock;
    float f = paramFloat;
    if (paramFloat < 0.0F) {
      f = 0.0F;
    }
    paramFloat = f;
    if (f > 1.0F) {
      paramFloat = 1.0F;
    }
    try
    {
      mVolume = paramFloat;
      applyPlaybackProperties_sync();
      return;
    }
    finally {}
  }
  
  public void stop()
  {
    if (mLocalPlayer != null) {
      destroyLocalPlayer();
    } else if ((mAllowRemote) && (mRemotePlayer != null)) {
      try
      {
        mRemotePlayer.stop(mRemoteToken);
      }
      catch (RemoteException localRemoteException)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Problem stopping ringtone: ");
        localStringBuilder.append(localRemoteException);
        Log.w("Ringtone", localStringBuilder.toString());
      }
    }
  }
  
  class MyOnCompletionListener
    implements MediaPlayer.OnCompletionListener
  {
    MyOnCompletionListener() {}
    
    public void onCompletion(MediaPlayer paramMediaPlayer)
    {
      synchronized (Ringtone.sActiveRingtones)
      {
        Ringtone.sActiveRingtones.remove(Ringtone.this);
        paramMediaPlayer.setOnCompletionListener(null);
        return;
      }
    }
  }
}
