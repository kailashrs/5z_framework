package android.filterfw.core;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class FilterContext
{
  private FrameManager mFrameManager;
  private GLEnvironment mGLEnvironment;
  private Set<FilterGraph> mGraphs = new HashSet();
  private HashMap<String, Frame> mStoredFrames = new HashMap();
  
  public FilterContext() {}
  
  final void addGraph(FilterGraph paramFilterGraph)
  {
    mGraphs.add(paramFilterGraph);
  }
  
  public Frame fetchFrame(String paramString)
  {
    try
    {
      paramString = (Frame)mStoredFrames.get(paramString);
      if (paramString != null) {
        paramString.onFrameFetch();
      }
      return paramString;
    }
    finally {}
  }
  
  public FrameManager getFrameManager()
  {
    return mFrameManager;
  }
  
  public GLEnvironment getGLEnvironment()
  {
    return mGLEnvironment;
  }
  
  public void initGLEnvironment(GLEnvironment paramGLEnvironment)
  {
    if (mGLEnvironment == null)
    {
      mGLEnvironment = paramGLEnvironment;
      return;
    }
    throw new RuntimeException("Attempting to re-initialize GL Environment for FilterContext!");
  }
  
  public void removeFrame(String paramString)
  {
    try
    {
      Frame localFrame = (Frame)mStoredFrames.get(paramString);
      if (localFrame != null)
      {
        mStoredFrames.remove(paramString);
        localFrame.release();
      }
      return;
    }
    finally {}
  }
  
  public void setFrameManager(FrameManager paramFrameManager)
  {
    if (paramFrameManager != null)
    {
      if (paramFrameManager.getContext() == null)
      {
        mFrameManager = paramFrameManager;
        mFrameManager.setContext(this);
        return;
      }
      throw new IllegalArgumentException("Attempting to set FrameManager which is already bound to another FilterContext!");
    }
    throw new NullPointerException("Attempting to set null FrameManager!");
  }
  
  public void storeFrame(String paramString, Frame paramFrame)
  {
    try
    {
      Frame localFrame = fetchFrame(paramString);
      if (localFrame != null) {
        localFrame.release();
      }
      paramFrame.onFrameStore();
      mStoredFrames.put(paramString, paramFrame.retain());
      return;
    }
    finally {}
  }
  
  public void tearDown()
  {
    try
    {
      Iterator localIterator = mStoredFrames.values().iterator();
      while (localIterator.hasNext()) {
        ((Frame)localIterator.next()).release();
      }
      mStoredFrames.clear();
      localIterator = mGraphs.iterator();
      while (localIterator.hasNext()) {
        ((FilterGraph)localIterator.next()).tearDown(this);
      }
      mGraphs.clear();
      if (mFrameManager != null)
      {
        mFrameManager.tearDown();
        mFrameManager = null;
      }
      if (mGLEnvironment != null)
      {
        mGLEnvironment.tearDown();
        mGLEnvironment = null;
      }
      return;
    }
    finally {}
  }
  
  public static abstract interface OnFrameReceivedListener
  {
    public abstract void onFrameReceived(Filter paramFilter, Frame paramFrame, Object paramObject);
  }
}
