package android.os;

public abstract interface IMaintenanceActivityListener
  extends IInterface
{
  public abstract void onMaintenanceActivityChanged(boolean paramBoolean)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IMaintenanceActivityListener
  {
    private static final String DESCRIPTOR = "android.os.IMaintenanceActivityListener";
    static final int TRANSACTION_onMaintenanceActivityChanged = 1;
    
    public Stub()
    {
      attachInterface(this, "android.os.IMaintenanceActivityListener");
    }
    
    public static IMaintenanceActivityListener asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.os.IMaintenanceActivityListener");
      if ((localIInterface != null) && ((localIInterface instanceof IMaintenanceActivityListener))) {
        return (IMaintenanceActivityListener)localIInterface;
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
        paramParcel2.writeString("android.os.IMaintenanceActivityListener");
        return true;
      }
      paramParcel1.enforceInterface("android.os.IMaintenanceActivityListener");
      boolean bool;
      if (paramParcel1.readInt() != 0) {
        bool = true;
      } else {
        bool = false;
      }
      onMaintenanceActivityChanged(bool);
      return true;
    }
    
    private static class Proxy
      implements IMaintenanceActivityListener
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
        return "android.os.IMaintenanceActivityListener";
      }
      
      public void onMaintenanceActivityChanged(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.os.IMaintenanceActivityListener");
          localParcel.writeInt(paramBoolean);
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
