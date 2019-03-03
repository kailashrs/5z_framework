package android.filterfw.core;

import android.os.ConditionVariable;
import android.util.Log;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SyncRunner
  extends GraphRunner
{
  private static final String TAG = "SyncRunner";
  private GraphRunner.OnRunnerDoneListener mDoneListener = null;
  private final boolean mLogVerbose = Log.isLoggable("SyncRunner", 2);
  private Scheduler mScheduler = null;
  private StopWatchMap mTimer = null;
  private ConditionVariable mWakeCondition = new ConditionVariable();
  private ScheduledThreadPoolExecutor mWakeExecutor = new ScheduledThreadPoolExecutor(1);
  
  public SyncRunner(FilterContext paramFilterContext, FilterGraph paramFilterGraph, Class paramClass)
  {
    super(paramFilterContext);
    if (mLogVerbose) {
      Log.v("SyncRunner", "Initializing SyncRunner");
    }
    if (Scheduler.class.isAssignableFrom(paramClass)) {
      try
      {
        mScheduler = ((Scheduler)paramClass.getConstructor(new Class[] { FilterGraph.class }).newInstance(new Object[] { paramFilterGraph }));
        mFilterContext = paramFilterContext;
        mFilterContext.addGraph(paramFilterGraph);
        mTimer = new StopWatchMap();
        if (mLogVerbose) {
          Log.v("SyncRunner", "Setting up filters");
        }
        paramFilterGraph.setupFilters();
        return;
      }
      catch (Exception paramFilterContext)
      {
        throw new RuntimeException("Could not instantiate Scheduler", paramFilterContext);
      }
      catch (InvocationTargetException paramFilterContext)
      {
        throw new RuntimeException("Scheduler constructor threw an exception", paramFilterContext);
      }
      catch (IllegalAccessException paramFilterContext)
      {
        throw new RuntimeException("Cannot access Scheduler constructor!", paramFilterContext);
      }
      catch (InstantiationException paramFilterContext)
      {
        throw new RuntimeException("Could not instantiate the Scheduler instance!", paramFilterContext);
      }
      catch (NoSuchMethodException paramFilterContext)
      {
        throw new RuntimeException("Scheduler does not have constructor <init>(FilterGraph)!", paramFilterContext);
      }
    }
    throw new IllegalArgumentException("Class provided is not a Scheduler subclass!");
  }
  
  void assertReadyToStep()
  {
    if (mScheduler != null)
    {
      if (getGraph() != null) {
        return;
      }
      throw new RuntimeException("Calling step on scheduler with no graph in place!");
    }
    throw new RuntimeException("Attempting to run schedule with no scheduler in place!");
  }
  
  public void beginProcessing()
  {
    mScheduler.reset();
    getGraph().beginProcessing();
  }
  
  public void close()
  {
    if (mLogVerbose) {
      Log.v("SyncRunner", "Closing graph.");
    }
    getGraph().closeFilters(mFilterContext);
    mScheduler.reset();
  }
  
  protected int determinePostRunState()
  {
    Iterator localIterator = mScheduler.getGraph().getFilters().iterator();
    while (localIterator.hasNext())
    {
      Filter localFilter = (Filter)localIterator.next();
      if (localFilter.isOpen())
      {
        if (localFilter.getStatus() == 4) {
          return 3;
        }
        return 4;
      }
    }
    return 2;
  }
  
  public Exception getError()
  {
    return null;
  }
  
  public FilterGraph getGraph()
  {
    FilterGraph localFilterGraph;
    if (mScheduler != null) {
      localFilterGraph = mScheduler.getGraph();
    } else {
      localFilterGraph = null;
    }
    return localFilterGraph;
  }
  
  public boolean isRunning()
  {
    return false;
  }
  
  boolean performStep()
  {
    if (mLogVerbose) {
      Log.v("SyncRunner", "Performing one step.");
    }
    Filter localFilter = mScheduler.scheduleNextNode();
    if (localFilter != null)
    {
      mTimer.start(localFilter.getName());
      processFilterNode(localFilter);
      mTimer.stop(localFilter.getName());
      return true;
    }
    return false;
  }
  
  protected void processFilterNode(Filter paramFilter)
  {
    if (mLogVerbose) {
      Log.v("SyncRunner", "Processing filter node");
    }
    paramFilter.performProcess(mFilterContext);
    if (paramFilter.getStatus() != 6)
    {
      if (paramFilter.getStatus() == 4)
      {
        if (mLogVerbose) {
          Log.v("SyncRunner", "Scheduling filter wakeup");
        }
        scheduleFilterWake(paramFilter, paramFilter.getSleepDelay());
      }
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("There was an error executing ");
    localStringBuilder.append(paramFilter);
    localStringBuilder.append("!");
    throw new RuntimeException(localStringBuilder.toString());
  }
  
  public void run()
  {
    if (mLogVerbose) {
      Log.v("SyncRunner", "Beginning run.");
    }
    assertReadyToStep();
    beginProcessing();
    boolean bool1 = activateGlContext();
    for (boolean bool2 = true; bool2; bool2 = performStep()) {}
    if (bool1) {
      deactivateGlContext();
    }
    if (mDoneListener != null)
    {
      if (mLogVerbose) {
        Log.v("SyncRunner", "Calling completion listener.");
      }
      mDoneListener.onRunnerDone(determinePostRunState());
    }
    if (mLogVerbose) {
      Log.v("SyncRunner", "Run complete");
    }
  }
  
  protected void scheduleFilterWake(final Filter paramFilter, int paramInt)
  {
    mWakeCondition.close();
    final ConditionVariable localConditionVariable = mWakeCondition;
    mWakeExecutor.schedule(new Runnable()
    {
      public void run()
      {
        paramFilter.unsetStatus(4);
        localConditionVariable.open();
      }
    }, paramInt, TimeUnit.MILLISECONDS);
  }
  
  public void setDoneCallback(GraphRunner.OnRunnerDoneListener paramOnRunnerDoneListener)
  {
    mDoneListener = paramOnRunnerDoneListener;
  }
  
  public int step()
  {
    assertReadyToStep();
    if (getGraph().isReady())
    {
      int i;
      if (performStep()) {
        i = 1;
      } else {
        i = determinePostRunState();
      }
      return i;
    }
    throw new RuntimeException("Trying to process graph that is not open!");
  }
  
  public void stop()
  {
    throw new RuntimeException("SyncRunner does not support stopping a graph!");
  }
  
  protected void waitUntilWake()
  {
    mWakeCondition.block();
  }
}
