package android.media;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

class AudioPortEventHandler
{
  private static final int AUDIOPORT_EVENT_NEW_LISTENER = 4;
  private static final int AUDIOPORT_EVENT_PATCH_LIST_UPDATED = 2;
  private static final int AUDIOPORT_EVENT_PORT_LIST_UPDATED = 1;
  private static final int AUDIOPORT_EVENT_SERVICE_DIED = 3;
  private static final long RESCHEDULE_MESSAGE_DELAY_MS = 100L;
  private static final String TAG = "AudioPortEventHandler";
  private Handler mHandler;
  private HandlerThread mHandlerThread;
  private long mJniCallback;
  private final ArrayList<AudioManager.OnAudioPortUpdateListener> mListeners = new ArrayList();
  
  AudioPortEventHandler() {}
  
  private native void native_finalize();
  
  private native void native_setup(Object paramObject);
  
  private static void postEventFromNative(Object paramObject1, int paramInt1, int paramInt2, int paramInt3, Object paramObject2)
  {
    paramObject1 = (AudioPortEventHandler)((WeakReference)paramObject1).get();
    if (paramObject1 == null) {
      return;
    }
    if (paramObject1 != null)
    {
      paramObject1 = paramObject1.handler();
      if (paramObject1 != null)
      {
        paramObject2 = paramObject1.obtainMessage(paramInt1, paramInt2, paramInt3, paramObject2);
        if (paramInt1 != 4) {
          paramObject1.removeMessages(paramInt1);
        }
        paramObject1.sendMessage(paramObject2);
      }
    }
  }
  
  protected void finalize()
  {
    native_finalize();
    if (mHandlerThread.isAlive()) {
      mHandlerThread.quit();
    }
  }
  
  Handler handler()
  {
    return mHandler;
  }
  
  void init()
  {
    try
    {
      if (mHandler != null) {
        return;
      }
      Object localObject1 = new android/os/HandlerThread;
      ((HandlerThread)localObject1).<init>("AudioPortEventHandler");
      mHandlerThread = ((HandlerThread)localObject1);
      mHandlerThread.start();
      if (mHandlerThread.getLooper() != null)
      {
        localObject1 = new android/media/AudioPortEventHandler$1;
        ((1)localObject1).<init>(this, mHandlerThread.getLooper());
        mHandler = ((Handler)localObject1);
        localObject1 = new java/lang/ref/WeakReference;
        ((WeakReference)localObject1).<init>(this);
        native_setup(localObject1);
      }
      else
      {
        mHandler = null;
      }
      return;
    }
    finally {}
  }
  
  void registerListener(AudioManager.OnAudioPortUpdateListener paramOnAudioPortUpdateListener)
  {
    try
    {
      mListeners.add(paramOnAudioPortUpdateListener);
      if (mHandler != null)
      {
        paramOnAudioPortUpdateListener = mHandler.obtainMessage(4, 0, 0, paramOnAudioPortUpdateListener);
        mHandler.sendMessage(paramOnAudioPortUpdateListener);
      }
      return;
    }
    finally {}
  }
  
  void unregisterListener(AudioManager.OnAudioPortUpdateListener paramOnAudioPortUpdateListener)
  {
    try
    {
      mListeners.remove(paramOnAudioPortUpdateListener);
      return;
    }
    finally {}
  }
}
