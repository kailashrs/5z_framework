package android.bluetooth;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import java.util.ArrayList;
import java.util.List;

public abstract interface IBluetoothPbapClient
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
  
  public abstract int getPriority(BluetoothDevice paramBluetoothDevice)
    throws RemoteException;
  
  public abstract boolean setPriority(BluetoothDevice paramBluetoothDevice, int paramInt)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IBluetoothPbapClient
  {
    private static final String DESCRIPTOR = "android.bluetooth.IBluetoothPbapClient";
    static final int TRANSACTION_connect = 1;
    static final int TRANSACTION_disconnect = 2;
    static final int TRANSACTION_getConnectedDevices = 3;
    static final int TRANSACTION_getConnectionState = 5;
    static final int TRANSACTION_getDevicesMatchingConnectionStates = 4;
    static final int TRANSACTION_getPriority = 7;
    static final int TRANSACTION_setPriority = 6;
    
    public Stub()
    {
      attachInterface(this, "android.bluetooth.IBluetoothPbapClient");
    }
    
    public static IBluetoothPbapClient asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.bluetooth.IBluetoothPbapClient");
      if ((localIInterface != null) && ((localIInterface instanceof IBluetoothPbapClient))) {
        return (IBluetoothPbapClient)localIInterface;
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
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 7: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothPbapClient");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = (Parcel)localObject5;
          }
          paramInt1 = getPriority(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothPbapClient");
          if (paramParcel1.readInt() != 0) {
            localObject5 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject5 = localObject1;
          }
          paramInt1 = setPriority((BluetoothDevice)localObject5, paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothPbapClient");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject2;
          }
          paramInt1 = getConnectionState(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothPbapClient");
          paramParcel1 = getDevicesMatchingConnectionStates(paramParcel1.createIntArray());
          paramParcel2.writeNoException();
          paramParcel2.writeTypedList(paramParcel1);
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothPbapClient");
          paramParcel1 = getConnectedDevices();
          paramParcel2.writeNoException();
          paramParcel2.writeTypedList(paramParcel1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothPbapClient");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject3;
          }
          paramInt1 = disconnect(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        }
        paramParcel1.enforceInterface("android.bluetooth.IBluetoothPbapClient");
        if (paramParcel1.readInt() != 0) {
          paramParcel1 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
        } else {
          paramParcel1 = localObject4;
        }
        paramInt1 = connect(paramParcel1);
        paramParcel2.writeNoException();
        paramParcel2.writeInt(paramInt1);
        return true;
      }
      paramParcel2.writeString("android.bluetooth.IBluetoothPbapClient");
      return true;
    }
    
    private static class Proxy
      implements IBluetoothPbapClient
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
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothPbapClient");
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
      
      public boolean disconnect(BluetoothDevice paramBluetoothDevice)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothPbapClient");
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
      
      public List<BluetoothDevice> getConnectedDevices()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothPbapClient");
          mRemote.transact(3, localParcel1, localParcel2, 0);
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
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothPbapClient");
          if (paramBluetoothDevice != null)
          {
            localParcel1.writeInt(1);
            paramBluetoothDevice.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(5, localParcel1, localParcel2, 0);
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
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothPbapClient");
          localParcel1.writeIntArray(paramArrayOfInt);
          mRemote.transact(4, localParcel1, localParcel2, 0);
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
        return "android.bluetooth.IBluetoothPbapClient";
      }
      
      public int getPriority(BluetoothDevice paramBluetoothDevice)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothPbapClient");
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
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean setPriority(BluetoothDevice paramBluetoothDevice, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothPbapClient");
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
          mRemote.transact(6, localParcel1, localParcel2, 0);
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
    }
  }
}
