package android.renderscript;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.res.Resources;
import android.graphics.SurfaceTexture;
import android.util.DisplayMetrics;
import android.view.Surface;
import android.view.SurfaceHolder;

public class RenderScriptGL
  extends RenderScript
{
  int mHeight;
  SurfaceConfig mSurfaceConfig;
  int mWidth;
  
  public RenderScriptGL(Context paramContext, SurfaceConfig paramSurfaceConfig)
  {
    super(paramContext);
    mSurfaceConfig = new SurfaceConfig(paramSurfaceConfig);
    int i = getApplicationInfotargetSdkVersion;
    mWidth = 0;
    mHeight = 0;
    long l = nDeviceCreate();
    int j = getResourcesgetDisplayMetricsdensityDpi;
    mContext = nContextCreateGL(l, 0, i, mSurfaceConfig.mColorMin, mSurfaceConfig.mColorPref, mSurfaceConfig.mAlphaMin, mSurfaceConfig.mAlphaPref, mSurfaceConfig.mDepthMin, mSurfaceConfig.mDepthPref, mSurfaceConfig.mStencilMin, mSurfaceConfig.mStencilPref, mSurfaceConfig.mSamplesMin, mSurfaceConfig.mSamplesPref, mSurfaceConfig.mSamplesQ, j);
    if (mContext != 0L)
    {
      mMessageThread = new RenderScript.MessageThread(this);
      mMessageThread.start();
      return;
    }
    throw new RSDriverException("Failed to create RS context.");
  }
  
  public void bindProgramFragment(ProgramFragment paramProgramFragment)
  {
    validate();
    nContextBindProgramFragment((int)safeID(paramProgramFragment));
  }
  
  public void bindProgramRaster(ProgramRaster paramProgramRaster)
  {
    validate();
    nContextBindProgramRaster((int)safeID(paramProgramRaster));
  }
  
  public void bindProgramStore(ProgramStore paramProgramStore)
  {
    validate();
    nContextBindProgramStore((int)safeID(paramProgramStore));
  }
  
  public void bindProgramVertex(ProgramVertex paramProgramVertex)
  {
    validate();
    nContextBindProgramVertex((int)safeID(paramProgramVertex));
  }
  
  public void bindRootScript(Script paramScript)
  {
    validate();
    nContextBindRootScript((int)safeID(paramScript));
  }
  
  public int getHeight()
  {
    return mHeight;
  }
  
  public int getWidth()
  {
    return mWidth;
  }
  
  public void pause()
  {
    validate();
    nContextPause();
  }
  
  public void resume()
  {
    validate();
    nContextResume();
  }
  
  public void setSurface(SurfaceHolder paramSurfaceHolder, int paramInt1, int paramInt2)
  {
    validate();
    Surface localSurface = null;
    if (paramSurfaceHolder != null) {
      localSurface = paramSurfaceHolder.getSurface();
    }
    mWidth = paramInt1;
    mHeight = paramInt2;
    nContextSetSurface(paramInt1, paramInt2, localSurface);
  }
  
  public void setSurfaceTexture(SurfaceTexture paramSurfaceTexture, int paramInt1, int paramInt2)
  {
    validate();
    Surface localSurface = null;
    if (paramSurfaceTexture != null) {
      localSurface = new Surface(paramSurfaceTexture);
    }
    mWidth = paramInt1;
    mHeight = paramInt2;
    nContextSetSurface(paramInt1, paramInt2, localSurface);
  }
  
  public static class SurfaceConfig
  {
    int mAlphaMin = 0;
    int mAlphaPref = 0;
    int mColorMin = 8;
    int mColorPref = 8;
    int mDepthMin = 0;
    int mDepthPref = 0;
    int mSamplesMin = 1;
    int mSamplesPref = 1;
    float mSamplesQ = 1.0F;
    int mStencilMin = 0;
    int mStencilPref = 0;
    
    public SurfaceConfig() {}
    
    public SurfaceConfig(SurfaceConfig paramSurfaceConfig)
    {
      mDepthMin = mDepthMin;
      mDepthPref = mDepthPref;
      mStencilMin = mStencilMin;
      mStencilPref = mStencilPref;
      mColorMin = mColorMin;
      mColorPref = mColorPref;
      mAlphaMin = mAlphaMin;
      mAlphaPref = mAlphaPref;
      mSamplesMin = mSamplesMin;
      mSamplesPref = mSamplesPref;
      mSamplesQ = mSamplesQ;
    }
    
    private void validateRange(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
      if ((paramInt1 >= paramInt3) && (paramInt1 <= paramInt4))
      {
        if (paramInt2 >= paramInt1) {
          return;
        }
        throw new RSIllegalArgumentException("preferred must be >= Minimum.");
      }
      throw new RSIllegalArgumentException("Minimum value provided out of range.");
    }
    
    public void setAlpha(int paramInt1, int paramInt2)
    {
      validateRange(paramInt1, paramInt2, 0, 8);
      mAlphaMin = paramInt1;
      mAlphaPref = paramInt2;
    }
    
    public void setColor(int paramInt1, int paramInt2)
    {
      validateRange(paramInt1, paramInt2, 5, 8);
      mColorMin = paramInt1;
      mColorPref = paramInt2;
    }
    
    public void setDepth(int paramInt1, int paramInt2)
    {
      validateRange(paramInt1, paramInt2, 0, 24);
      mDepthMin = paramInt1;
      mDepthPref = paramInt2;
    }
    
    public void setSamples(int paramInt1, int paramInt2, float paramFloat)
    {
      validateRange(paramInt1, paramInt2, 1, 32);
      if ((paramFloat >= 0.0F) && (paramFloat <= 1.0F))
      {
        mSamplesMin = paramInt1;
        mSamplesPref = paramInt2;
        mSamplesQ = paramFloat;
        return;
      }
      throw new RSIllegalArgumentException("Quality out of 0-1 range.");
    }
  }
}
