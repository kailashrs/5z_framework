package android.preference;

import android.app.NotificationManager;
import android.app.NotificationManager.Policy;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.media.AudioAttributes.Builder;
import android.media.AudioManager;
import android.media.AudioSystem;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings.System;
import android.service.notification.ZenModeConfig;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import com.android.internal.annotations.GuardedBy;

public class SeekBarVolumizer
  implements SeekBar.OnSeekBarChangeListener, Handler.Callback
{
  private static final int CHECK_RINGTONE_PLAYBACK_DELAY_MS = 1000;
  private static final int MSG_INIT_SAMPLE = 3;
  private static final int MSG_SET_STREAM_VOLUME = 0;
  private static final int MSG_START_SAMPLE = 1;
  private static final int MSG_STOP_SAMPLE = 2;
  private static final String TAG = "SeekBarVolumizer";
  private static boolean mNotificationUseRingVolume;
  private boolean DEBUG = Log.isLoggable("volume", 3);
  private final String SAFETY_WARNING_DIALOG_CANCELED_ACTION = "com.asus.systemui.volumepanel.SAFETY_WARNING_DIALOG_CANCELED_ACTION";
  private boolean mAffectedByRingerMode;
  private boolean mAllowAlarms;
  private boolean mAllowMedia;
  private boolean mAllowRinger;
  private boolean mAsusNotificationOrRing;
  private final AudioManager mAudioManager;
  private final Callback mCallback;
  private final Context mContext;
  private final Uri mDefaultUri;
  private Handler mHandler;
  private int mLastAudibleStreamVolume;
  private int mLastProgress = -1;
  private final int mMaxStreamVolume;
  private boolean mMuted;
  private final NotificationManager mNotificationManager;
  private boolean mNotificationOrRing;
  private NotificationManager.Policy mNotificationPolicy;
  private int mOriginalStreamVolume;
  private final Receiver mReceiver = new Receiver(null);
  private boolean mRing;
  private int mRingerMode;
  @GuardedBy("this")
  private Ringtone mRingtone;
  private SeekBar mSeekBar;
  private final int mStreamType;
  private final H mUiHandler = new H(null);
  private int mVolumeBeforeMute = -1;
  private Observer mVolumeObserver;
  private int mZenMode;
  
  public SeekBarVolumizer(Context paramContext, int paramInt, Uri paramUri, Callback paramCallback)
  {
    mContext = paramContext;
    mAudioManager = ((AudioManager)paramContext.getSystemService(AudioManager.class));
    mNotificationManager = ((NotificationManager)paramContext.getSystemService(NotificationManager.class));
    mNotificationPolicy = mNotificationManager.getNotificationPolicy();
    int i = mNotificationPolicy.priorityCategories;
    boolean bool1 = false;
    if ((i & 0x20) != 0) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    mAllowAlarms = bool2;
    if ((mNotificationPolicy.priorityCategories & 0x40) != 0) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    mAllowMedia = bool2;
    mAllowRinger = (ZenModeConfig.areAllPriorityOnlyNotificationZenSoundsMuted(mNotificationPolicy) ^ true);
    mStreamType = paramInt;
    mAffectedByRingerMode = mAudioManager.isStreamAffectedByRingerMode(mStreamType);
    mNotificationOrRing = isNotificationOrRing(mStreamType);
    mNotificationUseRingVolume = isNotificationUseRingVolume(mContext);
    boolean bool2 = bool1;
    if (mStreamType == 2) {
      bool2 = true;
    }
    mRing = bool2;
    if (!mNotificationUseRingVolume) {
      bool2 = mRing;
    } else {
      bool2 = mNotificationOrRing;
    }
    mAsusNotificationOrRing = bool2;
    if (mAsusNotificationOrRing) {
      mRingerMode = mAudioManager.getRingerModeInternal();
    }
    mZenMode = mNotificationManager.getZenMode();
    mMaxStreamVolume = mAudioManager.getStreamMaxVolume(mStreamType);
    mCallback = paramCallback;
    mOriginalStreamVolume = mAudioManager.getStreamVolume(mStreamType);
    mLastAudibleStreamVolume = mAudioManager.getLastAudibleStreamVolume(mStreamType);
    initStreamVolume();
    paramContext = paramUri;
    if (paramUri == null) {
      if (mStreamType == 2) {
        paramContext = Settings.System.DEFAULT_RINGTONE_URI;
      } else if (mStreamType == 5) {
        paramContext = Settings.System.DEFAULT_NOTIFICATION_URI;
      } else {
        paramContext = Settings.System.DEFAULT_ALARM_ALERT_URI;
      }
    }
    mDefaultUri = paramContext;
  }
  
  private boolean directAdjustVolume()
  {
    boolean bool;
    if ((mStreamType == 3) && (mAudioManager.isMusicActive())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private static boolean isAlarmsStream(int paramInt)
  {
    boolean bool;
    if (paramInt == 4) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private boolean isDisableSeekBar()
  {
    boolean bool1 = mNotificationUseRingVolume;
    boolean bool2 = false;
    boolean bool3 = false;
    if (!bool1)
    {
      if ((mStreamType == 2) || (mZenMode != 2))
      {
        bool1 = bool3;
        if (mStreamType == 5)
        {
          bool1 = bool3;
          if (mZenMode != 3) {}
        }
      }
      else
      {
        bool1 = true;
      }
      return bool1;
    }
    bool1 = bool2;
    if (mStreamType != 2)
    {
      bool1 = bool2;
      if (mZenMode == 2) {
        bool1 = true;
      }
    }
    return bool1;
  }
  
  private static boolean isMediaStream(int paramInt)
  {
    boolean bool;
    if (paramInt == 3) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private boolean isMuted(int paramInt)
  {
    boolean bool1 = mAsusNotificationOrRing;
    boolean bool2 = true;
    if (bool1) {
      if (paramInt < 0) {
        return bool2;
      }
    }
    for (;;)
    {
      bool1 = false;
      break;
      if ((mAudioManager.isStreamMute(mStreamType)) && (isZenMuted()))
      {
        bool1 = bool2;
        break;
      }
      bool1 = bool2;
      if (mAudioManager.isStreamMute(mStreamType)) {
        break;
      }
      if (mLastProgress == 0)
      {
        bool1 = bool2;
        break;
      }
    }
    return bool1;
  }
  
  private static boolean isNotificationOrRing(int paramInt)
  {
    boolean bool;
    if ((paramInt != 2) && (paramInt != 5)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  private static boolean isNotificationUseRingVolume(Context paramContext)
  {
    int i = AudioSystem.getPlatformType(paramContext);
    boolean bool = false;
    if (i == 1) {
      i = 1;
    } else {
      i = 0;
    }
    paramContext = paramContext.getContentResolver();
    if (i != 0) {
      i = 1;
    } else {
      i = -1;
    }
    if (Settings.System.getInt(paramContext, "asus_notification_use_ring_volume", i) != 0) {
      bool = true;
    }
    return bool;
  }
  
  private boolean isZenMuted()
  {
    boolean bool1 = mNotificationOrRing;
    boolean bool2 = true;
    if (bool1)
    {
      bool1 = bool2;
      if (mZenMode == 3) {}
    }
    else
    {
      bool1 = bool2;
      if (mZenMode != 2)
      {
        if (mZenMode == 1)
        {
          if (!mAllowAlarms)
          {
            bool1 = bool2;
            if (isAlarmsStream(mStreamType)) {
              return bool1;
            }
          }
          if (!mAllowMedia)
          {
            bool1 = bool2;
            if (isMediaStream(mStreamType)) {
              return bool1;
            }
          }
          if ((!mAllowRinger) && (isNotificationOrRing(mStreamType))) {
            return bool2;
          }
        }
        bool1 = false;
      }
    }
    return bool1;
  }
  
  private void onInitSample()
  {
    try
    {
      mRingtone = RingtoneManager.getRingtone(mContext, mDefaultUri);
      if (mRingtone != null) {
        mRingtone.setStreamType(mStreamType);
      }
      return;
    }
    finally {}
  }
  
  private void onStartSample()
  {
    if (!isSamplePlaying())
    {
      if (mCallback != null) {
        mCallback.onSampleStarting(this);
      }
      try
      {
        Object localObject1 = mRingtone;
        if (localObject1 != null) {
          try
          {
            localObject3 = mRingtone;
            localObject1 = new android/media/AudioAttributes$Builder;
            ((AudioAttributes.Builder)localObject1).<init>(mRingtone.getAudioAttributes());
            ((Ringtone)localObject3).setAudioAttributes(((AudioAttributes.Builder)localObject1).setFlags(128).build());
            mRingtone.play();
          }
          catch (Throwable localThrowable)
          {
            Object localObject3 = new java/lang/StringBuilder;
            ((StringBuilder)localObject3).<init>();
            ((StringBuilder)localObject3).append("Error playing ringtone, stream ");
            ((StringBuilder)localObject3).append(mStreamType);
            Log.w("SeekBarVolumizer", ((StringBuilder)localObject3).toString(), localThrowable);
          }
        }
      }
      finally {}
    }
  }
  
  private void onStopSample()
  {
    try
    {
      if (mRingtone != null) {
        mRingtone.stop();
      }
      return;
    }
    finally {}
  }
  
  private void postSetVolume(int paramInt)
  {
    if (mHandler == null) {
      return;
    }
    mLastProgress = paramInt;
    mHandler.removeMessages(0);
    mHandler.sendMessage(mHandler.obtainMessage(0));
  }
  
  private void postStartSample()
  {
    if ((mHandler != null) && (!directAdjustVolume()))
    {
      mHandler.removeMessages(1);
      Handler localHandler = mHandler;
      Message localMessage = mHandler.obtainMessage(1);
      long l;
      if (isSamplePlaying()) {
        l = 1000L;
      } else {
        l = 0L;
      }
      localHandler.sendMessageDelayed(localMessage, l);
      return;
    }
  }
  
  private void postStopSample()
  {
    if (mHandler == null) {
      return;
    }
    mHandler.removeMessages(1);
    mHandler.removeMessages(2);
    mHandler.sendMessage(mHandler.obtainMessage(2));
  }
  
  private void updateSlider()
  {
    if ((mSeekBar != null) && (mAudioManager != null))
    {
      int i = mAudioManager.getStreamVolume(mStreamType);
      int j = mAudioManager.getLastAudibleStreamVolume(mStreamType);
      boolean bool = mAudioManager.isStreamMute(mStreamType);
      if (DEBUG)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("updateSlider volume:");
        localStringBuilder.append(i);
        localStringBuilder.append(" lastAudibleVolume:");
        localStringBuilder.append(j);
        localStringBuilder.append(" Current mute state:");
        localStringBuilder.append(bool);
        localStringBuilder.append(" Previous mute state:");
        localStringBuilder.append(mMuted);
        Log.d("SeekBarVolumizer", localStringBuilder.toString());
      }
      mUiHandler.postUpdateSlider(i, j, bool);
    }
  }
  
  public void changeVolumeBy(int paramInt)
  {
    mSeekBar.incrementProgressBy(paramInt);
    postSetVolume(mSeekBar.getProgress());
    postStartSample();
    mVolumeBeforeMute = -1;
  }
  
  public SeekBar getSeekBar()
  {
    return mSeekBar;
  }
  
  public boolean handleMessage(Message paramMessage)
  {
    StringBuilder localStringBuilder;
    if (DEBUG)
    {
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("handleMessage:");
      localStringBuilder.append(what);
      localStringBuilder.append(" Previous mute state:");
      localStringBuilder.append(mMuted);
      localStringBuilder.append(" LastProgress:");
      localStringBuilder.append(mLastProgress);
      localStringBuilder.append(" OriginalStreamVolume:");
      localStringBuilder.append(mOriginalStreamVolume);
      localStringBuilder.append(" LastAudibleStreamVolume:");
      localStringBuilder.append(mLastAudibleStreamVolume);
      Log.d("SeekBarVolumizer", localStringBuilder.toString());
    }
    switch (what)
    {
    default: 
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("invalid SeekBarVolumizer message: ");
      localStringBuilder.append(what);
      Log.e("SeekBarVolumizer", localStringBuilder.toString());
      break;
    case 3: 
      onInitSample();
      break;
    case 2: 
      onStopSample();
      break;
    case 1: 
      onStartSample();
      break;
    case 0: 
      if ((mMuted) && (mLastProgress > 0)) {
        mAudioManager.adjustStreamVolume(mStreamType, 100, 0);
      } else if ((!mMuted) && (mLastProgress == 0)) {
        mAudioManager.adjustStreamVolume(mStreamType, -100, 0);
      }
      mAudioManager.setStreamVolume(mStreamType, mLastProgress, 1024);
    }
    return true;
  }
  
  public void initStreamVolume()
  {
    mOriginalStreamVolume = mAudioManager.getStreamVolume(mStreamType);
    mZenMode = mNotificationManager.getZenMode();
    mMuted = mAudioManager.isStreamMute(mStreamType);
    if ((!mAsusNotificationOrRing) && (mOriginalStreamVolume == 0)) {
      mMuted = true;
    }
    if (mCallback != null) {
      mCallback.onMuted(mMuted, isZenMuted());
    }
  }
  
  public boolean isSamplePlaying()
  {
    try
    {
      boolean bool;
      if ((mRingtone != null) && (mRingtone.isPlaying())) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    finally {}
  }
  
  public void muteVolume()
  {
    if (mVolumeBeforeMute != -1)
    {
      mSeekBar.setProgress(mVolumeBeforeMute, true);
      postSetVolume(mVolumeBeforeMute);
      postStartSample();
      mVolumeBeforeMute = -1;
    }
    else
    {
      mVolumeBeforeMute = mSeekBar.getProgress();
      mSeekBar.setProgress(0, true);
      postStopSample();
      postSetVolume(0);
    }
  }
  
  public void onProgressChanged(SeekBar paramSeekBar, int paramInt, boolean paramBoolean)
  {
    if (DEBUG)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("onProgressChanged Previous mute state:");
      localStringBuilder.append(mMuted);
      localStringBuilder.append(" progress:");
      localStringBuilder.append(paramInt);
      Log.d("SeekBarVolumizer", localStringBuilder.toString());
    }
    if (paramBoolean) {
      postSetVolume(paramInt);
    }
    if (mCallback != null) {
      mCallback.onProgressChanged(paramSeekBar, paramInt, paramBoolean);
    }
  }
  
  public void onRestoreInstanceState(VolumePreference.VolumeStore paramVolumeStore)
  {
    if (volume != -1)
    {
      mOriginalStreamVolume = originalVolume;
      mLastProgress = volume;
      postSetVolume(mLastProgress);
    }
  }
  
  public void onSaveInstanceState(VolumePreference.VolumeStore paramVolumeStore)
  {
    if (mLastProgress >= 0)
    {
      volume = mLastProgress;
      originalVolume = mOriginalStreamVolume;
    }
  }
  
  public void onStartTrackingTouch(SeekBar paramSeekBar) {}
  
  public void onStopTrackingTouch(SeekBar paramSeekBar)
  {
    postStartSample();
  }
  
  public void revertVolume()
  {
    mAudioManager.setStreamVolume(mStreamType, mOriginalStreamVolume, 0);
  }
  
  public void setSeekBar(SeekBar paramSeekBar)
  {
    if (mSeekBar != null)
    {
      mSeekBar.setOnSeekBarChangeListener(null);
      mLastProgress = -1;
      initStreamVolume();
    }
    mSeekBar = paramSeekBar;
    mSeekBar.setOnSeekBarChangeListener(null);
    mSeekBar.setMax(mMaxStreamVolume);
    updateSeekBar();
    mSeekBar.setOnSeekBarChangeListener(this);
  }
  
  public void start()
  {
    if (mHandler != null) {
      return;
    }
    HandlerThread localHandlerThread = new HandlerThread("SeekBarVolumizer.CallbackHandler");
    localHandlerThread.start();
    mHandler = new Handler(localHandlerThread.getLooper(), this);
    mHandler.sendEmptyMessage(3);
    mVolumeObserver = new Observer(mHandler);
    mContext.getContentResolver().registerContentObserver(Settings.System.getUriFor(Settings.System.VOLUME_SETTINGS[mStreamType]), false, mVolumeObserver);
    mReceiver.setListening(true);
  }
  
  public void startSample()
  {
    postStartSample();
  }
  
  public void stop()
  {
    if (mHandler == null) {
      return;
    }
    postStopSample();
    mContext.getContentResolver().unregisterContentObserver(mVolumeObserver);
    mReceiver.setListening(false);
    mSeekBar.setOnSeekBarChangeListener(null);
    mHandler.getLooper().quitSafely();
    mHandler = null;
    mVolumeObserver = null;
  }
  
  public void stopSample()
  {
    postStopSample();
  }
  
  protected void updateSeekBar()
  {
    Object localObject;
    if (DEBUG)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("updateSeekBar mLastProgress:");
      ((StringBuilder)localObject).append(mLastProgress);
      ((StringBuilder)localObject).append(" mOriginalStreamVolume:");
      ((StringBuilder)localObject).append(mOriginalStreamVolume);
      Log.d("SeekBarVolumizer", ((StringBuilder)localObject).toString());
    }
    boolean bool = isZenMuted();
    mSeekBar.setEnabled(isDisableSeekBar() ^ true);
    if ((!mMuted) && (!bool))
    {
      if ((mAsusNotificationOrRing) && (mRingerMode == 1))
      {
        mSeekBar.setProgress(0, true);
      }
      else
      {
        localObject = mSeekBar;
        int i;
        if (mLastProgress > -1) {
          i = mLastProgress;
        } else {
          i = mOriginalStreamVolume;
        }
        ((SeekBar)localObject).setProgress(i, true);
      }
    }
    else {
      mSeekBar.setProgress(0, true);
    }
  }
  
  public static abstract interface Callback
  {
    public abstract void onMuted(boolean paramBoolean1, boolean paramBoolean2);
    
    public abstract void onProgressChanged(SeekBar paramSeekBar, int paramInt, boolean paramBoolean);
    
    public abstract void onSampleStarting(SeekBarVolumizer paramSeekBarVolumizer);
  }
  
  private final class H
    extends Handler
  {
    private static final int UPDATE_SLIDER = 1;
    
    private H() {}
    
    public void handleMessage(Message paramMessage)
    {
      if ((what == 1) && (mSeekBar != null))
      {
        SeekBarVolumizer.access$302(SeekBarVolumizer.this, arg1);
        SeekBarVolumizer.access$402(SeekBarVolumizer.this, arg2);
        boolean bool = ((Boolean)obj).booleanValue();
        if (bool != mMuted)
        {
          SeekBarVolumizer.access$502(SeekBarVolumizer.this, bool);
          if (mCallback != null) {
            mCallback.onMuted(mMuted, SeekBarVolumizer.this.isZenMuted());
          }
        }
        updateSeekBar();
      }
    }
    
    public void postUpdateSlider(int paramInt1, int paramInt2, boolean paramBoolean)
    {
      obtainMessage(1, paramInt1, paramInt2, new Boolean(paramBoolean)).sendToTarget();
    }
  }
  
  private final class Observer
    extends ContentObserver
  {
    public Observer(Handler paramHandler)
    {
      super();
    }
    
    public void onChange(boolean paramBoolean)
    {
      super.onChange(paramBoolean);
      SeekBarVolumizer.this.updateSlider();
    }
  }
  
  private final class Receiver
    extends BroadcastReceiver
  {
    private boolean mListening;
    
    private Receiver() {}
    
    private void updateVolumeSlider(int paramInt1, int paramInt2)
    {
      boolean bool1 = mAsusNotificationOrRing;
      boolean bool2 = false;
      if (bool1)
      {
        if (!SeekBarVolumizer.mNotificationUseRingVolume)
        {
          if (paramInt1 != 2) {
            break label56;
          }
        }
        else
        {
          bool1 = SeekBarVolumizer.isNotificationOrRing(paramInt1);
          break label58;
        }
      }
      else {
        if (paramInt1 != mStreamType) {
          break label56;
        }
      }
      bool1 = true;
      break label58;
      label56:
      bool1 = false;
      label58:
      if ((mSeekBar != null) && (bool1) && (paramInt2 != -1))
      {
        if ((!mAudioManager.isStreamMute(mStreamType)) && (paramInt2 != 0)) {
          bool1 = bool2;
        } else {
          bool1 = true;
        }
        mUiHandler.postUpdateSlider(paramInt2, mLastAudibleStreamVolume, bool1);
      }
    }
    
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      paramContext = paramIntent.getAction();
      int i;
      int j;
      if ("android.media.VOLUME_CHANGED_ACTION".equals(paramContext))
      {
        i = paramIntent.getIntExtra("android.media.EXTRA_VOLUME_STREAM_TYPE", -1);
        j = paramIntent.getIntExtra("android.media.EXTRA_VOLUME_STREAM_VALUE", -1);
        if (DEBUG)
        {
          paramContext = new StringBuilder();
          paramContext.append("VOLUME_CHANGED_ACTION: streamType:");
          paramContext.append(i);
          paramContext.append(" streamValue:");
          paramContext.append(j);
          Log.d("SeekBarVolumizer", paramContext.toString());
        }
        updateVolumeSlider(i, j);
      }
      else if ("android.media.INTERNAL_RINGER_MODE_CHANGED_ACTION".equals(paramContext))
      {
        if (DEBUG)
        {
          paramContext = new StringBuilder();
          paramContext.append("INTERNAL_RINGER_MODE_CHANGED_ACTION: Current Mute Status:");
          paramContext.append(mMuted);
          paramContext.append(" mNotificationOrRing:");
          paramContext.append(mAsusNotificationOrRing);
          paramContext.append(" mRingerMode:");
          paramContext.append(mRingerMode);
          paramContext.append(" mAffectedByRingerMode:");
          paramContext.append(mAffectedByRingerMode);
          Log.d("SeekBarVolumizer", paramContext.toString());
        }
        if (mNotificationOrRing) {
          SeekBarVolumizer.access$1202(SeekBarVolumizer.this, mAudioManager.getRingerModeInternal());
        }
        if (mAffectedByRingerMode) {
          SeekBarVolumizer.this.updateSlider();
        }
      }
      else if ("android.media.STREAM_DEVICES_CHANGED_ACTION".equals(paramContext))
      {
        i = paramIntent.getIntExtra("android.media.EXTRA_VOLUME_STREAM_TYPE", -1);
        j = mAudioManager.getStreamVolume(i);
        if (DEBUG)
        {
          paramContext = new StringBuilder();
          paramContext.append("STREAM_DEVICES_CHANGED_ACTION: streamType:");
          paramContext.append(i);
          paramContext.append(" streamVolume:");
          paramContext.append(j);
          Log.d("SeekBarVolumizer", paramContext.toString());
        }
        updateVolumeSlider(i, j);
      }
      else if ("android.app.action.INTERRUPTION_FILTER_CHANGED".equals(paramContext))
      {
        SeekBarVolumizer.access$1602(SeekBarVolumizer.this, mNotificationManager.getZenMode());
        if (DEBUG)
        {
          paramContext = new StringBuilder();
          paramContext.append("ACTION_INTERRUPTION_FILTER_CHANGED:Current Mute Status:");
          paramContext.append(mMuted);
          paramContext.append(" mZenMode:");
          paramContext.append(mZenMode);
          Log.d("SeekBarVolumizer", paramContext.toString());
        }
        SeekBarVolumizer.this.updateSlider();
      }
      else
      {
        boolean bool1 = "android.app.action.NOTIFICATION_POLICY_CHANGED".equals(paramContext);
        boolean bool2 = false;
        if (bool1)
        {
          SeekBarVolumizer.access$1802(SeekBarVolumizer.this, mNotificationManager.getNotificationPolicy());
          paramContext = SeekBarVolumizer.this;
          if ((mNotificationPolicy.priorityCategories & 0x20) != 0) {
            bool1 = true;
          } else {
            bool1 = false;
          }
          SeekBarVolumizer.access$1902(paramContext, bool1);
          paramContext = SeekBarVolumizer.this;
          bool1 = bool2;
          if ((mNotificationPolicy.priorityCategories & 0x40) != 0) {
            bool1 = true;
          }
          SeekBarVolumizer.access$2002(paramContext, bool1);
          SeekBarVolumizer.access$2102(SeekBarVolumizer.this, ZenModeConfig.areAllPriorityOnlyNotificationZenSoundsMuted(mNotificationPolicy) ^ true);
          SeekBarVolumizer.this.updateSlider();
        }
        else if ("android.media.STREAM_MUTE_CHANGED_ACTION".equals(paramContext))
        {
          i = paramIntent.getIntExtra("android.media.EXTRA_VOLUME_STREAM_TYPE", -1);
          bool1 = paramIntent.getBooleanExtra("android.media.EXTRA_STREAM_VOLUME_MUTED", false);
          j = mAudioManager.getStreamVolume(i);
          if (DEBUG)
          {
            paramContext = new StringBuilder();
            paramContext.append("STREAM_MUTE_CHANGED_ACTION:stream:");
            paramContext.append(i);
            paramContext.append(" muted:");
            paramContext.append(bool1);
            paramContext.append(" streamValue");
            paramContext.append(j);
            paramContext.append(" mLastAudibleStreamVolume");
            paramContext.append(mLastAudibleStreamVolume);
            Log.d("SeekBarVolumizer", paramContext.toString());
          }
          if ((i == mStreamType) && (!bool1) && (j == mLastAudibleStreamVolume)) {
            mUiHandler.postUpdateSlider(j, mLastAudibleStreamVolume, bool1);
          }
        }
        else if ("com.asus.systemui.volumepanel.SAFETY_WARNING_DIALOG_CANCELED_ACTION".equals(paramContext))
        {
          if (DEBUG) {
            Log.d("SeekBarVolumizer", "onReceive SAFETY_WARNING_DIALOG_CANCELED_ACTION");
          }
          if (mStreamType == 3) {
            SeekBarVolumizer.this.updateSlider();
          }
        }
      }
    }
    
    public void setListening(boolean paramBoolean)
    {
      if (mListening == paramBoolean) {
        return;
      }
      mListening = paramBoolean;
      if (paramBoolean)
      {
        IntentFilter localIntentFilter = new IntentFilter("android.media.VOLUME_CHANGED_ACTION");
        localIntentFilter.addAction("android.media.INTERNAL_RINGER_MODE_CHANGED_ACTION");
        localIntentFilter.addAction("android.app.action.INTERRUPTION_FILTER_CHANGED");
        localIntentFilter.addAction("android.app.action.NOTIFICATION_POLICY_CHANGED");
        localIntentFilter.addAction("android.media.STREAM_DEVICES_CHANGED_ACTION");
        localIntentFilter.addAction("android.media.STREAM_MUTE_CHANGED_ACTION");
        localIntentFilter.addAction("com.asus.systemui.volumepanel.SAFETY_WARNING_DIALOG_CANCELED_ACTION");
        mContext.registerReceiver(this, localIntentFilter);
      }
      else
      {
        mContext.unregisterReceiver(this);
      }
    }
  }
}
