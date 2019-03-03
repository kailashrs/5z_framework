package android.security.net.config;

import android.content.Context;
import java.security.Provider;
import java.security.Security;
import libcore.net.NetworkSecurityPolicy;

public final class NetworkSecurityConfigProvider
  extends Provider
{
  private static final String PREFIX;
  
  static
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(NetworkSecurityConfigProvider.class.getPackage().getName());
    localStringBuilder.append(".");
    PREFIX = localStringBuilder.toString();
  }
  
  public NetworkSecurityConfigProvider()
  {
    super("AndroidNSSP", 1.0D, "Android Network Security Policy Provider");
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(PREFIX);
    localStringBuilder.append("RootTrustManagerFactorySpi");
    put("TrustManagerFactory.PKIX", localStringBuilder.toString());
    put("Alg.Alias.TrustManagerFactory.X509", "PKIX");
  }
  
  public static void install(Context paramContext)
  {
    paramContext = new ApplicationConfig(new ManifestConfigSource(paramContext));
    ApplicationConfig.setDefaultInstance(paramContext);
    int i = Security.insertProviderAt(new NetworkSecurityConfigProvider(), 1);
    if (i == 1)
    {
      NetworkSecurityPolicy.setInstance(new ConfigNetworkSecurityPolicy(paramContext));
      return;
    }
    paramContext = new StringBuilder();
    paramContext.append("Failed to install provider as highest priority provider. Provider was installed at position ");
    paramContext.append(i);
    throw new RuntimeException(paramContext.toString());
  }
}
