package android.security.net.config;

import java.security.cert.X509Certificate;
import java.util.Set;

public abstract interface CertificateSource
{
  public abstract Set<X509Certificate> findAllByIssuerAndSignature(X509Certificate paramX509Certificate);
  
  public abstract X509Certificate findByIssuerAndSignature(X509Certificate paramX509Certificate);
  
  public abstract X509Certificate findBySubjectAndPublicKey(X509Certificate paramX509Certificate);
  
  public abstract Set<X509Certificate> getCertificates();
  
  public abstract void handleTrustStorageUpdate();
}
