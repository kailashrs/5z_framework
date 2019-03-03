package android.media.audiofx;

import android.app.ActivityThread;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.UUID;

public class AudioEffect
{
  public static final String ACTION_CLOSE_AUDIO_EFFECT_CONTROL_SESSION = "android.media.action.CLOSE_AUDIO_EFFECT_CONTROL_SESSION";
  public static final String ACTION_DISPLAY_AUDIO_EFFECT_CONTROL_PANEL = "android.media.action.DISPLAY_AUDIO_EFFECT_CONTROL_PANEL";
  public static final String ACTION_OPEN_AUDIO_EFFECT_CONTROL_SESSION = "android.media.action.OPEN_AUDIO_EFFECT_CONTROL_SESSION";
  public static final int ALREADY_EXISTS = -2;
  public static final int CONTENT_TYPE_GAME = 2;
  public static final int CONTENT_TYPE_MOVIE = 1;
  public static final int CONTENT_TYPE_MUSIC = 0;
  public static final int CONTENT_TYPE_VOICE = 3;
  public static final String EFFECT_AUXILIARY = "Auxiliary";
  public static final String EFFECT_INSERT = "Insert";
  public static final String EFFECT_PRE_PROCESSING = "Pre Processing";
  public static final UUID EFFECT_TYPE_AEC;
  public static final UUID EFFECT_TYPE_AGC;
  public static final UUID EFFECT_TYPE_BASS_BOOST;
  public static final UUID EFFECT_TYPE_DYNAMICS_PROCESSING = UUID.fromString("7261676f-6d75-7369-6364-28e2fd3ac39e");
  public static final UUID EFFECT_TYPE_ENV_REVERB;
  public static final UUID EFFECT_TYPE_EQUALIZER;
  public static final UUID EFFECT_TYPE_LOUDNESS_ENHANCER;
  public static final UUID EFFECT_TYPE_NS;
  public static final UUID EFFECT_TYPE_NULL = UUID.fromString("ec7178ec-e5e1-4432-a3f4-4657e6795210");
  public static final UUID EFFECT_TYPE_PRESET_REVERB;
  public static final UUID EFFECT_TYPE_VIRTUALIZER;
  public static final int ERROR = -1;
  public static final int ERROR_BAD_VALUE = -4;
  public static final int ERROR_DEAD_OBJECT = -7;
  public static final int ERROR_INVALID_OPERATION = -5;
  public static final int ERROR_NO_INIT = -3;
  public static final int ERROR_NO_MEMORY = -6;
  public static final String EXTRA_AUDIO_SESSION = "android.media.extra.AUDIO_SESSION";
  public static final String EXTRA_CONTENT_TYPE = "android.media.extra.CONTENT_TYPE";
  public static final String EXTRA_PACKAGE_NAME = "android.media.extra.PACKAGE_NAME";
  public static final int NATIVE_EVENT_CONTROL_STATUS = 0;
  public static final int NATIVE_EVENT_ENABLED_STATUS = 1;
  public static final int NATIVE_EVENT_PARAMETER_CHANGED = 2;
  public static final int STATE_INITIALIZED = 1;
  public static final int STATE_UNINITIALIZED = 0;
  public static final int SUCCESS = 0;
  private static final String TAG = "AudioEffect-JAVA";
  private OnControlStatusChangeListener mControlChangeStatusListener = null;
  private Descriptor mDescriptor;
  private OnEnableStatusChangeListener mEnableStatusChangeListener = null;
  private int mId;
  private long mJniData;
  public final Object mListenerLock = new Object();
  private long mNativeAudioEffect;
  public NativeEventHandler mNativeEventHandler = null;
  private OnParameterChangeListener mParameterChangeListener = null;
  private int mState = 0;
  private final Object mStateLock = new Object();
  
  static
  {
    System.loadLibrary("audioeffect_jni");
    native_init();
    EFFECT_TYPE_ENV_REVERB = UUID.fromString("c2e5d5f0-94bd-4763-9cac-4e234d06839e");
    EFFECT_TYPE_PRESET_REVERB = UUID.fromString("47382d60-ddd8-11db-bf3a-0002a5d5c51b");
    EFFECT_TYPE_EQUALIZER = UUID.fromString("0bed4300-ddd6-11db-8f34-0002a5d5c51b");
    EFFECT_TYPE_BASS_BOOST = UUID.fromString("0634f220-ddd4-11db-a0fc-0002a5d5c51b");
    EFFECT_TYPE_VIRTUALIZER = UUID.fromString("37cc2c00-dddd-11db-8577-0002a5d5c51b");
    EFFECT_TYPE_AGC = UUID.fromString("0a8abfe0-654c-11e0-ba26-0002a5d5c51b");
    EFFECT_TYPE_AEC = UUID.fromString("7b491460-8d4d-11e0-bd61-0002a5d5c51b");
    EFFECT_TYPE_NS = UUID.fromString("58b4b260-8e06-11e0-aa8e-0002a5d5c51b");
    EFFECT_TYPE_LOUDNESS_ENHANCER = UUID.fromString("fe3199be-aed0-413f-87bb-11260eb63cf1");
  }
  
  public AudioEffect(UUID arg1, UUID paramUUID2, int paramInt1, int paramInt2)
    throws IllegalArgumentException, UnsupportedOperationException, RuntimeException
  {
    int[] arrayOfInt = new int[1];
    Descriptor[] arrayOfDescriptor = new Descriptor[1];
    paramInt1 = native_setup(new WeakReference(this), ???.toString(), paramUUID2.toString(), paramInt1, paramInt2, arrayOfInt, arrayOfDescriptor, ActivityThread.currentOpPackageName());
    if ((paramInt1 != 0) && (paramInt1 != -2))
    {
      paramUUID2 = new StringBuilder();
      paramUUID2.append("Error code ");
      paramUUID2.append(paramInt1);
      paramUUID2.append(" when initializing AudioEffect.");
      Log.e("AudioEffect-JAVA", paramUUID2.toString());
      switch (paramInt1)
      {
      default: 
        paramUUID2 = new StringBuilder();
        paramUUID2.append("Cannot initialize effect engine for type: ");
        paramUUID2.append(???);
        paramUUID2.append(" Error: ");
        paramUUID2.append(paramInt1);
        throw new RuntimeException(paramUUID2.toString());
      case -4: 
        paramUUID2 = new StringBuilder();
        paramUUID2.append("Effect type: ");
        paramUUID2.append(???);
        paramUUID2.append(" not supported.");
        throw new IllegalArgumentException(paramUUID2.toString());
      }
      throw new UnsupportedOperationException("Effect library not loaded");
    }
    mId = arrayOfInt[0];
    mDescriptor = arrayOfDescriptor[0];
    synchronized (mStateLock)
    {
      mState = 1;
      return;
    }
  }
  
  public static float byteArrayToFloat(byte[] paramArrayOfByte)
  {
    return byteArrayToFloat(paramArrayOfByte, 0);
  }
  
  public static float byteArrayToFloat(byte[] paramArrayOfByte, int paramInt)
  {
    paramArrayOfByte = ByteBuffer.wrap(paramArrayOfByte);
    paramArrayOfByte.order(ByteOrder.nativeOrder());
    return paramArrayOfByte.getFloat(paramInt);
  }
  
  public static int byteArrayToInt(byte[] paramArrayOfByte)
  {
    return byteArrayToInt(paramArrayOfByte, 0);
  }
  
  public static int byteArrayToInt(byte[] paramArrayOfByte, int paramInt)
  {
    paramArrayOfByte = ByteBuffer.wrap(paramArrayOfByte);
    paramArrayOfByte.order(ByteOrder.nativeOrder());
    return paramArrayOfByte.getInt(paramInt);
  }
  
  public static short byteArrayToShort(byte[] paramArrayOfByte)
  {
    return byteArrayToShort(paramArrayOfByte, 0);
  }
  
  public static short byteArrayToShort(byte[] paramArrayOfByte, int paramInt)
  {
    paramArrayOfByte = ByteBuffer.wrap(paramArrayOfByte);
    paramArrayOfByte.order(ByteOrder.nativeOrder());
    return paramArrayOfByte.getShort(paramInt);
  }
  
  public static byte[] concatArrays(byte[]... paramVarArgs)
  {
    int i = paramVarArgs.length;
    int j = 0;
    for (int k = 0; k < i; k++) {
      j += paramVarArgs[k].length;
    }
    byte[] arrayOfByte1 = new byte[j];
    i = paramVarArgs.length;
    k = 0;
    for (j = 0; j < i; j++)
    {
      byte[] arrayOfByte2 = paramVarArgs[j];
      System.arraycopy(arrayOfByte2, 0, arrayOfByte1, k, arrayOfByte2.length);
      k += arrayOfByte2.length;
    }
    return arrayOfByte1;
  }
  
  private void createNativeEventHandler()
  {
    Looper localLooper = Looper.myLooper();
    if (localLooper != null)
    {
      mNativeEventHandler = new NativeEventHandler(this, localLooper);
    }
    else
    {
      localLooper = Looper.getMainLooper();
      if (localLooper != null) {
        mNativeEventHandler = new NativeEventHandler(this, localLooper);
      } else {
        mNativeEventHandler = null;
      }
    }
  }
  
  public static byte[] floatToByteArray(float paramFloat)
  {
    ByteBuffer localByteBuffer = ByteBuffer.allocate(4);
    localByteBuffer.order(ByteOrder.nativeOrder());
    localByteBuffer.putFloat(paramFloat);
    return localByteBuffer.array();
  }
  
  public static byte[] intToByteArray(int paramInt)
  {
    ByteBuffer localByteBuffer = ByteBuffer.allocate(4);
    localByteBuffer.order(ByteOrder.nativeOrder());
    localByteBuffer.putInt(paramInt);
    return localByteBuffer.array();
  }
  
  public static boolean isEffectTypeAvailable(UUID paramUUID)
  {
    Descriptor[] arrayOfDescriptor = queryEffects();
    if (arrayOfDescriptor == null) {
      return false;
    }
    for (int i = 0; i < arrayOfDescriptor.length; i++) {
      if (type.equals(paramUUID)) {
        return true;
      }
    }
    return false;
  }
  
  public static boolean isError(int paramInt)
  {
    boolean bool;
    if (paramInt < 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private final native int native_command(int paramInt1, int paramInt2, byte[] paramArrayOfByte1, int paramInt3, byte[] paramArrayOfByte2);
  
  private final native void native_finalize();
  
  private final native boolean native_getEnabled();
  
  private final native int native_getParameter(int paramInt1, byte[] paramArrayOfByte1, int paramInt2, byte[] paramArrayOfByte2);
  
  private final native boolean native_hasControl();
  
  private static final native void native_init();
  
  private static native Object[] native_query_effects();
  
  private static native Object[] native_query_pre_processing(int paramInt);
  
  private final native void native_release();
  
  private final native int native_setEnabled(boolean paramBoolean);
  
  private final native int native_setParameter(int paramInt1, byte[] paramArrayOfByte1, int paramInt2, byte[] paramArrayOfByte2);
  
  private final native int native_setup(Object paramObject, String paramString1, String paramString2, int paramInt1, int paramInt2, int[] paramArrayOfInt, Object[] paramArrayOfObject, String paramString3);
  
  private static void postEventFromNative(Object paramObject1, int paramInt1, int paramInt2, int paramInt3, Object paramObject2)
  {
    paramObject1 = (AudioEffect)((WeakReference)paramObject1).get();
    if (paramObject1 == null) {
      return;
    }
    if (mNativeEventHandler != null)
    {
      paramObject2 = mNativeEventHandler.obtainMessage(paramInt1, paramInt2, paramInt3, paramObject2);
      mNativeEventHandler.sendMessage(paramObject2);
    }
  }
  
  public static Descriptor[] queryEffects()
  {
    return (Descriptor[])native_query_effects();
  }
  
  public static Descriptor[] queryPreProcessings(int paramInt)
  {
    return (Descriptor[])native_query_pre_processing(paramInt);
  }
  
  public static byte[] shortToByteArray(short paramShort)
  {
    ByteBuffer localByteBuffer = ByteBuffer.allocate(2);
    localByteBuffer.order(ByteOrder.nativeOrder());
    localByteBuffer.putShort(paramShort);
    return localByteBuffer.array();
  }
  
  public void checkState(String paramString)
    throws IllegalStateException
  {
    synchronized (mStateLock)
    {
      if (mState == 1) {
        return;
      }
      IllegalStateException localIllegalStateException = new java/lang/IllegalStateException;
      StringBuilder localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append(paramString);
      localStringBuilder.append(" called on uninitialized AudioEffect.");
      localIllegalStateException.<init>(localStringBuilder.toString());
      throw localIllegalStateException;
    }
  }
  
  public void checkStatus(int paramInt)
  {
    if (isError(paramInt))
    {
      switch (paramInt)
      {
      default: 
        throw new RuntimeException("AudioEffect: set/get parameter error");
      case -4: 
        throw new IllegalArgumentException("AudioEffect: bad parameter value");
      }
      throw new UnsupportedOperationException("AudioEffect: invalid parameter operation");
    }
  }
  
  public int command(int paramInt, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
    throws IllegalStateException
  {
    checkState("command()");
    return native_command(paramInt, paramArrayOfByte1.length, paramArrayOfByte1, paramArrayOfByte2.length, paramArrayOfByte2);
  }
  
  protected void finalize()
  {
    native_finalize();
  }
  
  public Descriptor getDescriptor()
    throws IllegalStateException
  {
    checkState("getDescriptor()");
    return mDescriptor;
  }
  
  public boolean getEnabled()
    throws IllegalStateException
  {
    checkState("getEnabled()");
    return native_getEnabled();
  }
  
  public int getId()
    throws IllegalStateException
  {
    checkState("getId()");
    return mId;
  }
  
  public int getParameter(int paramInt, byte[] paramArrayOfByte)
    throws IllegalStateException
  {
    return getParameter(intToByteArray(paramInt), paramArrayOfByte);
  }
  
  public int getParameter(int paramInt, int[] paramArrayOfInt)
    throws IllegalStateException
  {
    if (paramArrayOfInt.length > 2) {
      return -4;
    }
    byte[] arrayOfByte1 = intToByteArray(paramInt);
    byte[] arrayOfByte2 = new byte[paramArrayOfInt.length * 4];
    paramInt = getParameter(arrayOfByte1, arrayOfByte2);
    if ((paramInt != 4) && (paramInt != 8)) {
      paramInt = -1;
    }
    for (;;)
    {
      break;
      paramArrayOfInt[0] = byteArrayToInt(arrayOfByte2);
      if (paramInt == 8) {
        paramArrayOfInt[1] = byteArrayToInt(arrayOfByte2, 4);
      }
      paramInt /= 4;
    }
    return paramInt;
  }
  
  public int getParameter(int paramInt, short[] paramArrayOfShort)
    throws IllegalStateException
  {
    if (paramArrayOfShort.length > 2) {
      return -4;
    }
    byte[] arrayOfByte1 = intToByteArray(paramInt);
    byte[] arrayOfByte2 = new byte[paramArrayOfShort.length * 2];
    paramInt = getParameter(arrayOfByte1, arrayOfByte2);
    if ((paramInt != 2) && (paramInt != 4)) {
      paramInt = -1;
    }
    for (;;)
    {
      break;
      paramArrayOfShort[0] = byteArrayToShort(arrayOfByte2);
      if (paramInt == 4) {
        paramArrayOfShort[1] = byteArrayToShort(arrayOfByte2, 2);
      }
      paramInt /= 2;
    }
    return paramInt;
  }
  
  public int getParameter(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
    throws IllegalStateException
  {
    checkState("getParameter()");
    return native_getParameter(paramArrayOfByte1.length, paramArrayOfByte1, paramArrayOfByte2.length, paramArrayOfByte2);
  }
  
  public int getParameter(int[] paramArrayOfInt, byte[] paramArrayOfByte)
    throws IllegalStateException
  {
    if (paramArrayOfInt.length > 2) {
      return -4;
    }
    byte[] arrayOfByte1 = intToByteArray(paramArrayOfInt[0]);
    byte[] arrayOfByte2 = arrayOfByte1;
    if (paramArrayOfInt.length > 1) {
      arrayOfByte2 = concatArrays(new byte[][] { arrayOfByte1, intToByteArray(paramArrayOfInt[1]) });
    }
    return getParameter(arrayOfByte2, paramArrayOfByte);
  }
  
  public int getParameter(int[] paramArrayOfInt1, int[] paramArrayOfInt2)
    throws IllegalStateException
  {
    if ((paramArrayOfInt1.length <= 2) && (paramArrayOfInt2.length <= 2))
    {
      byte[] arrayOfByte1 = intToByteArray(paramArrayOfInt1[0]);
      byte[] arrayOfByte2 = arrayOfByte1;
      if (paramArrayOfInt1.length > 1) {
        arrayOfByte2 = concatArrays(new byte[][] { arrayOfByte1, intToByteArray(paramArrayOfInt1[1]) });
      }
      paramArrayOfInt1 = new byte[paramArrayOfInt2.length * 4];
      int i = getParameter(arrayOfByte2, paramArrayOfInt1);
      if ((i != 4) && (i != 8)) {
        i = -1;
      }
      for (;;)
      {
        break;
        paramArrayOfInt2[0] = byteArrayToInt(paramArrayOfInt1);
        if (i == 8) {
          paramArrayOfInt2[1] = byteArrayToInt(paramArrayOfInt1, 4);
        }
        i /= 4;
      }
      return i;
    }
    return -4;
  }
  
  public int getParameter(int[] paramArrayOfInt, short[] paramArrayOfShort)
    throws IllegalStateException
  {
    if ((paramArrayOfInt.length <= 2) && (paramArrayOfShort.length <= 2))
    {
      byte[] arrayOfByte1 = intToByteArray(paramArrayOfInt[0]);
      byte[] arrayOfByte2 = arrayOfByte1;
      if (paramArrayOfInt.length > 1) {
        arrayOfByte2 = concatArrays(new byte[][] { arrayOfByte1, intToByteArray(paramArrayOfInt[1]) });
      }
      paramArrayOfInt = new byte[paramArrayOfShort.length * 2];
      int i = getParameter(arrayOfByte2, paramArrayOfInt);
      if ((i != 2) && (i != 4)) {
        i = -1;
      }
      for (;;)
      {
        break;
        paramArrayOfShort[0] = byteArrayToShort(paramArrayOfInt);
        if (i == 4) {
          paramArrayOfShort[1] = byteArrayToShort(paramArrayOfInt, 2);
        }
        i /= 2;
      }
      return i;
    }
    return -4;
  }
  
  public boolean hasControl()
    throws IllegalStateException
  {
    checkState("hasControl()");
    return native_hasControl();
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
  
  public void setControlStatusListener(OnControlStatusChangeListener paramOnControlStatusChangeListener)
  {
    synchronized (mListenerLock)
    {
      mControlChangeStatusListener = paramOnControlStatusChangeListener;
      if ((paramOnControlStatusChangeListener != null) && (mNativeEventHandler == null)) {
        createNativeEventHandler();
      }
      return;
    }
  }
  
  public void setEnableStatusListener(OnEnableStatusChangeListener paramOnEnableStatusChangeListener)
  {
    synchronized (mListenerLock)
    {
      mEnableStatusChangeListener = paramOnEnableStatusChangeListener;
      if ((paramOnEnableStatusChangeListener != null) && (mNativeEventHandler == null)) {
        createNativeEventHandler();
      }
      return;
    }
  }
  
  public int setEnabled(boolean paramBoolean)
    throws IllegalStateException
  {
    checkState("setEnabled()");
    return native_setEnabled(paramBoolean);
  }
  
  public int setParameter(int paramInt1, int paramInt2)
    throws IllegalStateException
  {
    return setParameter(intToByteArray(paramInt1), intToByteArray(paramInt2));
  }
  
  public int setParameter(int paramInt, short paramShort)
    throws IllegalStateException
  {
    return setParameter(intToByteArray(paramInt), shortToByteArray(paramShort));
  }
  
  public int setParameter(int paramInt, byte[] paramArrayOfByte)
    throws IllegalStateException
  {
    return setParameter(intToByteArray(paramInt), paramArrayOfByte);
  }
  
  public int setParameter(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
    throws IllegalStateException
  {
    checkState("setParameter()");
    return native_setParameter(paramArrayOfByte1.length, paramArrayOfByte1, paramArrayOfByte2.length, paramArrayOfByte2);
  }
  
  public int setParameter(int[] paramArrayOfInt, byte[] paramArrayOfByte)
    throws IllegalStateException
  {
    if (paramArrayOfInt.length > 2) {
      return -4;
    }
    byte[] arrayOfByte1 = intToByteArray(paramArrayOfInt[0]);
    byte[] arrayOfByte2 = arrayOfByte1;
    if (paramArrayOfInt.length > 1) {
      arrayOfByte2 = concatArrays(new byte[][] { arrayOfByte1, intToByteArray(paramArrayOfInt[1]) });
    }
    return setParameter(arrayOfByte2, paramArrayOfByte);
  }
  
  public int setParameter(int[] paramArrayOfInt1, int[] paramArrayOfInt2)
    throws IllegalStateException
  {
    if ((paramArrayOfInt1.length <= 2) && (paramArrayOfInt2.length <= 2))
    {
      byte[] arrayOfByte1 = intToByteArray(paramArrayOfInt1[0]);
      byte[] arrayOfByte2 = arrayOfByte1;
      if (paramArrayOfInt1.length > 1) {
        arrayOfByte2 = concatArrays(new byte[][] { arrayOfByte1, intToByteArray(paramArrayOfInt1[1]) });
      }
      arrayOfByte1 = intToByteArray(paramArrayOfInt2[0]);
      paramArrayOfInt1 = arrayOfByte1;
      if (paramArrayOfInt2.length > 1) {
        paramArrayOfInt1 = concatArrays(new byte[][] { arrayOfByte1, intToByteArray(paramArrayOfInt2[1]) });
      }
      return setParameter(arrayOfByte2, paramArrayOfInt1);
    }
    return -4;
  }
  
  public int setParameter(int[] paramArrayOfInt, short[] paramArrayOfShort)
    throws IllegalStateException
  {
    if ((paramArrayOfInt.length <= 2) && (paramArrayOfShort.length <= 2))
    {
      byte[] arrayOfByte1 = intToByteArray(paramArrayOfInt[0]);
      byte[] arrayOfByte2 = arrayOfByte1;
      if (paramArrayOfInt.length > 1) {
        arrayOfByte2 = concatArrays(new byte[][] { arrayOfByte1, intToByteArray(paramArrayOfInt[1]) });
      }
      arrayOfByte1 = shortToByteArray(paramArrayOfShort[0]);
      paramArrayOfInt = arrayOfByte1;
      if (paramArrayOfShort.length > 1) {
        paramArrayOfInt = concatArrays(new byte[][] { arrayOfByte1, shortToByteArray(paramArrayOfShort[1]) });
      }
      return setParameter(arrayOfByte2, paramArrayOfInt);
    }
    return -4;
  }
  
  public void setParameterListener(OnParameterChangeListener paramOnParameterChangeListener)
  {
    synchronized (mListenerLock)
    {
      mParameterChangeListener = paramOnParameterChangeListener;
      if ((paramOnParameterChangeListener != null) && (mNativeEventHandler == null)) {
        createNativeEventHandler();
      }
      return;
    }
  }
  
  public static class Descriptor
  {
    public String connectMode;
    public String implementor;
    public String name;
    public UUID type;
    public UUID uuid;
    
    public Descriptor() {}
    
    public Descriptor(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5)
    {
      type = UUID.fromString(paramString1);
      uuid = UUID.fromString(paramString2);
      connectMode = paramString3;
      name = paramString4;
      implementor = paramString5;
    }
  }
  
  private class NativeEventHandler
    extends Handler
  {
    private AudioEffect mAudioEffect;
    
    public NativeEventHandler(AudioEffect paramAudioEffect, Looper paramLooper)
    {
      super();
      mAudioEffect = paramAudioEffect;
    }
    
    public void handleMessage(Message paramMessage)
    {
      if (mAudioEffect == null) {
        return;
      }
      int i = what;
      boolean bool1 = true;
      boolean bool2 = true;
      switch (i)
      {
      default: 
        ??? = new StringBuilder();
        ((StringBuilder)???).append("handleMessage() Unknown event type: ");
        ((StringBuilder)???).append(what);
        Log.e("AudioEffect-JAVA", ((StringBuilder)???).toString());
        break;
      case 2: 
        synchronized (mListenerLock)
        {
          ??? = mAudioEffect.mParameterChangeListener;
          if (??? != null)
          {
            int j = arg1;
            byte[] arrayOfByte = (byte[])obj;
            int k = AudioEffect.byteArrayToInt(arrayOfByte, 0);
            i = AudioEffect.byteArrayToInt(arrayOfByte, 4);
            int m = AudioEffect.byteArrayToInt(arrayOfByte, 8);
            paramMessage = new byte[i];
            ??? = new byte[m];
            System.arraycopy(arrayOfByte, 12, paramMessage, 0, i);
            System.arraycopy(arrayOfByte, j, (byte[])???, 0, m);
            ((AudioEffect.OnParameterChangeListener)???).onParameterChange(mAudioEffect, k, paramMessage, (byte[])???);
          }
        }
      case 1: 
        synchronized (mListenerLock)
        {
          ??? = mAudioEffect.mEnableStatusChangeListener;
          if (??? != null)
          {
            ??? = mAudioEffect;
            if (arg1 == 0) {
              bool2 = false;
            }
            ((AudioEffect.OnEnableStatusChangeListener)???).onEnableStatusChange((AudioEffect)???, bool2);
          }
        }
      case 0: 
        synchronized (mListenerLock)
        {
          ??? = mAudioEffect.mControlChangeStatusListener;
          if (??? != null)
          {
            ??? = mAudioEffect;
            if (arg1 != 0) {
              bool2 = bool1;
            } else {
              bool2 = false;
            }
            ((AudioEffect.OnControlStatusChangeListener)???).onControlStatusChange((AudioEffect)???, bool2);
          }
        }
      }
    }
  }
  
  public static abstract interface OnControlStatusChangeListener
  {
    public abstract void onControlStatusChange(AudioEffect paramAudioEffect, boolean paramBoolean);
  }
  
  public static abstract interface OnEnableStatusChangeListener
  {
    public abstract void onEnableStatusChange(AudioEffect paramAudioEffect, boolean paramBoolean);
  }
  
  public static abstract interface OnParameterChangeListener
  {
    public abstract void onParameterChange(AudioEffect paramAudioEffect, int paramInt, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2);
  }
}
