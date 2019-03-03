package android.security.net.config;

import android.util.ArrayMap;
import com.android.org.conscrypt.TrustManagerImpl;
import java.io.IOException;
import java.net.Socket;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.X509ExtendedTrustManager;

public class NetworkSecurityTrustManager
  extends X509ExtendedTrustManager
{
  private final TrustManagerImpl mDelegate;
  private X509Certificate[] mIssuers;
  private final Object mIssuersLock = new Object();
  private final NetworkSecurityConfig mNetworkSecurityConfig;
  
  public NetworkSecurityTrustManager(NetworkSecurityConfig paramNetworkSecurityConfig)
  {
    if (paramNetworkSecurityConfig != null)
    {
      mNetworkSecurityConfig = paramNetworkSecurityConfig;
      try
      {
        TrustedCertificateStoreAdapter localTrustedCertificateStoreAdapter = new android/security/net/config/TrustedCertificateStoreAdapter;
        localTrustedCertificateStoreAdapter.<init>(paramNetworkSecurityConfig);
        KeyStore localKeyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        localKeyStore.load(null);
        paramNetworkSecurityConfig = new com/android/org/conscrypt/TrustManagerImpl;
        paramNetworkSecurityConfig.<init>(localKeyStore, null, localTrustedCertificateStoreAdapter);
        mDelegate = paramNetworkSecurityConfig;
        return;
      }
      catch (GeneralSecurityException|IOException paramNetworkSecurityConfig)
      {
        throw new RuntimeException(paramNetworkSecurityConfig);
      }
    }
    throw new NullPointerException("config must not be null");
  }
  
  private void checkPins(List<X509Certificate> paramList)
    throws CertificateException
  {
    PinSet localPinSet = mNetworkSecurityConfig.getPins();
    if ((!pins.isEmpty()) && (System.currentTimeMillis() <= expirationTime) && (isPinningEnforced(paramList)))
    {
      Set localSet = localPinSet.getPinAlgorithms();
      ArrayMap localArrayMap = new ArrayMap(localSet.size());
      for (int i = paramList.size() - 1; i >= 0; i--)
      {
        byte[] arrayOfByte = ((X509Certificate)paramList.get(i)).getPublicKey().getEncoded();
        Iterator localIterator = localSet.iterator();
        while (localIterator.hasNext())
        {
          String str = (String)localIterator.next();
          MessageDigest localMessageDigest1 = (MessageDigest)localArrayMap.get(str);
          MessageDigest localMessageDigest2 = localMessageDigest1;
          if (localMessageDigest1 == null) {
            try
            {
              localMessageDigest2 = MessageDigest.getInstance(str);
              localArrayMap.put(str, localMessageDigest2);
            }
            catch (GeneralSecurityException paramList)
            {
              throw new RuntimeException(paramList);
            }
          }
          if (pins.contains(new Pin(str, localMessageDigest2.digest(arrayOfByte)))) {
            return;
          }
        }
      }
      throw new CertificateException("Pin verification failed");
    }
  }
  
  private boolean isPinningEnforced(List<X509Certificate> paramList)
    throws CertificateException
  {
    if (paramList.isEmpty()) {
      return false;
    }
    paramList = (X509Certificate)paramList.get(paramList.size() - 1);
    paramList = mNetworkSecurityConfig.findTrustAnchorBySubjectAndPublicKey(paramList);
    if (paramList != null) {
      return overridesPins ^ true;
    }
    throw new CertificateException("Trusted chain does not end in a TrustAnchor");
  }
  
  public void checkClientTrusted(X509Certificate[] paramArrayOfX509Certificate, String paramString)
    throws CertificateException
  {
    mDelegate.checkClientTrusted(paramArrayOfX509Certificate, paramString);
  }
  
  public void checkClientTrusted(X509Certificate[] paramArrayOfX509Certificate, String paramString, Socket paramSocket)
    throws CertificateException
  {
    mDelegate.checkClientTrusted(paramArrayOfX509Certificate, paramString, paramSocket);
  }
  
  public void checkClientTrusted(X509Certificate[] paramArrayOfX509Certificate, String paramString, SSLEngine paramSSLEngine)
    throws CertificateException
  {
    mDelegate.checkClientTrusted(paramArrayOfX509Certificate, paramString, paramSSLEngine);
  }
  
  public List<X509Certificate> checkServerTrusted(X509Certificate[] paramArrayOfX509Certificate, String paramString1, String paramString2)
    throws CertificateException
  {
    paramArrayOfX509Certificate = mDelegate.checkServerTrusted(paramArrayOfX509Certificate, paramString1, paramString2);
    checkPins(paramArrayOfX509Certificate);
    return paramArrayOfX509Certificate;
  }
  
  public void checkServerTrusted(X509Certificate[] paramArrayOfX509Certificate, String paramString)
    throws CertificateException
  {
    checkServerTrusted(paramArrayOfX509Certificate, paramString, (String)null);
  }
  
  public void checkServerTrusted(X509Certificate[] paramArrayOfX509Certificate, String paramString, Socket paramSocket)
    throws CertificateException
  {
    checkPins(mDelegate.getTrustedChainForServer(paramArrayOfX509Certificate, paramString, paramSocket));
  }
  
  public void checkServerTrusted(X509Certificate[] paramArrayOfX509Certificate, String paramString, SSLEngine paramSSLEngine)
    throws CertificateException
  {
    checkPins(mDelegate.getTrustedChainForServer(paramArrayOfX509Certificate, paramString, paramSSLEngine));
  }
  
  public X509Certificate[] getAcceptedIssuers()
  {
    synchronized (mIssuersLock)
    {
      if (mIssuers == null)
      {
        Object localObject2 = mNetworkSecurityConfig.getTrustAnchors();
        arrayOfX509Certificate = new X509Certificate[((Set)localObject2).size()];
        int i = 0;
        localObject2 = ((Set)localObject2).iterator();
        while (((Iterator)localObject2).hasNext())
        {
          arrayOfX509Certificate[i] = nextcertificate;
          i++;
        }
        mIssuers = arrayOfX509Certificate;
      }
      X509Certificate[] arrayOfX509Certificate = (X509Certificate[])mIssuers.clone();
      return arrayOfX509Certificate;
    }
  }
  
  public void handleTrustStorageUpdate()
  {
    synchronized (mIssuersLock)
    {
      mIssuers = null;
      mDelegate.handleTrustStorageUpdate();
      return;
    }
  }
}
