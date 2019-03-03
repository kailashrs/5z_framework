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
import android.graphics.Color;

public class DuotoneFilter
  extends Filter
{
  private final String mDuotoneShader = "precision mediump float;\nuniform sampler2D tex_sampler_0;\nuniform vec3 first;\nuniform vec3 second;\nvarying vec2 v_texcoord;\nvoid main() {\n  vec4 color = texture2D(tex_sampler_0, v_texcoord);\n  float energy = (color.r + color.g + color.b) * 0.3333;\n  vec3 new_color = (1.0 - energy) * first + energy * second;\n  gl_FragColor = vec4(new_color.rgb, color.a);\n}\n";
  @GenerateFieldPort(hasDefault=true, name="first_color")
  private int mFirstColor = -65536;
  private Program mProgram;
  @GenerateFieldPort(hasDefault=true, name="second_color")
  private int mSecondColor = 65280;
  private int mTarget = 0;
  @GenerateFieldPort(hasDefault=true, name="tile_size")
  private int mTileSize = 640;
  
  public DuotoneFilter(String paramString)
  {
    super(paramString);
  }
  
  private void updateParameters()
  {
    float f1 = Color.red(mFirstColor) / 255.0F;
    float f2 = Color.green(mFirstColor) / 255.0F;
    float f3 = Color.blue(mFirstColor) / 255.0F;
    float f4 = Color.red(mSecondColor) / 255.0F;
    float f5 = Color.green(mSecondColor) / 255.0F;
    float f6 = Color.blue(mSecondColor) / 255.0F;
    mProgram.setHostValue("first", new float[] { f1, f2, f3 });
    mProgram.setHostValue("second", new float[] { f4, f5, f6 });
  }
  
  public FrameFormat getOutputFormat(String paramString, FrameFormat paramFrameFormat)
  {
    return paramFrameFormat;
  }
  
  public void initProgram(FilterContext paramFilterContext, int paramInt)
  {
    if (paramInt == 3)
    {
      paramFilterContext = new ShaderProgram(paramFilterContext, "precision mediump float;\nuniform sampler2D tex_sampler_0;\nuniform vec3 first;\nuniform vec3 second;\nvarying vec2 v_texcoord;\nvoid main() {\n  vec4 color = texture2D(tex_sampler_0, v_texcoord);\n  float energy = (color.r + color.g + color.b) * 0.3333;\n  vec3 new_color = (1.0 - energy) * first + energy * second;\n  gl_FragColor = vec4(new_color.rgb, color.a);\n}\n");
      paramFilterContext.setMaximumTileSize(mTileSize);
      mProgram = paramFilterContext;
      mTarget = paramInt;
      return;
    }
    paramFilterContext = new StringBuilder();
    paramFilterContext.append("Filter Duotone does not support frames of target ");
    paramFilterContext.append(paramInt);
    paramFilterContext.append("!");
    throw new RuntimeException(paramFilterContext.toString());
  }
  
  public void process(FilterContext paramFilterContext)
  {
    Frame localFrame1 = pullInput("image");
    FrameFormat localFrameFormat = localFrame1.getFormat();
    Frame localFrame2 = paramFilterContext.getFrameManager().newFrame(localFrameFormat);
    if ((mProgram == null) || (localFrameFormat.getTarget() != mTarget)) {
      initProgram(paramFilterContext, localFrameFormat.getTarget());
    }
    updateParameters();
    mProgram.process(localFrame1, localFrame2);
    pushOutput("image", localFrame2);
    localFrame2.release();
  }
  
  public void setupPorts()
  {
    addMaskedInputPort("image", ImageFormat.create(3));
    addOutputBasedOnInput("image", "image");
  }
}
