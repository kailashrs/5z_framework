package android.bluetooth;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import java.util.ArrayList;
import java.util.List;

public abstract interface IBluetoothHidDevice
  extends IInterface
{
  public abstract boolean connect(BluetoothDevice paramBluetoothDevice)
    throws RemoteException;
  
  public abstract boolean disconnect(BluetoothDevice paramBluetoothDevice)
    throws RemoteException;
  
  public abstract List<BluetoothDevice> getConnectedDevices()
    throws RemoteException;
  
  public abstract int getConnectionState(BluetoothDevice paramBluetoothDevice)
    throws RemoteException;
  
  public abstract List<BluetoothDevice> getDevicesMatchingConnectionStates(int[] paramArrayOfInt)
    throws RemoteException;
  
  public abstract String getUserAppName()
    throws RemoteException;
  
  public abstract boolean registerApp(BluetoothHidDeviceAppSdpSettings paramBluetoothHidDeviceAppSdpSettings, BluetoothHidDeviceAppQosSettings paramBluetoothHidDeviceAppQosSettings1, BluetoothHidDeviceAppQosSettings paramBluetoothHidDeviceAppQosSettings2, IBluetoothHidDeviceCallback paramIBluetoothHidDeviceCallback)
    throws RemoteException;
  
  public abstract boolean replyReport(BluetoothDevice paramBluetoothDevice, byte paramByte1, byte paramByte2, byte[] paramArrayOfByte)
    throws RemoteException;
  
  public abstract boolean reportError(BluetoothDevice paramBluetoothDevice, byte paramByte)
    throws RemoteException;
  
  public abstract boolean sendReport(BluetoothDevice paramBluetoothDevice, int paramInt, byte[] paramArrayOfByte)
    throws RemoteException;
  
  public abstract boolean unplug(BluetoothDevice paramBluetoothDevice)
    throws RemoteException;
  
  public abstract boolean unregisterApp()
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IBluetoothHidDevice
  {
    private static final String DESCRIPTOR = "android.bluetooth.IBluetoothHidDevice";
    static final int TRANSACTION_connect = 7;
    static final int TRANSACTION_disconnect = 8;
    static final int TRANSACTION_getConnectedDevices = 9;
    static final int TRANSACTION_getConnectionState = 11;
    static final int TRANSACTION_getDevicesMatchingConnectionStates = 10;
    static final int TRANSACTION_getUserAppName = 12;
    static final int TRANSACTION_registerApp = 1;
    static final int TRANSACTION_replyReport = 4;
    static final int TRANSACTION_reportError = 5;
    static final int TRANSACTION_sendReport = 3;
    static final int TRANSACTION_unplug = 6;
    static final int TRANSACTION_unregisterApp = 2;
    
    public Stub()
    {
      attachInterface(this, "android.bluetooth.IBluetoothHidDevice");
    }
    
    public static IBluetoothHidDevice asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.bluetooth.IBluetoothHidDevice");
      if ((localIInterface != null) && ((localIInterface instanceof IBluetoothHidDevice))) {
        return (IBluetoothHidDevice)localIInterface;
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
        BluetoothHidDeviceAppQosSettings localBluetoothHidDeviceAppQosSettings1 = null;
        Object localObject1 = null;
        Object localObject2 = null;
        Object localObject3 = null;
        Object localObject4 = null;
        Object localObject5 = null;
        BluetoothHidDeviceAppQosSettings localBluetoothHidDeviceAppQosSettings2 = null;
        Object localObject6 = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 12: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothHidDevice");
          paramParcel1 = getUserAppName();
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 11: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothHidDevice");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = (Parcel)localObject6;
          }
          paramInt1 = getConnectionState(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 10: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothHidDevice");
          paramParcel1 = getDevicesMatchingConnectionStates(paramParcel1.createIntArray());
          paramParcel2.writeNoException();
          paramParcel2.writeTypedList(paramParcel1);
          return true;
        case 9: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothHidDevice");
          paramParcel1 = getConnectedDevices();
          paramParcel2.writeNoException();
          paramParcel2.writeTypedList(paramParcel1);
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothHidDevice");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localBluetoothHidDeviceAppQosSettings1;
          }
          paramInt1 = disconnect(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothHidDevice");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject1;
          }
          paramInt1 = connect(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothHidDevice");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject2;
          }
          paramInt1 = unplug(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothHidDevice");
          if (paramParcel1.readInt() != 0) {
            localObject6 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject6 = localObject3;
          }
          paramInt1 = reportError((BluetoothDevice)localObject6, paramParcel1.readByte());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothHidDevice");
          if (paramParcel1.readInt() != 0) {
            localObject6 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject6 = localObject4;
          }
          paramInt1 = replyReport((BluetoothDevice)localObject6, paramParcel1.readByte(), paramParcel1.readByte(), paramParcel1.createByteArray());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothHidDevice");
          if (paramParcel1.readInt() != 0) {
            localObject6 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject6 = localObject5;
          }
          paramInt1 = sendReport((BluetoothDevice)localObject6, paramParcel1.readInt(), paramParcel1.createByteArray());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothHidDevice");
          paramInt1 = unregisterApp();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        }
        paramParcel1.enforceInterface("android.bluetooth.IBluetoothHidDevice");
        if (paramParcel1.readInt() != 0) {
          localObject6 = (BluetoothHidDeviceAppSdpSettings)BluetoothHidDeviceAppSdpSettings.CREATOR.createFromParcel(paramParcel1);
        } else {
          localObject6 = null;
        }
        if (paramParcel1.readInt() != 0) {
          localBluetoothHidDeviceAppQosSettings1 = (BluetoothHidDeviceAppQosSettings)BluetoothHidDeviceAppQosSettings.CREATOR.createFromParcel(paramParcel1);
        } else {
          localBluetoothHidDeviceAppQosSettings1 = null;
        }
        if (paramParcel1.readInt() != 0) {
          localBluetoothHidDeviceAppQosSettings2 = (BluetoothHidDeviceAppQosSettings)BluetoothHidDeviceAppQosSettings.CREATOR.createFromParcel(paramParcel1);
        }
        paramInt1 = registerApp((BluetoothHidDeviceAppSdpSettings)localObject6, localBluetoothHidDeviceAppQosSettings1, localBluetoothHidDeviceAppQosSettings2, IBluetoothHidDeviceCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
        paramParcel2.writeNoException();
        paramParcel2.writeInt(paramInt1);
        return true;
      }
      paramParcel2.writeString("android.bluetooth.IBluetoothHidDevice");
      return true;
    }
    
    private static class Proxy
      implements IBluetoothHidDevice
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
      
      public boolean connect(BluetoothDevice paramBluetoothDevice)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothHidDevice");
          boolean bool = true;
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
          int i = localParcel2.readInt();
          if (i == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean disconnect(BluetoothDevice paramBluetoothDevice)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothHidDevice");
          boolean bool = true;
          if (paramBluetoothDevice != null)
          {
            localParcel1.writeInt(1);
            paramBluetoothDevice.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(8, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public List<BluetoothDevice> getConnectedDevices()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothHidDevice");
          mRemote.transact(9, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ArrayList localArrayList = localParcel2.createTypedArrayList(BluetoothDevice.CREATOR);
          return localArrayList;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getConnectionState(BluetoothDevice paramBluetoothDevice)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothHidDevice");
          if (paramBluetoothDevice != null)
          {
            localParcel1.writeInt(1);
            paramBluetoothDevice.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(11, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public List<BluetoothDevice> getDevicesMatchingConnectionStates(int[] paramArrayOfInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothHidDevice");
          localParcel1.writeIntArray(paramArrayOfInt);
          mRemote.transact(10, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramArrayOfInt = localParcel2.createTypedArrayList(BluetoothDevice.CREATOR);
          return paramArrayOfInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.bluetooth.IBluetoothHidDevice";
      }
      
      public String getUserAppName()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothHidDevice");
          mRemote.transact(12, localParcel1, localParcel2, 0);
          localParcel2.readException();
          String str = localParcel2.readString();
          return str;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean registerApp(BluetoothHidDeviceAppSdpSettings paramBluetoothHidDeviceAppSdpSettings, BluetoothHidDeviceAppQosSettings paramBluetoothHidDeviceAppQosSettings1, BluetoothHidDeviceAppQosSettings paramBluetoothHidDeviceAppQosSettings2, IBluetoothHidDeviceCallback paramIBluetoothHidDeviceCallback)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothHidDevice");
          boolean bool = true;
          if (paramBluetoothHidDeviceAppSdpSettings != null)
          {
            localParcel1.writeInt(1);
            paramBluetoothHidDeviceAppSdpSettings.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramBluetoothHidDeviceAppQosSettings1 != null)
          {
            localParcel1.writeInt(1);
            paramBluetoothHidDeviceAppQosSettings1.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramBluetoothHidDeviceAppQosSettings2 != null)
          {
            localParcel1.writeInt(1);
            paramBluetoothHidDeviceAppQosSettings2.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramIBluetoothHidDeviceCallback != null) {
            paramBluetoothHidDeviceAppSdpSettings = paramIBluetoothHidDeviceCallback.asBinder();
          } else {
            paramBluetoothHidDeviceAppSdpSettings = null;
          }
          localParcel1.writeStrongBinder(paramBluetoothHidDeviceAppSdpSettings);
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean replyReport(BluetoothDevice paramBluetoothDevice, byte paramByte1, byte paramByte2, byte[] paramArrayOfByte)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothHidDevice");
          boolean bool = true;
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
          int i = localParcel2.readInt();
          if (i == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean reportError(BluetoothDevice paramBluetoothDevice, byte paramByte)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothHidDevice");
          boolean bool = true;
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
          int i = localParcel2.readInt();
          if (i == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean sendReport(BluetoothDevice paramBluetoothDevice, int paramInt, byte[] paramArrayOfByte)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothHidDevice");
          boolean bool = true;
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
          localParcel1.writeByteArray(paramArrayOfByte);
          mRemote.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean unplug(BluetoothDevice paramBluetoothDevice)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothHidDevice");
          boolean bool = true;
          if (paramBluetoothDevice != null)
          {
            localParcel1.writeInt(1);
            paramBluetoothDevice.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(6, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean unregisterApp()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothHidDevice");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
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
