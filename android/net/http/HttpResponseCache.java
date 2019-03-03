package android.net.http;

import com.android.okhttp.AndroidShimResponseCache;
import com.android.okhttp.Cache;
import com.android.okhttp.OkCacheContainer;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.net.CacheRequest;
import java.net.CacheResponse;
import java.net.ResponseCache;
import java.net.URI;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

public final class HttpResponseCache
  extends ResponseCache
  implements Closeable, OkCacheContainer
{
  private final AndroidShimResponseCache delegate;
  
  private HttpResponseCache(AndroidShimResponseCache paramAndroidShimResponseCache)
  {
    delegate = paramAndroidShimResponseCache;
  }
  
  public static HttpResponseCache getInstalled()
  {
    ResponseCache localResponseCache = ResponseCache.getDefault();
    if ((localResponseCache instanceof HttpResponseCache)) {
      return (HttpResponseCache)localResponseCache;
    }
    return null;
  }
  
  public static HttpResponseCache install(File paramFile, long paramLong)
    throws IOException
  {
    try
    {
      Object localObject = ResponseCache.getDefault();
      if ((localObject instanceof HttpResponseCache))
      {
        HttpResponseCache localHttpResponseCache = (HttpResponseCache)localObject;
        localObject = delegate;
        boolean bool = ((AndroidShimResponseCache)localObject).isEquivalent(paramFile, paramLong);
        if (bool) {
          return localHttpResponseCache;
        }
        ((AndroidShimResponseCache)localObject).close();
      }
      localObject = AndroidShimResponseCache.create(paramFile, paramLong);
      paramFile = new android/net/http/HttpResponseCache;
      paramFile.<init>((AndroidShimResponseCache)localObject);
      ResponseCache.setDefault(paramFile);
      return paramFile;
    }
    finally {}
  }
  
  public void close()
    throws IOException
  {
    if (ResponseCache.getDefault() == this) {
      ResponseCache.setDefault(null);
    }
    delegate.close();
  }
  
  public void delete()
    throws IOException
  {
    if (ResponseCache.getDefault() == this) {
      ResponseCache.setDefault(null);
    }
    delegate.delete();
  }
  
  public void flush()
  {
    try
    {
      delegate.flush();
    }
    catch (IOException localIOException) {}
  }
  
  public CacheResponse get(URI paramURI, String paramString, Map<String, List<String>> paramMap)
    throws IOException
  {
    return delegate.get(paramURI, paramString, paramMap);
  }
  
  public Cache getCache()
  {
    return delegate.getCache();
  }
  
  public int getHitCount()
  {
    return delegate.getHitCount();
  }
  
  public int getNetworkCount()
  {
    return delegate.getNetworkCount();
  }
  
  public int getRequestCount()
  {
    return delegate.getRequestCount();
  }
  
  public long maxSize()
  {
    return delegate.maxSize();
  }
  
  public CacheRequest put(URI paramURI, URLConnection paramURLConnection)
    throws IOException
  {
    return delegate.put(paramURI, paramURLConnection);
  }
  
  public long size()
  {
    try
    {
      long l = delegate.size();
      return l;
    }
    catch (IOException localIOException) {}
    return -1L;
  }
}
