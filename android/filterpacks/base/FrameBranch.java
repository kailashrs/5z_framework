package android.filterpacks.base;

import android.filterfw.core.Filter;
import android.filterfw.core.FilterContext;
import android.filterfw.core.Frame;
import android.filterfw.core.FrameFormat;
import android.filterfw.core.GenerateFinalPort;

public class FrameBranch
  extends Filter
{
  @GenerateFinalPort(hasDefault=true, name="outputs")
  private int mNumberOfOutputs = 2;
  
  public FrameBranch(String paramString)
  {
    super(paramString);
  }
  
  public FrameFormat getOutputFormat(String paramString, FrameFormat paramFrameFormat)
  {
    return paramFrameFormat;
  }
  
  public void process(FilterContext paramFilterContext)
  {
    Frame localFrame = pullInput("in");
    for (int i = 0; i < mNumberOfOutputs; i++)
    {
      paramFilterContext = new StringBuilder();
      paramFilterContext.append("out");
      paramFilterContext.append(i);
      pushOutput(paramFilterContext.toString(), localFrame);
    }
  }
  
  public void setupPorts()
  {
    addInputPort("in");
    for (int i = 0; i < mNumberOfOutputs; i++)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("out");
      localStringBuilder.append(i);
      addOutputBasedOnInput(localStringBuilder.toString(), "in");
    }
  }
}
