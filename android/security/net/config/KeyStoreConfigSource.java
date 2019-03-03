package android.security.net.config;

import android.util.Pair;
import java.security.KeyStore;
import java.util.Set;

class KeyStoreConfigSource
  implements ConfigSource
{
  private final NetworkSecurityConfig mConfig;
  
  public KeyStoreConfigSource(KeyStore paramKeyStore)
  {
    mConfig = new NetworkSecurityConfig.Builder().addCertificatesEntryRef(new CertificatesEntryRef(new KeyStoreCertificateSource(paramKeyStore), false)).build();
  }
  
  public NetworkSecurityConfig getDefaultConfig()
  {
    return mConfig;
  }
  
  public Set<Pair<Domain, NetworkSecurityConfig>> getPerDomainConfigs()
  {
    return null;
  }
}
