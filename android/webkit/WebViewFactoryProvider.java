package android.webkit;

import android.annotation.SystemApi;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import java.util.List;

@SystemApi
public abstract interface WebViewFactoryProvider
{
  public abstract WebViewProvider createWebView(WebView paramWebView, WebView.PrivateAccess paramPrivateAccess);
  
  public abstract CookieManager getCookieManager();
  
  public abstract GeolocationPermissions getGeolocationPermissions();
  
  public abstract ServiceWorkerController getServiceWorkerController();
  
  public abstract Statics getStatics();
  
  public abstract TokenBindingService getTokenBindingService();
  
  public abstract TracingController getTracingController();
  
  public abstract WebIconDatabase getWebIconDatabase();
  
  public abstract WebStorage getWebStorage();
  
  public abstract ClassLoader getWebViewClassLoader();
  
  public abstract WebViewDatabase getWebViewDatabase(Context paramContext);
  
  public static abstract interface Statics
  {
    public abstract void clearClientCertPreferences(Runnable paramRunnable);
    
    public abstract void enableSlowWholeDocumentDraw();
    
    public abstract String findAddress(String paramString);
    
    public abstract void freeMemoryForTests();
    
    public abstract String getDefaultUserAgent(Context paramContext);
    
    public abstract Uri getSafeBrowsingPrivacyPolicyUrl();
    
    public abstract void initSafeBrowsing(Context paramContext, ValueCallback<Boolean> paramValueCallback);
    
    public abstract Uri[] parseFileChooserResult(int paramInt, Intent paramIntent);
    
    public abstract void setSafeBrowsingWhitelist(List<String> paramList, ValueCallback<Boolean> paramValueCallback);
    
    public abstract void setWebContentsDebuggingEnabled(boolean paramBoolean);
  }
}
