package android.security.net.config;

import android.util.Pair;
import java.util.Set;

public abstract interface ConfigSource
{
  public abstract NetworkSecurityConfig getDefaultConfig();
  
  public abstract Set<Pair<Domain, NetworkSecurityConfig>> getPerDomainConfigs();
}
