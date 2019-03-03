package android.security.net.config;

import android.os.Environment;
import android.os.UserHandle;
import java.io.File;

public final class SystemCertificateSource
  extends DirectoryCertificateSource
{
  private final File mUserRemovedCaDir = new File(Environment.getUserConfigDirectory(UserHandle.myUserId()), "cacerts-removed");
  
  private SystemCertificateSource()
  {
    super(new File(localStringBuilder.toString()));
  }
  
  public static SystemCertificateSource getInstance()
  {
    return NoPreloadHolder.INSTANCE;
  }
  
  protected boolean isCertMarkedAsRemoved(String paramString)
  {
    return new File(mUserRemovedCaDir, paramString).exists();
  }
  
  private static class NoPreloadHolder
  {
    private static final SystemCertificateSource INSTANCE = new SystemCertificateSource(null);
    
    private NoPreloadHolder() {}
  }
}
