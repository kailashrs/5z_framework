package android.media.effect.effects;

import android.filterfw.core.Frame;
import android.media.effect.EffectContext;
import android.media.effect.FilterEffect;

public class IdentityEffect
  extends FilterEffect
{
  public IdentityEffect(EffectContext paramEffectContext, String paramString)
  {
    super(paramEffectContext, paramString);
  }
  
  public void apply(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    beginGLEffect();
    Frame localFrame1 = frameFromTexture(paramInt1, paramInt2, paramInt3);
    Frame localFrame2 = frameFromTexture(paramInt4, paramInt2, paramInt3);
    localFrame2.setDataFromFrame(localFrame1);
    localFrame1.release();
    localFrame2.release();
    endGLEffect();
  }
  
  public void release() {}
  
  public void setParameter(String paramString, Object paramObject)
  {
    paramObject = new StringBuilder();
    paramObject.append("Unknown parameter ");
    paramObject.append(paramString);
    paramObject.append(" for IdentityEffect!");
    throw new IllegalArgumentException(paramObject.toString());
  }
}
