package android.app.backup;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface ISelectBackupTransportCallback
  extends IInterface
{
  public abstract void onFailure(int paramInt)
    throws RemoteException;
  
  public abstract void onSuccess(String paramString)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ISelectBackupTransportCallback
  {
    private static final String DESCRIPTOR = "android.app.backup.ISelectBackupTransportCallback";
    static final int TRANSACTION_onFailure = 2;
    static final int TRANSACTION_onSuccess = 1;
    
    public Stub()
    {
      attachInterface(this, "android.app.backup.ISelectBackupTransportCallback");
    }
    
    public static ISelectBackupTransportCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.app.backup.ISelectBackupTransportCallback");
      if ((localIInterface != null) && ((localIInterface instanceof ISelectBackupTransportCallback))) {
        return (ISelectBackupTransportCallback)localIInterface;
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
          paramParcel1.enforceInterface("android.app.backup.ISelectBackupTransportCallback");
          onFailure(paramParcel1.readInt());
          return true;
        }
        paramParcel1.enforceInterface("android.app.backup.ISelectBackupTransportCallback");
        onSuccess(paramParcel1.readString());
        return true;
      }
      paramParcel2.writeString("android.app.backup.ISelectBackupTransportCallback");
      return true;
    }
    
    private static class Proxy
      implements ISelectBackupTransportCallback
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
        return "android.app.backup.ISelectBackupTransportCallback";
      }
      
      public void onFailure(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.backup.ISelectBackupTransportCallback");
          localParcel.writeInt(paramInt);
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onSuccess(String paramString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.backup.ISelectBackupTransportCallback");
          localParcel.writeString(paramString);
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
