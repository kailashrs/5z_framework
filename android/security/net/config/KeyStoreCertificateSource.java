package android.security.net.config;

import android.util.ArraySet;
import com.android.org.conscrypt.TrustedCertificateIndex;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.cert.TrustAnchor;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Set;

class KeyStoreCertificateSource
  implements CertificateSource
{
  private Set<X509Certificate> mCertificates;
  private TrustedCertificateIndex mIndex;
  private final KeyStore mKeyStore;
  private final Object mLock = new Object();
  
  public KeyStoreCertificateSource(KeyStore paramKeyStore)
  {
    mKeyStore = paramKeyStore;
  }
  
  private void ensureInitialized()
  {
    synchronized (mLock)
    {
      if (mCertificates != null) {
        return;
      }
      try
      {
        TrustedCertificateIndex localTrustedCertificateIndex = new com/android/org/conscrypt/TrustedCertificateIndex;
        localTrustedCertificateIndex.<init>();
        ArraySet localArraySet = new android/util/ArraySet;
        localArraySet.<init>(mKeyStore.size());
        localObject3 = mKeyStore.aliases();
        while (((Enumeration)localObject3).hasMoreElements())
        {
          Object localObject4 = (String)((Enumeration)localObject3).nextElement();
          localObject4 = (X509Certificate)mKeyStore.getCertificate((String)localObject4);
          if (localObject4 != null)
          {
            localArraySet.add(localObject4);
            localTrustedCertificateIndex.index((X509Certificate)localObject4);
          }
        }
        mIndex = localTrustedCertificateIndex;
        mCertificates = localArraySet;
        return;
      }
      catch (KeyStoreException localKeyStoreException)
      {
        Object localObject3 = new java/lang/RuntimeException;
        ((RuntimeException)localObject3).<init>("Failed to load certificates from KeyStore", localKeyStoreException);
        throw ((Throwable)localObject3);
      }
    }
  }
  
  public Set<X509Certificate> findAllByIssuerAndSignature(X509Certificate paramX509Certificate)
  {
    ensureInitialized();
    Object localObject = mIndex.findAllByIssuerAndSignature(paramX509Certificate);
    if (((Set)localObject).isEmpty()) {
      return Collections.emptySet();
    }
    paramX509Certificate = new ArraySet(((Set)localObject).size());
    localObject = ((Set)localObject).iterator();
    while (((Iterator)localObject).hasNext()) {
      paramX509Certificate.add(((TrustAnchor)((Iterator)localObject).next()).getTrustedCert());
    }
    return paramX509Certificate;
  }
  
  public X509Certificate findByIssuerAndSignature(X509Certificate paramX509Certificate)
  {
    ensureInitialized();
    paramX509Certificate = mIndex.findByIssuerAndSignature(paramX509Certificate);
    if (paramX509Certificate == null) {
      return null;
    }
    return paramX509Certificate.getTrustedCert();
  }
  
  public X509Certificate findBySubjectAndPublicKey(X509Certificate paramX509Certificate)
  {
    ensureInitialized();
    paramX509Certificate = mIndex.findBySubjectAndPublicKey(paramX509Certificate);
    if (paramX509Certificate == null) {
      return null;
    }
    return paramX509Certificate.getTrustedCert();
  }
  
  public Set<X509Certificate> getCertificates()
  {
    ensureInitialized();
    return mCertificates;
  }
  
  public void handleTrustStorageUpdate() {}
}
