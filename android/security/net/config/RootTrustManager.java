package android.security.net.config;

import java.net.Socket;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.X509ExtendedTrustManager;

public class RootTrustManager
  extends X509ExtendedTrustManager
{
  private final ApplicationConfig mConfig;
  
  public RootTrustManager(ApplicationConfig paramApplicationConfig)
  {
    if (paramApplicationConfig != null)
    {
      mConfig = paramApplicationConfig;
      return;
    }
    throw new NullPointerException("config must not be null");
  }
  
  public void checkClientTrusted(X509Certificate[] paramArrayOfX509Certificate, String paramString)
    throws CertificateException
  {
    mConfig.getConfigForHostname("").getTrustManager().checkClientTrusted(paramArrayOfX509Certificate, paramString);
  }
  
  public void checkClientTrusted(X509Certificate[] paramArrayOfX509Certificate, String paramString, Socket paramSocket)
    throws CertificateException
  {
    mConfig.getConfigForHostname("").getTrustManager().checkClientTrusted(paramArrayOfX509Certificate, paramString, paramSocket);
  }
  
  public void checkClientTrusted(X509Certificate[] paramArrayOfX509Certificate, String paramString, SSLEngine paramSSLEngine)
    throws CertificateException
  {
    mConfig.getConfigForHostname("").getTrustManager().checkClientTrusted(paramArrayOfX509Certificate, paramString, paramSSLEngine);
  }
  
  public List<X509Certificate> checkServerTrusted(X509Certificate[] paramArrayOfX509Certificate, String paramString1, String paramString2)
    throws CertificateException
  {
    if ((paramString2 == null) && (mConfig.hasPerDomainConfigs())) {
      throw new CertificateException("Domain specific configurations require that the hostname be provided");
    }
    return mConfig.getConfigForHostname(paramString2).getTrustManager().checkServerTrusted(paramArrayOfX509Certificate, paramString1, paramString2);
  }
  
  public void checkServerTrusted(X509Certificate[] paramArrayOfX509Certificate, String paramString)
    throws CertificateException
  {
    if (!mConfig.hasPerDomainConfigs())
    {
      mConfig.getConfigForHostname("").getTrustManager().checkServerTrusted(paramArrayOfX509Certificate, paramString);
      return;
    }
    throw new CertificateException("Domain specific configurations require that hostname aware checkServerTrusted(X509Certificate[], String, String) is used");
  }
  
  public void checkServerTrusted(X509Certificate[] paramArrayOfX509Certificate, String paramString, Socket paramSocket)
    throws CertificateException
  {
    if ((paramSocket instanceof SSLSocket))
    {
      Object localObject = ((SSLSocket)paramSocket).getHandshakeSession();
      if (localObject != null)
      {
        localObject = ((SSLSession)localObject).getPeerHost();
        mConfig.getConfigForHostname((String)localObject).getTrustManager().checkServerTrusted(paramArrayOfX509Certificate, paramString, paramSocket);
      }
      else
      {
        throw new CertificateException("Not in handshake; no session available");
      }
    }
    else
    {
      checkServerTrusted(paramArrayOfX509Certificate, paramString);
    }
  }
  
  public void checkServerTrusted(X509Certificate[] paramArrayOfX509Certificate, String paramString, SSLEngine paramSSLEngine)
    throws CertificateException
  {
    Object localObject = paramSSLEngine.getHandshakeSession();
    if (localObject != null)
    {
      localObject = ((SSLSession)localObject).getPeerHost();
      mConfig.getConfigForHostname((String)localObject).getTrustManager().checkServerTrusted(paramArrayOfX509Certificate, paramString, paramSSLEngine);
      return;
    }
    throw new CertificateException("Not in handshake; no session available");
  }
  
  public X509Certificate[] getAcceptedIssuers()
  {
    return mConfig.getConfigForHostname("").getTrustManager().getAcceptedIssuers();
  }
  
  public boolean isSameTrustConfiguration(String paramString1, String paramString2)
  {
    return mConfig.getConfigForHostname(paramString1).equals(mConfig.getConfigForHostname(paramString2));
  }
}
