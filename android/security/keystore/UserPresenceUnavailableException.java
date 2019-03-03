package android.security.keystore;

import java.security.InvalidKeyException;

public class UserPresenceUnavailableException
  extends InvalidKeyException
{
  public UserPresenceUnavailableException()
  {
    super("No Strong Box available.");
  }
  
  public UserPresenceUnavailableException(String paramString)
  {
    super(paramString);
  }
  
  public UserPresenceUnavailableException(String paramString, Throwable paramThrowable)
  {
    super(paramString, paramThrowable);
  }
}
