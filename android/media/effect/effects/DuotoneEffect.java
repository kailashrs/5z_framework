package android.media.effect.effects;

import android.filterpacks.imageproc.DuotoneFilter;
import android.media.effect.EffectContext;
import android.media.effect.SingleFilterEffect;

public class DuotoneEffect
  extends SingleFilterEffect
{
  public DuotoneEffect(EffectContext paramEffectContext, String paramString)
  {
    super(paramEffectContext, paramString, DuotoneFilter.class, "image", "image", new Object[0]);
  }
}
