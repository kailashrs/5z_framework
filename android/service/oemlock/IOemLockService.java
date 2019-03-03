package android.service.oemlock;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IOemLockService
  extends IInterface
{
  public abstract boolean isDeviceOemUnlocked()
    throws RemoteException;
  
  public abstract boolean isOemUnlockAllowed()
    throws RemoteException;
  
  public abstract boolean isOemUnlockAllowedByCarrier()
    throws RemoteException;
  
  public abstract boolean isOemUnlockAllowedByUser()
    throws RemoteException;
  
  public abstract void setOemUnlockAllowedByCarrier(boolean paramBoolean, byte[] paramArrayOfByte)
    throws RemoteException;
  
  public abstract void setOemUnlockAllowedByUser(boolean paramBoolean)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IOemLockService
  {
    private static final String DESCRIPTOR = "android.service.oemlock.IOemLockService";
    static final int TRANSACTION_isDeviceOemUnlocked = 6;
    static final int TRANSACTION_isOemUnlockAllowed = 5;
    static final int TRANSACTION_isOemUnlockAllowedByCarrier = 2;
    static final int TRANSACTION_isOemUnlockAllowedByUser = 4;
    static final int TRANSACTION_setOemUnlockAllowedByCarrier = 1;
    static final int TRANSACTION_setOemUnlockAllowedByUser = 3;
    
    public Stub()
    {
      attachInterface(this, "android.service.oemlock.IOemLockService");
    }
    
    public static IOemLockService asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.service.oemlock.IOemLockService");
      if ((localIInterface != null) && ((localIInterface instanceof IOemLockService))) {
        return (IOemLockService)localIInterface;
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
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 6: 
          paramParcel1.enforceInterface("android.service.oemlock.IOemLockService");
          paramInt1 = isDeviceOemUnlocked();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.service.oemlock.IOemLockService");
          paramInt1 = isOemUnlockAllowed();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.service.oemlock.IOemLockService");
          paramInt1 = isOemUnlockAllowedByUser();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.service.oemlock.IOemLockService");
          if (paramParcel1.readInt() != 0) {
            bool2 = true;
          }
          setOemUnlockAllowedByUser(bool2);
          paramParcel2.writeNoException();
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.service.oemlock.IOemLockService");
          paramInt1 = isOemUnlockAllowedByCarrier();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        }
        paramParcel1.enforceInterface("android.service.oemlock.IOemLockService");
        bool2 = bool1;
        if (paramParcel1.readInt() != 0) {
          bool2 = true;
        }
        setOemUnlockAllowedByCarrier(bool2, paramParcel1.createByteArray());
        paramParcel2.writeNoException();
        return true;
      }
      paramParcel2.writeString("android.service.oemlock.IOemLockService");
      return true;
    }
    
    private static class Proxy
      implements IOemLockService
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
        return "android.service.oemlock.IOemLockService";
      }
      
      public boolean isDeviceOemUnlocked()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.service.oemlock.IOemLockService");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(6, localParcel1, localParcel2, 0);
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
      
      public boolean isOemUnlockAllowed()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.service.oemlock.IOemLockService");
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
      
      public boolean isOemUnlockAllowedByCarrier()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.service.oemlock.IOemLockService");
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
      
      public boolean isOemUnlockAllowedByUser()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.service.oemlock.IOemLockService");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(4, localParcel1, localParcel2, 0);
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
      
      public void setOemUnlockAllowedByCarrier(boolean paramBoolean, byte[] paramArrayOfByte)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.service.oemlock.IOemLockService");
          localParcel1.writeInt(paramBoolean);
          localParcel1.writeByteArray(paramArrayOfByte);
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
      
      public void setOemUnlockAllowedByUser(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.service.oemlock.IOemLockService");
          localParcel1.writeInt(paramBoolean);
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
