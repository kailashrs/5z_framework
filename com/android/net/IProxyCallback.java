package com.android.net;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IProxyCallback
  extends IInterface
{
  public abstract void getProxyPort(IBinder paramIBinder)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IProxyCallback
  {
    private static final String DESCRIPTOR = "com.android.net.IProxyCallback";
    static final int TRANSACTION_getProxyPort = 1;
    
    public Stub()
    {
      attachInterface(this, "com.android.net.IProxyCallback");
    }
    
    public static IProxyCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.net.IProxyCallback");
      if ((localIInterface != null) && ((localIInterface instanceof IProxyCallback))) {
        return (IProxyCallback)localIInterface;
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
      if (paramInt1 != 1)
      {
        if (paramInt1 != 1598968902) {
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        }
        paramParcel2.writeString("com.android.net.IProxyCallback");
        return true;
      }
      paramParcel1.enforceInterface("com.android.net.IProxyCallback");
      getProxyPort(paramParcel1.readStrongBinder());
      return true;
    }
    
    private static class Proxy
      implements IProxyCallback
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
        return "com.android.net.IProxyCallback";
      }
      
      public void getProxyPort(IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.net.IProxyCallback");
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
