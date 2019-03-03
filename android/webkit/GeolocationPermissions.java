package android.webkit;

import android.annotation.SystemApi;
import java.util.Set;

public class GeolocationPermissions
{
  @SystemApi
  public GeolocationPermissions() {}
  
  public static GeolocationPermissions getInstance()
  {
    return WebViewFactory.getProvider().getGeolocationPermissions();
  }
  
  public void allow(String paramString) {}
  
  public void clear(String paramString) {}
  
  public void clearAll() {}
  
  public void getAllowed(String paramString, ValueCallback<Boolean> paramValueCallback) {}
  
  public void getOrigins(ValueCallback<Set<String>> paramValueCallback) {}
  
  public static abstract interface Callback
  {
    public abstract void invoke(String paramString, boolean paramBoolean1, boolean paramBoolean2);
  }
}
