package android.filterpacks.base;

import android.filterfw.core.Filter;
import android.filterfw.core.FilterContext;
import android.filterfw.core.FrameFormat;
import android.filterfw.core.GenerateFieldPort;
import android.filterfw.core.GenerateFinalPort;

public class FrameFetch
  extends Filter
{
  @GenerateFinalPort(hasDefault=true, name="format")
  private FrameFormat mFormat;
  @GenerateFieldPort(name="key")
  private String mKey;
  @GenerateFieldPort(hasDefault=true, name="repeatFrame")
  private boolean mRepeatFrame = false;
  
  public FrameFetch(String paramString)
  {
    super(paramString);
  }
  
  public void process(FilterContext paramFilterContext)
  {
    paramFilterContext = paramFilterContext.fetchFrame(mKey);
    if (paramFilterContext != null)
    {
      pushOutput("frame", paramFilterContext);
      if (!mRepeatFrame) {
        closeOutputPort("frame");
      }
    }
    else
    {
      delayNextProcess(250);
    }
  }
  
  public void setupPorts()
  {
    FrameFormat localFrameFormat;
    if (mFormat == null) {
      localFrameFormat = FrameFormat.unspecified();
    } else {
      localFrameFormat = mFormat;
    }
    addOutputPort("frame", localFrameFormat);
  }
}
