package android.app;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface ISearchManagerCallback
  extends IInterface
{
  public abstract void onCancel()
    throws RemoteException;
  
  public abstract void onDismiss()
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ISearchManagerCallback
  {
    private static final String DESCRIPTOR = "android.app.ISearchManagerCallback";
    static final int TRANSACTION_onCancel = 2;
    static final int TRANSACTION_onDismiss = 1;
    
    public Stub()
    {
      attachInterface(this, "android.app.ISearchManagerCallback");
    }
    
    public static ISearchManagerCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.app.ISearchManagerCallback");
      if ((localIInterface != null) && ((localIInterface instanceof ISearchManagerCallback))) {
        return (ISearchManagerCallback)localIInterface;
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
          paramParcel1.enforceInterface("android.app.ISearchManagerCallback");
          onCancel();
          return true;
        }
        paramParcel1.enforceInterface("android.app.ISearchManagerCallback");
        onDismiss();
        return true;
      }
      paramParcel2.writeString("android.app.ISearchManagerCallback");
      return true;
    }
    
    private static class Proxy
      implements ISearchManagerCallback
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
        return "android.app.ISearchManagerCallback";
      }
      
      public void onCancel()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.ISearchManagerCallback");
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onDismiss()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.ISearchManagerCallback");
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
