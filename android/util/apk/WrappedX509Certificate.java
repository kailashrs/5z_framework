package android.util.apk;

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
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Set;

class WrappedX509Certificate
  extends X509Certificate
{
  private final X509Certificate mWrapped;
  
  WrappedX509Certificate(X509Certificate paramX509Certificate)
  {
    mWrapped = paramX509Certificate;
  }
  
  public void checkValidity()
    throws CertificateExpiredException, CertificateNotYetValidException
  {
    mWrapped.checkValidity();
  }
  
  public void checkValidity(Date paramDate)
    throws CertificateExpiredException, CertificateNotYetValidException
  {
    mWrapped.checkValidity(paramDate);
  }
  
  public int getBasicConstraints()
  {
    return mWrapped.getBasicConstraints();
  }
  
  public Set<String> getCriticalExtensionOIDs()
  {
    return mWrapped.getCriticalExtensionOIDs();
  }
  
  public byte[] getEncoded()
    throws CertificateEncodingException
  {
    return mWrapped.getEncoded();
  }
  
  public byte[] getExtensionValue(String paramString)
  {
    return mWrapped.getExtensionValue(paramString);
  }
  
  public Principal getIssuerDN()
  {
    return mWrapped.getIssuerDN();
  }
  
  public boolean[] getIssuerUniqueID()
  {
    return mWrapped.getIssuerUniqueID();
  }
  
  public boolean[] getKeyUsage()
  {
    return mWrapped.getKeyUsage();
  }
  
  public Set<String> getNonCriticalExtensionOIDs()
  {
    return mWrapped.getNonCriticalExtensionOIDs();
  }
  
  public Date getNotAfter()
  {
    return mWrapped.getNotAfter();
  }
  
  public Date getNotBefore()
  {
    return mWrapped.getNotBefore();
  }
  
  public PublicKey getPublicKey()
  {
    return mWrapped.getPublicKey();
  }
  
  public BigInteger getSerialNumber()
  {
    return mWrapped.getSerialNumber();
  }
  
  public String getSigAlgName()
  {
    return mWrapped.getSigAlgName();
  }
  
  public String getSigAlgOID()
  {
    return mWrapped.getSigAlgOID();
  }
  
  public byte[] getSigAlgParams()
  {
    return mWrapped.getSigAlgParams();
  }
  
  public byte[] getSignature()
  {
    return mWrapped.getSignature();
  }
  
  public Principal getSubjectDN()
  {
    return mWrapped.getSubjectDN();
  }
  
  public boolean[] getSubjectUniqueID()
  {
    return mWrapped.getSubjectUniqueID();
  }
  
  public byte[] getTBSCertificate()
    throws CertificateEncodingException
  {
    return mWrapped.getTBSCertificate();
  }
  
  public int getVersion()
  {
    return mWrapped.getVersion();
  }
  
  public boolean hasUnsupportedCriticalExtension()
  {
    return mWrapped.hasUnsupportedCriticalExtension();
  }
  
  public String toString()
  {
    return mWrapped.toString();
  }
  
  public void verify(PublicKey paramPublicKey)
    throws CertificateException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SignatureException
  {
    mWrapped.verify(paramPublicKey);
  }
  
  public void verify(PublicKey paramPublicKey, String paramString)
    throws CertificateException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SignatureException
  {
    mWrapped.verify(paramPublicKey, paramString);
  }
}
