package android.app.admin;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IDeviceAdminService
  extends IInterface
{
  public static abstract class Stub
    extends Binder
    implements IDeviceAdminService
  {
    private static final String DESCRIPTOR = "android.app.admin.IDeviceAdminService";
    
    public Stub()
    {
      attachInterface(this, "android.app.admin.IDeviceAdminService");
    }
    
    public static IDeviceAdminService asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.app.admin.IDeviceAdminService");
      if ((localIInterface != null) && ((localIInterface instanceof IDeviceAdminService))) {
        return (IDeviceAdminService)localIInterface;
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
      if (paramInt1 != 1598968902) {
        return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
      }
      paramParcel2.writeString("android.app.admin.IDeviceAdminService");
      return true;
    }
    
    private static class Proxy
      implements IDeviceAdminService
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
        return "android.app.admin.IDeviceAdminService";
      }
    }
  }
}
