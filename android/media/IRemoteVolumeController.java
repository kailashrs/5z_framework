package android.media;

import android.media.session.ISessionController;
import android.media.session.ISessionController.Stub;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IRemoteVolumeController
  extends IInterface
{
  public abstract void remoteVolumeChanged(ISessionController paramISessionController, int paramInt)
    throws RemoteException;
  
  public abstract void updateRemoteController(ISessionController paramISessionController)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IRemoteVolumeController
  {
    private static final String DESCRIPTOR = "android.media.IRemoteVolumeController";
    static final int TRANSACTION_remoteVolumeChanged = 1;
    static final int TRANSACTION_updateRemoteController = 2;
    
    public Stub()
    {
      attachInterface(this, "android.media.IRemoteVolumeController");
    }
    
    public static IRemoteVolumeController asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.media.IRemoteVolumeController");
      if ((localIInterface != null) && ((localIInterface instanceof IRemoteVolumeController))) {
        return (IRemoteVolumeController)localIInterface;
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
          paramParcel1.enforceInterface("android.media.IRemoteVolumeController");
          updateRemoteController(ISessionController.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        }
        paramParcel1.enforceInterface("android.media.IRemoteVolumeController");
        remoteVolumeChanged(ISessionController.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readInt());
        return true;
      }
      paramParcel2.writeString("android.media.IRemoteVolumeController");
      return true;
    }
    
    private static class Proxy
      implements IRemoteVolumeController
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
        return "android.media.IRemoteVolumeController";
      }
      
      public void remoteVolumeChanged(ISessionController paramISessionController, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.IRemoteVolumeController");
          if (paramISessionController != null) {
            paramISessionController = paramISessionController.asBinder();
          } else {
            paramISessionController = null;
          }
          localParcel.writeStrongBinder(paramISessionController);
          localParcel.writeInt(paramInt);
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void updateRemoteController(ISessionController paramISessionController)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.IRemoteVolumeController");
          if (paramISessionController != null) {
            paramISessionController = paramISessionController.asBinder();
          } else {
            paramISessionController = null;
          }
          localParcel.writeStrongBinder(paramISessionController);
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
