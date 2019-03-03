package android.media.effect.effects;

import android.filterpacks.imageproc.VignetteFilter;
import android.media.effect.EffectContext;
import android.media.effect.SingleFilterEffect;

public class VignetteEffect
  extends SingleFilterEffect
{
  public VignetteEffect(EffectContext paramEffectContext, String paramString)
  {
    super(paramEffectContext, paramString, VignetteFilter.class, "image", "image", new Object[0]);
  }
}
