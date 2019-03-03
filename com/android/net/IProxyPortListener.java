package com.android.net;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IProxyPortListener
  extends IInterface
{
  public abstract void setProxyPort(int paramInt)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IProxyPortListener
  {
    private static final String DESCRIPTOR = "com.android.net.IProxyPortListener";
    static final int TRANSACTION_setProxyPort = 1;
    
    public Stub()
    {
      attachInterface(this, "com.android.net.IProxyPortListener");
    }
    
    public static IProxyPortListener asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.net.IProxyPortListener");
      if ((localIInterface != null) && ((localIInterface instanceof IProxyPortListener))) {
        return (IProxyPortListener)localIInterface;
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
        paramParcel2.writeString("com.android.net.IProxyPortListener");
        return true;
      }
      paramParcel1.enforceInterface("com.android.net.IProxyPortListener");
      setProxyPort(paramParcel1.readInt());
      return true;
    }
    
    private static class Proxy
      implements IProxyPortListener
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
        return "com.android.net.IProxyPortListener";
      }
      
      public void setProxyPort(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.net.IProxyPortListener");
          localParcel.writeInt(paramInt);
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
