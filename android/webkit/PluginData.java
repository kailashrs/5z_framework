package android.webkit;

import java.io.InputStream;
import java.util.Map;

@Deprecated
public final class PluginData
{
  private long mContentLength;
  private Map<String, String[]> mHeaders;
  private int mStatusCode;
  private InputStream mStream;
  
  @Deprecated
  public PluginData(InputStream paramInputStream, long paramLong, Map<String, String[]> paramMap, int paramInt)
  {
    mStream = paramInputStream;
    mContentLength = paramLong;
    mHeaders = paramMap;
    mStatusCode = paramInt;
  }
  
  @Deprecated
  public long getContentLength()
  {
    return mContentLength;
  }
  
  @Deprecated
  public Map<String, String[]> getHeaders()
  {
    return mHeaders;
  }
  
  @Deprecated
  public InputStream getInputStream()
  {
    return mStream;
  }
  
  @Deprecated
  public int getStatusCode()
  {
    return mStatusCode;
  }
}
