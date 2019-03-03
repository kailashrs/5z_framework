package android.filterpacks.imageproc;

import android.filterfw.core.FilterContext;
import android.filterfw.core.NativeProgram;
import android.filterfw.core.Program;
import android.filterfw.core.ShaderProgram;

public class ContrastFilter
  extends SimpleImageFilter
{
  private static final String mContrastShader = "precision mediump float;\nuniform sampler2D tex_sampler_0;\nuniform float contrast;\nvarying vec2 v_texcoord;\nvoid main() {\n  vec4 color = texture2D(tex_sampler_0, v_texcoord);\n  color -= 0.5;\n  color *= contrast;\n  color += 0.5;\n  gl_FragColor = color;\n}\n";
  
  public ContrastFilter(String paramString)
  {
    super(paramString, "contrast");
  }
  
  protected Program getNativeProgram(FilterContext paramFilterContext)
  {
    return new NativeProgram("filterpack_imageproc", "contrast");
  }
  
  protected Program getShaderProgram(FilterContext paramFilterContext)
  {
    return new ShaderProgram(paramFilterContext, "precision mediump float;\nuniform sampler2D tex_sampler_0;\nuniform float contrast;\nvarying vec2 v_texcoord;\nvoid main() {\n  vec4 color = texture2D(tex_sampler_0, v_texcoord);\n  color -= 0.5;\n  color *= contrast;\n  color += 0.5;\n  gl_FragColor = color;\n}\n");
  }
}
