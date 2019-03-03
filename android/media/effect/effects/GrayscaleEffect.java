package android.media.effect.effects;

import android.filterpacks.imageproc.ToGrayFilter;
import android.media.effect.EffectContext;
import android.media.effect.SingleFilterEffect;

public class GrayscaleEffect
  extends SingleFilterEffect
{
  public GrayscaleEffect(EffectContext paramEffectContext, String paramString)
  {
    super(paramEffectContext, paramString, ToGrayFilter.class, "image", "image", new Object[0]);
  }
}
