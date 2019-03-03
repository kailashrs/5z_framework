package android.se.omapi;

import android.os.RemoteException;
import android.util.Log;

public final class Reader
{
  private static final String TAG = "OMAPI.Reader";
  private final Object mLock = new Object();
  private final String mName;
  private ISecureElementReader mReader;
  private final SEService mService;
  
  Reader(SEService paramSEService, String paramString, ISecureElementReader paramISecureElementReader)
  {
    if ((paramISecureElementReader != null) && (paramSEService != null) && (paramString != null))
    {
      mName = paramString;
      mService = paramSEService;
      mReader = paramISecureElementReader;
      return;
    }
    throw new IllegalArgumentException("Parameters cannot be null");
  }
  
  public void closeSessions()
  {
    if (!mService.isConnected())
    {
      Log.e("OMAPI.Reader", "service is not connected");
      return;
    }
    try
    {
      synchronized (mLock)
      {
        mReader.closeSessions();
      }
    }
    catch (RemoteException localRemoteException) {}
  }
  
  public String getName()
  {
    return mName;
  }
  
  public SEService getSEService()
  {
    return mService;
  }
  
  public boolean isSecureElementPresent()
  {
    if (mService.isConnected()) {
      try
      {
        boolean bool = mReader.isSecureElementPresent();
        return bool;
      }
      catch (RemoteException localRemoteException)
      {
        throw new IllegalStateException("Error in isSecureElementPresent()");
      }
    }
    throw new IllegalStateException("service is not connected");
  }
  
  /* Error */
  public Session openSession()
    throws java.io.IOException
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 26	android/se/omapi/Reader:mService	Landroid/se/omapi/SEService;
    //   4: invokevirtual 45	android/se/omapi/SEService:isConnected	()Z
    //   7: ifeq +92 -> 99
    //   10: aload_0
    //   11: getfield 22	android/se/omapi/Reader:mLock	Ljava/lang/Object;
    //   14: astore_1
    //   15: aload_1
    //   16: monitorenter
    //   17: aload_0
    //   18: getfield 28	android/se/omapi/Reader:mReader	Landroid/se/omapi/ISecureElementReader;
    //   21: invokeinterface 78 1 0
    //   26: astore_2
    //   27: aload_2
    //   28: ifnull +21 -> 49
    //   31: new 80	android/se/omapi/Session
    //   34: astore_3
    //   35: aload_3
    //   36: aload_0
    //   37: getfield 26	android/se/omapi/Reader:mService	Landroid/se/omapi/SEService;
    //   40: aload_2
    //   41: aload_0
    //   42: invokespecial 83	android/se/omapi/Session:<init>	(Landroid/se/omapi/SEService;Landroid/se/omapi/ISecureElementSession;Landroid/se/omapi/Reader;)V
    //   45: aload_1
    //   46: monitorexit
    //   47: aload_3
    //   48: areturn
    //   49: new 73	java/io/IOException
    //   52: astore_2
    //   53: aload_2
    //   54: ldc 85
    //   56: invokespecial 86	java/io/IOException:<init>	(Ljava/lang/String;)V
    //   59: aload_2
    //   60: athrow
    //   61: astore_2
    //   62: goto +33 -> 95
    //   65: astore_2
    //   66: new 66	java/lang/IllegalStateException
    //   69: astore_3
    //   70: aload_3
    //   71: aload_2
    //   72: invokevirtual 89	android/os/RemoteException:getMessage	()Ljava/lang/String;
    //   75: invokespecial 69	java/lang/IllegalStateException:<init>	(Ljava/lang/String;)V
    //   78: aload_3
    //   79: athrow
    //   80: astore_3
    //   81: new 73	java/io/IOException
    //   84: astore_2
    //   85: aload_2
    //   86: aload_3
    //   87: invokevirtual 90	android/os/ServiceSpecificException:getMessage	()Ljava/lang/String;
    //   90: invokespecial 86	java/io/IOException:<init>	(Ljava/lang/String;)V
    //   93: aload_2
    //   94: athrow
    //   95: aload_1
    //   96: monitorexit
    //   97: aload_2
    //   98: athrow
    //   99: new 66	java/lang/IllegalStateException
    //   102: dup
    //   103: ldc 47
    //   105: invokespecial 69	java/lang/IllegalStateException:<init>	(Ljava/lang/String;)V
    //   108: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	109	0	this	Reader
    //   14	82	1	localObject1	Object
    //   26	34	2	localObject2	Object
    //   61	1	2	localObject3	Object
    //   65	7	2	localRemoteException	RemoteException
    //   84	14	2	localIOException	java.io.IOException
    //   34	45	3	localObject4	Object
    //   80	7	3	localServiceSpecificException	android.os.ServiceSpecificException
    // Exception table:
    //   from	to	target	type
    //   17	27	61	finally
    //   31	47	61	finally
    //   49	61	61	finally
    //   66	80	61	finally
    //   81	95	61	finally
    //   95	97	61	finally
    //   17	27	65	android/os/RemoteException
    //   17	27	80	android/os/ServiceSpecificException
  }
}
