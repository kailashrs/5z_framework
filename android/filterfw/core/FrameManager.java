package android.filterfw.core;

public abstract class FrameManager
{
  private FilterContext mContext;
  
  public FrameManager() {}
  
  public Frame duplicateFrame(Frame paramFrame)
  {
    Frame localFrame = newFrame(paramFrame.getFormat());
    localFrame.setDataFromFrame(paramFrame);
    return localFrame;
  }
  
  public Frame duplicateFrameToTarget(Frame paramFrame, int paramInt)
  {
    Object localObject = paramFrame.getFormat().mutableCopy();
    ((MutableFrameFormat)localObject).setTarget(paramInt);
    localObject = newFrame((FrameFormat)localObject);
    ((Frame)localObject).setDataFromFrame(paramFrame);
    return localObject;
  }
  
  public FilterContext getContext()
  {
    return mContext;
  }
  
  public GLEnvironment getGLEnvironment()
  {
    GLEnvironment localGLEnvironment;
    if (mContext != null) {
      localGLEnvironment = mContext.getGLEnvironment();
    } else {
      localGLEnvironment = null;
    }
    return localGLEnvironment;
  }
  
  public abstract Frame newBoundFrame(FrameFormat paramFrameFormat, int paramInt, long paramLong);
  
  public abstract Frame newFrame(FrameFormat paramFrameFormat);
  
  public abstract Frame releaseFrame(Frame paramFrame);
  
  public abstract Frame retainFrame(Frame paramFrame);
  
  void setContext(FilterContext paramFilterContext)
  {
    mContext = paramFilterContext;
  }
  
  public void tearDown() {}
}
