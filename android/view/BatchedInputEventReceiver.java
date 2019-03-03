package android.view;

import android.os.Looper;

public class BatchedInputEventReceiver
  extends InputEventReceiver
{
  private final BatchedInputRunnable mBatchedInputRunnable = new BatchedInputRunnable(null);
  private boolean mBatchedInputScheduled;
  Choreographer mChoreographer;
  
  public BatchedInputEventReceiver(InputChannel paramInputChannel, Looper paramLooper, Choreographer paramChoreographer)
  {
    super(paramInputChannel, paramLooper);
    mChoreographer = paramChoreographer;
  }
  
  private void scheduleBatchedInput()
  {
    if (!mBatchedInputScheduled)
    {
      mBatchedInputScheduled = true;
      mChoreographer.postCallback(0, mBatchedInputRunnable, null);
    }
  }
  
  private void unscheduleBatchedInput()
  {
    if (mBatchedInputScheduled)
    {
      mBatchedInputScheduled = false;
      mChoreographer.removeCallbacks(0, mBatchedInputRunnable, null);
    }
  }
  
  public void dispose()
  {
    unscheduleBatchedInput();
    super.dispose();
  }
  
  void doConsumeBatchedInput(long paramLong)
  {
    if (mBatchedInputScheduled)
    {
      mBatchedInputScheduled = false;
      if ((consumeBatchedInputEvents(paramLong)) && (paramLong != -1L)) {
        scheduleBatchedInput();
      }
    }
  }
  
  public void onBatchedInputEventPending()
  {
    scheduleBatchedInput();
  }
  
  private final class BatchedInputRunnable
    implements Runnable
  {
    private BatchedInputRunnable() {}
    
    public void run()
    {
      doConsumeBatchedInput(mChoreographer.getFrameTimeNanos());
    }
  }
}
