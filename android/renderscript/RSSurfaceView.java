package android.renderscript;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class RSSurfaceView
  extends SurfaceView
  implements SurfaceHolder.Callback
{
  private RenderScriptGL mRS;
  private SurfaceHolder mSurfaceHolder;
  
  public RSSurfaceView(Context paramContext)
  {
    super(paramContext);
    init();
  }
  
  public RSSurfaceView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    init();
  }
  
  private void init()
  {
    getHolder().addCallback(this);
  }
  
  public RenderScriptGL createRenderScriptGL(RenderScriptGL.SurfaceConfig paramSurfaceConfig)
  {
    paramSurfaceConfig = new RenderScriptGL(getContext(), paramSurfaceConfig);
    setRenderScriptGL(paramSurfaceConfig);
    return paramSurfaceConfig;
  }
  
  public void destroyRenderScriptGL()
  {
    try
    {
      mRS.destroy();
      mRS = null;
      return;
    }
    finally {}
  }
  
  public RenderScriptGL getRenderScriptGL()
  {
    return mRS;
  }
  
  public void pause()
  {
    if (mRS != null) {
      mRS.pause();
    }
  }
  
  public void resume()
  {
    if (mRS != null) {
      mRS.resume();
    }
  }
  
  public void setRenderScriptGL(RenderScriptGL paramRenderScriptGL)
  {
    mRS = paramRenderScriptGL;
  }
  
  public void surfaceChanged(SurfaceHolder paramSurfaceHolder, int paramInt1, int paramInt2, int paramInt3)
  {
    try
    {
      if (mRS != null) {
        mRS.setSurface(paramSurfaceHolder, paramInt2, paramInt3);
      }
      return;
    }
    finally {}
  }
  
  public void surfaceCreated(SurfaceHolder paramSurfaceHolder)
  {
    mSurfaceHolder = paramSurfaceHolder;
  }
  
  public void surfaceDestroyed(SurfaceHolder paramSurfaceHolder)
  {
    try
    {
      if (mRS != null) {
        mRS.setSurface(null, 0, 0);
      }
      return;
    }
    finally {}
  }
}
