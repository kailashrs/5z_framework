package android.security.net.config;

import android.util.ArraySet;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.Set;

public final class CertificatesEntryRef
{
  private final boolean mOverridesPins;
  private final CertificateSource mSource;
  
  public CertificatesEntryRef(CertificateSource paramCertificateSource, boolean paramBoolean)
  {
    mSource = paramCertificateSource;
    mOverridesPins = paramBoolean;
  }
  
  public Set<X509Certificate> findAllCertificatesByIssuerAndSignature(X509Certificate paramX509Certificate)
  {
    return mSource.findAllByIssuerAndSignature(paramX509Certificate);
  }
  
  public TrustAnchor findByIssuerAndSignature(X509Certificate paramX509Certificate)
  {
    paramX509Certificate = mSource.findByIssuerAndSignature(paramX509Certificate);
    if (paramX509Certificate == null) {
      return null;
    }
    return new TrustAnchor(paramX509Certificate, mOverridesPins);
  }
  
  public TrustAnchor findBySubjectAndPublicKey(X509Certificate paramX509Certificate)
  {
    paramX509Certificate = mSource.findBySubjectAndPublicKey(paramX509Certificate);
    if (paramX509Certificate == null) {
      return null;
    }
    return new TrustAnchor(paramX509Certificate, mOverridesPins);
  }
  
  public Set<TrustAnchor> getTrustAnchors()
  {
    ArraySet localArraySet = new ArraySet();
    Iterator localIterator = mSource.getCertificates().iterator();
    while (localIterator.hasNext()) {
      localArraySet.add(new TrustAnchor((X509Certificate)localIterator.next(), mOverridesPins));
    }
    return localArraySet;
  }
  
  public void handleTrustStorageUpdate()
  {
    mSource.handleTrustStorageUpdate();
  }
  
  boolean overridesPins()
  {
    return mOverridesPins;
  }
}
