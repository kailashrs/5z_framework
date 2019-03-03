package android.media;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IVolumeController
  extends IInterface
{
  public abstract void dismiss()
    throws RemoteException;
  
  public abstract void displaySafeVolumeWarning(int paramInt)
    throws RemoteException;
  
  public abstract void masterMuteChanged(int paramInt)
    throws RemoteException;
  
  public abstract void setA11yMode(int paramInt)
    throws RemoteException;
  
  public abstract void setLayoutDirection(int paramInt)
    throws RemoteException;
  
  public abstract void volumeChanged(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IVolumeController
  {
    private static final String DESCRIPTOR = "android.media.IVolumeController";
    static final int TRANSACTION_dismiss = 5;
    static final int TRANSACTION_displaySafeVolumeWarning = 1;
    static final int TRANSACTION_masterMuteChanged = 3;
    static final int TRANSACTION_setA11yMode = 6;
    static final int TRANSACTION_setLayoutDirection = 4;
    static final int TRANSACTION_volumeChanged = 2;
    
    public Stub()
    {
      attachInterface(this, "android.media.IVolumeController");
    }
    
    public static IVolumeController asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.media.IVolumeController");
      if ((localIInterface != null) && ((localIInterface instanceof IVolumeController))) {
        return (IVolumeController)localIInterface;
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
        case 6: 
          paramParcel1.enforceInterface("android.media.IVolumeController");
          setA11yMode(paramParcel1.readInt());
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.media.IVolumeController");
          dismiss();
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.media.IVolumeController");
          setLayoutDirection(paramParcel1.readInt());
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.media.IVolumeController");
          masterMuteChanged(paramParcel1.readInt());
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.media.IVolumeController");
          volumeChanged(paramParcel1.readInt(), paramParcel1.readInt());
          return true;
        }
        paramParcel1.enforceInterface("android.media.IVolumeController");
        displaySafeVolumeWarning(paramParcel1.readInt());
        return true;
      }
      paramParcel2.writeString("android.media.IVolumeController");
      return true;
    }
    
    private static class Proxy
      implements IVolumeController
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
      
      public void dismiss()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.IVolumeController");
          mRemote.transact(5, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void displaySafeVolumeWarning(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.IVolumeController");
          localParcel.writeInt(paramInt);
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.media.IVolumeController";
      }
      
      public void masterMuteChanged(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.IVolumeController");
          localParcel.writeInt(paramInt);
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void setA11yMode(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.IVolumeController");
          localParcel.writeInt(paramInt);
          mRemote.transact(6, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void setLayoutDirection(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.IVolumeController");
          localParcel.writeInt(paramInt);
          mRemote.transact(4, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void volumeChanged(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.IVolumeController");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
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
