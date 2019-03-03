package android.filterfw.core;

public class StreamPort
  extends InputPort
{
  private Frame mFrame;
  private boolean mPersistent;
  
  public StreamPort(Filter paramFilter, String paramString)
  {
    super(paramFilter, paramString);
  }
  
  protected void assignFrame(Frame paramFrame, boolean paramBoolean)
  {
    try
    {
      assertPortIsOpen();
      checkFrameType(paramFrame, paramBoolean);
      if (paramBoolean)
      {
        if (mFrame != null) {
          mFrame.release();
        }
      }
      else {
        if (mFrame != null) {
          break label64;
        }
      }
      mFrame = paramFrame.retain();
      mFrame.markReadOnly();
      mPersistent = paramBoolean;
      return;
      label64:
      RuntimeException localRuntimeException = new java/lang/RuntimeException;
      paramFrame = new java/lang/StringBuilder;
      paramFrame.<init>();
      paramFrame.append("Attempting to push more than one frame on port: ");
      paramFrame.append(this);
      paramFrame.append("!");
      localRuntimeException.<init>(paramFrame.toString());
      throw localRuntimeException;
    }
    finally {}
  }
  
  public void clear()
  {
    if (mFrame != null)
    {
      mFrame.release();
      mFrame = null;
    }
  }
  
  public boolean hasFrame()
  {
    try
    {
      Frame localFrame = mFrame;
      boolean bool;
      if (localFrame != null) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public Frame pullFrame()
  {
    try
    {
      if (mFrame != null)
      {
        localObject1 = mFrame;
        if (mPersistent) {
          mFrame.retain();
        } else {
          mFrame = null;
        }
        return localObject1;
      }
      Object localObject1 = new java/lang/RuntimeException;
      StringBuilder localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append("No frame available to pull on port: ");
      localStringBuilder.append(this);
      localStringBuilder.append("!");
      ((RuntimeException)localObject1).<init>(localStringBuilder.toString());
      throw ((Throwable)localObject1);
    }
    finally {}
  }
  
  public void pushFrame(Frame paramFrame)
  {
    assignFrame(paramFrame, false);
  }
  
  public void setFrame(Frame paramFrame)
  {
    assignFrame(paramFrame, true);
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("input ");
    localStringBuilder.append(super.toString());
    return localStringBuilder.toString();
  }
  
  public void transfer(FilterContext paramFilterContext)
  {
    try
    {
      if (mFrame != null) {
        checkFrameManager(mFrame, paramFilterContext);
      }
      return;
    }
    finally
    {
      paramFilterContext = finally;
      throw paramFilterContext;
    }
  }
}
