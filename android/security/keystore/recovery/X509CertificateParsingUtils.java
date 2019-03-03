package android.security.keystore.recovery;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.Base64.Decoder;

public class X509CertificateParsingUtils
{
  private static final String CERT_FORMAT = "X.509";
  
  public X509CertificateParsingUtils() {}
  
  private static byte[] decodeBase64(String paramString)
  {
    return Base64.getDecoder().decode(paramString);
  }
  
  public static X509Certificate decodeBase64Cert(String paramString)
    throws CertificateException
  {
    try
    {
      paramString = decodeCert(decodeBase64(paramString));
      return paramString;
    }
    catch (IllegalArgumentException paramString)
    {
      throw new CertificateException(paramString);
    }
  }
  
  private static X509Certificate decodeCert(InputStream paramInputStream)
    throws CertificateException
  {
    try
    {
      CertificateFactory localCertificateFactory = CertificateFactory.getInstance("X.509");
      return (X509Certificate)localCertificateFactory.generateCertificate(paramInputStream);
    }
    catch (CertificateException paramInputStream)
    {
      throw new RuntimeException(paramInputStream);
    }
  }
  
  private static X509Certificate decodeCert(byte[] paramArrayOfByte)
    throws CertificateException
  {
    return decodeCert(new ByteArrayInputStream(paramArrayOfByte));
  }
}
