package android.app.backup;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IRestoreObserver
  extends IInterface
{
  public abstract void onUpdate(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract void restoreFinished(int paramInt)
    throws RemoteException;
  
  public abstract void restoreSetsAvailable(RestoreSet[] paramArrayOfRestoreSet)
    throws RemoteException;
  
  public abstract void restoreStarting(int paramInt)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IRestoreObserver
  {
    private static final String DESCRIPTOR = "android.app.backup.IRestoreObserver";
    static final int TRANSACTION_onUpdate = 3;
    static final int TRANSACTION_restoreFinished = 4;
    static final int TRANSACTION_restoreSetsAvailable = 1;
    static final int TRANSACTION_restoreStarting = 2;
    
    public Stub()
    {
      attachInterface(this, "android.app.backup.IRestoreObserver");
    }
    
    public static IRestoreObserver asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.app.backup.IRestoreObserver");
      if ((localIInterface != null) && ((localIInterface instanceof IRestoreObserver))) {
        return (IRestoreObserver)localIInterface;
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
        case 4: 
          paramParcel1.enforceInterface("android.app.backup.IRestoreObserver");
          restoreFinished(paramParcel1.readInt());
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.app.backup.IRestoreObserver");
          onUpdate(paramParcel1.readInt(), paramParcel1.readString());
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.app.backup.IRestoreObserver");
          restoreStarting(paramParcel1.readInt());
          return true;
        }
        paramParcel1.enforceInterface("android.app.backup.IRestoreObserver");
        restoreSetsAvailable((RestoreSet[])paramParcel1.createTypedArray(RestoreSet.CREATOR));
        return true;
      }
      paramParcel2.writeString("android.app.backup.IRestoreObserver");
      return true;
    }
    
    private static class Proxy
      implements IRestoreObserver
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
        return "android.app.backup.IRestoreObserver";
      }
      
      public void onUpdate(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.backup.IRestoreObserver");
          localParcel.writeInt(paramInt);
          localParcel.writeString(paramString);
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void restoreFinished(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.backup.IRestoreObserver");
          localParcel.writeInt(paramInt);
          mRemote.transact(4, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void restoreSetsAvailable(RestoreSet[] paramArrayOfRestoreSet)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.backup.IRestoreObserver");
          localParcel.writeTypedArray(paramArrayOfRestoreSet, 0);
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void restoreStarting(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.backup.IRestoreObserver");
          localParcel.writeInt(paramInt);
          mRemote.transact(2, localParcel, null, 1);
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
