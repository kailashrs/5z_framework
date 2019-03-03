package android.filterfw;

import android.filterfw.core.CachedFrameManager;
import android.filterfw.core.FilterContext;
import android.filterfw.core.FrameManager;
import android.filterfw.core.GLEnvironment;

public class MffEnvironment
{
  private FilterContext mContext;
  
  protected MffEnvironment(FrameManager paramFrameManager)
  {
    Object localObject = paramFrameManager;
    if (paramFrameManager == null) {
      localObject = new CachedFrameManager();
    }
    mContext = new FilterContext();
    mContext.setFrameManager((FrameManager)localObject);
  }
  
  public void activateGLEnvironment()
  {
    if (mContext.getGLEnvironment() != null)
    {
      mContext.getGLEnvironment().activate();
      return;
    }
    throw new NullPointerException("No GLEnvironment in place to activate!");
  }
  
  public void createGLEnvironment()
  {
    GLEnvironment localGLEnvironment = new GLEnvironment();
    localGLEnvironment.initWithNewContext();
    setGLEnvironment(localGLEnvironment);
  }
  
  public void deactivateGLEnvironment()
  {
    if (mContext.getGLEnvironment() != null)
    {
      mContext.getGLEnvironment().deactivate();
      return;
    }
    throw new NullPointerException("No GLEnvironment in place to deactivate!");
  }
  
  public FilterContext getContext()
  {
    return mContext;
  }
  
  public void setGLEnvironment(GLEnvironment paramGLEnvironment)
  {
    mContext.initGLEnvironment(paramGLEnvironment);
  }
}
