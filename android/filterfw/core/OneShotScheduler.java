package android.filterfw.core;

import android.util.Log;
import java.util.HashMap;

public class OneShotScheduler
  extends RoundRobinScheduler
{
  private static final String TAG = "OneShotScheduler";
  private final boolean mLogVerbose = Log.isLoggable("OneShotScheduler", 2);
  private HashMap<String, Integer> scheduled = new HashMap();
  
  public OneShotScheduler(FilterGraph paramFilterGraph)
  {
    super(paramFilterGraph);
  }
  
  public void reset()
  {
    super.reset();
    scheduled.clear();
  }
  
  public Filter scheduleNextNode()
  {
    Object localObject2;
    for (Object localObject1 = null;; localObject1 = localObject2)
    {
      Filter localFilter = super.scheduleNextNode();
      if (localFilter == null)
      {
        if (mLogVerbose) {
          Log.v("OneShotScheduler", "No filters available to run.");
        }
        return null;
      }
      if (!scheduled.containsKey(localFilter.getName()))
      {
        if (localFilter.getNumberOfConnectedInputs() == 0) {
          scheduled.put(localFilter.getName(), Integer.valueOf(1));
        }
        if (mLogVerbose)
        {
          localObject1 = new StringBuilder();
          ((StringBuilder)localObject1).append("Scheduling filter \"");
          ((StringBuilder)localObject1).append(localFilter.getName());
          ((StringBuilder)localObject1).append("\" of type ");
          ((StringBuilder)localObject1).append(localFilter.getFilterClassName());
          Log.v("OneShotScheduler", ((StringBuilder)localObject1).toString());
        }
        return localFilter;
      }
      if (localObject1 == localFilter)
      {
        if (mLogVerbose) {
          Log.v("OneShotScheduler", "One pass through graph completed.");
        }
        return null;
      }
      localObject2 = localObject1;
      if (localObject1 == null) {
        localObject2 = localFilter;
      }
    }
  }
}
