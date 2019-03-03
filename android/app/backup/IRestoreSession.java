package android.app.backup;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IRestoreSession
  extends IInterface
{
  public abstract void endRestoreSession()
    throws RemoteException;
  
  public abstract int getAvailableRestoreSets(IRestoreObserver paramIRestoreObserver, IBackupManagerMonitor paramIBackupManagerMonitor)
    throws RemoteException;
  
  public abstract int restoreAll(long paramLong, IRestoreObserver paramIRestoreObserver, IBackupManagerMonitor paramIBackupManagerMonitor)
    throws RemoteException;
  
  public abstract int restorePackage(String paramString, IRestoreObserver paramIRestoreObserver, IBackupManagerMonitor paramIBackupManagerMonitor)
    throws RemoteException;
  
  public abstract int restoreSome(long paramLong, IRestoreObserver paramIRestoreObserver, IBackupManagerMonitor paramIBackupManagerMonitor, String[] paramArrayOfString)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IRestoreSession
  {
    private static final String DESCRIPTOR = "android.app.backup.IRestoreSession";
    static final int TRANSACTION_endRestoreSession = 5;
    static final int TRANSACTION_getAvailableRestoreSets = 1;
    static final int TRANSACTION_restoreAll = 2;
    static final int TRANSACTION_restorePackage = 4;
    static final int TRANSACTION_restoreSome = 3;
    
    public Stub()
    {
      attachInterface(this, "android.app.backup.IRestoreSession");
    }
    
    public static IRestoreSession asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.app.backup.IRestoreSession");
      if ((localIInterface != null) && ((localIInterface instanceof IRestoreSession))) {
        return (IRestoreSession)localIInterface;
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
        case 5: 
          paramParcel1.enforceInterface("android.app.backup.IRestoreSession");
          endRestoreSession();
          paramParcel2.writeNoException();
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.app.backup.IRestoreSession");
          paramInt1 = restorePackage(paramParcel1.readString(), IRestoreObserver.Stub.asInterface(paramParcel1.readStrongBinder()), IBackupManagerMonitor.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.app.backup.IRestoreSession");
          paramInt1 = restoreSome(paramParcel1.readLong(), IRestoreObserver.Stub.asInterface(paramParcel1.readStrongBinder()), IBackupManagerMonitor.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.createStringArray());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.app.backup.IRestoreSession");
          paramInt1 = restoreAll(paramParcel1.readLong(), IRestoreObserver.Stub.asInterface(paramParcel1.readStrongBinder()), IBackupManagerMonitor.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        }
        paramParcel1.enforceInterface("android.app.backup.IRestoreSession");
        paramInt1 = getAvailableRestoreSets(IRestoreObserver.Stub.asInterface(paramParcel1.readStrongBinder()), IBackupManagerMonitor.Stub.asInterface(paramParcel1.readStrongBinder()));
        paramParcel2.writeNoException();
        paramParcel2.writeInt(paramInt1);
        return true;
      }
      paramParcel2.writeString("android.app.backup.IRestoreSession");
      return true;
    }
    
    private static class Proxy
      implements IRestoreSession
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
      
      public void endRestoreSession()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.backup.IRestoreSession");
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
      
      public int getAvailableRestoreSets(IRestoreObserver paramIRestoreObserver, IBackupManagerMonitor paramIBackupManagerMonitor)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.backup.IRestoreSession");
          Object localObject = null;
          if (paramIRestoreObserver != null) {
            paramIRestoreObserver = paramIRestoreObserver.asBinder();
          } else {
            paramIRestoreObserver = null;
          }
          localParcel1.writeStrongBinder(paramIRestoreObserver);
          paramIRestoreObserver = localObject;
          if (paramIBackupManagerMonitor != null) {
            paramIRestoreObserver = paramIBackupManagerMonitor.asBinder();
          }
          localParcel1.writeStrongBinder(paramIRestoreObserver);
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
      
      public String getInterfaceDescriptor()
      {
        return "android.app.backup.IRestoreSession";
      }
      
      public int restoreAll(long paramLong, IRestoreObserver paramIRestoreObserver, IBackupManagerMonitor paramIBackupManagerMonitor)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.backup.IRestoreSession");
          localParcel1.writeLong(paramLong);
          Object localObject = null;
          if (paramIRestoreObserver != null) {
            paramIRestoreObserver = paramIRestoreObserver.asBinder();
          } else {
            paramIRestoreObserver = null;
          }
          localParcel1.writeStrongBinder(paramIRestoreObserver);
          paramIRestoreObserver = localObject;
          if (paramIBackupManagerMonitor != null) {
            paramIRestoreObserver = paramIBackupManagerMonitor.asBinder();
          }
          localParcel1.writeStrongBinder(paramIRestoreObserver);
          mRemote.transact(2, localParcel1, localParcel2, 0);
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
      
      public int restorePackage(String paramString, IRestoreObserver paramIRestoreObserver, IBackupManagerMonitor paramIBackupManagerMonitor)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.backup.IRestoreSession");
          localParcel1.writeString(paramString);
          Object localObject = null;
          if (paramIRestoreObserver != null) {
            paramString = paramIRestoreObserver.asBinder();
          } else {
            paramString = null;
          }
          localParcel1.writeStrongBinder(paramString);
          paramString = localObject;
          if (paramIBackupManagerMonitor != null) {
            paramString = paramIBackupManagerMonitor.asBinder();
          }
          localParcel1.writeStrongBinder(paramString);
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
      
      public int restoreSome(long paramLong, IRestoreObserver paramIRestoreObserver, IBackupManagerMonitor paramIBackupManagerMonitor, String[] paramArrayOfString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.backup.IRestoreSession");
          localParcel1.writeLong(paramLong);
          Object localObject = null;
          if (paramIRestoreObserver != null) {
            paramIRestoreObserver = paramIRestoreObserver.asBinder();
          } else {
            paramIRestoreObserver = null;
          }
          localParcel1.writeStrongBinder(paramIRestoreObserver);
          paramIRestoreObserver = localObject;
          if (paramIBackupManagerMonitor != null) {
            paramIRestoreObserver = paramIBackupManagerMonitor.asBinder();
          }
          localParcel1.writeStrongBinder(paramIRestoreObserver);
          localParcel1.writeStringArray(paramArrayOfString);
          mRemote.transact(3, localParcel1, localParcel2, 0);
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
