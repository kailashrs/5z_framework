package android.os;

public abstract interface IVoldTaskListener
  extends IInterface
{
  public abstract void onFinished(int paramInt, PersistableBundle paramPersistableBundle)
    throws RemoteException;
  
  public abstract void onStatus(int paramInt, PersistableBundle paramPersistableBundle)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IVoldTaskListener
  {
    private static final String DESCRIPTOR = "android.os.IVoldTaskListener";
    static final int TRANSACTION_onFinished = 2;
    static final int TRANSACTION_onStatus = 1;
    
    public Stub()
    {
      attachInterface(this, "android.os.IVoldTaskListener");
    }
    
    public static IVoldTaskListener asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.os.IVoldTaskListener");
      if ((localIInterface != null) && ((localIInterface instanceof IVoldTaskListener))) {
        return (IVoldTaskListener)localIInterface;
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
        Object localObject1 = null;
        Object localObject2 = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 2: 
          paramParcel1.enforceInterface("android.os.IVoldTaskListener");
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (PersistableBundle)PersistableBundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject2;
          }
          onFinished(paramInt1, paramParcel1);
          return true;
        }
        paramParcel1.enforceInterface("android.os.IVoldTaskListener");
        paramInt1 = paramParcel1.readInt();
        if (paramParcel1.readInt() != 0) {
          paramParcel1 = (PersistableBundle)PersistableBundle.CREATOR.createFromParcel(paramParcel1);
        } else {
          paramParcel1 = localObject1;
        }
        onStatus(paramInt1, paramParcel1);
        return true;
      }
      paramParcel2.writeString("android.os.IVoldTaskListener");
      return true;
    }
    
    private static class Proxy
      implements IVoldTaskListener
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
        return "android.os.IVoldTaskListener";
      }
      
      public void onFinished(int paramInt, PersistableBundle paramPersistableBundle)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.os.IVoldTaskListener");
          localParcel.writeInt(paramInt);
          if (paramPersistableBundle != null)
          {
            localParcel.writeInt(1);
            paramPersistableBundle.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onStatus(int paramInt, PersistableBundle paramPersistableBundle)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.os.IVoldTaskListener");
          localParcel.writeInt(paramInt);
          if (paramPersistableBundle != null)
          {
            localParcel.writeInt(1);
            paramPersistableBundle.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(1, localParcel, null, 1);
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
