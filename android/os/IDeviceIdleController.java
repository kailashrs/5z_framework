package android.os;

public abstract interface IDeviceIdleController
  extends IInterface
{
  public abstract void addPowerSaveTempWhitelistApp(String paramString1, long paramLong, int paramInt, String paramString2)
    throws RemoteException;
  
  public abstract long addPowerSaveTempWhitelistAppForMms(String paramString1, int paramInt, String paramString2)
    throws RemoteException;
  
  public abstract long addPowerSaveTempWhitelistAppForSms(String paramString1, int paramInt, String paramString2)
    throws RemoteException;
  
  public abstract void addPowerSaveWhitelistApp(String paramString)
    throws RemoteException;
  
  public abstract void exitIdle(String paramString)
    throws RemoteException;
  
  public abstract int[] getAppIdTempWhitelist()
    throws RemoteException;
  
  public abstract int[] getAppIdUserWhitelist()
    throws RemoteException;
  
  public abstract int[] getAppIdWhitelist()
    throws RemoteException;
  
  public abstract int[] getAppIdWhitelistExceptIdle()
    throws RemoteException;
  
  public abstract String[] getFullPowerWhitelist()
    throws RemoteException;
  
  public abstract String[] getFullPowerWhitelistExceptIdle()
    throws RemoteException;
  
  public abstract String[] getRemovedSystemPowerWhitelistApps()
    throws RemoteException;
  
  public abstract String[] getSystemPowerWhitelist()
    throws RemoteException;
  
  public abstract String[] getSystemPowerWhitelistExceptIdle()
    throws RemoteException;
  
  public abstract String[] getUserPowerWhitelist()
    throws RemoteException;
  
  public abstract boolean isPowerSaveWhitelistApp(String paramString)
    throws RemoteException;
  
  public abstract boolean isPowerSaveWhitelistExceptIdleApp(String paramString)
    throws RemoteException;
  
  public abstract boolean registerMaintenanceActivityListener(IMaintenanceActivityListener paramIMaintenanceActivityListener)
    throws RemoteException;
  
  public abstract void removePowerSaveWhitelistApp(String paramString)
    throws RemoteException;
  
  public abstract void removeSystemPowerWhitelistApp(String paramString)
    throws RemoteException;
  
  public abstract void restoreSystemPowerWhitelistApp(String paramString)
    throws RemoteException;
  
  public abstract void unregisterMaintenanceActivityListener(IMaintenanceActivityListener paramIMaintenanceActivityListener)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IDeviceIdleController
  {
    private static final String DESCRIPTOR = "android.os.IDeviceIdleController";
    static final int TRANSACTION_addPowerSaveTempWhitelistApp = 17;
    static final int TRANSACTION_addPowerSaveTempWhitelistAppForMms = 18;
    static final int TRANSACTION_addPowerSaveTempWhitelistAppForSms = 19;
    static final int TRANSACTION_addPowerSaveWhitelistApp = 1;
    static final int TRANSACTION_exitIdle = 20;
    static final int TRANSACTION_getAppIdTempWhitelist = 14;
    static final int TRANSACTION_getAppIdUserWhitelist = 13;
    static final int TRANSACTION_getAppIdWhitelist = 12;
    static final int TRANSACTION_getAppIdWhitelistExceptIdle = 11;
    static final int TRANSACTION_getFullPowerWhitelist = 10;
    static final int TRANSACTION_getFullPowerWhitelistExceptIdle = 9;
    static final int TRANSACTION_getRemovedSystemPowerWhitelistApps = 5;
    static final int TRANSACTION_getSystemPowerWhitelist = 7;
    static final int TRANSACTION_getSystemPowerWhitelistExceptIdle = 6;
    static final int TRANSACTION_getUserPowerWhitelist = 8;
    static final int TRANSACTION_isPowerSaveWhitelistApp = 16;
    static final int TRANSACTION_isPowerSaveWhitelistExceptIdleApp = 15;
    static final int TRANSACTION_registerMaintenanceActivityListener = 21;
    static final int TRANSACTION_removePowerSaveWhitelistApp = 2;
    static final int TRANSACTION_removeSystemPowerWhitelistApp = 3;
    static final int TRANSACTION_restoreSystemPowerWhitelistApp = 4;
    static final int TRANSACTION_unregisterMaintenanceActivityListener = 22;
    
    public Stub()
    {
      attachInterface(this, "android.os.IDeviceIdleController");
    }
    
    public static IDeviceIdleController asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.os.IDeviceIdleController");
      if ((localIInterface != null) && ((localIInterface instanceof IDeviceIdleController))) {
        return (IDeviceIdleController)localIInterface;
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
        long l;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 22: 
          paramParcel1.enforceInterface("android.os.IDeviceIdleController");
          unregisterMaintenanceActivityListener(IMaintenanceActivityListener.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 21: 
          paramParcel1.enforceInterface("android.os.IDeviceIdleController");
          paramInt1 = registerMaintenanceActivityListener(IMaintenanceActivityListener.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 20: 
          paramParcel1.enforceInterface("android.os.IDeviceIdleController");
          exitIdle(paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 19: 
          paramParcel1.enforceInterface("android.os.IDeviceIdleController");
          l = addPowerSaveTempWhitelistAppForSms(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeLong(l);
          return true;
        case 18: 
          paramParcel1.enforceInterface("android.os.IDeviceIdleController");
          l = addPowerSaveTempWhitelistAppForMms(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeLong(l);
          return true;
        case 17: 
          paramParcel1.enforceInterface("android.os.IDeviceIdleController");
          addPowerSaveTempWhitelistApp(paramParcel1.readString(), paramParcel1.readLong(), paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 16: 
          paramParcel1.enforceInterface("android.os.IDeviceIdleController");
          paramInt1 = isPowerSaveWhitelistApp(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 15: 
          paramParcel1.enforceInterface("android.os.IDeviceIdleController");
          paramInt1 = isPowerSaveWhitelistExceptIdleApp(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 14: 
          paramParcel1.enforceInterface("android.os.IDeviceIdleController");
          paramParcel1 = getAppIdTempWhitelist();
          paramParcel2.writeNoException();
          paramParcel2.writeIntArray(paramParcel1);
          return true;
        case 13: 
          paramParcel1.enforceInterface("android.os.IDeviceIdleController");
          paramParcel1 = getAppIdUserWhitelist();
          paramParcel2.writeNoException();
          paramParcel2.writeIntArray(paramParcel1);
          return true;
        case 12: 
          paramParcel1.enforceInterface("android.os.IDeviceIdleController");
          paramParcel1 = getAppIdWhitelist();
          paramParcel2.writeNoException();
          paramParcel2.writeIntArray(paramParcel1);
          return true;
        case 11: 
          paramParcel1.enforceInterface("android.os.IDeviceIdleController");
          paramParcel1 = getAppIdWhitelistExceptIdle();
          paramParcel2.writeNoException();
          paramParcel2.writeIntArray(paramParcel1);
          return true;
        case 10: 
          paramParcel1.enforceInterface("android.os.IDeviceIdleController");
          paramParcel1 = getFullPowerWhitelist();
          paramParcel2.writeNoException();
          paramParcel2.writeStringArray(paramParcel1);
          return true;
        case 9: 
          paramParcel1.enforceInterface("android.os.IDeviceIdleController");
          paramParcel1 = getFullPowerWhitelistExceptIdle();
          paramParcel2.writeNoException();
          paramParcel2.writeStringArray(paramParcel1);
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.os.IDeviceIdleController");
          paramParcel1 = getUserPowerWhitelist();
          paramParcel2.writeNoException();
          paramParcel2.writeStringArray(paramParcel1);
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.os.IDeviceIdleController");
          paramParcel1 = getSystemPowerWhitelist();
          paramParcel2.writeNoException();
          paramParcel2.writeStringArray(paramParcel1);
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.os.IDeviceIdleController");
          paramParcel1 = getSystemPowerWhitelistExceptIdle();
          paramParcel2.writeNoException();
          paramParcel2.writeStringArray(paramParcel1);
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.os.IDeviceIdleController");
          paramParcel1 = getRemovedSystemPowerWhitelistApps();
          paramParcel2.writeNoException();
          paramParcel2.writeStringArray(paramParcel1);
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.os.IDeviceIdleController");
          restoreSystemPowerWhitelistApp(paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.os.IDeviceIdleController");
          removeSystemPowerWhitelistApp(paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.os.IDeviceIdleController");
          removePowerSaveWhitelistApp(paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        }
        paramParcel1.enforceInterface("android.os.IDeviceIdleController");
        addPowerSaveWhitelistApp(paramParcel1.readString());
        paramParcel2.writeNoException();
        return true;
      }
      paramParcel2.writeString("android.os.IDeviceIdleController");
      return true;
    }
    
    private static class Proxy
      implements IDeviceIdleController
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public void addPowerSaveTempWhitelistApp(String paramString1, long paramLong, int paramInt, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IDeviceIdleController");
          localParcel1.writeString(paramString1);
          localParcel1.writeLong(paramLong);
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString2);
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
      
      public long addPowerSaveTempWhitelistAppForMms(String paramString1, int paramInt, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IDeviceIdleController");
          localParcel1.writeString(paramString1);
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString2);
          mRemote.transact(18, localParcel1, localParcel2, 0);
          localParcel2.readException();
          long l = localParcel2.readLong();
          return l;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public long addPowerSaveTempWhitelistAppForSms(String paramString1, int paramInt, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IDeviceIdleController");
          localParcel1.writeString(paramString1);
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString2);
          mRemote.transact(19, localParcel1, localParcel2, 0);
          localParcel2.readException();
          long l = localParcel2.readLong();
          return l;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void addPowerSaveWhitelistApp(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IDeviceIdleController");
          localParcel1.writeString(paramString);
          mRemote.transact(1, localParcel1, localParcel2, 0);
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
      
      public void exitIdle(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IDeviceIdleController");
          localParcel1.writeString(paramString);
          mRemote.transact(20, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int[] getAppIdTempWhitelist()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IDeviceIdleController");
          mRemote.transact(14, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int[] arrayOfInt = localParcel2.createIntArray();
          return arrayOfInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int[] getAppIdUserWhitelist()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IDeviceIdleController");
          mRemote.transact(13, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int[] arrayOfInt = localParcel2.createIntArray();
          return arrayOfInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int[] getAppIdWhitelist()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IDeviceIdleController");
          mRemote.transact(12, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int[] arrayOfInt = localParcel2.createIntArray();
          return arrayOfInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int[] getAppIdWhitelistExceptIdle()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IDeviceIdleController");
          mRemote.transact(11, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int[] arrayOfInt = localParcel2.createIntArray();
          return arrayOfInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String[] getFullPowerWhitelist()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IDeviceIdleController");
          mRemote.transact(10, localParcel1, localParcel2, 0);
          localParcel2.readException();
          String[] arrayOfString = localParcel2.createStringArray();
          return arrayOfString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String[] getFullPowerWhitelistExceptIdle()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IDeviceIdleController");
          mRemote.transact(9, localParcel1, localParcel2, 0);
          localParcel2.readException();
          String[] arrayOfString = localParcel2.createStringArray();
          return arrayOfString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.os.IDeviceIdleController";
      }
      
      public String[] getRemovedSystemPowerWhitelistApps()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IDeviceIdleController");
          mRemote.transact(5, localParcel1, localParcel2, 0);
          localParcel2.readException();
          String[] arrayOfString = localParcel2.createStringArray();
          return arrayOfString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String[] getSystemPowerWhitelist()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IDeviceIdleController");
          mRemote.transact(7, localParcel1, localParcel2, 0);
          localParcel2.readException();
          String[] arrayOfString = localParcel2.createStringArray();
          return arrayOfString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String[] getSystemPowerWhitelistExceptIdle()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IDeviceIdleController");
          mRemote.transact(6, localParcel1, localParcel2, 0);
          localParcel2.readException();
          String[] arrayOfString = localParcel2.createStringArray();
          return arrayOfString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String[] getUserPowerWhitelist()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IDeviceIdleController");
          mRemote.transact(8, localParcel1, localParcel2, 0);
          localParcel2.readException();
          String[] arrayOfString = localParcel2.createStringArray();
          return arrayOfString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isPowerSaveWhitelistApp(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IDeviceIdleController");
          localParcel1.writeString(paramString);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(16, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isPowerSaveWhitelistExceptIdleApp(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IDeviceIdleController");
          localParcel1.writeString(paramString);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(15, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean registerMaintenanceActivityListener(IMaintenanceActivityListener paramIMaintenanceActivityListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IDeviceIdleController");
          if (paramIMaintenanceActivityListener != null) {
            paramIMaintenanceActivityListener = paramIMaintenanceActivityListener.asBinder();
          } else {
            paramIMaintenanceActivityListener = null;
          }
          localParcel1.writeStrongBinder(paramIMaintenanceActivityListener);
          paramIMaintenanceActivityListener = mRemote;
          boolean bool = false;
          paramIMaintenanceActivityListener.transact(21, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void removePowerSaveWhitelistApp(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IDeviceIdleController");
          localParcel1.writeString(paramString);
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
      
      public void removeSystemPowerWhitelistApp(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IDeviceIdleController");
          localParcel1.writeString(paramString);
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
      
      public void restoreSystemPowerWhitelistApp(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IDeviceIdleController");
          localParcel1.writeString(paramString);
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
      
      public void unregisterMaintenanceActivityListener(IMaintenanceActivityListener paramIMaintenanceActivityListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IDeviceIdleController");
          if (paramIMaintenanceActivityListener != null) {
            paramIMaintenanceActivityListener = paramIMaintenanceActivityListener.asBinder();
          } else {
            paramIMaintenanceActivityListener = null;
          }
          localParcel1.writeStrongBinder(paramIMaintenanceActivityListener);
          mRemote.transact(22, localParcel1, localParcel2, 0);
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
