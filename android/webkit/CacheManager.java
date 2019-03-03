package android.webkit;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

@Deprecated
public final class CacheManager
{
  public CacheManager() {}
  
  @Deprecated
  public static boolean cacheDisabled()
  {
    return false;
  }
  
  @Deprecated
  public static boolean endCacheTransaction()
  {
    return false;
  }
  
  @Deprecated
  public static CacheResult getCacheFile(String paramString, Map<String, String> paramMap)
  {
    return null;
  }
  
  @Deprecated
  public static File getCacheFileBaseDir()
  {
    return null;
  }
  
  static void saveCacheFile(String paramString, long paramLong, CacheResult paramCacheResult)
  {
    try
    {
      outStream.close();
      return;
    }
    catch (IOException paramString) {}
  }
  
  @Deprecated
  public static void saveCacheFile(String paramString, CacheResult paramCacheResult)
  {
    saveCacheFile(paramString, 0L, paramCacheResult);
  }
  
  @Deprecated
  public static boolean startCacheTransaction()
  {
    return false;
  }
  
  @Deprecated
  public static class CacheResult
  {
    long contentLength;
    String contentdisposition;
    String crossDomain;
    String encoding;
    String etag;
    long expires;
    String expiresString;
    int httpStatusCode;
    InputStream inStream;
    String lastModified;
    String localPath;
    String location;
    String mimeType;
    File outFile;
    OutputStream outStream;
    
    public CacheResult() {}
    
    public String getContentDisposition()
    {
      return contentdisposition;
    }
    
    public long getContentLength()
    {
      return contentLength;
    }
    
    public String getETag()
    {
      return etag;
    }
    
    public String getEncoding()
    {
      return encoding;
    }
    
    public long getExpires()
    {
      return expires;
    }
    
    public String getExpiresString()
    {
      return expiresString;
    }
    
    public int getHttpStatusCode()
    {
      return httpStatusCode;
    }
    
    public InputStream getInputStream()
    {
      return inStream;
    }
    
    public String getLastModified()
    {
      return lastModified;
    }
    
    public String getLocalPath()
    {
      return localPath;
    }
    
    public String getLocation()
    {
      return location;
    }
    
    public String getMimeType()
    {
      return mimeType;
    }
    
    public OutputStream getOutputStream()
    {
      return outStream;
    }
    
    public void setContentLength(long paramLong)
    {
      contentLength = paramLong;
    }
    
    public void setEncoding(String paramString)
    {
      encoding = paramString;
    }
    
    public void setInputStream(InputStream paramInputStream)
    {
      inStream = paramInputStream;
    }
  }
}
