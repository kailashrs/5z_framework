package android.security.net.config;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.res.Resources;
import android.util.Log;
import android.util.Pair;
import java.util.Set;

public class ManifestConfigSource
  implements ConfigSource
{
  private static final boolean DBG = true;
  private static final String LOG_TAG = "NetworkSecurityConfig";
  private final ApplicationInfo mApplicationInfo;
  private ConfigSource mConfigSource;
  private final Context mContext;
  private final Object mLock = new Object();
  
  public ManifestConfigSource(Context paramContext)
  {
    mContext = paramContext;
    mApplicationInfo = new ApplicationInfo(paramContext.getApplicationInfo());
  }
  
  private ConfigSource getConfigSource()
  {
    synchronized (mLock)
    {
      if (mConfigSource != null)
      {
        localObject2 = mConfigSource;
        return localObject2;
      }
      int i = mApplicationInfo.networkSecurityConfigRes;
      boolean bool1 = false;
      boolean bool2 = false;
      if (i != 0)
      {
        if ((0x2 & mApplicationInfo.flags) != 0) {
          bool2 = true;
        }
        localObject2 = new java/lang/StringBuilder;
        ((StringBuilder)localObject2).<init>();
        ((StringBuilder)localObject2).append("Using Network Security Config from resource ");
        ((StringBuilder)localObject2).append(mContext.getResources().getResourceEntryName(i));
        ((StringBuilder)localObject2).append(" debugBuild: ");
        ((StringBuilder)localObject2).append(bool2);
        Log.d("NetworkSecurityConfig", ((StringBuilder)localObject2).toString());
        localObject2 = new android/security/net/config/XmlConfigSource;
        ((XmlConfigSource)localObject2).<init>(mContext, i, mApplicationInfo);
      }
      else
      {
        Log.d("NetworkSecurityConfig", "No Network Security Config specified, using platform default");
        bool2 = bool1;
        if ((mApplicationInfo.flags & 0x8000000) != 0)
        {
          bool2 = bool1;
          if (mApplicationInfo.targetSandboxVersion < 2) {
            bool2 = true;
          }
        }
        localObject2 = new DefaultConfigSource(bool2, mApplicationInfo);
      }
      mConfigSource = ((ConfigSource)localObject2);
      Object localObject2 = mConfigSource;
      return localObject2;
    }
  }
  
  public NetworkSecurityConfig getDefaultConfig()
  {
    return getConfigSource().getDefaultConfig();
  }
  
  public Set<Pair<Domain, NetworkSecurityConfig>> getPerDomainConfigs()
  {
    return getConfigSource().getPerDomainConfigs();
  }
  
  private static final class DefaultConfigSource
    implements ConfigSource
  {
    private final NetworkSecurityConfig mDefaultConfig;
    
    DefaultConfigSource(boolean paramBoolean, ApplicationInfo paramApplicationInfo)
    {
      mDefaultConfig = NetworkSecurityConfig.getDefaultBuilder(paramApplicationInfo).setCleartextTrafficPermitted(paramBoolean).build();
    }
    
    public NetworkSecurityConfig getDefaultConfig()
    {
      return mDefaultConfig;
    }
    
    public Set<Pair<Domain, NetworkSecurityConfig>> getPerDomainConfigs()
    {
      return null;
    }
  }
}
