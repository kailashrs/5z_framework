package android.security.keystore;

import java.security.InvalidKeyException;

public class KeyNotYetValidException
  extends InvalidKeyException
{
  public KeyNotYetValidException()
  {
    super("Key not yet valid");
  }
  
  public KeyNotYetValidException(String paramString)
  {
    super(paramString);
  }
  
  public KeyNotYetValidException(String paramString, Throwable paramThrowable)
  {
    super(paramString, paramThrowable);
  }
}
