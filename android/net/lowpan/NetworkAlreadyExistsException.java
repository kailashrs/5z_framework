package android.net.lowpan;

public class NetworkAlreadyExistsException
  extends LowpanException
{
  public NetworkAlreadyExistsException() {}
  
  public NetworkAlreadyExistsException(Exception paramException)
  {
    super(paramException);
  }
  
  public NetworkAlreadyExistsException(String paramString)
  {
    super(paramString, null);
  }
  
  public NetworkAlreadyExistsException(String paramString, Throwable paramThrowable)
  {
    super(paramString, paramThrowable);
  }
}
