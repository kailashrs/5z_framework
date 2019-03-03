package android.media.effect;

import android.filterfw.core.Filter;
import android.filterfw.core.FilterFactory;
import android.filterfw.core.FilterFunction;
import android.filterfw.core.Frame;

public class SingleFilterEffect
  extends FilterEffect
{
  protected FilterFunction mFunction;
  protected String mInputName;
  protected String mOutputName;
  
  public SingleFilterEffect(EffectContext paramEffectContext, String paramString1, Class paramClass, String paramString2, String paramString3, Object... paramVarArgs)
  {
    super(paramEffectContext, paramString1);
    mInputName = paramString2;
    mOutputName = paramString3;
    paramEffectContext = paramClass.getSimpleName();
    paramEffectContext = FilterFactory.sharedFactory().createFilterByClass(paramClass, paramEffectContext);
    paramEffectContext.initWithAssignmentList(paramVarArgs);
    mFunction = new FilterFunction(getFilterContext(), paramEffectContext);
  }
  
  public void apply(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    beginGLEffect();
    Frame localFrame1 = frameFromTexture(paramInt1, paramInt2, paramInt3);
    Frame localFrame2 = frameFromTexture(paramInt4, paramInt2, paramInt3);
    Frame localFrame3 = mFunction.executeWithArgList(new Object[] { mInputName, localFrame1 });
    localFrame2.setDataFromFrame(localFrame3);
    localFrame1.release();
    localFrame2.release();
    localFrame3.release();
    endGLEffect();
  }
  
  public void release()
  {
    mFunction.tearDown();
    mFunction = null;
  }
  
  public void setParameter(String paramString, Object paramObject)
  {
    mFunction.setInputValue(paramString, paramObject);
  }
}
