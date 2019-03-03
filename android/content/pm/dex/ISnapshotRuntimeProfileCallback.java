package android.content.pm.dex;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface ISnapshotRuntimeProfileCallback
  extends IInterface
{
  public abstract void onError(int paramInt)
    throws RemoteException;
  
  public abstract void onSuccess(ParcelFileDescriptor paramParcelFileDescriptor)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ISnapshotRuntimeProfileCallback
  {
    private static final String DESCRIPTOR = "android.content.pm.dex.ISnapshotRuntimeProfileCallback";
    static final int TRANSACTION_onError = 2;
    static final int TRANSACTION_onSuccess = 1;
    
    public Stub()
    {
      attachInterface(this, "android.content.pm.dex.ISnapshotRuntimeProfileCallback");
    }
    
    public static ISnapshotRuntimeProfileCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.content.pm.dex.ISnapshotRuntimeProfileCallback");
      if ((localIInterface != null) && ((localIInterface instanceof ISnapshotRuntimeProfileCallback))) {
        return (ISnapshotRuntimeProfileCallback)localIInterface;
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
          paramParcel1.enforceInterface("android.content.pm.dex.ISnapshotRuntimeProfileCallback");
          onError(paramParcel1.readInt());
          return true;
        }
        paramParcel1.enforceInterface("android.content.pm.dex.ISnapshotRuntimeProfileCallback");
        if (paramParcel1.readInt() != 0) {
          paramParcel1 = (ParcelFileDescriptor)ParcelFileDescriptor.CREATOR.createFromParcel(paramParcel1);
        } else {
          paramParcel1 = null;
        }
        onSuccess(paramParcel1);
        return true;
      }
      paramParcel2.writeString("android.content.pm.dex.ISnapshotRuntimeProfileCallback");
      return true;
    }
    
    private static class Proxy
      implements ISnapshotRuntimeProfileCallback
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
        return "android.content.pm.dex.ISnapshotRuntimeProfileCallback";
      }
      
      public void onError(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.content.pm.dex.ISnapshotRuntimeProfileCallback");
          localParcel.writeInt(paramInt);
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onSuccess(ParcelFileDescriptor paramParcelFileDescriptor)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.content.pm.dex.ISnapshotRuntimeProfileCallback");
          if (paramParcelFileDescriptor != null)
          {
            localParcel.writeInt(1);
            paramParcelFileDescriptor.writeToParcel(localParcel, 0);
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
