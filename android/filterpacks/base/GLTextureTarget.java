package android.filterpacks.base;

import android.filterfw.core.Filter;
import android.filterfw.core.FilterContext;
import android.filterfw.core.Frame;
import android.filterfw.core.FrameFormat;
import android.filterfw.core.FrameManager;
import android.filterfw.core.GenerateFieldPort;
import android.filterfw.core.MutableFrameFormat;
import android.filterfw.format.ImageFormat;

public class GLTextureTarget
  extends Filter
{
  @GenerateFieldPort(name="texId")
  private int mTexId;
  
  public GLTextureTarget(String paramString)
  {
    super(paramString);
  }
  
  public void process(FilterContext paramFilterContext)
  {
    Frame localFrame = pullInput("frame");
    MutableFrameFormat localMutableFrameFormat = ImageFormat.create(localFrame.getFormat().getWidth(), localFrame.getFormat().getHeight(), 3, 3);
    paramFilterContext = paramFilterContext.getFrameManager().newBoundFrame(localMutableFrameFormat, 100, mTexId);
    paramFilterContext.setDataFromFrame(localFrame);
    paramFilterContext.release();
  }
  
  public void setupPorts()
  {
    addMaskedInputPort("frame", ImageFormat.create(3));
  }
}
