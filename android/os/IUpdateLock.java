package android.os;

public abstract interface IUpdateLock
  extends IInterface
{
  public abstract void acquireUpdateLock(IBinder paramIBinder, String paramString)
    throws RemoteException;
  
  public abstract void releaseUpdateLock(IBinder paramIBinder)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IUpdateLock
  {
    private static final String DESCRIPTOR = "android.os.IUpdateLock";
    static final int TRANSACTION_acquireUpdateLock = 1;
    static final int TRANSACTION_releaseUpdateLock = 2;
    
    public Stub()
    {
      attachInterface(this, "android.os.IUpdateLock");
    }
    
    public static IUpdateLock asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.os.IUpdateLock");
      if ((localIInterface != null) && ((localIInterface instanceof IUpdateLock))) {
        return (IUpdateLock)localIInterface;
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
        case 2: 
          paramParcel1.enforceInterface("android.os.IUpdateLock");
          releaseUpdateLock(paramParcel1.readStrongBinder());
          paramParcel2.writeNoException();
          return true;
        }
        paramParcel1.enforceInterface("android.os.IUpdateLock");
        acquireUpdateLock(paramParcel1.readStrongBinder(), paramParcel1.readString());
        paramParcel2.writeNoException();
        return true;
      }
      paramParcel2.writeString("android.os.IUpdateLock");
      return true;
    }
    
    private static class Proxy
      implements IUpdateLock
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public void acquireUpdateLock(IBinder paramIBinder, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IUpdateLock");
          localParcel1.writeStrongBinder(paramIBinder);
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
      
      public String getInterfaceDescriptor()
      {
        return "android.os.IUpdateLock";
      }
      
      public void releaseUpdateLock(IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IUpdateLock");
          localParcel1.writeStrongBinder(paramIBinder);
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
