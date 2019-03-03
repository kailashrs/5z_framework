package android.webkit;

import java.util.Map;

@Deprecated
public abstract interface UrlInterceptHandler
{
  @Deprecated
  public abstract PluginData getPluginData(String paramString, Map<String, String> paramMap);
  
  @Deprecated
  public abstract CacheManager.CacheResult service(String paramString, Map<String, String> paramMap);
}
