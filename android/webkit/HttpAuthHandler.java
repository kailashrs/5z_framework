package android.webkit;

import android.annotation.SystemApi;
import android.os.Handler;

public class HttpAuthHandler
  extends Handler
{
  @SystemApi
  public HttpAuthHandler() {}
  
  public void cancel() {}
  
  public void proceed(String paramString1, String paramString2) {}
  
  public boolean useHttpAuthUsernamePassword()
  {
    return false;
  }
}
