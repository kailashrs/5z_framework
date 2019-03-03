package android.filterfw.core;

import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.Vector;

public class RandomScheduler
  extends Scheduler
{
  private Random mRand = new Random();
  
  public RandomScheduler(FilterGraph paramFilterGraph)
  {
    super(paramFilterGraph);
  }
  
  public void reset() {}
  
  public Filter scheduleNextNode()
  {
    Vector localVector = new Vector();
    Iterator localIterator = getGraph().getFilters().iterator();
    while (localIterator.hasNext())
    {
      Filter localFilter = (Filter)localIterator.next();
      if (localFilter.canProcess()) {
        localVector.add(localFilter);
      }
    }
    if (localVector.size() > 0) {
      return (Filter)localVector.elementAt(mRand.nextInt(localVector.size()));
    }
    return null;
  }
}
