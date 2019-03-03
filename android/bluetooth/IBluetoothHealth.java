package android.bluetooth;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import java.util.ArrayList;
import java.util.List;

public abstract interface IBluetoothHealth
  extends IInterface
{
  public abstract boolean connectChannelToSink(BluetoothDevice paramBluetoothDevice, BluetoothHealthAppConfiguration paramBluetoothHealthAppConfiguration, int paramInt)
    throws RemoteException;
  
  public abstract boolean connectChannelToSource(BluetoothDevice paramBluetoothDevice, BluetoothHealthAppConfiguration paramBluetoothHealthAppConfiguration)
    throws RemoteException;
  
  public abstract boolean disconnectChannel(BluetoothDevice paramBluetoothDevice, BluetoothHealthAppConfiguration paramBluetoothHealthAppConfiguration, int paramInt)
    throws RemoteException;
  
  public abstract List<BluetoothDevice> getConnectedHealthDevices()
    throws RemoteException;
  
  public abstract int getHealthDeviceConnectionState(BluetoothDevice paramBluetoothDevice)
    throws RemoteException;
  
  public abstract List<BluetoothDevice> getHealthDevicesMatchingConnectionStates(int[] paramArrayOfInt)
    throws RemoteException;
  
  public abstract ParcelFileDescriptor getMainChannelFd(BluetoothDevice paramBluetoothDevice, BluetoothHealthAppConfiguration paramBluetoothHealthAppConfiguration)
    throws RemoteException;
  
  public abstract boolean registerAppConfiguration(BluetoothHealthAppConfiguration paramBluetoothHealthAppConfiguration, IBluetoothHealthCallback paramIBluetoothHealthCallback)
    throws RemoteException;
  
  public abstract boolean unregisterAppConfiguration(BluetoothHealthAppConfiguration paramBluetoothHealthAppConfiguration)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IBluetoothHealth
  {
    private static final String DESCRIPTOR = "android.bluetooth.IBluetoothHealth";
    static final int TRANSACTION_connectChannelToSink = 4;
    static final int TRANSACTION_connectChannelToSource = 3;
    static final int TRANSACTION_disconnectChannel = 5;
    static final int TRANSACTION_getConnectedHealthDevices = 7;
    static final int TRANSACTION_getHealthDeviceConnectionState = 9;
    static final int TRANSACTION_getHealthDevicesMatchingConnectionStates = 8;
    static final int TRANSACTION_getMainChannelFd = 6;
    static final int TRANSACTION_registerAppConfiguration = 1;
    static final int TRANSACTION_unregisterAppConfiguration = 2;
    
    public Stub()
    {
      attachInterface(this, "android.bluetooth.IBluetoothHealth");
    }
    
    public static IBluetoothHealth asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.bluetooth.IBluetoothHealth");
      if ((localIInterface != null) && ((localIInterface instanceof IBluetoothHealth))) {
        return (IBluetoothHealth)localIInterface;
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
        case 9: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothHealth");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = (Parcel)localObject7;
          }
          paramInt1 = getHealthDeviceConnectionState(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothHealth");
          paramParcel1 = getHealthDevicesMatchingConnectionStates(paramParcel1.createIntArray());
          paramParcel2.writeNoException();
          paramParcel2.writeTypedList(paramParcel1);
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothHealth");
          paramParcel1 = getConnectedHealthDevices();
          paramParcel2.writeNoException();
          paramParcel2.writeTypedList(paramParcel1);
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothHealth");
          if (paramParcel1.readInt() != 0) {
            localObject7 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject7 = null;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (BluetoothHealthAppConfiguration)BluetoothHealthAppConfiguration.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = (Parcel)localObject1;
          }
          paramParcel1 = getMainChannelFd((BluetoothDevice)localObject7, paramParcel1);
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothHealth");
          if (paramParcel1.readInt() != 0) {
            localObject7 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject7 = null;
          }
          if (paramParcel1.readInt() != 0) {
            localObject1 = (BluetoothHealthAppConfiguration)BluetoothHealthAppConfiguration.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject1 = localObject2;
          }
          paramInt1 = disconnectChannel((BluetoothDevice)localObject7, (BluetoothHealthAppConfiguration)localObject1, paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothHealth");
          if (paramParcel1.readInt() != 0) {
            localObject7 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject7 = null;
          }
          if (paramParcel1.readInt() != 0) {
            localObject1 = (BluetoothHealthAppConfiguration)BluetoothHealthAppConfiguration.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject1 = localObject3;
          }
          paramInt1 = connectChannelToSink((BluetoothDevice)localObject7, (BluetoothHealthAppConfiguration)localObject1, paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothHealth");
          if (paramParcel1.readInt() != 0) {
            localObject7 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject7 = null;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (BluetoothHealthAppConfiguration)BluetoothHealthAppConfiguration.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject4;
          }
          paramInt1 = connectChannelToSource((BluetoothDevice)localObject7, paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothHealth");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (BluetoothHealthAppConfiguration)BluetoothHealthAppConfiguration.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject5;
          }
          paramInt1 = unregisterAppConfiguration(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        }
        paramParcel1.enforceInterface("android.bluetooth.IBluetoothHealth");
        if (paramParcel1.readInt() != 0) {
          localObject7 = (BluetoothHealthAppConfiguration)BluetoothHealthAppConfiguration.CREATOR.createFromParcel(paramParcel1);
        } else {
          localObject7 = localObject6;
        }
        paramInt1 = registerAppConfiguration((BluetoothHealthAppConfiguration)localObject7, IBluetoothHealthCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
        paramParcel2.writeNoException();
        paramParcel2.writeInt(paramInt1);
        return true;
      }
      paramParcel2.writeString("android.bluetooth.IBluetoothHealth");
      return true;
    }
    
    private static class Proxy
      implements IBluetoothHealth
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
      
      public boolean connectChannelToSink(BluetoothDevice paramBluetoothDevice, BluetoothHealthAppConfiguration paramBluetoothHealthAppConfiguration, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothHealth");
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
          mRemote.transact(4, localParcel1, localParcel2, 0);
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
      
      public boolean connectChannelToSource(BluetoothDevice paramBluetoothDevice, BluetoothHealthAppConfiguration paramBluetoothHealthAppConfiguration)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothHealth");
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
          if (paramBluetoothHealthAppConfiguration != null)
          {
            localParcel1.writeInt(1);
            paramBluetoothHealthAppConfiguration.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(3, localParcel1, localParcel2, 0);
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
      
      public boolean disconnectChannel(BluetoothDevice paramBluetoothDevice, BluetoothHealthAppConfiguration paramBluetoothHealthAppConfiguration, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothHealth");
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
          mRemote.transact(5, localParcel1, localParcel2, 0);
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
      
      public List<BluetoothDevice> getConnectedHealthDevices()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothHealth");
          mRemote.transact(7, localParcel1, localParcel2, 0);
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
      
      public int getHealthDeviceConnectionState(BluetoothDevice paramBluetoothDevice)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothHealth");
          if (paramBluetoothDevice != null)
          {
            localParcel1.writeInt(1);
            paramBluetoothDevice.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(9, localParcel1, localParcel2, 0);
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
      
      public List<BluetoothDevice> getHealthDevicesMatchingConnectionStates(int[] paramArrayOfInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothHealth");
          localParcel1.writeIntArray(paramArrayOfInt);
          mRemote.transact(8, localParcel1, localParcel2, 0);
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
        return "android.bluetooth.IBluetoothHealth";
      }
      
      public ParcelFileDescriptor getMainChannelFd(BluetoothDevice paramBluetoothDevice, BluetoothHealthAppConfiguration paramBluetoothHealthAppConfiguration)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothHealth");
          if (paramBluetoothDevice != null)
          {
            localParcel1.writeInt(1);
            paramBluetoothDevice.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramBluetoothHealthAppConfiguration != null)
          {
            localParcel1.writeInt(1);
            paramBluetoothHealthAppConfiguration.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(6, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramBluetoothDevice = (ParcelFileDescriptor)ParcelFileDescriptor.CREATOR.createFromParcel(localParcel2);
          } else {
            paramBluetoothDevice = null;
          }
          return paramBluetoothDevice;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean registerAppConfiguration(BluetoothHealthAppConfiguration paramBluetoothHealthAppConfiguration, IBluetoothHealthCallback paramIBluetoothHealthCallback)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothHealth");
          boolean bool = true;
          if (paramBluetoothHealthAppConfiguration != null)
          {
            localParcel1.writeInt(1);
            paramBluetoothHealthAppConfiguration.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramIBluetoothHealthCallback != null) {
            paramBluetoothHealthAppConfiguration = paramIBluetoothHealthCallback.asBinder();
          } else {
            paramBluetoothHealthAppConfiguration = null;
          }
          localParcel1.writeStrongBinder(paramBluetoothHealthAppConfiguration);
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
      
      public boolean unregisterAppConfiguration(BluetoothHealthAppConfiguration paramBluetoothHealthAppConfiguration)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothHealth");
          boolean bool = true;
          if (paramBluetoothHealthAppConfiguration != null)
          {
            localParcel1.writeInt(1);
            paramBluetoothHealthAppConfiguration.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(2, localParcel1, localParcel2, 0);
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
    }
  }
}
