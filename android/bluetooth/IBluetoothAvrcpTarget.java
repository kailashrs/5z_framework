package android.bluetooth;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IBluetoothAvrcpTarget
  extends IInterface
{
  public abstract void sendVolumeChanged(int paramInt)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IBluetoothAvrcpTarget
  {
    private static final String DESCRIPTOR = "android.bluetooth.IBluetoothAvrcpTarget";
    static final int TRANSACTION_sendVolumeChanged = 1;
    
    public Stub()
    {
      attachInterface(this, "android.bluetooth.IBluetoothAvrcpTarget");
    }
    
    public static IBluetoothAvrcpTarget asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.bluetooth.IBluetoothAvrcpTarget");
      if ((localIInterface != null) && ((localIInterface instanceof IBluetoothAvrcpTarget))) {
        return (IBluetoothAvrcpTarget)localIInterface;
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
        paramParcel2.writeString("android.bluetooth.IBluetoothAvrcpTarget");
        return true;
      }
      paramParcel1.enforceInterface("android.bluetooth.IBluetoothAvrcpTarget");
      sendVolumeChanged(paramParcel1.readInt());
      paramParcel2.writeNoException();
      return true;
    }
    
    private static class Proxy
      implements IBluetoothAvrcpTarget
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
        return "android.bluetooth.IBluetoothAvrcpTarget";
      }
      
      public void sendVolumeChanged(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothAvrcpTarget");
          localParcel1.writeInt(paramInt);
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
    }
  }
}
