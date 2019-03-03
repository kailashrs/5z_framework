package android.security.keystore;

import android.security.KeyStoreException;
import java.security.ProviderException;

public class SecureKeyImportUnavailableException
  extends ProviderException
{
  public SecureKeyImportUnavailableException() {}
  
  public SecureKeyImportUnavailableException(String paramString)
  {
    super(paramString, new KeyStoreException(-68, "Secure Key Import not available"));
  }
  
  public SecureKeyImportUnavailableException(String paramString, Throwable paramThrowable)
  {
    super(paramString, paramThrowable);
  }
  
  public SecureKeyImportUnavailableException(Throwable paramThrowable)
  {
    super(paramThrowable);
  }
}
