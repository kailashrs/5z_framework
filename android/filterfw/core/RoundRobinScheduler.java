package android.filterfw.core;

import java.util.Iterator;
import java.util.Set;

public class RoundRobinScheduler
  extends Scheduler
{
  private int mLastPos = -1;
  
  public RoundRobinScheduler(FilterGraph paramFilterGraph)
  {
    super(paramFilterGraph);
  }
  
  public void reset()
  {
    mLastPos = -1;
  }
  
  public Filter scheduleNextNode()
  {
    Object localObject1 = getGraph().getFilters();
    if (mLastPos >= ((Set)localObject1).size()) {
      mLastPos = -1;
    }
    int i = 0;
    Object localObject2 = null;
    int j = -1;
    Iterator localIterator = ((Set)localObject1).iterator();
    while (localIterator.hasNext())
    {
      Filter localFilter = (Filter)localIterator.next();
      localObject1 = localObject2;
      int k = j;
      if (localFilter.canProcess()) {
        if (i <= mLastPos)
        {
          localObject1 = localObject2;
          k = j;
          if (localObject2 == null)
          {
            localObject1 = localFilter;
            k = i;
          }
        }
        else
        {
          mLastPos = i;
          return localFilter;
        }
      }
      i++;
      localObject2 = localObject1;
      j = k;
    }
    if (localObject2 != null)
    {
      mLastPos = j;
      return localObject2;
    }
    return null;
  }
}
