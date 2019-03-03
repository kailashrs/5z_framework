package android.content.pm;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IPackageDeleteObserver2
  extends IInterface
{
  public abstract void onPackageDeleted(String paramString1, int paramInt, String paramString2)
    throws RemoteException;
  
  public abstract void onUserActionRequired(Intent paramIntent)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IPackageDeleteObserver2
  {
    private static final String DESCRIPTOR = "android.content.pm.IPackageDeleteObserver2";
    static final int TRANSACTION_onPackageDeleted = 2;
    static final int TRANSACTION_onUserActionRequired = 1;
    
    public Stub()
    {
      attachInterface(this, "android.content.pm.IPackageDeleteObserver2");
    }
    
    public static IPackageDeleteObserver2 asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.content.pm.IPackageDeleteObserver2");
      if ((localIInterface != null) && ((localIInterface instanceof IPackageDeleteObserver2))) {
        return (IPackageDeleteObserver2)localIInterface;
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
      if (paramInt1 != 1598968902)
      {
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 2: 
          paramParcel1.enforceInterface("android.content.pm.IPackageDeleteObserver2");
          onPackageDeleted(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readString());
          return true;
        }
        paramParcel1.enforceInterface("android.content.pm.IPackageDeleteObserver2");
        if (paramParcel1.readInt() != 0) {
          paramParcel1 = (Intent)Intent.CREATOR.createFromParcel(paramParcel1);
        } else {
          paramParcel1 = null;
        }
        onUserActionRequired(paramParcel1);
        return true;
      }
      paramParcel2.writeString("android.content.pm.IPackageDeleteObserver2");
      return true;
    }
    
    private static class Proxy
      implements IPackageDeleteObserver2
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
        return "android.content.pm.IPackageDeleteObserver2";
      }
      
      public void onPackageDeleted(String paramString1, int paramInt, String paramString2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.content.pm.IPackageDeleteObserver2");
          localParcel.writeString(paramString1);
          localParcel.writeInt(paramInt);
          localParcel.writeString(paramString2);
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onUserActionRequired(Intent paramIntent)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.content.pm.IPackageDeleteObserver2");
          if (paramIntent != null)
          {
            localParcel.writeInt(1);
            paramIntent.writeToParcel(localParcel, 0);
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
