package android.net.http;

import java.security.cert.X509Certificate;

public class SslError
{
  public static final int SSL_DATE_INVALID = 4;
  public static final int SSL_EXPIRED = 1;
  public static final int SSL_IDMISMATCH = 2;
  public static final int SSL_INVALID = 5;
  @Deprecated
  public static final int SSL_MAX_ERROR = 6;
  public static final int SSL_NOTYETVALID = 0;
  public static final int SSL_UNTRUSTED = 3;
  final SslCertificate mCertificate;
  int mErrors;
  final String mUrl;
  
  @Deprecated
  public SslError(int paramInt, SslCertificate paramSslCertificate)
  {
    this(paramInt, paramSslCertificate, "");
  }
  
  public SslError(int paramInt, SslCertificate paramSslCertificate, String paramString)
  {
    addError(paramInt);
    mCertificate = paramSslCertificate;
    mUrl = paramString;
  }
  
  @Deprecated
  public SslError(int paramInt, X509Certificate paramX509Certificate)
  {
    this(paramInt, paramX509Certificate, "");
  }
  
  public SslError(int paramInt, X509Certificate paramX509Certificate, String paramString)
  {
    this(paramInt, new SslCertificate(paramX509Certificate), paramString);
  }
  
  public static SslError SslErrorFromChromiumErrorCode(int paramInt, SslCertificate paramSslCertificate, String paramString)
  {
    if (paramInt == 65336) {
      return new SslError(2, paramSslCertificate, paramString);
    }
    if (paramInt == 65335) {
      return new SslError(4, paramSslCertificate, paramString);
    }
    if (paramInt == 65334) {
      return new SslError(3, paramSslCertificate, paramString);
    }
    return new SslError(5, paramSslCertificate, paramString);
  }
  
  public boolean addError(int paramInt)
  {
    boolean bool;
    if ((paramInt >= 0) && (paramInt < 6)) {
      bool = true;
    } else {
      bool = false;
    }
    if (bool) {
      mErrors = (1 << paramInt | mErrors);
    }
    return bool;
  }
  
  public SslCertificate getCertificate()
  {
    return mCertificate;
  }
  
  public int getPrimaryError()
  {
    if (mErrors != 0) {
      for (int i = 5; i >= 0; i--) {
        if ((mErrors & 1 << i) != 0) {
          return i;
        }
      }
    }
    return -1;
  }
  
  public String getUrl()
  {
    return mUrl;
  }
  
  public boolean hasError(int paramInt)
  {
    boolean bool1 = false;
    boolean bool2;
    if ((paramInt >= 0) && (paramInt < 6)) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    boolean bool3 = bool2;
    if (bool2)
    {
      bool2 = bool1;
      if ((mErrors & 1 << paramInt) != 0) {
        bool2 = true;
      }
      bool3 = bool2;
    }
    return bool3;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("primary error: ");
    localStringBuilder.append(getPrimaryError());
    localStringBuilder.append(" certificate: ");
    localStringBuilder.append(getCertificate());
    localStringBuilder.append(" on URL: ");
    localStringBuilder.append(getUrl());
    return localStringBuilder.toString();
  }
}
