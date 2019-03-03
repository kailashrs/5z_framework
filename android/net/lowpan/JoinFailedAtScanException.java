package android.net.lowpan;

public class JoinFailedAtScanException
  extends JoinFailedException
{
  public JoinFailedAtScanException() {}
  
  public JoinFailedAtScanException(Exception paramException)
  {
    super(paramException);
  }
  
  public JoinFailedAtScanException(String paramString)
  {
    super(paramString);
  }
  
  public JoinFailedAtScanException(String paramString, Throwable paramThrowable)
  {
    super(paramString, paramThrowable);
  }
}
