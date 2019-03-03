package com.android.internal.inputmethod;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IInputContentUriToken
  extends IInterface
{
  public abstract void release()
    throws RemoteException;
  
  public abstract void take()
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IInputContentUriToken
  {
    private static final String DESCRIPTOR = "com.android.internal.inputmethod.IInputContentUriToken";
    static final int TRANSACTION_release = 2;
    static final int TRANSACTION_take = 1;
    
    public Stub()
    {
      attachInterface(this, "com.android.internal.inputmethod.IInputContentUriToken");
    }
    
    public static IInputContentUriToken asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.internal.inputmethod.IInputContentUriToken");
      if ((localIInterface != null) && ((localIInterface instanceof IInputContentUriToken))) {
        return (IInputContentUriToken)localIInterface;
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
          paramParcel1.enforceInterface("com.android.internal.inputmethod.IInputContentUriToken");
          release();
          paramParcel2.writeNoException();
          return true;
        }
        paramParcel1.enforceInterface("com.android.internal.inputmethod.IInputContentUriToken");
        take();
        paramParcel2.writeNoException();
        return true;
      }
      paramParcel2.writeString("com.android.internal.inputmethod.IInputContentUriToken");
      return true;
    }
    
    private static class Proxy
      implements IInputContentUriToken
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
        return "com.android.internal.inputmethod.IInputContentUriToken";
      }
      
      public void release()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.inputmethod.IInputContentUriToken");
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
      
      public void take()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.inputmethod.IInputContentUriToken");
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
