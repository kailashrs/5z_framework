package android.media.audiofx;

import java.util.StringTokenizer;

public class EnvironmentalReverb
  extends AudioEffect
{
  public static final int PARAM_DECAY_HF_RATIO = 3;
  public static final int PARAM_DECAY_TIME = 2;
  public static final int PARAM_DENSITY = 9;
  public static final int PARAM_DIFFUSION = 8;
  private static final int PARAM_PROPERTIES = 10;
  public static final int PARAM_REFLECTIONS_DELAY = 5;
  public static final int PARAM_REFLECTIONS_LEVEL = 4;
  public static final int PARAM_REVERB_DELAY = 7;
  public static final int PARAM_REVERB_LEVEL = 6;
  public static final int PARAM_ROOM_HF_LEVEL = 1;
  public static final int PARAM_ROOM_LEVEL = 0;
  private static int PROPERTY_SIZE = 26;
  private static final String TAG = "EnvironmentalReverb";
  private BaseParameterListener mBaseParamListener = null;
  private OnParameterChangeListener mParamListener = null;
  private final Object mParamListenerLock = new Object();
  
  public EnvironmentalReverb(int paramInt1, int paramInt2)
    throws IllegalArgumentException, UnsupportedOperationException, RuntimeException
  {
    super(EFFECT_TYPE_ENV_REVERB, EFFECT_TYPE_NULL, paramInt1, paramInt2);
  }
  
  public short getDecayHFRatio()
    throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException
  {
    byte[] arrayOfByte = new byte[2];
    checkStatus(getParameter(3, arrayOfByte));
    return byteArrayToShort(arrayOfByte);
  }
  
  public int getDecayTime()
    throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException
  {
    byte[] arrayOfByte = new byte[4];
    checkStatus(getParameter(2, arrayOfByte));
    return byteArrayToInt(arrayOfByte);
  }
  
  public short getDensity()
    throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException
  {
    byte[] arrayOfByte = new byte[2];
    checkStatus(getParameter(9, arrayOfByte));
    return byteArrayToShort(arrayOfByte);
  }
  
  public short getDiffusion()
    throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException
  {
    byte[] arrayOfByte = new byte[2];
    checkStatus(getParameter(8, arrayOfByte));
    return byteArrayToShort(arrayOfByte);
  }
  
  public Settings getProperties()
    throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException
  {
    byte[] arrayOfByte = new byte[PROPERTY_SIZE];
    checkStatus(getParameter(10, arrayOfByte));
    Settings localSettings = new Settings();
    roomLevel = byteArrayToShort(arrayOfByte, 0);
    roomHFLevel = byteArrayToShort(arrayOfByte, 2);
    decayTime = byteArrayToInt(arrayOfByte, 4);
    decayHFRatio = byteArrayToShort(arrayOfByte, 8);
    reflectionsLevel = byteArrayToShort(arrayOfByte, 10);
    reflectionsDelay = byteArrayToInt(arrayOfByte, 12);
    reverbLevel = byteArrayToShort(arrayOfByte, 16);
    reverbDelay = byteArrayToInt(arrayOfByte, 18);
    diffusion = byteArrayToShort(arrayOfByte, 22);
    density = byteArrayToShort(arrayOfByte, 24);
    return localSettings;
  }
  
  public int getReflectionsDelay()
    throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException
  {
    byte[] arrayOfByte = new byte[4];
    checkStatus(getParameter(5, arrayOfByte));
    return byteArrayToInt(arrayOfByte);
  }
  
  public short getReflectionsLevel()
    throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException
  {
    byte[] arrayOfByte = new byte[2];
    checkStatus(getParameter(4, arrayOfByte));
    return byteArrayToShort(arrayOfByte);
  }
  
  public int getReverbDelay()
    throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException
  {
    byte[] arrayOfByte = new byte[4];
    checkStatus(getParameter(7, arrayOfByte));
    return byteArrayToInt(arrayOfByte);
  }
  
  public short getReverbLevel()
    throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException
  {
    byte[] arrayOfByte = new byte[2];
    checkStatus(getParameter(6, arrayOfByte));
    return byteArrayToShort(arrayOfByte);
  }
  
  public short getRoomHFLevel()
    throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException
  {
    byte[] arrayOfByte = new byte[2];
    checkStatus(getParameter(1, arrayOfByte));
    return byteArrayToShort(arrayOfByte);
  }
  
  public short getRoomLevel()
    throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException
  {
    byte[] arrayOfByte = new byte[2];
    checkStatus(getParameter(0, arrayOfByte));
    return byteArrayToShort(arrayOfByte);
  }
  
  public void setDecayHFRatio(short paramShort)
    throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException
  {
    checkStatus(setParameter(3, shortToByteArray(paramShort)));
  }
  
  public void setDecayTime(int paramInt)
    throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException
  {
    checkStatus(setParameter(2, intToByteArray(paramInt)));
  }
  
  public void setDensity(short paramShort)
    throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException
  {
    checkStatus(setParameter(9, shortToByteArray(paramShort)));
  }
  
  public void setDiffusion(short paramShort)
    throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException
  {
    checkStatus(setParameter(8, shortToByteArray(paramShort)));
  }
  
  public void setParameterListener(OnParameterChangeListener paramOnParameterChangeListener)
  {
    synchronized (mParamListenerLock)
    {
      if (mParamListener == null)
      {
        mParamListener = paramOnParameterChangeListener;
        paramOnParameterChangeListener = new android/media/audiofx/EnvironmentalReverb$BaseParameterListener;
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
    checkStatus(setParameter(10, concatArrays(new byte[][] { shortToByteArray(roomLevel), shortToByteArray(roomHFLevel), intToByteArray(decayTime), shortToByteArray(decayHFRatio), shortToByteArray(reflectionsLevel), intToByteArray(reflectionsDelay), shortToByteArray(reverbLevel), intToByteArray(reverbDelay), shortToByteArray(diffusion), shortToByteArray(density) })));
  }
  
  public void setReflectionsDelay(int paramInt)
    throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException
  {
    checkStatus(setParameter(5, intToByteArray(paramInt)));
  }
  
  public void setReflectionsLevel(short paramShort)
    throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException
  {
    checkStatus(setParameter(4, shortToByteArray(paramShort)));
  }
  
  public void setReverbDelay(int paramInt)
    throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException
  {
    checkStatus(setParameter(7, intToByteArray(paramInt)));
  }
  
  public void setReverbLevel(short paramShort)
    throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException
  {
    checkStatus(setParameter(6, shortToByteArray(paramShort)));
  }
  
  public void setRoomHFLevel(short paramShort)
    throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException
  {
    checkStatus(setParameter(1, shortToByteArray(paramShort)));
  }
  
  public void setRoomLevel(short paramShort)
    throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException
  {
    checkStatus(setParameter(0, shortToByteArray(paramShort)));
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
          if (paramArrayOfByte1.length == 4) {
            i = AudioEffect.byteArrayToInt(paramArrayOfByte1, 0);
          }
          if (paramArrayOfByte2.length == 2) {
            j = AudioEffect.byteArrayToShort(paramArrayOfByte2, 0);
          } else if (paramArrayOfByte2.length == 4) {
            j = AudioEffect.byteArrayToInt(paramArrayOfByte2, 0);
          }
          if ((i != -1) && (j != -1)) {
            paramAudioEffect.onParameterChange(EnvironmentalReverb.this, paramInt, i, j);
          }
        }
        return;
      }
    }
  }
  
  public static abstract interface OnParameterChangeListener
  {
    public abstract void onParameterChange(EnvironmentalReverb paramEnvironmentalReverb, int paramInt1, int paramInt2, int paramInt3);
  }
  
  public static class Settings
  {
    public short decayHFRatio;
    public int decayTime;
    public short density;
    public short diffusion;
    public int reflectionsDelay;
    public short reflectionsLevel;
    public int reverbDelay;
    public short reverbLevel;
    public short roomHFLevel;
    public short roomLevel;
    
    public Settings() {}
    
    public Settings(String paramString)
    {
      Object localObject1 = new StringTokenizer(paramString, "=;");
      ((StringTokenizer)localObject1).countTokens();
      if (((StringTokenizer)localObject1).countTokens() == 21)
      {
        paramString = ((StringTokenizer)localObject1).nextToken();
        if (paramString.equals("EnvironmentalReverb")) {
          try
          {
            String str = ((StringTokenizer)localObject1).nextToken();
            paramString = str;
            if (str.equals("roomLevel"))
            {
              paramString = str;
              roomLevel = Short.parseShort(((StringTokenizer)localObject1).nextToken());
              paramString = str;
              str = ((StringTokenizer)localObject1).nextToken();
              paramString = str;
              if (str.equals("roomHFLevel"))
              {
                paramString = str;
                roomHFLevel = Short.parseShort(((StringTokenizer)localObject1).nextToken());
                paramString = str;
                str = ((StringTokenizer)localObject1).nextToken();
                paramString = str;
                if (str.equals("decayTime"))
                {
                  paramString = str;
                  decayTime = Integer.parseInt(((StringTokenizer)localObject1).nextToken());
                  paramString = str;
                  str = ((StringTokenizer)localObject1).nextToken();
                  paramString = str;
                  if (str.equals("decayHFRatio"))
                  {
                    paramString = str;
                    decayHFRatio = Short.parseShort(((StringTokenizer)localObject1).nextToken());
                    paramString = str;
                    str = ((StringTokenizer)localObject1).nextToken();
                    paramString = str;
                    if (str.equals("reflectionsLevel"))
                    {
                      paramString = str;
                      reflectionsLevel = Short.parseShort(((StringTokenizer)localObject1).nextToken());
                      paramString = str;
                      str = ((StringTokenizer)localObject1).nextToken();
                      paramString = str;
                      if (str.equals("reflectionsDelay"))
                      {
                        paramString = str;
                        reflectionsDelay = Integer.parseInt(((StringTokenizer)localObject1).nextToken());
                        paramString = str;
                        str = ((StringTokenizer)localObject1).nextToken();
                        paramString = str;
                        if (str.equals("reverbLevel"))
                        {
                          paramString = str;
                          reverbLevel = Short.parseShort(((StringTokenizer)localObject1).nextToken());
                          paramString = str;
                          str = ((StringTokenizer)localObject1).nextToken();
                          paramString = str;
                          if (str.equals("reverbDelay"))
                          {
                            paramString = str;
                            reverbDelay = Integer.parseInt(((StringTokenizer)localObject1).nextToken());
                            paramString = str;
                            str = ((StringTokenizer)localObject1).nextToken();
                            paramString = str;
                            if (str.equals("diffusion"))
                            {
                              paramString = str;
                              diffusion = Short.parseShort(((StringTokenizer)localObject1).nextToken());
                              paramString = str;
                              str = ((StringTokenizer)localObject1).nextToken();
                              paramString = str;
                              if (str.equals("density"))
                              {
                                paramString = str;
                                density = Short.parseShort(((StringTokenizer)localObject1).nextToken());
                                return;
                              }
                              paramString = str;
                              localObject2 = new java/lang/IllegalArgumentException;
                              paramString = str;
                              localObject1 = new java/lang/StringBuilder;
                              paramString = str;
                              ((StringBuilder)localObject1).<init>();
                              paramString = str;
                              ((StringBuilder)localObject1).append("invalid key name: ");
                              paramString = str;
                              ((StringBuilder)localObject1).append(str);
                              paramString = str;
                              ((IllegalArgumentException)localObject2).<init>(((StringBuilder)localObject1).toString());
                              paramString = str;
                              throw ((Throwable)localObject2);
                            }
                            paramString = str;
                            localObject2 = new java/lang/IllegalArgumentException;
                            paramString = str;
                            localObject1 = new java/lang/StringBuilder;
                            paramString = str;
                            ((StringBuilder)localObject1).<init>();
                            paramString = str;
                            ((StringBuilder)localObject1).append("invalid key name: ");
                            paramString = str;
                            ((StringBuilder)localObject1).append(str);
                            paramString = str;
                            ((IllegalArgumentException)localObject2).<init>(((StringBuilder)localObject1).toString());
                            paramString = str;
                            throw ((Throwable)localObject2);
                          }
                          paramString = str;
                          localObject1 = new java/lang/IllegalArgumentException;
                          paramString = str;
                          localObject2 = new java/lang/StringBuilder;
                          paramString = str;
                          ((StringBuilder)localObject2).<init>();
                          paramString = str;
                          ((StringBuilder)localObject2).append("invalid key name: ");
                          paramString = str;
                          ((StringBuilder)localObject2).append(str);
                          paramString = str;
                          ((IllegalArgumentException)localObject1).<init>(((StringBuilder)localObject2).toString());
                          paramString = str;
                          throw ((Throwable)localObject1);
                        }
                        paramString = str;
                        localObject2 = new java/lang/IllegalArgumentException;
                        paramString = str;
                        localObject1 = new java/lang/StringBuilder;
                        paramString = str;
                        ((StringBuilder)localObject1).<init>();
                        paramString = str;
                        ((StringBuilder)localObject1).append("invalid key name: ");
                        paramString = str;
                        ((StringBuilder)localObject1).append(str);
                        paramString = str;
                        ((IllegalArgumentException)localObject2).<init>(((StringBuilder)localObject1).toString());
                        paramString = str;
                        throw ((Throwable)localObject2);
                      }
                      paramString = str;
                      localObject1 = new java/lang/IllegalArgumentException;
                      paramString = str;
                      localObject2 = new java/lang/StringBuilder;
                      paramString = str;
                      ((StringBuilder)localObject2).<init>();
                      paramString = str;
                      ((StringBuilder)localObject2).append("invalid key name: ");
                      paramString = str;
                      ((StringBuilder)localObject2).append(str);
                      paramString = str;
                      ((IllegalArgumentException)localObject1).<init>(((StringBuilder)localObject2).toString());
                      paramString = str;
                      throw ((Throwable)localObject1);
                    }
                    paramString = str;
                    localObject1 = new java/lang/IllegalArgumentException;
                    paramString = str;
                    localObject2 = new java/lang/StringBuilder;
                    paramString = str;
                    ((StringBuilder)localObject2).<init>();
                    paramString = str;
                    ((StringBuilder)localObject2).append("invalid key name: ");
                    paramString = str;
                    ((StringBuilder)localObject2).append(str);
                    paramString = str;
                    ((IllegalArgumentException)localObject1).<init>(((StringBuilder)localObject2).toString());
                    paramString = str;
                    throw ((Throwable)localObject1);
                  }
                  paramString = str;
                  localObject1 = new java/lang/IllegalArgumentException;
                  paramString = str;
                  localObject2 = new java/lang/StringBuilder;
                  paramString = str;
                  ((StringBuilder)localObject2).<init>();
                  paramString = str;
                  ((StringBuilder)localObject2).append("invalid key name: ");
                  paramString = str;
                  ((StringBuilder)localObject2).append(str);
                  paramString = str;
                  ((IllegalArgumentException)localObject1).<init>(((StringBuilder)localObject2).toString());
                  paramString = str;
                  throw ((Throwable)localObject1);
                }
                paramString = str;
                localObject2 = new java/lang/IllegalArgumentException;
                paramString = str;
                localObject1 = new java/lang/StringBuilder;
                paramString = str;
                ((StringBuilder)localObject1).<init>();
                paramString = str;
                ((StringBuilder)localObject1).append("invalid key name: ");
                paramString = str;
                ((StringBuilder)localObject1).append(str);
                paramString = str;
                ((IllegalArgumentException)localObject2).<init>(((StringBuilder)localObject1).toString());
                paramString = str;
                throw ((Throwable)localObject2);
              }
              paramString = str;
              localObject2 = new java/lang/IllegalArgumentException;
              paramString = str;
              localObject1 = new java/lang/StringBuilder;
              paramString = str;
              ((StringBuilder)localObject1).<init>();
              paramString = str;
              ((StringBuilder)localObject1).append("invalid key name: ");
              paramString = str;
              ((StringBuilder)localObject1).append(str);
              paramString = str;
              ((IllegalArgumentException)localObject2).<init>(((StringBuilder)localObject1).toString());
              paramString = str;
              throw ((Throwable)localObject2);
            }
            paramString = str;
            localObject1 = new java/lang/IllegalArgumentException;
            paramString = str;
            Object localObject2 = new java/lang/StringBuilder;
            paramString = str;
            ((StringBuilder)localObject2).<init>();
            paramString = str;
            ((StringBuilder)localObject2).append("invalid key name: ");
            paramString = str;
            ((StringBuilder)localObject2).append(str);
            paramString = str;
            ((IllegalArgumentException)localObject1).<init>(((StringBuilder)localObject2).toString());
            paramString = str;
            throw ((Throwable)localObject1);
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
        localStringBuilder.append("invalid settings for EnvironmentalReverb: ");
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
      localStringBuilder.append("EnvironmentalReverb;roomLevel=");
      localStringBuilder.append(Short.toString(roomLevel));
      localStringBuilder.append(";roomHFLevel=");
      localStringBuilder.append(Short.toString(roomHFLevel));
      localStringBuilder.append(";decayTime=");
      localStringBuilder.append(Integer.toString(decayTime));
      localStringBuilder.append(";decayHFRatio=");
      localStringBuilder.append(Short.toString(decayHFRatio));
      localStringBuilder.append(";reflectionsLevel=");
      localStringBuilder.append(Short.toString(reflectionsLevel));
      localStringBuilder.append(";reflectionsDelay=");
      localStringBuilder.append(Integer.toString(reflectionsDelay));
      localStringBuilder.append(";reverbLevel=");
      localStringBuilder.append(Short.toString(reverbLevel));
      localStringBuilder.append(";reverbDelay=");
      localStringBuilder.append(Integer.toString(reverbDelay));
      localStringBuilder.append(";diffusion=");
      localStringBuilder.append(Short.toString(diffusion));
      localStringBuilder.append(";density=");
      localStringBuilder.append(Short.toString(density));
      return new String(localStringBuilder.toString());
    }
  }
}
