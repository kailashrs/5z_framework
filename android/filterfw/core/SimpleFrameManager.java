package android.filterfw.core;

public class SimpleFrameManager
  extends FrameManager
{
  public SimpleFrameManager() {}
  
  private Frame createNewFrame(FrameFormat paramFrameFormat)
  {
    switch (paramFrameFormat.getTarget())
    {
    default: 
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Unsupported frame target type: ");
      localStringBuilder.append(FrameFormat.targetToString(paramFrameFormat.getTarget()));
      localStringBuilder.append("!");
      throw new RuntimeException(localStringBuilder.toString());
    case 4: 
      paramFrameFormat = new VertexFrame(paramFrameFormat, this);
      break;
    case 3: 
      paramFrameFormat = new GLFrame(paramFrameFormat, this);
      paramFrameFormat.init(getGLEnvironment());
      break;
    case 2: 
      paramFrameFormat = new NativeFrame(paramFrameFormat, this);
      break;
    case 1: 
      paramFrameFormat = new SimpleFrame(paramFrameFormat, this);
    }
    return paramFrameFormat;
  }
  
  public Frame newBoundFrame(FrameFormat paramFrameFormat, int paramInt, long paramLong)
  {
    if (paramFrameFormat.getTarget() == 3)
    {
      paramFrameFormat = new GLFrame(paramFrameFormat, this, paramInt, paramLong);
      paramFrameFormat.init(getGLEnvironment());
      return paramFrameFormat;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Attached frames are not supported for target type: ");
    localStringBuilder.append(FrameFormat.targetToString(paramFrameFormat.getTarget()));
    localStringBuilder.append("!");
    throw new RuntimeException(localStringBuilder.toString());
  }
  
  public Frame newFrame(FrameFormat paramFrameFormat)
  {
    return createNewFrame(paramFrameFormat);
  }
  
  public Frame releaseFrame(Frame paramFrame)
  {
    int i = paramFrame.decRefCount();
    if ((i == 0) && (paramFrame.hasNativeAllocation()))
    {
      paramFrame.releaseNativeAllocation();
      return null;
    }
    if (i >= 0) {
      return paramFrame;
    }
    throw new RuntimeException("Frame reference count dropped below 0!");
  }
  
  public Frame retainFrame(Frame paramFrame)
  {
    paramFrame.incRefCount();
    return paramFrame;
  }
}
