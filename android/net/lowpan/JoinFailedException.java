package android.net.lowpan;

public class JoinFailedException
  extends LowpanException
{
  public JoinFailedException() {}
  
  protected JoinFailedException(Exception paramException)
  {
    super(paramException);
  }
  
  public JoinFailedException(String paramString)
  {
    super(paramString);
  }
  
  public JoinFailedException(String paramString, Throwable paramThrowable)
  {
    super(paramString, paramThrowable);
  }
}
