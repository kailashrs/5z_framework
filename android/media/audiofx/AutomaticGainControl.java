package android.media.audiofx;

import android.util.Log;

public class AutomaticGainControl
  extends AudioEffect
{
  private static final String TAG = "AutomaticGainControl";
  
  private AutomaticGainControl(int paramInt)
    throws IllegalArgumentException, UnsupportedOperationException, RuntimeException
  {
    super(EFFECT_TYPE_AGC, EFFECT_TYPE_NULL, 0, paramInt);
  }
  
  public static AutomaticGainControl create(int paramInt)
  {
    Object localObject1 = null;
    Object localObject2 = null;
    Object localObject3;
    try
    {
      AutomaticGainControl localAutomaticGainControl = new android/media/audiofx/AutomaticGainControl;
      localAutomaticGainControl.<init>(paramInt);
      localObject2 = localAutomaticGainControl;
    }
    catch (RuntimeException localRuntimeException)
    {
      Log.w("AutomaticGainControl", "not enough memory");
      localObject3 = localObject1;
    }
    catch (UnsupportedOperationException localUnsupportedOperationException)
    {
      for (;;)
      {
        Log.w("AutomaticGainControl", "not enough resources");
      }
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
      for (;;)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("not implemented on this device ");
        localStringBuilder.append(null);
        Log.w("AutomaticGainControl", localStringBuilder.toString());
      }
    }
    return localObject3;
  }
  
  public static boolean isAvailable()
  {
    return AudioEffect.isEffectTypeAvailable(AudioEffect.EFFECT_TYPE_AGC);
  }
}
