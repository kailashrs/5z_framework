package android.service.dreams;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.IRemoteCallback;
import android.os.IRemoteCallback.Stub;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IDreamService
  extends IInterface
{
  public abstract void attach(IBinder paramIBinder, boolean paramBoolean, IRemoteCallback paramIRemoteCallback)
    throws RemoteException;
  
  public abstract void detach()
    throws RemoteException;
  
  public abstract void wakeUp()
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IDreamService
  {
    private static final String DESCRIPTOR = "android.service.dreams.IDreamService";
    static final int TRANSACTION_attach = 1;
    static final int TRANSACTION_detach = 2;
    static final int TRANSACTION_wakeUp = 3;
    
    public Stub()
    {
      attachInterface(this, "android.service.dreams.IDreamService");
    }
    
    public static IDreamService asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.service.dreams.IDreamService");
      if ((localIInterface != null) && ((localIInterface instanceof IDreamService))) {
        return (IDreamService)localIInterface;
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
          paramParcel1.enforceInterface("android.service.dreams.IDreamService");
          wakeUp();
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.service.dreams.IDreamService");
          detach();
          return true;
        }
        paramParcel1.enforceInterface("android.service.dreams.IDreamService");
        paramParcel2 = paramParcel1.readStrongBinder();
        boolean bool;
        if (paramParcel1.readInt() != 0) {
          bool = true;
        } else {
          bool = false;
        }
        attach(paramParcel2, bool, IRemoteCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
        return true;
      }
      paramParcel2.writeString("android.service.dreams.IDreamService");
      return true;
    }
    
    private static class Proxy
      implements IDreamService
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
      
      public void attach(IBinder paramIBinder, boolean paramBoolean, IRemoteCallback paramIRemoteCallback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.dreams.IDreamService");
          localParcel.writeStrongBinder(paramIBinder);
          localParcel.writeInt(paramBoolean);
          if (paramIRemoteCallback != null) {
            paramIBinder = paramIRemoteCallback.asBinder();
          } else {
            paramIBinder = null;
          }
          localParcel.writeStrongBinder(paramIBinder);
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void detach()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.dreams.IDreamService");
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.service.dreams.IDreamService";
      }
      
      public void wakeUp()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.dreams.IDreamService");
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
