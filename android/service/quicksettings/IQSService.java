package android.service.quicksettings;

import android.graphics.drawable.Icon;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IQSService
  extends IInterface
{
  public abstract Tile getTile(IBinder paramIBinder)
    throws RemoteException;
  
  public abstract boolean isLocked()
    throws RemoteException;
  
  public abstract boolean isSecure()
    throws RemoteException;
  
  public abstract void onDialogHidden(IBinder paramIBinder)
    throws RemoteException;
  
  public abstract void onShowDialog(IBinder paramIBinder)
    throws RemoteException;
  
  public abstract void onStartActivity(IBinder paramIBinder)
    throws RemoteException;
  
  public abstract void onStartSuccessful(IBinder paramIBinder)
    throws RemoteException;
  
  public abstract void startUnlockAndRun(IBinder paramIBinder)
    throws RemoteException;
  
  public abstract void updateQsTile(Tile paramTile, IBinder paramIBinder)
    throws RemoteException;
  
  public abstract void updateStatusIcon(IBinder paramIBinder, Icon paramIcon, String paramString)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IQSService
  {
    private static final String DESCRIPTOR = "android.service.quicksettings.IQSService";
    static final int TRANSACTION_getTile = 1;
    static final int TRANSACTION_isLocked = 6;
    static final int TRANSACTION_isSecure = 7;
    static final int TRANSACTION_onDialogHidden = 9;
    static final int TRANSACTION_onShowDialog = 4;
    static final int TRANSACTION_onStartActivity = 5;
    static final int TRANSACTION_onStartSuccessful = 10;
    static final int TRANSACTION_startUnlockAndRun = 8;
    static final int TRANSACTION_updateQsTile = 2;
    static final int TRANSACTION_updateStatusIcon = 3;
    
    public Stub()
    {
      attachInterface(this, "android.service.quicksettings.IQSService");
    }
    
    public static IQSService asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.service.quicksettings.IQSService");
      if ((localIInterface != null) && ((localIInterface instanceof IQSService))) {
        return (IQSService)localIInterface;
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
        IBinder localIBinder = null;
        Object localObject = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 10: 
          paramParcel1.enforceInterface("android.service.quicksettings.IQSService");
          onStartSuccessful(paramParcel1.readStrongBinder());
          paramParcel2.writeNoException();
          return true;
        case 9: 
          paramParcel1.enforceInterface("android.service.quicksettings.IQSService");
          onDialogHidden(paramParcel1.readStrongBinder());
          paramParcel2.writeNoException();
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.service.quicksettings.IQSService");
          startUnlockAndRun(paramParcel1.readStrongBinder());
          paramParcel2.writeNoException();
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.service.quicksettings.IQSService");
          paramInt1 = isSecure();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.service.quicksettings.IQSService");
          paramInt1 = isLocked();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.service.quicksettings.IQSService");
          onStartActivity(paramParcel1.readStrongBinder());
          paramParcel2.writeNoException();
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.service.quicksettings.IQSService");
          onShowDialog(paramParcel1.readStrongBinder());
          paramParcel2.writeNoException();
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.service.quicksettings.IQSService");
          localIBinder = paramParcel1.readStrongBinder();
          if (paramParcel1.readInt() != 0) {
            localObject = (Icon)Icon.CREATOR.createFromParcel(paramParcel1);
          }
          updateStatusIcon(localIBinder, (Icon)localObject, paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.service.quicksettings.IQSService");
          if (paramParcel1.readInt() != 0) {
            localObject = (Tile)Tile.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject = localIBinder;
          }
          updateQsTile((Tile)localObject, paramParcel1.readStrongBinder());
          paramParcel2.writeNoException();
          return true;
        }
        paramParcel1.enforceInterface("android.service.quicksettings.IQSService");
        paramParcel1 = getTile(paramParcel1.readStrongBinder());
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
      paramParcel2.writeString("android.service.quicksettings.IQSService");
      return true;
    }
    
    private static class Proxy
      implements IQSService
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
        return "android.service.quicksettings.IQSService";
      }
      
      public Tile getTile(IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.service.quicksettings.IQSService");
          localParcel1.writeStrongBinder(paramIBinder);
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramIBinder = (Tile)Tile.CREATOR.createFromParcel(localParcel2);
          } else {
            paramIBinder = null;
          }
          return paramIBinder;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isLocked()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.service.quicksettings.IQSService");
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
      
      public boolean isSecure()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.service.quicksettings.IQSService");
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
      
      public void onDialogHidden(IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.service.quicksettings.IQSService");
          localParcel1.writeStrongBinder(paramIBinder);
          mRemote.transact(9, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void onShowDialog(IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.service.quicksettings.IQSService");
          localParcel1.writeStrongBinder(paramIBinder);
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
      
      public void onStartActivity(IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.service.quicksettings.IQSService");
          localParcel1.writeStrongBinder(paramIBinder);
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
      
      public void onStartSuccessful(IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.service.quicksettings.IQSService");
          localParcel1.writeStrongBinder(paramIBinder);
          mRemote.transact(10, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void startUnlockAndRun(IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.service.quicksettings.IQSService");
          localParcel1.writeStrongBinder(paramIBinder);
          mRemote.transact(8, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void updateQsTile(Tile paramTile, IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.service.quicksettings.IQSService");
          if (paramTile != null)
          {
            localParcel1.writeInt(1);
            paramTile.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeStrongBinder(paramIBinder);
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
      
      public void updateStatusIcon(IBinder paramIBinder, Icon paramIcon, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.service.quicksettings.IQSService");
          localParcel1.writeStrongBinder(paramIBinder);
          if (paramIcon != null)
          {
            localParcel1.writeInt(1);
            paramIcon.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
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
