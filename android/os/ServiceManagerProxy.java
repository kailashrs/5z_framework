package android.os;

import java.util.ArrayList;

class ServiceManagerProxy
  implements IServiceManager
{
  private IBinder mRemote;
  
  public ServiceManagerProxy(IBinder paramIBinder)
  {
    mRemote = paramIBinder;
  }
  
  public void addService(String paramString, IBinder paramIBinder, boolean paramBoolean, int paramInt)
    throws RemoteException
  {
    Parcel localParcel1 = Parcel.obtain();
    Parcel localParcel2 = Parcel.obtain();
    localParcel1.writeInterfaceToken("android.os.IServiceManager");
    localParcel1.writeString(paramString);
    localParcel1.writeStrongBinder(paramIBinder);
    localParcel1.writeInt(paramBoolean);
    localParcel1.writeInt(paramInt);
    mRemote.transact(3, localParcel1, localParcel2, 0);
    localParcel2.recycle();
    localParcel1.recycle();
  }
  
  public IBinder asBinder()
  {
    return mRemote;
  }
  
  public IBinder checkService(String paramString)
    throws RemoteException
  {
    Parcel localParcel1 = Parcel.obtain();
    Parcel localParcel2 = Parcel.obtain();
    localParcel1.writeInterfaceToken("android.os.IServiceManager");
    localParcel1.writeString(paramString);
    mRemote.transact(2, localParcel1, localParcel2, 0);
    paramString = localParcel2.readStrongBinder();
    localParcel2.recycle();
    localParcel1.recycle();
    return paramString;
  }
  
  public IBinder getService(String paramString)
    throws RemoteException
  {
    Parcel localParcel1 = Parcel.obtain();
    Parcel localParcel2 = Parcel.obtain();
    localParcel1.writeInterfaceToken("android.os.IServiceManager");
    localParcel1.writeString(paramString);
    mRemote.transact(1, localParcel1, localParcel2, 0);
    paramString = localParcel2.readStrongBinder();
    localParcel2.recycle();
    localParcel1.recycle();
    return paramString;
  }
  
  public String[] listServices(int paramInt)
    throws RemoteException
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    for (;;)
    {
      Parcel localParcel1 = Parcel.obtain();
      Parcel localParcel2 = Parcel.obtain();
      localParcel1.writeInterfaceToken("android.os.IServiceManager");
      localParcel1.writeInt(i);
      localParcel1.writeInt(paramInt);
      i++;
      try
      {
        boolean bool = mRemote.transact(4, localParcel1, localParcel2, 0);
        if (bool)
        {
          localArrayList.add(localParcel2.readString());
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      catch (RuntimeException localRuntimeException)
      {
        String[] arrayOfString = new String[localArrayList.size()];
        localArrayList.toArray(arrayOfString);
        return arrayOfString;
      }
    }
  }
  
  public void setPermissionController(IPermissionController paramIPermissionController)
    throws RemoteException
  {
    Parcel localParcel1 = Parcel.obtain();
    Parcel localParcel2 = Parcel.obtain();
    localParcel1.writeInterfaceToken("android.os.IServiceManager");
    localParcel1.writeStrongBinder(paramIPermissionController.asBinder());
    mRemote.transact(6, localParcel1, localParcel2, 0);
    localParcel2.recycle();
    localParcel1.recycle();
  }
}
