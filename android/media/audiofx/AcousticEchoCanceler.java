package android.media.audiofx;

import android.util.Log;

public class AcousticEchoCanceler
  extends AudioEffect
{
  private static final String TAG = "AcousticEchoCanceler";
  
  private AcousticEchoCanceler(int paramInt)
    throws IllegalArgumentException, UnsupportedOperationException, RuntimeException
  {
    super(EFFECT_TYPE_AEC, EFFECT_TYPE_NULL, 0, paramInt);
  }
  
  public static AcousticEchoCanceler create(int paramInt)
  {
    Object localObject1 = null;
    Object localObject2 = null;
    Object localObject3;
    try
    {
      AcousticEchoCanceler localAcousticEchoCanceler = new android/media/audiofx/AcousticEchoCanceler;
      localAcousticEchoCanceler.<init>(paramInt);
      localObject2 = localAcousticEchoCanceler;
    }
    catch (RuntimeException localRuntimeException)
    {
      Log.w("AcousticEchoCanceler", "not enough memory");
      localObject3 = localObject1;
    }
    catch (UnsupportedOperationException localUnsupportedOperationException)
    {
      for (;;)
      {
        Log.w("AcousticEchoCanceler", "not enough resources");
      }
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
      for (;;)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("not implemented on this device");
        localStringBuilder.append(null);
        Log.w("AcousticEchoCanceler", localStringBuilder.toString());
      }
    }
    return localObject3;
  }
  
  public static boolean isAvailable()
  {
    return AudioEffect.isEffectTypeAvailable(AudioEffect.EFFECT_TYPE_AEC);
  }
}
