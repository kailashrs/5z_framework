package android.media.effect.effects;

import android.filterpacks.imageproc.PosterizeFilter;
import android.media.effect.EffectContext;
import android.media.effect.SingleFilterEffect;

public class PosterizeEffect
  extends SingleFilterEffect
{
  public PosterizeEffect(EffectContext paramEffectContext, String paramString)
  {
    super(paramEffectContext, paramString, PosterizeFilter.class, "image", "image", new Object[0]);
  }
}
