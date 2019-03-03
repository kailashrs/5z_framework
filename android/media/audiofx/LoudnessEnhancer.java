package android.media.audiofx;

import android.util.Log;
import java.util.StringTokenizer;

public class LoudnessEnhancer
  extends AudioEffect
{
  public static final int PARAM_TARGET_GAIN_MB = 0;
  private static final String TAG = "LoudnessEnhancer";
  private BaseParameterListener mBaseParamListener = null;
  private OnParameterChangeListener mParamListener = null;
  private final Object mParamListenerLock = new Object();
  
  public LoudnessEnhancer(int paramInt)
    throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException, RuntimeException
  {
    super(EFFECT_TYPE_LOUDNESS_ENHANCER, EFFECT_TYPE_NULL, 0, paramInt);
    if (paramInt == 0) {
      Log.w("LoudnessEnhancer", "WARNING: attaching a LoudnessEnhancer to global output mix is deprecated!");
    }
  }
  
  public LoudnessEnhancer(int paramInt1, int paramInt2)
    throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException, RuntimeException
  {
    super(EFFECT_TYPE_LOUDNESS_ENHANCER, EFFECT_TYPE_NULL, paramInt1, paramInt2);
    if (paramInt2 == 0) {
      Log.w("LoudnessEnhancer", "WARNING: attaching a LoudnessEnhancer to global output mix is deprecated!");
    }
  }
  
  public Settings getProperties()
    throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException
  {
    Settings localSettings = new Settings();
    int[] arrayOfInt = new int[1];
    checkStatus(getParameter(0, arrayOfInt));
    targetGainmB = arrayOfInt[0];
    return localSettings;
  }
  
  public float getTargetGain()
    throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException
  {
    int[] arrayOfInt = new int[1];
    checkStatus(getParameter(0, arrayOfInt));
    return arrayOfInt[0];
  }
  
  public void setParameterListener(OnParameterChangeListener paramOnParameterChangeListener)
  {
    synchronized (mParamListenerLock)
    {
      if (mParamListener == null)
      {
        BaseParameterListener localBaseParameterListener = new android/media/audiofx/LoudnessEnhancer$BaseParameterListener;
        localBaseParameterListener.<init>(this, null);
        mBaseParamListener = localBaseParameterListener;
        super.setParameterListener(mBaseParamListener);
      }
      mParamListener = paramOnParameterChangeListener;
      return;
    }
  }
  
  public void setProperties(Settings paramSettings)
    throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException
  {
    checkStatus(setParameter(0, targetGainmB));
  }
  
  public void setTargetGain(int paramInt)
    throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException
  {
    checkStatus(setParameter(0, paramInt));
  }
  
  private class BaseParameterListener
    implements AudioEffect.OnParameterChangeListener
  {
    private BaseParameterListener() {}
    
    public void onParameterChange(AudioEffect paramAudioEffect, int paramInt, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
    {
      if (paramInt != 0) {
        return;
      }
      paramAudioEffect = null;
      synchronized (mParamListenerLock)
      {
        if (mParamListener != null) {
          paramAudioEffect = mParamListener;
        }
        if (paramAudioEffect != null)
        {
          paramInt = -1;
          int i = Integer.MIN_VALUE;
          if (paramArrayOfByte1.length == 4) {
            paramInt = AudioEffect.byteArrayToInt(paramArrayOfByte1, 0);
          }
          if (paramArrayOfByte2.length == 4) {
            i = AudioEffect.byteArrayToInt(paramArrayOfByte2, 0);
          }
          if ((paramInt != -1) && (i != Integer.MIN_VALUE)) {
            paramAudioEffect.onParameterChange(LoudnessEnhancer.this, paramInt, i);
          }
        }
        return;
      }
    }
  }
  
  public static abstract interface OnParameterChangeListener
  {
    public abstract void onParameterChange(LoudnessEnhancer paramLoudnessEnhancer, int paramInt1, int paramInt2);
  }
  
  public static class Settings
  {
    public int targetGainmB;
    
    public Settings() {}
    
    public Settings(String paramString)
    {
      Object localObject = new StringTokenizer(paramString, "=;");
      if (((StringTokenizer)localObject).countTokens() == 3)
      {
        paramString = ((StringTokenizer)localObject).nextToken();
        if (paramString.equals("LoudnessEnhancer")) {
          try
          {
            String str = ((StringTokenizer)localObject).nextToken();
            paramString = str;
            if (str.equals("targetGainmB"))
            {
              paramString = str;
              targetGainmB = Integer.parseInt(((StringTokenizer)localObject).nextToken());
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
        localStringBuilder1.append("invalid settings for LoudnessEnhancer: ");
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
      localStringBuilder.append("LoudnessEnhancer;targetGainmB=");
      localStringBuilder.append(Integer.toString(targetGainmB));
      return new String(localStringBuilder.toString());
    }
  }
}
