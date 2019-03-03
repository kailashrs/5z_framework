package com.android.ims.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IImsMultiEndpoint
  extends IInterface
{
  public abstract void requestImsExternalCallStateInfo()
    throws RemoteException;
  
  public abstract void setListener(IImsExternalCallStateListener paramIImsExternalCallStateListener)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IImsMultiEndpoint
  {
    private static final String DESCRIPTOR = "com.android.ims.internal.IImsMultiEndpoint";
    static final int TRANSACTION_requestImsExternalCallStateInfo = 2;
    static final int TRANSACTION_setListener = 1;
    
    public Stub()
    {
      attachInterface(this, "com.android.ims.internal.IImsMultiEndpoint");
    }
    
    public static IImsMultiEndpoint asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.ims.internal.IImsMultiEndpoint");
      if ((localIInterface != null) && ((localIInterface instanceof IImsMultiEndpoint))) {
        return (IImsMultiEndpoint)localIInterface;
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
          paramParcel1.enforceInterface("com.android.ims.internal.IImsMultiEndpoint");
          requestImsExternalCallStateInfo();
          paramParcel2.writeNoException();
          return true;
        }
        paramParcel1.enforceInterface("com.android.ims.internal.IImsMultiEndpoint");
        setListener(IImsExternalCallStateListener.Stub.asInterface(paramParcel1.readStrongBinder()));
        paramParcel2.writeNoException();
        return true;
      }
      paramParcel2.writeString("com.android.ims.internal.IImsMultiEndpoint");
      return true;
    }
    
    private static class Proxy
      implements IImsMultiEndpoint
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
        return "com.android.ims.internal.IImsMultiEndpoint";
      }
      
      public void requestImsExternalCallStateInfo()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.IImsMultiEndpoint");
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
      
      public void setListener(IImsExternalCallStateListener paramIImsExternalCallStateListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.IImsMultiEndpoint");
          if (paramIImsExternalCallStateListener != null) {
            paramIImsExternalCallStateListener = paramIImsExternalCallStateListener.asBinder();
          } else {
            paramIImsExternalCallStateListener = null;
          }
          localParcel1.writeStrongBinder(paramIImsExternalCallStateListener);
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
