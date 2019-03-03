package android.webkit;

import android.annotation.SystemApi;
import android.os.RemoteException;

@SystemApi
public final class WebViewUpdateService
{
  private WebViewUpdateService() {}
  
  public static WebViewProviderInfo[] getAllWebViewPackages()
  {
    Object localObject = getUpdateService();
    if (localObject == null) {
      return new WebViewProviderInfo[0];
    }
    try
    {
      localObject = ((IWebViewUpdateService)localObject).getAllWebViewPackages();
      return localObject;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public static String getCurrentWebViewPackageName()
  {
    Object localObject = getUpdateService();
    if (localObject == null) {
      return null;
    }
    try
    {
      localObject = ((IWebViewUpdateService)localObject).getCurrentWebViewPackageName();
      return localObject;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  private static IWebViewUpdateService getUpdateService()
  {
    return WebViewFactory.getUpdateService();
  }
  
  public static WebViewProviderInfo[] getValidWebViewPackages()
  {
    Object localObject = getUpdateService();
    if (localObject == null) {
      return new WebViewProviderInfo[0];
    }
    try
    {
      localObject = ((IWebViewUpdateService)localObject).getValidWebViewPackages();
      return localObject;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
}
