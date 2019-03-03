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
import android.filterfw.geometry.Point;
import android.filterfw.geometry.Quad;

public class RotateFilter
  extends Filter
{
  @GenerateFieldPort(name="angle")
  private int mAngle;
  private int mHeight = 0;
  private int mOutputHeight;
  private int mOutputWidth;
  private Program mProgram;
  private int mTarget = 0;
  @GenerateFieldPort(hasDefault=true, name="tile_size")
  private int mTileSize = 640;
  private int mWidth = 0;
  
  public RotateFilter(String paramString)
  {
    super(paramString);
  }
  
  private void updateParameters()
  {
    if (mAngle % 90 == 0)
    {
      int i = mAngle;
      float f1 = -1.0F;
      float f2;
      float f3;
      if (i % 180 == 0)
      {
        f2 = 0.0F;
        if (mAngle % 360 == 0) {
          f1 = 1.0F;
        }
        f3 = f1;
      }
      else
      {
        if ((mAngle + 90) % 360 != 0) {
          f1 = 1.0F;
        }
        mOutputWidth = mHeight;
        mOutputHeight = mWidth;
        f3 = 0.0F;
        f2 = f1;
      }
      Quad localQuad = new Quad(new Point((-f3 + f2 + 1.0F) * 0.5F, (-f2 - f3 + 1.0F) * 0.5F), new Point((f3 + f2 + 1.0F) * 0.5F, (f2 - f3 + 1.0F) * 0.5F), new Point((-f3 - f2 + 1.0F) * 0.5F, (-f2 + f3 + 1.0F) * 0.5F), new Point((f3 - f2 + 1.0F) * 0.5F, 0.5F * (f2 + f3 + 1.0F)));
      ((ShaderProgram)mProgram).setTargetRegion(localQuad);
      return;
    }
    throw new RuntimeException("degree has to be multiply of 90.");
  }
  
  public void fieldPortValueUpdated(String paramString, FilterContext paramFilterContext)
  {
    if (mProgram != null) {
      updateParameters();
    }
  }
  
  public void initProgram(FilterContext paramFilterContext, int paramInt)
  {
    if (paramInt == 3)
    {
      paramFilterContext = ShaderProgram.createIdentity(paramFilterContext);
      paramFilterContext.setMaximumTileSize(mTileSize);
      paramFilterContext.setClearsOutput(true);
      mProgram = paramFilterContext;
      mTarget = paramInt;
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
    Object localObject = localFrame.getFormat();
    if ((mProgram == null) || (((FrameFormat)localObject).getTarget() != mTarget)) {
      initProgram(paramFilterContext, ((FrameFormat)localObject).getTarget());
    }
    if ((((FrameFormat)localObject).getWidth() != mWidth) || (((FrameFormat)localObject).getHeight() != mHeight))
    {
      mWidth = ((FrameFormat)localObject).getWidth();
      mHeight = ((FrameFormat)localObject).getHeight();
      mOutputWidth = mWidth;
      mOutputHeight = mHeight;
      updateParameters();
    }
    localObject = ImageFormat.create(mOutputWidth, mOutputHeight, 3, 3);
    paramFilterContext = paramFilterContext.getFrameManager().newFrame((FrameFormat)localObject);
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
