package android.filterpacks.imageproc;

import android.filterfw.core.Filter;
import android.filterfw.core.FilterContext;
import android.filterfw.core.Frame;
import android.filterfw.core.FrameFormat;
import android.filterfw.core.FrameManager;
import android.filterfw.core.MutableFrameFormat;
import android.filterfw.core.NativeProgram;
import android.filterfw.core.Program;

public class ToRGBFilter
  extends Filter
{
  private int mInputBPP;
  private FrameFormat mLastFormat = null;
  private Program mProgram;
  
  public ToRGBFilter(String paramString)
  {
    super(paramString);
  }
  
  public void createProgram(FilterContext paramFilterContext, FrameFormat paramFrameFormat)
  {
    mInputBPP = paramFrameFormat.getBytesPerSample();
    if ((mLastFormat != null) && (mLastFormat.getBytesPerSample() == mInputBPP)) {
      return;
    }
    mLastFormat = paramFrameFormat;
    int i = mInputBPP;
    if (i != 1)
    {
      if (i == 4)
      {
        mProgram = new NativeProgram("filterpack_imageproc", "rgba_to_rgb");
      }
      else
      {
        paramFilterContext = new StringBuilder();
        paramFilterContext.append("Unsupported BytesPerPixel: ");
        paramFilterContext.append(mInputBPP);
        paramFilterContext.append("!");
        throw new RuntimeException(paramFilterContext.toString());
      }
    }
    else {
      mProgram = new NativeProgram("filterpack_imageproc", "gray_to_rgb");
    }
  }
  
  public FrameFormat getConvertedFormat(FrameFormat paramFrameFormat)
  {
    paramFrameFormat = paramFrameFormat.mutableCopy();
    paramFrameFormat.setMetaValue("colorspace", Integer.valueOf(2));
    paramFrameFormat.setBytesPerSample(3);
    return paramFrameFormat;
  }
  
  public FrameFormat getOutputFormat(String paramString, FrameFormat paramFrameFormat)
  {
    return getConvertedFormat(paramFrameFormat);
  }
  
  public void process(FilterContext paramFilterContext)
  {
    Frame localFrame = pullInput("image");
    createProgram(paramFilterContext, localFrame.getFormat());
    paramFilterContext = paramFilterContext.getFrameManager().newFrame(getConvertedFormat(localFrame.getFormat()));
    mProgram.process(localFrame, paramFilterContext);
    pushOutput("image", paramFilterContext);
    paramFilterContext.release();
  }
  
  public void setupPorts()
  {
    MutableFrameFormat localMutableFrameFormat = new MutableFrameFormat(2, 2);
    localMutableFrameFormat.setDimensionCount(2);
    addMaskedInputPort("image", localMutableFrameFormat);
    addOutputBasedOnInput("image", "image");
  }
}
