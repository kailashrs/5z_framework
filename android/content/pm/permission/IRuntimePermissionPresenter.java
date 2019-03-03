package android.content.pm.permission;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteCallback;
import android.os.RemoteException;

public abstract interface IRuntimePermissionPresenter
  extends IInterface
{
  public abstract void getAppPermissions(String paramString, RemoteCallback paramRemoteCallback)
    throws RemoteException;
  
  public abstract void revokeRuntimePermission(String paramString1, String paramString2)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IRuntimePermissionPresenter
  {
    private static final String DESCRIPTOR = "android.content.pm.permission.IRuntimePermissionPresenter";
    static final int TRANSACTION_getAppPermissions = 1;
    static final int TRANSACTION_revokeRuntimePermission = 2;
    
    public Stub()
    {
      attachInterface(this, "android.content.pm.permission.IRuntimePermissionPresenter");
    }
    
    public static IRuntimePermissionPresenter asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.content.pm.permission.IRuntimePermissionPresenter");
      if ((localIInterface != null) && ((localIInterface instanceof IRuntimePermissionPresenter))) {
        return (IRuntimePermissionPresenter)localIInterface;
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
          paramParcel1.enforceInterface("android.content.pm.permission.IRuntimePermissionPresenter");
          revokeRuntimePermission(paramParcel1.readString(), paramParcel1.readString());
          return true;
        }
        paramParcel1.enforceInterface("android.content.pm.permission.IRuntimePermissionPresenter");
        paramParcel2 = paramParcel1.readString();
        if (paramParcel1.readInt() != 0) {
          paramParcel1 = (RemoteCallback)RemoteCallback.CREATOR.createFromParcel(paramParcel1);
        } else {
          paramParcel1 = null;
        }
        getAppPermissions(paramParcel2, paramParcel1);
        return true;
      }
      paramParcel2.writeString("android.content.pm.permission.IRuntimePermissionPresenter");
      return true;
    }
    
    private static class Proxy
      implements IRuntimePermissionPresenter
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
      
      public void getAppPermissions(String paramString, RemoteCallback paramRemoteCallback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.content.pm.permission.IRuntimePermissionPresenter");
          localParcel.writeString(paramString);
          if (paramRemoteCallback != null)
          {
            localParcel.writeInt(1);
            paramRemoteCallback.writeToParcel(localParcel, 0);
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
      
      public String getInterfaceDescriptor()
      {
        return "android.content.pm.permission.IRuntimePermissionPresenter";
      }
      
      public void revokeRuntimePermission(String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.content.pm.permission.IRuntimePermissionPresenter");
          localParcel.writeString(paramString1);
          localParcel.writeString(paramString2);
          mRemote.transact(2, localParcel, null, 1);
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
