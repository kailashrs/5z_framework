package android.net;

import android.content.Context;
import android.util.Log;
import com.android.org.conscrypt.ClientSessionContext;
import com.android.org.conscrypt.FileClientSessionCache;
import com.android.org.conscrypt.SSLClientSessionCache;
import java.io.File;
import java.io.IOException;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSessionContext;

public final class SSLSessionCache
{
  private static final String TAG = "SSLSessionCache";
  final SSLClientSessionCache mSessionCache;
  
  public SSLSessionCache(Context paramContext)
  {
    File localFile = paramContext.getDir("sslcache", 0);
    paramContext = null;
    try
    {
      SSLClientSessionCache localSSLClientSessionCache = FileClientSessionCache.usingDirectory(localFile);
      paramContext = localSSLClientSessionCache;
    }
    catch (IOException localIOException)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Unable to create SSL session cache in ");
      localStringBuilder.append(localFile);
      Log.w("SSLSessionCache", localStringBuilder.toString(), localIOException);
    }
    mSessionCache = paramContext;
  }
  
  public SSLSessionCache(File paramFile)
    throws IOException
  {
    mSessionCache = FileClientSessionCache.usingDirectory(paramFile);
  }
  
  public SSLSessionCache(Object paramObject)
  {
    mSessionCache = ((SSLClientSessionCache)paramObject);
  }
  
  public static void install(SSLSessionCache paramSSLSessionCache, SSLContext paramSSLContext)
  {
    SSLSessionContext localSSLSessionContext = paramSSLContext.getClientSessionContext();
    if ((localSSLSessionContext instanceof ClientSessionContext))
    {
      paramSSLContext = (ClientSessionContext)localSSLSessionContext;
      if (paramSSLSessionCache == null) {
        paramSSLSessionCache = null;
      } else {
        paramSSLSessionCache = mSessionCache;
      }
      paramSSLContext.setPersistentCache(paramSSLSessionCache);
      return;
    }
    paramSSLSessionCache = new StringBuilder();
    paramSSLSessionCache.append("Incompatible SSLContext: ");
    paramSSLSessionCache.append(paramSSLContext);
    throw new IllegalArgumentException(paramSSLSessionCache.toString());
  }
}
