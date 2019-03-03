package android.renderscript;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.util.AttributeSet;
import android.view.TextureView;
import android.view.TextureView.SurfaceTextureListener;

public class RSTextureView
  extends TextureView
  implements TextureView.SurfaceTextureListener
{
  private RenderScriptGL mRS;
  private SurfaceTexture mSurfaceTexture;
  
  public RSTextureView(Context paramContext)
  {
    super(paramContext);
    init();
  }
  
  public RSTextureView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    init();
  }
  
  private void init()
  {
    setSurfaceTextureListener(this);
  }
  
  public RenderScriptGL createRenderScriptGL(RenderScriptGL.SurfaceConfig paramSurfaceConfig)
  {
    paramSurfaceConfig = new RenderScriptGL(getContext(), paramSurfaceConfig);
    setRenderScriptGL(paramSurfaceConfig);
    if (mSurfaceTexture != null) {
      mRS.setSurfaceTexture(mSurfaceTexture, getWidth(), getHeight());
    }
    return paramSurfaceConfig;
  }
  
  public void destroyRenderScriptGL()
  {
    mRS.destroy();
    mRS = null;
  }
  
  public RenderScriptGL getRenderScriptGL()
  {
    return mRS;
  }
  
  public void onSurfaceTextureAvailable(SurfaceTexture paramSurfaceTexture, int paramInt1, int paramInt2)
  {
    mSurfaceTexture = paramSurfaceTexture;
    if (mRS != null) {
      mRS.setSurfaceTexture(mSurfaceTexture, paramInt1, paramInt2);
    }
  }
  
  public boolean onSurfaceTextureDestroyed(SurfaceTexture paramSurfaceTexture)
  {
    mSurfaceTexture = paramSurfaceTexture;
    if (mRS != null) {
      mRS.setSurfaceTexture(null, 0, 0);
    }
    return true;
  }
  
  public void onSurfaceTextureSizeChanged(SurfaceTexture paramSurfaceTexture, int paramInt1, int paramInt2)
  {
    mSurfaceTexture = paramSurfaceTexture;
    if (mRS != null) {
      mRS.setSurfaceTexture(mSurfaceTexture, paramInt1, paramInt2);
    }
  }
  
  public void onSurfaceTextureUpdated(SurfaceTexture paramSurfaceTexture)
  {
    mSurfaceTexture = paramSurfaceTexture;
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
    if (mSurfaceTexture != null) {
      mRS.setSurfaceTexture(mSurfaceTexture, getWidth(), getHeight());
    }
  }
}
