package android.media.audiofx;

import android.util.Log;
import java.io.UnsupportedEncodingException;
import java.util.StringTokenizer;

public class Equalizer
  extends AudioEffect
{
  public static final int PARAM_BAND_FREQ_RANGE = 4;
  public static final int PARAM_BAND_LEVEL = 2;
  public static final int PARAM_CENTER_FREQ = 3;
  public static final int PARAM_CURRENT_PRESET = 6;
  public static final int PARAM_GET_BAND = 5;
  public static final int PARAM_GET_NUM_OF_PRESETS = 7;
  public static final int PARAM_GET_PRESET_NAME = 8;
  public static final int PARAM_LEVEL_RANGE = 1;
  public static final int PARAM_NUM_BANDS = 0;
  private static final int PARAM_PROPERTIES = 9;
  public static final int PARAM_STRING_SIZE_MAX = 32;
  private static final String TAG = "Equalizer";
  private BaseParameterListener mBaseParamListener = null;
  private short mNumBands = (short)0;
  private int mNumPresets;
  private OnParameterChangeListener mParamListener = null;
  private final Object mParamListenerLock = new Object();
  private String[] mPresetNames;
  
  public Equalizer(int paramInt1, int paramInt2)
    throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException, RuntimeException
  {
    super(EFFECT_TYPE_EQUALIZER, EFFECT_TYPE_NULL, paramInt1, paramInt2);
    if (paramInt2 == 0) {
      Log.w("Equalizer", "WARNING: attaching an Equalizer to global output mix is deprecated!");
    }
    getNumberOfBands();
    mNumPresets = getNumberOfPresets();
    if (mNumPresets != 0)
    {
      mPresetNames = new String[mNumPresets];
      byte[] arrayOfByte = new byte[32];
      int[] arrayOfInt = new int[2];
      arrayOfInt[0] = 8;
      for (paramInt1 = 0; paramInt1 < mNumPresets; paramInt1++)
      {
        arrayOfInt[1] = paramInt1;
        checkStatus(getParameter(arrayOfInt, arrayOfByte));
        for (paramInt2 = 0; arrayOfByte[paramInt2] != 0; paramInt2++) {}
        try
        {
          String[] arrayOfString = mPresetNames;
          String str = new java/lang/String;
          str.<init>(arrayOfByte, 0, paramInt2, "ISO-8859-1");
          arrayOfString[paramInt1] = str;
        }
        catch (UnsupportedEncodingException localUnsupportedEncodingException)
        {
          Log.e("Equalizer", "preset name decode error");
        }
      }
    }
  }
  
  public short getBand(int paramInt)
    throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException
  {
    short[] arrayOfShort = new short[1];
    checkStatus(getParameter(new int[] { 5, paramInt }, arrayOfShort));
    return arrayOfShort[0];
  }
  
  public int[] getBandFreqRange(short paramShort)
    throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException
  {
    int[] arrayOfInt = new int[2];
    checkStatus(getParameter(new int[] { 4, paramShort }, arrayOfInt));
    return arrayOfInt;
  }
  
  public short getBandLevel(short paramShort)
    throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException
  {
    short[] arrayOfShort = new short[1];
    checkStatus(getParameter(new int[] { 2, paramShort }, arrayOfShort));
    return arrayOfShort[0];
  }
  
  public short[] getBandLevelRange()
    throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException
  {
    short[] arrayOfShort = new short[2];
    checkStatus(getParameter(1, arrayOfShort));
    return arrayOfShort;
  }
  
  public int getCenterFreq(short paramShort)
    throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException
  {
    int[] arrayOfInt = new int[1];
    checkStatus(getParameter(new int[] { 3, paramShort }, arrayOfInt));
    return arrayOfInt[0];
  }
  
  public short getCurrentPreset()
    throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException
  {
    short[] arrayOfShort = new short[1];
    checkStatus(getParameter(6, arrayOfShort));
    return arrayOfShort[0];
  }
  
  public short getNumberOfBands()
    throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException
  {
    if (mNumBands != 0) {
      return mNumBands;
    }
    short[] arrayOfShort = new short[1];
    checkStatus(getParameter(new int[] { 0 }, arrayOfShort));
    mNumBands = ((short)arrayOfShort[0]);
    return mNumBands;
  }
  
  public short getNumberOfPresets()
    throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException
  {
    short[] arrayOfShort = new short[1];
    checkStatus(getParameter(7, arrayOfShort));
    return arrayOfShort[0];
  }
  
  public String getPresetName(short paramShort)
  {
    if ((paramShort >= 0) && (paramShort < mNumPresets)) {
      return mPresetNames[paramShort];
    }
    return "";
  }
  
  public Settings getProperties()
    throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException
  {
    byte[] arrayOfByte = new byte[mNumBands * 2 + 4];
    checkStatus(getParameter(9, arrayOfByte));
    Settings localSettings = new Settings();
    int i = 0;
    curPreset = byteArrayToShort(arrayOfByte, 0);
    numBands = byteArrayToShort(arrayOfByte, 2);
    bandLevels = new short[mNumBands];
    while (i < mNumBands)
    {
      bandLevels[i] = byteArrayToShort(arrayOfByte, 2 * i + 4);
      i++;
    }
    return localSettings;
  }
  
  public void setBandLevel(short paramShort1, short paramShort2)
    throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException
  {
    checkStatus(setParameter(new int[] { 2, paramShort1 }, new short[] { paramShort2 }));
  }
  
  public void setParameterListener(OnParameterChangeListener paramOnParameterChangeListener)
  {
    synchronized (mParamListenerLock)
    {
      if (mParamListener == null)
      {
        mParamListener = paramOnParameterChangeListener;
        paramOnParameterChangeListener = new android/media/audiofx/Equalizer$BaseParameterListener;
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
    if ((numBands == bandLevels.length) && (numBands == mNumBands))
    {
      localObject = concatArrays(new byte[][] { shortToByteArray(curPreset), shortToByteArray(mNumBands) });
      for (int i = 0; i < mNumBands; i++) {
        localObject = concatArrays(new byte[][] { localObject, shortToByteArray(bandLevels[i]) });
      }
      checkStatus(setParameter(9, (byte[])localObject));
      return;
    }
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("settings invalid band count: ");
    ((StringBuilder)localObject).append(numBands);
    throw new IllegalArgumentException(((StringBuilder)localObject).toString());
  }
  
  public void usePreset(short paramShort)
    throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException
  {
    checkStatus(setParameter(6, paramShort));
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
          int j = -1;
          int k = -1;
          int m = j;
          if (paramArrayOfByte1.length >= 4)
          {
            int n = AudioEffect.byteArrayToInt(paramArrayOfByte1, 0);
            i = n;
            m = j;
            if (paramArrayOfByte1.length >= 8)
            {
              m = AudioEffect.byteArrayToInt(paramArrayOfByte1, 4);
              i = n;
            }
          }
          if (paramArrayOfByte2.length == 2) {
            k = AudioEffect.byteArrayToShort(paramArrayOfByte2, 0);
          }
          for (;;)
          {
            break;
            if (paramArrayOfByte2.length == 4) {
              k = AudioEffect.byteArrayToInt(paramArrayOfByte2, 0);
            }
          }
          if ((i != -1) && (k != -1)) {
            paramAudioEffect.onParameterChange(Equalizer.this, paramInt, i, m, k);
          }
        }
        return;
      }
    }
  }
  
  public static abstract interface OnParameterChangeListener
  {
    public abstract void onParameterChange(Equalizer paramEqualizer, int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  }
  
  public static class Settings
  {
    public short[] bandLevels;
    public short curPreset;
    public short numBands;
    
    public Settings()
    {
      numBands = ((short)0);
      bandLevels = null;
    }
    
    public Settings(String paramString)
    {
      int i = 0;
      numBands = ((short)0);
      bandLevels = null;
      Object localObject1 = new StringTokenizer(paramString, "=;");
      ((StringTokenizer)localObject1).countTokens();
      if (((StringTokenizer)localObject1).countTokens() >= 5)
      {
        localObject2 = ((StringTokenizer)localObject1).nextToken();
        if (((String)localObject2).equals("Equalizer")) {
          try
          {
            Object localObject3 = ((StringTokenizer)localObject1).nextToken();
            localObject2 = localObject3;
            if (((String)localObject3).equals("curPreset"))
            {
              localObject2 = localObject3;
              curPreset = Short.parseShort(((StringTokenizer)localObject1).nextToken());
              localObject2 = localObject3;
              localObject3 = ((StringTokenizer)localObject1).nextToken();
              localObject2 = localObject3;
              if (((String)localObject3).equals("numBands"))
              {
                localObject2 = localObject3;
                numBands = Short.parseShort(((StringTokenizer)localObject1).nextToken());
                localObject2 = localObject3;
                if (((StringTokenizer)localObject1).countTokens() == numBands * 2)
                {
                  localObject2 = localObject3;
                  bandLevels = new short[numBands];
                  paramString = (String)localObject3;
                  for (;;)
                  {
                    localObject2 = paramString;
                    if (i >= numBands) {
                      break label330;
                    }
                    localObject2 = paramString;
                    paramString = ((StringTokenizer)localObject1).nextToken();
                    localObject2 = paramString;
                    localObject3 = new java/lang/StringBuilder;
                    localObject2 = paramString;
                    ((StringBuilder)localObject3).<init>();
                    localObject2 = paramString;
                    ((StringBuilder)localObject3).append("band");
                    localObject2 = paramString;
                    ((StringBuilder)localObject3).append(i + 1);
                    localObject2 = paramString;
                    ((StringBuilder)localObject3).append("Level");
                    localObject2 = paramString;
                    if (!paramString.equals(((StringBuilder)localObject3).toString())) {
                      break;
                    }
                    localObject2 = paramString;
                    bandLevels[i] = Short.parseShort(((StringTokenizer)localObject1).nextToken());
                    i++;
                  }
                  localObject2 = paramString;
                  localObject3 = new java/lang/IllegalArgumentException;
                  localObject2 = paramString;
                  localObject1 = new java/lang/StringBuilder;
                  localObject2 = paramString;
                  ((StringBuilder)localObject1).<init>();
                  localObject2 = paramString;
                  ((StringBuilder)localObject1).append("invalid key name: ");
                  localObject2 = paramString;
                  ((StringBuilder)localObject1).append(paramString);
                  localObject2 = paramString;
                  ((IllegalArgumentException)localObject3).<init>(((StringBuilder)localObject1).toString());
                  localObject2 = paramString;
                  throw ((Throwable)localObject3);
                  label330:
                  return;
                }
                localObject2 = localObject3;
                IllegalArgumentException localIllegalArgumentException = new java/lang/IllegalArgumentException;
                localObject2 = localObject3;
                localObject1 = new java/lang/StringBuilder;
                localObject2 = localObject3;
                ((StringBuilder)localObject1).<init>();
                localObject2 = localObject3;
                ((StringBuilder)localObject1).append("settings: ");
                localObject2 = localObject3;
                ((StringBuilder)localObject1).append(paramString);
                localObject2 = localObject3;
                localIllegalArgumentException.<init>(((StringBuilder)localObject1).toString());
                localObject2 = localObject3;
                throw localIllegalArgumentException;
              }
              localObject2 = localObject3;
              paramString = new java/lang/IllegalArgumentException;
              localObject2 = localObject3;
              localObject1 = new java/lang/StringBuilder;
              localObject2 = localObject3;
              ((StringBuilder)localObject1).<init>();
              localObject2 = localObject3;
              ((StringBuilder)localObject1).append("invalid key name: ");
              localObject2 = localObject3;
              ((StringBuilder)localObject1).append((String)localObject3);
              localObject2 = localObject3;
              paramString.<init>(((StringBuilder)localObject1).toString());
              localObject2 = localObject3;
              throw paramString;
            }
            localObject2 = localObject3;
            paramString = new java/lang/IllegalArgumentException;
            localObject2 = localObject3;
            localObject1 = new java/lang/StringBuilder;
            localObject2 = localObject3;
            ((StringBuilder)localObject1).<init>();
            localObject2 = localObject3;
            ((StringBuilder)localObject1).append("invalid key name: ");
            localObject2 = localObject3;
            ((StringBuilder)localObject1).append((String)localObject3);
            localObject2 = localObject3;
            paramString.<init>(((StringBuilder)localObject1).toString());
            localObject2 = localObject3;
            throw paramString;
          }
          catch (NumberFormatException paramString)
          {
            paramString = new StringBuilder();
            paramString.append("invalid value for key: ");
            paramString.append((String)localObject2);
            throw new IllegalArgumentException(paramString.toString());
          }
        }
        paramString = new StringBuilder();
        paramString.append("invalid settings for Equalizer: ");
        paramString.append((String)localObject2);
        throw new IllegalArgumentException(paramString.toString());
      }
      Object localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append("settings: ");
      ((StringBuilder)localObject2).append(paramString);
      throw new IllegalArgumentException(((StringBuilder)localObject2).toString());
    }
    
    public String toString()
    {
      Object localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Equalizer;curPreset=");
      ((StringBuilder)localObject).append(Short.toString(curPreset));
      ((StringBuilder)localObject).append(";numBands=");
      ((StringBuilder)localObject).append(Short.toString(numBands));
      localObject = new String(((StringBuilder)localObject).toString());
      for (int i = 0; i < numBands; i++)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append(";band");
        localStringBuilder.append(i + 1);
        localStringBuilder.append("Level=");
        localStringBuilder.append(Short.toString(bandLevels[i]));
        localObject = ((String)localObject).concat(localStringBuilder.toString());
      }
      return localObject;
    }
  }
}
