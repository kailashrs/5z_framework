package android.os;

public abstract interface IThermalService
  extends IInterface
{
  public abstract boolean isThrottling()
    throws RemoteException;
  
  public abstract void notifyThrottling(boolean paramBoolean, Temperature paramTemperature)
    throws RemoteException;
  
  public abstract void registerThermalEventListener(IThermalEventListener paramIThermalEventListener)
    throws RemoteException;
  
  public abstract void unregisterThermalEventListener(IThermalEventListener paramIThermalEventListener)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IThermalService
  {
    private static final String DESCRIPTOR = "android.os.IThermalService";
    static final int TRANSACTION_isThrottling = 4;
    static final int TRANSACTION_notifyThrottling = 3;
    static final int TRANSACTION_registerThermalEventListener = 1;
    static final int TRANSACTION_unregisterThermalEventListener = 2;
    
    public Stub()
    {
      attachInterface(this, "android.os.IThermalService");
    }
    
    public static IThermalService asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.os.IThermalService");
      if ((localIInterface != null) && ((localIInterface instanceof IThermalService))) {
        return (IThermalService)localIInterface;
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
        case 4: 
          paramParcel1.enforceInterface("android.os.IThermalService");
          paramInt1 = isThrottling();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.os.IThermalService");
          boolean bool;
          if (paramParcel1.readInt() != 0) {
            bool = true;
          } else {
            bool = false;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Temperature)Temperature.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = null;
          }
          notifyThrottling(bool, paramParcel1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.os.IThermalService");
          unregisterThermalEventListener(IThermalEventListener.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        }
        paramParcel1.enforceInterface("android.os.IThermalService");
        registerThermalEventListener(IThermalEventListener.Stub.asInterface(paramParcel1.readStrongBinder()));
        paramParcel2.writeNoException();
        return true;
      }
      paramParcel2.writeString("android.os.IThermalService");
      return true;
    }
    
    private static class Proxy
      implements IThermalService
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
      
      public String getInterfaceDescriptor()
      {
        return "android.os.IThermalService";
      }
      
      public boolean isThrottling()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IThermalService");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(4, localParcel1, localParcel2, 0);
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
      
      public void notifyThrottling(boolean paramBoolean, Temperature paramTemperature)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.os.IThermalService");
          localParcel.writeInt(paramBoolean);
          if (paramTemperature != null)
          {
            localParcel.writeInt(1);
            paramTemperature.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void registerThermalEventListener(IThermalEventListener paramIThermalEventListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IThermalService");
          if (paramIThermalEventListener != null) {
            paramIThermalEventListener = paramIThermalEventListener.asBinder();
          } else {
            paramIThermalEventListener = null;
          }
          localParcel1.writeStrongBinder(paramIThermalEventListener);
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
      
      public void unregisterThermalEventListener(IThermalEventListener paramIThermalEventListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IThermalService");
          if (paramIThermalEventListener != null) {
            paramIThermalEventListener = paramIThermalEventListener.asBinder();
          } else {
            paramIThermalEventListener = null;
          }
          localParcel1.writeStrongBinder(paramIThermalEventListener);
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
    }
  }
}
