package android.net.http;

import android.annotation.SystemApi;
import android.security.net.config.UserCertificateSource;
import com.android.org.conscrypt.TrustManagerImpl;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import javax.net.ssl.X509TrustManager;

public class X509TrustManagerExtensions
{
  private final Method mCheckServerTrusted;
  private final TrustManagerImpl mDelegate;
  private final Method mIsSameTrustConfiguration;
  private final X509TrustManager mTrustManager;
  
  public X509TrustManagerExtensions(X509TrustManager paramX509TrustManager)
    throws IllegalArgumentException
  {
    if ((paramX509TrustManager instanceof TrustManagerImpl))
    {
      mDelegate = ((TrustManagerImpl)paramX509TrustManager);
      mTrustManager = null;
      mCheckServerTrusted = null;
      mIsSameTrustConfiguration = null;
      return;
    }
    mDelegate = null;
    mTrustManager = paramX509TrustManager;
    try
    {
      mCheckServerTrusted = paramX509TrustManager.getClass().getMethod("checkServerTrusted", new Class[] { [Ljava.security.cert.X509Certificate.class, String.class, String.class });
      Object localObject = null;
      try
      {
        paramX509TrustManager = paramX509TrustManager.getClass().getMethod("isSameTrustConfiguration", new Class[] { String.class, String.class });
      }
      catch (ReflectiveOperationException paramX509TrustManager)
      {
        paramX509TrustManager = localObject;
      }
      mIsSameTrustConfiguration = paramX509TrustManager;
      return;
    }
    catch (NoSuchMethodException paramX509TrustManager)
    {
      throw new IllegalArgumentException("Required method checkServerTrusted(X509Certificate[], String, String, String) missing");
    }
  }
  
  public List<X509Certificate> checkServerTrusted(X509Certificate[] paramArrayOfX509Certificate, String paramString1, String paramString2)
    throws CertificateException
  {
    if (mDelegate != null) {
      return mDelegate.checkServerTrusted(paramArrayOfX509Certificate, paramString1, paramString2);
    }
    try
    {
      paramArrayOfX509Certificate = (List)mCheckServerTrusted.invoke(mTrustManager, new Object[] { paramArrayOfX509Certificate, paramString1, paramString2 });
      return paramArrayOfX509Certificate;
    }
    catch (InvocationTargetException paramArrayOfX509Certificate)
    {
      if (!(paramArrayOfX509Certificate.getCause() instanceof CertificateException))
      {
        if ((paramArrayOfX509Certificate.getCause() instanceof RuntimeException)) {
          throw ((RuntimeException)paramArrayOfX509Certificate.getCause());
        }
        throw new CertificateException("checkServerTrusted failed", paramArrayOfX509Certificate.getCause());
      }
      throw ((CertificateException)paramArrayOfX509Certificate.getCause());
    }
    catch (IllegalAccessException paramArrayOfX509Certificate)
    {
      throw new CertificateException("Failed to call checkServerTrusted", paramArrayOfX509Certificate);
    }
  }
  
  @SystemApi
  public boolean isSameTrustConfiguration(String paramString1, String paramString2)
  {
    if (mIsSameTrustConfiguration == null) {
      return true;
    }
    try
    {
      boolean bool = ((Boolean)mIsSameTrustConfiguration.invoke(mTrustManager, new Object[] { paramString1, paramString2 })).booleanValue();
      return bool;
    }
    catch (InvocationTargetException paramString1)
    {
      if ((paramString1.getCause() instanceof RuntimeException)) {
        throw ((RuntimeException)paramString1.getCause());
      }
      throw new RuntimeException("isSameTrustConfiguration failed", paramString1.getCause());
    }
    catch (IllegalAccessException paramString1)
    {
      throw new RuntimeException("Failed to call isSameTrustConfiguration", paramString1);
    }
  }
  
  public boolean isUserAddedCertificate(X509Certificate paramX509Certificate)
  {
    boolean bool;
    if (UserCertificateSource.getInstance().findBySubjectAndPublicKey(paramX509Certificate) != null) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
}
