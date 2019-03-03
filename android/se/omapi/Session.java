package android.se.omapi;

import android.os.RemoteException;
import android.util.Log;
import java.io.IOException;

public final class Session
{
  private static final String TAG = "OMAPI.Session";
  private final Object mLock = new Object();
  private final Reader mReader;
  private final SEService mService;
  private final ISecureElementSession mSession;
  
  Session(SEService paramSEService, ISecureElementSession paramISecureElementSession, Reader paramReader)
  {
    if ((paramSEService != null) && (paramReader != null) && (paramISecureElementSession != null))
    {
      mService = paramSEService;
      mReader = paramReader;
      mSession = paramISecureElementSession;
      return;
    }
    throw new IllegalArgumentException("Parameters cannot be null");
  }
  
  public void close()
  {
    if (!mService.isConnected())
    {
      Log.e("OMAPI.Session", "service not connected to system");
      return;
    }
    try
    {
      synchronized (mLock)
      {
        mSession.close();
      }
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("OMAPI.Session", "Error closing session", localRemoteException);
      return;
    }
  }
  
  public void closeChannels()
  {
    if (!mService.isConnected())
    {
      Log.e("OMAPI.Session", "service not connected to system");
      return;
    }
    try
    {
      synchronized (mLock)
      {
        mSession.closeChannels();
      }
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("OMAPI.Session", "Error closing channels", localRemoteException);
      return;
    }
  }
  
  public byte[] getATR()
  {
    if (mService.isConnected()) {
      try
      {
        byte[] arrayOfByte = mSession.getAtr();
        return arrayOfByte;
      }
      catch (RemoteException localRemoteException)
      {
        throw new IllegalStateException(localRemoteException.getMessage());
      }
    }
    throw new IllegalStateException("service not connected to system");
  }
  
  public Reader getReader()
  {
    return mReader;
  }
  
  public boolean isClosed()
  {
    try
    {
      boolean bool = mSession.isClosed();
      return bool;
    }
    catch (RemoteException localRemoteException) {}
    return true;
  }
  
  public Channel openBasicChannel(byte[] paramArrayOfByte)
    throws IOException
  {
    return openBasicChannel(paramArrayOfByte, (byte)0);
  }
  
  /* Error */
  public Channel openBasicChannel(byte[] paramArrayOfByte, byte paramByte)
    throws IOException
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 25	android/se/omapi/Session:mService	Landroid/se/omapi/SEService;
    //   4: invokevirtual 46	android/se/omapi/SEService:isConnected	()Z
    //   7: ifeq +155 -> 162
    //   10: aload_0
    //   11: getfield 23	android/se/omapi/Session:mLock	Ljava/lang/Object;
    //   14: astore_3
    //   15: aload_3
    //   16: monitorenter
    //   17: aload_0
    //   18: getfield 29	android/se/omapi/Session:mSession	Landroid/se/omapi/ISecureElementSession;
    //   21: aload_1
    //   22: iload_2
    //   23: aload_0
    //   24: getfield 27	android/se/omapi/Session:mReader	Landroid/se/omapi/Reader;
    //   27: invokevirtual 101	android/se/omapi/Reader:getSEService	()Landroid/se/omapi/SEService;
    //   30: invokevirtual 105	android/se/omapi/SEService:getListener	()Landroid/se/omapi/ISecureElementListener;
    //   33: invokeinterface 108 4 0
    //   38: astore 4
    //   40: aload 4
    //   42: ifnonnull +7 -> 49
    //   45: aload_3
    //   46: monitorexit
    //   47: aconst_null
    //   48: areturn
    //   49: new 110	android/se/omapi/Channel
    //   52: astore_1
    //   53: aload_1
    //   54: aload_0
    //   55: getfield 25	android/se/omapi/Session:mService	Landroid/se/omapi/SEService;
    //   58: aload_0
    //   59: aload 4
    //   61: invokespecial 113	android/se/omapi/Channel:<init>	(Landroid/se/omapi/SEService;Landroid/se/omapi/Session;Landroid/se/omapi/ISecureElementChannel;)V
    //   64: aload_3
    //   65: monitorexit
    //   66: aload_1
    //   67: areturn
    //   68: astore_1
    //   69: goto +89 -> 158
    //   72: astore_1
    //   73: new 75	java/lang/IllegalStateException
    //   76: astore 4
    //   78: aload 4
    //   80: aload_1
    //   81: invokevirtual 79	android/os/RemoteException:getMessage	()Ljava/lang/String;
    //   84: invokespecial 80	java/lang/IllegalStateException:<init>	(Ljava/lang/String;)V
    //   87: aload 4
    //   89: athrow
    //   90: astore_1
    //   91: aload_1
    //   92: getfield 117	android/os/ServiceSpecificException:errorCode	I
    //   95: iconst_1
    //   96: if_icmpeq +45 -> 141
    //   99: aload_1
    //   100: getfield 117	android/os/ServiceSpecificException:errorCode	I
    //   103: iconst_2
    //   104: if_icmpne +20 -> 124
    //   107: new 119	java/util/NoSuchElementException
    //   110: astore 4
    //   112: aload 4
    //   114: aload_1
    //   115: invokevirtual 120	android/os/ServiceSpecificException:getMessage	()Ljava/lang/String;
    //   118: invokespecial 121	java/util/NoSuchElementException:<init>	(Ljava/lang/String;)V
    //   121: aload 4
    //   123: athrow
    //   124: new 75	java/lang/IllegalStateException
    //   127: astore 4
    //   129: aload 4
    //   131: aload_1
    //   132: invokevirtual 120	android/os/ServiceSpecificException:getMessage	()Ljava/lang/String;
    //   135: invokespecial 80	java/lang/IllegalStateException:<init>	(Ljava/lang/String;)V
    //   138: aload 4
    //   140: athrow
    //   141: new 89	java/io/IOException
    //   144: astore 4
    //   146: aload 4
    //   148: aload_1
    //   149: invokevirtual 120	android/os/ServiceSpecificException:getMessage	()Ljava/lang/String;
    //   152: invokespecial 122	java/io/IOException:<init>	(Ljava/lang/String;)V
    //   155: aload 4
    //   157: athrow
    //   158: aload_3
    //   159: monitorexit
    //   160: aload_1
    //   161: athrow
    //   162: new 75	java/lang/IllegalStateException
    //   165: dup
    //   166: ldc 48
    //   168: invokespecial 80	java/lang/IllegalStateException:<init>	(Ljava/lang/String;)V
    //   171: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	172	0	this	Session
    //   0	172	1	paramArrayOfByte	byte[]
    //   0	172	2	paramByte	byte
    //   14	145	3	localObject1	Object
    //   38	118	4	localObject2	Object
    // Exception table:
    //   from	to	target	type
    //   17	40	68	finally
    //   45	47	68	finally
    //   49	64	68	finally
    //   64	66	68	finally
    //   73	90	68	finally
    //   91	124	68	finally
    //   124	141	68	finally
    //   141	158	68	finally
    //   158	160	68	finally
    //   17	40	72	android/os/RemoteException
    //   49	64	72	android/os/RemoteException
    //   17	40	90	android/os/ServiceSpecificException
    //   49	64	90	android/os/ServiceSpecificException
  }
  
  public Channel openLogicalChannel(byte[] paramArrayOfByte)
    throws IOException
  {
    return openLogicalChannel(paramArrayOfByte, (byte)0);
  }
  
  /* Error */
  public Channel openLogicalChannel(byte[] paramArrayOfByte, byte paramByte)
    throws IOException
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 25	android/se/omapi/Session:mService	Landroid/se/omapi/SEService;
    //   4: invokevirtual 46	android/se/omapi/SEService:isConnected	()Z
    //   7: ifeq +155 -> 162
    //   10: aload_0
    //   11: getfield 23	android/se/omapi/Session:mLock	Ljava/lang/Object;
    //   14: astore_3
    //   15: aload_3
    //   16: monitorenter
    //   17: aload_0
    //   18: getfield 29	android/se/omapi/Session:mSession	Landroid/se/omapi/ISecureElementSession;
    //   21: aload_1
    //   22: iload_2
    //   23: aload_0
    //   24: getfield 27	android/se/omapi/Session:mReader	Landroid/se/omapi/Reader;
    //   27: invokevirtual 101	android/se/omapi/Reader:getSEService	()Landroid/se/omapi/SEService;
    //   30: invokevirtual 105	android/se/omapi/SEService:getListener	()Landroid/se/omapi/ISecureElementListener;
    //   33: invokeinterface 127 4 0
    //   38: astore 4
    //   40: aload 4
    //   42: ifnonnull +7 -> 49
    //   45: aload_3
    //   46: monitorexit
    //   47: aconst_null
    //   48: areturn
    //   49: new 110	android/se/omapi/Channel
    //   52: astore_1
    //   53: aload_1
    //   54: aload_0
    //   55: getfield 25	android/se/omapi/Session:mService	Landroid/se/omapi/SEService;
    //   58: aload_0
    //   59: aload 4
    //   61: invokespecial 113	android/se/omapi/Channel:<init>	(Landroid/se/omapi/SEService;Landroid/se/omapi/Session;Landroid/se/omapi/ISecureElementChannel;)V
    //   64: aload_3
    //   65: monitorexit
    //   66: aload_1
    //   67: areturn
    //   68: astore_1
    //   69: goto +89 -> 158
    //   72: astore_1
    //   73: new 75	java/lang/IllegalStateException
    //   76: astore 4
    //   78: aload 4
    //   80: aload_1
    //   81: invokevirtual 79	android/os/RemoteException:getMessage	()Ljava/lang/String;
    //   84: invokespecial 80	java/lang/IllegalStateException:<init>	(Ljava/lang/String;)V
    //   87: aload 4
    //   89: athrow
    //   90: astore_1
    //   91: aload_1
    //   92: getfield 117	android/os/ServiceSpecificException:errorCode	I
    //   95: iconst_1
    //   96: if_icmpeq +45 -> 141
    //   99: aload_1
    //   100: getfield 117	android/os/ServiceSpecificException:errorCode	I
    //   103: iconst_2
    //   104: if_icmpne +20 -> 124
    //   107: new 119	java/util/NoSuchElementException
    //   110: astore 4
    //   112: aload 4
    //   114: aload_1
    //   115: invokevirtual 120	android/os/ServiceSpecificException:getMessage	()Ljava/lang/String;
    //   118: invokespecial 121	java/util/NoSuchElementException:<init>	(Ljava/lang/String;)V
    //   121: aload 4
    //   123: athrow
    //   124: new 75	java/lang/IllegalStateException
    //   127: astore 4
    //   129: aload 4
    //   131: aload_1
    //   132: invokevirtual 120	android/os/ServiceSpecificException:getMessage	()Ljava/lang/String;
    //   135: invokespecial 80	java/lang/IllegalStateException:<init>	(Ljava/lang/String;)V
    //   138: aload 4
    //   140: athrow
    //   141: new 89	java/io/IOException
    //   144: astore 4
    //   146: aload 4
    //   148: aload_1
    //   149: invokevirtual 120	android/os/ServiceSpecificException:getMessage	()Ljava/lang/String;
    //   152: invokespecial 122	java/io/IOException:<init>	(Ljava/lang/String;)V
    //   155: aload 4
    //   157: athrow
    //   158: aload_3
    //   159: monitorexit
    //   160: aload_1
    //   161: athrow
    //   162: new 75	java/lang/IllegalStateException
    //   165: dup
    //   166: ldc 48
    //   168: invokespecial 80	java/lang/IllegalStateException:<init>	(Ljava/lang/String;)V
    //   171: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	172	0	this	Session
    //   0	172	1	paramArrayOfByte	byte[]
    //   0	172	2	paramByte	byte
    //   14	145	3	localObject1	Object
    //   38	118	4	localObject2	Object
    // Exception table:
    //   from	to	target	type
    //   17	40	68	finally
    //   45	47	68	finally
    //   49	64	68	finally
    //   64	66	68	finally
    //   73	90	68	finally
    //   91	124	68	finally
    //   124	141	68	finally
    //   141	158	68	finally
    //   158	160	68	finally
    //   17	40	72	android/os/RemoteException
    //   49	64	72	android/os/RemoteException
    //   17	40	90	android/os/ServiceSpecificException
    //   49	64	90	android/os/ServiceSpecificException
  }
}
