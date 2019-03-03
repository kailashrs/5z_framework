package android.media;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PersistableBundle;
import android.util.ArrayMap;
import android.util.Log;
import com.android.internal.annotations.GuardedBy;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.NioUtils;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.Executor;

public class AudioTrack
  extends PlayerBase
  implements AudioRouting, VolumeAutomation
{
  private static final int AUDIO_OUTPUT_FLAG_DEEP_BUFFER = 8;
  private static final int AUDIO_OUTPUT_FLAG_FAST = 4;
  public static final int CHANNEL_COUNT_MAX = ;
  public static final int ERROR = -1;
  public static final int ERROR_BAD_VALUE = -2;
  public static final int ERROR_DEAD_OBJECT = -6;
  public static final int ERROR_INVALID_OPERATION = -3;
  private static final int ERROR_NATIVESETUP_AUDIOSYSTEM = -16;
  private static final int ERROR_NATIVESETUP_INVALIDCHANNELMASK = -17;
  private static final int ERROR_NATIVESETUP_INVALIDFORMAT = -18;
  private static final int ERROR_NATIVESETUP_INVALIDSTREAMTYPE = -19;
  private static final int ERROR_NATIVESETUP_NATIVEINITFAILED = -20;
  public static final int ERROR_WOULD_BLOCK = -7;
  private static final float GAIN_MAX = 1.0F;
  private static final float GAIN_MIN = 0.0F;
  private static final float HEADER_V2_SIZE_BYTES = 20.0F;
  public static final int MODE_STATIC = 0;
  public static final int MODE_STREAM = 1;
  private static final int NATIVE_EVENT_MARKER = 3;
  private static final int NATIVE_EVENT_MORE_DATA = 0;
  private static final int NATIVE_EVENT_NEW_IAUDIOTRACK = 6;
  private static final int NATIVE_EVENT_NEW_POS = 4;
  private static final int NATIVE_EVENT_STREAM_END = 7;
  public static final int PERFORMANCE_MODE_LOW_LATENCY = 1;
  public static final int PERFORMANCE_MODE_NONE = 0;
  public static final int PERFORMANCE_MODE_POWER_SAVING = 2;
  public static final int PLAYSTATE_PAUSED = 2;
  public static final int PLAYSTATE_PLAYING = 3;
  public static final int PLAYSTATE_STOPPED = 1;
  public static final int STATE_INITIALIZED = 1;
  public static final int STATE_NO_STATIC_DATA = 2;
  public static final int STATE_UNINITIALIZED = 0;
  public static final int SUCCESS = 0;
  private static final int SUPPORTED_OUT_CHANNELS = 7420;
  private static final String TAG = "android.media.AudioTrack";
  public static final int WRITE_BLOCKING = 0;
  public static final int WRITE_NON_BLOCKING = 1;
  private int mAudioFormat;
  private int mAvSyncBytesRemaining = 0;
  private ByteBuffer mAvSyncHeader = null;
  private int mChannelConfiguration = 4;
  private int mChannelCount = 1;
  private int mChannelIndexMask = 0;
  private int mChannelMask = 4;
  private int mDataLoadMode = 1;
  private NativePositionEventHandlerDelegate mEventHandlerDelegate;
  private final Looper mInitializationLooper;
  private long mJniData;
  private int mNativeBufferSizeInBytes = 0;
  private int mNativeBufferSizeInFrames = 0;
  protected long mNativeTrackInJavaObj;
  private int mOffset = 0;
  private int mPlayState = 1;
  private final Object mPlayStateLock = new Object();
  private AudioDeviceInfo mPreferredDevice = null;
  @GuardedBy("mRoutingChangeListeners")
  private ArrayMap<AudioRouting.OnRoutingChangedListener, NativeRoutingEventHandlerDelegate> mRoutingChangeListeners = new ArrayMap();
  private int mSampleRate;
  private int mSessionId = 0;
  private int mState = 0;
  private StreamEventCallback mStreamEventCb;
  private final Object mStreamEventCbLock = new Object();
  private Executor mStreamEventExec;
  private int mStreamType = 3;
  
  public AudioTrack(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
    throws IllegalArgumentException
  {
    this(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, 0);
  }
  
  public AudioTrack(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7)
    throws IllegalArgumentException
  {
    this(new AudioAttributes.Builder().setLegacyStreamType(paramInt1).build(), new AudioFormat.Builder().setChannelMask(paramInt3).setEncoding(paramInt4).setSampleRate(paramInt2).build(), paramInt5, paramInt6, paramInt7);
    deprecateStreamTypeForPlayback(paramInt1, "AudioTrack", "AudioTrack()");
  }
  
  AudioTrack(long paramLong)
  {
    super(new AudioAttributes.Builder().build(), 1);
    mNativeTrackInJavaObj = 0L;
    mJniData = 0L;
    Looper localLooper1 = Looper.myLooper();
    Looper localLooper2 = localLooper1;
    if (localLooper1 == null) {
      localLooper2 = Looper.getMainLooper();
    }
    mInitializationLooper = localLooper2;
    if (paramLong != 0L)
    {
      baseRegisterPlayer();
      deferred_connect(paramLong);
    }
    else
    {
      mState = 0;
    }
  }
  
  public AudioTrack(AudioAttributes paramAudioAttributes, AudioFormat paramAudioFormat, int paramInt1, int paramInt2, int paramInt3)
    throws IllegalArgumentException
  {
    this(paramAudioAttributes, paramAudioFormat, paramInt1, paramInt2, paramInt3, false);
  }
  
  private AudioTrack(AudioAttributes paramAudioAttributes, AudioFormat paramAudioFormat, int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean)
    throws IllegalArgumentException
  {
    super(paramAudioAttributes, 1);
    if (paramAudioFormat != null)
    {
      if (shouldEnablePowerSaving(mAttributes, paramAudioFormat, paramInt1, paramInt2)) {
        mAttributes = new AudioAttributes.Builder(mAttributes).replaceFlags((mAttributes.getAllFlags() | 0x200) & 0xFEFF).build();
      }
      Looper localLooper = Looper.myLooper();
      paramAudioAttributes = localLooper;
      if (localLooper == null) {
        paramAudioAttributes = Looper.getMainLooper();
      }
      int i = paramAudioFormat.getSampleRate();
      int j = i;
      if (i == 0) {
        j = 0;
      }
      int k = 0;
      if ((paramAudioFormat.getPropertySetMask() & 0x8) != 0) {
        k = paramAudioFormat.getChannelIndexMask();
      }
      if ((0x4 & paramAudioFormat.getPropertySetMask()) != 0) {
        i = paramAudioFormat.getChannelMask();
      }
      for (;;)
      {
        break;
        if (k == 0) {
          i = 12;
        } else {
          i = 0;
        }
      }
      int m = 1;
      if ((paramAudioFormat.getPropertySetMask() & 0x1) != 0) {
        m = paramAudioFormat.getEncoding();
      }
      audioParamCheck(j, i, k, m, paramInt2);
      mStreamType = -1;
      audioBuffSizeCheck(paramInt1);
      mInitializationLooper = paramAudioAttributes;
      if (paramInt3 >= 0)
      {
        paramAudioFormat = new int[1];
        paramAudioFormat[0] = mSampleRate;
        paramAudioAttributes = new int[1];
        paramAudioAttributes[0] = paramInt3;
        paramInt1 = native_setup(new WeakReference(this), mAttributes, paramAudioFormat, mChannelMask, mChannelIndexMask, mAudioFormat, mNativeBufferSizeInBytes, mDataLoadMode, paramAudioAttributes, 0L, paramBoolean);
        if (paramInt1 != 0)
        {
          paramAudioAttributes = new StringBuilder();
          paramAudioAttributes.append("Error code ");
          paramAudioAttributes.append(paramInt1);
          paramAudioAttributes.append(" when initializing AudioTrack.");
          loge(paramAudioAttributes.toString());
          return;
        }
        mSampleRate = paramAudioFormat[0];
        mSessionId = paramAudioAttributes[0];
        if ((mAttributes.getFlags() & 0x10) != 0)
        {
          if (AudioFormat.isEncodingLinearFrames(mAudioFormat)) {
            paramInt1 = mChannelCount * AudioFormat.getBytesPerSample(mAudioFormat);
          } else {
            paramInt1 = 1;
          }
          mOffset = ((int)Math.ceil(20.0F / paramInt1) * paramInt1);
        }
        if (mDataLoadMode == 0) {
          mState = 2;
        } else {
          mState = 1;
        }
        baseRegisterPlayer();
        return;
      }
      paramAudioAttributes = new StringBuilder();
      paramAudioAttributes.append("Invalid audio session ID: ");
      paramAudioAttributes.append(paramInt3);
      throw new IllegalArgumentException(paramAudioAttributes.toString());
    }
    throw new IllegalArgumentException("Illegal null AudioFormat");
  }
  
  private void audioBuffSizeCheck(int paramInt)
  {
    int i;
    if (AudioFormat.isEncodingLinearFrames(mAudioFormat)) {
      i = mChannelCount * AudioFormat.getBytesPerSample(mAudioFormat);
    } else {
      i = 1;
    }
    if ((paramInt % i == 0) && (paramInt >= 1))
    {
      mNativeBufferSizeInBytes = paramInt;
      mNativeBufferSizeInFrames = (paramInt / i);
      return;
    }
    throw new IllegalArgumentException("Invalid audio buffer size.");
  }
  
  private void audioParamCheck(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
  {
    if (((paramInt1 >= 4000) && (paramInt1 <= 192000)) || (paramInt1 == 0))
    {
      mSampleRate = paramInt1;
      if ((paramInt4 == 13) && (paramInt2 != 12)) {
        throw new IllegalArgumentException("ENCODING_IEC61937 must be configured as CHANNEL_OUT_STEREO");
      }
      mChannelConfiguration = paramInt2;
      if (paramInt2 != 12)
      {
        switch (paramInt2)
        {
        default: 
          if ((paramInt2 == 0) && (paramInt3 != 0))
          {
            mChannelCount = 0;
            break;
          }
          if (isMultichannelConfigSupported(paramInt2))
          {
            mChannelMask = paramInt2;
            mChannelCount = AudioFormat.channelCountFromOutChannelMask(paramInt2);
            break;
          }
          throw new IllegalArgumentException("Unsupported channel configuration.");
        case 1: 
        case 2: 
        case 4: 
          mChannelCount = 1;
          mChannelMask = 4;
          break;
        }
      }
      else
      {
        mChannelCount = 2;
        mChannelMask = 12;
      }
      mChannelIndexMask = paramInt3;
      if (mChannelIndexMask != 0) {
        if (((1 << CHANNEL_COUNT_MAX) - 1 & paramInt3) == 0)
        {
          paramInt1 = Integer.bitCount(paramInt3);
          if (mChannelCount == 0) {
            mChannelCount = paramInt1;
          } else if (mChannelCount != paramInt1) {
            throw new IllegalArgumentException("Channel count must match");
          }
        }
        else
        {
          localStringBuilder = new StringBuilder();
          localStringBuilder.append("Unsupported channel index configuration ");
          localStringBuilder.append(paramInt3);
          throw new IllegalArgumentException(localStringBuilder.toString());
        }
      }
      paramInt1 = paramInt4;
      if (paramInt4 == 1) {
        paramInt1 = 2;
      }
      if (AudioFormat.isPublicEncoding(paramInt1))
      {
        mAudioFormat = paramInt1;
        if (((paramInt5 != 1) && (paramInt5 != 0)) || ((paramInt5 != 1) && (!AudioFormat.isEncodingLinearPcm(mAudioFormat)))) {
          throw new IllegalArgumentException("Invalid mode.");
        }
        mDataLoadMode = paramInt5;
        return;
      }
      throw new IllegalArgumentException("Unsupported audio encoding.");
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramInt1);
    localStringBuilder.append("Hz is not a supported sample rate.");
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  private void broadcastRoutingChange()
  {
    AudioManager.resetAudioPortGeneration();
    synchronized (mRoutingChangeListeners)
    {
      Iterator localIterator = mRoutingChangeListeners.values().iterator();
      while (localIterator.hasNext()) {
        ((NativeRoutingEventHandlerDelegate)localIterator.next()).notifyClient();
      }
      return;
    }
  }
  
  private static float clampGainOrLevel(float paramFloat)
  {
    if (!Float.isNaN(paramFloat))
    {
      float f;
      if (paramFloat < 0.0F)
      {
        f = 0.0F;
      }
      else
      {
        f = paramFloat;
        if (paramFloat > 1.0F) {
          f = 1.0F;
        }
      }
      return f;
    }
    throw new IllegalArgumentException();
  }
  
  public static float getMaxVolume()
  {
    return 1.0F;
  }
  
  public static int getMinBufferSize(int paramInt1, int paramInt2, int paramInt3)
  {
    if (paramInt2 != 12) {
      switch (paramInt2)
      {
      default: 
        if (!isMultichannelConfigSupported(paramInt2))
        {
          loge("getMinBufferSize(): Invalid channel configuration.");
          return -2;
        }
        paramInt2 = AudioFormat.channelCountFromOutChannelMask(paramInt2);
        break;
      case 2: 
      case 4: 
        paramInt2 = 1;
        break;
      }
    } else {
      paramInt2 = 2;
    }
    if (!AudioFormat.isPublicEncoding(paramInt3))
    {
      loge("getMinBufferSize(): Invalid audio format.");
      return -2;
    }
    if ((paramInt1 >= 4000) && (paramInt1 <= 192000))
    {
      paramInt1 = native_get_min_buff_size(paramInt1, paramInt2, paramInt3);
      if (paramInt1 <= 0)
      {
        loge("getMinBufferSize(): error querying hardware");
        return -1;
      }
      return paramInt1;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("getMinBufferSize(): ");
    localStringBuilder.append(paramInt1);
    localStringBuilder.append(" Hz is not a supported sample rate.");
    loge(localStringBuilder.toString());
    return -2;
  }
  
  public static float getMinVolume()
  {
    return 0.0F;
  }
  
  public static int getNativeOutputSampleRate(int paramInt)
  {
    return native_get_output_sample_rate(paramInt);
  }
  
  private static boolean isMultichannelConfigSupported(int paramInt)
  {
    if ((paramInt & 0x1CFC) != paramInt)
    {
      loge("Channel configuration features unsupported channels");
      return false;
    }
    int i = AudioFormat.channelCountFromOutChannelMask(paramInt);
    if (i > CHANNEL_COUNT_MAX)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Channel configuration contains too many channels ");
      localStringBuilder.append(i);
      localStringBuilder.append(">");
      localStringBuilder.append(CHANNEL_COUNT_MAX);
      loge(localStringBuilder.toString());
      return false;
    }
    if ((paramInt & 0xC) != 12)
    {
      loge("Front channels must be present in multichannel configurations");
      return false;
    }
    if (((paramInt & 0xC0) != 0) && ((paramInt & 0xC0) != 192))
    {
      loge("Rear channels can't be used independently");
      return false;
    }
    if (((paramInt & 0x1800) != 0) && ((paramInt & 0x1800) != 6144))
    {
      loge("Side channels can't be used independently");
      return false;
    }
    return true;
  }
  
  private static void logd(String paramString)
  {
    Log.d("android.media.AudioTrack", paramString);
  }
  
  private static void loge(String paramString)
  {
    Log.e("android.media.AudioTrack", paramString);
  }
  
  private native int native_applyVolumeShaper(VolumeShaper.Configuration paramConfiguration, VolumeShaper.Operation paramOperation);
  
  private final native int native_attachAuxEffect(int paramInt);
  
  private final native void native_disableDeviceCallback();
  
  private final native void native_enableDeviceCallback();
  
  private final native void native_finalize();
  
  private final native void native_flush();
  
  private native PersistableBundle native_getMetrics();
  
  private final native int native_getRoutedDeviceId();
  
  private native VolumeShaper.State native_getVolumeShaperState(int paramInt);
  
  private static native int native_get_FCC_8();
  
  private final native int native_get_buffer_capacity_frames();
  
  private final native int native_get_buffer_size_frames();
  
  private final native int native_get_flags();
  
  private final native int native_get_latency();
  
  private final native int native_get_marker_pos();
  
  private static final native int native_get_min_buff_size(int paramInt1, int paramInt2, int paramInt3);
  
  private static final native int native_get_output_sample_rate(int paramInt);
  
  private final native PlaybackParams native_get_playback_params();
  
  private final native int native_get_playback_rate();
  
  private final native int native_get_pos_update_period();
  
  private final native int native_get_position();
  
  private final native int native_get_timestamp(long[] paramArrayOfLong);
  
  private final native int native_get_underrun_count();
  
  private final native void native_pause();
  
  private final native int native_reload_static();
  
  private final native int native_setAuxEffectSendLevel(float paramFloat);
  
  private final native boolean native_setOutputDevice(int paramInt);
  
  private final native int native_setPresentation(int paramInt1, int paramInt2);
  
  private final native void native_setVolume(float paramFloat1, float paramFloat2);
  
  private final native int native_set_buffer_size_frames(int paramInt);
  
  private final native int native_set_loop(int paramInt1, int paramInt2, int paramInt3);
  
  private final native int native_set_marker_pos(int paramInt);
  
  private final native void native_set_playback_params(PlaybackParams paramPlaybackParams);
  
  private final native int native_set_playback_rate(int paramInt);
  
  private final native int native_set_pos_update_period(int paramInt);
  
  private final native int native_set_position(int paramInt);
  
  private final native int native_setup(Object paramObject1, Object paramObject2, int[] paramArrayOfInt1, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int[] paramArrayOfInt2, long paramLong, boolean paramBoolean);
  
  private final native void native_start();
  
  private final native void native_stop();
  
  private final native int native_write_byte(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean);
  
  private final native int native_write_float(float[] paramArrayOfFloat, int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean);
  
  private final native int native_write_native_bytes(Object paramObject, int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean);
  
  private final native int native_write_short(short[] paramArrayOfShort, int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean);
  
  private static void postEventFromNative(Object arg0, int paramInt1, int paramInt2, int paramInt3, Object paramObject2)
  {
    AudioTrack localAudioTrack = (AudioTrack)((WeakReference)???).get();
    if (localAudioTrack == null) {
      return;
    }
    if (paramInt1 == 1000)
    {
      localAudioTrack.broadcastRoutingChange();
      return;
    }
    if ((paramInt1 == 0) || (paramInt1 == 6) || (paramInt1 == 7)) {}
    synchronized (mStreamEventCbLock)
    {
      Executor localExecutor = mStreamEventExec;
      StreamEventCallback localStreamEventCallback = mStreamEventCb;
      if ((localExecutor != null) && (localStreamEventCallback != null))
      {
        if (paramInt1 != 0)
        {
          switch (paramInt1)
          {
          default: 
            ??? = mEventHandlerDelegate;
            if (??? != null)
            {
              ??? = ???.getHandler();
              if (??? != null) {
                ???.sendMessage(???.obtainMessage(paramInt1, paramInt2, paramInt3, paramObject2));
              }
            }
            return;
          case 7: 
            localExecutor.execute(new _..Lambda.AudioTrack.om39tqtuoUKWEwKYDHE7uiykjxw(localStreamEventCallback, localAudioTrack));
            return;
          }
          localExecutor.execute(new _..Lambda.AudioTrack.m_q5GeJNFuHKP4bKA5zNcUJmptg(localStreamEventCallback, localAudioTrack));
          return;
        }
        localExecutor.execute(new _..Lambda.AudioTrack.RYzHLsveZX4qW27TDViuZeb3nTQ(localStreamEventCallback, localAudioTrack));
        return;
      }
      return;
    }
  }
  
  private static boolean shouldEnablePowerSaving(AudioAttributes paramAudioAttributes, AudioFormat paramAudioFormat, int paramInt1, int paramInt2)
  {
    if ((paramAudioAttributes != null) && ((paramAudioAttributes.getAllFlags() != 0) || (paramAudioAttributes.getUsage() != 1) || ((paramAudioAttributes.getContentType() != 0) && (paramAudioAttributes.getContentType() != 2) && (paramAudioAttributes.getContentType() != 3)))) {
      return false;
    }
    if ((paramAudioFormat != null) && (paramAudioFormat.getSampleRate() != 0) && (AudioFormat.isEncodingLinearPcm(paramAudioFormat.getEncoding())) && (AudioFormat.isValidEncoding(paramAudioFormat.getEncoding())) && (paramAudioFormat.getChannelCount() >= 1))
    {
      if (paramInt2 != 1) {
        return false;
      }
      if (paramInt1 != 0)
      {
        long l = 100L * paramAudioFormat.getChannelCount() * AudioFormat.getBytesPerSample(paramAudioFormat.getEncoding()) * paramAudioFormat.getSampleRate() / 1000L;
        if (paramInt1 < l) {
          return false;
        }
      }
      return true;
    }
    return false;
  }
  
  private void startImpl()
  {
    synchronized (mPlayStateLock)
    {
      if (mStreamType == 3) {
        executeAIVolume("android.media.AudioTrack");
      }
      baseStart();
      native_start();
      mPlayState = 3;
      return;
    }
  }
  
  @GuardedBy("mRoutingChangeListeners")
  private void testDisableNativeRoutingCallbacksLocked()
  {
    if (mRoutingChangeListeners.size() == 0) {
      native_disableDeviceCallback();
    }
  }
  
  @GuardedBy("mRoutingChangeListeners")
  private void testEnableNativeRoutingCallbacksLocked()
  {
    if (mRoutingChangeListeners.size() == 0) {
      native_enableDeviceCallback();
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
          testEnableNativeRoutingCallbacksLocked();
          ArrayMap localArrayMap2 = mRoutingChangeListeners;
          NativeRoutingEventHandlerDelegate localNativeRoutingEventHandlerDelegate = new android/media/NativeRoutingEventHandlerDelegate;
          if (paramHandler == null) {
            paramHandler = new Handler(mInitializationLooper);
          }
          localNativeRoutingEventHandlerDelegate.<init>(this, paramOnRoutingChangedListener, paramHandler);
          localArrayMap2.put(paramOnRoutingChangedListener, localNativeRoutingEventHandlerDelegate);
        }
      }
      finally
      {
        break label83;
      }
    }
    return;
    label83:
    throw paramOnRoutingChangedListener;
  }
  
  @Deprecated
  public void addOnRoutingChangedListener(OnRoutingChangedListener paramOnRoutingChangedListener, Handler paramHandler)
  {
    addOnRoutingChangedListener(paramOnRoutingChangedListener, paramHandler);
  }
  
  public int attachAuxEffect(int paramInt)
  {
    if (mState == 0) {
      return -3;
    }
    return native_attachAuxEffect(paramInt);
  }
  
  public VolumeShaper createVolumeShaper(VolumeShaper.Configuration paramConfiguration)
  {
    return new VolumeShaper(paramConfiguration, this);
  }
  
  void deferred_connect(long paramLong)
  {
    if (mState != 1)
    {
      Object localObject = new int[1];
      localObject[0] = 0;
      int i = native_setup(new WeakReference(this), null, new int[] { 0 }, 0, 0, 0, 0, 0, (int[])localObject, paramLong, false);
      if (i != 0)
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("Error code ");
        ((StringBuilder)localObject).append(i);
        ((StringBuilder)localObject).append(" when initializing AudioTrack.");
        loge(((StringBuilder)localObject).toString());
        return;
      }
      mSessionId = localObject[0];
      mState = 1;
    }
  }
  
  protected void finalize()
  {
    baseRelease();
    native_finalize();
  }
  
  public void flush()
  {
    if (mState == 1)
    {
      native_flush();
      mAvSyncHeader = null;
      mAvSyncBytesRemaining = 0;
    }
  }
  
  public int getAudioFormat()
  {
    return mAudioFormat;
  }
  
  public int getAudioSessionId()
  {
    return mSessionId;
  }
  
  public int getBufferCapacityInFrames()
  {
    return native_get_buffer_capacity_frames();
  }
  
  public int getBufferSizeInFrames()
  {
    return native_get_buffer_size_frames();
  }
  
  public int getChannelConfiguration()
  {
    return mChannelConfiguration;
  }
  
  public int getChannelCount()
  {
    return mChannelCount;
  }
  
  public AudioFormat getFormat()
  {
    AudioFormat.Builder localBuilder = new AudioFormat.Builder().setSampleRate(mSampleRate).setEncoding(mAudioFormat);
    if (mChannelConfiguration != 0) {
      localBuilder.setChannelMask(mChannelConfiguration);
    }
    if (mChannelIndexMask != 0) {
      localBuilder.setChannelIndexMask(mChannelIndexMask);
    }
    return localBuilder.build();
  }
  
  public int getLatency()
  {
    return native_get_latency();
  }
  
  public PersistableBundle getMetrics()
  {
    return native_getMetrics();
  }
  
  @Deprecated
  protected int getNativeFrameCount()
  {
    return native_get_buffer_capacity_frames();
  }
  
  public int getNotificationMarkerPosition()
  {
    return native_get_marker_pos();
  }
  
  public int getPerformanceMode()
  {
    int i = native_get_flags();
    if ((i & 0x4) != 0) {
      return 1;
    }
    if ((i & 0x8) != 0) {
      return 2;
    }
    return 0;
  }
  
  public int getPlayState()
  {
    synchronized (mPlayStateLock)
    {
      int i = mPlayState;
      return i;
    }
  }
  
  public int getPlaybackHeadPosition()
  {
    return native_get_position();
  }
  
  public PlaybackParams getPlaybackParams()
  {
    return native_get_playback_params();
  }
  
  public int getPlaybackRate()
  {
    return native_get_playback_rate();
  }
  
  public int getPositionNotificationPeriod()
  {
    return native_get_pos_update_period();
  }
  
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
  
  public int getSampleRate()
  {
    return mSampleRate;
  }
  
  public int getState()
  {
    return mState;
  }
  
  public int getStreamType()
  {
    return mStreamType;
  }
  
  public boolean getTimestamp(AudioTimestamp paramAudioTimestamp)
  {
    if (paramAudioTimestamp != null)
    {
      long[] arrayOfLong = new long[2];
      if (native_get_timestamp(arrayOfLong) != 0) {
        return false;
      }
      framePosition = arrayOfLong[0];
      nanoTime = arrayOfLong[1];
      return true;
    }
    throw new IllegalArgumentException();
  }
  
  public int getTimestampWithStatus(AudioTimestamp paramAudioTimestamp)
  {
    if (paramAudioTimestamp != null)
    {
      long[] arrayOfLong = new long[2];
      int i = native_get_timestamp(arrayOfLong);
      framePosition = arrayOfLong[0];
      nanoTime = arrayOfLong[1];
      return i;
    }
    throw new IllegalArgumentException();
  }
  
  public int getUnderrunCount()
  {
    return native_get_underrun_count();
  }
  
  boolean isStreamSystemEnforcedMute()
  {
    return false;
  }
  
  public final native void native_release();
  
  public void pause()
    throws IllegalStateException
  {
    if (mState == 1) {
      synchronized (mPlayStateLock)
      {
        native_pause();
        basePause();
        mPlayState = 2;
        return;
      }
    }
    throw new IllegalStateException("pause() called on uninitialized AudioTrack.");
  }
  
  public void play()
    throws IllegalStateException
  {
    if (mState == 1)
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
              AudioTrack.this.startImpl();
            }
            catch (IllegalStateException localIllegalStateException) {}
          }
        }.start();
      }
      return;
    }
    throw new IllegalStateException("play() called on uninitialized AudioTrack.");
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
    int i;
    if (native_setAuxEffectSendLevel(clampGainOrLevel(paramFloat)) == 0) {
      i = 0;
    } else {
      i = -1;
    }
    return i;
  }
  
  void playerSetVolume(boolean paramBoolean, float paramFloat1, float paramFloat2)
  {
    float f = 0.0F;
    if (paramBoolean) {
      paramFloat1 = 0.0F;
    }
    paramFloat1 = clampGainOrLevel(paramFloat1);
    if (paramBoolean) {
      paramFloat2 = f;
    }
    native_setVolume(paramFloat1, clampGainOrLevel(paramFloat2));
  }
  
  void playerStart()
  {
    play();
  }
  
  void playerStop()
  {
    stop();
  }
  
  public void release()
  {
    try
    {
      stop();
    }
    catch (IllegalStateException localIllegalStateException) {}
    baseRelease();
    native_release();
    mState = 0;
  }
  
  public int reloadStaticData()
  {
    if ((mDataLoadMode != 1) && (mState == 1)) {
      return native_reload_static();
    }
    return -3;
  }
  
  public void removeOnRoutingChangedListener(AudioRouting.OnRoutingChangedListener paramOnRoutingChangedListener)
  {
    synchronized (mRoutingChangeListeners)
    {
      if (mRoutingChangeListeners.containsKey(paramOnRoutingChangedListener)) {
        mRoutingChangeListeners.remove(paramOnRoutingChangedListener);
      }
      testDisableNativeRoutingCallbacksLocked();
      return;
    }
  }
  
  @Deprecated
  public void removeOnRoutingChangedListener(OnRoutingChangedListener paramOnRoutingChangedListener)
  {
    removeOnRoutingChangedListener(paramOnRoutingChangedListener);
  }
  
  public void removeStreamEventCallback()
  {
    synchronized (mStreamEventCbLock)
    {
      mStreamEventExec = null;
      mStreamEventCb = null;
      return;
    }
  }
  
  public int setAuxEffectSendLevel(float paramFloat)
  {
    if (mState == 0) {
      return -3;
    }
    return baseSetAuxEffectSendLevel(paramFloat);
  }
  
  public int setBufferSizeInFrames(int paramInt)
  {
    if ((mDataLoadMode != 0) && (mState != 0))
    {
      if (paramInt < 0) {
        return -2;
      }
      return native_set_buffer_size_frames(paramInt);
    }
    return -3;
  }
  
  public int setLoopPoints(int paramInt1, int paramInt2, int paramInt3)
  {
    if ((mDataLoadMode != 1) && (mState != 0) && (getPlayState() != 3))
    {
      if ((paramInt3 == 0) || ((paramInt1 >= 0) && (paramInt1 < mNativeBufferSizeInFrames) && (paramInt1 < paramInt2) && (paramInt2 <= mNativeBufferSizeInFrames))) {
        return native_set_loop(paramInt1, paramInt2, paramInt3);
      }
      return -2;
    }
    return -3;
  }
  
  public int setNotificationMarkerPosition(int paramInt)
  {
    if (mState == 0) {
      return -3;
    }
    return native_set_marker_pos(paramInt);
  }
  
  public int setPlaybackHeadPosition(int paramInt)
  {
    if ((mDataLoadMode != 1) && (mState != 0) && (getPlayState() != 3))
    {
      if ((paramInt >= 0) && (paramInt <= mNativeBufferSizeInFrames)) {
        return native_set_position(paramInt);
      }
      return -2;
    }
    return -3;
  }
  
  public void setPlaybackParams(PlaybackParams paramPlaybackParams)
  {
    if (paramPlaybackParams != null)
    {
      native_set_playback_params(paramPlaybackParams);
      return;
    }
    throw new IllegalArgumentException("params is null");
  }
  
  public void setPlaybackPositionUpdateListener(OnPlaybackPositionUpdateListener paramOnPlaybackPositionUpdateListener)
  {
    setPlaybackPositionUpdateListener(paramOnPlaybackPositionUpdateListener, null);
  }
  
  public void setPlaybackPositionUpdateListener(OnPlaybackPositionUpdateListener paramOnPlaybackPositionUpdateListener, Handler paramHandler)
  {
    if (paramOnPlaybackPositionUpdateListener != null) {
      mEventHandlerDelegate = new NativePositionEventHandlerDelegate(this, paramOnPlaybackPositionUpdateListener, paramHandler);
    } else {
      mEventHandlerDelegate = null;
    }
  }
  
  public int setPlaybackRate(int paramInt)
  {
    if (mState != 1) {
      return -3;
    }
    if (paramInt <= 0) {
      return -2;
    }
    return native_set_playback_rate(paramInt);
  }
  
  public int setPositionNotificationPeriod(int paramInt)
  {
    if (mState == 0) {
      return -3;
    }
    return native_set_pos_update_period(paramInt);
  }
  
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
  
  public int setPresentation(AudioPresentation paramAudioPresentation)
  {
    if (paramAudioPresentation != null) {
      return native_setPresentation(paramAudioPresentation.getPresentationId(), paramAudioPresentation.getProgramId());
    }
    throw new IllegalArgumentException("audio presentation is null");
  }
  
  @Deprecated
  protected void setState(int paramInt)
  {
    mState = paramInt;
  }
  
  @Deprecated
  public int setStereoVolume(float paramFloat1, float paramFloat2)
  {
    if (mState == 0) {
      return -3;
    }
    baseSetVolume(paramFloat1, paramFloat2);
    return 0;
  }
  
  public void setStreamEventCallback(Executor paramExecutor, StreamEventCallback paramStreamEventCallback)
  {
    if (paramStreamEventCallback != null)
    {
      if (paramExecutor != null) {
        synchronized (mStreamEventCbLock)
        {
          mStreamEventExec = paramExecutor;
          mStreamEventCb = paramStreamEventCallback;
          return;
        }
      }
      throw new IllegalArgumentException("Illegal null Executor for the StreamEventCallback");
    }
    throw new IllegalArgumentException("Illegal null StreamEventCallback");
  }
  
  public int setVolume(float paramFloat)
  {
    return setStereoVolume(paramFloat, paramFloat);
  }
  
  public void stop()
    throws IllegalStateException
  {
    if (mState == 1) {
      synchronized (mPlayStateLock)
      {
        native_stop();
        baseStop();
        mPlayState = 1;
        mAvSyncHeader = null;
        mAvSyncBytesRemaining = 0;
        return;
      }
    }
    throw new IllegalStateException("stop() called on uninitialized AudioTrack.");
  }
  
  public int write(ByteBuffer paramByteBuffer, int paramInt1, int paramInt2)
  {
    if (mState == 0)
    {
      Log.e("android.media.AudioTrack", "AudioTrack.write() called in invalid state STATE_UNINITIALIZED");
      return -3;
    }
    if ((paramInt2 != 0) && (paramInt2 != 1))
    {
      Log.e("android.media.AudioTrack", "AudioTrack.write() called with invalid blocking mode");
      return -2;
    }
    if ((paramByteBuffer != null) && (paramInt1 >= 0) && (paramInt1 <= paramByteBuffer.remaining()))
    {
      int i;
      int j;
      boolean bool;
      if (paramByteBuffer.isDirect())
      {
        i = paramByteBuffer.position();
        j = mAudioFormat;
        if (paramInt2 == 0) {
          bool = true;
        } else {
          bool = false;
        }
        paramInt1 = native_write_native_bytes(paramByteBuffer, i, paramInt1, j, bool);
      }
      else
      {
        byte[] arrayOfByte = NioUtils.unsafeArray(paramByteBuffer);
        i = NioUtils.unsafeArrayOffset(paramByteBuffer);
        j = paramByteBuffer.position();
        int k = mAudioFormat;
        if (paramInt2 == 0) {
          bool = true;
        } else {
          bool = false;
        }
        paramInt1 = native_write_byte(arrayOfByte, j + i, paramInt1, k, bool);
      }
      if ((mDataLoadMode == 0) && (mState == 2) && (paramInt1 > 0)) {
        mState = 1;
      }
      if (paramInt1 > 0) {
        paramByteBuffer.position(paramByteBuffer.position() + paramInt1);
      }
      return paramInt1;
    }
    paramByteBuffer = new StringBuilder();
    paramByteBuffer.append("AudioTrack.write() called with invalid size (");
    paramByteBuffer.append(paramInt1);
    paramByteBuffer.append(") value");
    Log.e("android.media.AudioTrack", paramByteBuffer.toString());
    return -2;
  }
  
  public int write(ByteBuffer paramByteBuffer, int paramInt1, int paramInt2, long paramLong)
  {
    if (mState == 0)
    {
      Log.e("android.media.AudioTrack", "AudioTrack.write() called in invalid state STATE_UNINITIALIZED");
      return -3;
    }
    if ((paramInt2 != 0) && (paramInt2 != 1))
    {
      Log.e("android.media.AudioTrack", "AudioTrack.write() called with invalid blocking mode");
      return -2;
    }
    if (mDataLoadMode != 1)
    {
      Log.e("android.media.AudioTrack", "AudioTrack.write() with timestamp called for non-streaming mode track");
      return -3;
    }
    if ((mAttributes.getFlags() & 0x10) == 0)
    {
      Log.d("android.media.AudioTrack", "AudioTrack.write() called on a regular AudioTrack. Ignoring pts...");
      return write(paramByteBuffer, paramInt1, paramInt2);
    }
    if ((paramByteBuffer != null) && (paramInt1 >= 0) && (paramInt1 <= paramByteBuffer.remaining()))
    {
      if (mAvSyncHeader == null)
      {
        mAvSyncHeader = ByteBuffer.allocate(mOffset);
        mAvSyncHeader.order(ByteOrder.BIG_ENDIAN);
        mAvSyncHeader.putInt(1431633922);
      }
      if (mAvSyncBytesRemaining == 0)
      {
        mAvSyncHeader.putInt(4, paramInt1);
        mAvSyncHeader.putLong(8, paramLong);
        mAvSyncHeader.putInt(16, mOffset);
        mAvSyncHeader.position(0);
        mAvSyncBytesRemaining = paramInt1;
      }
      if (mAvSyncHeader.remaining() != 0)
      {
        int i = write(mAvSyncHeader, mAvSyncHeader.remaining(), paramInt2);
        if (i < 0)
        {
          Log.e("android.media.AudioTrack", "AudioTrack.write() could not write timestamp header!");
          mAvSyncHeader = null;
          mAvSyncBytesRemaining = 0;
          return i;
        }
        if (mAvSyncHeader.remaining() > 0)
        {
          Log.v("android.media.AudioTrack", "AudioTrack.write() partial timestamp header written.");
          return 0;
        }
      }
      paramInt1 = write(paramByteBuffer, Math.min(mAvSyncBytesRemaining, paramInt1), paramInt2);
      if (paramInt1 < 0)
      {
        Log.e("android.media.AudioTrack", "AudioTrack.write() could not write audio data!");
        mAvSyncHeader = null;
        mAvSyncBytesRemaining = 0;
        return paramInt1;
      }
      mAvSyncBytesRemaining -= paramInt1;
      return paramInt1;
    }
    paramByteBuffer = new StringBuilder();
    paramByteBuffer.append("AudioTrack.write() called with invalid size (");
    paramByteBuffer.append(paramInt1);
    paramByteBuffer.append(") value");
    Log.e("android.media.AudioTrack", paramByteBuffer.toString());
    return -2;
  }
  
  public int write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    return write(paramArrayOfByte, paramInt1, paramInt2, 0);
  }
  
  public int write(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3)
  {
    if ((mState != 0) && (mAudioFormat != 4))
    {
      if ((paramInt3 != 0) && (paramInt3 != 1))
      {
        Log.e("android.media.AudioTrack", "AudioTrack.write() called with invalid blocking mode");
        return -2;
      }
      if ((paramArrayOfByte != null) && (paramInt1 >= 0) && (paramInt2 >= 0) && (paramInt1 + paramInt2 >= 0) && (paramInt1 + paramInt2 <= paramArrayOfByte.length))
      {
        int i = mAudioFormat;
        boolean bool;
        if (paramInt3 == 0) {
          bool = true;
        } else {
          bool = false;
        }
        paramInt1 = native_write_byte(paramArrayOfByte, paramInt1, paramInt2, i, bool);
        if ((mDataLoadMode == 0) && (mState == 2) && (paramInt1 > 0)) {
          mState = 1;
        }
        return paramInt1;
      }
      return -2;
    }
    return -3;
  }
  
  public int write(float[] paramArrayOfFloat, int paramInt1, int paramInt2, int paramInt3)
  {
    if (mState == 0)
    {
      Log.e("android.media.AudioTrack", "AudioTrack.write() called in invalid state STATE_UNINITIALIZED");
      return -3;
    }
    if (mAudioFormat != 4)
    {
      Log.e("android.media.AudioTrack", "AudioTrack.write(float[] ...) requires format ENCODING_PCM_FLOAT");
      return -3;
    }
    if ((paramInt3 != 0) && (paramInt3 != 1))
    {
      Log.e("android.media.AudioTrack", "AudioTrack.write() called with invalid blocking mode");
      return -2;
    }
    if ((paramArrayOfFloat != null) && (paramInt1 >= 0) && (paramInt2 >= 0) && (paramInt1 + paramInt2 >= 0) && (paramInt1 + paramInt2 <= paramArrayOfFloat.length))
    {
      int i = mAudioFormat;
      boolean bool;
      if (paramInt3 == 0) {
        bool = true;
      } else {
        bool = false;
      }
      paramInt1 = native_write_float(paramArrayOfFloat, paramInt1, paramInt2, i, bool);
      if ((mDataLoadMode == 0) && (mState == 2) && (paramInt1 > 0)) {
        mState = 1;
      }
      return paramInt1;
    }
    Log.e("android.media.AudioTrack", "AudioTrack.write() called with invalid array, offset, or size");
    return -2;
  }
  
  public int write(short[] paramArrayOfShort, int paramInt1, int paramInt2)
  {
    return write(paramArrayOfShort, paramInt1, paramInt2, 0);
  }
  
  public int write(short[] paramArrayOfShort, int paramInt1, int paramInt2, int paramInt3)
  {
    if ((mState != 0) && (mAudioFormat != 4))
    {
      if ((paramInt3 != 0) && (paramInt3 != 1))
      {
        Log.e("android.media.AudioTrack", "AudioTrack.write() called with invalid blocking mode");
        return -2;
      }
      if ((paramArrayOfShort != null) && (paramInt1 >= 0) && (paramInt2 >= 0) && (paramInt1 + paramInt2 >= 0) && (paramInt1 + paramInt2 <= paramArrayOfShort.length))
      {
        int i = mAudioFormat;
        boolean bool;
        if (paramInt3 == 0) {
          bool = true;
        } else {
          bool = false;
        }
        paramInt1 = native_write_short(paramArrayOfShort, paramInt1, paramInt2, i, bool);
        if ((mDataLoadMode == 0) && (mState == 2) && (paramInt1 > 0)) {
          mState = 1;
        }
        return paramInt1;
      }
      return -2;
    }
    return -3;
  }
  
  public static class Builder
  {
    private AudioAttributes mAttributes;
    private int mBufferSizeInBytes;
    private AudioFormat mFormat;
    private int mMode = 1;
    private boolean mOffload = false;
    private int mPerformanceMode = 0;
    private int mSessionId = 0;
    
    public Builder() {}
    
    public AudioTrack build()
      throws UnsupportedOperationException
    {
      if (mAttributes == null) {
        mAttributes = new AudioAttributes.Builder().setUsage(1).build();
      }
      switch (mPerformanceMode)
      {
      default: 
        break;
      case 1: 
        mAttributes = new AudioAttributes.Builder(mAttributes).replaceFlags((mAttributes.getAllFlags() | 0x100) & 0xFDFF).build();
        break;
      case 0: 
        if (!AudioTrack.shouldEnablePowerSaving(mAttributes, mFormat, mBufferSizeInBytes, mMode)) {
          break;
        }
      case 2: 
        mAttributes = new AudioAttributes.Builder(mAttributes).replaceFlags((mAttributes.getAllFlags() | 0x200) & 0xFEFF).build();
      }
      if (mFormat == null) {
        mFormat = new AudioFormat.Builder().setChannelMask(12).setEncoding(1).build();
      }
      if (mOffload) {
        if (mAttributes.getUsage() == 1)
        {
          if (!AudioSystem.isOffloadSupported(mFormat)) {
            throw new UnsupportedOperationException("Cannot create AudioTrack, offload format not supported");
          }
        }
        else {
          throw new UnsupportedOperationException("Cannot create AudioTrack, offload requires USAGE_MEDIA");
        }
      }
      try
      {
        if ((mMode == 1) && (mBufferSizeInBytes == 0))
        {
          int i = mFormat.getChannelCount();
          localObject = mFormat;
          mBufferSizeInBytes = (i * AudioFormat.getBytesPerSample(mFormat.getEncoding()));
        }
        Object localObject = new android/media/AudioTrack;
        ((AudioTrack)localObject).<init>(mAttributes, mFormat, mBufferSizeInBytes, mMode, mSessionId, mOffload, null);
        if (((AudioTrack)localObject).getState() != 0) {
          return localObject;
        }
        localObject = new java/lang/UnsupportedOperationException;
        ((UnsupportedOperationException)localObject).<init>("Cannot create AudioTrack");
        throw ((Throwable)localObject);
      }
      catch (IllegalArgumentException localIllegalArgumentException)
      {
        throw new UnsupportedOperationException(localIllegalArgumentException.getMessage());
      }
    }
    
    public Builder setAudioAttributes(AudioAttributes paramAudioAttributes)
      throws IllegalArgumentException
    {
      if (paramAudioAttributes != null)
      {
        mAttributes = paramAudioAttributes;
        return this;
      }
      throw new IllegalArgumentException("Illegal null AudioAttributes argument");
    }
    
    public Builder setAudioFormat(AudioFormat paramAudioFormat)
      throws IllegalArgumentException
    {
      if (paramAudioFormat != null)
      {
        mFormat = paramAudioFormat;
        return this;
      }
      throw new IllegalArgumentException("Illegal null AudioFormat argument");
    }
    
    public Builder setBufferSizeInBytes(int paramInt)
      throws IllegalArgumentException
    {
      if (paramInt > 0)
      {
        mBufferSizeInBytes = paramInt;
        return this;
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Invalid buffer size ");
      localStringBuilder.append(paramInt);
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
    
    public Builder setOffloadedPlayback(boolean paramBoolean)
    {
      mOffload = paramBoolean;
      return this;
    }
    
    public Builder setPerformanceMode(int paramInt)
    {
      switch (paramInt)
      {
      default: 
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Invalid performance mode ");
        localStringBuilder.append(paramInt);
        throw new IllegalArgumentException(localStringBuilder.toString());
      }
      mPerformanceMode = paramInt;
      return this;
    }
    
    public Builder setSessionId(int paramInt)
      throws IllegalArgumentException
    {
      if ((paramInt != 0) && (paramInt < 1))
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Invalid audio session ID ");
        localStringBuilder.append(paramInt);
        throw new IllegalArgumentException(localStringBuilder.toString());
      }
      mSessionId = paramInt;
      return this;
    }
    
    public Builder setTransferMode(int paramInt)
      throws IllegalArgumentException
    {
      switch (paramInt)
      {
      default: 
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Invalid transfer mode ");
        localStringBuilder.append(paramInt);
        throw new IllegalArgumentException(localStringBuilder.toString());
      }
      mMode = paramInt;
      return this;
    }
  }
  
  public static final class MetricsConstants
  {
    public static final String CHANNELMASK = "android.media.audiorecord.channelmask";
    public static final String CONTENTTYPE = "android.media.audiotrack.type";
    public static final String SAMPLERATE = "android.media.audiorecord.samplerate";
    public static final String STREAMTYPE = "android.media.audiotrack.streamtype";
    public static final String USAGE = "android.media.audiotrack.usage";
    
    private MetricsConstants() {}
  }
  
  private class NativePositionEventHandlerDelegate
  {
    private final Handler mHandler;
    
    NativePositionEventHandlerDelegate(final AudioTrack paramAudioTrack, final AudioTrack.OnPlaybackPositionUpdateListener paramOnPlaybackPositionUpdateListener, Handler paramHandler)
    {
      if (paramHandler != null) {
        paramHandler = paramHandler.getLooper();
      } else {
        paramHandler = mInitializationLooper;
      }
      if (paramHandler != null) {
        mHandler = new Handler(paramHandler)
        {
          public void handleMessage(Message paramAnonymousMessage)
          {
            if (paramAudioTrack == null) {
              return;
            }
            switch (what)
            {
            default: 
              StringBuilder localStringBuilder = new StringBuilder();
              localStringBuilder.append("Unknown native event type: ");
              localStringBuilder.append(what);
              AudioTrack.loge(localStringBuilder.toString());
              break;
            case 4: 
              if (paramOnPlaybackPositionUpdateListener != null) {
                paramOnPlaybackPositionUpdateListener.onPeriodicNotification(paramAudioTrack);
              }
              break;
            case 3: 
              if (paramOnPlaybackPositionUpdateListener != null) {
                paramOnPlaybackPositionUpdateListener.onMarkerReached(paramAudioTrack);
              }
              break;
            }
          }
        };
      } else {
        mHandler = null;
      }
    }
    
    Handler getHandler()
    {
      return mHandler;
    }
  }
  
  public static abstract interface OnPlaybackPositionUpdateListener
  {
    public abstract void onMarkerReached(AudioTrack paramAudioTrack);
    
    public abstract void onPeriodicNotification(AudioTrack paramAudioTrack);
  }
  
  @Deprecated
  public static abstract interface OnRoutingChangedListener
    extends AudioRouting.OnRoutingChangedListener
  {
    public void onRoutingChanged(AudioRouting paramAudioRouting)
    {
      if ((paramAudioRouting instanceof AudioTrack)) {
        onRoutingChanged((AudioTrack)paramAudioRouting);
      }
    }
    
    public abstract void onRoutingChanged(AudioTrack paramAudioTrack);
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface PerformanceMode {}
  
  public static abstract class StreamEventCallback
  {
    public StreamEventCallback() {}
    
    public void onStreamDataRequest(AudioTrack paramAudioTrack) {}
    
    public void onStreamPresentationEnd(AudioTrack paramAudioTrack) {}
    
    public void onTearDown(AudioTrack paramAudioTrack) {}
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface TransferMode {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface WriteMode {}
}
