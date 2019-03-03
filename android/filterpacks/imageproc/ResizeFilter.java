package android.filterpacks.imageproc;

import android.filterfw.core.Filter;
import android.filterfw.core.FilterContext;
import android.filterfw.core.Frame;
import android.filterfw.core.FrameFormat;
import android.filterfw.core.FrameManager;
import android.filterfw.core.GLFrame;
import android.filterfw.core.GenerateFieldPort;
import android.filterfw.core.MutableFrameFormat;
import android.filterfw.core.Program;
import android.filterfw.core.ShaderProgram;
import android.filterfw.format.ImageFormat;

public class ResizeFilter
  extends Filter
{
  @GenerateFieldPort(hasDefault=true, name="generateMipMap")
  private boolean mGenerateMipMap = false;
  private int mInputChannels;
  @GenerateFieldPort(hasDefault=true, name="keepAspectRatio")
  private boolean mKeepAspectRatio = false;
  private FrameFormat mLastFormat = null;
  @GenerateFieldPort(name="oheight")
  private int mOHeight;
  @GenerateFieldPort(name="owidth")
  private int mOWidth;
  private MutableFrameFormat mOutputFormat;
  private Program mProgram;
  
  public ResizeFilter(String paramString)
  {
    super(paramString);
  }
  
  protected void createProgram(FilterContext paramFilterContext, FrameFormat paramFrameFormat)
  {
    if ((mLastFormat != null) && (mLastFormat.getTarget() == paramFrameFormat.getTarget())) {
      return;
    }
    mLastFormat = paramFrameFormat;
    switch (paramFrameFormat.getTarget())
    {
    default: 
      throw new RuntimeException("ResizeFilter could not create suitable program!");
    case 3: 
      mProgram = ShaderProgram.createIdentity(paramFilterContext);
      return;
    }
    throw new RuntimeException("Native ResizeFilter not implemented yet!");
  }
  
  public FrameFormat getOutputFormat(String paramString, FrameFormat paramFrameFormat)
  {
    return paramFrameFormat;
  }
  
  public void process(FilterContext paramFilterContext)
  {
    Frame localFrame = pullInput("image");
    createProgram(paramFilterContext, localFrame.getFormat());
    MutableFrameFormat localMutableFrameFormat = localFrame.getFormat().mutableCopy();
    if (mKeepAspectRatio)
    {
      localObject = localFrame.getFormat();
      mOHeight = (mOWidth * ((FrameFormat)localObject).getHeight() / ((FrameFormat)localObject).getWidth());
    }
    localMutableFrameFormat.setDimensions(mOWidth, mOHeight);
    Object localObject = paramFilterContext.getFrameManager().newFrame(localMutableFrameFormat);
    if (mGenerateMipMap)
    {
      paramFilterContext = (GLFrame)paramFilterContext.getFrameManager().newFrame(localFrame.getFormat());
      paramFilterContext.setTextureParameter(10241, 9985);
      paramFilterContext.setDataFromFrame(localFrame);
      paramFilterContext.generateMipMap();
      mProgram.process(paramFilterContext, (Frame)localObject);
      paramFilterContext.release();
    }
    else
    {
      mProgram.process(localFrame, (Frame)localObject);
    }
    pushOutput("image", (Frame)localObject);
    ((Frame)localObject).release();
  }
  
  public void setupPorts()
  {
    addMaskedInputPort("image", ImageFormat.create(3));
    addOutputBasedOnInput("image", "image");
  }
}
