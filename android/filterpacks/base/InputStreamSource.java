package android.filterpacks.base;

import android.filterfw.core.Filter;
import android.filterfw.core.FilterContext;
import android.filterfw.core.Frame;
import android.filterfw.core.FrameFormat;
import android.filterfw.core.FrameManager;
import android.filterfw.core.GenerateFieldPort;
import android.filterfw.core.GenerateFinalPort;
import android.filterfw.core.MutableFrameFormat;
import android.filterfw.format.PrimitiveFormat;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class InputStreamSource
  extends Filter
{
  @GenerateFieldPort(name="stream")
  private InputStream mInputStream;
  @GenerateFinalPort(hasDefault=true, name="format")
  private MutableFrameFormat mOutputFormat = null;
  @GenerateFinalPort(name="target")
  private String mTarget;
  
  public InputStreamSource(String paramString)
  {
    super(paramString);
  }
  
  public void process(FilterContext paramFilterContext)
  {
    int i = 0;
    try
    {
      localObject = new java/io/ByteArrayOutputStream;
      ((ByteArrayOutputStream)localObject).<init>();
      byte[] arrayOfByte = new byte['Ѐ'];
      for (;;)
      {
        int j = mInputStream.read(arrayOfByte);
        if (j <= 0) {
          break;
        }
        ((ByteArrayOutputStream)localObject).write(arrayOfByte, 0, j);
        i += j;
      }
      localObject = ByteBuffer.wrap(((ByteArrayOutputStream)localObject).toByteArray());
      mOutputFormat.setDimensions(i);
      paramFilterContext = paramFilterContext.getFrameManager().newFrame(mOutputFormat);
      paramFilterContext.setData((ByteBuffer)localObject);
      pushOutput("data", paramFilterContext);
      paramFilterContext.release();
      closeOutputPort("data");
      return;
    }
    catch (IOException paramFilterContext)
    {
      Object localObject = new StringBuilder();
      ((StringBuilder)localObject).append("InputStreamSource: Could not read stream: ");
      ((StringBuilder)localObject).append(paramFilterContext.getMessage());
      ((StringBuilder)localObject).append("!");
      throw new RuntimeException(((StringBuilder)localObject).toString());
    }
  }
  
  public void setupPorts()
  {
    int i = FrameFormat.readTargetString(mTarget);
    if (mOutputFormat == null) {
      mOutputFormat = PrimitiveFormat.createByteFormat(i);
    }
    addOutputPort("data", mOutputFormat);
  }
}
