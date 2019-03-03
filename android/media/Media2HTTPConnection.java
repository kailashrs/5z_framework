package android.media;

import android.net.NetworkUtils;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy.Builder;
import android.util.Log;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.CookieHandler;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NoRouteToHostException;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.URL;
import java.net.UnknownServiceException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Media2HTTPConnection
{
  private static final int CONNECT_TIMEOUT_MS = 30000;
  private static final int HTTP_TEMP_REDIRECT = 307;
  private static final int MAX_REDIRECTS = 20;
  private static final String TAG = "Media2HTTPConnection";
  private static final boolean VERBOSE = false;
  private boolean mAllowCrossDomainRedirect = true;
  private boolean mAllowCrossProtocolRedirect = true;
  private HttpURLConnection mConnection = null;
  private long mCurrentOffset = -1L;
  private Map<String, String> mHeaders = null;
  private InputStream mInputStream = null;
  private long mTotalSize = -1L;
  private URL mURL = null;
  
  public Media2HTTPConnection()
  {
    if (CookieHandler.getDefault() == null) {
      Log.w("Media2HTTPConnection", "Media2HTTPConnection: Unexpected. No CookieHandler found.");
    }
  }
  
  private Map<String, String> convertHeaderStringToMap(String paramString)
  {
    HashMap localHashMap = new HashMap();
    for (String str1 : paramString.split("\r\n"))
    {
      int k = str1.indexOf(":");
      if (k >= 0)
      {
        String str2 = str1.substring(0, k);
        str1 = str1.substring(k + 1);
        if (!filterOutInternalHeaders(str2, str1)) {
          localHashMap.put(str2, str1);
        }
      }
    }
    return localHashMap;
  }
  
  private boolean filterOutInternalHeaders(String paramString1, String paramString2)
  {
    if ("android-allow-cross-domain-redirect".equalsIgnoreCase(paramString1))
    {
      mAllowCrossDomainRedirect = parseBoolean(paramString2);
      mAllowCrossProtocolRedirect = mAllowCrossDomainRedirect;
      return true;
    }
    return false;
  }
  
  private static final boolean isLocalHost(URL paramURL)
  {
    if (paramURL == null) {
      return false;
    }
    paramURL = paramURL.getHost();
    if (paramURL == null) {
      return false;
    }
    try
    {
      if (paramURL.equalsIgnoreCase("localhost")) {
        return true;
      }
      boolean bool = NetworkUtils.numericToInetAddress(paramURL).isLoopbackAddress();
      if (bool) {
        return true;
      }
    }
    catch (IllegalArgumentException paramURL) {}
    return false;
  }
  
  private boolean parseBoolean(String paramString)
  {
    boolean bool1 = false;
    boolean bool2 = false;
    try
    {
      long l = Long.parseLong(paramString);
      if (l != 0L) {
        bool2 = true;
      }
      return bool2;
    }
    catch (NumberFormatException localNumberFormatException)
    {
      if ((!"true".equalsIgnoreCase(paramString)) && (!"yes".equalsIgnoreCase(paramString))) {
        bool2 = bool1;
      } else {
        bool2 = true;
      }
    }
    return bool2;
  }
  
  private void seekTo(long paramLong)
    throws IOException
  {
    teardownConnection();
    try
    {
      Object localObject1 = mURL;
      boolean bool1 = isLocalHost((URL)localObject1);
      int i = 0;
      for (;;)
      {
        if (bool1) {
          mConnection = ((HttpURLConnection)((URL)localObject1).openConnection(Proxy.NO_PROXY));
        } else {
          mConnection = ((HttpURLConnection)((URL)localObject1).openConnection());
        }
        mConnection.setConnectTimeout(30000);
        mConnection.setInstanceFollowRedirects(mAllowCrossDomainRedirect);
        if (mHeaders != null)
        {
          localObject1 = mHeaders.entrySet().iterator();
          while (((Iterator)localObject1).hasNext())
          {
            localObject3 = (Map.Entry)((Iterator)localObject1).next();
            mConnection.setRequestProperty((String)((Map.Entry)localObject3).getKey(), (String)((Map.Entry)localObject3).getValue());
          }
        }
        if (paramLong > 0L)
        {
          localObject1 = mConnection;
          localObject3 = new java/lang/StringBuilder;
          ((StringBuilder)localObject3).<init>();
          ((StringBuilder)localObject3).append("bytes=");
          ((StringBuilder)localObject3).append(paramLong);
          ((StringBuilder)localObject3).append("-");
          ((HttpURLConnection)localObject1).setRequestProperty("Range", ((StringBuilder)localObject3).toString());
        }
        int j = mConnection.getResponseCode();
        if ((j != 300) && (j != 301) && (j != 302) && (j != 303) && (j != 307))
        {
          if (mAllowCrossDomainRedirect) {
            mURL = mConnection.getURL();
          }
          if (j == 206)
          {
            localObject1 = mConnection.getHeaderField("Content-Range");
            mTotalSize = -1L;
            if (localObject1 != null)
            {
              i = ((String)localObject1).lastIndexOf('/');
              if (i >= 0)
              {
                localObject1 = ((String)localObject1).substring(i + 1);
                try
                {
                  mTotalSize = Long.parseLong((String)localObject1);
                }
                catch (NumberFormatException localNumberFormatException) {}
              }
            }
          }
          else
          {
            if (j != 200) {
              break label405;
            }
            mTotalSize = mConnection.getContentLength();
          }
          if ((paramLong > 0L) && (j != 206))
          {
            localObject2 = new java/net/ProtocolException;
            ((ProtocolException)localObject2).<init>();
            throw ((Throwable)localObject2);
          }
          localObject2 = new java/io/BufferedInputStream;
          ((BufferedInputStream)localObject2).<init>(mConnection.getInputStream());
          mInputStream = ((InputStream)localObject2);
          mCurrentOffset = paramLong;
          return;
          label405:
          localObject2 = new java/io/IOException;
          ((IOException)localObject2).<init>();
          throw ((Throwable)localObject2);
        }
        i++;
        if (i > 20) {
          break label667;
        }
        localObject2 = mConnection.getRequestMethod();
        if ((j == 307) && (!((String)localObject2).equals("GET")) && (!((String)localObject2).equals("HEAD")))
        {
          localObject2 = new java/net/NoRouteToHostException;
          ((NoRouteToHostException)localObject2).<init>("Invalid redirect");
          throw ((Throwable)localObject2);
        }
        localObject3 = mConnection.getHeaderField("Location");
        if (localObject3 == null) {
          break;
        }
        localObject2 = new java/net/URL;
        ((URL)localObject2).<init>(mURL, (String)localObject3);
        if ((!((URL)localObject2).getProtocol().equals("https")) && (!((URL)localObject2).getProtocol().equals("http")))
        {
          localObject2 = new java/net/NoRouteToHostException;
          ((NoRouteToHostException)localObject2).<init>("Unsupported protocol redirect");
          throw ((Throwable)localObject2);
        }
        boolean bool2 = mURL.getProtocol().equals(((URL)localObject2).getProtocol());
        if ((!mAllowCrossProtocolRedirect) && (!bool2))
        {
          localObject2 = new java/net/NoRouteToHostException;
          ((NoRouteToHostException)localObject2).<init>("Cross-protocol redirects are disallowed");
          throw ((Throwable)localObject2);
        }
        bool2 = mURL.getHost().equals(((URL)localObject2).getHost());
        if ((!mAllowCrossDomainRedirect) && (!bool2))
        {
          localObject2 = new java/net/NoRouteToHostException;
          ((NoRouteToHostException)localObject2).<init>("Cross-domain redirects are disallowed");
          throw ((Throwable)localObject2);
        }
        if (j != 307) {
          mURL = ((URL)localObject2);
        }
      }
      Object localObject2 = new java/net/NoRouteToHostException;
      ((NoRouteToHostException)localObject2).<init>("Invalid redirect");
      throw ((Throwable)localObject2);
      label667:
      Object localObject3 = new java/net/NoRouteToHostException;
      localObject2 = new java/lang/StringBuilder;
      ((StringBuilder)localObject2).<init>();
      ((StringBuilder)localObject2).append("Too many redirects: ");
      ((StringBuilder)localObject2).append(i);
      ((NoRouteToHostException)localObject3).<init>(((StringBuilder)localObject2).toString());
      throw ((Throwable)localObject3);
    }
    catch (IOException localIOException)
    {
      mTotalSize = -1L;
      teardownConnection();
      mCurrentOffset = -1L;
      throw localIOException;
    }
  }
  
  private void teardownConnection()
  {
    if (mConnection != null)
    {
      if (mInputStream != null)
      {
        try
        {
          mInputStream.close();
        }
        catch (IOException localIOException) {}
        mInputStream = null;
      }
      mConnection.disconnect();
      mConnection = null;
      mCurrentOffset = -1L;
    }
  }
  
  public boolean connect(String paramString1, String paramString2)
  {
    try
    {
      disconnect();
      mAllowCrossDomainRedirect = true;
      URL localURL = new java/net/URL;
      localURL.<init>(paramString1);
      mURL = localURL;
      mHeaders = convertHeaderStringToMap(paramString2);
      return true;
    }
    catch (MalformedURLException paramString1) {}
    return false;
  }
  
  public void disconnect()
  {
    teardownConnection();
    mHeaders = null;
    mURL = null;
  }
  
  public String getMIMEType()
  {
    if (mConnection == null) {
      try
      {
        seekTo(0L);
      }
      catch (IOException localIOException)
      {
        return "application/octet-stream";
      }
    }
    return mConnection.getContentType();
  }
  
  public long getSize()
  {
    if (mConnection == null) {
      try
      {
        seekTo(0L);
      }
      catch (IOException localIOException)
      {
        return -1L;
      }
    }
    return mTotalSize;
  }
  
  public String getUri()
  {
    return mURL.toString();
  }
  
  public int readAt(long paramLong, byte[] paramArrayOfByte, int paramInt)
  {
    StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
    try
    {
      if (paramLong != mCurrentOffset) {
        seekTo(paramLong);
      }
      int i = mInputStream.read(paramArrayOfByte, 0, paramInt);
      int j = i;
      if (i == -1) {
        j = 0;
      }
      mCurrentOffset += j;
      return j;
    }
    catch (Exception paramArrayOfByte)
    {
      return -1;
    }
    catch (IOException paramArrayOfByte)
    {
      return -1;
    }
    catch (UnknownServiceException localUnknownServiceException)
    {
      paramArrayOfByte = new StringBuilder();
      paramArrayOfByte.append("readAt ");
      paramArrayOfByte.append(paramLong);
      paramArrayOfByte.append(" / ");
      paramArrayOfByte.append(paramInt);
      paramArrayOfByte.append(" => ");
      paramArrayOfByte.append(localUnknownServiceException);
      Log.w("Media2HTTPConnection", paramArrayOfByte.toString());
      return 64526;
    }
    catch (NoRouteToHostException paramArrayOfByte)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("readAt ");
      localStringBuilder.append(paramLong);
      localStringBuilder.append(" / ");
      localStringBuilder.append(paramInt);
      localStringBuilder.append(" => ");
      localStringBuilder.append(paramArrayOfByte);
      Log.w("Media2HTTPConnection", localStringBuilder.toString());
      return 64526;
    }
    catch (ProtocolException localProtocolException)
    {
      paramArrayOfByte = new StringBuilder();
      paramArrayOfByte.append("readAt ");
      paramArrayOfByte.append(paramLong);
      paramArrayOfByte.append(" / ");
      paramArrayOfByte.append(paramInt);
      paramArrayOfByte.append(" => ");
      paramArrayOfByte.append(localProtocolException);
      Log.w("Media2HTTPConnection", paramArrayOfByte.toString());
    }
    return 64526;
  }
}
