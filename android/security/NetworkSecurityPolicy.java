package android.security;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.security.net.config.ApplicationConfig;
import android.security.net.config.ManifestConfigSource;

public class NetworkSecurityPolicy
{
  private static final NetworkSecurityPolicy INSTANCE = new NetworkSecurityPolicy();
  
  private NetworkSecurityPolicy() {}
  
  public static ApplicationConfig getApplicationConfigForPackage(Context paramContext, String paramString)
    throws PackageManager.NameNotFoundException
  {
    return new ApplicationConfig(new ManifestConfigSource(paramContext.createPackageContext(paramString, 0)));
  }
  
  public static NetworkSecurityPolicy getInstance()
  {
    return INSTANCE;
  }
  
  public void handleTrustStorageUpdate()
  {
    ApplicationConfig localApplicationConfig = ApplicationConfig.getDefaultInstance();
    if (localApplicationConfig != null) {
      localApplicationConfig.handleTrustStorageUpdate();
    }
  }
  
  public boolean isCleartextTrafficPermitted()
  {
    return libcore.net.NetworkSecurityPolicy.getInstance().isCleartextTrafficPermitted();
  }
  
  public boolean isCleartextTrafficPermitted(String paramString)
  {
    return libcore.net.NetworkSecurityPolicy.getInstance().isCleartextTrafficPermitted(paramString);
  }
  
  public void setCleartextTrafficPermitted(boolean paramBoolean)
  {
    libcore.net.NetworkSecurityPolicy.setInstance(new FrameworkNetworkSecurityPolicy(paramBoolean));
  }
}
