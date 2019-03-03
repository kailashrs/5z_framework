package android.net;

import android.os.SystemProperties;
import android.util.Log;
import com.android.internal.os.RoSystemProperties;
import com.android.org.conscrypt.ClientSessionContext;
import com.android.org.conscrypt.Conscrypt;
import com.android.org.conscrypt.OpenSSLContextImpl;
import com.android.org.conscrypt.OpenSSLSocketImpl;
import com.android.org.conscrypt.SSLClientSessionCache;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.security.KeyManagementException;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import javax.net.SocketFactory;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class SSLCertificateSocketFactory
  extends javax.net.ssl.SSLSocketFactory
{
  private static final TrustManager[] INSECURE_TRUST_MANAGER = { new X509TrustManager()
  {
    public void checkClientTrusted(X509Certificate[] paramAnonymousArrayOfX509Certificate, String paramAnonymousString) {}
    
    public void checkServerTrusted(X509Certificate[] paramAnonymousArrayOfX509Certificate, String paramAnonymousString) {}
    
    public X509Certificate[] getAcceptedIssuers()
    {
      return null;
    }
  } };
  private static final String TAG = "SSLCertificateSocketFactory";
  private byte[] mAlpnProtocols;
  private PrivateKey mChannelIdPrivateKey;
  private final int mHandshakeTimeoutMillis;
  private javax.net.ssl.SSLSocketFactory mInsecureFactory;
  private KeyManager[] mKeyManagers;
  private byte[] mNpnProtocols;
  private final boolean mSecure;
  private javax.net.ssl.SSLSocketFactory mSecureFactory;
  private final SSLClientSessionCache mSessionCache;
  private TrustManager[] mTrustManagers;
  
  @Deprecated
  public SSLCertificateSocketFactory(int paramInt)
  {
    this(paramInt, null, true);
  }
  
  private SSLCertificateSocketFactory(int paramInt, SSLSessionCache paramSSLSessionCache, boolean paramBoolean)
  {
    Object localObject = null;
    mInsecureFactory = null;
    mSecureFactory = null;
    mTrustManagers = null;
    mKeyManagers = null;
    mNpnProtocols = null;
    mAlpnProtocols = null;
    mChannelIdPrivateKey = null;
    mHandshakeTimeoutMillis = paramInt;
    if (paramSSLSessionCache == null) {
      paramSSLSessionCache = localObject;
    } else {
      paramSSLSessionCache = mSessionCache;
    }
    mSessionCache = paramSSLSessionCache;
    mSecure = paramBoolean;
  }
  
  private static OpenSSLSocketImpl castToOpenSSLSocket(Socket paramSocket)
  {
    if ((paramSocket instanceof OpenSSLSocketImpl)) {
      return (OpenSSLSocketImpl)paramSocket;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Socket not created by this factory: ");
    localStringBuilder.append(paramSocket);
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  public static SocketFactory getDefault(int paramInt)
  {
    return new SSLCertificateSocketFactory(paramInt, null, true);
  }
  
  public static javax.net.ssl.SSLSocketFactory getDefault(int paramInt, SSLSessionCache paramSSLSessionCache)
  {
    return new SSLCertificateSocketFactory(paramInt, paramSSLSessionCache, true);
  }
  
  private javax.net.ssl.SSLSocketFactory getDelegate()
  {
    try
    {
      if ((mSecure) && (!isSslCheckRelaxed()))
      {
        if (mSecureFactory == null) {
          mSecureFactory = makeSocketFactory(mKeyManagers, mTrustManagers);
        }
        localSSLSocketFactory = mSecureFactory;
        return localSSLSocketFactory;
      }
      if (mInsecureFactory == null)
      {
        if (mSecure) {
          Log.w("SSLCertificateSocketFactory", "*** BYPASSING SSL SECURITY CHECKS (socket.relaxsslcheck=yes) ***");
        } else {
          Log.w("SSLCertificateSocketFactory", "Bypassing SSL security checks at caller's request");
        }
        mInsecureFactory = makeSocketFactory(mKeyManagers, INSECURE_TRUST_MANAGER);
      }
      javax.net.ssl.SSLSocketFactory localSSLSocketFactory = mInsecureFactory;
      return localSSLSocketFactory;
    }
    finally {}
  }
  
  @Deprecated
  public static org.apache.http.conn.ssl.SSLSocketFactory getHttpSocketFactory(int paramInt, SSLSessionCache paramSSLSessionCache)
  {
    return new org.apache.http.conn.ssl.SSLSocketFactory(new SSLCertificateSocketFactory(paramInt, paramSSLSessionCache, true));
  }
  
  public static javax.net.ssl.SSLSocketFactory getInsecure(int paramInt, SSLSessionCache paramSSLSessionCache)
  {
    return new SSLCertificateSocketFactory(paramInt, paramSSLSessionCache, false);
  }
  
  private static boolean isSslCheckRelaxed()
  {
    boolean bool1 = RoSystemProperties.DEBUGGABLE;
    boolean bool2 = false;
    boolean bool3 = bool2;
    if (bool1)
    {
      bool3 = bool2;
      if (SystemProperties.getBoolean("socket.relaxsslcheck", false)) {
        bool3 = true;
      }
    }
    return bool3;
  }
  
  private javax.net.ssl.SSLSocketFactory makeSocketFactory(KeyManager[] paramArrayOfKeyManager, TrustManager[] paramArrayOfTrustManager)
  {
    try
    {
      OpenSSLContextImpl localOpenSSLContextImpl = (OpenSSLContextImpl)Conscrypt.newPreferredSSLContextSpi();
      localOpenSSLContextImpl.engineInit(paramArrayOfKeyManager, paramArrayOfTrustManager, null);
      localOpenSSLContextImpl.engineGetClientSessionContext().setPersistentCache(mSessionCache);
      paramArrayOfKeyManager = localOpenSSLContextImpl.engineGetSocketFactory();
      return paramArrayOfKeyManager;
    }
    catch (KeyManagementException paramArrayOfKeyManager)
    {
      Log.wtf("SSLCertificateSocketFactory", paramArrayOfKeyManager);
    }
    return (javax.net.ssl.SSLSocketFactory)javax.net.ssl.SSLSocketFactory.getDefault();
  }
  
  static byte[] toLengthPrefixedList(byte[]... paramVarArgs)
  {
    if (paramVarArgs.length != 0)
    {
      int i = paramVarArgs.length;
      int j = 0;
      int k = 0;
      while (k < i)
      {
        arrayOfByte1 = paramVarArgs[k];
        if ((arrayOfByte1.length != 0) && (arrayOfByte1.length <= 255))
        {
          j += 1 + arrayOfByte1.length;
          k++;
        }
        else
        {
          paramVarArgs = new StringBuilder();
          paramVarArgs.append("s.length == 0 || s.length > 255: ");
          paramVarArgs.append(arrayOfByte1.length);
          throw new IllegalArgumentException(paramVarArgs.toString());
        }
      }
      byte[] arrayOfByte1 = new byte[j];
      int m = paramVarArgs.length;
      j = 0;
      for (k = 0; k < m; k++)
      {
        byte[] arrayOfByte2 = paramVarArgs[k];
        arrayOfByte1[j] = ((byte)(byte)arrayOfByte2.length);
        int n = arrayOfByte2.length;
        j++;
        i = 0;
        while (i < n)
        {
          arrayOfByte1[j] = ((byte)arrayOfByte2[i]);
          i++;
          j++;
        }
      }
      return arrayOfByte1;
    }
    throw new IllegalArgumentException("items.length == 0");
  }
  
  public static void verifyHostname(Socket paramSocket, String paramString)
    throws IOException
  {
    if ((paramSocket instanceof SSLSocket))
    {
      if (!isSslCheckRelaxed())
      {
        paramSocket = (SSLSocket)paramSocket;
        paramSocket.startHandshake();
        paramSocket = paramSocket.getSession();
        if (paramSocket != null)
        {
          if (!HttpsURLConnection.getDefaultHostnameVerifier().verify(paramString, paramSocket))
          {
            paramSocket = new StringBuilder();
            paramSocket.append("Cannot verify hostname: ");
            paramSocket.append(paramString);
            throw new SSLPeerUnverifiedException(paramSocket.toString());
          }
        }
        else {
          throw new SSLException("Cannot verify SSL socket without session");
        }
      }
      return;
    }
    throw new IllegalArgumentException("Attempt to verify non-SSL socket");
  }
  
  public Socket createSocket()
    throws IOException
  {
    OpenSSLSocketImpl localOpenSSLSocketImpl = (OpenSSLSocketImpl)getDelegate().createSocket();
    localOpenSSLSocketImpl.setNpnProtocols(mNpnProtocols);
    localOpenSSLSocketImpl.setAlpnProtocols(mAlpnProtocols);
    localOpenSSLSocketImpl.setHandshakeTimeout(mHandshakeTimeoutMillis);
    localOpenSSLSocketImpl.setChannelIdPrivateKey(mChannelIdPrivateKey);
    return localOpenSSLSocketImpl;
  }
  
  public Socket createSocket(String paramString, int paramInt)
    throws IOException
  {
    OpenSSLSocketImpl localOpenSSLSocketImpl = (OpenSSLSocketImpl)getDelegate().createSocket(paramString, paramInt);
    localOpenSSLSocketImpl.setNpnProtocols(mNpnProtocols);
    localOpenSSLSocketImpl.setAlpnProtocols(mAlpnProtocols);
    localOpenSSLSocketImpl.setHandshakeTimeout(mHandshakeTimeoutMillis);
    localOpenSSLSocketImpl.setChannelIdPrivateKey(mChannelIdPrivateKey);
    if (mSecure) {
      verifyHostname(localOpenSSLSocketImpl, paramString);
    }
    return localOpenSSLSocketImpl;
  }
  
  public Socket createSocket(String paramString, int paramInt1, InetAddress paramInetAddress, int paramInt2)
    throws IOException
  {
    paramInetAddress = (OpenSSLSocketImpl)getDelegate().createSocket(paramString, paramInt1, paramInetAddress, paramInt2);
    paramInetAddress.setNpnProtocols(mNpnProtocols);
    paramInetAddress.setAlpnProtocols(mAlpnProtocols);
    paramInetAddress.setHandshakeTimeout(mHandshakeTimeoutMillis);
    paramInetAddress.setChannelIdPrivateKey(mChannelIdPrivateKey);
    if (mSecure) {
      verifyHostname(paramInetAddress, paramString);
    }
    return paramInetAddress;
  }
  
  public Socket createSocket(InetAddress paramInetAddress, int paramInt)
    throws IOException
  {
    paramInetAddress = (OpenSSLSocketImpl)getDelegate().createSocket(paramInetAddress, paramInt);
    paramInetAddress.setNpnProtocols(mNpnProtocols);
    paramInetAddress.setAlpnProtocols(mAlpnProtocols);
    paramInetAddress.setHandshakeTimeout(mHandshakeTimeoutMillis);
    paramInetAddress.setChannelIdPrivateKey(mChannelIdPrivateKey);
    return paramInetAddress;
  }
  
  public Socket createSocket(InetAddress paramInetAddress1, int paramInt1, InetAddress paramInetAddress2, int paramInt2)
    throws IOException
  {
    paramInetAddress1 = (OpenSSLSocketImpl)getDelegate().createSocket(paramInetAddress1, paramInt1, paramInetAddress2, paramInt2);
    paramInetAddress1.setNpnProtocols(mNpnProtocols);
    paramInetAddress1.setAlpnProtocols(mAlpnProtocols);
    paramInetAddress1.setHandshakeTimeout(mHandshakeTimeoutMillis);
    paramInetAddress1.setChannelIdPrivateKey(mChannelIdPrivateKey);
    return paramInetAddress1;
  }
  
  public Socket createSocket(Socket paramSocket, String paramString, int paramInt, boolean paramBoolean)
    throws IOException
  {
    paramSocket = (OpenSSLSocketImpl)getDelegate().createSocket(paramSocket, paramString, paramInt, paramBoolean);
    paramSocket.setNpnProtocols(mNpnProtocols);
    paramSocket.setAlpnProtocols(mAlpnProtocols);
    paramSocket.setHandshakeTimeout(mHandshakeTimeoutMillis);
    paramSocket.setChannelIdPrivateKey(mChannelIdPrivateKey);
    if (mSecure) {
      verifyHostname(paramSocket, paramString);
    }
    return paramSocket;
  }
  
  public byte[] getAlpnSelectedProtocol(Socket paramSocket)
  {
    return castToOpenSSLSocket(paramSocket).getAlpnSelectedProtocol();
  }
  
  public String[] getDefaultCipherSuites()
  {
    return getDelegate().getDefaultCipherSuites();
  }
  
  public byte[] getNpnSelectedProtocol(Socket paramSocket)
  {
    return castToOpenSSLSocket(paramSocket).getNpnSelectedProtocol();
  }
  
  public String[] getSupportedCipherSuites()
  {
    return getDelegate().getSupportedCipherSuites();
  }
  
  public void setAlpnProtocols(byte[][] paramArrayOfByte)
  {
    mAlpnProtocols = toLengthPrefixedList(paramArrayOfByte);
  }
  
  public void setChannelIdPrivateKey(PrivateKey paramPrivateKey)
  {
    mChannelIdPrivateKey = paramPrivateKey;
  }
  
  public void setHostname(Socket paramSocket, String paramString)
  {
    castToOpenSSLSocket(paramSocket).setHostname(paramString);
  }
  
  public void setKeyManagers(KeyManager[] paramArrayOfKeyManager)
  {
    mKeyManagers = paramArrayOfKeyManager;
    mSecureFactory = null;
    mInsecureFactory = null;
  }
  
  public void setNpnProtocols(byte[][] paramArrayOfByte)
  {
    mNpnProtocols = toLengthPrefixedList(paramArrayOfByte);
  }
  
  public void setSoWriteTimeout(Socket paramSocket, int paramInt)
    throws SocketException
  {
    castToOpenSSLSocket(paramSocket).setSoWriteTimeout(paramInt);
  }
  
  public void setTrustManagers(TrustManager[] paramArrayOfTrustManager)
  {
    mTrustManagers = paramArrayOfTrustManager;
    mSecureFactory = null;
  }
  
  public void setUseSessionTickets(Socket paramSocket, boolean paramBoolean)
  {
    castToOpenSSLSocket(paramSocket).setUseSessionTickets(paramBoolean);
  }
}
