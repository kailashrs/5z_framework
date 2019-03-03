package android.content.pm;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IPackageDataObserver
  extends IInterface
{
  public abstract void onRemoveCompleted(String paramString, boolean paramBoolean)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IPackageDataObserver
  {
    private static final String DESCRIPTOR = "android.content.pm.IPackageDataObserver";
    static final int TRANSACTION_onRemoveCompleted = 1;
    
    public Stub()
    {
      attachInterface(this, "android.content.pm.IPackageDataObserver");
    }
    
    public static IPackageDataObserver asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.content.pm.IPackageDataObserver");
      if ((localIInterface != null) && ((localIInterface instanceof IPackageDataObserver))) {
        return (IPackageDataObserver)localIInterface;
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
        paramParcel2.writeString("android.content.pm.IPackageDataObserver");
        return true;
      }
      paramParcel1.enforceInterface("android.content.pm.IPackageDataObserver");
      paramParcel2 = paramParcel1.readString();
      boolean bool;
      if (paramParcel1.readInt() != 0) {
        bool = true;
      } else {
        bool = false;
      }
      onRemoveCompleted(paramParcel2, bool);
      return true;
    }
    
    private static class Proxy
      implements IPackageDataObserver
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
        return "android.content.pm.IPackageDataObserver";
      }
      
      public void onRemoveCompleted(String paramString, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.content.pm.IPackageDataObserver");
          localParcel.writeString(paramString);
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
