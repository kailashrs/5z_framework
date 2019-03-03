package android.filterpacks.imageproc;

import android.filterfw.core.FilterContext;
import android.filterfw.core.FrameFormat;
import android.filterfw.core.GenerateFieldPort;
import android.filterfw.core.MutableFrameFormat;
import android.filterfw.core.Program;
import android.filterfw.core.ShaderProgram;
import android.filterfw.format.ImageFormat;

public class ToGrayFilter
  extends SimpleImageFilter
{
  private static final String mColorToGray4Shader = "precision mediump float;\nuniform sampler2D tex_sampler_0;\nvarying vec2 v_texcoord;\nvoid main() {\n  vec4 color = texture2D(tex_sampler_0, v_texcoord);\n  float y = dot(color, vec4(0.299, 0.587, 0.114, 0));\n  gl_FragColor = vec4(y, y, y, color.a);\n}\n";
  @GenerateFieldPort(hasDefault=true, name="invertSource")
  private boolean mInvertSource = false;
  private MutableFrameFormat mOutputFormat;
  @GenerateFieldPort(hasDefault=true, name="tile_size")
  private int mTileSize = 640;
  
  public ToGrayFilter(String paramString)
  {
    super(paramString, null);
  }
  
  protected Program getNativeProgram(FilterContext paramFilterContext)
  {
    throw new RuntimeException("Native toGray not implemented yet!");
  }
  
  protected Program getShaderProgram(FilterContext paramFilterContext)
  {
    int i = getInputFormat("image").getBytesPerSample();
    if (i == 4)
    {
      paramFilterContext = new ShaderProgram(paramFilterContext, "precision mediump float;\nuniform sampler2D tex_sampler_0;\nvarying vec2 v_texcoord;\nvoid main() {\n  vec4 color = texture2D(tex_sampler_0, v_texcoord);\n  float y = dot(color, vec4(0.299, 0.587, 0.114, 0));\n  gl_FragColor = vec4(y, y, y, color.a);\n}\n");
      paramFilterContext.setMaximumTileSize(mTileSize);
      if (mInvertSource) {
        paramFilterContext.setSourceRect(0.0F, 1.0F, 1.0F, -1.0F);
      }
      return paramFilterContext;
    }
    paramFilterContext = new StringBuilder();
    paramFilterContext.append("Unsupported GL input channels: ");
    paramFilterContext.append(i);
    paramFilterContext.append("! Channels must be 4!");
    throw new RuntimeException(paramFilterContext.toString());
  }
  
  public void setupPorts()
  {
    addMaskedInputPort("image", ImageFormat.create(3, 3));
    addOutputBasedOnInput("image", "image");
  }
}
