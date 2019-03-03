package android.net;

import java.io.Closeable;
import java.io.FileDescriptor;
import java.io.IOException;

public class LocalServerSocket
  implements Closeable
{
  private static final int LISTEN_BACKLOG = 50;
  private final LocalSocketImpl impl;
  private final LocalSocketAddress localAddress;
  
  public LocalServerSocket(FileDescriptor paramFileDescriptor)
    throws IOException
  {
    impl = new LocalSocketImpl(paramFileDescriptor);
    impl.listen(50);
    localAddress = impl.getSockAddress();
  }
  
  public LocalServerSocket(String paramString)
    throws IOException
  {
    impl = new LocalSocketImpl();
    impl.create(2);
    localAddress = new LocalSocketAddress(paramString);
    impl.bind(localAddress);
    impl.listen(50);
  }
  
  public LocalSocket accept()
    throws IOException
  {
    LocalSocketImpl localLocalSocketImpl = new LocalSocketImpl();
    impl.accept(localLocalSocketImpl);
    return LocalSocket.createLocalSocketForAccept(localLocalSocketImpl);
  }
  
  public void close()
    throws IOException
  {
    impl.close();
  }
  
  public FileDescriptor getFileDescriptor()
  {
    return impl.getFileDescriptor();
  }
  
  public LocalSocketAddress getLocalSocketAddress()
  {
    return localAddress;
  }
}
