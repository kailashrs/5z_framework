package android.filterfw.core;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

public class FilterFunction
{
  private Filter mFilter;
  private FilterContext mFilterContext;
  private boolean mFilterIsSetup = false;
  private FrameHolderPort[] mResultHolders;
  
  public FilterFunction(FilterContext paramFilterContext, Filter paramFilter)
  {
    mFilterContext = paramFilterContext;
    mFilter = paramFilter;
  }
  
  private void connectFilterOutputs()
  {
    int i = 0;
    mResultHolders = new FrameHolderPort[mFilter.getNumberOfOutputs()];
    Iterator localIterator = mFilter.getOutputPorts().iterator();
    while (localIterator.hasNext())
    {
      OutputPort localOutputPort = (OutputPort)localIterator.next();
      mResultHolders[i] = new FrameHolderPort();
      localOutputPort.connectTo(mResultHolders[i]);
      i++;
    }
  }
  
  public void close()
  {
    mFilter.performClose(mFilterContext);
  }
  
  public Frame execute(KeyValueMap paramKeyValueMap)
  {
    int i = mFilter.getNumberOfOutputs();
    if (i <= 1)
    {
      if (!mFilterIsSetup)
      {
        connectFilterOutputs();
        mFilterIsSetup = true;
      }
      int j = 0;
      GLEnvironment localGLEnvironment = mFilterContext.getGLEnvironment();
      int k = j;
      if (localGLEnvironment != null)
      {
        k = j;
        if (!localGLEnvironment.isActive())
        {
          localGLEnvironment.activate();
          k = 1;
        }
      }
      paramKeyValueMap = paramKeyValueMap.entrySet().iterator();
      while (paramKeyValueMap.hasNext())
      {
        localEntry = (Map.Entry)paramKeyValueMap.next();
        if ((localEntry.getValue() instanceof Frame)) {
          mFilter.pushInputFrame((String)localEntry.getKey(), (Frame)localEntry.getValue());
        } else {
          mFilter.pushInputValue((String)localEntry.getKey(), localEntry.getValue());
        }
      }
      if (mFilter.getStatus() != 3) {
        mFilter.openOutputs();
      }
      mFilter.performProcess(mFilterContext);
      Map.Entry localEntry = null;
      paramKeyValueMap = localEntry;
      if (i == 1)
      {
        paramKeyValueMap = localEntry;
        if (mResultHolders[0].hasFrame()) {
          paramKeyValueMap = mResultHolders[0].pullFrame();
        }
      }
      if (k != 0) {
        localGLEnvironment.deactivate();
      }
      return paramKeyValueMap;
    }
    paramKeyValueMap = new StringBuilder();
    paramKeyValueMap.append("Calling execute on filter ");
    paramKeyValueMap.append(mFilter);
    paramKeyValueMap.append(" with multiple outputs! Use executeMulti() instead!");
    throw new RuntimeException(paramKeyValueMap.toString());
  }
  
  public Frame executeWithArgList(Object... paramVarArgs)
  {
    return execute(KeyValueMap.fromKeyValues(paramVarArgs));
  }
  
  public FilterContext getContext()
  {
    return mFilterContext;
  }
  
  public Filter getFilter()
  {
    return mFilter;
  }
  
  public void setInputFrame(String paramString, Frame paramFrame)
  {
    mFilter.setInputFrame(paramString, paramFrame);
  }
  
  public void setInputValue(String paramString, Object paramObject)
  {
    mFilter.setInputValue(paramString, paramObject);
  }
  
  public void tearDown()
  {
    mFilter.performTearDown(mFilterContext);
    mFilter = null;
  }
  
  public String toString()
  {
    return mFilter.getName();
  }
  
  private class FrameHolderPort
    extends StreamPort
  {
    public FrameHolderPort()
    {
      super("holder");
    }
  }
}
