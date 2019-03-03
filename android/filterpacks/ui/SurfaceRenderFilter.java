package android.filterpacks.ui;

import android.filterfw.core.Filter;
import android.filterfw.core.FilterContext;
import android.filterfw.core.FilterSurfaceView;
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
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;

public class SurfaceRenderFilter
  extends Filter
  implements SurfaceHolder.Callback
{
  private static final String TAG = "SurfaceRenderFilter";
  private final int RENDERMODE_FILL_CROP = 2;
  private final int RENDERMODE_FIT = 1;
  private final int RENDERMODE_STRETCH = 0;
  private float mAspectRatio = 1.0F;
  private boolean mIsBound = false;
  private boolean mLogVerbose = Log.isLoggable("SurfaceRenderFilter", 2);
  private ShaderProgram mProgram;
  private int mRenderMode = 1;
  @GenerateFieldPort(hasDefault=true, name="renderMode")
  private String mRenderModeString;
  private GLFrame mScreen;
  private int mScreenHeight;
  private int mScreenWidth;
  @GenerateFinalPort(name="surfaceView")
  private FilterSurfaceView mSurfaceView;
  
  public SurfaceRenderFilter(String paramString)
  {
    super(paramString);
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
    mSurfaceView.unbind();
  }
  
  public void fieldPortValueUpdated(String paramString, FilterContext paramFilterContext)
  {
    updateTargetRect();
  }
  
  public void open(FilterContext paramFilterContext)
  {
    mSurfaceView.unbind();
    mSurfaceView.bindToListener(this, paramFilterContext.getGLEnvironment());
  }
  
  public void prepare(FilterContext paramFilterContext)
  {
    mProgram = ShaderProgram.createIdentity(paramFilterContext);
    mProgram.setSourceRect(0.0F, 1.0F, 1.0F, -1.0F);
    mProgram.setClearsOutput(true);
    mProgram.setClearColor(0.0F, 0.0F, 0.0F);
    updateRenderMode();
    MutableFrameFormat localMutableFrameFormat = ImageFormat.create(mSurfaceView.getWidth(), mSurfaceView.getHeight(), 3, 3);
    mScreen = ((GLFrame)paramFilterContext.getFrameManager().newBoundFrame(localMutableFrameFormat, 101, 0L));
  }
  
  public void process(FilterContext paramFilterContext)
  {
    if (!mIsBound)
    {
      paramFilterContext = new StringBuilder();
      paramFilterContext.append(this);
      paramFilterContext.append(": Ignoring frame as there is no surface to render to!");
      Log.w("SurfaceRenderFilter", paramFilterContext.toString());
      return;
    }
    if (mLogVerbose) {
      Log.v("SurfaceRenderFilter", "Starting frame processing");
    }
    GLEnvironment localGLEnvironment = mSurfaceView.getGLEnv();
    if (localGLEnvironment == paramFilterContext.getGLEnvironment())
    {
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
      localGLEnvironment.activateSurfaceWithId(mSurfaceView.getSurfaceId());
      mProgram.process(paramFilterContext, mScreen);
      localGLEnvironment.swapBuffers();
      if (i != 0) {
        paramFilterContext.release();
      }
      return;
    }
    throw new RuntimeException("Surface created under different GLEnvironment!");
  }
  
  public void setupPorts()
  {
    if (mSurfaceView != null)
    {
      addMaskedInputPort("frame", ImageFormat.create(3));
      return;
    }
    throw new RuntimeException("NULL SurfaceView passed to SurfaceRenderFilter");
  }
  
  public void surfaceChanged(SurfaceHolder paramSurfaceHolder, int paramInt1, int paramInt2, int paramInt3)
  {
    try
    {
      if (mScreen != null)
      {
        mScreenWidth = paramInt2;
        mScreenHeight = paramInt3;
        mScreen.setViewport(0, 0, mScreenWidth, mScreenHeight);
        updateTargetRect();
      }
      return;
    }
    finally
    {
      paramSurfaceHolder = finally;
      throw paramSurfaceHolder;
    }
  }
  
  public void surfaceCreated(SurfaceHolder paramSurfaceHolder)
  {
    try
    {
      mIsBound = true;
      return;
    }
    finally
    {
      paramSurfaceHolder = finally;
      throw paramSurfaceHolder;
    }
  }
  
  public void surfaceDestroyed(SurfaceHolder paramSurfaceHolder)
  {
    try
    {
      mIsBound = false;
      return;
    }
    finally
    {
      paramSurfaceHolder = finally;
      throw paramSurfaceHolder;
    }
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
