package android.os;

public abstract interface IBatteryPropertiesRegistrar
  extends IInterface
{
  public abstract int getProperty(int paramInt, BatteryProperty paramBatteryProperty)
    throws RemoteException;
  
  public abstract void registerListener(IBatteryPropertiesListener paramIBatteryPropertiesListener)
    throws RemoteException;
  
  public abstract void scheduleUpdate()
    throws RemoteException;
  
  public abstract void unregisterListener(IBatteryPropertiesListener paramIBatteryPropertiesListener)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IBatteryPropertiesRegistrar
  {
    private static final String DESCRIPTOR = "android.os.IBatteryPropertiesRegistrar";
    static final int TRANSACTION_getProperty = 3;
    static final int TRANSACTION_registerListener = 1;
    static final int TRANSACTION_scheduleUpdate = 4;
    static final int TRANSACTION_unregisterListener = 2;
    
    public Stub()
    {
      attachInterface(this, "android.os.IBatteryPropertiesRegistrar");
    }
    
    public static IBatteryPropertiesRegistrar asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.os.IBatteryPropertiesRegistrar");
      if ((localIInterface != null) && ((localIInterface instanceof IBatteryPropertiesRegistrar))) {
        return (IBatteryPropertiesRegistrar)localIInterface;
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
          paramParcel1.enforceInterface("android.os.IBatteryPropertiesRegistrar");
          scheduleUpdate();
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.os.IBatteryPropertiesRegistrar");
          paramInt1 = paramParcel1.readInt();
          paramParcel1 = new BatteryProperty();
          paramInt1 = getProperty(paramInt1, paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          paramParcel2.writeInt(1);
          paramParcel1.writeToParcel(paramParcel2, 1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.os.IBatteryPropertiesRegistrar");
          unregisterListener(IBatteryPropertiesListener.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        }
        paramParcel1.enforceInterface("android.os.IBatteryPropertiesRegistrar");
        registerListener(IBatteryPropertiesListener.Stub.asInterface(paramParcel1.readStrongBinder()));
        paramParcel2.writeNoException();
        return true;
      }
      paramParcel2.writeString("android.os.IBatteryPropertiesRegistrar");
      return true;
    }
    
    private static class Proxy
      implements IBatteryPropertiesRegistrar
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
        return "android.os.IBatteryPropertiesRegistrar";
      }
      
      public int getProperty(int paramInt, BatteryProperty paramBatteryProperty)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IBatteryPropertiesRegistrar");
          localParcel1.writeInt(paramInt);
          mRemote.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (localParcel2.readInt() != 0) {
            paramBatteryProperty.readFromParcel(localParcel2);
          }
          return paramInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void registerListener(IBatteryPropertiesListener paramIBatteryPropertiesListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IBatteryPropertiesRegistrar");
          if (paramIBatteryPropertiesListener != null) {
            paramIBatteryPropertiesListener = paramIBatteryPropertiesListener.asBinder();
          } else {
            paramIBatteryPropertiesListener = null;
          }
          localParcel1.writeStrongBinder(paramIBatteryPropertiesListener);
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
      
      public void scheduleUpdate()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.os.IBatteryPropertiesRegistrar");
          mRemote.transact(4, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void unregisterListener(IBatteryPropertiesListener paramIBatteryPropertiesListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IBatteryPropertiesRegistrar");
          if (paramIBatteryPropertiesListener != null) {
            paramIBatteryPropertiesListener = paramIBatteryPropertiesListener.asBinder();
          } else {
            paramIBatteryPropertiesListener = null;
          }
          localParcel1.writeStrongBinder(paramIBatteryPropertiesListener);
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
