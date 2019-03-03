package android.bluetooth;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IBluetoothGattServerCallback
  extends IInterface
{
  public abstract void onCharacteristicReadRequest(String paramString, int paramInt1, int paramInt2, boolean paramBoolean, int paramInt3)
    throws RemoteException;
  
  public abstract void onCharacteristicWriteRequest(String paramString, int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean1, boolean paramBoolean2, int paramInt4, byte[] paramArrayOfByte)
    throws RemoteException;
  
  public abstract void onConnectionUpdated(String paramString, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    throws RemoteException;
  
  public abstract void onDescriptorReadRequest(String paramString, int paramInt1, int paramInt2, boolean paramBoolean, int paramInt3)
    throws RemoteException;
  
  public abstract void onDescriptorWriteRequest(String paramString, int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean1, boolean paramBoolean2, int paramInt4, byte[] paramArrayOfByte)
    throws RemoteException;
  
  public abstract void onExecuteWrite(String paramString, int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void onMtuChanged(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract void onNotificationSent(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract void onPhyRead(String paramString, int paramInt1, int paramInt2, int paramInt3)
    throws RemoteException;
  
  public abstract void onPhyUpdate(String paramString, int paramInt1, int paramInt2, int paramInt3)
    throws RemoteException;
  
  public abstract void onServerConnectionState(int paramInt1, int paramInt2, boolean paramBoolean, String paramString)
    throws RemoteException;
  
  public abstract void onServerRegistered(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void onServiceAdded(int paramInt, BluetoothGattService paramBluetoothGattService)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IBluetoothGattServerCallback
  {
    private static final String DESCRIPTOR = "android.bluetooth.IBluetoothGattServerCallback";
    static final int TRANSACTION_onCharacteristicReadRequest = 4;
    static final int TRANSACTION_onCharacteristicWriteRequest = 6;
    static final int TRANSACTION_onConnectionUpdated = 13;
    static final int TRANSACTION_onDescriptorReadRequest = 5;
    static final int TRANSACTION_onDescriptorWriteRequest = 7;
    static final int TRANSACTION_onExecuteWrite = 8;
    static final int TRANSACTION_onMtuChanged = 10;
    static final int TRANSACTION_onNotificationSent = 9;
    static final int TRANSACTION_onPhyRead = 12;
    static final int TRANSACTION_onPhyUpdate = 11;
    static final int TRANSACTION_onServerConnectionState = 2;
    static final int TRANSACTION_onServerRegistered = 1;
    static final int TRANSACTION_onServiceAdded = 3;
    
    public Stub()
    {
      attachInterface(this, "android.bluetooth.IBluetoothGattServerCallback");
    }
    
    public static IBluetoothGattServerCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.bluetooth.IBluetoothGattServerCallback");
      if ((localIInterface != null) && ((localIInterface instanceof IBluetoothGattServerCallback))) {
        return (IBluetoothGattServerCallback)localIInterface;
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
        boolean bool1 = false;
        boolean bool2 = false;
        int i;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 13: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothGattServerCallback");
          onConnectionUpdated(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt());
          return true;
        case 12: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothGattServerCallback");
          onPhyRead(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt());
          return true;
        case 11: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothGattServerCallback");
          onPhyUpdate(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt());
          return true;
        case 10: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothGattServerCallback");
          onMtuChanged(paramParcel1.readString(), paramParcel1.readInt());
          return true;
        case 9: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothGattServerCallback");
          onNotificationSent(paramParcel1.readString(), paramParcel1.readInt());
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothGattServerCallback");
          paramParcel2 = paramParcel1.readString();
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            bool2 = true;
          }
          onExecuteWrite(paramParcel2, paramInt1, bool2);
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothGattServerCallback");
          paramParcel2 = paramParcel1.readString();
          paramInt1 = paramParcel1.readInt();
          i = paramParcel1.readInt();
          paramInt2 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            bool2 = true;
          } else {
            bool2 = false;
          }
          if (paramParcel1.readInt() != 0) {
            bool1 = true;
          } else {
            bool1 = false;
          }
          onDescriptorWriteRequest(paramParcel2, paramInt1, i, paramInt2, bool2, bool1, paramParcel1.readInt(), paramParcel1.createByteArray());
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothGattServerCallback");
          paramParcel2 = paramParcel1.readString();
          paramInt2 = paramParcel1.readInt();
          i = paramParcel1.readInt();
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            bool2 = true;
          } else {
            bool2 = false;
          }
          if (paramParcel1.readInt() != 0) {
            bool1 = true;
          } else {
            bool1 = false;
          }
          onCharacteristicWriteRequest(paramParcel2, paramInt2, i, paramInt1, bool2, bool1, paramParcel1.readInt(), paramParcel1.createByteArray());
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothGattServerCallback");
          paramParcel2 = paramParcel1.readString();
          paramInt1 = paramParcel1.readInt();
          paramInt2 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            bool2 = true;
          } else {
            bool2 = false;
          }
          onDescriptorReadRequest(paramParcel2, paramInt1, paramInt2, bool2, paramParcel1.readInt());
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothGattServerCallback");
          paramParcel2 = paramParcel1.readString();
          paramInt1 = paramParcel1.readInt();
          paramInt2 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            bool2 = true;
          } else {
            bool2 = false;
          }
          onCharacteristicReadRequest(paramParcel2, paramInt1, paramInt2, bool2, paramParcel1.readInt());
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothGattServerCallback");
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (BluetoothGattService)BluetoothGattService.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = null;
          }
          onServiceAdded(paramInt1, paramParcel1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothGattServerCallback");
          paramInt1 = paramParcel1.readInt();
          paramInt2 = paramParcel1.readInt();
          bool2 = bool1;
          if (paramParcel1.readInt() != 0) {
            bool2 = true;
          }
          onServerConnectionState(paramInt1, paramInt2, bool2, paramParcel1.readString());
          return true;
        }
        paramParcel1.enforceInterface("android.bluetooth.IBluetoothGattServerCallback");
        onServerRegistered(paramParcel1.readInt(), paramParcel1.readInt());
        return true;
      }
      paramParcel2.writeString("android.bluetooth.IBluetoothGattServerCallback");
      return true;
    }
    
    private static class Proxy
      implements IBluetoothGattServerCallback
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
        return "android.bluetooth.IBluetoothGattServerCallback";
      }
      
      public void onCharacteristicReadRequest(String paramString, int paramInt1, int paramInt2, boolean paramBoolean, int paramInt3)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.bluetooth.IBluetoothGattServerCallback");
          localParcel.writeString(paramString);
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          localParcel.writeInt(paramBoolean);
          localParcel.writeInt(paramInt3);
          mRemote.transact(4, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onCharacteristicWriteRequest(String paramString, int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean1, boolean paramBoolean2, int paramInt4, byte[] paramArrayOfByte)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.bluetooth.IBluetoothGattServerCallback");
          localParcel.writeString(paramString);
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          localParcel.writeInt(paramInt3);
          localParcel.writeInt(paramBoolean1);
          localParcel.writeInt(paramBoolean2);
          localParcel.writeInt(paramInt4);
          localParcel.writeByteArray(paramArrayOfByte);
          mRemote.transact(6, localParcel, null, 1);
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
          localParcel.writeInterfaceToken("android.bluetooth.IBluetoothGattServerCallback");
          localParcel.writeString(paramString);
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          localParcel.writeInt(paramInt3);
          localParcel.writeInt(paramInt4);
          mRemote.transact(13, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onDescriptorReadRequest(String paramString, int paramInt1, int paramInt2, boolean paramBoolean, int paramInt3)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.bluetooth.IBluetoothGattServerCallback");
          localParcel.writeString(paramString);
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          localParcel.writeInt(paramBoolean);
          localParcel.writeInt(paramInt3);
          mRemote.transact(5, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onDescriptorWriteRequest(String paramString, int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean1, boolean paramBoolean2, int paramInt4, byte[] paramArrayOfByte)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.bluetooth.IBluetoothGattServerCallback");
          localParcel.writeString(paramString);
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          localParcel.writeInt(paramInt3);
          localParcel.writeInt(paramBoolean1);
          localParcel.writeInt(paramBoolean2);
          localParcel.writeInt(paramInt4);
          localParcel.writeByteArray(paramArrayOfByte);
          mRemote.transact(7, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onExecuteWrite(String paramString, int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.bluetooth.IBluetoothGattServerCallback");
          localParcel.writeString(paramString);
          localParcel.writeInt(paramInt);
          localParcel.writeInt(paramBoolean);
          mRemote.transact(8, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onMtuChanged(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.bluetooth.IBluetoothGattServerCallback");
          localParcel.writeString(paramString);
          localParcel.writeInt(paramInt);
          mRemote.transact(10, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onNotificationSent(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.bluetooth.IBluetoothGattServerCallback");
          localParcel.writeString(paramString);
          localParcel.writeInt(paramInt);
          mRemote.transact(9, localParcel, null, 1);
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
          localParcel.writeInterfaceToken("android.bluetooth.IBluetoothGattServerCallback");
          localParcel.writeString(paramString);
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          localParcel.writeInt(paramInt3);
          mRemote.transact(12, localParcel, null, 1);
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
          localParcel.writeInterfaceToken("android.bluetooth.IBluetoothGattServerCallback");
          localParcel.writeString(paramString);
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          localParcel.writeInt(paramInt3);
          mRemote.transact(11, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onServerConnectionState(int paramInt1, int paramInt2, boolean paramBoolean, String paramString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.bluetooth.IBluetoothGattServerCallback");
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
      
      public void onServerRegistered(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.bluetooth.IBluetoothGattServerCallback");
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
      
      public void onServiceAdded(int paramInt, BluetoothGattService paramBluetoothGattService)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.bluetooth.IBluetoothGattServerCallback");
          localParcel.writeInt(paramInt);
          if (paramBluetoothGattService != null)
          {
            localParcel.writeInt(1);
            paramBluetoothGattService.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
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
