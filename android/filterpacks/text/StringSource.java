package android.filterpacks.text;

import android.filterfw.core.Filter;
import android.filterfw.core.FilterContext;
import android.filterfw.core.Frame;
import android.filterfw.core.FrameFormat;
import android.filterfw.core.FrameManager;
import android.filterfw.core.GenerateFieldPort;
import android.filterfw.format.ObjectFormat;

public class StringSource
  extends Filter
{
  private FrameFormat mOutputFormat;
  @GenerateFieldPort(name="stringValue")
  private String mString;
  
  public StringSource(String paramString)
  {
    super(paramString);
  }
  
  public void process(FilterContext paramFilterContext)
  {
    paramFilterContext = paramFilterContext.getFrameManager().newFrame(mOutputFormat);
    paramFilterContext.setObjectValue(mString);
    paramFilterContext.setTimestamp(-1L);
    pushOutput("string", paramFilterContext);
    closeOutputPort("string");
  }
  
  public void setupPorts()
  {
    mOutputFormat = ObjectFormat.fromClass(String.class, 1);
    addOutputPort("string", mOutputFormat);
  }
}
