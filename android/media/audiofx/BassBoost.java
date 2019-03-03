package android.media.audiofx;

import android.util.Log;
import java.util.StringTokenizer;

public class BassBoost
  extends AudioEffect
{
  public static final int PARAM_STRENGTH = 1;
  public static final int PARAM_STRENGTH_SUPPORTED = 0;
  private static final String TAG = "BassBoost";
  private BaseParameterListener mBaseParamListener;
  private OnParameterChangeListener mParamListener;
  private final Object mParamListenerLock;
  private boolean mStrengthSupported;
  
  public BassBoost(int paramInt1, int paramInt2)
    throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException, RuntimeException
  {
    super(EFFECT_TYPE_BASS_BOOST, EFFECT_TYPE_NULL, paramInt1, paramInt2);
    boolean bool = false;
    mStrengthSupported = false;
    mParamListener = null;
    mBaseParamListener = null;
    mParamListenerLock = new Object();
    if (paramInt2 == 0) {
      Log.w("BassBoost", "WARNING: attaching a BassBoost to global output mix is deprecated!");
    }
    int[] arrayOfInt = new int[1];
    checkStatus(getParameter(0, arrayOfInt));
    if (arrayOfInt[0] != 0) {
      bool = true;
    }
    mStrengthSupported = bool;
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
  
  public boolean getStrengthSupported()
  {
    return mStrengthSupported;
  }
  
  public void setParameterListener(OnParameterChangeListener paramOnParameterChangeListener)
  {
    synchronized (mParamListenerLock)
    {
      if (mParamListener == null)
      {
        mParamListener = paramOnParameterChangeListener;
        paramOnParameterChangeListener = new android/media/audiofx/BassBoost$BaseParameterListener;
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
            paramAudioEffect.onParameterChange(BassBoost.this, paramInt, i, s2);
          }
        }
        return;
      }
    }
  }
  
  public static abstract interface OnParameterChangeListener
  {
    public abstract void onParameterChange(BassBoost paramBassBoost, int paramInt1, int paramInt2, short paramShort);
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
        if (paramString.equals("BassBoost")) {
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
        localStringBuilder1.append("invalid settings for BassBoost: ");
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
      localStringBuilder.append("BassBoost;strength=");
      localStringBuilder.append(Short.toString(strength));
      return new String(localStringBuilder.toString());
    }
  }
}
