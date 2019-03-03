package android.security.keystore;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Principal;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.security.auth.x500.X500Principal;

class DelegatingX509Certificate
  extends X509Certificate
{
  private final X509Certificate mDelegate;
  
  DelegatingX509Certificate(X509Certificate paramX509Certificate)
  {
    mDelegate = paramX509Certificate;
  }
  
  public void checkValidity()
    throws CertificateExpiredException, CertificateNotYetValidException
  {
    mDelegate.checkValidity();
  }
  
  public void checkValidity(Date paramDate)
    throws CertificateExpiredException, CertificateNotYetValidException
  {
    mDelegate.checkValidity(paramDate);
  }
  
  public int getBasicConstraints()
  {
    return mDelegate.getBasicConstraints();
  }
  
  public Set<String> getCriticalExtensionOIDs()
  {
    return mDelegate.getCriticalExtensionOIDs();
  }
  
  public byte[] getEncoded()
    throws CertificateEncodingException
  {
    return mDelegate.getEncoded();
  }
  
  public List<String> getExtendedKeyUsage()
    throws CertificateParsingException
  {
    return mDelegate.getExtendedKeyUsage();
  }
  
  public byte[] getExtensionValue(String paramString)
  {
    return mDelegate.getExtensionValue(paramString);
  }
  
  public Collection<List<?>> getIssuerAlternativeNames()
    throws CertificateParsingException
  {
    return mDelegate.getIssuerAlternativeNames();
  }
  
  public Principal getIssuerDN()
  {
    return mDelegate.getIssuerDN();
  }
  
  public boolean[] getIssuerUniqueID()
  {
    return mDelegate.getIssuerUniqueID();
  }
  
  public X500Principal getIssuerX500Principal()
  {
    return mDelegate.getIssuerX500Principal();
  }
  
  public boolean[] getKeyUsage()
  {
    return mDelegate.getKeyUsage();
  }
  
  public Set<String> getNonCriticalExtensionOIDs()
  {
    return mDelegate.getNonCriticalExtensionOIDs();
  }
  
  public Date getNotAfter()
  {
    return mDelegate.getNotAfter();
  }
  
  public Date getNotBefore()
  {
    return mDelegate.getNotBefore();
  }
  
  public PublicKey getPublicKey()
  {
    return mDelegate.getPublicKey();
  }
  
  public BigInteger getSerialNumber()
  {
    return mDelegate.getSerialNumber();
  }
  
  public String getSigAlgName()
  {
    return mDelegate.getSigAlgName();
  }
  
  public String getSigAlgOID()
  {
    return mDelegate.getSigAlgOID();
  }
  
  public byte[] getSigAlgParams()
  {
    return mDelegate.getSigAlgParams();
  }
  
  public byte[] getSignature()
  {
    return mDelegate.getSignature();
  }
  
  public Collection<List<?>> getSubjectAlternativeNames()
    throws CertificateParsingException
  {
    return mDelegate.getSubjectAlternativeNames();
  }
  
  public Principal getSubjectDN()
  {
    return mDelegate.getSubjectDN();
  }
  
  public boolean[] getSubjectUniqueID()
  {
    return mDelegate.getSubjectUniqueID();
  }
  
  public X500Principal getSubjectX500Principal()
  {
    return mDelegate.getSubjectX500Principal();
  }
  
  public byte[] getTBSCertificate()
    throws CertificateEncodingException
  {
    return mDelegate.getTBSCertificate();
  }
  
  public int getVersion()
  {
    return mDelegate.getVersion();
  }
  
  public boolean hasUnsupportedCriticalExtension()
  {
    return mDelegate.hasUnsupportedCriticalExtension();
  }
  
  public String toString()
  {
    return mDelegate.toString();
  }
  
  public void verify(PublicKey paramPublicKey)
    throws CertificateException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SignatureException
  {
    mDelegate.verify(paramPublicKey);
  }
  
  public void verify(PublicKey paramPublicKey, String paramString)
    throws CertificateException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SignatureException
  {
    mDelegate.verify(paramPublicKey, paramString);
  }
}
