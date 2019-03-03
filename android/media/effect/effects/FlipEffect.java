package android.media.effect.effects;

import android.filterpacks.imageproc.FlipFilter;
import android.media.effect.EffectContext;
import android.media.effect.SingleFilterEffect;

public class FlipEffect
  extends SingleFilterEffect
{
  public FlipEffect(EffectContext paramEffectContext, String paramString)
  {
    super(paramEffectContext, paramString, FlipFilter.class, "image", "image", new Object[0]);
  }
}
