package android.app.backup;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IFullBackupRestoreObserver
  extends IInterface
{
  public abstract void onBackupPackage(String paramString)
    throws RemoteException;
  
  public abstract void onEndBackup()
    throws RemoteException;
  
  public abstract void onEndRestore()
    throws RemoteException;
  
  public abstract void onRestorePackage(String paramString)
    throws RemoteException;
  
  public abstract void onStartBackup()
    throws RemoteException;
  
  public abstract void onStartRestore()
    throws RemoteException;
  
  public abstract void onTimeout()
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IFullBackupRestoreObserver
  {
    private static final String DESCRIPTOR = "android.app.backup.IFullBackupRestoreObserver";
    static final int TRANSACTION_onBackupPackage = 2;
    static final int TRANSACTION_onEndBackup = 3;
    static final int TRANSACTION_onEndRestore = 6;
    static final int TRANSACTION_onRestorePackage = 5;
    static final int TRANSACTION_onStartBackup = 1;
    static final int TRANSACTION_onStartRestore = 4;
    static final int TRANSACTION_onTimeout = 7;
    
    public Stub()
    {
      attachInterface(this, "android.app.backup.IFullBackupRestoreObserver");
    }
    
    public static IFullBackupRestoreObserver asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.app.backup.IFullBackupRestoreObserver");
      if ((localIInterface != null) && ((localIInterface instanceof IFullBackupRestoreObserver))) {
        return (IFullBackupRestoreObserver)localIInterface;
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
        case 7: 
          paramParcel1.enforceInterface("android.app.backup.IFullBackupRestoreObserver");
          onTimeout();
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.app.backup.IFullBackupRestoreObserver");
          onEndRestore();
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.app.backup.IFullBackupRestoreObserver");
          onRestorePackage(paramParcel1.readString());
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.app.backup.IFullBackupRestoreObserver");
          onStartRestore();
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.app.backup.IFullBackupRestoreObserver");
          onEndBackup();
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.app.backup.IFullBackupRestoreObserver");
          onBackupPackage(paramParcel1.readString());
          return true;
        }
        paramParcel1.enforceInterface("android.app.backup.IFullBackupRestoreObserver");
        onStartBackup();
        return true;
      }
      paramParcel2.writeString("android.app.backup.IFullBackupRestoreObserver");
      return true;
    }
    
    private static class Proxy
      implements IFullBackupRestoreObserver
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
        return "android.app.backup.IFullBackupRestoreObserver";
      }
      
      public void onBackupPackage(String paramString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.backup.IFullBackupRestoreObserver");
          localParcel.writeString(paramString);
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onEndBackup()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.backup.IFullBackupRestoreObserver");
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onEndRestore()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.backup.IFullBackupRestoreObserver");
          mRemote.transact(6, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onRestorePackage(String paramString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.backup.IFullBackupRestoreObserver");
          localParcel.writeString(paramString);
          mRemote.transact(5, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onStartBackup()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.backup.IFullBackupRestoreObserver");
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onStartRestore()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.backup.IFullBackupRestoreObserver");
          mRemote.transact(4, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onTimeout()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.backup.IFullBackupRestoreObserver");
          mRemote.transact(7, localParcel, null, 1);
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
