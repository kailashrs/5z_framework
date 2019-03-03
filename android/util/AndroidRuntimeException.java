package android.util;

public class AndroidRuntimeException
  extends RuntimeException
{
  public AndroidRuntimeException() {}
  
  public AndroidRuntimeException(Exception paramException)
  {
    super(paramException);
  }
  
  public AndroidRuntimeException(String paramString)
  {
    super(paramString);
  }
  
  public AndroidRuntimeException(String paramString, Throwable paramThrowable)
  {
    super(paramString, paramThrowable);
  }
}
