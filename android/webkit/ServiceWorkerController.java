package android.webkit;

public abstract class ServiceWorkerController
{
  public ServiceWorkerController() {}
  
  public static ServiceWorkerController getInstance()
  {
    return WebViewFactory.getProvider().getServiceWorkerController();
  }
  
  public abstract ServiceWorkerWebSettings getServiceWorkerWebSettings();
  
  public abstract void setServiceWorkerClient(ServiceWorkerClient paramServiceWorkerClient);
}
