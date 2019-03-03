package android.filterfw.core;

public abstract class Scheduler
{
  private FilterGraph mGraph;
  
  Scheduler(FilterGraph paramFilterGraph)
  {
    mGraph = paramFilterGraph;
  }
  
  boolean finished()
  {
    return true;
  }
  
  FilterGraph getGraph()
  {
    return mGraph;
  }
  
  abstract void reset();
  
  abstract Filter scheduleNextNode();
}
