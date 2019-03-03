package android.filterpacks.imageproc;

import android.filterfw.core.Filter;
import android.filterfw.core.FilterContext;
import android.filterfw.core.Frame;
import android.filterfw.core.FrameFormat;
import android.filterfw.core.FrameManager;
import android.filterfw.core.Program;
import android.filterfw.format.ImageFormat;
import java.lang.reflect.Field;

public abstract class SimpleImageFilter
  extends Filter
{
  protected int mCurrentTarget = 0;
  protected String mParameterName;
  protected Program mProgram;
  
  public SimpleImageFilter(String paramString1, String paramString2)
  {
    super(paramString1);
    mParameterName = paramString2;
  }
  
  protected abstract Program getNativeProgram(FilterContext paramFilterContext);
  
  public FrameFormat getOutputFormat(String paramString, FrameFormat paramFrameFormat)
  {
    return paramFrameFormat;
  }
  
  protected abstract Program getShaderProgram(FilterContext paramFilterContext);
  
  public void process(FilterContext paramFilterContext)
  {
    Frame localFrame1 = pullInput("image");
    FrameFormat localFrameFormat = localFrame1.getFormat();
    Frame localFrame2 = paramFilterContext.getFrameManager().newFrame(localFrameFormat);
    updateProgramWithTarget(localFrameFormat.getTarget(), paramFilterContext);
    mProgram.process(localFrame1, localFrame2);
    pushOutput("image", localFrame2);
    localFrame2.release();
  }
  
  public void setupPorts()
  {
    if (mParameterName != null) {
      try
      {
        Field localField = SimpleImageFilter.class.getDeclaredField("mProgram");
        addProgramPort(mParameterName, mParameterName, localField, Float.TYPE, false);
      }
      catch (NoSuchFieldException localNoSuchFieldException)
      {
        throw new RuntimeException("Internal Error: mProgram field not found!");
      }
    }
    addMaskedInputPort("image", ImageFormat.create(3));
    addOutputBasedOnInput("image", "image");
  }
  
  protected void updateProgramWithTarget(int paramInt, FilterContext paramFilterContext)
  {
    if (paramInt != mCurrentTarget)
    {
      switch (paramInt)
      {
      default: 
        mProgram = null;
        break;
      case 3: 
        mProgram = getShaderProgram(paramFilterContext);
        break;
      case 2: 
        mProgram = getNativeProgram(paramFilterContext);
      }
      if (mProgram != null)
      {
        initProgramInputs(mProgram, paramFilterContext);
        mCurrentTarget = paramInt;
      }
      else
      {
        paramFilterContext = new StringBuilder();
        paramFilterContext.append("Could not create a program for image filter ");
        paramFilterContext.append(this);
        paramFilterContext.append("!");
        throw new RuntimeException(paramFilterContext.toString());
      }
    }
  }
}
