package android.security;

import libcore.net.NetworkSecurityPolicy;

public class FrameworkNetworkSecurityPolicy
  extends NetworkSecurityPolicy
{
  private final boolean mCleartextTrafficPermitted;
  
  public FrameworkNetworkSecurityPolicy(boolean paramBoolean)
  {
    mCleartextTrafficPermitted = paramBoolean;
  }
  
  public boolean isCertificateTransparencyVerificationRequired(String paramString)
  {
    return false;
  }
  
  public boolean isCleartextTrafficPermitted()
  {
    return mCleartextTrafficPermitted;
  }
  
  public boolean isCleartextTrafficPermitted(String paramString)
  {
    return isCleartextTrafficPermitted();
  }
}
