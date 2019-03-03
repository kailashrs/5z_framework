package android.filterpacks.base;

import android.filterfw.core.Filter;
import android.filterfw.core.FilterContext;
import android.filterfw.core.Frame;
import android.filterfw.core.FrameFormat;
import android.filterfw.core.GenerateFieldPort;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class OutputStreamTarget
  extends Filter
{
  @GenerateFieldPort(name="stream")
  private OutputStream mOutputStream;
  
  public OutputStreamTarget(String paramString)
  {
    super(paramString);
  }
  
  public void process(FilterContext paramFilterContext)
  {
    paramFilterContext = pullInput("data");
    if (paramFilterContext.getFormat().getObjectClass() == String.class) {
      paramFilterContext = ByteBuffer.wrap(((String)paramFilterContext.getObjectValue()).getBytes());
    } else {
      paramFilterContext = paramFilterContext.getData();
    }
    try
    {
      mOutputStream.write(paramFilterContext.array(), 0, paramFilterContext.limit());
      mOutputStream.flush();
      return;
    }
    catch (IOException paramFilterContext)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("OutputStreamTarget: Could not write to stream: ");
      localStringBuilder.append(paramFilterContext.getMessage());
      localStringBuilder.append("!");
      throw new RuntimeException(localStringBuilder.toString());
    }
  }
  
  public void setupPorts()
  {
    addInputPort("data");
  }
}
