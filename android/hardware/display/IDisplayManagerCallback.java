package android.hardware.display;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IDisplayManagerCallback
  extends IInterface
{
  public abstract void onDisplayEvent(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IDisplayManagerCallback
  {
    private static final String DESCRIPTOR = "android.hardware.display.IDisplayManagerCallback";
    static final int TRANSACTION_onDisplayEvent = 1;
    
    public Stub()
    {
      attachInterface(this, "android.hardware.display.IDisplayManagerCallback");
    }
    
    public static IDisplayManagerCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.hardware.display.IDisplayManagerCallback");
      if ((localIInterface != null) && ((localIInterface instanceof IDisplayManagerCallback))) {
        return (IDisplayManagerCallback)localIInterface;
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
        paramParcel2.writeString("android.hardware.display.IDisplayManagerCallback");
        return true;
      }
      paramParcel1.enforceInterface("android.hardware.display.IDisplayManagerCallback");
      onDisplayEvent(paramParcel1.readInt(), paramParcel1.readInt());
      return true;
    }
    
    private static class Proxy
      implements IDisplayManagerCallback
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
        return "android.hardware.display.IDisplayManagerCallback";
      }
      
      public void onDisplayEvent(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.hardware.display.IDisplayManagerCallback");
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
    }
  }
}
