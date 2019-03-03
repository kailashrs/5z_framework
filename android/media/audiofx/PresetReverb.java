package android.media.audiofx;

import java.util.StringTokenizer;

public class PresetReverb
  extends AudioEffect
{
  public static final int PARAM_PRESET = 0;
  public static final short PRESET_LARGEHALL = 5;
  public static final short PRESET_LARGEROOM = 3;
  public static final short PRESET_MEDIUMHALL = 4;
  public static final short PRESET_MEDIUMROOM = 2;
  public static final short PRESET_NONE = 0;
  public static final short PRESET_PLATE = 6;
  public static final short PRESET_SMALLROOM = 1;
  private static final String TAG = "PresetReverb";
  private BaseParameterListener mBaseParamListener = null;
  private OnParameterChangeListener mParamListener = null;
  private final Object mParamListenerLock = new Object();
  
  public PresetReverb(int paramInt1, int paramInt2)
    throws IllegalArgumentException, UnsupportedOperationException, RuntimeException
  {
    super(EFFECT_TYPE_PRESET_REVERB, EFFECT_TYPE_NULL, paramInt1, paramInt2);
  }
  
  public short getPreset()
    throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException
  {
    short[] arrayOfShort = new short[1];
    checkStatus(getParameter(0, arrayOfShort));
    return arrayOfShort[0];
  }
  
  public Settings getProperties()
    throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException
  {
    Settings localSettings = new Settings();
    short[] arrayOfShort = new short[1];
    checkStatus(getParameter(0, arrayOfShort));
    preset = ((short)arrayOfShort[0]);
    return localSettings;
  }
  
  public void setParameterListener(OnParameterChangeListener paramOnParameterChangeListener)
  {
    synchronized (mParamListenerLock)
    {
      if (mParamListener == null)
      {
        mParamListener = paramOnParameterChangeListener;
        paramOnParameterChangeListener = new android/media/audiofx/PresetReverb$BaseParameterListener;
        paramOnParameterChangeListener.<init>(this, null);
        mBaseParamListener = paramOnParameterChangeListener;
        super.setParameterListener(mBaseParamListener);
      }
      return;
    }
  }
  
  public void setPreset(short paramShort)
    throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException
  {
    checkStatus(setParameter(0, paramShort));
  }
  
  public void setProperties(Settings paramSettings)
    throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException
  {
    checkStatus(setParameter(0, preset));
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
            paramAudioEffect.onParameterChange(PresetReverb.this, paramInt, i, s2);
          }
        }
        return;
      }
    }
  }
  
  public static abstract interface OnParameterChangeListener
  {
    public abstract void onParameterChange(PresetReverb paramPresetReverb, int paramInt1, int paramInt2, short paramShort);
  }
  
  public static class Settings
  {
    public short preset;
    
    public Settings() {}
    
    public Settings(String paramString)
    {
      Object localObject = new StringTokenizer(paramString, "=;");
      ((StringTokenizer)localObject).countTokens();
      if (((StringTokenizer)localObject).countTokens() == 3)
      {
        paramString = ((StringTokenizer)localObject).nextToken();
        if (paramString.equals("PresetReverb")) {
          try
          {
            String str = ((StringTokenizer)localObject).nextToken();
            paramString = str;
            if (str.equals("preset"))
            {
              paramString = str;
              preset = Short.parseShort(((StringTokenizer)localObject).nextToken());
              return;
            }
            paramString = str;
            IllegalArgumentException localIllegalArgumentException = new java/lang/IllegalArgumentException;
            paramString = str;
            localObject = new java/lang/StringBuilder;
            paramString = str;
            ((StringBuilder)localObject).<init>();
            paramString = str;
            ((StringBuilder)localObject).append("invalid key name: ");
            paramString = str;
            ((StringBuilder)localObject).append(str);
            paramString = str;
            localIllegalArgumentException.<init>(((StringBuilder)localObject).toString());
            paramString = str;
            throw localIllegalArgumentException;
          }
          catch (NumberFormatException localNumberFormatException)
          {
            localStringBuilder = new StringBuilder();
            localStringBuilder.append("invalid value for key: ");
            localStringBuilder.append(paramString);
            throw new IllegalArgumentException(localStringBuilder.toString());
          }
        }
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("invalid settings for PresetReverb: ");
        localStringBuilder.append(paramString);
        throw new IllegalArgumentException(localStringBuilder.toString());
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("settings: ");
      localStringBuilder.append(paramString);
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("PresetReverb;preset=");
      localStringBuilder.append(Short.toString(preset));
      return new String(localStringBuilder.toString());
    }
  }
}
