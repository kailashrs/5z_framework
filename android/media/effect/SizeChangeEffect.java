package android.media.effect;

import android.filterfw.core.FilterFunction;
import android.filterfw.core.Frame;
import android.filterfw.core.FrameFormat;

public class SizeChangeEffect
  extends SingleFilterEffect
{
  public SizeChangeEffect(EffectContext paramEffectContext, String paramString1, Class paramClass, String paramString2, String paramString3, Object... paramVarArgs)
  {
    super(paramEffectContext, paramString1, paramClass, paramString2, paramString3, paramVarArgs);
  }
  
  public void apply(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    beginGLEffect();
    Frame localFrame1 = frameFromTexture(paramInt1, paramInt2, paramInt3);
    Frame localFrame2 = mFunction.executeWithArgList(new Object[] { mInputName, localFrame1 });
    Frame localFrame3 = frameFromTexture(paramInt4, localFrame2.getFormat().getWidth(), localFrame2.getFormat().getHeight());
    localFrame3.setDataFromFrame(localFrame2);
    localFrame1.release();
    localFrame3.release();
    localFrame2.release();
    endGLEffect();
  }
}
