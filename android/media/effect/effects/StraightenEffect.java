package android.media.effect.effects;

import android.filterpacks.imageproc.StraightenFilter;
import android.media.effect.EffectContext;
import android.media.effect.SingleFilterEffect;

public class StraightenEffect
  extends SingleFilterEffect
{
  public StraightenEffect(EffectContext paramEffectContext, String paramString)
  {
    super(paramEffectContext, paramString, StraightenFilter.class, "image", "image", new Object[0]);
  }
}
