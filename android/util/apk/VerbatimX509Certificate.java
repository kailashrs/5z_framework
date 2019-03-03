package android.util.apk;

import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.Arrays;

class VerbatimX509Certificate
  extends WrappedX509Certificate
{
  private final byte[] mEncodedVerbatim;
  private int mHash = -1;
  
  VerbatimX509Certificate(X509Certificate paramX509Certificate, byte[] paramArrayOfByte)
  {
    super(paramX509Certificate);
    mEncodedVerbatim = paramArrayOfByte;
  }
  
  public boolean equals(Object paramObject)
  {
    if (this == paramObject) {
      return true;
    }
    if (!(paramObject instanceof VerbatimX509Certificate)) {
      return false;
    }
    try
    {
      boolean bool = Arrays.equals(getEncoded(), ((VerbatimX509Certificate)paramObject).getEncoded());
      return bool;
    }
    catch (CertificateEncodingException paramObject) {}
    return false;
  }
  
  public byte[] getEncoded()
    throws CertificateEncodingException
  {
    return mEncodedVerbatim;
  }
  
  public int hashCode()
  {
    if (mHash == -1) {
      try
      {
        mHash = Arrays.hashCode(getEncoded());
      }
      catch (CertificateEncodingException localCertificateEncodingException)
      {
        mHash = 0;
      }
    }
    return mHash;
  }
}
