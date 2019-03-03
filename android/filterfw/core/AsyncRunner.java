package android.filterfw.core;

import android.os.AsyncTask;
import android.util.Log;

public class AsyncRunner
  extends GraphRunner
{
  private static final String TAG = "AsyncRunner";
  private boolean isProcessing;
  private GraphRunner.OnRunnerDoneListener mDoneListener;
  private Exception mException;
  private boolean mLogVerbose;
  private AsyncRunnerTask mRunTask;
  private SyncRunner mRunner;
  private Class mSchedulerClass;
  
  public AsyncRunner(FilterContext paramFilterContext)
  {
    super(paramFilterContext);
    mSchedulerClass = SimpleScheduler.class;
    mLogVerbose = Log.isLoggable("AsyncRunner", 2);
  }
  
  public AsyncRunner(FilterContext paramFilterContext, Class paramClass)
  {
    super(paramFilterContext);
    mSchedulerClass = paramClass;
    mLogVerbose = Log.isLoggable("AsyncRunner", 2);
  }
  
  private void setException(Exception paramException)
  {
    try
    {
      mException = paramException;
      return;
    }
    finally
    {
      paramException = finally;
      throw paramException;
    }
  }
  
  private void setRunning(boolean paramBoolean)
  {
    try
    {
      isProcessing = paramBoolean;
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public void close()
  {
    try
    {
      if (!isRunning())
      {
        if (mLogVerbose) {
          Log.v("AsyncRunner", "Closing filters.");
        }
        mRunner.close();
        return;
      }
      RuntimeException localRuntimeException = new java/lang/RuntimeException;
      localRuntimeException.<init>("Cannot close graph while it is running!");
      throw localRuntimeException;
    }
    finally {}
  }
  
  public Exception getError()
  {
    try
    {
      Exception localException = mException;
      return localException;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public FilterGraph getGraph()
  {
    FilterGraph localFilterGraph;
    if (mRunner != null) {
      localFilterGraph = mRunner.getGraph();
    } else {
      localFilterGraph = null;
    }
    return localFilterGraph;
  }
  
  public boolean isRunning()
  {
    try
    {
      boolean bool = isProcessing;
      return bool;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public void run()
  {
    try
    {
      if (mLogVerbose) {
        Log.v("AsyncRunner", "Running graph.");
      }
      setException(null);
      if (!isRunning())
      {
        if (mRunner != null)
        {
          localObject1 = new android/filterfw/core/AsyncRunner$AsyncRunnerTask;
          ((AsyncRunnerTask)localObject1).<init>(this, null);
          mRunTask = ((AsyncRunnerTask)localObject1);
          setRunning(true);
          mRunTask.execute(new SyncRunner[] { mRunner });
          return;
        }
        localObject1 = new java/lang/RuntimeException;
        ((RuntimeException)localObject1).<init>("Cannot run before a graph is set!");
        throw ((Throwable)localObject1);
      }
      Object localObject1 = new java/lang/RuntimeException;
      ((RuntimeException)localObject1).<init>("Graph is already running!");
      throw ((Throwable)localObject1);
    }
    finally {}
  }
  
  public void setDoneCallback(GraphRunner.OnRunnerDoneListener paramOnRunnerDoneListener)
  {
    mDoneListener = paramOnRunnerDoneListener;
  }
  
  public void setGraph(FilterGraph paramFilterGraph)
  {
    try
    {
      if (!isRunning())
      {
        SyncRunner localSyncRunner = new android/filterfw/core/SyncRunner;
        localSyncRunner.<init>(mFilterContext, paramFilterGraph, mSchedulerClass);
        mRunner = localSyncRunner;
        return;
      }
      paramFilterGraph = new java/lang/RuntimeException;
      paramFilterGraph.<init>("Graph is already running!");
      throw paramFilterGraph;
    }
    finally {}
  }
  
  public void stop()
  {
    try
    {
      if ((mRunTask != null) && (!mRunTask.isCancelled()))
      {
        if (mLogVerbose) {
          Log.v("AsyncRunner", "Stopping graph.");
        }
        mRunTask.cancel(false);
      }
      return;
    }
    finally {}
  }
  
  private class AsyncRunnerTask
    extends AsyncTask<SyncRunner, Void, AsyncRunner.RunnerResult>
  {
    private static final String TAG = "AsyncRunnerTask";
    
    private AsyncRunnerTask() {}
    
    protected AsyncRunner.RunnerResult doInBackground(SyncRunner... paramVarArgs)
    {
      AsyncRunner.RunnerResult localRunnerResult = new AsyncRunner.RunnerResult(AsyncRunner.this, null);
      try
      {
        if (paramVarArgs.length <= 1)
        {
          paramVarArgs[0].assertReadyToStep();
          if (mLogVerbose) {
            Log.v("AsyncRunnerTask", "Starting background graph processing.");
          }
          activateGlContext();
          if (mLogVerbose) {
            Log.v("AsyncRunnerTask", "Preparing filter graph for processing.");
          }
          paramVarArgs[0].beginProcessing();
          if (mLogVerbose) {
            Log.v("AsyncRunnerTask", "Running graph.");
          }
          status = 1;
          while ((!isCancelled()) && (status == 1)) {
            if (!paramVarArgs[0].performStep())
            {
              status = paramVarArgs[0].determinePostRunState();
              if (status == 3)
              {
                paramVarArgs[0].waitUntilWake();
                status = 1;
              }
            }
          }
          if (isCancelled()) {
            status = 5;
          }
        }
        else
        {
          paramVarArgs = new java/lang/RuntimeException;
          paramVarArgs.<init>("More than one runner received!");
          throw paramVarArgs;
        }
      }
      catch (Exception paramVarArgs)
      {
        exception = paramVarArgs;
        status = 6;
        try
        {
          deactivateGlContext();
        }
        catch (Exception paramVarArgs)
        {
          exception = paramVarArgs;
          status = 6;
        }
        if (mLogVerbose) {
          Log.v("AsyncRunnerTask", "Done with background graph processing.");
        }
      }
      return localRunnerResult;
    }
    
    protected void onCancelled(AsyncRunner.RunnerResult paramRunnerResult)
    {
      onPostExecute(paramRunnerResult);
    }
    
    protected void onPostExecute(AsyncRunner.RunnerResult paramRunnerResult)
    {
      if (mLogVerbose) {
        Log.v("AsyncRunnerTask", "Starting post-execute.");
      }
      AsyncRunner.this.setRunning(false);
      AsyncRunner.RunnerResult localRunnerResult = paramRunnerResult;
      if (paramRunnerResult == null)
      {
        localRunnerResult = new AsyncRunner.RunnerResult(AsyncRunner.this, null);
        status = 5;
      }
      AsyncRunner.this.setException(exception);
      if ((status == 5) || (status == 6))
      {
        if (mLogVerbose) {
          Log.v("AsyncRunnerTask", "Closing filters.");
        }
        try
        {
          mRunner.close();
        }
        catch (Exception paramRunnerResult)
        {
          status = 6;
          AsyncRunner.this.setException(paramRunnerResult);
        }
      }
      if (mDoneListener != null)
      {
        if (mLogVerbose) {
          Log.v("AsyncRunnerTask", "Calling graph done callback.");
        }
        mDoneListener.onRunnerDone(status);
      }
      if (mLogVerbose) {
        Log.v("AsyncRunnerTask", "Completed post-execute.");
      }
    }
  }
  
  private class RunnerResult
  {
    public Exception exception;
    public int status = 0;
    
    private RunnerResult() {}
  }
}
