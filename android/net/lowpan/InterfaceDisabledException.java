package android.net.lowpan;

public class InterfaceDisabledException
  extends LowpanException
{
  public InterfaceDisabledException() {}
  
  protected InterfaceDisabledException(Exception paramException)
  {
    super(paramException);
  }
  
  public InterfaceDisabledException(String paramString)
  {
    super(paramString);
  }
  
  public InterfaceDisabledException(String paramString, Throwable paramThrowable)
  {
    super(paramString, paramThrowable);
  }
}
