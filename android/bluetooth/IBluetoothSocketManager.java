package android.bluetooth;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.ParcelUuid;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IBluetoothSocketManager
  extends IInterface
{
  public abstract ParcelFileDescriptor connectSocket(BluetoothDevice paramBluetoothDevice, int paramInt1, ParcelUuid paramParcelUuid, int paramInt2, int paramInt3)
    throws RemoteException;
  
  public abstract ParcelFileDescriptor createSocketChannel(int paramInt1, String paramString, ParcelUuid paramParcelUuid, int paramInt2, int paramInt3)
    throws RemoteException;
  
  public abstract void requestMaximumTxDataLength(BluetoothDevice paramBluetoothDevice)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IBluetoothSocketManager
  {
    private static final String DESCRIPTOR = "android.bluetooth.IBluetoothSocketManager";
    static final int TRANSACTION_connectSocket = 1;
    static final int TRANSACTION_createSocketChannel = 2;
    static final int TRANSACTION_requestMaximumTxDataLength = 3;
    
    public Stub()
    {
      attachInterface(this, "android.bluetooth.IBluetoothSocketManager");
    }
    
    public static IBluetoothSocketManager asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.bluetooth.IBluetoothSocketManager");
      if ((localIInterface != null) && ((localIInterface instanceof IBluetoothSocketManager))) {
        return (IBluetoothSocketManager)localIInterface;
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
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 3: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothSocketManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject3;
          }
          requestMaximumTxDataLength(paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothSocketManager");
          paramInt1 = paramParcel1.readInt();
          localObject2 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            localObject1 = (ParcelUuid)ParcelUuid.CREATOR.createFromParcel(paramParcel1);
          }
          for (;;)
          {
            break;
          }
          paramParcel1 = createSocketChannel(paramInt1, (String)localObject2, (ParcelUuid)localObject1, paramParcel1.readInt(), paramParcel1.readInt());
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
        }
        paramParcel1.enforceInterface("android.bluetooth.IBluetoothSocketManager");
        if (paramParcel1.readInt() != 0) {
          localObject1 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
        } else {
          localObject1 = null;
        }
        paramInt1 = paramParcel1.readInt();
        if (paramParcel1.readInt() != 0) {
          localObject2 = (ParcelUuid)ParcelUuid.CREATOR.createFromParcel(paramParcel1);
        }
        for (;;)
        {
          break;
        }
        paramParcel1 = connectSocket((BluetoothDevice)localObject1, paramInt1, (ParcelUuid)localObject2, paramParcel1.readInt(), paramParcel1.readInt());
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
      }
      paramParcel2.writeString("android.bluetooth.IBluetoothSocketManager");
      return true;
    }
    
    private static class Proxy
      implements IBluetoothSocketManager
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
      
      public ParcelFileDescriptor connectSocket(BluetoothDevice paramBluetoothDevice, int paramInt1, ParcelUuid paramParcelUuid, int paramInt2, int paramInt3)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothSocketManager");
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
          if (paramParcelUuid != null)
          {
            localParcel1.writeInt(1);
            paramParcelUuid.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
          mRemote.transact(1, localParcel1, localParcel2, 0);
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
      
      public ParcelFileDescriptor createSocketChannel(int paramInt1, String paramString, ParcelUuid paramParcelUuid, int paramInt2, int paramInt3)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothSocketManager");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeString(paramString);
          if (paramParcelUuid != null)
          {
            localParcel1.writeInt(1);
            paramParcelUuid.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
          mRemote.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString = (ParcelFileDescriptor)ParcelFileDescriptor.CREATOR.createFromParcel(localParcel2);
          } else {
            paramString = null;
          }
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.bluetooth.IBluetoothSocketManager";
      }
      
      public void requestMaximumTxDataLength(BluetoothDevice paramBluetoothDevice)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothSocketManager");
          if (paramBluetoothDevice != null)
          {
            localParcel1.writeInt(1);
            paramBluetoothDevice.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
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
    }
  }
}
