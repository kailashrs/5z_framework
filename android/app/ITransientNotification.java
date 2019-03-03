package android.app;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface ITransientNotification
  extends IInterface
{
  public abstract void hide()
    throws RemoteException;
  
  public abstract void show(IBinder paramIBinder)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ITransientNotification
  {
    private static final String DESCRIPTOR = "android.app.ITransientNotification";
    static final int TRANSACTION_hide = 2;
    static final int TRANSACTION_show = 1;
    
    public Stub()
    {
      attachInterface(this, "android.app.ITransientNotification");
    }
    
    public static ITransientNotification asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.app.ITransientNotification");
      if ((localIInterface != null) && ((localIInterface instanceof ITransientNotification))) {
        return (ITransientNotification)localIInterface;
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
        case 2: 
          paramParcel1.enforceInterface("android.app.ITransientNotification");
          hide();
          return true;
        }
        paramParcel1.enforceInterface("android.app.ITransientNotification");
        show(paramParcel1.readStrongBinder());
        return true;
      }
      paramParcel2.writeString("android.app.ITransientNotification");
      return true;
    }
    
    private static class Proxy
      implements ITransientNotification
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
        return "android.app.ITransientNotification";
      }
      
      public void hide()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.ITransientNotification");
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void show(IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.ITransientNotification");
          localParcel.writeStrongBinder(paramIBinder);
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
