package android.app.backup;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IBackupObserver
  extends IInterface
{
  public abstract void backupFinished(int paramInt)
    throws RemoteException;
  
  public abstract void onResult(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract void onUpdate(String paramString, BackupProgress paramBackupProgress)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IBackupObserver
  {
    private static final String DESCRIPTOR = "android.app.backup.IBackupObserver";
    static final int TRANSACTION_backupFinished = 3;
    static final int TRANSACTION_onResult = 2;
    static final int TRANSACTION_onUpdate = 1;
    
    public Stub()
    {
      attachInterface(this, "android.app.backup.IBackupObserver");
    }
    
    public static IBackupObserver asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.app.backup.IBackupObserver");
      if ((localIInterface != null) && ((localIInterface instanceof IBackupObserver))) {
        return (IBackupObserver)localIInterface;
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
        case 3: 
          paramParcel1.enforceInterface("android.app.backup.IBackupObserver");
          backupFinished(paramParcel1.readInt());
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.app.backup.IBackupObserver");
          onResult(paramParcel1.readString(), paramParcel1.readInt());
          return true;
        }
        paramParcel1.enforceInterface("android.app.backup.IBackupObserver");
        paramParcel2 = paramParcel1.readString();
        if (paramParcel1.readInt() != 0) {
          paramParcel1 = (BackupProgress)BackupProgress.CREATOR.createFromParcel(paramParcel1);
        } else {
          paramParcel1 = null;
        }
        onUpdate(paramParcel2, paramParcel1);
        return true;
      }
      paramParcel2.writeString("android.app.backup.IBackupObserver");
      return true;
    }
    
    private static class Proxy
      implements IBackupObserver
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
      
      public void backupFinished(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.backup.IBackupObserver");
          localParcel.writeInt(paramInt);
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.app.backup.IBackupObserver";
      }
      
      public void onResult(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.backup.IBackupObserver");
          localParcel.writeString(paramString);
          localParcel.writeInt(paramInt);
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onUpdate(String paramString, BackupProgress paramBackupProgress)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.backup.IBackupObserver");
          localParcel.writeString(paramString);
          if (paramBackupProgress != null)
          {
            localParcel.writeInt(1);
            paramBackupProgress.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(1, localParcel, null, 1);
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
