package android.os;

public abstract interface INetworkActivityListener
  extends IInterface
{
  public abstract void onNetworkActive()
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements INetworkActivityListener
  {
    private static final String DESCRIPTOR = "android.os.INetworkActivityListener";
    static final int TRANSACTION_onNetworkActive = 1;
    
    public Stub()
    {
      attachInterface(this, "android.os.INetworkActivityListener");
    }
    
    public static INetworkActivityListener asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.os.INetworkActivityListener");
      if ((localIInterface != null) && ((localIInterface instanceof INetworkActivityListener))) {
        return (INetworkActivityListener)localIInterface;
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
        paramParcel2.writeString("android.os.INetworkActivityListener");
        return true;
      }
      paramParcel1.enforceInterface("android.os.INetworkActivityListener");
      onNetworkActive();
      return true;
    }
    
    private static class Proxy
      implements INetworkActivityListener
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
        return "android.os.INetworkActivityListener";
      }
      
      public void onNetworkActive()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.os.INetworkActivityListener");
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
