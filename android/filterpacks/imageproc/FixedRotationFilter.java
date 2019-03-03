package android.filterpacks.imageproc;

import android.filterfw.core.Filter;
import android.filterfw.core.FilterContext;
import android.filterfw.core.Frame;
import android.filterfw.core.FrameFormat;
import android.filterfw.core.FrameManager;
import android.filterfw.core.GenerateFieldPort;
import android.filterfw.core.MutableFrameFormat;
import android.filterfw.core.ShaderProgram;
import android.filterfw.format.ImageFormat;
import android.filterfw.geometry.Point;
import android.filterfw.geometry.Quad;

public class FixedRotationFilter
  extends Filter
{
  private ShaderProgram mProgram = null;
  @GenerateFieldPort(hasDefault=true, name="rotation")
  private int mRotation = 0;
  
  public FixedRotationFilter(String paramString)
  {
    super(paramString);
  }
  
  public FrameFormat getOutputFormat(String paramString, FrameFormat paramFrameFormat)
  {
    return paramFrameFormat;
  }
  
  public void process(FilterContext paramFilterContext)
  {
    Frame localFrame = pullInput("image");
    if (mRotation == 0)
    {
      pushOutput("image", localFrame);
      return;
    }
    Object localObject = localFrame.getFormat();
    if (mProgram == null) {
      mProgram = ShaderProgram.createIdentity(paramFilterContext);
    }
    MutableFrameFormat localMutableFrameFormat = ((FrameFormat)localObject).mutableCopy();
    int i = ((FrameFormat)localObject).getWidth();
    int j = ((FrameFormat)localObject).getHeight();
    Point localPoint1 = new Point(0.0F, 0.0F);
    Point localPoint2 = new Point(1.0F, 0.0F);
    Point localPoint3 = new Point(0.0F, 1.0F);
    localObject = new Point(1.0F, 1.0F);
    switch (Math.round(mRotation / 90.0F) % 4)
    {
    default: 
      localObject = new Quad(localPoint1, localPoint2, localPoint3, (Point)localObject);
      break;
    case 3: 
      localObject = new Quad(localPoint2, (Point)localObject, localPoint1, localPoint3);
      localMutableFrameFormat.setDimensions(j, i);
      break;
    case 2: 
      localObject = new Quad((Point)localObject, localPoint3, localPoint2, localPoint1);
      break;
    case 1: 
      localObject = new Quad(localPoint3, localPoint1, (Point)localObject, localPoint2);
      localMutableFrameFormat.setDimensions(j, i);
    }
    paramFilterContext = paramFilterContext.getFrameManager().newFrame(localMutableFrameFormat);
    mProgram.setSourceRegion((Quad)localObject);
    mProgram.process(localFrame, paramFilterContext);
    pushOutput("image", paramFilterContext);
    paramFilterContext.release();
  }
  
  public void setupPorts()
  {
    addMaskedInputPort("image", ImageFormat.create(3, 3));
    addOutputBasedOnInput("image", "image");
  }
}
