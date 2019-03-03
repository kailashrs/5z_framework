package android.os;

import com.android.internal.util.Preconditions;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;

public class HandlerExecutor
  implements Executor
{
  private final Handler mHandler;
  
  public HandlerExecutor(Handler paramHandler)
  {
    mHandler = ((Handler)Preconditions.checkNotNull(paramHandler));
  }
  
  public void execute(Runnable paramRunnable)
  {
    if (mHandler.post(paramRunnable)) {
      return;
    }
    paramRunnable = new StringBuilder();
    paramRunnable.append(mHandler);
    paramRunnable.append(" is shutting down");
    throw new RejectedExecutionException(paramRunnable.toString());
  }
}
