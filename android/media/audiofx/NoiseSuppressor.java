package android.media.audiofx;

import android.util.Log;

public class NoiseSuppressor
  extends AudioEffect
{
  private static final String TAG = "NoiseSuppressor";
  
  private NoiseSuppressor(int paramInt)
    throws IllegalArgumentException, UnsupportedOperationException, RuntimeException
  {
    super(EFFECT_TYPE_NS, EFFECT_TYPE_NULL, 0, paramInt);
  }
  
  public static NoiseSuppressor create(int paramInt)
  {
    Object localObject1 = null;
    Object localObject2 = null;
    Object localObject3;
    try
    {
      NoiseSuppressor localNoiseSuppressor = new android/media/audiofx/NoiseSuppressor;
      localNoiseSuppressor.<init>(paramInt);
      localObject2 = localNoiseSuppressor;
    }
    catch (RuntimeException localRuntimeException)
    {
      Log.w("NoiseSuppressor", "not enough memory");
      localObject3 = localObject1;
    }
    catch (UnsupportedOperationException localUnsupportedOperationException)
    {
      for (;;)
      {
        Log.w("NoiseSuppressor", "not enough resources");
      }
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
      for (;;)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("not implemented on this device ");
        localStringBuilder.append(null);
        Log.w("NoiseSuppressor", localStringBuilder.toString());
      }
    }
    return localObject3;
  }
  
  public static boolean isAvailable()
  {
    return AudioEffect.isEffectTypeAvailable(AudioEffect.EFFECT_TYPE_NS);
  }
}
