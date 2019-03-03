package android.media.effect.effects;

import android.filterpacks.imageproc.GrainFilter;
import android.media.effect.EffectContext;
import android.media.effect.SingleFilterEffect;

public class GrainEffect
  extends SingleFilterEffect
{
  public GrainEffect(EffectContext paramEffectContext, String paramString)
  {
    super(paramEffectContext, paramString, GrainFilter.class, "image", "image", new Object[0]);
  }
}
