package android.media.audiofx;

import android.media.AudioDeviceInfo;
import android.media.AudioFormat;
import android.util.Log;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.StringTokenizer;

public class Virtualizer
  extends AudioEffect
{
  private static final boolean DEBUG = false;
  public static final int PARAM_FORCE_VIRTUALIZATION_MODE = 3;
  public static final int PARAM_STRENGTH = 1;
  public static final int PARAM_STRENGTH_SUPPORTED = 0;
  public static final int PARAM_VIRTUALIZATION_MODE = 4;
  public static final int PARAM_VIRTUAL_SPEAKER_ANGLES = 2;
  private static final String TAG = "Virtualizer";
  public static final int VIRTUALIZATION_MODE_AUTO = 1;
  public static final int VIRTUALIZATION_MODE_BINAURAL = 2;
  public static final int VIRTUALIZATION_MODE_OFF = 0;
  public static final int VIRTUALIZATION_MODE_TRANSAURAL = 3;
  private BaseParameterListener mBaseParamListener;
  private OnParameterChangeListener mParamListener;
  private final Object mParamListenerLock;
  private boolean mStrengthSupported;
  
  public Virtualizer(int paramInt1, int paramInt2)
    throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException, RuntimeException
  {
    super(EFFECT_TYPE_VIRTUALIZER, EFFECT_TYPE_NULL, paramInt1, paramInt2);
    boolean bool = false;
    mStrengthSupported = false;
    mParamListener = null;
    mBaseParamListener = null;
    mParamListenerLock = new Object();
    if (paramInt2 == 0) {
      Log.w("Virtualizer", "WARNING: attaching a Virtualizer to global output mix is deprecated!");
    }
    int[] arrayOfInt = new int[1];
    checkStatus(getParameter(0, arrayOfInt));
    if (arrayOfInt[0] != 0) {
      bool = true;
    }
    mStrengthSupported = bool;
  }
  
  private static int deviceToMode(int paramInt)
  {
    if (paramInt != 19)
    {
      if (paramInt != 22) {}
      switch (paramInt)
      {
      default: 
        return 0;
      case 1: 
      case 3: 
      case 4: 
      case 7: 
        return 2;
      }
    }
    return 3;
  }
  
  private boolean getAnglesInt(int paramInt1, int paramInt2, int[] paramArrayOfInt)
    throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException
  {
    if (paramInt1 != 0)
    {
      if (paramInt1 == 1) {
        paramInt1 = 12;
      }
      int i = AudioFormat.channelCountFromOutChannelMask(paramInt1);
      if ((paramArrayOfInt != null) && (paramArrayOfInt.length < i * 3))
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("Size of array for angles cannot accomodate number of channels in mask (");
        ((StringBuilder)localObject).append(i);
        ((StringBuilder)localObject).append(")");
        Log.e("Virtualizer", ((StringBuilder)localObject).toString());
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("Virtualizer: array for channel / angle pairs is too small: is ");
        ((StringBuilder)localObject).append(paramArrayOfInt.length);
        ((StringBuilder)localObject).append(", should be ");
        ((StringBuilder)localObject).append(i * 3);
        throw new IllegalArgumentException(((StringBuilder)localObject).toString());
      }
      ByteBuffer localByteBuffer = ByteBuffer.allocate(12);
      localByteBuffer.order(ByteOrder.nativeOrder());
      localByteBuffer.putInt(2);
      localByteBuffer.putInt(AudioFormat.convertChannelOutMaskToNativeMask(paramInt1));
      localByteBuffer.putInt(AudioDeviceInfo.convertDeviceTypeToInternalDevice(paramInt2));
      Object localObject = new byte[i * 4 * 3];
      paramInt2 = getParameter(localByteBuffer.array(), (byte[])localObject);
      paramInt1 = 0;
      if (paramInt2 >= 0)
      {
        if (paramArrayOfInt != null)
        {
          localObject = ByteBuffer.wrap((byte[])localObject);
          ((ByteBuffer)localObject).order(ByteOrder.nativeOrder());
          while (paramInt1 < i)
          {
            paramArrayOfInt[(3 * paramInt1)] = AudioFormat.convertNativeChannelMaskToOutMask(((ByteBuffer)localObject).getInt(paramInt1 * 4 * 3));
            paramArrayOfInt[(3 * paramInt1 + 1)] = ((ByteBuffer)localObject).getInt(paramInt1 * 4 * 3 + 4);
            paramArrayOfInt[(3 * paramInt1 + 2)] = ((ByteBuffer)localObject).getInt(paramInt1 * 4 * 3 + 8);
            paramInt1++;
          }
        }
        return true;
      }
      if (paramInt2 == -4) {
        return false;
      }
      checkStatus(paramInt2);
      paramArrayOfInt = new StringBuilder();
      paramArrayOfInt.append("unexpected status code ");
      paramArrayOfInt.append(paramInt2);
      paramArrayOfInt.append(" after getParameter(PARAM_VIRTUAL_SPEAKER_ANGLES)");
      Log.e("Virtualizer", paramArrayOfInt.toString());
      return false;
    }
    throw new IllegalArgumentException("Virtualizer: illegal CHANNEL_INVALID channel mask");
  }
  
  private static int getDeviceForModeForce(int paramInt)
    throws IllegalArgumentException
  {
    if (paramInt == 1) {
      return 0;
    }
    return getDeviceForModeQuery(paramInt);
  }
  
  private static int getDeviceForModeQuery(int paramInt)
    throws IllegalArgumentException
  {
    switch (paramInt)
    {
    default: 
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Virtualizer: illegal virtualization mode ");
      localStringBuilder.append(paramInt);
      throw new IllegalArgumentException(localStringBuilder.toString());
    case 3: 
      return 2;
    }
    return 4;
  }
  
  public boolean canVirtualize(int paramInt1, int paramInt2)
    throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException
  {
    return getAnglesInt(paramInt1, getDeviceForModeQuery(paramInt2), null);
  }
  
  public boolean forceVirtualizationMode(int paramInt)
    throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException
  {
    paramInt = setParameter(3, AudioDeviceInfo.convertDeviceTypeToInternalDevice(getDeviceForModeForce(paramInt)));
    if (paramInt >= 0) {
      return true;
    }
    if (paramInt == -4) {
      return false;
    }
    checkStatus(paramInt);
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("unexpected status code ");
    localStringBuilder.append(paramInt);
    localStringBuilder.append(" after setParameter(PARAM_FORCE_VIRTUALIZATION_MODE)");
    Log.e("Virtualizer", localStringBuilder.toString());
    return false;
  }
  
  public Settings getProperties()
    throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException
  {
    Settings localSettings = new Settings();
    short[] arrayOfShort = new short[1];
    checkStatus(getParameter(1, arrayOfShort));
    strength = ((short)arrayOfShort[0]);
    return localSettings;
  }
  
  public short getRoundedStrength()
    throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException
  {
    short[] arrayOfShort = new short[1];
    checkStatus(getParameter(1, arrayOfShort));
    return arrayOfShort[0];
  }
  
  public boolean getSpeakerAngles(int paramInt1, int paramInt2, int[] paramArrayOfInt)
    throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException
  {
    if (paramArrayOfInt != null) {
      return getAnglesInt(paramInt1, getDeviceForModeQuery(paramInt2), paramArrayOfInt);
    }
    throw new IllegalArgumentException("Virtualizer: illegal null channel / angle array");
  }
  
  public boolean getStrengthSupported()
  {
    return mStrengthSupported;
  }
  
  public int getVirtualizationMode()
    throws IllegalStateException, UnsupportedOperationException
  {
    Object localObject = new int[1];
    int i = getParameter(4, (int[])localObject);
    if (i >= 0) {
      return deviceToMode(AudioDeviceInfo.convertInternalDeviceToDeviceType(localObject[0]));
    }
    if (i == -4) {
      return 0;
    }
    checkStatus(i);
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("unexpected status code ");
    ((StringBuilder)localObject).append(i);
    ((StringBuilder)localObject).append(" after getParameter(PARAM_VIRTUALIZATION_MODE)");
    Log.e("Virtualizer", ((StringBuilder)localObject).toString());
    return 0;
  }
  
  public void setParameterListener(OnParameterChangeListener paramOnParameterChangeListener)
  {
    synchronized (mParamListenerLock)
    {
      if (mParamListener == null)
      {
        mParamListener = paramOnParameterChangeListener;
        paramOnParameterChangeListener = new android/media/audiofx/Virtualizer$BaseParameterListener;
        paramOnParameterChangeListener.<init>(this, null);
        mBaseParamListener = paramOnParameterChangeListener;
        super.setParameterListener(mBaseParamListener);
      }
      return;
    }
  }
  
  public void setProperties(Settings paramSettings)
    throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException
  {
    checkStatus(setParameter(1, strength));
  }
  
  public void setStrength(short paramShort)
    throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException
  {
    checkStatus(setParameter(1, paramShort));
  }
  
  private class BaseParameterListener
    implements AudioEffect.OnParameterChangeListener
  {
    private BaseParameterListener() {}
    
    public void onParameterChange(AudioEffect paramAudioEffect, int paramInt, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
    {
      paramAudioEffect = null;
      synchronized (mParamListenerLock)
      {
        if (mParamListener != null) {
          paramAudioEffect = mParamListener;
        }
        if (paramAudioEffect != null)
        {
          int i = -1;
          short s1 = -1;
          if (paramArrayOfByte1.length == 4) {
            i = AudioEffect.byteArrayToInt(paramArrayOfByte1, 0);
          }
          short s2 = s1;
          if (paramArrayOfByte2.length == 2)
          {
            s1 = AudioEffect.byteArrayToShort(paramArrayOfByte2, 0);
            s2 = s1;
          }
          if ((i != -1) && (s2 != -1)) {
            paramAudioEffect.onParameterChange(Virtualizer.this, paramInt, i, s2);
          }
        }
        return;
      }
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface ForceVirtualizationMode {}
  
  public static abstract interface OnParameterChangeListener
  {
    public abstract void onParameterChange(Virtualizer paramVirtualizer, int paramInt1, int paramInt2, short paramShort);
  }
  
  public static class Settings
  {
    public short strength;
    
    public Settings() {}
    
    public Settings(String paramString)
    {
      Object localObject = new StringTokenizer(paramString, "=;");
      ((StringTokenizer)localObject).countTokens();
      if (((StringTokenizer)localObject).countTokens() == 3)
      {
        paramString = ((StringTokenizer)localObject).nextToken();
        if (paramString.equals("Virtualizer")) {
          try
          {
            String str = ((StringTokenizer)localObject).nextToken();
            paramString = str;
            if (str.equals("strength"))
            {
              paramString = str;
              strength = Short.parseShort(((StringTokenizer)localObject).nextToken());
              return;
            }
            paramString = str;
            localObject = new java/lang/IllegalArgumentException;
            paramString = str;
            StringBuilder localStringBuilder2 = new java/lang/StringBuilder;
            paramString = str;
            localStringBuilder2.<init>();
            paramString = str;
            localStringBuilder2.append("invalid key name: ");
            paramString = str;
            localStringBuilder2.append(str);
            paramString = str;
            ((IllegalArgumentException)localObject).<init>(localStringBuilder2.toString());
            paramString = str;
            throw ((Throwable)localObject);
          }
          catch (NumberFormatException localNumberFormatException)
          {
            localStringBuilder1 = new StringBuilder();
            localStringBuilder1.append("invalid value for key: ");
            localStringBuilder1.append(paramString);
            throw new IllegalArgumentException(localStringBuilder1.toString());
          }
        }
        localStringBuilder1 = new StringBuilder();
        localStringBuilder1.append("invalid settings for Virtualizer: ");
        localStringBuilder1.append(paramString);
        throw new IllegalArgumentException(localStringBuilder1.toString());
      }
      StringBuilder localStringBuilder1 = new StringBuilder();
      localStringBuilder1.append("settings: ");
      localStringBuilder1.append(paramString);
      throw new IllegalArgumentException(localStringBuilder1.toString());
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Virtualizer;strength=");
      localStringBuilder.append(Short.toString(strength));
      return new String(localStringBuilder.toString());
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface VirtualizationMode {}
}
