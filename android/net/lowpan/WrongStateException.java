package android.net.lowpan;

public class WrongStateException
  extends LowpanException
{
  public WrongStateException() {}
  
  protected WrongStateException(Exception paramException)
  {
    super(paramException);
  }
  
  public WrongStateException(String paramString)
  {
    super(paramString);
  }
  
  public WrongStateException(String paramString, Throwable paramThrowable)
  {
    super(paramString, paramThrowable);
  }
}
