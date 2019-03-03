package android.filterpacks.imageproc;

import android.filterfw.core.Filter;
import android.filterfw.core.FilterContext;
import android.filterfw.core.Frame;
import android.filterfw.core.FrameFormat;
import android.filterfw.core.FrameManager;
import android.filterfw.core.MutableFrameFormat;
import android.filterfw.core.ShaderProgram;
import android.filterfw.format.ImageFormat;
import android.filterfw.format.ObjectFormat;
import android.filterfw.geometry.Quad;

public class DrawOverlayFilter
  extends Filter
{
  private ShaderProgram mProgram;
  
  public DrawOverlayFilter(String paramString)
  {
    super(paramString);
  }
  
  public FrameFormat getOutputFormat(String paramString, FrameFormat paramFrameFormat)
  {
    return paramFrameFormat;
  }
  
  public void prepare(FilterContext paramFilterContext)
  {
    mProgram = ShaderProgram.createIdentity(paramFilterContext);
  }
  
  public void process(FilterContext paramFilterContext)
  {
    Frame localFrame1 = pullInput("source");
    Frame localFrame2 = pullInput("overlay");
    Quad localQuad = ((Quad)pullInput("box").getObjectValue()).translated(1.0F, 1.0F).scaled(2.0F);
    mProgram.setTargetRegion(localQuad);
    paramFilterContext = paramFilterContext.getFrameManager().newFrame(localFrame1.getFormat());
    paramFilterContext.setDataFromFrame(localFrame1);
    mProgram.process(localFrame2, paramFilterContext);
    pushOutput("image", paramFilterContext);
    paramFilterContext.release();
  }
  
  public void setupPorts()
  {
    MutableFrameFormat localMutableFrameFormat = ImageFormat.create(3, 3);
    addMaskedInputPort("source", localMutableFrameFormat);
    addMaskedInputPort("overlay", localMutableFrameFormat);
    addMaskedInputPort("box", ObjectFormat.fromClass(Quad.class, 1));
    addOutputBasedOnInput("image", "source");
  }
}
