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
import android.util.Log;

public class FillLightFilter
  extends Filter
{
  @GenerateFieldPort(hasDefault=true, name="strength")
  private float mBacklight = 0.0F;
  private final String mFillLightShader = "precision mediump float;\nuniform sampler2D tex_sampler_0;\nuniform float mult;\nuniform float igamma;\nvarying vec2 v_texcoord;\nvoid main()\n{\n  const vec3 color_weights = vec3(0.25, 0.5, 0.25);\n  vec4 color = texture2D(tex_sampler_0, v_texcoord);\n  float lightmask = dot(color.rgb, color_weights);\n  float backmask = (1.0 - lightmask);\n  vec3 ones = vec3(1.0, 1.0, 1.0);\n  vec3 diff = pow(mult * color.rgb, igamma * ones) - color.rgb;\n  diff = min(diff, 1.0);\n  vec3 new_color = min(color.rgb + diff * backmask, 1.0);\n  gl_FragColor = vec4(new_color, color.a);\n}\n";
  private Program mProgram;
  private int mTarget = 0;
  @GenerateFieldPort(hasDefault=true, name="tile_size")
  private int mTileSize = 640;
  
  public FillLightFilter(String paramString)
  {
    super(paramString);
  }
  
  private void updateParameters()
  {
    float f1 = 1.0F / (0.7F * (1.0F - mBacklight) + 0.3F);
    float f2 = 1.0F / ((1.0F - 0.3F) * f1 + 0.3F);
    mProgram.setHostValue("mult", Float.valueOf(f1));
    mProgram.setHostValue("igamma", Float.valueOf(f2));
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
      ShaderProgram localShaderProgram = new ShaderProgram(paramFilterContext, "precision mediump float;\nuniform sampler2D tex_sampler_0;\nuniform float mult;\nuniform float igamma;\nvarying vec2 v_texcoord;\nvoid main()\n{\n  const vec3 color_weights = vec3(0.25, 0.5, 0.25);\n  vec4 color = texture2D(tex_sampler_0, v_texcoord);\n  float lightmask = dot(color.rgb, color_weights);\n  float backmask = (1.0 - lightmask);\n  vec3 ones = vec3(1.0, 1.0, 1.0);\n  vec3 diff = pow(mult * color.rgb, igamma * ones) - color.rgb;\n  diff = min(diff, 1.0);\n  vec3 new_color = min(color.rgb + diff * backmask, 1.0);\n  gl_FragColor = vec4(new_color, color.a);\n}\n");
      paramFilterContext = new StringBuilder();
      paramFilterContext.append("tile size: ");
      paramFilterContext.append(mTileSize);
      Log.e("FillLight", paramFilterContext.toString());
      localShaderProgram.setMaximumTileSize(mTileSize);
      mProgram = localShaderProgram;
      mTarget = paramInt;
      return;
    }
    paramFilterContext = new StringBuilder();
    paramFilterContext.append("Filter FillLight does not support frames of target ");
    paramFilterContext.append(paramInt);
    paramFilterContext.append("!");
    throw new RuntimeException(paramFilterContext.toString());
  }
  
  public void process(FilterContext paramFilterContext)
  {
    Frame localFrame1 = pullInput("image");
    FrameFormat localFrameFormat = localFrame1.getFormat();
    Frame localFrame2 = paramFilterContext.getFrameManager().newFrame(localFrameFormat);
    if ((mProgram == null) || (localFrameFormat.getTarget() != mTarget))
    {
      initProgram(paramFilterContext, localFrameFormat.getTarget());
      updateParameters();
    }
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
