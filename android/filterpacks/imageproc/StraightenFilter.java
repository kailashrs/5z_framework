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

public class StraightenFilter
  extends Filter
{
  private static final float DEGREE_TO_RADIAN = 0.017453292F;
  @GenerateFieldPort(hasDefault=true, name="angle")
  private float mAngle = 0.0F;
  private int mHeight = 0;
  @GenerateFieldPort(hasDefault=true, name="maxAngle")
  private float mMaxAngle = 45.0F;
  private Program mProgram;
  private int mTarget = 0;
  @GenerateFieldPort(hasDefault=true, name="tile_size")
  private int mTileSize = 640;
  private int mWidth = 0;
  
  public StraightenFilter(String paramString)
  {
    super(paramString);
  }
  
  private void updateParameters()
  {
    float f1 = (float)Math.cos(mAngle * 0.017453292F);
    float f2 = (float)Math.sin(mAngle * 0.017453292F);
    if (mMaxAngle > 0.0F)
    {
      float f3 = mMaxAngle;
      float f4 = 90.0F;
      if (f3 <= 90.0F) {
        f4 = mMaxAngle;
      }
      mMaxAngle = f4;
      Point localPoint1 = new Point(-f1 * mWidth + mHeight * f2, -f2 * mWidth - mHeight * f1);
      Object localObject = new Point(mWidth * f1 + mHeight * f2, mWidth * f2 - mHeight * f1);
      Point localPoint2 = new Point(-f1 * mWidth - mHeight * f2, -f2 * mWidth + mHeight * f1);
      Point localPoint3 = new Point(mWidth * f1 - mHeight * f2, mWidth * f2 + mHeight * f1);
      f4 = Math.max(Math.abs(x), Math.abs(x));
      f1 = Math.max(Math.abs(y), Math.abs(y));
      f4 = Math.min(mWidth / f4, mHeight / f1) * 0.5F;
      localPoint1.set(x * f4 / mWidth + 0.5F, y * f4 / mHeight + 0.5F);
      ((Point)localObject).set(x * f4 / mWidth + 0.5F, y * f4 / mHeight + 0.5F);
      localPoint2.set(x * f4 / mWidth + 0.5F, y * f4 / mHeight + 0.5F);
      localPoint3.set(x * f4 / mWidth + 0.5F, y * f4 / mHeight + 0.5F);
      localObject = new Quad(localPoint1, (Point)localObject, localPoint2, localPoint3);
      ((ShaderProgram)mProgram).setSourceRegion((Quad)localObject);
      return;
    }
    throw new RuntimeException("Max angle is out of range (0-180).");
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
    if ((mProgram == null) || (localFrameFormat.getTarget() != mTarget)) {
      initProgram(paramFilterContext, localFrameFormat.getTarget());
    }
    if ((localFrameFormat.getWidth() != mWidth) || (localFrameFormat.getHeight() != mHeight))
    {
      mWidth = localFrameFormat.getWidth();
      mHeight = localFrameFormat.getHeight();
      updateParameters();
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
