package android.security.net.config;

import libcore.net.NetworkSecurityPolicy;

public class ConfigNetworkSecurityPolicy
  extends NetworkSecurityPolicy
{
  private final ApplicationConfig mConfig;
  
  public ConfigNetworkSecurityPolicy(ApplicationConfig paramApplicationConfig)
  {
    mConfig = paramApplicationConfig;
  }
  
  public boolean isCertificateTransparencyVerificationRequired(String paramString)
  {
    return false;
  }
  
  public boolean isCleartextTrafficPermitted()
  {
    return mConfig.isCleartextTrafficPermitted();
  }
  
  public boolean isCleartextTrafficPermitted(String paramString)
  {
    return mConfig.isCleartextTrafficPermitted(paramString);
  }
}
