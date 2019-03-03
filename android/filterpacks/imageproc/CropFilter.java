package android.filterpacks.imageproc;

import android.filterfw.core.Filter;
import android.filterfw.core.FilterContext;
import android.filterfw.core.Frame;
import android.filterfw.core.FrameFormat;
import android.filterfw.core.FrameManager;
import android.filterfw.core.GenerateFieldPort;
import android.filterfw.core.MutableFrameFormat;
import android.filterfw.core.Program;
import android.filterfw.core.ShaderProgram;
import android.filterfw.format.ImageFormat;
import android.filterfw.format.ObjectFormat;
import android.filterfw.geometry.Quad;

public class CropFilter
  extends Filter
{
  @GenerateFieldPort(name="fillblack")
  private boolean mFillBlack = false;
  private final String mFragShader = "precision mediump float;\nuniform sampler2D tex_sampler_0;\nvarying vec2 v_texcoord;\nvoid main() {\n  const vec2 lo = vec2(0.0, 0.0);\n  const vec2 hi = vec2(1.0, 1.0);\n  const vec4 black = vec4(0.0, 0.0, 0.0, 1.0);\n  bool out_of_bounds =\n    any(lessThan(v_texcoord, lo)) ||\n    any(greaterThan(v_texcoord, hi));\n  if (out_of_bounds) {\n    gl_FragColor = black;\n  } else {\n    gl_FragColor = texture2D(tex_sampler_0, v_texcoord);\n  }\n}\n";
  private FrameFormat mLastFormat = null;
  @GenerateFieldPort(name="oheight")
  private int mOutputHeight = -1;
  @GenerateFieldPort(name="owidth")
  private int mOutputWidth = -1;
  private Program mProgram;
  
  public CropFilter(String paramString)
  {
    super(paramString);
  }
  
  protected void createProgram(FilterContext paramFilterContext, FrameFormat paramFrameFormat)
  {
    if ((mLastFormat != null) && (mLastFormat.getTarget() == paramFrameFormat.getTarget())) {
      return;
    }
    mLastFormat = paramFrameFormat;
    mProgram = null;
    if (paramFrameFormat.getTarget() == 3) {
      if (mFillBlack) {
        mProgram = new ShaderProgram(paramFilterContext, "precision mediump float;\nuniform sampler2D tex_sampler_0;\nvarying vec2 v_texcoord;\nvoid main() {\n  const vec2 lo = vec2(0.0, 0.0);\n  const vec2 hi = vec2(1.0, 1.0);\n  const vec4 black = vec4(0.0, 0.0, 0.0, 1.0);\n  bool out_of_bounds =\n    any(lessThan(v_texcoord, lo)) ||\n    any(greaterThan(v_texcoord, hi));\n  if (out_of_bounds) {\n    gl_FragColor = black;\n  } else {\n    gl_FragColor = texture2D(tex_sampler_0, v_texcoord);\n  }\n}\n");
      } else {
        mProgram = ShaderProgram.createIdentity(paramFilterContext);
      }
    }
    if (mProgram != null) {
      return;
    }
    paramFilterContext = new StringBuilder();
    paramFilterContext.append("Could not create a program for crop filter ");
    paramFilterContext.append(this);
    paramFilterContext.append("!");
    throw new RuntimeException(paramFilterContext.toString());
  }
  
  public FrameFormat getOutputFormat(String paramString, FrameFormat paramFrameFormat)
  {
    paramString = paramFrameFormat.mutableCopy();
    paramString.setDimensions(0, 0);
    return paramString;
  }
  
  public void process(FilterContext paramFilterContext)
  {
    Frame localFrame = pullInput("image");
    Object localObject = pullInput("box");
    createProgram(paramFilterContext, localFrame.getFormat());
    localObject = (Quad)((Frame)localObject).getObjectValue();
    MutableFrameFormat localMutableFrameFormat = localFrame.getFormat().mutableCopy();
    int i;
    if (mOutputWidth == -1) {
      i = localMutableFrameFormat.getWidth();
    } else {
      i = mOutputWidth;
    }
    int j;
    if (mOutputHeight == -1) {
      j = localMutableFrameFormat.getHeight();
    } else {
      j = mOutputHeight;
    }
    localMutableFrameFormat.setDimensions(i, j);
    paramFilterContext = paramFilterContext.getFrameManager().newFrame(localMutableFrameFormat);
    if ((mProgram instanceof ShaderProgram)) {
      ((ShaderProgram)mProgram).setSourceRegion((Quad)localObject);
    }
    mProgram.process(localFrame, paramFilterContext);
    pushOutput("image", paramFilterContext);
    paramFilterContext.release();
  }
  
  public void setupPorts()
  {
    addMaskedInputPort("image", ImageFormat.create(3));
    addMaskedInputPort("box", ObjectFormat.fromClass(Quad.class, 1));
    addOutputBasedOnInput("image", "image");
  }
}
