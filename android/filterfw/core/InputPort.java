package android.filterfw.core;

public abstract class InputPort
  extends FilterPort
{
  protected OutputPort mSourcePort;
  
  public InputPort(Filter paramFilter, String paramString)
  {
    super(paramFilter, paramString);
  }
  
  public boolean acceptsFrame()
  {
    return hasFrame() ^ true;
  }
  
  public void close()
  {
    if ((mSourcePort != null) && (mSourcePort.isOpen())) {
      mSourcePort.close();
    }
    super.close();
  }
  
  public boolean filterMustClose()
  {
    boolean bool;
    if ((!isOpen()) && (isBlocking()) && (!hasFrame())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public Filter getSourceFilter()
  {
    Filter localFilter;
    if (mSourcePort == null) {
      localFilter = null;
    } else {
      localFilter = mSourcePort.getFilter();
    }
    return localFilter;
  }
  
  public FrameFormat getSourceFormat()
  {
    FrameFormat localFrameFormat;
    if (mSourcePort != null) {
      localFrameFormat = mSourcePort.getPortFormat();
    } else {
      localFrameFormat = getPortFormat();
    }
    return localFrameFormat;
  }
  
  public OutputPort getSourcePort()
  {
    return mSourcePort;
  }
  
  public Object getTarget()
  {
    return null;
  }
  
  public boolean isConnected()
  {
    boolean bool;
    if (mSourcePort != null) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isReady()
  {
    boolean bool;
    if ((!hasFrame()) && (isBlocking())) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public void open()
  {
    super.open();
    if ((mSourcePort != null) && (!mSourcePort.isOpen())) {
      mSourcePort.open();
    }
  }
  
  public void setSourcePort(OutputPort paramOutputPort)
  {
    if (mSourcePort == null)
    {
      mSourcePort = paramOutputPort;
      return;
    }
    paramOutputPort = new StringBuilder();
    paramOutputPort.append(this);
    paramOutputPort.append(" already connected to ");
    paramOutputPort.append(mSourcePort);
    paramOutputPort.append("!");
    throw new RuntimeException(paramOutputPort.toString());
  }
  
  public abstract void transfer(FilterContext paramFilterContext);
}
