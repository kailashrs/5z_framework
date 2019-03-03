package android.filterpacks.base;

import android.filterfw.core.Filter;
import android.filterfw.core.FilterContext;

public class NullFilter
  extends Filter
{
  public NullFilter(String paramString)
  {
    super(paramString);
  }
  
  public void process(FilterContext paramFilterContext)
  {
    pullInput("frame");
  }
  
  public void setupPorts()
  {
    addInputPort("frame");
  }
}
