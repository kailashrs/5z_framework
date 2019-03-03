package android.accounts;

public class OperationCanceledException
  extends AccountsException
{
  public OperationCanceledException() {}
  
  public OperationCanceledException(String paramString)
  {
    super(paramString);
  }
  
  public OperationCanceledException(String paramString, Throwable paramThrowable)
  {
    super(paramString, paramThrowable);
  }
  
  public OperationCanceledException(Throwable paramThrowable)
  {
    super(paramThrowable);
  }
}
