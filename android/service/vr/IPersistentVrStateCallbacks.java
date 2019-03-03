package android.service.vr;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IPersistentVrStateCallbacks
  extends IInterface
{
  public abstract void onPersistentVrStateChanged(boolean paramBoolean)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IPersistentVrStateCallbacks
  {
    private static final String DESCRIPTOR = "android.service.vr.IPersistentVrStateCallbacks";
    static final int TRANSACTION_onPersistentVrStateChanged = 1;
    
    public Stub()
    {
      attachInterface(this, "android.service.vr.IPersistentVrStateCallbacks");
    }
    
    public static IPersistentVrStateCallbacks asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.service.vr.IPersistentVrStateCallbacks");
      if ((localIInterface != null) && ((localIInterface instanceof IPersistentVrStateCallbacks))) {
        return (IPersistentVrStateCallbacks)localIInterface;
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
        paramParcel2.writeString("android.service.vr.IPersistentVrStateCallbacks");
        return true;
      }
      paramParcel1.enforceInterface("android.service.vr.IPersistentVrStateCallbacks");
      boolean bool;
      if (paramParcel1.readInt() != 0) {
        bool = true;
      } else {
        bool = false;
      }
      onPersistentVrStateChanged(bool);
      return true;
    }
    
    private static class Proxy
      implements IPersistentVrStateCallbacks
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
        return "android.service.vr.IPersistentVrStateCallbacks";
      }
      
      public void onPersistentVrStateChanged(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.vr.IPersistentVrStateCallbacks");
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
