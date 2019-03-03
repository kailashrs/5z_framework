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

public class CropRectFilter
  extends Filter
{
  private int mHeight = 0;
  @GenerateFieldPort(name="height")
  private int mOutputHeight;
  @GenerateFieldPort(name="width")
  private int mOutputWidth;
  private Program mProgram;
  private int mTarget = 0;
  @GenerateFieldPort(hasDefault=true, name="tile_size")
  private int mTileSize = 640;
  private int mWidth = 0;
  @GenerateFieldPort(name="xorigin")
  private int mXorigin;
  @GenerateFieldPort(name="yorigin")
  private int mYorigin;
  
  public CropRectFilter(String paramString)
  {
    super(paramString);
  }
  
  public void fieldPortValueUpdated(String paramString, FilterContext paramFilterContext)
  {
    if (mProgram != null) {
      updateSourceRect(mWidth, mHeight);
    }
  }
  
  public void initProgram(FilterContext paramFilterContext, int paramInt)
  {
    if (paramInt == 3)
    {
      paramFilterContext = ShaderProgram.createIdentity(paramFilterContext);
      paramFilterContext.setMaximumTileSize(mTileSize);
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
    FrameFormat localFrameFormat = localFrame.getFormat();
    Object localObject = ImageFormat.create(mOutputWidth, mOutputHeight, 3, 3);
    localObject = paramFilterContext.getFrameManager().newFrame((FrameFormat)localObject);
    if ((mProgram == null) || (localFrameFormat.getTarget() != mTarget)) {
      initProgram(paramFilterContext, localFrameFormat.getTarget());
    }
    if ((localFrameFormat.getWidth() != mWidth) || (localFrameFormat.getHeight() != mHeight)) {
      updateSourceRect(localFrameFormat.getWidth(), localFrameFormat.getHeight());
    }
    mProgram.process(localFrame, (Frame)localObject);
    pushOutput("image", (Frame)localObject);
    ((Frame)localObject).release();
  }
  
  public void setupPorts()
  {
    addMaskedInputPort("image", ImageFormat.create(3));
    addOutputBasedOnInput("image", "image");
  }
  
  void updateSourceRect(int paramInt1, int paramInt2)
  {
    mWidth = paramInt1;
    mHeight = paramInt2;
    ((ShaderProgram)mProgram).setSourceRect(mXorigin / mWidth, mYorigin / mHeight, mOutputWidth / mWidth, mOutputHeight / mHeight);
  }
}
