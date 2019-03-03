package android.filterfw.core;

public class OutputPort
  extends FilterPort
{
  protected InputPort mBasePort;
  protected InputPort mTargetPort;
  
  public OutputPort(Filter paramFilter, String paramString)
  {
    super(paramFilter, paramString);
  }
  
  public void clear()
  {
    if (mTargetPort != null) {
      mTargetPort.clear();
    }
  }
  
  public void close()
  {
    super.close();
    if ((mTargetPort != null) && (mTargetPort.isOpen())) {
      mTargetPort.close();
    }
  }
  
  public void connectTo(InputPort paramInputPort)
  {
    if (mTargetPort == null)
    {
      mTargetPort = paramInputPort;
      mTargetPort.setSourcePort(this);
      return;
    }
    paramInputPort = new StringBuilder();
    paramInputPort.append(this);
    paramInputPort.append(" already connected to ");
    paramInputPort.append(mTargetPort);
    paramInputPort.append("!");
    throw new RuntimeException(paramInputPort.toString());
  }
  
  public boolean filterMustClose()
  {
    boolean bool;
    if ((!isOpen()) && (isBlocking())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public InputPort getBasePort()
  {
    return mBasePort;
  }
  
  public Filter getTargetFilter()
  {
    Filter localFilter;
    if (mTargetPort == null) {
      localFilter = null;
    } else {
      localFilter = mTargetPort.getFilter();
    }
    return localFilter;
  }
  
  public InputPort getTargetPort()
  {
    return mTargetPort;
  }
  
  public boolean hasFrame()
  {
    boolean bool;
    if (mTargetPort == null) {
      bool = false;
    } else {
      bool = mTargetPort.hasFrame();
    }
    return bool;
  }
  
  public boolean isConnected()
  {
    boolean bool;
    if (mTargetPort != null) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isReady()
  {
    boolean bool;
    if (((isOpen()) && (mTargetPort.acceptsFrame())) || (!isBlocking())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void open()
  {
    super.open();
    if ((mTargetPort != null) && (!mTargetPort.isOpen())) {
      mTargetPort.open();
    }
  }
  
  public Frame pullFrame()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Cannot pull frame on ");
    localStringBuilder.append(this);
    localStringBuilder.append("!");
    throw new RuntimeException(localStringBuilder.toString());
  }
  
  public void pushFrame(Frame paramFrame)
  {
    if (mTargetPort != null)
    {
      mTargetPort.pushFrame(paramFrame);
      return;
    }
    paramFrame = new StringBuilder();
    paramFrame.append("Attempting to push frame on unconnected port: ");
    paramFrame.append(this);
    paramFrame.append("!");
    throw new RuntimeException(paramFrame.toString());
  }
  
  public void setBasePort(InputPort paramInputPort)
  {
    mBasePort = paramInputPort;
  }
  
  public void setFrame(Frame paramFrame)
  {
    assertPortIsOpen();
    if (mTargetPort != null)
    {
      mTargetPort.setFrame(paramFrame);
      return;
    }
    paramFrame = new StringBuilder();
    paramFrame.append("Attempting to set frame on unconnected port: ");
    paramFrame.append(this);
    paramFrame.append("!");
    throw new RuntimeException(paramFrame.toString());
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("output ");
    localStringBuilder.append(super.toString());
    return localStringBuilder.toString();
  }
}
