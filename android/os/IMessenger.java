package android.os;

public abstract interface IMessenger
  extends IInterface
{
  public abstract void send(Message paramMessage)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IMessenger
  {
    private static final String DESCRIPTOR = "android.os.IMessenger";
    static final int TRANSACTION_send = 1;
    
    public Stub()
    {
      attachInterface(this, "android.os.IMessenger");
    }
    
    public static IMessenger asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.os.IMessenger");
      if ((localIInterface != null) && ((localIInterface instanceof IMessenger))) {
        return (IMessenger)localIInterface;
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
        paramParcel2.writeString("android.os.IMessenger");
        return true;
      }
      paramParcel1.enforceInterface("android.os.IMessenger");
      if (paramParcel1.readInt() != 0) {
        paramParcel1 = (Message)Message.CREATOR.createFromParcel(paramParcel1);
      } else {
        paramParcel1 = null;
      }
      send(paramParcel1);
      return true;
    }
    
    private static class Proxy
      implements IMessenger
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
        return "android.os.IMessenger";
      }
      
      public void send(Message paramMessage)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.os.IMessenger");
          if (paramMessage != null)
          {
            localParcel.writeInt(1);
            paramMessage.writeToParcel(localParcel, 0);
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
