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
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;

public class RedEyeFilter
  extends Filter
{
  private static final float DEFAULT_RED_INTENSITY = 1.3F;
  private static final float MIN_RADIUS = 10.0F;
  private static final float RADIUS_RATIO = 0.06F;
  private final Canvas mCanvas = new Canvas();
  @GenerateFieldPort(name="centers")
  private float[] mCenters;
  private int mHeight = 0;
  private final Paint mPaint = new Paint();
  private Program mProgram;
  private float mRadius;
  private Bitmap mRedEyeBitmap;
  private Frame mRedEyeFrame;
  private final String mRedEyeShader = "precision mediump float;\nuniform sampler2D tex_sampler_0;\nuniform sampler2D tex_sampler_1;\nuniform float intensity;\nvarying vec2 v_texcoord;\nvoid main() {\n  vec4 color = texture2D(tex_sampler_0, v_texcoord);\n  vec4 mask = texture2D(tex_sampler_1, v_texcoord);\n  if (mask.a > 0.0) {\n    float green_blue = color.g + color.b;\n    float red_intensity = color.r / green_blue;\n    if (red_intensity > intensity) {\n      color.r = 0.5 * green_blue;\n    }\n  }\n  gl_FragColor = color;\n}\n";
  private int mTarget = 0;
  @GenerateFieldPort(hasDefault=true, name="tile_size")
  private int mTileSize = 640;
  private int mWidth = 0;
  
  public RedEyeFilter(String paramString)
  {
    super(paramString);
  }
  
  private void createRedEyeFrame(FilterContext paramFilterContext)
  {
    int i = mWidth / 2;
    int j = mHeight / 2;
    Bitmap localBitmap = Bitmap.createBitmap(i, j, Bitmap.Config.ARGB_8888);
    mCanvas.setBitmap(localBitmap);
    mPaint.setColor(-1);
    mRadius = Math.max(10.0F, 0.06F * Math.min(i, j));
    for (int k = 0; k < mCenters.length; k += 2) {
      mCanvas.drawCircle(mCenters[k] * i, mCenters[(k + 1)] * j, mRadius, mPaint);
    }
    MutableFrameFormat localMutableFrameFormat = ImageFormat.create(i, j, 3, 3);
    mRedEyeFrame = paramFilterContext.getFrameManager().newFrame(localMutableFrameFormat);
    mRedEyeFrame.setBitmap(localBitmap);
    localBitmap.recycle();
  }
  
  private void updateProgramParams()
  {
    if (mCenters.length % 2 != 1) {
      return;
    }
    throw new RuntimeException("The size of center array must be even.");
  }
  
  public void fieldPortValueUpdated(String paramString, FilterContext paramFilterContext)
  {
    if (mProgram != null) {
      updateProgramParams();
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
      paramFilterContext = new ShaderProgram(paramFilterContext, "precision mediump float;\nuniform sampler2D tex_sampler_0;\nuniform sampler2D tex_sampler_1;\nuniform float intensity;\nvarying vec2 v_texcoord;\nvoid main() {\n  vec4 color = texture2D(tex_sampler_0, v_texcoord);\n  vec4 mask = texture2D(tex_sampler_1, v_texcoord);\n  if (mask.a > 0.0) {\n    float green_blue = color.g + color.b;\n    float red_intensity = color.r / green_blue;\n    if (red_intensity > intensity) {\n      color.r = 0.5 * green_blue;\n    }\n  }\n  gl_FragColor = color;\n}\n");
      paramFilterContext.setMaximumTileSize(mTileSize);
      mProgram = paramFilterContext;
      mProgram.setHostValue("intensity", Float.valueOf(1.3F));
      mTarget = paramInt;
      return;
    }
    paramFilterContext = new StringBuilder();
    paramFilterContext.append("Filter RedEye does not support frames of target ");
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
    if ((localFrameFormat.getWidth() != mWidth) || (localFrameFormat.getHeight() != mHeight))
    {
      mWidth = localFrameFormat.getWidth();
      mHeight = localFrameFormat.getHeight();
    }
    createRedEyeFrame(paramFilterContext);
    paramFilterContext = mRedEyeFrame;
    mProgram.process(new Frame[] { localFrame1, paramFilterContext }, localFrame2);
    pushOutput("image", localFrame2);
    localFrame2.release();
    mRedEyeFrame.release();
    mRedEyeFrame = null;
  }
  
  public void setupPorts()
  {
    addMaskedInputPort("image", ImageFormat.create(3));
    addOutputBasedOnInput("image", "image");
  }
}
