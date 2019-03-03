package android.media.projection;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IMediaProjectionCallback
  extends IInterface
{
  public abstract void onStop()
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IMediaProjectionCallback
  {
    private static final String DESCRIPTOR = "android.media.projection.IMediaProjectionCallback";
    static final int TRANSACTION_onStop = 1;
    
    public Stub()
    {
      attachInterface(this, "android.media.projection.IMediaProjectionCallback");
    }
    
    public static IMediaProjectionCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.media.projection.IMediaProjectionCallback");
      if ((localIInterface != null) && ((localIInterface instanceof IMediaProjectionCallback))) {
        return (IMediaProjectionCallback)localIInterface;
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
        paramParcel2.writeString("android.media.projection.IMediaProjectionCallback");
        return true;
      }
      paramParcel1.enforceInterface("android.media.projection.IMediaProjectionCallback");
      onStop();
      return true;
    }
    
    private static class Proxy
      implements IMediaProjectionCallback
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
        return "android.media.projection.IMediaProjectionCallback";
      }
      
      public void onStop()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.projection.IMediaProjectionCallback");
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
