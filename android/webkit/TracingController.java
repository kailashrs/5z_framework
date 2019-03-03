package android.webkit;

import java.io.OutputStream;
import java.util.concurrent.Executor;

public abstract class TracingController
{
  public TracingController() {}
  
  public static TracingController getInstance()
  {
    return WebViewFactory.getProvider().getTracingController();
  }
  
  public abstract boolean isTracing();
  
  public abstract void start(TracingConfig paramTracingConfig);
  
  public abstract boolean stop(OutputStream paramOutputStream, Executor paramExecutor);
}
