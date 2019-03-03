package android.webkit;

import android.annotation.SystemApi;
import android.os.Handler;

public abstract class WebMessagePort
{
  @SystemApi
  public WebMessagePort() {}
  
  public abstract void close();
  
  public abstract void postMessage(WebMessage paramWebMessage);
  
  public abstract void setWebMessageCallback(WebMessageCallback paramWebMessageCallback);
  
  public abstract void setWebMessageCallback(WebMessageCallback paramWebMessageCallback, Handler paramHandler);
  
  public static abstract class WebMessageCallback
  {
    public WebMessageCallback() {}
    
    public void onMessage(WebMessagePort paramWebMessagePort, WebMessage paramWebMessage) {}
  }
}
