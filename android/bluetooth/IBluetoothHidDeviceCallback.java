package android.bluetooth;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IBluetoothHidDeviceCallback
  extends IInterface
{
  public abstract void onAppStatusChanged(BluetoothDevice paramBluetoothDevice, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void onConnectionStateChanged(BluetoothDevice paramBluetoothDevice, int paramInt)
    throws RemoteException;
  
  public abstract void onGetReport(BluetoothDevice paramBluetoothDevice, byte paramByte1, byte paramByte2, int paramInt)
    throws RemoteException;
  
  public abstract void onInterruptData(BluetoothDevice paramBluetoothDevice, byte paramByte, byte[] paramArrayOfByte)
    throws RemoteException;
  
  public abstract void onSetProtocol(BluetoothDevice paramBluetoothDevice, byte paramByte)
    throws RemoteException;
  
  public abstract void onSetReport(BluetoothDevice paramBluetoothDevice, byte paramByte1, byte paramByte2, byte[] paramArrayOfByte)
    throws RemoteException;
  
  public abstract void onVirtualCableUnplug(BluetoothDevice paramBluetoothDevice)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IBluetoothHidDeviceCallback
  {
    private static final String DESCRIPTOR = "android.bluetooth.IBluetoothHidDeviceCallback";
    static final int TRANSACTION_onAppStatusChanged = 1;
    static final int TRANSACTION_onConnectionStateChanged = 2;
    static final int TRANSACTION_onGetReport = 3;
    static final int TRANSACTION_onInterruptData = 6;
    static final int TRANSACTION_onSetProtocol = 5;
    static final int TRANSACTION_onSetReport = 4;
    static final int TRANSACTION_onVirtualCableUnplug = 7;
    
    public Stub()
    {
      attachInterface(this, "android.bluetooth.IBluetoothHidDeviceCallback");
    }
    
    public static IBluetoothHidDeviceCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.bluetooth.IBluetoothHidDeviceCallback");
      if ((localIInterface != null) && ((localIInterface instanceof IBluetoothHidDeviceCallback))) {
        return (IBluetoothHidDeviceCallback)localIInterface;
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
        Object localObject1 = null;
        Object localObject2 = null;
        Object localObject3 = null;
        Object localObject4 = null;
        Object localObject5 = null;
        Object localObject6 = null;
        Object localObject7 = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 7: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothHidDeviceCallback");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = (Parcel)localObject7;
          }
          onVirtualCableUnplug(paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothHidDeviceCallback");
          if (paramParcel1.readInt() != 0) {
            localObject7 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject7 = localObject1;
          }
          onInterruptData((BluetoothDevice)localObject7, paramParcel1.readByte(), paramParcel1.createByteArray());
          paramParcel2.writeNoException();
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothHidDeviceCallback");
          if (paramParcel1.readInt() != 0) {
            localObject7 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject7 = localObject2;
          }
          onSetProtocol((BluetoothDevice)localObject7, paramParcel1.readByte());
          paramParcel2.writeNoException();
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothHidDeviceCallback");
          if (paramParcel1.readInt() != 0) {
            localObject7 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject7 = localObject3;
          }
          onSetReport((BluetoothDevice)localObject7, paramParcel1.readByte(), paramParcel1.readByte(), paramParcel1.createByteArray());
          paramParcel2.writeNoException();
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothHidDeviceCallback");
          if (paramParcel1.readInt() != 0) {
            localObject7 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject7 = localObject4;
          }
          onGetReport((BluetoothDevice)localObject7, paramParcel1.readByte(), paramParcel1.readByte(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothHidDeviceCallback");
          if (paramParcel1.readInt() != 0) {
            localObject7 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject7 = localObject5;
          }
          onConnectionStateChanged((BluetoothDevice)localObject7, paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        }
        paramParcel1.enforceInterface("android.bluetooth.IBluetoothHidDeviceCallback");
        if (paramParcel1.readInt() != 0) {
          localObject7 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
        } else {
          localObject7 = localObject6;
        }
        boolean bool;
        if (paramParcel1.readInt() != 0) {
          bool = true;
        } else {
          bool = false;
        }
        onAppStatusChanged((BluetoothDevice)localObject7, bool);
        paramParcel2.writeNoException();
        return true;
      }
      paramParcel2.writeString("android.bluetooth.IBluetoothHidDeviceCallback");
      return true;
    }
    
    private static class Proxy
      implements IBluetoothHidDeviceCallback
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
        return "android.bluetooth.IBluetoothHidDeviceCallback";
      }
      
      public void onAppStatusChanged(BluetoothDevice paramBluetoothDevice, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothHidDeviceCallback");
          if (paramBluetoothDevice != null)
          {
            localParcel1.writeInt(1);
            paramBluetoothDevice.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramBoolean);
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
      
      public void onConnectionStateChanged(BluetoothDevice paramBluetoothDevice, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothHidDeviceCallback");
          if (paramBluetoothDevice != null)
          {
            localParcel1.writeInt(1);
            paramBluetoothDevice.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
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
      
      public void onGetReport(BluetoothDevice paramBluetoothDevice, byte paramByte1, byte paramByte2, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothHidDeviceCallback");
          if (paramBluetoothDevice != null)
          {
            localParcel1.writeInt(1);
            paramBluetoothDevice.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeByte(paramByte1);
          localParcel1.writeByte(paramByte2);
          localParcel1.writeInt(paramInt);
          mRemote.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void onInterruptData(BluetoothDevice paramBluetoothDevice, byte paramByte, byte[] paramArrayOfByte)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothHidDeviceCallback");
          if (paramBluetoothDevice != null)
          {
            localParcel1.writeInt(1);
            paramBluetoothDevice.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeByte(paramByte);
          localParcel1.writeByteArray(paramArrayOfByte);
          mRemote.transact(6, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void onSetProtocol(BluetoothDevice paramBluetoothDevice, byte paramByte)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothHidDeviceCallback");
          if (paramBluetoothDevice != null)
          {
            localParcel1.writeInt(1);
            paramBluetoothDevice.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeByte(paramByte);
          mRemote.transact(5, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void onSetReport(BluetoothDevice paramBluetoothDevice, byte paramByte1, byte paramByte2, byte[] paramArrayOfByte)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothHidDeviceCallback");
          if (paramBluetoothDevice != null)
          {
            localParcel1.writeInt(1);
            paramBluetoothDevice.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeByte(paramByte1);
          localParcel1.writeByte(paramByte2);
          localParcel1.writeByteArray(paramArrayOfByte);
          mRemote.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void onVirtualCableUnplug(BluetoothDevice paramBluetoothDevice)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothHidDeviceCallback");
          if (paramBluetoothDevice != null)
          {
            localParcel1.writeInt(1);
            paramBluetoothDevice.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(7, localParcel1, localParcel2, 0);
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
