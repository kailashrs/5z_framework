package android.filterpacks.base;

import android.filterfw.core.Filter;
import android.filterfw.core.FilterContext;
import android.filterfw.core.Frame;
import android.filterfw.core.FrameFormat;
import android.filterfw.core.FrameManager;
import android.filterfw.core.GenerateFinalPort;
import android.filterfw.core.MutableFrameFormat;

public class RetargetFilter
  extends Filter
{
  private MutableFrameFormat mOutputFormat;
  private int mTarget = -1;
  @GenerateFinalPort(hasDefault=false, name="target")
  private String mTargetString;
  
  public RetargetFilter(String paramString)
  {
    super(paramString);
  }
  
  public FrameFormat getOutputFormat(String paramString, FrameFormat paramFrameFormat)
  {
    paramString = paramFrameFormat.mutableCopy();
    paramString.setTarget(mTarget);
    return paramString;
  }
  
  public void process(FilterContext paramFilterContext)
  {
    Frame localFrame = pullInput("frame");
    paramFilterContext = paramFilterContext.getFrameManager().duplicateFrameToTarget(localFrame, mTarget);
    pushOutput("frame", paramFilterContext);
    paramFilterContext.release();
  }
  
  public void setupPorts()
  {
    mTarget = FrameFormat.readTargetString(mTargetString);
    addInputPort("frame");
    addOutputBasedOnInput("frame", "frame");
  }
}
