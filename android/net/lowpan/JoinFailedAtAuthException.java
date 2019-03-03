package android.net.lowpan;

public class JoinFailedAtAuthException
  extends JoinFailedException
{
  public JoinFailedAtAuthException() {}
  
  public JoinFailedAtAuthException(Exception paramException)
  {
    super(paramException);
  }
  
  public JoinFailedAtAuthException(String paramString)
  {
    super(paramString);
  }
  
  public JoinFailedAtAuthException(String paramString, Throwable paramThrowable)
  {
    super(paramString, paramThrowable);
  }
}
