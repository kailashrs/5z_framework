package android.net.lowpan;

import android.os.ServiceSpecificException;
import android.util.AndroidException;

public class LowpanException
  extends AndroidException
{
  public LowpanException() {}
  
  public LowpanException(Exception paramException)
  {
    super(paramException);
  }
  
  public LowpanException(String paramString)
  {
    super(paramString);
  }
  
  public LowpanException(String paramString, Throwable paramThrowable)
  {
    super(paramString, paramThrowable);
  }
  
  static LowpanException rethrowFromServiceSpecificException(ServiceSpecificException paramServiceSpecificException)
    throws LowpanException
  {
    String str;
    switch (errorCode)
    {
    case 5: 
    case 6: 
    case 8: 
    case 9: 
    default: 
      throw new LowpanRuntimeException(paramServiceSpecificException);
    case 15: 
      throw new NetworkAlreadyExistsException(paramServiceSpecificException);
    case 14: 
      throw new JoinFailedAtAuthException(paramServiceSpecificException);
    case 13: 
      throw new JoinFailedAtScanException(paramServiceSpecificException);
    case 12: 
      throw new JoinFailedException(paramServiceSpecificException);
    case 11: 
      if (paramServiceSpecificException.getMessage() != null) {
        str = paramServiceSpecificException.getMessage();
      } else {
        str = "Feature not supported";
      }
      throw new LowpanException(str, paramServiceSpecificException);
    case 10: 
      throw new OperationCanceledException(paramServiceSpecificException);
    case 7: 
      if (paramServiceSpecificException.getMessage() != null) {
        str = paramServiceSpecificException.getMessage();
      } else {
        str = "NCP problem";
      }
      throw new LowpanRuntimeException(str, paramServiceSpecificException);
    case 4: 
      throw new WrongStateException(paramServiceSpecificException);
    case 3: 
      throw new InterfaceDisabledException(paramServiceSpecificException);
    }
    if (paramServiceSpecificException.getMessage() != null) {
      str = paramServiceSpecificException.getMessage();
    } else {
      str = "Invalid argument";
    }
    throw new LowpanRuntimeException(str, paramServiceSpecificException);
  }
}
