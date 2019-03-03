package android.hardware.soundtrigger;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import java.lang.ref.WeakReference;

public class SoundTriggerModule
{
  private static final int EVENT_RECOGNITION = 1;
  private static final int EVENT_SERVICE_DIED = 2;
  private static final int EVENT_SERVICE_STATE_CHANGE = 4;
  private static final int EVENT_SOUNDMODEL = 3;
  private NativeEventHandlerDelegate mEventHandlerDelegate;
  private int mId;
  private long mNativeContext;
  
  SoundTriggerModule(int paramInt, SoundTrigger.StatusListener paramStatusListener, Handler paramHandler)
  {
    mId = paramInt;
    mEventHandlerDelegate = new NativeEventHandlerDelegate(paramStatusListener, paramHandler);
    native_setup(new WeakReference(this));
  }
  
  private native void native_finalize();
  
  private native void native_setup(Object paramObject);
  
  private static void postEventFromNative(Object paramObject1, int paramInt1, int paramInt2, int paramInt3, Object paramObject2)
  {
    paramObject1 = (SoundTriggerModule)((WeakReference)paramObject1).get();
    if (paramObject1 == null) {
      return;
    }
    paramObject1 = mEventHandlerDelegate;
    if (paramObject1 != null)
    {
      paramObject1 = paramObject1.handler();
      if (paramObject1 != null) {
        paramObject1.sendMessage(paramObject1.obtainMessage(paramInt1, paramInt2, paramInt3, paramObject2));
      }
    }
  }
  
  public native void detach();
  
  protected void finalize()
  {
    native_finalize();
  }
  
  public native int loadSoundModel(SoundTrigger.SoundModel paramSoundModel, int[] paramArrayOfInt);
  
  public native int startRecognition(int paramInt, SoundTrigger.RecognitionConfig paramRecognitionConfig);
  
  public native int stopRecognition(int paramInt);
  
  public native int unloadSoundModel(int paramInt);
  
  private class NativeEventHandlerDelegate
  {
    private final Handler mHandler;
    
    NativeEventHandlerDelegate(final SoundTrigger.StatusListener paramStatusListener, Handler paramHandler)
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
              break;
            case 4: 
              if (paramStatusListener != null) {
                paramStatusListener.onServiceStateChange(arg1);
              }
              break;
            case 3: 
              if (paramStatusListener != null) {
                paramStatusListener.onSoundModelUpdate((SoundTrigger.SoundModelEvent)obj);
              }
              break;
            case 2: 
              if (paramStatusListener != null) {
                paramStatusListener.onServiceDied();
              }
              break;
            case 1: 
              if (paramStatusListener != null) {
                paramStatusListener.onRecognition((SoundTrigger.RecognitionEvent)obj);
              }
              break;
            }
          }
        };
      } else {
        mHandler = null;
      }
    }
    
    Handler handler()
    {
      return mHandler;
    }
  }
}
