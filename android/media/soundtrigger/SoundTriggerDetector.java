package android.media.soundtrigger;

import android.annotation.SystemApi;
import android.hardware.soundtrigger.IRecognitionStatusCallback.Stub;
import android.hardware.soundtrigger.SoundTrigger.GenericRecognitionEvent;
import android.hardware.soundtrigger.SoundTrigger.KeyphraseRecognitionEvent;
import android.hardware.soundtrigger.SoundTrigger.RecognitionConfig;
import android.media.AudioFormat;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.ParcelUuid;
import android.os.RemoteException;
import android.util.Slog;
import com.android.internal.app.ISoundTriggerService;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.UUID;

@SystemApi
public final class SoundTriggerDetector
{
  private static final boolean DBG = false;
  private static final int MSG_AVAILABILITY_CHANGED = 1;
  private static final int MSG_DETECTION_ERROR = 3;
  private static final int MSG_DETECTION_PAUSE = 4;
  private static final int MSG_DETECTION_RESUME = 5;
  private static final int MSG_SOUND_TRIGGER_DETECTED = 2;
  public static final int RECOGNITION_FLAG_ALLOW_MULTIPLE_TRIGGERS = 2;
  public static final int RECOGNITION_FLAG_CAPTURE_TRIGGER_AUDIO = 1;
  public static final int RECOGNITION_FLAG_NONE = 0;
  private static final String TAG = "SoundTriggerDetector";
  private final Callback mCallback;
  private final Handler mHandler;
  private final Object mLock = new Object();
  private final RecognitionCallback mRecognitionCallback;
  private final UUID mSoundModelId;
  private final ISoundTriggerService mSoundTriggerService;
  
  SoundTriggerDetector(ISoundTriggerService paramISoundTriggerService, UUID paramUUID, Callback paramCallback, Handler paramHandler)
  {
    mSoundTriggerService = paramISoundTriggerService;
    mSoundModelId = paramUUID;
    mCallback = paramCallback;
    if (paramHandler == null) {
      mHandler = new MyHandler();
    } else {
      mHandler = new MyHandler(paramHandler.getLooper());
    }
    mRecognitionCallback = new RecognitionCallback(null);
  }
  
  public void dump(String paramString, PrintWriter arg2)
  {
    synchronized (mLock) {}
  }
  
  public boolean startRecognition(int paramInt)
  {
    boolean bool1 = true;
    boolean bool2;
    if ((paramInt & 0x1) != 0) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    boolean bool3;
    if ((paramInt & 0x2) != 0) {
      bool3 = true;
    } else {
      bool3 = false;
    }
    try
    {
      ISoundTriggerService localISoundTriggerService = mSoundTriggerService;
      ParcelUuid localParcelUuid = new android/os/ParcelUuid;
      localParcelUuid.<init>(mSoundModelId);
      RecognitionCallback localRecognitionCallback = mRecognitionCallback;
      SoundTrigger.RecognitionConfig localRecognitionConfig = new android/hardware/soundtrigger/SoundTrigger$RecognitionConfig;
      localRecognitionConfig.<init>(bool2, bool3, null, null);
      paramInt = localISoundTriggerService.startRecognition(localParcelUuid, localRecognitionCallback, localRecognitionConfig);
      if (paramInt == 0) {
        bool2 = bool1;
      } else {
        bool2 = false;
      }
      return bool2;
    }
    catch (RemoteException localRemoteException) {}
    return false;
  }
  
  public boolean stopRecognition()
  {
    boolean bool = false;
    try
    {
      ISoundTriggerService localISoundTriggerService = mSoundTriggerService;
      ParcelUuid localParcelUuid = new android/os/ParcelUuid;
      localParcelUuid.<init>(mSoundModelId);
      int i = localISoundTriggerService.stopRecognition(localParcelUuid, mRecognitionCallback);
      if (i == 0) {
        bool = true;
      }
      return bool;
    }
    catch (RemoteException localRemoteException) {}
    return false;
  }
  
  public static abstract class Callback
  {
    public Callback() {}
    
    public abstract void onAvailabilityChanged(int paramInt);
    
    public abstract void onDetected(SoundTriggerDetector.EventPayload paramEventPayload);
    
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
    
    public byte[] getData()
    {
      if (!mTriggerAvailable) {
        return mData;
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
  
  private class MyHandler
    extends Handler
  {
    MyHandler() {}
    
    MyHandler(Looper paramLooper)
    {
      super();
    }
    
    public void handleMessage(Message paramMessage)
    {
      if (mCallback == null)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Received message: ");
        localStringBuilder.append(what);
        localStringBuilder.append(" for NULL callback.");
        Slog.w("SoundTriggerDetector", localStringBuilder.toString());
        return;
      }
      switch (what)
      {
      default: 
        super.handleMessage(paramMessage);
        break;
      case 5: 
        mCallback.onRecognitionResumed();
        break;
      case 4: 
        mCallback.onRecognitionPaused();
        break;
      case 3: 
        mCallback.onError();
        break;
      case 2: 
        mCallback.onDetected((SoundTriggerDetector.EventPayload)obj);
      }
    }
  }
  
  private class RecognitionCallback
    extends IRecognitionStatusCallback.Stub
  {
    private RecognitionCallback() {}
    
    public void onError(int paramInt)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("onError()");
      localStringBuilder.append(paramInt);
      Slog.d("SoundTriggerDetector", localStringBuilder.toString());
      mHandler.sendEmptyMessage(3);
    }
    
    public void onGenericSoundTriggerDetected(SoundTrigger.GenericRecognitionEvent paramGenericRecognitionEvent)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("onGenericSoundTriggerDetected()");
      localStringBuilder.append(paramGenericRecognitionEvent);
      Slog.d("SoundTriggerDetector", localStringBuilder.toString());
      Message.obtain(mHandler, 2, new SoundTriggerDetector.EventPayload(triggerInData, captureAvailable, captureFormat, captureSession, data, null)).sendToTarget();
    }
    
    public void onKeyphraseDetected(SoundTrigger.KeyphraseRecognitionEvent paramKeyphraseRecognitionEvent)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Ignoring onKeyphraseDetected() called for ");
      localStringBuilder.append(paramKeyphraseRecognitionEvent);
      Slog.e("SoundTriggerDetector", localStringBuilder.toString());
    }
    
    public void onRecognitionPaused()
    {
      Slog.d("SoundTriggerDetector", "onRecognitionPaused()");
      mHandler.sendEmptyMessage(4);
    }
    
    public void onRecognitionResumed()
    {
      Slog.d("SoundTriggerDetector", "onRecognitionResumed()");
      mHandler.sendEmptyMessage(5);
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface RecognitionFlags {}
}
