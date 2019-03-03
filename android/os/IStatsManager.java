package android.os;

public abstract interface IStatsManager
  extends IInterface
{
  public abstract void addConfiguration(long paramLong, byte[] paramArrayOfByte, String paramString)
    throws RemoteException;
  
  public abstract byte[] getData(long paramLong, String paramString)
    throws RemoteException;
  
  public abstract byte[] getMetadata(String paramString)
    throws RemoteException;
  
  public abstract void informAlarmForSubscriberTriggeringFired()
    throws RemoteException;
  
  public abstract void informAllUidData(int[] paramArrayOfInt, long[] paramArrayOfLong, String[] paramArrayOfString)
    throws RemoteException;
  
  public abstract void informAnomalyAlarmFired()
    throws RemoteException;
  
  public abstract void informDeviceShutdown()
    throws RemoteException;
  
  public abstract void informOnePackage(String paramString, int paramInt, long paramLong)
    throws RemoteException;
  
  public abstract void informOnePackageRemoved(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract void informPollAlarmFired()
    throws RemoteException;
  
  public abstract void removeConfiguration(long paramLong, String paramString)
    throws RemoteException;
  
  public abstract void removeDataFetchOperation(long paramLong, String paramString)
    throws RemoteException;
  
  public abstract void sendAppBreadcrumbAtom(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void setBroadcastSubscriber(long paramLong1, long paramLong2, IBinder paramIBinder, String paramString)
    throws RemoteException;
  
  public abstract void setDataFetchOperation(long paramLong, IBinder paramIBinder, String paramString)
    throws RemoteException;
  
  public abstract void statsCompanionReady()
    throws RemoteException;
  
  public abstract void systemRunning()
    throws RemoteException;
  
  public abstract void unsetBroadcastSubscriber(long paramLong1, long paramLong2, String paramString)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IStatsManager
  {
    private static final String DESCRIPTOR = "android.os.IStatsManager";
    static final int TRANSACTION_addConfiguration = 12;
    static final int TRANSACTION_getData = 10;
    static final int TRANSACTION_getMetadata = 11;
    static final int TRANSACTION_informAlarmForSubscriberTriggeringFired = 5;
    static final int TRANSACTION_informAllUidData = 7;
    static final int TRANSACTION_informAnomalyAlarmFired = 3;
    static final int TRANSACTION_informDeviceShutdown = 6;
    static final int TRANSACTION_informOnePackage = 8;
    static final int TRANSACTION_informOnePackageRemoved = 9;
    static final int TRANSACTION_informPollAlarmFired = 4;
    static final int TRANSACTION_removeConfiguration = 15;
    static final int TRANSACTION_removeDataFetchOperation = 14;
    static final int TRANSACTION_sendAppBreadcrumbAtom = 18;
    static final int TRANSACTION_setBroadcastSubscriber = 16;
    static final int TRANSACTION_setDataFetchOperation = 13;
    static final int TRANSACTION_statsCompanionReady = 2;
    static final int TRANSACTION_systemRunning = 1;
    static final int TRANSACTION_unsetBroadcastSubscriber = 17;
    
    public Stub()
    {
      attachInterface(this, "android.os.IStatsManager");
    }
    
    public static IStatsManager asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.os.IStatsManager");
      if ((localIInterface != null) && ((localIInterface instanceof IStatsManager))) {
        return (IStatsManager)localIInterface;
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
        case 18: 
          paramParcel1.enforceInterface("android.os.IStatsManager");
          sendAppBreadcrumbAtom(paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 17: 
          paramParcel1.enforceInterface("android.os.IStatsManager");
          unsetBroadcastSubscriber(paramParcel1.readLong(), paramParcel1.readLong(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 16: 
          paramParcel1.enforceInterface("android.os.IStatsManager");
          setBroadcastSubscriber(paramParcel1.readLong(), paramParcel1.readLong(), paramParcel1.readStrongBinder(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 15: 
          paramParcel1.enforceInterface("android.os.IStatsManager");
          removeConfiguration(paramParcel1.readLong(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 14: 
          paramParcel1.enforceInterface("android.os.IStatsManager");
          removeDataFetchOperation(paramParcel1.readLong(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 13: 
          paramParcel1.enforceInterface("android.os.IStatsManager");
          setDataFetchOperation(paramParcel1.readLong(), paramParcel1.readStrongBinder(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 12: 
          paramParcel1.enforceInterface("android.os.IStatsManager");
          addConfiguration(paramParcel1.readLong(), paramParcel1.createByteArray(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 11: 
          paramParcel1.enforceInterface("android.os.IStatsManager");
          paramParcel1 = getMetadata(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeByteArray(paramParcel1);
          return true;
        case 10: 
          paramParcel1.enforceInterface("android.os.IStatsManager");
          paramParcel1 = getData(paramParcel1.readLong(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeByteArray(paramParcel1);
          return true;
        case 9: 
          paramParcel1.enforceInterface("android.os.IStatsManager");
          informOnePackageRemoved(paramParcel1.readString(), paramParcel1.readInt());
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.os.IStatsManager");
          informOnePackage(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readLong());
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.os.IStatsManager");
          informAllUidData(paramParcel1.createIntArray(), paramParcel1.createLongArray(), paramParcel1.createStringArray());
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.os.IStatsManager");
          informDeviceShutdown();
          paramParcel2.writeNoException();
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.os.IStatsManager");
          informAlarmForSubscriberTriggeringFired();
          paramParcel2.writeNoException();
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.os.IStatsManager");
          informPollAlarmFired();
          paramParcel2.writeNoException();
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.os.IStatsManager");
          informAnomalyAlarmFired();
          paramParcel2.writeNoException();
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.os.IStatsManager");
          statsCompanionReady();
          paramParcel2.writeNoException();
          return true;
        }
        paramParcel1.enforceInterface("android.os.IStatsManager");
        systemRunning();
        return true;
      }
      paramParcel2.writeString("android.os.IStatsManager");
      return true;
    }
    
    private static class Proxy
      implements IStatsManager
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public void addConfiguration(long paramLong, byte[] paramArrayOfByte, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IStatsManager");
          localParcel1.writeLong(paramLong);
          localParcel1.writeByteArray(paramArrayOfByte);
          localParcel1.writeString(paramString);
          mRemote.transact(12, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public IBinder asBinder()
      {
        return mRemote;
      }
      
      public byte[] getData(long paramLong, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IStatsManager");
          localParcel1.writeLong(paramLong);
          localParcel1.writeString(paramString);
          mRemote.transact(10, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = localParcel2.createByteArray();
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.os.IStatsManager";
      }
      
      public byte[] getMetadata(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IStatsManager");
          localParcel1.writeString(paramString);
          mRemote.transact(11, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = localParcel2.createByteArray();
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void informAlarmForSubscriberTriggeringFired()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IStatsManager");
          mRemote.transact(5, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void informAllUidData(int[] paramArrayOfInt, long[] paramArrayOfLong, String[] paramArrayOfString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.os.IStatsManager");
          localParcel.writeIntArray(paramArrayOfInt);
          localParcel.writeLongArray(paramArrayOfLong);
          localParcel.writeStringArray(paramArrayOfString);
          mRemote.transact(7, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void informAnomalyAlarmFired()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IStatsManager");
          mRemote.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void informDeviceShutdown()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IStatsManager");
          mRemote.transact(6, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void informOnePackage(String paramString, int paramInt, long paramLong)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.os.IStatsManager");
          localParcel.writeString(paramString);
          localParcel.writeInt(paramInt);
          localParcel.writeLong(paramLong);
          mRemote.transact(8, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void informOnePackageRemoved(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.os.IStatsManager");
          localParcel.writeString(paramString);
          localParcel.writeInt(paramInt);
          mRemote.transact(9, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void informPollAlarmFired()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IStatsManager");
          mRemote.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void removeConfiguration(long paramLong, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IStatsManager");
          localParcel1.writeLong(paramLong);
          localParcel1.writeString(paramString);
          mRemote.transact(15, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void removeDataFetchOperation(long paramLong, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IStatsManager");
          localParcel1.writeLong(paramLong);
          localParcel1.writeString(paramString);
          mRemote.transact(14, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void sendAppBreadcrumbAtom(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IStatsManager");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(18, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setBroadcastSubscriber(long paramLong1, long paramLong2, IBinder paramIBinder, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IStatsManager");
          localParcel1.writeLong(paramLong1);
          localParcel1.writeLong(paramLong2);
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeString(paramString);
          mRemote.transact(16, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setDataFetchOperation(long paramLong, IBinder paramIBinder, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IStatsManager");
          localParcel1.writeLong(paramLong);
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeString(paramString);
          mRemote.transact(13, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void statsCompanionReady()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IStatsManager");
          mRemote.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void systemRunning()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.os.IStatsManager");
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void unsetBroadcastSubscriber(long paramLong1, long paramLong2, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IStatsManager");
          localParcel1.writeLong(paramLong1);
          localParcel1.writeLong(paramLong2);
          localParcel1.writeString(paramString);
          mRemote.transact(17, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
    }
  }
}
