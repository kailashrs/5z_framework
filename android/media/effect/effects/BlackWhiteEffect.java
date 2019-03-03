package android.media.effect.effects;

import android.filterpacks.imageproc.BlackWhiteFilter;
import android.media.effect.EffectContext;
import android.media.effect.SingleFilterEffect;

public class BlackWhiteEffect
  extends SingleFilterEffect
{
  public BlackWhiteEffect(EffectContext paramEffectContext, String paramString)
  {
    super(paramEffectContext, paramString, BlackWhiteFilter.class, "image", "image", new Object[0]);
  }
}
