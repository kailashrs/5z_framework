package android.os;

import android.util.AndroidRuntimeException;

public class BadParcelableException
  extends AndroidRuntimeException
{
  public BadParcelableException(Exception paramException)
  {
    super(paramException);
  }
  
  public BadParcelableException(String paramString)
  {
    super(paramString);
  }
}
