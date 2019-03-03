package android.filterpacks.text;

import android.filterfw.core.Filter;
import android.filterfw.core.FilterContext;
import android.filterfw.core.Frame;
import android.filterfw.core.FrameFormat;
import android.filterfw.core.FrameManager;
import android.filterfw.format.ObjectFormat;
import java.util.Locale;

public class ToUpperCase
  extends Filter
{
  private FrameFormat mOutputFormat;
  
  public ToUpperCase(String paramString)
  {
    super(paramString);
  }
  
  public void process(FilterContext paramFilterContext)
  {
    String str = (String)pullInput("mixedcase").getObjectValue();
    paramFilterContext = paramFilterContext.getFrameManager().newFrame(mOutputFormat);
    paramFilterContext.setObjectValue(str.toUpperCase(Locale.getDefault()));
    pushOutput("uppercase", paramFilterContext);
  }
  
  public void setupPorts()
  {
    mOutputFormat = ObjectFormat.fromClass(String.class, 1);
    addMaskedInputPort("mixedcase", mOutputFormat);
    addOutputPort("uppercase", mOutputFormat);
  }
}
