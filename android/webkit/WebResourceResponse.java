package android.webkit;

import android.annotation.SystemApi;
import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.util.Map;

public class WebResourceResponse
{
  private String mEncoding;
  private boolean mImmutable;
  private InputStream mInputStream;
  private String mMimeType;
  private String mReasonPhrase;
  private Map<String, String> mResponseHeaders;
  private int mStatusCode;
  
  public WebResourceResponse(String paramString1, String paramString2, int paramInt, String paramString3, Map<String, String> paramMap, InputStream paramInputStream)
  {
    this(paramString1, paramString2, paramInputStream);
    setStatusCodeAndReasonPhrase(paramInt, paramString3);
    setResponseHeaders(paramMap);
  }
  
  public WebResourceResponse(String paramString1, String paramString2, InputStream paramInputStream)
  {
    mMimeType = paramString1;
    mEncoding = paramString2;
    setData(paramInputStream);
  }
  
  @SystemApi
  public WebResourceResponse(boolean paramBoolean, String paramString1, String paramString2, int paramInt, String paramString3, Map<String, String> paramMap, InputStream paramInputStream)
  {
    mImmutable = paramBoolean;
    mMimeType = paramString1;
    mEncoding = paramString2;
    mStatusCode = paramInt;
    mReasonPhrase = paramString3;
    mResponseHeaders = paramMap;
    mInputStream = paramInputStream;
  }
  
  private void checkImmutable()
  {
    if (!mImmutable) {
      return;
    }
    throw new IllegalStateException("This WebResourceResponse instance is immutable");
  }
  
  public InputStream getData()
  {
    return mInputStream;
  }
  
  public String getEncoding()
  {
    return mEncoding;
  }
  
  public String getMimeType()
  {
    return mMimeType;
  }
  
  public String getReasonPhrase()
  {
    return mReasonPhrase;
  }
  
  public Map<String, String> getResponseHeaders()
  {
    return mResponseHeaders;
  }
  
  public int getStatusCode()
  {
    return mStatusCode;
  }
  
  public void setData(InputStream paramInputStream)
  {
    checkImmutable();
    if ((paramInputStream != null) && (StringBufferInputStream.class.isAssignableFrom(paramInputStream.getClass()))) {
      throw new IllegalArgumentException("StringBufferInputStream is deprecated and must not be passed to a WebResourceResponse");
    }
    mInputStream = paramInputStream;
  }
  
  public void setEncoding(String paramString)
  {
    checkImmutable();
    mEncoding = paramString;
  }
  
  public void setMimeType(String paramString)
  {
    checkImmutable();
    mMimeType = paramString;
  }
  
  public void setResponseHeaders(Map<String, String> paramMap)
  {
    checkImmutable();
    mResponseHeaders = paramMap;
  }
  
  public void setStatusCodeAndReasonPhrase(int paramInt, String paramString)
  {
    checkImmutable();
    if (paramInt >= 100)
    {
      if (paramInt <= 599)
      {
        if ((paramInt > 299) && (paramInt < 400)) {
          throw new IllegalArgumentException("statusCode can't be in the [300, 399] range.");
        }
        if (paramString != null)
        {
          if (!paramString.trim().isEmpty())
          {
            int i = 0;
            while (i < paramString.length()) {
              if (paramString.charAt(i) <= '') {
                i++;
              } else {
                throw new IllegalArgumentException("reasonPhrase can't contain non-ASCII characters.");
              }
            }
            mStatusCode = paramInt;
            mReasonPhrase = paramString;
            return;
          }
          throw new IllegalArgumentException("reasonPhrase can't be empty.");
        }
        throw new IllegalArgumentException("reasonPhrase can't be null.");
      }
      throw new IllegalArgumentException("statusCode can't be greater than 599.");
    }
    throw new IllegalArgumentException("statusCode can't be less than 100.");
  }
}
