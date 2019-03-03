package android.net;

import java.io.Closeable;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class LocalSocket
  implements Closeable
{
  public static final int SOCKET_DGRAM = 1;
  public static final int SOCKET_SEQPACKET = 3;
  public static final int SOCKET_STREAM = 2;
  static final int SOCKET_UNKNOWN = 0;
  private final LocalSocketImpl impl;
  private volatile boolean implCreated;
  private boolean isBound;
  private boolean isConnected;
  private LocalSocketAddress localAddress;
  private final int sockType;
  
  public LocalSocket()
  {
    this(2);
  }
  
  public LocalSocket(int paramInt)
  {
    this(new LocalSocketImpl(), paramInt);
  }
  
  private LocalSocket(LocalSocketImpl paramLocalSocketImpl, int paramInt)
  {
    impl = paramLocalSocketImpl;
    sockType = paramInt;
    isConnected = false;
    isBound = false;
  }
  
  private static LocalSocket createConnectedLocalSocket(LocalSocketImpl paramLocalSocketImpl, int paramInt)
  {
    paramLocalSocketImpl = new LocalSocket(paramLocalSocketImpl, paramInt);
    isConnected = true;
    isBound = true;
    implCreated = true;
    return paramLocalSocketImpl;
  }
  
  public static LocalSocket createConnectedLocalSocket(FileDescriptor paramFileDescriptor)
  {
    return createConnectedLocalSocket(new LocalSocketImpl(paramFileDescriptor), 0);
  }
  
  static LocalSocket createLocalSocketForAccept(LocalSocketImpl paramLocalSocketImpl)
  {
    return createConnectedLocalSocket(paramLocalSocketImpl, 0);
  }
  
  private void implCreateIfNeeded()
    throws IOException
  {
    if (!implCreated) {
      try
      {
        boolean bool = implCreated;
        if (!bool) {}
        try
        {
          impl.create(sockType);
          implCreated = true;
        }
        finally
        {
          implCreated = true;
        }
      }
      finally {}
    }
  }
  
  public void bind(LocalSocketAddress paramLocalSocketAddress)
    throws IOException
  {
    implCreateIfNeeded();
    try
    {
      if (!isBound)
      {
        localAddress = paramLocalSocketAddress;
        impl.bind(localAddress);
        isBound = true;
        return;
      }
      paramLocalSocketAddress = new java/io/IOException;
      paramLocalSocketAddress.<init>("already bound");
      throw paramLocalSocketAddress;
    }
    finally {}
  }
  
  public void close()
    throws IOException
  {
    implCreateIfNeeded();
    impl.close();
  }
  
  public void connect(LocalSocketAddress paramLocalSocketAddress)
    throws IOException
  {
    try
    {
      if (!isConnected)
      {
        implCreateIfNeeded();
        impl.connect(paramLocalSocketAddress, 0);
        isConnected = true;
        isBound = true;
        return;
      }
      paramLocalSocketAddress = new java/io/IOException;
      paramLocalSocketAddress.<init>("already connected");
      throw paramLocalSocketAddress;
    }
    finally {}
  }
  
  public void connect(LocalSocketAddress paramLocalSocketAddress, int paramInt)
    throws IOException
  {
    throw new UnsupportedOperationException();
  }
  
  public FileDescriptor[] getAncillaryFileDescriptors()
    throws IOException
  {
    return impl.getAncillaryFileDescriptors();
  }
  
  public FileDescriptor getFileDescriptor()
  {
    return impl.getFileDescriptor();
  }
  
  public InputStream getInputStream()
    throws IOException
  {
    implCreateIfNeeded();
    return impl.getInputStream();
  }
  
  public LocalSocketAddress getLocalSocketAddress()
  {
    return localAddress;
  }
  
  public OutputStream getOutputStream()
    throws IOException
  {
    implCreateIfNeeded();
    return impl.getOutputStream();
  }
  
  public Credentials getPeerCredentials()
    throws IOException
  {
    return impl.getPeerCredentials();
  }
  
  public int getReceiveBufferSize()
    throws IOException
  {
    return ((Integer)impl.getOption(4098)).intValue();
  }
  
  public LocalSocketAddress getRemoteSocketAddress()
  {
    throw new UnsupportedOperationException();
  }
  
  public int getSendBufferSize()
    throws IOException
  {
    return ((Integer)impl.getOption(4097)).intValue();
  }
  
  public int getSoTimeout()
    throws IOException
  {
    return ((Integer)impl.getOption(4102)).intValue();
  }
  
  public boolean isBound()
  {
    try
    {
      boolean bool = isBound;
      return bool;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public boolean isClosed()
  {
    throw new UnsupportedOperationException();
  }
  
  public boolean isConnected()
  {
    try
    {
      boolean bool = isConnected;
      return bool;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public boolean isInputShutdown()
  {
    throw new UnsupportedOperationException();
  }
  
  public boolean isOutputShutdown()
  {
    throw new UnsupportedOperationException();
  }
  
  public void setFileDescriptorsForSend(FileDescriptor[] paramArrayOfFileDescriptor)
  {
    impl.setFileDescriptorsForSend(paramArrayOfFileDescriptor);
  }
  
  public void setReceiveBufferSize(int paramInt)
    throws IOException
  {
    impl.setOption(4098, Integer.valueOf(paramInt));
  }
  
  public void setSendBufferSize(int paramInt)
    throws IOException
  {
    impl.setOption(4097, Integer.valueOf(paramInt));
  }
  
  public void setSoTimeout(int paramInt)
    throws IOException
  {
    impl.setOption(4102, Integer.valueOf(paramInt));
  }
  
  public void shutdownInput()
    throws IOException
  {
    implCreateIfNeeded();
    impl.shutdownInput();
  }
  
  public void shutdownOutput()
    throws IOException
  {
    implCreateIfNeeded();
    impl.shutdownOutput();
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(super.toString());
    localStringBuilder.append(" impl:");
    localStringBuilder.append(impl);
    return localStringBuilder.toString();
  }
}
