package android.media.effect.effects;

import android.filterpacks.imageproc.LomoishFilter;
import android.media.effect.EffectContext;
import android.media.effect.SingleFilterEffect;

public class LomoishEffect
  extends SingleFilterEffect
{
  public LomoishEffect(EffectContext paramEffectContext, String paramString)
  {
    super(paramEffectContext, paramString, LomoishFilter.class, "image", "image", new Object[0]);
  }
}
