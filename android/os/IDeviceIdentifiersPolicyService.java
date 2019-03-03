package android.os;

public abstract interface IDeviceIdentifiersPolicyService
  extends IInterface
{
  public abstract String getSerial()
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IDeviceIdentifiersPolicyService
  {
    private static final String DESCRIPTOR = "android.os.IDeviceIdentifiersPolicyService";
    static final int TRANSACTION_getSerial = 1;
    
    public Stub()
    {
      attachInterface(this, "android.os.IDeviceIdentifiersPolicyService");
    }
    
    public static IDeviceIdentifiersPolicyService asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.os.IDeviceIdentifiersPolicyService");
      if ((localIInterface != null) && ((localIInterface instanceof IDeviceIdentifiersPolicyService))) {
        return (IDeviceIdentifiersPolicyService)localIInterface;
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
        paramParcel2.writeString("android.os.IDeviceIdentifiersPolicyService");
        return true;
      }
      paramParcel1.enforceInterface("android.os.IDeviceIdentifiersPolicyService");
      paramParcel1 = getSerial();
      paramParcel2.writeNoException();
      paramParcel2.writeString(paramParcel1);
      return true;
    }
    
    private static class Proxy
      implements IDeviceIdentifiersPolicyService
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
        return "android.os.IDeviceIdentifiersPolicyService";
      }
      
      public String getSerial()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IDeviceIdentifiersPolicyService");
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          String str = localParcel2.readString();
          return str;
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
