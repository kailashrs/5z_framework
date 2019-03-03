package android.security.net.config;

import java.security.cert.X509Certificate;

public final class TrustAnchor
{
  public final X509Certificate certificate;
  public final boolean overridesPins;
  
  public TrustAnchor(X509Certificate paramX509Certificate, boolean paramBoolean)
  {
    if (paramX509Certificate != null)
    {
      certificate = paramX509Certificate;
      overridesPins = paramBoolean;
      return;
    }
    throw new NullPointerException("certificate");
  }
}
