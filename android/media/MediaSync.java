package android.media;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Surface;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public final class MediaSync
{
  private static final int CB_RETURN_AUDIO_BUFFER = 1;
  private static final int EVENT_CALLBACK = 1;
  private static final int EVENT_SET_CALLBACK = 2;
  public static final int MEDIASYNC_ERROR_AUDIOTRACK_FAIL = 1;
  public static final int MEDIASYNC_ERROR_SURFACE_FAIL = 2;
  private static final String TAG = "MediaSync";
  private List<AudioBuffer> mAudioBuffers = new LinkedList();
  private Handler mAudioHandler = null;
  private final Object mAudioLock = new Object();
  private Looper mAudioLooper = null;
  private Thread mAudioThread = null;
  private AudioTrack mAudioTrack = null;
  private Callback mCallback = null;
  private Handler mCallbackHandler = null;
  private final Object mCallbackLock = new Object();
  private long mNativeContext;
  private OnErrorListener mOnErrorListener = null;
  private Handler mOnErrorListenerHandler = null;
  private final Object mOnErrorListenerLock = new Object();
  private float mPlaybackRate = 0.0F;
  
  static
  {
    System.loadLibrary("media_jni");
    native_init();
  }
  
  public MediaSync()
  {
    native_setup();
  }
  
  private void createAudioThread()
  {
    mAudioThread = new Thread()
    {
      public void run()
      {
        
        synchronized (mAudioLock)
        {
          MediaSync.access$1102(MediaSync.this, Looper.myLooper());
          MediaSync localMediaSync = MediaSync.this;
          Handler localHandler = new android/os/Handler;
          localHandler.<init>();
          MediaSync.access$1202(localMediaSync, localHandler);
          mAudioLock.notify();
          Looper.loop();
          return;
        }
      }
    };
    mAudioThread.start();
    try
    {
      synchronized (mAudioLock)
      {
        mAudioLock.wait();
      }
    }
    catch (InterruptedException localInterruptedException) {}
  }
  
  private final native void native_finalize();
  
  private final native void native_flush();
  
  private final native long native_getPlayTimeForPendingAudioFrames();
  
  private final native boolean native_getTimestamp(MediaTimestamp paramMediaTimestamp);
  
  private static final native void native_init();
  
  private final native void native_release();
  
  private final native void native_setAudioTrack(AudioTrack paramAudioTrack);
  
  private native float native_setPlaybackParams(PlaybackParams paramPlaybackParams);
  
  private final native void native_setSurface(Surface paramSurface);
  
  private native float native_setSyncParams(SyncParams paramSyncParams);
  
  private final native void native_setup();
  
  private final native void native_updateQueuedAudioData(int paramInt, long paramLong);
  
  private void postRenderAudio(long paramLong)
  {
    mAudioHandler.postDelayed(new Runnable()
    {
      public void run()
      {
        synchronized (mAudioLock)
        {
          if (mPlaybackRate == 0.0D) {
            return;
          }
          if (mAudioBuffers.isEmpty()) {
            return;
          }
          MediaSync.AudioBuffer localAudioBuffer = (MediaSync.AudioBuffer)mAudioBuffers.get(0);
          int i = mByteBuffer.remaining();
          if (i > 0)
          {
            j = mAudioTrack.getPlayState();
            if (j != 3) {
              try
              {
                mAudioTrack.play();
              }
              catch (IllegalStateException localIllegalStateException)
              {
                Log.w("MediaSync", "could not start audio track");
              }
            }
          }
          int j = mAudioTrack.write(mByteBuffer, i, 1);
          if (j > 0)
          {
            if (mPresentationTimeUs != -1L)
            {
              MediaSync.this.native_updateQueuedAudioData(i, mPresentationTimeUs);
              mPresentationTimeUs = -1L;
            }
            if (j == i)
            {
              MediaSync.this.postReturnByteBuffer(localAudioBuffer);
              mAudioBuffers.remove(0);
              if (!mAudioBuffers.isEmpty()) {
                MediaSync.this.postRenderAudio(0L);
              }
              return;
            }
          }
          long l = TimeUnit.MICROSECONDS.toMillis(MediaSync.this.native_getPlayTimeForPendingAudioFrames());
          MediaSync.this.postRenderAudio(l / 2L);
          return;
        }
      }
    }, paramLong);
  }
  
  private final void postReturnByteBuffer(AudioBuffer paramAudioBuffer)
  {
    synchronized (mCallbackLock)
    {
      if (mCallbackHandler != null)
      {
        Handler localHandler = mCallbackHandler;
        Runnable local2 = new android/media/MediaSync$2;
        local2.<init>(this, this, paramAudioBuffer);
        localHandler.post(local2);
      }
      return;
    }
  }
  
  private final void returnAudioBuffers()
  {
    synchronized (mAudioLock)
    {
      Iterator localIterator = mAudioBuffers.iterator();
      while (localIterator.hasNext()) {
        postReturnByteBuffer((AudioBuffer)localIterator.next());
      }
      mAudioBuffers.clear();
      return;
    }
  }
  
  public final native Surface createInputSurface();
  
  protected void finalize()
  {
    native_finalize();
  }
  
  public void flush()
  {
    synchronized (mAudioLock)
    {
      mAudioBuffers.clear();
      mCallbackHandler.removeCallbacksAndMessages(null);
      if (mAudioTrack != null)
      {
        mAudioTrack.pause();
        mAudioTrack.flush();
        mAudioTrack.stop();
      }
      native_flush();
      return;
    }
  }
  
  public native PlaybackParams getPlaybackParams();
  
  public native SyncParams getSyncParams();
  
  public MediaTimestamp getTimestamp()
  {
    try
    {
      MediaTimestamp localMediaTimestamp = new android/media/MediaTimestamp;
      localMediaTimestamp.<init>();
      boolean bool = native_getTimestamp(localMediaTimestamp);
      if (bool) {
        return localMediaTimestamp;
      }
      return null;
    }
    catch (IllegalStateException localIllegalStateException) {}
    return null;
  }
  
  public void queueAudio(ByteBuffer paramByteBuffer, int paramInt, long paramLong)
  {
    if ((mAudioTrack != null) && (mAudioThread != null)) {
      synchronized (mAudioLock)
      {
        List localList = mAudioBuffers;
        AudioBuffer localAudioBuffer = new android/media/MediaSync$AudioBuffer;
        localAudioBuffer.<init>(paramByteBuffer, paramInt, paramLong);
        localList.add(localAudioBuffer);
        if (mPlaybackRate != 0.0D) {
          postRenderAudio(0L);
        }
        return;
      }
    }
    throw new IllegalStateException("AudioTrack is NOT set or audio thread is not created");
  }
  
  public final void release()
  {
    returnAudioBuffers();
    if ((mAudioThread != null) && (mAudioLooper != null)) {
      mAudioLooper.quit();
    }
    setCallback(null, null);
    native_release();
  }
  
  public void setAudioTrack(AudioTrack paramAudioTrack)
  {
    native_setAudioTrack(paramAudioTrack);
    mAudioTrack = paramAudioTrack;
    if ((paramAudioTrack != null) && (mAudioThread == null)) {
      createAudioThread();
    }
  }
  
  public void setCallback(Callback paramCallback, Handler paramHandler)
  {
    Object localObject1 = mCallbackLock;
    if (paramHandler != null)
    {
      try
      {
        mCallbackHandler = paramHandler;
      }
      finally
      {
        break label77;
      }
    }
    else
    {
      Object localObject2 = Looper.myLooper();
      paramHandler = (Handler)localObject2;
      if (localObject2 == null) {
        paramHandler = Looper.getMainLooper();
      }
      if (paramHandler == null)
      {
        mCallbackHandler = null;
      }
      else
      {
        localObject2 = new android/os/Handler;
        ((Handler)localObject2).<init>(paramHandler);
        mCallbackHandler = ((Handler)localObject2);
      }
    }
    mCallback = paramCallback;
    return;
    label77:
    throw paramCallback;
  }
  
  public void setOnErrorListener(OnErrorListener paramOnErrorListener, Handler paramHandler)
  {
    Object localObject1 = mOnErrorListenerLock;
    if (paramHandler != null)
    {
      try
      {
        mOnErrorListenerHandler = paramHandler;
      }
      finally
      {
        break label77;
      }
    }
    else
    {
      Object localObject2 = Looper.myLooper();
      paramHandler = (Handler)localObject2;
      if (localObject2 == null) {
        paramHandler = Looper.getMainLooper();
      }
      if (paramHandler == null)
      {
        mOnErrorListenerHandler = null;
      }
      else
      {
        localObject2 = new android/os/Handler;
        ((Handler)localObject2).<init>(paramHandler);
        mOnErrorListenerHandler = ((Handler)localObject2);
      }
    }
    mOnErrorListener = paramOnErrorListener;
    return;
    label77:
    throw paramOnErrorListener;
  }
  
  public void setPlaybackParams(PlaybackParams paramPlaybackParams)
  {
    synchronized (mAudioLock)
    {
      mPlaybackRate = native_setPlaybackParams(paramPlaybackParams);
      if ((mPlaybackRate != 0.0D) && (mAudioThread != null)) {
        postRenderAudio(0L);
      }
      return;
    }
  }
  
  public void setSurface(Surface paramSurface)
  {
    native_setSurface(paramSurface);
  }
  
  public void setSyncParams(SyncParams paramSyncParams)
  {
    synchronized (mAudioLock)
    {
      mPlaybackRate = native_setSyncParams(paramSyncParams);
      if ((mPlaybackRate != 0.0D) && (mAudioThread != null)) {
        postRenderAudio(0L);
      }
      return;
    }
  }
  
  private static class AudioBuffer
  {
    public int mBufferIndex;
    public ByteBuffer mByteBuffer;
    long mPresentationTimeUs;
    
    public AudioBuffer(ByteBuffer paramByteBuffer, int paramInt, long paramLong)
    {
      mByteBuffer = paramByteBuffer;
      mBufferIndex = paramInt;
      mPresentationTimeUs = paramLong;
    }
  }
  
  public static abstract class Callback
  {
    public Callback() {}
    
    public abstract void onAudioBufferConsumed(MediaSync paramMediaSync, ByteBuffer paramByteBuffer, int paramInt);
  }
  
  public static abstract interface OnErrorListener
  {
    public abstract void onError(MediaSync paramMediaSync, int paramInt1, int paramInt2);
  }
}
