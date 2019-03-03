package android.media.effect.effects;

import android.filterpacks.imageproc.CropRectFilter;
import android.media.effect.EffectContext;
import android.media.effect.SizeChangeEffect;

public class CropEffect
  extends SizeChangeEffect
{
  public CropEffect(EffectContext paramEffectContext, String paramString)
  {
    super(paramEffectContext, paramString, CropRectFilter.class, "image", "image", new Object[0]);
  }
}
