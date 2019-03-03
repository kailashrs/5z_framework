package android.media.effect;

import android.filterfw.core.FilterContext;
import android.filterfw.core.Frame;
import android.filterfw.core.FrameManager;
import android.filterfw.format.ImageFormat;

public abstract class FilterEffect
  extends Effect
{
  protected EffectContext mEffectContext;
  private String mName;
  
  protected FilterEffect(EffectContext paramEffectContext, String paramString)
  {
    mEffectContext = paramEffectContext;
    mName = paramString;
  }
  
  protected void beginGLEffect()
  {
    mEffectContext.assertValidGLState();
    mEffectContext.saveGLState();
  }
  
  protected void endGLEffect()
  {
    mEffectContext.restoreGLState();
  }
  
  protected Frame frameFromTexture(int paramInt1, int paramInt2, int paramInt3)
  {
    Frame localFrame = getFilterContext().getFrameManager().newBoundFrame(ImageFormat.create(paramInt2, paramInt3, 3, 3), 100, paramInt1);
    localFrame.setTimestamp(-1L);
    return localFrame;
  }
  
  protected FilterContext getFilterContext()
  {
    return mEffectContext.mFilterContext;
  }
  
  public String getName()
  {
    return mName;
  }
}
