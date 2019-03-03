package android.media;

import android.annotation.SuppressLint;
import android.annotation.SystemApi;
import android.app.PendingIntent;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.res.Resources;
import android.media.audiopolicy.AudioPolicy;
import android.media.session.MediaSessionLegacyHelper;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.os.UserHandle;
import android.provider.Settings.System;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.view.KeyEvent;
import com.android.internal.annotations.GuardedBy;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

public class AudioManager
{
  public static final String ACTION_AUDIO_BECOMING_NOISY = "android.media.AUDIO_BECOMING_NOISY";
  public static final String ACTION_HDMI_AUDIO_PLUG = "android.media.action.HDMI_AUDIO_PLUG";
  public static final String ACTION_HEADSET_PLUG = "android.intent.action.HEADSET_PLUG";
  public static final String ACTION_MICROPHONE_MUTE_CHANGED = "android.media.action.MICROPHONE_MUTE_CHANGED";
  @Deprecated
  public static final String ACTION_SCO_AUDIO_STATE_CHANGED = "android.media.SCO_AUDIO_STATE_CHANGED";
  public static final String ACTION_SCO_AUDIO_STATE_UPDATED = "android.media.ACTION_SCO_AUDIO_STATE_UPDATED";
  public static final int ADJUST_LOWER = -1;
  public static final int ADJUST_MUTE = -100;
  public static final int ADJUST_RAISE = 1;
  public static final int ADJUST_SAME = 0;
  public static final int ADJUST_TOGGLE_MUTE = 101;
  public static final int ADJUST_UNMUTE = 100;
  public static final int AUDIOFOCUS_FLAGS_APPS = 3;
  public static final int AUDIOFOCUS_FLAGS_SYSTEM = 7;
  @SystemApi
  public static final int AUDIOFOCUS_FLAG_DELAY_OK = 1;
  @SystemApi
  public static final int AUDIOFOCUS_FLAG_LOCK = 4;
  @SystemApi
  public static final int AUDIOFOCUS_FLAG_PAUSES_ON_DUCKABLE_LOSS = 2;
  public static final int AUDIOFOCUS_GAIN = 1;
  public static final int AUDIOFOCUS_GAIN_TRANSIENT = 2;
  public static final int AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE = 4;
  public static final int AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK = 3;
  public static final int AUDIOFOCUS_LOSS = -1;
  public static final int AUDIOFOCUS_LOSS_TRANSIENT = -2;
  public static final int AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK = -3;
  public static final int AUDIOFOCUS_NONE = 0;
  public static final int AUDIOFOCUS_REQUEST_DELAYED = 2;
  public static final int AUDIOFOCUS_REQUEST_FAILED = 0;
  public static final int AUDIOFOCUS_REQUEST_GRANTED = 1;
  public static final int AUDIOFOCUS_REQUEST_WAITING_FOR_EXT_POLICY = 100;
  static final int AUDIOPORT_GENERATION_INIT = 0;
  public static final int AUDIO_SESSION_ID_GENERATE = 0;
  private static final boolean DEBUG = false;
  public static final int DEVICE_IN_ANLG_DOCK_HEADSET = -2147483136;
  public static final int DEVICE_IN_BACK_MIC = -2147483520;
  public static final int DEVICE_IN_BLUETOOTH_SCO_HEADSET = -2147483640;
  public static final int DEVICE_IN_BUILTIN_MIC = -2147483644;
  public static final int DEVICE_IN_DGTL_DOCK_HEADSET = -2147482624;
  public static final int DEVICE_IN_FM_TUNER = -2147475456;
  public static final int DEVICE_IN_HDMI = -2147483616;
  public static final int DEVICE_IN_LINE = -2147450880;
  public static final int DEVICE_IN_LOOPBACK = -2147221504;
  public static final int DEVICE_IN_SPDIF = -2147418112;
  public static final int DEVICE_IN_TELEPHONY_RX = -2147483584;
  public static final int DEVICE_IN_TV_TUNER = -2147467264;
  public static final int DEVICE_IN_USB_ACCESSORY = -2147481600;
  public static final int DEVICE_IN_USB_DEVICE = -2147479552;
  public static final int DEVICE_IN_WIRED_HEADSET = -2147483632;
  public static final int DEVICE_NONE = 0;
  public static final int DEVICE_OUT_ANLG_DOCK_HEADSET = 2048;
  public static final int DEVICE_OUT_AUX_DIGITAL = 1024;
  public static final int DEVICE_OUT_BLUETOOTH_A2DP = 128;
  public static final int DEVICE_OUT_BLUETOOTH_A2DP_HEADPHONES = 256;
  public static final int DEVICE_OUT_BLUETOOTH_A2DP_SPEAKER = 512;
  public static final int DEVICE_OUT_BLUETOOTH_SCO = 16;
  public static final int DEVICE_OUT_BLUETOOTH_SCO_CARKIT = 64;
  public static final int DEVICE_OUT_BLUETOOTH_SCO_HEADSET = 32;
  public static final int DEVICE_OUT_DEFAULT = 1073741824;
  public static final int DEVICE_OUT_DGTL_DOCK_HEADSET = 4096;
  public static final int DEVICE_OUT_EARPIECE = 1;
  public static final int DEVICE_OUT_FM = 1048576;
  public static final int DEVICE_OUT_HDMI = 1024;
  public static final int DEVICE_OUT_HDMI_ARC = 262144;
  public static final int DEVICE_OUT_LINE = 131072;
  public static final int DEVICE_OUT_REMOTE_SUBMIX = 32768;
  public static final int DEVICE_OUT_SPDIF = 524288;
  public static final int DEVICE_OUT_SPEAKER = 2;
  public static final int DEVICE_OUT_TELEPHONY_TX = 65536;
  public static final int DEVICE_OUT_USB_ACCESSORY = 8192;
  public static final int DEVICE_OUT_USB_DEVICE = 16384;
  public static final int DEVICE_OUT_USB_HEADSET = 67108864;
  public static final int DEVICE_OUT_WIRED_HEADPHONE = 8;
  public static final int DEVICE_OUT_WIRED_HEADSET = 4;
  public static final int ERROR = -1;
  public static final int ERROR_BAD_VALUE = -2;
  public static final int ERROR_DEAD_OBJECT = -6;
  public static final int ERROR_INVALID_OPERATION = -3;
  public static final int ERROR_NO_INIT = -5;
  public static final int ERROR_PERMISSION_DENIED = -4;
  public static final String EXTRA_AUDIO_PLUG_STATE = "android.media.extra.AUDIO_PLUG_STATE";
  public static final String EXTRA_ENCODINGS = "android.media.extra.ENCODINGS";
  public static final String EXTRA_MASTER_VOLUME_MUTED = "android.media.EXTRA_MASTER_VOLUME_MUTED";
  public static final String EXTRA_MAX_CHANNEL_COUNT = "android.media.extra.MAX_CHANNEL_COUNT";
  public static final String EXTRA_PREV_VOLUME_STREAM_DEVICES = "android.media.EXTRA_PREV_VOLUME_STREAM_DEVICES";
  public static final String EXTRA_PREV_VOLUME_STREAM_VALUE = "android.media.EXTRA_PREV_VOLUME_STREAM_VALUE";
  public static final String EXTRA_RINGER_MODE = "android.media.EXTRA_RINGER_MODE";
  public static final String EXTRA_SCO_AUDIO_PREVIOUS_STATE = "android.media.extra.SCO_AUDIO_PREVIOUS_STATE";
  public static final String EXTRA_SCO_AUDIO_STATE = "android.media.extra.SCO_AUDIO_STATE";
  public static final String EXTRA_STREAM_VOLUME_MUTED = "android.media.EXTRA_STREAM_VOLUME_MUTED";
  public static final String EXTRA_VIBRATE_SETTING = "android.media.EXTRA_VIBRATE_SETTING";
  public static final String EXTRA_VIBRATE_TYPE = "android.media.EXTRA_VIBRATE_TYPE";
  public static final String EXTRA_VOLUME_STREAM_DEVICES = "android.media.EXTRA_VOLUME_STREAM_DEVICES";
  public static final String EXTRA_VOLUME_STREAM_TYPE = "android.media.EXTRA_VOLUME_STREAM_TYPE";
  public static final String EXTRA_VOLUME_STREAM_TYPE_ALIAS = "android.media.EXTRA_VOLUME_STREAM_TYPE_ALIAS";
  public static final String EXTRA_VOLUME_STREAM_VALUE = "android.media.EXTRA_VOLUME_STREAM_VALUE";
  private static final int EXT_FOCUS_POLICY_TIMEOUT_MS = 200;
  public static final int FLAG_ACTIVE_MEDIA_ONLY = 512;
  public static final int FLAG_ALLOW_RINGER_MODES = 2;
  public static final int FLAG_BLUETOOTH_ABS_VOLUME = 64;
  public static final int FLAG_FIXED_VOLUME = 32;
  public static final int FLAG_FROM_KEY = 4096;
  public static final int FLAG_HDMI_SYSTEM_AUDIO_VOLUME = 256;
  private static final String[] FLAG_NAMES;
  public static final int FLAG_PLAY_SOUND = 4;
  public static final int FLAG_REMOVE_SOUND_AND_VIBRATE = 8;
  public static final int FLAG_SHOW_SILENT_HINT = 128;
  public static final int FLAG_SHOW_UI = 1;
  public static final int FLAG_SHOW_UI_WARNINGS = 1024;
  public static final int FLAG_SHOW_VIBRATE_HINT = 2048;
  public static final int FLAG_VIBRATE = 16;
  private static final String FOCUS_CLIENT_ID_STRING = "android_audio_focus_client_id";
  public static final int FX_FOCUS_NAVIGATION_DOWN = 2;
  public static final int FX_FOCUS_NAVIGATION_LEFT = 3;
  public static final int FX_FOCUS_NAVIGATION_RIGHT = 4;
  public static final int FX_FOCUS_NAVIGATION_UP = 1;
  public static final int FX_KEYPRESS_DELETE = 7;
  public static final int FX_KEYPRESS_INVALID = 9;
  public static final int FX_KEYPRESS_RETURN = 8;
  public static final int FX_KEYPRESS_SPACEBAR = 6;
  public static final int FX_KEYPRESS_STANDARD = 5;
  public static final int FX_KEY_CLICK = 0;
  public static final int GET_DEVICES_ALL = 3;
  public static final int GET_DEVICES_INPUTS = 1;
  public static final int GET_DEVICES_OUTPUTS = 2;
  public static final String INTERNAL_RINGER_MODE_CHANGED_ACTION = "android.media.INTERNAL_RINGER_MODE_CHANGED_ACTION";
  public static final String MASTER_MUTE_CHANGED_ACTION = "android.media.MASTER_MUTE_CHANGED_ACTION";
  public static final int MODE_CURRENT = -1;
  public static final int MODE_INVALID = -2;
  public static final int MODE_IN_CALL = 2;
  public static final int MODE_IN_COMMUNICATION = 3;
  public static final int MODE_NORMAL = 0;
  public static final int MODE_RINGTONE = 1;
  private static final int MSG_DEVICES_CALLBACK_REGISTERED = 0;
  private static final int MSG_DEVICES_DEVICES_ADDED = 1;
  private static final int MSG_DEVICES_DEVICES_REMOVED = 2;
  private static final int MSSG_FOCUS_CHANGE = 0;
  private static final int MSSG_PLAYBACK_CONFIG_CHANGE = 2;
  private static final int MSSG_RECORDING_CONFIG_CHANGE = 1;
  public static final int NUM_SOUND_EFFECTS = 10;
  @Deprecated
  public static final int NUM_STREAMS = 5;
  public static final String PROPERTY_OUTPUT_FRAMES_PER_BUFFER = "android.media.property.OUTPUT_FRAMES_PER_BUFFER";
  public static final String PROPERTY_OUTPUT_SAMPLE_RATE = "android.media.property.OUTPUT_SAMPLE_RATE";
  public static final String PROPERTY_SUPPORT_AUDIO_SOURCE_UNPROCESSED = "android.media.property.SUPPORT_AUDIO_SOURCE_UNPROCESSED";
  public static final String PROPERTY_SUPPORT_MIC_NEAR_ULTRASOUND = "android.media.property.SUPPORT_MIC_NEAR_ULTRASOUND";
  public static final String PROPERTY_SUPPORT_SPEAKER_NEAR_ULTRASOUND = "android.media.property.SUPPORT_SPEAKER_NEAR_ULTRASOUND";
  public static final int RECORD_CONFIG_EVENT_START = 1;
  public static final int RECORD_CONFIG_EVENT_STOP = 0;
  public static final String RINGER_MODE_CHANGED_ACTION = "android.media.RINGER_MODE_CHANGED";
  public static final int RINGER_MODE_MAX = 2;
  public static final int RINGER_MODE_NORMAL = 2;
  public static final int RINGER_MODE_SILENT = 0;
  public static final int RINGER_MODE_VIBRATE = 1;
  @Deprecated
  public static final int ROUTE_ALL = -1;
  @Deprecated
  public static final int ROUTE_BLUETOOTH = 4;
  @Deprecated
  public static final int ROUTE_BLUETOOTH_A2DP = 16;
  @Deprecated
  public static final int ROUTE_BLUETOOTH_SCO = 4;
  @Deprecated
  public static final int ROUTE_EARPIECE = 1;
  @Deprecated
  public static final int ROUTE_HEADSET = 8;
  @Deprecated
  public static final int ROUTE_SPEAKER = 2;
  public static final int SCO_AUDIO_STATE_CONNECTED = 1;
  public static final int SCO_AUDIO_STATE_CONNECTING = 2;
  public static final int SCO_AUDIO_STATE_DISCONNECTED = 0;
  public static final int SCO_AUDIO_STATE_ERROR = -1;
  public static final int STREAM_ACCESSIBILITY = 10;
  public static final int STREAM_ALARM = 4;
  public static final int STREAM_BLUETOOTH_SCO = 6;
  public static final String STREAM_DEVICES_CHANGED_ACTION = "android.media.STREAM_DEVICES_CHANGED_ACTION";
  public static final int STREAM_DTMF = 8;
  public static final int STREAM_MUSIC = 3;
  public static final String STREAM_MUTE_CHANGED_ACTION = "android.media.STREAM_MUTE_CHANGED_ACTION";
  public static final int STREAM_NOTIFICATION = 5;
  public static final int STREAM_RING = 2;
  public static final int STREAM_SYSTEM = 1;
  public static final int STREAM_SYSTEM_ENFORCED = 7;
  public static final int STREAM_TTS = 9;
  public static final int STREAM_VOICE_CALL = 0;
  public static final int SUCCESS = 0;
  private static final String TAG = "AudioManager";
  public static final int USE_DEFAULT_STREAM_TYPE = Integer.MIN_VALUE;
  public static final String VIBRATE_SETTING_CHANGED_ACTION = "android.media.VIBRATE_SETTING_CHANGED";
  public static final int VIBRATE_SETTING_OFF = 0;
  public static final int VIBRATE_SETTING_ON = 1;
  public static final int VIBRATE_SETTING_ONLY_SILENT = 2;
  public static final int VIBRATE_TYPE_NOTIFICATION = 1;
  public static final int VIBRATE_TYPE_RINGER = 0;
  public static final String VOLUME_CHANGED_ACTION = "android.media.VOLUME_CHANGED_ACTION";
  private static final float VOLUME_MIN_DB = -758.0F;
  static ArrayList<AudioPatch> sAudioPatchesCached = new ArrayList();
  private static final AudioPortEventHandler sAudioPortEventHandler = new AudioPortEventHandler();
  static Integer sAudioPortGeneration;
  static ArrayList<AudioPort> sAudioPortsCached;
  static ArrayList<AudioPort> sPreviousAudioPortsCached;
  private static IAudioService sService;
  private Context mApplicationContext;
  private final IAudioFocusDispatcher mAudioFocusDispatcher = new IAudioFocusDispatcher.Stub()
  {
    public void dispatchAudioFocusChange(int paramAnonymousInt, String paramAnonymousString)
    {
      Object localObject = AudioManager.this.findFocusRequestInfo(paramAnonymousString);
      if ((localObject != null) && (mRequest.getOnAudioFocusChangeListener() != null))
      {
        if (mHandler == null) {
          localObject = mServiceEventHandlerDelegate.getHandler();
        } else {
          localObject = mHandler;
        }
        ((Handler)localObject).sendMessage(((Handler)localObject).obtainMessage(0, paramAnonymousInt, 0, paramAnonymousString));
      }
    }
    
    public void dispatchFocusResultFromExtPolicy(int paramAnonymousInt, String paramAnonymousString)
    {
      synchronized (mFocusRequestsLock)
      {
        paramAnonymousString = (AudioManager.BlockingFocusResultReceiver)mFocusRequestsAwaitingResult.remove(paramAnonymousString);
        if (paramAnonymousString != null) {
          paramAnonymousString.notifyResult(paramAnonymousInt);
        } else {
          Log.e("AudioManager", "dispatchFocusResultFromExtPolicy found no result receiver");
        }
        return;
      }
    }
  };
  private final ConcurrentHashMap<String, FocusRequestInfo> mAudioFocusIdListenerMap = new ConcurrentHashMap();
  private AudioServerStateCallback mAudioServerStateCb;
  private final Object mAudioServerStateCbLock = new Object();
  private final IAudioServerStateDispatcher mAudioServerStateDispatcher = new IAudioServerStateDispatcher.Stub()
  {
    public void dispatchAudioServerStateChange(boolean paramAnonymousBoolean)
    {
      synchronized (mAudioServerStateCbLock)
      {
        Executor localExecutor = mAudioServerStateExec;
        AudioManager.AudioServerStateCallback localAudioServerStateCallback = mAudioServerStateCb;
        if ((localExecutor != null) && (localAudioServerStateCallback != null))
        {
          if (paramAnonymousBoolean) {
            localExecutor.execute(new _..Lambda.AudioManager.4.Q85LmhgKDCoq1YI14giFabZrM7A(localAudioServerStateCallback));
          } else {
            localExecutor.execute(new _..Lambda.AudioManager.4.7k7uSoMGULBCueASQSmf9jAil7I(localAudioServerStateCallback));
          }
          return;
        }
        return;
      }
    }
  };
  private Executor mAudioServerStateExec;
  private final ArrayMap<AudioDeviceCallback, NativeEventHandlerDelegate> mDeviceCallbacks = new ArrayMap();
  @GuardedBy("mFocusRequestsLock")
  private HashMap<String, BlockingFocusResultReceiver> mFocusRequestsAwaitingResult;
  private final Object mFocusRequestsLock = new Object();
  private final IBinder mICallBack = new Binder();
  private Context mOriginalContext;
  private final IPlaybackConfigDispatcher mPlayCb = new IPlaybackConfigDispatcher.Stub()
  {
    public void dispatchPlaybackConfigChange(List<AudioPlaybackConfiguration> paramAnonymousList, boolean paramAnonymousBoolean)
    {
      if (paramAnonymousBoolean) {
        Binder.flushPendingCommands();
      }
      synchronized (mPlaybackCallbackLock)
      {
        if (mPlaybackCallbackList != null) {
          for (int i = 0; i < mPlaybackCallbackList.size(); i++)
          {
            AudioManager.AudioPlaybackCallbackInfo localAudioPlaybackCallbackInfo = (AudioManager.AudioPlaybackCallbackInfo)mPlaybackCallbackList.get(i);
            if (mHandler != null)
            {
              Handler localHandler = mHandler;
              Object localObject2 = new android/media/AudioManager$PlaybackConfigChangeCallbackData;
              ((AudioManager.PlaybackConfigChangeCallbackData)localObject2).<init>(mCb, paramAnonymousList);
              localObject2 = localHandler.obtainMessage(2, localObject2);
              mHandler.sendMessage((Message)localObject2);
            }
          }
        }
        return;
      }
    }
  };
  private List<AudioPlaybackCallbackInfo> mPlaybackCallbackList;
  private final Object mPlaybackCallbackLock = new Object();
  private OnAmPortUpdateListener mPortListener = null;
  private ArrayList<AudioDevicePort> mPreviousPorts = new ArrayList();
  private final IRecordingConfigDispatcher mRecCb = new IRecordingConfigDispatcher.Stub()
  {
    public void dispatchRecordingConfigChange(List<AudioRecordingConfiguration> paramAnonymousList)
    {
      synchronized (mRecordCallbackLock)
      {
        if (mRecordCallbackList != null) {
          for (int i = 0; i < mRecordCallbackList.size(); i++)
          {
            AudioManager.AudioRecordingCallbackInfo localAudioRecordingCallbackInfo = (AudioManager.AudioRecordingCallbackInfo)mRecordCallbackList.get(i);
            if (mHandler != null)
            {
              Handler localHandler = mHandler;
              Object localObject2 = new android/media/AudioManager$RecordConfigChangeCallbackData;
              ((AudioManager.RecordConfigChangeCallbackData)localObject2).<init>(mCb, paramAnonymousList);
              localObject2 = localHandler.obtainMessage(1, localObject2);
              mHandler.sendMessage((Message)localObject2);
            }
          }
        }
        return;
      }
    }
  };
  private List<AudioRecordingCallbackInfo> mRecordCallbackList;
  private final Object mRecordCallbackLock = new Object();
  private final ServiceEventHandlerDelegate mServiceEventHandlerDelegate = new ServiceEventHandlerDelegate(null);
  private final boolean mUseFixedVolume;
  private final boolean mUseVolumeKeySounds;
  private long mVolumeKeyUpTime;
  
  static
  {
    FLAG_NAMES = new String[] { "FLAG_SHOW_UI", "FLAG_ALLOW_RINGER_MODES", "FLAG_PLAY_SOUND", "FLAG_REMOVE_SOUND_AND_VIBRATE", "FLAG_VIBRATE", "FLAG_FIXED_VOLUME", "FLAG_BLUETOOTH_ABS_VOLUME", "FLAG_SHOW_SILENT_HINT", "FLAG_HDMI_SYSTEM_AUDIO_VOLUME", "FLAG_ACTIVE_MEDIA_ONLY", "FLAG_SHOW_UI_WARNINGS", "FLAG_SHOW_VIBRATE_HINT", "FLAG_FROM_KEY" };
    sAudioPortGeneration = new Integer(0);
    sAudioPortsCached = new ArrayList();
    sPreviousAudioPortsCached = new ArrayList();
  }
  
  public AudioManager()
  {
    mUseVolumeKeySounds = true;
    mUseFixedVolume = false;
  }
  
  public AudioManager(Context paramContext)
  {
    setContext(paramContext);
    mUseVolumeKeySounds = getContext().getResources().getBoolean(17957071);
    mUseFixedVolume = getContext().getResources().getBoolean(17957068);
  }
  
  private void addMicrophonesFromAudioDeviceInfo(ArrayList<MicrophoneInfo> paramArrayList, HashSet<Integer> paramHashSet)
  {
    for (AudioDeviceInfo localAudioDeviceInfo : getDevicesStatic(1)) {
      if (!paramHashSet.contains(Integer.valueOf(localAudioDeviceInfo.getType()))) {
        paramArrayList.add(microphoneInfoFromAudioDeviceInfo(localAudioDeviceInfo));
      }
    }
  }
  
  public static final String adjustToString(int paramInt)
  {
    if (paramInt != -100)
    {
      switch (paramInt)
      {
      default: 
        switch (paramInt)
        {
        default: 
          StringBuilder localStringBuilder = new StringBuilder("unknown adjust mode ");
          localStringBuilder.append(paramInt);
          return localStringBuilder.toString();
        case 101: 
          return "ADJUST_TOGGLE_MUTE";
        }
        return "ADJUST_UNMUTE";
      case 1: 
        return "ADJUST_RAISE";
      case 0: 
        return "ADJUST_SAME";
      }
      return "ADJUST_LOWER";
    }
    return "ADJUST_MUTE";
  }
  
  private void broadcastDeviceListChange_sync(Handler paramHandler)
  {
    ArrayList localArrayList = new ArrayList();
    if (listAudioDevicePorts(localArrayList) != 0) {
      return;
    }
    int i = 0;
    AudioDeviceInfo[] arrayOfAudioDeviceInfo;
    if (paramHandler != null)
    {
      arrayOfAudioDeviceInfo = infoListFromPortList(localArrayList, 3);
      paramHandler.sendMessage(Message.obtain(paramHandler, 0, arrayOfAudioDeviceInfo));
    }
    else
    {
      paramHandler = calcListDeltas(mPreviousPorts, localArrayList, 3);
      arrayOfAudioDeviceInfo = calcListDeltas(localArrayList, mPreviousPorts, 3);
      if ((paramHandler.length != 0) || (arrayOfAudioDeviceInfo.length != 0)) {
        while (i < mDeviceCallbacks.size())
        {
          Handler localHandler = ((NativeEventHandlerDelegate)mDeviceCallbacks.valueAt(i)).getHandler();
          if (localHandler != null)
          {
            if (arrayOfAudioDeviceInfo.length != 0) {
              localHandler.sendMessage(Message.obtain(localHandler, 2, arrayOfAudioDeviceInfo));
            }
            if (paramHandler.length != 0) {
              localHandler.sendMessage(Message.obtain(localHandler, 1, paramHandler));
            }
          }
          i++;
        }
      }
    }
    mPreviousPorts = localArrayList;
  }
  
  private static AudioDeviceInfo[] calcListDeltas(ArrayList<AudioDevicePort> paramArrayList1, ArrayList<AudioDevicePort> paramArrayList2, int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    for (int i = 0; i < paramArrayList2.size(); i++)
    {
      AudioDevicePort localAudioDevicePort = (AudioDevicePort)paramArrayList2.get(i);
      int j = 0;
      for (int k = 0; (k < paramArrayList1.size()) && (j == 0); k++) {
        if (localAudioDevicePort.id() == ((AudioDevicePort)paramArrayList1.get(k)).id()) {
          j = 1;
        } else {
          j = 0;
        }
      }
      if (j == 0) {
        localArrayList.add(localAudioDevicePort);
      }
    }
    return infoListFromPortList(localArrayList, paramInt);
  }
  
  private static boolean checkFlags(AudioDevicePort paramAudioDevicePort, int paramInt)
  {
    int i = paramAudioDevicePort.role();
    boolean bool = true;
    if (((i == 2) && ((paramInt & 0x2) != 0)) || ((paramAudioDevicePort.role() != 1) || ((paramInt & 0x1) == 0))) {
      bool = false;
    }
    return bool;
  }
  
  private static boolean checkTypes(AudioDevicePort paramAudioDevicePort)
  {
    boolean bool;
    if (AudioDeviceInfo.convertInternalDeviceToDeviceType(paramAudioDevicePort.type()) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static int createAudioPatch(AudioPatch[] paramArrayOfAudioPatch, AudioPortConfig[] paramArrayOfAudioPortConfig1, AudioPortConfig[] paramArrayOfAudioPortConfig2)
  {
    return AudioSystem.createAudioPatch(paramArrayOfAudioPatch, paramArrayOfAudioPortConfig1, paramArrayOfAudioPortConfig2);
  }
  
  private static void filterDevicePorts(ArrayList<AudioPort> paramArrayList, ArrayList<AudioDevicePort> paramArrayList1)
  {
    paramArrayList1.clear();
    for (int i = 0; i < paramArrayList.size(); i++) {
      if ((paramArrayList.get(i) instanceof AudioDevicePort)) {
        paramArrayList1.add((AudioDevicePort)paramArrayList.get(i));
      }
    }
  }
  
  private FocusRequestInfo findFocusRequestInfo(String paramString)
  {
    return (FocusRequestInfo)mAudioFocusIdListenerMap.get(paramString);
  }
  
  public static String flagsToString(int paramInt)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    int i = 0;
    int j = paramInt;
    paramInt = i;
    while (paramInt < FLAG_NAMES.length)
    {
      int k = 1 << paramInt;
      i = j;
      if ((j & k) != 0)
      {
        if (localStringBuilder.length() > 0) {
          localStringBuilder.append(',');
        }
        localStringBuilder.append(FLAG_NAMES[paramInt]);
        i = j & k;
      }
      paramInt++;
      j = i;
    }
    if (j != 0)
    {
      if (localStringBuilder.length() > 0) {
        localStringBuilder.append(',');
      }
      localStringBuilder.append(j);
    }
    return localStringBuilder.toString();
  }
  
  private Context getContext()
  {
    if (mApplicationContext == null) {
      setContext(mOriginalContext);
    }
    if (mApplicationContext != null) {
      return mApplicationContext;
    }
    return mOriginalContext;
  }
  
  public static AudioDeviceInfo[] getDevicesStatic(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    if (listAudioDevicePorts(localArrayList) != 0) {
      return new AudioDeviceInfo[0];
    }
    return infoListFromPortList(localArrayList, paramInt);
  }
  
  private String getIdForAudioFocusListener(OnAudioFocusChangeListener paramOnAudioFocusChangeListener)
  {
    if (paramOnAudioFocusChangeListener == null) {
      return new String(toString());
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(toString());
    localStringBuilder.append(paramOnAudioFocusChangeListener.toString());
    return new String(localStringBuilder.toString());
  }
  
  private static IAudioService getService()
  {
    if (sService != null) {
      return sService;
    }
    sService = IAudioService.Stub.asInterface(ServiceManager.getService("audio"));
    return sService;
  }
  
  private boolean hasPlaybackCallback_sync(AudioPlaybackCallback paramAudioPlaybackCallback)
  {
    if (mPlaybackCallbackList != null) {
      for (int i = 0; i < mPlaybackCallbackList.size(); i++) {
        if (paramAudioPlaybackCallback.equals(mPlaybackCallbackList.get(i)).mCb)) {
          return true;
        }
      }
    }
    return false;
  }
  
  private boolean hasRecordCallback_sync(AudioRecordingCallback paramAudioRecordingCallback)
  {
    if (mRecordCallbackList != null) {
      for (int i = 0; i < mRecordCallbackList.size(); i++) {
        if (paramAudioRecordingCallback.equals(mRecordCallbackList.get(i)).mCb)) {
          return true;
        }
      }
    }
    return false;
  }
  
  private static AudioDeviceInfo[] infoListFromPortList(ArrayList<AudioDevicePort> paramArrayList, int paramInt)
  {
    int i = 0;
    Object localObject1 = paramArrayList.iterator();
    while (((Iterator)localObject1).hasNext())
    {
      localObject2 = (AudioDevicePort)((Iterator)localObject1).next();
      j = i;
      if (checkTypes((AudioDevicePort)localObject2))
      {
        j = i;
        if (checkFlags((AudioDevicePort)localObject2, paramInt)) {
          j = i + 1;
        }
      }
      i = j;
    }
    localObject1 = new AudioDeviceInfo[i];
    int j = 0;
    Object localObject2 = paramArrayList.iterator();
    while (((Iterator)localObject2).hasNext())
    {
      paramArrayList = (AudioDevicePort)((Iterator)localObject2).next();
      i = j;
      if (checkTypes(paramArrayList))
      {
        i = j;
        if (checkFlags(paramArrayList, paramInt))
        {
          localObject1[j] = new AudioDeviceInfo(paramArrayList);
          i = j + 1;
        }
      }
      j = i;
    }
    return localObject1;
  }
  
  public static boolean isInputDevice(int paramInt)
  {
    boolean bool;
    if ((paramInt & 0x80000000) == Integer.MIN_VALUE) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static boolean isOutputDevice(int paramInt)
  {
    boolean bool;
    if ((0x80000000 & paramInt) == 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private static boolean isPublicStreamType(int paramInt)
  {
    if ((paramInt != 8) && (paramInt != 10)) {
      switch (paramInt)
      {
      default: 
        return false;
      }
    }
    return true;
  }
  
  public static boolean isValidRingerMode(int paramInt)
  {
    if ((paramInt >= 0) && (paramInt <= 2))
    {
      IAudioService localIAudioService = getService();
      try
      {
        boolean bool = localIAudioService.isValidRingerMode(paramInt);
        return bool;
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
    return false;
  }
  
  public static int listAudioDevicePorts(ArrayList<AudioDevicePort> paramArrayList)
  {
    if (paramArrayList == null) {
      return -2;
    }
    ArrayList localArrayList = new ArrayList();
    int i = updateAudioPortCache(localArrayList, null, null);
    if (i == 0) {
      filterDevicePorts(localArrayList, paramArrayList);
    }
    return i;
  }
  
  public static int listAudioPatches(ArrayList<AudioPatch> paramArrayList)
  {
    return updateAudioPortCache(null, paramArrayList, null);
  }
  
  public static int listAudioPorts(ArrayList<AudioPort> paramArrayList)
  {
    return updateAudioPortCache(paramArrayList, null, null);
  }
  
  public static int listPreviousAudioDevicePorts(ArrayList<AudioDevicePort> paramArrayList)
  {
    if (paramArrayList == null) {
      return -2;
    }
    ArrayList localArrayList = new ArrayList();
    int i = updateAudioPortCache(null, null, localArrayList);
    if (i == 0) {
      filterDevicePorts(localArrayList, paramArrayList);
    }
    return i;
  }
  
  public static int listPreviousAudioPorts(ArrayList<AudioPort> paramArrayList)
  {
    return updateAudioPortCache(null, null, paramArrayList);
  }
  
  public static MicrophoneInfo microphoneInfoFromAudioDeviceInfo(AudioDeviceInfo paramAudioDeviceInfo)
  {
    int i = paramAudioDeviceInfo.getType();
    if ((i != 15) && (i != 18))
    {
      if (i == 0) {
        i = 0;
      } else {
        i = 3;
      }
    }
    else {
      i = 1;
    }
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append(paramAudioDeviceInfo.getPort().name());
    ((StringBuilder)localObject).append(paramAudioDeviceInfo.getId());
    localObject = new MicrophoneInfo(((StringBuilder)localObject).toString(), paramAudioDeviceInfo.getPort().type(), paramAudioDeviceInfo.getAddress(), i, -1, -1, MicrophoneInfo.POSITION_UNKNOWN, MicrophoneInfo.ORIENTATION_UNKNOWN, new ArrayList(), new ArrayList(), -3.4028235E38F, -3.4028235E38F, -3.4028235E38F, 0);
    ((MicrophoneInfo)localObject).setId(paramAudioDeviceInfo.getId());
    return localObject;
  }
  
  private boolean querySoundEffectsEnabled(int paramInt)
  {
    ContentResolver localContentResolver = getContext().getContentResolver();
    boolean bool = false;
    if (Settings.System.getIntForUser(localContentResolver, "sound_effects_enabled", 0, paramInt) != 0) {
      bool = true;
    }
    return bool;
  }
  
  public static int releaseAudioPatch(AudioPatch paramAudioPatch)
  {
    return AudioSystem.releaseAudioPatch(paramAudioPatch);
  }
  
  private boolean removePlaybackCallback_sync(AudioPlaybackCallback paramAudioPlaybackCallback)
  {
    if (mPlaybackCallbackList != null) {
      for (int i = 0; i < mPlaybackCallbackList.size(); i++) {
        if (paramAudioPlaybackCallback.equals(mPlaybackCallbackList.get(i)).mCb))
        {
          mPlaybackCallbackList.remove(i);
          return true;
        }
      }
    }
    return false;
  }
  
  private boolean removeRecordCallback_sync(AudioRecordingCallback paramAudioRecordingCallback)
  {
    if (mRecordCallbackList != null) {
      for (int i = 0; i < mRecordCallbackList.size(); i++) {
        if (paramAudioRecordingCallback.equals(mRecordCallbackList.get(i)).mCb))
        {
          mRecordCallbackList.remove(i);
          return true;
        }
      }
    }
    return false;
  }
  
  static int resetAudioPortGeneration()
  {
    synchronized (sAudioPortGeneration)
    {
      int i = sAudioPortGeneration.intValue();
      sAudioPortGeneration = Integer.valueOf(0);
      return i;
    }
  }
  
  public static int setAudioPortGain(AudioPort paramAudioPort, AudioGainConfig paramAudioGainConfig)
  {
    if ((paramAudioPort != null) && (paramAudioGainConfig != null))
    {
      AudioPortConfig localAudioPortConfig = paramAudioPort.activeConfig();
      paramAudioPort = new AudioPortConfig(paramAudioPort, localAudioPortConfig.samplingRate(), localAudioPortConfig.channelMask(), localAudioPortConfig.format(), paramAudioGainConfig);
      mConfigMask = 8;
      return AudioSystem.setAudioPortConfig(paramAudioPort);
    }
    return -2;
  }
  
  private void setContext(Context paramContext)
  {
    mApplicationContext = paramContext.getApplicationContext();
    if (mApplicationContext != null) {
      mOriginalContext = null;
    } else {
      mOriginalContext = paramContext;
    }
  }
  
  public static void setPortIdForMicrophones(ArrayList<MicrophoneInfo> paramArrayList)
  {
    AudioDeviceInfo[] arrayOfAudioDeviceInfo = getDevicesStatic(1);
    for (int i = paramArrayList.size() - 1; i >= 0; i--)
    {
      int j = 0;
      int k = arrayOfAudioDeviceInfo.length;
      int n;
      Object localObject;
      for (int m = 0;; m++)
      {
        n = j;
        if (m >= k) {
          break;
        }
        localObject = arrayOfAudioDeviceInfo[m];
        if ((((AudioDeviceInfo)localObject).getPort().type() == ((MicrophoneInfo)paramArrayList.get(i)).getInternalDeviceType()) && (TextUtils.equals(((AudioDeviceInfo)localObject).getAddress(), ((MicrophoneInfo)paramArrayList.get(i)).getAddress())))
        {
          ((MicrophoneInfo)paramArrayList.get(i)).setId(((AudioDeviceInfo)localObject).getId());
          n = 1;
          break;
        }
      }
      if (n == 0)
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("Failed to find port id for device with type:");
        ((StringBuilder)localObject).append(((MicrophoneInfo)paramArrayList.get(i)).getType());
        ((StringBuilder)localObject).append(" address:");
        ((StringBuilder)localObject).append(((MicrophoneInfo)paramArrayList.get(i)).getAddress());
        Log.i("AudioManager", ((StringBuilder)localObject).toString());
        paramArrayList.remove(i);
      }
    }
  }
  
  static int updateAudioPortCache(ArrayList<AudioPort> paramArrayList1, ArrayList<AudioPatch> paramArrayList, ArrayList<AudioPort> paramArrayList2)
  {
    sAudioPortEventHandler.init();
    synchronized (sAudioPortGeneration)
    {
      if (sAudioPortGeneration.intValue() == 0)
      {
        Object localObject1 = new int[1];
        int[] arrayOfInt = new int[1];
        ArrayList localArrayList1 = new java/util/ArrayList;
        localArrayList1.<init>();
        ArrayList localArrayList2 = new java/util/ArrayList;
        localArrayList2.<init>();
        do
        {
          localArrayList1.clear();
          i = AudioSystem.listAudioPorts(localArrayList1, arrayOfInt);
          if (i != 0)
          {
            Log.w("AudioManager", "updateAudioPortCache: listAudioPorts failed");
            return i;
          }
          localArrayList2.clear();
          i = AudioSystem.listAudioPatches(localArrayList2, (int[])localObject1);
          if (i != 0)
          {
            Log.w("AudioManager", "updateAudioPortCache: listAudioPatches failed");
            return i;
          }
        } while ((localObject1[0] != arrayOfInt[0]) && ((paramArrayList1 == null) || (paramArrayList == null)));
        if (localObject1[0] != arrayOfInt[0]) {
          return -1;
        }
        int j;
        for (int i = 0; i < localArrayList2.size(); i++)
        {
          for (j = 0; j < ((AudioPatch)localArrayList2.get(i)).sources().length; j++)
          {
            localObject1 = updatePortConfig(((AudioPatch)localArrayList2.get(i)).sources()[j], localArrayList1);
            ((AudioPatch)localArrayList2.get(i)).sources()[j] = localObject1;
          }
          for (j = 0; j < ((AudioPatch)localArrayList2.get(i)).sinks().length; j++)
          {
            localObject1 = updatePortConfig(((AudioPatch)localArrayList2.get(i)).sinks()[j], localArrayList1);
            ((AudioPatch)localArrayList2.get(i)).sinks()[j] = localObject1;
          }
        }
        localObject1 = localArrayList2.iterator();
        while (((Iterator)localObject1).hasNext())
        {
          Object localObject2 = (AudioPatch)((Iterator)localObject1).next();
          int k = 0;
          AudioPortConfig[] arrayOfAudioPortConfig = ((AudioPatch)localObject2).sources();
          int m = arrayOfAudioPortConfig.length;
          for (j = 0;; j++)
          {
            i = k;
            if (j >= m) {
              break;
            }
            if (arrayOfAudioPortConfig[j] == null)
            {
              i = 1;
              break;
            }
          }
          localObject2 = ((AudioPatch)localObject2).sinks();
          m = localObject2.length;
          for (k = 0;; k++)
          {
            j = i;
            if (k >= m) {
              break;
            }
            if (localObject2[k] == null)
            {
              j = 1;
              break;
            }
          }
          if (j != 0) {
            ((Iterator)localObject1).remove();
          }
        }
        sPreviousAudioPortsCached = sAudioPortsCached;
        sAudioPortsCached = localArrayList1;
        sAudioPatchesCached = localArrayList2;
        sAudioPortGeneration = Integer.valueOf(arrayOfInt[0]);
      }
      if (paramArrayList1 != null)
      {
        paramArrayList1.clear();
        paramArrayList1.addAll(sAudioPortsCached);
      }
      if (paramArrayList != null)
      {
        paramArrayList.clear();
        paramArrayList.addAll(sAudioPatchesCached);
      }
      if (paramArrayList2 != null)
      {
        paramArrayList2.clear();
        paramArrayList2.addAll(sPreviousAudioPortsCached);
      }
      return 0;
    }
  }
  
  static AudioPortConfig updatePortConfig(AudioPortConfig paramAudioPortConfig, ArrayList<AudioPort> paramArrayList)
  {
    Object localObject1 = paramAudioPortConfig.port();
    Object localObject2;
    for (int i = 0;; i++)
    {
      localObject2 = localObject1;
      if (i >= paramArrayList.size()) {
        break;
      }
      if (((AudioPort)paramArrayList.get(i)).handle().equals(((AudioPort)localObject1).handle()))
      {
        localObject2 = (AudioPort)paramArrayList.get(i);
        break;
      }
    }
    if (i == paramArrayList.size())
    {
      paramAudioPortConfig = new StringBuilder();
      paramAudioPortConfig.append("updatePortConfig port not found for handle: ");
      paramAudioPortConfig.append(((AudioPort)localObject2).handle().id());
      Log.e("AudioManager", paramAudioPortConfig.toString());
      return null;
    }
    localObject1 = paramAudioPortConfig.gain();
    paramArrayList = (ArrayList<AudioPort>)localObject1;
    if (localObject1 != null) {
      paramArrayList = ((AudioPort)localObject2).gain(((AudioGainConfig)localObject1).index()).buildConfig(((AudioGainConfig)localObject1).mode(), ((AudioGainConfig)localObject1).channelMask(), ((AudioGainConfig)localObject1).values(), ((AudioGainConfig)localObject1).rampDurationMs());
    }
    return ((AudioPort)localObject2).buildConfig(paramAudioPortConfig.samplingRate(), paramAudioPortConfig.channelMask(), paramAudioPortConfig.format(), paramArrayList);
  }
  
  public int abandonAudioFocus(OnAudioFocusChangeListener paramOnAudioFocusChangeListener)
  {
    return abandonAudioFocus(paramOnAudioFocusChangeListener, null);
  }
  
  @SystemApi
  @SuppressLint({"Doclava125"})
  public int abandonAudioFocus(OnAudioFocusChangeListener paramOnAudioFocusChangeListener, AudioAttributes paramAudioAttributes)
  {
    unregisterAudioFocusRequest(paramOnAudioFocusChangeListener);
    IAudioService localIAudioService = getService();
    try
    {
      int i = localIAudioService.abandonAudioFocus(mAudioFocusDispatcher, getIdForAudioFocusListener(paramOnAudioFocusChangeListener), paramAudioAttributes, getContext().getOpPackageName());
      return i;
    }
    catch (RemoteException paramOnAudioFocusChangeListener)
    {
      throw paramOnAudioFocusChangeListener.rethrowFromSystemServer();
    }
  }
  
  public void abandonAudioFocusForCall()
  {
    IAudioService localIAudioService = getService();
    try
    {
      localIAudioService.abandonAudioFocus(null, "AudioFocus_For_Phone_Ring_And_Calls", null, getContext().getOpPackageName());
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public int abandonAudioFocusRequest(AudioFocusRequest paramAudioFocusRequest)
  {
    if (paramAudioFocusRequest != null) {
      return abandonAudioFocus(paramAudioFocusRequest.getOnAudioFocusChangeListener(), paramAudioFocusRequest.getAudioAttributes());
    }
    throw new IllegalArgumentException("Illegal null AudioFocusRequest");
  }
  
  public void adjustStreamVolume(int paramInt1, int paramInt2, int paramInt3)
  {
    IAudioService localIAudioService = getService();
    try
    {
      localIAudioService.adjustStreamVolume(paramInt1, paramInt2, paramInt3, getContext().getOpPackageName());
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void adjustSuggestedStreamVolume(int paramInt1, int paramInt2, int paramInt3)
  {
    MediaSessionLegacyHelper.getHelper(getContext()).sendAdjustVolumeBy(paramInt2, paramInt1, paramInt3);
  }
  
  public void adjustVolume(int paramInt1, int paramInt2)
  {
    MediaSessionLegacyHelper.getHelper(getContext()).sendAdjustVolumeBy(Integer.MIN_VALUE, paramInt1, paramInt2);
  }
  
  public void avrcpSupportsAbsoluteVolume(String paramString, boolean paramBoolean)
  {
    IAudioService localIAudioService = getService();
    try
    {
      localIAudioService.avrcpSupportsAbsoluteVolume(paramString, paramBoolean);
      return;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  @SystemApi
  public void clearAudioServerStateCallback()
  {
    synchronized (mAudioServerStateCbLock)
    {
      if (mAudioServerStateCb != null)
      {
        IAudioService localIAudioService = getService();
        try
        {
          localIAudioService.unregisterAudioServerStateDispatcher(mAudioServerStateDispatcher);
        }
        catch (RemoteException localRemoteException)
        {
          throw localRemoteException.rethrowFromSystemServer();
        }
      }
      mAudioServerStateExec = null;
      mAudioServerStateCb = null;
      return;
    }
  }
  
  public void disableSafeMediaVolume()
  {
    try
    {
      getService().disableSafeMediaVolume(mApplicationContext.getOpPackageName());
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  @SystemApi
  public int dispatchAudioFocusChange(AudioFocusInfo paramAudioFocusInfo, int paramInt, AudioPolicy paramAudioPolicy)
  {
    if (paramAudioFocusInfo != null)
    {
      if (paramAudioPolicy != null)
      {
        IAudioService localIAudioService = getService();
        try
        {
          paramInt = localIAudioService.dispatchFocusChange(paramAudioFocusInfo, paramInt, paramAudioPolicy.cb());
          return paramInt;
        }
        catch (RemoteException paramAudioFocusInfo)
        {
          throw paramAudioFocusInfo.rethrowFromSystemServer();
        }
      }
      throw new NullPointerException("Illegal null AudioPolicy");
    }
    throw new NullPointerException("Illegal null AudioFocusInfo");
  }
  
  public void dispatchMediaKeyEvent(KeyEvent paramKeyEvent)
  {
    MediaSessionLegacyHelper.getHelper(getContext()).sendMediaButtonEvent(paramKeyEvent, false);
  }
  
  public void forceVolumeControlStream(int paramInt)
  {
    IAudioService localIAudioService = getService();
    try
    {
      localIAudioService.forceVolumeControlStream(paramInt, mICallBack);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public int generateAudioSessionId()
  {
    int i = AudioSystem.newAudioSessionId();
    if (i > 0) {
      return i;
    }
    Log.e("AudioManager", "Failure to generate a new audio session ID");
    return -1;
  }
  
  public List<AudioPlaybackConfiguration> getActivePlaybackConfigurations()
  {
    Object localObject = getService();
    try
    {
      localObject = ((IAudioService)localObject).getActivePlaybackConfigurations();
      return localObject;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public List<AudioRecordingConfiguration> getActiveRecordingConfigurations()
  {
    Object localObject = getService();
    try
    {
      localObject = ((IAudioService)localObject).getActiveRecordingConfigurations();
      return localObject;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public AudioDeviceInfo[] getDevices(int paramInt)
  {
    return getDevicesStatic(paramInt);
  }
  
  public int getDevicesForStream(int paramInt)
  {
    if ((paramInt != 8) && (paramInt != 10)) {
      switch (paramInt)
      {
      default: 
        return 0;
      }
    }
    return AudioSystem.getDevicesForStream(paramInt);
  }
  
  public int getFocusRampTimeMs(int paramInt, AudioAttributes paramAudioAttributes)
  {
    IAudioService localIAudioService = getService();
    try
    {
      paramInt = localIAudioService.getFocusRampTimeMs(paramInt, paramAudioAttributes);
      return paramInt;
    }
    catch (RemoteException paramAudioAttributes)
    {
      throw paramAudioAttributes.rethrowFromSystemServer();
    }
  }
  
  public int getLastAudibleStreamVolume(int paramInt)
  {
    IAudioService localIAudioService = getService();
    try
    {
      paramInt = localIAudioService.getLastAudibleStreamVolume(paramInt);
      return paramInt;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public List<MicrophoneInfo> getMicrophones()
    throws IOException
  {
    ArrayList localArrayList = new ArrayList();
    int i = AudioSystem.getMicrophones(localArrayList);
    HashSet localHashSet = new HashSet();
    localHashSet.add(Integer.valueOf(18));
    if (i != 0)
    {
      if (i != -3)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("getMicrophones failed:");
        localStringBuilder.append(i);
        Log.e("AudioManager", localStringBuilder.toString());
      }
      Log.i("AudioManager", "fallback on device info");
      addMicrophonesFromAudioDeviceInfo(localArrayList, localHashSet);
      return localArrayList;
    }
    setPortIdForMicrophones(localArrayList);
    localHashSet.add(Integer.valueOf(15));
    addMicrophonesFromAudioDeviceInfo(localArrayList, localHashSet);
    return localArrayList;
  }
  
  public int getMode()
  {
    IAudioService localIAudioService = getService();
    try
    {
      int i = localIAudioService.getMode();
      return i;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public int getOutputLatency(int paramInt)
  {
    return AudioSystem.getOutputLatency(paramInt);
  }
  
  public String getParameters(String paramString)
  {
    return AudioSystem.getParameters(paramString);
  }
  
  public String getProperty(String paramString)
  {
    boolean bool = "android.media.property.OUTPUT_SAMPLE_RATE".equals(paramString);
    Object localObject1 = null;
    Object localObject2 = null;
    int i;
    if (bool)
    {
      i = AudioSystem.getPrimaryOutputSamplingRate();
      paramString = localObject2;
      if (i > 0) {
        paramString = Integer.toString(i);
      }
      return paramString;
    }
    if ("android.media.property.OUTPUT_FRAMES_PER_BUFFER".equals(paramString))
    {
      i = AudioSystem.getPrimaryOutputFrameCount();
      paramString = localObject1;
      if (i > 0) {
        paramString = Integer.toString(i);
      }
      return paramString;
    }
    if ("android.media.property.SUPPORT_MIC_NEAR_ULTRASOUND".equals(paramString)) {
      return String.valueOf(getContext().getResources().getBoolean(17957045));
    }
    if ("android.media.property.SUPPORT_SPEAKER_NEAR_ULTRASOUND".equals(paramString)) {
      return String.valueOf(getContext().getResources().getBoolean(17957047));
    }
    if ("android.media.property.SUPPORT_AUDIO_SOURCE_UNPROCESSED".equals(paramString)) {
      return String.valueOf(getContext().getResources().getBoolean(17957040));
    }
    return null;
  }
  
  public Map<Integer, Boolean> getReportedSurroundFormats()
  {
    Object localObject = new HashMap();
    int i = AudioSystem.getSurroundFormats((Map)localObject, true);
    if (i != 0)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("getReportedSurroundFormats failed:");
      ((StringBuilder)localObject).append(i);
      Log.e("AudioManager", ((StringBuilder)localObject).toString());
      return new HashMap();
    }
    return localObject;
  }
  
  public int getRingerMode()
  {
    IAudioService localIAudioService = getService();
    try
    {
      int i = localIAudioService.getRingerModeExternal();
      return i;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public int getRingerModeInternal()
  {
    try
    {
      int i = getService().getRingerModeInternal();
      return i;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public IRingtonePlayer getRingtonePlayer()
  {
    try
    {
      IRingtonePlayer localIRingtonePlayer = getService().getRingtonePlayer();
      return localIRingtonePlayer;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  @Deprecated
  public int getRouting(int paramInt)
  {
    return -1;
  }
  
  public int getStreamMaxVolume(int paramInt)
  {
    IAudioService localIAudioService = getService();
    try
    {
      paramInt = localIAudioService.getStreamMaxVolume(paramInt);
      return paramInt;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public int getStreamMinVolume(int paramInt)
  {
    if (isPublicStreamType(paramInt)) {
      return getStreamMinVolumeInt(paramInt);
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Invalid stream type ");
    localStringBuilder.append(paramInt);
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  public int getStreamMinVolumeInt(int paramInt)
  {
    IAudioService localIAudioService = getService();
    try
    {
      paramInt = localIAudioService.getStreamMinVolume(paramInt);
      return paramInt;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public int getStreamVolume(int paramInt)
  {
    IAudioService localIAudioService = getService();
    try
    {
      paramInt = localIAudioService.getStreamVolume(paramInt);
      return paramInt;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public float getStreamVolumeDb(int paramInt1, int paramInt2, int paramInt3)
  {
    if (isPublicStreamType(paramInt1))
    {
      if ((paramInt2 <= getStreamMaxVolume(paramInt1)) && (paramInt2 >= getStreamMinVolume(paramInt1)))
      {
        if (AudioDeviceInfo.isValidAudioDeviceTypeOut(paramInt3))
        {
          float f = AudioSystem.getStreamVolumeDB(paramInt1, paramInt2, AudioDeviceInfo.convertDeviceTypeToInternalDevice(paramInt3));
          if (f <= -758.0F) {
            return Float.NEGATIVE_INFINITY;
          }
          return f;
        }
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("Invalid audio output device type ");
        localStringBuilder.append(paramInt3);
        throw new IllegalArgumentException(localStringBuilder.toString());
      }
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("Invalid stream volume index ");
      localStringBuilder.append(paramInt2);
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Invalid stream type ");
    localStringBuilder.append(paramInt1);
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  public Map<Integer, Boolean> getSurroundFormats()
  {
    Object localObject = new HashMap();
    int i = AudioSystem.getSurroundFormats((Map)localObject, false);
    if (i != 0)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("getSurroundFormats failed:");
      ((StringBuilder)localObject).append(i);
      Log.e("AudioManager", ((StringBuilder)localObject).toString());
      return new HashMap();
    }
    return localObject;
  }
  
  public int getUiSoundsStreamType()
  {
    IAudioService localIAudioService = getService();
    try
    {
      int i = localIAudioService.getUiSoundsStreamType();
      return i;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public int getVibrateSetting(int paramInt)
  {
    IAudioService localIAudioService = getService();
    try
    {
      paramInt = localIAudioService.getVibrateSetting(paramInt);
      return paramInt;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void handleBluetoothA2dpDeviceConfigChange(BluetoothDevice paramBluetoothDevice)
  {
    IAudioService localIAudioService = getService();
    try
    {
      localIAudioService.handleBluetoothA2dpDeviceConfigChange(paramBluetoothDevice);
      return;
    }
    catch (RemoteException paramBluetoothDevice)
    {
      throw paramBluetoothDevice.rethrowFromSystemServer();
    }
  }
  
  public boolean isAudioFocusExclusive()
  {
    IAudioService localIAudioService = getService();
    try
    {
      int i = localIAudioService.getCurrentAudioFocus();
      boolean bool;
      if (i == 4) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  @SystemApi
  public boolean isAudioServerRunning()
  {
    IAudioService localIAudioService = getService();
    try
    {
      boolean bool = localIAudioService.isAudioServerRunning();
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public boolean isBluetoothA2dpOn()
  {
    if (AudioSystem.getDeviceConnectionState(128, "") == 1) {
      return true;
    }
    if (AudioSystem.getDeviceConnectionState(256, "") == 1) {
      return true;
    }
    return AudioSystem.getDeviceConnectionState(512, "") == 1;
  }
  
  public boolean isBluetoothScoAvailableOffCall()
  {
    boolean bool = getContext().getResources().getBoolean(17956905);
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("In isBluetoothScoAvailableOffCall(), calling appilication: ");
    localStringBuilder.append(mApplicationContext.getOpPackageName());
    localStringBuilder.append(", return value: ");
    localStringBuilder.append(bool);
    Log.i("AudioManager", localStringBuilder.toString());
    return bool;
  }
  
  public boolean isBluetoothScoOn()
  {
    IAudioService localIAudioService = getService();
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("In isBluetoothScoOn(), calling application: ");
    localStringBuilder.append(mApplicationContext.getOpPackageName());
    Log.i("AudioManager", localStringBuilder.toString());
    try
    {
      boolean bool = localIAudioService.isBluetoothScoOn();
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  @SystemApi
  @SuppressLint({"Doclava125"})
  public boolean isHdmiSystemAudioSupported()
  {
    try
    {
      boolean bool = getService().isHdmiSystemAudioSupported();
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public boolean isMasterMute()
  {
    IAudioService localIAudioService = getService();
    try
    {
      boolean bool = localIAudioService.isMasterMute();
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public boolean isMicrophoneMute()
  {
    return AudioSystem.isMicrophoneMuted();
  }
  
  public boolean isMusicActive()
  {
    return AudioSystem.isStreamActive(3, 0);
  }
  
  public boolean isMusicActiveRemotely()
  {
    return AudioSystem.isStreamActiveRemotely(3, 0);
  }
  
  public boolean isOffloadedPlaybackSupported(AudioFormat paramAudioFormat)
  {
    return AudioSystem.isOffloadSupported(paramAudioFormat);
  }
  
  public boolean isSilentMode()
  {
    int i = getRingerMode();
    boolean bool1 = true;
    boolean bool2 = bool1;
    if (i != 0) {
      if (i == 1) {
        bool2 = bool1;
      } else {
        bool2 = false;
      }
    }
    return bool2;
  }
  
  public boolean isSpeakerphoneOn()
  {
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("In isSpeakerphoneOn(), calling application: ");
    ((StringBuilder)localObject).append(mApplicationContext.getOpPackageName());
    Log.d("AudioManager", ((StringBuilder)localObject).toString());
    localObject = getService();
    try
    {
      boolean bool = ((IAudioService)localObject).isSpeakerphoneOn();
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public boolean isStreamAffectedByMute(int paramInt)
  {
    try
    {
      boolean bool = getService().isStreamAffectedByMute(paramInt);
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public boolean isStreamAffectedByRingerMode(int paramInt)
  {
    try
    {
      boolean bool = getService().isStreamAffectedByRingerMode(paramInt);
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public boolean isStreamMute(int paramInt)
  {
    IAudioService localIAudioService = getService();
    try
    {
      boolean bool = localIAudioService.isStreamMute(paramInt);
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public boolean isVolumeFixed()
  {
    return mUseFixedVolume;
  }
  
  public boolean isWiredHeadsetOn()
  {
    return (AudioSystem.getDeviceConnectionState(4, "") != 0) || (AudioSystem.getDeviceConnectionState(8, "") != 0) || (AudioSystem.getDeviceConnectionState(67108864, "") != 0);
  }
  
  public void loadSoundEffects()
  {
    IAudioService localIAudioService = getService();
    try
    {
      localIAudioService.loadSoundEffects();
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void notifyVolumeControllerVisible(IVolumeController paramIVolumeController, boolean paramBoolean)
  {
    try
    {
      getService().notifyVolumeControllerVisible(paramIVolumeController, paramBoolean);
      return;
    }
    catch (RemoteException paramIVolumeController)
    {
      throw paramIVolumeController.rethrowFromSystemServer();
    }
  }
  
  public void playSoundEffect(int paramInt)
  {
    if ((paramInt >= 0) && (paramInt < 10))
    {
      if (!querySoundEffectsEnabled(Process.myUserHandle().getIdentifier())) {
        return;
      }
      IAudioService localIAudioService = getService();
      try
      {
        localIAudioService.playSoundEffect(paramInt);
        return;
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
  }
  
  public void playSoundEffect(int paramInt, float paramFloat)
  {
    if ((paramInt >= 0) && (paramInt < 10))
    {
      IAudioService localIAudioService = getService();
      try
      {
        localIAudioService.playSoundEffectVolume(paramInt, paramFloat);
        return;
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
  }
  
  public void playSoundEffect(int paramInt1, int paramInt2)
  {
    if ((paramInt1 >= 0) && (paramInt1 < 10))
    {
      if (!querySoundEffectsEnabled(paramInt2)) {
        return;
      }
      IAudioService localIAudioService = getService();
      try
      {
        localIAudioService.playSoundEffect(paramInt1);
        return;
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
  }
  
  public void preDispatchKeyEvent(KeyEvent paramKeyEvent, int paramInt)
  {
    int i = paramKeyEvent.getKeyCode();
    if ((i != 25) && (i != 24) && (i != 164) && (mVolumeKeyUpTime + 300L > SystemClock.uptimeMillis())) {
      adjustSuggestedStreamVolume(0, paramInt, 8);
    }
  }
  
  public void registerAudioDeviceCallback(AudioDeviceCallback paramAudioDeviceCallback, Handler paramHandler)
  {
    ArrayMap localArrayMap = mDeviceCallbacks;
    if (paramAudioDeviceCallback != null) {
      try
      {
        if (!mDeviceCallbacks.containsKey(paramAudioDeviceCallback))
        {
          if (mDeviceCallbacks.size() == 0)
          {
            if (mPortListener == null)
            {
              localObject = new android/media/AudioManager$OnAmPortUpdateListener;
              ((OnAmPortUpdateListener)localObject).<init>(this, null);
              mPortListener = ((OnAmPortUpdateListener)localObject);
            }
            registerAudioPortUpdateListener(mPortListener);
          }
          Object localObject = new android/media/AudioManager$NativeEventHandlerDelegate;
          ((NativeEventHandlerDelegate)localObject).<init>(this, paramAudioDeviceCallback, paramHandler);
          mDeviceCallbacks.put(paramAudioDeviceCallback, localObject);
          broadcastDeviceListChange_sync(((NativeEventHandlerDelegate)localObject).getHandler());
        }
      }
      finally
      {
        break label108;
      }
    }
    return;
    label108:
    throw paramAudioDeviceCallback;
  }
  
  public void registerAudioFocusRequest(AudioFocusRequest paramAudioFocusRequest)
  {
    Object localObject = paramAudioFocusRequest.getOnAudioFocusChangeListenerHandler();
    if (localObject == null) {
      localObject = null;
    } else {
      localObject = new ServiceEventHandlerDelegate((Handler)localObject).getHandler();
    }
    localObject = new FocusRequestInfo(paramAudioFocusRequest, (Handler)localObject);
    paramAudioFocusRequest = getIdForAudioFocusListener(paramAudioFocusRequest.getOnAudioFocusChangeListener());
    mAudioFocusIdListenerMap.put(paramAudioFocusRequest, localObject);
  }
  
  public void registerAudioPlaybackCallback(AudioPlaybackCallback paramAudioPlaybackCallback, Handler paramHandler)
  {
    if (paramAudioPlaybackCallback != null) {
      synchronized (mPlaybackCallbackLock)
      {
        Object localObject2;
        if (mPlaybackCallbackList == null)
        {
          localObject2 = new java/util/ArrayList;
          ((ArrayList)localObject2).<init>();
          mPlaybackCallbackList = ((List)localObject2);
        }
        int i = mPlaybackCallbackList.size();
        if (!hasPlaybackCallback_sync(paramAudioPlaybackCallback))
        {
          List localList = mPlaybackCallbackList;
          localObject2 = new android/media/AudioManager$AudioPlaybackCallbackInfo;
          ServiceEventHandlerDelegate localServiceEventHandlerDelegate = new android/media/AudioManager$ServiceEventHandlerDelegate;
          localServiceEventHandlerDelegate.<init>(this, paramHandler);
          ((AudioPlaybackCallbackInfo)localObject2).<init>(paramAudioPlaybackCallback, localServiceEventHandlerDelegate.getHandler());
          localList.add(localObject2);
          int j = mPlaybackCallbackList.size();
          if ((i == 0) && (j > 0)) {
            try
            {
              getService().registerPlaybackCallback(mPlayCb);
            }
            catch (RemoteException paramAudioPlaybackCallback)
            {
              throw paramAudioPlaybackCallback.rethrowFromSystemServer();
            }
          }
        }
        else
        {
          Log.w("AudioManager", "attempt to call registerAudioPlaybackCallback() on a previouslyregistered callback");
        }
        return;
      }
    }
    throw new IllegalArgumentException("Illegal null AudioPlaybackCallback argument");
  }
  
  @SystemApi
  public int registerAudioPolicy(AudioPolicy paramAudioPolicy)
  {
    if (paramAudioPolicy != null)
    {
      Object localObject = getService();
      try
      {
        localObject = ((IAudioService)localObject).registerAudioPolicy(paramAudioPolicy.getConfig(), paramAudioPolicy.cb(), paramAudioPolicy.hasFocusListener(), paramAudioPolicy.isFocusPolicy(), paramAudioPolicy.isVolumeController());
        if (localObject == null) {
          return -1;
        }
        paramAudioPolicy.setRegistration((String)localObject);
        return 0;
      }
      catch (RemoteException paramAudioPolicy)
      {
        throw paramAudioPolicy.rethrowFromSystemServer();
      }
    }
    throw new IllegalArgumentException("Illegal null AudioPolicy argument");
  }
  
  public void registerAudioPortUpdateListener(OnAudioPortUpdateListener paramOnAudioPortUpdateListener)
  {
    sAudioPortEventHandler.init();
    sAudioPortEventHandler.registerListener(paramOnAudioPortUpdateListener);
  }
  
  public void registerAudioRecordingCallback(AudioRecordingCallback paramAudioRecordingCallback, Handler paramHandler)
  {
    if (paramAudioRecordingCallback != null) {
      synchronized (mRecordCallbackLock)
      {
        Object localObject2;
        if (mRecordCallbackList == null)
        {
          localObject2 = new java/util/ArrayList;
          ((ArrayList)localObject2).<init>();
          mRecordCallbackList = ((List)localObject2);
        }
        int i = mRecordCallbackList.size();
        if (!hasRecordCallback_sync(paramAudioRecordingCallback))
        {
          List localList = mRecordCallbackList;
          localObject2 = new android/media/AudioManager$AudioRecordingCallbackInfo;
          ServiceEventHandlerDelegate localServiceEventHandlerDelegate = new android/media/AudioManager$ServiceEventHandlerDelegate;
          localServiceEventHandlerDelegate.<init>(this, paramHandler);
          ((AudioRecordingCallbackInfo)localObject2).<init>(paramAudioRecordingCallback, localServiceEventHandlerDelegate.getHandler());
          localList.add(localObject2);
          int j = mRecordCallbackList.size();
          if ((i == 0) && (j > 0))
          {
            paramAudioRecordingCallback = getService();
            try
            {
              paramAudioRecordingCallback.registerRecordingCallback(mRecCb);
            }
            catch (RemoteException paramAudioRecordingCallback)
            {
              throw paramAudioRecordingCallback.rethrowFromSystemServer();
            }
          }
        }
        else
        {
          Log.w("AudioManager", "attempt to call registerAudioRecordingCallback() on a previouslyregistered callback");
        }
        return;
      }
    }
    throw new IllegalArgumentException("Illegal null AudioRecordingCallback argument");
  }
  
  @Deprecated
  public void registerMediaButtonEventReceiver(PendingIntent paramPendingIntent)
  {
    if (paramPendingIntent == null) {
      return;
    }
    registerMediaButtonIntent(paramPendingIntent, null);
  }
  
  @Deprecated
  public void registerMediaButtonEventReceiver(ComponentName paramComponentName)
  {
    if (paramComponentName == null) {
      return;
    }
    if (!paramComponentName.getPackageName().equals(getContext().getPackageName()))
    {
      Log.e("AudioManager", "registerMediaButtonEventReceiver() error: receiver and context package names don't match");
      return;
    }
    Intent localIntent = new Intent("android.intent.action.MEDIA_BUTTON");
    localIntent.setComponent(paramComponentName);
    registerMediaButtonIntent(PendingIntent.getBroadcast(getContext(), 0, localIntent, 0), paramComponentName);
  }
  
  public void registerMediaButtonIntent(PendingIntent paramPendingIntent, ComponentName paramComponentName)
  {
    if (paramPendingIntent == null)
    {
      Log.e("AudioManager", "Cannot call registerMediaButtonIntent() with a null parameter");
      return;
    }
    MediaSessionLegacyHelper.getHelper(getContext()).addMediaButtonListener(paramPendingIntent, paramComponentName, getContext());
  }
  
  @Deprecated
  public void registerRemoteControlClient(RemoteControlClient paramRemoteControlClient)
  {
    if ((paramRemoteControlClient != null) && (paramRemoteControlClient.getRcMediaIntent() != null))
    {
      paramRemoteControlClient.registerWithSession(MediaSessionLegacyHelper.getHelper(getContext()));
      return;
    }
  }
  
  @Deprecated
  public boolean registerRemoteController(RemoteController paramRemoteController)
  {
    if (paramRemoteController == null) {
      return false;
    }
    paramRemoteController.startListeningToSessions();
    return true;
  }
  
  public void reloadAudioSettings()
  {
    IAudioService localIAudioService = getService();
    try
    {
      localIAudioService.reloadAudioSettings();
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public int requestAudioFocus(AudioFocusRequest paramAudioFocusRequest)
  {
    return requestAudioFocus(paramAudioFocusRequest, null);
  }
  
  /* Error */
  @SystemApi
  public int requestAudioFocus(AudioFocusRequest arg1, AudioPolicy paramAudioPolicy)
  {
    // Byte code:
    //   0: aload_1
    //   1: ifnull +265 -> 266
    //   4: aload_1
    //   5: invokevirtual 1543	android/media/AudioFocusRequest:locksFocus	()Z
    //   8: ifeq +21 -> 29
    //   11: aload_2
    //   12: ifnull +6 -> 18
    //   15: goto +14 -> 29
    //   18: new 1074	java/lang/IllegalArgumentException
    //   21: dup
    //   22: ldc_w 1545
    //   25: invokespecial 1077	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
    //   28: athrow
    //   29: aload_0
    //   30: aload_1
    //   31: invokevirtual 1547	android/media/AudioManager:registerAudioFocusRequest	(Landroid/media/AudioFocusRequest;)V
    //   34: invokestatic 819	android/media/AudioManager:getService	()Landroid/media/IAudioService;
    //   37: astore_3
    //   38: aload_0
    //   39: invokespecial 554	android/media/AudioManager:getContext	()Landroid/content/Context;
    //   42: invokevirtual 1551	android/content/Context:getApplicationInfo	()Landroid/content/pm/ApplicationInfo;
    //   45: getfield 1556	android/content/pm/ApplicationInfo:targetSdkVersion	I
    //   48: istore 4
    //   50: goto +10 -> 60
    //   53: astore 5
    //   55: getstatic 1561	android/os/Build$VERSION:SDK_INT	I
    //   58: istore 4
    //   60: aload_0
    //   61: aload_1
    //   62: invokevirtual 1068	android/media/AudioFocusRequest:getOnAudioFocusChangeListener	()Landroid/media/AudioManager$OnAudioFocusChangeListener;
    //   65: invokespecial 1049	android/media/AudioManager:getIdForAudioFocusListener	(Landroid/media/AudioManager$OnAudioFocusChangeListener;)Ljava/lang/String;
    //   68: astore 6
    //   70: aload_0
    //   71: getfield 513	android/media/AudioManager:mFocusRequestsLock	Ljava/lang/Object;
    //   74: astore 5
    //   76: aload 5
    //   78: monitorenter
    //   79: aload_1
    //   80: invokevirtual 1072	android/media/AudioFocusRequest:getAudioAttributes	()Landroid/media/AudioAttributes;
    //   83: astore 7
    //   85: aload_1
    //   86: invokevirtual 1564	android/media/AudioFocusRequest:getFocusGain	()I
    //   89: istore 8
    //   91: aload_0
    //   92: getfield 528	android/media/AudioManager:mICallBack	Landroid/os/IBinder;
    //   95: astore 9
    //   97: aload_0
    //   98: getfield 511	android/media/AudioManager:mAudioFocusDispatcher	Landroid/media/IAudioFocusDispatcher;
    //   101: astore 10
    //   103: aload_0
    //   104: invokespecial 554	android/media/AudioManager:getContext	()Landroid/content/Context;
    //   107: invokevirtual 1052	android/content/Context:getOpPackageName	()Ljava/lang/String;
    //   110: astore 11
    //   112: aload_1
    //   113: invokevirtual 1567	android/media/AudioFocusRequest:getFlags	()I
    //   116: istore 12
    //   118: aload_2
    //   119: ifnull +11 -> 130
    //   122: aload_2
    //   123: invokevirtual 1114	android/media/audiopolicy/AudioPolicy:cb	()Landroid/media/audiopolicy/IAudioPolicyCallback;
    //   126: astore_1
    //   127: goto +5 -> 132
    //   130: aconst_null
    //   131: astore_1
    //   132: aload_3
    //   133: aload 7
    //   135: iload 8
    //   137: aload 9
    //   139: aload 10
    //   141: aload 6
    //   143: aload 11
    //   145: iload 12
    //   147: aload_1
    //   148: iload 4
    //   150: invokeinterface 1570 10 0
    //   155: istore 4
    //   157: iload 4
    //   159: bipush 100
    //   161: if_icmpeq +9 -> 170
    //   164: aload 5
    //   166: monitorexit
    //   167: iload 4
    //   169: ireturn
    //   170: aload_0
    //   171: getfield 598	android/media/AudioManager:mFocusRequestsAwaitingResult	Ljava/util/HashMap;
    //   174: ifnonnull +17 -> 191
    //   177: new 1208	java/util/HashMap
    //   180: astore_1
    //   181: aload_1
    //   182: iconst_1
    //   183: invokespecial 1571	java/util/HashMap:<init>	(I)V
    //   186: aload_0
    //   187: aload_1
    //   188: putfield 598	android/media/AudioManager:mFocusRequestsAwaitingResult	Ljava/util/HashMap;
    //   191: new 29	android/media/AudioManager$BlockingFocusResultReceiver
    //   194: astore_2
    //   195: aload_2
    //   196: aload 6
    //   198: invokespecial 1572	android/media/AudioManager$BlockingFocusResultReceiver:<init>	(Ljava/lang/String;)V
    //   201: aload_0
    //   202: getfield 598	android/media/AudioManager:mFocusRequestsAwaitingResult	Ljava/util/HashMap;
    //   205: aload 6
    //   207: aload_2
    //   208: invokevirtual 1573	java/util/HashMap:put	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   211: pop
    //   212: aload 5
    //   214: monitorexit
    //   215: aload_2
    //   216: ldc2_w 1574
    //   219: invokevirtual 1579	android/media/AudioManager$BlockingFocusResultReceiver:waitForResult	(J)V
    //   222: aload_0
    //   223: getfield 513	android/media/AudioManager:mFocusRequestsLock	Ljava/lang/Object;
    //   226: astore_1
    //   227: aload_1
    //   228: monitorenter
    //   229: aload_0
    //   230: getfield 598	android/media/AudioManager:mFocusRequestsAwaitingResult	Ljava/util/HashMap;
    //   233: aload 6
    //   235: invokevirtual 1581	java/util/HashMap:remove	(Ljava/lang/Object;)Ljava/lang/Object;
    //   238: pop
    //   239: aload_1
    //   240: monitorexit
    //   241: aload_2
    //   242: invokevirtual 1584	android/media/AudioManager$BlockingFocusResultReceiver:requestResult	()I
    //   245: ireturn
    //   246: astore_2
    //   247: aload_1
    //   248: monitorexit
    //   249: aload_2
    //   250: athrow
    //   251: astore_1
    //   252: goto +9 -> 261
    //   255: astore_1
    //   256: aload_1
    //   257: invokevirtual 827	android/os/RemoteException:rethrowFromSystemServer	()Ljava/lang/RuntimeException;
    //   260: athrow
    //   261: aload 5
    //   263: monitorexit
    //   264: aload_1
    //   265: athrow
    //   266: new 1120	java/lang/NullPointerException
    //   269: dup
    //   270: ldc_w 1076
    //   273: invokespecial 1123	java/lang/NullPointerException:<init>	(Ljava/lang/String;)V
    //   276: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	277	0	this	AudioManager
    //   0	277	2	paramAudioPolicy	AudioPolicy
    //   37	96	3	localIAudioService	IAudioService
    //   48	120	4	i	int
    //   53	1	5	localNullPointerException	NullPointerException
    //   74	188	5	localObject	Object
    //   68	166	6	str1	String
    //   83	51	7	localAudioAttributes	AudioAttributes
    //   89	47	8	j	int
    //   95	43	9	localIBinder	IBinder
    //   101	39	10	localIAudioFocusDispatcher	IAudioFocusDispatcher
    //   110	34	11	str2	String
    //   116	30	12	k	int
    // Exception table:
    //   from	to	target	type
    //   38	50	53	java/lang/NullPointerException
    //   229	241	246	finally
    //   247	249	246	finally
    //   79	118	251	finally
    //   122	127	251	finally
    //   132	157	251	finally
    //   164	167	251	finally
    //   170	191	251	finally
    //   191	215	251	finally
    //   256	261	251	finally
    //   261	264	251	finally
    //   79	118	255	android/os/RemoteException
    //   122	127	255	android/os/RemoteException
    //   132	157	255	android/os/RemoteException
  }
  
  public int requestAudioFocus(OnAudioFocusChangeListener paramOnAudioFocusChangeListener, int paramInt1, int paramInt2)
  {
    PlayerBase.deprecateStreamTypeForPlayback(paramInt1, "AudioManager", "requestAudioFocus()");
    int i = 0;
    try
    {
      AudioAttributes.Builder localBuilder = new android/media/AudioAttributes$Builder;
      localBuilder.<init>();
      paramInt1 = requestAudioFocus(paramOnAudioFocusChangeListener, localBuilder.setInternalLegacyStreamType(paramInt1).build(), paramInt2, 0);
    }
    catch (IllegalArgumentException paramOnAudioFocusChangeListener)
    {
      Log.e("AudioManager", "Audio focus request denied due to ", paramOnAudioFocusChangeListener);
      paramInt1 = i;
    }
    return paramInt1;
  }
  
  @SystemApi
  public int requestAudioFocus(OnAudioFocusChangeListener paramOnAudioFocusChangeListener, AudioAttributes paramAudioAttributes, int paramInt1, int paramInt2)
    throws IllegalArgumentException
  {
    if (paramInt2 == (paramInt2 & 0x3)) {
      return requestAudioFocus(paramOnAudioFocusChangeListener, paramAudioAttributes, paramInt1, paramInt2 & 0x3, null);
    }
    paramOnAudioFocusChangeListener = new StringBuilder();
    paramOnAudioFocusChangeListener.append("Invalid flags 0x");
    paramOnAudioFocusChangeListener.append(Integer.toHexString(paramInt2).toUpperCase());
    throw new IllegalArgumentException(paramOnAudioFocusChangeListener.toString());
  }
  
  @SystemApi
  public int requestAudioFocus(OnAudioFocusChangeListener paramOnAudioFocusChangeListener, AudioAttributes paramAudioAttributes, int paramInt1, int paramInt2, AudioPolicy paramAudioPolicy)
    throws IllegalArgumentException
  {
    if (paramAudioAttributes != null)
    {
      if (AudioFocusRequest.isValidFocusGain(paramInt1))
      {
        if (paramInt2 == (paramInt2 & 0x7))
        {
          boolean bool1 = true;
          if (((paramInt2 & 0x1) == 1) && (paramOnAudioFocusChangeListener == null)) {
            throw new IllegalArgumentException("Illegal null focus listener when flagged as accepting delayed focus grant");
          }
          if (((paramInt2 & 0x2) == 2) && (paramOnAudioFocusChangeListener == null)) {
            throw new IllegalArgumentException("Illegal null focus listener when flagged as pausing instead of ducking");
          }
          if (((paramInt2 & 0x4) == 4) && (paramAudioPolicy == null)) {
            throw new IllegalArgumentException("Illegal null audio policy when locking audio focus");
          }
          paramOnAudioFocusChangeListener = new AudioFocusRequest.Builder(paramInt1).setOnAudioFocusChangeListenerInt(paramOnAudioFocusChangeListener, null).setAudioAttributes(paramAudioAttributes);
          boolean bool2;
          if ((paramInt2 & 0x1) == 1) {
            bool2 = true;
          } else {
            bool2 = false;
          }
          paramOnAudioFocusChangeListener = paramOnAudioFocusChangeListener.setAcceptsDelayedFocusGain(bool2);
          if ((paramInt2 & 0x2) == 2) {
            bool2 = true;
          } else {
            bool2 = false;
          }
          paramOnAudioFocusChangeListener = paramOnAudioFocusChangeListener.setWillPauseWhenDucked(bool2);
          if ((paramInt2 & 0x4) == 4) {
            bool2 = bool1;
          } else {
            bool2 = false;
          }
          return requestAudioFocus(paramOnAudioFocusChangeListener.setLocksFocus(bool2).build(), paramAudioPolicy);
        }
        paramOnAudioFocusChangeListener = new StringBuilder();
        paramOnAudioFocusChangeListener.append("Illegal flags 0x");
        paramOnAudioFocusChangeListener.append(Integer.toHexString(paramInt2).toUpperCase());
        throw new IllegalArgumentException(paramOnAudioFocusChangeListener.toString());
      }
      throw new IllegalArgumentException("Invalid duration hint");
    }
    throw new IllegalArgumentException("Illegal null AudioAttributes argument");
  }
  
  public void requestAudioFocusForCall(int paramInt1, int paramInt2)
  {
    IAudioService localIAudioService = getService();
    try
    {
      AudioAttributes.Builder localBuilder = new android/media/AudioAttributes$Builder;
      localBuilder.<init>();
      localIAudioService.requestAudioFocus(localBuilder.setInternalLegacyStreamType(paramInt1).build(), paramInt2, mICallBack, null, "AudioFocus_For_Phone_Ring_And_Calls", getContext().getOpPackageName(), 4, null, 0);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  @SystemApi
  public void setAudioServerStateCallback(Executor paramExecutor, AudioServerStateCallback paramAudioServerStateCallback)
  {
    if (paramAudioServerStateCallback != null)
    {
      if (paramExecutor != null) {
        synchronized (mAudioServerStateCbLock)
        {
          if (mAudioServerStateCb == null)
          {
            IAudioService localIAudioService = getService();
            try
            {
              localIAudioService.registerAudioServerStateDispatcher(mAudioServerStateDispatcher);
              mAudioServerStateExec = paramExecutor;
              mAudioServerStateCb = paramAudioServerStateCallback;
              return;
            }
            catch (RemoteException paramExecutor)
            {
              throw paramExecutor.rethrowFromSystemServer();
            }
          }
          paramExecutor = new java/lang/IllegalStateException;
          paramExecutor.<init>("setAudioServerStateCallback called with already registered callabck");
          throw paramExecutor;
        }
      }
      throw new IllegalArgumentException("Illegal null Executor for the AudioServerStateCallback");
    }
    throw new IllegalArgumentException("Illegal null AudioServerStateCallback");
  }
  
  public void setAudioWizardForcePresetState(int paramInt, String paramString)
  {
    IAudioService localIAudioService = getService();
    try
    {
      localIAudioService.setAudioWizardForcePresetState(paramInt, paramString);
    }
    catch (RemoteException localRemoteException)
    {
      paramString = new StringBuilder();
      paramString.append("Dead object in setAudioWizardForcePresetState ");
      paramString.append(localRemoteException);
      Log.e("AudioManager", paramString.toString());
    }
  }
  
  public int setBluetoothA2dpDeviceConnectionState(BluetoothDevice paramBluetoothDevice, int paramInt1, int paramInt2)
  {
    IAudioService localIAudioService = getService();
    try
    {
      paramInt1 = localIAudioService.setBluetoothA2dpDeviceConnectionState(paramBluetoothDevice, paramInt1, paramInt2);
      return paramInt1;
    }
    catch (RemoteException paramBluetoothDevice)
    {
      throw paramBluetoothDevice.rethrowFromSystemServer();
    }
  }
  
  public int setBluetoothA2dpDeviceConnectionStateSuppressNoisyIntent(BluetoothDevice paramBluetoothDevice, int paramInt1, int paramInt2, boolean paramBoolean, int paramInt3)
  {
    IAudioService localIAudioService = getService();
    try
    {
      paramInt1 = localIAudioService.setBluetoothA2dpDeviceConnectionStateSuppressNoisyIntent(paramBluetoothDevice, paramInt1, paramInt2, paramBoolean, paramInt3);
      return paramInt1;
    }
    catch (RemoteException paramBluetoothDevice)
    {
      throw paramBluetoothDevice.rethrowFromSystemServer();
    }
  }
  
  @Deprecated
  public void setBluetoothA2dpOn(boolean paramBoolean) {}
  
  public void setBluetoothScoOn(boolean paramBoolean)
  {
    IAudioService localIAudioService = getService();
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("In setBluetoothScoOn(), on: ");
    localStringBuilder.append(paramBoolean);
    localStringBuilder.append(", calling application: ");
    localStringBuilder.append(mApplicationContext.getOpPackageName());
    Log.i("AudioManager", localStringBuilder.toString());
    try
    {
      localIAudioService.setBluetoothScoOn(paramBoolean);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  @SystemApi
  public void setFocusRequestResult(AudioFocusInfo paramAudioFocusInfo, int paramInt, AudioPolicy paramAudioPolicy)
  {
    if (paramAudioFocusInfo != null)
    {
      if (paramAudioPolicy != null)
      {
        IAudioService localIAudioService = getService();
        try
        {
          localIAudioService.setFocusRequestResultFromExtPolicy(paramAudioFocusInfo, paramInt, paramAudioPolicy.cb());
          return;
        }
        catch (RemoteException paramAudioFocusInfo)
        {
          throw paramAudioFocusInfo.rethrowFromSystemServer();
        }
      }
      throw new IllegalArgumentException("Illegal null AudioPolicy");
    }
    throw new IllegalArgumentException("Illegal null AudioFocusInfo");
  }
  
  public int setHdmiSystemAudioSupported(boolean paramBoolean)
  {
    try
    {
      int i = getService().setHdmiSystemAudioSupported(paramBoolean);
      return i;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void setHearingAidDeviceConnectionState(BluetoothDevice paramBluetoothDevice, int paramInt)
  {
    IAudioService localIAudioService = getService();
    try
    {
      localIAudioService.setHearingAidDeviceConnectionState(paramBluetoothDevice, paramInt);
      return;
    }
    catch (RemoteException paramBluetoothDevice)
    {
      throw paramBluetoothDevice.rethrowFromSystemServer();
    }
  }
  
  public void setMasterMute(boolean paramBoolean, int paramInt)
  {
    IAudioService localIAudioService = getService();
    try
    {
      localIAudioService.setMasterMute(paramBoolean, paramInt, getContext().getOpPackageName(), UserHandle.getCallingUserId());
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void setMicrophoneMute(boolean paramBoolean)
  {
    IAudioService localIAudioService = getService();
    try
    {
      localIAudioService.setMicrophoneMute(paramBoolean, getContext().getOpPackageName(), UserHandle.getCallingUserId());
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void setMode(int paramInt)
  {
    IAudioService localIAudioService = getService();
    try
    {
      localIAudioService.setMode(paramInt, mICallBack, mApplicationContext.getOpPackageName());
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  @Deprecated
  public void setParameter(String paramString1, String paramString2)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString1);
    localStringBuilder.append("=");
    localStringBuilder.append(paramString2);
    setParameters(localStringBuilder.toString());
  }
  
  public void setParameters(String paramString)
  {
    AudioSystem.setParameters(paramString);
  }
  
  public void setRingerMode(int paramInt)
  {
    if (!isValidRingerMode(paramInt)) {
      return;
    }
    IAudioService localIAudioService = getService();
    try
    {
      localIAudioService.setRingerModeExternal(paramInt, getContext().getOpPackageName());
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void setRingerModeInternal(int paramInt)
  {
    try
    {
      getService().setRingerModeInternal(paramInt, getContext().getOpPackageName());
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  @Deprecated
  public void setRouting(int paramInt1, int paramInt2, int paramInt3) {}
  
  public void setSpeakerphoneOn(boolean paramBoolean)
  {
    IAudioService localIAudioService = getService();
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("In setSpeakerphoneOn(), on: ");
    localStringBuilder.append(paramBoolean);
    localStringBuilder.append(", calling application: ");
    localStringBuilder.append(mApplicationContext.getOpPackageName());
    Log.d("AudioManager", localStringBuilder.toString());
    try
    {
      localIAudioService.setSpeakerphoneOn(paramBoolean);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  @Deprecated
  public void setStreamMute(int paramInt, boolean paramBoolean)
  {
    Log.w("AudioManager", "setStreamMute is deprecated. adjustStreamVolume should be used instead.");
    int i;
    if (paramBoolean) {
      i = -100;
    } else {
      i = 100;
    }
    if (paramInt == Integer.MIN_VALUE) {
      adjustSuggestedStreamVolume(i, paramInt, 0);
    } else {
      adjustStreamVolume(paramInt, i, 0);
    }
  }
  
  @Deprecated
  public void setStreamSolo(int paramInt, boolean paramBoolean)
  {
    Log.w("AudioManager", "setStreamSolo has been deprecated. Do not use.");
  }
  
  public void setStreamVolume(int paramInt1, int paramInt2, int paramInt3)
  {
    IAudioService localIAudioService = getService();
    try
    {
      localIAudioService.setStreamVolume(paramInt1, paramInt2, paramInt3, getContext().getOpPackageName());
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public boolean setSurroundFormatEnabled(int paramInt, boolean paramBoolean)
  {
    if (AudioSystem.setSurroundFormatEnabled(paramInt, paramBoolean) == 0) {
      paramBoolean = true;
    } else {
      paramBoolean = false;
    }
    return paramBoolean;
  }
  
  public void setVibrateSetting(int paramInt1, int paramInt2)
  {
    IAudioService localIAudioService = getService();
    try
    {
      localIAudioService.setVibrateSetting(paramInt1, paramInt2);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void setVolumeController(IVolumeController paramIVolumeController)
  {
    try
    {
      getService().setVolumeController(paramIVolumeController);
      return;
    }
    catch (RemoteException paramIVolumeController)
    {
      throw paramIVolumeController.rethrowFromSystemServer();
    }
  }
  
  public void setVolumePolicy(VolumePolicy paramVolumePolicy)
  {
    try
    {
      getService().setVolumePolicy(paramVolumePolicy);
      return;
    }
    catch (RemoteException paramVolumePolicy)
    {
      throw paramVolumePolicy.rethrowFromSystemServer();
    }
  }
  
  public void setWiredDeviceConnectionState(int paramInt1, int paramInt2, String paramString1, String paramString2)
  {
    IAudioService localIAudioService = getService();
    try
    {
      localIAudioService.setWiredDeviceConnectionState(paramInt1, paramInt2, paramString1, paramString2, mApplicationContext.getOpPackageName());
      return;
    }
    catch (RemoteException paramString1)
    {
      throw paramString1.rethrowFromSystemServer();
    }
  }
  
  @Deprecated
  public void setWiredHeadsetOn(boolean paramBoolean) {}
  
  public boolean shouldVibrate(int paramInt)
  {
    IAudioService localIAudioService = getService();
    try
    {
      boolean bool = localIAudioService.shouldVibrate(paramInt);
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void startBluetoothSco()
  {
    IAudioService localIAudioService = getService();
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("In startbluetoothSco(), calling application: ");
    localStringBuilder.append(mApplicationContext.getOpPackageName());
    Log.i("AudioManager", localStringBuilder.toString());
    try
    {
      localIAudioService.startBluetoothSco(mICallBack, getContextgetApplicationInfotargetSdkVersion);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void startBluetoothScoVirtualCall()
  {
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("In startBluetoothScoVirtualCall(), calling application: ");
    ((StringBuilder)localObject).append(mApplicationContext.getOpPackageName());
    Log.i("AudioManager", ((StringBuilder)localObject).toString());
    localObject = getService();
    try
    {
      ((IAudioService)localObject).startBluetoothScoVirtualCall(mICallBack);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void stopBluetoothSco()
  {
    IAudioService localIAudioService = getService();
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("In stopBluetoothSco(), calling application: ");
    localStringBuilder.append(mApplicationContext.getOpPackageName());
    Log.i("AudioManager", localStringBuilder.toString());
    try
    {
      localIAudioService.stopBluetoothSco(mICallBack);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void unloadSoundEffects()
  {
    IAudioService localIAudioService = getService();
    try
    {
      localIAudioService.unloadSoundEffects();
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void unregisterAudioDeviceCallback(AudioDeviceCallback paramAudioDeviceCallback)
  {
    synchronized (mDeviceCallbacks)
    {
      if (mDeviceCallbacks.containsKey(paramAudioDeviceCallback))
      {
        mDeviceCallbacks.remove(paramAudioDeviceCallback);
        if (mDeviceCallbacks.size() == 0) {
          unregisterAudioPortUpdateListener(mPortListener);
        }
      }
      return;
    }
  }
  
  public void unregisterAudioFocusRequest(OnAudioFocusChangeListener paramOnAudioFocusChangeListener)
  {
    mAudioFocusIdListenerMap.remove(getIdForAudioFocusListener(paramOnAudioFocusChangeListener));
  }
  
  public void unregisterAudioPlaybackCallback(AudioPlaybackCallback paramAudioPlaybackCallback)
  {
    if (paramAudioPlaybackCallback != null) {
      synchronized (mPlaybackCallbackLock)
      {
        if (mPlaybackCallbackList == null)
        {
          Log.w("AudioManager", "attempt to call unregisterAudioPlaybackCallback() on a callback that was never registered");
          return;
        }
        int i = mPlaybackCallbackList.size();
        if (removePlaybackCallback_sync(paramAudioPlaybackCallback))
        {
          int j = mPlaybackCallbackList.size();
          if ((i > 0) && (j == 0)) {
            try
            {
              getService().unregisterPlaybackCallback(mPlayCb);
            }
            catch (RemoteException paramAudioPlaybackCallback)
            {
              throw paramAudioPlaybackCallback.rethrowFromSystemServer();
            }
          }
        }
        else
        {
          Log.w("AudioManager", "attempt to call unregisterAudioPlaybackCallback() on a callback already unregistered or never registered");
        }
        return;
      }
    }
    throw new IllegalArgumentException("Illegal null AudioPlaybackCallback argument");
  }
  
  @SystemApi
  public void unregisterAudioPolicyAsync(AudioPolicy paramAudioPolicy)
  {
    if (paramAudioPolicy != null)
    {
      IAudioService localIAudioService = getService();
      try
      {
        localIAudioService.unregisterAudioPolicyAsync(paramAudioPolicy.cb());
        paramAudioPolicy.setRegistration(null);
        return;
      }
      catch (RemoteException paramAudioPolicy)
      {
        throw paramAudioPolicy.rethrowFromSystemServer();
      }
    }
    throw new IllegalArgumentException("Illegal null AudioPolicy argument");
  }
  
  public void unregisterAudioPortUpdateListener(OnAudioPortUpdateListener paramOnAudioPortUpdateListener)
  {
    sAudioPortEventHandler.unregisterListener(paramOnAudioPortUpdateListener);
  }
  
  public void unregisterAudioRecordingCallback(AudioRecordingCallback paramAudioRecordingCallback)
  {
    if (paramAudioRecordingCallback != null) {
      synchronized (mRecordCallbackLock)
      {
        if (mRecordCallbackList == null) {
          return;
        }
        int i = mRecordCallbackList.size();
        if (removeRecordCallback_sync(paramAudioRecordingCallback))
        {
          int j = mRecordCallbackList.size();
          if ((i > 0) && (j == 0))
          {
            paramAudioRecordingCallback = getService();
            try
            {
              paramAudioRecordingCallback.unregisterRecordingCallback(mRecCb);
            }
            catch (RemoteException paramAudioRecordingCallback)
            {
              throw paramAudioRecordingCallback.rethrowFromSystemServer();
            }
          }
        }
        else
        {
          Log.w("AudioManager", "attempt to call unregisterAudioRecordingCallback() on a callback already unregistered or never registered");
        }
        return;
      }
    }
    throw new IllegalArgumentException("Illegal null AudioRecordingCallback argument");
  }
  
  @Deprecated
  public void unregisterMediaButtonEventReceiver(PendingIntent paramPendingIntent)
  {
    if (paramPendingIntent == null) {
      return;
    }
    unregisterMediaButtonIntent(paramPendingIntent);
  }
  
  @Deprecated
  public void unregisterMediaButtonEventReceiver(ComponentName paramComponentName)
  {
    if (paramComponentName == null) {
      return;
    }
    Intent localIntent = new Intent("android.intent.action.MEDIA_BUTTON");
    localIntent.setComponent(paramComponentName);
    unregisterMediaButtonIntent(PendingIntent.getBroadcast(getContext(), 0, localIntent, 0));
  }
  
  public void unregisterMediaButtonIntent(PendingIntent paramPendingIntent)
  {
    MediaSessionLegacyHelper.getHelper(getContext()).removeMediaButtonListener(paramPendingIntent);
  }
  
  @Deprecated
  public void unregisterRemoteControlClient(RemoteControlClient paramRemoteControlClient)
  {
    if ((paramRemoteControlClient != null) && (paramRemoteControlClient.getRcMediaIntent() != null))
    {
      paramRemoteControlClient.unregisterWithSession(MediaSessionLegacyHelper.getHelper(getContext()));
      return;
    }
  }
  
  @Deprecated
  public void unregisterRemoteController(RemoteController paramRemoteController)
  {
    if (paramRemoteController == null) {
      return;
    }
    paramRemoteController.stopListeningToSessions();
  }
  
  public static abstract class AudioPlaybackCallback
  {
    public AudioPlaybackCallback() {}
    
    public void onPlaybackConfigChanged(List<AudioPlaybackConfiguration> paramList) {}
  }
  
  private static class AudioPlaybackCallbackInfo
  {
    final AudioManager.AudioPlaybackCallback mCb;
    final Handler mHandler;
    
    AudioPlaybackCallbackInfo(AudioManager.AudioPlaybackCallback paramAudioPlaybackCallback, Handler paramHandler)
    {
      mCb = paramAudioPlaybackCallback;
      mHandler = paramHandler;
    }
  }
  
  public static abstract class AudioRecordingCallback
  {
    public AudioRecordingCallback() {}
    
    public void onRecordingConfigChanged(List<AudioRecordingConfiguration> paramList) {}
  }
  
  private static class AudioRecordingCallbackInfo
  {
    final AudioManager.AudioRecordingCallback mCb;
    final Handler mHandler;
    
    AudioRecordingCallbackInfo(AudioManager.AudioRecordingCallback paramAudioRecordingCallback, Handler paramHandler)
    {
      mCb = paramAudioRecordingCallback;
      mHandler = paramHandler;
    }
  }
  
  @SystemApi
  public static abstract class AudioServerStateCallback
  {
    public AudioServerStateCallback() {}
    
    public void onAudioServerDown() {}
    
    public void onAudioServerUp() {}
  }
  
  private static final class BlockingFocusResultReceiver
  {
    private final String mFocusClientId;
    private int mFocusRequestResult = 0;
    private final AudioManager.SafeWaitObject mLock = new AudioManager.SafeWaitObject(null);
    @GuardedBy("mLock")
    private boolean mResultReceived = false;
    
    BlockingFocusResultReceiver(String paramString)
    {
      mFocusClientId = paramString;
    }
    
    void notifyResult(int paramInt)
    {
      synchronized (mLock)
      {
        mResultReceived = true;
        mFocusRequestResult = paramInt;
        mLock.safeNotify();
        return;
      }
    }
    
    boolean receivedResult()
    {
      return mResultReceived;
    }
    
    int requestResult()
    {
      return mFocusRequestResult;
    }
    
    public void waitForResult(long paramLong)
    {
      synchronized (mLock)
      {
        if (mResultReceived) {
          return;
        }
        try
        {
          mLock.safeWait(paramLong);
        }
        catch (InterruptedException localInterruptedException) {}
        return;
      }
    }
  }
  
  private static class FocusRequestInfo
  {
    final Handler mHandler;
    final AudioFocusRequest mRequest;
    
    FocusRequestInfo(AudioFocusRequest paramAudioFocusRequest, Handler paramHandler)
    {
      mRequest = paramAudioFocusRequest;
      mHandler = paramHandler;
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface FocusRequestResult {}
  
  private class NativeEventHandlerDelegate
  {
    private final Handler mHandler;
    
    NativeEventHandlerDelegate(final AudioDeviceCallback paramAudioDeviceCallback, Handler paramHandler)
    {
      if (paramHandler != null) {
        paramHandler = paramHandler.getLooper();
      } else {
        paramHandler = Looper.getMainLooper();
      }
      if (paramHandler != null) {
        mHandler = new Handler(paramHandler)
        {
          public void handleMessage(Message paramAnonymousMessage)
          {
            switch (what)
            {
            default: 
              StringBuilder localStringBuilder = new StringBuilder();
              localStringBuilder.append("Unknown native event type: ");
              localStringBuilder.append(what);
              Log.e("AudioManager", localStringBuilder.toString());
              break;
            case 2: 
              if (paramAudioDeviceCallback != null) {
                paramAudioDeviceCallback.onAudioDevicesRemoved((AudioDeviceInfo[])obj);
              }
              break;
            case 0: 
            case 1: 
              if (paramAudioDeviceCallback != null) {
                paramAudioDeviceCallback.onAudioDevicesAdded((AudioDeviceInfo[])obj);
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
  
  private class OnAmPortUpdateListener
    implements AudioManager.OnAudioPortUpdateListener
  {
    static final String TAG = "OnAmPortUpdateListener";
    
    private OnAmPortUpdateListener() {}
    
    public void onAudioPatchListUpdate(AudioPatch[] paramArrayOfAudioPatch) {}
    
    public void onAudioPortListUpdate(AudioPort[] arg1)
    {
      synchronized (mDeviceCallbacks)
      {
        AudioManager.this.broadcastDeviceListChange_sync(null);
        return;
      }
    }
    
    public void onServiceDied()
    {
      synchronized (mDeviceCallbacks)
      {
        AudioManager.this.broadcastDeviceListChange_sync(null);
        return;
      }
    }
  }
  
  public static abstract interface OnAudioFocusChangeListener
  {
    public abstract void onAudioFocusChange(int paramInt);
  }
  
  public static abstract interface OnAudioPortUpdateListener
  {
    public abstract void onAudioPatchListUpdate(AudioPatch[] paramArrayOfAudioPatch);
    
    public abstract void onAudioPortListUpdate(AudioPort[] paramArrayOfAudioPort);
    
    public abstract void onServiceDied();
  }
  
  private static final class PlaybackConfigChangeCallbackData
  {
    final AudioManager.AudioPlaybackCallback mCb;
    final List<AudioPlaybackConfiguration> mConfigs;
    
    PlaybackConfigChangeCallbackData(AudioManager.AudioPlaybackCallback paramAudioPlaybackCallback, List<AudioPlaybackConfiguration> paramList)
    {
      mCb = paramAudioPlaybackCallback;
      mConfigs = paramList;
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface PublicStreamTypes {}
  
  private static final class RecordConfigChangeCallbackData
  {
    final AudioManager.AudioRecordingCallback mCb;
    final List<AudioRecordingConfiguration> mConfigs;
    
    RecordConfigChangeCallbackData(AudioManager.AudioRecordingCallback paramAudioRecordingCallback, List<AudioRecordingConfiguration> paramList)
    {
      mCb = paramAudioRecordingCallback;
      mConfigs = paramList;
    }
  }
  
  private static final class SafeWaitObject
  {
    private boolean mQuit = false;
    
    private SafeWaitObject() {}
    
    public void safeNotify()
    {
      try
      {
        mQuit = true;
        notify();
        return;
      }
      finally {}
    }
    
    public void safeWait(long paramLong)
      throws InterruptedException
    {
      long l1 = System.currentTimeMillis();
      try
      {
        while (!mQuit)
        {
          long l2 = l1 + paramLong - System.currentTimeMillis();
          if (l2 < 0L) {
            break;
          }
          wait(l2);
        }
        return;
      }
      finally {}
    }
  }
  
  private class ServiceEventHandlerDelegate
  {
    private final Handler mHandler;
    
    ServiceEventHandlerDelegate(Handler paramHandler)
    {
      if (paramHandler == null)
      {
        Looper localLooper = Looper.myLooper();
        paramHandler = localLooper;
        if (localLooper == null) {
          paramHandler = Looper.getMainLooper();
        }
      }
      else
      {
        paramHandler = paramHandler.getLooper();
      }
      if (paramHandler != null) {
        mHandler = new Handler(paramHandler)
        {
          public void handleMessage(Message paramAnonymousMessage)
          {
            Object localObject;
            switch (what)
            {
            default: 
              localObject = new StringBuilder();
              ((StringBuilder)localObject).append("Unknown event ");
              ((StringBuilder)localObject).append(what);
              Log.e("AudioManager", ((StringBuilder)localObject).toString());
              break;
            case 2: 
              paramAnonymousMessage = (AudioManager.PlaybackConfigChangeCallbackData)obj;
              if (mCb != null) {
                mCb.onPlaybackConfigChanged(mConfigs);
              }
              break;
            case 1: 
              paramAnonymousMessage = (AudioManager.RecordConfigChangeCallbackData)obj;
              if (mCb != null) {
                mCb.onRecordingConfigChanged(mConfigs);
              }
              break;
            case 0: 
              localObject = AudioManager.this.findFocusRequestInfo((String)obj);
              if (localObject != null)
              {
                localObject = mRequest.getOnAudioFocusChangeListener();
                if (localObject != null)
                {
                  StringBuilder localStringBuilder = new StringBuilder();
                  localStringBuilder.append("dispatching onAudioFocusChange(");
                  localStringBuilder.append(arg1);
                  localStringBuilder.append(") to ");
                  localStringBuilder.append(obj);
                  Log.d("AudioManager", localStringBuilder.toString());
                  ((AudioManager.OnAudioFocusChangeListener)localObject).onAudioFocusChange(arg1);
                }
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
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface VolumeAdjustment {}
}
