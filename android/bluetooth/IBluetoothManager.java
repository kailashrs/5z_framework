package android.bluetooth;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IBluetoothManager
  extends IInterface
{
  public abstract boolean bindBluetoothProfileService(int paramInt, IBluetoothProfileServiceConnection paramIBluetoothProfileServiceConnection)
    throws RemoteException;
  
  public abstract boolean disable(String paramString, boolean paramBoolean)
    throws RemoteException;
  
  public abstract boolean enable(String paramString)
    throws RemoteException;
  
  public abstract boolean enableNoAutoConnect(String paramString)
    throws RemoteException;
  
  public abstract boolean factoryReset()
    throws RemoteException;
  
  public abstract String getAddress()
    throws RemoteException;
  
  public abstract IBluetoothGatt getBluetoothGatt()
    throws RemoteException;
  
  public abstract String getName()
    throws RemoteException;
  
  public abstract int getState()
    throws RemoteException;
  
  public abstract boolean isBleAppPresent()
    throws RemoteException;
  
  public abstract boolean isBleScanAlwaysAvailable()
    throws RemoteException;
  
  public abstract boolean isEnabled()
    throws RemoteException;
  
  public abstract IBluetooth registerAdapter(IBluetoothManagerCallback paramIBluetoothManagerCallback)
    throws RemoteException;
  
  public abstract void registerStateChangeCallback(IBluetoothStateChangeCallback paramIBluetoothStateChangeCallback)
    throws RemoteException;
  
  public abstract void unbindBluetoothProfileService(int paramInt, IBluetoothProfileServiceConnection paramIBluetoothProfileServiceConnection)
    throws RemoteException;
  
  public abstract void unregisterAdapter(IBluetoothManagerCallback paramIBluetoothManagerCallback)
    throws RemoteException;
  
  public abstract void unregisterStateChangeCallback(IBluetoothStateChangeCallback paramIBluetoothStateChangeCallback)
    throws RemoteException;
  
  public abstract int updateBleAppCount(IBinder paramIBinder, boolean paramBoolean, String paramString)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IBluetoothManager
  {
    private static final String DESCRIPTOR = "android.bluetooth.IBluetoothManager";
    static final int TRANSACTION_bindBluetoothProfileService = 11;
    static final int TRANSACTION_disable = 8;
    static final int TRANSACTION_enable = 6;
    static final int TRANSACTION_enableNoAutoConnect = 7;
    static final int TRANSACTION_factoryReset = 18;
    static final int TRANSACTION_getAddress = 13;
    static final int TRANSACTION_getBluetoothGatt = 10;
    static final int TRANSACTION_getName = 14;
    static final int TRANSACTION_getState = 9;
    static final int TRANSACTION_isBleAppPresent = 17;
    static final int TRANSACTION_isBleScanAlwaysAvailable = 15;
    static final int TRANSACTION_isEnabled = 5;
    static final int TRANSACTION_registerAdapter = 1;
    static final int TRANSACTION_registerStateChangeCallback = 3;
    static final int TRANSACTION_unbindBluetoothProfileService = 12;
    static final int TRANSACTION_unregisterAdapter = 2;
    static final int TRANSACTION_unregisterStateChangeCallback = 4;
    static final int TRANSACTION_updateBleAppCount = 16;
    
    public Stub()
    {
      attachInterface(this, "android.bluetooth.IBluetoothManager");
    }
    
    public static IBluetoothManager asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.bluetooth.IBluetoothManager");
      if ((localIInterface != null) && ((localIInterface instanceof IBluetoothManager))) {
        return (IBluetoothManager)localIInterface;
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
        IBluetoothGatt localIBluetoothGatt = null;
        Object localObject = null;
        boolean bool1 = false;
        boolean bool2 = false;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 18: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothManager");
          paramInt1 = factoryReset();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 17: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothManager");
          paramInt1 = isBleAppPresent();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 16: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothManager");
          localObject = paramParcel1.readStrongBinder();
          if (paramParcel1.readInt() != 0) {
            bool2 = true;
          }
          paramInt1 = updateBleAppCount((IBinder)localObject, bool2, paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 15: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothManager");
          paramInt1 = isBleScanAlwaysAvailable();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 14: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothManager");
          paramParcel1 = getName();
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 13: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothManager");
          paramParcel1 = getAddress();
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 12: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothManager");
          unbindBluetoothProfileService(paramParcel1.readInt(), IBluetoothProfileServiceConnection.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 11: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothManager");
          paramInt1 = bindBluetoothProfileService(paramParcel1.readInt(), IBluetoothProfileServiceConnection.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 10: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothManager");
          localIBluetoothGatt = getBluetoothGatt();
          paramParcel2.writeNoException();
          paramParcel1 = (Parcel)localObject;
          if (localIBluetoothGatt != null) {
            paramParcel1 = localIBluetoothGatt.asBinder();
          }
          paramParcel2.writeStrongBinder(paramParcel1);
          return true;
        case 9: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothManager");
          paramInt1 = getState();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothManager");
          localObject = paramParcel1.readString();
          bool2 = bool1;
          if (paramParcel1.readInt() != 0) {
            bool2 = true;
          }
          paramInt1 = disable((String)localObject, bool2);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothManager");
          paramInt1 = enableNoAutoConnect(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothManager");
          paramInt1 = enable(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothManager");
          paramInt1 = isEnabled();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothManager");
          unregisterStateChangeCallback(IBluetoothStateChangeCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothManager");
          registerStateChangeCallback(IBluetoothStateChangeCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothManager");
          unregisterAdapter(IBluetoothManagerCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        }
        paramParcel1.enforceInterface("android.bluetooth.IBluetoothManager");
        localObject = registerAdapter(IBluetoothManagerCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
        paramParcel2.writeNoException();
        paramParcel1 = localIBluetoothGatt;
        if (localObject != null) {
          paramParcel1 = ((IBluetooth)localObject).asBinder();
        }
        paramParcel2.writeStrongBinder(paramParcel1);
        return true;
      }
      paramParcel2.writeString("android.bluetooth.IBluetoothManager");
      return true;
    }
    
    private static class Proxy
      implements IBluetoothManager
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
      
      public boolean bindBluetoothProfileService(int paramInt, IBluetoothProfileServiceConnection paramIBluetoothProfileServiceConnection)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothManager");
          localParcel1.writeInt(paramInt);
          if (paramIBluetoothProfileServiceConnection != null) {
            paramIBluetoothProfileServiceConnection = paramIBluetoothProfileServiceConnection.asBinder();
          } else {
            paramIBluetoothProfileServiceConnection = null;
          }
          localParcel1.writeStrongBinder(paramIBluetoothProfileServiceConnection);
          paramIBluetoothProfileServiceConnection = mRemote;
          boolean bool = false;
          paramIBluetoothProfileServiceConnection.transact(11, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
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
      
      public boolean disable(String paramString, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramBoolean);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(8, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramBoolean = localParcel2.readInt();
          if (paramBoolean) {
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
      
      public boolean enable(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothManager");
          localParcel1.writeString(paramString);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(6, localParcel1, localParcel2, 0);
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
      
      public boolean enableNoAutoConnect(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothManager");
          localParcel1.writeString(paramString);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(7, localParcel1, localParcel2, 0);
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
      
      public boolean factoryReset()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothManager");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(18, localParcel1, localParcel2, 0);
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
      
      public String getAddress()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothManager");
          mRemote.transact(13, localParcel1, localParcel2, 0);
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
      
      public IBluetoothGatt getBluetoothGatt()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothManager");
          mRemote.transact(10, localParcel1, localParcel2, 0);
          localParcel2.readException();
          IBluetoothGatt localIBluetoothGatt = IBluetoothGatt.Stub.asInterface(localParcel2.readStrongBinder());
          return localIBluetoothGatt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.bluetooth.IBluetoothManager";
      }
      
      public String getName()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothManager");
          mRemote.transact(14, localParcel1, localParcel2, 0);
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
      
      public int getState()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothManager");
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
      
      public boolean isBleAppPresent()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothManager");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(17, localParcel1, localParcel2, 0);
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
      
      public boolean isBleScanAlwaysAvailable()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothManager");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(15, localParcel1, localParcel2, 0);
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
      
      public boolean isEnabled()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothManager");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(5, localParcel1, localParcel2, 0);
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
      
      public IBluetooth registerAdapter(IBluetoothManagerCallback paramIBluetoothManagerCallback)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothManager");
          if (paramIBluetoothManagerCallback != null) {
            paramIBluetoothManagerCallback = paramIBluetoothManagerCallback.asBinder();
          } else {
            paramIBluetoothManagerCallback = null;
          }
          localParcel1.writeStrongBinder(paramIBluetoothManagerCallback);
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramIBluetoothManagerCallback = IBluetooth.Stub.asInterface(localParcel2.readStrongBinder());
          return paramIBluetoothManagerCallback;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void registerStateChangeCallback(IBluetoothStateChangeCallback paramIBluetoothStateChangeCallback)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothManager");
          if (paramIBluetoothStateChangeCallback != null) {
            paramIBluetoothStateChangeCallback = paramIBluetoothStateChangeCallback.asBinder();
          } else {
            paramIBluetoothStateChangeCallback = null;
          }
          localParcel1.writeStrongBinder(paramIBluetoothStateChangeCallback);
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
      
      public void unbindBluetoothProfileService(int paramInt, IBluetoothProfileServiceConnection paramIBluetoothProfileServiceConnection)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothManager");
          localParcel1.writeInt(paramInt);
          if (paramIBluetoothProfileServiceConnection != null) {
            paramIBluetoothProfileServiceConnection = paramIBluetoothProfileServiceConnection.asBinder();
          } else {
            paramIBluetoothProfileServiceConnection = null;
          }
          localParcel1.writeStrongBinder(paramIBluetoothProfileServiceConnection);
          mRemote.transact(12, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void unregisterAdapter(IBluetoothManagerCallback paramIBluetoothManagerCallback)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothManager");
          if (paramIBluetoothManagerCallback != null) {
            paramIBluetoothManagerCallback = paramIBluetoothManagerCallback.asBinder();
          } else {
            paramIBluetoothManagerCallback = null;
          }
          localParcel1.writeStrongBinder(paramIBluetoothManagerCallback);
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
      
      public void unregisterStateChangeCallback(IBluetoothStateChangeCallback paramIBluetoothStateChangeCallback)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothManager");
          if (paramIBluetoothStateChangeCallback != null) {
            paramIBluetoothStateChangeCallback = paramIBluetoothStateChangeCallback.asBinder();
          } else {
            paramIBluetoothStateChangeCallback = null;
          }
          localParcel1.writeStrongBinder(paramIBluetoothStateChangeCallback);
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
      
      public int updateBleAppCount(IBinder paramIBinder, boolean paramBoolean, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothManager");
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeInt(paramBoolean);
          localParcel1.writeString(paramString);
          mRemote.transact(16, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramBoolean = localParcel2.readInt();
          return paramBoolean;
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
