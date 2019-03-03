package android.os;

public abstract interface IThermalEventListener
  extends IInterface
{
  public abstract void notifyThrottling(boolean paramBoolean, Temperature paramTemperature)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IThermalEventListener
  {
    private static final String DESCRIPTOR = "android.os.IThermalEventListener";
    static final int TRANSACTION_notifyThrottling = 1;
    
    public Stub()
    {
      attachInterface(this, "android.os.IThermalEventListener");
    }
    
    public static IThermalEventListener asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.os.IThermalEventListener");
      if ((localIInterface != null) && ((localIInterface instanceof IThermalEventListener))) {
        return (IThermalEventListener)localIInterface;
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
        paramParcel2.writeString("android.os.IThermalEventListener");
        return true;
      }
      paramParcel1.enforceInterface("android.os.IThermalEventListener");
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
    }
    
    private static class Proxy
      implements IThermalEventListener
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
        return "android.os.IThermalEventListener";
      }
      
      public void notifyThrottling(boolean paramBoolean, Temperature paramTemperature)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.os.IThermalEventListener");
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
