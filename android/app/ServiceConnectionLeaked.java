package android.app;

import android.util.AndroidRuntimeException;

final class ServiceConnectionLeaked
  extends AndroidRuntimeException
{
  public ServiceConnectionLeaked(String paramString)
  {
    super(paramString);
  }
}
