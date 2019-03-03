package android.service.persistentdata;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IPersistentDataBlockService
  extends IInterface
{
  public abstract int getDataBlockSize()
    throws RemoteException;
  
  public abstract int getFlashLockState()
    throws RemoteException;
  
  public abstract long getMaximumDataBlockSize()
    throws RemoteException;
  
  public abstract boolean getOemUnlockEnabled()
    throws RemoteException;
  
  public abstract boolean hasFrpCredentialHandle()
    throws RemoteException;
  
  public abstract byte[] read()
    throws RemoteException;
  
  public abstract void setOemUnlockEnabled(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void wipe()
    throws RemoteException;
  
  public abstract int write(byte[] paramArrayOfByte)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IPersistentDataBlockService
  {
    private static final String DESCRIPTOR = "android.service.persistentdata.IPersistentDataBlockService";
    static final int TRANSACTION_getDataBlockSize = 4;
    static final int TRANSACTION_getFlashLockState = 8;
    static final int TRANSACTION_getMaximumDataBlockSize = 5;
    static final int TRANSACTION_getOemUnlockEnabled = 7;
    static final int TRANSACTION_hasFrpCredentialHandle = 9;
    static final int TRANSACTION_read = 2;
    static final int TRANSACTION_setOemUnlockEnabled = 6;
    static final int TRANSACTION_wipe = 3;
    static final int TRANSACTION_write = 1;
    
    public Stub()
    {
      attachInterface(this, "android.service.persistentdata.IPersistentDataBlockService");
    }
    
    public static IPersistentDataBlockService asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.service.persistentdata.IPersistentDataBlockService");
      if ((localIInterface != null) && ((localIInterface instanceof IPersistentDataBlockService))) {
        return (IPersistentDataBlockService)localIInterface;
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
        case 9: 
          paramParcel1.enforceInterface("android.service.persistentdata.IPersistentDataBlockService");
          paramInt1 = hasFrpCredentialHandle();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.service.persistentdata.IPersistentDataBlockService");
          paramInt1 = getFlashLockState();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.service.persistentdata.IPersistentDataBlockService");
          paramInt1 = getOemUnlockEnabled();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.service.persistentdata.IPersistentDataBlockService");
          boolean bool;
          if (paramParcel1.readInt() != 0) {
            bool = true;
          } else {
            bool = false;
          }
          setOemUnlockEnabled(bool);
          paramParcel2.writeNoException();
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.service.persistentdata.IPersistentDataBlockService");
          long l = getMaximumDataBlockSize();
          paramParcel2.writeNoException();
          paramParcel2.writeLong(l);
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.service.persistentdata.IPersistentDataBlockService");
          paramInt1 = getDataBlockSize();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.service.persistentdata.IPersistentDataBlockService");
          wipe();
          paramParcel2.writeNoException();
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.service.persistentdata.IPersistentDataBlockService");
          paramParcel1 = read();
          paramParcel2.writeNoException();
          paramParcel2.writeByteArray(paramParcel1);
          return true;
        }
        paramParcel1.enforceInterface("android.service.persistentdata.IPersistentDataBlockService");
        paramInt1 = write(paramParcel1.createByteArray());
        paramParcel2.writeNoException();
        paramParcel2.writeInt(paramInt1);
        return true;
      }
      paramParcel2.writeString("android.service.persistentdata.IPersistentDataBlockService");
      return true;
    }
    
    private static class Proxy
      implements IPersistentDataBlockService
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
      
      public int getDataBlockSize()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.service.persistentdata.IPersistentDataBlockService");
          mRemote.transact(4, localParcel1, localParcel2, 0);
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
      
      public int getFlashLockState()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.service.persistentdata.IPersistentDataBlockService");
          mRemote.transact(8, localParcel1, localParcel2, 0);
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
      
      public String getInterfaceDescriptor()
      {
        return "android.service.persistentdata.IPersistentDataBlockService";
      }
      
      public long getMaximumDataBlockSize()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.service.persistentdata.IPersistentDataBlockService");
          mRemote.transact(5, localParcel1, localParcel2, 0);
          localParcel2.readException();
          long l = localParcel2.readLong();
          return l;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean getOemUnlockEnabled()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.service.persistentdata.IPersistentDataBlockService");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(7, localParcel1, localParcel2, 0);
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
      
      public boolean hasFrpCredentialHandle()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.service.persistentdata.IPersistentDataBlockService");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(9, localParcel1, localParcel2, 0);
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
      
      public byte[] read()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.service.persistentdata.IPersistentDataBlockService");
          mRemote.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          byte[] arrayOfByte = localParcel2.createByteArray();
          return arrayOfByte;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setOemUnlockEnabled(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.service.persistentdata.IPersistentDataBlockService");
          localParcel1.writeInt(paramBoolean);
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
      
      public void wipe()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.service.persistentdata.IPersistentDataBlockService");
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
      
      public int write(byte[] paramArrayOfByte)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.service.persistentdata.IPersistentDataBlockService");
          localParcel1.writeByteArray(paramArrayOfByte);
          mRemote.transact(1, localParcel1, localParcel2, 0);
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
    }
  }
}
