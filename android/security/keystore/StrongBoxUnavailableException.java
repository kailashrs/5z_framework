package android.security.keystore;

import android.security.KeyStoreException;
import java.security.ProviderException;

public class StrongBoxUnavailableException
  extends ProviderException
{
  public StrongBoxUnavailableException() {}
  
  public StrongBoxUnavailableException(String paramString)
  {
    super(paramString, new KeyStoreException(-68, "No StrongBox available"));
  }
  
  public StrongBoxUnavailableException(String paramString, Throwable paramThrowable)
  {
    super(paramString, paramThrowable);
  }
  
  public StrongBoxUnavailableException(Throwable paramThrowable)
  {
    super(paramThrowable);
  }
}
