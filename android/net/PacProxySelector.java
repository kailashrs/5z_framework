package android.net;

import android.os.ServiceManager;
import android.util.Log;
import com.android.net.IProxyService;
import com.android.net.IProxyService.Stub;
import com.google.android.collect.Lists;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

public class PacProxySelector
  extends ProxySelector
{
  private static final String PROXY = "PROXY ";
  public static final String PROXY_SERVICE = "com.android.net.IProxyService";
  private static final String SOCKS = "SOCKS ";
  private static final String TAG = "PacProxySelector";
  private final List<Proxy> mDefaultList;
  private IProxyService mProxyService = IProxyService.Stub.asInterface(ServiceManager.getService("com.android.net.IProxyService"));
  
  public PacProxySelector()
  {
    if (mProxyService == null) {
      Log.e("PacProxySelector", "PacManager: no proxy service");
    }
    mDefaultList = Lists.newArrayList(new Proxy[] { Proxy.NO_PROXY });
  }
  
  private static List<Proxy> parseResponse(String paramString)
  {
    String[] arrayOfString = paramString.split(";");
    paramString = Lists.newArrayList();
    int i = arrayOfString.length;
    for (int j = 0; j < i; j++)
    {
      Object localObject = arrayOfString[j].trim();
      if (((String)localObject).equals("DIRECT"))
      {
        paramString.add(Proxy.NO_PROXY);
      }
      else if (((String)localObject).startsWith("PROXY "))
      {
        localObject = proxyFromHostPort(Proxy.Type.HTTP, ((String)localObject).substring("PROXY ".length()));
        if (localObject != null) {
          paramString.add(localObject);
        }
      }
      else if (((String)localObject).startsWith("SOCKS "))
      {
        localObject = proxyFromHostPort(Proxy.Type.SOCKS, ((String)localObject).substring("SOCKS ".length()));
        if (localObject != null) {
          paramString.add(localObject);
        }
      }
    }
    if (paramString.size() == 0) {
      paramString.add(Proxy.NO_PROXY);
    }
    return paramString;
  }
  
  private static Proxy proxyFromHostPort(Proxy.Type paramType, String paramString)
  {
    try
    {
      localObject = paramString.split(":");
      paramType = new Proxy(paramType, InetSocketAddress.createUnresolved(localObject[0], Integer.parseInt(localObject[1])));
      return paramType;
    }
    catch (NumberFormatException|ArrayIndexOutOfBoundsException paramType)
    {
      Object localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Unable to parse proxy ");
      ((StringBuilder)localObject).append(paramString);
      ((StringBuilder)localObject).append(" ");
      ((StringBuilder)localObject).append(paramType);
      Log.d("PacProxySelector", ((StringBuilder)localObject).toString());
    }
    return null;
  }
  
  public void connectFailed(URI paramURI, SocketAddress paramSocketAddress, IOException paramIOException) {}
  
  public List<Proxy> select(URI paramURI)
  {
    if (mProxyService == null) {
      mProxyService = IProxyService.Stub.asInterface(ServiceManager.getService("com.android.net.IProxyService"));
    }
    if (mProxyService == null)
    {
      Log.e("PacProxySelector", "select: no proxy service return NO_PROXY");
      return Lists.newArrayList(new Proxy[] { Proxy.NO_PROXY });
    }
    Object localObject1 = null;
    Object localObject2 = paramURI;
    Object localObject3 = paramURI;
    Object localObject4 = paramURI;
    try
    {
      if (!"http".equalsIgnoreCase(paramURI.getScheme()))
      {
        localObject3 = paramURI;
        localObject4 = paramURI;
        localObject2 = new java/net/URI;
        localObject3 = paramURI;
        localObject4 = paramURI;
        ((URI)localObject2).<init>(paramURI.getScheme(), null, paramURI.getHost(), paramURI.getPort(), "/", null, null);
      }
      localObject3 = localObject2;
      localObject4 = localObject2;
      paramURI = ((URI)localObject2).toURL().toString();
    }
    catch (MalformedURLException paramURI)
    {
      paramURI = ((URI)localObject3).getHost();
      localObject2 = localObject3;
    }
    catch (URISyntaxException paramURI)
    {
      paramURI = ((URI)localObject4).getHost();
      localObject2 = localObject4;
    }
    try
    {
      paramURI = mProxyService.resolvePacFile(((URI)localObject2).getHost(), paramURI);
    }
    catch (Exception paramURI)
    {
      Log.e("PacProxySelector", "Error resolving PAC File", paramURI);
      paramURI = localObject1;
    }
    if (paramURI == null) {
      return mDefaultList;
    }
    return parseResponse(paramURI);
  }
}
