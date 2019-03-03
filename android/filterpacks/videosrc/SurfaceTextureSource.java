package android.filterpacks.videosrc;

import android.filterfw.core.Filter;
import android.filterfw.core.FilterContext;
import android.filterfw.core.Frame;
import android.filterfw.core.FrameManager;
import android.filterfw.core.GLFrame;
import android.filterfw.core.GenerateFieldPort;
import android.filterfw.core.GenerateFinalPort;
import android.filterfw.core.MutableFrameFormat;
import android.filterfw.core.ShaderProgram;
import android.filterfw.format.ImageFormat;
import android.graphics.SurfaceTexture;
import android.graphics.SurfaceTexture.OnFrameAvailableListener;
import android.opengl.Matrix;
import android.os.ConditionVariable;
import android.util.Log;

public class SurfaceTextureSource
  extends Filter
{
  private static final String TAG = "SurfaceTextureSource";
  private static final boolean mLogVerbose = Log.isLoggable("SurfaceTextureSource", 2);
  private static final float[] mSourceCoords = { 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F, 0.0F, 1.0F };
  @GenerateFieldPort(hasDefault=true, name="closeOnTimeout")
  private boolean mCloseOnTimeout = false;
  private boolean mFirstFrame;
  private ShaderProgram mFrameExtractor;
  private float[] mFrameTransform = new float[16];
  @GenerateFieldPort(name="height")
  private int mHeight;
  private float[] mMappedCoords = new float[16];
  private GLFrame mMediaFrame;
  private ConditionVariable mNewFrameAvailable = new ConditionVariable();
  private MutableFrameFormat mOutputFormat;
  private final String mRenderShader = "#extension GL_OES_EGL_image_external : require\nprecision mediump float;\nuniform samplerExternalOES tex_sampler_0;\nvarying vec2 v_texcoord;\nvoid main() {\n  gl_FragColor = texture2D(tex_sampler_0, v_texcoord);\n}\n";
  @GenerateFinalPort(name="sourceListener")
  private SurfaceTextureSourceListener mSourceListener;
  private SurfaceTexture mSurfaceTexture;
  @GenerateFieldPort(hasDefault=true, name="waitForNewFrame")
  private boolean mWaitForNewFrame = true;
  @GenerateFieldPort(hasDefault=true, name="waitTimeout")
  private int mWaitTimeout = 1000;
  @GenerateFieldPort(name="width")
  private int mWidth;
  private SurfaceTexture.OnFrameAvailableListener onFrameAvailableListener = new SurfaceTexture.OnFrameAvailableListener()
  {
    public void onFrameAvailable(SurfaceTexture paramAnonymousSurfaceTexture)
    {
      if (SurfaceTextureSource.mLogVerbose) {
        Log.v("SurfaceTextureSource", "New frame from SurfaceTexture");
      }
      mNewFrameAvailable.open();
    }
  };
  
  public SurfaceTextureSource(String paramString)
  {
    super(paramString);
  }
  
  private void createFormats()
  {
    mOutputFormat = ImageFormat.create(mWidth, mHeight, 3, 3);
  }
  
  public void close(FilterContext paramFilterContext)
  {
    if (mLogVerbose) {
      Log.v("SurfaceTextureSource", "SurfaceTextureSource closed");
    }
    mSourceListener.onSurfaceTextureSourceReady(null);
    mSurfaceTexture.release();
    mSurfaceTexture = null;
  }
  
  public void fieldPortValueUpdated(String paramString, FilterContext paramFilterContext)
  {
    if ((paramString.equals("width")) || (paramString.equals("height"))) {
      mOutputFormat.setDimensions(mWidth, mHeight);
    }
  }
  
  public void open(FilterContext paramFilterContext)
  {
    if (mLogVerbose) {
      Log.v("SurfaceTextureSource", "Opening SurfaceTextureSource");
    }
    mSurfaceTexture = new SurfaceTexture(mMediaFrame.getTextureId());
    mSurfaceTexture.setOnFrameAvailableListener(onFrameAvailableListener);
    mSourceListener.onSurfaceTextureSourceReady(mSurfaceTexture);
    mFirstFrame = true;
  }
  
  protected void prepare(FilterContext paramFilterContext)
  {
    if (mLogVerbose) {
      Log.v("SurfaceTextureSource", "Preparing SurfaceTextureSource");
    }
    createFormats();
    mMediaFrame = ((GLFrame)paramFilterContext.getFrameManager().newBoundFrame(mOutputFormat, 104, 0L));
    mFrameExtractor = new ShaderProgram(paramFilterContext, "#extension GL_OES_EGL_image_external : require\nprecision mediump float;\nuniform samplerExternalOES tex_sampler_0;\nvarying vec2 v_texcoord;\nvoid main() {\n  gl_FragColor = texture2D(tex_sampler_0, v_texcoord);\n}\n");
  }
  
  public void process(FilterContext paramFilterContext)
  {
    if (mLogVerbose) {
      Log.v("SurfaceTextureSource", "Processing new frame");
    }
    if ((mWaitForNewFrame) || (mFirstFrame))
    {
      if (mWaitTimeout != 0)
      {
        if (!mNewFrameAvailable.block(mWaitTimeout))
        {
          if (mCloseOnTimeout)
          {
            if (mLogVerbose) {
              Log.v("SurfaceTextureSource", "Timeout waiting for a new frame. Closing.");
            }
            closeOutputPort("video");
            return;
          }
          throw new RuntimeException("Timeout waiting for new frame");
        }
      }
      else {
        mNewFrameAvailable.block();
      }
      mNewFrameAvailable.close();
      mFirstFrame = false;
    }
    mSurfaceTexture.updateTexImage();
    mSurfaceTexture.getTransformMatrix(mFrameTransform);
    Matrix.multiplyMM(mMappedCoords, 0, mFrameTransform, 0, mSourceCoords, 0);
    mFrameExtractor.setSourceRegion(mMappedCoords[0], mMappedCoords[1], mMappedCoords[4], mMappedCoords[5], mMappedCoords[8], mMappedCoords[9], mMappedCoords[12], mMappedCoords[13]);
    paramFilterContext = paramFilterContext.getFrameManager().newFrame(mOutputFormat);
    mFrameExtractor.process(mMediaFrame, paramFilterContext);
    paramFilterContext.setTimestamp(mSurfaceTexture.getTimestamp());
    pushOutput("video", paramFilterContext);
    paramFilterContext.release();
  }
  
  public void setupPorts()
  {
    addOutputPort("video", ImageFormat.create(3, 3));
  }
  
  public void tearDown(FilterContext paramFilterContext)
  {
    if (mMediaFrame != null) {
      mMediaFrame.release();
    }
  }
  
  public static abstract interface SurfaceTextureSourceListener
  {
    public abstract void onSurfaceTextureSourceReady(SurfaceTexture paramSurfaceTexture);
  }
}
