package android.media;

import android.annotation.SystemApi;
import android.app.ActivityThread;
import android.hardware.Camera;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PersistableBundle;
import android.util.ArrayMap;
import android.util.Log;
import android.util.Pair;
import android.view.Surface;
import com.android.internal.annotations.GuardedBy;
import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class MediaRecorder
  implements AudioRouting
{
  public static final int MEDIA_ERROR_SERVER_DIED = 100;
  public static final int MEDIA_RECORDER_ERROR_UNKNOWN = 1;
  public static final int MEDIA_RECORDER_INFO_MAX_DURATION_REACHED = 800;
  public static final int MEDIA_RECORDER_INFO_MAX_FILESIZE_APPROACHING = 802;
  public static final int MEDIA_RECORDER_INFO_MAX_FILESIZE_REACHED = 801;
  public static final int MEDIA_RECORDER_INFO_NEXT_OUTPUT_FILE_STARTED = 803;
  public static final int MEDIA_RECORDER_INFO_UNKNOWN = 1;
  public static final int MEDIA_RECORDER_TRACK_INFO_COMPLETION_STATUS = 1000;
  public static final int MEDIA_RECORDER_TRACK_INFO_DATA_KBYTES = 1009;
  public static final int MEDIA_RECORDER_TRACK_INFO_DURATION_MS = 1003;
  public static final int MEDIA_RECORDER_TRACK_INFO_ENCODED_FRAMES = 1005;
  public static final int MEDIA_RECORDER_TRACK_INFO_INITIAL_DELAY_MS = 1007;
  public static final int MEDIA_RECORDER_TRACK_INFO_LIST_END = 2000;
  public static final int MEDIA_RECORDER_TRACK_INFO_LIST_START = 1000;
  public static final int MEDIA_RECORDER_TRACK_INFO_MAX_CHUNK_DUR_MS = 1004;
  public static final int MEDIA_RECORDER_TRACK_INFO_PROGRESS_IN_TIME = 1001;
  public static final int MEDIA_RECORDER_TRACK_INFO_START_OFFSET_MS = 1008;
  public static final int MEDIA_RECORDER_TRACK_INFO_TYPE = 1002;
  public static final int MEDIA_RECORDER_TRACK_INTER_CHUNK_TIME_MS = 1006;
  private static final String TAG = "MediaRecorder";
  private int mChannelCount;
  private EventHandler mEventHandler;
  private FileDescriptor mFd;
  private File mFile;
  private long mNativeContext;
  private OnErrorListener mOnErrorListener;
  private OnInfoListener mOnInfoListener;
  private String mPath;
  private AudioDeviceInfo mPreferredDevice = null;
  @GuardedBy("mRoutingChangeListeners")
  private ArrayMap<AudioRouting.OnRoutingChangedListener, NativeRoutingEventHandlerDelegate> mRoutingChangeListeners = new ArrayMap();
  private Surface mSurface;
  
  static
  {
    System.loadLibrary("media_jni");
    native_init();
  }
  
  public MediaRecorder()
  {
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
    mChannelCount = 1;
    localObject = ActivityThread.currentPackageName();
    native_setup(new WeakReference(this), (String)localObject, ActivityThread.currentOpPackageName());
  }
  
  private native void _prepare()
    throws IllegalStateException, IOException;
  
  private native void _setNextOutputFile(FileDescriptor paramFileDescriptor)
    throws IllegalStateException, IOException;
  
  private native void _setOutputFile(FileDescriptor paramFileDescriptor)
    throws IllegalStateException, IOException;
  
  @GuardedBy("mRoutingChangeListeners")
  private void enableNativeRoutingCallbacksLocked(boolean paramBoolean)
  {
    if (mRoutingChangeListeners.size() == 0) {
      native_enableDeviceCallback(paramBoolean);
    }
  }
  
  public static final int getAudioSourceMax()
  {
    return 10;
  }
  
  public static boolean isSystemOnlyAudioSource(int paramInt)
  {
    switch (paramInt)
    {
    case 8: 
    default: 
      return true;
    }
    return false;
  }
  
  private final native void native_enableDeviceCallback(boolean paramBoolean);
  
  private final native void native_finalize();
  
  private final native int native_getActiveMicrophones(ArrayList<MicrophoneInfo> paramArrayList);
  
  private native PersistableBundle native_getMetrics();
  
  private final native int native_getRoutedDeviceId();
  
  private static final native void native_init();
  
  private native void native_reset();
  
  private final native boolean native_setInputDevice(int paramInt);
  
  private final native void native_setInputSurface(Surface paramSurface);
  
  private final native void native_setup(Object paramObject, String paramString1, String paramString2)
    throws IllegalStateException;
  
  private static void postEventFromNative(Object paramObject1, int paramInt1, int paramInt2, int paramInt3, Object paramObject2)
  {
    paramObject1 = (MediaRecorder)((WeakReference)paramObject1).get();
    if (paramObject1 == null) {
      return;
    }
    if (mEventHandler != null)
    {
      paramObject2 = mEventHandler.obtainMessage(paramInt1, paramInt2, paramInt3, paramObject2);
      mEventHandler.sendMessage(paramObject2);
    }
  }
  
  private native void setParameter(String paramString);
  
  public static final String toLogFriendlyAudioSource(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      switch (paramInt)
      {
      default: 
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("unknown source ");
        localStringBuilder.append(paramInt);
        return localStringBuilder.toString();
      case 1999: 
        return "HOTWORD";
      }
      return "RADIO_TUNER";
    case 9: 
      return "UNPROCESSED";
    case 8: 
      return "REMOTE_SUBMIX";
    case 7: 
      return "VOICE_COMMUNICATION";
    case 6: 
      return "VOICE_RECOGNITION";
    case 5: 
      return "CAMCORDER";
    case 4: 
      return "VOICE_CALL";
    case 3: 
      return "VOICE_DOWNLINK";
    case 2: 
      return "VOICE_UPLINK";
    case 1: 
      return "MIC";
    case 0: 
      return "DEFAULT";
    }
    return "AUDIO_SOURCE_INVALID";
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
  
  protected void finalize()
  {
    native_finalize();
  }
  
  public List<MicrophoneInfo> getActiveMicrophones()
    throws IOException
  {
    ArrayList localArrayList = new ArrayList();
    int i = native_getActiveMicrophones(localArrayList);
    Object localObject;
    if (i != 0)
    {
      if (i != -3)
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("getActiveMicrophones failed:");
        ((StringBuilder)localObject).append(i);
        Log.e("MediaRecorder", ((StringBuilder)localObject).toString());
      }
      Log.i("MediaRecorder", "getActiveMicrophones failed, fallback on routed device info");
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
  
  public native int getMaxAmplitude()
    throws IllegalStateException;
  
  public PersistableBundle getMetrics()
  {
    return native_getMetrics();
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
    AudioDeviceInfo[] arrayOfAudioDeviceInfo = AudioManager.getDevicesStatic(1);
    for (int j = 0; j < arrayOfAudioDeviceInfo.length; j++) {
      if (arrayOfAudioDeviceInfo[j].getId() == i) {
        return arrayOfAudioDeviceInfo[j];
      }
    }
    return null;
  }
  
  public native Surface getSurface();
  
  public native void pause()
    throws IllegalStateException;
  
  public void prepare()
    throws IllegalStateException, IOException
  {
    RandomAccessFile localRandomAccessFile1;
    if (mPath != null) {
      localRandomAccessFile1 = new RandomAccessFile(mPath, "rw");
    }
    try
    {
      _setOutputFile(localRandomAccessFile1.getFD());
      localRandomAccessFile1.close();
    }
    finally
    {
      localRandomAccessFile1.close();
    }
    _setOutputFile(mFd);
    if (mFile != null)
    {
      RandomAccessFile localRandomAccessFile2 = new RandomAccessFile(mFile, "rw");
      try
      {
        _setOutputFile(localRandomAccessFile2.getFD());
        localRandomAccessFile2.close();
        _prepare();
        return;
      }
      finally
      {
        localRandomAccessFile2.close();
      }
    }
    throw new IOException("No valid output file");
  }
  
  public native void release();
  
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
    native_reset();
    mEventHandler.removeCallbacksAndMessages(null);
  }
  
  public native void resume()
    throws IllegalStateException;
  
  public void setAudioChannels(int paramInt)
  {
    if (paramInt > 0)
    {
      mChannelCount = paramInt;
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("audio-param-number-of-channels=");
      localStringBuilder.append(paramInt);
      setParameter(localStringBuilder.toString());
      return;
    }
    throw new IllegalArgumentException("Number of channels is not positive");
  }
  
  public native void setAudioEncoder(int paramInt)
    throws IllegalStateException;
  
  public void setAudioEncodingBitRate(int paramInt)
  {
    if (paramInt > 0)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("audio-param-encoding-bitrate=");
      localStringBuilder.append(paramInt);
      setParameter(localStringBuilder.toString());
      return;
    }
    throw new IllegalArgumentException("Audio encoding bit rate is not positive");
  }
  
  public void setAudioSamplingRate(int paramInt)
  {
    if (paramInt > 0)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("audio-param-sampling-rate=");
      localStringBuilder.append(paramInt);
      setParameter(localStringBuilder.toString());
      return;
    }
    throw new IllegalArgumentException("Audio sampling rate is not positive");
  }
  
  public native void setAudioSource(int paramInt)
    throws IllegalStateException;
  
  public void setAuxiliaryOutputFile(FileDescriptor paramFileDescriptor)
  {
    Log.w("MediaRecorder", "setAuxiliaryOutputFile(FileDescriptor) is no longer supported.");
  }
  
  public void setAuxiliaryOutputFile(String paramString)
  {
    Log.w("MediaRecorder", "setAuxiliaryOutputFile(String) is no longer supported.");
  }
  
  @Deprecated
  public native void setCamera(Camera paramCamera);
  
  public void setCaptureRate(double paramDouble)
  {
    setParameter("time-lapse-enable=1");
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("time-lapse-fps=");
    localStringBuilder.append(paramDouble);
    setParameter(localStringBuilder.toString());
  }
  
  public void setInputSurface(Surface paramSurface)
  {
    if ((paramSurface instanceof MediaCodec.PersistentSurface))
    {
      native_setInputSurface(paramSurface);
      return;
    }
    throw new IllegalArgumentException("not a PersistentSurface");
  }
  
  public void setLocation(float paramFloat1, float paramFloat2)
  {
    int i = (int)(paramFloat1 * 10000.0F + 0.5D);
    int j = (int)(10000.0F * paramFloat2 + 0.5D);
    if ((i <= 900000) && (i >= -900000))
    {
      if ((j <= 1800000) && (j >= -1800000))
      {
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("param-geotag-latitude=");
        localStringBuilder.append(i);
        setParameter(localStringBuilder.toString());
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("param-geotag-longitude=");
        localStringBuilder.append(j);
        setParameter(localStringBuilder.toString());
        return;
      }
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("Longitude: ");
      localStringBuilder.append(paramFloat2);
      localStringBuilder.append(" out of range");
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Latitude: ");
    localStringBuilder.append(paramFloat1);
    localStringBuilder.append(" out of range.");
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  public native void setMaxDuration(int paramInt)
    throws IllegalArgumentException;
  
  public native void setMaxFileSize(long paramLong)
    throws IllegalArgumentException;
  
  public void setNextOutputFile(File paramFile)
    throws IOException
  {
    paramFile = new RandomAccessFile(paramFile, "rw");
    try
    {
      _setNextOutputFile(paramFile.getFD());
      return;
    }
    finally
    {
      paramFile.close();
    }
  }
  
  public void setNextOutputFile(FileDescriptor paramFileDescriptor)
    throws IOException
  {
    _setNextOutputFile(paramFileDescriptor);
  }
  
  public void setOnErrorListener(OnErrorListener paramOnErrorListener)
  {
    mOnErrorListener = paramOnErrorListener;
  }
  
  public void setOnInfoListener(OnInfoListener paramOnInfoListener)
  {
    mOnInfoListener = paramOnInfoListener;
  }
  
  public void setOrientationHint(int paramInt)
  {
    if ((paramInt != 0) && (paramInt != 90) && (paramInt != 180) && (paramInt != 270))
    {
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("Unsupported angle: ");
      localStringBuilder.append(paramInt);
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("video-param-rotation-angle-degrees=");
    localStringBuilder.append(paramInt);
    setParameter(localStringBuilder.toString());
  }
  
  public void setOutputFile(File paramFile)
  {
    mPath = null;
    mFd = null;
    mFile = paramFile;
  }
  
  public void setOutputFile(FileDescriptor paramFileDescriptor)
    throws IllegalStateException
  {
    mPath = null;
    mFile = null;
    mFd = paramFileDescriptor;
  }
  
  public void setOutputFile(String paramString)
    throws IllegalStateException
  {
    mFd = null;
    mFile = null;
    mPath = paramString;
  }
  
  public native void setOutputFormat(int paramInt)
    throws IllegalStateException;
  
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
  
  public void setPreviewDisplay(Surface paramSurface)
  {
    mSurface = paramSurface;
  }
  
  public void setProfile(CamcorderProfile paramCamcorderProfile)
  {
    setOutputFormat(fileFormat);
    setVideoFrameRate(videoFrameRate);
    setVideoSize(videoFrameWidth, videoFrameHeight);
    setVideoEncodingBitRate(videoBitRate);
    setVideoEncoder(videoCodec);
    if ((quality < 1000) || (quality > 1007))
    {
      setAudioEncodingBitRate(audioBitRate);
      setAudioChannels(audioChannels);
      setAudioSamplingRate(audioSampleRate);
      setAudioEncoder(audioCodec);
    }
  }
  
  public native void setVideoEncoder(int paramInt)
    throws IllegalStateException;
  
  public void setVideoEncodingBitRate(int paramInt)
  {
    if (paramInt > 0)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("video-param-encoding-bitrate=");
      localStringBuilder.append(paramInt);
      setParameter(localStringBuilder.toString());
      return;
    }
    throw new IllegalArgumentException("Video encoding bit rate is not positive");
  }
  
  public void setVideoEncodingProfileLevel(int paramInt1, int paramInt2)
  {
    if (paramInt1 > 0)
    {
      if (paramInt2 > 0)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("video-param-encoder-profile=");
        localStringBuilder.append(paramInt1);
        setParameter(localStringBuilder.toString());
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("video-param-encoder-level=");
        localStringBuilder.append(paramInt2);
        setParameter(localStringBuilder.toString());
        return;
      }
      throw new IllegalArgumentException("Video encoding level is not positive");
    }
    throw new IllegalArgumentException("Video encoding profile is not positive");
  }
  
  public native void setVideoFrameRate(int paramInt)
    throws IllegalStateException;
  
  public native void setVideoSize(int paramInt1, int paramInt2)
    throws IllegalStateException;
  
  public native void setVideoSource(int paramInt)
    throws IllegalStateException;
  
  public native void start()
    throws IllegalStateException;
  
  public native void stop()
    throws IllegalStateException;
  
  public final class AudioEncoder
  {
    public static final int AAC = 3;
    public static final int AAC_ELD = 5;
    public static final int AMR_NB = 1;
    public static final int AMR_WB = 2;
    public static final int DEFAULT = 0;
    public static final int EVRC = 10;
    public static final int HE_AAC = 4;
    public static final int LPCM = 12;
    public static final int QCELP = 11;
    public static final int VORBIS = 6;
    
    private AudioEncoder() {}
  }
  
  public final class AudioSource
  {
    public static final int AUDIO_SOURCE_INVALID = -1;
    public static final int CAMCORDER = 5;
    public static final int DEFAULT = 0;
    @SystemApi
    public static final int HOTWORD = 1999;
    public static final int MIC = 1;
    @SystemApi
    public static final int RADIO_TUNER = 1998;
    public static final int REMOTE_SUBMIX = 8;
    public static final int REMOTE_SUBMIX_GAMEGENIE = 10;
    public static final int UNPROCESSED = 9;
    public static final int VOICE_CALL = 4;
    public static final int VOICE_COMMUNICATION = 7;
    public static final int VOICE_DOWNLINK = 3;
    public static final int VOICE_RECOGNITION = 6;
    public static final int VOICE_UPLINK = 2;
    
    private AudioSource() {}
  }
  
  private class EventHandler
    extends Handler
  {
    private static final int MEDIA_RECORDER_AUDIO_ROUTING_CHANGED = 10000;
    private static final int MEDIA_RECORDER_EVENT_ERROR = 1;
    private static final int MEDIA_RECORDER_EVENT_INFO = 2;
    private static final int MEDIA_RECORDER_EVENT_LIST_END = 99;
    private static final int MEDIA_RECORDER_EVENT_LIST_START = 1;
    private static final int MEDIA_RECORDER_TRACK_EVENT_ERROR = 100;
    private static final int MEDIA_RECORDER_TRACK_EVENT_INFO = 101;
    private static final int MEDIA_RECORDER_TRACK_EVENT_LIST_END = 1000;
    private static final int MEDIA_RECORDER_TRACK_EVENT_LIST_START = 100;
    private MediaRecorder mMediaRecorder;
    
    public EventHandler(MediaRecorder paramMediaRecorder, Looper paramLooper)
    {
      super();
      mMediaRecorder = paramMediaRecorder;
    }
    
    public void handleMessage(Message arg1)
    {
      if (mMediaRecorder.mNativeContext == 0L)
      {
        Log.w("MediaRecorder", "mediarecorder went away with unhandled events");
        return;
      }
      Object localObject1;
      switch (what)
      {
      default: 
        localObject1 = new StringBuilder();
        ((StringBuilder)localObject1).append("Unknown message type ");
        ((StringBuilder)localObject1).append(what);
        Log.e("MediaRecorder", ((StringBuilder)localObject1).toString());
        return;
      case 10000: 
        AudioManager.resetAudioPortGeneration();
        synchronized (mRoutingChangeListeners)
        {
          localObject1 = mRoutingChangeListeners.values().iterator();
          while (((Iterator)localObject1).hasNext()) {
            ((NativeRoutingEventHandlerDelegate)((Iterator)localObject1).next()).notifyClient();
          }
          return;
        }
      case 2: 
      case 101: 
        if (mOnInfoListener != null) {
          mOnInfoListener.onInfo(mMediaRecorder, arg1, arg2);
        }
        return;
      }
      if (mOnErrorListener != null) {
        mOnErrorListener.onError(mMediaRecorder, arg1, arg2);
      }
    }
  }
  
  public static final class MetricsConstants
  {
    public static final String AUDIO_BITRATE = "android.media.mediarecorder.audio-bitrate";
    public static final String AUDIO_CHANNELS = "android.media.mediarecorder.audio-channels";
    public static final String AUDIO_SAMPLERATE = "android.media.mediarecorder.audio-samplerate";
    public static final String AUDIO_TIMESCALE = "android.media.mediarecorder.audio-timescale";
    public static final String CAPTURE_FPS = "android.media.mediarecorder.capture-fps";
    public static final String CAPTURE_FPS_ENABLE = "android.media.mediarecorder.capture-fpsenable";
    public static final String FRAMERATE = "android.media.mediarecorder.frame-rate";
    public static final String HEIGHT = "android.media.mediarecorder.height";
    public static final String MOVIE_TIMESCALE = "android.media.mediarecorder.movie-timescale";
    public static final String ROTATION = "android.media.mediarecorder.rotation";
    public static final String VIDEO_BITRATE = "android.media.mediarecorder.video-bitrate";
    public static final String VIDEO_IFRAME_INTERVAL = "android.media.mediarecorder.video-iframe-interval";
    public static final String VIDEO_LEVEL = "android.media.mediarecorder.video-encoder-level";
    public static final String VIDEO_PROFILE = "android.media.mediarecorder.video-encoder-profile";
    public static final String VIDEO_TIMESCALE = "android.media.mediarecorder.video-timescale";
    public static final String WIDTH = "android.media.mediarecorder.width";
    
    private MetricsConstants() {}
  }
  
  public static abstract interface OnErrorListener
  {
    public abstract void onError(MediaRecorder paramMediaRecorder, int paramInt1, int paramInt2);
  }
  
  public static abstract interface OnInfoListener
  {
    public abstract void onInfo(MediaRecorder paramMediaRecorder, int paramInt1, int paramInt2);
  }
  
  public final class OutputFormat
  {
    public static final int AAC_ADIF = 5;
    public static final int AAC_ADTS = 6;
    public static final int AMR_NB = 3;
    public static final int AMR_WB = 4;
    public static final int DEFAULT = 0;
    public static final int MPEG_2_TS = 8;
    public static final int MPEG_4 = 2;
    public static final int OUTPUT_FORMAT_RTP_AVP = 7;
    public static final int QCP = 20;
    public static final int RAW_AMR = 3;
    public static final int THREE_GPP = 1;
    public static final int WAVE = 21;
    public static final int WEBM = 9;
    
    private OutputFormat() {}
  }
  
  public final class VideoEncoder
  {
    public static final int DEFAULT = 0;
    public static final int H263 = 1;
    public static final int H264 = 2;
    public static final int HEVC = 5;
    public static final int MPEG_4_SP = 3;
    public static final int VP8 = 4;
    
    private VideoEncoder() {}
  }
  
  public final class VideoSource
  {
    public static final int CAMERA = 1;
    public static final int DEFAULT = 0;
    public static final int SURFACE = 2;
    
    private VideoSource() {}
  }
}
