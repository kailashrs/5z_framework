package android.net.lowpan;

public class OperationCanceledException
  extends LowpanException
{
  public OperationCanceledException() {}
  
  protected OperationCanceledException(Exception paramException)
  {
    super(paramException);
  }
  
  public OperationCanceledException(String paramString)
  {
    super(paramString);
  }
  
  public OperationCanceledException(String paramString, Throwable paramThrowable)
  {
    super(paramString, paramThrowable);
  }
}
