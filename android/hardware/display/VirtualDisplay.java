package android.hardware.display;

import android.view.Display;
import android.view.Surface;

public final class VirtualDisplay
{
  private final Display mDisplay;
  private final DisplayManagerGlobal mGlobal;
  private Surface mSurface;
  private IVirtualDisplayCallback mToken;
  
  VirtualDisplay(DisplayManagerGlobal paramDisplayManagerGlobal, Display paramDisplay, IVirtualDisplayCallback paramIVirtualDisplayCallback, Surface paramSurface)
  {
    mGlobal = paramDisplayManagerGlobal;
    mDisplay = paramDisplay;
    mToken = paramIVirtualDisplayCallback;
    mSurface = paramSurface;
  }
  
  public Display getDisplay()
  {
    return mDisplay;
  }
  
  public Surface getSurface()
  {
    return mSurface;
  }
  
  public void release()
  {
    if (mToken != null)
    {
      mGlobal.releaseVirtualDisplay(mToken);
      mToken = null;
    }
  }
  
  public void resize(int paramInt1, int paramInt2, int paramInt3)
  {
    mGlobal.resizeVirtualDisplay(mToken, paramInt1, paramInt2, paramInt3);
  }
  
  public void setSurface(Surface paramSurface)
  {
    if (mSurface != paramSurface)
    {
      mGlobal.setVirtualDisplaySurface(mToken, paramSurface);
      mSurface = paramSurface;
    }
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("VirtualDisplay{display=");
    localStringBuilder.append(mDisplay);
    localStringBuilder.append(", token=");
    localStringBuilder.append(mToken);
    localStringBuilder.append(", surface=");
    localStringBuilder.append(mSurface);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public static abstract class Callback
  {
    public Callback() {}
    
    public void onPaused() {}
    
    public void onResumed() {}
    
    public void onStopped() {}
  }
}
