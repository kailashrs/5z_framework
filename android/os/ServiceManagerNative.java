package android.os;

public abstract class ServiceManagerNative
  extends Binder
  implements IServiceManager
{
  public ServiceManagerNative()
  {
    attachInterface(this, "android.os.IServiceManager");
  }
  
  public static IServiceManager asInterface(IBinder paramIBinder)
  {
    if (paramIBinder == null) {
      return null;
    }
    IServiceManager localIServiceManager = (IServiceManager)paramIBinder.queryLocalInterface("android.os.IServiceManager");
    if (localIServiceManager != null) {
      return localIServiceManager;
    }
    return new ServiceManagerProxy(paramIBinder);
  }
  
  public IBinder asBinder()
  {
    return this;
  }
  
  public boolean onTransact(int paramInt1, Parcel paramParcel1, Parcel paramParcel2, int paramInt2)
  {
    if (paramInt1 != 6)
    {
      switch (paramInt1)
      {
      default: 
        break;
      case 4: 
      case 3: 
      case 2: 
      case 1: 
        boolean bool;
        try
        {
          paramParcel1.enforceInterface("android.os.IServiceManager");
          paramParcel2.writeStringArray(listServices(paramParcel1.readInt()));
          return true;
        }
        catch (RemoteException paramParcel1) {}
        paramParcel1.enforceInterface("android.os.IServiceManager");
        paramParcel2 = paramParcel1.readString();
        IBinder localIBinder = paramParcel1.readStrongBinder();
        if (paramParcel1.readInt() != 0) {
          bool = true;
        } else {
          bool = false;
        }
        addService(paramParcel2, localIBinder, bool, paramParcel1.readInt());
        return true;
        paramParcel1.enforceInterface("android.os.IServiceManager");
        paramParcel2.writeStrongBinder(checkService(paramParcel1.readString()));
        return true;
        paramParcel1.enforceInterface("android.os.IServiceManager");
        paramParcel2.writeStrongBinder(getService(paramParcel1.readString()));
        return true;
      }
    }
    else
    {
      paramParcel1.enforceInterface("android.os.IServiceManager");
      setPermissionController(IPermissionController.Stub.asInterface(paramParcel1.readStrongBinder()));
      return true;
    }
    return false;
  }
}
