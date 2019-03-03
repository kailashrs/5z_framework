package android.os;

public abstract interface IStatsCompanionService
  extends IInterface
{
  public abstract void cancelAlarmForSubscriberTriggering()
    throws RemoteException;
  
  public abstract void cancelAnomalyAlarm()
    throws RemoteException;
  
  public abstract void cancelPullingAlarm()
    throws RemoteException;
  
  public abstract StatsLogEventWrapper[] pullData(int paramInt)
    throws RemoteException;
  
  public abstract void sendDataBroadcast(IBinder paramIBinder, long paramLong)
    throws RemoteException;
  
  public abstract void sendSubscriberBroadcast(IBinder paramIBinder, long paramLong1, long paramLong2, long paramLong3, long paramLong4, String[] paramArrayOfString, StatsDimensionsValue paramStatsDimensionsValue)
    throws RemoteException;
  
  public abstract void setAlarmForSubscriberTriggering(long paramLong)
    throws RemoteException;
  
  public abstract void setAnomalyAlarm(long paramLong)
    throws RemoteException;
  
  public abstract void setPullingAlarm(long paramLong)
    throws RemoteException;
  
  public abstract void statsdReady()
    throws RemoteException;
  
  public abstract void triggerUidSnapshot()
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IStatsCompanionService
  {
    private static final String DESCRIPTOR = "android.os.IStatsCompanionService";
    static final int TRANSACTION_cancelAlarmForSubscriberTriggering = 7;
    static final int TRANSACTION_cancelAnomalyAlarm = 3;
    static final int TRANSACTION_cancelPullingAlarm = 5;
    static final int TRANSACTION_pullData = 8;
    static final int TRANSACTION_sendDataBroadcast = 9;
    static final int TRANSACTION_sendSubscriberBroadcast = 10;
    static final int TRANSACTION_setAlarmForSubscriberTriggering = 6;
    static final int TRANSACTION_setAnomalyAlarm = 2;
    static final int TRANSACTION_setPullingAlarm = 4;
    static final int TRANSACTION_statsdReady = 1;
    static final int TRANSACTION_triggerUidSnapshot = 11;
    
    public Stub()
    {
      attachInterface(this, "android.os.IStatsCompanionService");
    }
    
    public static IStatsCompanionService asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.os.IStatsCompanionService");
      if ((localIInterface != null) && ((localIInterface instanceof IStatsCompanionService))) {
        return (IStatsCompanionService)localIInterface;
      }
      return new Proxy(paramIBinder);
    }
    
    public IBinder asBinder()
    {
      return this;
    }
    
    public boolean onTransact(int paramInt1, Parcel paramParcel1, Parcel paramParcel2, int paramInt2)
      throws RemoteException
    {
      if (paramInt1 != 1598968902)
      {
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 11: 
          paramParcel1.enforceInterface("android.os.IStatsCompanionService");
          triggerUidSnapshot();
          return true;
        case 10: 
          paramParcel1.enforceInterface("android.os.IStatsCompanionService");
          paramParcel2 = paramParcel1.readStrongBinder();
          long l1 = paramParcel1.readLong();
          long l2 = paramParcel1.readLong();
          long l3 = paramParcel1.readLong();
          long l4 = paramParcel1.readLong();
          String[] arrayOfString = paramParcel1.createStringArray();
          if (paramParcel1.readInt() != 0) {}
          for (paramParcel1 = (StatsDimensionsValue)StatsDimensionsValue.CREATOR.createFromParcel(paramParcel1);; paramParcel1 = null) {
            break;
          }
          sendSubscriberBroadcast(paramParcel2, l1, l2, l3, l4, arrayOfString, paramParcel1);
          return true;
        case 9: 
          paramParcel1.enforceInterface("android.os.IStatsCompanionService");
          sendDataBroadcast(paramParcel1.readStrongBinder(), paramParcel1.readLong());
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.os.IStatsCompanionService");
          paramParcel1 = pullData(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeTypedArray(paramParcel1, 1);
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.os.IStatsCompanionService");
          cancelAlarmForSubscriberTriggering();
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.os.IStatsCompanionService");
          setAlarmForSubscriberTriggering(paramParcel1.readLong());
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.os.IStatsCompanionService");
          cancelPullingAlarm();
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.os.IStatsCompanionService");
          setPullingAlarm(paramParcel1.readLong());
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.os.IStatsCompanionService");
          cancelAnomalyAlarm();
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.os.IStatsCompanionService");
          setAnomalyAlarm(paramParcel1.readLong());
          return true;
        }
        paramParcel1.enforceInterface("android.os.IStatsCompanionService");
        statsdReady();
        return true;
      }
      paramParcel2.writeString("android.os.IStatsCompanionService");
      return true;
    }
    
    private static class Proxy
      implements IStatsCompanionService
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public IBinder asBinder()
      {
        return mRemote;
      }
      
      public void cancelAlarmForSubscriberTriggering()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.os.IStatsCompanionService");
          mRemote.transact(7, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void cancelAnomalyAlarm()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.os.IStatsCompanionService");
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void cancelPullingAlarm()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.os.IStatsCompanionService");
          mRemote.transact(5, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.os.IStatsCompanionService";
      }
      
      public StatsLogEventWrapper[] pullData(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IStatsCompanionService");
          localParcel1.writeInt(paramInt);
          mRemote.transact(8, localParcel1, localParcel2, 0);
          localParcel2.readException();
          StatsLogEventWrapper[] arrayOfStatsLogEventWrapper = (StatsLogEventWrapper[])localParcel2.createTypedArray(StatsLogEventWrapper.CREATOR);
          return arrayOfStatsLogEventWrapper;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void sendDataBroadcast(IBinder paramIBinder, long paramLong)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.os.IStatsCompanionService");
          localParcel.writeStrongBinder(paramIBinder);
          localParcel.writeLong(paramLong);
          mRemote.transact(9, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      /* Error */
      public void sendSubscriberBroadcast(IBinder paramIBinder, long paramLong1, long paramLong2, long paramLong3, long paramLong4, String[] paramArrayOfString, StatsDimensionsValue paramStatsDimensionsValue)
        throws RemoteException
      {
        // Byte code:
        //   0: invokestatic 31	android/os/Parcel:obtain	()Landroid/os/Parcel;
        //   3: astore 12
        //   5: aload 12
        //   7: ldc 33
        //   9: invokevirtual 37	android/os/Parcel:writeInterfaceToken	(Ljava/lang/String;)V
        //   12: aload 12
        //   14: aload_1
        //   15: invokevirtual 77	android/os/Parcel:writeStrongBinder	(Landroid/os/IBinder;)V
        //   18: aload 12
        //   20: lload_2
        //   21: invokevirtual 81	android/os/Parcel:writeLong	(J)V
        //   24: aload 12
        //   26: lload 4
        //   28: invokevirtual 81	android/os/Parcel:writeLong	(J)V
        //   31: aload 12
        //   33: lload 6
        //   35: invokevirtual 81	android/os/Parcel:writeLong	(J)V
        //   38: aload 12
        //   40: lload 8
        //   42: invokevirtual 81	android/os/Parcel:writeLong	(J)V
        //   45: aload 12
        //   47: aload 10
        //   49: invokevirtual 87	android/os/Parcel:writeStringArray	([Ljava/lang/String;)V
        //   52: aload 11
        //   54: ifnull +20 -> 74
        //   57: aload 12
        //   59: iconst_1
        //   60: invokevirtual 57	android/os/Parcel:writeInt	(I)V
        //   63: aload 11
        //   65: aload 12
        //   67: iconst_0
        //   68: invokevirtual 93	android/os/StatsDimensionsValue:writeToParcel	(Landroid/os/Parcel;I)V
        //   71: goto +9 -> 80
        //   74: aload 12
        //   76: iconst_0
        //   77: invokevirtual 57	android/os/Parcel:writeInt	(I)V
        //   80: aload_0
        //   81: getfield 19	android/os/IStatsCompanionService$Stub$Proxy:mRemote	Landroid/os/IBinder;
        //   84: bipush 10
        //   86: aload 12
        //   88: aconst_null
        //   89: iconst_1
        //   90: invokeinterface 43 5 0
        //   95: pop
        //   96: aload 12
        //   98: invokevirtual 46	android/os/Parcel:recycle	()V
        //   101: return
        //   102: astore_1
        //   103: goto +28 -> 131
        //   106: astore_1
        //   107: goto +24 -> 131
        //   110: astore_1
        //   111: goto +20 -> 131
        //   114: astore_1
        //   115: goto +16 -> 131
        //   118: astore_1
        //   119: goto +12 -> 131
        //   122: astore_1
        //   123: goto +8 -> 131
        //   126: astore_1
        //   127: goto +4 -> 131
        //   130: astore_1
        //   131: aload 12
        //   133: invokevirtual 46	android/os/Parcel:recycle	()V
        //   136: aload_1
        //   137: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	138	0	this	Proxy
        //   0	138	1	paramIBinder	IBinder
        //   0	138	2	paramLong1	long
        //   0	138	4	paramLong2	long
        //   0	138	6	paramLong3	long
        //   0	138	8	paramLong4	long
        //   0	138	10	paramArrayOfString	String[]
        //   0	138	11	paramStatsDimensionsValue	StatsDimensionsValue
        //   3	129	12	localParcel	Parcel
        // Exception table:
        //   from	to	target	type
        //   80	96	102	finally
        //   45	52	106	finally
        //   57	71	106	finally
        //   74	80	106	finally
        //   38	45	110	finally
        //   31	38	114	finally
        //   24	31	118	finally
        //   18	24	122	finally
        //   12	18	126	finally
        //   5	12	130	finally
      }
      
      public void setAlarmForSubscriberTriggering(long paramLong)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.os.IStatsCompanionService");
          localParcel.writeLong(paramLong);
          mRemote.transact(6, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void setAnomalyAlarm(long paramLong)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.os.IStatsCompanionService");
          localParcel.writeLong(paramLong);
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void setPullingAlarm(long paramLong)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.os.IStatsCompanionService");
          localParcel.writeLong(paramLong);
          mRemote.transact(4, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void statsdReady()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.os.IStatsCompanionService");
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void triggerUidSnapshot()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.os.IStatsCompanionService");
          mRemote.transact(11, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
    }
  }
}
