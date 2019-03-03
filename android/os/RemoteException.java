package android.os;

import android.util.AndroidException;

public class RemoteException
  extends AndroidException
{
  public RemoteException() {}
  
  public RemoteException(String paramString)
  {
    super(paramString);
  }
  
  public RemoteException(String paramString, Throwable paramThrowable, boolean paramBoolean1, boolean paramBoolean2)
  {
    super(paramString, paramThrowable, paramBoolean1, paramBoolean2);
  }
  
  public RuntimeException rethrowAsRuntimeException()
  {
    throw new RuntimeException(this);
  }
  
  public RuntimeException rethrowFromSystemServer()
  {
    if ((this instanceof DeadObjectException)) {
      throw new RuntimeException(new DeadSystemException());
    }
    throw new RuntimeException(this);
  }
}
