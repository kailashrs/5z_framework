package android.media;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IRemoteVolumeObserver
  extends IInterface
{
  public abstract void dispatchRemoteVolumeUpdate(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IRemoteVolumeObserver
  {
    private static final String DESCRIPTOR = "android.media.IRemoteVolumeObserver";
    static final int TRANSACTION_dispatchRemoteVolumeUpdate = 1;
    
    public Stub()
    {
      attachInterface(this, "android.media.IRemoteVolumeObserver");
    }
    
    public static IRemoteVolumeObserver asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.media.IRemoteVolumeObserver");
      if ((localIInterface != null) && ((localIInterface instanceof IRemoteVolumeObserver))) {
        return (IRemoteVolumeObserver)localIInterface;
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
        paramParcel2.writeString("android.media.IRemoteVolumeObserver");
        return true;
      }
      paramParcel1.enforceInterface("android.media.IRemoteVolumeObserver");
      dispatchRemoteVolumeUpdate(paramParcel1.readInt(), paramParcel1.readInt());
      return true;
    }
    
    private static class Proxy
      implements IRemoteVolumeObserver
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
      
      public void dispatchRemoteVolumeUpdate(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.IRemoteVolumeObserver");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
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
        return "android.media.IRemoteVolumeObserver";
      }
    }
  }
}
