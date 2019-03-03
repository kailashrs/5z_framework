package android.bluetooth;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IBluetoothManagerCallback
  extends IInterface
{
  public abstract void onBluetoothServiceDown()
    throws RemoteException;
  
  public abstract void onBluetoothServiceUp(IBluetooth paramIBluetooth)
    throws RemoteException;
  
  public abstract void onBrEdrDown()
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IBluetoothManagerCallback
  {
    private static final String DESCRIPTOR = "android.bluetooth.IBluetoothManagerCallback";
    static final int TRANSACTION_onBluetoothServiceDown = 2;
    static final int TRANSACTION_onBluetoothServiceUp = 1;
    static final int TRANSACTION_onBrEdrDown = 3;
    
    public Stub()
    {
      attachInterface(this, "android.bluetooth.IBluetoothManagerCallback");
    }
    
    public static IBluetoothManagerCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.bluetooth.IBluetoothManagerCallback");
      if ((localIInterface != null) && ((localIInterface instanceof IBluetoothManagerCallback))) {
        return (IBluetoothManagerCallback)localIInterface;
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
        case 3: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothManagerCallback");
          onBrEdrDown();
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothManagerCallback");
          onBluetoothServiceDown();
          return true;
        }
        paramParcel1.enforceInterface("android.bluetooth.IBluetoothManagerCallback");
        onBluetoothServiceUp(IBluetooth.Stub.asInterface(paramParcel1.readStrongBinder()));
        return true;
      }
      paramParcel2.writeString("android.bluetooth.IBluetoothManagerCallback");
      return true;
    }
    
    private static class Proxy
      implements IBluetoothManagerCallback
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
        return "android.bluetooth.IBluetoothManagerCallback";
      }
      
      public void onBluetoothServiceDown()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.bluetooth.IBluetoothManagerCallback");
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onBluetoothServiceUp(IBluetooth paramIBluetooth)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.bluetooth.IBluetoothManagerCallback");
          if (paramIBluetooth != null) {
            paramIBluetooth = paramIBluetooth.asBinder();
          } else {
            paramIBluetooth = null;
          }
          localParcel.writeStrongBinder(paramIBluetooth);
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onBrEdrDown()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.bluetooth.IBluetoothManagerCallback");
          mRemote.transact(3, localParcel, null, 1);
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
