package com.android.ims.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IImsEcbmListener
  extends IInterface
{
  public abstract void enteredECBM()
    throws RemoteException;
  
  public abstract void exitedECBM()
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IImsEcbmListener
  {
    private static final String DESCRIPTOR = "com.android.ims.internal.IImsEcbmListener";
    static final int TRANSACTION_enteredECBM = 1;
    static final int TRANSACTION_exitedECBM = 2;
    
    public Stub()
    {
      attachInterface(this, "com.android.ims.internal.IImsEcbmListener");
    }
    
    public static IImsEcbmListener asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.ims.internal.IImsEcbmListener");
      if ((localIInterface != null) && ((localIInterface instanceof IImsEcbmListener))) {
        return (IImsEcbmListener)localIInterface;
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
          paramParcel1.enforceInterface("com.android.ims.internal.IImsEcbmListener");
          exitedECBM();
          return true;
        }
        paramParcel1.enforceInterface("com.android.ims.internal.IImsEcbmListener");
        enteredECBM();
        return true;
      }
      paramParcel2.writeString("com.android.ims.internal.IImsEcbmListener");
      return true;
    }
    
    private static class Proxy
      implements IImsEcbmListener
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
      
      public void enteredECBM()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.ims.internal.IImsEcbmListener");
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void exitedECBM()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.ims.internal.IImsEcbmListener");
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
        return "com.android.ims.internal.IImsEcbmListener";
      }
    }
  }
}
