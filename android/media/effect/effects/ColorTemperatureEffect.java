package android.media.effect.effects;

import android.filterpacks.imageproc.ColorTemperatureFilter;
import android.media.effect.EffectContext;
import android.media.effect.SingleFilterEffect;

public class ColorTemperatureEffect
  extends SingleFilterEffect
{
  public ColorTemperatureEffect(EffectContext paramEffectContext, String paramString)
  {
    super(paramEffectContext, paramString, ColorTemperatureFilter.class, "image", "image", new Object[0]);
  }
}
