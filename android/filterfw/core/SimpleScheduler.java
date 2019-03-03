package android.filterfw.core;

import java.util.Iterator;
import java.util.Set;

public class SimpleScheduler
  extends Scheduler
{
  public SimpleScheduler(FilterGraph paramFilterGraph)
  {
    super(paramFilterGraph);
  }
  
  public void reset() {}
  
  public Filter scheduleNextNode()
  {
    Iterator localIterator = getGraph().getFilters().iterator();
    while (localIterator.hasNext())
    {
      Filter localFilter = (Filter)localIterator.next();
      if (localFilter.canProcess()) {
        return localFilter;
      }
    }
    return null;
  }
}
