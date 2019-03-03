package android.hardware.camera2.utils;

import java.util.concurrent.Executor;

public class TaskSingleDrainer
{
  private final Object mSingleTask = new Object();
  private final TaskDrainer<Object> mTaskDrainer;
  
  public TaskSingleDrainer(Executor paramExecutor, TaskDrainer.DrainListener paramDrainListener)
  {
    mTaskDrainer = new TaskDrainer(paramExecutor, paramDrainListener);
  }
  
  public TaskSingleDrainer(Executor paramExecutor, TaskDrainer.DrainListener paramDrainListener, String paramString)
  {
    mTaskDrainer = new TaskDrainer(paramExecutor, paramDrainListener, paramString);
  }
  
  public void beginDrain()
  {
    mTaskDrainer.beginDrain();
  }
  
  public void taskFinished()
  {
    mTaskDrainer.taskFinished(mSingleTask);
  }
  
  public void taskStarted()
  {
    mTaskDrainer.taskStarted(mSingleTask);
  }
}
