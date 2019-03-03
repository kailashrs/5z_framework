package android.os.storage;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IStorageShutdownObserver
  extends IInterface
{
  public abstract void onShutDownComplete(int paramInt)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IStorageShutdownObserver
  {
    private static final String DESCRIPTOR = "android.os.storage.IStorageShutdownObserver";
    static final int TRANSACTION_onShutDownComplete = 1;
    
    public Stub()
    {
      attachInterface(this, "android.os.storage.IStorageShutdownObserver");
    }
    
    public static IStorageShutdownObserver asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.os.storage.IStorageShutdownObserver");
      if ((localIInterface != null) && ((localIInterface instanceof IStorageShutdownObserver))) {
        return (IStorageShutdownObserver)localIInterface;
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
        paramParcel2.writeString("android.os.storage.IStorageShutdownObserver");
        return true;
      }
      paramParcel1.enforceInterface("android.os.storage.IStorageShutdownObserver");
      onShutDownComplete(paramParcel1.readInt());
      paramParcel2.writeNoException();
      return true;
    }
    
    private static class Proxy
      implements IStorageShutdownObserver
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
        return "android.os.storage.IStorageShutdownObserver";
      }
      
      public void onShutDownComplete(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.storage.IStorageShutdownObserver");
          localParcel1.writeInt(paramInt);
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
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
