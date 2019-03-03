package android.bluetooth;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IBluetoothStateChangeCallback
  extends IInterface
{
  public abstract void onBluetoothStateChange(boolean paramBoolean)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IBluetoothStateChangeCallback
  {
    private static final String DESCRIPTOR = "android.bluetooth.IBluetoothStateChangeCallback";
    static final int TRANSACTION_onBluetoothStateChange = 1;
    
    public Stub()
    {
      attachInterface(this, "android.bluetooth.IBluetoothStateChangeCallback");
    }
    
    public static IBluetoothStateChangeCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.bluetooth.IBluetoothStateChangeCallback");
      if ((localIInterface != null) && ((localIInterface instanceof IBluetoothStateChangeCallback))) {
        return (IBluetoothStateChangeCallback)localIInterface;
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
        paramParcel2.writeString("android.bluetooth.IBluetoothStateChangeCallback");
        return true;
      }
      paramParcel1.enforceInterface("android.bluetooth.IBluetoothStateChangeCallback");
      boolean bool;
      if (paramParcel1.readInt() != 0) {
        bool = true;
      } else {
        bool = false;
      }
      onBluetoothStateChange(bool);
      return true;
    }
    
    private static class Proxy
      implements IBluetoothStateChangeCallback
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
        return "android.bluetooth.IBluetoothStateChangeCallback";
      }
      
      public void onBluetoothStateChange(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.bluetooth.IBluetoothStateChangeCallback");
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
