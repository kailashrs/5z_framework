package android.media.effect;

import android.filterfw.core.Filter;
import android.filterfw.core.FilterGraph;
import android.filterfw.core.GraphRunner;
import android.filterfw.core.SyncRunner;
import android.filterfw.io.GraphIOException;
import android.filterfw.io.GraphReader;
import android.filterfw.io.TextGraphReader;

public class FilterGraphEffect
  extends FilterEffect
{
  private static final String TAG = "FilterGraphEffect";
  protected FilterGraph mGraph;
  protected String mInputName;
  protected String mOutputName;
  protected GraphRunner mRunner;
  protected Class mSchedulerClass;
  
  public FilterGraphEffect(EffectContext paramEffectContext, String paramString1, String paramString2, String paramString3, String paramString4, Class paramClass)
  {
    super(paramEffectContext, paramString1);
    mInputName = paramString3;
    mOutputName = paramString4;
    mSchedulerClass = paramClass;
    createGraph(paramString2);
  }
  
  private void createGraph(String paramString)
  {
    TextGraphReader localTextGraphReader = new TextGraphReader();
    try
    {
      mGraph = localTextGraphReader.readGraphString(paramString);
      if (mGraph != null)
      {
        mRunner = new SyncRunner(getFilterContext(), mGraph, mSchedulerClass);
        return;
      }
      throw new RuntimeException("Could not setup effect");
    }
    catch (GraphIOException paramString)
    {
      throw new RuntimeException("Could not setup effect", paramString);
    }
  }
  
  public void apply(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    beginGLEffect();
    Filter localFilter = mGraph.getFilter(mInputName);
    if (localFilter != null)
    {
      localFilter.setInputValue("texId", Integer.valueOf(paramInt1));
      localFilter.setInputValue("width", Integer.valueOf(paramInt2));
      localFilter.setInputValue("height", Integer.valueOf(paramInt3));
      localFilter = mGraph.getFilter(mOutputName);
      if (localFilter != null)
      {
        localFilter.setInputValue("texId", Integer.valueOf(paramInt4));
        try
        {
          mRunner.run();
          endGLEffect();
          return;
        }
        catch (RuntimeException localRuntimeException)
        {
          throw new RuntimeException("Internal error applying effect: ", localRuntimeException);
        }
      }
      throw new RuntimeException("Internal error applying effect");
    }
    throw new RuntimeException("Internal error applying effect");
  }
  
  public void release()
  {
    mGraph.tearDown(getFilterContext());
    mGraph = null;
  }
  
  public void setParameter(String paramString, Object paramObject) {}
}
