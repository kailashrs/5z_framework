package android.security.keystore;

import java.security.InvalidKeyException;

public class KeyPermanentlyInvalidatedException
  extends InvalidKeyException
{
  public KeyPermanentlyInvalidatedException()
  {
    super("Key permanently invalidated");
  }
  
  public KeyPermanentlyInvalidatedException(String paramString)
  {
    super(paramString);
  }
  
  public KeyPermanentlyInvalidatedException(String paramString, Throwable paramThrowable)
  {
    super(paramString, paramThrowable);
  }
}
