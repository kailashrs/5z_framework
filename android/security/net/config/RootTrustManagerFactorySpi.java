package android.security.net.config;

import com.android.internal.annotations.VisibleForTesting;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import javax.net.ssl.ManagerFactoryParameters;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactorySpi;

public class RootTrustManagerFactorySpi
  extends TrustManagerFactorySpi
{
  private ApplicationConfig mApplicationConfig;
  private NetworkSecurityConfig mConfig;
  
  public RootTrustManagerFactorySpi() {}
  
  public TrustManager[] engineGetTrustManagers()
  {
    if (mApplicationConfig != null) {
      return new TrustManager[] { mApplicationConfig.getTrustManager() };
    }
    throw new IllegalStateException("TrustManagerFactory not initialized");
  }
  
  public void engineInit(KeyStore paramKeyStore)
    throws KeyStoreException
  {
    if (paramKeyStore != null) {
      mApplicationConfig = new ApplicationConfig(new KeyStoreConfigSource(paramKeyStore));
    } else {
      mApplicationConfig = ApplicationConfig.getDefaultInstance();
    }
  }
  
  public void engineInit(ManagerFactoryParameters paramManagerFactoryParameters)
    throws InvalidAlgorithmParameterException
  {
    if ((paramManagerFactoryParameters instanceof ApplicationConfigParameters))
    {
      mApplicationConfig = config;
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Unsupported spec: ");
    localStringBuilder.append(paramManagerFactoryParameters);
    localStringBuilder.append(". Only ");
    localStringBuilder.append(ApplicationConfigParameters.class.getName());
    localStringBuilder.append(" supported");
    throw new InvalidAlgorithmParameterException(localStringBuilder.toString());
  }
  
  @VisibleForTesting
  public static final class ApplicationConfigParameters
    implements ManagerFactoryParameters
  {
    public final ApplicationConfig config;
    
    public ApplicationConfigParameters(ApplicationConfig paramApplicationConfig)
    {
      config = paramApplicationConfig;
    }
  }
}
