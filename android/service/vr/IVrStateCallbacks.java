package android.service.vr;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IVrStateCallbacks
  extends IInterface
{
  public abstract void onVrStateChanged(boolean paramBoolean)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IVrStateCallbacks
  {
    private static final String DESCRIPTOR = "android.service.vr.IVrStateCallbacks";
    static final int TRANSACTION_onVrStateChanged = 1;
    
    public Stub()
    {
      attachInterface(this, "android.service.vr.IVrStateCallbacks");
    }
    
    public static IVrStateCallbacks asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.service.vr.IVrStateCallbacks");
      if ((localIInterface != null) && ((localIInterface instanceof IVrStateCallbacks))) {
        return (IVrStateCallbacks)localIInterface;
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
        paramParcel2.writeString("android.service.vr.IVrStateCallbacks");
        return true;
      }
      paramParcel1.enforceInterface("android.service.vr.IVrStateCallbacks");
      boolean bool;
      if (paramParcel1.readInt() != 0) {
        bool = true;
      } else {
        bool = false;
      }
      onVrStateChanged(bool);
      return true;
    }
    
    private static class Proxy
      implements IVrStateCallbacks
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
        return "android.service.vr.IVrStateCallbacks";
      }
      
      public void onVrStateChanged(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.vr.IVrStateCallbacks");
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
