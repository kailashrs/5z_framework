package android.media.effect.effects;

import android.filterpacks.imageproc.CrossProcessFilter;
import android.media.effect.EffectContext;
import android.media.effect.SingleFilterEffect;

public class CrossProcessEffect
  extends SingleFilterEffect
{
  public CrossProcessEffect(EffectContext paramEffectContext, String paramString)
  {
    super(paramEffectContext, paramString, CrossProcessFilter.class, "image", "image", new Object[0]);
  }
}
