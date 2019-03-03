package android.os;

public abstract interface ISystemUpdateManager
  extends IInterface
{
  public abstract Bundle retrieveSystemUpdateInfo()
    throws RemoteException;
  
  public abstract void updateSystemUpdateInfo(PersistableBundle paramPersistableBundle)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ISystemUpdateManager
  {
    private static final String DESCRIPTOR = "android.os.ISystemUpdateManager";
    static final int TRANSACTION_retrieveSystemUpdateInfo = 1;
    static final int TRANSACTION_updateSystemUpdateInfo = 2;
    
    public Stub()
    {
      attachInterface(this, "android.os.ISystemUpdateManager");
    }
    
    public static ISystemUpdateManager asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.os.ISystemUpdateManager");
      if ((localIInterface != null) && ((localIInterface instanceof ISystemUpdateManager))) {
        return (ISystemUpdateManager)localIInterface;
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
          paramParcel1.enforceInterface("android.os.ISystemUpdateManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (PersistableBundle)PersistableBundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = null;
          }
          updateSystemUpdateInfo(paramParcel1);
          paramParcel2.writeNoException();
          return true;
        }
        paramParcel1.enforceInterface("android.os.ISystemUpdateManager");
        paramParcel1 = retrieveSystemUpdateInfo();
        paramParcel2.writeNoException();
        if (paramParcel1 != null)
        {
          paramParcel2.writeInt(1);
          paramParcel1.writeToParcel(paramParcel2, 1);
        }
        else
        {
          paramParcel2.writeInt(0);
        }
        return true;
      }
      paramParcel2.writeString("android.os.ISystemUpdateManager");
      return true;
    }
    
    private static class Proxy
      implements ISystemUpdateManager
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
        return "android.os.ISystemUpdateManager";
      }
      
      public Bundle retrieveSystemUpdateInfo()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.ISystemUpdateManager");
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          Bundle localBundle;
          if (localParcel2.readInt() != 0) {
            localBundle = (Bundle)Bundle.CREATOR.createFromParcel(localParcel2);
          } else {
            localBundle = null;
          }
          return localBundle;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void updateSystemUpdateInfo(PersistableBundle paramPersistableBundle)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.ISystemUpdateManager");
          if (paramPersistableBundle != null)
          {
            localParcel1.writeInt(1);
            paramPersistableBundle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
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
