package android.media;

import android.annotation.SystemApi;
import android.app.ActivityThread;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.ArrayMap;
import android.util.Log;
import android.util.Pair;
import android.util.SeempLog;
import com.android.internal.annotations.GuardedBy;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class AudioRecord
  implements AudioRouting
{
  private static final int AUDIORECORD_ERROR_SETUP_INVALIDCHANNELMASK = -17;
  private static final int AUDIORECORD_ERROR_SETUP_INVALIDFORMAT = -18;
  private static final int AUDIORECORD_ERROR_SETUP_INVALIDSOURCE = -19;
  private static final int AUDIORECORD_ERROR_SETUP_NATIVEINITFAILED = -20;
  private static final int AUDIORECORD_ERROR_SETUP_ZEROFRAMECOUNT = -16;
  public static final int ERROR = -1;
  public static final int ERROR_BAD_VALUE = -2;
  public static final int ERROR_DEAD_OBJECT = -6;
  public static final int ERROR_INVALID_OPERATION = -3;
  private static final int NATIVE_EVENT_MARKER = 2;
  private static final int NATIVE_EVENT_NEW_POS = 3;
  public static final int READ_BLOCKING = 0;
  public static final int READ_NON_BLOCKING = 1;
  public static final int RECORDSTATE_RECORDING = 3;
  public static final int RECORDSTATE_STOPPED = 1;
  public static final int STATE_INITIALIZED = 1;
  public static final int STATE_UNINITIALIZED = 0;
  public static final String SUBMIX_FIXED_VOLUME = "fixedVolume";
  public static final int SUCCESS = 0;
  private static final String TAG = "android.media.AudioRecord";
  private AudioAttributes mAudioAttributes;
  private int mAudioFormat;
  private int mChannelCount;
  private int mChannelIndexMask;
  private int mChannelMask;
  private NativeEventHandler mEventHandler = null;
  private final IBinder mICallBack = new Binder();
  private Looper mInitializationLooper = null;
  private boolean mIsSubmixFullVolume = false;
  private int mNativeBufferSizeInBytes = 0;
  private long mNativeCallbackCookie;
  private long mNativeDeviceCallback;
  private long mNativeRecorderInJavaObj;
  private OnRecordPositionUpdateListener mPositionListener = null;
  private final Object mPositionListenerLock = new Object();
  private AudioDeviceInfo mPreferredDevice = null;
  private int mRecordSource;
  private int mRecordingState = 1;
  private final Object mRecordingStateLock = new Object();
  @GuardedBy("mRoutingChangeListeners")
  private ArrayMap<AudioRouting.OnRoutingChangedListener, NativeRoutingEventHandlerDelegate> mRoutingChangeListeners = new ArrayMap();
  private int mSampleRate;
  private int mSessionId = 0;
  private int mState = 0;
  
  public AudioRecord(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
    throws IllegalArgumentException
  {
    this(new AudioAttributes.Builder().setInternalCapturePreset(paramInt1).build(), new AudioFormat.Builder().setChannelMask(getChannelMaskFromLegacyConfig(paramInt3, true)).setEncoding(paramInt4).setSampleRate(paramInt2).build(), paramInt5, 0);
  }
  
  AudioRecord(long paramLong)
  {
    mNativeRecorderInJavaObj = 0L;
    mNativeCallbackCookie = 0L;
    mNativeDeviceCallback = 0L;
    if (paramLong != 0L) {
      deferred_connect(paramLong);
    } else {
      mState = 0;
    }
  }
  
  @SystemApi
  public AudioRecord(AudioAttributes paramAudioAttributes, AudioFormat paramAudioFormat, int paramInt1, int paramInt2)
    throws IllegalArgumentException
  {
    mRecordingState = 1;
    if (paramAudioAttributes != null)
    {
      if (paramAudioFormat != null)
      {
        Object localObject = Looper.myLooper();
        mInitializationLooper = ((Looper)localObject);
        if (localObject == null) {
          mInitializationLooper = Looper.getMainLooper();
        }
        if (paramAudioAttributes.getCapturePreset() == 8)
        {
          localObject = new AudioAttributes.Builder();
          Iterator localIterator = paramAudioAttributes.getTags().iterator();
          while (localIterator.hasNext())
          {
            String str = (String)localIterator.next();
            if (str.equalsIgnoreCase("fixedVolume"))
            {
              mIsSubmixFullVolume = true;
              Log.v("android.media.AudioRecord", "Will record from REMOTE_SUBMIX at full fixed volume");
            }
            else
            {
              ((AudioAttributes.Builder)localObject).addTag(str);
            }
          }
          ((AudioAttributes.Builder)localObject).setInternalCapturePreset(paramAudioAttributes.getCapturePreset());
          mAudioAttributes = ((AudioAttributes.Builder)localObject).build();
        }
        else
        {
          mAudioAttributes = paramAudioAttributes;
        }
        int i = paramAudioFormat.getSampleRate();
        int j = i;
        if (i == 0) {
          j = 0;
        }
        i = 1;
        if ((paramAudioFormat.getPropertySetMask() & 0x1) != 0) {
          i = paramAudioFormat.getEncoding();
        }
        audioParamCheck(paramAudioAttributes.getCapturePreset(), j, i);
        if ((paramAudioFormat.getPropertySetMask() & 0x8) != 0)
        {
          mChannelIndexMask = paramAudioFormat.getChannelIndexMask();
          mChannelCount = paramAudioFormat.getChannelCount();
        }
        if ((paramAudioFormat.getPropertySetMask() & 0x4) != 0)
        {
          mChannelMask = getChannelMaskFromLegacyConfig(paramAudioFormat.getChannelMask(), false);
          mChannelCount = paramAudioFormat.getChannelCount();
        }
        else if (mChannelIndexMask == 0)
        {
          mChannelMask = getChannelMaskFromLegacyConfig(1, false);
          mChannelCount = AudioFormat.channelCountFromInChannelMask(mChannelMask);
        }
        audioBuffSizeCheck(paramInt1);
        paramAudioAttributes = new int[1];
        paramAudioAttributes[0] = mSampleRate;
        paramAudioFormat = new int[1];
        paramAudioFormat[0] = paramInt2;
        paramInt1 = native_setup(new WeakReference(this), mAudioAttributes, paramAudioAttributes, mChannelMask, mChannelIndexMask, mAudioFormat, mNativeBufferSizeInBytes, paramAudioFormat, ActivityThread.currentOpPackageName(), 0L);
        if (paramInt1 != 0)
        {
          paramAudioAttributes = new StringBuilder();
          paramAudioAttributes.append("Error code ");
          paramAudioAttributes.append(paramInt1);
          paramAudioAttributes.append(" when initializing native AudioRecord object.");
          loge(paramAudioAttributes.toString());
          return;
        }
        mSampleRate = paramAudioAttributes[0];
        mSessionId = paramAudioFormat[0];
        mState = 1;
        return;
      }
      throw new IllegalArgumentException("Illegal null AudioFormat");
    }
    throw new IllegalArgumentException("Illegal null AudioAttributes");
  }
  
  private void audioBuffSizeCheck(int paramInt)
    throws IllegalArgumentException
  {
    int i = mChannelCount * AudioFormat.getBytesPerSample(mAudioFormat);
    if ((paramInt % i == 0) && (paramInt >= 1))
    {
      mNativeBufferSizeInBytes = paramInt;
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Invalid audio buffer size ");
    localStringBuilder.append(paramInt);
    localStringBuilder.append(" (frame size ");
    localStringBuilder.append(i);
    localStringBuilder.append(")");
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  private void audioParamCheck(int paramInt1, int paramInt2, int paramInt3)
    throws IllegalArgumentException
  {
    if ((paramInt1 >= 0) && ((paramInt1 <= MediaRecorder.getAudioSourceMax()) || (paramInt1 == 1998) || (paramInt1 == 1999)))
    {
      mRecordSource = paramInt1;
      if (((paramInt2 >= 4000) && (paramInt2 <= 192000)) || (paramInt2 == 0))
      {
        mSampleRate = paramInt2;
        switch (paramInt3)
        {
        default: 
          switch (paramInt3)
          {
          default: 
            localStringBuilder = new StringBuilder();
            localStringBuilder.append("Unsupported sample encoding ");
            localStringBuilder.append(paramInt3);
            localStringBuilder.append(". Should be ENCODING_PCM_8BIT, ENCODING_PCM_16BIT, or ENCODING_PCM_FLOAT.");
            throw new IllegalArgumentException(localStringBuilder.toString());
          }
        case 2: 
        case 3: 
        case 4: 
          mAudioFormat = paramInt3;
          break;
        case 1: 
          mAudioFormat = 2;
        }
        return;
      }
      localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramInt2);
      localStringBuilder.append("Hz is not a supported sample rate.");
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Invalid audio source ");
    localStringBuilder.append(paramInt1);
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
  
  private static int getChannelMaskFromLegacyConfig(int paramInt, boolean paramBoolean)
  {
    int i;
    if (paramInt != 12)
    {
      if (paramInt != 16) {
        if (paramInt == 48) {}
      }
      switch (paramInt)
      {
      default: 
        throw new IllegalArgumentException("Unsupported channel configuration.");
        i = paramInt;
        break;
      case 1: 
      case 2: 
        i = 16;
        break;
      }
    }
    else
    {
      i = 12;
    }
    if ((!paramBoolean) && ((paramInt == 2) || (paramInt == 3))) {
      throw new IllegalArgumentException("Unsupported deprecated configuration.");
    }
    return i;
  }
  
  public static int getMinBufferSize(int paramInt1, int paramInt2, int paramInt3)
  {
    if (paramInt2 != 12)
    {
      if (paramInt2 != 16)
      {
        if (paramInt2 == 48) {
          break label72;
        }
        if (paramInt2 == 252) {}
      }
      switch (paramInt2)
      {
      default: 
        loge("getMinBufferSize(): Invalid channel configuration.");
        return -2;
        paramInt2 = 6;
        break;
      case 1: 
      case 2: 
        paramInt2 = 1;
        break;
      }
    }
    label72:
    paramInt2 = 2;
    paramInt1 = native_get_min_buff_size(paramInt1, paramInt2, paramInt3);
    if (paramInt1 == 0) {
      return -2;
    }
    if (paramInt1 == -1) {
      return -1;
    }
    return paramInt1;
  }
  
  private void handleFullVolumeRec(boolean paramBoolean)
  {
    if (!mIsSubmixFullVolume) {
      return;
    }
    IAudioService localIAudioService = IAudioService.Stub.asInterface(ServiceManager.getService("audio"));
    try
    {
      localIAudioService.forceRemoteSubmixFullVolume(paramBoolean, mICallBack);
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("android.media.AudioRecord", "Error talking to AudioService when handling full submix volume", localRemoteException);
    }
  }
  
  private static void logd(String paramString)
  {
    Log.d("android.media.AudioRecord", paramString);
  }
  
  private static void loge(String paramString)
  {
    Log.e("android.media.AudioRecord", paramString);
  }
  
  private final native void native_disableDeviceCallback();
  
  private final native void native_enableDeviceCallback();
  
  private final native void native_finalize();
  
  private native PersistableBundle native_getMetrics();
  
  private final native int native_getRoutedDeviceId();
  
  private final native int native_get_active_microphones(ArrayList<MicrophoneInfo> paramArrayList);
  
  private final native int native_get_buffer_size_in_frames();
  
  private final native int native_get_marker_pos();
  
  private static final native int native_get_min_buff_size(int paramInt1, int paramInt2, int paramInt3);
  
  private final native int native_get_pos_update_period();
  
  private final native int native_get_timestamp(AudioTimestamp paramAudioTimestamp, int paramInt);
  
  private final native int native_read_in_byte_array(byte[] paramArrayOfByte, int paramInt1, int paramInt2, boolean paramBoolean);
  
  private final native int native_read_in_direct_buffer(Object paramObject, int paramInt, boolean paramBoolean);
  
  private final native int native_read_in_float_array(float[] paramArrayOfFloat, int paramInt1, int paramInt2, boolean paramBoolean);
  
  private final native int native_read_in_short_array(short[] paramArrayOfShort, int paramInt1, int paramInt2, boolean paramBoolean);
  
  private final native boolean native_setInputDevice(int paramInt);
  
  private final native int native_set_marker_pos(int paramInt);
  
  private final native int native_set_pos_update_period(int paramInt);
  
  private final native int native_setup(Object paramObject1, Object paramObject2, int[] paramArrayOfInt1, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt2, String paramString, long paramLong);
  
  private final native int native_start(int paramInt1, int paramInt2);
  
  private final native void native_stop();
  
  private static void postEventFromNative(Object paramObject1, int paramInt1, int paramInt2, int paramInt3, Object paramObject2)
  {
    paramObject1 = (AudioRecord)((WeakReference)paramObject1).get();
    if (paramObject1 == null) {
      return;
    }
    if (paramInt1 == 1000)
    {
      paramObject1.broadcastRoutingChange();
      return;
    }
    if (mEventHandler != null)
    {
      paramObject2 = mEventHandler.obtainMessage(paramInt1, paramInt2, paramInt3, paramObject2);
      mEventHandler.sendMessage(paramObject2);
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
  
  @Deprecated
  public void addOnRoutingChangedListener(OnRoutingChangedListener paramOnRoutingChangedListener, Handler paramHandler)
  {
    addOnRoutingChangedListener(paramOnRoutingChangedListener, paramHandler);
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
  
  void deferred_connect(long paramLong)
  {
    if (mState != 1)
    {
      int[] arrayOfInt = new int[1];
      arrayOfInt[0] = 0;
      WeakReference localWeakReference = new WeakReference(this);
      Object localObject = ActivityThread.currentOpPackageName();
      int i = native_setup(localWeakReference, null, new int[] { 0 }, 0, 0, 0, 0, arrayOfInt, (String)localObject, paramLong);
      if (i != 0)
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("Error code ");
        ((StringBuilder)localObject).append(i);
        ((StringBuilder)localObject).append(" when initializing native AudioRecord object.");
        loge(((StringBuilder)localObject).toString());
        return;
      }
      mSessionId = arrayOfInt[0];
      mState = 1;
    }
  }
  
  protected void finalize()
  {
    release();
  }
  
  public List<MicrophoneInfo> getActiveMicrophones()
    throws IOException
  {
    ArrayList localArrayList = new ArrayList();
    int i = native_get_active_microphones(localArrayList);
    Object localObject;
    if (i != 0)
    {
      if (i != -3)
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("getActiveMicrophones failed:");
        ((StringBuilder)localObject).append(i);
        Log.e("android.media.AudioRecord", ((StringBuilder)localObject).toString());
      }
      Log.i("android.media.AudioRecord", "getActiveMicrophones failed, fallback on routed device info");
    }
    AudioManager.setPortIdForMicrophones(localArrayList);
    if (localArrayList.size() == 0)
    {
      localObject = getRoutedDevice();
      if (localObject != null)
      {
        MicrophoneInfo localMicrophoneInfo = AudioManager.microphoneInfoFromAudioDeviceInfo((AudioDeviceInfo)localObject);
        localObject = new ArrayList();
        for (i = 0; i < mChannelCount; i++) {
          ((ArrayList)localObject).add(new Pair(Integer.valueOf(i), Integer.valueOf(1)));
        }
        localMicrophoneInfo.setChannelMapping((List)localObject);
        localArrayList.add(localMicrophoneInfo);
      }
    }
    return localArrayList;
  }
  
  public int getAudioFormat()
  {
    return mAudioFormat;
  }
  
  public int getAudioSessionId()
  {
    return mSessionId;
  }
  
  public int getAudioSource()
  {
    return mRecordSource;
  }
  
  public int getBufferSizeInFrames()
  {
    return native_get_buffer_size_in_frames();
  }
  
  public int getChannelConfiguration()
  {
    return mChannelMask;
  }
  
  public int getChannelCount()
  {
    return mChannelCount;
  }
  
  public AudioFormat getFormat()
  {
    AudioFormat.Builder localBuilder = new AudioFormat.Builder().setSampleRate(mSampleRate).setEncoding(mAudioFormat);
    if (mChannelMask != 0) {
      localBuilder.setChannelMask(mChannelMask);
    }
    if (mChannelIndexMask != 0) {
      localBuilder.setChannelIndexMask(mChannelIndexMask);
    }
    return localBuilder.build();
  }
  
  public PersistableBundle getMetrics()
  {
    return native_getMetrics();
  }
  
  public int getNotificationMarkerPosition()
  {
    return native_get_marker_pos();
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
  
  public int getRecordingState()
  {
    synchronized (mRecordingStateLock)
    {
      int i = mRecordingState;
      return i;
    }
  }
  
  public AudioDeviceInfo getRoutedDevice()
  {
    int i = native_getRoutedDeviceId();
    if (i == 0) {
      return null;
    }
    AudioDeviceInfo[] arrayOfAudioDeviceInfo = AudioManager.getDevicesStatic(1);
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
  
  public int getTimestamp(AudioTimestamp paramAudioTimestamp, int paramInt)
  {
    if ((paramAudioTimestamp != null) && ((paramInt == 1) || (paramInt == 0))) {
      return native_get_timestamp(paramAudioTimestamp, paramInt);
    }
    throw new IllegalArgumentException();
  }
  
  public final native void native_release();
  
  public int read(ByteBuffer paramByteBuffer, int paramInt)
  {
    return read(paramByteBuffer, paramInt, 0);
  }
  
  public int read(ByteBuffer paramByteBuffer, int paramInt1, int paramInt2)
  {
    int i = mState;
    boolean bool = true;
    if (i != 1) {
      return -3;
    }
    if ((paramInt2 != 0) && (paramInt2 != 1))
    {
      Log.e("android.media.AudioRecord", "AudioRecord.read() called with invalid blocking mode");
      return -2;
    }
    if ((paramByteBuffer != null) && (paramInt1 >= 0))
    {
      if (paramInt2 != 0) {
        bool = false;
      }
      return native_read_in_direct_buffer(paramByteBuffer, paramInt1, bool);
    }
    return -2;
  }
  
  public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    return read(paramArrayOfByte, paramInt1, paramInt2, 0);
  }
  
  public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3)
  {
    int i = mState;
    boolean bool = true;
    if ((i == 1) && (mAudioFormat != 4))
    {
      if ((paramInt3 != 0) && (paramInt3 != 1))
      {
        Log.e("android.media.AudioRecord", "AudioRecord.read() called with invalid blocking mode");
        return -2;
      }
      if ((paramArrayOfByte != null) && (paramInt1 >= 0) && (paramInt2 >= 0) && (paramInt1 + paramInt2 >= 0) && (paramInt1 + paramInt2 <= paramArrayOfByte.length))
      {
        if (paramInt3 != 0) {
          bool = false;
        }
        return native_read_in_byte_array(paramArrayOfByte, paramInt1, paramInt2, bool);
      }
      return -2;
    }
    return -3;
  }
  
  public int read(float[] paramArrayOfFloat, int paramInt1, int paramInt2, int paramInt3)
  {
    if (mState == 0)
    {
      Log.e("android.media.AudioRecord", "AudioRecord.read() called in invalid state STATE_UNINITIALIZED");
      return -3;
    }
    if (mAudioFormat != 4)
    {
      Log.e("android.media.AudioRecord", "AudioRecord.read(float[] ...) requires format ENCODING_PCM_FLOAT");
      return -3;
    }
    boolean bool = true;
    if ((paramInt3 != 0) && (paramInt3 != 1))
    {
      Log.e("android.media.AudioRecord", "AudioRecord.read() called with invalid blocking mode");
      return -2;
    }
    if ((paramArrayOfFloat != null) && (paramInt1 >= 0) && (paramInt2 >= 0) && (paramInt1 + paramInt2 >= 0) && (paramInt1 + paramInt2 <= paramArrayOfFloat.length))
    {
      if (paramInt3 != 0) {
        bool = false;
      }
      return native_read_in_float_array(paramArrayOfFloat, paramInt1, paramInt2, bool);
    }
    return -2;
  }
  
  public int read(short[] paramArrayOfShort, int paramInt1, int paramInt2)
  {
    return read(paramArrayOfShort, paramInt1, paramInt2, 0);
  }
  
  public int read(short[] paramArrayOfShort, int paramInt1, int paramInt2, int paramInt3)
  {
    int i = mState;
    boolean bool = true;
    if ((i == 1) && (mAudioFormat != 4))
    {
      if ((paramInt3 != 0) && (paramInt3 != 1))
      {
        Log.e("android.media.AudioRecord", "AudioRecord.read() called with invalid blocking mode");
        return -2;
      }
      if ((paramArrayOfShort != null) && (paramInt1 >= 0) && (paramInt2 >= 0) && (paramInt1 + paramInt2 >= 0) && (paramInt1 + paramInt2 <= paramArrayOfShort.length))
      {
        if (paramInt3 != 0) {
          bool = false;
        }
        return native_read_in_short_array(paramArrayOfShort, paramInt1, paramInt2, bool);
      }
      return -2;
    }
    return -3;
  }
  
  public void release()
  {
    try
    {
      stop();
    }
    catch (IllegalStateException localIllegalStateException) {}
    native_release();
    mState = 0;
  }
  
  @Deprecated
  public void removeOnRoutingChangedListener(OnRoutingChangedListener paramOnRoutingChangedListener)
  {
    removeOnRoutingChangedListener(paramOnRoutingChangedListener);
  }
  
  public void removeOnRoutingChangedListener(AudioRouting.OnRoutingChangedListener paramOnRoutingChangedListener)
  {
    synchronized (mRoutingChangeListeners)
    {
      if (mRoutingChangeListeners.containsKey(paramOnRoutingChangedListener))
      {
        mRoutingChangeListeners.remove(paramOnRoutingChangedListener);
        testDisableNativeRoutingCallbacksLocked();
      }
      return;
    }
  }
  
  public int setNotificationMarkerPosition(int paramInt)
  {
    if (mState == 0) {
      return -3;
    }
    return native_set_marker_pos(paramInt);
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
    if ((paramAudioDeviceInfo != null) && (!paramAudioDeviceInfo.isSource())) {
      return false;
    }
    if (paramAudioDeviceInfo != null) {
      i = paramAudioDeviceInfo.getId();
    }
    boolean bool = native_setInputDevice(i);
    if (bool == true) {
      try
      {
        mPreferredDevice = paramAudioDeviceInfo;
      }
      finally {}
    }
    return bool;
  }
  
  public void setRecordPositionUpdateListener(OnRecordPositionUpdateListener paramOnRecordPositionUpdateListener)
  {
    setRecordPositionUpdateListener(paramOnRecordPositionUpdateListener, null);
  }
  
  public void setRecordPositionUpdateListener(OnRecordPositionUpdateListener paramOnRecordPositionUpdateListener, Handler paramHandler)
  {
    synchronized (mPositionListenerLock)
    {
      mPositionListener = paramOnRecordPositionUpdateListener;
      if (paramOnRecordPositionUpdateListener != null)
      {
        if (paramHandler != null)
        {
          paramOnRecordPositionUpdateListener = new android/media/AudioRecord$NativeEventHandler;
          paramOnRecordPositionUpdateListener.<init>(this, this, paramHandler.getLooper());
          mEventHandler = paramOnRecordPositionUpdateListener;
        }
        else
        {
          paramOnRecordPositionUpdateListener = new android/media/AudioRecord$NativeEventHandler;
          paramOnRecordPositionUpdateListener.<init>(this, this, mInitializationLooper);
          mEventHandler = paramOnRecordPositionUpdateListener;
        }
      }
      else {
        mEventHandler = null;
      }
      return;
    }
  }
  
  public void startRecording()
    throws IllegalStateException
  {
    SeempLog.record(70);
    if (mState == 1) {
      synchronized (mRecordingStateLock)
      {
        if (native_start(0, 0) == 0)
        {
          handleFullVolumeRec(true);
          mRecordingState = 3;
        }
        return;
      }
    }
    throw new IllegalStateException("startRecording() called on an uninitialized AudioRecord.");
  }
  
  public void startRecording(MediaSyncEvent paramMediaSyncEvent)
    throws IllegalStateException
  {
    SeempLog.record(70);
    if (mState == 1) {
      synchronized (mRecordingStateLock)
      {
        if (native_start(paramMediaSyncEvent.getType(), paramMediaSyncEvent.getAudioSessionId()) == 0)
        {
          handleFullVolumeRec(true);
          mRecordingState = 3;
        }
        return;
      }
    }
    throw new IllegalStateException("startRecording() called on an uninitialized AudioRecord.");
  }
  
  public void stop()
    throws IllegalStateException
  {
    if (mState == 1) {
      synchronized (mRecordingStateLock)
      {
        handleFullVolumeRec(false);
        native_stop();
        mRecordingState = 1;
        return;
      }
    }
    throw new IllegalStateException("stop() called on an uninitialized AudioRecord.");
  }
  
  public static class Builder
  {
    private AudioAttributes mAttributes;
    private int mBufferSizeInBytes;
    private AudioFormat mFormat;
    private int mSessionId = 0;
    
    public Builder() {}
    
    public AudioRecord build()
      throws UnsupportedOperationException
    {
      if (mFormat == null)
      {
        mFormat = new AudioFormat.Builder().setEncoding(2).setChannelMask(16).build();
      }
      else
      {
        if (mFormat.getEncoding() == 0) {
          mFormat = new AudioFormat.Builder(mFormat).setEncoding(2).build();
        }
        if ((mFormat.getChannelMask() == 0) && (mFormat.getChannelIndexMask() == 0)) {
          mFormat = new AudioFormat.Builder(mFormat).setChannelMask(16).build();
        }
      }
      if (mAttributes == null) {
        mAttributes = new AudioAttributes.Builder().setInternalCapturePreset(0).build();
      }
      try
      {
        if (mBufferSizeInBytes == 0)
        {
          int i = mFormat.getChannelCount();
          localObject = mFormat;
          mBufferSizeInBytes = (i * AudioFormat.getBytesPerSample(mFormat.getEncoding()));
        }
        Object localObject = new android/media/AudioRecord;
        ((AudioRecord)localObject).<init>(mAttributes, mFormat, mBufferSizeInBytes, mSessionId);
        if (((AudioRecord)localObject).getState() != 0) {
          return localObject;
        }
        localObject = new java/lang/UnsupportedOperationException;
        ((UnsupportedOperationException)localObject).<init>("Cannot create AudioRecord");
        throw ((Throwable)localObject);
      }
      catch (IllegalArgumentException localIllegalArgumentException)
      {
        throw new UnsupportedOperationException(localIllegalArgumentException.getMessage());
      }
    }
    
    @SystemApi
    public Builder setAudioAttributes(AudioAttributes paramAudioAttributes)
      throws IllegalArgumentException
    {
      if (paramAudioAttributes != null)
      {
        if (paramAudioAttributes.getCapturePreset() != -1)
        {
          mAttributes = paramAudioAttributes;
          return this;
        }
        throw new IllegalArgumentException("No valid capture preset in AudioAttributes argument");
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
    
    public Builder setAudioSource(int paramInt)
      throws IllegalArgumentException
    {
      if ((paramInt >= 0) && (paramInt <= MediaRecorder.getAudioSourceMax()))
      {
        mAttributes = new AudioAttributes.Builder().setInternalCapturePreset(paramInt).build();
        return this;
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Invalid audio source ");
      localStringBuilder.append(paramInt);
      throw new IllegalArgumentException(localStringBuilder.toString());
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
    
    @SystemApi
    public Builder setSessionId(int paramInt)
      throws IllegalArgumentException
    {
      if (paramInt >= 0)
      {
        mSessionId = paramInt;
        return this;
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Invalid session ID ");
      localStringBuilder.append(paramInt);
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
  }
  
  public static final class MetricsConstants
  {
    public static final String CHANNELS = "android.media.audiorecord.channels";
    public static final String ENCODING = "android.media.audiorecord.encoding";
    public static final String LATENCY = "android.media.audiorecord.latency";
    public static final String SAMPLERATE = "android.media.audiorecord.samplerate";
    public static final String SOURCE = "android.media.audiorecord.source";
    
    private MetricsConstants() {}
  }
  
  private class NativeEventHandler
    extends Handler
  {
    private final AudioRecord mAudioRecord;
    
    NativeEventHandler(AudioRecord paramAudioRecord, Looper paramLooper)
    {
      super();
      mAudioRecord = paramAudioRecord;
    }
    
    public void handleMessage(Message paramMessage)
    {
      synchronized (mPositionListenerLock)
      {
        AudioRecord.OnRecordPositionUpdateListener localOnRecordPositionUpdateListener = mAudioRecord.mPositionListener;
        switch (what)
        {
        default: 
          ??? = new StringBuilder();
          ((StringBuilder)???).append("Unknown native event type: ");
          ((StringBuilder)???).append(what);
          AudioRecord.loge(((StringBuilder)???).toString());
          break;
        case 3: 
          if (localOnRecordPositionUpdateListener != null) {
            localOnRecordPositionUpdateListener.onPeriodicNotification(mAudioRecord);
          }
          break;
        case 2: 
          if (localOnRecordPositionUpdateListener != null) {
            localOnRecordPositionUpdateListener.onMarkerReached(mAudioRecord);
          }
          break;
        }
        return;
      }
    }
  }
  
  public static abstract interface OnRecordPositionUpdateListener
  {
    public abstract void onMarkerReached(AudioRecord paramAudioRecord);
    
    public abstract void onPeriodicNotification(AudioRecord paramAudioRecord);
  }
  
  @Deprecated
  public static abstract interface OnRoutingChangedListener
    extends AudioRouting.OnRoutingChangedListener
  {
    public abstract void onRoutingChanged(AudioRecord paramAudioRecord);
    
    public void onRoutingChanged(AudioRouting paramAudioRouting)
    {
      if ((paramAudioRouting instanceof AudioRecord)) {
        onRoutingChanged((AudioRecord)paramAudioRouting);
      }
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface ReadMode {}
}
