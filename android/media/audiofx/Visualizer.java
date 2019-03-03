package android.media.audiofx;

import android.app.ActivityThread;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import java.lang.ref.WeakReference;

public class Visualizer
{
  public static final int ALREADY_EXISTS = -2;
  public static final int ERROR = -1;
  public static final int ERROR_BAD_VALUE = -4;
  public static final int ERROR_DEAD_OBJECT = -7;
  public static final int ERROR_INVALID_OPERATION = -5;
  public static final int ERROR_NO_INIT = -3;
  public static final int ERROR_NO_MEMORY = -6;
  public static final int MEASUREMENT_MODE_NONE = 0;
  public static final int MEASUREMENT_MODE_PEAK_RMS = 1;
  private static final int NATIVE_EVENT_FFT_CAPTURE = 1;
  private static final int NATIVE_EVENT_PCM_CAPTURE = 0;
  private static final int NATIVE_EVENT_SERVER_DIED = 2;
  public static final int SCALING_MODE_AS_PLAYED = 1;
  public static final int SCALING_MODE_NORMALIZED = 0;
  public static final int STATE_ENABLED = 2;
  public static final int STATE_INITIALIZED = 1;
  public static final int STATE_UNINITIALIZED = 0;
  public static final int SUCCESS = 0;
  private static final String TAG = "Visualizer-JAVA";
  private OnDataCaptureListener mCaptureListener = null;
  private int mId;
  private long mJniData;
  private final Object mListenerLock = new Object();
  private NativeEventHandler mNativeEventHandler = null;
  private long mNativeVisualizer;
  private OnServerDiedListener mServerDiedListener = null;
  private int mState = 0;
  private final Object mStateLock = new Object();
  
  static
  {
    System.loadLibrary("audioeffect_jni");
    native_init();
  }
  
  public Visualizer(int paramInt)
    throws UnsupportedOperationException, RuntimeException
  {
    Object localObject1 = new int[1];
    synchronized (mStateLock)
    {
      mState = 0;
      Object localObject4 = new java/lang/ref/WeakReference;
      ((WeakReference)localObject4).<init>(this);
      paramInt = native_setup(localObject4, paramInt, (int[])localObject1, ActivityThread.currentOpPackageName());
      if ((paramInt != 0) && (paramInt != -2))
      {
        localObject1 = new java/lang/StringBuilder;
        ((StringBuilder)localObject1).<init>();
        ((StringBuilder)localObject1).append("Error code ");
        ((StringBuilder)localObject1).append(paramInt);
        ((StringBuilder)localObject1).append(" when initializing Visualizer.");
        Log.e("Visualizer-JAVA", ((StringBuilder)localObject1).toString());
        if (paramInt != -5)
        {
          localObject4 = new java/lang/RuntimeException;
          localObject1 = new java/lang/StringBuilder;
          ((StringBuilder)localObject1).<init>();
          ((StringBuilder)localObject1).append("Cannot initialize Visualizer engine, error: ");
          ((StringBuilder)localObject1).append(paramInt);
          ((RuntimeException)localObject4).<init>(((StringBuilder)localObject1).toString());
          throw ((Throwable)localObject4);
        }
        localObject1 = new java/lang/UnsupportedOperationException;
        ((UnsupportedOperationException)localObject1).<init>("Effect library not loaded");
        throw ((Throwable)localObject1);
      }
      mId = localObject1[0];
      if (native_getEnabled()) {
        mState = 2;
      } else {
        mState = 1;
      }
      return;
    }
  }
  
  public static native int[] getCaptureSizeRange();
  
  public static native int getMaxCaptureRate();
  
  private final native void native_finalize();
  
  private final native int native_getCaptureSize();
  
  private final native boolean native_getEnabled();
  
  private final native int native_getFft(byte[] paramArrayOfByte);
  
  private final native int native_getMeasurementMode();
  
  private final native int native_getPeakRms(MeasurementPeakRms paramMeasurementPeakRms);
  
  private final native int native_getSamplingRate();
  
  private final native int native_getScalingMode();
  
  private final native int native_getWaveForm(byte[] paramArrayOfByte);
  
  private static final native void native_init();
  
  private final native void native_release();
  
  private final native int native_setCaptureSize(int paramInt);
  
  private final native int native_setEnabled(boolean paramBoolean);
  
  private final native int native_setMeasurementMode(int paramInt);
  
  private final native int native_setPeriodicCapture(int paramInt, boolean paramBoolean1, boolean paramBoolean2);
  
  private final native int native_setScalingMode(int paramInt);
  
  private final native int native_setup(Object paramObject, int paramInt, int[] paramArrayOfInt, String paramString);
  
  private static void postEventFromNative(Object paramObject1, int paramInt1, int paramInt2, int paramInt3, Object paramObject2)
  {
    paramObject1 = (Visualizer)((WeakReference)paramObject1).get();
    if (paramObject1 == null) {
      return;
    }
    if (mNativeEventHandler != null)
    {
      paramObject2 = mNativeEventHandler.obtainMessage(paramInt1, paramInt2, paramInt3, paramObject2);
      mNativeEventHandler.sendMessage(paramObject2);
    }
  }
  
  protected void finalize()
  {
    native_finalize();
  }
  
  public int getCaptureSize()
    throws IllegalStateException
  {
    synchronized (mStateLock)
    {
      if (mState != 0)
      {
        int i = native_getCaptureSize();
        return i;
      }
      IllegalStateException localIllegalStateException = new java/lang/IllegalStateException;
      StringBuilder localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append("getCaptureSize() called in wrong state: ");
      localStringBuilder.append(mState);
      localIllegalStateException.<init>(localStringBuilder.toString());
      throw localIllegalStateException;
    }
  }
  
  public boolean getEnabled()
  {
    synchronized (mStateLock)
    {
      if (mState != 0)
      {
        boolean bool = native_getEnabled();
        return bool;
      }
      IllegalStateException localIllegalStateException = new java/lang/IllegalStateException;
      StringBuilder localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append("getEnabled() called in wrong state: ");
      localStringBuilder.append(mState);
      localIllegalStateException.<init>(localStringBuilder.toString());
      throw localIllegalStateException;
    }
  }
  
  public int getFft(byte[] paramArrayOfByte)
    throws IllegalStateException
  {
    synchronized (mStateLock)
    {
      if (mState == 2)
      {
        int i = native_getFft(paramArrayOfByte);
        return i;
      }
      IllegalStateException localIllegalStateException = new java/lang/IllegalStateException;
      paramArrayOfByte = new java/lang/StringBuilder;
      paramArrayOfByte.<init>();
      paramArrayOfByte.append("getFft() called in wrong state: ");
      paramArrayOfByte.append(mState);
      localIllegalStateException.<init>(paramArrayOfByte.toString());
      throw localIllegalStateException;
    }
  }
  
  public int getMeasurementMode()
    throws IllegalStateException
  {
    synchronized (mStateLock)
    {
      if (mState != 0)
      {
        int i = native_getMeasurementMode();
        return i;
      }
      IllegalStateException localIllegalStateException = new java/lang/IllegalStateException;
      StringBuilder localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append("getMeasurementMode() called in wrong state: ");
      localStringBuilder.append(mState);
      localIllegalStateException.<init>(localStringBuilder.toString());
      throw localIllegalStateException;
    }
  }
  
  public int getMeasurementPeakRms(MeasurementPeakRms paramMeasurementPeakRms)
  {
    if (paramMeasurementPeakRms == null)
    {
      Log.e("Visualizer-JAVA", "Cannot store measurements in a null object");
      return -4;
    }
    synchronized (mStateLock)
    {
      if (mState == 2)
      {
        int i = native_getPeakRms(paramMeasurementPeakRms);
        return i;
      }
      paramMeasurementPeakRms = new java/lang/IllegalStateException;
      StringBuilder localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append("getMeasurementPeakRms() called in wrong state: ");
      localStringBuilder.append(mState);
      paramMeasurementPeakRms.<init>(localStringBuilder.toString());
      throw paramMeasurementPeakRms;
    }
  }
  
  public int getSamplingRate()
    throws IllegalStateException
  {
    synchronized (mStateLock)
    {
      if (mState != 0)
      {
        int i = native_getSamplingRate();
        return i;
      }
      IllegalStateException localIllegalStateException = new java/lang/IllegalStateException;
      StringBuilder localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append("getSamplingRate() called in wrong state: ");
      localStringBuilder.append(mState);
      localIllegalStateException.<init>(localStringBuilder.toString());
      throw localIllegalStateException;
    }
  }
  
  public int getScalingMode()
    throws IllegalStateException
  {
    synchronized (mStateLock)
    {
      if (mState != 0)
      {
        int i = native_getScalingMode();
        return i;
      }
      IllegalStateException localIllegalStateException = new java/lang/IllegalStateException;
      StringBuilder localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append("getScalingMode() called in wrong state: ");
      localStringBuilder.append(mState);
      localIllegalStateException.<init>(localStringBuilder.toString());
      throw localIllegalStateException;
    }
  }
  
  public int getWaveForm(byte[] paramArrayOfByte)
    throws IllegalStateException
  {
    synchronized (mStateLock)
    {
      if (mState == 2)
      {
        int i = native_getWaveForm(paramArrayOfByte);
        return i;
      }
      paramArrayOfByte = new java/lang/IllegalStateException;
      StringBuilder localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append("getWaveForm() called in wrong state: ");
      localStringBuilder.append(mState);
      paramArrayOfByte.<init>(localStringBuilder.toString());
      throw paramArrayOfByte;
    }
  }
  
  public void release()
  {
    synchronized (mStateLock)
    {
      native_release();
      mState = 0;
      return;
    }
  }
  
  public int setCaptureSize(int paramInt)
    throws IllegalStateException
  {
    synchronized (mStateLock)
    {
      if (mState == 1)
      {
        paramInt = native_setCaptureSize(paramInt);
        return paramInt;
      }
      IllegalStateException localIllegalStateException = new java/lang/IllegalStateException;
      StringBuilder localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append("setCaptureSize() called in wrong state: ");
      localStringBuilder.append(mState);
      localIllegalStateException.<init>(localStringBuilder.toString());
      throw localIllegalStateException;
    }
  }
  
  public int setDataCaptureListener(OnDataCaptureListener paramOnDataCaptureListener, int paramInt, boolean paramBoolean1, boolean paramBoolean2)
  {
    synchronized (mListenerLock)
    {
      mCaptureListener = paramOnDataCaptureListener;
      if (paramOnDataCaptureListener == null)
      {
        paramBoolean1 = false;
        paramBoolean2 = false;
      }
      int i = native_setPeriodicCapture(paramInt, paramBoolean1, paramBoolean2);
      paramInt = i;
      if (i == 0)
      {
        paramInt = i;
        if (paramOnDataCaptureListener != null)
        {
          paramInt = i;
          if (mNativeEventHandler == null)
          {
            paramOnDataCaptureListener = Looper.myLooper();
            if (paramOnDataCaptureListener != null)
            {
              mNativeEventHandler = new NativeEventHandler(this, paramOnDataCaptureListener);
              paramInt = i;
            }
            else
            {
              paramOnDataCaptureListener = Looper.getMainLooper();
              if (paramOnDataCaptureListener != null)
              {
                mNativeEventHandler = new NativeEventHandler(this, paramOnDataCaptureListener);
                paramInt = i;
              }
              else
              {
                mNativeEventHandler = null;
                paramInt = -3;
              }
            }
          }
        }
      }
      return paramInt;
    }
  }
  
  public int setEnabled(boolean paramBoolean)
    throws IllegalStateException
  {
    synchronized (mStateLock)
    {
      if (mState != 0)
      {
        int i = 0;
        int j = 2;
        int k;
        if ((!paramBoolean) || (mState != 1))
        {
          k = i;
          if (!paramBoolean)
          {
            k = i;
            if (mState != 2) {}
          }
        }
        else
        {
          i = native_setEnabled(paramBoolean);
          k = i;
          if (i == 0)
          {
            if (paramBoolean) {
              k = j;
            } else {
              k = 1;
            }
            mState = k;
            k = i;
          }
        }
        return k;
      }
      IllegalStateException localIllegalStateException = new java/lang/IllegalStateException;
      StringBuilder localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append("setEnabled() called in wrong state: ");
      localStringBuilder.append(mState);
      localIllegalStateException.<init>(localStringBuilder.toString());
      throw localIllegalStateException;
    }
  }
  
  public int setMeasurementMode(int paramInt)
    throws IllegalStateException
  {
    synchronized (mStateLock)
    {
      if (mState != 0)
      {
        paramInt = native_setMeasurementMode(paramInt);
        return paramInt;
      }
      IllegalStateException localIllegalStateException = new java/lang/IllegalStateException;
      StringBuilder localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append("setMeasurementMode() called in wrong state: ");
      localStringBuilder.append(mState);
      localIllegalStateException.<init>(localStringBuilder.toString());
      throw localIllegalStateException;
    }
  }
  
  public int setScalingMode(int paramInt)
    throws IllegalStateException
  {
    synchronized (mStateLock)
    {
      if (mState != 0)
      {
        paramInt = native_setScalingMode(paramInt);
        return paramInt;
      }
      IllegalStateException localIllegalStateException = new java/lang/IllegalStateException;
      StringBuilder localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append("setScalingMode() called in wrong state: ");
      localStringBuilder.append(mState);
      localIllegalStateException.<init>(localStringBuilder.toString());
      throw localIllegalStateException;
    }
  }
  
  public int setServerDiedListener(OnServerDiedListener paramOnServerDiedListener)
  {
    synchronized (mListenerLock)
    {
      mServerDiedListener = paramOnServerDiedListener;
      return 0;
    }
  }
  
  public static final class MeasurementPeakRms
  {
    public int mPeak;
    public int mRms;
    
    public MeasurementPeakRms() {}
  }
  
  private class NativeEventHandler
    extends Handler
  {
    private Visualizer mVisualizer;
    
    public NativeEventHandler(Visualizer paramVisualizer, Looper paramLooper)
    {
      super();
      mVisualizer = paramVisualizer;
    }
    
    private void handleCaptureMessage(Message paramMessage)
    {
      synchronized (mListenerLock)
      {
        Visualizer.OnDataCaptureListener localOnDataCaptureListener = mVisualizer.mCaptureListener;
        if (localOnDataCaptureListener != null)
        {
          ??? = (byte[])obj;
          int i = arg1;
          switch (what)
          {
          default: 
            ??? = new StringBuilder();
            ((StringBuilder)???).append("Unknown native event in handleCaptureMessge: ");
            ((StringBuilder)???).append(what);
            Log.e("Visualizer-JAVA", ((StringBuilder)???).toString());
            break;
          case 1: 
            localOnDataCaptureListener.onFftDataCapture(mVisualizer, (byte[])???, i);
            break;
          case 0: 
            localOnDataCaptureListener.onWaveFormDataCapture(mVisualizer, (byte[])???, i);
          }
        }
        return;
      }
    }
    
    private void handleServerDiedMessage(Message arg1)
    {
      synchronized (mListenerLock)
      {
        Visualizer.OnServerDiedListener localOnServerDiedListener = mVisualizer.mServerDiedListener;
        if (localOnServerDiedListener != null) {
          localOnServerDiedListener.onServerDied();
        }
        return;
      }
    }
    
    public void handleMessage(Message paramMessage)
    {
      if (mVisualizer == null) {
        return;
      }
      switch (what)
      {
      default: 
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Unknown native event: ");
        localStringBuilder.append(what);
        Log.e("Visualizer-JAVA", localStringBuilder.toString());
        break;
      case 2: 
        handleServerDiedMessage(paramMessage);
        break;
      case 0: 
      case 1: 
        handleCaptureMessage(paramMessage);
      }
    }
  }
  
  public static abstract interface OnDataCaptureListener
  {
    public abstract void onFftDataCapture(Visualizer paramVisualizer, byte[] paramArrayOfByte, int paramInt);
    
    public abstract void onWaveFormDataCapture(Visualizer paramVisualizer, byte[] paramArrayOfByte, int paramInt);
  }
  
  public static abstract interface OnServerDiedListener
  {
    public abstract void onServerDied();
  }
}
