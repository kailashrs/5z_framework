package android.filterpacks.imageproc;

import android.filterfw.core.Filter;
import android.filterfw.core.FilterContext;
import android.filterfw.core.Frame;
import android.filterfw.core.FrameFormat;
import android.filterfw.core.FrameManager;
import android.filterfw.core.GenerateFieldPort;
import android.filterfw.core.Program;
import android.filterfw.core.ShaderProgram;
import android.filterfw.format.ImageFormat;

public class FlipFilter
  extends Filter
{
  @GenerateFieldPort(hasDefault=true, name="horizontal")
  private boolean mHorizontal = false;
  private Program mProgram;
  private int mTarget = 0;
  @GenerateFieldPort(hasDefault=true, name="tile_size")
  private int mTileSize = 640;
  @GenerateFieldPort(hasDefault=true, name="vertical")
  private boolean mVertical = false;
  
  public FlipFilter(String paramString)
  {
    super(paramString);
  }
  
  private void updateParameters()
  {
    boolean bool = mHorizontal;
    float f1 = 0.0F;
    float f2 = 1.0F;
    float f3;
    if (bool) {
      f3 = 1.0F;
    } else {
      f3 = 0.0F;
    }
    if (mVertical) {
      f1 = 1.0F;
    }
    float f4;
    if (mHorizontal) {
      f4 = -1.0F;
    } else {
      f4 = 1.0F;
    }
    if (mVertical) {
      f2 = -1.0F;
    }
    ((ShaderProgram)mProgram).setSourceRect(f3, f1, f4, f2);
  }
  
  public void fieldPortValueUpdated(String paramString, FilterContext paramFilterContext)
  {
    if (mProgram != null) {
      updateParameters();
    }
  }
  
  public FrameFormat getOutputFormat(String paramString, FrameFormat paramFrameFormat)
  {
    return paramFrameFormat;
  }
  
  public void initProgram(FilterContext paramFilterContext, int paramInt)
  {
    if (paramInt == 3)
    {
      paramFilterContext = ShaderProgram.createIdentity(paramFilterContext);
      paramFilterContext.setMaximumTileSize(mTileSize);
      mProgram = paramFilterContext;
      mTarget = paramInt;
      updateParameters();
      return;
    }
    paramFilterContext = new StringBuilder();
    paramFilterContext.append("Filter Sharpen does not support frames of target ");
    paramFilterContext.append(paramInt);
    paramFilterContext.append("!");
    throw new RuntimeException(paramFilterContext.toString());
  }
  
  public void process(FilterContext paramFilterContext)
  {
    Frame localFrame = pullInput("image");
    FrameFormat localFrameFormat = localFrame.getFormat();
    if ((mProgram == null) || (localFrameFormat.getTarget() != mTarget)) {
      initProgram(paramFilterContext, localFrameFormat.getTarget());
    }
    paramFilterContext = paramFilterContext.getFrameManager().newFrame(localFrameFormat);
    mProgram.process(localFrame, paramFilterContext);
    pushOutput("image", paramFilterContext);
    paramFilterContext.release();
  }
  
  public void setupPorts()
  {
    addMaskedInputPort("image", ImageFormat.create(3));
    addOutputBasedOnInput("image", "image");
  }
}
