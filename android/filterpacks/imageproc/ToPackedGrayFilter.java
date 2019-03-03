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

public class ToPackedGrayFilter
  extends Filter
{
  private final String mColorToPackedGrayShader = "precision mediump float;\nconst vec4 coeff_y = vec4(0.299, 0.587, 0.114, 0);\nuniform sampler2D tex_sampler_0;\nuniform float pix_stride;\nvarying vec2 v_texcoord;\nvoid main() {\n  for (int i = 0; i < 4; ++i) {\n    vec4 p = texture2D(tex_sampler_0,\n                       v_texcoord + vec2(pix_stride * float(i), 0.0));\n    gl_FragColor[i] = dot(p, coeff_y);\n  }\n}\n";
  @GenerateFieldPort(hasDefault=true, name="keepAspectRatio")
  private boolean mKeepAspectRatio = false;
  @GenerateFieldPort(hasDefault=true, name="oheight")
  private int mOHeight = 0;
  @GenerateFieldPort(hasDefault=true, name="owidth")
  private int mOWidth = 0;
  private Program mProgram;
  
  public ToPackedGrayFilter(String paramString)
  {
    super(paramString);
  }
  
  private void checkOutputDimensions(int paramInt1, int paramInt2)
  {
    if ((paramInt1 > 0) && (paramInt2 > 0)) {
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Invalid output dimensions: ");
    localStringBuilder.append(paramInt1);
    localStringBuilder.append(" ");
    localStringBuilder.append(paramInt2);
    throw new RuntimeException(localStringBuilder.toString());
  }
  
  private FrameFormat convertInputFormat(FrameFormat paramFrameFormat)
  {
    int i = mOWidth;
    int j = mOHeight;
    int k = paramFrameFormat.getWidth();
    int m = paramFrameFormat.getHeight();
    if (mOWidth == 0) {
      i = k;
    }
    if (mOHeight == 0) {
      j = m;
    }
    int n = i;
    int i1 = j;
    if (mKeepAspectRatio) {
      if (k > m)
      {
        n = Math.max(i, j);
        i1 = n * m / k;
      }
      else
      {
        i1 = Math.max(i, j);
        n = i1 * k / m;
      }
    }
    i = 4;
    if ((n <= 0) || (n >= 4)) {
      i = 4 * (n / 4);
    }
    return ImageFormat.create(i, i1, 1, 2);
  }
  
  public FrameFormat getOutputFormat(String paramString, FrameFormat paramFrameFormat)
  {
    return convertInputFormat(paramFrameFormat);
  }
  
  public void prepare(FilterContext paramFilterContext)
  {
    mProgram = new ShaderProgram(paramFilterContext, "precision mediump float;\nconst vec4 coeff_y = vec4(0.299, 0.587, 0.114, 0);\nuniform sampler2D tex_sampler_0;\nuniform float pix_stride;\nvarying vec2 v_texcoord;\nvoid main() {\n  for (int i = 0; i < 4; ++i) {\n    vec4 p = texture2D(tex_sampler_0,\n                       v_texcoord + vec2(pix_stride * float(i), 0.0));\n    gl_FragColor[i] = dot(p, coeff_y);\n  }\n}\n");
  }
  
  public void process(FilterContext paramFilterContext)
  {
    Frame localFrame = pullInput("image");
    Object localObject = localFrame.getFormat();
    FrameFormat localFrameFormat = convertInputFormat((FrameFormat)localObject);
    int i = localFrameFormat.getWidth();
    int j = localFrameFormat.getHeight();
    checkOutputDimensions(i, j);
    mProgram.setHostValue("pix_stride", Float.valueOf(1.0F / i));
    localObject = ((FrameFormat)localObject).mutableCopy();
    ((MutableFrameFormat)localObject).setDimensions(i / 4, j);
    localObject = paramFilterContext.getFrameManager().newFrame((FrameFormat)localObject);
    mProgram.process(localFrame, (Frame)localObject);
    paramFilterContext = paramFilterContext.getFrameManager().newFrame(localFrameFormat);
    paramFilterContext.setDataFromFrame((Frame)localObject);
    ((Frame)localObject).release();
    pushOutput("image", paramFilterContext);
    paramFilterContext.release();
  }
  
  public void setupPorts()
  {
    addMaskedInputPort("image", ImageFormat.create(3, 3));
    addOutputBasedOnInput("image", "image");
  }
}
