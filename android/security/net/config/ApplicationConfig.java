package android.security.net.config;

import android.util.Pair;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;
import javax.net.ssl.X509TrustManager;

public final class ApplicationConfig
{
  private static ApplicationConfig sInstance;
  private static Object sLock = new Object();
  private ConfigSource mConfigSource;
  private Set<Pair<Domain, NetworkSecurityConfig>> mConfigs;
  private NetworkSecurityConfig mDefaultConfig;
  private boolean mInitialized;
  private final Object mLock = new Object();
  private X509TrustManager mTrustManager;
  
  public ApplicationConfig(ConfigSource paramConfigSource)
  {
    mConfigSource = paramConfigSource;
    mInitialized = false;
  }
  
  private void ensureInitialized()
  {
    synchronized (mLock)
    {
      if (mInitialized) {
        return;
      }
      mConfigs = mConfigSource.getPerDomainConfigs();
      mDefaultConfig = mConfigSource.getDefaultConfig();
      mConfigSource = null;
      RootTrustManager localRootTrustManager = new android/security/net/config/RootTrustManager;
      localRootTrustManager.<init>(this);
      mTrustManager = localRootTrustManager;
      mInitialized = true;
      return;
    }
  }
  
  public static ApplicationConfig getDefaultInstance()
  {
    synchronized (sLock)
    {
      ApplicationConfig localApplicationConfig = sInstance;
      return localApplicationConfig;
    }
  }
  
  public static void setDefaultInstance(ApplicationConfig paramApplicationConfig)
  {
    synchronized (sLock)
    {
      sInstance = paramApplicationConfig;
      return;
    }
  }
  
  public NetworkSecurityConfig getConfigForHostname(String paramString)
  {
    ensureInitialized();
    if ((paramString != null) && (!paramString.isEmpty()) && (mConfigs != null))
    {
      if (paramString.charAt(0) != '.')
      {
        paramString = paramString.toLowerCase(Locale.US);
        String str1 = paramString;
        if (paramString.charAt(paramString.length() - 1) == '.') {
          str1 = paramString.substring(0, paramString.length() - 1);
        }
        String str2 = null;
        Iterator localIterator = mConfigs.iterator();
        while (localIterator.hasNext())
        {
          Pair localPair = (Pair)localIterator.next();
          Domain localDomain = (Domain)first;
          paramString = (NetworkSecurityConfig)second;
          if (hostname.equals(str1)) {
            return paramString;
          }
          paramString = str2;
          if (subdomainsIncluded)
          {
            paramString = str2;
            if (str1.endsWith(hostname))
            {
              paramString = str2;
              if (str1.charAt(str1.length() - hostname.length() - 1) == '.') {
                if (str2 == null)
                {
                  paramString = localPair;
                }
                else
                {
                  paramString = str2;
                  if (hostname.length() > first).hostname.length()) {
                    paramString = localPair;
                  }
                }
              }
            }
          }
          str2 = paramString;
        }
        if (str2 != null) {
          return (NetworkSecurityConfig)second;
        }
        return mDefaultConfig;
      }
      throw new IllegalArgumentException("hostname must not begin with a .");
    }
    return mDefaultConfig;
  }
  
  public X509TrustManager getTrustManager()
  {
    ensureInitialized();
    return mTrustManager;
  }
  
  public void handleTrustStorageUpdate()
  {
    synchronized (mLock)
    {
      if (!mInitialized) {
        return;
      }
      mDefaultConfig.handleTrustStorageUpdate();
      if (mConfigs != null)
      {
        HashSet localHashSet = new java/util/HashSet;
        localHashSet.<init>(mConfigs.size());
        Iterator localIterator = mConfigs.iterator();
        while (localIterator.hasNext())
        {
          Pair localPair = (Pair)localIterator.next();
          if (localHashSet.add((NetworkSecurityConfig)second)) {
            ((NetworkSecurityConfig)second).handleTrustStorageUpdate();
          }
        }
      }
      return;
    }
  }
  
  public boolean hasPerDomainConfigs()
  {
    ensureInitialized();
    boolean bool;
    if ((mConfigs != null) && (!mConfigs.isEmpty())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isCleartextTrafficPermitted()
  {
    ensureInitialized();
    if (mConfigs != null)
    {
      Iterator localIterator = mConfigs.iterator();
      while (localIterator.hasNext()) {
        if (!((NetworkSecurityConfig)nextsecond).isCleartextTrafficPermitted()) {
          return false;
        }
      }
    }
    return mDefaultConfig.isCleartextTrafficPermitted();
  }
  
  public boolean isCleartextTrafficPermitted(String paramString)
  {
    return getConfigForHostname(paramString).isCleartextTrafficPermitted();
  }
}
