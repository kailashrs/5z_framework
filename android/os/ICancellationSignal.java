package android.os;

public abstract interface ICancellationSignal
  extends IInterface
{
  public abstract void cancel()
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ICancellationSignal
  {
    private static final String DESCRIPTOR = "android.os.ICancellationSignal";
    static final int TRANSACTION_cancel = 1;
    
    public Stub()
    {
      attachInterface(this, "android.os.ICancellationSignal");
    }
    
    public static ICancellationSignal asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.os.ICancellationSignal");
      if ((localIInterface != null) && ((localIInterface instanceof ICancellationSignal))) {
        return (ICancellationSignal)localIInterface;
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
      if (paramInt1 != 1)
      {
        if (paramInt1 != 1598968902) {
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        }
        paramParcel2.writeString("android.os.ICancellationSignal");
        return true;
      }
      paramParcel1.enforceInterface("android.os.ICancellationSignal");
      cancel();
      return true;
    }
    
    private static class Proxy
      implements ICancellationSignal
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
      
      public void cancel()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.os.ICancellationSignal");
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.os.ICancellationSignal";
      }
    }
  }
}
