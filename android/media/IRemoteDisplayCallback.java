package android.media;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IRemoteDisplayCallback
  extends IInterface
{
  public abstract void onStateChanged(RemoteDisplayState paramRemoteDisplayState)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IRemoteDisplayCallback
  {
    private static final String DESCRIPTOR = "android.media.IRemoteDisplayCallback";
    static final int TRANSACTION_onStateChanged = 1;
    
    public Stub()
    {
      attachInterface(this, "android.media.IRemoteDisplayCallback");
    }
    
    public static IRemoteDisplayCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.media.IRemoteDisplayCallback");
      if ((localIInterface != null) && ((localIInterface instanceof IRemoteDisplayCallback))) {
        return (IRemoteDisplayCallback)localIInterface;
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
        paramParcel2.writeString("android.media.IRemoteDisplayCallback");
        return true;
      }
      paramParcel1.enforceInterface("android.media.IRemoteDisplayCallback");
      if (paramParcel1.readInt() != 0) {
        paramParcel1 = (RemoteDisplayState)RemoteDisplayState.CREATOR.createFromParcel(paramParcel1);
      } else {
        paramParcel1 = null;
      }
      onStateChanged(paramParcel1);
      return true;
    }
    
    private static class Proxy
      implements IRemoteDisplayCallback
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
        return "android.media.IRemoteDisplayCallback";
      }
      
      public void onStateChanged(RemoteDisplayState paramRemoteDisplayState)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.IRemoteDisplayCallback");
          if (paramRemoteDisplayState != null)
          {
            localParcel.writeInt(1);
            paramRemoteDisplayState.writeToParcel(localParcel, 0);
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
