package android.os;

import java.net.InetSocketAddress;
import java.util.NoSuchElementException;

public class CommonTimeConfig
{
  public static final int ERROR = -1;
  public static final int ERROR_BAD_VALUE = -4;
  public static final int ERROR_DEAD_OBJECT = -7;
  public static final long INVALID_GROUP_ID = -1L;
  private static final int METHOD_FORCE_NETWORKLESS_MASTER_MODE = 17;
  private static final int METHOD_GET_AUTO_DISABLE = 15;
  private static final int METHOD_GET_CLIENT_SYNC_INTERVAL = 11;
  private static final int METHOD_GET_INTERFACE_BINDING = 7;
  private static final int METHOD_GET_MASTER_ANNOUNCE_INTERVAL = 9;
  private static final int METHOD_GET_MASTER_ELECTION_ENDPOINT = 3;
  private static final int METHOD_GET_MASTER_ELECTION_GROUP_ID = 5;
  private static final int METHOD_GET_MASTER_ELECTION_PRIORITY = 1;
  private static final int METHOD_GET_PANIC_THRESHOLD = 13;
  private static final int METHOD_SET_AUTO_DISABLE = 16;
  private static final int METHOD_SET_CLIENT_SYNC_INTERVAL = 12;
  private static final int METHOD_SET_INTERFACE_BINDING = 8;
  private static final int METHOD_SET_MASTER_ANNOUNCE_INTERVAL = 10;
  private static final int METHOD_SET_MASTER_ELECTION_ENDPOINT = 4;
  private static final int METHOD_SET_MASTER_ELECTION_GROUP_ID = 6;
  private static final int METHOD_SET_MASTER_ELECTION_PRIORITY = 2;
  private static final int METHOD_SET_PANIC_THRESHOLD = 14;
  public static final String SERVICE_NAME = "common_time.config";
  public static final int SUCCESS = 0;
  private IBinder.DeathRecipient mDeathHandler = new IBinder.DeathRecipient()
  {
    public void binderDied()
    {
      synchronized (mListenerLock)
      {
        if (mServerDiedListener != null) {
          mServerDiedListener.onServerDied();
        }
        return;
      }
    }
  };
  private String mInterfaceDesc = "";
  private final Object mListenerLock = new Object();
  private IBinder mRemote = null;
  private OnServerDiedListener mServerDiedListener = null;
  private CommonTimeUtils mUtils;
  
  public CommonTimeConfig()
    throws RemoteException
  {
    if (mRemote != null)
    {
      mInterfaceDesc = mRemote.getInterfaceDescriptor();
      mUtils = new CommonTimeUtils(mRemote, mInterfaceDesc);
      mRemote.linkToDeath(mDeathHandler, 0);
      return;
    }
    throw new RemoteException();
  }
  
  private boolean checkDeadServer()
  {
    boolean bool;
    if ((mRemote != null) && (mUtils != null)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public static CommonTimeConfig create()
  {
    CommonTimeConfig localCommonTimeConfig2;
    try
    {
      CommonTimeConfig localCommonTimeConfig1 = new android/os/CommonTimeConfig;
      localCommonTimeConfig1.<init>();
    }
    catch (RemoteException localRemoteException)
    {
      localCommonTimeConfig2 = null;
    }
    return localCommonTimeConfig2;
  }
  
  private void throwOnDeadServer()
    throws RemoteException
  {
    if (!checkDeadServer()) {
      return;
    }
    throw new RemoteException();
  }
  
  protected void finalize()
    throws Throwable
  {
    release();
  }
  
  /* Error */
  public int forceNetworklessMasterMode()
  {
    // Byte code:
    //   0: invokestatic 144	android/os/Parcel:obtain	()Landroid/os/Parcel;
    //   3: astore_1
    //   4: invokestatic 144	android/os/Parcel:obtain	()Landroid/os/Parcel;
    //   7: astore_2
    //   8: aload_1
    //   9: aload_0
    //   10: getfield 87	android/os/CommonTimeConfig:mInterfaceDesc	Ljava/lang/String;
    //   13: invokevirtual 148	android/os/Parcel:writeInterfaceToken	(Ljava/lang/String;)V
    //   16: aload_0
    //   17: getfield 83	android/os/CommonTimeConfig:mRemote	Landroid/os/IBinder;
    //   20: bipush 17
    //   22: aload_1
    //   23: aload_2
    //   24: iconst_0
    //   25: invokeinterface 152 5 0
    //   30: pop
    //   31: aload_2
    //   32: invokevirtual 155	android/os/Parcel:readInt	()I
    //   35: istore_3
    //   36: aload_2
    //   37: invokevirtual 158	android/os/Parcel:recycle	()V
    //   40: aload_1
    //   41: invokevirtual 158	android/os/Parcel:recycle	()V
    //   44: iload_3
    //   45: ireturn
    //   46: astore 4
    //   48: aload_2
    //   49: invokevirtual 158	android/os/Parcel:recycle	()V
    //   52: aload_1
    //   53: invokevirtual 158	android/os/Parcel:recycle	()V
    //   56: aload 4
    //   58: athrow
    //   59: astore 4
    //   61: aload_2
    //   62: invokevirtual 158	android/os/Parcel:recycle	()V
    //   65: aload_1
    //   66: invokevirtual 158	android/os/Parcel:recycle	()V
    //   69: bipush -7
    //   71: ireturn
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	72	0	this	CommonTimeConfig
    //   3	63	1	localParcel1	Parcel
    //   7	55	2	localParcel2	Parcel
    //   35	10	3	i	int
    //   46	11	4	localObject	Object
    //   59	1	4	localRemoteException	RemoteException
    // Exception table:
    //   from	to	target	type
    //   8	36	46	finally
    //   8	36	59	android/os/RemoteException
  }
  
  public boolean getAutoDisable()
    throws RemoteException
  {
    throwOnDeadServer();
    CommonTimeUtils localCommonTimeUtils = mUtils;
    boolean bool = true;
    if (1 != localCommonTimeUtils.transactGetInt(15, 1)) {
      bool = false;
    }
    return bool;
  }
  
  public int getClientSyncInterval()
    throws RemoteException
  {
    throwOnDeadServer();
    return mUtils.transactGetInt(11, -1);
  }
  
  public String getInterfaceBinding()
    throws RemoteException
  {
    throwOnDeadServer();
    String str = mUtils.transactGetString(7, null);
    if ((str != null) && (str.length() == 0)) {
      return null;
    }
    return str;
  }
  
  public int getMasterAnnounceInterval()
    throws RemoteException
  {
    throwOnDeadServer();
    return mUtils.transactGetInt(9, -1);
  }
  
  public InetSocketAddress getMasterElectionEndpoint()
    throws RemoteException
  {
    throwOnDeadServer();
    return mUtils.transactGetSockaddr(3);
  }
  
  public long getMasterElectionGroupId()
    throws RemoteException
  {
    throwOnDeadServer();
    return mUtils.transactGetLong(5, -1L);
  }
  
  public byte getMasterElectionPriority()
    throws RemoteException
  {
    throwOnDeadServer();
    return (byte)mUtils.transactGetInt(1, -1);
  }
  
  public int getPanicThreshold()
    throws RemoteException
  {
    throwOnDeadServer();
    return mUtils.transactGetInt(13, -1);
  }
  
  public void release()
  {
    if (mRemote != null)
    {
      try
      {
        mRemote.unlinkToDeath(mDeathHandler, 0);
      }
      catch (NoSuchElementException localNoSuchElementException) {}
      mRemote = null;
    }
    mUtils = null;
  }
  
  public int setAutoDisable(boolean paramBoolean)
  {
    if (checkDeadServer()) {
      return -7;
    }
    return mUtils.transactSetInt(16, paramBoolean);
  }
  
  public int setClientSyncInterval(int paramInt)
  {
    if (checkDeadServer()) {
      return -7;
    }
    return mUtils.transactSetInt(12, paramInt);
  }
  
  public int setMasterAnnounceInterval(int paramInt)
  {
    if (checkDeadServer()) {
      return -7;
    }
    return mUtils.transactSetInt(10, paramInt);
  }
  
  public int setMasterElectionEndpoint(InetSocketAddress paramInetSocketAddress)
  {
    if (checkDeadServer()) {
      return -7;
    }
    return mUtils.transactSetSockaddr(4, paramInetSocketAddress);
  }
  
  public int setMasterElectionGroupId(long paramLong)
  {
    if (checkDeadServer()) {
      return -7;
    }
    return mUtils.transactSetLong(6, paramLong);
  }
  
  public int setMasterElectionPriority(byte paramByte)
  {
    if (checkDeadServer()) {
      return -7;
    }
    return mUtils.transactSetInt(2, paramByte);
  }
  
  public int setNetworkBinding(String paramString)
  {
    if (checkDeadServer()) {
      return -7;
    }
    CommonTimeUtils localCommonTimeUtils = mUtils;
    if (paramString == null) {
      paramString = "";
    }
    return localCommonTimeUtils.transactSetString(8, paramString);
  }
  
  public int setPanicThreshold(int paramInt)
  {
    if (checkDeadServer()) {
      return -7;
    }
    return mUtils.transactSetInt(14, paramInt);
  }
  
  public void setServerDiedListener(OnServerDiedListener paramOnServerDiedListener)
  {
    synchronized (mListenerLock)
    {
      mServerDiedListener = paramOnServerDiedListener;
      return;
    }
  }
  
  public static abstract interface OnServerDiedListener
  {
    public abstract void onServerDied();
  }
}
