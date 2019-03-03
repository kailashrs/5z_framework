package android.security.keystore.recovery;

import android.annotation.SystemApi;
import java.security.GeneralSecurityException;

@SystemApi
public class InternalRecoveryServiceException
  extends GeneralSecurityException
{
  public InternalRecoveryServiceException(String paramString)
  {
    super(paramString);
  }
  
  public InternalRecoveryServiceException(String paramString, Throwable paramThrowable)
  {
    super(paramString, paramThrowable);
  }
}
