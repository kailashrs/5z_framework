package com.android.ims.internal.uce.uceservice;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IUceListener
  extends IInterface
{
  public abstract void setStatus(int paramInt)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IUceListener
  {
    private static final String DESCRIPTOR = "com.android.ims.internal.uce.uceservice.IUceListener";
    static final int TRANSACTION_setStatus = 1;
    
    public Stub()
    {
      attachInterface(this, "com.android.ims.internal.uce.uceservice.IUceListener");
    }
    
    public static IUceListener asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.ims.internal.uce.uceservice.IUceListener");
      if ((localIInterface != null) && ((localIInterface instanceof IUceListener))) {
        return (IUceListener)localIInterface;
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
        paramParcel2.writeString("com.android.ims.internal.uce.uceservice.IUceListener");
        return true;
      }
      paramParcel1.enforceInterface("com.android.ims.internal.uce.uceservice.IUceListener");
      setStatus(paramParcel1.readInt());
      paramParcel2.writeNoException();
      return true;
    }
    
    private static class Proxy
      implements IUceListener
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
        return "com.android.ims.internal.uce.uceservice.IUceListener";
      }
      
      public void setStatus(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.uce.uceservice.IUceListener");
          localParcel1.writeInt(paramInt);
          mRemote.transact(1, localParcel1, localParcel2, 0);
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
