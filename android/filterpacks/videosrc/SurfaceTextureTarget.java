package android.filterpacks.videosrc;

import android.filterfw.core.Filter;
import android.filterfw.core.FilterContext;
import android.filterfw.core.Frame;
import android.filterfw.core.FrameFormat;
import android.filterfw.core.FrameManager;
import android.filterfw.core.GLEnvironment;
import android.filterfw.core.GLFrame;
import android.filterfw.core.GenerateFieldPort;
import android.filterfw.core.GenerateFinalPort;
import android.filterfw.core.MutableFrameFormat;
import android.filterfw.core.ShaderProgram;
import android.filterfw.format.ImageFormat;
import android.filterfw.geometry.Point;
import android.filterfw.geometry.Quad;
import android.graphics.SurfaceTexture;
import android.util.Log;

public class SurfaceTextureTarget
  extends Filter
{
  private static final String TAG = "SurfaceTextureTarget";
  private final int RENDERMODE_CUSTOMIZE = 3;
  private final int RENDERMODE_FILL_CROP = 2;
  private final int RENDERMODE_FIT = 1;
  private final int RENDERMODE_STRETCH = 0;
  private float mAspectRatio = 1.0F;
  private boolean mLogVerbose = Log.isLoggable("SurfaceTextureTarget", 2);
  private ShaderProgram mProgram;
  private int mRenderMode = 1;
  @GenerateFieldPort(hasDefault=true, name="renderMode")
  private String mRenderModeString;
  private GLFrame mScreen;
  @GenerateFinalPort(name="height")
  private int mScreenHeight;
  @GenerateFinalPort(name="width")
  private int mScreenWidth;
  @GenerateFieldPort(hasDefault=true, name="sourceQuad")
  private Quad mSourceQuad = new Quad(new Point(0.0F, 1.0F), new Point(1.0F, 1.0F), new Point(0.0F, 0.0F), new Point(1.0F, 0.0F));
  private int mSurfaceId;
  @GenerateFinalPort(name="surfaceTexture")
  private SurfaceTexture mSurfaceTexture;
  @GenerateFieldPort(hasDefault=true, name="targetQuad")
  private Quad mTargetQuad = new Quad(new Point(0.0F, 0.0F), new Point(1.0F, 0.0F), new Point(0.0F, 1.0F), new Point(1.0F, 1.0F));
  
  public SurfaceTextureTarget(String paramString)
  {
    super(paramString);
  }
  
  private void updateTargetRect()
  {
    StringBuilder localStringBuilder;
    if (mLogVerbose)
    {
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("updateTargetRect. Thread: ");
      localStringBuilder.append(Thread.currentThread());
      Log.v("SurfaceTextureTarget", localStringBuilder.toString());
    }
    if ((mScreenWidth > 0) && (mScreenHeight > 0) && (mProgram != null))
    {
      float f1 = mScreenWidth / mScreenHeight;
      float f2 = f1 / mAspectRatio;
      if (mLogVerbose)
      {
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("UTR. screen w = ");
        localStringBuilder.append(mScreenWidth);
        localStringBuilder.append(" x screen h = ");
        localStringBuilder.append(mScreenHeight);
        localStringBuilder.append(" Screen AR: ");
        localStringBuilder.append(f1);
        localStringBuilder.append(", frame AR: ");
        localStringBuilder.append(mAspectRatio);
        localStringBuilder.append(", relative AR: ");
        localStringBuilder.append(f2);
        Log.v("SurfaceTextureTarget", localStringBuilder.toString());
      }
      if ((f2 == 1.0F) && (mRenderMode != 3))
      {
        mProgram.setTargetRect(0.0F, 0.0F, 1.0F, 1.0F);
        mProgram.setClearsOutput(false);
      }
      else
      {
        switch (mRenderMode)
        {
        default: 
          break;
        case 3: 
          mProgram.setSourceRegion(mSourceQuad);
          break;
        case 2: 
          if (f2 > 1.0F)
          {
            mTargetQuad.p0.set(0.0F, 0.5F - 0.5F * f2);
            mTargetQuad.p1.set(1.0F, 0.5F - 0.5F * f2);
            mTargetQuad.p2.set(0.0F, 0.5F * f2 + 0.5F);
            mTargetQuad.p3.set(1.0F, 0.5F + 0.5F * f2);
          }
          else
          {
            mTargetQuad.p0.set(0.5F - 0.5F / f2, 0.0F);
            mTargetQuad.p1.set(0.5F / f2 + 0.5F, 0.0F);
            mTargetQuad.p2.set(0.5F - 0.5F / f2, 1.0F);
            mTargetQuad.p3.set(0.5F + 0.5F / f2, 1.0F);
          }
          mProgram.setClearsOutput(true);
          break;
        case 1: 
          if (f2 > 1.0F)
          {
            mTargetQuad.p0.set(0.5F - 0.5F / f2, 0.0F);
            mTargetQuad.p1.set(0.5F / f2 + 0.5F, 0.0F);
            mTargetQuad.p2.set(0.5F - 0.5F / f2, 1.0F);
            mTargetQuad.p3.set(0.5F + 0.5F / f2, 1.0F);
          }
          else
          {
            mTargetQuad.p0.set(0.0F, 0.5F - 0.5F * f2);
            mTargetQuad.p1.set(1.0F, 0.5F - 0.5F * f2);
            mTargetQuad.p2.set(0.0F, 0.5F * f2 + 0.5F);
            mTargetQuad.p3.set(1.0F, 0.5F + 0.5F * f2);
          }
          mProgram.setClearsOutput(true);
          break;
        case 0: 
          mTargetQuad.p0.set(0.0F, 0.0F);
          mTargetQuad.p1.set(1.0F, 0.0F);
          mTargetQuad.p2.set(0.0F, 1.0F);
          mTargetQuad.p3.set(1.0F, 1.0F);
          mProgram.setClearsOutput(false);
        }
        if (mLogVerbose)
        {
          localStringBuilder = new StringBuilder();
          localStringBuilder.append("UTR. quad: ");
          localStringBuilder.append(mTargetQuad);
          Log.v("SurfaceTextureTarget", localStringBuilder.toString());
        }
        mProgram.setTargetRegion(mTargetQuad);
      }
    }
  }
  
  public void close(FilterContext paramFilterContext)
  {
    try
    {
      if (mSurfaceId > 0)
      {
        paramFilterContext.getGLEnvironment().unregisterSurfaceId(mSurfaceId);
        mSurfaceId = -1;
      }
      return;
    }
    finally
    {
      paramFilterContext = finally;
      throw paramFilterContext;
    }
  }
  
  public void disconnect(FilterContext paramFilterContext)
  {
    try
    {
      if (mLogVerbose) {
        Log.v("SurfaceTextureTarget", "disconnect");
      }
      if (mSurfaceTexture == null)
      {
        Log.d("SurfaceTextureTarget", "SurfaceTexture is already null. Nothing to disconnect.");
        return;
      }
      mSurfaceTexture = null;
      if (mSurfaceId > 0)
      {
        paramFilterContext.getGLEnvironment().unregisterSurfaceId(mSurfaceId);
        mSurfaceId = -1;
      }
      return;
    }
    finally {}
  }
  
  public void fieldPortValueUpdated(String paramString, FilterContext paramFilterContext)
  {
    if (mLogVerbose)
    {
      paramString = new StringBuilder();
      paramString.append("FPVU. Thread: ");
      paramString.append(Thread.currentThread());
      Log.v("SurfaceTextureTarget", paramString.toString());
    }
    updateRenderMode();
  }
  
  public void open(FilterContext paramFilterContext)
  {
    try
    {
      if (mSurfaceTexture != null)
      {
        mSurfaceId = paramFilterContext.getGLEnvironment().registerSurfaceTexture(mSurfaceTexture, mScreenWidth, mScreenHeight);
        int i = mSurfaceId;
        if (i > 0) {
          return;
        }
        localRuntimeException = new java/lang/RuntimeException;
        paramFilterContext = new java/lang/StringBuilder;
        paramFilterContext.<init>();
        paramFilterContext.append("Could not register SurfaceTexture: ");
        paramFilterContext.append(mSurfaceTexture);
        localRuntimeException.<init>(paramFilterContext.toString());
        throw localRuntimeException;
      }
      Log.e("SurfaceTextureTarget", "SurfaceTexture is null!!");
      RuntimeException localRuntimeException = new java/lang/RuntimeException;
      paramFilterContext = new java/lang/StringBuilder;
      paramFilterContext.<init>();
      paramFilterContext.append("Could not register SurfaceTexture: ");
      paramFilterContext.append(mSurfaceTexture);
      localRuntimeException.<init>(paramFilterContext.toString());
      throw localRuntimeException;
    }
    finally {}
  }
  
  public void prepare(FilterContext paramFilterContext)
  {
    if (mLogVerbose)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Prepare. Thread: ");
      ((StringBuilder)localObject).append(Thread.currentThread());
      Log.v("SurfaceTextureTarget", ((StringBuilder)localObject).toString());
    }
    mProgram = ShaderProgram.createIdentity(paramFilterContext);
    mProgram.setSourceRect(0.0F, 1.0F, 1.0F, -1.0F);
    mProgram.setClearColor(0.0F, 0.0F, 0.0F);
    updateRenderMode();
    Object localObject = new MutableFrameFormat(2, 3);
    ((MutableFrameFormat)localObject).setBytesPerSample(4);
    ((MutableFrameFormat)localObject).setDimensions(mScreenWidth, mScreenHeight);
    mScreen = ((GLFrame)paramFilterContext.getFrameManager().newBoundFrame((FrameFormat)localObject, 101, 0L));
  }
  
  public void process(FilterContext paramFilterContext)
  {
    try
    {
      int i = mSurfaceId;
      if (i <= 0) {
        return;
      }
      GLEnvironment localGLEnvironment = paramFilterContext.getGLEnvironment();
      Frame localFrame = pullInput("frame");
      i = 0;
      float f = localFrame.getFormat().getWidth() / localFrame.getFormat().getHeight();
      if (f != mAspectRatio)
      {
        if (mLogVerbose)
        {
          StringBuilder localStringBuilder = new java/lang/StringBuilder;
          localStringBuilder.<init>();
          localStringBuilder.append("Process. New aspect ratio: ");
          localStringBuilder.append(f);
          localStringBuilder.append(", previously: ");
          localStringBuilder.append(mAspectRatio);
          localStringBuilder.append(". Thread: ");
          localStringBuilder.append(Thread.currentThread());
          Log.v("SurfaceTextureTarget", localStringBuilder.toString());
        }
        mAspectRatio = f;
        updateTargetRect();
      }
      if (localFrame.getFormat().getTarget() != 3)
      {
        paramFilterContext = paramFilterContext.getFrameManager().duplicateFrameToTarget(localFrame, 3);
        i = 1;
      }
      else
      {
        paramFilterContext = localFrame;
      }
      localGLEnvironment.activateSurfaceWithId(mSurfaceId);
      mProgram.process(paramFilterContext, mScreen);
      localGLEnvironment.setSurfaceTimestamp(localFrame.getTimestamp());
      localGLEnvironment.swapBuffers();
      if (i != 0) {
        paramFilterContext.release();
      }
      return;
    }
    finally {}
  }
  
  public void setupPorts()
  {
    try
    {
      if (mSurfaceTexture != null)
      {
        addMaskedInputPort("frame", ImageFormat.create(3));
        return;
      }
      RuntimeException localRuntimeException = new java/lang/RuntimeException;
      localRuntimeException.<init>("Null SurfaceTexture passed to SurfaceTextureTarget");
      throw localRuntimeException;
    }
    finally {}
  }
  
  public void tearDown(FilterContext paramFilterContext)
  {
    if (mScreen != null) {
      mScreen.release();
    }
  }
  
  public void updateRenderMode()
  {
    StringBuilder localStringBuilder;
    if (mLogVerbose)
    {
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("updateRenderMode. Thread: ");
      localStringBuilder.append(Thread.currentThread());
      Log.v("SurfaceTextureTarget", localStringBuilder.toString());
    }
    if (mRenderModeString != null) {
      if (mRenderModeString.equals("stretch"))
      {
        mRenderMode = 0;
      }
      else if (mRenderModeString.equals("fit"))
      {
        mRenderMode = 1;
      }
      else if (mRenderModeString.equals("fill_crop"))
      {
        mRenderMode = 2;
      }
      else if (mRenderModeString.equals("customize"))
      {
        mRenderMode = 3;
      }
      else
      {
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("Unknown render mode '");
        localStringBuilder.append(mRenderModeString);
        localStringBuilder.append("'!");
        throw new RuntimeException(localStringBuilder.toString());
      }
    }
    updateTargetRect();
  }
}
