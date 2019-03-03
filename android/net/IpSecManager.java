package android.net;

import android.content.Context;
import android.os.Binder;
import android.os.IBinder;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.os.ServiceSpecificException;
import android.system.ErrnoException;
import android.system.OsConstants;
import android.util.AndroidException;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.Preconditions;
import dalvik.system.CloseGuard;
import java.io.FileDescriptor;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

public final class IpSecManager
{
  public static final int DIRECTION_IN = 0;
  public static final int DIRECTION_OUT = 1;
  public static final int INVALID_RESOURCE_ID = -1;
  public static final int INVALID_SECURITY_PARAMETER_INDEX = 0;
  private static final String TAG = "IpSecManager";
  private final Context mContext;
  private final IIpSecService mService;
  
  public IpSecManager(Context paramContext, IIpSecService paramIIpSecService)
  {
    mContext = paramContext;
    mService = ((IIpSecService)Preconditions.checkNotNull(paramIIpSecService, "missing service"));
  }
  
  private static void maybeHandleServiceSpecificException(ServiceSpecificException paramServiceSpecificException)
  {
    if (errorCode != OsConstants.EINVAL)
    {
      if (errorCode != OsConstants.EAGAIN)
      {
        if (errorCode != OsConstants.EOPNOTSUPP) {
          return;
        }
        throw new UnsupportedOperationException(paramServiceSpecificException);
      }
      throw new IllegalStateException(paramServiceSpecificException);
    }
    throw new IllegalArgumentException(paramServiceSpecificException);
  }
  
  static IOException rethrowCheckedExceptionFromServiceSpecificException(ServiceSpecificException paramServiceSpecificException)
    throws IOException
  {
    maybeHandleServiceSpecificException(paramServiceSpecificException);
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("IpSec encountered errno=");
    localStringBuilder.append(errorCode);
    throw new ErrnoException(localStringBuilder.toString(), errorCode).rethrowAsIOException();
  }
  
  static RuntimeException rethrowUncheckedExceptionFromServiceSpecificException(ServiceSpecificException paramServiceSpecificException)
  {
    maybeHandleServiceSpecificException(paramServiceSpecificException);
    throw new RuntimeException(paramServiceSpecificException);
  }
  
  public SecurityParameterIndex allocateSecurityParameterIndex(InetAddress paramInetAddress)
    throws IpSecManager.ResourceUnavailableException
  {
    try
    {
      paramInetAddress = new SecurityParameterIndex(mService, paramInetAddress, 0, null);
      return paramInetAddress;
    }
    catch (SpiUnavailableException paramInetAddress)
    {
      throw new ResourceUnavailableException("No SPIs available");
    }
    catch (ServiceSpecificException paramInetAddress)
    {
      throw rethrowUncheckedExceptionFromServiceSpecificException(paramInetAddress);
    }
  }
  
  public SecurityParameterIndex allocateSecurityParameterIndex(InetAddress paramInetAddress, int paramInt)
    throws IpSecManager.SpiUnavailableException, IpSecManager.ResourceUnavailableException
  {
    if (paramInt != 0) {
      try
      {
        paramInetAddress = new SecurityParameterIndex(mService, paramInetAddress, paramInt, null);
        return paramInetAddress;
      }
      catch (ServiceSpecificException paramInetAddress)
      {
        throw rethrowUncheckedExceptionFromServiceSpecificException(paramInetAddress);
      }
    }
    throw new IllegalArgumentException("Requested SPI must be a valid (non-zero) SPI");
  }
  
  /* Error */
  public void applyTransportModeTransform(FileDescriptor paramFileDescriptor, int paramInt, IpSecTransform paramIpSecTransform)
    throws IOException
  {
    // Byte code:
    //   0: aload_1
    //   1: invokestatic 167	android/os/ParcelFileDescriptor:dup	(Ljava/io/FileDescriptor;)Landroid/os/ParcelFileDescriptor;
    //   4: astore 4
    //   6: aconst_null
    //   7: astore_1
    //   8: aload_0
    //   9: getfield 76	android/net/IpSecManager:mService	Landroid/net/IIpSecService;
    //   12: aload 4
    //   14: iload_2
    //   15: aload_3
    //   16: invokevirtual 173	android/net/IpSecTransform:getResourceId	()I
    //   19: invokeinterface 176 4 0
    //   24: aload 4
    //   26: ifnull +9 -> 35
    //   29: aconst_null
    //   30: aload 4
    //   32: invokestatic 178	android/net/IpSecManager:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   35: return
    //   36: astore_3
    //   37: goto +8 -> 45
    //   40: astore_3
    //   41: aload_3
    //   42: astore_1
    //   43: aload_3
    //   44: athrow
    //   45: aload 4
    //   47: ifnull +9 -> 56
    //   50: aload_1
    //   51: aload 4
    //   53: invokestatic 178	android/net/IpSecManager:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   56: aload_3
    //   57: athrow
    //   58: astore_1
    //   59: aload_1
    //   60: invokevirtual 182	android/os/RemoteException:rethrowFromSystemServer	()Ljava/lang/RuntimeException;
    //   63: athrow
    //   64: astore_1
    //   65: aload_1
    //   66: invokestatic 184	android/net/IpSecManager:rethrowCheckedExceptionFromServiceSpecificException	(Landroid/os/ServiceSpecificException;)Ljava/io/IOException;
    //   69: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	70	0	this	IpSecManager
    //   0	70	1	paramFileDescriptor	FileDescriptor
    //   0	70	2	paramInt	int
    //   0	70	3	paramIpSecTransform	IpSecTransform
    //   4	48	4	localParcelFileDescriptor	ParcelFileDescriptor
    // Exception table:
    //   from	to	target	type
    //   8	24	36	finally
    //   43	45	36	finally
    //   8	24	40	java/lang/Throwable
    //   0	6	58	android/os/RemoteException
    //   29	35	58	android/os/RemoteException
    //   50	56	58	android/os/RemoteException
    //   56	58	58	android/os/RemoteException
    //   0	6	64	android/os/ServiceSpecificException
    //   29	35	64	android/os/ServiceSpecificException
    //   50	56	64	android/os/ServiceSpecificException
    //   56	58	64	android/os/ServiceSpecificException
  }
  
  public void applyTransportModeTransform(DatagramSocket paramDatagramSocket, int paramInt, IpSecTransform paramIpSecTransform)
    throws IOException
  {
    applyTransportModeTransform(paramDatagramSocket.getFileDescriptor$(), paramInt, paramIpSecTransform);
  }
  
  public void applyTransportModeTransform(Socket paramSocket, int paramInt, IpSecTransform paramIpSecTransform)
    throws IOException
  {
    paramSocket.getSoLinger();
    applyTransportModeTransform(paramSocket.getFileDescriptor$(), paramInt, paramIpSecTransform);
  }
  
  public void applyTunnelModeTransform(IpSecTunnelInterface paramIpSecTunnelInterface, int paramInt, IpSecTransform paramIpSecTransform)
    throws IOException
  {
    try
    {
      mService.applyTunnelModeTransform(paramIpSecTunnelInterface.getResourceId(), paramInt, paramIpSecTransform.getResourceId(), mContext.getOpPackageName());
      return;
    }
    catch (RemoteException paramIpSecTunnelInterface)
    {
      throw paramIpSecTunnelInterface.rethrowFromSystemServer();
    }
    catch (ServiceSpecificException paramIpSecTunnelInterface)
    {
      throw rethrowCheckedExceptionFromServiceSpecificException(paramIpSecTunnelInterface);
    }
  }
  
  public IpSecTunnelInterface createIpSecTunnelInterface(InetAddress paramInetAddress1, InetAddress paramInetAddress2, Network paramNetwork)
    throws IpSecManager.ResourceUnavailableException, IOException
  {
    try
    {
      paramInetAddress1 = new IpSecTunnelInterface(mContext, mService, paramInetAddress1, paramInetAddress2, paramNetwork, null);
      return paramInetAddress1;
    }
    catch (ServiceSpecificException paramInetAddress1)
    {
      throw rethrowCheckedExceptionFromServiceSpecificException(paramInetAddress1);
    }
  }
  
  public UdpEncapsulationSocket openUdpEncapsulationSocket()
    throws IOException, IpSecManager.ResourceUnavailableException
  {
    try
    {
      UdpEncapsulationSocket localUdpEncapsulationSocket = new UdpEncapsulationSocket(mService, 0, null);
      return localUdpEncapsulationSocket;
    }
    catch (ServiceSpecificException localServiceSpecificException)
    {
      throw rethrowCheckedExceptionFromServiceSpecificException(localServiceSpecificException);
    }
  }
  
  public UdpEncapsulationSocket openUdpEncapsulationSocket(int paramInt)
    throws IOException, IpSecManager.ResourceUnavailableException
  {
    if (paramInt != 0) {
      try
      {
        UdpEncapsulationSocket localUdpEncapsulationSocket = new UdpEncapsulationSocket(mService, paramInt, null);
        return localUdpEncapsulationSocket;
      }
      catch (ServiceSpecificException localServiceSpecificException)
      {
        throw rethrowCheckedExceptionFromServiceSpecificException(localServiceSpecificException);
      }
    }
    throw new IllegalArgumentException("Specified port must be a valid port number!");
  }
  
  /* Error */
  public void removeTransportModeTransforms(FileDescriptor paramFileDescriptor)
    throws IOException
  {
    // Byte code:
    //   0: aload_1
    //   1: invokestatic 167	android/os/ParcelFileDescriptor:dup	(Ljava/io/FileDescriptor;)Landroid/os/ParcelFileDescriptor;
    //   4: astore_2
    //   5: aconst_null
    //   6: astore_1
    //   7: aload_0
    //   8: getfield 76	android/net/IpSecManager:mService	Landroid/net/IIpSecService;
    //   11: aload_2
    //   12: invokeinterface 229 2 0
    //   17: aload_2
    //   18: ifnull +8 -> 26
    //   21: aconst_null
    //   22: aload_2
    //   23: invokestatic 178	android/net/IpSecManager:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   26: return
    //   27: astore_3
    //   28: goto +8 -> 36
    //   31: astore_3
    //   32: aload_3
    //   33: astore_1
    //   34: aload_3
    //   35: athrow
    //   36: aload_2
    //   37: ifnull +8 -> 45
    //   40: aload_1
    //   41: aload_2
    //   42: invokestatic 178	android/net/IpSecManager:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   45: aload_3
    //   46: athrow
    //   47: astore_1
    //   48: aload_1
    //   49: invokevirtual 182	android/os/RemoteException:rethrowFromSystemServer	()Ljava/lang/RuntimeException;
    //   52: athrow
    //   53: astore_1
    //   54: aload_1
    //   55: invokestatic 184	android/net/IpSecManager:rethrowCheckedExceptionFromServiceSpecificException	(Landroid/os/ServiceSpecificException;)Ljava/io/IOException;
    //   58: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	59	0	this	IpSecManager
    //   0	59	1	paramFileDescriptor	FileDescriptor
    //   4	38	2	localParcelFileDescriptor	ParcelFileDescriptor
    //   27	1	3	localObject	Object
    //   31	15	3	localThrowable	Throwable
    // Exception table:
    //   from	to	target	type
    //   7	17	27	finally
    //   34	36	27	finally
    //   7	17	31	java/lang/Throwable
    //   0	5	47	android/os/RemoteException
    //   21	26	47	android/os/RemoteException
    //   40	45	47	android/os/RemoteException
    //   45	47	47	android/os/RemoteException
    //   0	5	53	android/os/ServiceSpecificException
    //   21	26	53	android/os/ServiceSpecificException
    //   40	45	53	android/os/ServiceSpecificException
    //   45	47	53	android/os/ServiceSpecificException
  }
  
  public void removeTransportModeTransforms(DatagramSocket paramDatagramSocket)
    throws IOException
  {
    removeTransportModeTransforms(paramDatagramSocket.getFileDescriptor$());
  }
  
  public void removeTransportModeTransforms(Socket paramSocket)
    throws IOException
  {
    paramSocket.getSoLinger();
    removeTransportModeTransforms(paramSocket.getFileDescriptor$());
  }
  
  public void removeTunnelModeTransform(Network paramNetwork, IpSecTransform paramIpSecTransform) {}
  
  public static final class IpSecTunnelInterface
    implements AutoCloseable
  {
    private final CloseGuard mCloseGuard = CloseGuard.get();
    private String mInterfaceName;
    private final InetAddress mLocalAddress;
    private final String mOpPackageName;
    private final InetAddress mRemoteAddress;
    private int mResourceId = -1;
    private final IIpSecService mService;
    private final Network mUnderlyingNetwork;
    
    private IpSecTunnelInterface(Context paramContext, IIpSecService paramIIpSecService, InetAddress paramInetAddress1, InetAddress paramInetAddress2, Network paramNetwork)
      throws IpSecManager.ResourceUnavailableException, IOException
    {
      mOpPackageName = paramContext.getOpPackageName();
      mService = paramIIpSecService;
      mLocalAddress = paramInetAddress1;
      mRemoteAddress = paramInetAddress2;
      mUnderlyingNetwork = paramNetwork;
      try
      {
        paramContext = mService;
        paramIIpSecService = paramInetAddress1.getHostAddress();
        paramInetAddress2 = paramInetAddress2.getHostAddress();
        paramInetAddress1 = new android/os/Binder;
        paramInetAddress1.<init>();
        paramContext = paramContext.createTunnelInterface(paramIIpSecService, paramInetAddress2, paramNetwork, paramInetAddress1, mOpPackageName);
        switch (status)
        {
        default: 
          paramInetAddress1 = new java/lang/RuntimeException;
          break;
        case 1: 
          paramContext = new android/net/IpSecManager$ResourceUnavailableException;
          paramContext.<init>("No more tunnel interfaces may be allocated by this requester.");
          throw paramContext;
        case 0: 
          mResourceId = resourceId;
          mInterfaceName = interfaceName;
          mCloseGuard.open("constructor");
          return;
        }
        paramIIpSecService = new java/lang/StringBuilder;
        paramIIpSecService.<init>();
        paramIIpSecService.append("Unknown status returned by IpSecService: ");
        paramIIpSecService.append(status);
        paramInetAddress1.<init>(paramIIpSecService.toString());
        throw paramInetAddress1;
      }
      catch (RemoteException paramContext)
      {
        throw paramContext.rethrowFromSystemServer();
      }
    }
    
    public void addAddress(InetAddress paramInetAddress, int paramInt)
      throws IOException
    {
      try
      {
        IIpSecService localIIpSecService = mService;
        int i = mResourceId;
        LinkAddress localLinkAddress = new android/net/LinkAddress;
        localLinkAddress.<init>(paramInetAddress, paramInt);
        localIIpSecService.addAddressToTunnelInterface(i, localLinkAddress, mOpPackageName);
        return;
      }
      catch (RemoteException paramInetAddress)
      {
        throw paramInetAddress.rethrowFromSystemServer();
      }
      catch (ServiceSpecificException paramInetAddress)
      {
        throw IpSecManager.rethrowCheckedExceptionFromServiceSpecificException(paramInetAddress);
      }
    }
    
    /* Error */
    public void close()
    {
      // Byte code:
      //   0: aload_0
      //   1: getfield 54	android/net/IpSecManager$IpSecTunnelInterface:mService	Landroid/net/IIpSecService;
      //   4: aload_0
      //   5: getfield 44	android/net/IpSecManager$IpSecTunnelInterface:mResourceId	I
      //   8: aload_0
      //   9: getfield 52	android/net/IpSecManager$IpSecTunnelInterface:mOpPackageName	Ljava/lang/String;
      //   12: invokeinterface 147 3 0
      //   17: aload_0
      //   18: iconst_m1
      //   19: putfield 44	android/net/IpSecManager$IpSecTunnelInterface:mResourceId	I
      //   22: aload_0
      //   23: getfield 42	android/net/IpSecManager$IpSecTunnelInterface:mCloseGuard	Ldalvik/system/CloseGuard;
      //   26: invokevirtual 149	dalvik/system/CloseGuard:close	()V
      //   29: goto +55 -> 84
      //   32: astore_1
      //   33: goto +58 -> 91
      //   36: astore_1
      //   37: new 101	java/lang/StringBuilder
      //   40: astore_2
      //   41: aload_2
      //   42: invokespecial 102	java/lang/StringBuilder:<init>	()V
      //   45: aload_2
      //   46: ldc -105
      //   48: invokevirtual 108	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   51: pop
      //   52: aload_2
      //   53: aload_0
      //   54: invokevirtual 154	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
      //   57: pop
      //   58: aload_2
      //   59: ldc -100
      //   61: invokevirtual 108	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   64: pop
      //   65: aload_2
      //   66: aload_1
      //   67: invokevirtual 154	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
      //   70: pop
      //   71: ldc -98
      //   73: aload_2
      //   74: invokevirtual 114	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   77: invokestatic 164	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
      //   80: pop
      //   81: goto -64 -> 17
      //   84: return
      //   85: astore_1
      //   86: aload_1
      //   87: invokevirtual 119	android/os/RemoteException:rethrowFromSystemServer	()Ljava/lang/RuntimeException;
      //   90: athrow
      //   91: aload_0
      //   92: iconst_m1
      //   93: putfield 44	android/net/IpSecManager$IpSecTunnelInterface:mResourceId	I
      //   96: aload_0
      //   97: getfield 42	android/net/IpSecManager$IpSecTunnelInterface:mCloseGuard	Ldalvik/system/CloseGuard;
      //   100: invokevirtual 149	dalvik/system/CloseGuard:close	()V
      //   103: aload_1
      //   104: athrow
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	105	0	this	IpSecTunnelInterface
      //   32	1	1	localObject	Object
      //   36	31	1	localException	Exception
      //   85	19	1	localRemoteException	RemoteException
      //   40	34	2	localStringBuilder	StringBuilder
      // Exception table:
      //   from	to	target	type
      //   0	17	32	finally
      //   37	81	32	finally
      //   86	91	32	finally
      //   0	17	36	java/lang/Exception
      //   0	17	85	android/os/RemoteException
    }
    
    protected void finalize()
      throws Throwable
    {
      if (mCloseGuard != null) {
        mCloseGuard.warnIfOpen();
      }
      close();
    }
    
    public String getInterfaceName()
    {
      return mInterfaceName;
    }
    
    @VisibleForTesting
    public int getResourceId()
    {
      return mResourceId;
    }
    
    public void removeAddress(InetAddress paramInetAddress, int paramInt)
      throws IOException
    {
      try
      {
        IIpSecService localIIpSecService = mService;
        int i = mResourceId;
        LinkAddress localLinkAddress = new android/net/LinkAddress;
        localLinkAddress.<init>(paramInetAddress, paramInt);
        localIIpSecService.removeAddressFromTunnelInterface(i, localLinkAddress, mOpPackageName);
        return;
      }
      catch (RemoteException paramInetAddress)
      {
        throw paramInetAddress.rethrowFromSystemServer();
      }
      catch (ServiceSpecificException paramInetAddress)
      {
        throw IpSecManager.rethrowCheckedExceptionFromServiceSpecificException(paramInetAddress);
      }
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("IpSecTunnelInterface{ifname=");
      localStringBuilder.append(mInterfaceName);
      localStringBuilder.append(",resourceId=");
      localStringBuilder.append(mResourceId);
      localStringBuilder.append("}");
      return localStringBuilder.toString();
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface PolicyDirection {}
  
  public static final class ResourceUnavailableException
    extends AndroidException
  {
    ResourceUnavailableException(String paramString)
    {
      super();
    }
  }
  
  public static final class SecurityParameterIndex
    implements AutoCloseable
  {
    private final CloseGuard mCloseGuard = CloseGuard.get();
    private final InetAddress mDestinationAddress;
    private int mResourceId = -1;
    private final IIpSecService mService;
    private int mSpi = 0;
    
    private SecurityParameterIndex(IIpSecService paramIIpSecService, InetAddress paramInetAddress, int paramInt)
      throws IpSecManager.ResourceUnavailableException, IpSecManager.SpiUnavailableException
    {
      mService = paramIIpSecService;
      mDestinationAddress = paramInetAddress;
      try
      {
        paramIIpSecService = mService;
        String str = paramInetAddress.getHostAddress();
        paramInetAddress = new android/os/Binder;
        paramInetAddress.<init>();
        paramIIpSecService = paramIIpSecService.allocateSecurityParameterIndex(str, paramInt, paramInetAddress);
        if (paramIIpSecService != null)
        {
          int i = status;
          switch (i)
          {
          default: 
            paramIIpSecService = new java/lang/RuntimeException;
            break;
          case 2: 
            paramIIpSecService = new android/net/IpSecManager$SpiUnavailableException;
            paramIIpSecService.<init>("Requested SPI is unavailable", paramInt);
            throw paramIIpSecService;
          case 1: 
            paramIIpSecService = new android/net/IpSecManager$ResourceUnavailableException;
            paramIIpSecService.<init>("No more SPIs may be allocated by this requester.");
            throw paramIIpSecService;
          case 0: 
            mSpi = spi;
            mResourceId = resourceId;
            if (mSpi != 0)
            {
              paramInt = mResourceId;
              if (paramInt != -1)
              {
                mCloseGuard.open("open");
                return;
              }
              paramInetAddress = new java/lang/RuntimeException;
              paramIIpSecService = new java/lang/StringBuilder;
              paramIIpSecService.<init>();
              paramIIpSecService.append("Invalid Resource ID returned by IpSecService: ");
              paramIIpSecService.append(i);
              paramInetAddress.<init>(paramIIpSecService.toString());
              throw paramInetAddress;
            }
            paramInetAddress = new java/lang/RuntimeException;
            paramIIpSecService = new java/lang/StringBuilder;
            paramIIpSecService.<init>();
            paramIIpSecService.append("Invalid SPI returned by IpSecService: ");
            paramIIpSecService.append(i);
            paramInetAddress.<init>(paramIIpSecService.toString());
            throw paramInetAddress;
          }
          paramInetAddress = new java/lang/StringBuilder;
          paramInetAddress.<init>();
          paramInetAddress.append("Unknown status returned by IpSecService: ");
          paramInetAddress.append(i);
          paramIIpSecService.<init>(paramInetAddress.toString());
          throw paramIIpSecService;
        }
        paramIIpSecService = new java/lang/NullPointerException;
        paramIIpSecService.<init>("Received null response from IpSecService");
        throw paramIIpSecService;
      }
      catch (RemoteException paramIIpSecService)
      {
        throw paramIIpSecService.rethrowFromSystemServer();
      }
    }
    
    /* Error */
    public void close()
    {
      // Byte code:
      //   0: aload_0
      //   1: getfield 43	android/net/IpSecManager$SecurityParameterIndex:mService	Landroid/net/IIpSecService;
      //   4: aload_0
      //   5: getfield 41	android/net/IpSecManager$SecurityParameterIndex:mResourceId	I
      //   8: invokeinterface 128 2 0
      //   13: aload_0
      //   14: iconst_m1
      //   15: putfield 41	android/net/IpSecManager$SecurityParameterIndex:mResourceId	I
      //   18: aload_0
      //   19: getfield 37	android/net/IpSecManager$SecurityParameterIndex:mCloseGuard	Ldalvik/system/CloseGuard;
      //   22: invokevirtual 130	dalvik/system/CloseGuard:close	()V
      //   25: goto +55 -> 80
      //   28: astore_1
      //   29: goto +58 -> 87
      //   32: astore_1
      //   33: new 89	java/lang/StringBuilder
      //   36: astore_2
      //   37: aload_2
      //   38: invokespecial 90	java/lang/StringBuilder:<init>	()V
      //   41: aload_2
      //   42: ldc -124
      //   44: invokevirtual 96	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   47: pop
      //   48: aload_2
      //   49: aload_0
      //   50: invokevirtual 135	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
      //   53: pop
      //   54: aload_2
      //   55: ldc -119
      //   57: invokevirtual 96	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   60: pop
      //   61: aload_2
      //   62: aload_1
      //   63: invokevirtual 135	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
      //   66: pop
      //   67: ldc -117
      //   69: aload_2
      //   70: invokevirtual 102	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   73: invokestatic 145	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
      //   76: pop
      //   77: goto -64 -> 13
      //   80: return
      //   81: astore_1
      //   82: aload_1
      //   83: invokevirtual 116	android/os/RemoteException:rethrowFromSystemServer	()Ljava/lang/RuntimeException;
      //   86: athrow
      //   87: aload_0
      //   88: iconst_m1
      //   89: putfield 41	android/net/IpSecManager$SecurityParameterIndex:mResourceId	I
      //   92: aload_0
      //   93: getfield 37	android/net/IpSecManager$SecurityParameterIndex:mCloseGuard	Ldalvik/system/CloseGuard;
      //   96: invokevirtual 130	dalvik/system/CloseGuard:close	()V
      //   99: aload_1
      //   100: athrow
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	101	0	this	SecurityParameterIndex
      //   28	1	1	localObject	Object
      //   32	31	1	localException	Exception
      //   81	19	1	localRemoteException	RemoteException
      //   36	34	2	localStringBuilder	StringBuilder
      // Exception table:
      //   from	to	target	type
      //   0	13	28	finally
      //   33	77	28	finally
      //   82	87	28	finally
      //   0	13	32	java/lang/Exception
      //   0	13	81	android/os/RemoteException
    }
    
    protected void finalize()
      throws Throwable
    {
      if (mCloseGuard != null) {
        mCloseGuard.warnIfOpen();
      }
      close();
    }
    
    @VisibleForTesting
    public int getResourceId()
    {
      return mResourceId;
    }
    
    public int getSpi()
    {
      return mSpi;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("SecurityParameterIndex{spi=");
      localStringBuilder.append(mSpi);
      localStringBuilder.append(",resourceId=");
      localStringBuilder.append(mResourceId);
      localStringBuilder.append("}");
      return localStringBuilder.toString();
    }
  }
  
  public static final class SpiUnavailableException
    extends AndroidException
  {
    private final int mSpi;
    
    SpiUnavailableException(String paramString, int paramInt)
    {
      super();
      mSpi = paramInt;
    }
    
    public int getSpi()
    {
      return mSpi;
    }
  }
  
  public static abstract interface Status
  {
    public static final int OK = 0;
    public static final int RESOURCE_UNAVAILABLE = 1;
    public static final int SPI_UNAVAILABLE = 2;
  }
  
  public static final class UdpEncapsulationSocket
    implements AutoCloseable
  {
    private final CloseGuard mCloseGuard = CloseGuard.get();
    private final ParcelFileDescriptor mPfd;
    private final int mPort;
    private int mResourceId = -1;
    private final IIpSecService mService;
    
    private UdpEncapsulationSocket(IIpSecService paramIIpSecService, int paramInt)
      throws IpSecManager.ResourceUnavailableException, IOException
    {
      mService = paramIIpSecService;
      try
      {
        paramIIpSecService = mService;
        Object localObject = new android/os/Binder;
        ((Binder)localObject).<init>();
        paramIIpSecService = paramIIpSecService.openUdpEncapsulationSocket(paramInt, (IBinder)localObject);
        switch (status)
        {
        default: 
          localObject = new java/lang/RuntimeException;
          break;
        case 1: 
          paramIIpSecService = new android/net/IpSecManager$ResourceUnavailableException;
          paramIIpSecService.<init>("No more Sockets may be allocated by this requester.");
          throw paramIIpSecService;
        case 0: 
          mResourceId = resourceId;
          mPort = port;
          mPfd = fileDescriptor;
          mCloseGuard.open("constructor");
          return;
        }
        StringBuilder localStringBuilder = new java/lang/StringBuilder;
        localStringBuilder.<init>();
        localStringBuilder.append("Unknown status returned by IpSecService: ");
        localStringBuilder.append(status);
        ((RuntimeException)localObject).<init>(localStringBuilder.toString());
        throw ((Throwable)localObject);
      }
      catch (RemoteException paramIIpSecService)
      {
        throw paramIIpSecService.rethrowFromSystemServer();
      }
    }
    
    /* Error */
    public void close()
      throws IOException
    {
      // Byte code:
      //   0: aload_0
      //   1: getfield 41	android/net/IpSecManager$UdpEncapsulationSocket:mService	Landroid/net/IIpSecService;
      //   4: aload_0
      //   5: getfield 31	android/net/IpSecManager$UdpEncapsulationSocket:mResourceId	I
      //   8: invokeinterface 113 2 0
      //   13: aload_0
      //   14: iconst_m1
      //   15: putfield 31	android/net/IpSecManager$UdpEncapsulationSocket:mResourceId	I
      //   18: aload_0
      //   19: iconst_m1
      //   20: putfield 31	android/net/IpSecManager$UdpEncapsulationSocket:mResourceId	I
      //   23: aload_0
      //   24: getfield 39	android/net/IpSecManager$UdpEncapsulationSocket:mCloseGuard	Ldalvik/system/CloseGuard;
      //   27: invokevirtual 115	dalvik/system/CloseGuard:close	()V
      //   30: goto +55 -> 85
      //   33: astore_1
      //   34: goto +102 -> 136
      //   37: astore_1
      //   38: new 82	java/lang/StringBuilder
      //   41: astore_2
      //   42: aload_2
      //   43: invokespecial 83	java/lang/StringBuilder:<init>	()V
      //   46: aload_2
      //   47: ldc 117
      //   49: invokevirtual 89	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   52: pop
      //   53: aload_2
      //   54: aload_0
      //   55: invokevirtual 120	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
      //   58: pop
      //   59: aload_2
      //   60: ldc 122
      //   62: invokevirtual 89	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   65: pop
      //   66: aload_2
      //   67: aload_1
      //   68: invokevirtual 120	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
      //   71: pop
      //   72: ldc 124
      //   74: aload_2
      //   75: invokevirtual 96	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   78: invokestatic 130	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
      //   81: pop
      //   82: goto -64 -> 18
      //   85: aload_0
      //   86: getfield 75	android/net/IpSecManager$UdpEncapsulationSocket:mPfd	Landroid/os/ParcelFileDescriptor;
      //   89: invokevirtual 133	android/os/ParcelFileDescriptor:close	()V
      //   92: return
      //   93: astore_2
      //   94: new 82	java/lang/StringBuilder
      //   97: dup
      //   98: invokespecial 83	java/lang/StringBuilder:<init>	()V
      //   101: astore_1
      //   102: aload_1
      //   103: ldc -121
      //   105: invokevirtual 89	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   108: pop
      //   109: aload_1
      //   110: aload_0
      //   111: getfield 70	android/net/IpSecManager$UdpEncapsulationSocket:mPort	I
      //   114: invokevirtual 92	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
      //   117: pop
      //   118: ldc 124
      //   120: aload_1
      //   121: invokevirtual 96	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   124: invokestatic 130	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
      //   127: pop
      //   128: aload_2
      //   129: athrow
      //   130: astore_1
      //   131: aload_1
      //   132: invokevirtual 101	android/os/RemoteException:rethrowFromSystemServer	()Ljava/lang/RuntimeException;
      //   135: athrow
      //   136: aload_0
      //   137: iconst_m1
      //   138: putfield 31	android/net/IpSecManager$UdpEncapsulationSocket:mResourceId	I
      //   141: aload_0
      //   142: getfield 39	android/net/IpSecManager$UdpEncapsulationSocket:mCloseGuard	Ldalvik/system/CloseGuard;
      //   145: invokevirtual 115	dalvik/system/CloseGuard:close	()V
      //   148: aload_1
      //   149: athrow
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	150	0	this	UdpEncapsulationSocket
      //   33	1	1	localObject	Object
      //   37	31	1	localException	Exception
      //   101	20	1	localStringBuilder1	StringBuilder
      //   130	19	1	localRemoteException	RemoteException
      //   41	34	2	localStringBuilder2	StringBuilder
      //   93	36	2	localIOException	IOException
      // Exception table:
      //   from	to	target	type
      //   0	18	33	finally
      //   38	82	33	finally
      //   131	136	33	finally
      //   0	18	37	java/lang/Exception
      //   85	92	93	java/io/IOException
      //   0	18	130	android/os/RemoteException
    }
    
    protected void finalize()
      throws Throwable
    {
      if (mCloseGuard != null) {
        mCloseGuard.warnIfOpen();
      }
      close();
    }
    
    public FileDescriptor getFileDescriptor()
    {
      if (mPfd == null) {
        return null;
      }
      return mPfd.getFileDescriptor();
    }
    
    public int getPort()
    {
      return mPort;
    }
    
    @VisibleForTesting
    public int getResourceId()
    {
      return mResourceId;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("UdpEncapsulationSocket{port=");
      localStringBuilder.append(mPort);
      localStringBuilder.append(",resourceId=");
      localStringBuilder.append(mResourceId);
      localStringBuilder.append("}");
      return localStringBuilder.toString();
    }
  }
}
