package android.security.keystore;

import java.security.InvalidKeyException;

public class UserNotAuthenticatedException
  extends InvalidKeyException
{
  public UserNotAuthenticatedException()
  {
    super("User not authenticated");
  }
  
  public UserNotAuthenticatedException(String paramString)
  {
    super(paramString);
  }
  
  public UserNotAuthenticatedException(String paramString, Throwable paramThrowable)
  {
    super(paramString, paramThrowable);
  }
}
