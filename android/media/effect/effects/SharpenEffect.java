package android.media.effect.effects;

import android.filterpacks.imageproc.SharpenFilter;
import android.media.effect.EffectContext;
import android.media.effect.SingleFilterEffect;

public class SharpenEffect
  extends SingleFilterEffect
{
  public SharpenEffect(EffectContext paramEffectContext, String paramString)
  {
    super(paramEffectContext, paramString, SharpenFilter.class, "image", "image", new Object[0]);
  }
}
