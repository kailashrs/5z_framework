package android.media;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IMediaRouterClient
  extends IInterface
{
  public abstract void onRestoreRoute()
    throws RemoteException;
  
  public abstract void onStateChanged()
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IMediaRouterClient
  {
    private static final String DESCRIPTOR = "android.media.IMediaRouterClient";
    static final int TRANSACTION_onRestoreRoute = 2;
    static final int TRANSACTION_onStateChanged = 1;
    
    public Stub()
    {
      attachInterface(this, "android.media.IMediaRouterClient");
    }
    
    public static IMediaRouterClient asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.media.IMediaRouterClient");
      if ((localIInterface != null) && ((localIInterface instanceof IMediaRouterClient))) {
        return (IMediaRouterClient)localIInterface;
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
          paramParcel1.enforceInterface("android.media.IMediaRouterClient");
          onRestoreRoute();
          return true;
        }
        paramParcel1.enforceInterface("android.media.IMediaRouterClient");
        onStateChanged();
        return true;
      }
      paramParcel2.writeString("android.media.IMediaRouterClient");
      return true;
    }
    
    private static class Proxy
      implements IMediaRouterClient
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
        return "android.media.IMediaRouterClient";
      }
      
      public void onRestoreRoute()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.IMediaRouterClient");
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onStateChanged()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.IMediaRouterClient");
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
