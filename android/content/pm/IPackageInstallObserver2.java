package android.content.pm;

import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IPackageInstallObserver2
  extends IInterface
{
  public abstract void onPackageInstalled(String paramString1, int paramInt, String paramString2, Bundle paramBundle)
    throws RemoteException;
  
  public abstract void onUserActionRequired(Intent paramIntent)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IPackageInstallObserver2
  {
    private static final String DESCRIPTOR = "android.content.pm.IPackageInstallObserver2";
    static final int TRANSACTION_onPackageInstalled = 2;
    static final int TRANSACTION_onUserActionRequired = 1;
    
    public Stub()
    {
      attachInterface(this, "android.content.pm.IPackageInstallObserver2");
    }
    
    public static IPackageInstallObserver2 asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.content.pm.IPackageInstallObserver2");
      if ((localIInterface != null) && ((localIInterface instanceof IPackageInstallObserver2))) {
        return (IPackageInstallObserver2)localIInterface;
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
        String str = null;
        Object localObject = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 2: 
          paramParcel1.enforceInterface("android.content.pm.IPackageInstallObserver2");
          str = paramParcel1.readString();
          paramInt1 = paramParcel1.readInt();
          paramParcel2 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject;
          }
          onPackageInstalled(str, paramInt1, paramParcel2, paramParcel1);
          return true;
        }
        paramParcel1.enforceInterface("android.content.pm.IPackageInstallObserver2");
        if (paramParcel1.readInt() != 0) {
          paramParcel1 = (Intent)Intent.CREATOR.createFromParcel(paramParcel1);
        } else {
          paramParcel1 = str;
        }
        onUserActionRequired(paramParcel1);
        return true;
      }
      paramParcel2.writeString("android.content.pm.IPackageInstallObserver2");
      return true;
    }
    
    private static class Proxy
      implements IPackageInstallObserver2
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
        return "android.content.pm.IPackageInstallObserver2";
      }
      
      public void onPackageInstalled(String paramString1, int paramInt, String paramString2, Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.content.pm.IPackageInstallObserver2");
          localParcel.writeString(paramString1);
          localParcel.writeInt(paramInt);
          localParcel.writeString(paramString2);
          if (paramBundle != null)
          {
            localParcel.writeInt(1);
            paramBundle.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
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
          localParcel.writeInterfaceToken("android.content.pm.IPackageInstallObserver2");
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
