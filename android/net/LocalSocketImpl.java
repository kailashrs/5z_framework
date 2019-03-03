package android.net;

import android.system.ErrnoException;
import android.system.Int32Ref;
import android.system.Os;
import android.system.OsConstants;
import android.system.StructLinger;
import android.system.StructTimeval;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

class LocalSocketImpl
{
  private FileDescriptor fd;
  private SocketInputStream fis;
  private SocketOutputStream fos;
  FileDescriptor[] inboundFileDescriptors;
  private boolean mFdCreatedInternally;
  FileDescriptor[] outboundFileDescriptors;
  private Object readMonitor = new Object();
  private Object writeMonitor = new Object();
  
  LocalSocketImpl() {}
  
  LocalSocketImpl(FileDescriptor paramFileDescriptor)
  {
    fd = paramFileDescriptor;
  }
  
  private native void bindLocal(FileDescriptor paramFileDescriptor, String paramString, int paramInt)
    throws IOException;
  
  private native void connectLocal(FileDescriptor paramFileDescriptor, String paramString, int paramInt)
    throws IOException;
  
  private native Credentials getPeerCredentials_native(FileDescriptor paramFileDescriptor)
    throws IOException;
  
  private static int javaSoToOsOpt(int paramInt)
  {
    if (paramInt != 4)
    {
      switch (paramInt)
      {
      default: 
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Unknown option: ");
        localStringBuilder.append(paramInt);
        throw new UnsupportedOperationException(localStringBuilder.toString());
      case 4098: 
        return OsConstants.SO_RCVBUF;
      }
      return OsConstants.SO_SNDBUF;
    }
    return OsConstants.SO_REUSEADDR;
  }
  
  private native int read_native(FileDescriptor paramFileDescriptor)
    throws IOException;
  
  private native int readba_native(byte[] paramArrayOfByte, int paramInt1, int paramInt2, FileDescriptor paramFileDescriptor)
    throws IOException;
  
  private native void write_native(int paramInt, FileDescriptor paramFileDescriptor)
    throws IOException;
  
  private native void writeba_native(byte[] paramArrayOfByte, int paramInt1, int paramInt2, FileDescriptor paramFileDescriptor)
    throws IOException;
  
  protected void accept(LocalSocketImpl paramLocalSocketImpl)
    throws IOException
  {
    if (fd != null) {
      try
      {
        fd = Os.accept(fd, null);
        mFdCreatedInternally = true;
        return;
      }
      catch (ErrnoException paramLocalSocketImpl)
      {
        throw paramLocalSocketImpl.rethrowAsIOException();
      }
    }
    throw new IOException("socket not created");
  }
  
  protected int available()
    throws IOException
  {
    return getInputStream().available();
  }
  
  public void bind(LocalSocketAddress paramLocalSocketAddress)
    throws IOException
  {
    if (fd != null)
    {
      bindLocal(fd, paramLocalSocketAddress.getName(), paramLocalSocketAddress.getNamespace().getId());
      return;
    }
    throw new IOException("socket not created");
  }
  
  public void close()
    throws IOException
  {
    try
    {
      if (fd != null)
      {
        boolean bool = mFdCreatedInternally;
        if (bool)
        {
          try
          {
            Os.close(fd);
          }
          catch (ErrnoException localErrnoException)
          {
            localErrnoException.rethrowAsIOException();
          }
          fd = null;
          return;
        }
      }
      fd = null;
      return;
    }
    finally {}
  }
  
  protected void connect(LocalSocketAddress paramLocalSocketAddress, int paramInt)
    throws IOException
  {
    if (fd != null)
    {
      connectLocal(fd, paramLocalSocketAddress.getName(), paramLocalSocketAddress.getNamespace().getId());
      return;
    }
    throw new IOException("socket not created");
  }
  
  public void create(int paramInt)
    throws IOException
  {
    if (fd == null)
    {
      switch (paramInt)
      {
      default: 
        throw new IllegalStateException("unknown sockType");
      case 3: 
        paramInt = OsConstants.SOCK_SEQPACKET;
        break;
      case 2: 
        paramInt = OsConstants.SOCK_STREAM;
        break;
      case 1: 
        paramInt = OsConstants.SOCK_DGRAM;
      }
      try
      {
        fd = Os.socket(OsConstants.AF_UNIX, paramInt, 0);
        mFdCreatedInternally = true;
      }
      catch (ErrnoException localErrnoException)
      {
        localErrnoException.rethrowAsIOException();
      }
      return;
    }
    throw new IOException("LocalSocketImpl already has an fd");
  }
  
  protected void finalize()
    throws IOException
  {
    close();
  }
  
  public FileDescriptor[] getAncillaryFileDescriptors()
    throws IOException
  {
    synchronized (readMonitor)
    {
      FileDescriptor[] arrayOfFileDescriptor = inboundFileDescriptors;
      inboundFileDescriptors = null;
      return arrayOfFileDescriptor;
    }
  }
  
  protected FileDescriptor getFileDescriptor()
  {
    return fd;
  }
  
  protected InputStream getInputStream()
    throws IOException
  {
    if (fd != null) {
      try
      {
        if (fis == null)
        {
          localSocketInputStream = new android/net/LocalSocketImpl$SocketInputStream;
          localSocketInputStream.<init>(this);
          fis = localSocketInputStream;
        }
        SocketInputStream localSocketInputStream = fis;
        return localSocketInputStream;
      }
      finally {}
    }
    throw new IOException("socket not created");
  }
  
  public Object getOption(int paramInt)
    throws IOException
  {
    if (fd != null)
    {
      Object localObject;
      if (paramInt != 1)
      {
        if (paramInt != 4)
        {
          if (paramInt != 128) {
            if (paramInt == 4102) {}
          }
          switch (paramInt)
          {
          default: 
            try
            {
              IOException localIOException = new java/io/IOException;
              StringBuilder localStringBuilder = new java/lang/StringBuilder;
              localStringBuilder.<init>();
              localStringBuilder.append("Unknown option: ");
              localStringBuilder.append(paramInt);
              localIOException.<init>(localStringBuilder.toString());
              throw localIOException;
            }
            catch (ErrnoException localErrnoException) {}
            localObject = Integer.valueOf((int)Os.getsockoptTimeval(fd, OsConstants.SOL_SOCKET, OsConstants.SO_SNDTIMEO).toMillis());
            break;
            localObject = Os.getsockoptLinger(fd, OsConstants.SOL_SOCKET, OsConstants.SO_LINGER);
            if (!((StructLinger)localObject).isOn()) {
              localObject = Integer.valueOf(-1);
            } else {
              localObject = Integer.valueOf(l_linger);
            }
            break;
          }
        }
        else
        {
          paramInt = javaSoToOsOpt(paramInt);
          localObject = Integer.valueOf(Os.getsockoptInt(fd, OsConstants.SOL_SOCKET, paramInt));
        }
      }
      else
      {
        paramInt = Os.getsockoptInt(fd, OsConstants.IPPROTO_TCP, OsConstants.TCP_NODELAY);
        localObject = Integer.valueOf(paramInt);
      }
      return localObject;
      throw ((ErrnoException)localObject).rethrowAsIOException();
    }
    throw new IOException("socket not created");
  }
  
  protected OutputStream getOutputStream()
    throws IOException
  {
    if (fd != null) {
      try
      {
        if (fos == null)
        {
          localSocketOutputStream = new android/net/LocalSocketImpl$SocketOutputStream;
          localSocketOutputStream.<init>(this);
          fos = localSocketOutputStream;
        }
        SocketOutputStream localSocketOutputStream = fos;
        return localSocketOutputStream;
      }
      finally {}
    }
    throw new IOException("socket not created");
  }
  
  public Credentials getPeerCredentials()
    throws IOException
  {
    return getPeerCredentials_native(fd);
  }
  
  public LocalSocketAddress getSockAddress()
    throws IOException
  {
    return null;
  }
  
  protected void listen(int paramInt)
    throws IOException
  {
    if (fd != null) {
      try
      {
        Os.listen(fd, paramInt);
        return;
      }
      catch (ErrnoException localErrnoException)
      {
        throw localErrnoException.rethrowAsIOException();
      }
    }
    throw new IOException("socket not created");
  }
  
  protected void sendUrgentData(int paramInt)
    throws IOException
  {
    throw new RuntimeException("not impled");
  }
  
  public void setFileDescriptorsForSend(FileDescriptor[] paramArrayOfFileDescriptor)
  {
    synchronized (writeMonitor)
    {
      outboundFileDescriptors = paramArrayOfFileDescriptor;
      return;
    }
  }
  
  public void setOption(int paramInt, Object paramObject)
    throws IOException
  {
    if (fd != null)
    {
      int i = -1;
      int k = 0;
      int j;
      if ((paramObject instanceof Integer))
      {
        k = ((Integer)paramObject).intValue();
      }
      else
      {
        if (!(paramObject instanceof Boolean)) {
          break label244;
        }
        j = ((Boolean)paramObject).booleanValue();
      }
      if (paramInt != 1)
      {
        if (paramInt != 4)
        {
          if (paramInt != 128) {
            if (paramInt == 4102) {}
          }
          switch (paramInt)
          {
          default: 
            try
            {
              paramObject = new java/io/IOException;
              localStringBuilder = new java/lang/StringBuilder;
              localStringBuilder.<init>();
              localStringBuilder.append("Unknown option: ");
              localStringBuilder.append(paramInt);
              paramObject.<init>(localStringBuilder.toString());
              throw paramObject;
            }
            catch (ErrnoException paramObject) {}
            paramObject = StructTimeval.fromMillis(k);
            Os.setsockoptTimeval(fd, OsConstants.SOL_SOCKET, OsConstants.SO_RCVTIMEO, paramObject);
            Os.setsockoptTimeval(fd, OsConstants.SOL_SOCKET, OsConstants.SO_SNDTIMEO, paramObject);
            break;
            paramObject = new android/system/StructLinger;
            paramObject.<init>(j, k);
            Os.setsockoptLinger(fd, OsConstants.SOL_SOCKET, OsConstants.SO_LINGER, paramObject);
            break;
          }
        }
        else
        {
          paramInt = javaSoToOsOpt(paramInt);
          Os.setsockoptInt(fd, OsConstants.SOL_SOCKET, paramInt, k);
        }
      }
      else {
        Os.setsockoptInt(fd, OsConstants.IPPROTO_TCP, OsConstants.TCP_NODELAY, k);
      }
      return;
      throw paramObject.rethrowAsIOException();
      label244:
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("bad value: ");
      localStringBuilder.append(paramObject);
      throw new IOException(localStringBuilder.toString());
    }
    throw new IOException("socket not created");
  }
  
  protected void shutdownInput()
    throws IOException
  {
    if (fd != null) {
      try
      {
        Os.shutdown(fd, OsConstants.SHUT_RD);
        return;
      }
      catch (ErrnoException localErrnoException)
      {
        throw localErrnoException.rethrowAsIOException();
      }
    }
    throw new IOException("socket not created");
  }
  
  protected void shutdownOutput()
    throws IOException
  {
    if (fd != null) {
      try
      {
        Os.shutdown(fd, OsConstants.SHUT_WR);
        return;
      }
      catch (ErrnoException localErrnoException)
      {
        throw localErrnoException.rethrowAsIOException();
      }
    }
    throw new IOException("socket not created");
  }
  
  protected boolean supportsUrgentData()
  {
    return false;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(super.toString());
    localStringBuilder.append(" fd:");
    localStringBuilder.append(fd);
    return localStringBuilder.toString();
  }
  
  class SocketInputStream
    extends InputStream
  {
    SocketInputStream() {}
    
    public int available()
      throws IOException
    {
      FileDescriptor localFileDescriptor = fd;
      if (localFileDescriptor != null)
      {
        Int32Ref localInt32Ref = new Int32Ref(0);
        try
        {
          Os.ioctlInt(localFileDescriptor, OsConstants.FIONREAD, localInt32Ref);
          return value;
        }
        catch (ErrnoException localErrnoException)
        {
          throw localErrnoException.rethrowAsIOException();
        }
      }
      throw new IOException("socket closed");
    }
    
    public void close()
      throws IOException
    {
      LocalSocketImpl.this.close();
    }
    
    public int read()
      throws IOException
    {
      synchronized (readMonitor)
      {
        Object localObject2 = fd;
        if (localObject2 != null)
        {
          int i = LocalSocketImpl.this.read_native((FileDescriptor)localObject2);
          return i;
        }
        localObject2 = new java/io/IOException;
        ((IOException)localObject2).<init>("socket closed");
        throw ((Throwable)localObject2);
      }
    }
    
    public int read(byte[] paramArrayOfByte)
      throws IOException
    {
      return read(paramArrayOfByte, 0, paramArrayOfByte.length);
    }
    
    public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
      throws IOException
    {
      synchronized (readMonitor)
      {
        FileDescriptor localFileDescriptor = fd;
        if (localFileDescriptor != null)
        {
          if ((paramInt1 >= 0) && (paramInt2 >= 0) && (paramInt1 + paramInt2 <= paramArrayOfByte.length))
          {
            paramInt1 = LocalSocketImpl.this.readba_native(paramArrayOfByte, paramInt1, paramInt2, localFileDescriptor);
            return paramInt1;
          }
          paramArrayOfByte = new java/lang/ArrayIndexOutOfBoundsException;
          paramArrayOfByte.<init>();
          throw paramArrayOfByte;
        }
        paramArrayOfByte = new java/io/IOException;
        paramArrayOfByte.<init>("socket closed");
        throw paramArrayOfByte;
      }
    }
  }
  
  class SocketOutputStream
    extends OutputStream
  {
    SocketOutputStream() {}
    
    public void close()
      throws IOException
    {
      LocalSocketImpl.this.close();
    }
    
    public void flush()
      throws IOException
    {
      FileDescriptor localFileDescriptor = fd;
      if (localFileDescriptor != null)
      {
        Int32Ref localInt32Ref = new Int32Ref(0);
        try
        {
          for (;;)
          {
            Os.ioctlInt(localFileDescriptor, OsConstants.TIOCOUTQ, localInt32Ref);
            if (value > 0) {
              try
              {
                Thread.sleep(10L);
              }
              catch (InterruptedException localInterruptedException) {}
            }
          }
          return;
        }
        catch (ErrnoException localErrnoException)
        {
          throw localErrnoException.rethrowAsIOException();
        }
      }
      throw new IOException("socket closed");
    }
    
    public void write(int paramInt)
      throws IOException
    {
      synchronized (writeMonitor)
      {
        Object localObject2 = fd;
        if (localObject2 != null)
        {
          LocalSocketImpl.this.write_native(paramInt, (FileDescriptor)localObject2);
          return;
        }
        localObject2 = new java/io/IOException;
        ((IOException)localObject2).<init>("socket closed");
        throw ((Throwable)localObject2);
      }
    }
    
    public void write(byte[] paramArrayOfByte)
      throws IOException
    {
      write(paramArrayOfByte, 0, paramArrayOfByte.length);
    }
    
    public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
      throws IOException
    {
      synchronized (writeMonitor)
      {
        FileDescriptor localFileDescriptor = fd;
        if (localFileDescriptor != null)
        {
          if ((paramInt1 >= 0) && (paramInt2 >= 0) && (paramInt1 + paramInt2 <= paramArrayOfByte.length))
          {
            LocalSocketImpl.this.writeba_native(paramArrayOfByte, paramInt1, paramInt2, localFileDescriptor);
            return;
          }
          paramArrayOfByte = new java/lang/ArrayIndexOutOfBoundsException;
          paramArrayOfByte.<init>();
          throw paramArrayOfByte;
        }
        paramArrayOfByte = new java/io/IOException;
        paramArrayOfByte.<init>("socket closed");
        throw paramArrayOfByte;
      }
    }
  }
}
