package android.content.pm;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IPackageDeleteObserver
  extends IInterface
{
  public abstract void packageDeleted(String paramString, int paramInt)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IPackageDeleteObserver
  {
    private static final String DESCRIPTOR = "android.content.pm.IPackageDeleteObserver";
    static final int TRANSACTION_packageDeleted = 1;
    
    public Stub()
    {
      attachInterface(this, "android.content.pm.IPackageDeleteObserver");
    }
    
    public static IPackageDeleteObserver asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.content.pm.IPackageDeleteObserver");
      if ((localIInterface != null) && ((localIInterface instanceof IPackageDeleteObserver))) {
        return (IPackageDeleteObserver)localIInterface;
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
        paramParcel2.writeString("android.content.pm.IPackageDeleteObserver");
        return true;
      }
      paramParcel1.enforceInterface("android.content.pm.IPackageDeleteObserver");
      packageDeleted(paramParcel1.readString(), paramParcel1.readInt());
      return true;
    }
    
    private static class Proxy
      implements IPackageDeleteObserver
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
        return "android.content.pm.IPackageDeleteObserver";
      }
      
      public void packageDeleted(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.content.pm.IPackageDeleteObserver");
          localParcel.writeString(paramString);
          localParcel.writeInt(paramInt);
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
