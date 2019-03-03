package android.os;

public abstract interface IBatteryPropertiesListener
  extends IInterface
{
  public abstract void batteryPropertiesChanged(BatteryProperties paramBatteryProperties)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IBatteryPropertiesListener
  {
    private static final String DESCRIPTOR = "android.os.IBatteryPropertiesListener";
    static final int TRANSACTION_batteryPropertiesChanged = 1;
    
    public Stub()
    {
      attachInterface(this, "android.os.IBatteryPropertiesListener");
    }
    
    public static IBatteryPropertiesListener asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.os.IBatteryPropertiesListener");
      if ((localIInterface != null) && ((localIInterface instanceof IBatteryPropertiesListener))) {
        return (IBatteryPropertiesListener)localIInterface;
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
        paramParcel2.writeString("android.os.IBatteryPropertiesListener");
        return true;
      }
      paramParcel1.enforceInterface("android.os.IBatteryPropertiesListener");
      if (paramParcel1.readInt() != 0) {
        paramParcel1 = (BatteryProperties)BatteryProperties.CREATOR.createFromParcel(paramParcel1);
      } else {
        paramParcel1 = null;
      }
      batteryPropertiesChanged(paramParcel1);
      return true;
    }
    
    private static class Proxy
      implements IBatteryPropertiesListener
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
      
      public void batteryPropertiesChanged(BatteryProperties paramBatteryProperties)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.os.IBatteryPropertiesListener");
          if (paramBatteryProperties != null)
          {
            localParcel.writeInt(1);
            paramBatteryProperties.writeToParcel(localParcel, 0);
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
      
      public String getInterfaceDescriptor()
      {
        return "android.os.IBatteryPropertiesListener";
      }
    }
  }
}
