package android.content.pm;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IPackageStatsObserver
  extends IInterface
{
  public abstract void onGetStatsCompleted(PackageStats paramPackageStats, boolean paramBoolean)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IPackageStatsObserver
  {
    private static final String DESCRIPTOR = "android.content.pm.IPackageStatsObserver";
    static final int TRANSACTION_onGetStatsCompleted = 1;
    
    public Stub()
    {
      attachInterface(this, "android.content.pm.IPackageStatsObserver");
    }
    
    public static IPackageStatsObserver asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.content.pm.IPackageStatsObserver");
      if ((localIInterface != null) && ((localIInterface instanceof IPackageStatsObserver))) {
        return (IPackageStatsObserver)localIInterface;
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
        paramParcel2.writeString("android.content.pm.IPackageStatsObserver");
        return true;
      }
      paramParcel1.enforceInterface("android.content.pm.IPackageStatsObserver");
      if (paramParcel1.readInt() != 0) {
        paramParcel2 = (PackageStats)PackageStats.CREATOR.createFromParcel(paramParcel1);
      } else {
        paramParcel2 = null;
      }
      boolean bool;
      if (paramParcel1.readInt() != 0) {
        bool = true;
      } else {
        bool = false;
      }
      onGetStatsCompleted(paramParcel2, bool);
      return true;
    }
    
    private static class Proxy
      implements IPackageStatsObserver
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
        return "android.content.pm.IPackageStatsObserver";
      }
      
      public void onGetStatsCompleted(PackageStats paramPackageStats, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.content.pm.IPackageStatsObserver");
          if (paramPackageStats != null)
          {
            localParcel.writeInt(1);
            paramPackageStats.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
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
