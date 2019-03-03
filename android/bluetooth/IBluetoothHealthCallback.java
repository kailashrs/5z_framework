package android.bluetooth;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IBluetoothHealthCallback
  extends IInterface
{
  public abstract void onHealthAppConfigurationStatusChange(BluetoothHealthAppConfiguration paramBluetoothHealthAppConfiguration, int paramInt)
    throws RemoteException;
  
  public abstract void onHealthChannelStateChange(BluetoothHealthAppConfiguration paramBluetoothHealthAppConfiguration, BluetoothDevice paramBluetoothDevice, int paramInt1, int paramInt2, ParcelFileDescriptor paramParcelFileDescriptor, int paramInt3)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IBluetoothHealthCallback
  {
    private static final String DESCRIPTOR = "android.bluetooth.IBluetoothHealthCallback";
    static final int TRANSACTION_onHealthAppConfigurationStatusChange = 1;
    static final int TRANSACTION_onHealthChannelStateChange = 2;
    
    public Stub()
    {
      attachInterface(this, "android.bluetooth.IBluetoothHealthCallback");
    }
    
    public static IBluetoothHealthCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.bluetooth.IBluetoothHealthCallback");
      if ((localIInterface != null) && ((localIInterface instanceof IBluetoothHealthCallback))) {
        return (IBluetoothHealthCallback)localIInterface;
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
        BluetoothHealthAppConfiguration localBluetoothHealthAppConfiguration = null;
        ParcelFileDescriptor localParcelFileDescriptor = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 2: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothHealthCallback");
          if (paramParcel1.readInt() != 0) {
            localBluetoothHealthAppConfiguration = (BluetoothHealthAppConfiguration)BluetoothHealthAppConfiguration.CREATOR.createFromParcel(paramParcel1);
          } else {
            localBluetoothHealthAppConfiguration = null;
          }
          BluetoothDevice localBluetoothDevice;
          if (paramParcel1.readInt() != 0) {
            localBluetoothDevice = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
          } else {
            localBluetoothDevice = null;
          }
          paramInt2 = paramParcel1.readInt();
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            localParcelFileDescriptor = (ParcelFileDescriptor)ParcelFileDescriptor.CREATOR.createFromParcel(paramParcel1);
          }
          for (;;)
          {
            break;
          }
          onHealthChannelStateChange(localBluetoothHealthAppConfiguration, localBluetoothDevice, paramInt2, paramInt1, localParcelFileDescriptor, paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        }
        paramParcel1.enforceInterface("android.bluetooth.IBluetoothHealthCallback");
        if (paramParcel1.readInt() != 0) {
          localBluetoothHealthAppConfiguration = (BluetoothHealthAppConfiguration)BluetoothHealthAppConfiguration.CREATOR.createFromParcel(paramParcel1);
        }
        onHealthAppConfigurationStatusChange(localBluetoothHealthAppConfiguration, paramParcel1.readInt());
        paramParcel2.writeNoException();
        return true;
      }
      paramParcel2.writeString("android.bluetooth.IBluetoothHealthCallback");
      return true;
    }
    
    private static class Proxy
      implements IBluetoothHealthCallback
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
        return "android.bluetooth.IBluetoothHealthCallback";
      }
      
      public void onHealthAppConfigurationStatusChange(BluetoothHealthAppConfiguration paramBluetoothHealthAppConfiguration, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothHealthCallback");
          if (paramBluetoothHealthAppConfiguration != null)
          {
            localParcel1.writeInt(1);
            paramBluetoothHealthAppConfiguration.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
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
      
      public void onHealthChannelStateChange(BluetoothHealthAppConfiguration paramBluetoothHealthAppConfiguration, BluetoothDevice paramBluetoothDevice, int paramInt1, int paramInt2, ParcelFileDescriptor paramParcelFileDescriptor, int paramInt3)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothHealthCallback");
          if (paramBluetoothHealthAppConfiguration != null)
          {
            localParcel1.writeInt(1);
            paramBluetoothHealthAppConfiguration.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramBluetoothDevice != null)
          {
            localParcel1.writeInt(1);
            paramBluetoothDevice.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          if (paramParcelFileDescriptor != null)
          {
            localParcel1.writeInt(1);
            paramParcelFileDescriptor.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt3);
          mRemote.transact(2, localParcel1, localParcel2, 0);
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
