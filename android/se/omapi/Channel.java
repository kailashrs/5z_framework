package android.se.omapi;

import android.os.RemoteException;
import android.os.ServiceSpecificException;
import android.util.Log;
import java.io.IOException;

public final class Channel
  implements java.nio.channels.Channel
{
  private static final String TAG = "OMAPI.Channel";
  private final ISecureElementChannel mChannel;
  private final Object mLock = new Object();
  private final SEService mService;
  private Session mSession;
  
  Channel(SEService paramSEService, Session paramSession, ISecureElementChannel paramISecureElementChannel)
  {
    if ((paramSEService != null) && (paramSession != null) && (paramISecureElementChannel != null))
    {
      mService = paramSEService;
      mSession = paramSession;
      mChannel = paramISecureElementChannel;
      return;
    }
    throw new IllegalArgumentException("Parameters cannot be null");
  }
  
  public void close()
  {
    if (isOpen())
    {
      try
      {
        synchronized (mLock)
        {
          mChannel.close();
        }
      }
      catch (Exception localException)
      {
        Log.e("OMAPI.Channel", "Error closing channel", localException);
      }
      throw localException;
    }
  }
  
  public byte[] getSelectResponse()
  {
    if (mService.isConnected()) {
      try
      {
        byte[] arrayOfByte1 = mChannel.getSelectResponse();
        byte[] arrayOfByte2 = arrayOfByte1;
        if (arrayOfByte1 != null)
        {
          arrayOfByte2 = arrayOfByte1;
          if (arrayOfByte1.length == 0) {
            arrayOfByte2 = null;
          }
        }
        return arrayOfByte2;
      }
      catch (RemoteException localRemoteException)
      {
        throw new IllegalStateException(localRemoteException.getMessage());
      }
    }
    throw new IllegalStateException("service not connected to system");
  }
  
  public Session getSession()
  {
    return mSession;
  }
  
  public boolean isBasicChannel()
  {
    if (mService.isConnected()) {
      try
      {
        boolean bool = mChannel.isBasicChannel();
        return bool;
      }
      catch (RemoteException localRemoteException)
      {
        throw new IllegalStateException(localRemoteException.getMessage());
      }
    }
    throw new IllegalStateException("service not connected to system");
  }
  
  public boolean isOpen()
  {
    if (!mService.isConnected())
    {
      Log.e("OMAPI.Channel", "service not connected to system");
      return false;
    }
    try
    {
      boolean bool = mChannel.isClosed();
      return bool ^ true;
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("OMAPI.Channel", "Exception in isClosed()");
    }
    return false;
  }
  
  public boolean selectNext()
    throws IOException
  {
    if (mService.isConnected()) {
      try
      {
        synchronized (mLock)
        {
          boolean bool = mChannel.selectNext();
          return bool;
        }
        throw new IllegalStateException("service not connected to system");
      }
      catch (RemoteException localRemoteException)
      {
        throw new IllegalStateException(localRemoteException.getMessage());
      }
      catch (ServiceSpecificException localServiceSpecificException)
      {
        throw new IOException(localServiceSpecificException.getMessage());
      }
    }
  }
  
  /* Error */
  public byte[] transmit(byte[] paramArrayOfByte)
    throws IOException
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 27	android/se/omapi/Channel:mService	Landroid/se/omapi/SEService;
    //   4: invokevirtual 67	android/se/omapi/SEService:isConnected	()Z
    //   7: ifeq +79 -> 86
    //   10: aload_0
    //   11: getfield 25	android/se/omapi/Channel:mLock	Ljava/lang/Object;
    //   14: astore_2
    //   15: aload_2
    //   16: monitorenter
    //   17: aload_0
    //   18: getfield 31	android/se/omapi/Channel:mChannel	Landroid/se/omapi/ISecureElementChannel;
    //   21: aload_1
    //   22: invokeinterface 105 2 0
    //   27: astore_1
    //   28: aload_1
    //   29: ifnull +7 -> 36
    //   32: aload_2
    //   33: monitorexit
    //   34: aload_1
    //   35: areturn
    //   36: new 94	java/io/IOException
    //   39: astore_1
    //   40: aload_1
    //   41: ldc 107
    //   43: invokespecial 100	java/io/IOException:<init>	(Ljava/lang/String;)V
    //   46: aload_1
    //   47: athrow
    //   48: astore_1
    //   49: goto +33 -> 82
    //   52: astore_3
    //   53: new 71	java/lang/IllegalStateException
    //   56: astore_1
    //   57: aload_1
    //   58: aload_3
    //   59: invokevirtual 75	android/os/RemoteException:getMessage	()Ljava/lang/String;
    //   62: invokespecial 76	java/lang/IllegalStateException:<init>	(Ljava/lang/String;)V
    //   65: aload_1
    //   66: athrow
    //   67: astore_1
    //   68: new 94	java/io/IOException
    //   71: astore_3
    //   72: aload_3
    //   73: aload_1
    //   74: invokevirtual 99	android/os/ServiceSpecificException:getMessage	()Ljava/lang/String;
    //   77: invokespecial 100	java/io/IOException:<init>	(Ljava/lang/String;)V
    //   80: aload_3
    //   81: athrow
    //   82: aload_2
    //   83: monitorexit
    //   84: aload_1
    //   85: athrow
    //   86: new 71	java/lang/IllegalStateException
    //   89: dup
    //   90: ldc 78
    //   92: invokespecial 76	java/lang/IllegalStateException:<init>	(Ljava/lang/String;)V
    //   95: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	96	0	this	Channel
    //   0	96	1	paramArrayOfByte	byte[]
    //   14	69	2	localObject	Object
    //   52	7	3	localRemoteException	RemoteException
    //   71	10	3	localIOException	IOException
    // Exception table:
    //   from	to	target	type
    //   17	28	48	finally
    //   32	34	48	finally
    //   36	48	48	finally
    //   53	67	48	finally
    //   68	82	48	finally
    //   82	84	48	finally
    //   17	28	52	android/os/RemoteException
    //   36	48	52	android/os/RemoteException
    //   17	28	67	android/os/ServiceSpecificException
    //   36	48	67	android/os/ServiceSpecificException
  }
}
