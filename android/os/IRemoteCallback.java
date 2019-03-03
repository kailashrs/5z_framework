package android.os;

public abstract interface IRemoteCallback
  extends IInterface
{
  public abstract void sendResult(Bundle paramBundle)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IRemoteCallback
  {
    private static final String DESCRIPTOR = "android.os.IRemoteCallback";
    static final int TRANSACTION_sendResult = 1;
    
    public Stub()
    {
      attachInterface(this, "android.os.IRemoteCallback");
    }
    
    public static IRemoteCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.os.IRemoteCallback");
      if ((localIInterface != null) && ((localIInterface instanceof IRemoteCallback))) {
        return (IRemoteCallback)localIInterface;
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
        paramParcel2.writeString("android.os.IRemoteCallback");
        return true;
      }
      paramParcel1.enforceInterface("android.os.IRemoteCallback");
      if (paramParcel1.readInt() != 0) {
        paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
      } else {
        paramParcel1 = null;
      }
      sendResult(paramParcel1);
      return true;
    }
    
    private static class Proxy
      implements IRemoteCallback
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
        return "android.os.IRemoteCallback";
      }
      
      public void sendResult(Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.os.IRemoteCallback");
          if (paramBundle != null)
          {
            localParcel.writeInt(1);
            paramBundle.writeToParcel(localParcel, 0);
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
