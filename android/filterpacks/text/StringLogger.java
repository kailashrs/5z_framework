package android.filterpacks.text;

import android.filterfw.core.Filter;
import android.filterfw.core.FilterContext;
import android.filterfw.core.Frame;
import android.filterfw.format.ObjectFormat;
import android.util.Log;

public class StringLogger
  extends Filter
{
  public StringLogger(String paramString)
  {
    super(paramString);
  }
  
  public void process(FilterContext paramFilterContext)
  {
    Log.i("StringLogger", pullInput("string").getObjectValue().toString());
  }
  
  public void setupPorts()
  {
    addMaskedInputPort("string", ObjectFormat.fromClass(Object.class, 1));
  }
}
