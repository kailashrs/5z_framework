package android.os;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class SynchronousResultReceiver
  extends ResultReceiver
{
  private final CompletableFuture<Result> mFuture = new CompletableFuture();
  private final String mName;
  
  public SynchronousResultReceiver()
  {
    super((Handler)null);
    mName = null;
  }
  
  public SynchronousResultReceiver(String paramString)
  {
    super((Handler)null);
    mName = paramString;
  }
  
  public Result awaitResult(long paramLong)
    throws TimeoutException
  {
    long l1 = System.currentTimeMillis();
    long l2 = paramLong;
    while (l2 >= 0L) {
      try
      {
        Result localResult = (Result)mFuture.get(l2, TimeUnit.MILLISECONDS);
        return localResult;
      }
      catch (InterruptedException localInterruptedException)
      {
        l2 -= l1 + paramLong - System.currentTimeMillis();
      }
      catch (ExecutionException localExecutionException)
      {
        throw new AssertionError("Error receiving response", localExecutionException);
      }
    }
    throw new TimeoutException();
  }
  
  public String getName()
  {
    return mName;
  }
  
  protected final void onReceiveResult(int paramInt, Bundle paramBundle)
  {
    super.onReceiveResult(paramInt, paramBundle);
    mFuture.complete(new Result(paramInt, paramBundle));
  }
  
  public static class Result
  {
    public Bundle bundle;
    public int resultCode;
    
    public Result(int paramInt, Bundle paramBundle)
    {
      resultCode = paramInt;
      bundle = paramBundle;
    }
  }
}
