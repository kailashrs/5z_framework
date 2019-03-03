package android.media.effect.effects;

import android.filterpacks.imageproc.SepiaFilter;
import android.media.effect.EffectContext;
import android.media.effect.SingleFilterEffect;

public class SepiaEffect
  extends SingleFilterEffect
{
  public SepiaEffect(EffectContext paramEffectContext, String paramString)
  {
    super(paramEffectContext, paramString, SepiaFilter.class, "image", "image", new Object[0]);
  }
}
