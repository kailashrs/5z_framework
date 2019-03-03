package android.security.keystore;

import java.security.InvalidKeyException;

public class KeyExpiredException
  extends InvalidKeyException
{
  public KeyExpiredException()
  {
    super("Key expired");
  }
  
  public KeyExpiredException(String paramString)
  {
    super(paramString);
  }
  
  public KeyExpiredException(String paramString, Throwable paramThrowable)
  {
    super(paramString, paramThrowable);
  }
}
