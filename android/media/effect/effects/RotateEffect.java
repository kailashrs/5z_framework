package android.media.effect.effects;

import android.filterpacks.imageproc.RotateFilter;
import android.media.effect.EffectContext;
import android.media.effect.SizeChangeEffect;

public class RotateEffect
  extends SizeChangeEffect
{
  public RotateEffect(EffectContext paramEffectContext, String paramString)
  {
    super(paramEffectContext, paramString, RotateFilter.class, "image", "image", new Object[0]);
  }
}
