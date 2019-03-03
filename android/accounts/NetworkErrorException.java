package android.accounts;

public class NetworkErrorException
  extends AccountsException
{
  public NetworkErrorException() {}
  
  public NetworkErrorException(String paramString)
  {
    super(paramString);
  }
  
  public NetworkErrorException(String paramString, Throwable paramThrowable)
  {
    super(paramString, paramThrowable);
  }
  
  public NetworkErrorException(Throwable paramThrowable)
  {
    super(paramThrowable);
  }
}
