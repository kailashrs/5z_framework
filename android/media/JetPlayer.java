package android.media;

import android.content.res.AssetFileDescriptor;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AndroidRuntimeException;
import android.util.Log;
import java.io.FileDescriptor;
import java.lang.ref.WeakReference;

public class JetPlayer
{
  private static final int JET_EVENT = 1;
  private static final int JET_EVENT_CHAN_MASK = 245760;
  private static final int JET_EVENT_CHAN_SHIFT = 14;
  private static final int JET_EVENT_CTRL_MASK = 16256;
  private static final int JET_EVENT_CTRL_SHIFT = 7;
  private static final int JET_EVENT_SEG_MASK = -16777216;
  private static final int JET_EVENT_SEG_SHIFT = 24;
  private static final int JET_EVENT_TRACK_MASK = 16515072;
  private static final int JET_EVENT_TRACK_SHIFT = 18;
  private static final int JET_EVENT_VAL_MASK = 127;
  private static final int JET_NUMQUEUEDSEGMENT_UPDATE = 3;
  private static final int JET_OUTPUT_CHANNEL_CONFIG = 12;
  private static final int JET_OUTPUT_RATE = 22050;
  private static final int JET_PAUSE_UPDATE = 4;
  private static final int JET_USERID_UPDATE = 2;
  private static int MAXTRACKS = 32;
  private static final String TAG = "JetPlayer-J";
  private static JetPlayer singletonRef;
  private NativeEventHandler mEventHandler = null;
  private final Object mEventListenerLock = new Object();
  private Looper mInitializationLooper = null;
  private OnJetEventListener mJetEventListener = null;
  private long mNativePlayerInJavaObj;
  
  private JetPlayer()
  {
    Looper localLooper = Looper.myLooper();
    mInitializationLooper = localLooper;
    if (localLooper == null) {
      mInitializationLooper = Looper.getMainLooper();
    }
    int i = AudioTrack.getMinBufferSize(22050, 12, 2);
    if ((i != -1) && (i != -2)) {
      native_setup(new WeakReference(this), getMaxTracks(), Math.max(1200, i / (AudioFormat.getBytesPerSample(2) * 2)));
    }
  }
  
  public static JetPlayer getJetPlayer()
  {
    if (singletonRef == null) {
      singletonRef = new JetPlayer();
    }
    return singletonRef;
  }
  
  public static int getMaxTracks()
  {
    return MAXTRACKS;
  }
  
  private static void logd(String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("[ android.media.JetPlayer ] ");
    localStringBuilder.append(paramString);
    Log.d("JetPlayer-J", localStringBuilder.toString());
  }
  
  private static void loge(String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("[ android.media.JetPlayer ] ");
    localStringBuilder.append(paramString);
    Log.e("JetPlayer-J", localStringBuilder.toString());
  }
  
  private final native boolean native_clearQueue();
  
  private final native boolean native_closeJetFile();
  
  private final native void native_finalize();
  
  private final native boolean native_loadJetFromFile(String paramString);
  
  private final native boolean native_loadJetFromFileD(FileDescriptor paramFileDescriptor, long paramLong1, long paramLong2);
  
  private final native boolean native_pauseJet();
  
  private final native boolean native_playJet();
  
  private final native boolean native_queueJetSegment(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, byte paramByte);
  
  private final native boolean native_queueJetSegmentMuteArray(int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean[] paramArrayOfBoolean, byte paramByte);
  
  private final native void native_release();
  
  private final native boolean native_setMuteArray(boolean[] paramArrayOfBoolean, boolean paramBoolean);
  
  private final native boolean native_setMuteFlag(int paramInt, boolean paramBoolean1, boolean paramBoolean2);
  
  private final native boolean native_setMuteFlags(int paramInt, boolean paramBoolean);
  
  private final native boolean native_setup(Object paramObject, int paramInt1, int paramInt2);
  
  private final native boolean native_triggerClip(int paramInt);
  
  private static void postEventFromNative(Object paramObject, int paramInt1, int paramInt2, int paramInt3)
  {
    paramObject = (JetPlayer)((WeakReference)paramObject).get();
    if ((paramObject != null) && (mEventHandler != null))
    {
      Message localMessage = mEventHandler.obtainMessage(paramInt1, paramInt2, paramInt3, null);
      mEventHandler.sendMessage(localMessage);
    }
  }
  
  public boolean clearQueue()
  {
    return native_clearQueue();
  }
  
  public Object clone()
    throws CloneNotSupportedException
  {
    throw new CloneNotSupportedException();
  }
  
  public boolean closeJetFile()
  {
    return native_closeJetFile();
  }
  
  protected void finalize()
  {
    native_finalize();
  }
  
  public boolean loadJetFile(AssetFileDescriptor paramAssetFileDescriptor)
  {
    long l = paramAssetFileDescriptor.getLength();
    if (l >= 0L) {
      return native_loadJetFromFileD(paramAssetFileDescriptor.getFileDescriptor(), paramAssetFileDescriptor.getStartOffset(), l);
    }
    throw new AndroidRuntimeException("no length for fd");
  }
  
  public boolean loadJetFile(String paramString)
  {
    return native_loadJetFromFile(paramString);
  }
  
  public boolean pause()
  {
    return native_pauseJet();
  }
  
  public boolean play()
  {
    return native_playJet();
  }
  
  public boolean queueJetSegment(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, byte paramByte)
  {
    return native_queueJetSegment(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramByte);
  }
  
  public boolean queueJetSegmentMuteArray(int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean[] paramArrayOfBoolean, byte paramByte)
  {
    if (paramArrayOfBoolean.length != getMaxTracks()) {
      return false;
    }
    return native_queueJetSegmentMuteArray(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfBoolean, paramByte);
  }
  
  public void release()
  {
    native_release();
    singletonRef = null;
  }
  
  public void setEventListener(OnJetEventListener paramOnJetEventListener)
  {
    setEventListener(paramOnJetEventListener, null);
  }
  
  public void setEventListener(OnJetEventListener paramOnJetEventListener, Handler paramHandler)
  {
    synchronized (mEventListenerLock)
    {
      mJetEventListener = paramOnJetEventListener;
      if (paramOnJetEventListener != null)
      {
        if (paramHandler != null)
        {
          paramOnJetEventListener = new android/media/JetPlayer$NativeEventHandler;
          paramOnJetEventListener.<init>(this, this, paramHandler.getLooper());
          mEventHandler = paramOnJetEventListener;
        }
        else
        {
          paramOnJetEventListener = new android/media/JetPlayer$NativeEventHandler;
          paramOnJetEventListener.<init>(this, this, mInitializationLooper);
          mEventHandler = paramOnJetEventListener;
        }
      }
      else {
        mEventHandler = null;
      }
      return;
    }
  }
  
  public boolean setMuteArray(boolean[] paramArrayOfBoolean, boolean paramBoolean)
  {
    if (paramArrayOfBoolean.length != getMaxTracks()) {
      return false;
    }
    return native_setMuteArray(paramArrayOfBoolean, paramBoolean);
  }
  
  public boolean setMuteFlag(int paramInt, boolean paramBoolean1, boolean paramBoolean2)
  {
    return native_setMuteFlag(paramInt, paramBoolean1, paramBoolean2);
  }
  
  public boolean setMuteFlags(int paramInt, boolean paramBoolean)
  {
    return native_setMuteFlags(paramInt, paramBoolean);
  }
  
  public boolean triggerClip(int paramInt)
  {
    return native_triggerClip(paramInt);
  }
  
  private class NativeEventHandler
    extends Handler
  {
    private JetPlayer mJet;
    
    public NativeEventHandler(JetPlayer paramJetPlayer, Looper paramLooper)
    {
      super();
      mJet = paramJetPlayer;
    }
    
    public void handleMessage(Message paramMessage)
    {
      synchronized (mEventListenerLock)
      {
        JetPlayer.OnJetEventListener localOnJetEventListener = mJet.mJetEventListener;
        switch (what)
        {
        default: 
          ??? = new StringBuilder();
          ((StringBuilder)???).append("Unknown message type ");
          ((StringBuilder)???).append(what);
          JetPlayer.loge(((StringBuilder)???).toString());
          return;
        case 4: 
          if (localOnJetEventListener != null) {
            localOnJetEventListener.onJetPauseUpdate(mJet, arg1);
          }
          return;
        case 3: 
          if (localOnJetEventListener != null) {
            localOnJetEventListener.onJetNumQueuedSegmentUpdate(mJet, arg1);
          }
          return;
        case 2: 
          if (localOnJetEventListener != null) {
            localOnJetEventListener.onJetUserIdUpdate(mJet, arg1, arg2);
          }
          return;
        }
        if (localOnJetEventListener != null) {
          mJetEventListener.onJetEvent(mJet, (short)((arg1 & 0xFF000000) >> 24), (byte)((arg1 & 0xFC0000) >> 18), (byte)(((arg1 & 0x3C000) >> 14) + 1), (byte)((arg1 & 0x3F80) >> 7), (byte)(arg1 & 0x7F));
        }
        return;
      }
    }
  }
  
  public static abstract interface OnJetEventListener
  {
    public abstract void onJetEvent(JetPlayer paramJetPlayer, short paramShort, byte paramByte1, byte paramByte2, byte paramByte3, byte paramByte4);
    
    public abstract void onJetNumQueuedSegmentUpdate(JetPlayer paramJetPlayer, int paramInt);
    
    public abstract void onJetPauseUpdate(JetPlayer paramJetPlayer, int paramInt);
    
    public abstract void onJetUserIdUpdate(JetPlayer paramJetPlayer, int paramInt1, int paramInt2);
  }
}
