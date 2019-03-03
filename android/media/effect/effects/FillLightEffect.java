package android.media.effect.effects;

import android.filterpacks.imageproc.FillLightFilter;
import android.media.effect.EffectContext;
import android.media.effect.SingleFilterEffect;

public class FillLightEffect
  extends SingleFilterEffect
{
  public FillLightEffect(EffectContext paramEffectContext, String paramString)
  {
    super(paramEffectContext, paramString, FillLightFilter.class, "image", "image", new Object[0]);
  }
}
