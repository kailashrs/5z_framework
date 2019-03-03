package android.filterpacks.ui;

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
import android.util.Log;
import android.view.Surface;

public class SurfaceTargetFilter
  extends Filter
{
  private static final String TAG = "SurfaceRenderFilter";
  private final int RENDERMODE_FILL_CROP = 2;
  private final int RENDERMODE_FIT = 1;
  private final int RENDERMODE_STRETCH = 0;
  private float mAspectRatio = 1.0F;
  private GLEnvironment mGlEnv;
  private boolean mLogVerbose = Log.isLoggable("SurfaceRenderFilter", 2);
  private ShaderProgram mProgram;
  private int mRenderMode = 1;
  @GenerateFieldPort(hasDefault=true, name="renderMode")
  private String mRenderModeString;
  private GLFrame mScreen;
  @GenerateFieldPort(name="oheight")
  private int mScreenHeight;
  @GenerateFieldPort(name="owidth")
  private int mScreenWidth;
  @GenerateFinalPort(name="surface")
  private Surface mSurface;
  private int mSurfaceId = -1;
  
  public SurfaceTargetFilter(String paramString)
  {
    super(paramString);
  }
  
  private void registerSurface()
  {
    mSurfaceId = mGlEnv.registerSurface(mSurface);
    if (mSurfaceId >= 0) {
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Could not register Surface: ");
    localStringBuilder.append(mSurface);
    throw new RuntimeException(localStringBuilder.toString());
  }
  
  private void unregisterSurface()
  {
    if (mSurfaceId > 0) {
      mGlEnv.unregisterSurfaceId(mSurfaceId);
    }
  }
  
  private void updateTargetRect()
  {
    if ((mScreenWidth > 0) && (mScreenHeight > 0) && (mProgram != null))
    {
      float f = mScreenWidth / mScreenHeight / mAspectRatio;
      switch (mRenderMode)
      {
      default: 
        break;
      case 2: 
        if (f > 1.0F) {
          mProgram.setTargetRect(0.0F, 0.5F - 0.5F * f, 1.0F, f);
        } else {
          mProgram.setTargetRect(0.5F - 0.5F / f, 0.0F, 1.0F / f, 1.0F);
        }
        break;
      case 1: 
        if (f > 1.0F) {
          mProgram.setTargetRect(0.5F - 0.5F / f, 0.0F, 1.0F / f, 1.0F);
        } else {
          mProgram.setTargetRect(0.0F, 0.5F - 0.5F * f, 1.0F, f);
        }
        break;
      case 0: 
        mProgram.setTargetRect(0.0F, 0.0F, 1.0F, 1.0F);
      }
    }
  }
  
  public void close(FilterContext paramFilterContext)
  {
    unregisterSurface();
  }
  
  public void fieldPortValueUpdated(String paramString, FilterContext paramFilterContext)
  {
    mScreen.setViewport(0, 0, mScreenWidth, mScreenHeight);
    updateTargetRect();
  }
  
  public void open(FilterContext paramFilterContext)
  {
    registerSurface();
  }
  
  public void prepare(FilterContext paramFilterContext)
  {
    mGlEnv = paramFilterContext.getGLEnvironment();
    mProgram = ShaderProgram.createIdentity(paramFilterContext);
    mProgram.setSourceRect(0.0F, 1.0F, 1.0F, -1.0F);
    mProgram.setClearsOutput(true);
    mProgram.setClearColor(0.0F, 0.0F, 0.0F);
    MutableFrameFormat localMutableFrameFormat = ImageFormat.create(mScreenWidth, mScreenHeight, 3, 3);
    mScreen = ((GLFrame)paramFilterContext.getFrameManager().newBoundFrame(localMutableFrameFormat, 101, 0L));
    updateRenderMode();
  }
  
  public void process(FilterContext paramFilterContext)
  {
    if (mLogVerbose) {
      Log.v("SurfaceRenderFilter", "Starting frame processing");
    }
    Frame localFrame = pullInput("frame");
    int i = 0;
    float f = localFrame.getFormat().getWidth() / localFrame.getFormat().getHeight();
    StringBuilder localStringBuilder;
    if (f != mAspectRatio)
    {
      if (mLogVerbose)
      {
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("New aspect ratio: ");
        localStringBuilder.append(f);
        localStringBuilder.append(", previously: ");
        localStringBuilder.append(mAspectRatio);
        Log.v("SurfaceRenderFilter", localStringBuilder.toString());
      }
      mAspectRatio = f;
      updateTargetRect();
    }
    if (mLogVerbose)
    {
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("Got input format: ");
      localStringBuilder.append(localFrame.getFormat());
      Log.v("SurfaceRenderFilter", localStringBuilder.toString());
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
    mGlEnv.activateSurfaceWithId(mSurfaceId);
    mProgram.process(paramFilterContext, mScreen);
    mGlEnv.swapBuffers();
    if (i != 0) {
      paramFilterContext.release();
    }
  }
  
  public void setupPorts()
  {
    if (mSurface != null)
    {
      addMaskedInputPort("frame", ImageFormat.create(3));
      return;
    }
    throw new RuntimeException("NULL Surface passed to SurfaceTargetFilter");
  }
  
  public void tearDown(FilterContext paramFilterContext)
  {
    if (mScreen != null) {
      mScreen.release();
    }
  }
  
  public void updateRenderMode()
  {
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
      else
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Unknown render mode '");
        localStringBuilder.append(mRenderModeString);
        localStringBuilder.append("'!");
        throw new RuntimeException(localStringBuilder.toString());
      }
    }
    updateTargetRect();
  }
}
