package android.media;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.util.AndroidRuntimeException;
import android.util.Log;
import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.lang.ref.WeakReference;

public class SoundPool
  extends PlayerBase
{
  private static final boolean DEBUG = Log.isLoggable("SoundPool", 3);
  private static final int SAMPLE_LOADED = 1;
  private static final String TAG = "SoundPool";
  private final AudioAttributes mAttributes;
  private EventHandler mEventHandler;
  private boolean mHasAppOpsPlayAudio;
  private final Object mLock;
  private long mNativeContext;
  private OnLoadCompleteListener mOnLoadCompleteListener;
  
  static
  {
    System.loadLibrary("soundpool");
  }
  
  public SoundPool(int paramInt1, int paramInt2, int paramInt3)
  {
    this(paramInt1, new AudioAttributes.Builder().setInternalLegacyStreamType(paramInt2).build());
    PlayerBase.deprecateStreamTypeForPlayback(paramInt2, "SoundPool", "SoundPool()");
  }
  
  private SoundPool(int paramInt, AudioAttributes paramAudioAttributes)
  {
    super(paramAudioAttributes, 3);
    if (native_setup(new WeakReference(this), paramInt, paramAudioAttributes) == 0)
    {
      mLock = new Object();
      mAttributes = paramAudioAttributes;
      baseRegisterPlayer();
      return;
    }
    throw new RuntimeException("Native setup failed");
  }
  
  private final native int _load(FileDescriptor paramFileDescriptor, long paramLong1, long paramLong2, int paramInt);
  
  private final native void _mute(boolean paramBoolean);
  
  private final native int _play(int paramInt1, float paramFloat1, float paramFloat2, int paramInt2, int paramInt3, float paramFloat3);
  
  private final native void _setVolume(int paramInt, float paramFloat1, float paramFloat2);
  
  private final native void native_release();
  
  private final native int native_setup(Object paramObject1, int paramInt, Object paramObject2);
  
  private static void postEventFromNative(Object paramObject1, int paramInt1, int paramInt2, int paramInt3, Object paramObject2)
  {
    paramObject1 = (SoundPool)((WeakReference)paramObject1).get();
    if (paramObject1 == null) {
      return;
    }
    if (mEventHandler != null)
    {
      paramObject2 = mEventHandler.obtainMessage(paramInt1, paramInt2, paramInt3, paramObject2);
      mEventHandler.sendMessage(paramObject2);
    }
  }
  
  public final native void autoPause();
  
  public final native void autoResume();
  
  protected void finalize()
  {
    release();
  }
  
  boolean isStreamSystemEnforcedMute()
  {
    return false;
  }
  
  public int load(Context paramContext, int paramInt1, int paramInt2)
  {
    paramContext = paramContext.getResources().openRawResourceFd(paramInt1);
    paramInt1 = 0;
    if (paramContext != null)
    {
      paramInt1 = _load(paramContext.getFileDescriptor(), paramContext.getStartOffset(), paramContext.getLength(), paramInt2);
      try
      {
        paramContext.close();
      }
      catch (IOException paramContext) {}
    }
    return paramInt1;
  }
  
  public int load(AssetFileDescriptor paramAssetFileDescriptor, int paramInt)
  {
    if (paramAssetFileDescriptor != null)
    {
      long l = paramAssetFileDescriptor.getLength();
      if (l >= 0L) {
        return _load(paramAssetFileDescriptor.getFileDescriptor(), paramAssetFileDescriptor.getStartOffset(), l, paramInt);
      }
      throw new AndroidRuntimeException("no length for fd");
    }
    return 0;
  }
  
  public int load(FileDescriptor paramFileDescriptor, long paramLong1, long paramLong2, int paramInt)
  {
    return _load(paramFileDescriptor, paramLong1, paramLong2, paramInt);
  }
  
  public int load(String paramString, int paramInt)
  {
    int i = 0;
    int j = 0;
    int k = i;
    try
    {
      File localFile = new java/io/File;
      k = i;
      localFile.<init>(paramString);
      k = i;
      ParcelFileDescriptor localParcelFileDescriptor = ParcelFileDescriptor.open(localFile, 268435456);
      k = j;
      if (localParcelFileDescriptor != null)
      {
        k = i;
        paramInt = _load(localParcelFileDescriptor.getFileDescriptor(), 0L, localFile.length(), paramInt);
        k = paramInt;
        localParcelFileDescriptor.close();
        k = paramInt;
      }
    }
    catch (IOException localIOException)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("error loading ");
      localStringBuilder.append(paramString);
      Log.e("SoundPool", localStringBuilder.toString());
    }
    return k;
  }
  
  public final native void pause(int paramInt);
  
  public final int play(int paramInt1, float paramFloat1, float paramFloat2, int paramInt2, int paramInt3, float paramFloat3)
  {
    if (mAttributes.getContentType() == 2) {
      executeAIVolume("SoundPool");
    }
    baseStart();
    return _play(paramInt1, paramFloat1, paramFloat2, paramInt2, paramInt3, paramFloat3);
  }
  
  int playerApplyVolumeShaper(VolumeShaper.Configuration paramConfiguration, VolumeShaper.Operation paramOperation)
  {
    return -1;
  }
  
  VolumeShaper.State playerGetVolumeShaperState(int paramInt)
  {
    return null;
  }
  
  void playerPause() {}
  
  int playerSetAuxEffectSendLevel(boolean paramBoolean, float paramFloat)
  {
    return 0;
  }
  
  void playerSetVolume(boolean paramBoolean, float paramFloat1, float paramFloat2)
  {
    _mute(paramBoolean);
  }
  
  void playerStart() {}
  
  void playerStop() {}
  
  public final void release()
  {
    baseRelease();
    native_release();
  }
  
  public final native void resume(int paramInt);
  
  public final native void setLoop(int paramInt1, int paramInt2);
  
  public void setOnLoadCompleteListener(OnLoadCompleteListener paramOnLoadCompleteListener)
  {
    Object localObject1 = mLock;
    if (paramOnLoadCompleteListener != null) {
      try
      {
        Object localObject2 = Looper.myLooper();
        Object localObject3;
        if (localObject2 != null)
        {
          localObject3 = new android/media/SoundPool$EventHandler;
          ((EventHandler)localObject3).<init>(this, (Looper)localObject2);
          mEventHandler = ((EventHandler)localObject3);
        }
        else
        {
          localObject3 = Looper.getMainLooper();
          if (localObject3 != null)
          {
            localObject2 = new android/media/SoundPool$EventHandler;
            ((EventHandler)localObject2).<init>(this, (Looper)localObject3);
            mEventHandler = ((EventHandler)localObject2);
          }
          else
          {
            mEventHandler = null;
          }
        }
      }
      finally
      {
        break label94;
      }
    }
    mEventHandler = null;
    mOnLoadCompleteListener = paramOnLoadCompleteListener;
    return;
    label94:
    throw paramOnLoadCompleteListener;
  }
  
  public final native void setPriority(int paramInt1, int paramInt2);
  
  public final native void setRate(int paramInt, float paramFloat);
  
  public void setVolume(int paramInt, float paramFloat)
  {
    setVolume(paramInt, paramFloat, paramFloat);
  }
  
  public final void setVolume(int paramInt, float paramFloat1, float paramFloat2)
  {
    _setVolume(paramInt, paramFloat1, paramFloat2);
  }
  
  public final native void stop(int paramInt);
  
  public final native boolean unload(int paramInt);
  
  public static class Builder
  {
    private AudioAttributes mAudioAttributes;
    private int mMaxStreams = 1;
    
    public Builder() {}
    
    public SoundPool build()
    {
      if (mAudioAttributes == null) {
        mAudioAttributes = new AudioAttributes.Builder().setUsage(1).build();
      }
      return new SoundPool(mMaxStreams, mAudioAttributes, null);
    }
    
    public Builder setAudioAttributes(AudioAttributes paramAudioAttributes)
      throws IllegalArgumentException
    {
      if (paramAudioAttributes != null)
      {
        mAudioAttributes = paramAudioAttributes;
        return this;
      }
      throw new IllegalArgumentException("Invalid null AudioAttributes");
    }
    
    public Builder setMaxStreams(int paramInt)
      throws IllegalArgumentException
    {
      if (paramInt > 0)
      {
        mMaxStreams = paramInt;
        return this;
      }
      throw new IllegalArgumentException("Strictly positive value required for the maximum number of streams");
    }
  }
  
  private final class EventHandler
    extends Handler
  {
    public EventHandler(Looper paramLooper)
    {
      super();
    }
    
    public void handleMessage(Message paramMessage)
    {
      if (what != 1)
      {
        ??? = new StringBuilder();
        ((StringBuilder)???).append("Unknown message type ");
        ((StringBuilder)???).append(what);
        Log.e("SoundPool", ((StringBuilder)???).toString());
        return;
      }
      if (SoundPool.DEBUG)
      {
        ??? = new StringBuilder();
        ((StringBuilder)???).append("Sample ");
        ((StringBuilder)???).append(arg1);
        ((StringBuilder)???).append(" loaded");
        Log.d("SoundPool", ((StringBuilder)???).toString());
      }
      synchronized (mLock)
      {
        if (mOnLoadCompleteListener != null) {
          mOnLoadCompleteListener.onLoadComplete(SoundPool.this, arg1, arg2);
        }
        return;
      }
    }
  }
  
  public static abstract interface OnLoadCompleteListener
  {
    public abstract void onLoadComplete(SoundPool paramSoundPool, int paramInt1, int paramInt2);
  }
}
