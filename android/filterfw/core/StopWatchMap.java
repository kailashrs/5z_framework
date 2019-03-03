package android.filterfw.core;

import java.util.HashMap;

public class StopWatchMap
{
  public boolean LOG_MFF_RUNNING_TIMES = false;
  private HashMap<String, StopWatch> mStopWatches = null;
  
  public StopWatchMap() {}
  
  public void start(String paramString)
  {
    if (!LOG_MFF_RUNNING_TIMES) {
      return;
    }
    if (!mStopWatches.containsKey(paramString)) {
      mStopWatches.put(paramString, new StopWatch(paramString));
    }
    ((StopWatch)mStopWatches.get(paramString)).start();
  }
  
  public void stop(String paramString)
  {
    if (!LOG_MFF_RUNNING_TIMES) {
      return;
    }
    if (mStopWatches.containsKey(paramString))
    {
      ((StopWatch)mStopWatches.get(paramString)).stop();
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Calling stop with unknown stopWatchName: ");
    localStringBuilder.append(paramString);
    throw new RuntimeException(localStringBuilder.toString());
  }
}
