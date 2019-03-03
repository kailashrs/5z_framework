package android.bluetooth;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

public abstract interface IBluetoothGattCallback
  extends IInterface
{
  public abstract void onCharacteristicRead(String paramString, int paramInt1, int paramInt2, byte[] paramArrayOfByte)
    throws RemoteException;
  
  public abstract void onCharacteristicWrite(String paramString, int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void onClientConnectionState(int paramInt1, int paramInt2, boolean paramBoolean, String paramString)
    throws RemoteException;
  
  public abstract void onClientRegistered(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void onConfigureMTU(String paramString, int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void onConnectionUpdated(String paramString, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    throws RemoteException;
  
  public abstract void onDescriptorRead(String paramString, int paramInt1, int paramInt2, byte[] paramArrayOfByte)
    throws RemoteException;
  
  public abstract void onDescriptorWrite(String paramString, int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void onExecuteWrite(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract void onNotify(String paramString, int paramInt, byte[] paramArrayOfByte)
    throws RemoteException;
  
  public abstract void onPhyRead(String paramString, int paramInt1, int paramInt2, int paramInt3)
    throws RemoteException;
  
  public abstract void onPhyUpdate(String paramString, int paramInt1, int paramInt2, int paramInt3)
    throws RemoteException;
  
  public abstract void onReadRemoteRssi(String paramString, int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void onSearchComplete(String paramString, List<BluetoothGattService> paramList, int paramInt)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IBluetoothGattCallback
  {
    private static final String DESCRIPTOR = "android.bluetooth.IBluetoothGattCallback";
    static final int TRANSACTION_onCharacteristicRead = 6;
    static final int TRANSACTION_onCharacteristicWrite = 7;
    static final int TRANSACTION_onClientConnectionState = 2;
    static final int TRANSACTION_onClientRegistered = 1;
    static final int TRANSACTION_onConfigureMTU = 13;
    static final int TRANSACTION_onConnectionUpdated = 14;
    static final int TRANSACTION_onDescriptorRead = 9;
    static final int TRANSACTION_onDescriptorWrite = 10;
    static final int TRANSACTION_onExecuteWrite = 8;
    static final int TRANSACTION_onNotify = 11;
    static final int TRANSACTION_onPhyRead = 4;
    static final int TRANSACTION_onPhyUpdate = 3;
    static final int TRANSACTION_onReadRemoteRssi = 12;
    static final int TRANSACTION_onSearchComplete = 5;
    
    public Stub()
    {
      attachInterface(this, "android.bluetooth.IBluetoothGattCallback");
    }
    
    public static IBluetoothGattCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.bluetooth.IBluetoothGattCallback");
      if ((localIInterface != null) && ((localIInterface instanceof IBluetoothGattCallback))) {
        return (IBluetoothGattCallback)localIInterface;
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
        case 14: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothGattCallback");
          onConnectionUpdated(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt());
          return true;
        case 13: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothGattCallback");
          onConfigureMTU(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt());
          return true;
        case 12: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothGattCallback");
          onReadRemoteRssi(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt());
          return true;
        case 11: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothGattCallback");
          onNotify(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.createByteArray());
          return true;
        case 10: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothGattCallback");
          onDescriptorWrite(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt());
          return true;
        case 9: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothGattCallback");
          onDescriptorRead(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.createByteArray());
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothGattCallback");
          onExecuteWrite(paramParcel1.readString(), paramParcel1.readInt());
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothGattCallback");
          onCharacteristicWrite(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt());
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothGattCallback");
          onCharacteristicRead(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.createByteArray());
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothGattCallback");
          onSearchComplete(paramParcel1.readString(), paramParcel1.createTypedArrayList(BluetoothGattService.CREATOR), paramParcel1.readInt());
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothGattCallback");
          onPhyRead(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt());
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothGattCallback");
          onPhyUpdate(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt());
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothGattCallback");
          paramInt2 = paramParcel1.readInt();
          paramInt1 = paramParcel1.readInt();
          boolean bool;
          if (paramParcel1.readInt() != 0) {
            bool = true;
          } else {
            bool = false;
          }
          onClientConnectionState(paramInt2, paramInt1, bool, paramParcel1.readString());
          return true;
        }
        paramParcel1.enforceInterface("android.bluetooth.IBluetoothGattCallback");
        onClientRegistered(paramParcel1.readInt(), paramParcel1.readInt());
        return true;
      }
      paramParcel2.writeString("android.bluetooth.IBluetoothGattCallback");
      return true;
    }
    
    private static class Proxy
      implements IBluetoothGattCallback
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
        return "android.bluetooth.IBluetoothGattCallback";
      }
      
      public void onCharacteristicRead(String paramString, int paramInt1, int paramInt2, byte[] paramArrayOfByte)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.bluetooth.IBluetoothGattCallback");
          localParcel.writeString(paramString);
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          localParcel.writeByteArray(paramArrayOfByte);
          mRemote.transact(6, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onCharacteristicWrite(String paramString, int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.bluetooth.IBluetoothGattCallback");
          localParcel.writeString(paramString);
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          mRemote.transact(7, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onClientConnectionState(int paramInt1, int paramInt2, boolean paramBoolean, String paramString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.bluetooth.IBluetoothGattCallback");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          localParcel.writeInt(paramBoolean);
          localParcel.writeString(paramString);
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onClientRegistered(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.bluetooth.IBluetoothGattCallback");
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
      
      public void onConfigureMTU(String paramString, int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.bluetooth.IBluetoothGattCallback");
          localParcel.writeString(paramString);
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          mRemote.transact(13, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onConnectionUpdated(String paramString, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.bluetooth.IBluetoothGattCallback");
          localParcel.writeString(paramString);
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          localParcel.writeInt(paramInt3);
          localParcel.writeInt(paramInt4);
          mRemote.transact(14, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onDescriptorRead(String paramString, int paramInt1, int paramInt2, byte[] paramArrayOfByte)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.bluetooth.IBluetoothGattCallback");
          localParcel.writeString(paramString);
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          localParcel.writeByteArray(paramArrayOfByte);
          mRemote.transact(9, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onDescriptorWrite(String paramString, int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.bluetooth.IBluetoothGattCallback");
          localParcel.writeString(paramString);
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          mRemote.transact(10, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onExecuteWrite(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.bluetooth.IBluetoothGattCallback");
          localParcel.writeString(paramString);
          localParcel.writeInt(paramInt);
          mRemote.transact(8, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onNotify(String paramString, int paramInt, byte[] paramArrayOfByte)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.bluetooth.IBluetoothGattCallback");
          localParcel.writeString(paramString);
          localParcel.writeInt(paramInt);
          localParcel.writeByteArray(paramArrayOfByte);
          mRemote.transact(11, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onPhyRead(String paramString, int paramInt1, int paramInt2, int paramInt3)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.bluetooth.IBluetoothGattCallback");
          localParcel.writeString(paramString);
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          localParcel.writeInt(paramInt3);
          mRemote.transact(4, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onPhyUpdate(String paramString, int paramInt1, int paramInt2, int paramInt3)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.bluetooth.IBluetoothGattCallback");
          localParcel.writeString(paramString);
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          localParcel.writeInt(paramInt3);
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onReadRemoteRssi(String paramString, int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.bluetooth.IBluetoothGattCallback");
          localParcel.writeString(paramString);
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          mRemote.transact(12, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onSearchComplete(String paramString, List<BluetoothGattService> paramList, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.bluetooth.IBluetoothGattCallback");
          localParcel.writeString(paramString);
          localParcel.writeTypedList(paramList);
          localParcel.writeInt(paramInt);
          mRemote.transact(5, localParcel, null, 1);
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
