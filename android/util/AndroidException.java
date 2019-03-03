package android.util;

public class AndroidException
  extends Exception
{
  public AndroidException() {}
  
  public AndroidException(Exception paramException)
  {
    super(paramException);
  }
  
  public AndroidException(String paramString)
  {
    super(paramString);
  }
  
  public AndroidException(String paramString, Throwable paramThrowable)
  {
    super(paramString, paramThrowable);
  }
  
  protected AndroidException(String paramString, Throwable paramThrowable, boolean paramBoolean1, boolean paramBoolean2)
  {
    super(paramString, paramThrowable, paramBoolean1, paramBoolean2);
  }
}
