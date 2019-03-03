package android.net.lowpan;

import android.util.AndroidRuntimeException;

public class LowpanRuntimeException
  extends AndroidRuntimeException
{
  public LowpanRuntimeException() {}
  
  public LowpanRuntimeException(Exception paramException)
  {
    super(paramException);
  }
  
  public LowpanRuntimeException(String paramString)
  {
    super(paramString);
  }
  
  public LowpanRuntimeException(String paramString, Throwable paramThrowable)
  {
    super(paramString, paramThrowable);
  }
}
