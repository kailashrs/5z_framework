package android.net;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import android.util.proto.ProtoOutputStream;
import com.android.okhttp.internalandroidapi.HttpURLConnectionFactory;
import java.io.FileDescriptor;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;
import javax.net.SocketFactory;
import libcore.io.IoUtils;

public class Network
  implements Parcelable
{
  public static final Parcelable.Creator<Network> CREATOR = new Parcelable.Creator()
  {
    public Network createFromParcel(Parcel paramAnonymousParcel)
    {
      return new Network(paramAnonymousParcel.readInt());
    }
    
    public Network[] newArray(int paramAnonymousInt)
    {
      return new Network[paramAnonymousInt];
    }
  };
  private static final long HANDLE_MAGIC = 3405697037L;
  private static final int HANDLE_MAGIC_SIZE = 32;
  private static final boolean httpKeepAlive = Boolean.parseBoolean(System.getProperty("http.keepAlive", "true"));
  private static final long httpKeepAliveDurationMs;
  private static final int httpMaxConnections;
  private final Object mLock = new Object();
  private volatile NetworkBoundSocketFactory mNetworkBoundSocketFactory = null;
  private boolean mPrivateDnsBypass = false;
  private volatile HttpURLConnectionFactory mUrlConnectionFactory;
  public final int netId;
  
  static
  {
    int i;
    if (httpKeepAlive) {
      i = Integer.parseInt(System.getProperty("http.maxConnections", "5"));
    } else {
      i = 0;
    }
    httpMaxConnections = i;
    httpKeepAliveDurationMs = Long.parseLong(System.getProperty("http.keepAliveDuration", "300000"));
  }
  
  public Network(int paramInt)
  {
    netId = paramInt;
  }
  
  public Network(Network paramNetwork)
  {
    netId = netId;
  }
  
  public static Network fromNetworkHandle(long paramLong)
  {
    if (paramLong != 0L)
    {
      if (((0xFFFFFFFF & paramLong) == 3405697037L) && (paramLong >= 0L)) {
        return new Network((int)(paramLong >> 32));
      }
      throw new IllegalArgumentException("Value passed to fromNetworkHandle() is not a network handle.");
    }
    throw new IllegalArgumentException("Network.fromNetworkHandle refusing to instantiate NETID_UNSET Network.");
  }
  
  private void maybeInitUrlConnectionFactory()
  {
    synchronized (mLock)
    {
      if (mUrlConnectionFactory == null)
      {
        _..Lambda.Network.KD6DxaMRJIcajhj36TU1K7lJnHQ localKD6DxaMRJIcajhj36TU1K7lJnHQ = new android/net/_$$Lambda$Network$KD6DxaMRJIcajhj36TU1K7lJnHQ;
        localKD6DxaMRJIcajhj36TU1K7lJnHQ.<init>(this);
        HttpURLConnectionFactory localHttpURLConnectionFactory = new com/android/okhttp/internalandroidapi/HttpURLConnectionFactory;
        localHttpURLConnectionFactory.<init>();
        localHttpURLConnectionFactory.setDns(localKD6DxaMRJIcajhj36TU1K7lJnHQ);
        localHttpURLConnectionFactory.setNewConnectionPool(httpMaxConnections, httpKeepAliveDurationMs, TimeUnit.MILLISECONDS);
        mUrlConnectionFactory = localHttpURLConnectionFactory;
      }
      return;
    }
  }
  
  public void bindSocket(FileDescriptor paramFileDescriptor)
    throws IOException
  {
    try
    {
      if (!((InetSocketAddress)Os.getpeername(paramFileDescriptor)).getAddress().isAnyLocalAddress())
      {
        SocketException localSocketException = new java/net/SocketException;
        localSocketException.<init>("Socket is connected");
        throw localSocketException;
      }
    }
    catch (ClassCastException paramFileDescriptor)
    {
      throw new SocketException("Only AF_INET/AF_INET6 sockets supported");
    }
    catch (ErrnoException localErrnoException)
    {
      if (errno == OsConstants.ENOTCONN)
      {
        int i = NetworkUtils.bindSocketToNetwork(paramFileDescriptor.getInt$(), netId);
        if (i == 0) {
          return;
        }
        paramFileDescriptor = new StringBuilder();
        paramFileDescriptor.append("Binding socket to network ");
        paramFileDescriptor.append(netId);
        throw new ErrnoException(paramFileDescriptor.toString(), -i).rethrowAsSocketException();
      }
      throw localErrnoException.rethrowAsSocketException();
    }
  }
  
  public void bindSocket(DatagramSocket paramDatagramSocket)
    throws IOException
  {
    paramDatagramSocket.getReuseAddress();
    bindSocket(paramDatagramSocket.getFileDescriptor$());
  }
  
  public void bindSocket(Socket paramSocket)
    throws IOException
  {
    paramSocket.getReuseAddress();
    bindSocket(paramSocket.getFileDescriptor$());
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool1 = paramObject instanceof Network;
    boolean bool2 = false;
    if (!bool1) {
      return false;
    }
    paramObject = (Network)paramObject;
    if (netId == netId) {
      bool2 = true;
    }
    return bool2;
  }
  
  public InetAddress[] getAllByName(String paramString)
    throws UnknownHostException
  {
    return InetAddress.getAllByNameOnNet(paramString, getNetIdForResolv());
  }
  
  public InetAddress getByName(String paramString)
    throws UnknownHostException
  {
    return InetAddress.getByNameOnNet(paramString, getNetIdForResolv());
  }
  
  public int getNetIdForResolv()
  {
    int i;
    if (mPrivateDnsBypass) {
      i = (int)(0x80000000 | netId);
    } else {
      i = netId;
    }
    return i;
  }
  
  public long getNetworkHandle()
  {
    if (netId == 0) {
      return 0L;
    }
    return netId << 32 | 0xCAFED00D;
  }
  
  public SocketFactory getSocketFactory()
  {
    if (mNetworkBoundSocketFactory == null) {
      synchronized (mLock)
      {
        if (mNetworkBoundSocketFactory == null)
        {
          NetworkBoundSocketFactory localNetworkBoundSocketFactory = new android/net/Network$NetworkBoundSocketFactory;
          localNetworkBoundSocketFactory.<init>(this, netId);
          mNetworkBoundSocketFactory = localNetworkBoundSocketFactory;
        }
      }
    }
    return mNetworkBoundSocketFactory;
  }
  
  public int hashCode()
  {
    return netId * 11;
  }
  
  public URLConnection openConnection(URL paramURL)
    throws IOException
  {
    Object localObject = ConnectivityManager.getInstanceOrNull();
    if (localObject != null)
    {
      localObject = ((ConnectivityManager)localObject).getProxyForNetwork(this);
      if (localObject != null) {
        localObject = ((ProxyInfo)localObject).makeProxy();
      } else {
        localObject = Proxy.NO_PROXY;
      }
      return openConnection(paramURL, (Proxy)localObject);
    }
    throw new IOException("No ConnectivityManager yet constructed, please construct one");
  }
  
  public URLConnection openConnection(URL paramURL, Proxy paramProxy)
    throws IOException
  {
    if (paramProxy != null)
    {
      maybeInitUrlConnectionFactory();
      SocketFactory localSocketFactory = getSocketFactory();
      return mUrlConnectionFactory.openConnection(paramURL, localSocketFactory, paramProxy);
    }
    throw new IllegalArgumentException("proxy is null");
  }
  
  public void setPrivateDnsBypass(boolean paramBoolean)
  {
    mPrivateDnsBypass = paramBoolean;
  }
  
  public String toString()
  {
    return Integer.toString(netId);
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(netId);
  }
  
  public void writeToProto(ProtoOutputStream paramProtoOutputStream, long paramLong)
  {
    paramLong = paramProtoOutputStream.start(paramLong);
    paramProtoOutputStream.write(1120986464257L, netId);
    paramProtoOutputStream.end(paramLong);
  }
  
  private class NetworkBoundSocketFactory
    extends SocketFactory
  {
    private final int mNetId;
    
    public NetworkBoundSocketFactory(int paramInt)
    {
      mNetId = paramInt;
    }
    
    private Socket connectToHost(String paramString, int paramInt, SocketAddress paramSocketAddress)
      throws IOException
    {
      InetAddress[] arrayOfInetAddress = getAllByName(paramString);
      int i = 0;
      while (i < arrayOfInetAddress.length) {
        try
        {
          Socket localSocket = createSocket();
          if (paramSocketAddress != null) {
            try
            {
              localSocket.bind(paramSocketAddress);
            }
            finally
            {
              break label80;
            }
          }
          InetSocketAddress localInetSocketAddress = new java/net/InetSocketAddress;
          localInetSocketAddress.<init>(arrayOfInetAddress[i], paramInt);
          localSocket.connect(localInetSocketAddress);
          if (0 != 0) {
            IoUtils.closeQuietly(localSocket);
          }
          return localSocket;
          label80:
          if (1 != 0) {
            IoUtils.closeQuietly(localSocket);
          }
          throw localInetSocketAddress;
        }
        catch (IOException localIOException)
        {
          if (i != arrayOfInetAddress.length - 1) {
            i++;
          } else {
            throw localIOException;
          }
        }
      }
      throw new UnknownHostException(paramString);
    }
    
    public Socket createSocket()
      throws IOException
    {
      Socket localSocket = new Socket();
      try
      {
        bindSocket(localSocket);
        if (0 != 0) {
          IoUtils.closeQuietly(localSocket);
        }
        return localSocket;
      }
      finally
      {
        if (1 != 0) {
          IoUtils.closeQuietly(localSocket);
        }
      }
    }
    
    public Socket createSocket(String paramString, int paramInt)
      throws IOException
    {
      return connectToHost(paramString, paramInt, null);
    }
    
    public Socket createSocket(String paramString, int paramInt1, InetAddress paramInetAddress, int paramInt2)
      throws IOException
    {
      return connectToHost(paramString, paramInt1, new InetSocketAddress(paramInetAddress, paramInt2));
    }
    
    public Socket createSocket(InetAddress paramInetAddress, int paramInt)
      throws IOException
    {
      Socket localSocket = createSocket();
      try
      {
        InetSocketAddress localInetSocketAddress = new java/net/InetSocketAddress;
        localInetSocketAddress.<init>(paramInetAddress, paramInt);
        localSocket.connect(localInetSocketAddress);
        if (0 != 0) {
          IoUtils.closeQuietly(localSocket);
        }
        return localSocket;
      }
      finally
      {
        if (1 != 0) {
          IoUtils.closeQuietly(localSocket);
        }
      }
    }
    
    public Socket createSocket(InetAddress paramInetAddress1, int paramInt1, InetAddress paramInetAddress2, int paramInt2)
      throws IOException
    {
      Socket localSocket = createSocket();
      try
      {
        InetSocketAddress localInetSocketAddress = new java/net/InetSocketAddress;
        localInetSocketAddress.<init>(paramInetAddress2, paramInt2);
        localSocket.bind(localInetSocketAddress);
        paramInetAddress2 = new java/net/InetSocketAddress;
        paramInetAddress2.<init>(paramInetAddress1, paramInt1);
        localSocket.connect(paramInetAddress2);
        if (0 != 0) {
          IoUtils.closeQuietly(localSocket);
        }
        return localSocket;
      }
      finally
      {
        if (1 != 0) {
          IoUtils.closeQuietly(localSocket);
        }
      }
    }
  }
}
