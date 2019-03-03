package android.filterfw.core;

public abstract class GraphRunner
{
  public static final int RESULT_BLOCKED = 4;
  public static final int RESULT_ERROR = 6;
  public static final int RESULT_FINISHED = 2;
  public static final int RESULT_RUNNING = 1;
  public static final int RESULT_SLEEPING = 3;
  public static final int RESULT_STOPPED = 5;
  public static final int RESULT_UNKNOWN = 0;
  protected FilterContext mFilterContext = null;
  
  public GraphRunner(FilterContext paramFilterContext)
  {
    mFilterContext = paramFilterContext;
  }
  
  protected boolean activateGlContext()
  {
    GLEnvironment localGLEnvironment = mFilterContext.getGLEnvironment();
    if ((localGLEnvironment != null) && (!localGLEnvironment.isActive()))
    {
      localGLEnvironment.activate();
      return true;
    }
    return false;
  }
  
  public abstract void close();
  
  protected void deactivateGlContext()
  {
    GLEnvironment localGLEnvironment = mFilterContext.getGLEnvironment();
    if (localGLEnvironment != null) {
      localGLEnvironment.deactivate();
    }
  }
  
  public FilterContext getContext()
  {
    return mFilterContext;
  }
  
  public abstract Exception getError();
  
  public abstract FilterGraph getGraph();
  
  public abstract boolean isRunning();
  
  public abstract void run();
  
  public abstract void setDoneCallback(OnRunnerDoneListener paramOnRunnerDoneListener);
  
  public abstract void stop();
  
  public static abstract interface OnRunnerDoneListener
  {
    public abstract void onRunnerDone(int paramInt);
  }
}
