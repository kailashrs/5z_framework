package android.view;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IRotationWatcher
  extends IInterface
{
  public abstract void onRotationChanged(int paramInt)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IRotationWatcher
  {
    private static final String DESCRIPTOR = "android.view.IRotationWatcher";
    static final int TRANSACTION_onRotationChanged = 1;
    
    public Stub()
    {
      attachInterface(this, "android.view.IRotationWatcher");
    }
    
    public static IRotationWatcher asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.view.IRotationWatcher");
      if ((localIInterface != null) && ((localIInterface instanceof IRotationWatcher))) {
        return (IRotationWatcher)localIInterface;
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
        paramParcel2.writeString("android.view.IRotationWatcher");
        return true;
      }
      paramParcel1.enforceInterface("android.view.IRotationWatcher");
      onRotationChanged(paramParcel1.readInt());
      return true;
    }
    
    private static class Proxy
      implements IRotationWatcher
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
        return "android.view.IRotationWatcher";
      }
      
      public void onRotationChanged(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.view.IRotationWatcher");
          localParcel.writeInt(paramInt);
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
