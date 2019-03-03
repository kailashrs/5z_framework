package android.util.apk;

public class SignatureNotFoundException
  extends Exception
{
  private static final long serialVersionUID = 1L;
  
  public SignatureNotFoundException(String paramString)
  {
    super(paramString);
  }
  
  public SignatureNotFoundException(String paramString, Throwable paramThrowable)
  {
    super(paramString, paramThrowable);
  }
}
