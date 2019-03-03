package com.android.ims.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IImsStreamMediaSession
  extends IInterface
{
  public abstract void close()
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IImsStreamMediaSession
  {
    private static final String DESCRIPTOR = "com.android.ims.internal.IImsStreamMediaSession";
    static final int TRANSACTION_close = 1;
    
    public Stub()
    {
      attachInterface(this, "com.android.ims.internal.IImsStreamMediaSession");
    }
    
    public static IImsStreamMediaSession asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.ims.internal.IImsStreamMediaSession");
      if ((localIInterface != null) && ((localIInterface instanceof IImsStreamMediaSession))) {
        return (IImsStreamMediaSession)localIInterface;
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
        paramParcel2.writeString("com.android.ims.internal.IImsStreamMediaSession");
        return true;
      }
      paramParcel1.enforceInterface("com.android.ims.internal.IImsStreamMediaSession");
      close();
      paramParcel2.writeNoException();
      return true;
    }
    
    private static class Proxy
      implements IImsStreamMediaSession
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
      
      public void close()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.IImsStreamMediaSession");
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
      
      public String getInterfaceDescriptor()
      {
        return "com.android.ims.internal.IImsStreamMediaSession";
      }
    }
  }
}
