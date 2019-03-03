package org.apache.http.conn.ssl;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import org.apache.http.conn.scheme.HostNameResolver;
import org.apache.http.conn.scheme.LayeredSocketFactory;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

@Deprecated
public class SSLSocketFactory
  implements LayeredSocketFactory
{
  public static final X509HostnameVerifier ALLOW_ALL_HOSTNAME_VERIFIER = new AllowAllHostnameVerifier();
  public static final X509HostnameVerifier BROWSER_COMPATIBLE_HOSTNAME_VERIFIER = new BrowserCompatHostnameVerifier();
  public static final String SSL = "SSL";
  public static final String SSLV2 = "SSLv2";
  public static final X509HostnameVerifier STRICT_HOSTNAME_VERIFIER = new StrictHostnameVerifier();
  public static final String TLS = "TLS";
  private X509HostnameVerifier hostnameVerifier = BROWSER_COMPATIBLE_HOSTNAME_VERIFIER;
  private final HostNameResolver nameResolver;
  private final javax.net.ssl.SSLSocketFactory socketfactory;
  private final SSLContext sslcontext;
  
  private SSLSocketFactory()
  {
    sslcontext = null;
    socketfactory = HttpsURLConnection.getDefaultSSLSocketFactory();
    nameResolver = null;
  }
  
  public SSLSocketFactory(String paramString1, KeyStore paramKeyStore1, String paramString2, KeyStore paramKeyStore2, SecureRandom paramSecureRandom, HostNameResolver paramHostNameResolver)
    throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException
  {
    String str = paramString1;
    if (paramString1 == null) {
      str = "TLS";
    }
    paramString1 = null;
    if (paramKeyStore1 != null) {
      paramString1 = createKeyManagers(paramKeyStore1, paramString2);
    }
    paramKeyStore1 = null;
    if (paramKeyStore2 != null) {
      paramKeyStore1 = createTrustManagers(paramKeyStore2);
    }
    sslcontext = SSLContext.getInstance(str);
    sslcontext.init(paramString1, paramKeyStore1, paramSecureRandom);
    socketfactory = sslcontext.getSocketFactory();
    nameResolver = paramHostNameResolver;
  }
  
  public SSLSocketFactory(KeyStore paramKeyStore)
    throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException
  {
    this("TLS", null, null, paramKeyStore, null, null);
  }
  
  public SSLSocketFactory(KeyStore paramKeyStore, String paramString)
    throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException
  {
    this("TLS", paramKeyStore, paramString, null, null, null);
  }
  
  public SSLSocketFactory(KeyStore paramKeyStore1, String paramString, KeyStore paramKeyStore2)
    throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException
  {
    this("TLS", paramKeyStore1, paramString, paramKeyStore2, null, null);
  }
  
  public SSLSocketFactory(javax.net.ssl.SSLSocketFactory paramSSLSocketFactory)
  {
    sslcontext = null;
    socketfactory = paramSSLSocketFactory;
    nameResolver = null;
  }
  
  private static KeyManager[] createKeyManagers(KeyStore paramKeyStore, String paramString)
    throws KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException
  {
    if (paramKeyStore != null)
    {
      KeyManagerFactory localKeyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
      if (paramString != null) {
        paramString = paramString.toCharArray();
      } else {
        paramString = null;
      }
      localKeyManagerFactory.init(paramKeyStore, paramString);
      return localKeyManagerFactory.getKeyManagers();
    }
    throw new IllegalArgumentException("Keystore may not be null");
  }
  
  private static TrustManager[] createTrustManagers(KeyStore paramKeyStore)
    throws KeyStoreException, NoSuchAlgorithmException
  {
    if (paramKeyStore != null)
    {
      TrustManagerFactory localTrustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
      localTrustManagerFactory.init(paramKeyStore);
      return localTrustManagerFactory.getTrustManagers();
    }
    throw new IllegalArgumentException("Keystore may not be null");
  }
  
  public static SSLSocketFactory getSocketFactory()
  {
    return NoPreloadHolder.DEFAULT_FACTORY;
  }
  
  public Socket connectSocket(Socket paramSocket, String paramString, int paramInt1, InetAddress paramInetAddress, int paramInt2, HttpParams paramHttpParams)
    throws IOException
  {
    if (paramString != null)
    {
      if (paramHttpParams != null)
      {
        if (paramSocket == null) {
          paramSocket = createSocket();
        }
        SSLSocket localSSLSocket = (SSLSocket)paramSocket;
        if ((paramInetAddress != null) || (paramInt2 > 0))
        {
          i = paramInt2;
          if (paramInt2 < 0) {
            i = 0;
          }
          localSSLSocket.bind(new InetSocketAddress(paramInetAddress, i));
        }
        paramInt2 = HttpConnectionParams.getConnectionTimeout(paramHttpParams);
        int i = HttpConnectionParams.getSoTimeout(paramHttpParams);
        if (nameResolver != null) {
          paramSocket = new InetSocketAddress(nameResolver.resolve(paramString), paramInt1);
        } else {
          paramSocket = new InetSocketAddress(paramString, paramInt1);
        }
        localSSLSocket.connect(paramSocket, paramInt2);
        localSSLSocket.setSoTimeout(i);
        try
        {
          localSSLSocket.startHandshake();
          hostnameVerifier.verify(paramString, localSSLSocket);
          return localSSLSocket;
        }
        catch (IOException paramSocket)
        {
          try
          {
            localSSLSocket.close();
          }
          catch (Exception paramString) {}
          throw paramSocket;
        }
      }
      throw new IllegalArgumentException("Parameters may not be null.");
    }
    throw new IllegalArgumentException("Target host may not be null.");
  }
  
  public Socket createSocket()
    throws IOException
  {
    return (SSLSocket)socketfactory.createSocket();
  }
  
  public Socket createSocket(Socket paramSocket, String paramString, int paramInt, boolean paramBoolean)
    throws IOException, UnknownHostException
  {
    paramSocket = (SSLSocket)socketfactory.createSocket(paramSocket, paramString, paramInt, paramBoolean);
    paramSocket.startHandshake();
    hostnameVerifier.verify(paramString, paramSocket);
    return paramSocket;
  }
  
  public X509HostnameVerifier getHostnameVerifier()
  {
    return hostnameVerifier;
  }
  
  public boolean isSecure(Socket paramSocket)
    throws IllegalArgumentException
  {
    if (paramSocket != null)
    {
      if ((paramSocket instanceof SSLSocket))
      {
        if (!paramSocket.isClosed()) {
          return true;
        }
        throw new IllegalArgumentException("Socket is closed.");
      }
      throw new IllegalArgumentException("Socket not created by this factory.");
    }
    throw new IllegalArgumentException("Socket may not be null.");
  }
  
  public void setHostnameVerifier(X509HostnameVerifier paramX509HostnameVerifier)
  {
    if (paramX509HostnameVerifier != null)
    {
      hostnameVerifier = paramX509HostnameVerifier;
      return;
    }
    throw new IllegalArgumentException("Hostname verifier may not be null");
  }
  
  private static class NoPreloadHolder
  {
    private static final SSLSocketFactory DEFAULT_FACTORY = new SSLSocketFactory(null);
    
    private NoPreloadHolder() {}
  }
}
