package com.android.ims.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IImsEcbm
  extends IInterface
{
  public abstract void exitEmergencyCallbackMode()
    throws RemoteException;
  
  public abstract void setListener(IImsEcbmListener paramIImsEcbmListener)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IImsEcbm
  {
    private static final String DESCRIPTOR = "com.android.ims.internal.IImsEcbm";
    static final int TRANSACTION_exitEmergencyCallbackMode = 2;
    static final int TRANSACTION_setListener = 1;
    
    public Stub()
    {
      attachInterface(this, "com.android.ims.internal.IImsEcbm");
    }
    
    public static IImsEcbm asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.ims.internal.IImsEcbm");
      if ((localIInterface != null) && ((localIInterface instanceof IImsEcbm))) {
        return (IImsEcbm)localIInterface;
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
          paramParcel1.enforceInterface("com.android.ims.internal.IImsEcbm");
          exitEmergencyCallbackMode();
          paramParcel2.writeNoException();
          return true;
        }
        paramParcel1.enforceInterface("com.android.ims.internal.IImsEcbm");
        setListener(IImsEcbmListener.Stub.asInterface(paramParcel1.readStrongBinder()));
        paramParcel2.writeNoException();
        return true;
      }
      paramParcel2.writeString("com.android.ims.internal.IImsEcbm");
      return true;
    }
    
    private static class Proxy
      implements IImsEcbm
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
      
      public void exitEmergencyCallbackMode()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.IImsEcbm");
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
      
      public String getInterfaceDescriptor()
      {
        return "com.android.ims.internal.IImsEcbm";
      }
      
      public void setListener(IImsEcbmListener paramIImsEcbmListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.IImsEcbm");
          if (paramIImsEcbmListener != null) {
            paramIImsEcbmListener = paramIImsEcbmListener.asBinder();
          } else {
            paramIImsEcbmListener = null;
          }
          localParcel1.writeStrongBinder(paramIImsEcbmListener);
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
