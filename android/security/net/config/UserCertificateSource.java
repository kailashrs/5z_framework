package android.security.net.config;

import android.os.Environment;
import android.os.UserHandle;
import java.io.File;

public final class UserCertificateSource
  extends DirectoryCertificateSource
{
  private UserCertificateSource()
  {
    super(new File(Environment.getUserConfigDirectory(UserHandle.myUserId()), "cacerts-added"));
  }
  
  public static UserCertificateSource getInstance()
  {
    return NoPreloadHolder.INSTANCE;
  }
  
  protected boolean isCertMarkedAsRemoved(String paramString)
  {
    return false;
  }
  
  private static class NoPreloadHolder
  {
    private static final UserCertificateSource INSTANCE = new UserCertificateSource(null);
    
    private NoPreloadHolder() {}
  }
}
