package android.media.effect.effects;

import android.filterpacks.imageproc.NegativeFilter;
import android.media.effect.EffectContext;
import android.media.effect.SingleFilterEffect;

public class NegativeEffect
  extends SingleFilterEffect
{
  public NegativeEffect(EffectContext paramEffectContext, String paramString)
  {
    super(paramEffectContext, paramString, NegativeFilter.class, "image", "image", new Object[0]);
  }
}
