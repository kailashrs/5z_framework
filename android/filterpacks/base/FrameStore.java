package android.filterpacks.base;

import android.filterfw.core.Filter;
import android.filterfw.core.FilterContext;
import android.filterfw.core.Frame;
import android.filterfw.core.GenerateFieldPort;

public class FrameStore
  extends Filter
{
  @GenerateFieldPort(name="key")
  private String mKey;
  
  public FrameStore(String paramString)
  {
    super(paramString);
  }
  
  public void process(FilterContext paramFilterContext)
  {
    Frame localFrame = pullInput("frame");
    paramFilterContext.storeFrame(mKey, localFrame);
  }
  
  public void setupPorts()
  {
    addInputPort("frame");
  }
}
