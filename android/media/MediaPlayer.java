package android.media;

import android.app.ActivityThread;
import android.app.Application;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.PersistableBundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.SystemProperties;
import android.system.ErrnoException;
import android.system.Os;
import android.util.ArrayMap;
import android.util.Log;
import android.util.Pair;
import android.view.Surface;
import android.view.SurfaceHolder;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.util.Preconditions;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.UUID;
import java.util.Vector;

public class MediaPlayer
  extends PlayerBase
  implements SubtitleController.Listener, VolumeAutomation, AudioRouting
{
  public static final boolean APPLY_METADATA_FILTER = true;
  public static final boolean BYPASS_METADATA_FILTER = false;
  private static final String IMEDIA_PLAYER = "android.media.IMediaPlayer";
  private static final int INVOKE_ID_ADD_EXTERNAL_SOURCE = 2;
  private static final int INVOKE_ID_ADD_EXTERNAL_SOURCE_FD = 3;
  private static final int INVOKE_ID_DESELECT_TRACK = 5;
  private static final int INVOKE_ID_GET_SELECTED_TRACK = 7;
  private static final int INVOKE_ID_GET_TRACK_INFO = 1;
  private static final int INVOKE_ID_SELECT_TRACK = 4;
  private static final int INVOKE_ID_SET_VIDEO_SCALE_MODE = 6;
  private static final int KEY_PARAMETER_AUDIO_ATTRIBUTES = 1400;
  private static final int MEDIA_AUDIO_ROUTING_CHANGED = 10000;
  private static final int MEDIA_BUFFERING_UPDATE = 3;
  private static final int MEDIA_DRM_INFO = 210;
  private static final int MEDIA_ERROR = 100;
  public static final int MEDIA_ERROR_IO = -1004;
  public static final int MEDIA_ERROR_MALFORMED = -1007;
  public static final int MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK = 200;
  public static final int MEDIA_ERROR_SERVER_DIED = 100;
  public static final int MEDIA_ERROR_SYSTEM = Integer.MIN_VALUE;
  public static final int MEDIA_ERROR_TIMED_OUT = -110;
  public static final int MEDIA_ERROR_UNKNOWN = 1;
  public static final int MEDIA_ERROR_UNSUPPORTED = -1010;
  private static final int MEDIA_INFO = 200;
  public static final int MEDIA_INFO_AUDIO_NOT_PLAYING = 804;
  public static final int MEDIA_INFO_BAD_INTERLEAVING = 800;
  public static final int MEDIA_INFO_BUFFERING_END = 702;
  public static final int MEDIA_INFO_BUFFERING_START = 701;
  public static final int MEDIA_INFO_EXTERNAL_METADATA_UPDATE = 803;
  public static final int MEDIA_INFO_METADATA_UPDATE = 802;
  public static final int MEDIA_INFO_NETWORK_BANDWIDTH = 703;
  public static final int MEDIA_INFO_NOT_SEEKABLE = 801;
  public static final int MEDIA_INFO_STARTED_AS_NEXT = 2;
  public static final int MEDIA_INFO_SUBTITLE_TIMED_OUT = 902;
  public static final int MEDIA_INFO_TIMED_TEXT_ERROR = 900;
  public static final int MEDIA_INFO_UNKNOWN = 1;
  public static final int MEDIA_INFO_UNSUPPORTED_SUBTITLE = 901;
  public static final int MEDIA_INFO_VIDEO_NOT_PLAYING = 805;
  public static final int MEDIA_INFO_VIDEO_RENDERING_START = 3;
  public static final int MEDIA_INFO_VIDEO_TRACK_LAGGING = 700;
  private static final int MEDIA_META_DATA = 202;
  public static final String MEDIA_MIMETYPE_TEXT_CEA_608 = "text/cea-608";
  public static final String MEDIA_MIMETYPE_TEXT_CEA_708 = "text/cea-708";
  public static final String MEDIA_MIMETYPE_TEXT_SUBRIP = "application/x-subrip";
  public static final String MEDIA_MIMETYPE_TEXT_VTT = "text/vtt";
  private static final int MEDIA_NOP = 0;
  private static final int MEDIA_NOTIFY_TIME = 98;
  private static final int MEDIA_PAUSED = 7;
  private static final int MEDIA_PLAYBACK_COMPLETE = 2;
  private static final int MEDIA_PREPARED = 1;
  private static final int MEDIA_SEEK_COMPLETE = 4;
  private static final int MEDIA_SET_VIDEO_SIZE = 5;
  private static final int MEDIA_SKIPPED = 9;
  private static final int MEDIA_STARTED = 6;
  private static final int MEDIA_STOPPED = 8;
  private static final int MEDIA_SUBTITLE_DATA = 201;
  private static final int MEDIA_TIMED_TEXT = 99;
  private static final int MEDIA_TIME_DISCONTINUITY = 211;
  public static final boolean METADATA_ALL = false;
  public static final boolean METADATA_UPDATE_ONLY = true;
  public static final int PLAYBACK_RATE_AUDIO_MODE_DEFAULT = 0;
  public static final int PLAYBACK_RATE_AUDIO_MODE_RESAMPLE = 2;
  public static final int PLAYBACK_RATE_AUDIO_MODE_STRETCH = 1;
  public static final int PREPARE_DRM_STATUS_PREPARATION_ERROR = 3;
  public static final int PREPARE_DRM_STATUS_PROVISIONING_NETWORK_ERROR = 1;
  public static final int PREPARE_DRM_STATUS_PROVISIONING_SERVER_ERROR = 2;
  public static final int PREPARE_DRM_STATUS_SUCCESS = 0;
  public static final int SEEK_CLOSEST = 3;
  public static final int SEEK_CLOSEST_SYNC = 2;
  public static final int SEEK_NEXT_SYNC = 1;
  public static final int SEEK_PREVIOUS_SYNC = 0;
  private static final String TAG = "MediaPlayer";
  public static final int VIDEO_SCALING_MODE_SCALE_TO_FIT = 1;
  public static final int VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING = 2;
  private boolean mActiveDrmScheme;
  private AudioManager mAudioManager;
  private boolean mBypassInterruptionPolicy;
  private boolean mConfigCameraSoundForced;
  private boolean mDrmConfigAllowed;
  private DrmInfo mDrmInfo;
  private boolean mDrmInfoResolved;
  private final Object mDrmLock = new Object();
  private MediaDrm mDrmObj;
  private boolean mDrmProvisioningInProgress;
  private ProvisioningThread mDrmProvisioningThread;
  private byte[] mDrmSessionId;
  private UUID mDrmUUID;
  private EventHandler mEventHandler;
  private Handler mExtSubtitleDataHandler;
  private OnSubtitleDataListener mExtSubtitleDataListener;
  private BitSet mInbandTrackIndices = new BitSet();
  private Vector<Pair<Integer, SubtitleTrack>> mIndexTrackPairs = new Vector();
  private final OnSubtitleDataListener mIntSubtitleDataListener = new OnSubtitleDataListener()
  {
    public void onSubtitleData(MediaPlayer arg1, SubtitleData paramAnonymousSubtitleData)
    {
      int i = paramAnonymousSubtitleData.getTrackIndex();
      synchronized (mIndexTrackPairs)
      {
        Iterator localIterator = mIndexTrackPairs.iterator();
        while (localIterator.hasNext())
        {
          Pair localPair = (Pair)localIterator.next();
          if ((first != null) && (((Integer)first).intValue() == i) && (second != null)) {
            ((SubtitleTrack)second).onData(paramAnonymousSubtitleData);
          }
        }
        return;
      }
    }
  };
  private int mListenerContext;
  private long mNativeContext;
  private long mNativeSurfaceTexture;
  private OnBufferingUpdateListener mOnBufferingUpdateListener;
  private final OnCompletionListener mOnCompletionInternalListener = new OnCompletionListener()
  {
    public void onCompletion(MediaPlayer paramAnonymousMediaPlayer)
    {
      baseStop();
    }
  };
  private OnCompletionListener mOnCompletionListener;
  private OnDrmConfigHelper mOnDrmConfigHelper;
  private OnDrmInfoHandlerDelegate mOnDrmInfoHandlerDelegate;
  private OnDrmPreparedHandlerDelegate mOnDrmPreparedHandlerDelegate;
  private OnErrorListener mOnErrorListener;
  private OnInfoListener mOnInfoListener;
  private Handler mOnMediaTimeDiscontinuityHandler;
  private OnMediaTimeDiscontinuityListener mOnMediaTimeDiscontinuityListener;
  private OnPreparedListener mOnPreparedListener;
  private OnSeekCompleteListener mOnSeekCompleteListener;
  private OnTimedMetaDataAvailableListener mOnTimedMetaDataAvailableListener;
  private OnTimedTextListener mOnTimedTextListener;
  private OnVideoSizeChangedListener mOnVideoSizeChangedListener;
  private Vector<InputStream> mOpenSubtitleSources;
  private AudioDeviceInfo mPreferredDevice = null;
  private boolean mPrepareDrmInProgress;
  @GuardedBy("mRoutingChangeListeners")
  private ArrayMap<AudioRouting.OnRoutingChangedListener, NativeRoutingEventHandlerDelegate> mRoutingChangeListeners = new ArrayMap();
  private boolean mScreenOnWhilePlaying;
  private int mSelectedSubtitleTrackIndex = -1;
  private boolean mStayAwake;
  private int mStreamType = Integer.MIN_VALUE;
  private SubtitleController mSubtitleController;
  private boolean mSubtitleDataListenerDisabled;
  private SurfaceHolder mSurfaceHolder;
  private TimeProvider mTimeProvider;
  private int mUsage = -1;
  private PowerManager.WakeLock mWakeLock = null;
  
  static
  {
    System.loadLibrary("media_jni");
    native_init();
  }
  
  public MediaPlayer()
  {
    super(new AudioAttributes.Builder().build(), 2);
    Object localObject = Looper.myLooper();
    if (localObject != null)
    {
      mEventHandler = new EventHandler(this, (Looper)localObject);
    }
    else
    {
      localObject = Looper.getMainLooper();
      if (localObject != null) {
        mEventHandler = new EventHandler(this, (Looper)localObject);
      } else {
        mEventHandler = null;
      }
    }
    mTimeProvider = new TimeProvider(this);
    mOpenSubtitleSources = new Vector();
    native_setup(new WeakReference(this));
    baseRegisterPlayer();
    localObject = ActivityThread.currentApplication();
    if ((localObject != null) && (((Context)localObject).getPackageManager() != null) && (((Context)localObject).getPackageManager().hasSystemFeature("asus.software.dnd.unrestricted"))) {
      initAudioManager();
    }
  }
  
  private int HandleProvisioninig(UUID paramUUID)
  {
    if (mDrmProvisioningInProgress)
    {
      Log.e("MediaPlayer", "HandleProvisioninig: Unexpected mDrmProvisioningInProgress");
      return 3;
    }
    MediaDrm.ProvisionRequest localProvisionRequest = mDrmObj.getProvisionRequest();
    if (localProvisionRequest == null)
    {
      Log.e("MediaPlayer", "HandleProvisioninig: getProvisionRequest returned null.");
      return 3;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("HandleProvisioninig provReq  data: ");
    localStringBuilder.append(localProvisionRequest.getData());
    localStringBuilder.append(" url: ");
    localStringBuilder.append(localProvisionRequest.getDefaultUrl());
    Log.v("MediaPlayer", localStringBuilder.toString());
    mDrmProvisioningInProgress = true;
    mDrmProvisioningThread = new ProvisioningThread(null).initialize(localProvisionRequest, paramUUID, this);
    mDrmProvisioningThread.start();
    int i;
    if (mOnDrmPreparedHandlerDelegate != null)
    {
      i = 0;
    }
    else
    {
      try
      {
        mDrmProvisioningThread.join();
      }
      catch (Exception paramUUID)
      {
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("HandleProvisioninig: Thread.join Exception ");
        localStringBuilder.append(paramUUID);
        Log.w("MediaPlayer", localStringBuilder.toString());
      }
      i = mDrmProvisioningThread.status();
      mDrmProvisioningThread = null;
    }
    return i;
  }
  
  private native int _getAudioStreamType()
    throws IllegalStateException;
  
  private native void _notifyAt(long paramLong);
  
  private native void _pause()
    throws IllegalStateException;
  
  private native void _prepare()
    throws IOException, IllegalStateException;
  
  private native void _prepareDrm(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2);
  
  private native void _release();
  
  private native void _releaseDrm();
  
  private native void _reset();
  
  private final native void _seekTo(long paramLong, int paramInt);
  
  private native void _setAudioStreamType(int paramInt);
  
  private native void _setAuxEffectSendLevel(float paramFloat);
  
  private native void _setDataSource(MediaDataSource paramMediaDataSource)
    throws IllegalArgumentException, IllegalStateException;
  
  private native void _setDataSource(FileDescriptor paramFileDescriptor, long paramLong1, long paramLong2)
    throws IOException, IllegalArgumentException, IllegalStateException;
  
  private native void _setVideoSurface(Surface paramSurface);
  
  private native void _setVolume(float paramFloat1, float paramFloat2);
  
  private native void _start()
    throws IllegalStateException;
  
  private native void _stop()
    throws IllegalStateException;
  
  /* Error */
  private boolean attemptDataSource(ContentResolver paramContentResolver, Uri paramUri)
  {
    // Byte code:
    //   0: aload_1
    //   1: aload_2
    //   2: ldc_w 697
    //   5: invokevirtual 703	android/content/ContentResolver:openAssetFileDescriptor	(Landroid/net/Uri;Ljava/lang/String;)Landroid/content/res/AssetFileDescriptor;
    //   8: astore_3
    //   9: aconst_null
    //   10: astore_1
    //   11: aload_0
    //   12: aload_3
    //   13: invokevirtual 707	android/media/MediaPlayer:setDataSource	(Landroid/content/res/AssetFileDescriptor;)V
    //   16: aload_3
    //   17: ifnull +7 -> 24
    //   20: aload_3
    //   21: invokevirtual 712	android/content/res/AssetFileDescriptor:close	()V
    //   24: iconst_1
    //   25: ireturn
    //   26: astore 4
    //   28: goto +11 -> 39
    //   31: astore 4
    //   33: aload 4
    //   35: astore_1
    //   36: aload 4
    //   38: athrow
    //   39: aload_3
    //   40: ifnull +27 -> 67
    //   43: aload_1
    //   44: ifnull +19 -> 63
    //   47: aload_3
    //   48: invokevirtual 712	android/content/res/AssetFileDescriptor:close	()V
    //   51: goto +16 -> 67
    //   54: astore_3
    //   55: aload_1
    //   56: aload_3
    //   57: invokevirtual 716	java/lang/Throwable:addSuppressed	(Ljava/lang/Throwable;)V
    //   60: goto +7 -> 67
    //   63: aload_3
    //   64: invokevirtual 712	android/content/res/AssetFileDescriptor:close	()V
    //   67: aload 4
    //   69: athrow
    //   70: astore_1
    //   71: new 483	java/lang/StringBuilder
    //   74: dup
    //   75: invokespecial 484	java/lang/StringBuilder:<init>	()V
    //   78: astore 4
    //   80: aload 4
    //   82: ldc_w 718
    //   85: invokevirtual 490	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   88: pop
    //   89: aload 4
    //   91: aload_2
    //   92: invokevirtual 499	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   95: pop
    //   96: aload 4
    //   98: ldc_w 720
    //   101: invokevirtual 490	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   104: pop
    //   105: aload 4
    //   107: aload_1
    //   108: invokevirtual 499	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   111: pop
    //   112: ldc -6
    //   114: aload 4
    //   116: invokevirtual 508	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   119: invokestatic 533	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   122: pop
    //   123: iconst_0
    //   124: ireturn
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	125	0	this	MediaPlayer
    //   0	125	1	paramContentResolver	ContentResolver
    //   0	125	2	paramUri	Uri
    //   8	40	3	localAssetFileDescriptor	AssetFileDescriptor
    //   54	10	3	localThrowable1	Throwable
    //   26	1	4	localObject	Object
    //   31	37	4	localThrowable2	Throwable
    //   78	37	4	localStringBuilder	StringBuilder
    // Exception table:
    //   from	to	target	type
    //   11	16	26	finally
    //   36	39	26	finally
    //   11	16	31	java/lang/Throwable
    //   47	51	54	java/lang/Throwable
    //   0	9	70	java/lang/NullPointerException
    //   0	9	70	java/lang/SecurityException
    //   0	9	70	java/io/IOException
    //   20	24	70	java/lang/NullPointerException
    //   20	24	70	java/lang/SecurityException
    //   20	24	70	java/io/IOException
    //   47	51	70	java/lang/NullPointerException
    //   47	51	70	java/lang/SecurityException
    //   47	51	70	java/io/IOException
    //   55	60	70	java/lang/NullPointerException
    //   55	60	70	java/lang/SecurityException
    //   55	60	70	java/io/IOException
    //   63	67	70	java/lang/NullPointerException
    //   63	67	70	java/lang/SecurityException
    //   63	67	70	java/io/IOException
    //   67	70	70	java/lang/NullPointerException
    //   67	70	70	java/lang/SecurityException
    //   67	70	70	java/io/IOException
  }
  
  private static boolean availableMimeTypeForExternalSource(String paramString)
  {
    return "application/x-subrip".equals(paramString);
  }
  
  private void cleanDrmObj()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("cleanDrmObj: mDrmObj=");
    localStringBuilder.append(mDrmObj);
    localStringBuilder.append(" mDrmSessionId=");
    localStringBuilder.append(mDrmSessionId);
    Log.v("MediaPlayer", localStringBuilder.toString());
    if (mDrmSessionId != null)
    {
      mDrmObj.closeSession(mDrmSessionId);
      mDrmSessionId = null;
    }
    if (mDrmObj != null)
    {
      mDrmObj.release();
      mDrmObj = null;
    }
  }
  
  public static MediaPlayer create(Context paramContext, int paramInt)
  {
    int i = AudioSystem.newAudioSessionId();
    if (i <= 0) {
      i = 0;
    }
    return create(paramContext, paramInt, null, i);
  }
  
  public static MediaPlayer create(Context paramContext, int paramInt1, AudioAttributes paramAudioAttributes, int paramInt2)
  {
    try
    {
      paramContext = paramContext.getResources().openRawResourceFd(paramInt1);
      if (paramContext == null) {
        return null;
      }
      MediaPlayer localMediaPlayer = new android/media/MediaPlayer;
      localMediaPlayer.<init>();
      if (paramAudioAttributes == null)
      {
        paramAudioAttributes = new android/media/AudioAttributes$Builder;
        paramAudioAttributes.<init>();
        paramAudioAttributes = paramAudioAttributes.build();
      }
      localMediaPlayer.setAudioAttributes(paramAudioAttributes);
      localMediaPlayer.setAudioSessionId(paramInt2);
      localMediaPlayer.setDataSource(paramContext.getFileDescriptor(), paramContext.getStartOffset(), paramContext.getLength());
      paramContext.close();
      localMediaPlayer.prepare();
      return localMediaPlayer;
    }
    catch (SecurityException paramContext)
    {
      Log.d("MediaPlayer", "create failed:", paramContext);
    }
    catch (IllegalArgumentException paramContext)
    {
      Log.d("MediaPlayer", "create failed:", paramContext);
    }
    catch (IOException paramContext)
    {
      Log.d("MediaPlayer", "create failed:", paramContext);
    }
    return null;
  }
  
  public static MediaPlayer create(Context paramContext, Uri paramUri)
  {
    return create(paramContext, paramUri, null);
  }
  
  public static MediaPlayer create(Context paramContext, Uri paramUri, SurfaceHolder paramSurfaceHolder)
  {
    int i = AudioSystem.newAudioSessionId();
    if (i <= 0) {
      i = 0;
    }
    return create(paramContext, paramUri, paramSurfaceHolder, null, i);
  }
  
  public static MediaPlayer create(Context paramContext, Uri paramUri, SurfaceHolder paramSurfaceHolder, AudioAttributes paramAudioAttributes, int paramInt)
  {
    try
    {
      MediaPlayer localMediaPlayer = new android/media/MediaPlayer;
      localMediaPlayer.<init>();
      if (paramAudioAttributes == null)
      {
        paramAudioAttributes = new android/media/AudioAttributes$Builder;
        paramAudioAttributes.<init>();
        paramAudioAttributes = paramAudioAttributes.build();
      }
      localMediaPlayer.setAudioAttributes(paramAudioAttributes);
      localMediaPlayer.setAudioSessionId(paramInt);
      localMediaPlayer.setDataSource(paramContext, paramUri);
      if (paramSurfaceHolder != null) {
        localMediaPlayer.setDisplay(paramSurfaceHolder);
      }
      localMediaPlayer.prepare();
      return localMediaPlayer;
    }
    catch (SecurityException paramContext)
    {
      Log.d("MediaPlayer", "create failed:", paramContext);
    }
    catch (IllegalArgumentException paramContext)
    {
      Log.d("MediaPlayer", "create failed:", paramContext);
    }
    catch (IOException paramContext)
    {
      Log.d("MediaPlayer", "create failed:", paramContext);
    }
    return null;
  }
  
  @GuardedBy("mRoutingChangeListeners")
  private void enableNativeRoutingCallbacksLocked(boolean paramBoolean)
  {
    if (mRoutingChangeListeners.size() == 0) {
      native_enableDeviceCallback(paramBoolean);
    }
  }
  
  private int getAudioStreamType()
  {
    if (mStreamType == Integer.MIN_VALUE) {
      mStreamType = _getAudioStreamType();
    }
    return mStreamType;
  }
  
  private static final byte[] getByteArrayFromUUID(UUID paramUUID)
  {
    long l1 = paramUUID.getMostSignificantBits();
    long l2 = paramUUID.getLeastSignificantBits();
    paramUUID = new byte[16];
    for (int i = 0; i < 8; i++)
    {
      paramUUID[i] = ((byte)(byte)(int)(l1 >>> (7 - i) * 8));
      paramUUID[(8 + i)] = ((byte)(byte)(int)(l2 >>> 8 * (7 - i)));
    }
    return paramUUID;
  }
  
  private TrackInfo[] getInbandTrackInfo()
    throws IllegalStateException
  {
    Parcel localParcel1 = Parcel.obtain();
    Parcel localParcel2 = Parcel.obtain();
    try
    {
      localParcel1.writeInterfaceToken("android.media.IMediaPlayer");
      localParcel1.writeInt(1);
      invoke(localParcel1, localParcel2);
      TrackInfo[] arrayOfTrackInfo = (TrackInfo[])localParcel2.createTypedArray(TrackInfo.CREATOR);
      return arrayOfTrackInfo;
    }
    finally
    {
      localParcel1.recycle();
      localParcel2.recycle();
    }
  }
  
  private void initAudioManager()
  {
    Application localApplication = ActivityThread.currentApplication();
    mAudioManager = ((AudioManager)localApplication.getSystemService("audio"));
    mConfigCameraSoundForced = localApplication.getResources().getBoolean(17956910);
  }
  
  private boolean isStreamRingMute()
  {
    boolean bool;
    if (mAudioManager != null) {
      bool = mAudioManager.isStreamMute(2);
    } else {
      bool = false;
    }
    return bool;
  }
  
  private boolean isVideoScalingModeSupported(int paramInt)
  {
    boolean bool1 = true;
    boolean bool2 = bool1;
    if (paramInt != 1) {
      if (paramInt == 2) {
        bool2 = bool1;
      } else {
        bool2 = false;
      }
    }
    return bool2;
  }
  
  private native void nativeSetDataSource(IBinder paramIBinder, String paramString, String[] paramArrayOfString1, String[] paramArrayOfString2)
    throws IOException, IllegalArgumentException, SecurityException, IllegalStateException;
  
  private native int native_applyVolumeShaper(VolumeShaper.Configuration paramConfiguration, VolumeShaper.Operation paramOperation);
  
  private final native void native_enableDeviceCallback(boolean paramBoolean);
  
  private final native void native_finalize();
  
  private final native boolean native_getMetadata(boolean paramBoolean1, boolean paramBoolean2, Parcel paramParcel);
  
  private native PersistableBundle native_getMetrics();
  
  private final native int native_getRoutedDeviceId();
  
  private native VolumeShaper.State native_getVolumeShaperState(int paramInt);
  
  private static final native void native_init();
  
  private final native int native_invoke(Parcel paramParcel1, Parcel paramParcel2);
  
  public static native int native_pullBatteryData(Parcel paramParcel);
  
  private final native int native_setMetadataFilter(Parcel paramParcel);
  
  private final native boolean native_setOutputDevice(int paramInt);
  
  private final native int native_setRetransmitEndpoint(String paramString, int paramInt);
  
  private final native void native_setup(Object paramObject);
  
  private void populateInbandTracks()
  {
    TrackInfo[] arrayOfTrackInfo = getInbandTrackInfo();
    Vector localVector = mIndexTrackPairs;
    int i = 0;
    try
    {
      while (i < arrayOfTrackInfo.length)
      {
        if (!mInbandTrackIndices.get(i))
        {
          mInbandTrackIndices.set(i);
          Object localObject2;
          if (arrayOfTrackInfo[i] == null)
          {
            localObject2 = new java/lang/StringBuilder;
            ((StringBuilder)localObject2).<init>();
            ((StringBuilder)localObject2).append("unexpected NULL track at index ");
            ((StringBuilder)localObject2).append(i);
            Log.w("MediaPlayer", ((StringBuilder)localObject2).toString());
          }
          if ((arrayOfTrackInfo[i] != null) && (arrayOfTrackInfo[i].getTrackType() == 4))
          {
            localObject2 = mSubtitleController.addTrack(arrayOfTrackInfo[i].getFormat());
            mIndexTrackPairs.add(Pair.create(Integer.valueOf(i), localObject2));
          }
          else
          {
            mIndexTrackPairs.add(Pair.create(Integer.valueOf(i), null));
          }
        }
        i++;
      }
      return;
    }
    finally {}
  }
  
  private static void postEventFromNative(Object paramObject1, int paramInt1, int paramInt2, int paramInt3, Object paramObject2)
  {
    paramObject1 = (MediaPlayer)((WeakReference)paramObject1).get();
    if (paramObject1 == null) {
      return;
    }
    if (paramInt1 != 1) {
      if (paramInt1 != 200)
      {
        if (paramInt1 == 210)
        {
          Log.v("MediaPlayer", "postEventFromNative MEDIA_DRM_INFO");
          if ((paramObject2 instanceof Parcel))
          {
            DrmInfo localDrmInfo = new DrmInfo((Parcel)paramObject2, null);
            synchronized (mDrmLock)
            {
              mDrmInfo = localDrmInfo;
            }
          }
          ??? = new StringBuilder();
          ((StringBuilder)???).append("MEDIA_DRM_INFO msg.obj of unexpected type ");
          ((StringBuilder)???).append(paramObject2);
          Log.w("MediaPlayer", ((StringBuilder)???).toString());
        }
      }
      else if (paramInt2 == 2)
      {
        new Thread(new Runnable()
        {
          public void run()
          {
            start();
          }
        }).start();
        Thread.yield();
      }
    }
    synchronized (mDrmLock)
    {
      mDrmInfoResolved = true;
      if (mEventHandler != null)
      {
        paramObject2 = mEventHandler.obtainMessage(paramInt1, paramInt2, paramInt3, paramObject2);
        mEventHandler.sendMessage(paramObject2);
      }
      return;
    }
  }
  
  private void prepareDrm_createDrmStep(UUID paramUUID)
    throws UnsupportedSchemeException
  {
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("prepareDrm_createDrmStep: UUID: ");
    ((StringBuilder)localObject).append(paramUUID);
    Log.v("MediaPlayer", ((StringBuilder)localObject).toString());
    try
    {
      localObject = new android/media/MediaDrm;
      ((MediaDrm)localObject).<init>(paramUUID);
      mDrmObj = ((MediaDrm)localObject);
      paramUUID = new java/lang/StringBuilder;
      paramUUID.<init>();
      paramUUID.append("prepareDrm_createDrmStep: Created mDrmObj=");
      paramUUID.append(mDrmObj);
      Log.v("MediaPlayer", paramUUID.toString());
      return;
    }
    catch (Exception localException)
    {
      paramUUID = new StringBuilder();
      paramUUID.append("prepareDrm_createDrmStep: MediaDrm failed with ");
      paramUUID.append(localException);
      Log.e("MediaPlayer", paramUUID.toString());
      throw localException;
    }
  }
  
  private void prepareDrm_openSessionStep(UUID paramUUID)
    throws NotProvisionedException, ResourceBusyException
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("prepareDrm_openSessionStep: uuid: ");
    localStringBuilder.append(paramUUID);
    Log.v("MediaPlayer", localStringBuilder.toString());
    try
    {
      mDrmSessionId = mDrmObj.openSession();
      localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append("prepareDrm_openSessionStep: mDrmSessionId=");
      localStringBuilder.append(mDrmSessionId);
      Log.v("MediaPlayer", localStringBuilder.toString());
      _prepareDrm(getByteArrayFromUUID(paramUUID), mDrmSessionId);
      Log.v("MediaPlayer", "prepareDrm_openSessionStep: _prepareDrm/Crypto succeeded");
      return;
    }
    catch (Exception paramUUID)
    {
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("prepareDrm_openSessionStep: open/crypto failed with ");
      localStringBuilder.append(paramUUID);
      Log.e("MediaPlayer", localStringBuilder.toString());
      throw paramUUID;
    }
  }
  
  private boolean readCameraSoundForced()
  {
    boolean bool = false;
    if ((!SystemProperties.getBoolean("audio.camerasound.force", false)) && (!SystemProperties.get("ro.config.versatility", "WW").equals("JP")) && (!mConfigCameraSoundForced)) {
      return bool;
    }
    bool = true;
    return bool;
  }
  
  private void resetDrmState()
  {
    synchronized (mDrmLock)
    {
      Object localObject2 = new java/lang/StringBuilder;
      ((StringBuilder)localObject2).<init>();
      ((StringBuilder)localObject2).append("resetDrmState:  mDrmInfo=");
      ((StringBuilder)localObject2).append(mDrmInfo);
      ((StringBuilder)localObject2).append(" mDrmProvisioningThread=");
      ((StringBuilder)localObject2).append(mDrmProvisioningThread);
      ((StringBuilder)localObject2).append(" mPrepareDrmInProgress=");
      ((StringBuilder)localObject2).append(mPrepareDrmInProgress);
      ((StringBuilder)localObject2).append(" mActiveDrmScheme=");
      ((StringBuilder)localObject2).append(mActiveDrmScheme);
      Log.v("MediaPlayer", ((StringBuilder)localObject2).toString());
      mDrmInfoResolved = false;
      mDrmInfo = null;
      localObject2 = mDrmProvisioningThread;
      if (localObject2 != null)
      {
        try
        {
          mDrmProvisioningThread.join();
        }
        catch (InterruptedException localInterruptedException)
        {
          StringBuilder localStringBuilder = new java/lang/StringBuilder;
          localStringBuilder.<init>();
          localStringBuilder.append("resetDrmState: ProvThread.join Exception ");
          localStringBuilder.append(localInterruptedException);
          Log.w("MediaPlayer", localStringBuilder.toString());
        }
        mDrmProvisioningThread = null;
      }
      mPrepareDrmInProgress = false;
      mActiveDrmScheme = false;
      cleanDrmObj();
      return;
    }
  }
  
  private boolean resumePrepareDrm(UUID paramUUID)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("resumePrepareDrm: uuid: ");
    localStringBuilder.append(paramUUID);
    Log.v("MediaPlayer", localStringBuilder.toString());
    boolean bool = false;
    try
    {
      prepareDrm_openSessionStep(paramUUID);
      mDrmUUID = paramUUID;
      mActiveDrmScheme = true;
      bool = true;
    }
    catch (Exception paramUUID)
    {
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("HandleProvisioninig: Thread run _prepareDrm resume failed with ");
      localStringBuilder.append(paramUUID);
      Log.w("MediaPlayer", localStringBuilder.toString());
    }
    return bool;
  }
  
  private void scanInternalSubtitleTracks()
  {
    setSubtitleAnchor();
    populateInbandTracks();
    if (mSubtitleController != null) {
      mSubtitleController.selectDefaultTrack();
    }
  }
  
  private void selectOrDeselectInbandTrack(int paramInt, boolean paramBoolean)
    throws IllegalStateException
  {
    Parcel localParcel1 = Parcel.obtain();
    Parcel localParcel2 = Parcel.obtain();
    try
    {
      localParcel1.writeInterfaceToken("android.media.IMediaPlayer");
      int i;
      if (paramBoolean) {
        i = 4;
      } else {
        i = 5;
      }
      localParcel1.writeInt(i);
      localParcel1.writeInt(paramInt);
      invoke(localParcel1, localParcel2);
      return;
    }
    finally
    {
      localParcel1.recycle();
      localParcel2.recycle();
    }
  }
  
  private void selectOrDeselectTrack(int paramInt, boolean paramBoolean)
    throws IllegalStateException
  {
    populateInbandTracks();
    try
    {
      Object localObject = (Pair)mIndexTrackPairs.get(paramInt);
      SubtitleTrack localSubtitleTrack1 = (SubtitleTrack)second;
      if (localSubtitleTrack1 == null)
      {
        selectOrDeselectInbandTrack(((Integer)first).intValue(), paramBoolean);
        return;
      }
      if (mSubtitleController == null) {
        return;
      }
      if (!paramBoolean)
      {
        if (mSubtitleController.getSelectedTrack() == localSubtitleTrack1) {
          mSubtitleController.selectTrack(null);
        } else {
          Log.w("MediaPlayer", "trying to deselect track that was not selected");
        }
        return;
      }
      if (localSubtitleTrack1.getTrackType() == 3)
      {
        paramInt = getSelectedTrack(3);
        localObject = mIndexTrackPairs;
        if (paramInt >= 0) {
          try
          {
            if (paramInt < mIndexTrackPairs.size())
            {
              Pair localPair = (Pair)mIndexTrackPairs.get(paramInt);
              if ((first != null) && (second == null)) {
                selectOrDeselectInbandTrack(((Integer)first).intValue(), false);
              }
            }
          }
          finally
          {
            break label187;
          }
        }
        break label192;
        label187:
        throw localSubtitleTrack2;
      }
      label192:
      mSubtitleController.selectTrack(localSubtitleTrack2);
      return;
    }
    catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException) {}
  }
  
  private void setDataSource(String paramString, Map<String, String> paramMap, List<HttpCookie> paramList)
    throws IOException, IllegalArgumentException, SecurityException, IllegalStateException
  {
    Object localObject1 = null;
    Object localObject2 = null;
    if (paramMap != null)
    {
      String[] arrayOfString1 = new String[paramMap.size()];
      String[] arrayOfString2 = new String[paramMap.size()];
      int i = 0;
      paramMap = paramMap.entrySet().iterator();
      for (;;)
      {
        localObject1 = arrayOfString1;
        localObject2 = arrayOfString2;
        if (!paramMap.hasNext()) {
          break;
        }
        localObject1 = (Map.Entry)paramMap.next();
        arrayOfString1[i] = ((String)((Map.Entry)localObject1).getKey());
        arrayOfString2[i] = ((String)((Map.Entry)localObject1).getValue());
        i++;
      }
    }
    setDataSource(paramString, (String[])localObject1, localObject2, paramList);
  }
  
  private void setDataSource(String paramString, String[] paramArrayOfString1, String[] paramArrayOfString2, List<HttpCookie> paramList)
    throws IOException, IllegalArgumentException, SecurityException, IllegalStateException
  {
    Object localObject = Uri.parse(paramString);
    String str = ((Uri)localObject).getScheme();
    if ("file".equals(str))
    {
      localObject = ((Uri)localObject).getPath();
    }
    else
    {
      localObject = paramString;
      if (str != null)
      {
        nativeSetDataSource(MediaHTTPService.createHttpServiceBinderIfNecessary(paramString, paramList), paramString, paramArrayOfString1, paramArrayOfString2);
        return;
      }
    }
    paramString = new File((String)localObject);
    if (paramString.exists())
    {
      paramString = new FileInputStream(paramString);
      setDataSource(paramString.getFD());
      paramString.close();
      return;
    }
    throw new IOException("setDataSource failed.");
  }
  
  private void setOnMediaTimeDiscontinuityListenerInt(OnMediaTimeDiscontinuityListener paramOnMediaTimeDiscontinuityListener, Handler paramHandler)
  {
    try
    {
      mOnMediaTimeDiscontinuityListener = paramOnMediaTimeDiscontinuityListener;
      mOnMediaTimeDiscontinuityHandler = paramHandler;
      return;
    }
    finally {}
  }
  
  private void setOnSubtitleDataListenerInt(OnSubtitleDataListener paramOnSubtitleDataListener, Handler paramHandler)
  {
    try
    {
      mExtSubtitleDataListener = paramOnSubtitleDataListener;
      mExtSubtitleDataHandler = paramHandler;
      return;
    }
    finally {}
  }
  
  private native boolean setParameter(int paramInt, Parcel paramParcel);
  
  private void setSubtitleAnchor()
  {
    try
    {
      if ((mSubtitleController == null) && (ActivityThread.currentApplication() != null))
      {
        HandlerThread localHandlerThread = new android/os/HandlerThread;
        localHandlerThread.<init>("SetSubtitleAnchorThread");
        localHandlerThread.start();
        Handler localHandler = new android/os/Handler;
        localHandler.<init>(localHandlerThread.getLooper());
        Runnable local2 = new android/media/MediaPlayer$2;
        local2.<init>(this, localHandlerThread);
        localHandler.post(local2);
        try
        {
          localHandlerThread.join();
        }
        catch (InterruptedException localInterruptedException)
        {
          Thread.currentThread().interrupt();
          Log.w("MediaPlayer", "failed to join SetSubtitleAnchorThread");
        }
      }
      return;
    }
    finally {}
  }
  
  private void startImpl()
  {
    if (getAudioStreamType() == 3) {
      executeAIVolume("MediaPlayer");
    }
    baseStart();
    stayAwake(true);
    _start();
  }
  
  private void stayAwake(boolean paramBoolean)
  {
    if (mWakeLock != null) {
      if ((paramBoolean) && (!mWakeLock.isHeld())) {
        mWakeLock.acquire();
      } else if ((!paramBoolean) && (mWakeLock.isHeld())) {
        mWakeLock.release();
      }
    }
    mStayAwake = paramBoolean;
    updateSurfaceScreenOn();
  }
  
  private void updateSurfaceScreenOn()
  {
    if (mSurfaceHolder != null)
    {
      SurfaceHolder localSurfaceHolder = mSurfaceHolder;
      boolean bool;
      if ((mScreenOnWhilePlaying) && (mStayAwake)) {
        bool = true;
      } else {
        bool = false;
      }
      localSurfaceHolder.setKeepScreenOn(bool);
    }
  }
  
  public void addOnRoutingChangedListener(AudioRouting.OnRoutingChangedListener paramOnRoutingChangedListener, Handler paramHandler)
  {
    ArrayMap localArrayMap1 = mRoutingChangeListeners;
    if (paramOnRoutingChangedListener != null) {
      try
      {
        if (!mRoutingChangeListeners.containsKey(paramOnRoutingChangedListener))
        {
          enableNativeRoutingCallbacksLocked(true);
          ArrayMap localArrayMap2 = mRoutingChangeListeners;
          NativeRoutingEventHandlerDelegate localNativeRoutingEventHandlerDelegate = new android/media/NativeRoutingEventHandlerDelegate;
          if (paramHandler == null) {
            paramHandler = mEventHandler;
          }
          localNativeRoutingEventHandlerDelegate.<init>(this, paramOnRoutingChangedListener, paramHandler);
          localArrayMap2.put(paramOnRoutingChangedListener, localNativeRoutingEventHandlerDelegate);
        }
      }
      finally
      {
        break label77;
      }
    }
    return;
    label77:
    throw paramOnRoutingChangedListener;
  }
  
  public void addSubtitleSource(final InputStream paramInputStream, final MediaFormat paramMediaFormat)
    throws IllegalStateException
  {
    if (paramInputStream != null) {
      synchronized (mOpenSubtitleSources)
      {
        mOpenSubtitleSources.add(paramInputStream);
      }
    }
    Log.w("MediaPlayer", "addSubtitleSource called with null InputStream");
    getMediaTimeProvider();
    ??? = new HandlerThread("SubtitleReadThread", 9);
    ((HandlerThread)???).start();
    new Handler(((HandlerThread)???).getLooper()).post(new Runnable()
    {
      private int addTrack()
      {
        if ((paramInputStream != null) && (mSubtitleController != null))
        {
          SubtitleTrack localSubtitleTrack = mSubtitleController.addTrack(paramMediaFormat);
          if (localSubtitleTrack == null) {
            return 901;
          }
          Scanner localScanner = new Scanner(paramInputStream, "UTF-8");
          String str = localScanner.useDelimiter("\\A").next();
          synchronized (mOpenSubtitleSources)
          {
            mOpenSubtitleSources.remove(paramInputStream);
            localScanner.close();
            synchronized (mIndexTrackPairs)
            {
              mIndexTrackPairs.add(Pair.create(null, localSubtitleTrack));
              ??? = MediaPlayer.TimeProvider.access$500(mTimeProvider);
              ((Handler)???).sendMessage(((Handler)???).obtainMessage(1, 4, 0, Pair.create(localSubtitleTrack, str.getBytes())));
              return 803;
            }
          }
        }
        return 901;
      }
      
      public void run()
      {
        int i = addTrack();
        if (mEventHandler != null)
        {
          Message localMessage = mEventHandler.obtainMessage(200, i, 0, null);
          mEventHandler.sendMessage(localMessage);
        }
        val$thread.getLooper().quitSafely();
      }
    });
  }
  
  /* Error */
  public void addTimedTextSource(Context paramContext, Uri paramUri, String paramString)
    throws IOException, IllegalArgumentException, IllegalStateException
  {
    // Byte code:
    //   0: aload_2
    //   1: invokevirtual 1136	android/net/Uri:getScheme	()Ljava/lang/String;
    //   4: astore 4
    //   6: aload 4
    //   8: ifnull +119 -> 127
    //   11: aload 4
    //   13: ldc_w 1138
    //   16: invokevirtual 727	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   19: ifeq +6 -> 25
    //   22: goto +105 -> 127
    //   25: aconst_null
    //   26: astore 4
    //   28: aconst_null
    //   29: astore 5
    //   31: aconst_null
    //   32: astore 6
    //   34: aload_1
    //   35: invokevirtual 1280	android/content/Context:getContentResolver	()Landroid/content/ContentResolver;
    //   38: aload_2
    //   39: ldc_w 697
    //   42: invokevirtual 703	android/content/ContentResolver:openAssetFileDescriptor	(Landroid/net/Uri;Ljava/lang/String;)Landroid/content/res/AssetFileDescriptor;
    //   45: astore_1
    //   46: aload_1
    //   47: ifnonnull +12 -> 59
    //   50: aload_1
    //   51: ifnull +7 -> 58
    //   54: aload_1
    //   55: invokevirtual 712	android/content/res/AssetFileDescriptor:close	()V
    //   58: return
    //   59: aload_1
    //   60: astore 6
    //   62: aload_1
    //   63: astore 4
    //   65: aload_1
    //   66: astore 5
    //   68: aload_0
    //   69: aload_1
    //   70: invokevirtual 772	android/content/res/AssetFileDescriptor:getFileDescriptor	()Ljava/io/FileDescriptor;
    //   73: aload_3
    //   74: invokevirtual 1283	android/media/MediaPlayer:addTimedTextSource	(Ljava/io/FileDescriptor;Ljava/lang/String;)V
    //   77: aload_1
    //   78: ifnull +7 -> 85
    //   81: aload_1
    //   82: invokevirtual 712	android/content/res/AssetFileDescriptor:close	()V
    //   85: return
    //   86: astore_1
    //   87: aload 6
    //   89: ifnull +8 -> 97
    //   92: aload 6
    //   94: invokevirtual 712	android/content/res/AssetFileDescriptor:close	()V
    //   97: aload_1
    //   98: athrow
    //   99: astore_1
    //   100: aload 4
    //   102: ifnull +24 -> 126
    //   105: aload 4
    //   107: invokevirtual 712	android/content/res/AssetFileDescriptor:close	()V
    //   110: goto +16 -> 126
    //   113: astore_1
    //   114: aload 5
    //   116: ifnull +10 -> 126
    //   119: aload 5
    //   121: astore 4
    //   123: goto -18 -> 105
    //   126: return
    //   127: aload_0
    //   128: aload_2
    //   129: invokevirtual 1141	android/net/Uri:getPath	()Ljava/lang/String;
    //   132: aload_3
    //   133: invokevirtual 1286	android/media/MediaPlayer:addTimedTextSource	(Ljava/lang/String;Ljava/lang/String;)V
    //   136: return
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	137	0	this	MediaPlayer
    //   0	137	1	paramContext	Context
    //   0	137	2	paramUri	Uri
    //   0	137	3	paramString	String
    //   4	118	4	localObject	Object
    //   29	91	5	localContext1	Context
    //   32	61	6	localContext2	Context
    // Exception table:
    //   from	to	target	type
    //   34	46	86	finally
    //   68	77	86	finally
    //   34	46	99	java/io/IOException
    //   68	77	99	java/io/IOException
    //   34	46	113	java/lang/SecurityException
    //   68	77	113	java/lang/SecurityException
  }
  
  public void addTimedTextSource(final FileDescriptor paramFileDescriptor, final long paramLong1, long paramLong2, String arg6)
    throws IllegalArgumentException, IllegalStateException
  {
    if (availableMimeTypeForExternalSource(???)) {
      try
      {
        paramFileDescriptor = Os.dup(paramFileDescriptor);
        Object localObject = new MediaFormat();
        ((MediaFormat)localObject).setString("mime", ???);
        ((MediaFormat)localObject).setInteger("is-timed-text", 1);
        if (mSubtitleController == null) {
          setSubtitleAnchor();
        }
        if (!mSubtitleController.hasRendererFor((MediaFormat)localObject))
        {
          ??? = ActivityThread.currentApplication();
          mSubtitleController.registerRenderer(new SRTRenderer(???, mEventHandler));
        }
        localObject = mSubtitleController.addTrack((MediaFormat)localObject);
        synchronized (mIndexTrackPairs)
        {
          mIndexTrackPairs.add(Pair.create(null, localObject));
          getMediaTimeProvider();
          ??? = new HandlerThread("TimedTextReadThread", 9);
          ???.start();
          new Handler(???.getLooper()).post(new Runnable()
          {
            /* Error */
            private int addTrack()
            {
              // Byte code:
              //   0: new 48	java/io/ByteArrayOutputStream
              //   3: dup
              //   4: invokespecial 49	java/io/ByteArrayOutputStream:<init>	()V
              //   7: astore_1
              //   8: aload_0
              //   9: getfield 28	android/media/MediaPlayer$5:val$dupedFd	Ljava/io/FileDescriptor;
              //   12: aload_0
              //   13: getfield 30	android/media/MediaPlayer$5:val$offset2	J
              //   16: getstatic 55	android/system/OsConstants:SEEK_SET	I
              //   19: invokestatic 61	android/system/Os:lseek	(Ljava/io/FileDescriptor;JI)J
              //   22: pop2
              //   23: sipush 4096
              //   26: newarray byte
              //   28: astore_2
              //   29: lconst_0
              //   30: lstore_3
              //   31: lload_3
              //   32: aload_0
              //   33: getfield 32	android/media/MediaPlayer$5:val$length2	J
              //   36: lcmp
              //   37: ifge +56 -> 93
              //   40: aload_2
              //   41: arraylength
              //   42: i2l
              //   43: aload_0
              //   44: getfield 32	android/media/MediaPlayer$5:val$length2	J
              //   47: lload_3
              //   48: lsub
              //   49: invokestatic 67	java/lang/Math:min	(JJ)J
              //   52: l2i
              //   53: istore 5
              //   55: aload_0
              //   56: getfield 28	android/media/MediaPlayer$5:val$dupedFd	Ljava/io/FileDescriptor;
              //   59: aload_2
              //   60: iconst_0
              //   61: iload 5
              //   63: invokestatic 73	libcore/io/IoBridge:read	(Ljava/io/FileDescriptor;[BII)I
              //   66: istore 5
              //   68: iload 5
              //   70: ifge +6 -> 76
              //   73: goto +20 -> 93
              //   76: aload_1
              //   77: aload_2
              //   78: iconst_0
              //   79: iload 5
              //   81: invokevirtual 77	java/io/ByteArrayOutputStream:write	([BII)V
              //   84: lload_3
              //   85: iload 5
              //   87: i2l
              //   88: ladd
              //   89: lstore_3
              //   90: goto -59 -> 31
              //   93: aload_0
              //   94: getfield 26	android/media/MediaPlayer$5:this$0	Landroid/media/MediaPlayer;
              //   97: invokestatic 81	android/media/MediaPlayer:access$200	(Landroid/media/MediaPlayer;)Landroid/media/MediaPlayer$TimeProvider;
              //   100: invokestatic 87	android/media/MediaPlayer$TimeProvider:access$500	(Landroid/media/MediaPlayer$TimeProvider;)Landroid/os/Handler;
              //   103: astore_2
              //   104: aload_2
              //   105: aload_2
              //   106: iconst_1
              //   107: iconst_4
              //   108: iconst_0
              //   109: aload_0
              //   110: getfield 34	android/media/MediaPlayer$5:val$track	Landroid/media/SubtitleTrack;
              //   113: aload_1
              //   114: invokevirtual 91	java/io/ByteArrayOutputStream:toByteArray	()[B
              //   117: invokestatic 97	android/util/Pair:create	(Ljava/lang/Object;Ljava/lang/Object;)Landroid/util/Pair;
              //   120: invokevirtual 103	android/os/Handler:obtainMessage	(IIILjava/lang/Object;)Landroid/os/Message;
              //   123: invokevirtual 107	android/os/Handler:sendMessage	(Landroid/os/Message;)Z
              //   126: pop
              //   127: aload_0
              //   128: getfield 28	android/media/MediaPlayer$5:val$dupedFd	Ljava/io/FileDescriptor;
              //   131: invokestatic 111	android/system/Os:close	(Ljava/io/FileDescriptor;)V
              //   134: goto +15 -> 149
              //   137: astore_1
              //   138: ldc 113
              //   140: aload_1
              //   141: invokevirtual 117	android/system/ErrnoException:getMessage	()Ljava/lang/String;
              //   144: aload_1
              //   145: invokestatic 123	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
              //   148: pop
              //   149: sipush 803
              //   152: ireturn
              //   153: astore_1
              //   154: goto +41 -> 195
              //   157: astore_1
              //   158: ldc 113
              //   160: aload_1
              //   161: invokevirtual 124	java/lang/Exception:getMessage	()Ljava/lang/String;
              //   164: aload_1
              //   165: invokestatic 123	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
              //   168: pop
              //   169: aload_0
              //   170: getfield 28	android/media/MediaPlayer$5:val$dupedFd	Ljava/io/FileDescriptor;
              //   173: invokestatic 111	android/system/Os:close	(Ljava/io/FileDescriptor;)V
              //   176: goto +15 -> 191
              //   179: astore_1
              //   180: ldc 113
              //   182: aload_1
              //   183: invokevirtual 117	android/system/ErrnoException:getMessage	()Ljava/lang/String;
              //   186: aload_1
              //   187: invokestatic 123	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
              //   190: pop
              //   191: sipush 900
              //   194: ireturn
              //   195: aload_0
              //   196: getfield 28	android/media/MediaPlayer$5:val$dupedFd	Ljava/io/FileDescriptor;
              //   199: invokestatic 111	android/system/Os:close	(Ljava/io/FileDescriptor;)V
              //   202: goto +15 -> 217
              //   205: astore_2
              //   206: ldc 113
              //   208: aload_2
              //   209: invokevirtual 117	android/system/ErrnoException:getMessage	()Ljava/lang/String;
              //   212: aload_2
              //   213: invokestatic 123	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
              //   216: pop
              //   217: aload_1
              //   218: athrow
              // Local variable table:
              //   start	length	slot	name	signature
              //   0	219	0	this	5
              //   7	107	1	localByteArrayOutputStream	java.io.ByteArrayOutputStream
              //   137	8	1	localErrnoException1	ErrnoException
              //   153	1	1	localObject1	Object
              //   157	8	1	localException	Exception
              //   179	39	1	localErrnoException2	ErrnoException
              //   28	78	2	localObject2	Object
              //   205	8	2	localErrnoException3	ErrnoException
              //   30	60	3	l	long
              //   53	33	5	i	int
              // Exception table:
              //   from	to	target	type
              //   127	134	137	android/system/ErrnoException
              //   8	29	153	finally
              //   31	68	153	finally
              //   76	84	153	finally
              //   93	127	153	finally
              //   158	169	153	finally
              //   8	29	157	java/lang/Exception
              //   31	68	157	java/lang/Exception
              //   76	84	157	java/lang/Exception
              //   93	127	157	java/lang/Exception
              //   169	176	179	android/system/ErrnoException
              //   195	202	205	android/system/ErrnoException
            }
            
            public void run()
            {
              int i = addTrack();
              if (mEventHandler != null)
              {
                Message localMessage = mEventHandler.obtainMessage(200, i, 0, null);
                mEventHandler.sendMessage(localMessage);
              }
              val$thread.getLooper().quitSafely();
            }
          });
          return;
        }
        paramFileDescriptor = new StringBuilder();
      }
      catch (ErrnoException paramFileDescriptor)
      {
        Log.e("MediaPlayer", paramFileDescriptor.getMessage(), paramFileDescriptor);
        throw new RuntimeException(paramFileDescriptor);
      }
    }
    paramFileDescriptor.append("Illegal mimeType for timed text source: ");
    paramFileDescriptor.append(???);
    throw new IllegalArgumentException(paramFileDescriptor.toString());
  }
  
  public void addTimedTextSource(FileDescriptor paramFileDescriptor, String paramString)
    throws IllegalArgumentException, IllegalStateException
  {
    addTimedTextSource(paramFileDescriptor, 0L, 576460752303423487L, paramString);
  }
  
  public void addTimedTextSource(String paramString1, String paramString2)
    throws IOException, IllegalArgumentException, IllegalStateException
  {
    if (availableMimeTypeForExternalSource(paramString2))
    {
      File localFile = new File(paramString1);
      if (localFile.exists())
      {
        paramString1 = new FileInputStream(localFile);
        addTimedTextSource(paramString1.getFD(), paramString2);
        paramString1.close();
        return;
      }
      throw new IOException(paramString1);
    }
    paramString1 = new StringBuilder();
    paramString1.append("Illegal mimeType for timed text source: ");
    paramString1.append(paramString2);
    throw new IllegalArgumentException(paramString1.toString());
  }
  
  public native void attachAuxEffect(int paramInt);
  
  public void clearOnMediaTimeDiscontinuityListener()
  {
    setOnMediaTimeDiscontinuityListenerInt(null, null);
  }
  
  public void clearOnSubtitleDataListener()
  {
    setOnSubtitleDataListenerInt(null, null);
  }
  
  public VolumeShaper createVolumeShaper(VolumeShaper.Configuration paramConfiguration)
  {
    return new VolumeShaper(paramConfiguration, this);
  }
  
  public void deselectTrack(int paramInt)
    throws IllegalStateException
  {
    selectOrDeselectTrack(paramInt, false);
  }
  
  public PlaybackParams easyPlaybackParams(float paramFloat, int paramInt)
  {
    Object localObject = new PlaybackParams();
    ((PlaybackParams)localObject).allowDefaults();
    switch (paramInt)
    {
    default: 
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Audio playback mode ");
      ((StringBuilder)localObject).append(paramInt);
      ((StringBuilder)localObject).append(" is not supported");
      throw new IllegalArgumentException(((StringBuilder)localObject).toString());
    case 2: 
      ((PlaybackParams)localObject).setSpeed(paramFloat).setPitch(paramFloat);
      break;
    case 1: 
      ((PlaybackParams)localObject).setSpeed(paramFloat).setPitch(1.0F).setAudioFallbackMode(2);
      break;
    case 0: 
      ((PlaybackParams)localObject).setSpeed(paramFloat).setPitch(1.0F);
    }
    return localObject;
  }
  
  protected void finalize()
  {
    baseRelease();
    native_finalize();
  }
  
  public native int getAudioSessionId();
  
  public native BufferingParams getBufferingParams();
  
  public native int getCurrentPosition();
  
  public DrmInfo getDrmInfo()
  {
    Object localObject1 = null;
    synchronized (mDrmLock)
    {
      if ((!mDrmInfoResolved) && (mDrmInfo == null))
      {
        Log.v("MediaPlayer", "The Player has not been prepared yet");
        localObject1 = new java/lang/IllegalStateException;
        ((IllegalStateException)localObject1).<init>("The Player has not been prepared yet");
        throw ((Throwable)localObject1);
      }
      if (mDrmInfo != null) {
        localObject1 = mDrmInfo.makeCopy();
      }
      return localObject1;
    }
  }
  
  public String getDrmPropertyString(String paramString)
    throws MediaPlayer.NoDrmSchemeException
  {
    ??? = new StringBuilder();
    ((StringBuilder)???).append("getDrmPropertyString: propertyName: ");
    ((StringBuilder)???).append(paramString);
    Log.v("MediaPlayer", ((StringBuilder)???).toString());
    synchronized (mDrmLock)
    {
      if ((!mActiveDrmScheme) && (!mDrmConfigAllowed))
      {
        Log.w("MediaPlayer", "getDrmPropertyString NoDrmSchemeException");
        paramString = new android/media/MediaPlayer$NoDrmSchemeException;
        paramString.<init>("getDrmPropertyString: Has to prepareDrm() first.");
        throw paramString;
      }
      try
      {
        localObject2 = mDrmObj.getPropertyString(paramString);
        ??? = new StringBuilder();
        ((StringBuilder)???).append("getDrmPropertyString: propertyName: ");
        ((StringBuilder)???).append(paramString);
        ((StringBuilder)???).append(" --> value: ");
        ((StringBuilder)???).append((String)localObject2);
        Log.v("MediaPlayer", ((StringBuilder)???).toString());
        return localObject2;
      }
      catch (Exception paramString)
      {
        Object localObject2 = new java/lang/StringBuilder;
        ((StringBuilder)localObject2).<init>();
        ((StringBuilder)localObject2).append("getDrmPropertyString Exception ");
        ((StringBuilder)localObject2).append(paramString);
        Log.w("MediaPlayer", ((StringBuilder)localObject2).toString());
        throw paramString;
      }
    }
  }
  
  public native int getDuration();
  
  public MediaDrm.KeyRequest getKeyRequest(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, String paramString, int paramInt, Map<String, String> paramMap)
    throws MediaPlayer.NoDrmSchemeException
  {
    Object localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append("getKeyRequest:  keySetId: ");
    ((StringBuilder)localObject1).append(paramArrayOfByte1);
    ((StringBuilder)localObject1).append(" initData:");
    ((StringBuilder)localObject1).append(paramArrayOfByte2);
    ((StringBuilder)localObject1).append(" mimeType: ");
    ((StringBuilder)localObject1).append(paramString);
    ((StringBuilder)localObject1).append(" keyType: ");
    ((StringBuilder)localObject1).append(paramInt);
    ((StringBuilder)localObject1).append(" optionalParameters: ");
    ((StringBuilder)localObject1).append(paramMap);
    Log.v("MediaPlayer", ((StringBuilder)localObject1).toString());
    synchronized (mDrmLock)
    {
      boolean bool = mActiveDrmScheme;
      if (bool)
      {
        if (paramInt != 3) {
          try
          {
            paramArrayOfByte1 = mDrmSessionId;
          }
          catch (Exception paramArrayOfByte2)
          {
            break label223;
          }
          catch (NotProvisionedException paramArrayOfByte1)
          {
            break label257;
          }
        }
        if (paramMap != null)
        {
          localObject1 = new java/util/HashMap;
          ((HashMap)localObject1).<init>(paramMap);
          paramMap = (Map<String, String>)localObject1;
        }
        else
        {
          paramMap = null;
        }
        paramArrayOfByte1 = mDrmObj.getKeyRequest(paramArrayOfByte1, paramArrayOfByte2, paramString, paramInt, paramMap);
        paramArrayOfByte2 = new java/lang/StringBuilder;
        paramArrayOfByte2.<init>();
        paramArrayOfByte2.append("getKeyRequest:   --> request: ");
        paramArrayOfByte2.append(paramArrayOfByte1);
        Log.v("MediaPlayer", paramArrayOfByte2.toString());
        return paramArrayOfByte1;
        label223:
        paramArrayOfByte1 = new java/lang/StringBuilder;
        paramArrayOfByte1.<init>();
        paramArrayOfByte1.append("getKeyRequest Exception ");
        paramArrayOfByte1.append(paramArrayOfByte2);
        Log.w("MediaPlayer", paramArrayOfByte1.toString());
        throw paramArrayOfByte2;
        label257:
        Log.w("MediaPlayer", "getKeyRequest NotProvisionedException: Unexpected. Shouldn't have reached here.");
        paramArrayOfByte1 = new java/lang/IllegalStateException;
        paramArrayOfByte1.<init>("getKeyRequest: Unexpected provisioning error.");
        throw paramArrayOfByte1;
      }
      Log.e("MediaPlayer", "getKeyRequest NoDrmSchemeException");
      paramArrayOfByte1 = new android/media/MediaPlayer$NoDrmSchemeException;
      paramArrayOfByte1.<init>("getKeyRequest: Has to set a DRM scheme first.");
      throw paramArrayOfByte1;
    }
  }
  
  public MediaTimeProvider getMediaTimeProvider()
  {
    if (mTimeProvider == null) {
      mTimeProvider = new TimeProvider(this);
    }
    return mTimeProvider;
  }
  
  public Metadata getMetadata(boolean paramBoolean1, boolean paramBoolean2)
  {
    Parcel localParcel = Parcel.obtain();
    Metadata localMetadata = new Metadata();
    if (!native_getMetadata(paramBoolean1, paramBoolean2, localParcel))
    {
      localParcel.recycle();
      return null;
    }
    if (!localMetadata.parse(localParcel))
    {
      localParcel.recycle();
      return null;
    }
    return localMetadata;
  }
  
  public PersistableBundle getMetrics()
  {
    return native_getMetrics();
  }
  
  public native PlaybackParams getPlaybackParams();
  
  public AudioDeviceInfo getPreferredDevice()
  {
    try
    {
      AudioDeviceInfo localAudioDeviceInfo = mPreferredDevice;
      return localAudioDeviceInfo;
    }
    finally {}
  }
  
  public AudioDeviceInfo getRoutedDevice()
  {
    int i = native_getRoutedDeviceId();
    if (i == 0) {
      return null;
    }
    AudioDeviceInfo[] arrayOfAudioDeviceInfo = AudioManager.getDevicesStatic(2);
    for (int j = 0; j < arrayOfAudioDeviceInfo.length; j++) {
      if (arrayOfAudioDeviceInfo[j].getId() == i) {
        return arrayOfAudioDeviceInfo[j];
      }
    }
    return null;
  }
  
  /* Error */
  public int getSelectedTrack(int paramInt)
    throws IllegalStateException
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 576	android/media/MediaPlayer:mSubtitleController	Landroid/media/SubtitleController;
    //   4: astore_2
    //   5: iconst_0
    //   6: istore_3
    //   7: aload_2
    //   8: ifnull +101 -> 109
    //   11: iload_1
    //   12: iconst_4
    //   13: if_icmpeq +8 -> 21
    //   16: iload_1
    //   17: iconst_3
    //   18: if_icmpne +91 -> 109
    //   21: aload_0
    //   22: getfield 576	android/media/MediaPlayer:mSubtitleController	Landroid/media/SubtitleController;
    //   25: invokevirtual 1081	android/media/SubtitleController:getSelectedTrack	()Landroid/media/SubtitleTrack;
    //   28: astore 4
    //   30: aload 4
    //   32: ifnull +77 -> 109
    //   35: aload_0
    //   36: getfield 389	android/media/MediaPlayer:mIndexTrackPairs	Ljava/util/Vector;
    //   39: astore_2
    //   40: aload_2
    //   41: monitorenter
    //   42: iconst_0
    //   43: istore 5
    //   45: iload 5
    //   47: aload_0
    //   48: getfield 389	android/media/MediaPlayer:mIndexTrackPairs	Ljava/util/Vector;
    //   51: invokevirtual 1092	java/util/Vector:size	()I
    //   54: if_icmpge +43 -> 97
    //   57: aload_0
    //   58: getfield 389	android/media/MediaPlayer:mIndexTrackPairs	Ljava/util/Vector;
    //   61: iload 5
    //   63: invokevirtual 1064	java/util/Vector:get	(I)Ljava/lang/Object;
    //   66: checkcast 935	android/util/Pair
    //   69: getfield 1067	android/util/Pair:second	Ljava/lang/Object;
    //   72: aload 4
    //   74: if_acmpne +17 -> 91
    //   77: aload 4
    //   79: invokevirtual 1088	android/media/SubtitleTrack:getTrackType	()I
    //   82: iload_1
    //   83: if_icmpne +8 -> 91
    //   86: aload_2
    //   87: monitorexit
    //   88: iload 5
    //   90: ireturn
    //   91: iinc 5 1
    //   94: goto -49 -> 45
    //   97: aload_2
    //   98: monitorexit
    //   99: goto +10 -> 109
    //   102: astore 4
    //   104: aload_2
    //   105: monitorexit
    //   106: aload 4
    //   108: athrow
    //   109: invokestatic 833	android/os/Parcel:obtain	()Landroid/os/Parcel;
    //   112: astore_2
    //   113: invokestatic 833	android/os/Parcel:obtain	()Landroid/os/Parcel;
    //   116: astore 4
    //   118: aload_2
    //   119: ldc -122
    //   121: invokevirtual 836	android/os/Parcel:writeInterfaceToken	(Ljava/lang/String;)V
    //   124: aload_2
    //   125: bipush 7
    //   127: invokevirtual 839	android/os/Parcel:writeInt	(I)V
    //   130: aload_2
    //   131: iload_1
    //   132: invokevirtual 839	android/os/Parcel:writeInt	(I)V
    //   135: aload_0
    //   136: aload_2
    //   137: aload 4
    //   139: invokevirtual 843	android/media/MediaPlayer:invoke	(Landroid/os/Parcel;Landroid/os/Parcel;)V
    //   142: aload 4
    //   144: invokevirtual 1487	android/os/Parcel:readInt	()I
    //   147: istore 5
    //   149: aload_0
    //   150: getfield 389	android/media/MediaPlayer:mIndexTrackPairs	Ljava/util/Vector;
    //   153: astore 6
    //   155: aload 6
    //   157: monitorenter
    //   158: iload_3
    //   159: istore_1
    //   160: iload_1
    //   161: aload_0
    //   162: getfield 389	android/media/MediaPlayer:mIndexTrackPairs	Ljava/util/Vector;
    //   165: invokevirtual 1092	java/util/Vector:size	()I
    //   168: if_icmpge +60 -> 228
    //   171: aload_0
    //   172: getfield 389	android/media/MediaPlayer:mIndexTrackPairs	Ljava/util/Vector;
    //   175: iload_1
    //   176: invokevirtual 1064	java/util/Vector:get	(I)Ljava/lang/Object;
    //   179: checkcast 935	android/util/Pair
    //   182: astore 7
    //   184: aload 7
    //   186: getfield 1072	android/util/Pair:first	Ljava/lang/Object;
    //   189: ifnull +33 -> 222
    //   192: aload 7
    //   194: getfield 1072	android/util/Pair:first	Ljava/lang/Object;
    //   197: checkcast 929	java/lang/Integer
    //   200: invokevirtual 1075	java/lang/Integer:intValue	()I
    //   203: iload 5
    //   205: if_icmpne +17 -> 222
    //   208: aload 6
    //   210: monitorexit
    //   211: aload_2
    //   212: invokevirtual 856	android/os/Parcel:recycle	()V
    //   215: aload 4
    //   217: invokevirtual 856	android/os/Parcel:recycle	()V
    //   220: iload_1
    //   221: ireturn
    //   222: iinc 1 1
    //   225: goto -65 -> 160
    //   228: aload 6
    //   230: monitorexit
    //   231: aload_2
    //   232: invokevirtual 856	android/os/Parcel:recycle	()V
    //   235: aload 4
    //   237: invokevirtual 856	android/os/Parcel:recycle	()V
    //   240: iconst_m1
    //   241: ireturn
    //   242: astore 7
    //   244: aload 6
    //   246: monitorexit
    //   247: aload 7
    //   249: athrow
    //   250: astore 6
    //   252: aload_2
    //   253: invokevirtual 856	android/os/Parcel:recycle	()V
    //   256: aload 4
    //   258: invokevirtual 856	android/os/Parcel:recycle	()V
    //   261: aload 6
    //   263: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	264	0	this	MediaPlayer
    //   0	264	1	paramInt	int
    //   4	249	2	localObject1	Object
    //   6	153	3	i	int
    //   28	50	4	localSubtitleTrack	SubtitleTrack
    //   102	5	4	localObject2	Object
    //   116	141	4	localParcel	Parcel
    //   43	163	5	j	int
    //   153	92	6	localVector	Vector
    //   250	12	6	localObject3	Object
    //   182	11	7	localPair	Pair
    //   242	6	7	localObject4	Object
    // Exception table:
    //   from	to	target	type
    //   45	88	102	finally
    //   97	99	102	finally
    //   104	106	102	finally
    //   160	211	242	finally
    //   228	231	242	finally
    //   244	247	242	finally
    //   118	158	250	finally
    //   247	250	250	finally
  }
  
  public native SyncParams getSyncParams();
  
  public MediaTimestamp getTimestamp()
  {
    try
    {
      long l1 = getCurrentPosition();
      long l2 = System.nanoTime();
      float f;
      if (isPlaying()) {
        f = getPlaybackParams().getSpeed();
      } else {
        f = 0.0F;
      }
      MediaTimestamp localMediaTimestamp = new MediaTimestamp(l1 * 1000L, l2, f);
      return localMediaTimestamp;
    }
    catch (IllegalStateException localIllegalStateException) {}
    return null;
  }
  
  public TrackInfo[] getTrackInfo()
    throws IllegalStateException
  {
    TrackInfo[] arrayOfTrackInfo1 = getInbandTrackInfo();
    synchronized (mIndexTrackPairs)
    {
      TrackInfo[] arrayOfTrackInfo2 = new TrackInfo[mIndexTrackPairs.size()];
      for (int i = 0; i < arrayOfTrackInfo2.length; i++)
      {
        Object localObject2 = (Pair)mIndexTrackPairs.get(i);
        if (first != null)
        {
          arrayOfTrackInfo2[i] = arrayOfTrackInfo1[((Integer)first).intValue()];
        }
        else
        {
          localObject2 = (SubtitleTrack)second;
          arrayOfTrackInfo2[i] = new TrackInfo(((SubtitleTrack)localObject2).getTrackType(), ((SubtitleTrack)localObject2).getFormat());
        }
      }
      return arrayOfTrackInfo2;
    }
  }
  
  public native int getVideoHeight();
  
  public native int getVideoWidth();
  
  public void invoke(Parcel paramParcel1, Parcel paramParcel2)
  {
    int i = native_invoke(paramParcel1, paramParcel2);
    paramParcel2.setDataPosition(0);
    if (i == 0) {
      return;
    }
    paramParcel1 = new StringBuilder();
    paramParcel1.append("failure code: ");
    paramParcel1.append(i);
    throw new RuntimeException(paramParcel1.toString());
  }
  
  public native boolean isLooping();
  
  public native boolean isPlaying();
  
  boolean isStreamSystemEnforcedMute()
  {
    boolean bool;
    if ((!readCameraSoundForced()) && (isStreamRingMute())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public Parcel newRequest()
  {
    Parcel localParcel = Parcel.obtain();
    localParcel.writeInterfaceToken("android.media.IMediaPlayer");
    return localParcel;
  }
  
  public void notifyAt(long paramLong)
  {
    _notifyAt(paramLong);
  }
  
  /* Error */
  public void onSubtitleTrackSelected(SubtitleTrack paramSubtitleTrack)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 396	android/media/MediaPlayer:mSelectedSubtitleTrackIndex	I
    //   4: iflt +21 -> 25
    //   7: aload_0
    //   8: aload_0
    //   9: getfield 396	android/media/MediaPlayer:mSelectedSubtitleTrackIndex	I
    //   12: iconst_0
    //   13: invokespecial 1077	android/media/MediaPlayer:selectOrDeselectInbandTrack	(IZ)V
    //   16: goto +4 -> 20
    //   19: astore_2
    //   20: aload_0
    //   21: iconst_m1
    //   22: putfield 396	android/media/MediaPlayer:mSelectedSubtitleTrackIndex	I
    //   25: aload_0
    //   26: monitorenter
    //   27: aload_0
    //   28: iconst_1
    //   29: putfield 629	android/media/MediaPlayer:mSubtitleDataListenerDisabled	Z
    //   32: aload_0
    //   33: monitorexit
    //   34: aload_1
    //   35: ifnonnull +4 -> 39
    //   38: return
    //   39: aload_0
    //   40: getfield 389	android/media/MediaPlayer:mIndexTrackPairs	Ljava/util/Vector;
    //   43: astore_2
    //   44: aload_2
    //   45: monitorenter
    //   46: aload_0
    //   47: getfield 389	android/media/MediaPlayer:mIndexTrackPairs	Ljava/util/Vector;
    //   50: invokevirtual 1540	java/util/Vector:iterator	()Ljava/util/Iterator;
    //   53: astore_3
    //   54: aload_3
    //   55: invokeinterface 1111 1 0
    //   60: ifeq +52 -> 112
    //   63: aload_3
    //   64: invokeinterface 1114 1 0
    //   69: checkcast 935	android/util/Pair
    //   72: astore 4
    //   74: aload 4
    //   76: getfield 1072	android/util/Pair:first	Ljava/lang/Object;
    //   79: ifnull +30 -> 109
    //   82: aload 4
    //   84: getfield 1067	android/util/Pair:second	Ljava/lang/Object;
    //   87: aload_1
    //   88: if_acmpne +21 -> 109
    //   91: aload_0
    //   92: aload 4
    //   94: getfield 1072	android/util/Pair:first	Ljava/lang/Object;
    //   97: checkcast 929	java/lang/Integer
    //   100: invokevirtual 1075	java/lang/Integer:intValue	()I
    //   103: putfield 396	android/media/MediaPlayer:mSelectedSubtitleTrackIndex	I
    //   106: goto +6 -> 112
    //   109: goto -55 -> 54
    //   112: aload_2
    //   113: monitorexit
    //   114: aload_0
    //   115: getfield 396	android/media/MediaPlayer:mSelectedSubtitleTrackIndex	I
    //   118: iflt +33 -> 151
    //   121: aload_0
    //   122: aload_0
    //   123: getfield 396	android/media/MediaPlayer:mSelectedSubtitleTrackIndex	I
    //   126: iconst_1
    //   127: invokespecial 1077	android/media/MediaPlayer:selectOrDeselectInbandTrack	(IZ)V
    //   130: goto +4 -> 134
    //   133: astore_1
    //   134: aload_0
    //   135: monitorenter
    //   136: aload_0
    //   137: iconst_0
    //   138: putfield 629	android/media/MediaPlayer:mSubtitleDataListenerDisabled	Z
    //   141: aload_0
    //   142: monitorexit
    //   143: goto +8 -> 151
    //   146: astore_1
    //   147: aload_0
    //   148: monitorexit
    //   149: aload_1
    //   150: athrow
    //   151: return
    //   152: astore_1
    //   153: aload_2
    //   154: monitorexit
    //   155: aload_1
    //   156: athrow
    //   157: astore_1
    //   158: aload_0
    //   159: monitorexit
    //   160: aload_1
    //   161: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	162	0	this	MediaPlayer
    //   0	162	1	paramSubtitleTrack	SubtitleTrack
    //   19	1	2	localIllegalStateException	IllegalStateException
    //   53	11	3	localIterator	Iterator
    //   72	21	4	localPair	Pair
    // Exception table:
    //   from	to	target	type
    //   7	16	19	java/lang/IllegalStateException
    //   121	130	133	java/lang/IllegalStateException
    //   136	143	146	finally
    //   147	149	146	finally
    //   46	54	152	finally
    //   54	106	152	finally
    //   112	114	152	finally
    //   153	155	152	finally
    //   27	34	157	finally
    //   158	160	157	finally
  }
  
  public void pause()
    throws IllegalStateException
  {
    stayAwake(false);
    _pause();
    basePause();
  }
  
  int playerApplyVolumeShaper(VolumeShaper.Configuration paramConfiguration, VolumeShaper.Operation paramOperation)
  {
    return native_applyVolumeShaper(paramConfiguration, paramOperation);
  }
  
  VolumeShaper.State playerGetVolumeShaperState(int paramInt)
  {
    return native_getVolumeShaperState(paramInt);
  }
  
  void playerPause()
  {
    pause();
  }
  
  int playerSetAuxEffectSendLevel(boolean paramBoolean, float paramFloat)
  {
    if (paramBoolean) {
      paramFloat = 0.0F;
    }
    _setAuxEffectSendLevel(paramFloat);
    return 0;
  }
  
  void playerSetVolume(boolean paramBoolean, float paramFloat1, float paramFloat2)
  {
    float f = 0.0F;
    if (paramBoolean) {
      paramFloat1 = 0.0F;
    }
    if (paramBoolean) {
      paramFloat2 = f;
    }
    _setVolume(paramFloat1, paramFloat2);
  }
  
  void playerStart()
  {
    start();
  }
  
  void playerStop()
  {
    stop();
  }
  
  public void prepare()
    throws IOException, IllegalStateException
  {
    _prepare();
    scanInternalSubtitleTracks();
    synchronized (mDrmLock)
    {
      mDrmInfoResolved = true;
      return;
    }
  }
  
  public native void prepareAsync()
    throws IllegalStateException;
  
  /* Error */
  public void prepareDrm(UUID paramUUID)
    throws UnsupportedSchemeException, ResourceBusyException, MediaPlayer.ProvisioningNetworkErrorException, MediaPlayer.ProvisioningServerErrorException
  {
    // Byte code:
    //   0: new 483	java/lang/StringBuilder
    //   3: dup
    //   4: invokespecial 484	java/lang/StringBuilder:<init>	()V
    //   7: astore_2
    //   8: aload_2
    //   9: ldc_w 1575
    //   12: invokevirtual 490	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   15: pop
    //   16: aload_2
    //   17: aload_1
    //   18: invokevirtual 499	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   21: pop
    //   22: aload_2
    //   23: ldc_w 1577
    //   26: invokevirtual 490	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   29: pop
    //   30: aload_2
    //   31: aload_0
    //   32: getfield 1579	android/media/MediaPlayer:mOnDrmConfigHelper	Landroid/media/MediaPlayer$OnDrmConfigHelper;
    //   35: invokevirtual 499	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   38: pop
    //   39: ldc -6
    //   41: aload_2
    //   42: invokevirtual 508	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   45: invokestatic 511	android/util/Log:v	(Ljava/lang/String;Ljava/lang/String;)I
    //   48: pop
    //   49: iconst_0
    //   50: istore_3
    //   51: iconst_0
    //   52: istore 4
    //   54: aload_0
    //   55: getfield 377	android/media/MediaPlayer:mDrmLock	Ljava/lang/Object;
    //   58: astore_2
    //   59: aload_2
    //   60: monitorenter
    //   61: aload_0
    //   62: getfield 588	android/media/MediaPlayer:mDrmInfo	Landroid/media/MediaPlayer$DrmInfo;
    //   65: ifnull +617 -> 682
    //   68: aload_0
    //   69: getfield 1038	android/media/MediaPlayer:mActiveDrmScheme	Z
    //   72: ifne +559 -> 631
    //   75: aload_0
    //   76: getfield 669	android/media/MediaPlayer:mPrepareDrmInProgress	Z
    //   79: ifne +530 -> 609
    //   82: aload_0
    //   83: getfield 463	android/media/MediaPlayer:mDrmProvisioningInProgress	Z
    //   86: ifne +501 -> 587
    //   89: aload_0
    //   90: invokespecial 673	android/media/MediaPlayer:cleanDrmObj	()V
    //   93: aload_0
    //   94: iconst_1
    //   95: putfield 669	android/media/MediaPlayer:mPrepareDrmInProgress	Z
    //   98: aload_0
    //   99: getfield 525	android/media/MediaPlayer:mOnDrmPreparedHandlerDelegate	Landroid/media/MediaPlayer$OnDrmPreparedHandlerDelegate;
    //   102: astore 5
    //   104: aload_0
    //   105: aload_1
    //   106: invokespecial 1581	android/media/MediaPlayer:prepareDrm_createDrmStep	(Ljava/util/UUID;)V
    //   109: aload_0
    //   110: iconst_1
    //   111: putfield 1410	android/media/MediaPlayer:mDrmConfigAllowed	Z
    //   114: aload_2
    //   115: monitorexit
    //   116: aload_0
    //   117: getfield 1579	android/media/MediaPlayer:mOnDrmConfigHelper	Landroid/media/MediaPlayer$OnDrmConfigHelper;
    //   120: ifnull +13 -> 133
    //   123: aload_0
    //   124: getfield 1579	android/media/MediaPlayer:mOnDrmConfigHelper	Landroid/media/MediaPlayer$OnDrmConfigHelper;
    //   127: aload_0
    //   128: invokeinterface 1584 2 0
    //   133: aload_0
    //   134: getfield 377	android/media/MediaPlayer:mDrmLock	Ljava/lang/Object;
    //   137: astore_2
    //   138: aload_2
    //   139: monitorenter
    //   140: aload_0
    //   141: iconst_0
    //   142: putfield 1410	android/media/MediaPlayer:mDrmConfigAllowed	Z
    //   145: iconst_0
    //   146: istore 6
    //   148: iload 6
    //   150: istore 7
    //   152: aload_0
    //   153: aload_1
    //   154: invokespecial 1044	android/media/MediaPlayer:prepareDrm_openSessionStep	(Ljava/util/UUID;)V
    //   157: iload 6
    //   159: istore 7
    //   161: aload_0
    //   162: aload_1
    //   163: putfield 1046	android/media/MediaPlayer:mDrmUUID	Ljava/util/UUID;
    //   166: iload 6
    //   168: istore 7
    //   170: aload_0
    //   171: iconst_1
    //   172: putfield 1038	android/media/MediaPlayer:mActiveDrmScheme	Z
    //   175: iconst_1
    //   176: istore 7
    //   178: iconst_1
    //   179: istore 4
    //   181: aload_0
    //   182: getfield 463	android/media/MediaPlayer:mDrmProvisioningInProgress	Z
    //   185: ifne +8 -> 193
    //   188: aload_0
    //   189: iconst_0
    //   190: putfield 669	android/media/MediaPlayer:mPrepareDrmInProgress	Z
    //   193: iload 6
    //   195: ifeq +285 -> 480
    //   198: iload 4
    //   200: istore 7
    //   202: aload_0
    //   203: invokespecial 673	android/media/MediaPlayer:cleanDrmObj	()V
    //   206: goto +274 -> 480
    //   209: astore_1
    //   210: goto +331 -> 541
    //   213: astore_1
    //   214: iload 6
    //   216: istore 7
    //   218: new 483	java/lang/StringBuilder
    //   221: astore 5
    //   223: iload 6
    //   225: istore 7
    //   227: aload 5
    //   229: invokespecial 484	java/lang/StringBuilder:<init>	()V
    //   232: iload 6
    //   234: istore 7
    //   236: aload 5
    //   238: ldc_w 1586
    //   241: invokevirtual 490	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   244: pop
    //   245: iload 6
    //   247: istore 7
    //   249: aload 5
    //   251: aload_1
    //   252: invokevirtual 499	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   255: pop
    //   256: iload 6
    //   258: istore 7
    //   260: ldc -6
    //   262: aload 5
    //   264: invokevirtual 508	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   267: invokestatic 471	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   270: pop
    //   271: iconst_1
    //   272: istore 7
    //   274: aload_1
    //   275: athrow
    //   276: astore 8
    //   278: iload 6
    //   280: istore 7
    //   282: ldc -6
    //   284: ldc_w 1588
    //   287: invokestatic 533	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   290: pop
    //   291: iload 6
    //   293: istore 7
    //   295: aload_0
    //   296: aload_1
    //   297: invokespecial 1590	android/media/MediaPlayer:HandleProvisioninig	(Ljava/util/UUID;)I
    //   300: istore 9
    //   302: iload 9
    //   304: ifeq +149 -> 453
    //   307: iconst_1
    //   308: istore 6
    //   310: iload 9
    //   312: tableswitch	default:+24->336, 1:+65->377, 2:+27->339
    //   336: goto +79 -> 415
    //   339: iload 6
    //   341: istore 7
    //   343: ldc -6
    //   345: ldc_w 1592
    //   348: invokestatic 471	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   351: pop
    //   352: iload 6
    //   354: istore 7
    //   356: new 105	android/media/MediaPlayer$ProvisioningServerErrorException
    //   359: astore_1
    //   360: iload 6
    //   362: istore 7
    //   364: aload_1
    //   365: ldc_w 1592
    //   368: invokespecial 1593	android/media/MediaPlayer$ProvisioningServerErrorException:<init>	(Ljava/lang/String;)V
    //   371: iload 6
    //   373: istore 7
    //   375: aload_1
    //   376: athrow
    //   377: iload 6
    //   379: istore 7
    //   381: ldc -6
    //   383: ldc_w 1595
    //   386: invokestatic 471	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   389: pop
    //   390: iload 6
    //   392: istore 7
    //   394: new 102	android/media/MediaPlayer$ProvisioningNetworkErrorException
    //   397: astore_1
    //   398: iload 6
    //   400: istore 7
    //   402: aload_1
    //   403: ldc_w 1595
    //   406: invokespecial 1596	android/media/MediaPlayer$ProvisioningNetworkErrorException:<init>	(Ljava/lang/String;)V
    //   409: iload 6
    //   411: istore 7
    //   413: aload_1
    //   414: athrow
    //   415: iload 6
    //   417: istore 7
    //   419: ldc -6
    //   421: ldc_w 1598
    //   424: invokestatic 471	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   427: pop
    //   428: iload 6
    //   430: istore 7
    //   432: new 540	java/lang/IllegalStateException
    //   435: astore_1
    //   436: iload 6
    //   438: istore 7
    //   440: aload_1
    //   441: ldc_w 1598
    //   444: invokespecial 1400	java/lang/IllegalStateException:<init>	(Ljava/lang/String;)V
    //   447: iload 6
    //   449: istore 7
    //   451: aload_1
    //   452: athrow
    //   453: aload_0
    //   454: getfield 463	android/media/MediaPlayer:mDrmProvisioningInProgress	Z
    //   457: ifne +8 -> 465
    //   460: aload_0
    //   461: iconst_0
    //   462: putfield 669	android/media/MediaPlayer:mPrepareDrmInProgress	Z
    //   465: iload_3
    //   466: istore 7
    //   468: iload 6
    //   470: ifeq +10 -> 480
    //   473: iload 4
    //   475: istore 7
    //   477: goto -275 -> 202
    //   480: aload_2
    //   481: monitorexit
    //   482: iload 7
    //   484: ifeq +14 -> 498
    //   487: aload 5
    //   489: ifnull +9 -> 498
    //   492: aload 5
    //   494: iconst_0
    //   495: invokevirtual 1601	android/media/MediaPlayer$OnDrmPreparedHandlerDelegate:notifyClient	(I)V
    //   498: return
    //   499: astore_1
    //   500: iload 6
    //   502: istore 7
    //   504: ldc -6
    //   506: ldc_w 1603
    //   509: invokestatic 471	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   512: pop
    //   513: iconst_1
    //   514: istore 6
    //   516: iload 6
    //   518: istore 7
    //   520: new 540	java/lang/IllegalStateException
    //   523: astore_1
    //   524: iload 6
    //   526: istore 7
    //   528: aload_1
    //   529: ldc_w 1603
    //   532: invokespecial 1400	java/lang/IllegalStateException:<init>	(Ljava/lang/String;)V
    //   535: iload 6
    //   537: istore 7
    //   539: aload_1
    //   540: athrow
    //   541: aload_0
    //   542: getfield 463	android/media/MediaPlayer:mDrmProvisioningInProgress	Z
    //   545: ifne +8 -> 553
    //   548: aload_0
    //   549: iconst_0
    //   550: putfield 669	android/media/MediaPlayer:mPrepareDrmInProgress	Z
    //   553: iload 7
    //   555: ifeq +7 -> 562
    //   558: aload_0
    //   559: invokespecial 673	android/media/MediaPlayer:cleanDrmObj	()V
    //   562: aload_1
    //   563: athrow
    //   564: astore_1
    //   565: aload_2
    //   566: monitorexit
    //   567: aload_1
    //   568: athrow
    //   569: astore_1
    //   570: ldc -6
    //   572: ldc_w 1605
    //   575: aload_1
    //   576: invokestatic 1607	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   579: pop
    //   580: aload_0
    //   581: iconst_0
    //   582: putfield 669	android/media/MediaPlayer:mPrepareDrmInProgress	Z
    //   585: aload_1
    //   586: athrow
    //   587: ldc -6
    //   589: ldc_w 1609
    //   592: invokestatic 471	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   595: pop
    //   596: new 540	java/lang/IllegalStateException
    //   599: astore_1
    //   600: aload_1
    //   601: ldc_w 1609
    //   604: invokespecial 1400	java/lang/IllegalStateException:<init>	(Ljava/lang/String;)V
    //   607: aload_1
    //   608: athrow
    //   609: ldc -6
    //   611: ldc_w 1611
    //   614: invokestatic 471	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   617: pop
    //   618: new 540	java/lang/IllegalStateException
    //   621: astore_1
    //   622: aload_1
    //   623: ldc_w 1611
    //   626: invokespecial 1400	java/lang/IllegalStateException:<init>	(Ljava/lang/String;)V
    //   629: aload_1
    //   630: athrow
    //   631: new 483	java/lang/StringBuilder
    //   634: astore_1
    //   635: aload_1
    //   636: invokespecial 484	java/lang/StringBuilder:<init>	()V
    //   639: aload_1
    //   640: ldc_w 1613
    //   643: invokevirtual 490	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   646: pop
    //   647: aload_1
    //   648: aload_0
    //   649: getfield 1046	android/media/MediaPlayer:mDrmUUID	Ljava/util/UUID;
    //   652: invokevirtual 499	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   655: pop
    //   656: aload_1
    //   657: invokevirtual 508	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   660: astore 5
    //   662: ldc -6
    //   664: aload 5
    //   666: invokestatic 471	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   669: pop
    //   670: new 540	java/lang/IllegalStateException
    //   673: astore_1
    //   674: aload_1
    //   675: aload 5
    //   677: invokespecial 1400	java/lang/IllegalStateException:<init>	(Ljava/lang/String;)V
    //   680: aload_1
    //   681: athrow
    //   682: ldc -6
    //   684: ldc_w 1615
    //   687: invokestatic 471	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   690: pop
    //   691: new 540	java/lang/IllegalStateException
    //   694: astore_1
    //   695: aload_1
    //   696: ldc_w 1615
    //   699: invokespecial 1400	java/lang/IllegalStateException:<init>	(Ljava/lang/String;)V
    //   702: aload_1
    //   703: athrow
    //   704: astore_1
    //   705: aload_2
    //   706: monitorexit
    //   707: aload_1
    //   708: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	709	0	this	MediaPlayer
    //   0	709	1	paramUUID	UUID
    //   7	699	2	localObject1	Object
    //   50	416	3	i	int
    //   52	422	4	j	int
    //   102	574	5	localObject2	Object
    //   146	390	6	k	int
    //   150	404	7	m	int
    //   276	1	8	localNotProvisionedException	NotProvisionedException
    //   300	11	9	n	int
    // Exception table:
    //   from	to	target	type
    //   152	157	209	finally
    //   161	166	209	finally
    //   170	175	209	finally
    //   218	223	209	finally
    //   227	232	209	finally
    //   236	245	209	finally
    //   249	256	209	finally
    //   260	271	209	finally
    //   274	276	209	finally
    //   282	291	209	finally
    //   295	302	209	finally
    //   343	352	209	finally
    //   356	360	209	finally
    //   364	371	209	finally
    //   375	377	209	finally
    //   381	390	209	finally
    //   394	398	209	finally
    //   402	409	209	finally
    //   413	415	209	finally
    //   419	428	209	finally
    //   432	436	209	finally
    //   440	447	209	finally
    //   451	453	209	finally
    //   504	513	209	finally
    //   520	524	209	finally
    //   528	535	209	finally
    //   539	541	209	finally
    //   152	157	213	java/lang/Exception
    //   161	166	213	java/lang/Exception
    //   170	175	213	java/lang/Exception
    //   152	157	276	android/media/NotProvisionedException
    //   161	166	276	android/media/NotProvisionedException
    //   170	175	276	android/media/NotProvisionedException
    //   152	157	499	java/lang/IllegalStateException
    //   161	166	499	java/lang/IllegalStateException
    //   170	175	499	java/lang/IllegalStateException
    //   140	145	564	finally
    //   181	193	564	finally
    //   202	206	564	finally
    //   453	465	564	finally
    //   480	482	564	finally
    //   541	553	564	finally
    //   558	562	564	finally
    //   562	564	564	finally
    //   565	567	564	finally
    //   104	109	569	java/lang/Exception
    //   61	104	704	finally
    //   104	109	704	finally
    //   109	116	704	finally
    //   570	587	704	finally
    //   587	609	704	finally
    //   609	631	704	finally
    //   631	682	704	finally
    //   682	704	704	finally
    //   705	707	704	finally
  }
  
  public byte[] provideKeyResponse(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
    throws MediaPlayer.NoDrmSchemeException, DeniedByServerException
  {
    Object localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append("provideKeyResponse: keySetId: ");
    ((StringBuilder)localObject1).append(paramArrayOfByte1);
    ((StringBuilder)localObject1).append(" response: ");
    ((StringBuilder)localObject1).append(paramArrayOfByte2);
    Log.v("MediaPlayer", ((StringBuilder)localObject1).toString());
    synchronized (mDrmLock)
    {
      boolean bool = mActiveDrmScheme;
      if (bool)
      {
        if (paramArrayOfByte1 == null) {
          try
          {
            localObject1 = mDrmSessionId;
          }
          catch (Exception paramArrayOfByte2)
          {
            break label166;
          }
          catch (NotProvisionedException paramArrayOfByte1)
          {
            break label200;
          }
        } else {
          localObject1 = paramArrayOfByte1;
        }
        byte[] arrayOfByte = mDrmObj.provideKeyResponse((byte[])localObject1, paramArrayOfByte2);
        localObject1 = new java/lang/StringBuilder;
        ((StringBuilder)localObject1).<init>();
        ((StringBuilder)localObject1).append("provideKeyResponse: keySetId: ");
        ((StringBuilder)localObject1).append(paramArrayOfByte1);
        ((StringBuilder)localObject1).append(" response: ");
        ((StringBuilder)localObject1).append(paramArrayOfByte2);
        ((StringBuilder)localObject1).append(" --> ");
        ((StringBuilder)localObject1).append(arrayOfByte);
        Log.v("MediaPlayer", ((StringBuilder)localObject1).toString());
        return arrayOfByte;
        label166:
        paramArrayOfByte1 = new java/lang/StringBuilder;
        paramArrayOfByte1.<init>();
        paramArrayOfByte1.append("provideKeyResponse Exception ");
        paramArrayOfByte1.append(paramArrayOfByte2);
        Log.w("MediaPlayer", paramArrayOfByte1.toString());
        throw paramArrayOfByte2;
        label200:
        Log.w("MediaPlayer", "provideKeyResponse NotProvisionedException: Unexpected. Shouldn't have reached here.");
        paramArrayOfByte1 = new java/lang/IllegalStateException;
        paramArrayOfByte1.<init>("provideKeyResponse: Unexpected provisioning error.");
        throw paramArrayOfByte1;
      }
      Log.e("MediaPlayer", "getKeyRequest NoDrmSchemeException");
      paramArrayOfByte1 = new android/media/MediaPlayer$NoDrmSchemeException;
      paramArrayOfByte1.<init>("getKeyRequest: Has to set a DRM scheme first.");
      throw paramArrayOfByte1;
    }
  }
  
  public void release()
  {
    baseRelease();
    stayAwake(false);
    updateSurfaceScreenOn();
    mOnPreparedListener = null;
    mOnBufferingUpdateListener = null;
    mOnCompletionListener = null;
    mOnSeekCompleteListener = null;
    mOnErrorListener = null;
    mOnInfoListener = null;
    mOnVideoSizeChangedListener = null;
    mOnTimedTextListener = null;
    if (mTimeProvider != null)
    {
      mTimeProvider.close();
      mTimeProvider = null;
    }
    try
    {
      mSubtitleDataListenerDisabled = false;
      mExtSubtitleDataListener = null;
      mExtSubtitleDataHandler = null;
      mOnMediaTimeDiscontinuityListener = null;
      mOnMediaTimeDiscontinuityHandler = null;
      mOnDrmConfigHelper = null;
      mOnDrmInfoHandlerDelegate = null;
      mOnDrmPreparedHandlerDelegate = null;
      resetDrmState();
      _release();
      return;
    }
    finally {}
  }
  
  public void releaseDrm()
    throws MediaPlayer.NoDrmSchemeException
  {
    Log.v("MediaPlayer", "releaseDrm:");
    synchronized (mDrmLock)
    {
      boolean bool = mActiveDrmScheme;
      if (bool) {
        try
        {
          try
          {
            _releaseDrm();
            cleanDrmObj();
            mActiveDrmScheme = false;
          }
          catch (Exception localException)
          {
            Log.e("MediaPlayer", "releaseDrm: Exception ", localException);
          }
          return;
        }
        catch (IllegalStateException localIllegalStateException)
        {
          Log.w("MediaPlayer", "releaseDrm: Exception ", localIllegalStateException);
          localObject2 = new java/lang/IllegalStateException;
          ((IllegalStateException)localObject2).<init>("releaseDrm: The player is not in a valid state.");
          throw ((Throwable)localObject2);
        }
      }
      Log.e("MediaPlayer", "releaseDrm(): No active DRM scheme to release.");
      Object localObject2 = new android/media/MediaPlayer$NoDrmSchemeException;
      ((NoDrmSchemeException)localObject2).<init>("releaseDrm: No active DRM scheme to release.");
      throw ((Throwable)localObject2);
    }
  }
  
  public void removeOnRoutingChangedListener(AudioRouting.OnRoutingChangedListener paramOnRoutingChangedListener)
  {
    synchronized (mRoutingChangeListeners)
    {
      if (mRoutingChangeListeners.containsKey(paramOnRoutingChangedListener))
      {
        mRoutingChangeListeners.remove(paramOnRoutingChangedListener);
        enableNativeRoutingCallbacksLocked(false);
      }
      return;
    }
  }
  
  public void reset()
  {
    mSelectedSubtitleTrackIndex = -1;
    synchronized (mOpenSubtitleSources)
    {
      ??? = mOpenSubtitleSources.iterator();
      while (((Iterator)???).hasNext())
      {
        InputStream localInputStream = (InputStream)((Iterator)???).next();
        try
        {
          localInputStream.close();
        }
        catch (IOException localIOException) {}
      }
      mOpenSubtitleSources.clear();
      if (mSubtitleController != null) {
        mSubtitleController.reset();
      }
      if (mTimeProvider != null)
      {
        mTimeProvider.close();
        mTimeProvider = null;
      }
      stayAwake(false);
      _reset();
      if (mEventHandler != null) {
        mEventHandler.removeCallbacksAndMessages(null);
      }
      synchronized (mIndexTrackPairs)
      {
        mIndexTrackPairs.clear();
        mInbandTrackIndices.clear();
        resetDrmState();
        return;
      }
    }
  }
  
  public void restoreKeys(byte[] paramArrayOfByte)
    throws MediaPlayer.NoDrmSchemeException
  {
    ??? = new StringBuilder();
    ((StringBuilder)???).append("restoreKeys: keySetId: ");
    ((StringBuilder)???).append(paramArrayOfByte);
    Log.v("MediaPlayer", ((StringBuilder)???).toString());
    synchronized (mDrmLock)
    {
      boolean bool = mActiveDrmScheme;
      if (bool) {
        try
        {
          mDrmObj.restoreKeys(mDrmSessionId, paramArrayOfByte);
          return;
        }
        catch (Exception paramArrayOfByte)
        {
          StringBuilder localStringBuilder = new java/lang/StringBuilder;
          localStringBuilder.<init>();
          localStringBuilder.append("restoreKeys Exception ");
          localStringBuilder.append(paramArrayOfByte);
          Log.w("MediaPlayer", localStringBuilder.toString());
          throw paramArrayOfByte;
        }
      }
      Log.w("MediaPlayer", "restoreKeys NoDrmSchemeException");
      paramArrayOfByte = new android/media/MediaPlayer$NoDrmSchemeException;
      paramArrayOfByte.<init>("restoreKeys: Has to set a DRM scheme first.");
      throw paramArrayOfByte;
    }
  }
  
  public void seekTo(int paramInt)
    throws IllegalStateException
  {
    seekTo(paramInt, 0);
  }
  
  public void seekTo(long paramLong, int paramInt)
  {
    if ((paramInt >= 0) && (paramInt <= 3))
    {
      long l;
      if (paramLong > 2147483647L)
      {
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("seekTo offset ");
        localStringBuilder.append(paramLong);
        localStringBuilder.append(" is too large, cap to ");
        localStringBuilder.append(Integer.MAX_VALUE);
        Log.w("MediaPlayer", localStringBuilder.toString());
        l = 2147483647L;
      }
      else
      {
        l = paramLong;
        if (paramLong < -2147483648L)
        {
          localStringBuilder = new StringBuilder();
          localStringBuilder.append("seekTo offset ");
          localStringBuilder.append(paramLong);
          localStringBuilder.append(" is too small, cap to ");
          localStringBuilder.append(Integer.MIN_VALUE);
          Log.w("MediaPlayer", localStringBuilder.toString());
          l = -2147483648L;
        }
      }
      _seekTo(l, paramInt);
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Illegal seek mode: ");
    localStringBuilder.append(paramInt);
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  public void selectTrack(int paramInt)
    throws IllegalStateException
  {
    selectOrDeselectTrack(paramInt, true);
  }
  
  public void setAudioAttributes(AudioAttributes paramAudioAttributes)
    throws IllegalArgumentException
  {
    if (paramAudioAttributes != null)
    {
      baseUpdateAudioAttributes(paramAudioAttributes);
      mUsage = paramAudioAttributes.getUsage();
      boolean bool;
      if ((paramAudioAttributes.getAllFlags() & 0x40) != 0) {
        bool = true;
      } else {
        bool = false;
      }
      mBypassInterruptionPolicy = bool;
      Parcel localParcel = Parcel.obtain();
      paramAudioAttributes.writeToParcel(localParcel, 1);
      setParameter(1400, localParcel);
      localParcel.recycle();
      return;
    }
    throw new IllegalArgumentException("Cannot set AudioAttributes to null");
  }
  
  public native void setAudioSessionId(int paramInt)
    throws IllegalArgumentException, IllegalStateException;
  
  public void setAudioStreamType(int paramInt)
  {
    deprecateStreamTypeForPlayback(paramInt, "MediaPlayer", "setAudioStreamType()");
    baseUpdateAudioAttributes(new AudioAttributes.Builder().setInternalLegacyStreamType(paramInt).build());
    _setAudioStreamType(paramInt);
    mStreamType = paramInt;
  }
  
  public void setAuxEffectSendLevel(float paramFloat)
  {
    baseSetAuxEffectSendLevel(paramFloat);
  }
  
  public native void setBufferingParams(BufferingParams paramBufferingParams);
  
  public void setDataSource(Context paramContext, Uri paramUri)
    throws IOException, IllegalArgumentException, SecurityException, IllegalStateException
  {
    setDataSource(paramContext, paramUri, null, null);
  }
  
  public void setDataSource(Context paramContext, Uri paramUri, Map<String, String> paramMap)
    throws IOException, IllegalArgumentException, SecurityException, IllegalStateException
  {
    setDataSource(paramContext, paramUri, paramMap, null);
  }
  
  public void setDataSource(Context paramContext, Uri paramUri, Map<String, String> paramMap, List<HttpCookie> paramList)
    throws IOException
  {
    if (paramContext != null)
    {
      if (paramUri != null)
      {
        if (paramList != null)
        {
          localObject1 = CookieHandler.getDefault();
          if ((localObject1 != null) && (!(localObject1 instanceof CookieManager))) {
            throw new IllegalArgumentException("The cookie handler has to be of CookieManager type when cookies are provided.");
          }
        }
        Object localObject1 = paramContext.getContentResolver();
        Object localObject2 = paramUri.getScheme();
        String str = ContentProvider.getAuthorityWithoutUserId(paramUri.getAuthority());
        if ("file".equals(localObject2))
        {
          setDataSource(paramUri.getPath());
          return;
        }
        if (("content".equals(localObject2)) && ("settings".equals(str)))
        {
          int i = RingtoneManager.getDefaultType(paramUri);
          localObject2 = RingtoneManager.getCacheForType(i, paramContext.getUserId());
          paramContext = RingtoneManager.getActualDefaultRingtoneUri(paramContext, i);
          if (attemptDataSource((ContentResolver)localObject1, (Uri)localObject2)) {
            return;
          }
          if (attemptDataSource((ContentResolver)localObject1, paramContext)) {
            return;
          }
          setDataSource(paramUri.toString(), paramMap, paramList);
        }
        else
        {
          if (attemptDataSource((ContentResolver)localObject1, paramUri)) {
            return;
          }
          setDataSource(paramUri.toString(), paramMap, paramList);
        }
        return;
      }
      throw new NullPointerException("uri param can not be null.");
    }
    throw new NullPointerException("context param can not be null.");
  }
  
  public void setDataSource(AssetFileDescriptor paramAssetFileDescriptor)
    throws IOException, IllegalArgumentException, IllegalStateException
  {
    Preconditions.checkNotNull(paramAssetFileDescriptor);
    if (paramAssetFileDescriptor.getDeclaredLength() < 0L) {
      setDataSource(paramAssetFileDescriptor.getFileDescriptor());
    } else {
      setDataSource(paramAssetFileDescriptor.getFileDescriptor(), paramAssetFileDescriptor.getStartOffset(), paramAssetFileDescriptor.getDeclaredLength());
    }
  }
  
  public void setDataSource(MediaDataSource paramMediaDataSource)
    throws IllegalArgumentException, IllegalStateException
  {
    _setDataSource(paramMediaDataSource);
  }
  
  public void setDataSource(FileDescriptor paramFileDescriptor)
    throws IOException, IllegalArgumentException, IllegalStateException
  {
    setDataSource(paramFileDescriptor, 0L, 576460752303423487L);
  }
  
  public void setDataSource(FileDescriptor paramFileDescriptor, long paramLong1, long paramLong2)
    throws IOException, IllegalArgumentException, IllegalStateException
  {
    _setDataSource(paramFileDescriptor, paramLong1, paramLong2);
  }
  
  public void setDataSource(String paramString)
    throws IOException, IllegalArgumentException, SecurityException, IllegalStateException
  {
    setDataSource(paramString, null, null);
  }
  
  public void setDataSource(String paramString, Map<String, String> paramMap)
    throws IOException, IllegalArgumentException, SecurityException, IllegalStateException
  {
    setDataSource(paramString, paramMap, null);
  }
  
  public void setDisplay(SurfaceHolder paramSurfaceHolder)
  {
    mSurfaceHolder = paramSurfaceHolder;
    if (paramSurfaceHolder != null) {
      paramSurfaceHolder = paramSurfaceHolder.getSurface();
    } else {
      paramSurfaceHolder = null;
    }
    _setVideoSurface(paramSurfaceHolder);
    updateSurfaceScreenOn();
  }
  
  public void setDrmPropertyString(String paramString1, String paramString2)
    throws MediaPlayer.NoDrmSchemeException
  {
    ??? = new StringBuilder();
    ((StringBuilder)???).append("setDrmPropertyString: propertyName: ");
    ((StringBuilder)???).append(paramString1);
    ((StringBuilder)???).append(" value: ");
    ((StringBuilder)???).append(paramString2);
    Log.v("MediaPlayer", ((StringBuilder)???).toString());
    synchronized (mDrmLock)
    {
      if ((!mActiveDrmScheme) && (!mDrmConfigAllowed))
      {
        Log.w("MediaPlayer", "setDrmPropertyString NoDrmSchemeException");
        paramString1 = new android/media/MediaPlayer$NoDrmSchemeException;
        paramString1.<init>("setDrmPropertyString: Has to prepareDrm() first.");
        throw paramString1;
      }
      try
      {
        mDrmObj.setPropertyString(paramString1, paramString2);
        return;
      }
      catch (Exception paramString1)
      {
        paramString2 = new java/lang/StringBuilder;
        paramString2.<init>();
        paramString2.append("setDrmPropertyString Exception ");
        paramString2.append(paramString1);
        Log.w("MediaPlayer", paramString2.toString());
        throw paramString1;
      }
    }
  }
  
  public native void setLooping(boolean paramBoolean);
  
  public int setMetadataFilter(Set<Integer> paramSet1, Set<Integer> paramSet2)
  {
    Parcel localParcel = newRequest();
    int i = localParcel.dataSize() + 4 * (paramSet1.size() + 1 + 1 + paramSet2.size());
    if (localParcel.dataCapacity() < i) {
      localParcel.setDataCapacity(i);
    }
    localParcel.writeInt(paramSet1.size());
    paramSet1 = paramSet1.iterator();
    while (paramSet1.hasNext()) {
      localParcel.writeInt(((Integer)paramSet1.next()).intValue());
    }
    localParcel.writeInt(paramSet2.size());
    paramSet1 = paramSet2.iterator();
    while (paramSet1.hasNext()) {
      localParcel.writeInt(((Integer)paramSet1.next()).intValue());
    }
    return native_setMetadataFilter(localParcel);
  }
  
  public native void setNextMediaPlayer(MediaPlayer paramMediaPlayer);
  
  public void setOnBufferingUpdateListener(OnBufferingUpdateListener paramOnBufferingUpdateListener)
  {
    mOnBufferingUpdateListener = paramOnBufferingUpdateListener;
  }
  
  public void setOnCompletionListener(OnCompletionListener paramOnCompletionListener)
  {
    mOnCompletionListener = paramOnCompletionListener;
  }
  
  public void setOnDrmConfigHelper(OnDrmConfigHelper paramOnDrmConfigHelper)
  {
    synchronized (mDrmLock)
    {
      mOnDrmConfigHelper = paramOnDrmConfigHelper;
      return;
    }
  }
  
  public void setOnDrmInfoListener(OnDrmInfoListener paramOnDrmInfoListener)
  {
    setOnDrmInfoListener(paramOnDrmInfoListener, null);
  }
  
  public void setOnDrmInfoListener(OnDrmInfoListener paramOnDrmInfoListener, Handler paramHandler)
  {
    Object localObject = mDrmLock;
    if (paramOnDrmInfoListener != null) {
      try
      {
        OnDrmInfoHandlerDelegate localOnDrmInfoHandlerDelegate = new android/media/MediaPlayer$OnDrmInfoHandlerDelegate;
        localOnDrmInfoHandlerDelegate.<init>(this, this, paramOnDrmInfoListener, paramHandler);
        mOnDrmInfoHandlerDelegate = localOnDrmInfoHandlerDelegate;
      }
      finally
      {
        break label46;
      }
    } else {
      mOnDrmInfoHandlerDelegate = null;
    }
    return;
    label46:
    throw paramOnDrmInfoListener;
  }
  
  public void setOnDrmPreparedListener(OnDrmPreparedListener paramOnDrmPreparedListener)
  {
    setOnDrmPreparedListener(paramOnDrmPreparedListener, null);
  }
  
  public void setOnDrmPreparedListener(OnDrmPreparedListener paramOnDrmPreparedListener, Handler paramHandler)
  {
    Object localObject = mDrmLock;
    if (paramOnDrmPreparedListener != null) {
      try
      {
        OnDrmPreparedHandlerDelegate localOnDrmPreparedHandlerDelegate = new android/media/MediaPlayer$OnDrmPreparedHandlerDelegate;
        localOnDrmPreparedHandlerDelegate.<init>(this, this, paramOnDrmPreparedListener, paramHandler);
        mOnDrmPreparedHandlerDelegate = localOnDrmPreparedHandlerDelegate;
      }
      finally
      {
        break label46;
      }
    } else {
      mOnDrmPreparedHandlerDelegate = null;
    }
    return;
    label46:
    throw paramOnDrmPreparedListener;
  }
  
  public void setOnErrorListener(OnErrorListener paramOnErrorListener)
  {
    mOnErrorListener = paramOnErrorListener;
  }
  
  public void setOnInfoListener(OnInfoListener paramOnInfoListener)
  {
    mOnInfoListener = paramOnInfoListener;
  }
  
  public void setOnMediaTimeDiscontinuityListener(OnMediaTimeDiscontinuityListener paramOnMediaTimeDiscontinuityListener)
  {
    if (paramOnMediaTimeDiscontinuityListener != null)
    {
      setOnMediaTimeDiscontinuityListenerInt(paramOnMediaTimeDiscontinuityListener, null);
      return;
    }
    throw new IllegalArgumentException("Illegal null listener");
  }
  
  public void setOnMediaTimeDiscontinuityListener(OnMediaTimeDiscontinuityListener paramOnMediaTimeDiscontinuityListener, Handler paramHandler)
  {
    if (paramOnMediaTimeDiscontinuityListener != null)
    {
      if (paramHandler != null)
      {
        setOnMediaTimeDiscontinuityListenerInt(paramOnMediaTimeDiscontinuityListener, paramHandler);
        return;
      }
      throw new IllegalArgumentException("Illegal null handler");
    }
    throw new IllegalArgumentException("Illegal null listener");
  }
  
  public void setOnPreparedListener(OnPreparedListener paramOnPreparedListener)
  {
    mOnPreparedListener = paramOnPreparedListener;
  }
  
  public void setOnSeekCompleteListener(OnSeekCompleteListener paramOnSeekCompleteListener)
  {
    mOnSeekCompleteListener = paramOnSeekCompleteListener;
  }
  
  public void setOnSubtitleDataListener(OnSubtitleDataListener paramOnSubtitleDataListener)
  {
    if (paramOnSubtitleDataListener != null)
    {
      setOnSubtitleDataListenerInt(paramOnSubtitleDataListener, null);
      return;
    }
    throw new IllegalArgumentException("Illegal null listener");
  }
  
  public void setOnSubtitleDataListener(OnSubtitleDataListener paramOnSubtitleDataListener, Handler paramHandler)
  {
    if (paramOnSubtitleDataListener != null)
    {
      if (paramHandler != null)
      {
        setOnSubtitleDataListenerInt(paramOnSubtitleDataListener, paramHandler);
        return;
      }
      throw new IllegalArgumentException("Illegal null handler");
    }
    throw new IllegalArgumentException("Illegal null listener");
  }
  
  public void setOnTimedMetaDataAvailableListener(OnTimedMetaDataAvailableListener paramOnTimedMetaDataAvailableListener)
  {
    mOnTimedMetaDataAvailableListener = paramOnTimedMetaDataAvailableListener;
  }
  
  public void setOnTimedTextListener(OnTimedTextListener paramOnTimedTextListener)
  {
    mOnTimedTextListener = paramOnTimedTextListener;
  }
  
  public void setOnVideoSizeChangedListener(OnVideoSizeChangedListener paramOnVideoSizeChangedListener)
  {
    mOnVideoSizeChangedListener = paramOnVideoSizeChangedListener;
  }
  
  public native void setPlaybackParams(PlaybackParams paramPlaybackParams);
  
  public boolean setPreferredDevice(AudioDeviceInfo paramAudioDeviceInfo)
  {
    int i = 0;
    if ((paramAudioDeviceInfo != null) && (!paramAudioDeviceInfo.isSink())) {
      return false;
    }
    if (paramAudioDeviceInfo != null) {
      i = paramAudioDeviceInfo.getId();
    }
    boolean bool = native_setOutputDevice(i);
    if (bool == true) {
      try
      {
        mPreferredDevice = paramAudioDeviceInfo;
      }
      finally {}
    }
    return bool;
  }
  
  public void setRetransmitEndpoint(InetSocketAddress paramInetSocketAddress)
    throws IllegalStateException, IllegalArgumentException
  {
    String str = null;
    int i = 0;
    if (paramInetSocketAddress != null)
    {
      str = paramInetSocketAddress.getAddress().getHostAddress();
      i = paramInetSocketAddress.getPort();
    }
    i = native_setRetransmitEndpoint(str, i);
    if (i == 0) {
      return;
    }
    paramInetSocketAddress = new StringBuilder();
    paramInetSocketAddress.append("Illegal re-transmit endpoint; native ret ");
    paramInetSocketAddress.append(i);
    throw new IllegalArgumentException(paramInetSocketAddress.toString());
  }
  
  public void setScreenOnWhilePlaying(boolean paramBoolean)
  {
    if (mScreenOnWhilePlaying != paramBoolean)
    {
      if ((paramBoolean) && (mSurfaceHolder == null)) {
        Log.w("MediaPlayer", "setScreenOnWhilePlaying(true) is ineffective without a SurfaceHolder");
      }
      mScreenOnWhilePlaying = paramBoolean;
      updateSurfaceScreenOn();
    }
  }
  
  public void setSubtitleAnchor(SubtitleController paramSubtitleController, SubtitleController.Anchor paramAnchor)
  {
    mSubtitleController = paramSubtitleController;
    mSubtitleController.setAnchor(paramAnchor);
  }
  
  public void setSurface(Surface paramSurface)
  {
    if ((mScreenOnWhilePlaying) && (paramSurface != null)) {
      Log.w("MediaPlayer", "setScreenOnWhilePlaying(true) is ineffective for Surface");
    }
    mSurfaceHolder = null;
    _setVideoSurface(paramSurface);
    updateSurfaceScreenOn();
  }
  
  public native void setSyncParams(SyncParams paramSyncParams);
  
  public void setVideoScalingMode(int paramInt)
  {
    if (isVideoScalingModeSupported(paramInt))
    {
      localObject1 = Parcel.obtain();
      Parcel localParcel = Parcel.obtain();
      try
      {
        ((Parcel)localObject1).writeInterfaceToken("android.media.IMediaPlayer");
        ((Parcel)localObject1).writeInt(6);
        ((Parcel)localObject1).writeInt(paramInt);
        invoke((Parcel)localObject1, localParcel);
        return;
      }
      finally
      {
        ((Parcel)localObject1).recycle();
        localParcel.recycle();
      }
    }
    Object localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append("Scaling mode ");
    ((StringBuilder)localObject1).append(paramInt);
    ((StringBuilder)localObject1).append(" is not supported");
    throw new IllegalArgumentException(((StringBuilder)localObject1).toString());
  }
  
  public void setVolume(float paramFloat)
  {
    setVolume(paramFloat, paramFloat);
  }
  
  public void setVolume(float paramFloat1, float paramFloat2)
  {
    baseSetVolume(paramFloat1, paramFloat2);
  }
  
  public void setWakeMode(Context paramContext, int paramInt)
  {
    int i = 0;
    int j = 0;
    if (SystemProperties.getBoolean("audio.offload.ignore_setawake", false) == true)
    {
      paramContext = new StringBuilder();
      paramContext.append("IGNORING setWakeMode ");
      paramContext.append(paramInt);
      Log.w("MediaPlayer", paramContext.toString());
      return;
    }
    if (mWakeLock != null)
    {
      i = j;
      if (mWakeLock.isHeld())
      {
        i = 1;
        mWakeLock.release();
      }
      mWakeLock = null;
    }
    mWakeLock = ((PowerManager)paramContext.getSystemService("power")).newWakeLock(0x20000000 | paramInt, MediaPlayer.class.getName());
    mWakeLock.setReferenceCounted(false);
    if (i != 0) {
      mWakeLock.acquire();
    }
  }
  
  public void start()
    throws IllegalStateException
  {
    final int i = getStartDelayMs();
    if (i == 0) {
      startImpl();
    } else {
      new Thread()
      {
        public void run()
        {
          try
          {
            Thread.sleep(i);
          }
          catch (InterruptedException localInterruptedException)
          {
            localInterruptedException.printStackTrace();
          }
          baseSetStartDelayMs(0);
          try
          {
            MediaPlayer.this.startImpl();
          }
          catch (IllegalStateException localIllegalStateException) {}
        }
      }.start();
    }
  }
  
  public void stop()
    throws IllegalStateException
  {
    stayAwake(false);
    _stop();
    baseStop();
  }
  
  public static final class DrmInfo
  {
    private Map<UUID, byte[]> mapPssh;
    private UUID[] supportedSchemes;
    
    private DrmInfo(Parcel paramParcel)
    {
      Object localObject = new StringBuilder();
      ((StringBuilder)localObject).append("DrmInfo(");
      ((StringBuilder)localObject).append(paramParcel);
      ((StringBuilder)localObject).append(") size ");
      ((StringBuilder)localObject).append(paramParcel.dataSize());
      Log.v("MediaPlayer", ((StringBuilder)localObject).toString());
      int i = paramParcel.readInt();
      byte[] arrayOfByte = new byte[i];
      paramParcel.readByteArray(arrayOfByte);
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("DrmInfo() PSSH: ");
      ((StringBuilder)localObject).append(arrToHex(arrayOfByte));
      Log.v("MediaPlayer", ((StringBuilder)localObject).toString());
      mapPssh = parsePSSH(arrayOfByte, i);
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("DrmInfo() PSSH: ");
      ((StringBuilder)localObject).append(mapPssh);
      Log.v("MediaPlayer", ((StringBuilder)localObject).toString());
      int j = paramParcel.readInt();
      supportedSchemes = new UUID[j];
      for (int k = 0; k < j; k++)
      {
        localObject = new byte[16];
        paramParcel.readByteArray((byte[])localObject);
        supportedSchemes[k] = bytesToUUID((byte[])localObject);
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("DrmInfo() supportedScheme[");
        ((StringBuilder)localObject).append(k);
        ((StringBuilder)localObject).append("]: ");
        ((StringBuilder)localObject).append(supportedSchemes[k]);
        Log.v("MediaPlayer", ((StringBuilder)localObject).toString());
      }
      paramParcel = new StringBuilder();
      paramParcel.append("DrmInfo() Parcel psshsize: ");
      paramParcel.append(i);
      paramParcel.append(" supportedDRMsCount: ");
      paramParcel.append(j);
      Log.v("MediaPlayer", paramParcel.toString());
    }
    
    private DrmInfo(Map<UUID, byte[]> paramMap, UUID[] paramArrayOfUUID)
    {
      mapPssh = paramMap;
      supportedSchemes = paramArrayOfUUID;
    }
    
    private String arrToHex(byte[] paramArrayOfByte)
    {
      String str = "0x";
      for (int i = 0; i < paramArrayOfByte.length; i++)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append(str);
        localStringBuilder.append(String.format("%02x", new Object[] { Byte.valueOf(paramArrayOfByte[i]) }));
        str = localStringBuilder.toString();
      }
      return str;
    }
    
    private UUID bytesToUUID(byte[] paramArrayOfByte)
    {
      long l1 = 0L;
      long l2 = 0L;
      for (int i = 0; i < 8; i++)
      {
        l1 |= (paramArrayOfByte[i] & 0xFF) << (7 - i) * 8;
        l2 |= (paramArrayOfByte[(i + 8)] & 0xFF) << 8 * (7 - i);
      }
      return new UUID(l1, l2);
    }
    
    private DrmInfo makeCopy()
    {
      return new DrmInfo(mapPssh, supportedSchemes);
    }
    
    private Map<UUID, byte[]> parsePSSH(byte[] paramArrayOfByte, int paramInt)
    {
      HashMap localHashMap = new HashMap();
      int i = 0;
      int j = paramInt;
      int k = 0;
      while (j > 0)
      {
        if (j < 16)
        {
          Log.w("MediaPlayer", String.format("parsePSSH: len is too short to parse UUID: (%d < 16) pssh: %d", new Object[] { Integer.valueOf(j), Integer.valueOf(paramInt) }));
          return null;
        }
        UUID localUUID = bytesToUUID(Arrays.copyOfRange(paramArrayOfByte, k, k + 16));
        k += 16;
        int m = j - 16;
        if (m < 4)
        {
          Log.w("MediaPlayer", String.format("parsePSSH: len is too short to parse datalen: (%d < 4) pssh: %d", new Object[] { Integer.valueOf(m), Integer.valueOf(paramInt) }));
          return null;
        }
        byte[] arrayOfByte = Arrays.copyOfRange(paramArrayOfByte, k, k + 4);
        if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
          j = (arrayOfByte[3] & 0xFF) << 24 | (arrayOfByte[2] & 0xFF) << 16 | (arrayOfByte[1] & 0xFF) << 8 | arrayOfByte[0] & 0xFF;
        } else {
          j = (arrayOfByte[0] & 0xFF) << 24 | (arrayOfByte[1] & 0xFF) << 16 | (arrayOfByte[2] & 0xFF) << 8 | arrayOfByte[3] & 0xFF;
        }
        k += 4;
        m -= 4;
        if (m < j)
        {
          Log.w("MediaPlayer", String.format("parsePSSH: len is too short to parse data: (%d < %d) pssh: %d", new Object[] { Integer.valueOf(m), Integer.valueOf(j), Integer.valueOf(paramInt) }));
          return null;
        }
        arrayOfByte = Arrays.copyOfRange(paramArrayOfByte, k, k + j);
        k += j;
        j = m - j;
        Log.v("MediaPlayer", String.format("parsePSSH[%d]: <%s, %s> pssh: %d", new Object[] { Integer.valueOf(i), localUUID, arrToHex(arrayOfByte), Integer.valueOf(paramInt) }));
        i++;
        localHashMap.put(localUUID, arrayOfByte);
      }
      return localHashMap;
    }
    
    public Map<UUID, byte[]> getPssh()
    {
      return mapPssh;
    }
    
    public UUID[] getSupportedSchemes()
    {
      return supportedSchemes;
    }
  }
  
  private class EventHandler
    extends Handler
  {
    private MediaPlayer mMediaPlayer;
    
    public EventHandler(MediaPlayer paramMediaPlayer, Looper paramLooper)
    {
      super();
      mMediaPlayer = paramMediaPlayer;
    }
    
    public void handleMessage(Message arg1)
    {
      if (mMediaPlayer.mNativeContext == 0L)
      {
        Log.w("MediaPlayer", "mediaplayer went away with unhandled events");
        return;
      }
      int i = what;
      Object localObject2;
      if (i != 10000)
      {
        boolean bool1 = true;
        boolean bool2 = true;
        switch (i)
        {
        default: 
          Object localObject4;
          switch (i)
          {
          default: 
            Object localObject1;
            switch (i)
            {
            default: 
              switch (i)
              {
              default: 
                localObject1 = new StringBuilder();
                ((StringBuilder)localObject1).append("Unknown message type ");
                ((StringBuilder)localObject1).append(what);
                Log.e("MediaPlayer", ((StringBuilder)localObject1).toString());
                return;
              case 211: 
                try
                {
                  localObject1 = mOnMediaTimeDiscontinuityListener;
                  localObject4 = mOnMediaTimeDiscontinuityHandler;
                  if (localObject1 == null) {
                    return;
                  }
                  if ((obj instanceof Parcel))
                  {
                    ??? = (Parcel)obj;
                    ???.setDataPosition(0);
                    long l1 = ???.readLong();
                    long l2 = ???.readLong();
                    float f = ???.readFloat();
                    ???.recycle();
                    if ((l1 != -1L) && (l2 != -1L)) {
                      ??? = new MediaTimestamp(l1, l2 * 1000L, f);
                    } else {
                      ??? = MediaTimestamp.TIMESTAMP_UNKNOWN;
                    }
                    if (localObject4 == null) {
                      ((MediaPlayer.OnMediaTimeDiscontinuityListener)localObject1).onMediaTimeDiscontinuity(mMediaPlayer, ???);
                    } else {
                      ((Handler)localObject4).post(new Runnable()
                      {
                        public void run()
                        {
                          val$mediaTimeListener.onMediaTimeDiscontinuity(MediaPlayer.this, paramMessage);
                        }
                      });
                    }
                  }
                  return;
                }
                finally {}
              }
              localObject1 = new StringBuilder();
              ((StringBuilder)localObject1).append("MEDIA_DRM_INFO ");
              ((StringBuilder)localObject1).append(mOnDrmInfoHandlerDelegate);
              Log.v("MediaPlayer", ((StringBuilder)localObject1).toString());
              if (obj == null)
              {
                Log.w("MediaPlayer", "MEDIA_DRM_INFO msg.obj=NULL");
              }
              else
              {
                if ((obj instanceof Parcel))
                {
                  localObject1 = null;
                  localObject4 = mDrmLock;
                  ??? = (Message)localObject1;
                  try
                  {
                    if (mOnDrmInfoHandlerDelegate != null)
                    {
                      ??? = (Message)localObject1;
                      if (mDrmInfo != null) {
                        ??? = mDrmInfo.makeCopy();
                      }
                    }
                    localObject1 = mOnDrmInfoHandlerDelegate;
                    if (localObject1 != null) {
                      ((MediaPlayer.OnDrmInfoHandlerDelegate)localObject1).notifyClient(???);
                    }
                  }
                  finally {}
                }
                localObject1 = new StringBuilder();
                ((StringBuilder)localObject1).append("MEDIA_DRM_INFO msg.obj of unexpected type ");
                ((StringBuilder)localObject1).append(obj);
                Log.w("MediaPlayer", ((StringBuilder)localObject1).toString());
              }
              return;
            case 202: 
              localObject1 = mOnTimedMetaDataAvailableListener;
              if (localObject1 == null) {
                return;
              }
              if ((obj instanceof Parcel))
              {
                ??? = (Parcel)obj;
                localObject4 = TimedMetaData.createTimedMetaDataFromParcel(???);
                ???.recycle();
                ((MediaPlayer.OnTimedMetaDataAvailableListener)localObject1).onTimedMetaDataAvailable(mMediaPlayer, (TimedMetaData)localObject4);
              }
              return;
            case 201: 
              try
              {
                if (mSubtitleDataListenerDisabled) {
                  return;
                }
                localObject4 = mExtSubtitleDataListener;
                localObject1 = mExtSubtitleDataHandler;
                if ((obj instanceof Parcel))
                {
                  Parcel localParcel = (Parcel)obj;
                  ??? = new SubtitleData(localParcel);
                  localParcel.recycle();
                  mIntSubtitleDataListener.onSubtitleData(mMediaPlayer, ???);
                  if (localObject4 != null) {
                    if (localObject1 == null) {
                      ((MediaPlayer.OnSubtitleDataListener)localObject4).onSubtitleData(mMediaPlayer, ???);
                    } else {
                      ((Handler)localObject1).post(new Runnable()
                      {
                        public void run()
                        {
                          val$extSubtitleListener.onSubtitleData(MediaPlayer.this, paramMessage);
                        }
                      });
                    }
                  }
                }
                return;
              }
              finally {}
            }
            i = arg1;
            switch (i)
            {
            default: 
              switch (i)
              {
              default: 
                break;
              case 802: 
                try
                {
                  MediaPlayer.this.scanInternalSubtitleTracks();
                }
                catch (RuntimeException localRuntimeException)
                {
                  sendMessage(obtainMessage(100, 1, 64526, null));
                }
              }
              arg1 = 802;
              if (mSubtitleController != null) {
                mSubtitleController.selectDefaultTrack();
              }
              break;
            case 701: 
            case 702: 
              localObject2 = mTimeProvider;
              if (localObject2 != null)
              {
                if (arg1 != 701) {
                  bool2 = false;
                }
                ((MediaPlayer.TimeProvider)localObject2).onBuffering(bool2);
              }
              break;
            case 700: 
              localObject2 = new StringBuilder();
              ((StringBuilder)localObject2).append("Info (");
              ((StringBuilder)localObject2).append(arg1);
              ((StringBuilder)localObject2).append(",");
              ((StringBuilder)localObject2).append(arg2);
              ((StringBuilder)localObject2).append(")");
              Log.i("MediaPlayer", ((StringBuilder)localObject2).toString());
            }
            localObject2 = mOnInfoListener;
            if (localObject2 != null) {
              ((MediaPlayer.OnInfoListener)localObject2).onInfo(mMediaPlayer, arg1, arg2);
            }
            return;
          case 100: 
            localObject2 = new StringBuilder();
            ((StringBuilder)localObject2).append("Error (");
            ((StringBuilder)localObject2).append(arg1);
            ((StringBuilder)localObject2).append(",");
            ((StringBuilder)localObject2).append(arg2);
            ((StringBuilder)localObject2).append(")");
            Log.e("MediaPlayer", ((StringBuilder)localObject2).toString());
            bool2 = false;
            localObject2 = mOnErrorListener;
            if (localObject2 != null) {
              bool2 = ((MediaPlayer.OnErrorListener)localObject2).onError(mMediaPlayer, arg1, arg2);
            }
            mOnCompletionInternalListener.onCompletion(mMediaPlayer);
            ??? = mOnCompletionListener;
            if ((??? != null) && (!bool2)) {
              ???.onCompletion(mMediaPlayer);
            }
            MediaPlayer.this.stayAwake(false);
            return;
          case 99: 
            localObject2 = mOnTimedTextListener;
            if (localObject2 == null) {
              return;
            }
            if (obj == null)
            {
              ((MediaPlayer.OnTimedTextListener)localObject2).onTimedText(mMediaPlayer, null);
            }
            else if ((obj instanceof Parcel))
            {
              ??? = (Parcel)obj;
              localObject4 = new TimedText(???);
              ???.recycle();
              ((MediaPlayer.OnTimedTextListener)localObject2).onTimedText(mMediaPlayer, (TimedText)localObject4);
            }
            return;
          }
          ??? = mTimeProvider;
          if (??? != null) {
            ???.onNotifyTime();
          }
          return;
        case 8: 
          ??? = mTimeProvider;
          if (??? != null) {
            ???.onStopped();
          }
          break;
        case 6: 
        case 7: 
          localObject2 = mTimeProvider;
          if (localObject2 != null)
          {
            if (what == 7) {
              bool2 = bool1;
            } else {
              bool2 = false;
            }
            ((MediaPlayer.TimeProvider)localObject2).onPaused(bool2);
          }
          break;
        case 5: 
          localObject2 = mOnVideoSizeChangedListener;
          if (localObject2 != null) {
            ((MediaPlayer.OnVideoSizeChangedListener)localObject2).onVideoSizeChanged(mMediaPlayer, arg1, arg2);
          }
          return;
        case 4: 
          ??? = mOnSeekCompleteListener;
          if (??? != null) {
            ???.onSeekComplete(mMediaPlayer);
          }
        case 9: 
          ??? = mTimeProvider;
          if (??? != null) {
            ???.onSeekComplete(mMediaPlayer);
          }
          return;
        case 3: 
          localObject2 = mOnBufferingUpdateListener;
          if (localObject2 != null) {
            ((MediaPlayer.OnBufferingUpdateListener)localObject2).onBufferingUpdate(mMediaPlayer, arg1);
          }
          return;
        case 2: 
          mOnCompletionInternalListener.onCompletion(mMediaPlayer);
          ??? = mOnCompletionListener;
          if (??? != null) {
            ???.onCompletion(mMediaPlayer);
          }
          MediaPlayer.this.stayAwake(false);
          return;
        case 1: 
          try
          {
            MediaPlayer.this.scanInternalSubtitleTracks();
          }
          catch (RuntimeException ???)
          {
            sendMessage(obtainMessage(100, 1, 64526, null));
          }
          ??? = mOnPreparedListener;
          if (??? != null) {
            ???.onPrepared(mMediaPlayer);
          }
          return;
        }
        return;
      }
      AudioManager.resetAudioPortGeneration();
      synchronized (mRoutingChangeListeners)
      {
        localObject2 = mRoutingChangeListeners.values().iterator();
        while (((Iterator)localObject2).hasNext()) {
          ((NativeRoutingEventHandlerDelegate)((Iterator)localObject2).next()).notifyClient();
        }
        return;
      }
    }
  }
  
  public static final class MetricsConstants
  {
    public static final String CODEC_AUDIO = "android.media.mediaplayer.audio.codec";
    public static final String CODEC_VIDEO = "android.media.mediaplayer.video.codec";
    public static final String DURATION = "android.media.mediaplayer.durationMs";
    public static final String ERRORS = "android.media.mediaplayer.err";
    public static final String ERROR_CODE = "android.media.mediaplayer.errcode";
    public static final String FRAMES = "android.media.mediaplayer.frames";
    public static final String FRAMES_DROPPED = "android.media.mediaplayer.dropped";
    public static final String HEIGHT = "android.media.mediaplayer.height";
    public static final String MIME_TYPE_AUDIO = "android.media.mediaplayer.audio.mime";
    public static final String MIME_TYPE_VIDEO = "android.media.mediaplayer.video.mime";
    public static final String PLAYING = "android.media.mediaplayer.playingMs";
    public static final String WIDTH = "android.media.mediaplayer.width";
    
    private MetricsConstants() {}
  }
  
  public static final class NoDrmSchemeException
    extends MediaDrmException
  {
    public NoDrmSchemeException(String paramString)
    {
      super();
    }
  }
  
  public static abstract interface OnBufferingUpdateListener
  {
    public abstract void onBufferingUpdate(MediaPlayer paramMediaPlayer, int paramInt);
  }
  
  public static abstract interface OnCompletionListener
  {
    public abstract void onCompletion(MediaPlayer paramMediaPlayer);
  }
  
  public static abstract interface OnDrmConfigHelper
  {
    public abstract void onDrmConfig(MediaPlayer paramMediaPlayer);
  }
  
  private class OnDrmInfoHandlerDelegate
  {
    private Handler mHandler;
    private MediaPlayer mMediaPlayer;
    private MediaPlayer.OnDrmInfoListener mOnDrmInfoListener;
    
    OnDrmInfoHandlerDelegate(MediaPlayer paramMediaPlayer, MediaPlayer.OnDrmInfoListener paramOnDrmInfoListener, Handler paramHandler)
    {
      mMediaPlayer = paramMediaPlayer;
      mOnDrmInfoListener = paramOnDrmInfoListener;
      if (paramHandler != null) {
        mHandler = paramHandler;
      }
    }
    
    void notifyClient(final MediaPlayer.DrmInfo paramDrmInfo)
    {
      if (mHandler != null) {
        mHandler.post(new Runnable()
        {
          public void run()
          {
            mOnDrmInfoListener.onDrmInfo(MediaPlayer.this, paramDrmInfo);
          }
        });
      } else {
        mOnDrmInfoListener.onDrmInfo(mMediaPlayer, paramDrmInfo);
      }
    }
  }
  
  public static abstract interface OnDrmInfoListener
  {
    public abstract void onDrmInfo(MediaPlayer paramMediaPlayer, MediaPlayer.DrmInfo paramDrmInfo);
  }
  
  private class OnDrmPreparedHandlerDelegate
  {
    private Handler mHandler;
    private MediaPlayer mMediaPlayer;
    private MediaPlayer.OnDrmPreparedListener mOnDrmPreparedListener;
    
    OnDrmPreparedHandlerDelegate(MediaPlayer paramMediaPlayer, MediaPlayer.OnDrmPreparedListener paramOnDrmPreparedListener, Handler paramHandler)
    {
      mMediaPlayer = paramMediaPlayer;
      mOnDrmPreparedListener = paramOnDrmPreparedListener;
      if (paramHandler != null) {
        mHandler = paramHandler;
      } else if (mEventHandler != null) {
        mHandler = mEventHandler;
      } else {
        Log.e("MediaPlayer", "OnDrmPreparedHandlerDelegate: Unexpected null mEventHandler");
      }
    }
    
    void notifyClient(final int paramInt)
    {
      if (mHandler != null) {
        mHandler.post(new Runnable()
        {
          public void run()
          {
            mOnDrmPreparedListener.onDrmPrepared(MediaPlayer.this, paramInt);
          }
        });
      } else {
        Log.e("MediaPlayer", "OnDrmPreparedHandlerDelegate:notifyClient: Unexpected null mHandler");
      }
    }
  }
  
  public static abstract interface OnDrmPreparedListener
  {
    public abstract void onDrmPrepared(MediaPlayer paramMediaPlayer, int paramInt);
  }
  
  public static abstract interface OnErrorListener
  {
    public abstract boolean onError(MediaPlayer paramMediaPlayer, int paramInt1, int paramInt2);
  }
  
  public static abstract interface OnInfoListener
  {
    public abstract boolean onInfo(MediaPlayer paramMediaPlayer, int paramInt1, int paramInt2);
  }
  
  public static abstract interface OnMediaTimeDiscontinuityListener
  {
    public abstract void onMediaTimeDiscontinuity(MediaPlayer paramMediaPlayer, MediaTimestamp paramMediaTimestamp);
  }
  
  public static abstract interface OnPreparedListener
  {
    public abstract void onPrepared(MediaPlayer paramMediaPlayer);
  }
  
  public static abstract interface OnSeekCompleteListener
  {
    public abstract void onSeekComplete(MediaPlayer paramMediaPlayer);
  }
  
  public static abstract interface OnSubtitleDataListener
  {
    public abstract void onSubtitleData(MediaPlayer paramMediaPlayer, SubtitleData paramSubtitleData);
  }
  
  public static abstract interface OnTimedMetaDataAvailableListener
  {
    public abstract void onTimedMetaDataAvailable(MediaPlayer paramMediaPlayer, TimedMetaData paramTimedMetaData);
  }
  
  public static abstract interface OnTimedTextListener
  {
    public abstract void onTimedText(MediaPlayer paramMediaPlayer, TimedText paramTimedText);
  }
  
  public static abstract interface OnVideoSizeChangedListener
  {
    public abstract void onVideoSizeChanged(MediaPlayer paramMediaPlayer, int paramInt1, int paramInt2);
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface PlaybackRateAudioMode {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface PrepareDrmStatusCode {}
  
  public static final class ProvisioningNetworkErrorException
    extends MediaDrmException
  {
    public ProvisioningNetworkErrorException(String paramString)
    {
      super();
    }
  }
  
  public static final class ProvisioningServerErrorException
    extends MediaDrmException
  {
    public ProvisioningServerErrorException(String paramString)
    {
      super();
    }
  }
  
  private class ProvisioningThread
    extends Thread
  {
    public static final int TIMEOUT_MS = 60000;
    private Object drmLock;
    private boolean finished;
    private MediaPlayer mediaPlayer;
    private MediaPlayer.OnDrmPreparedHandlerDelegate onDrmPreparedHandlerDelegate;
    private int status;
    private String urlStr;
    private UUID uuid;
    
    private ProvisioningThread() {}
    
    public ProvisioningThread initialize(MediaDrm.ProvisionRequest paramProvisionRequest, UUID paramUUID, MediaPlayer paramMediaPlayer)
    {
      drmLock = mDrmLock;
      onDrmPreparedHandlerDelegate = mOnDrmPreparedHandlerDelegate;
      mediaPlayer = paramMediaPlayer;
      paramMediaPlayer = new StringBuilder();
      paramMediaPlayer.append(paramProvisionRequest.getDefaultUrl());
      paramMediaPlayer.append("&signedRequest=");
      paramMediaPlayer.append(new String(paramProvisionRequest.getData()));
      urlStr = paramMediaPlayer.toString();
      uuid = paramUUID;
      status = 3;
      paramProvisionRequest = new StringBuilder();
      paramProvisionRequest.append("HandleProvisioninig: Thread is initialised url: ");
      paramProvisionRequest.append(urlStr);
      Log.v("MediaPlayer", paramProvisionRequest.toString());
      return this;
    }
    
    /* Error */
    public void run()
    {
      // Byte code:
      //   0: aconst_null
      //   1: astore_1
      //   2: aconst_null
      //   3: astore_2
      //   4: aconst_null
      //   5: astore_3
      //   6: iconst_0
      //   7: istore 4
      //   9: aload_2
      //   10: astore 5
      //   12: new 99	java/net/URL
      //   15: astore 6
      //   17: aload_2
      //   18: astore 5
      //   20: aload 6
      //   22: aload_0
      //   23: getfield 80	android/media/MediaPlayer$ProvisioningThread:urlStr	Ljava/lang/String;
      //   26: invokespecial 102	java/net/URL:<init>	(Ljava/lang/String;)V
      //   29: aload_2
      //   30: astore 5
      //   32: aload 6
      //   34: invokevirtual 106	java/net/URL:openConnection	()Ljava/net/URLConnection;
      //   37: checkcast 108	java/net/HttpURLConnection
      //   40: astore 7
      //   42: aload_3
      //   43: astore 8
      //   45: aload_1
      //   46: astore_2
      //   47: aload 7
      //   49: ldc 110
      //   51: invokevirtual 113	java/net/HttpURLConnection:setRequestMethod	(Ljava/lang/String;)V
      //   54: aload_3
      //   55: astore 8
      //   57: aload_1
      //   58: astore_2
      //   59: aload 7
      //   61: iconst_0
      //   62: invokevirtual 117	java/net/HttpURLConnection:setDoOutput	(Z)V
      //   65: aload_3
      //   66: astore 8
      //   68: aload_1
      //   69: astore_2
      //   70: aload 7
      //   72: iconst_1
      //   73: invokevirtual 120	java/net/HttpURLConnection:setDoInput	(Z)V
      //   76: aload_3
      //   77: astore 8
      //   79: aload_1
      //   80: astore_2
      //   81: aload 7
      //   83: ldc 10
      //   85: invokevirtual 124	java/net/HttpURLConnection:setConnectTimeout	(I)V
      //   88: aload_3
      //   89: astore 8
      //   91: aload_1
      //   92: astore_2
      //   93: aload 7
      //   95: ldc 10
      //   97: invokevirtual 127	java/net/HttpURLConnection:setReadTimeout	(I)V
      //   100: aload_3
      //   101: astore 8
      //   103: aload_1
      //   104: astore_2
      //   105: aload 7
      //   107: invokevirtual 130	java/net/HttpURLConnection:connect	()V
      //   110: aload_3
      //   111: astore 8
      //   113: aload_1
      //   114: astore_2
      //   115: aload 7
      //   117: invokevirtual 134	java/net/HttpURLConnection:getInputStream	()Ljava/io/InputStream;
      //   120: invokestatic 140	libcore/io/Streams:readFully	(Ljava/io/InputStream;)[B
      //   123: astore_3
      //   124: aload_3
      //   125: astore 8
      //   127: aload_3
      //   128: astore_2
      //   129: new 53	java/lang/StringBuilder
      //   132: astore 5
      //   134: aload_3
      //   135: astore 8
      //   137: aload_3
      //   138: astore_2
      //   139: aload 5
      //   141: invokespecial 54	java/lang/StringBuilder:<init>	()V
      //   144: aload_3
      //   145: astore 8
      //   147: aload_3
      //   148: astore_2
      //   149: aload 5
      //   151: ldc -114
      //   153: invokevirtual 64	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   156: pop
      //   157: aload_3
      //   158: astore 8
      //   160: aload_3
      //   161: astore_2
      //   162: aload 5
      //   164: aload_3
      //   165: arraylength
      //   166: invokevirtual 145	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
      //   169: pop
      //   170: aload_3
      //   171: astore 8
      //   173: aload_3
      //   174: astore_2
      //   175: aload 5
      //   177: ldc -109
      //   179: invokevirtual 64	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   182: pop
      //   183: aload_3
      //   184: astore 8
      //   186: aload_3
      //   187: astore_2
      //   188: aload 5
      //   190: aload_3
      //   191: invokevirtual 150	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
      //   194: pop
      //   195: aload_3
      //   196: astore 8
      //   198: aload_3
      //   199: astore_2
      //   200: ldc 88
      //   202: aload 5
      //   204: invokevirtual 78	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   207: invokestatic 94	android/util/Log:v	(Ljava/lang/String;Ljava/lang/String;)I
      //   210: pop
      //   211: aload_3
      //   212: astore 5
      //   214: aload 7
      //   216: invokevirtual 153	java/net/HttpURLConnection:disconnect	()V
      //   219: aload_3
      //   220: astore_2
      //   221: goto +97 -> 318
      //   224: astore_2
      //   225: goto +99 -> 324
      //   228: astore_3
      //   229: aload_2
      //   230: astore 8
      //   232: aload_0
      //   233: iconst_1
      //   234: putfield 84	android/media/MediaPlayer$ProvisioningThread:status	I
      //   237: aload_2
      //   238: astore 8
      //   240: new 53	java/lang/StringBuilder
      //   243: astore 5
      //   245: aload_2
      //   246: astore 8
      //   248: aload 5
      //   250: invokespecial 54	java/lang/StringBuilder:<init>	()V
      //   253: aload_2
      //   254: astore 8
      //   256: aload 5
      //   258: ldc -101
      //   260: invokevirtual 64	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   263: pop
      //   264: aload_2
      //   265: astore 8
      //   267: aload 5
      //   269: aload_3
      //   270: invokevirtual 150	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
      //   273: pop
      //   274: aload_2
      //   275: astore 8
      //   277: aload 5
      //   279: ldc -99
      //   281: invokevirtual 64	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   284: pop
      //   285: aload_2
      //   286: astore 8
      //   288: aload 5
      //   290: aload 6
      //   292: invokevirtual 150	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
      //   295: pop
      //   296: aload_2
      //   297: astore 8
      //   299: ldc 88
      //   301: aload 5
      //   303: invokevirtual 78	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   306: invokestatic 160	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
      //   309: pop
      //   310: aload_2
      //   311: astore 5
      //   313: aload 7
      //   315: invokevirtual 153	java/net/HttpURLConnection:disconnect	()V
      //   318: aload_2
      //   319: astore 5
      //   321: goto +59 -> 380
      //   324: aload 8
      //   326: astore 5
      //   328: aload 7
      //   330: invokevirtual 153	java/net/HttpURLConnection:disconnect	()V
      //   333: aload 8
      //   335: astore 5
      //   337: aload_2
      //   338: athrow
      //   339: astore_2
      //   340: aload_0
      //   341: iconst_1
      //   342: putfield 84	android/media/MediaPlayer$ProvisioningThread:status	I
      //   345: new 53	java/lang/StringBuilder
      //   348: dup
      //   349: invokespecial 54	java/lang/StringBuilder:<init>	()V
      //   352: astore 8
      //   354: aload 8
      //   356: ldc -94
      //   358: invokevirtual 64	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   361: pop
      //   362: aload 8
      //   364: aload_2
      //   365: invokevirtual 150	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
      //   368: pop
      //   369: ldc 88
      //   371: aload 8
      //   373: invokevirtual 78	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   376: invokestatic 160	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
      //   379: pop
      //   380: iload 4
      //   382: istore 9
      //   384: aload 5
      //   386: ifnull +74 -> 460
      //   389: aload_0
      //   390: getfield 28	android/media/MediaPlayer$ProvisioningThread:this$0	Landroid/media/MediaPlayer;
      //   393: invokestatic 166	android/media/MediaPlayer:access$3800	(Landroid/media/MediaPlayer;)Landroid/media/MediaDrm;
      //   396: aload 5
      //   398: invokevirtual 171	android/media/MediaDrm:provideProvisionResponse	([B)V
      //   401: ldc 88
      //   403: ldc -83
      //   405: invokestatic 94	android/util/Log:v	(Ljava/lang/String;Ljava/lang/String;)I
      //   408: pop
      //   409: iconst_1
      //   410: istore 9
      //   412: goto +48 -> 460
      //   415: astore_2
      //   416: aload_0
      //   417: iconst_2
      //   418: putfield 84	android/media/MediaPlayer$ProvisioningThread:status	I
      //   421: new 53	java/lang/StringBuilder
      //   424: dup
      //   425: invokespecial 54	java/lang/StringBuilder:<init>	()V
      //   428: astore 5
      //   430: aload 5
      //   432: ldc -81
      //   434: invokevirtual 64	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   437: pop
      //   438: aload 5
      //   440: aload_2
      //   441: invokevirtual 150	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
      //   444: pop
      //   445: ldc 88
      //   447: aload 5
      //   449: invokevirtual 78	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   452: invokestatic 160	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
      //   455: pop
      //   456: iload 4
      //   458: istore 9
      //   460: iconst_0
      //   461: istore 10
      //   463: iconst_0
      //   464: istore 11
      //   466: aload_0
      //   467: getfield 49	android/media/MediaPlayer$ProvisioningThread:onDrmPreparedHandlerDelegate	Landroid/media/MediaPlayer$OnDrmPreparedHandlerDelegate;
      //   470: astore 5
      //   472: iconst_3
      //   473: istore 4
      //   475: aload 5
      //   477: ifnull +103 -> 580
      //   480: aload_0
      //   481: getfield 43	android/media/MediaPlayer$ProvisioningThread:drmLock	Ljava/lang/Object;
      //   484: astore 5
      //   486: aload 5
      //   488: monitorenter
      //   489: iload 9
      //   491: ifeq +37 -> 528
      //   494: aload_0
      //   495: getfield 51	android/media/MediaPlayer$ProvisioningThread:mediaPlayer	Landroid/media/MediaPlayer;
      //   498: aload_0
      //   499: getfield 82	android/media/MediaPlayer$ProvisioningThread:uuid	Ljava/util/UUID;
      //   502: invokestatic 179	android/media/MediaPlayer:access$3900	(Landroid/media/MediaPlayer;Ljava/util/UUID;)Z
      //   505: istore 11
      //   507: iload 11
      //   509: ifeq +6 -> 515
      //   512: iconst_0
      //   513: istore 4
      //   515: aload_0
      //   516: iload 4
      //   518: putfield 84	android/media/MediaPlayer$ProvisioningThread:status	I
      //   521: goto +7 -> 528
      //   524: astore_2
      //   525: goto +50 -> 575
      //   528: aload_0
      //   529: getfield 51	android/media/MediaPlayer$ProvisioningThread:mediaPlayer	Landroid/media/MediaPlayer;
      //   532: iconst_0
      //   533: invokestatic 183	android/media/MediaPlayer:access$4002	(Landroid/media/MediaPlayer;Z)Z
      //   536: pop
      //   537: aload_0
      //   538: getfield 51	android/media/MediaPlayer$ProvisioningThread:mediaPlayer	Landroid/media/MediaPlayer;
      //   541: iconst_0
      //   542: invokestatic 186	android/media/MediaPlayer:access$4102	(Landroid/media/MediaPlayer;Z)Z
      //   545: pop
      //   546: iload 11
      //   548: ifne +10 -> 558
      //   551: aload_0
      //   552: getfield 28	android/media/MediaPlayer$ProvisioningThread:this$0	Landroid/media/MediaPlayer;
      //   555: invokestatic 189	android/media/MediaPlayer:access$4200	(Landroid/media/MediaPlayer;)V
      //   558: aload 5
      //   560: monitorexit
      //   561: aload_0
      //   562: getfield 49	android/media/MediaPlayer$ProvisioningThread:onDrmPreparedHandlerDelegate	Landroid/media/MediaPlayer$OnDrmPreparedHandlerDelegate;
      //   565: aload_0
      //   566: getfield 84	android/media/MediaPlayer$ProvisioningThread:status	I
      //   569: invokevirtual 194	android/media/MediaPlayer$OnDrmPreparedHandlerDelegate:notifyClient	(I)V
      //   572: goto +74 -> 646
      //   575: aload 5
      //   577: monitorexit
      //   578: aload_2
      //   579: athrow
      //   580: iload 10
      //   582: istore 11
      //   584: iload 9
      //   586: ifeq +30 -> 616
      //   589: aload_0
      //   590: getfield 51	android/media/MediaPlayer$ProvisioningThread:mediaPlayer	Landroid/media/MediaPlayer;
      //   593: aload_0
      //   594: getfield 82	android/media/MediaPlayer$ProvisioningThread:uuid	Ljava/util/UUID;
      //   597: invokestatic 179	android/media/MediaPlayer:access$3900	(Landroid/media/MediaPlayer;Ljava/util/UUID;)Z
      //   600: istore 11
      //   602: iload 11
      //   604: ifeq +6 -> 610
      //   607: iconst_0
      //   608: istore 4
      //   610: aload_0
      //   611: iload 4
      //   613: putfield 84	android/media/MediaPlayer$ProvisioningThread:status	I
      //   616: aload_0
      //   617: getfield 51	android/media/MediaPlayer$ProvisioningThread:mediaPlayer	Landroid/media/MediaPlayer;
      //   620: iconst_0
      //   621: invokestatic 183	android/media/MediaPlayer:access$4002	(Landroid/media/MediaPlayer;Z)Z
      //   624: pop
      //   625: aload_0
      //   626: getfield 51	android/media/MediaPlayer$ProvisioningThread:mediaPlayer	Landroid/media/MediaPlayer;
      //   629: iconst_0
      //   630: invokestatic 186	android/media/MediaPlayer:access$4102	(Landroid/media/MediaPlayer;Z)Z
      //   633: pop
      //   634: iload 11
      //   636: ifne +10 -> 646
      //   639: aload_0
      //   640: getfield 28	android/media/MediaPlayer$ProvisioningThread:this$0	Landroid/media/MediaPlayer;
      //   643: invokestatic 189	android/media/MediaPlayer:access$4200	(Landroid/media/MediaPlayer;)V
      //   646: aload_0
      //   647: iconst_1
      //   648: putfield 196	android/media/MediaPlayer$ProvisioningThread:finished	Z
      //   651: return
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	652	0	this	ProvisioningThread
      //   1	113	1	localObject1	Object
      //   3	218	2	localObject2	Object
      //   224	114	2	localObject3	Object
      //   339	26	2	localException1	Exception
      //   415	26	2	localException2	Exception
      //   524	55	2	localObject4	Object
      //   5	215	3	arrayOfByte	byte[]
      //   228	42	3	localException3	Exception
      //   7	605	4	i	int
      //   10	566	5	localObject5	Object
      //   15	276	6	localURL	java.net.URL
      //   40	289	7	localHttpURLConnection	java.net.HttpURLConnection
      //   43	329	8	localObject6	Object
      //   382	203	9	j	int
      //   461	120	10	bool1	boolean
      //   464	171	11	bool2	boolean
      // Exception table:
      //   from	to	target	type
      //   47	54	224	finally
      //   59	65	224	finally
      //   70	76	224	finally
      //   81	88	224	finally
      //   93	100	224	finally
      //   105	110	224	finally
      //   115	124	224	finally
      //   129	134	224	finally
      //   139	144	224	finally
      //   149	157	224	finally
      //   162	170	224	finally
      //   175	183	224	finally
      //   188	195	224	finally
      //   200	211	224	finally
      //   232	237	224	finally
      //   240	245	224	finally
      //   248	253	224	finally
      //   256	264	224	finally
      //   267	274	224	finally
      //   277	285	224	finally
      //   288	296	224	finally
      //   299	310	224	finally
      //   47	54	228	java/lang/Exception
      //   59	65	228	java/lang/Exception
      //   70	76	228	java/lang/Exception
      //   81	88	228	java/lang/Exception
      //   93	100	228	java/lang/Exception
      //   105	110	228	java/lang/Exception
      //   115	124	228	java/lang/Exception
      //   129	134	228	java/lang/Exception
      //   139	144	228	java/lang/Exception
      //   149	157	228	java/lang/Exception
      //   162	170	228	java/lang/Exception
      //   175	183	228	java/lang/Exception
      //   188	195	228	java/lang/Exception
      //   200	211	228	java/lang/Exception
      //   12	17	339	java/lang/Exception
      //   20	29	339	java/lang/Exception
      //   32	42	339	java/lang/Exception
      //   214	219	339	java/lang/Exception
      //   313	318	339	java/lang/Exception
      //   328	333	339	java/lang/Exception
      //   337	339	339	java/lang/Exception
      //   389	409	415	java/lang/Exception
      //   494	507	524	finally
      //   515	521	524	finally
      //   528	546	524	finally
      //   551	558	524	finally
      //   558	561	524	finally
      //   575	578	524	finally
    }
    
    public int status()
    {
      return status;
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface SeekMode {}
  
  static class TimeProvider
    implements MediaPlayer.OnSeekCompleteListener, MediaTimeProvider
  {
    private static final long MAX_EARLY_CALLBACK_US = 1000L;
    private static final long MAX_NS_WITHOUT_POSITION_CHECK = 5000000000L;
    private static final int NOTIFY = 1;
    private static final int NOTIFY_SEEK = 3;
    private static final int NOTIFY_STOP = 2;
    private static final int NOTIFY_TIME = 0;
    private static final int NOTIFY_TRACK_DATA = 4;
    private static final String TAG = "MTP";
    private static final long TIME_ADJUSTMENT_RATE = 2L;
    public boolean DEBUG = false;
    private boolean mBuffering;
    private Handler mEventHandler;
    private HandlerThread mHandlerThread;
    private long mLastReportedTime;
    private long mLastTimeUs = 0L;
    private MediaTimeProvider.OnMediaTimeListener[] mListeners;
    private boolean mPaused = true;
    private boolean mPausing = false;
    private MediaPlayer mPlayer;
    private boolean mRefresh = false;
    private boolean mSeeking = false;
    private boolean mStopped = true;
    private long[] mTimes;
    
    public TimeProvider(MediaPlayer paramMediaPlayer)
    {
      mPlayer = paramMediaPlayer;
      try
      {
        getCurrentTimeUs(true, false);
      }
      catch (IllegalStateException paramMediaPlayer)
      {
        mRefresh = true;
      }
      Looper localLooper = Looper.myLooper();
      paramMediaPlayer = localLooper;
      if (localLooper == null)
      {
        localLooper = Looper.getMainLooper();
        paramMediaPlayer = localLooper;
        if (localLooper == null)
        {
          mHandlerThread = new HandlerThread("MediaPlayerMTPEventThread", -2);
          mHandlerThread.start();
          paramMediaPlayer = mHandlerThread.getLooper();
        }
      }
      mEventHandler = new EventHandler(paramMediaPlayer);
      mListeners = new MediaTimeProvider.OnMediaTimeListener[0];
      mTimes = new long[0];
      mLastTimeUs = 0L;
    }
    
    private void notifySeek()
    {
      try
      {
        mSeeking = false;
        try
        {
          long l = getCurrentTimeUs(true, false);
          StringBuilder localStringBuilder;
          if (DEBUG)
          {
            localStringBuilder = new java/lang/StringBuilder;
            localStringBuilder.<init>();
            localStringBuilder.append("onSeekComplete at ");
            localStringBuilder.append(l);
            Log.d("MTP", localStringBuilder.toString());
          }
          for (localStringBuilder : mListeners)
          {
            if (localStringBuilder == null) {
              break;
            }
            localStringBuilder.onSeek(l);
          }
        }
        catch (IllegalStateException localIllegalStateException)
        {
          if (DEBUG) {
            Log.d("MTP", "onSeekComplete but no player");
          }
          mPausing = true;
          notifyTimedEvent(false);
        }
        return;
      }
      finally {}
    }
    
    private void notifyStop()
    {
      try
      {
        for (MediaTimeProvider.OnMediaTimeListener localOnMediaTimeListener : mListeners)
        {
          if (localOnMediaTimeListener == null) {
            break;
          }
          localOnMediaTimeListener.onStop();
        }
        return;
      }
      finally {}
    }
    
    /* Error */
    private void notifyTimedEvent(boolean paramBoolean)
    {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: iload_1
      //   4: iconst_1
      //   5: invokevirtual 86	android/media/MediaPlayer$TimeProvider:getCurrentTimeUs	(ZZ)J
      //   8: lstore_2
      //   9: goto +27 -> 36
      //   12: astore 4
      //   14: goto +492 -> 506
      //   17: astore 4
      //   19: aload_0
      //   20: iconst_1
      //   21: putfield 74	android/media/MediaPlayer$TimeProvider:mRefresh	Z
      //   24: aload_0
      //   25: iconst_1
      //   26: putfield 76	android/media/MediaPlayer$TimeProvider:mPausing	Z
      //   29: aload_0
      //   30: iload_1
      //   31: iconst_1
      //   32: invokevirtual 86	android/media/MediaPlayer$TimeProvider:getCurrentTimeUs	(ZZ)J
      //   35: lstore_2
      //   36: lload_2
      //   37: lstore 5
      //   39: aload_0
      //   40: getfield 78	android/media/MediaPlayer$TimeProvider:mSeeking	Z
      //   43: istore_1
      //   44: iload_1
      //   45: ifeq +6 -> 51
      //   48: aload_0
      //   49: monitorexit
      //   50: return
      //   51: aload_0
      //   52: getfield 80	android/media/MediaPlayer$TimeProvider:DEBUG	Z
      //   55: istore_1
      //   56: iconst_0
      //   57: istore 7
      //   59: iload_1
      //   60: ifeq +149 -> 209
      //   63: new 147	java/lang/StringBuilder
      //   66: astore 8
      //   68: aload 8
      //   70: invokespecial 148	java/lang/StringBuilder:<init>	()V
      //   73: aload 8
      //   75: ldc -78
      //   77: invokevirtual 154	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   80: pop
      //   81: aload 8
      //   83: aload_0
      //   84: getfield 68	android/media/MediaPlayer$TimeProvider:mLastTimeUs	J
      //   87: invokevirtual 157	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
      //   90: pop
      //   91: aload 8
      //   93: ldc -76
      //   95: invokevirtual 154	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   98: pop
      //   99: aload 8
      //   101: lload_2
      //   102: invokevirtual 157	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
      //   105: pop
      //   106: aload 8
      //   108: ldc -74
      //   110: invokevirtual 154	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   113: pop
      //   114: aload_0
      //   115: getfield 121	android/media/MediaPlayer$TimeProvider:mTimes	[J
      //   118: astore 4
      //   120: aload 4
      //   122: arraylength
      //   123: istore 9
      //   125: iconst_1
      //   126: istore 10
      //   128: iconst_0
      //   129: istore 11
      //   131: iload 11
      //   133: iload 9
      //   135: if_icmpge +52 -> 187
      //   138: aload 4
      //   140: iload 11
      //   142: laload
      //   143: lstore 12
      //   145: lload 12
      //   147: ldc2_w 183
      //   150: lcmp
      //   151: ifne +6 -> 157
      //   154: goto +27 -> 181
      //   157: iload 10
      //   159: ifne +11 -> 170
      //   162: aload 8
      //   164: ldc -70
      //   166: invokevirtual 154	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   169: pop
      //   170: aload 8
      //   172: lload 12
      //   174: invokevirtual 157	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
      //   177: pop
      //   178: iconst_0
      //   179: istore 10
      //   181: iinc 11 1
      //   184: goto -53 -> 131
      //   187: aload 8
      //   189: ldc -68
      //   191: invokevirtual 154	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   194: pop
      //   195: ldc 36
      //   197: aload 8
      //   199: invokevirtual 161	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   202: invokestatic 167	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
      //   205: pop
      //   206: goto +3 -> 209
      //   209: new 190	java/util/Vector
      //   212: astore 4
      //   214: aload 4
      //   216: invokespecial 191	java/util/Vector:<init>	()V
      //   219: iload 7
      //   221: istore 11
      //   223: iload 11
      //   225: aload_0
      //   226: getfield 121	android/media/MediaPlayer$TimeProvider:mTimes	[J
      //   229: arraylength
      //   230: if_icmpge +141 -> 371
      //   233: aload_0
      //   234: getfield 119	android/media/MediaPlayer$TimeProvider:mListeners	[Landroid/media/MediaTimeProvider$OnMediaTimeListener;
      //   237: iload 11
      //   239: aaload
      //   240: ifnonnull +6 -> 246
      //   243: goto +128 -> 371
      //   246: aload_0
      //   247: getfield 121	android/media/MediaPlayer$TimeProvider:mTimes	[J
      //   250: iload 11
      //   252: laload
      //   253: ldc2_w 183
      //   256: lcmp
      //   257: ifgt +10 -> 267
      //   260: lload 5
      //   262: lstore 12
      //   264: goto +97 -> 361
      //   267: aload_0
      //   268: getfield 121	android/media/MediaPlayer$TimeProvider:mTimes	[J
      //   271: iload 11
      //   273: laload
      //   274: lload_2
      //   275: ldc2_w 17
      //   278: ladd
      //   279: lcmp
      //   280: ifgt +48 -> 328
      //   283: aload 4
      //   285: aload_0
      //   286: getfield 119	android/media/MediaPlayer$TimeProvider:mListeners	[Landroid/media/MediaTimeProvider$OnMediaTimeListener;
      //   289: iload 11
      //   291: aaload
      //   292: invokevirtual 195	java/util/Vector:add	(Ljava/lang/Object;)Z
      //   295: pop
      //   296: aload_0
      //   297: getfield 80	android/media/MediaPlayer$TimeProvider:DEBUG	Z
      //   300: ifeq +11 -> 311
      //   303: ldc 36
      //   305: ldc -59
      //   307: invokestatic 167	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
      //   310: pop
      //   311: aload_0
      //   312: getfield 121	android/media/MediaPlayer$TimeProvider:mTimes	[J
      //   315: iload 11
      //   317: ldc2_w 183
      //   320: lastore
      //   321: lload 5
      //   323: lstore 12
      //   325: goto +36 -> 361
      //   328: lload 5
      //   330: lload_2
      //   331: lcmp
      //   332: ifeq +20 -> 352
      //   335: lload 5
      //   337: lstore 12
      //   339: aload_0
      //   340: getfield 121	android/media/MediaPlayer$TimeProvider:mTimes	[J
      //   343: iload 11
      //   345: laload
      //   346: lload 5
      //   348: lcmp
      //   349: ifge +12 -> 361
      //   352: aload_0
      //   353: getfield 121	android/media/MediaPlayer$TimeProvider:mTimes	[J
      //   356: iload 11
      //   358: laload
      //   359: lstore 12
      //   361: iinc 11 1
      //   364: lload 12
      //   366: lstore 5
      //   368: goto -145 -> 223
      //   371: lload 5
      //   373: lload_2
      //   374: lcmp
      //   375: ifle +84 -> 459
      //   378: aload_0
      //   379: getfield 70	android/media/MediaPlayer$TimeProvider:mPaused	Z
      //   382: ifne +77 -> 459
      //   385: aload_0
      //   386: getfield 80	android/media/MediaPlayer$TimeProvider:DEBUG	Z
      //   389: ifeq +58 -> 447
      //   392: new 147	java/lang/StringBuilder
      //   395: astore 8
      //   397: aload 8
      //   399: invokespecial 148	java/lang/StringBuilder:<init>	()V
      //   402: aload 8
      //   404: ldc -57
      //   406: invokevirtual 154	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   409: pop
      //   410: aload 8
      //   412: lload 5
      //   414: invokevirtual 157	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
      //   417: pop
      //   418: aload 8
      //   420: ldc -55
      //   422: invokevirtual 154	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   425: pop
      //   426: aload 8
      //   428: lload_2
      //   429: invokevirtual 157	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
      //   432: pop
      //   433: ldc 36
      //   435: aload 8
      //   437: invokevirtual 161	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   440: invokestatic 167	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
      //   443: pop
      //   444: goto +3 -> 447
      //   447: aload_0
      //   448: getfield 82	android/media/MediaPlayer$TimeProvider:mPlayer	Landroid/media/MediaPlayer;
      //   451: lload 5
      //   453: invokevirtual 204	android/media/MediaPlayer:notifyAt	(J)V
      //   456: goto +11 -> 467
      //   459: aload_0
      //   460: getfield 115	android/media/MediaPlayer$TimeProvider:mEventHandler	Landroid/os/Handler;
      //   463: iconst_1
      //   464: invokevirtual 210	android/os/Handler:removeMessages	(I)V
      //   467: aload 4
      //   469: invokevirtual 214	java/util/Vector:iterator	()Ljava/util/Iterator;
      //   472: astore 4
      //   474: aload 4
      //   476: invokeinterface 220 1 0
      //   481: ifeq +22 -> 503
      //   484: aload 4
      //   486: invokeinterface 224 1 0
      //   491: checkcast 117	android/media/MediaTimeProvider$OnMediaTimeListener
      //   494: lload_2
      //   495: invokeinterface 227 3 0
      //   500: goto -26 -> 474
      //   503: aload_0
      //   504: monitorexit
      //   505: return
      //   506: aload_0
      //   507: monitorexit
      //   508: aload 4
      //   510: athrow
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	511	0	this	TimeProvider
      //   0	511	1	paramBoolean	boolean
      //   8	487	2	l1	long
      //   12	1	4	localObject1	Object
      //   17	1	4	localIllegalStateException	IllegalStateException
      //   118	391	4	localObject2	Object
      //   37	415	5	l2	long
      //   57	163	7	i	int
      //   66	370	8	localStringBuilder	StringBuilder
      //   123	13	9	j	int
      //   126	54	10	k	int
      //   129	233	11	m	int
      //   143	222	12	l3	long
      // Exception table:
      //   from	to	target	type
      //   2	9	12	finally
      //   19	36	12	finally
      //   39	44	12	finally
      //   51	56	12	finally
      //   63	125	12	finally
      //   162	170	12	finally
      //   170	178	12	finally
      //   187	206	12	finally
      //   209	219	12	finally
      //   223	243	12	finally
      //   246	260	12	finally
      //   267	311	12	finally
      //   311	321	12	finally
      //   339	352	12	finally
      //   352	361	12	finally
      //   378	444	12	finally
      //   447	456	12	finally
      //   459	467	12	finally
      //   467	474	12	finally
      //   474	500	12	finally
      //   2	9	17	java/lang/IllegalStateException
    }
    
    private void notifyTrackData(Pair<SubtitleTrack, byte[]> paramPair)
    {
      try
      {
        ((SubtitleTrack)first).onData((byte[])second, true, -1L);
        return;
      }
      finally
      {
        paramPair = finally;
        throw paramPair;
      }
    }
    
    private int registerListener(MediaTimeProvider.OnMediaTimeListener paramOnMediaTimeListener)
    {
      for (int i = 0; (i < mListeners.length) && (mListeners[i] != paramOnMediaTimeListener) && (mListeners[i] != null); i++) {}
      if (i >= mListeners.length)
      {
        MediaTimeProvider.OnMediaTimeListener[] arrayOfOnMediaTimeListener = new MediaTimeProvider.OnMediaTimeListener[i + 1];
        long[] arrayOfLong = new long[i + 1];
        System.arraycopy(mListeners, 0, arrayOfOnMediaTimeListener, 0, mListeners.length);
        System.arraycopy(mTimes, 0, arrayOfLong, 0, mTimes.length);
        mListeners = arrayOfOnMediaTimeListener;
        mTimes = arrayOfLong;
      }
      if (mListeners[i] == null)
      {
        mListeners[i] = paramOnMediaTimeListener;
        mTimes[i] = -1L;
      }
      return i;
    }
    
    private void scheduleNotification(int paramInt, long paramLong)
    {
      if ((mSeeking) && (paramInt == 0)) {
        return;
      }
      if (DEBUG)
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("scheduleNotification ");
        ((StringBuilder)localObject).append(paramInt);
        ((StringBuilder)localObject).append(" in ");
        ((StringBuilder)localObject).append(paramLong);
        Log.v("MTP", ((StringBuilder)localObject).toString());
      }
      mEventHandler.removeMessages(1);
      Object localObject = mEventHandler.obtainMessage(1, paramInt, 0);
      mEventHandler.sendMessageDelayed((Message)localObject, (int)(paramLong / 1000L));
    }
    
    public void cancelNotifications(MediaTimeProvider.OnMediaTimeListener paramOnMediaTimeListener)
    {
      int i = 0;
      try
      {
        while (i < mListeners.length)
        {
          if (mListeners[i] == paramOnMediaTimeListener)
          {
            System.arraycopy(mListeners, i + 1, mListeners, i, mListeners.length - i - 1);
            System.arraycopy(mTimes, i + 1, mTimes, i, mTimes.length - i - 1);
            mListeners[(mListeners.length - 1)] = null;
            mTimes[(mTimes.length - 1)] = -1L;
            break;
          }
          if (mListeners[i] == null) {
            break;
          }
          i++;
        }
        scheduleNotification(0, 0L);
        return;
      }
      finally {}
    }
    
    public void close()
    {
      mEventHandler.removeMessages(1);
      if (mHandlerThread != null)
      {
        mHandlerThread.quitSafely();
        mHandlerThread = null;
      }
    }
    
    protected void finalize()
    {
      if (mHandlerThread != null) {
        mHandlerThread.quitSafely();
      }
    }
    
    /* Error */
    public long getCurrentTimeUs(boolean paramBoolean1, boolean paramBoolean2)
      throws IllegalStateException
    {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: getfield 70	android/media/MediaPlayer$TimeProvider:mPaused	Z
      //   6: ifeq +16 -> 22
      //   9: iload_1
      //   10: ifne +12 -> 22
      //   13: aload_0
      //   14: getfield 285	android/media/MediaPlayer$TimeProvider:mLastReportedTime	J
      //   17: lstore_3
      //   18: aload_0
      //   19: monitorexit
      //   20: lload_3
      //   21: lreturn
      //   22: aload_0
      //   23: aload_0
      //   24: getfield 82	android/media/MediaPlayer$TimeProvider:mPlayer	Landroid/media/MediaPlayer;
      //   27: invokevirtual 289	android/media/MediaPlayer:getCurrentPosition	()I
      //   30: i2l
      //   31: ldc2_w 17
      //   34: lmul
      //   35: putfield 68	android/media/MediaPlayer$TimeProvider:mLastTimeUs	J
      //   38: aload_0
      //   39: getfield 82	android/media/MediaPlayer$TimeProvider:mPlayer	Landroid/media/MediaPlayer;
      //   42: invokevirtual 292	android/media/MediaPlayer:isPlaying	()Z
      //   45: ifeq +18 -> 63
      //   48: aload_0
      //   49: getfield 294	android/media/MediaPlayer$TimeProvider:mBuffering	Z
      //   52: ifeq +6 -> 58
      //   55: goto +8 -> 63
      //   58: iconst_0
      //   59: istore_1
      //   60: goto +5 -> 65
      //   63: iconst_1
      //   64: istore_1
      //   65: aload_0
      //   66: iload_1
      //   67: putfield 70	android/media/MediaPlayer$TimeProvider:mPaused	Z
      //   70: aload_0
      //   71: getfield 80	android/media/MediaPlayer$TimeProvider:DEBUG	Z
      //   74: ifeq +71 -> 145
      //   77: new 147	java/lang/StringBuilder
      //   80: astore 5
      //   82: aload 5
      //   84: invokespecial 148	java/lang/StringBuilder:<init>	()V
      //   87: aload_0
      //   88: getfield 70	android/media/MediaPlayer$TimeProvider:mPaused	Z
      //   91: ifeq +11 -> 102
      //   94: ldc_w 296
      //   97: astore 6
      //   99: goto +8 -> 107
      //   102: ldc_w 298
      //   105: astore 6
      //   107: aload 5
      //   109: aload 6
      //   111: invokevirtual 154	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   114: pop
      //   115: aload 5
      //   117: ldc_w 300
      //   120: invokevirtual 154	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   123: pop
      //   124: aload 5
      //   126: aload_0
      //   127: getfield 68	android/media/MediaPlayer$TimeProvider:mLastTimeUs	J
      //   130: invokevirtual 157	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
      //   133: pop
      //   134: ldc 36
      //   136: aload 5
      //   138: invokevirtual 161	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   141: invokestatic 266	android/util/Log:v	(Ljava/lang/String;Ljava/lang/String;)I
      //   144: pop
      //   145: iload_2
      //   146: ifeq +50 -> 196
      //   149: aload_0
      //   150: getfield 68	android/media/MediaPlayer$TimeProvider:mLastTimeUs	J
      //   153: aload_0
      //   154: getfield 285	android/media/MediaPlayer$TimeProvider:mLastReportedTime	J
      //   157: lcmp
      //   158: ifge +38 -> 196
      //   161: aload_0
      //   162: getfield 285	android/media/MediaPlayer$TimeProvider:mLastReportedTime	J
      //   165: aload_0
      //   166: getfield 68	android/media/MediaPlayer$TimeProvider:mLastTimeUs	J
      //   169: lsub
      //   170: ldc2_w 301
      //   173: lcmp
      //   174: ifle +30 -> 204
      //   177: aload_0
      //   178: iconst_0
      //   179: putfield 72	android/media/MediaPlayer$TimeProvider:mStopped	Z
      //   182: aload_0
      //   183: iconst_1
      //   184: putfield 78	android/media/MediaPlayer$TimeProvider:mSeeking	Z
      //   187: aload_0
      //   188: iconst_3
      //   189: lconst_0
      //   190: invokespecial 278	android/media/MediaPlayer$TimeProvider:scheduleNotification	(IJ)V
      //   193: goto +11 -> 204
      //   196: aload_0
      //   197: aload_0
      //   198: getfield 68	android/media/MediaPlayer$TimeProvider:mLastTimeUs	J
      //   201: putfield 285	android/media/MediaPlayer$TimeProvider:mLastReportedTime	J
      //   204: aload_0
      //   205: getfield 285	android/media/MediaPlayer$TimeProvider:mLastReportedTime	J
      //   208: lstore_3
      //   209: aload_0
      //   210: monitorexit
      //   211: lload_3
      //   212: lreturn
      //   213: astore 6
      //   215: aload_0
      //   216: getfield 76	android/media/MediaPlayer$TimeProvider:mPausing	Z
      //   219: ifeq +93 -> 312
      //   222: aload_0
      //   223: iconst_0
      //   224: putfield 76	android/media/MediaPlayer$TimeProvider:mPausing	Z
      //   227: iload_2
      //   228: ifeq +15 -> 243
      //   231: aload_0
      //   232: getfield 285	android/media/MediaPlayer$TimeProvider:mLastReportedTime	J
      //   235: aload_0
      //   236: getfield 68	android/media/MediaPlayer$TimeProvider:mLastTimeUs	J
      //   239: lcmp
      //   240: ifge +11 -> 251
      //   243: aload_0
      //   244: aload_0
      //   245: getfield 68	android/media/MediaPlayer$TimeProvider:mLastTimeUs	J
      //   248: putfield 285	android/media/MediaPlayer$TimeProvider:mLastReportedTime	J
      //   251: aload_0
      //   252: iconst_1
      //   253: putfield 70	android/media/MediaPlayer$TimeProvider:mPaused	Z
      //   256: aload_0
      //   257: getfield 80	android/media/MediaPlayer$TimeProvider:DEBUG	Z
      //   260: ifeq +43 -> 303
      //   263: new 147	java/lang/StringBuilder
      //   266: astore 6
      //   268: aload 6
      //   270: invokespecial 148	java/lang/StringBuilder:<init>	()V
      //   273: aload 6
      //   275: ldc_w 304
      //   278: invokevirtual 154	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   281: pop
      //   282: aload 6
      //   284: aload_0
      //   285: getfield 285	android/media/MediaPlayer$TimeProvider:mLastReportedTime	J
      //   288: invokevirtual 157	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
      //   291: pop
      //   292: ldc 36
      //   294: aload 6
      //   296: invokevirtual 161	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   299: invokestatic 167	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
      //   302: pop
      //   303: aload_0
      //   304: getfield 285	android/media/MediaPlayer$TimeProvider:mLastReportedTime	J
      //   307: lstore_3
      //   308: aload_0
      //   309: monitorexit
      //   310: lload_3
      //   311: lreturn
      //   312: aload 6
      //   314: athrow
      //   315: astore 6
      //   317: aload_0
      //   318: monitorexit
      //   319: aload 6
      //   321: athrow
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	322	0	this	TimeProvider
      //   0	322	1	paramBoolean1	boolean
      //   0	322	2	paramBoolean2	boolean
      //   17	294	3	l	long
      //   80	57	5	localStringBuilder1	StringBuilder
      //   97	13	6	str	String
      //   213	1	6	localIllegalStateException	IllegalStateException
      //   266	47	6	localStringBuilder2	StringBuilder
      //   315	5	6	localObject	Object
      // Exception table:
      //   from	to	target	type
      //   22	55	213	java/lang/IllegalStateException
      //   65	94	213	java/lang/IllegalStateException
      //   107	145	213	java/lang/IllegalStateException
      //   2	9	315	finally
      //   13	20	315	finally
      //   22	55	315	finally
      //   65	94	315	finally
      //   107	145	315	finally
      //   149	193	315	finally
      //   196	204	315	finally
      //   204	211	315	finally
      //   215	227	315	finally
      //   231	243	315	finally
      //   243	251	315	finally
      //   251	303	315	finally
      //   303	310	315	finally
      //   312	315	315	finally
      //   317	319	315	finally
    }
    
    public void notifyAt(long paramLong, MediaTimeProvider.OnMediaTimeListener paramOnMediaTimeListener)
    {
      try
      {
        if (DEBUG)
        {
          StringBuilder localStringBuilder = new java/lang/StringBuilder;
          localStringBuilder.<init>();
          localStringBuilder.append("notifyAt ");
          localStringBuilder.append(paramLong);
          Log.d("MTP", localStringBuilder.toString());
        }
        mTimes[registerListener(paramOnMediaTimeListener)] = paramLong;
        scheduleNotification(0, 0L);
        return;
      }
      finally {}
    }
    
    public void onBuffering(boolean paramBoolean)
    {
      try
      {
        if (DEBUG)
        {
          StringBuilder localStringBuilder = new java/lang/StringBuilder;
          localStringBuilder.<init>();
          localStringBuilder.append("onBuffering: ");
          localStringBuilder.append(paramBoolean);
          Log.d("MTP", localStringBuilder.toString());
        }
        mBuffering = paramBoolean;
        scheduleNotification(0, 0L);
        return;
      }
      finally {}
    }
    
    public void onNewPlayer()
    {
      if (mRefresh) {
        try
        {
          mStopped = false;
          mSeeking = true;
          mBuffering = false;
          scheduleNotification(3, 0L);
        }
        finally {}
      }
    }
    
    public void onNotifyTime()
    {
      try
      {
        if (DEBUG) {
          Log.d("MTP", "onNotifyTime: ");
        }
        scheduleNotification(0, 0L);
        return;
      }
      finally {}
    }
    
    public void onPaused(boolean paramBoolean)
    {
      try
      {
        if (DEBUG)
        {
          StringBuilder localStringBuilder = new java/lang/StringBuilder;
          localStringBuilder.<init>();
          localStringBuilder.append("onPaused: ");
          localStringBuilder.append(paramBoolean);
          Log.d("MTP", localStringBuilder.toString());
        }
        if (mStopped)
        {
          mStopped = false;
          mSeeking = true;
          scheduleNotification(3, 0L);
        }
        else
        {
          mPausing = paramBoolean;
          mSeeking = false;
          scheduleNotification(0, 0L);
        }
        return;
      }
      finally {}
    }
    
    public void onSeekComplete(MediaPlayer paramMediaPlayer)
    {
      try
      {
        mStopped = false;
        mSeeking = true;
        scheduleNotification(3, 0L);
        return;
      }
      finally {}
    }
    
    public void onStopped()
    {
      try
      {
        if (DEBUG) {
          Log.d("MTP", "onStopped");
        }
        mPaused = true;
        mStopped = true;
        mSeeking = false;
        mBuffering = false;
        scheduleNotification(2, 0L);
        return;
      }
      finally {}
    }
    
    public void scheduleUpdate(MediaTimeProvider.OnMediaTimeListener paramOnMediaTimeListener)
    {
      try
      {
        if (DEBUG) {
          Log.d("MTP", "scheduleUpdate");
        }
        int i = registerListener(paramOnMediaTimeListener);
        if (!mStopped)
        {
          mTimes[i] = 0L;
          scheduleNotification(0, 0L);
        }
        return;
      }
      finally {}
    }
    
    private class EventHandler
      extends Handler
    {
      public EventHandler(Looper paramLooper)
      {
        super();
      }
      
      public void handleMessage(Message paramMessage)
      {
        if (what == 1)
        {
          int i = arg1;
          if (i != 0) {
            switch (i)
            {
            default: 
              break;
            case 4: 
              MediaPlayer.TimeProvider.this.notifyTrackData((Pair)obj);
              break;
            case 3: 
              MediaPlayer.TimeProvider.this.notifySeek();
              break;
            case 2: 
              MediaPlayer.TimeProvider.this.notifyStop();
              break;
            }
          } else {
            MediaPlayer.TimeProvider.this.notifyTimedEvent(true);
          }
        }
      }
    }
  }
  
  public static class TrackInfo
    implements Parcelable
  {
    static final Parcelable.Creator<TrackInfo> CREATOR = new Parcelable.Creator()
    {
      public MediaPlayer.TrackInfo createFromParcel(Parcel paramAnonymousParcel)
      {
        return new MediaPlayer.TrackInfo(paramAnonymousParcel);
      }
      
      public MediaPlayer.TrackInfo[] newArray(int paramAnonymousInt)
      {
        return new MediaPlayer.TrackInfo[paramAnonymousInt];
      }
    };
    public static final int MEDIA_TRACK_TYPE_AUDIO = 2;
    public static final int MEDIA_TRACK_TYPE_METADATA = 5;
    public static final int MEDIA_TRACK_TYPE_SUBTITLE = 4;
    public static final int MEDIA_TRACK_TYPE_TIMEDTEXT = 3;
    public static final int MEDIA_TRACK_TYPE_UNKNOWN = 0;
    public static final int MEDIA_TRACK_TYPE_VIDEO = 1;
    final MediaFormat mFormat;
    final int mTrackType;
    
    TrackInfo(int paramInt, MediaFormat paramMediaFormat)
    {
      mTrackType = paramInt;
      mFormat = paramMediaFormat;
    }
    
    TrackInfo(Parcel paramParcel)
    {
      mTrackType = paramParcel.readInt();
      mFormat = MediaFormat.createSubtitleFormat(paramParcel.readString(), paramParcel.readString());
      if (mTrackType == 4)
      {
        mFormat.setInteger("is-autoselect", paramParcel.readInt());
        mFormat.setInteger("is-default", paramParcel.readInt());
        mFormat.setInteger("is-forced-subtitle", paramParcel.readInt());
      }
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public MediaFormat getFormat()
    {
      if ((mTrackType != 3) && (mTrackType != 4)) {
        return null;
      }
      return mFormat;
    }
    
    public String getLanguage()
    {
      String str = mFormat.getString("language");
      if (str == null) {
        str = "und";
      }
      return str;
    }
    
    public int getTrackType()
    {
      return mTrackType;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder1 = new StringBuilder(128);
      localStringBuilder1.append(getClass().getName());
      localStringBuilder1.append('{');
      switch (mTrackType)
      {
      default: 
        localStringBuilder1.append("UNKNOWN");
        break;
      case 4: 
        localStringBuilder1.append("SUBTITLE");
        break;
      case 3: 
        localStringBuilder1.append("TIMEDTEXT");
        break;
      case 2: 
        localStringBuilder1.append("AUDIO");
        break;
      case 1: 
        localStringBuilder1.append("VIDEO");
      }
      StringBuilder localStringBuilder2 = new StringBuilder();
      localStringBuilder2.append(", ");
      localStringBuilder2.append(mFormat.toString());
      localStringBuilder1.append(localStringBuilder2.toString());
      localStringBuilder1.append("}");
      return localStringBuilder1.toString();
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeInt(mTrackType);
      paramParcel.writeString(mFormat.getString("mime"));
      paramParcel.writeString(getLanguage());
      if (mTrackType == 4)
      {
        paramParcel.writeInt(mFormat.getInteger("is-autoselect"));
        paramParcel.writeInt(mFormat.getInteger("is-default"));
        paramParcel.writeInt(mFormat.getInteger("is-forced-subtitle"));
      }
    }
    
    @Retention(RetentionPolicy.SOURCE)
    public static @interface TrackType {}
  }
}
