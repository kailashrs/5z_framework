package android.service.voice;

import android.content.Intent;
import android.hardware.soundtrigger.IRecognitionStatusCallback.Stub;
import android.hardware.soundtrigger.KeyphraseEnrollmentInfo;
import android.hardware.soundtrigger.KeyphraseMetadata;
import android.hardware.soundtrigger.SoundTrigger.ConfidenceLevel;
import android.hardware.soundtrigger.SoundTrigger.GenericRecognitionEvent;
import android.hardware.soundtrigger.SoundTrigger.KeyphraseRecognitionEvent;
import android.hardware.soundtrigger.SoundTrigger.KeyphraseRecognitionExtra;
import android.hardware.soundtrigger.SoundTrigger.ModuleProperties;
import android.hardware.soundtrigger.SoundTrigger.RecognitionConfig;
import android.media.AudioFormat;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.util.Slog;
import com.android.internal.app.IVoiceInteractionManagerService;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Locale;

public class AlwaysOnHotwordDetector
{
  static final boolean DBG = false;
  public static final int MANAGE_ACTION_ENROLL = 0;
  public static final int MANAGE_ACTION_RE_ENROLL = 1;
  public static final int MANAGE_ACTION_UN_ENROLL = 2;
  private static final int MSG_AVAILABILITY_CHANGED = 1;
  private static final int MSG_DETECTION_ERROR = 3;
  private static final int MSG_DETECTION_PAUSE = 4;
  private static final int MSG_DETECTION_RESUME = 5;
  private static final int MSG_HOTWORD_DETECTED = 2;
  public static final int RECOGNITION_FLAG_ALLOW_MULTIPLE_TRIGGERS = 2;
  public static final int RECOGNITION_FLAG_CAPTURE_TRIGGER_AUDIO = 1;
  public static final int RECOGNITION_FLAG_NONE = 0;
  public static final int RECOGNITION_MODE_USER_IDENTIFICATION = 2;
  public static final int RECOGNITION_MODE_VOICE_TRIGGER = 1;
  public static final int STATE_HARDWARE_UNAVAILABLE = -2;
  private static final int STATE_INVALID = -3;
  public static final int STATE_KEYPHRASE_ENROLLED = 2;
  public static final int STATE_KEYPHRASE_UNENROLLED = 1;
  public static final int STATE_KEYPHRASE_UNSUPPORTED = -1;
  private static final int STATE_NOT_READY = 0;
  private static final int STATUS_ERROR = Integer.MIN_VALUE;
  private static final int STATUS_OK = 0;
  static final String TAG = "AlwaysOnHotwordDetector";
  private int mAvailability = 0;
  private final Callback mExternalCallback;
  private final Handler mHandler;
  private final SoundTriggerListener mInternalCallback;
  private final KeyphraseEnrollmentInfo mKeyphraseEnrollmentInfo;
  private final KeyphraseMetadata mKeyphraseMetadata;
  private final Locale mLocale;
  private final Object mLock = new Object();
  private final IVoiceInteractionManagerService mModelManagementService;
  private final String mText;
  private final IVoiceInteractionService mVoiceInteractionService;
  
  public AlwaysOnHotwordDetector(String paramString, Locale paramLocale, Callback paramCallback, KeyphraseEnrollmentInfo paramKeyphraseEnrollmentInfo, IVoiceInteractionService paramIVoiceInteractionService, IVoiceInteractionManagerService paramIVoiceInteractionManagerService)
  {
    mText = paramString;
    mLocale = paramLocale;
    mKeyphraseEnrollmentInfo = paramKeyphraseEnrollmentInfo;
    mKeyphraseMetadata = mKeyphraseEnrollmentInfo.getKeyphraseMetadata(paramString, paramLocale);
    mExternalCallback = paramCallback;
    mHandler = new MyHandler();
    mInternalCallback = new SoundTriggerListener(mHandler);
    mVoiceInteractionService = paramIVoiceInteractionService;
    mModelManagementService = paramIVoiceInteractionManagerService;
    new RefreshAvailabiltyTask().execute(new Void[0]);
  }
  
  private Intent getManageIntentLocked(int paramInt)
  {
    if (mAvailability != -3)
    {
      if ((mAvailability != 2) && (mAvailability != 1)) {
        throw new UnsupportedOperationException("Managing the given keyphrase is not supported");
      }
      return mKeyphraseEnrollmentInfo.getManageKeyphraseIntent(paramInt, mText, mLocale);
    }
    throw new IllegalStateException("getManageIntent called on an invalid detector");
  }
  
  private int getSupportedRecognitionModesLocked()
  {
    if (mAvailability != -3)
    {
      if ((mAvailability != 2) && (mAvailability != 1)) {
        throw new UnsupportedOperationException("Getting supported recognition modes for the keyphrase is not supported");
      }
      return mKeyphraseMetadata.recognitionModeFlags;
    }
    throw new IllegalStateException("getSupportedRecognitionModes called on an invalid detector");
  }
  
  private void notifyStateChangedLocked()
  {
    Message localMessage = Message.obtain(mHandler, 1);
    arg1 = mAvailability;
    localMessage.sendToTarget();
  }
  
  private int startRecognitionLocked(int paramInt)
  {
    boolean bool1 = true;
    SoundTrigger.KeyphraseRecognitionExtra localKeyphraseRecognitionExtra = new SoundTrigger.KeyphraseRecognitionExtra(mKeyphraseMetadata.id, mKeyphraseMetadata.recognitionModeFlags, 0, new SoundTrigger.ConfidenceLevel[0]);
    boolean bool2;
    if ((paramInt & 0x1) != 0) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    if ((paramInt & 0x2) == 0) {
      bool1 = false;
    }
    paramInt = Integer.MIN_VALUE;
    try
    {
      IVoiceInteractionManagerService localIVoiceInteractionManagerService = mModelManagementService;
      IVoiceInteractionService localIVoiceInteractionService = mVoiceInteractionService;
      int i = mKeyphraseMetadata.id;
      String str = mLocale.toLanguageTag();
      SoundTriggerListener localSoundTriggerListener = mInternalCallback;
      SoundTrigger.RecognitionConfig localRecognitionConfig = new android/hardware/soundtrigger/SoundTrigger$RecognitionConfig;
      localRecognitionConfig.<init>(bool2, bool1, new SoundTrigger.KeyphraseRecognitionExtra[] { localKeyphraseRecognitionExtra }, null);
      i = localIVoiceInteractionManagerService.startRecognition(localIVoiceInteractionService, i, str, localSoundTriggerListener, localRecognitionConfig);
      paramInt = i;
    }
    catch (RemoteException localRemoteException)
    {
      Slog.w("AlwaysOnHotwordDetector", "RemoteException in startRecognition!", localRemoteException);
    }
    if (paramInt != 0)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("startRecognition() failed with error code ");
      localStringBuilder.append(paramInt);
      Slog.w("AlwaysOnHotwordDetector", localStringBuilder.toString());
    }
    return paramInt;
  }
  
  private int stopRecognitionLocked()
  {
    int i = Integer.MIN_VALUE;
    try
    {
      int j = mModelManagementService.stopRecognition(mVoiceInteractionService, mKeyphraseMetadata.id, mInternalCallback);
      i = j;
    }
    catch (RemoteException localRemoteException)
    {
      Slog.w("AlwaysOnHotwordDetector", "RemoteException in stopRecognition!", localRemoteException);
    }
    if (i != 0)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("stopRecognition() failed with error code ");
      localStringBuilder.append(i);
      Slog.w("AlwaysOnHotwordDetector", localStringBuilder.toString());
    }
    return i;
  }
  
  public Intent createEnrollIntent()
  {
    synchronized (mLock)
    {
      Intent localIntent = getManageIntentLocked(0);
      return localIntent;
    }
  }
  
  public Intent createReEnrollIntent()
  {
    synchronized (mLock)
    {
      Intent localIntent = getManageIntentLocked(1);
      return localIntent;
    }
  }
  
  public Intent createUnEnrollIntent()
  {
    synchronized (mLock)
    {
      Intent localIntent = getManageIntentLocked(2);
      return localIntent;
    }
  }
  
  public void dump(String paramString, PrintWriter paramPrintWriter)
  {
    synchronized (mLock)
    {
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("Text=");
      paramPrintWriter.println(mText);
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("Locale=");
      paramPrintWriter.println(mLocale);
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("Availability=");
      paramPrintWriter.println(mAvailability);
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("KeyphraseMetadata=");
      paramPrintWriter.println(mKeyphraseMetadata);
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("EnrollmentInfo=");
      paramPrintWriter.println(mKeyphraseEnrollmentInfo);
      return;
    }
  }
  
  public int getSupportedRecognitionModes()
  {
    synchronized (mLock)
    {
      int i = getSupportedRecognitionModesLocked();
      return i;
    }
  }
  
  void invalidate()
  {
    synchronized (mLock)
    {
      mAvailability = -3;
      notifyStateChangedLocked();
      return;
    }
  }
  
  void onSoundModelsChanged()
  {
    synchronized (mLock)
    {
      if ((mAvailability != -3) && (mAvailability != -2) && (mAvailability != -1))
      {
        stopRecognitionLocked();
        RefreshAvailabiltyTask localRefreshAvailabiltyTask = new android/service/voice/AlwaysOnHotwordDetector$RefreshAvailabiltyTask;
        localRefreshAvailabiltyTask.<init>(this);
        localRefreshAvailabiltyTask.execute(new Void[0]);
        return;
      }
      Slog.w("AlwaysOnHotwordDetector", "Received onSoundModelsChanged for an unsupported keyphrase/config");
      return;
    }
  }
  
  public boolean startRecognition(int paramInt)
  {
    synchronized (mLock)
    {
      if (mAvailability != -3)
      {
        if (mAvailability == 2)
        {
          boolean bool;
          if (startRecognitionLocked(paramInt) == 0) {
            bool = true;
          } else {
            bool = false;
          }
          return bool;
        }
        localObject2 = new java/lang/UnsupportedOperationException;
        ((UnsupportedOperationException)localObject2).<init>("Recognition for the given keyphrase is not supported");
        throw ((Throwable)localObject2);
      }
      Object localObject2 = new java/lang/IllegalStateException;
      ((IllegalStateException)localObject2).<init>("startRecognition called on an invalid detector");
      throw ((Throwable)localObject2);
    }
  }
  
  public boolean stopRecognition()
  {
    synchronized (mLock)
    {
      if (mAvailability != -3)
      {
        if (mAvailability == 2)
        {
          boolean bool;
          if (stopRecognitionLocked() == 0) {
            bool = true;
          } else {
            bool = false;
          }
          return bool;
        }
        localObject2 = new java/lang/UnsupportedOperationException;
        ((UnsupportedOperationException)localObject2).<init>("Recognition for the given keyphrase is not supported");
        throw ((Throwable)localObject2);
      }
      Object localObject2 = new java/lang/IllegalStateException;
      ((IllegalStateException)localObject2).<init>("stopRecognition called on an invalid detector");
      throw ((Throwable)localObject2);
    }
  }
  
  public static abstract class Callback
  {
    public Callback() {}
    
    public abstract void onAvailabilityChanged(int paramInt);
    
    public abstract void onDetected(AlwaysOnHotwordDetector.EventPayload paramEventPayload);
    
    public abstract void onError();
    
    public abstract void onRecognitionPaused();
    
    public abstract void onRecognitionResumed();
  }
  
  public static class EventPayload
  {
    private final AudioFormat mAudioFormat;
    private final boolean mCaptureAvailable;
    private final int mCaptureSession;
    private final byte[] mData;
    private final boolean mTriggerAvailable;
    
    private EventPayload(boolean paramBoolean1, boolean paramBoolean2, AudioFormat paramAudioFormat, int paramInt, byte[] paramArrayOfByte)
    {
      mTriggerAvailable = paramBoolean1;
      mCaptureAvailable = paramBoolean2;
      mCaptureSession = paramInt;
      mAudioFormat = paramAudioFormat;
      mData = paramArrayOfByte;
    }
    
    public AudioFormat getCaptureAudioFormat()
    {
      return mAudioFormat;
    }
    
    public Integer getCaptureSession()
    {
      if (mCaptureAvailable) {
        return Integer.valueOf(mCaptureSession);
      }
      return null;
    }
    
    public byte[] getTriggerAudio()
    {
      if (mTriggerAvailable) {
        return mData;
      }
      return null;
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  private static @interface ManageActions {}
  
  class MyHandler
    extends Handler
  {
    MyHandler() {}
    
    public void handleMessage(Message paramMessage)
    {
      synchronized (mLock)
      {
        if (mAvailability == -3)
        {
          StringBuilder localStringBuilder = new java/lang/StringBuilder;
          localStringBuilder.<init>();
          localStringBuilder.append("Received message: ");
          localStringBuilder.append(what);
          localStringBuilder.append(" for an invalid detector");
          Slog.w("AlwaysOnHotwordDetector", localStringBuilder.toString());
          return;
        }
        switch (what)
        {
        default: 
          super.handleMessage(paramMessage);
          break;
        case 5: 
          mExternalCallback.onRecognitionResumed();
          break;
        case 4: 
          mExternalCallback.onRecognitionPaused();
          break;
        case 3: 
          mExternalCallback.onError();
          break;
        case 2: 
          mExternalCallback.onDetected((AlwaysOnHotwordDetector.EventPayload)obj);
          break;
        case 1: 
          mExternalCallback.onAvailabilityChanged(arg1);
        }
        return;
      }
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface RecognitionFlags {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface RecognitionModes {}
  
  class RefreshAvailabiltyTask
    extends AsyncTask<Void, Void, Void>
  {
    RefreshAvailabiltyTask() {}
    
    private int internalGetInitialAvailability()
    {
      synchronized (mLock)
      {
        if (mAvailability == -3) {
          return -3;
        }
        ??? = null;
        try
        {
          SoundTrigger.ModuleProperties localModuleProperties = mModelManagementService.getDspModuleProperties(mVoiceInteractionService);
          ??? = localModuleProperties;
        }
        catch (RemoteException localRemoteException)
        {
          Slog.w("AlwaysOnHotwordDetector", "RemoteException in getDspProperties!", localRemoteException);
        }
        if (??? == null) {
          return -2;
        }
        if (mKeyphraseMetadata == null) {
          return -1;
        }
        return 0;
      }
    }
    
    private boolean internalGetIsEnrolled(int paramInt, Locale paramLocale)
    {
      try
      {
        boolean bool = mModelManagementService.isEnrolledForKeyphrase(mVoiceInteractionService, paramInt, paramLocale.toLanguageTag());
        return bool;
      }
      catch (RemoteException paramLocale)
      {
        Slog.w("AlwaysOnHotwordDetector", "RemoteException in listRegisteredKeyphraseSoundModels!", paramLocale);
      }
      return false;
    }
    
    public Void doInBackground(Void... arg1)
    {
      int i = internalGetInitialAvailability();
      int j;
      if ((i != 0) && (i != 1))
      {
        j = i;
        if (i != 2) {}
      }
      else if (!internalGetIsEnrolled(mKeyphraseMetadata.id, mLocale))
      {
        j = 1;
      }
      else
      {
        j = 2;
      }
      synchronized (mLock)
      {
        AlwaysOnHotwordDetector.access$202(AlwaysOnHotwordDetector.this, j);
        AlwaysOnHotwordDetector.this.notifyStateChangedLocked();
        return null;
      }
    }
  }
  
  static final class SoundTriggerListener
    extends IRecognitionStatusCallback.Stub
  {
    private final Handler mHandler;
    
    public SoundTriggerListener(Handler paramHandler)
    {
      mHandler = paramHandler;
    }
    
    public void onError(int paramInt)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("onError: ");
      localStringBuilder.append(paramInt);
      Slog.i("AlwaysOnHotwordDetector", localStringBuilder.toString());
      mHandler.sendEmptyMessage(3);
    }
    
    public void onGenericSoundTriggerDetected(SoundTrigger.GenericRecognitionEvent paramGenericRecognitionEvent)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Generic sound trigger event detected at AOHD: ");
      localStringBuilder.append(paramGenericRecognitionEvent);
      Slog.w("AlwaysOnHotwordDetector", localStringBuilder.toString());
    }
    
    public void onKeyphraseDetected(SoundTrigger.KeyphraseRecognitionEvent paramKeyphraseRecognitionEvent)
    {
      Slog.i("AlwaysOnHotwordDetector", "onDetected");
      Message.obtain(mHandler, 2, new AlwaysOnHotwordDetector.EventPayload(triggerInData, captureAvailable, captureFormat, captureSession, data, null)).sendToTarget();
    }
    
    public void onRecognitionPaused()
    {
      Slog.i("AlwaysOnHotwordDetector", "onRecognitionPaused");
      mHandler.sendEmptyMessage(4);
    }
    
    public void onRecognitionResumed()
    {
      Slog.i("AlwaysOnHotwordDetector", "onRecognitionResumed");
      mHandler.sendEmptyMessage(5);
    }
  }
}
