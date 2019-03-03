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

public class SaturateFilter
  extends Filter
{
  private Program mBenProgram;
  private final String mBenSaturateShader = "precision mediump float;\nuniform sampler2D tex_sampler_0;\nuniform float scale;\nuniform float shift;\nuniform vec3 weights;\nvarying vec2 v_texcoord;\nvoid main() {\n  vec4 color = texture2D(tex_sampler_0, v_texcoord);\n  float kv = dot(color.rgb, weights) + shift;\n  vec3 new_color = scale * color.rgb + (1.0 - scale) * kv;\n  gl_FragColor = vec4(new_color, color.a);\n}\n";
  private Program mHerfProgram;
  private final String mHerfSaturateShader = "precision mediump float;\nuniform sampler2D tex_sampler_0;\nuniform vec3 weights;\nuniform vec3 exponents;\nvarying vec2 v_texcoord;\nvoid main() {\n  vec4 color = texture2D(tex_sampler_0, v_texcoord);\n  float de = dot(color.rgb, weights);\n  float inv_de = 1.0 / de;\n  vec3 new_color = de * pow(color.rgb * inv_de, exponents);\n  float max_color = max(max(max(new_color.r, new_color.g), new_color.b), 1.0);\n  gl_FragColor = vec4(new_color / max_color, color.a);\n}\n";
  @GenerateFieldPort(hasDefault=true, name="scale")
  private float mScale = 0.0F;
  private int mTarget = 0;
  @GenerateFieldPort(hasDefault=true, name="tile_size")
  private int mTileSize = 640;
  
  public SaturateFilter(String paramString)
  {
    super(paramString);
  }
  
  private void initParameters()
  {
    float[] arrayOfFloat = new float[3];
    float[] tmp5_4 = arrayOfFloat;
    tmp5_4[0] = 0.25F;
    float[] tmp10_5 = tmp5_4;
    tmp10_5[1] = 0.625F;
    float[] tmp15_10 = tmp10_5;
    tmp15_10[2] = 0.125F;
    tmp15_10;
    mBenProgram.setHostValue("weights", arrayOfFloat);
    mBenProgram.setHostValue("shift", Float.valueOf(0.003921569F));
    mHerfProgram.setHostValue("weights", arrayOfFloat);
    updateParameters();
  }
  
  private void updateParameters()
  {
    if (mScale > 0.0F)
    {
      float f1 = mScale;
      float f2 = mScale;
      float f3 = mScale;
      mHerfProgram.setHostValue("exponents", new float[] { 0.9F * f1 + 1.0F, 2.1F * f2 + 1.0F, 2.7F * f3 + 1.0F });
    }
    else
    {
      mBenProgram.setHostValue("scale", Float.valueOf(1.0F + mScale));
    }
  }
  
  public void fieldPortValueUpdated(String paramString, FilterContext paramFilterContext)
  {
    if ((mBenProgram != null) && (mHerfProgram != null)) {
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
      ShaderProgram localShaderProgram = new ShaderProgram(paramFilterContext, "precision mediump float;\nuniform sampler2D tex_sampler_0;\nuniform float scale;\nuniform float shift;\nuniform vec3 weights;\nvarying vec2 v_texcoord;\nvoid main() {\n  vec4 color = texture2D(tex_sampler_0, v_texcoord);\n  float kv = dot(color.rgb, weights) + shift;\n  vec3 new_color = scale * color.rgb + (1.0 - scale) * kv;\n  gl_FragColor = vec4(new_color, color.a);\n}\n");
      localShaderProgram.setMaximumTileSize(mTileSize);
      mBenProgram = localShaderProgram;
      paramFilterContext = new ShaderProgram(paramFilterContext, "precision mediump float;\nuniform sampler2D tex_sampler_0;\nuniform vec3 weights;\nuniform vec3 exponents;\nvarying vec2 v_texcoord;\nvoid main() {\n  vec4 color = texture2D(tex_sampler_0, v_texcoord);\n  float de = dot(color.rgb, weights);\n  float inv_de = 1.0 / de;\n  vec3 new_color = de * pow(color.rgb * inv_de, exponents);\n  float max_color = max(max(max(new_color.r, new_color.g), new_color.b), 1.0);\n  gl_FragColor = vec4(new_color / max_color, color.a);\n}\n");
      paramFilterContext.setMaximumTileSize(mTileSize);
      mHerfProgram = paramFilterContext;
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
    if ((mBenProgram == null) || (localFrameFormat.getTarget() != mTarget))
    {
      initProgram(paramFilterContext, localFrameFormat.getTarget());
      initParameters();
    }
    paramFilterContext = paramFilterContext.getFrameManager().newFrame(localFrameFormat);
    if (mScale > 0.0F) {
      mHerfProgram.process(localFrame, paramFilterContext);
    } else {
      mBenProgram.process(localFrame, paramFilterContext);
    }
    pushOutput("image", paramFilterContext);
    paramFilterContext.release();
  }
  
  public void setupPorts()
  {
    addMaskedInputPort("image", ImageFormat.create(3));
    addOutputBasedOnInput("image", "image");
  }
}
