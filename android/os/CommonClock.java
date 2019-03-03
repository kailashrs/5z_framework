package android.os;

import java.net.InetSocketAddress;
import java.util.NoSuchElementException;

public class CommonClock
{
  public static final int ERROR_ESTIMATE_UNKNOWN = Integer.MAX_VALUE;
  public static final long INVALID_TIMELINE_ID = 0L;
  private static final int METHOD_CBK_ON_TIMELINE_CHANGED = 1;
  private static final int METHOD_COMMON_TIME_TO_LOCAL_TIME = 2;
  private static final int METHOD_GET_COMMON_FREQ = 5;
  private static final int METHOD_GET_COMMON_TIME = 4;
  private static final int METHOD_GET_ESTIMATED_ERROR = 8;
  private static final int METHOD_GET_LOCAL_FREQ = 7;
  private static final int METHOD_GET_LOCAL_TIME = 6;
  private static final int METHOD_GET_MASTER_ADDRESS = 11;
  private static final int METHOD_GET_STATE = 10;
  private static final int METHOD_GET_TIMELINE_ID = 9;
  private static final int METHOD_IS_COMMON_TIME_VALID = 1;
  private static final int METHOD_LOCAL_TIME_TO_COMMON_TIME = 3;
  private static final int METHOD_REGISTER_LISTENER = 12;
  private static final int METHOD_UNREGISTER_LISTENER = 13;
  public static final String SERVICE_NAME = "common_time.clock";
  public static final int STATE_CLIENT = 1;
  public static final int STATE_INITIAL = 0;
  public static final int STATE_INVALID = -1;
  public static final int STATE_MASTER = 2;
  public static final int STATE_RONIN = 3;
  public static final int STATE_WAIT_FOR_ELECTION = 4;
  public static final long TIME_NOT_SYNCED = -1L;
  private TimelineChangedListener mCallbackTgt = null;
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
  private OnTimelineChangedListener mTimelineChangedListener = null;
  private CommonTimeUtils mUtils;
  
  public CommonClock()
    throws RemoteException
  {
    if (mRemote != null)
    {
      mInterfaceDesc = mRemote.getInterfaceDescriptor();
      mUtils = new CommonTimeUtils(mRemote, mInterfaceDesc);
      mRemote.linkToDeath(mDeathHandler, 0);
      registerTimelineChangeListener();
      return;
    }
    throw new RemoteException();
  }
  
  public static CommonClock create()
  {
    CommonClock localCommonClock2;
    try
    {
      CommonClock localCommonClock1 = new android/os/CommonClock;
      localCommonClock1.<init>();
    }
    catch (RemoteException localRemoteException)
    {
      localCommonClock2 = null;
    }
    return localCommonClock2;
  }
  
  /* Error */
  private void registerTimelineChangeListener()
    throws RemoteException
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 104	android/os/CommonClock:mCallbackTgt	Landroid/os/CommonClock$TimelineChangedListener;
    //   4: ifnull +4 -> 8
    //   7: return
    //   8: invokestatic 148	android/os/Parcel:obtain	()Landroid/os/Parcel;
    //   11: astore_1
    //   12: invokestatic 148	android/os/Parcel:obtain	()Landroid/os/Parcel;
    //   15: astore_2
    //   16: aload_0
    //   17: new 14	android/os/CommonClock$TimelineChangedListener
    //   20: dup
    //   21: aload_0
    //   22: aconst_null
    //   23: invokespecial 151	android/os/CommonClock$TimelineChangedListener:<init>	(Landroid/os/CommonClock;Landroid/os/CommonClock$1;)V
    //   26: putfield 104	android/os/CommonClock:mCallbackTgt	Landroid/os/CommonClock$TimelineChangedListener;
    //   29: aload_1
    //   30: aload_0
    //   31: getfield 97	android/os/CommonClock:mInterfaceDesc	Ljava/lang/String;
    //   34: invokevirtual 155	android/os/Parcel:writeInterfaceToken	(Ljava/lang/String;)V
    //   37: aload_1
    //   38: aload_0
    //   39: getfield 104	android/os/CommonClock:mCallbackTgt	Landroid/os/CommonClock$TimelineChangedListener;
    //   42: invokevirtual 159	android/os/Parcel:writeStrongBinder	(Landroid/os/IBinder;)V
    //   45: aload_0
    //   46: getfield 93	android/os/CommonClock:mRemote	Landroid/os/IBinder;
    //   49: astore_3
    //   50: iconst_0
    //   51: istore 4
    //   53: aload_3
    //   54: bipush 12
    //   56: aload_1
    //   57: aload_2
    //   58: iconst_0
    //   59: invokeinterface 163 5 0
    //   64: pop
    //   65: aload_2
    //   66: invokevirtual 167	android/os/Parcel:readInt	()I
    //   69: istore 5
    //   71: iload 5
    //   73: ifne +6 -> 79
    //   76: iconst_1
    //   77: istore 4
    //   79: goto +18 -> 97
    //   82: astore_3
    //   83: aload_2
    //   84: invokevirtual 170	android/os/Parcel:recycle	()V
    //   87: aload_1
    //   88: invokevirtual 170	android/os/Parcel:recycle	()V
    //   91: aload_3
    //   92: athrow
    //   93: astore_3
    //   94: iconst_0
    //   95: istore 4
    //   97: aload_2
    //   98: invokevirtual 170	android/os/Parcel:recycle	()V
    //   101: aload_1
    //   102: invokevirtual 170	android/os/Parcel:recycle	()V
    //   105: iload 4
    //   107: ifne +18 -> 125
    //   110: aload_0
    //   111: aconst_null
    //   112: putfield 104	android/os/CommonClock:mCallbackTgt	Landroid/os/CommonClock$TimelineChangedListener;
    //   115: aload_0
    //   116: aconst_null
    //   117: putfield 93	android/os/CommonClock:mRemote	Landroid/os/IBinder;
    //   120: aload_0
    //   121: aconst_null
    //   122: putfield 123	android/os/CommonClock:mUtils	Landroid/os/CommonTimeUtils;
    //   125: return
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	126	0	this	CommonClock
    //   11	91	1	localParcel1	Parcel
    //   15	83	2	localParcel2	Parcel
    //   49	5	3	localIBinder	IBinder
    //   82	10	3	localObject	Object
    //   93	1	3	localRemoteException	RemoteException
    //   51	55	4	i	int
    //   69	3	5	j	int
    // Exception table:
    //   from	to	target	type
    //   29	50	82	finally
    //   53	71	82	finally
    //   29	50	93	android/os/RemoteException
    //   53	71	93	android/os/RemoteException
  }
  
  private void throwOnDeadServer()
    throws RemoteException
  {
    if ((mRemote != null) && (mUtils != null)) {
      return;
    }
    throw new RemoteException();
  }
  
  /* Error */
  private void unregisterTimelineChangeListener()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 104	android/os/CommonClock:mCallbackTgt	Landroid/os/CommonClock$TimelineChangedListener;
    //   4: ifnonnull +4 -> 8
    //   7: return
    //   8: invokestatic 148	android/os/Parcel:obtain	()Landroid/os/Parcel;
    //   11: astore_1
    //   12: invokestatic 148	android/os/Parcel:obtain	()Landroid/os/Parcel;
    //   15: astore_2
    //   16: aload_1
    //   17: aload_0
    //   18: getfield 97	android/os/CommonClock:mInterfaceDesc	Ljava/lang/String;
    //   21: invokevirtual 155	android/os/Parcel:writeInterfaceToken	(Ljava/lang/String;)V
    //   24: aload_1
    //   25: aload_0
    //   26: getfield 104	android/os/CommonClock:mCallbackTgt	Landroid/os/CommonClock$TimelineChangedListener;
    //   29: invokevirtual 159	android/os/Parcel:writeStrongBinder	(Landroid/os/IBinder;)V
    //   32: aload_0
    //   33: getfield 93	android/os/CommonClock:mRemote	Landroid/os/IBinder;
    //   36: bipush 13
    //   38: aload_1
    //   39: aload_2
    //   40: iconst_0
    //   41: invokeinterface 163 5 0
    //   46: pop
    //   47: goto +20 -> 67
    //   50: astore_3
    //   51: aload_2
    //   52: invokevirtual 170	android/os/Parcel:recycle	()V
    //   55: aload_1
    //   56: invokevirtual 170	android/os/Parcel:recycle	()V
    //   59: aload_0
    //   60: aconst_null
    //   61: putfield 104	android/os/CommonClock:mCallbackTgt	Landroid/os/CommonClock$TimelineChangedListener;
    //   64: aload_3
    //   65: athrow
    //   66: astore_3
    //   67: aload_2
    //   68: invokevirtual 170	android/os/Parcel:recycle	()V
    //   71: aload_1
    //   72: invokevirtual 170	android/os/Parcel:recycle	()V
    //   75: aload_0
    //   76: aconst_null
    //   77: putfield 104	android/os/CommonClock:mCallbackTgt	Landroid/os/CommonClock$TimelineChangedListener;
    //   80: return
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	81	0	this	CommonClock
    //   11	61	1	localParcel1	Parcel
    //   15	53	2	localParcel2	Parcel
    //   50	15	3	localObject	Object
    //   66	1	3	localRemoteException	RemoteException
    // Exception table:
    //   from	to	target	type
    //   16	47	50	finally
    //   16	47	66	android/os/RemoteException
  }
  
  protected void finalize()
    throws Throwable
  {
    release();
  }
  
  public int getEstimatedError()
    throws RemoteException
  {
    throwOnDeadServer();
    return mUtils.transactGetInt(8, Integer.MAX_VALUE);
  }
  
  public InetSocketAddress getMasterAddr()
    throws RemoteException
  {
    throwOnDeadServer();
    return mUtils.transactGetSockaddr(11);
  }
  
  public int getState()
    throws RemoteException
  {
    throwOnDeadServer();
    return mUtils.transactGetInt(10, -1);
  }
  
  public long getTime()
    throws RemoteException
  {
    throwOnDeadServer();
    return mUtils.transactGetLong(4, -1L);
  }
  
  public long getTimelineId()
    throws RemoteException
  {
    throwOnDeadServer();
    return mUtils.transactGetLong(9, 0L);
  }
  
  public void release()
  {
    unregisterTimelineChangeListener();
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
  
  public void setServerDiedListener(OnServerDiedListener paramOnServerDiedListener)
  {
    synchronized (mListenerLock)
    {
      mServerDiedListener = paramOnServerDiedListener;
      return;
    }
  }
  
  public void setTimelineChangedListener(OnTimelineChangedListener paramOnTimelineChangedListener)
  {
    synchronized (mListenerLock)
    {
      mTimelineChangedListener = paramOnTimelineChangedListener;
      return;
    }
  }
  
  public static abstract interface OnServerDiedListener
  {
    public abstract void onServerDied();
  }
  
  public static abstract interface OnTimelineChangedListener
  {
    public abstract void onTimelineChanged(long paramLong);
  }
  
  private class TimelineChangedListener
    extends Binder
  {
    private static final String DESCRIPTOR = "android.os.ICommonClockListener";
    
    private TimelineChangedListener() {}
    
    protected boolean onTransact(int paramInt1, Parcel arg2, Parcel paramParcel2, int paramInt2)
      throws RemoteException
    {
      if (paramInt1 != 1) {
        return super.onTransact(paramInt1, ???, paramParcel2, paramInt2);
      }
      ???.enforceInterface("android.os.ICommonClockListener");
      long l = ???.readLong();
      synchronized (mListenerLock)
      {
        if (mTimelineChangedListener != null) {
          mTimelineChangedListener.onTimelineChanged(l);
        }
        return true;
      }
    }
  }
}
