package android.filterfw.core;

import android.util.Log;

public abstract class FilterPort
{
  private static final String TAG = "FilterPort";
  protected boolean mChecksType = false;
  protected Filter mFilter;
  protected boolean mIsBlocking = true;
  protected boolean mIsOpen = false;
  private boolean mLogVerbose;
  protected String mName;
  protected FrameFormat mPortFormat;
  
  public FilterPort(Filter paramFilter, String paramString)
  {
    mName = paramString;
    mFilter = paramFilter;
    mLogVerbose = Log.isLoggable("FilterPort", 2);
  }
  
  protected void assertPortIsOpen()
  {
    if (isOpen()) {
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Illegal operation on closed ");
    localStringBuilder.append(this);
    localStringBuilder.append("!");
    throw new RuntimeException(localStringBuilder.toString());
  }
  
  protected void checkFrameManager(Frame paramFrame, FilterContext paramFilterContext)
  {
    if ((paramFrame.getFrameManager() != null) && (paramFrame.getFrameManager() != paramFilterContext.getFrameManager()))
    {
      paramFilterContext = new StringBuilder();
      paramFilterContext.append("Frame ");
      paramFilterContext.append(paramFrame);
      paramFilterContext.append(" is managed by foreign FrameManager! ");
      throw new RuntimeException(paramFilterContext.toString());
    }
  }
  
  protected void checkFrameType(Frame paramFrame, boolean paramBoolean)
  {
    if (((mChecksType) || (paramBoolean)) && (mPortFormat != null) && (!paramFrame.getFormat().isCompatibleWith(mPortFormat)))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Frame passed to ");
      localStringBuilder.append(this);
      localStringBuilder.append(" is of incorrect type! Expected ");
      localStringBuilder.append(mPortFormat);
      localStringBuilder.append(" but got ");
      localStringBuilder.append(paramFrame.getFormat());
      throw new RuntimeException(localStringBuilder.toString());
    }
  }
  
  public abstract void clear();
  
  public void close()
  {
    if ((mIsOpen) && (mLogVerbose))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Closing ");
      localStringBuilder.append(this);
      Log.v("FilterPort", localStringBuilder.toString());
    }
    mIsOpen = false;
  }
  
  public abstract boolean filterMustClose();
  
  public Filter getFilter()
  {
    return mFilter;
  }
  
  public String getName()
  {
    return mName;
  }
  
  public FrameFormat getPortFormat()
  {
    return mPortFormat;
  }
  
  public abstract boolean hasFrame();
  
  public boolean isAttached()
  {
    boolean bool;
    if (mFilter != null) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isBlocking()
  {
    return mIsBlocking;
  }
  
  public boolean isOpen()
  {
    return mIsOpen;
  }
  
  public abstract boolean isReady();
  
  public void open()
  {
    if ((!mIsOpen) && (mLogVerbose))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Opening ");
      localStringBuilder.append(this);
      Log.v("FilterPort", localStringBuilder.toString());
    }
    mIsOpen = true;
  }
  
  public abstract Frame pullFrame();
  
  public abstract void pushFrame(Frame paramFrame);
  
  public void setBlocking(boolean paramBoolean)
  {
    mIsBlocking = paramBoolean;
  }
  
  public void setChecksType(boolean paramBoolean)
  {
    mChecksType = paramBoolean;
  }
  
  public abstract void setFrame(Frame paramFrame);
  
  public void setPortFormat(FrameFormat paramFrameFormat)
  {
    mPortFormat = paramFrameFormat;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("port '");
    localStringBuilder.append(mName);
    localStringBuilder.append("' of ");
    localStringBuilder.append(mFilter);
    return localStringBuilder.toString();
  }
}
